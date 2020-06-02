package com.zcj.wxpro.body.addMsg;

import com.zcj.wxpro.model.AddMsg;
import com.zcj.wxpro.model.User;

import java.util.Date;

public class AddMsgHaveUser{
    private Integer id;

    private String fromUserId;
    private String targetUserId;
    private String node;
    private Date time;

    private short isRead;//是否已读 1-已读   2-未读
    private short result;//消息的结果  11-通过   12-拒绝  10-未知

    private SimpleUserBody user;
    public AddMsgHaveUser(AddMsg addMsg,User user){
        this.id=addMsg.getId();
        this.fromUserId = addMsg.getFromUserId();
        this.targetUserId = addMsg.getTargetUserId();
        this.node = addMsg.getNode();
        this.time = addMsg.getTime();
        this.isRead = addMsg.getIsRead();
        this.result = addMsg.getResult();

        this.user = new SimpleUserBody();

        this.user.setId(user.getId());
        this.user.setAvatarUrl(user.getAvatarUrl());
        this.user.setGender(user.getGender());
        this.user.setName(user.getName());
        this.user.setUserId(user.getUserId());
    }
    public SimpleUserBody getUser() {
        return user;
    }

    public void setUser(SimpleUserBody user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public short getIsRead() {
        return isRead;
    }

    public void setIsRead(short isRead) {
        this.isRead = isRead;
    }

    public short getResult() {
        return result;
    }

    public void setResult(short result) {
        this.result = result;
    }
}
