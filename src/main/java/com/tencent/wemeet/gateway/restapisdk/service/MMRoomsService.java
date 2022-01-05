package com.tencent.wemeet.gateway.restapisdk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wemeet.gateway.restapisdk.common.BeanUtils;
import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.GuavaRetryHandler;
import com.tencent.wemeet.gateway.restapisdk.config.redis.RedisUtil;
import com.tencent.wemeet.gateway.restapisdk.models.PageVo;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.rooms.BookRoomsVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.MeetingRoomsResVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.RoomsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName UserService.java
 * @description:  会议管理用户 业务处理层
 * @createTime 2021年11月25日 17:52:00
 */
@Service
@Slf4j
public class MMRoomsService {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private GuavaRetryHandler guavaRetryHandler;

    @Autowired
    private RoomsApi roomsApi;

    /**
     * 会议室列表
     * @param pageVo
     * @return
     */
    public JSONObject meetingRooms(PageVo pageVo){
        //调用腾讯会议API接口 查询会议室列表
        HttpResponse httpResponse = roomsApi.meetingRooms(pageVo);
        JSONObject meetingRoomsResult = JSON.parseObject(httpResponse.getResponseBody());
        return meetingRoomsResult;
    }

    /**
     * 预定会议室
     * @param bookRoomsVo
     */
    public void bookRooms(BookRoomsVo bookRoomsVo){
        HttpResponse httpResponse = roomsApi.bookRooms(bookRoomsVo);
        if(!httpResponse.getResponseCode().equals(200)){
            taskExecutor.submit(() ->{
                guavaRetryHandler.handler(bookRoomsVo , new MMUserService() , "save");
            });
        }
    }
}
