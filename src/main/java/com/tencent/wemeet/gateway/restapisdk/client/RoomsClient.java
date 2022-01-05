package com.tencent.wemeet.gateway.restapisdk.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wemeet.gateway.restapisdk.models.PageVo;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.ActiveCodeVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.rooms.BookRoomsVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.rooms.QueryDeviceListVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.RoomsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author dongliang7
 * 
 * @ClassName RoomsClient.java
 * @description: 腾讯会议API调用 会议室控制层
 * @createTime 2021年11月19日 10:52:00
 */
@RestController
@RequestMapping("roomsClient")
public class RoomsClient {

    @Autowired
    private RoomsApi roomsService;


    /**
     * 查询账户下Rooms资源
     * @return
     */
    @GetMapping("/v1/roomsInventory")
    public HttpResponse roomsInventory(){
        return roomsService.roomsInventory();
    }

    /**
     * 查询会议室列表
     * @return
     */
    @PostMapping("/v1/meetingRooms")
    public Map<String, Object> meetingRooms(@RequestBody PageVo pageVo){
        HttpResponse httpResponse = roomsService.meetingRooms(pageVo);
        Map map = JSON.parseObject(httpResponse.getResponseBody(), Map.class);
        return map;
    }

    /**
     * 生成设备激活码
     * @param activeCodeVo
     * @return
     */
    @PostMapping("/v1/activeCode")
    public Map<String, Object> activeCode(@RequestBody ActiveCodeVo activeCodeVo){
        return roomsService.activeCode(activeCodeVo);
    }

    /**
     * 查询会议室详情
     * @param meetingRoomId
     * @return
     */
    @GetMapping("/v1/queryMeetingRoomInfo/{meetingRoomId}")
    public HttpResponse queryMeetingRoomInfo(@PathVariable("meetingRoomId") String meetingRoomId){
        return roomsService.queryMeetingRoomInfo(meetingRoomId);
    }

    /**
     * 查询设备列表
     * @param queryDeviceListVo
     * @return
     */
    @PostMapping("/v1/queryDeviceList")
    public Map<String, Object> queryDeviceList(@RequestBody QueryDeviceListVo queryDeviceListVo){
        Map<String, Object> stringObjectMap = roomsService.queryDeviceList(queryDeviceListVo);
        return stringObjectMap;
    }

    /**
     * 预定会议室
     * @param bookRoomsVo
     * @return
     */
    @PostMapping("/v1/bookRooms")
    public HttpResponse bookRooms(@RequestBody BookRoomsVo bookRoomsVo){
        return roomsService.bookRooms(bookRoomsVo);
    }

    /**
     * 释放会议室
     * @param bookRoomsVo
     * @return
     */
    @PostMapping("/v1/releaseRooms")
    public HttpResponse releaseRooms(@RequestBody BookRoomsVo bookRoomsVo){
        return roomsService.releaseRooms(bookRoomsVo);
    }
}
