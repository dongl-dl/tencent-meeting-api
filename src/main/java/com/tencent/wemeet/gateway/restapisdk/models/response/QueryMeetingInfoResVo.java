package com.tencent.wemeet.gateway.restapisdk.models.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.models.MeetingInfoVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 *
 * @ClassName QueryMeetingInfoResVo.java
 * @description: 获取会议详情响应体
 * @createTime 2021年11月19日 14:05:00
 */
@Data
public class QueryMeetingInfoResVo implements Serializable {

    @JSONField(name = "meeting_number")
    private Integer meetingNumber;

    @JSONField(name = "meeting_info_list")
    private List<MeetingInfoVo> meetingInfoVoList;


    public static final class Builder {
        private Integer meetingNumber;
        private List<MeetingInfoVo> meetingInfoVoList;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withMeetingNumber(Integer meetingNumber) {
            this.meetingNumber = meetingNumber;
            return this;
        }

        public Builder withMeetingInfoVoList(List<MeetingInfoVo> meetingInfoVoList) {
            this.meetingInfoVoList = meetingInfoVoList;
            return this;
        }

        public QueryMeetingInfoResVo build() {
            QueryMeetingInfoResVo queryMeetingInfoResVo = new QueryMeetingInfoResVo();
            queryMeetingInfoResVo.setMeetingNumber(meetingNumber);
            queryMeetingInfoResVo.setMeetingInfoVoList(meetingInfoVoList);
            return queryMeetingInfoResVo;
        }
    }
}
