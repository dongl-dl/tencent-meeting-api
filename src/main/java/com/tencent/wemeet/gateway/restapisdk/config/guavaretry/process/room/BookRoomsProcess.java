package com.tencent.wemeet.gateway.restapisdk.config.guavaretry.process.room;

import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.RetryProcess;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.rooms.BookRoomsVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.RoomsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName BookRoomsProcess.java
 * @description: 处理重试预定会议室
 * @createTime 2021年12月02日 14:52:00
 */
@Component
@Slf4j
public class BookRoomsProcess implements RetryProcess<BookRoomsVo> {

    @Autowired
    private RoomsApi roomsApi;

    @Override
    public boolean process(BookRoomsVo msg) {
        log.info("<<<<<<<<<<<<<<处理重试预定会议室开始>>>>>>>>>>>>>>");
        //调用腾讯会议 创建会议API
        HttpResponse httpResponse = roomsApi.bookRooms(msg);

        //根据调用结果决定是否需要进行重试
        if(200 == httpResponse.getResponseCode()){
            return true;
        }
        log.info("处理重试预定会议室失败----------- msg:{}" , msg);
        return false;
    }
}
