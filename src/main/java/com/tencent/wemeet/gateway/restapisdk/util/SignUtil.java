package com.tencent.wemeet.gateway.restapisdk.util;

import com.tencent.wemeet.gateway.restapisdk.models.SignVo;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author dongliang7
 * 
 * @ClassName SignUtil.java
 * @description: 腾讯接口调用请求头签名生成工具
 * @createTime 2021年11月19日 08:45:00
 */
@Slf4j
public class SignUtil {

    static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String bytesToHex(byte[] bytes) {

        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    /**
     * 获取签名
     * @param signVo 签名实体类
     * @return
     */
    public static String getSign(SignVo signVo) {
        try {
            String contentCharset = "UTF-8";
            String hmacAlgorithm = "HmacSHA256";
            String tobeSig = signVo.getMethod() + "\nX-TC-Key=" + signVo.getSecretId() + "&X-TC-Nonce="
                    +  signVo.getNonce() + "&X-TC-Timestamp=" + signVo.getTimestamp() + "\n"
                    + signVo.getUri() + "\n" + signVo.getBody();
            Mac mac = Mac.getInstance(hmacAlgorithm);
            byte[] hash;
            SecretKeySpec spec = new SecretKeySpec(signVo.getSecretKey().getBytes(UTF_8), mac.getAlgorithm());
            mac.init(spec);
            hash = mac.doFinal(tobeSig.getBytes(contentCharset));
            String hexHash = bytesToHex(hash);
            log.info("hexHash -> " + hexHash);
            String sig = new String(Base64.getEncoder().encode(hexHash.getBytes(contentCharset)));
            log.info("sig -> " + sig);
            return sig;
        } catch (Exception e) {
            log.info("ERROR OCCURED");
            throw new RuntimeException(e.getCause());
        }
    }
}
