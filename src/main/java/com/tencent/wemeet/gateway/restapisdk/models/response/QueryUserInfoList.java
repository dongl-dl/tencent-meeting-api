package com.tencent.wemeet.gateway.restapisdk.models.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.tencent.wemeet.gateway.restapisdk.models.UserInfoVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongliang7
 *
 * @ClassName QueryUserInfoList.java
 * @description: 获取用户详情列表响应体
 * @createTime 2021年11月19日 14:05:00
 */
@Data
public class QueryUserInfoList implements Serializable {

    @JSONField(name = "total_count")
    private Integer totalCount;

    @JSONField(name = "current_size")
    private Integer currentSize;

    @JSONField(name = "current_page")
    private Integer currentPage;

    @JSONField(name = "page_size")
    private Integer pageSize;

    private List<UserInfoVo> users;


}
