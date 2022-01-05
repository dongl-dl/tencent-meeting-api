package com.tencent.wemeet.gateway.restapisdk.tencentapi;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.models.PageVo;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.ActiveCodeVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.rooms.BookRoomsVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.rooms.QueryDeviceListVo;
import com.tencent.wemeet.gateway.restapisdk.util.TencentMeetingProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author dongliang7
 * 
 * @ClassName RoomsServiceImpl.java
 * @description:  腾讯会议API调用 会议室业务处理层
 * @createTime 2021年11月24日 10:14:00
 */
@Service
@Slf4j
public class RoomsApi {
    private final String baseUri = "/v1/meeting-rooms";

    /**
     * 查询账户下 Rooms 资源
     * @return
     */
    public HttpResponse roomsInventory() {
        String uri = "/v1/rooms-inventory";
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return httpResponse;
    }

    /**
     * 查询会议室列表
     * @return
     */
    public HttpResponse meetingRooms(PageVo pageVo) {
        ///v1/meeting-rooms?page={page}&page_size={page_size}&meeting_room_name={meeting_room_name}
        String uri = baseUri.concat("?page=").concat(String.valueOf(pageVo.getPage()))
                .concat("&page_size=").concat(String.valueOf(pageVo.getPageSize()));
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return httpResponse;
    }

    /**
     * 生成设备激活码
     * @param activeCodeVo
     * @return
     */
    public Map<String, Object> activeCode(ActiveCodeVo activeCodeVo) {
        String uri = baseUri.concat("/").concat(activeCodeVo.getMeetingRoomId()).concat("/active-code");
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, activeCodeVo);
        return JSON.parseObject(httpResponse.getResponseBody().toString(), Map.class);
    }

    /**
     * 查询会议室详情
     * @param meetingRoomId
     * @return
     */
    public HttpResponse queryMeetingRoomInfo(String meetingRoomId) {
        String uri = baseUri.concat("/").concat(meetingRoomId);
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return httpResponse;
    }

    /**
     * 查询设备列表
     * @param queryDeviceListVo
     * @return
     */
    public Map<String, Object> queryDeviceList(QueryDeviceListVo queryDeviceListVo) {
        //https://api.meeting.qq.com/v1/devices?page={page}&page_size={page_size}&meeting_room_name={meeting_room_name}
        String uri = "/v1/devices".concat("?page=").concat(String.valueOf(queryDeviceListVo.getPage()))
                .concat("&page_size=").concat(String.valueOf(queryDeviceListVo.getPageSize()))
                .concat("&meeting_room_name=").concat(queryDeviceListVo.getMeetingRoomName());
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return JSON.parseObject(httpResponse.getResponseBody().toString(), Map.class);
    }

    /**
     * 预定会议室
     * @param bookRoomsVo
     * @return
     */
    public HttpResponse bookRooms(BookRoomsVo bookRoomsVo) {
        String uri = "/v1/meetings/".concat(bookRoomsVo.getMeetingId()).concat("/book-rooms");
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, bookRoomsVo);
        return httpResponse;
    }

    /**
     * 释放会议室
     * @param bookRoomsVo
     * @return
     */
    public HttpResponse releaseRooms(BookRoomsVo bookRoomsVo) {
        //https://api.meeting.qq.com/v1/meetings/{meeting_id}/release-rooms
        String uri = "/v1/meetings/".concat(bookRoomsVo.getMeetingId()).concat("/release-rooms");
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, bookRoomsVo);
        return httpResponse;
    }
}
