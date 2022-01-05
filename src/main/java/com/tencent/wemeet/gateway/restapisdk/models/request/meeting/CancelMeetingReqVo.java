package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 *
 * @ClassName ActiveCodeVo.java
 * @description: 取消会议
 * @createTime 2021年11月16日 16:16:20
 */
@Data
public class CancelMeetingReqVo extends BaseMsg implements Serializable {
    private static final long serialVersionUID = -1L;

    @NonNull
    @JSONField(name = "userid")
    private String userId;

    @NonNull
    @JSONField(name = "instanceid")
    private Integer instanceId;

    @NonNull
    @JSONField(name = "reason_code")
    private Integer reasonCode;

    @JSONField(name = "reason_detail")
    private String reasonDetail;

    private String meetingId;

    private List<String> meetingRoomIdList;

    private Boolean cancelMeeting = false;

    public CancelMeetingReqVo(@NonNull String meetingId ,  @NonNull String userId, @NonNull Integer instanceId, @NonNull Integer reasonCode) {
        this.userId = userId;
        this.instanceId = instanceId;
        this.reasonCode = reasonCode;
        this.meetingId = meetingId;
    }
    public CancelMeetingReqVo() {
    }

    public static final class Builder {
        private String userId;
        private Integer instanceId;
        private Integer reasonCode;
        private String reasonDetail;
        private String meetingId;

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

        public Builder withReasonCode(Integer reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        public Builder withReasonDetail(String reasonDetail) {
            this.reasonDetail = reasonDetail;
            return this;
        }

        public CancelMeetingReqVo build() {
            CancelMeetingReqVo cancelMeetingReqVo = new CancelMeetingReqVo(meetingId ,userId, instanceId, reasonCode);
            cancelMeetingReqVo.setReasonDetail(reasonDetail);
            return cancelMeetingReqVo;
        }
    }
}
