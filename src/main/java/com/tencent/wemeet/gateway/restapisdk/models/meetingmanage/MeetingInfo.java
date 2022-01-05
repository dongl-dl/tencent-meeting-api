package com.tencent.wemeet.gateway.restapisdk.models.meetingmanage;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;

@Slf4j
@Data
public class MeetingInfo implements Serializable{

    private static final long serialVersionUID = 6082941705888852860L;

    private String id;

    private String type;

    private String title;

    private String startDate;

    private String endDate;

    private String startTime;

    private String endTime;

    private String members;

    private String moderator;

    private String creator;

}
