package com.tencent.wemeet.gateway.restapisdk.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName RecurringRule.java
 * @description: 周期性会议 recurring_rule
 * @createTime 2021年12月02日 10:00:00
 */
@Data
public class RecurringRule implements Serializable {

    /**
     * 重复类型，默认值为0
     * 0：每天
     * 1：每周一至周五
     * 2：每周
     * 3：每两周
     * 4：每月
     */
    @JSONField(name = "recurring_type")
    private Integer recurringType;

    /**
     * 结束重复类型，默认值为0。
     * 0：按日期结束重复
     * 1：按次数结束重复
     */
    @JSONField(name = "until_type")
    private Integer untilType;

    /**
     * 结束日期时间戳。
     * 说明：结束日期与第一场会议的开始时间换算成的场次数不能超过以下限制：每天、每个工作日、每周最大支持200场子会议；
     * 每两周、每月最大支持50场子会议，例如：对于每天的重复类型，第一场会议开始时间为1609430400，
     * 则结束日期时间戳不能超过1609430400 + 200 × 24 × 60 × 60 - 1。如未填写，默认为当前日期往后推7天。
     */
    @JSONField(name = "until_date")
    private Integer untilDate;

    /**
     * 限定会议次数。
     * 说明：每天、每个工作日、每周最大支持200场子会议；每两周、每月最大支持50场子会议。如未填写，则默认为7次。
     */
    @JSONField(name = "until_count")
    private Integer untilCount;
}
