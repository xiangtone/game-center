package com.hykj.gamecenter.sdk.entry;

public class NiuSDKConstant
{

    public final static int GAME_TYPE_ONLINE = 10001;
    public final static int GAME_TYPE_OFFLINE = 10002;

    /* 
     *  error-code
     */
    // 在调用SDK前没有初始化，既没有调用getInstance
    public final static int SDK_NOT_INITIALED = 20001;
    // 在调用SDK前没有初始化appinfo，即没有调用initialAppInfo
    public final static int SDK_APPINFO_NOT_INITIALED = 20002;
    // 在调用消费时，没有传入物品名、订单号或者>=0的pa币值
    public final static int SDK_ORDER_NOT_INITIALED = 20003;
    //在调用消费时，角色token失效
    public final static int SDK_ROLE_TOKEN_FAIL = 20004;
    // 在调用SDK（登陆、消费）后，用户back键取消操作
    public final static int SDK_USER_CANCEL = 20005;

    /*
     *  dev-mode
     */
    // 调试模式
    public final static int SDK_DEBUG_MODE = 30001;
    public final static int SDK_PUBLIC_MODE = 30002;

}
