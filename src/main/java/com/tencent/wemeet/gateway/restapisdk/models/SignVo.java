package com.tencent.wemeet.gateway.restapisdk.models;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName SignVo.java
 * @description:  签名实体类
 * @createTime 2021年11月15日 23:15:30
 */
@ToString
@Data
public class SignVo implements Serializable {
    private static final long serialVersionUID = -1L;

    //安全凭证 SecretId
    private String secretId;

    //安全凭证 SecretKey
    private String secretKey;

    /**
     * 随机正整数
     */
    private String nonce;

    /**
     * 时间戳 10位
     */
    private String timestamp;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求路径
     */
    private String uri;

    /**
     * 请求体
     */
    private String body;


    public static final class Builder {
        private String secretId;
        private String secretKey;
        private String nonce;
        private String timestamp;
        private String method;
        private String uri;
        private String body;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withSecretId(String secretId) {
            this.secretId = secretId;
            return this;
        }

        public Builder withSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder withNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder withTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder withUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder withBody(String body) {
            this.body = body;
            return this;
        }

        public SignVo build() {
            SignVo signVo = new SignVo();
            signVo.setSecretId(secretId);
            signVo.setSecretKey(secretKey);
            signVo.setNonce(nonce);
            signVo.setTimestamp(timestamp);
            signVo.setMethod(method);
            signVo.setUri(uri);
            signVo.setBody(body);
            return signVo;
        }
    }
}
