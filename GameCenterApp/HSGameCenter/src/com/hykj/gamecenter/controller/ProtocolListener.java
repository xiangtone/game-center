
package com.hykj.gamecenter.controller;

import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Apps.RspGlobalConfig;
import com.hykj.gamecenter.protocol.Apps.UserCommentInfo;
import com.hykj.gamecenter.protocol.Apps.UserScoreInfo;
import com.hykj.gamecenter.protocol.Pay.RspAccRecharge;
import com.hykj.gamecenter.protocol.Pay.RspAccRechargeList;
import com.hykj.gamecenter.protocol.Pay.RspCheckAccRecharge;
import com.hykj.gamecenter.protocol.Pay.RspConsume;
import com.hykj.gamecenter.protocol.Pay.RspConsumeList;
import com.hykj.gamecenter.protocol.Pay.RspPayNotice;
import com.hykj.gamecenter.protocol.Pay.RspPayType;
import com.hykj.gamecenter.protocol.Pay.UserAccInfo;
import com.hykj.gamecenter.protocol.Pay.UserAppRoleInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.protocol.Updater.RspUpdate;

public interface ProtocolListener {

	final class KEY {
		// 是否是专题app list （热门，最新，专题，必备）
		public static final String SUBJECT_APPLIST = "SUBJECT_APPLIST";
		public static final String BK_COLOR = "BK_COLOR";
		public static final String BACK_PRESSED = "BACK_PRESSED";
		public static final String ADV_ENTRY = "ADV_ENTRY"; // 是从广告位进入
		// 应用的一级分类
		public static final String MAIN_TYPE = "appType";
		// 应用的二级分类
		public static final String SUB_TYPE = "appSubType";
		// 排序类型
		public static final String ORDERBY = "appOrderBy";
		// 分类名
		public static final String CATEGORY_NAME = "appCategoryName";

		public static final String TOPIC_INFO = "topic_info";
		// 分类信息
		public static final String GROUP_INFO = "group_info";
		// 首页应用位置ID
		public static final String APP_POSITION = "app_position";
		// 分组ID
		public static final String GROUP_ID = "group_id";
		// 分组class
		public static final String GROUP_CLASS = "group_class";
		// 分组type
		public static final String GROUP_TYPE = "group_type";
		// 分组order
		public static final String ORDER_TYPE = "order_type";

		// 启动帐号中心
		public static final String TO_ACC_CENTER_POS = "start_acc";
		// 应用列表
		public static final String ITEM_TYPE = "item_type";
	}

	// ITEM类别：1:显示缩略图 2:不显示缩略图
	final class ITEM_TYPE {
		public static final int SHOW_SNAPSHOT = 0;
		public static final int UNSHOW_SNAPSHOT = 1;
	}

	final class CLASSIFY_TYPE {

		public static final int APP_CLASSIFY = 0;
		public static final int GAME_CLASSIFY = 1;
	}

	// 应用一级分类
	// 应用类别：0=全部，11=应用，12=游戏，4=排行，
	final class MAIN_TYPE {
		public static final int ALL = 0;
		public static final int APP = 11; // 推荐 应用分类
		public static final int RANKING = 4; // 排行
		public static final int GAME_CLASS = 12;// 分类 游戏分类
		public static final int APP_CLASS = 13;// 分类
		public static final int TOPIC = 3;
		public static final int SEARCH = 5;
		public static final int JING_PIN = 6;
		public static final int GAME = 7;
	}

	// 应用二级分类
	// 应用类型：0=全部
	final class SUB_TYPE {
		public static final int ALL = 0;
	}

	// 排序类型：0=自动排序(下载量)，2=时间
	final class ORDER_BY {
		public static final int AUTO = 0;
		public static final int DOWNLOAD = 0;
		public static final int TIME = 2;
	}

	// 元素类型：1=App，2=Link，3=跳转至分类，4=跳转至网游或单机，5=跳转至专题（不支持跳转至推荐）
	final class ELEMENT_TYPE {
		public static final int TYPE_APP = 1;
		public static final int TYPE_LINK = 2;
		public static final int TYPE_SKIP_CLASS = 3;
		public static final int TYPE_SKIP_LOCAL_OR_ONLINE = 4;
		public static final int TYPE_SKIP_TIP = 5;
	}

	// 页尺寸：页页需要下发多少个
	final class PAGE_SIZE {
		public static int GENERAL = 36;
		public static int NO_THUMB = 24;
		public static int APPLIST = GENERAL;
		public static int SEARCH = 30;
		public static int CATEGORY = GENERAL;
		public static int CATEGORY_LIST = GENERAL;
		public static int TOPIC_LIST = 36;
		public static int USER_COMMENT_LIST = 20;
		public static int MAX_COUNT = 200; // 页尺寸上限
	}

	final class GROUP_TYPE {
		public static int ALL_ONLY_GAMES_TYPE = 1200; // 纯游戏
		// ,热门应用=1000按下载量排序，最新上线=1000按时间排序
		public static int ONLINE_TYPE = 2101;
		public static int LOCAL_TYPE = 2102;
		public static int TOPIC_TYPE = 3100; // 专题
		public static int MUST_GAMES_TYPE = 3202;// 游戏的装机必备
		public static int LAUNCH_RECOMMED_TYPE = 4101;
		public static int HOME_RECOMMED_TYPE = 4106;	//首页推荐（应用+游戏）
		public static int NICE_APP_TYPE = 4107; // 热门 推荐
		public static int NICE_GAME_TYPE = 4108;// 热门 游戏
		public static int SPLASH_TYPE = 4105;
		public static int HOT_SEARCH_GROUP_TYPE = 4104;
		public static int HOT_GAMES_GROUP_TYPE = 4103;
		public static int ALL_ONLY_APP_TYPE = 1100; // 纯应用
		public static int ALL_APP_AND_GAME_TYPE = 1000; // (目前包含APP和GAME，具体由后台配置)
		// 热门应用=1000按下载量排序，最新上线=1000按时间排序
		public static int MOST_POPULAR_APP = 1001;
		public static int MUST_APP_TYPE = 3102;// 应用的装机必备
		public static int CLASSIFY_GAME_TYPE = 1199;
		public static int GAME_RECOMMED_TYPE = 4102;
		public static int BOOT_RECOMMED_TYPE = 4101;// 启动推荐
		public static int SEARCH_HOT_WORDS = 4104; //热门搜索词
		public static int WIFI_ADV_RECOMMED_TYPE = 4110;//wifi广告推荐
	}

	final class GROUP_CLASS {
		public static int APP_CLASSIFY_CLASS = 11;// 应用分类
		public static int SUBJECT_CLASS = 31;// 专题（游戏跟应用，具体看后台的配置）
		public static int GAME_SUBJECT_CLASS = 32;// 游戏专题
		public static int GAME_CLASSIFY_CLASS = 12;// 游戏分类
		public static int RECOMMEND_CLASS = 41;// 推荐
	}

	final class HOME_PAGE_POSITION {
		public static final int CAROUSEL_POSITION = 1;// groupElem中posid==CAROUSEL_POSITION的添加到最上端广告位
		public static final int LIST_START_POSITION = 4;
		public static final int SHOW_TYPE_APP = 0; // 应用
		public static final int SHOW_TYPE_ADV = 1; // 广告位
	}

	final class DOWNLOAD_TO_HANDLE {
		public static final int UPADATE_STATE = 1;
		public static final int UPDATE_LIST = 2;
		public static final int UPDATE_PROGRESS = 3;
	}

	final class APP_OPEN_UPDATE {
		public static final int OPEN = 1;
	}

	final class ACTION_PATH {
		public static final String HOME_PAGE = "/homepage";
		public static final String APP_INFO = "/appinfo";
		public static final String GROUP_APP_LIST = "/groupapplist";
		public static final String GROUP_APP = "/groupapp";
		public static final String GAME_CLASS = "/gameclass";
	}

	final class ACTION {

		public static String ACTION_REPORT_LANUCH = "ReqReported";
		public static String ACTION_REPORT_LANUCH_RESPONSE = "RspReported";
		//
		// public static String ACTION_GLOBAL = "ReqGlobalConfig";
		// public static String ACTION_GLOBAL_RESPONSE = "RspGlobalConfig";
		//
		// public static String ACTION_RECOMMAPPS = "ReqRecommApps";
		// public static String ACTION_RECOMMAPPS_RESPONSE = "RspRecommApps";
		//
		// public static String ACTION_APPDETAIL = "ReqAppDetail";
		// public static String ACTION_APPDETAIL_RESPONSE = "RspAppDetail";
		//
		// public static String ACTION_APPLIST = "ReqTopApps";
		// public static String ACTION_APPLIST_RESPONSE = "RspTopApps";
		//
		// public static String ACTION_APPUPDATELIST = "ReqAppsUpdate";
		// public static String ACTION_APPUPDATELIST_RESPONSE = "RspAppsUpdate";
		//
		// public static String ACTION_REPORT_DOWNLOAD = "ReqDownRes";
		// public static String ACTION_REPORT_DOWNLOAD_RESPONSE = "RspDownRes";
		//
		// update
		public static String ACTION_UPDATE = "ReqUpdate";
		public static String ACTION_UPDATE_RESPONSE = "RspUpdate";
		//
		// public static String ACTION_GROUPLSIT = "ReqGroups";
		// public static String ACTION_GROUPLIST_RESPONSE = "RspGroups";
		//
		// public static String ACTION_GROUP_APPLIST = "ReqGroupApps";
		// public static String ACTION_GROUP_APPLIST_RESPONSE = "RspGroupApps";
		//
		// public static String ACTION_SEARCH = "ReqAppList4SearchKey";
		// public static String ACTION_SEARCH_RESPONSE = "RspAppList4SearchKey";

		// GC2.0 -----------------------------------------------------start
		public static String ACTION_GLOBAL_CONFIG = "ReqGlobalConfig";
		public static String ACTION_GLOBAL_CONFIG_RESPONSE = "RspGlobalConfig";

		public static String ACTION_GROUP_ELEMS = "ReqGroupElems";
		public static String ACTION_GROUP_ELEMS_RESPONSE = "RspGroupElems";

		public static String ACTION_APPINFO = "ReqAppInfo";
		public static String ACTION_APPINFO_RESPONSE = "RspAppInfo";

		public static String ACTION_DOWN_RESULT = "ReqDownRes";
		public static String ACTION_DOWN_RESULT_RESPONSE = "RspDownRes";

		public static String ACTION_APPS_UPDATE = "ReqAppsUpdate";
		public static String ACTION_APPS_UPDATE_RESPONSE = "RspAppsUpdate";

		public static String ACTION_APPSLIST4_SEARCHKEY = "ReqAppList4SearchKey";
		public static String ACTION_APPSLIST4_SEARCHKEY_RESPONSE = "RspAppList4SearchKey";

		public static String ACTION_USER_SCORE_INFO = "ReqUserScoreInfo";
		public static String ACTION_USER_SCORE_INFO_RESPONSE = "RspUserScoreInfo";

		public static String ACTION_USER_COMMENTS = "ReqUserComments";
		public static String ACTION_USER_COMMENTS_RESPONSE = "RspUserComments";

		public static String ACTION_ADD_COMMENTS = "ReqAddComment";
		public static String ACTION_ADD_COMMENTS_RESPONSE = "RspAddComment";

		public static String ACTION_REPORTED_INFO = "ReqReported";
		public static String ACTION_REPORTED_INFO_RESPONSE = "RspReported";

		public static String ACTION_REQPAYTYPE = "ReqPayType";// 支付方式
		public static String ACTION_REQPAYTYPE_RESPONSE = "RspPayType";

		public static String ACTION_REQOPENID = "ReqOpenId";
		public static String ACTION_REQOPENID_RESPONSE = "RspOpenId";

		public static String ACTION_VALIDATE = "ReqValidate";
		public static String ACTION_VALIDATE_RESPONSE = "RspValidate";

		public static String ACTION_BIND = "ReqBind";
		public static String ACTION_BIND_RESPONSE = "RspBind";

		public static String ACTION_RECHARGE = "ReqAccRecharge";
		public static String ACTION_RECHARGE_RESPONSE = "RspAccRecharge";

		public static String ACTION_PAY_NOTICE = "ReqPayNotice";
		public static String ACTION_PAY_NOTICE_RESPONSE = "RspPayNotice";

		public static String ACTION_ACCINFO = "ReqUserAccInfo";
		public static String ACTION_ACCINFO_RESPONSE = "RspUserAccInfo";

		public static String ACTION_APP_ROLES = "ReqGetUserAppRoles";
		public static String ACTION_APP_ROLES_RESPONSE = "RspGetUserAppRoles";

		public static String ACTION_CONSUME = "ReqConsume";
		public static String ACTION_CONSUME_RESPONSE = "RspConsume";

		public static String ACTION_RECHARGE_LIST = "ReqAccRechargeList";
		public static String ACTION_RECHARGE_RESPONSE_LIST = "RspAccRechargeList";

		public static String ACTION_RECHARGE_CHECKACC = "ReqCheckAccRecharge";
		public static String ACTION_RECHARGE_RESPONSE_CHECKACC = "RspCheckAccRecharge";

		public static String ACTION_CONSUME_LIST = "ReqConsumeList";
		public static String ACTION_CONSUME_LIST_RESPONSE = "RspConsumeList";

		public static String ACTION_SET_USERNAME = "ReqSetUserName";
		public static String ACTION_SET_USERNAME_RESPONSE = "RspSetUserName";

		public static String ACTION_BING_THIRD = "ReqThirdLogin";
		public static String ACTION_BIND_THIRD_RESPONSE = "RspThirdLogin";

		public static String ACTION_IMPROPERREPORT = "ReqAppInform";
		public static String ACTION_IMPROPERREPORT_RESPONSE = "RspAppInform";

		public static String ACTION_FEEDBACK = "ReqFeedback";
		public static String ACTION_FEEDBACK_RESPONSE = "RspFeedback";

		public static String ACTION_RECOMMAPP = "ReqRecommApp";
		public static String ACTION_RECOMMAPP_RESPONSE = "RspRecommApp";


		// GC2.0 -----------------------------------------------------end
	}

	final class ERROR {
		public static int ERROR_ACTION_MISMATCH = 0x1000;
		public static int ERROR_BAD_PACKET = 0x1001;
		public static int ERROR_ACTION_FAIL = 0x1002;
	}

	interface AbstractNetListener {
		void onNetError(int errCode, String errorMsg);
	}

	// public interface ReqRecommAppsListener extends AbstractNetListener
	// {
	//
	// public void onReqRecommAppsFailed( int statusCode , String errorMsg );
	//
	// public void onReqRecommAppsSucceed( List< RecommAppInfo > infoes );
	// }
	//
	// public interface ReqAppDetailListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqAppDetailSucceed( AppInfo info );
	// }
	//
	// public interface ReqAppListListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqAppListSucceed( List< AppInfo > infoes );
	// }
	//
	// public interface ReqAppUpdateListListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqAppUpdateListSucceed( List< AppInfo > infoes );
	// }
	//
	interface ReqUpdateListener extends AbstractNetListener {

		/**
		 * @param statusCode 检查更新错误
		 * @param errorMsg   错误信息
		 */
		void onReqFailed(int statusCode, String errorMsg);

		void onRequpdateSucceed(RspUpdate updateInfo);
	}

	//
	// public interface ReqGroupListListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqGroupListSucceed( List< GroupInfo > infoes );
	// }
	//
	// public interface ReqGroupAppListListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqGroupAppListSucceed( List< AppInfo > infoes );
	// }
	//
	// public interface ReqSearchListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqSearchSucceed( List< AppInfo > infoes );
	// }
	//
	// public interface ReqHotGamesListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqHotGamesSucceed( List< AppInfo > infoes );
	// }
	//
	// public interface ReqHotSearchListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqHotSearchSucceed( List< String > infoes );
	// }
	//
	// public interface ReqGlobalConfigListener extends AbstractNetListener
	// {
	//
	// public void onReqFailed( int statusCode , String errorMsg );
	//
	// public void onReqGroupConfigSucceed( int thumbSwitch );
	//
	// }
	//
	interface ReportLanuchInfoListener extends AbstractNetListener {

		final class RESCODE_STATE {
			public static final int REPORT_SUCCEED = 0x0; // 无新消息
			public static final int REPORT_FAILED = 0x1;// 有新消息
		}

		void onReqFailed(int statusCode, String errorMsg);

		void onReportLanuchInfoSucceed();

	}

	// GC2.0协议 start-----------------------------------------------------
	interface ReqGlobalConfigListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqGlobalConfigSucceed(RspGlobalConfig config);

	}

	interface ReqGroupElemsListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqGroupElemsSucceed(GroupElemInfo[] infoList,
									String serverDataVer);

	}

	interface ReqAppInfoListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqAppInfoSucceed(AppInfo appInfo, String serverCacheVer);

	}

	interface ReqDownResultListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqDownResultSucceed();

	}

	interface RepAppsUpdateListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onRepAppsUpdateSucceed(AppInfo[] infoList);

	}

	interface RepUserAppsListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onRepAppsUpdateSucceed(AppInfo[] infoList);

	}

	interface ReqAppList4SearchKeyListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqAppList4SearchKeySucceed(GroupElemInfo[] infoList);

	}

	interface ReqUserScoreInfoListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqUserScoreInfoSucceed(UserScoreInfo userScoreInfo);

	}

	interface ReqUserCommentsListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqUserCommentsSucceed(UserCommentInfo[] list);

	}

	interface ReqAddCommentListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqAddCommentSucceed(long commentId, String userName);

	}

	// 支付方式
	interface ReqPayTypeListener extends AbstractNetListener {

		void onReqPayTypeFailed(int statusCode, String errorMsg);

		void onReqPayTypeSucceed(RspPayType rpPayType);
	}

	// 获取 openid
	interface ReqOpenIdListener extends AbstractNetListener {
		void onReqFailed(int statusCode, String errorMsg);

		void onReqOpenIdSucceed(AccountInfo accountInfo);
	}

	// 获取 验证码
	interface ReqValidateListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqValidateSucceed();
	}

	// 绑定手机
	interface ReqBindListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqBindSucceed(AccountInfo account);
	}

	// 获取账户信息
	interface ReqUserAccInfoListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqUserAccInfoSucceed(UserAccInfo accInfo);
	}

	// 提交支付订单
	interface ReqRechargeListener extends AbstractNetListener {

		void onReqRechargeFailed(int statusCode, String errorMsg);

		void onReqRechargeSucceed(RspAccRecharge rspRecharge);
	}

	// 提交支付成功回应
	interface ReqPayNoticeListener extends AbstractNetListener {

		void onReqPayNoticeFailed(int statusCode, String errorMsg);

		void onReqPayNoticeSucceed(RspPayNotice rspPayNotice);
	}

	// 获取角色信息
	interface ReqGetUserAppRolesListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqGetUserAppRolesSucceed(UserAppRoleInfo roleInfo);
	}

	//消费
	interface ReqConsumeListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onConsumeSucceed(RspConsume rspData);

	}

	//获取充值列表
	interface ReqRechargeListListener extends AbstractNetListener {

		void onReqRechargeListFailed(int statusCode, String errorMsg);

		void onReqRechargeListSucceed(RspAccRechargeList rspRecharge);
	}

	//检查游戏中心单个订单充值状态
	interface ReqCheckAccListener extends AbstractNetListener {

		void onReqCheckAccFailed(int statusCode, String errorMsg);

		void onReqCheckAccSucceed(RspCheckAccRecharge rspCheck);
	}

	//获取消费列表
	interface ReqConsumeListListener extends AbstractNetListener {

		void onReqConsumeListFailed(int statusCode, String errorMsg);

		void onReqConsumeListSucceed(RspConsumeList rspConsume);

	}

	//修改用户名
	interface ReqSetUserNameListener extends AbstractNetListener {
		void onReqFailed(int statusCode, String errorMsg);

		void onReqSetUserNameSucceed();
	}

	interface ReqReportedListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqReportedSucceed();

	}

	// 绑定第三方账号
	interface ReqThirdLoginListener extends AbstractNetListener {

		void onReqFailed(int statusCode, String errorMsg);

		void onReqThirdLoginSucceed(AccountInfo account);
	}

	// 游戏举报
	interface ReqImproperReportListener extends AbstractNetListener {

		void onReqImproperReportFailed(int statusCode, String errorMsg);

		void onReqImproperReportSucceed(int statusCode, String errorMsg);
	}


	// 用户反馈
	interface ReqFeedbackListener extends AbstractNetListener {
		void onReqFailed(int statusCode, String errorMsg);

		void onReqFeedbackSucceed();
	}

	// 获取游戏推荐
	interface ReqRecommAppListener extends AbstractNetListener {
		void onReqFailed(int statusCode, String errorMsg);

		void onReqRecommAppSucceed(GroupElemInfo[] infoList,
								   String serverDataVer);
	}
	// GC2.0协议 end--------------------------------------------------------------

}
