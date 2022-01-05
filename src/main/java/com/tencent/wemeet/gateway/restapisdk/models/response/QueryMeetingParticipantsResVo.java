package com.tencent.wemeet.gateway.restapisdk.models.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.models.UserVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 *
 * @ClassName QueryMeetingParticipantsResVo.java
 * @description: 获取参会人员列表响应体
 * @createTime 2021年11月19日 14:05:00
 */
@Data
public class QueryMeetingParticipantsResVo implements Serializable {

    @JSONField(name = "meeting_id")
    private String meetingId;

    @JSONField(name = "meeting_code")
    private String meetingCode;

    private String subject;

    @JSONField(name = "schedule_start_time")
    private String scheduleStartTime;

    @JSONField(name = "schedule_end_time")
    private String scheduleEndTime;

    private List<UserVo> participants;
}
