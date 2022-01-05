package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;

/**
 * @author dongliang7
 *
 * @ClassName QueryMeetingByCodeRegVo.java
 * @description: 获取会议根据 code 会议编码
 * @createTime 2021年11月16日 16:36:00
 */
@Data
public class QueryMeetingByCodeRegVo {

    @NonNull
    @JSONField(name = "meeting_code")
    private String meetingCode;

    @NonNull
    @JSONField(name = "userid")
    private String userId;

    @NonNull
    @JSONField(name = "instanceid")
    private Integer instanceId;


    public static final class Builder {
        private String meetingCode;
        private String userId;
        private Integer instanceId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withMeetingCode(String meetingCode) {
            this.meetingCode = meetingCode;
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

        public QueryMeetingByCodeRegVo build() {
            return new QueryMeetingByCodeRegVo(meetingCode, userId, instanceId);
        }
    }
}
