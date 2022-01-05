package com.tencent.wemeet.gateway.restapisdk.config.guavaretry;

import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName RetryHandlerDispatcher.java
 * @description: 重试处理器调度器
 * @createTime 2021年11月26日 09:56:00
 */
public interface RetryHandlerDispatcher {

    /**
     * 实现处理器调度 ，并对重试任务进行处理
     * @param baseMsg
     * @return
     */
    boolean dispatcher(BaseMsg baseMsg);
}
