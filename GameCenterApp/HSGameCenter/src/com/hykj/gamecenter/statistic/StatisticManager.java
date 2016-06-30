
package com.hykj.gamecenter.statistic;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;

/**
 * 只存放一些常量，统计相关的数据迁移到 {@link ReportConstants}中
 */
public class StatisticManager {
	private static final String TAG = "StatisticManager";
	//以下两个参数用于统计
	public static final String APP_POS_TYPE = "app_pos_type";
	public static final String APP_POS_POSITION = "app_pos_position";

	public static final String APP_ID = "app_id";
	public static final String APP_PERMISSION = "app_permission";

	//preference  key
	public static final String KEY_OPENID = "key_openid";
	public static final String KEY_TOKEN = "key_token";
	public static final String KEY_MOBILE = "key_mobile";
	public static final String KEY_MM_CODE = "key_mm_code";
	public static final String KEY_SHOW_TIPS_DIALOG = "key_show_tips_dialog";
	public static final String KEY_WIFI_SESSID = "key_wifi_sessid";
	public static final String KEY_WIFI_UUID = "key_wifi_uuid";
	public static final String KEY_WIFI_UCODE = "key_wifi_ucode";
	public static final String KEY_WIFI_UTIME = "key_wifi_utime";
	public static final String KEY_WIFIMASK_SHOW = "key_wifimask_show";

	public interface ThridSource{
		String WEI_XIN = "weixin";
		String WEI_BO = "xlwb";
		String QQ = "qq";
	}


	//int constant
	public static final int TIEM_CAPTCHA = 60; //再次发送验证码时间
	public static final int TIME_COUNTDOWN = 300; //倒计时时间

	private static void initInstance() {
		if (mInstance == null)
			mInstance =getInstance();
	}
	public static String getCONSTANT_USER_AGGREMENT() {
		initInstance();
		return CONSTANT_USER_AGGREMENT;
	}


	public static String getConstantRechargeHelp() {
		initInstance();
		return CONSTANT_RECHARGE_HELP;
	}

	public static String getConstantConsumeHelp() {
		initInstance();
		return CONSTANT_CONSUME_HELP;
	}

	public static String getWeixinAppId() {
		initInstance();
		return WEIXIN_APP_ID;
	}

	public static String getWeixinAppSecret() {
		initInstance();
		return WEIXIN_APP_SECRET;
	}

	public static String getQqAppId() {
		initInstance();
		return QQ_APP_ID;
	}

	public static String getBuglyAppid() {
		initInstance();
		return BUGLY_APPID;
	}

	public static String getWechatpayAppId() {
		initInstance();
		return WECHATPAY_APP_ID;
	}

	private static String CONSTANT_USER_AGGREMENT = "http://uac.api.niuwan.cc/userdeal";
	private static String CONSTANT_RECHARGE_HELP = "http://pay.api.niuwan.cc/gamehelp";
	private static String CONSTANT_CONSUME_HELP = "http://pay.api.niuwan.cc/gamehelp";
	//app id scope ...
	public static final String WEIXIN_SCOPE = "snsapi_userinfo";
	public static final String WEIXIN_STATE = "wechat_sdk_demo_test";
	private static String WEIXIN_APP_ID = "wx380b8664b7e36848";
	private static String WEIXIN_APP_SECRET = "076a6f7777a921794358e0c3a5971dcf";
	private static String QQ_APP_ID = "1104770960";
	//bugley
	private static String BUGLY_APPID = "900014376";


	//支付宝参数, 华硕
//    public static final String PARTNER = "2088711131639813"; // 合作者身份ID   账户：牛玩
//    public static final String SELLER = "developer@niuwan.cc";// 卖家支付宝账号    账户：牛玩
//    public static final String RSA_PRIVATE = "MIICXAIBAAKBgQDUaRoComnrUcAUhleeWBCnrQmNt5JvVvZ4fa4R4YBv/5Eapp7cTd+6St7iaP2i1fTHU9T0gka3BIgdY11vswSskbFA32GZx9P5Ykc7huOg0UIw97AKnCB4Z4afnjLhZZBx883kE7vn8/mYpNIznsNZNuQRIcWGJAj08mX/H10iDwIDAQABAoGAPvYQcat2vfvzdaIEorjz5t/RryI006/xSvHmAQLYsVoNxtqQyDiFEpFS08XPOM82yUa+HuPm1iUPu7ZSYeOshHf5nlX9TJgasRrVjKO2WIOJ21tsWI54PAuFdrkLGjOqAoPlCX/7NKziHk1uZfRbMtbF2zEah2gDU/URKTcc2WECQQD46ilFwmvvpNuf4hWvwlaiMauOf48+dh5u2QGTIr+PQ6OYIXq2P2XfXuYZYa/9ZfBGa3cEpgbF2vHJErShtHjXAkEA2nTvYqWnsrbw/GxgXIHHFqdSIXo6Ongb+/quKqqrfJR3g9H6O6lpLCgOxZKTJPKE0ydRufKNl91BszUb8RlhiQJBAK59yE0EzBGEqglcvgMl4wF0fGU6ero4p4DhUz9H3q2ZpvcgkRttOgbqSbeFSZADg/p5n0d888aDg3eHS2UIVKUCQGnFIj59/FMQNl/RTVqczzsNqjthRZ4Xl10KQ5eO6Na5v6AIY4LNhImHn5dTX4ENFvhxWiWQ5bN4wh21giDa7BkCQH8WNt+C57WymW6kIJlE431z7MXwI7nCkXIDp15J6OyTvxqqpnDMf0v8t9CEKZEjeV/opkbVwONK4j0/QooMwFc=";
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	//支付宝参数，昂达
	public static String PARTNER = "2088021819767304"; // 合作者身份ID   账户：闪现
	public static String SELLER = "sxs@niuwan.cc";// 卖家支付宝账号    账户：闪现
	public static String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjLlZq6Y9hv2YXCUwEcRWfvY67QZzi9/mfFPStif5IKjmuTjlRW5rN9+Xoec05KC71BLe/7j/af6eOde1E16ROnGoK0IlPyc1yWSu9RKVoOsK5DIqxZt/0oceSxqZgWGA06iJRalKVa+TlZbwh6OwCUN7jVNFGGHBB/stqxqjOnAgMBAAECgYEAh/XxekfMkj+o0QrqOxTr086DjujyKQNQnFS37qWnqnFt26RSwaa2UjVLShgBSgacoH/GJpz1jMSXNsp1IokBlBvT3wS838tDwYrfPTtVYhiz5UDjxr2tM+d3GRKM4RlbTfEjXRmhFIW2fukYo5NNLhMZA0cTN9NQnOWe5iEz9mECQQD0UD6tbnUpDuQLwTdsXbr1prlnjvFMUTKANI0LHPYiAaWNmx3x77XvHtanSSmar1crZ8PmMP78aJrTbt5SHdLvAkEAwaKBe4RD1L73sZPsDyC7Nwk5WmX032caDu+Up8gF/8lyPQ4tO1hVP3kjdxnmaaJmLr6zJuUsADCbhAtDcSHKyQJABgB6qbQO+MU+4PMMM4NR9nr0DxE56FAOV4vy8xTxDwDPWl03LRxgcdFmIJHxjbVFBPH1pBAME5NWSl9s4sOMzQJAAsxJWLKRlFpqE9vSVmmfquzsMj0066F+w+Z8XGxMgeS0TOOOjcP5/1/NvmPWa4JYPaatygsXtqz9IkxTXigh2QJBAIix0BT4NDM2UxOS1u7e4tS7VLs95hCf0x2RXyAwOwlx/dPUgm9UgNUX0rcpDqjUaa0Q3G3PsGhsdwkUHTyC4BY=";
//     public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


	//微信支付 参数
	private static String WECHATPAY_APP_ID = "wxca6a7344a7ad01f2";    //昂达
//	public static String WECHATPAY_APP_ID = "wx5c5d736c6e36f416"; // 华硕

    /*private String APP_ID = "wx4164917f32b41662"; // APP_ID  应用从官方网站申请到的合法appId    测试账号
	private String APP_ID = "wx6c0279330a811860"; // APP_ID  应用从官方网站申请到的合法appId         测试账号
    private String APP_ID = "wx380b8664b7e36848"; // APP_ID  应用从官方网站申请到的合法appId    正式账号  牛玩*/

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。 目前
	 * Scope 支持传入多个 Scope 权限，用逗号分隔。 有关哪些 OpenAPI
	 * 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI 关于 Scope
	 * 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String WEIBO_SCOPE = /*"email";*/
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";

	public static String getWeiboAppkey() {
		initInstance();
		return WEIBO_APPKEY;
	}

	private static String WEIBO_APPKEY = "2381914778";
	// weibo
	public static final String WEIBO_REDIRECTURI = "https://api.weibo.com/oauth2/default.html";
	public static final String QQ_SCOPE = "all";

	//intent key
	public static final String KEY_LOGIN_CLASS = "result_class";
	public static final String KEY_HTML5_HELP_TITLE = "help_title";
	public static final String KEY_HTML5_HELP_URL = "help_url";

	//protocal constant
	public static final int RESOURCE_INT_GAMECENTER = 101;
	public static final int RESOURCE_INT_SDK = 102;

	//渠道号
	public static class ChnNo {
		public static final String ASUS = "70";
	}

	//新手推荐数量限定
	public static final int NOVICEGUIDANCE_SIZE = 6;

	/**
	 * 下载失败相关
	 */
	// apk文件损坏
	public static final int STAC_DOWNLOAD_APK_ERROR_FILE_BROKEN = 20001;
	// temp文件无法命名
	public static final int STAC_DOWNLOAD_APK_ERROR_RENAME = 20002;
	// 下载的temp文件超过了apk文件应用大小
	public static final int STAC_DOWNLOAD_APK_ERROR_OVERSIZE = 20003;
	// 网络失败
	public static final int STAC_DOWNLOAD_APK_ERROR_NETWORK = 20004;

	public static final String ERROR_DOWNLOAD_FILE_BROKEN = "File_Broken";
	public static final String ERROR_DOWNLOAD_NETWORK = "Network";

	public static final String STOP_REASON_USER_ACTIVE_STOP = "User_Active_Stop";
	public static final String STOP_REASON_SERVER_BUSY = "Server_Busy";
	public static final String STOP_REASON_OTHERS = "Other_Reason";
	public static final String STOP_REASON_NETWORK_INTERRUPT_STOP = "Network_Interrupt_Stop";

	private static StatisticManager mInstance;

	private StatisticManager() {

	}

	public static StatisticManager getInstance() {
		if (mInstance == null) {
			mInstance = new StatisticManager();
			App appContext = App.getAppContext();
			WEIXIN_APP_ID = appContext.getString(R.string.weixin_app_id);
			WEIXIN_APP_SECRET = appContext.getString(R.string.weixin_app_secret);
			QQ_APP_ID = appContext.getString(R.string.qq_app_id);
			BUGLY_APPID = appContext.getString(R.string.bugly_appid);
			WEIBO_APPKEY = appContext.getString(R.string.weibo_appkey);
			WECHATPAY_APP_ID = appContext.getString(R.string.wechatpay_appid);
			PARTNER = appContext.getString(R.string.alipay_partner);
			SELLER = appContext.getString(R.string.alipay_seller);
			RSA_PRIVATE = appContext.getString(R.string.alipay_rsa_private);
			CONSTANT_USER_AGGREMENT = appContext.getString(R.string.user_aggrement_url);
			CONSTANT_RECHARGE_HELP = appContext.getString(R.string.recharge_help_url);
			CONSTANT_CONSUME_HELP = appContext.getString(R.string.consume_help_url);
		}
		return mInstance;
	}

	/**
	 * 设置游戏详情上报URL 1、客户端所有的下载请求需带上以下参数: ?appid=xx&packid=xx&... 参数描述:
	 * [Packet.proto协议上的所有基本参数,包括
	 * udi,reqNo,chnNo,chnPos,clientId,clientPos,clientVer] appId=应用id;
	 * packId=安装包id
	 *
	 * @param url
	 * @return
	 */
	public String appDetialReprotUrl(String url) {
		return url + "&packid=xx";
	}


}
