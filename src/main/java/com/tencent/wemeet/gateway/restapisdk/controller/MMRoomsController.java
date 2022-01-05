package com.tencent.wemeet.gateway.restapisdk.controller;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wemeet.gateway.restapisdk.models.PageVo;
import com.tencent.wemeet.gateway.restapisdk.service.MMRoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName MMUserController.java
 * @description: TODO
 * @createTime 2021年11月25日 18:46:00
 */
@RestController
@RequestMapping("/rooms")
public class MMRoomsController {

    @Autowired
    private MMRoomsService meetingRooms;

    @PostMapping("/v1/getMeetingRoomsFromTenCentMeeting")
    public JSONObject getMeetingRoomsFromTenCentMeeting(@RequestBody PageVo pageVo){
        Optional.ofNullable(pageVo).orElseThrow(() ->new RuntimeException("分页参数为空"));
        JSONObject jsonObject = meetingRooms.meetingRooms(pageVo);
        return jsonObject;
    }
}
