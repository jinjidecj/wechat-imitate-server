package com.zcj.wxpro.repository;

import com.zcj.wxpro.model.AddMsg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAddMsgRepository extends JpaRepository<AddMsg,Integer> {
    public List<AddMsg> findByTargetUserId(String targetId);
    public List<AddMsg> findByTargetUserIdAndFromUserId(String targetUserId,String fromUserId);
    public List<AddMsg> findByTargetUserIdAndIsRead(String targetId,Short isRead);
//    public Optional<AddMsg> findById(Integer id);
}
