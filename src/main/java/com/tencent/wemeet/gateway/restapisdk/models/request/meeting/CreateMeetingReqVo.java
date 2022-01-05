package com.tencent.wemeet.gateway.restapisdk.models.request.meeting;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import com.tencent.wemeet.gateway.restapisdk.models.MediaSetting;
import com.tencent.wemeet.gateway.restapisdk.models.RecurringRule;
import com.tencent.wemeet.gateway.restapisdk.models.UserVo;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 *
 * @ClassName EditMeetingRegVo.java
 * @description: 修改会议
 * @createTime 2021年11月16日 16:20:20
 */
@Data
public class CreateMeetingReqVo extends BaseMsg implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 调用方用于标示用户的唯一 ID（企业内部请使用企业唯一用户标识）
     */
    @NonNull
    @JSONField(name = "userid")
    private String userId;

    /**
     * 用户的终端设备类型
     */
    @NonNull
    @JSONField(name = "instanceid")
    private Integer instanceId;

    /**
     * 会议主题
     */
    @NonNull
    private String subject;

    /**
     * 会议类型：
     * 0：预约会议
     * 1：快速会议
     */
    private Integer type;

    /**
     * 会议指定主持人的用户 ID，如果无指定，
     * 主持人将被设定为参数 userid 的用户，即 API 调用者
     */
    private List<UserVo> hosts;

    /**
     * 会议邀请的参会者，可为空。
     */
    private List<UserVo> invitees;

    /**
     * 会议开始时间戳（单位秒）
     */
    @JSONField(name = "start_time")
    private String startTime;

    /**
     * 会议结束时间戳（单位秒）
     */
    @JSONField(name = "end_time")
    private String endTime;

    /**
     * 会议密码（4~6位数字），可不填
     */
    private String password;

    /**
     * 会议媒体参数配置
     */
    private MediaSetting settings;

    /**
     * 默认值为0。
     * 0：普通会议
     * 1：周期性会议（周期性会议时 type 不能为快速会议，同一账号同时最多可预定50场周期性会议）
     */
    @JSONField(name = "meeting_type")
    private Integer meetingType;

    /**
     * 周期性会议配置
     * meeting_type 为1时起作用
     */
    @JSONField(name = "recurring_rule")
    private RecurringRule recurringRule;

    private String meetingId;

    private  List<String> meetingRoomsIdList;

    public static final class Builder {

        private String userId;
        private Integer instanceId;
        private String subject;
        private List<UserVo> hosts;
        private List<UserVo> invitees;
        private String startTime;
        private String endTime;
        private String password;
        private Integer type;
        private MediaSetting settings;
        private Integer meetingType;
        private RecurringRule recurringRule;

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

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withHosts(List<UserVo> hosts) {
            this.hosts = hosts;
            return this;
        }

        public Builder withInvitees(List<UserVo> invitees) {
            this.invitees = invitees;
            return this;
        }

        public Builder withStartTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder withEndTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder withType(Integer type) {
            this.type = type;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withMeetingType(Integer meetingType) {
            this.meetingType = meetingType;
            return this;
        }

        public Builder withSettings(MediaSetting settings) {
            this.settings = settings;
            return this;
        }

        public Builder withRecurringRule(RecurringRule recurringRule) {
            this.recurringRule = recurringRule;
            return this;
        }


        public CreateMeetingReqVo build() {
            CreateMeetingReqVo createMeetingReqVo = new CreateMeetingReqVo(userId, instanceId, subject);
            createMeetingReqVo.setHosts(hosts);
            createMeetingReqVo.setInvitees(invitees);
            createMeetingReqVo.setStartTime(startTime);
            createMeetingReqVo.setEndTime(endTime);
            createMeetingReqVo.setPassword(password);
            createMeetingReqVo.setType(type);
            createMeetingReqVo.setSettings(settings);
            createMeetingReqVo.setMeetingType(meetingType);
            createMeetingReqVo.setRecurringRule(recurringRule);
            return createMeetingReqVo;
        }
    }
}
