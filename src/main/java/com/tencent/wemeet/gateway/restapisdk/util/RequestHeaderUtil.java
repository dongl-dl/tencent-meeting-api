package com.tencent.wemeet.gateway.restapisdk.util;

import com.tencent.wemeet.gateway.restapisdk.constants.Constant;
import com.tencent.wemeet.gateway.restapisdk.models.HeadVo;
import com.tencent.wemeet.gateway.restapisdk.models.SignVo;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

/**
 * @author dongliang7
 * 
 * @ClassName RequestHeaderUtil.java
 * @description: 请求头参数封装工具类
 * @createTime 2021年11月19日 13:44:00
 */
public class RequestHeaderUtil {
    /**
     * 组建 header
     * @param body 请求体
     * @param httpMethod 请求方法 GET POST .....
     * @param uri 请求路径
     * @return 返回请求头
     */
    public static HeadVo getHeader(String body, HttpMethod httpMethod, String uri) {
        // 1、随机正整数字符串
        String randomStr = String.valueOf(new Random().nextInt(999999));
        // 2、获取时间戳字符串
        long seconds = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        String dateTimeStr = String.valueOf(seconds);
        //3、构建签名对象
        SignVo signVo = SignVo.Builder.create().withNonce(randomStr).withSecretId(Constant.SECRET_ID)
                .withTimestamp(dateTimeStr).withSecretKey(Constant.SECRET_KEY)
                .withBody(body).withMethod(httpMethod.name()).withUri(uri).build();
        //4、获取签名字符串
        String sign = SignUtil.getSign(signVo);
        HeadVo headVo = HeadVo.Builder.create().withNonce(signVo.getNonce()).withTimestamp(signVo.getTimestamp())
                .withAppId(Constant.APP_ID).withSdkId(Constant.SDK_ID).withKey(Constant.SECRET_ID)
                .withSignature(sign).withRegistered("1").build();
        return headVo;
    }
}
