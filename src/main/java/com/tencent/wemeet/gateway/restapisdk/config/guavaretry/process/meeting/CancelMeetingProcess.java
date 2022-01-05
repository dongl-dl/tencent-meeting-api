package com.tencent.wemeet.gateway.restapisdk.config.guavaretry.process.meeting;

import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.RetryProcess;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.CancelMeetingReqVo;
import com.tencent.wemeet.gateway.restapisdk.service.MMMeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName CancelMeetingProcess.java
 * @description: 取消会议及释放会议室重试处理类
 * @createTime 2021年12月03日 19:32:00
 */
@Component
@Slf4j
public class CancelMeetingProcess implements RetryProcess<CancelMeetingReqVo> {
    @Autowired
    private MMMeetingService mmMeetingService;

    @Override
    public boolean process(CancelMeetingReqVo cancelMeetingReqVo) {
        log.info("处理重试------取消会议");
        //调用腾讯会议 创建会议API
        boolean cancelMeetingAndReleaseRooms = mmMeetingService.cancelMeetingAndReleaseRooms(cancelMeetingReqVo);
        //根据调用结果决定是否需要进行重试
        if(cancelMeetingAndReleaseRooms){
            return true;
        }
        return false;
    }
}
