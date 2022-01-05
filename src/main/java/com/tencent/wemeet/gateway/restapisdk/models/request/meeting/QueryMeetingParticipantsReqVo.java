package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dongliang7
 *
 * @ClassName QueryMeetingParticipantsReqVo.java
 * @description: 查询参会人员
 * @createTime 2021年11月16日 18:20:00
 */
@Data
public class QueryMeetingParticipantsReqVo implements Serializable {

    @JSONField(name = "meeting_id")
    private String meetingId;

    private String userId;


    public static final class Builder {
        private String meetingId;
        private String userId;

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

        public QueryMeetingParticipantsReqVo build() {
            QueryMeetingParticipantsReqVo queryMeetingParticipantsReqVo = new QueryMeetingParticipantsReqVo();
            queryMeetingParticipantsReqVo.setMeetingId(meetingId);
            queryMeetingParticipantsReqVo.setUserId(userId);
            return queryMeetingParticipantsReqVo;
        }
    }
}
