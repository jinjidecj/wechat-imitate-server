package com.zcj.wxpro.util;



public class Common {
    public final static String CODE="status";
    public final static String MSG="msg";

    //code 状态码
    public final static String CODE_SUCCESS="200";
    public final static String CODE_FAIL="800";

    //登录成功
    public final static String MSG_LOGIN_SUCCESS="登录成功";
    public final static String MSG_LOGIN_FAIL="登录失败";

    //微信api登录
    public final static String WX_LOGIN_KEY_APPID="appid";
    public final static String WX_LOGIN_KEY_SECRET="secret";
    public final static String WX_LOGIN_KEY_JSCODE="js_code";
    public final static String WX_LOGIN_KEY_GRANT_TYPE="grant_type";

    public final static String WX_OPENID="openid";
    public final static String WX_SESSION_KEY="session_key";

    public final static WebSocketInfo WS = new WebSocketInfo();
    public final static UserInfo USERINFO = new UserInfo();
}
class WebSocketInfo{
    //鉴权字段
    public final static String OPENID="openId";
    public final static String SESSION_KEY="sessionKey";
    //数据key
    public final static String DATA="data";
    //类型
    public final static String TYPE="type";

    //type的值，代表的意义
    public final static int TYPE_CONNECT_SUCCESS=1;
    public final static int TYPE_CHAT_MESSAGE=2;

    public final static String CHAT_CONTENT="content";
    public final static String CHAT_FROM_ID="fromId";
    public final static String CHAT_TARGET_ID="targetId";
}
class UserInfo{
    //是否已读 1-已读   2-未读
    //消息的结果  11-通过   12-拒绝  10-未知
    public final static short ADD_READ=1;
    public final static short ADD_NO_READ=2;
    public final static short ADD_PASS=11;
    public final static short ADD_NO_PASS=12;
    public final static short ADD_UNKONW=10;
}
