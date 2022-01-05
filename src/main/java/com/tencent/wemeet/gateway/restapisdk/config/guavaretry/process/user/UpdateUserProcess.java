package com.tencent.wemeet.gateway.restapisdk.config.guavaretry.process.user;


import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.RetryProcess;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserUpdateVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.UsersApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author dongliang7
 * @projectName guava-retry-demo-master
 * @ClassName CreateUserProcess.java
 * @description: TODO
 * @createTime 2021年11月25日 15:00:00
 */
@Component
@Slf4j
public class UpdateUserProcess implements RetryProcess<UserUpdateVo> {
    @Autowired
    private UsersApi usersApi;

    @Override
    public boolean process(UserUpdateVo userUpdateVo) {
        log.info("处理重试------修改用户");
        HttpResponse httpResponse = usersApi.updateUser(userUpdateVo);

        if(200 == httpResponse.getResponseCode()){
            return true;
        }
        return false;
    }
}
