package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.models.UserVo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 * 
 * @ClassName MeetingInviteesVo.java
 * @description: 设置会议邀请成员
 * @createTime 2021年11月23日 14:36:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingInviteesVo implements Serializable {

    @NonNull
    @JSONField(name = "meeting_id")
    private String meetingId;

    @NonNull
    @JSONField(name = "userid")
    private String userId;

    @NonNull
    @JSONField(name = "instanceid")
    private Integer instanceId;

    @JSONField(name = "invitees")
    private List<UserVo> invitees;

    private static final long serialVersionUID = -3042686055658047285L;
}
