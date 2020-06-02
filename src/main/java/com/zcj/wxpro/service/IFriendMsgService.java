package com.zcj.wxpro.service;

import com.zcj.wxpro.model.FriendMsg;

import java.util.List;

public interface IFriendMsgService {
    public void save(FriendMsg friendMsg);
    public List<FriendMsg> findMyUnReadFriendMsg(String targetId);
}
