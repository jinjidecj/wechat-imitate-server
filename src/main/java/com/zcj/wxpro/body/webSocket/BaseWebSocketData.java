package com.zcj.wxpro.body.webSocket;

import com.zcj.wxpro.body.BaseResponse;

public class BaseWebSocketData extends BaseResponse {
    private String data;
    public BaseWebSocketData(String status, String msg) {
        super(status, msg);
    }
}
