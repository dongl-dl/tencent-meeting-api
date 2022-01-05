package com.tencent.wemeet.gateway.restapisdk.config.guavaretry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName TaskHandlerDispatcher.java
 * @description: 任务调度封装类
 * @createTime 2021年11月26日 09:53:00
 */
@Slf4j
@Component
public class TaskHandlerDispatcher implements ApplicationContextAware ,  RetryHandlerDispatcher{

    //创建Map容器存储处理重试任务的处理类相关信息
    private Map<String , HandlerRetryType> handlers = new ConcurrentHashMap<>();

    /**
     * 调度处理方法
     * @param baseMsg 处理信息
     * @return  true 成功
     *          false  失败
     */
    @Override
    public boolean dispatcher(BaseMsg baseMsg) {
        String content = JSON.toJSONString(baseMsg);
        boolean result = false;
        if(StringUtils.isNotBlank(content)){
            try {
                JSONObject jsonObject = JSON.parseObject(content);
                String retryKey = jsonObject.getString(RetryUtil.HANDLER_KEY_NAME);
                HandlerRetryType handlerRetryType = handlers.get(retryKey);
                if (Objects.isNull(handlerRetryType)) {
                    log.info("handler for [{}] does not existed !", retryKey);
                } else {
                    Object retry = JSON.parseObject(content, handlerRetryType.getRetryType());
                    result = handlerRetryType.getHandler().process((BaseMsg) retry);
                }
            }catch (Exception e){
                log.error("retry [{}] dispatcher failed ---{}" , content , e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        assembleHandlers(applicationContext);
    }

    /**
     * 将处理类信息放入Map集合中
     * @param applicationContext
     */
    private void assembleHandlers(ApplicationContext applicationContext){
        Map<String, RetryProcess> handlerBeans = applicationContext.getBeansOfType(RetryProcess.class);
        handlerBeans.entrySet().forEach(e ->{
            RetryProcess process = e.getValue();
            Class<?> retryType = resolveHandlerRetryType(process);
            handlers.put(RetryUtil.getHandlerKeyName(retryType) , new HandlerRetryType(process , retryType));
            log.info("Retry [{}] , RetryHandler [{}]  assembled " ,retryType.getSimpleName() , process.getClass().getSimpleName());
        });
    }

    /**
     * 使用ResolvableType获取泛型信息
     * @param process
     * @return
     */
    private Class<?> resolveHandlerRetryType(RetryProcess process){
        Class<?> retryType = null;
        ResolvableType[] ifs = ResolvableType.forClass(process.getClass()).getInterfaces();
        for(ResolvableType rt : ifs){
            if(rt.resolve().equals(RetryProcess.class)){
                retryType = rt.getGeneric(0).resolve();
                break;
            }
        }
        return retryType;
    }

    /**
     * 重试类型
     */
    static class HandlerRetryType{

        private RetryProcess handler;

        private Class<?> retryType;

        public HandlerRetryType(RetryProcess handler, Class<?> retryType) {
            super();
            this.handler = handler;
            this.retryType = retryType;
        }

        public RetryProcess getHandler(){
            return handler;
        }

        public void setHandler(RetryProcess handler) {
            this.handler = handler;
        }

        public Class<?> getRetryType() {
            return retryType;
        }

        public void setRetryType(Class<?> retryType) {
            this.retryType = retryType;
        }
    }
}
