package com.tencent.wemeet.gateway.restapisdk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tencent.wemeet.gateway.restapisdk.constants.Constant;
import com.tencent.wemeet.gateway.restapisdk.models.HeadVo;
import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import com.tencent.wemeet.gateway.restapisdk.okhttp.OKHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * @author dongliang7
 * 
 * @ClassName TencentMeetingProcess.java
 * @description: 请求方法及参数封装
 * @createTime 2021年11月22日 14:39:00
 */
@Slf4j
public class TencentMeetingProcess {

    /**
     * POST 请求封装
     * @param uri 请求路径
     * @param requestBody 请求体
     * @param <T>
     * @return 返回接口调用结果
     */
    public static<T> HttpResponse postProcess(String uri ,T requestBody){
        String url = Constant.HOST.concat(uri);
        String body = JSON.toJSONString(requestBody);
        HeadVo header = RequestHeaderUtil.getHeader(body, HttpMethod.POST, uri);
        Map<String, String> headMap = fromHeadVo(header);
        log.info("tencentMeeting the http post url ---" + url);
        log.info("request tencentMeeting header --->" + JSON.toJSONString(headMap));
        HttpResponse httpResponse = OKHttpClient.httpPostJson(url, headMap, body);
        return httpResponse;
    }

    /**
     * PUT 请求封装
     * @param uri 请求路径
     * @param requestBody 请求体
     * @param <T>
     * @return 返回接口调用结果
     */
    public static<T> HttpResponse putProcess(String uri ,T requestBody){
        String url = Constant.HOST.concat(uri);
        String body = JSON.toJSONString(requestBody);
        HeadVo header = RequestHeaderUtil.getHeader(body, HttpMethod.PUT, uri);
        Map<String, String> headMap = fromHeadVo(header);
        log.info("tencentMeeting the http put url ---" + url);
        log.info("request tencentMeeting header --->" + JSON.toJSONString(headMap));
        HttpResponse httpResponse = OKHttpClient.httpPutJson(url, headMap, body);
        return httpResponse;
    }

    /**
     * GET 请求封装
     * @param uri 请求路径
     * @return 返回接口调用结果
     */
    public static HttpResponse getProcess(String uri){
        String url = Constant.HOST.concat(uri);
        HeadVo header = RequestHeaderUtil.getHeader(Constant.NULL_CHAR_STR, HttpMethod.GET, uri);
        Map<String, String> headMap = fromHeadVo(header);
        log.info("tencentMeeting the http get url ---" + url);
        log.info("request tencentMeeting header --->" + JSON.toJSONString(headMap));
        HttpResponse httpResponse = OKHttpClient.httpGet(url, headMap);
        return httpResponse;
    }



    /**
     * DELETE 请求封装
     * @param uri 请求路径
     * @return 返回接口调用结果
     */
    public static HttpResponse deleteProcess(String uri){
        String url = Constant.HOST.concat(uri);
        HeadVo header = RequestHeaderUtil.getHeader(Constant.NULL_CHAR_STR, HttpMethod.DELETE, uri);
        Map<String, String> headMap = fromHeadVo(header);
        log.info("tencentMeeting the http delete url ---" + url);
        log.info("request tencentMeeting header --->" + JSON.toJSONString(headMap));
        HttpResponse httpResponse = OKHttpClient.httpDelete(url, headMap);
        return httpResponse;
    }

    /**
     * 将实体类转换成Map集合
     * @param headVo 请求head公参实体
     * @return 返回map集合
     */
    public static Map<String, String> fromHeadVo(HeadVo headVo) {
        return JSON.parseObject(JSON.toJSONString(headVo), new TypeReference<Map<String, String>>() {});
    }
}
