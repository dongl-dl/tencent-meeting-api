package com.tencent.wemeet.gateway.restapisdk.service;

import com.alibaba.fastjson.JSON;

import com.tencent.wemeet.gateway.restapisdk.common.BeanUtils;
import com.tencent.wemeet.gateway.restapisdk.config.guavaretry.RetryUtil;
import com.tencent.wemeet.gateway.restapisdk.config.redis.RedisUtil;
import com.tencent.wemeet.gateway.restapisdk.models.meetingmanage.UserInfo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserCreateVo;
import com.tencent.wemeet.gateway.restapisdk.models.request.user.UserUpdateVo;
import com.tencent.wemeet.gateway.restapisdk.util.ClassCompareUtil;
import com.tencent.wemeet.gateway.restapisdk.util.PhoneFormatCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName UserInfoProcessService.java
 * @description:  异步 处理会管平台调用腾讯会议用户创建修改模块
 * @createTime 2021年11月30日 14:18:00
 */
@Service
@Slf4j
public class TenCentUserService {
    @Autowired
    private MMUserService mmUserService;
    @Autowired
    private RedisUtil redis;

    //初始化队列
    private static LinkedBlockingQueue<UserCreateVo> taskQueue = new LinkedBlockingQueue<>();

    static final String USER_PROCESS_LIST_KEY = "TEN_CENT:USER:USER_PROCESS_LIST";

    //线程池参数
    static final Integer CPU_NUM = Runtime.getRuntime().availableProcessors();
    static final Integer THREAD_NUM = CPU_NUM * 2 + 1;
    static final Long KEEP_ALIVE_TIME = 0L;

    //初始化线程池
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(THREAD_NUM - 1 , THREAD_NUM ,
            KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024)
    );

    //线程池是否销毁标识
    private volatile boolean flag;

    /**
     * 处理队列中的用户信息
     */
    @PostConstruct
    public void initDealQueue() {
        flag = true;
        //启动一个线程，去队列中取消息  这里为阻塞队列  阻塞式的取
        threadPoolExecutor.execute(() -> {
            UserCreateVo userCreateVo;
            for (; ; ) {
                if (!flag) {
                    return;
                }
                try {
                    userCreateVo = taskQueue.take();
                    //处理业务数据
                    this.doCreateUserToTenCent(userCreateVo);

                    //处理完，将缓存中的信息删除
                    redis.lRemove(USER_PROCESS_LIST_KEY , 1 , JSON.toJSONString(userCreateVo));
                } catch (Exception e) {
                    log.error("定时任务处理腾讯会议创建人员失败:{}", e);
                }
            }
        });
    }

    /**
     * 只在启动的时候处理因为宕机等异常情况导致未处理的用户信息
     */
    @PostConstruct
    public void initDealCache() {
        flag = true;
        //启动一个线程，去缓存中获取宕机未处理的消息
        threadPoolExecutor.execute(() -> {
            //服务启动后延时10秒
            try {
                TimeUnit.SECONDS.sleep(10);
                if (!flag) {
                    return;
                }
                //获取redis缓存中宕机丢失的处理消息
                List<Object> list = redis.lGet(USER_PROCESS_LIST_KEY, 0, -1);
                log.info("服务启动处理宕机未处理腾讯会议用户信息开始，需要处理的数据集：{}" , list);
                if(!CollectionUtils.isEmpty(list)){
                    list.stream().forEach(user ->{
                        UserCreateVo userCreateVo = JSON.parseObject(user.toString(), UserCreateVo.class);
                        this.doCreateUserToTenCent(userCreateVo);
                        //处理完，将缓存中的信息删除
                        redis.lRemove(USER_PROCESS_LIST_KEY , 1 , JSON.toJSONString(userCreateVo));
                    });
                }
            } catch (InterruptedException e) {
                log.error("服务启动处理宕机丢失腾讯会议用户消息失败， msg:{}" , e.getMessage());
            }
        });
    }

    /**
     * 关闭线程池
     */
    @PreDestroy
    public void destroy() {
        flag = false;
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

    /**
     * 将待处理的用户信息放入队列
     * @param userInfo
     */
    public void putUserInfoToQueue(UserInfo userInfo){
        //1、参数校验
        parameterCalibration(userInfo);
        //2、转换实体类
        UserCreateVo userCreateVo = UserCreateVo.builder().userid(userInfo.getId()).username(userInfo.getUserName())
                .phone(userInfo.getUserMobile()).email(userInfo.getUserInMail()).build();
        try {
        //3、为了防止宕机数据无法恢复 且这里因为某些原因没有引入消息中间件 先使用阻塞队列+缓存 后续可以优化
            redis.lSet(USER_PROCESS_LIST_KEY , JSON.toJSONString(userCreateVo));
        //4、放入队列
            taskQueue.put(userCreateVo);
        } catch (Exception e) {
            log.error("将待处理的用户信息放入缓存或队列失败------ userUpdateVo：{}" , userCreateVo);
        }
    }

    /**
     * 参数校验
     * @param userInfo
     */
    private void parameterCalibration(UserInfo userInfo) {
        //校验是否为空
        if(null == userInfo){
            log.error("同步用户信息到腾讯会议--用户信息为空");
            return;
        }
        //校验用户是否含有手机号且是否合法
        String userMobile = userInfo.getUserMobile();
        if(StringUtils.isBlank(userMobile)){
            log.error("同步用户信息到腾讯会议--用户手机号为空 , userInfo:{}" , userInfo);
            return;
        }
        boolean chinaPhoneLegal = PhoneFormatCheckUtils.isChinaPhoneLegal(userMobile);
        if(!chinaPhoneLegal){
            log.error("同步用户信息到腾讯会议--用户手机号不合法" , userInfo);
            return;
        }
        //校验是否含有邮箱
        String userInMail = userInfo.getUserInMail();
        if(StringUtils.isBlank(userInMail)){
            log.error("同步用户信息到腾讯会议--用户邮箱为空 , userInfo:{}" , userInfo);
            return;
        }
    }

    /**
     * 调用腾讯会议API创建用户
     * @param userVo api调用参数
     */
    public synchronized void doCreateUserToTenCent(UserCreateVo userVo){
        if(null != userVo){
            //TODO 1、根据userId从数据库中获取该用户信息  mapper.getUserInfo(userId);
            UserCreateVo userCreateVo = new UserCreateVo();

            //2、判断用户是否已经创建 根据userId 如果未创建，直接调用创建用户接口
            if(userCreateVo == null){
                mmUserService.createTenCentUser(userVo);
                return;
            }
            //3、如果已经创建 判断用户信息是否被更新 如果已更新  调用腾讯会议修改用户接口
            //比较对象属性值有无差别 true无差别 false有差别
            boolean compare = ClassCompareUtil.compareObject(userVo, userCreateVo);
            if(!compare){
                UserUpdateVo userUpdateVo = BeanUtils.convert(userVo, UserUpdateVo.class);
                //这里要修改 handlerKey 之后处理失败重试 要用到
                userUpdateVo.setHandlerKey(RetryUtil.getHandlerKeyName(userUpdateVo.getClass()));
                mmUserService.updateTenCentUser(userUpdateVo);
            }
        }
    }
}
