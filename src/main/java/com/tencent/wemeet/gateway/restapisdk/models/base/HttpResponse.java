package com.tencent.wemeet.gateway.restapisdk.models.base;

import lombok.*;
import okhttp3.ResponseBody;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName HttpResponse.java
 * @description: http请求响应结果集
 * @createTime 2021年11月25日 18:09:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class HttpResponse {

    /**
     * 响应结果
     */
    private String responseBody;

    /**
     * 响应码
     */
    private Integer responseCode = 400;

}
