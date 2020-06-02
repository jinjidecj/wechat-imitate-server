package com.zcj.wxpro.body.webSocket;

import com.zcj.wxpro.model.AddMsg;
import com.zcj.wxpro.model.User;

public class WsAddFriend {
    private int type;
    private User from;
    private AddMsg addMsg;

    public WsAddFriend(int type, User from, AddMsg addMsg) {
        this.type = type;
        this.from = from;
        this.addMsg = addMsg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public AddMsg getAddMsg() {
        return addMsg;
    }

    public void setAddMsg(AddMsg addMsg) {
        this.addMsg = addMsg;
    }
}
