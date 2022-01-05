package com.tencent.wemeet.gateway.restapisdk.controller;

import com.tencent.wemeet.gateway.restapisdk.models.meetingmanage.UserInfo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserCreateVo;
import com.tencent.wemeet.gateway.restapisdk.service.MMUserService;
import com.tencent.wemeet.gateway.restapisdk.service.TenCentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName MMUserController.java
 * @description: TODO
 * @createTime 2021年11月25日 18:46:00
 */
@RestController
@RequestMapping("/user")
public class MMUserController {

    @Autowired
    private MMUserService mmUserService;

    @Autowired
    private TenCentUserService tenCentUserService;

    @PostMapping("/v1/saveUserToTenCentMeeting")
    public boolean saveUserToTenCentMeeting(@RequestBody UserCreateVo userCreateVo){
        boolean result = mmUserService.createTenCentUser(userCreateVo);
        return result;
    }


    @PostMapping("/v1/putUserInfoToQueue")
    public void putUserInfoToQueue(@RequestBody UserInfo userInfo){
        tenCentUserService.putUserInfoToQueue(userInfo);
    }

    @GetMapping("/v1/deleteUserFromTenCentMeeting/{userId}")
    public boolean deleteUserFromTenCentMeeting(@PathVariable("userId") String userId){
        boolean result = mmUserService.deteleTenCentUser(userId);
        return result;
    }
}
