package com.tencent.wemeet.gateway.restapisdk.tencentapi;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.*;
import com.tencent.wemeet.gateway.restapisdk.models.response.EditMeetingInfoResVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryMeetingInfoResVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryMeetingParticipantsResVo;
import com.tencent.wemeet.gateway.restapisdk.util.TencentMeetingProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author dongliang7
 * 
 * @ClassName MeetingsServiceImpl.java
 * @description: 腾讯会议API调用  会议管理业务处理层
 * @createTime 2021年11月24日 09:51:00
 */
@Service
@Slf4j
public class MeetingsApi {
    private final String baseUri = "/v1/meetings";

    /**
     * 创建会议
     *
     * @param createMeetingReqVo
     * @return
     */
    public HttpResponse scheduleMeeting(CreateMeetingReqVo createMeetingReqVo) {
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(baseUri, createMeetingReqVo);
        return httpResponse;
    }

    /**
     * 通过 meetingId 查询会议
     *
     * @param queryMeetingByIdRegVo
     * @return
     */
    public QueryMeetingInfoResVo queryMeetingInfo(QueryMeetingByIdRegVo queryMeetingByIdRegVo) {
        String uri = baseUri.concat("/").concat(queryMeetingByIdRegVo.getMeetingId()).concat("?userid=")
                .concat(queryMeetingByIdRegVo.getUserId()).concat("&instanceid=")
                .concat(String.valueOf(queryMeetingByIdRegVo.getInstanceId()));
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return JSON.parseObject(httpResponse.getResponseBody(), QueryMeetingInfoResVo.class);
    }

    /**
     * 取消会议
     *
     * @param cancelMeetingReqVo
     * @return
     */
    public HttpResponse cancelMeeting(CancelMeetingReqVo cancelMeetingReqVo) {
        String uri = baseUri.concat("/").concat(cancelMeetingReqVo.getMeetingId()).concat("/").concat("cancel");
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, cancelMeetingReqVo);
        return httpResponse;
    }

    /**
     * 修改会议
     *
     * @param createMeetingReqVo
     * @return
     */
    public EditMeetingInfoResVo editMeeting(CreateMeetingReqVo createMeetingReqVo, String meetingId) {
        String uri = baseUri.concat("/").concat(meetingId);
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, createMeetingReqVo);
        return JSON.parseObject(httpResponse.getResponseBody().toString(), EditMeetingInfoResVo.class);
    }

    /**
     * 查询会议列表
     *
     * @param queryMeetingInfoListVo
     * @return
     */
    public QueryMeetingInfoResVo queryMeetingInfoList(QueryMeetingInfoListVo queryMeetingInfoListVo) {
        String uri = baseUri.concat("?").concat("userid=").concat(queryMeetingInfoListVo.getUserId())
                .concat("&instanceid=").concat(String.valueOf(queryMeetingInfoListVo.getInstanceId()));
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return JSON.parseObject(httpResponse.getResponseBody().toString(), QueryMeetingInfoResVo.class);
    }

    /**
     * 查询参会人员
     * @param reqVo
     * @return
     */
    public QueryMeetingParticipantsResVo queryMeetingParticipants(QueryMeetingParticipantsReqVo reqVo) {
        String uri = baseUri.concat("/").concat(reqVo.getMeetingId()).concat("/participants?userid=").concat(reqVo.getUserId());
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return JSON.parseObject(httpResponse.getResponseBody().toString(), QueryMeetingParticipantsResVo.class);
    }

    /**
     * 设置会议邀请成员
     * @param meetingInviteesVo
     * @return
     */
    public String setUpMeetingInvitees(MeetingInviteesVo meetingInviteesVo) {
        //https://api.meeting.qq.com/v1/meetings/{meeting_id}/invitees
        String uri = baseUri.concat("/").concat(meetingInviteesVo.getMeetingId()).concat("/invitees");
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, meetingInviteesVo);
        return httpResponse.getResponseCode().toString();
    }
}
