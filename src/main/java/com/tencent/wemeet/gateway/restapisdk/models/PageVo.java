package com.tencent.wemeet.gateway.restapisdk.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName PageVo.java
 * @description: 分页参数 请求入参
 * @createTime 2021年11月24日 11:23:00
 */
@Data
public class PageVo implements Serializable {
    private static final long serialVersionUID  = -1L;

    @NonNull
    @JSONField(name = "page")
    private Integer page;

    @NonNull
    @JSONField(name = "page_size")
    private Integer pageSize;
}
