package com.tencent.wemeet.gateway.restapisdk.config.guavaretry.process.meeting;


import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.RetryProcess;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.CreateMeetingReqVo;
import com.tencent.wemeet.gateway.restapisdk.service.MMMeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author dongliang7
 * @projectName guava-retry-demo-master
 * @ClassName CreateUserProcess.java
 * @description: 创建会议重试任务处理类
 * @createTime 2021年11月25日 15:00:00
 */
@Component
@Slf4j
public class CreateMeetingProcess implements RetryProcess<CreateMeetingReqVo> {
    @Autowired
    private MMMeetingService mmMeetingService;

    @Override
    public boolean process(CreateMeetingReqVo createMeetingReqVo) {
        log.info("处理重试------创建会议");
        //调用腾讯会议 创建会议API
        boolean meetingAndBookRooms = mmMeetingService.createMeetingAndBookRooms(createMeetingReqVo);
        //根据调用结果决定是否需要进行重试
        if(meetingAndBookRooms){
            return true;
        }
        return false;
    }
}
