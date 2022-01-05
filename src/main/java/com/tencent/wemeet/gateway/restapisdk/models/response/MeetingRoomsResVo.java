package com.tencent.wemeet.gateway.restapisdk.models.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName MeetingRoomsResVo.java
 * @description: 会议室详情
 * @createTime 2021年11月29日 19:41:00
 */
@Data
public class MeetingRoomsResVo {

    @JSONField(name = "meeting_room_id")
    private String meetingRoomId;

    @JSONField(name = "meeting_room_name")
    private String meetingRoomName;

    @JSONField(name = "meeting_room_location")
    private String meetingRoomLocation;

    @JSONField(name = "account_type")
    private Integer accountType;

    @JSONField(name = "active_code")
    private String activeCode;

    @JSONField(name = "participant_number")
    private Integer participantNumber;

    @JSONField(name = "meeting_room_status")
    private Integer meetingRoomStatus;

    @JSONField(name = "scheduled_status")
    private Integer scheduledStatus;
}
