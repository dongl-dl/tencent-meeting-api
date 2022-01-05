package com.tencent.wemeet.gateway.restapisdk.models.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dongliang7
 * @projectName parent
 * @ClassName TenCentSdkError.java
 * @description: 腾讯会议调用API接口失败返回体
 * @createTime 2021年12月09日 19:10:00
 */
@Data
public class TenCentSdkError implements Serializable {

    private  ErrorDetail  error_info;

    private static final long serialVersionUID = 1L;

    class ErrorDetail{

        private String error_code;

        private String new_error_code;

        private String message;

        public String getError_code() {
            return error_code;
        }

        public void setError_code(String error_code) {
            this.error_code = error_code;
        }

        public String getNew_error_code() {
            return new_error_code;
        }

        public void setNew_error_code(String new_error_code) {
            this.new_error_code = new_error_code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


//     {"error_info":{"error_code":9007,"new_error_code":1000009007,"message":"MEETING CANCLED"}}
}
