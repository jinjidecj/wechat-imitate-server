package com.zcj.wxpro.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcj.wxpro.body.BaseResponse;
import com.zcj.wxpro.body.CommonBody;
import com.zcj.wxpro.body.addMsg.SimpleUserBody;
import com.zcj.wxpro.body.user.UserAddFriend;
import com.zcj.wxpro.body.user.UserRegister;
import com.zcj.wxpro.body.user.UserRegisterResponse;
import com.zcj.wxpro.body.user.UserResponseBody;
import com.zcj.wxpro.body.webSocket.WsAddFriend;
import com.zcj.wxpro.model.AddMsg;
import com.zcj.wxpro.model.User;
import com.zcj.wxpro.service.IAddMsgService;
import com.zcj.wxpro.service.IUserService;
import com.zcj.wxpro.service.impl.UserServiceImpl;
import com.zcj.wxpro.util.Common;
import com.zcj.wxpro.util.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAddMsgService addMsgService;

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @GetMapping(value = "/sendCode")
    public String saveCode(@RequestParam(value = "code") String code){
        String result = userService.sendCode(code);
        return userService.saveOpenId(result);
    }

    @PostMapping(value = "/userInfo")
    public String saveUserInfo(@RequestBody User user){
        if(userService.checkAuth(user.getOpenId(),user.getSessionKey())) {
            if (userService.updateUserByOpenId(user)) {
                return JSON.toJSONString(BaseResponse.successResponse("更新用户成功"));
            }else{
                return JSON.toJSONString(BaseResponse.failResponse("更新用户信息失败"));
            }
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }

    }
    @GetMapping(value = "/allUser")
    public String findAllUser(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            return userService.findAllUser();
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }

    /**
     * 查找好友
     * @param user
     * @return
     */
    @GetMapping(value = "/findFriend")
    public String findFriend(UserAddFriend user){
        if(userService.checkAuth(user.getOpenId(),user.getSessionKey())) {
            User userFindFriend =  userService.findUserByUserID(user.getFriendUserId());
            if(userFindFriend==null){
                //没有这个好友
                return JSON.toJSONString(BaseResponse.failResponse("未查找到该用户"));
            }else{
                //有这个好友
                return JSON.toJSONString(new UserResponseBody(userFindFriend,"200","查找好友成功"));
            }
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }
    @PostMapping(value = "/addFriend")
    public String addFriend(@RequestBody UserAddFriend user) throws IOException {
        if(userService.checkAuth(user.getOpenId(),user.getSessionKey())) {
            User userFrom = userService.findUserByOpenId(user.getOpenId());
            User userTarget = userService.findUserByUserID(user.getFriendUserId());
            //判断是否为自己的好友
            if(userService.isMyFriend(userFrom,userTarget)){
                return JSON.toJSONString(BaseResponse.failResponse("该用户已为您的好友"));
            }
            //判断之前是否添加过,不论结果如何
            if(addMsgService.hasSended(userTarget.getUserId(),userFrom.getUserId())){
                return JSON.toJSONString(BaseResponse.failResponse("已发送过好友请求"));
            }
            //保存好友请求
            AddMsg addMsg = new AddMsg(userFrom,userTarget,user.getNode());
            addMsgService.save(addMsg);

            //发送好友通知

            WsAddFriend wsAddFriend = new WsAddFriend(3,userFrom,addMsg);
            WebSocketServer.sendMsgById(userTarget.getOpenId(),JSON.toJSONString(wsAddFriend));

            return JSON.toJSONString(BaseResponse.successResponse("成功发送好友请求"));
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }
    @GetMapping(value = "/isRegister")
    public String isRegister(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            User user = userService.findUserByOpenId(commonBody.getOpenId());
            if(user.getUserId()!=null && !user.getUserId().equals("")){
                return JSON.toJSONString(new UserRegisterResponse("200","用户注册过",user.getUserId()));
            }else{
                return JSON.toJSONString(BaseResponse.failResponse("用户没注册过"));
            }
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }

    @PostMapping(value = "/register")
    public String register(@RequestBody UserRegister userRegister){
        if(userService.checkAuth(userRegister.getOpenId(),userRegister.getSessionKey())) {
            if(userService.hasUserByUserId(userRegister.getUserId())){
                return JSON.toJSONString(BaseResponse.failResponse("该ID已被注册"));
            }
            User user = userService.findUserByOpenId(userRegister.getOpenId());
            user.setUserId(userRegister.getUserId());
            if(userService.saveUser(user)){
                return JSON.toJSONString(BaseResponse.successResponse("用户注册ID成功"));
            }else{
                return JSON.toJSONString(BaseResponse.failResponse("用户注册ID失败"));
            }
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }
    @GetMapping(value = "/allFriend")
    public String allFriend(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            User user = userService.findUserByOpenId(commonBody.getOpenId());
            if(user.getFriends().size()==0){
                return JSON.toJSONString(BaseResponse.failResponse("你还没有好友"));
            }else{
                List<User> friends = user.getFriends();
                List<SimpleUserBody> sendFriends = new ArrayList<SimpleUserBody>();
                for(int i = 0;i<friends.size();i++){
                    sendFriends.add(new SimpleUserBody(friends.get(i)));
                }
                BaseResponse baseResponse = BaseResponse.successResponse("成功获取好友");
                baseResponse.setData(sendFriends);
                return JSON.toJSONString(baseResponse);
            }

        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }
}
