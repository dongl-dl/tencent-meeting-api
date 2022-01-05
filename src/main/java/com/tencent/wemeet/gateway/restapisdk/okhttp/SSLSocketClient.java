package com.tencent.wemeet.gateway.restapisdk.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author dongliang7
 * 
 * @ClassName SSLSocketClient.java
 * @description: 创建 OkHttpClient 不进行SSL（证书）验证
 * @createTime 2021年11月19日 09:50:00
 */
@Slf4j
public class SSLSocketClient {

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 创建不验证证书链的信任管理器
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };
            if (trustAllCerts.length != 1 || !(trustAllCerts[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustAllCerts));
            }
            X509TrustManager x509TrustManager = (X509TrustManager) trustAllCerts[0];

            // 安装全信任信任管理器
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // 使用我们完全信任的管理器创建 ssl 套接字工厂
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(60 , TimeUnit.SECONDS).readTimeout(60 , TimeUnit.SECONDS).writeTimeout(120 , TimeUnit.SECONDS);
            builder.sslSocketFactory(sslSocketFactory , x509TrustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            log.error("创建OkHttpClient不进行SSL（证书）验证失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
