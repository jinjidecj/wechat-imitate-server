package com.zcj.wxpro.body.user;

import com.zcj.wxpro.body.BaseResponse;

public class UserRegisterResponse extends BaseResponse {
    private String userId;

    public UserRegisterResponse(String status, String msg,String userId) {
        super(status, msg);
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
