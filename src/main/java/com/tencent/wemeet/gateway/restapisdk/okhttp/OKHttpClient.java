package com.tencent.wemeet.gateway.restapisdk.okhttp;

import com.tencent.wemeet.gateway.restapisdk.models.base.HttpResponse;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author dongliang7
 * 
 * @ClassName OKHttpClient.java
 * @description: OkHttpClient封装三方接口调用工具
 * @createTime 2021年11月19日 10:05:00
 */
public class OKHttpClient {

    private static final Logger log = LoggerFactory.getLogger(OKHttpClient.class);

    private static final String JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
    //创建 OkHttpClient 不进行SSL（证书）验证
    private static final OkHttpClient okHttpClient = SSLSocketClient.getUnsafeOkHttpClient();


    /**
     * Http GET 请求 不带请求头
     * @param url 请求路径
     * @return 返回请求结果
     */
    public static HttpResponse httpGet(String url) {
        if(StringUtils.isBlank(url)){
            log.error("http GET 请求过程中url为null!");
            return new HttpResponse();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        HttpResponse httpResponse = new HttpResponse();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            httpResponse.setResponseBody(responseBody.string());
            httpResponse.setResponseCode(response.code());
            if (response.code() == 200) {
                log.info("http GET 请求成功; [url={}]", url);
            } else {
                log.warn("Http GET 请求失败; [errorCode = {} , url={}]," +
                        "responseBody={}]", response.code(), url, httpResponse.getResponseBody());
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return httpResponse;
    }

    /**
     * Http GET 请求 带请求头
     * @param url 请求路径
     * @param headers 请求头
     * @return 返回请求结果
     */
    public static HttpResponse httpGet(String url, Map<String, String> headers) {
        if (CollectionUtils.isEmpty(headers)) {
            return httpGet(url);
        }
        Request.Builder builder = new Request.Builder();
        headers.forEach(builder::header);
        Request request = builder.get().url(url).build();
        HttpResponse httpResponse = new HttpResponse();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            httpResponse.setResponseBody(responseBody.string());
            httpResponse.setResponseCode(response.code());
            if (response.code() == 200) {
                log.info("Http GET 请求成功; [url={},responseBody={}]", url, httpResponse.getResponseBody());
            } else {
                log.warn("Http GET 请求失败; [errorCode = {} , url={}, " +
                        "responseBody={}]", response.code(), url, httpResponse.getResponseBody());
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http GET 请求失败,url:" + url, e);
        }
        return httpResponse;
    }

    /**
     * Http POST请求 不带请求头
     * @param url  请求路径
     * @param json 请求体 body
     * @return 返回请求结果
     */
    public static HttpResponse httpPostJson(String url, String json) {
        if(StringUtils.isBlank(url)){
            log.error("Http POST 请求过程中url为null!");
            return new HttpResponse();
        }
        MediaType mediaType = MediaType.parse(JSON_CHARSET_UTF_8);
        RequestBody body = RequestBody.create(json,  mediaType);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.post(body).build();
        HttpResponse httpResponse = new HttpResponse();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            httpResponse.setResponseBody(responseBody.string());
            httpResponse.setResponseCode(response.code());
            if (response.code() == 200) {
                log.info("Http POST 请求成功; [url={}, requestContent={}]", url, json);
            } else {
                log.warn("Http POST 请求失败; [ errorCode = {}, url={}, " +
                        "param={}]", response.code(), url, json);
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http请求失败,url:" + url, e);
        }
        return httpResponse;
    }

    /**
     * HTTP POST请求 带请求头
     * @param url 请求路径
     * @param headers 请求头
     * @param json 请求体 body
     * @return 返回请求结果
     */
    public static HttpResponse httpPostJson(String url, Map<String, String> headers, String json) {
        //校验是否需要请求头 ， 如果需要直接执行 ， 如果不需要则调用
        if (CollectionUtils.isEmpty(headers)) {
            return httpPostJson(url, json);
        }
        MediaType mediaType = MediaType.parse(JSON_CHARSET_UTF_8);
        RequestBody body = RequestBody.create(json,mediaType);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        headers.forEach(requestBuilder::addHeader);
        Request request = requestBuilder.post(body).build();
        HttpResponse httpResponse = new HttpResponse();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            httpResponse.setResponseBody(responseBody.string());
            httpResponse.setResponseCode(response.code());
            if (response.code() == 200) {
                log.info("Http POST 请求成功; [url={},responseBody={}]", url, httpResponse.getResponseBody());
            } else {
                log.warn("Http POST 请求失败 ; [ errorCode = {}, url={},"
                        + " param={},responseBody ={}]", response.code(), url, json, httpResponse.getResponseBody());
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http POST 请求失败,url:" + url, e);
        }
        return httpResponse;
    }


    /**
     *  HTTP PUT 请求 带请求头
     * @param url 请求路径
     * @param headers 请求头
     * @param json 请求体
     * @return 返回 put 请求响应体
     */
    public static HttpResponse httpPutJson(String url, Map<String, String> headers, String json) {
        MediaType mediaType = MediaType.parse(JSON_CHARSET_UTF_8);
        RequestBody body = RequestBody.create(json,mediaType);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        headers.forEach(requestBuilder::addHeader);
        Request request = requestBuilder.put(body).build();
        HttpResponse httpResponse = new HttpResponse();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            httpResponse.setResponseBody(responseBody.string());
            httpResponse.setResponseCode(response.code());
            if (response.code() == 200) {
                log.info("Http PUT 请求成功; [url={},responseBody={}]", url, httpResponse.getResponseBody());
            } else {
                log.warn("Http PUT 请求失败 ; [ errorCode = {}, url={}," + " param={},responseBody ={}]", response.code(), url, json, httpResponse.getResponseBody());
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http PUT 请求失败,url:" + url, e);
        }
        return httpResponse;
    }



    /**
     * Http DELETE 请求 带请求头
     * @param url 请求路径
     * @param headers 请求头
     * @return 返回请求结果
     */
    public static HttpResponse httpDelete(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        headers.forEach(builder::header);
        Request request = builder.delete().url(url).build();
        HttpResponse httpResponse = new HttpResponse();
        try {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            httpResponse.setResponseBody(responseBody.string());
            httpResponse.setResponseCode(response.code());
            if (response.code() == 200) {
                log.info("Http DELETE 请求成功; [url={},responseBody={}]", url, httpResponse.getResponseBody());
            } else {
                log.warn("Http DELETE 请求失败; [errorCode = {} , url={}, " + "responseBody={}]", response.code(), url, httpResponse.getResponseBody());
            }
        } catch (IOException e) {
            throw new RuntimeException("同步http DELETE 请求失败,url:" + url, e);
        }
        return httpResponse;
    }
}
