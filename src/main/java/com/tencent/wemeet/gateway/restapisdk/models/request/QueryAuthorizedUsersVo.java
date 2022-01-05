package com.tencent.wemeet.gateway.restapisdk.models.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName QueryAuthorizedUsersVo.java
 * @description: 查询企业下可发起会议的成员列表
 * @createTime 2021年11月24日 11:27:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryAuthorizedUsersVo implements Serializable {
    private static final long serialVersionUID = -1L;

    @NonNull
    @JSONField(name = "operator_userid")
    private String operatorUserId;

    @NonNull
    @JSONField(name = "page")
    private Integer page;

    @NonNull
    @JSONField(name = "page_size")
    private Integer pageSize;
}
