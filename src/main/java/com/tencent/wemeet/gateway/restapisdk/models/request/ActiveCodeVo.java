package com.tencent.wemeet.gateway.restapisdk.models.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName ActiveCodeVo.java
 * @description: 生成设备激活码
 * @createTime 2021年11月19日 16:19:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveCodeVo  implements Serializable {
    @NonNull
    @JSONField(name = "meeting_room_id")
    private String meetingRoomId;
}
