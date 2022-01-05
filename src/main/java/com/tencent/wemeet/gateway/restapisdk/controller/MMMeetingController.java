package com.tencent.wemeet.gateway.restapisdk.controller;

import com.tencent.wemeet.gateway.restapisdk.models.meetingmanage.MeetingInfo;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.ScheduleMeetingReqVo;
import com.tencent.wemeet.gateway.restapisdk.service.MMMeetingService;
import com.tencent.wemeet.gateway.restapisdk.service.TenCentBookMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName MMUserController.java
 * @description: TODO
 * @createTime 2021年11月25日 18:46:00
 */
@RestController
@RequestMapping("/meeting")
public class MMMeetingController {

    @Autowired
    private MMMeetingService mmMeetingService;
    @Autowired
    private TenCentBookMeetingService tenCentBookMeetingService;

    @PostMapping("/v1/saveMeetingToTenCentMeeting")
    public boolean saveMeetingToTenCentMeeting(@RequestBody ScheduleMeetingReqVo scheduleMeetingRegVo){
        boolean result = mmMeetingService.createTenCentMeeting(scheduleMeetingRegVo);
        return result;
    }

    @PostMapping("/v1/putMeetingInfoToQueue")
    public void putMeetingInfoToQueue(@RequestBody MeetingInfo meetingInfo){
        tenCentBookMeetingService.putMeetingInfoToQueue(meetingInfo);
    }
}
