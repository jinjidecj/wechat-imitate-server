package com.zcj.wxpro.body.addMsg;

import com.zcj.wxpro.body.CommonBody;

public class AddMsgResult  extends CommonBody {
    private Integer msgId;
    private Short result;

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Short getResult() {
        return result;
    }

    public void setResult(Short result) {
        this.result = result;
    }
}
