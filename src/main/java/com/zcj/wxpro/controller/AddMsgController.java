package com.zcj.wxpro.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zcj.wxpro.body.BaseResponse;
import com.zcj.wxpro.body.CommonBody;
import com.zcj.wxpro.body.addMsg.AddMsgHaveUser;
import com.zcj.wxpro.body.addMsg.AddMsgResult;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AddMsgController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IAddMsgService addMsgService;

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 获取我的好友请求列表
     * @param commonBody
     * @return
     */
    @GetMapping(value = "/myAddMsg")
    public String myAddMsg(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            User user = userService.findUserByOpenId(commonBody.getOpenId());
            List<AddMsg> allAddMsg = addMsgService.findAllByWho(user.getUserId());
            List<AddMsgHaveUser> allAddMsgHaveUser = new ArrayList<AddMsgHaveUser>();
            for(int i = 0;i<allAddMsg.size();i++){
                User fromUser = userService.findUserByUserID(allAddMsg.get(i).getFromUserId());
                LOG.info(allAddMsg.get(i).getFromUserId());
                LOG.info(fromUser.toString());
                allAddMsgHaveUser.add(new AddMsgHaveUser(
                        allAddMsg.get(i),
                        fromUser
                ));
            }
            BaseResponse successResponse = BaseResponse.successResponse("查找好友请求列表成功");
            JSONObject root = JSON.parseObject(JSON.toJSONString(successResponse));
            root.put("addMsg",allAddMsgHaveUser);
            return JSON.toJSONString(root, SerializerFeature.DisableCircularReferenceDetect);
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("查找好友请求列表失败"));
        }
    }
    @GetMapping(value = "/addMsgResult")
    public String setResult(AddMsgResult addMsgResult){
        if(userService.checkAuth(addMsgResult.getOpenId(),addMsgResult.getSessionKey())) {
            AddMsg addMsg = addMsgService.findById(addMsgResult.getMsgId());
            if(addMsg==null){
                return JSON.toJSONString(BaseResponse.failResponse("消息错误"));
            }
            //保存结果
            addMsg.setResult(addMsgResult.getResult());
            addMsgService.save(addMsg);
            //如果为同意添加好友的请求，为其添加好友
            if(addMsgResult.getResult()==1){
                User user = userService.findUserByOpenId(addMsgResult.getOpenId());
                User addFriend = userService.findUserByUserID(addMsg.getFromUserId());
                if(!userService.ifHasFriend(user,addFriend)){
                    user.getFriends().add(addFriend);
                }
                if(!userService.ifHasFriend(addFriend,user)){
                    addFriend.getFriends().add(user);
                }

                userService.saveUser(addFriend);
                userService.saveUser(user);
//                try {
//                    WebSocketServer.sendMsgById(addFriend.getOpenId(),JSON.toJSONString(BaseResponse.successResponse("有好友添加成功")));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            return JSON.toJSONString(BaseResponse.successResponse("发送成功"));
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("登录过期"));
        }
    }
    @GetMapping(value = "/unReadAddMsg")
    public String unReadAddMsg(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            User user = userService.findUserByOpenId(commonBody.getOpenId());
            int num = addMsgService.findUnReadAddMsg(user.getUserId());
            BaseResponse successResponse = BaseResponse.successResponse("查找好友请求列表成功");
            successResponse.setData(num);
            return JSON.toJSONString(successResponse);
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("查找好友未读消息失败"));
        }
    }
    @GetMapping(value = "/readAllAddMsg")
    public String hasReadAllAddMsg(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            User user = userService.findUserByOpenId(commonBody.getOpenId());
            addMsgService.hasReadAllAddMsg(user.getUserId());
            return JSON.toJSONString(BaseResponse.successResponse("阅读全部好友添加消息成功"));
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("好友阅读消息失败"));
        }
    }

}
