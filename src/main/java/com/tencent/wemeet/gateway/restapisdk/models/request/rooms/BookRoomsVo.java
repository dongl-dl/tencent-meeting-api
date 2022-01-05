package com.tencent.wemeet.gateway.restapisdk.models.request.rooms;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 * 
 * @ClassName BookRoomsVo.java
 * @description: 预定会议
 * @createTime 2021年11月19日 17:48:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRoomsVo extends BaseMsg implements Serializable {

    @NonNull
    @JSONField(name = "meeting_id")
    private String meetingId;

    @NonNull
    @JSONField(name = "meeting_room_id_list")
    private List<String> meetingRoomIdList;
}
