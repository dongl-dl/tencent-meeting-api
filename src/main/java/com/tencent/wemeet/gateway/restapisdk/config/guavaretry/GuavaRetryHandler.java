package com.tencent.wemeet.gateway.restapisdk.config.guavaretry;

import com.github.rholder.retry.*;
import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author dongliang7
 * @projectName guava-retry-demo-master
 * @ClassName GuavaRetryConfig.java
 * @description: guava 重试规则 工具类
 * @createTime 2021年11月25日 13:46:00
 */
@Component
@Slf4j
public class GuavaRetryHandler{
    @Autowired
    private TaskHandlerDispatcher taskHandlerDispatcher;

    public <T> boolean handler(BaseMsg baseMsg , T t , String method) {
        // 定义重试规则
        Retryer<Boolean> retry = RetryerBuilder.<Boolean>newBuilder()
                .retryIfException()
                // 运行时异常时
                .retryIfRuntimeException()
                // call方法返回true时重试
                .retryIfResult(ab -> Objects.equals(ab, false))
                // 10秒后重试
                .withWaitStrategy(WaitStrategies.fixedWait(5, TimeUnit.SECONDS))
                // 重试n次，超过次数就...
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                // 可以自定义监听器
                .withRetryListener(new TenCentMeetingRetryListener<>(baseMsg , t , method , 5))
                .build();

        // 调用重试任务
        try {
            retry.call(() -> {
                try {
                    boolean process = taskHandlerDispatcher.dispatcher(baseMsg);
                    return process;
                } catch (Exception e) {
                    System.out.println("[" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + "]尝试重新发送：" + e.getMessage());
                    return false;
                }
            });
        } catch (ExecutionException | RetryException e) {
            log.error("失败重试达到一定次数仍未成功， [processBody:{} , msg:{}]" , baseMsg , e.getMessage());
        }
        return true;
    }
}
