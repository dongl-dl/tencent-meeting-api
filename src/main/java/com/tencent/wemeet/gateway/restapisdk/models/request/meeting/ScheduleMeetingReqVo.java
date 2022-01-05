package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import lombok.NonNull;

import java.io.Serializable;

/**
 * @author dongliang7
 *
 * @ClassName ScheduleMeetingRegVo.java
 * @description: 创建会议
 * @createTime 2021年11月16日 18:15:00
 */

public class ScheduleMeetingReqVo extends CreateMeetingReqVo implements Serializable {

    public ScheduleMeetingReqVo(@NonNull String userId, @NonNull Integer instanceId, @NonNull String subject) {
        super(userId, instanceId, subject);
    }

}
