package com.tencent.wemeet.gateway.restapisdk.client;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.models.UserInfoVo;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.base.TenCentSdkError;
import com.tencent.wemeet.gateway.restapisdk.models.request.AuthorizedUsersVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.QueryAuthorizedUsersVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserCreateVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserDeleteVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryUserInfoList;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryUserInfoResVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserUpdateVo;
import com.tencent.wemeet.gateway.restapisdk.tencentapi.UsersApi;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dongliang7
 * 
 * @ClassName UserController.java
 * @description: 腾讯会议API调用 用户控制层
 * @createTime 2021年11月19日 10:47:00
 */
@RestController
@RequestMapping("/usersClient")
public class UsersClient {

    @Autowired
    private UsersApi usersService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     *  创建用户
     * @param userCreateVo
     */
    @PostMapping("/v1/createUser")
    public HttpResponse createUser(@RequestBody UserCreateVo userCreateVo){
        HttpResponse httpResponse = usersService.createUser(userCreateVo);
        String responseBody = httpResponse.getResponseBody();
        Integer responseCode = httpResponse.getResponseCode();
        if(!responseCode.equals(200)){
            TenCentSdkError tenCentSdkError = JSON.parseObject(httpResponse.getResponseBody(), TenCentSdkError.class);
            System.out.println(tenCentSdkError);
        }

        return httpResponse;
    }

    /**
     * 通过 userid 更新用户
     * @param userUpdateVo
     * @return
     */
    @PostMapping("/v1/updateUser")
    public HttpResponse updateUser(@RequestBody UserUpdateVo userUpdateVo){
        return usersService.updateUser(userUpdateVo);
    }

    /**
     * 查询用户详情
     * @param userId
     * @return
     */
    @GetMapping("/v1/queryUserInfoDetail/{userId}")
    public QueryUserInfoResVo queryUserInfoDetail(@PathVariable("userId") String userId){
        return usersService.queryUserInfoDetail(userId);
    }

    /**
     * 查询用户列表
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/v1/queryUserInfoList/{page}/{pageSize}")
    public QueryUserInfoList queryUserInfoList(@PathVariable("page") Integer page,@PathVariable("pageSize")  Integer pageSize){
        return usersService.queryUserInfoList(page , pageSize);
    }

    /**
     * 通过 userid 删除用户
     * @param userId
     * @return
     */
    @GetMapping("/v1/deleteUser/{userId}")
    public HttpResponse deleteUser(@PathVariable("userId") String userId){
        UserDeleteVo userDeleteVo = UserDeleteVo.builder().userId(userId).build();
        return usersService.deleteUser(userDeleteVo);
    }

    /**
     * 查询个人会议号配置信息
     * @param userId
     * @return
     */
    @GetMapping("/v1/getMeetingPmiConfig/{userId}/{instanceId}")
    public String getMeetingPmiConfig(@PathVariable("userId") String userId , @PathVariable("instanceId") String instanceId){
        String meetingPmiConfig = usersService.getMeetingPmiConfig(userId ,instanceId);
        return meetingPmiConfig;
    }

    /**
     * 设置企业成员发起会议的权限
     * @param authorizedUsersVo
     * @return
     */
    @PostMapping("/v1/authorizedUsers")
    public String authorizedUsers(@RequestBody AuthorizedUsersVo authorizedUsersVo){
        String authorizedUsers = usersService.setUpAuthorizedUsers(authorizedUsersVo);
        return authorizedUsers;
    }

    /**
     * 查询企业下可发起会议的成员列表
     * @param queryAuthorizedUsersVo
     * @return
     */
    @PostMapping("/v1/queryAuthorizedUsers")
    public String queryAuthorizedUsers(@RequestBody QueryAuthorizedUsersVo queryAuthorizedUsersVo){
        String authorizedUsers = usersService.queryAuthorizedUsers(queryAuthorizedUsersVo);
        return authorizedUsers;
    }


    /**
     * 批量移除腾讯会议平台用户
     */
    @GetMapping("/moveTenCentUsers")
    public void moveTenCentUsers(){
        QueryUserInfoList queryUserInfoList = usersService.queryUserInfoList(1, 20);

        if(null != queryUserInfoList && null != queryUserInfoList.getPageSize() && queryUserInfoList.getPageSize() > 0){
            Integer pageSize = queryUserInfoList.getPageSize();
            Integer totalCount = queryUserInfoList.getTotalCount();
            for(int i = 2; i <= totalCount/pageSize ;i++){
                QueryUserInfoList queryUserInfoList1 = usersService.queryUserInfoList(i, 20);
                this.deleteTenCentUser(queryUserInfoList1);
            }
        }
    }

    /**
     * 删除腾讯会议平台用户
     * @param queryUserInfoList1
     */
    private void deleteTenCentUser(QueryUserInfoList queryUserInfoList1) {
        List<UserInfoVo> users = queryUserInfoList1.getUsers();
        if(CollectionUtils.isNotEmpty(users)){
            users.stream().forEach(user ->{
                taskExecutor.submit(() ->{
                    UserDeleteVo userDeleteVo = UserDeleteVo.builder().userId(user.getUserid()).build();
                    usersService.deleteUser(userDeleteVo);
                });
            });
        }
    }
}
