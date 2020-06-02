package com.zcj.wxpro.body.user;

import com.zcj.wxpro.body.CommonBody;

public class UserRegister extends CommonBody {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserRegister{" +
                "userId='" + userId + '\'' +
                ", openId='" + openId + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                '}';
    }
}
