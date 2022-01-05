package com.tencent.wemeet.gateway.restapisdk.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName UserInfoVo.java
 * @description: 用户详情 实体类
 * @createTime 2021年11月17日 11:15:30
 */
@Data
public class UserInfoVo implements Serializable {

    private String email;

    private String phone;

    private String username;

    private String userid;

    @JSONField(name = "update_time")
    private String updateTime;

    private String area;
    @JSONField(name = "avatar_url")
    private String avatarUrl;

    private String status;
}
