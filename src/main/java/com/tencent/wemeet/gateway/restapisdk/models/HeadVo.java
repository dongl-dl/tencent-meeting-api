package com.tencent.wemeet.gateway.restapisdk.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author dongliang7
 * 
 * @ClassName HeadVo.java
 * @description: 请求头实体类
 * @createTime 2021年11月19日 14:05:00
 */
@Data
public class HeadVo implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 操作的接口名称。取值参考接口文档中输入参数公共参数 Action 的说明。
     * 例如云服务器的查询实例列表接口，取值为 DescribeInstances。
     */
    @JSONField(name = "X-TC-Action")
    private String action;

    /**
     * 地域参数，用来标识希望操作哪个地域的数据。接口接受的地域取值参考接口文档中输入参数公共参数 Region 的说明。
     * 注意：某些接口不需要传递该参数，接口文档中会对此特别说明，此时即使传递该参数也不会生效。
     */
    @JSONField(name = "X-TC-Region")
    private String region;

    /**
     * 此参数参与签名计算。腾讯云 API 接入，申请的安全凭证密钥对中的 SecretId
     * 注意：：：：：这是个坑爹的地方   你说你 X-TC-Key 用个锤子 SecretId
     */
    @NonNull
    @JSONField(name = "X-TC-Key")
    private String key;

    /**
     * 此参数参与签名计算。当前 UNIX 时间戳，可记录发起 API 请求的时间。例如1529223702，单位为秒。
     * 注意：如果与服务器时间相差超过5分钟，会引起签名过期错误。
     * 这里也是坑：：：：时间戳为10位  精确到秒
     */
    @NonNull
    @JSONField(name = "X-TC-Timestamp")
    private String timestamp;

    /**
     * 此参数参与签名计算。随机正整数。
     */
    @NonNull
    @JSONField(name = "X-TC-Nonce")
    private String nonce;

    /**
     * 放置由下面的签名方法产生的签名
     */
    @NonNull
    @JSONField(name = "X-TC-Signature")
    private String signature;

    /**
     * 临时证书所用的 Token ，需要结合临时密钥一起使用。
     * 临时密钥和 Token 需要到访问管理服务调用接口获取。长期密钥不需要 Token
     */
    @JSONField(name = "X-TC-Token")
    private String token;

    /**
     * 腾讯会议分配给三方开发应用的 App ID
     */
    @NonNull
    @JSONField(name = "AppId")
    private String appId;

    /**
     * 用户子账号或开发的应用 ID
     */
    @NonNull
    @JSONField(name = "SdkId")
    private String sdkId;

    /**
     * 启用账户通讯录，传入值必须为1，创建的会议可出现在用户的会议列表中。
     * 启用账户通讯录说明：
     * 1. 通过 SSO 接入腾讯会议账号体系。
     * 2. 通过调用接口创建企业用户。
     * 3. 通过企业管理后台添加或批量导入企业用户。
     */
    @JSONField(name = "X-TC-Registered")
    private String registered = "1";


    public static final class Builder {
        private String action;
        private String region;
        private String key;
        private String timestamp;
        private String nonce;
        private String signature;
        private String token;
        private String appId;
        private String sdkId;
        private String registered;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withAction(String action) {
            this.action = action;
            return this;
        }

        public Builder withRegion(String region) {
            this.region = region;
            return this;
        }

        public Builder withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder withTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder withSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public Builder withAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder withSdkId(String sdkId) {
            this.sdkId = sdkId;
            return this;
        }

        public Builder withRegistered(String registered) {
            this.registered = registered;
            return this;
        }

        public HeadVo build() {
            HeadVo headVo = new HeadVo(key, timestamp, nonce, signature, appId, sdkId);
            headVo.setAction(action);
            headVo.setRegion(region);
            headVo.setToken(token);
            headVo.setRegistered(registered);
            return headVo;
        }
    }
}
