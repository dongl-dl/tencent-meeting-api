package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author dongliang7
 *
 * @ClassName QueryMeetingInfoListVo.java
 * @description: 获取会议列表
 * @createTime 2021年11月16日 16:56:00
 */
@Data
public class QueryMeetingInfoListVo implements Serializable {
    private static final long serialVersionUID = -1L;

    @NonNull
    @JSONField(name = "userid")
    private String userId;

    @NonNull
    @JSONField(name = "instanceid")
    private Integer instanceId;


    public static final class Builder {
        private String userId;
        private Integer instanceId;

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

        public QueryMeetingInfoListVo build() {
            return new QueryMeetingInfoListVo(userId, instanceId);
        }
    }
}
