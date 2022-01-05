package com.tencent.wemeet.gateway.restapisdk.models.meetingmanage;


import lombok.Data;

import java.io.Serializable;
import java.util.Locale;

@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 5334790730065804854L;

    private String id;

    private String userName;

    private transient String password;


    private transient String salt;


    private String userShortName;


    private String userNotesName;


    private String userDeptPath;



    private String userDeptPathEn;

    /**
     * 图片id
     */
    private String userPictureFileId;

    /** 人脸照片路径*/
    private String userPicture;

    /** 手机号码 */
    private String userMobile;

    private String userInMail;

    private int status;

    private int deleteFlag;

    private String userEmpId;

    /**
     * 人员类型
     */
    private String userType;

    private String companyName;

    private String locationCode;

    private String areaId;

    /**
     * 人员卡号
     */
    private String cardNo;

    private int approver;

    private int permission;

    private String area;

    private String areaName;

    private String deptCodePath;

    private String locationName;

    private Locale language;

    /**
     * 系统管理员
     */
    private boolean systemAdmin;

    /**
     * 接待会议室预定
     */
    private boolean customerBooking;

    /**
     * 专用会议室预定
     */
    private boolean reservedBooking;

    /**
     * 普通会议查询
     */
    private boolean normalSearch;

    /**
     * 自定义会议查询
     */
    private boolean customizeSearch;

    /**
     * 接待会议查询
     */
    private boolean customerSearch;

    /**
     * 专用会议查询
     */
    private boolean reservedSearch;

    /**
     * 区域配置
     */
    private boolean areaConfig;

    /**
     * 会议室配置
     */
    private boolean roomConfig;

    /**
     * 人员权限控制
     */
    private boolean userControl;

}
