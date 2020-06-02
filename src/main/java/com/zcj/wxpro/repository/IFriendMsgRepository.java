package com.zcj.wxpro.repository;

import com.zcj.wxpro.model.FriendMsg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFriendMsgRepository extends JpaRepository<FriendMsg,Integer> {
    public List<FriendMsg> findByTargetUserIdAndIsRead(String targetId,Short isRead);
}
