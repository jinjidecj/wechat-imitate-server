package com.zcj.wxpro.service.impl;

import com.zcj.wxpro.model.AddMsg;
import com.zcj.wxpro.repository.IAddMsgRepository;
import com.zcj.wxpro.service.IAddMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddMsgServiceImpl implements IAddMsgService {
    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private IAddMsgRepository addMsgRepository;

    @Override
    public void save(AddMsg addMsg) {
        addMsgRepository.save(addMsg);
    }

    @Override
    public List<AddMsg> findAll() {
        return addMsgRepository.findAll();
    }

    @Override
    public List<AddMsg> findAllByWho(String userId) {
        return addMsgRepository.findByTargetUserId(userId);
    }

    @Override
    public boolean hasSended(String targetId, String fromId) {
        return addMsgRepository.findByTargetUserIdAndFromUserId(targetId,fromId).size()>0;
    }

    @Override
    public AddMsg findById(Integer id) {
        Optional<AddMsg> op = addMsgRepository.findById(id);
        if(op.isPresent()){
            return op.get();
        }else{
            return null;
        }
    }

    /**
     * 查找还有多少没有读过的好友请求消息
     * @param targetId
     * @return
     */
    @Override
    public int findUnReadAddMsg(String targetId) {
        List<AddMsg> addMsgs = addMsgRepository.findByTargetUserIdAndIsRead(targetId, (short) 2);
        return addMsgs.size();
    }

    @Override
    public void hasReadAllAddMsg(String targetId) {
        List<AddMsg> addMsgs = addMsgRepository.findByTargetUserIdAndIsRead(targetId, (short) 2);
        for(int i=0;i<addMsgs.size();i++){
            addMsgs.get(i).setIsRead((short) 1);
            addMsgRepository.save(addMsgs.get(i));
        }

    }
}
