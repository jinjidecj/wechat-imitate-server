package com.zcj.wxpro.body;

import com.zcj.wxpro.util.Common;

public class BaseResponse {
    private String status;
    private String msg;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static BaseResponse failResponse(){
        return new BaseResponse(Common.CODE_FAIL,"");
    }
    public static BaseResponse failResponse(String msg){
        return new BaseResponse(Common.CODE_FAIL,msg);
    }
    public static BaseResponse successResponse(){
        return new BaseResponse(Common.CODE_SUCCESS,"");
    }
    public static BaseResponse successResponse(String msg){
        return new BaseResponse(Common.CODE_SUCCESS,msg);
    }
    public BaseResponse(String status, String msg){
        this.status=status;
        this.msg=msg;
    }
    public BaseResponse(){}

    public void setFailResponse(){
        this.status=Common.CODE_FAIL;
    }
    public void setFailResponse(String msg){
        this.setFailResponse();
        this.msg = msg;
    }
    public void setSuccessResponse(){
        this.status = Common.CODE_SUCCESS;
    }
    public void setSuccessResponse(String msg){
        this.setSuccessResponse();
        this.msg = msg;
    }
    public void setResponse(String status, String msg){
        this.status=status;
        this.msg=msg;
    }



}
