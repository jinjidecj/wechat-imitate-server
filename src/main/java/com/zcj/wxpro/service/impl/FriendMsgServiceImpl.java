package com.zcj.wxpro.service.impl;

import com.zcj.wxpro.model.FriendMsg;
import com.zcj.wxpro.repository.IFriendMsgRepository;
import com.zcj.wxpro.service.IFriendMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendMsgServiceImpl implements IFriendMsgService {
    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private IFriendMsgRepository friendMsgRepository;

    @Override
    public void save(FriendMsg friendMsg) {
        friendMsgRepository.save(friendMsg);
    }

    @Override
    public List<FriendMsg> findMyUnReadFriendMsg(String targetId) {
        return friendMsgRepository.findByTargetUserIdAndIsRead(targetId, (short) 2);
    }
}
