package com.tencent.wemeet.gateway.restapisdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
public class TenCentMeetingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenCentMeetingApiApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskExecutor getThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuNum = Runtime.getRuntime().availableProcessors();
        int threadNum = cpuNum * 2 + 1;
        System.out.println(threadNum);
        executor.setCorePoolSize(threadNum-1);
        executor.setMaxPoolSize(threadNum);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("TenCent-Meeting-Server_");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
