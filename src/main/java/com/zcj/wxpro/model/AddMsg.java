package com.zcj.wxpro.model;

import com.zcj.wxpro.util.Common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class AddMsg {
    @Id
    @GeneratedValue
    private Integer id;

    private String fromUserId;
    private String targetUserId;
    private String node;
    private Date time;

    private short isRead;//是否已读 1-已读   2-未读
    private short result;//消息的结果  1-通过   2-拒绝  0-未知

    public AddMsg(){}
    public AddMsg(User from,User target,String node){
        this.fromUserId = from.getUserId();
        this.targetUserId = target.getUserId();
        this.node = node;
        this.time = new Date();
        this.isRead = 2;
        this.result = 0;
    }

    @Override
    public String toString() {
        return "AddMsg{" +
                "id=" + id +
                ", fromUserId='" + fromUserId + '\'' +
                ", targetUserId='" + targetUserId + '\'' +
                ", node='" + node + '\'' +
                ", time=" + time +
                ", isRead=" + isRead +
                ", result=" + result +
                '}';
    }
    public short getResult() {
        return result;
    }

    public void setResult(short result) {
        this.result = result;
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

}
