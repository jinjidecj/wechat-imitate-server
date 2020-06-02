package com.zcj.wxpro.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcj.wxpro.body.BaseResponse;
import com.zcj.wxpro.model.FriendMsg;
import com.zcj.wxpro.model.User;
import com.zcj.wxpro.service.IFriendMsgService;
import com.zcj.wxpro.service.IUserService;
import com.zcj.wxpro.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{openId}")
@Component
public class WebSocketServer {

    //spring管理的都是单例（singleton），和 websocket （多对象）相冲突。
    //项目启动时初始化，会初始化 websocket （非用户连接的），spring 同时会为其注入 service，该对象的 service 不是 null，被成功注入。但是，由于 spring 默认管理的是单例，所以只会注入一次 service。当新用户进入聊天时，系统又会创建一个新的 websocket 对象，这时矛盾出现了：spring 管理的都是单例，不会给第二个 websocket 对象注入 service，所以导致只要是用户连接创建的 websocket 对象，都不能再注入了。
    //
    //像 controller 里面有 service， service 里面有 dao。因为 controller，service ，dao 都有是单例，所以注入时不会报 null。但是 websocket 不是单例，所以使用spring注入一次后，后面的对象就不会再注入了，会报null。
    private static IUserService userService;
    private static IFriendMsgService friendMsgService;
    public WebSocketServer(){
        log.info("websocket 启动");
    }
    @Autowired
    public void setUserService(IUserService userService) {
        WebSocketServer.userService = userService;
    }

    @Autowired
    public void setFriendMsgService(IFriendMsgService friendMsgService) {
        WebSocketServer.friendMsgService = friendMsgService;
    }

    private final static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String openId="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("openId") String openId) {
        this.session = session;
        this.openId=openId;
        if(webSocketMap.containsKey(openId)){
            webSocketMap.remove(openId);
            webSocketMap.put(openId,this);
            //加入set中
        }else{
            webSocketMap.put(openId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+openId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+openId+",网络异常!!!!!!");
        }
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(openId)){
            webSocketMap.remove(openId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+openId+",当前在线人数为:" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+openId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                this.handleMsgReceive(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理接收到的json数据
     * @param root 接收到的json
     */
    private void handleMsgReceive(JSONObject root) throws IOException {
        String openId = root.getString(Common.WS.OPENID);
        String sessionKey = root.getString(Common.WS.SESSION_KEY);
        JSONObject returnJson = new JSONObject();
        //保存消息转发的目标websocket连接id
        String targetReturnOpenId = "";
        if(userService.checkAuth(openId,sessionKey)) {
            JSONObject dataJson = root.getJSONObject(Common.WS.DATA);
            Integer msgType = dataJson.getInteger(Common.WS.TYPE);
            if(msgType==Common.WS.TYPE_CONNECT_SUCCESS){
                //连接成功消息
                targetReturnOpenId=openId;
                returnJson.put(Common.WS.DATA,"连接成功");
            }else if(msgType==Common.WS.TYPE_CHAT_MESSAGE){
                //发送消息
                //获取消息的目标user的id
                Integer id = Integer.parseInt(dataJson.getString(Common.WS.CHAT_TARGET_ID));
                //获取消息来源的user
                User fromUser = userService.findUserByOpenId(openId);
                //获取目标user
                User user = userService.findUserById(id);
                targetReturnOpenId=user.getOpenId();
//                targetReturnOpenId= openId;//测试：发给自己
                //保存消息
                FriendMsg friendMsg = new FriendMsg(fromUser,user,dataJson.getString(Common.WS.CHAT_CONTENT));

                //判断是否在线上
//                if(StringUtils.isNotBlank(targetReturnOpenId)&&webSocketMap.containsKey(targetReturnOpenId)) {
//                    //在线上 把消息设置成已读
//                    friendMsg.setIsRead((short) 1);
//                }

                friendMsgService.save(friendMsg);

                returnJson.put(Common.WS.CHAT_CONTENT,dataJson.getString(Common.WS.CHAT_CONTENT));
                returnJson.put(Common.WS.TYPE,Common.WS.TYPE_CHAT_MESSAGE);
                returnJson.put(Common.WS.CHAT_FROM_ID,fromUser.getId());
                returnJson.put(Common.WS.CHAT_TARGET_ID,user.getId());
            }

        }else {
            returnJson.put(Common.CODE,Common.CODE_FAIL);
            returnJson.put(Common.MSG,"用户登录过期");
            targetReturnOpenId = openId;
        }
        this.sendMsgById(targetReturnOpenId,returnJson);
    }

    /**
     * 根据用户的id-key发送消息
     * @param msg
     * @param toUserId 用户在websocket map的key里的id
     */
    public static void sendMsgById(String toUserId , JSONObject msg ) throws IOException {
        if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
            webSocketMap.get(toUserId).sendMessage(msg.toJSONString());
        }else{
            log.error("请求的userId:"+toUserId+"不在该服务器上");
            //否则不在这个服务器上，发送到mysql或者redis
        }
    }

    /**
     * 根据用户的id-key发送消息
     * @param msg
     * @param toUserId 用户在websocket map的key里的id
     */
    public static void sendMsgById(String toUserId , String  msg ) throws IOException {
        if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
            webSocketMap.get(toUserId).sendMessage(msg);
        }else{
            log.error("请求的userId:"+toUserId+"不在该服务器上");
            //否则不在这个服务器上，发送到mysql或者redis
        }
    }
    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.openId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
