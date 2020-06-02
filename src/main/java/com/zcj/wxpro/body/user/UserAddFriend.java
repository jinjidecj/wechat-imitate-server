package com.zcj.wxpro.body.user;

import com.zcj.wxpro.body.CommonBody;

public class UserAddFriend extends CommonBody {
    private String friendUserId;//要添加的朋友的userId

    private String node;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    @Override
    public String toString() {
        return "UserAddFriend{" +
                "friendUserId='" + friendUserId + '\'' +
                ", node='" + node + '\'' +
                ", openId='" + openId + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                '}';
    }
}
