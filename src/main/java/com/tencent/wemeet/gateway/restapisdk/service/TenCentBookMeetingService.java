package com.tencent.wemeet.gateway.restapisdk.service;

import com.alibaba.fastjson.JSON;
import com.tencent.wemeet.gateway.restapisdk.config.redis.RedisUtil;
import com.tencent.wemeet.gateway.restapisdk.models.MediaSetting;
import com.tencent.wemeet.gateway.restapisdk.models.RecurringRule;
import com.tencent.wemeet.gateway.restapisdk.models.UserVo;
import com.tencent.wemeet.gateway.restapisdk.models.meetingmanage.MeetingInfo;
import com.tencent.wemeet.gateway.restapisdk.models.meetingmanage.UserInfo;
import com.tencent.wemeet.gateway.restapisdk.models.request.meeting.CreateMeetingReqVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName UserInfoProcessService.java
 * @description:  异步 处理会管平台调用腾讯会议会议创建修改模块
 * @createTime 2021年11月30日 14:18:00
 */
@Service
@Slf4j
public class TenCentBookMeetingService {
    @Autowired
    private MMMeetingService mmMeetingService;
    @Autowired
    private RedisUtil redis;

    //初始化队列
    private static LinkedBlockingQueue<CreateMeetingReqVo> taskQueue = new LinkedBlockingQueue<>();

    static final String MEETING_PROCESS_LIST_KEY = "TEN_CENT:MEETING:MEETING_PROCESS_LIST";

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
     * 处理队列中的会议信息
     */
    @PostConstruct
    public void initDealQueue() {
        flag = true;
        //启动一个线程，去队列中取消息  这里为阻塞队列  阻塞式的取
        threadPoolExecutor.execute(() -> {
            CreateMeetingReqVo createMeetingReqVo;
            for (;;) {
                if (!flag) {
                    return;
                }
                try {
                    createMeetingReqVo = taskQueue.take();
                    //处理业务数据
                    this.doCreateMeetingToTenCent(createMeetingReqVo);

                    //处理完，将缓存中的信息删除
                    redis.lRemove(MEETING_PROCESS_LIST_KEY , 1 , JSON.toJSONString(createMeetingReqVo));
                } catch (Exception e) {
                    log.error("定时任务处理腾讯会议创建会议失败:{}", e);
                }
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
                List<Object> list = redis.lGet(MEETING_PROCESS_LIST_KEY, 0, -1);
                log.info("服务启动处理宕机未处理腾讯会议会议信息开始，需要处理的数据集：{}" , list);
                if(!CollectionUtils.isEmpty(list)){
                    list.stream().forEach(user ->{
                        CreateMeetingReqVo createMeetingReqVo = JSON.parseObject(user.toString(), CreateMeetingReqVo.class);
                        this.doCreateMeetingToTenCent(createMeetingReqVo);
                        //处理完，将缓存中的信息删除
                        redis.lRemove(MEETING_PROCESS_LIST_KEY , 1 , JSON.toJSONString(createMeetingReqVo));
                    });
                }
            } catch (InterruptedException e) {
                log.error("服务启动处理宕机丢失腾讯会议会议消息失败， msg:{}" , e.getMessage());
            }
        });
    }

    /**
     * 将待处理的会议信息放入队列
     * @param meetingInfo
     */
    public void putMeetingInfoToQueue(MeetingInfo meetingInfo){
        //1、参数校验
        parameterCalibration(meetingInfo);
        //2、构建会议预定入参
        CreateMeetingReqVo createMeetingReqVo = buildBookMeetingParams(meetingInfo);
        //3、为了防止宕机数据无法恢复 且这里因为某些原因没有引入消息中间件 先使用阻塞队列+缓存 后续可以优化
        redis.lSet(MEETING_PROCESS_LIST_KEY , JSON.toJSONString(createMeetingReqVo));

        //4、将待处理的会议信息放入队列
        try {
            taskQueue.put(createMeetingReqVo);
        } catch (InterruptedException e) {
           log.error("将会议信息放入队列失败 ， msg:{} , editMeetingReqVo:{}" , e.getMessage() , createMeetingReqVo);
        }
    }

    private CreateMeetingReqVo buildBookMeetingParams(MeetingInfo meetingInfo) {
        Integer meetingType = StringUtils.equals(meetingInfo.getType() ,"3") ? 1 : 0;
        //会议创建人
        String creator = meetingInfo.getCreator();
        //TODO 通过userName 获取创建人 userId
        String createUserId = "110000";

        //主持人
        String moderator = meetingInfo.getModerator();
        UserVo userVo = UserVo.builder().nickName(moderator).userId("").isAnonymous(false).build();
        List<UserVo> hosts = new ArrayList<>();
        hosts.add(userVo);

        //邀请人
        String members = meetingInfo.getMembers();
        String[] membersStrs = members.split(",");
        List<UserVo> invitees = new ArrayList<>();
        //TODO 通过userName集合获取userId集合
        Arrays.stream(membersStrs).forEach(member ->{
            UserVo invitee = UserVo.builder().nickName(member).userId(member).isAnonymous(false).build();
            invitees.add(invitee);
        });

        //会议时间
        ImmutableTriple<DateTime, DateTime, Boolean> meetingTime = buildMeetingTime(meetingInfo);
        long meetingStart = meetingTime.getLeft().getMillis()/1000;
        long meetingEnd = meetingTime.getMiddle().getMillis()/1000;

        //媒体配置
        MediaSetting settings = new MediaSetting();

        //周期会议配置
        RecurringRule recurringRule = new RecurringRule();

        //构建腾讯会议创建实体参数
        CreateMeetingReqVo createMeetingReqVo = CreateMeetingReqVo.Builder.create().withUserId(createUserId)
                .withInstanceId(1).withSubject(meetingInfo.getTitle())
                .withType(0).withHosts(hosts).withInvitees(invitees).withStartTime(String.valueOf(meetingStart))
                .withEndTime(String.valueOf(meetingEnd)).withPassword("888888").withSettings(settings)
                .withMeetingType(meetingType).withRecurringRule(recurringRule).build();
        return createMeetingReqVo;
    }

    /**
     * 参数检验
     * @param meetingInfo
     */
    private void parameterCalibration(MeetingInfo meetingInfo) {
        Optional.ofNullable(meetingInfo).orElseThrow(()-> new RuntimeException("会议室信息为空"));

        //会议时间间隔
        ImmutableTriple<DateTime, DateTime, Boolean> meetingTime = buildMeetingTime(meetingInfo);
        Duration duration = new Duration(meetingTime.getLeft(), meetingTime.getMiddle());
        long millis = duration.getMillis();
        if(!meetingTime.right){
            log.error("创建腾讯会议------会议开始时间大于会议结束时间 ,meetingInfo:{}" , meetingInfo);
            return;
        }
    }

    /**
     * 构建会议时间 并比较时间大小
     * @param meetingInfo
     * @return
     */
    private ImmutableTriple<DateTime , DateTime, Boolean> buildMeetingTime(MeetingInfo meetingInfo){
        //拼接日期时间
        String meetingStartStr = String.format("%sT%s" , meetingInfo.getStartDate() , meetingInfo.getStartTime());
        String meetingEndStr = String.format("%sT%s" , meetingInfo.getEndDate() , meetingInfo.getEndTime());
        DateTime meetingStart = new DateTime(meetingStartStr);
        DateTime meetingEnd = new DateTime(meetingEndStr);
        //比较开始日期和结束日期
        boolean startBeforeEnd = meetingStart.isBefore(meetingEnd);
        ImmutableTriple<DateTime , DateTime, Boolean> meetingTime = ImmutableTriple.of(meetingStart , meetingEnd , startBeforeEnd);
        return meetingTime;
    }

    /**
     * 调用腾讯会议API创建用户
     * @param userVo api调用参数
     */
    public synchronized void doCreateMeetingToTenCent(CreateMeetingReqVo createMeetingReqVo){
        mmMeetingService.createTenCentMeeting(createMeetingReqVo);
    }
}
