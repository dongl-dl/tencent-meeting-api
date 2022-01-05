package com.tencent.wemeet.gateway.restapisdk.models.request.user;

import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dongliang7
 *
 * @ClassName UserUpdateVo.java
 * @description: 新增用户
 * @createTime 2021年11月16日 18:43:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateVo extends BaseMsg implements Serializable {

    private String email;

    private String phone;

    private String username;

    private String userid;

    private static final long serialVersionUID = -1L;
}
