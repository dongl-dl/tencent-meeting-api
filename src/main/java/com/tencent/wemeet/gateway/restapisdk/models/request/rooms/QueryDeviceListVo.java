package com.tencent.wemeet.gateway.restapisdk.models.request.rooms;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName QueryDeviceListVo.java
 * @description: 获取设备列表
 * @createTime 2021年11月19日 16:52:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryDeviceListVo implements Serializable {
    private static final long serialVersionUID  = -1L;
    @NonNull
    @JSONField(name = "meeting_room_name")
    private String meetingRoomName;

    @NonNull
    @JSONField(name = "page")
    private Integer page;

    @NonNull
    @JSONField(name = "page_size")
    private Integer pageSize;
}
