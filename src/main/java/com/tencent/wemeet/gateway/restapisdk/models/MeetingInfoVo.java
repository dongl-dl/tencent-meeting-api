package com.tencent.wemeet.gateway.restapisdk.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 * 
 * @ClassName MeetingInfoVo.java
 * @description: 会议详情 实体类
 * @createTime 2021年11月18日 14:34:00
 */
@Data
public class MeetingInfoVo implements Serializable {

    //主题
    private String subject;

    //会议id
    @JSONField(name = "meeting_id")
    private String meetingId;

    //会议编码
    @JSONField(name = "meeting_code")
    private String meetingCode;

    //入会密码
    private String password;

    //会议状态
    private String status;

    //会议开始时间
    @JSONField(name = "start_time")
    private String startTime;

    //会议结束时间
    @JSONField(name = "end_time")
    private String endTime;

    //会议加入链接
    @JSONField(name = "join_url")
    private String joinUrl;

    //会议媒体设置
    private MediaSetting settings;

    //主持人
    private List<UserVo> hosts;

    //与会人员
    private List<UserVo> participants;
}
