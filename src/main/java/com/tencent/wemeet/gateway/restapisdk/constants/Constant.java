package com.tencent.wemeet.gateway.restapisdk.constants;

/**
 * @author dongliang7
 *
 * @ClassName Constant.java
 * @description: 常量类
 * @createTime 2021年11月22日 16:25:00
 */
public class Constant {
    /******************************************企业腾讯云配置 开始 ****************************************/
    /**
     * 私有化 url,不同企业可能不同
     */
    public static final String HOST = "https://api.meeting.qq.com";

    /**
     * 企业 APPId
     */
    public static final String APP_ID = "210827502";

    /**
     * 企业 SDKId
     */
    public static final String SDK_ID = "18110133523";

    /**
     * 权限密钥
     */
    public static final String SECRET_ID = "cE9JIYU5OJrnZae4SeJqxSlAquWP0DXW";

    /**
     * 客户端证书
     */
    public static final String SECRET_KEY = "3AyTqYVSxoJ3EUyS4xn5DlvCOPXgLYxeE3VXXC1z9cPOJxqk";

    /******************************************企业腾讯云配置 结束 ****************************************/


    /**
     * 空字符串
     */
    public static final String NULL_CHAR_STR = "";

    /**
     * 问号
     */
    public static final String QUESTION_MARK = "?";

    /**
     * 斜线
     */
    public static final String SLASH = "/";

    /**
     * &符号
     */
    public static final String CONNENT = "&";

    /**
     * 等于号
     */
    public static final String EQUAL_SIGN = "=";


    /********************************************* 腾讯会议创建会议参数 *******************************************/

    public static final Integer ZORE = 0;

    public static final String MEETING_RESPONSE = "TENCENT:MEETING:RESPONSE:";


}
