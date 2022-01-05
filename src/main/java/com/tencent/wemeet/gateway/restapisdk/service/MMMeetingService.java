package com.tencent.wemeet.gateway.restapisdk.service;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.GuavaRetryHandler;
import com.tencent.wemeet.gateway.restapisdk.config.redis.RedisUtil;
import com.tencent.wemeet.gateway.restapisdk.models.MeetingInfoVo;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.CancelMeetingReqVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.CreateMeetingReqVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.rooms.BookRoomsVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.EditMeetingInfoResVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.MeetingsApi;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.RoomsApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.tencent.wemeet.gateway.restapisdk.constants.Constant.MEETING_RESPONSE;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName UserService.java
 * @description:  会议管理用户 业务处理层
 * @createTime 2021年11月25日 17:52:00
 */
@Service
@Slf4j
public class MMMeetingService {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private GuavaRetryHandler guavaRetryHandler;

    @Autowired
    private MeetingsApi meetingsApi;

    @Autowired
    private RoomsApi roomsApi;

    @Autowired
    private RedisUtil redis;

    /**
     * 创建
     * @param createMeetingReqVo
     * @return
     */
    public boolean createTenCentMeeting(CreateMeetingReqVo createMeetingReqVo){
        boolean meetingAndBookRooms = createMeetingAndBookRooms(createMeetingReqVo);
        System.out.println(createMeetingReqVo);
        if(!meetingAndBookRooms){
            taskExecutor.submit(() ->{
                guavaRetryHandler.handler(createMeetingReqVo , new MMMeetingService() , "saveMeeting");
            });
        }
        return true;
    }

    /**
     * 创建腾讯会议同时预约会议室
     * @param createMeetingReqVo
     * @return
     */
    public boolean createMeetingAndBookRooms(CreateMeetingReqVo createMeetingReqVo){
        String meetingId = null;
        String bookRoomsMeetingId = null;
        try {
            //第一次预定会议 meetingId为空  如果是会议创建成功，且满足预定会议室，但预约会议室失败，无需重新预定会议
            if (StringUtils.isBlank(createMeetingReqVo.getMeetingId())) {
                //1、调用腾讯会议API接口创建会议
                HttpResponse httpResponse = meetingsApi.scheduleMeeting(createMeetingReqVo);
                if (!httpResponse.getResponseCode().equals(200)) {
                    log.error("创建腾讯会议失败 --- editMeetingRegVo：{}", JSON.toJSONString(createMeetingReqVo));
                    return false;
                }
                //2、创建腾讯会议成功后 预定腾讯会议会议室
                EditMeetingInfoResVo editMeetingInfoResVo = JSON.parseObject(httpResponse.getResponseBody(), EditMeetingInfoResVo.class);
                MeetingInfoVo meetingInfoVo = editMeetingInfoResVo.getMeetingInfoVoList().get(0);
                meetingId = meetingInfoVo.getMeetingId();

                //3、将数据放入缓存，为后续预定会议室失败重试工作做准备
                createMeetingReqVo.setMeetingId(meetingId);
                redis.setIfAbsentExpiration(String.format("%s%s", MEETING_RESPONSE, meetingId),
                        JSON.toJSONString(meetingInfoVo), 60 * 60 * 24, TimeUnit.SECONDS);
            }

            //4、会议室预定对会议时长有硬性要求，会议时长不得小于15分钟且不得大于24小时；且不支持周期性会议。
            boolean whetherTheConditionsBookRooms = whetherTheConditionsBookRooms(createMeetingReqVo);

            bookRoomsMeetingId = meetingId != null ? meetingId : createMeetingReqVo.getMeetingId();
            if (whetherTheConditionsBookRooms) {
                //5、构建预定会议室参数
                BookRoomsVo bookRoomsVo = BookRoomsVo.builder().meetingId(bookRoomsMeetingId).meetingRoomIdList(createMeetingReqVo
                        .getMeetingRoomsIdList()).build();
                //6、调用腾讯会议API预定会议室
                HttpResponse response = roomsApi.bookRooms(bookRoomsVo);
                if (!response.getResponseCode().equals(200)) {
                    log.error("预定会议室失败 --- bookRoomsVo：{}", JSON.toJSONString(bookRoomsVo));
                    return false;
                }
            }
        }catch (Exception e){
            log.error("取消会议、释放会议室失败 ， createMeetingReqVo：{}" , createMeetingReqVo);
            return false;
        }
        //7、获取预定会议信息 持久化到数据库
        Object meetingInfoCache = redis.get(String.format("%s%s", MEETING_RESPONSE, bookRoomsMeetingId));
        MeetingInfoVo meetingInfoVo = JSON.parseObject(meetingInfoCache.toString(), MeetingInfoVo.class);
        //TODO 将预定的会议信息入库 邮件的形式发送给与会人员

        return true;
    }

    /**
     * 判断是否满足预约会议室的条件
     * @param createMeetingReqVo
     * @return
     */
    public static boolean whetherTheConditionsBookRooms(CreateMeetingReqVo createMeetingReqVo) {
        //会议时长不得小于15分钟且不得大于24小时；且不支持周期性会议。
        if(createMeetingReqVo.getMeetingType().equals(1)){
            //周期性会议 0：普通会议 1：周期性会议
            return false;
        }
        Long startTime = Long.valueOf(createMeetingReqVo.getStartTime());
        Long endTime = Long.valueOf(createMeetingReqVo.getEndTime());
        long timeDifference = endTime-startTime;
        //会议时长不得小于15分钟且不得大于24小时
        if(timeDifference/60 < 15 || timeDifference/3600 > 24){
            return false;
        }
        return true;
    }

    /**
     * 取消会议
     * @param cancelMeetingReqVo
     * @return
     */
    public boolean cancelMeeting(CancelMeetingReqVo cancelMeetingReqVo){
        boolean cancelMeetingAndReleaseRooms = cancelMeetingAndReleaseRooms(cancelMeetingReqVo);
        System.out.println(cancelMeetingReqVo);
        if(!cancelMeetingAndReleaseRooms){
            taskExecutor.submit(() ->{
                guavaRetryHandler.handler(cancelMeetingReqVo , new MMMeetingService() , "saveMeeting");
            });
        }
        return true;
    }

    /**
     * 取消会议同时释放会议室
     * @param cancelMeetingReqVo
     * @return
     */
    public boolean cancelMeetingAndReleaseRooms(CancelMeetingReqVo cancelMeetingReqVo) {
        try {
            List<String> meetingRoomIdList = new ArrayList<>();
            if (cancelMeetingReqVo.getCancelMeeting()) {
                //1、调用腾讯会议接口  取消会议
                HttpResponse httpResponse = meetingsApi.cancelMeeting(cancelMeetingReqVo);
                if (!httpResponse.getResponseCode().equals(200)) {
                    log.error("取消腾讯会议失败 --- cancelMeetingReqVo：{}", JSON.toJSONString(cancelMeetingReqVo));
                    return false;
                }
                //添加释放会议成功标识 ， 为释放会议室失败重试做准备
                cancelMeetingReqVo.setCancelMeeting(true);
                //2、判断是否需要释放会议室
                meetingRoomIdList = cancelMeetingReqVo.getMeetingRoomIdList();
                //没有关联会议室，直接返回成功 ， 反之---需要释放会议室
                if (CollectionUtils.isEmpty(meetingRoomIdList)) {
                    return true;
                }
            }

            //3、构建释放会议室的接口入参 ， 调用释放会议室API接口
            BookRoomsVo bookRoomsVo = BookRoomsVo.builder().meetingId(cancelMeetingReqVo.getMeetingId())
                    .meetingRoomIdList(meetingRoomIdList).build();
            HttpResponse httpResponse1 = roomsApi.releaseRooms(bookRoomsVo);
            if (!httpResponse1.getResponseCode().equals(200)) {
                log.error("释放会议室失败 --- cancelMeetingReqVo：{}", JSON.toJSONString(cancelMeetingReqVo));
                return false;
            }
        }catch (Exception e){
            log.error("取消会议、释放会议室失败 ， cancelMeetingReqVo：{}" , cancelMeetingReqVo);
        }
        return true;
    }


    /**
     * 持久化到数据库
     * @param baseMsg
     */
    public void saveMeeting(BaseMsg baseMsg) {
        System.out.println("保存数据库成功--------------" + baseMsg);
    }
}
