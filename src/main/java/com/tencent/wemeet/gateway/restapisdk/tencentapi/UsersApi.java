package com.tencent.wemeet.gateway.restapisdk.tencentapi;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.models.request.AuthorizedUsersVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.QueryAuthorizedUsersVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserCreateVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserDeleteVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserUpdateVo;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryUserInfoList;
import com.tencent.wemeet.gateway.restapisdk.models.response.QueryUserInfoResVo;
import com.tencent.wemeet.gateway.restapisdk.util.TencentMeetingProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author dongliang7
 * 
 * @ClassName UsersServiceImpl.java
 * @description: 腾讯会议API调用 用户业务处理层
 * @createTime 2021年11月24日 10:19:00
 */
@Service
@Slf4j
public class UsersApi {
    private final String userUri = "/v1/users";

    /**
     * 创建用户
     * @param userCreateVo
     */
    public HttpResponse createUser(UserCreateVo userCreateVo) {
        String uri = userUri;
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, userCreateVo);
        return httpResponse;
    }

    /**
     * 通过 userid 更新用户
     * @param userUpdateVo
     * @return
     */
    public HttpResponse updateUser(UserUpdateVo userUpdateVo) {
        String uri = userUri.concat("/").concat(userUpdateVo.getUserid());
        HttpResponse httpResponse = TencentMeetingProcess.putProcess(uri, userUpdateVo);
        return httpResponse;
    }

    /**
     * 查询用户详情
     * @param userId
     * @return
     */
    public QueryUserInfoResVo queryUserInfoDetail(String userId) {
        String uri = userUri.concat("/").concat(userId);
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return JSON.parseObject(httpResponse.getResponseBody(), QueryUserInfoResVo.class);
    }

    /**
     * 查询用户列表
     * @param page
     * @param pageSize
     * @return
     */
    public QueryUserInfoList queryUserInfoList(Integer page, Integer pageSize) {
        String uri = userUri.concat("/list?page=").concat(String.valueOf(page))
                .concat("&page_size=").concat(String.valueOf(pageSize));
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return JSON.parseObject(httpResponse.getResponseBody().toString(), QueryUserInfoList.class);
    }

    /**
     * 通过 userid 删除用户
     * @param userDeleteVo
     * @return
     */
    public HttpResponse deleteUser(UserDeleteVo userDeleteVo) {
        String uri = userUri.concat("/").concat(userDeleteVo.getUserId());
        HttpResponse httpResponse = TencentMeetingProcess.deleteProcess(uri);
        return httpResponse;
    }

    /**
     *  查询个人会议号配置信息
     * @param userId
     * @param instanceId
     * @return
     */
    public String getMeetingPmiConfig(String userId, String instanceId) {
        //GET https://api.meeting.qq.com/v1/pmi-meetings/pmi-config?userid=tester&instanceid=1
        String uri = "/v1/pmi-meetings/pmi-config".concat("?userid=").concat(userId).concat("&instanceid=").concat(instanceId);
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return httpResponse.getResponseBody().toString();
    }

    /**
     * 设置企业成员发起会议的权限
     * @param authorizedUsersVo
     * @return
     */
    public String setUpAuthorizedUsers(AuthorizedUsersVo authorizedUsersVo) {
        //POST https://api.meeting.qq.com/v1/corp-resource/book-meeting/authorized-users
        String uri = "/v1/corp-resource/book-meeting/authorized-users";
        HttpResponse httpResponse = TencentMeetingProcess.postProcess(uri, authorizedUsersVo);
        return httpResponse.getResponseBody().toString();
    }

    /**
     * 查询企业下可发起会议的成员列表
     * @param queryAuthorizedUsersVo
     * @return
     */
    public String queryAuthorizedUsers(QueryAuthorizedUsersVo queryAuthorizedUsersVo) {
        //GET v1/corp-resource/book-meeting/authorized-users?operator_userid={operator_userid}&page={page}&page_size={page_size}
        String uri = "/v1/corp-resource/book-meeting/authorized-users?operator_userid="
                .concat(queryAuthorizedUsersVo.getOperatorUserId())
                .concat("&page=").concat(String.valueOf(queryAuthorizedUsersVo.getPage()))
                .concat("&page_size=").concat(String.valueOf(queryAuthorizedUsersVo.getPageSize()));
        HttpResponse httpResponse = TencentMeetingProcess.getProcess(uri);
        return httpResponse.getResponseBody().toString();
    }
}
