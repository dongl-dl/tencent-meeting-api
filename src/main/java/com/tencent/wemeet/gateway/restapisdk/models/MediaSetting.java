package com.tencent.wemeet.gateway.restapisdk.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName MediaSetting.java
 * @description: 会议媒体参数配置 实体类
 * @createTime 2021年11月19日 15:34:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaSetting implements Serializable {
    private static final long serialVersionUID = -1L;

    @NonNull
    @JSONField(name = "mute_enable_join")
    private Boolean muteEnableJoin;


    @JSONField(name = "allow_unmute_self")
    private Boolean allowUnmuteSelf;

    @JSONField(name = "mute_all")
    private Boolean muteAll;

    @JSONField(name = "host_video")
    private Boolean hostVideo;

    @JSONField(name = "participant_video")
    private Boolean participantVideo;

    @JSONField(name = "enable_record")
    private Boolean enableRecord;

    @JSONField(name = "play_ivr_on_leave")
    private Boolean playIvrOnLeave;

    @JSONField(name = "play_ivr_on_join")
    private Boolean playIvrOnJoin;

    @JSONField(name = "live_url")
    private Boolean liveUrl;

    @JSONField(name = "only_enterprise_user_allowed")
    private Boolean onlyEnterpriseUserAllowed;

}
