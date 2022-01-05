package com.tencent.wemeet.gateway.restapisdk.models.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.models.MeetingInfoVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 *
 * @ClassName EditMeetingInfoResVo.java
 * @description: 修改会议
 * @createTime 2021年11月19日 14:05:00
 */
@Data
public class EditMeetingInfoResVo implements Serializable {

    @JSONField(name = "meeting_number")
    private Integer meetingNumber;

    @JSONField(name = "meeting_info_list")
    private List<MeetingInfoVo> meetingInfoVoList;
}
