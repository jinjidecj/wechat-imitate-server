package com.zcj.wxpro.service;

import com.zcj.wxpro.model.AddMsg;

import java.util.List;

public interface IAddMsgService {
    public void save(AddMsg addMsg);
    public List<AddMsg> findAll();
    public List<AddMsg> findAllByWho(String userId);
    public boolean hasSended(String targetId,String fromId);
    public AddMsg findById(Integer id);
    public int findUnReadAddMsg(String targetId);
    public void hasReadAllAddMsg(String targetId);
}
