package com.tencent.wemeet.gateway.restapisdk.service;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.config.redis.RedisUtil;
import com.tencent.wemeet.gateway.restapisdk.models.MediaSetting;
import com.tencent.wemeet.gateway.restapisdk.models.RecurringRule;
import com.tencent.wemeet.gateway.restapisdk.models.UserVo;
import com.tencent.wemeet.gateway.restapisdk.models.meetingmanage.MeetingInfo;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.CancelMeetingReqVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.CreateMeetingReqVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName TenCentRoomsProcessService.java
 * @description: TODO
 * @createTime 2021年12月04日 14:43:00
 */
@Service
@Slf4j
public class TenCentCancelMeetingService {
    @Autowired
    private MMMeetingService mmMeetingService;
    @Autowired
    private RedisUtil redis;

    //初始化队列
    private static LinkedBlockingQueue<CancelMeetingReqVo> taskQueue = new LinkedBlockingQueue<>();

    static final String MEETING_CANCEL_LIST_KEY = "TEN_CENT:MEETING:MEETING_CANCEL_LIST";

    //线程池参数
    static final Integer CPU_NUM = Runtime.getRuntime().availableProcessors();
    static final Integer THREAD_NUM = CPU_NUM * 2 + 1;
    static final Long KEEP_ALIVE_TIME = 0L;

    //初始化线程池
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(THREAD_NUM - 1 , THREAD_NUM ,
            KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024)
    );

    //线程池是否销毁标识
    private volatile boolean flag;

    /**
     * 处理队列中的会议信息
     */
    @PostConstruct
    public void initDealQueue() {
        flag = true;
        threadPoolExecutor.execute(() -> {
            CancelMeetingReqVo cancelMeetingReqVo;
            for (;;) {
                if (!flag) {
                    return;
                }
                try {
                    cancelMeetingReqVo = taskQueue.take();
                    //处理业务数据
                    this.doCancelMeetingFromTenCent(cancelMeetingReqVo);

                    //处理完，将缓存中的信息删除
                    redis.lRemove(MEETING_CANCEL_LIST_KEY , 1 , JSON.toJSONString(cancelMeetingReqVo));
                } catch (Exception e) {
                    log.error("定时任务处理腾讯会议取消会议失败:{}", e);
                }
            }
        });
    }


    /**
     * 关闭线程池
     */
    @PreDestroy
    public void destroy() {
        flag = false;
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

    @PostConstruct
    public void initDealCache() {
        flag = true;
        //启动一个线程，去缓存中获取宕机未处理的消息
        threadPoolExecutor.execute(() -> {
            //服务启动后延时10秒
            try {
                TimeUnit.SECONDS.sleep(10);
                if (!flag) {
                    return;
                }
                //获取redis缓存中宕机丢失的处理消息
                List<Object> list = redis.lGet(MEETING_CANCEL_LIST_KEY, 0, -1);
                log.info("服务启动处理宕机未处理腾讯会议会议信息开始，需要处理的数据集：{}" , list);
                if(!CollectionUtils.isEmpty(list)){
                    list.stream().forEach(user ->{
                        CancelMeetingReqVo cancelMeetingReqVo = JSON.parseObject(user.toString(), CancelMeetingReqVo.class);
                        this.doCancelMeetingFromTenCent(cancelMeetingReqVo);
                        //处理完，将缓存中的信息删除
                        redis.lRemove(MEETING_CANCEL_LIST_KEY , 1 , JSON.toJSONString(cancelMeetingReqVo));
                    });
                }
            } catch (InterruptedException e) {
                log.error("服务启动处理宕机丢失腾讯会议会议取消失败， msg:{}" , e.getMessage());
            }
        });
    }

    /**
     * 将待取消的会议信息放入队列
     * @param meetingInfo 会议信息
     * @param reason 取消或结束的原因
     */
    public void putMeetingInfoToQueue(MeetingInfo meetingInfo , String reason){
        //1、参数校验
        parameterCalibration(meetingInfo);
        //2、构建会议预定入参
        CancelMeetingReqVo cancelMeetingReqVo = CancelMeetingReqVo.Builder.create()
                .withUserId(meetingInfo.getId()).withInstanceId(1).withReasonCode(10001)
                .withMeetingId(meetingInfo.getId()).withReasonDetail(reason)
                .build();
        //3、为了防止宕机数据无法恢复 且这里因为某些原因没有引入消息中间件 先使用阻塞队列+缓存 后续可以优化
        redis.lSet(MEETING_CANCEL_LIST_KEY , JSON.toJSONString(cancelMeetingReqVo));

        //4、将待处理的会议信息放入队列
        try {
            taskQueue.put(cancelMeetingReqVo);
        } catch (InterruptedException e) {
            log.error("将会议信息放入队列失败 ， msg:{} , editMeetingReqVo:{}" , e.getMessage() , cancelMeetingReqVo);
        }
    }


    /**
     * 参数检验
     * @param meetingInfo
     */
    private void parameterCalibration(MeetingInfo meetingInfo) {
        Optional.ofNullable(meetingInfo).orElseThrow(()-> new RuntimeException("会议室信息为空"));

        if(StringUtils.isBlank(meetingInfo.getId())){
            log.error("取消会议--会议id为空， meetingInfo:{}" , meetingInfo);
        }
    }

    /**
     * 调用腾讯会议API 取消会议 释放会议室
     * @param cancelMeetingReqVo
     */
    public synchronized void doCancelMeetingFromTenCent(CancelMeetingReqVo cancelMeetingReqVo){
        mmMeetingService.cancelMeeting(cancelMeetingReqVo);
    }
}
