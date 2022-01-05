package com.tencent.wemeet.gateway.restapisdk.models.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 * 
 * @ClassName AuthorizedUsersVo.java
 * @description: 设置企业成员发起会议的权限
 * @createTime 2021年11月24日 10:56:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorizedUsersVo implements Serializable {

    /**
     * 操作者用户 ID，必须为企业下具有操作资源权限的注册用户
     */
    @JSONField(name = "operator_userid")
    @NonNull
    private String operatorUserId;

    /**
     * 发起会议类型：
     * 0：企业下全部用户可发起会议
     * 1：企业下部分用户可发起会议
     */
    @NonNull
    private Integer type;

    /**
     * 企业成员 userid 数组，必须为企业下的注册用户，仅当 type 为1时此字段有效。
     */
    private List<String> users;

    private static final long serialVersionUID = -1L;
}
