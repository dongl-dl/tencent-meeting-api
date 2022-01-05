package com.tencent.wemeet.gateway.restapisdk.models.request.user;

import com.tencent.wemeet.gateway.restapisdk.common.BaseMsg;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dongliang7
 *
 * @ClassName UserUpdateVo.java
 * @description: 更新用户
 * @createTime 2021年11月16日 18:43:00
 */
@Data
public class UserUpdateVo extends BaseMsg implements Serializable {
    private static final long serialVersionUID = -1L;

    private String email;

    private String phone;

    private String username;

    private String userid;


    public static final class Builder {
        private String email;
        private String phone;
        private String username;
        private String userid;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withUserid(String userid) {
            this.userid = userid;
            return this;
        }

        public UserUpdateVo build() {
            UserUpdateVo userUpdateVo = new UserUpdateVo();
            userUpdateVo.setEmail(email);
            userUpdateVo.setPhone(phone);
            userUpdateVo.setUsername(username);
            userUpdateVo.setUserid(userid);
            return userUpdateVo;
        }
    }
}
