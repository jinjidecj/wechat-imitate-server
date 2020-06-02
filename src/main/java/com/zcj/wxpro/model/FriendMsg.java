package com.zcj.wxpro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class FriendMsg {
    @Id
    @GeneratedValue
    private Integer id;

    private String fromUserId;
    private String targetUserId;
    private String content;
    private Date time;
    private short isRead;//是否已读 1-已读   2-未读

    public FriendMsg(){}
    public FriendMsg(User fromUser,User targetUsr,String content){
        this.fromUserId =fromUser.getUserId();
        this.targetUserId = targetUsr.getUserId();
        this.content = content;
        this.time = new Date();
        this.isRead = 2;
    }

    @Override
    public String toString() {
        return "FriendMsg{" +
                "id=" + id +
                ", fromUserId='" + fromUserId + '\'' +
                ", targetUserId='" + targetUserId + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", isRead=" + isRead +
                '}';
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
