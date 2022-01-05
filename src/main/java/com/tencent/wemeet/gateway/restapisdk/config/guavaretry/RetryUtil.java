package com.tencent.wemeet.gateway.restapisdk.config.guavaretry;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName RetryUtil.java
 * @description: 获取处理 bean 示例名称
 * @createTime 2021年11月26日 10:52:00
 */
public class RetryUtil {

    public static final String HANDLER_KEY_NAME = "handlerKey";

    public static String getHandlerKeyName(Class<?> retryType){
        return retryType.getSimpleName();
    }

}
