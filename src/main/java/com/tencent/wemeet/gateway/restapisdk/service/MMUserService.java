package com.tencent.wemeet.gateway.restapisdk.service;

import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.GuavaRetryHandler;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserCreateVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserDeleteVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserUpdateVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.UsersApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName UserService.java
 * @description:  会议管理用户 业务处理层
 * @createTime 2021年11月25日 17:52:00
 */
@Service
@Slf4j
public class MMUserService {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private GuavaRetryHandler guavaRetryHandler;

    @Autowired
    private UsersApi usersApi;

    /**
     * 创建用户
     * @param userCreateVo
     * @return
     */
    public boolean createTenCentUser(UserCreateVo userCreateVo){

        //调用腾讯会议API接口创建用户
        HttpResponse httpResponse = usersApi.createUser(userCreateVo);

        if(!httpResponse.getResponseCode().equals(200)){
            taskExecutor.submit(() ->{
                guavaRetryHandler.handler(userCreateVo , new MMUserService() , "save");
            });
        }
        return true;
    }

    /**
     * 修改用户信息
     * @param userUpdateVo
     * @return
     */
    public boolean updateTenCentUser(UserUpdateVo userUpdateVo){
        //调用腾讯会议API接口修改用户
        HttpResponse httpResponse = usersApi.updateUser(userUpdateVo);
        if(!httpResponse.getResponseCode().equals(200)){
            taskExecutor.submit(() ->{
                guavaRetryHandler.handler(userUpdateVo , new MMUserService() , "save");
            });
        }
        return true;
    }

    /**
     * 删除腾讯会议用户（根据userId）
     * @param userId
     * @return
     */
    public boolean deteleTenCentUser(String userId){
        UserDeleteVo userDeleteVo = UserDeleteVo.builder().userId(userId).build();
        HttpResponse httpResponse = usersApi.deleteUser(userDeleteVo);
        if(!httpResponse.getResponseCode().equals(200)){
            taskExecutor.submit(() ->{
                guavaRetryHandler.handler(userDeleteVo , new MMUserService() , "save");
            });
        }
        return true;
    }

    /**
     * 持久化到数据库
     * @param baseMsg
     */
    public void save(BaseMsg baseMsg) {
        System.out.println("保存数据库成功--------------" + baseMsg);
    }
}
