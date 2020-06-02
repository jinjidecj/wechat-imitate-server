package com.zcj.wxpro.body.addMsg;

import com.zcj.wxpro.model.User;

import java.util.List;

public class SimpleUserBody {
    private Integer id;
    private String name;
    private Short gender;
    private String avatarUrl;
    private String userId;

    public SimpleUserBody(){}
    public SimpleUserBody(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.gender = user.getGender();
        this.avatarUrl = user.getAvatarUrl();
        this.userId = user.getUserId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
