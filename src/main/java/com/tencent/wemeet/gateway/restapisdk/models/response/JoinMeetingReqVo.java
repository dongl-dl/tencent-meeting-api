package com.tencent.wemeet.gateway.restapisdk.models.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dongliang7
 *
 * @ClassName JoinMeetingReqVo.java
 * @description:
 * @createTime 2021年11月19日 14:05:00
 */
@Data
public class JoinMeetingReqVo implements Serializable {

    @JSONField(name = "userid")
    private String userId;

    @JSONField(name = "instanceid")
    private Integer instanceId;

    @JSONField(name = "display_name")
    private String displayName;

    private String password;


    public static final class Builder {
        private String userId;
        private Integer instanceId;
        private String displayName;
        private String password;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withInstanceId(Integer instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public JoinMeetingReqVo build() {
            JoinMeetingReqVo joinMeetingReqVo = new JoinMeetingReqVo();
            joinMeetingReqVo.setUserId(userId);
            joinMeetingReqVo.setInstanceId(instanceId);
            joinMeetingReqVo.setDisplayName(displayName);
            joinMeetingReqVo.setPassword(password);
            return joinMeetingReqVo;
        }
    }
}
