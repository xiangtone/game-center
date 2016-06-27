package com.hykj.gamecenter.statistic;


import com.hykj.gamecenter.controller.ProtocolListener;
import com.hykj.gamecenter.controller.ReqDownResultController;
import com.hykj.gamecenter.controller.ReqReportedInfoController;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.protocol.Reported;
import com.hykj.gamecenter.utils.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by OddsHou on 2015/12/2.
 */
public class ReportConstants {

	/**
	 * 下载暂停、成功上报类型相关
	 */
	public static final int STAC_DOWNLOAD_APK_START = 1;
	// 升级界面下载任务上报
	public static final int STAC_UPGRADE_DOWNLOAD_APK_START = 2;
	// 下载成功
	public static final int STAC_DOWNLOAD_APK_SUCCESS = 0;
	// 用户主动暂停
	public static final int STAC_DOWNLOAD_APK_USER_ACTIVE_STOP = 11;
	// 网络中断暂停
	public static final int STAC_DOWNLOAD_APK_NETWORK_INTERRUPT_STOP = 12;
	// 服务器繁忙等原因暂停
	public static final int STAC_DOWNLOAD_APK_SERVER_BUSY_STOP = 13;
	// 其他原因暂停
	public static final int STAC_DOWNLOAD_APK_OTHERS_STOP = 14;
	// 暂停任务后继续断点续传
	public static final int STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME = 21;
	// 取消下载任务
	public static final int STAC_DOWNLOAD_APK_CANCEL_TASK = 22;
	private static final String TAG = "ReportConstants";
	private static ReportConstants mInstance;

	private ReportConstants() {

	}

	public static ReportConstants getInstance() {
		if (mInstance == null) {
			mInstance = new ReportConstants();
			//缓存数据，加入
			DatabaseUtils.queryReportData(mReportedInfo);
		}
		return mInstance;
	}

	/**
	 * 新手推荐 点击安装的时候才会调用 STAC_APP_POSITION_NEW_PERSON = 1000000;
	 */
	public static final int STAC_APP_POSITION_NEW_PERSON = 1000000;
	public static final String PAGE_NEW_PERSON_RECOMMEND = "New_Person_Recommend_Page";
	/**
	 * 首页位置(home推荐界面) STAC_APP_POSITION_FIRST_PAGE = 10000000; 位置1= 100000001
	 */
	public static final int STAC_APP_POSITION_RECOMM_PAGE = 10000000;// 首页推荐的广告位点击
	public static final String PAGE_RECOMMEND = "Recommend_Page";// 用来上报腾讯云分析的
	/**
	 * 首页(home推荐界面)新品推荐
	 */
	public static final int STAC_APP_POSITION_RECOMM_PAGE_NEWEST = 10200000;
	public static final String PAGE_RECOMMED_NEWST = "Recommened_Page_Newest";
	/**
	 * 首页(home推荐界面)精品应用
	 */
	public static final int STAC_APP_POSITION_RECOMM_PAGE_NICE = 10100000;
	public static final String PAGE_RECOMMED_NICE = "Recommened_Page_Nice";
	/**
	 * 首页位置(游戏推荐界面) STAC_APP_POSITION_FIRST_PAGE = 2000000; 位置1= 2000001 位置2=
	 * 2000002 位置3= 2000003
	 */
	public static final int STAC_APP_POSITION_GAME_PAGE = 2000000;
	public static final String PAGE_GAME = "Game_Page";
	/**
	 * 首页(游戏推荐界面)新品推荐
	 */
	public static final int STAC_APP_POSITION_GAME_PAGE_HOTEST = 2100000;
	public static final String PAGE_GAME_HOTST = "Game_Page_Hotest";
	/**
	 * 首页(游戏推荐界面)新品推荐
	 */
	public static final int STAC_APP_POSITION_GAME_PAGE_NEWEST = 2200000;
	public static final String PAGE_GAME_NEWST = "Game_Page_Newest";
	/**
	 * 首页(游戏推荐界面)精品应用
	 */
	public static final int STAC_APP_POSITION_GAME_PAGE_NICE = 2100000;
	public static final String PAGE_GAME_NICE = "Game_Page_Nice";
	/**
	 * 排行位置 STAC_APP_POSITION_LOCAL_GAME = 3000000;
	 */
	public static final int STAC_APP_POSITION_RANK = 3000000;
	public static final String PAGE_RANK = "Rank_Page";
	/**
	 * 网络游戏位置 STAC_APP_POSITION_WEB_GAME = 3100000;
	 */
	public static final int STAC_APP_POSITION_WEB_GAME = 3100000;
	/**
	 * 单机游戏位置 STAC_APP_POSITION_LOCAL_GAME = 3200000;
	 */
	public static final int STAC_APP_POSITION_LOCAL_GAME = 3200000;
	public static final int STAC_APP_POSITION_RANK_ONLY_APP = 3300000;
	public static final int STAC_APP_POSITION_RANK_GAME = 3400000;

	/**
	 * 游戏详情 STAC_APP_POSITION_APP_DETAIL = 5000000;
	 */
	public static final int STAC_APP_POSITION_APP_DETAIL = 5000000;
	/**
	 * 游戏推荐列表位置id
	 */
	public static final int STAC_APP_POSITION_RECOMMED_APP = 5100000;
	/**
	 * 游戏分类 STAC_APP_POSITION_GROUP_GAME = 6000000;
	 */
	public static final int STAC_APP_POSITION_GROUP_GAME = 6000000;
	public static final String PAGE_CLASSIFY = "Classify_Page";
	/**
	 * 游戏分类应用列表 STAC_APP_POSITION_GROUP_GAME_APP = 6100000;
	 */
	public static final int STAC_APP_POSITION_GROUP_GAME_APP = 6100000;
	/**
	 * 精品专题 STAC_APP_POSITION_TIP_GAME = 7000000;
	 */
	public static final int STAC_APP_POSITION_TIP_GAME = 7000000;
	public static final String PAGE_TIP = "Tip_Page";
	/**
	 * 精品专题应用列表 即 专题详情 STAC_APP_POSITION_TIP_GAME_APP = 7100000;
	 */
	public static final int STAC_APP_POSITION_TIP_GAME_APP = 7100000;
	/**
	 * 搜索页 STAC_APP_POSITION_TIP_GAME_APP = 8000000;
	 */
	public static final int STAC_APP_POSITION_SEARCH_GAME_APP = 8000000;
	public static final String PAGE_SEARCH = "Search_Page";
	/**
	 * 热门游戏 STAC_APP_POSITION_TIP_GAME_APP = 8100000;
	 */
	public static final int STAC_APP_POSITION_SEARCH_HOT_GAME_APP = 8100000;
	/**
	 * 热门搜索词 STAC_APP_POSITION_TIP_GAME_APP = 8200000;
	 */
	public static final int STAC_APP_POSITION_SEARCH_HOT_WORD_APP = 8200000;
	/**
	 * 下载管理 STAC_APP_POSITION_DOWNLOAD_MANAGER_APP = 9000000;
	 */
	public static final int STAC_APP_POSITION_DOWNLOAD_MANAGER_APP = 9000000;
	/**
	 * 升级管理 STAC_APP_POSITION_DOWNLOAD_MANAGER_APP = 9000000;
	 */
	public static final int STAC_APP_POSITION_DOWNLOAD_UPDATE_APP = 9100000;
	/**
	 * 推送进入
	 */
	public static final int STAC_APP_POSITION_NOTIFY = 11000000;
	/**
	 * WIFI页
	 */
	public static final int STAC_APP_POSITION_WIFI = 12000000;

	//============================================================以下为行为上报定义
	/**
	 * 首次启动
	 */
	public static final int STATACT_ID_LAUNCH = 60001;
	/**
	 * 广告位点击
	 */
	public static final int STATACT_ID_ADV = 60002;
	/**
	 * 个人中心
	 */
	public static final int STATACT_ID_PERSON = 60003;
	/**
	 * 充值
	 */
	public static final int STATACT_ID_RECHARGE = 60004;
	/**
	 * 启动游戏
	 */
	public static final int STATACT_ID_GAME_START = 60005;
	/**
	 * 启动升级
	 */
	public static final int STATACT_ID_UPDATE_START = 60006;
	/**
	 * 升级成功
	 */
	public static final int STATACT_ID_UPDATE_SUCCEED = 60007;
	/**
	 * 第三方登录
	 */
	public static final int STATACT_ID_THIRD_LOGIN = 60008;
	/**
	 * 访问当前页面统计
	 */
	public static final int STATACT_ID_PAGE_VISIT = 60009;
	/**
	 * 权限详情页面统计
	 */
	public static final int STATACT_ID_IMPROPER_DETAIL = 60010;
	public static final String PAGE_DOWNLOAD_MANAGER = "Download_Magage_Page";

	// 统计类别
	public static final class STATIS_TYPE {
		public static final int RECOM = 1;// 推荐
		public static final int RANKING = 2; // 排行
		public static final int SUBJECT = 3; // 专题
		public static final int WEB = 4; // 网络游戏
		public static final int LOC = 5; // 单机游戏
		public static final int NEED = 6; //
		public static final int CLASSIFY = 7;// 分类
		public static final int APP_INFO = 8; // 详情
		public static final int SEARCH = 9;// 搜索
		public static final int DOWNLOAD = 10;// 管理中心
		public static final int GAME = 11;// 游戏Tab
		public static final int NOTIFY = 12;//通知（推送）
		public static final int RANKING_GAME = 21;
		public static final int RANKING_ONLY_APP = 22;
		public static final int WIFI_CONNECT = 30;//WIFI推荐页
		public static final int APP_INFO_RECOMMED = 80;//详情推荐
		public static final int RECOM_ADV = 101;// 推荐广告1
		public static final int RECOM_NICE = 102;// 精品应用
		public static final int RECOM_NEWEST = 103;// 最新应用
		// public static final int RECOMADV2 = 104;//推荐广告2
		// public static final int RECOMADV3 = 105;//推荐广告3
		public static final int NEW_PERSON_RECOM = 106;// 新手推荐
		public static final int INSTALL_REQUIRED = 107;// 装机必备
		public static final int HOTEST_LIST = 108; // 热门应用(包括游戏)
		public static final int GAME_HOTEST_LIST = 109; // 热门应用(包括游戏)
		public static final int GAME_INSTALL_REQUIRED = 110;// 装机必备
		public static final int SUBJECT_LIST = 301;// 专题列表
		public static final int GAME_SUBJECT_LIST = 302;// 专题列表
		public static final int CLASSIFY_LIST = 701;// 分类列表
		public static final int GAME_ADV = 1101;// 推荐广告1
		public static final int GAME_NICE = 1102;// 精品游戏
		public static final int GAME_NEWEST = 1103;// 最新游戏
		public static final int RECOM_NICE_LIST = 10201;
		public static final int RECOM_NEWEST_LIST = 10301;
		public static final int GAME_NICE_LIST = 110201;
		public static final int GAME_NEWEST_LIST = 110301;//与GAME_HOTEST_LIST 109 同样
	}

	// 得到位置编号
	public static int reportPos(int appType) {
		int pos = 0;
		switch (appType) {
			case STATIS_TYPE.RECOM:
				pos = STAC_APP_POSITION_RECOMM_PAGE;
				break;
			case STATIS_TYPE.RECOM_ADV:
				pos = STAC_APP_POSITION_RECOMM_PAGE + 1;
				break;
			case STATIS_TYPE.RECOM_NICE:
				pos = STAC_APP_POSITION_RECOMM_PAGE + 2;
				break;
			case STATIS_TYPE.RECOM_NEWEST:
				pos = STAC_APP_POSITION_RECOMM_PAGE + 3;
				break;
			case STATIS_TYPE.RECOM_NICE_LIST:
				pos = STAC_APP_POSITION_RECOMM_PAGE_NICE;
				break;
			case STATIS_TYPE.RECOM_NEWEST_LIST:
				pos = STAC_APP_POSITION_RECOMM_PAGE_NEWEST;
				break;
			case STATIS_TYPE.NEW_PERSON_RECOM:
				pos = STAC_APP_POSITION_NEW_PERSON;
				break;
			case STATIS_TYPE.GAME:
				pos = STAC_APP_POSITION_GAME_PAGE;
				break;
			case STATIS_TYPE.GAME_NICE:
				pos = STAC_APP_POSITION_GAME_PAGE + 2;
				break;
			case STATIS_TYPE.GAME_NEWEST:
				pos = STAC_APP_POSITION_GAME_PAGE + 3;
				break;
			case STATIS_TYPE.GAME_NICE_LIST:
				pos = STAC_APP_POSITION_GAME_PAGE_NICE;
				break;
			case STATIS_TYPE.GAME_HOTEST_LIST:
				pos = STAC_APP_POSITION_GAME_PAGE_HOTEST;
				break;
			case STATIS_TYPE.GAME_NEWEST_LIST:
				pos = STAC_APP_POSITION_GAME_PAGE_NEWEST;
				break;
			case STATIS_TYPE.GAME_ADV:
				pos = STAC_APP_POSITION_GAME_PAGE + 1;
				break;
			// case STATIS_TYPE.RECOMADV2 :
			// pos = STAC_APP_POSITION_GAME_PAGE + 2;
			// break;
			// case STATIS_TYPE.RECOMADV3 :
			// pos = STAC_APP_POSITION_GAME_PAGE + 3;
			// break;
			case STATIS_TYPE.SUBJECT:
				pos = STAC_APP_POSITION_TIP_GAME;
				break;
			case STATIS_TYPE.SUBJECT_LIST:
				pos = STAC_APP_POSITION_TIP_GAME_APP;
				break;
			case STATIS_TYPE.WEB:
				pos = STAC_APP_POSITION_WEB_GAME;
				break;
			case STATIS_TYPE.LOC:
				pos = STAC_APP_POSITION_LOCAL_GAME;
				break;
			case STATIS_TYPE.APP_INFO:
				pos = STAC_APP_POSITION_APP_DETAIL;
				break;
			case STATIS_TYPE.APP_INFO_RECOMMED:
				pos = STAC_APP_POSITION_RECOMMED_APP;
				break;
			case STATIS_TYPE.CLASSIFY:
				pos = STAC_APP_POSITION_GROUP_GAME;
				break;
			case STATIS_TYPE.CLASSIFY_LIST:
				pos = STAC_APP_POSITION_GROUP_GAME_APP;
				break;
			case STATIS_TYPE.RANKING:
				pos = STAC_APP_POSITION_RANK;
				break;
			case STATIS_TYPE.RANKING_ONLY_APP:
				pos = STAC_APP_POSITION_RANK_ONLY_APP;
				break;
			case STATIS_TYPE.WIFI_CONNECT:
				pos = STAC_APP_POSITION_WIFI;
			case STATIS_TYPE.RANKING_GAME:
				pos = STAC_APP_POSITION_RANK_GAME;
				break;
			case STATIS_TYPE.DOWNLOAD:
				pos = STAC_APP_POSITION_DOWNLOAD_MANAGER_APP;
				break;
			case STATIS_TYPE.SEARCH:
				pos = STAC_APP_POSITION_SEARCH_GAME_APP;
				break;
			case STATIS_TYPE.NOTIFY:
				pos = STAC_APP_POSITION_NOTIFY;
				break;
			default:
				pos = 0;
				Logger.i("StatisManager", "应用位置定位出现异常");
				break;
		}

		return pos;
	}


	// 根据操作位置获取MTA page参数值,用来统计下载请求原始界面分布情况
	public static String getPageFrom(int pagePostion) {
		String page = "";// "error_page" + mPagePostion;
		switch (pagePostion) {
			case STATIS_TYPE.RECOM:
			case STATIS_TYPE.RECOM_ADV:
				page = PAGE_RECOMMEND;
				break;

			case STATIS_TYPE.RECOM_NICE:
			case STATIS_TYPE.RECOM_NICE_LIST:
				page = PAGE_RECOMMED_NICE;
				break;
			case STATIS_TYPE.RECOM_NEWEST:
			case STATIS_TYPE.RECOM_NEWEST_LIST:
				page = PAGE_RECOMMED_NEWST;
				break;
			case STATIS_TYPE.NEW_PERSON_RECOM:
				page = PAGE_NEW_PERSON_RECOMMEND;
				break;
			case STATIS_TYPE.RANKING:
				page = PAGE_RANK;
				break;

			case STATIS_TYPE.SUBJECT:
			case STATIS_TYPE.SUBJECT_LIST:
				page = PAGE_TIP;
				break;

			case STATIS_TYPE.CLASSIFY:
			case STATIS_TYPE.CLASSIFY_LIST:
				page = PAGE_CLASSIFY;
				break;

			case STATIS_TYPE.SEARCH:
				page = PAGE_SEARCH;
				break;

			case STATIS_TYPE.DOWNLOAD:
				page = PAGE_DOWNLOAD_MANAGER;
				break;

			case STATIS_TYPE.GAME:
			case STATIS_TYPE.GAME_ADV:
				page = PAGE_GAME;
				break;

			case STATIS_TYPE.GAME_NEWEST:
			case STATIS_TYPE.GAME_NEWEST_LIST:
				page = PAGE_GAME_NEWST;
				break;

			case STATIS_TYPE.GAME_NICE:
			case STATIS_TYPE.GAME_NICE_LIST:
				page = PAGE_GAME_NICE;
				break;
			default:
				page = "error_page" + pagePostion;
				break;
		}
		return page;
	}

	public List<Reported.ReportedInfo> getmReportedInfo() {
		return mReportedInfo;
	}

	private static List<Reported.ReportedInfo> mReportedInfo = new ArrayList<Reported.ReportedInfo>();
	/**
	 * 5条上报记录上报一次开关
	 */
	private boolean mIsDebug = Logger.isDebug;

	/**
	 * 统计上报 oddshou
	 */
	public void reportReportedInfo(Reported.ReportedInfo reportedInfo) {
		reportedInfo.actionTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
				.format(new Date());
		mReportedInfo.add(reportedInfo);
		Logger.e(TAG, "reportReportedInfo " + reportedInfo.statActId + " " + reportedInfo.ext1, "oddshou");
		if (mIsDebug || mReportedInfo.size() >= 5) {
			//五条上报一次
			ReqReportedInfoController controller = new ReqReportedInfoController(
					mReportedInfo.toArray(new Reported.ReportedInfo[mReportedInfo.size()]), null);
			//		controller.setClientPos(positionId);    //多条上报，不设置 position id
			controller.doRequest();
			mReportedInfo.clear();
			DatabaseUtils.deleteAllReport();    //清空缓存
			return;
		}

		DatabaseUtils.insertReportData(reportedInfo);
	}
	//把剩余的不足五条的全部上报
	public void reportAllLast() {
		if (mReportedInfo.size() <= 0) {
			return;
		}
		//五条上报一次
		ReqReportedInfoController controller = new ReqReportedInfoController(
				mReportedInfo.toArray(new Reported.ReportedInfo[mReportedInfo.size()]), null);
		//		controller.setClientPos(positionId);    //多条上报，不设置 position id
		controller.doRequest();
		mReportedInfo.clear();
		DatabaseUtils.deleteAllReport();    //清空缓存
	}


	/**
	 * 统计上报, 立即上报 oddshou
	 */
	public void reportReportedInfoNow(Reported.ReportedInfo reportedInfo, ProtocolListener.ReqReportedListener listener) {
		reportedInfo.actionTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
				.format(new Date());
		Logger.e(TAG, "reportReportedInfoNow " + reportedInfo, "oddshou");
		ReqReportedInfoController controller = new ReqReportedInfoController(
				new Reported.ReportedInfo[]{reportedInfo}, listener);
		controller.doRequest();
	}

	// 上报下载成功 需带上下载完成总时 、下载速度
	public static void reportDownloadSuccessed(int appid, int packid, int pos,
											   String timeconsume, String downloadspeed, int groupId) {
		ReqDownResultController controller = new ReqDownResultController(appid,
				packid, STAC_DOWNLOAD_APK_SUCCESS, "", timeconsume,
				downloadspeed, groupId, null);
		controller.setClientPos(pos);
		controller.doRequest();
	}

	// 上报任务开始下载
	public static void reportDownloadStart(int appid, int packid, int pos) {
		ReqDownResultController controller = new ReqDownResultController(appid,
				packid, STAC_DOWNLOAD_APK_START, "", "", "", null);
		controller.setClientPos(pos);
		controller.doRequest();
	}

	// 上报升级界面任务开始下载
	public static void reportUpgradeDownloadStart(int appid, int packid, int pos) {
		ReqDownResultController controller = new ReqDownResultController(appid,
				packid, STAC_UPGRADE_DOWNLOAD_APK_START, "", "", "", null);
		controller.setClientPos(pos);
		controller.doRequest();
	}

	// 上报暂停
	public static void reportDownloadStop(int appid, int packid, int pos,
										  int downloadres, String remarks) {
		ReqDownResultController controller = new ReqDownResultController(appid,
				packid, downloadres, remarks, null);
		controller.setClientPos(pos);
		controller.doRequest();
	}

	// 上报暂停继续续传
	public static void reportDownloadResume(int appid, int packid, int pos,
											int downloadres, String remarks) {
		ReqDownResultController controller = new ReqDownResultController(appid,
				packid, downloadres, remarks, null);
		controller.setClientPos(pos);
		controller.doRequest();
	}

	public static void reportDownloadFail(int appId, int packId, int pos,
										  int errorCode, String msg) {
		ReqDownResultController controller = new ReqDownResultController(appId,
				packId, errorCode, msg, null);
		controller.setClientPos(pos);
		controller.doRequest();
	}
}
