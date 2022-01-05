package com.tencent.wemeet.gateway.restapisdk.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName UserVo.java
 * @description: 用户对象 UserObj 实体类
 * @createTime 2021年11月17日 14:05:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo implements Serializable {

    @NonNull
    @JSONField(name = "userid")
    private String userId;

    @JSONField(name = "is_anonymous")
    private Boolean isAnonymous;

    @JSONField(name = "nick_name")
    private String nickName;

    private static final long serialVersionUID = -1L;
}
