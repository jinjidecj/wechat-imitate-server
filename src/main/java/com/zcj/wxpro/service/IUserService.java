package com.zcj.wxpro.service;

import com.zcj.wxpro.model.User;

public interface IUserService {
    public String sendCode(String code);
    public User findUserByOpenId(String openId);
    public String saveOpenId(String result);
    public boolean checkAuth(String openId,String sessionKey);
    public boolean updateUserByOpenId(User user);
    public String findFriendsByName(User user);
    public String addFriendByName(User user);
    public String findAllUser();
    public User findUserById(Integer id);
    public boolean saveUser(User user);
    public boolean hasUserByUserId(String userId);
    public User findUserByUserID(String userId);
    public boolean isMyFriend(User my,User friend);
    public boolean ifHasFriend(User my,User friend);
}
