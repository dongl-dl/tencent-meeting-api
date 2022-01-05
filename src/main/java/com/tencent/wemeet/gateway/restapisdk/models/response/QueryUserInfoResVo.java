package com.tencent.wemeet.gateway.restapisdk.models.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dongliang7
 *
 * @ClassName QueryUserInfoResVo.java
 * @description: 查询用户详情响应体
 * @createTime 2021年11月19日 14:05:00
 */
@Data
public class QueryUserInfoResVo implements Serializable {
    private String email;

    private String phone;

    private String username;

    private String userid;

    @JSONField(name = "update_time")
    private String updateTime;

    private String area;

    private String status;
}
