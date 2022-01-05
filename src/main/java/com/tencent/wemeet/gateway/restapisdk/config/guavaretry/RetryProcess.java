package com.tencent.wemeet.gateway.restapisdk.config.guavaretry;

/**
 * @author dongliang7
 * @projectName guava-retry-demo-master
 * @ClassName RetryProcess.java
 * @description:  任务重试处理接口
 * @createTime 2021年11月25日 13:48:00
 */

import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;

/**
 *
 * @param <M> BaseMsg 子类
 */
public interface RetryProcess<M extends BaseMsg> {

    /**
     * 任务处理方法
     * @param msg 处理任务体
     * @return
     *        false 处理失败
     *        true  处理成功
     */
    boolean process(M msg);
}
