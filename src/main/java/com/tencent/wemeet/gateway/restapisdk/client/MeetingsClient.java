package com.tencent.wemeet.gateway.restapisdk.client;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.base.TenCentSdkError;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.*;
import com.tencent.wemeet.gateway.restapisdk.models.response.EditMeetingInfoResVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryMeetingInfoResVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryMeetingParticipantsResVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.MeetingsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dongliang7
 * 
 * @ClassName MeetingsController.java
 * @description: 腾讯会议API调用 会议管理控制层
 * @createTime 2021年11月19日 10:46:00
 */
@RestController
@RequestMapping("/meetingsClient")
public class MeetingsClient {

    @Autowired
    private MeetingsApi meetingsService;

    /**
     * 创建会议
     * @param scheduleMeetingRegVo {@link ScheduleMeetingReqVo}
     * @return
     */
    @PostMapping("/v1/scheduleMeeting")
    public HttpResponse scheduleMeeting(@RequestBody ScheduleMeetingReqVo scheduleMeetingRegVo){
        return meetingsService.scheduleMeeting(scheduleMeetingRegVo);
    }

    /**
     * 通过 meetingId查询会议
     * @param queryMeetingByIdRegVo  {@link QueryMeetingByIdRegVo}
     * @return
     */
    @PostMapping("/v1/queryMeetingInfo")
    public QueryMeetingInfoResVo queryMeetingInfo(QueryMeetingByIdRegVo queryMeetingByIdRegVo){
        return meetingsService.queryMeetingInfo(queryMeetingByIdRegVo);
    }

    /**
     * 取消会议
     * @param meetingId
     * @return
     */
    @PostMapping("/v1/cancelMeeting/{meetingId}")
    public Object cancelMeeting(@PathVariable("meetingId") String meetingId ,@RequestBody CancelMeetingReqVo cancelMeetingReqVo){
        cancelMeetingReqVo.setMeetingId(meetingId);
        HttpResponse httpResponse = meetingsService.cancelMeeting(cancelMeetingReqVo);
        if(!httpResponse.getResponseCode().equals(200)){
            TenCentSdkError tenCentSdkError = JSON.parseObject(httpResponse.getResponseBody(), TenCentSdkError.class);
            System.out.println(tenCentSdkError);
            return tenCentSdkError;
        }
        return httpResponse;
    }

    /**
     * 修改会议
     * @param createMeetingReqVo  {@link CreateMeetingReqVo}
     * @param meetingId
     * @return
     */
    @PostMapping("/v1/editMeeting/{meetingId}")
    public EditMeetingInfoResVo editMeeting(@RequestBody CreateMeetingReqVo createMeetingReqVo, @PathVariable("meetingId") String meetingId){
        return meetingsService.editMeeting(createMeetingReqVo, meetingId);
    }

    /**
     * 查询会议列表
     * @param queryMeetingInfoListVo {@link QueryMeetingInfoListVo}
     * @return
     */
    @PostMapping("/v1/queryMeetingInfoList")
    public  QueryMeetingInfoResVo queryMeetingInfoList(@RequestBody QueryMeetingInfoListVo queryMeetingInfoListVo){
        return meetingsService.queryMeetingInfoList(queryMeetingInfoListVo);
    }

    /**
     * 查询参会人员
     * @param reqVo
     * @return
     */
    @PostMapping("/v1/queryMeetingParticipants")
    public QueryMeetingParticipantsResVo queryMeetingParticipants(@RequestBody QueryMeetingParticipantsReqVo reqVo){
        return meetingsService.queryMeetingParticipants(reqVo);
    }

    /**
     * 设置会议邀请成员
     * @param meetingInviteesVo
     * @return
     */
    @PostMapping("/v1/setUpMeetingInvitees")
    public String setUpMeetingInvitees(@RequestBody MeetingInviteesVo meetingInviteesVo){
        return meetingsService.setUpMeetingInvitees(meetingInviteesVo);
    }
}
