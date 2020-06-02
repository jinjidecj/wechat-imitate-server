package com.zcj.wxpro.body.user;


import com.zcj.wxpro.model.User;

public class UserResponseBody  {
    private Integer id;
    private String status;
    private String msg;
    private String name;
    private Short gender;
    private String avatarUrl;
    private String userId;

    //构造函数
    public UserResponseBody(User user,String status,String msg){
        this.id=user.getId();
        this.name=user.getName();
        this.gender = user.getGender();
        this.avatarUrl = user.getAvatarUrl();
        this.userId = user.getUserId();
        this.status = status;
        this.msg = msg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
