package com.zcj.wxpro.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcj.wxpro.body.BaseResponse;
import com.zcj.wxpro.body.user.UserResponseBody;
import com.zcj.wxpro.config.WXConfig;
import com.zcj.wxpro.model.User;
import com.zcj.wxpro.repository.IUserRepository;
import com.zcj.wxpro.service.IUserService;
import com.zcj.wxpro.util.Common;
import com.zcj.wxpro.util.HTTPsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements  IUserService {
    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private WXConfig wxConfig;
    @Autowired
    private IUserRepository userRepository;

    /**
     * 发送code，返回微信端登录结果
     * @param code 微信小程序传过来的code
     * @return 微信登录返回的结果
     */
    @Override
    public String sendCode(String code) {
        //调用微信登录凭证校验
        Map<String,String> params = new HashMap<String, String>();
        params.put(Common.WX_LOGIN_KEY_SECRET, wxConfig.getAppSecret());
        params.put(Common.WX_LOGIN_KEY_APPID, wxConfig.getAppId());
        params.put(Common.WX_LOGIN_KEY_JSCODE, code);
        params.put(Common.WX_LOGIN_KEY_GRANT_TYPE, wxConfig.getGrantType());
        LOG.info(params.toString());
        String result = null;
        try {
            result = HTTPsUtils.get(wxConfig.getLoginUrl(),params,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public User findUserByOpenId(String openId) {
        List<User> users = userRepository.findByOpenId(openId);
        if(users.size()==0){
            return null;
        }else{
            return users.get(0);
        }
    }

    /**
     * 保存openid，如果数据库里有，则更新sessionkey字段，如果没有，则新增一个
     * @param result 微信登录的结果字段
     * @return 成功保存/更新的结果、或者微信登录失败的结果
     */
    @Override
    public String saveOpenId(String result) {
        JSONObject root = JSON.parseObject(result);
        if(root.getString(Common.WX_OPENID)!=null){
            User user = findUserByOpenId(root.getString(Common.WX_OPENID));
            if(user!=null){
                LOG.info(user.toString());
                user.setSessionKey(root.getString(Common.WX_SESSION_KEY));
                userRepository.save(user);
            }else{
                LOG.info("user is null");
                User newUser = new User();
                newUser.setOpenId(root.getString(Common.WX_OPENID));
                newUser.setSessionKey(root.getString(Common.WX_SESSION_KEY));
                userRepository.save(newUser);
            }
            root.put(Common.CODE,Common.CODE_SUCCESS);
            root.put(Common.MSG,Common.MSG_LOGIN_SUCCESS);
            LOG.info(JSON.toJSONString(root));
            return JSON.toJSONString(root);
        }else{
            LOG.warn("获取不到openid");
            LOG.info(result);
            return result;
        }
    }

    /**
     * 检查用户的openid和sessionkey
     * @param openId
     * @return
     */
    @Override
    public boolean checkAuth(String openId,String sessionKey) {
        User user = findUserByOpenId(openId);
        if(user!=null){
            if(user.getSessionKey().equals(sessionKey)){
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public boolean updateUserByOpenId(User user) {
        try{
            User userIn = findUserByOpenId(user.getOpenId());
            userIn.setName(user.getName());
            userIn.setGender(user.getGender());
            userIn.setAvatarUrl(user.getAvatarUrl());
            userRepository.save(userIn);
            LOG.info("更新用户信息成功："+userIn.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 同过好友的名字来查找好友，并且返回好友的信息
     * @param user 此处的user保存了查询者的openid，sessionkey，user的name是要查询的好友的名字
     * @return 返回小程序需要的json格式字符串
     */
    @Override
    public String findFriendsByName(User user) {
        List<User> users = userRepository.findByName(user.getName());
        if(users.size()>0){
            User friendFind = users.get(0);
            UserResponseBody userR = new UserResponseBody(friendFind,"200","查找新好友成功");
            return JSON.toJSONString(userR);
        }else{
            return JSON.toJSONString(BaseResponse.failResponse("未查询到该用户"));
        }
    }

    /**
     * 添加好友
     * @param user 此处的user保存了查询者的openid，sessionkey，user的id是要添加的好友的id
     * @return
     */
    @Override
    public String addFriendByName(User user) {
        return null;
    }

    @Override
    public String findAllUser() {
        List<User> users = userRepository.findAll();
        return JSON.toJSONString(users);
    }

    @Override
    public User findUserById(Integer id) {
        Optional<User> us =  userRepository.findById(id);
        if(us.isPresent()){
            return us.get();
        }else{
            return null;
        }
    }

    @Override
    public boolean saveUser(User user) {
        User userReturn = userRepository.save(user);
        if(userReturn.getId()==user.getId()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 通过userid判断是否数据库中userid已经被用了
     * @param userId
     * @return
     */
    @Override
    public boolean hasUserByUserId(String userId) {
        List<User> us = userRepository.findByUserId(userId);
        if(us.size()==0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 通过userid来寻找user
     * @param userId
     * @return
     */
    @Override
    public User findUserByUserID(String userId) {
        List<User> us = userRepository.findByUserId(userId);
        if(us.size()==0){
            return null;
        }else{
            return us.get(0);
        }
    }

    @Override
    public boolean isMyFriend(User my, User friend) {
        List<User> myFriends = my.getFriends();
        for(int i = 0;i<myFriends.size();i++){
            if(myFriends.get(i).getId()==friend.getId()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ifHasFriend(User my, User friend) {
        List<User> myFriends = my.getFriends();
        for(int i=0;i<myFriends.size();i++){
            if(friend.getId().equals( myFriends.get(i).getId())){
                return true;
            }
        }
        return false;
    }

}
