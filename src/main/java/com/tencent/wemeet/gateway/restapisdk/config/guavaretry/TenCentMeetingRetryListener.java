package com.tencent.wemeet.gateway.restapisdk.config.guavaretry;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName MyRetryListener.java
 * @description: 封装自定义重试监听类
 * @createTime 2021年11月25日 10:44:00
 */
@Slf4j
public class TenCentMeetingRetryListener<T extends BaseMsg,R> implements RetryListener {

    //处理参数
    private T processBody;

    //处理类
    private R processService;

    //处理方法
    private String method;

    //处理次数
    private Integer processTimes;

    public TenCentMeetingRetryListener(T processBody, R processService, String method , Integer processTimes) {
        this.processBody = processBody;
        this.processService = processService;
        this.method = method;
        this.processTimes = processTimes;
    }

    @Override
    public <Boolean> void onRetry(Attempt<Boolean> attempt) {
        // 第几次重试,(注意:第一次重试其实是第一次调用)
        long attemptNumber = attempt.getAttemptNumber();

        // 距离第一次重试的延迟
        long delaySinceFirstAttempt = attempt.getDelaySinceFirstAttempt();

        // 重试结果: 是异常终止, 还是正常返回
        boolean hasException = attempt.hasException();
        boolean hasResult = attempt.hasResult();

        // 是什么原因导致异常
        String resultStr = "";
        if (attempt.hasException()) {
            resultStr = attempt.getExceptionCause().toString();
        } else {
            // 正常返回时的结果
            resultStr = String.valueOf(attempt.getResult());
        }

        log.info("[retry]time={} , delay={} , hasException={} , hasResult={} , result={}]" ,
                attemptNumber , delaySinceFirstAttempt , hasException , hasResult , resultStr);

        //处理失败达到一定次数时，持久化到数据库
        if(attemptNumber == processTimes && "false".equals(resultStr)){
            log.error("没成功  还是入库吧");
            try {
                Method method = processService.getClass().getMethod(this.method, BaseMsg.class);
                method.invoke(processService, processBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}