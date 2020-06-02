package com.zcj.wxpro.controller;

import com.alibaba.fastjson.JSON;
import com.zcj.wxpro.body.BaseResponse;
import com.zcj.wxpro.body.CommonBody;
import com.zcj.wxpro.model.FriendMsg;
import com.zcj.wxpro.model.User;
import com.zcj.wxpro.service.IFriendMsgService;
import com.zcj.wxpro.service.IUserService;
import com.zcj.wxpro.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FriendMsgController {
    @Autowired
    private IFriendMsgService friendMsgService;
    @Autowired
    private IUserService userService;

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @GetMapping(value = "/unreadFriendMsg")
    private String getUnreadMsg(CommonBody commonBody){
        if(userService.checkAuth(commonBody.getOpenId(),commonBody.getSessionKey())) {
            User user = userService.findUserByOpenId(commonBody.getOpenId());
            List<FriendMsg> unreadMsg = friendMsgService.findMyUnReadFriendMsg(user.getUserId());
            if(unreadMsg.size()==0){
                return JSON.toJSONString(BaseResponse.failResponse("没有未读消息"));
            }
            //获取之后修改为已读
            for(int i=0;i<unreadMsg.size();i++){
                unreadMsg.get(i).setIsRead((short) 1);
                friendMsgService.save(unreadMsg.get(i));
            }
            BaseResponse baseResponse = BaseResponse.successResponse("获取未读消息成功");
            baseResponse.setData(unreadMsg);
            return JSON.toJSONString(baseResponse);
        }else {
            return JSON.toJSONString(BaseResponse.failResponse("用户登录失效"));
        }
    }
}
