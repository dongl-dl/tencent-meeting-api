package com.tencent.wemeet.gateway.restapisdk.common;

import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.RetryUtil;

/**
 * @author dongliang7
 * @projectName guava-retry-demo-master
 * @ClassName BaseMsg.java
 * @description: 腾讯会议api接口重试入参基类
 * @createTime 2021年11月25日 13:57:00
 */
public abstract class BaseMsg {

    /**
     *  handlerKey 根据key 查找重试处理类
     */
    private String handlerKey = RetryUtil.getHandlerKeyName(this.getClass());
    /**
     * 重试时间
     */
    protected long msgTime = System.currentTimeMillis();

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public String getHandlerKey() {
        return handlerKey;
    }

    public void setHandlerKey(String handlerKey) {
        this.handlerKey = handlerKey;
    }
}
