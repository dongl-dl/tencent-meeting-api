package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;

/**
 * @author dongliang7
 *
 * @ClassName QueryMeetingByIdRegVo.java
 * @description: 获取会议根据 id 会议id
 * @createTime 2021年11月16日 16:56:00
 */
@Data
public class QueryMeetingByIdRegVo {

    @NonNull
    @JSONField(name = "meetingId")
    private String meetingId;

    @NonNull
    @JSONField(name = "userid")
    private String userId;

    @NonNull
    @JSONField(name = "instanceid")
    private Integer instanceId;


    public static final class Builder {
        private String meetingId;
        private String userId;
        private Integer instanceId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withInstanceId(Integer instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public QueryMeetingByIdRegVo build() {
            return new QueryMeetingByIdRegVo(meetingId, userId, instanceId);
        }
    }
}
