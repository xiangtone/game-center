/**   
 * @Title: DataFetcher.java
 * @Package com.mas.amineappstore.http
 * @Description: TODO 
 
 * @date 2013-12-16 下午01:44:45
 * @version V1.0   
 */

package com.x.publics.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.x.publics.http.model.AccordDownloadRequest;
import com.x.publics.http.model.AppDownloadLogRequest;
import com.x.publics.http.model.AppGamesCategoryRequest;
import com.x.publics.http.model.AppsUpgradeRequest;
import com.x.publics.http.model.BannerRequest;
import com.x.publics.http.model.CategoryDetailRequest;
import com.x.publics.http.model.CategoryRequest;
import com.x.publics.http.model.CommentRequest;
import com.x.publics.http.model.CountryRequest;
import com.x.publics.http.model.DetailRequest;
import com.x.publics.http.model.FeedbackDialogueRequest;
import com.x.publics.http.model.FeedbackListRequest;
import com.x.publics.http.model.FeedbackRequest;
import com.x.publics.http.model.FeedbackWarnRequest;
import com.x.publics.http.model.FindPwdRequest;
import com.x.publics.http.model.HomeCollectionDetailRequest;
import com.x.publics.http.model.HomeCollectionRequest;
import com.x.publics.http.model.HomeMustHaveRequest;
import com.x.publics.http.model.KeywordsRequest;
import com.x.publics.http.model.LoginRequest;
import com.x.publics.http.model.LogoutRequest;
import com.x.publics.http.model.MachineActivateRequest;
import com.x.publics.http.model.ModifyUserNickNameRequest;
import com.x.publics.http.model.ModifyUserPwdRequest;
import com.x.publics.http.model.PlatFormInitRequest;
import com.x.publics.http.model.RaveCrashRequest;
import com.x.publics.http.model.RecommendRequest;
import com.x.publics.http.model.RegisterRequest;
import com.x.publics.http.model.RingtonesRequest;
import com.x.publics.http.model.SearchRequest;
import com.x.publics.http.model.SearchTipsRequest;
import com.x.publics.http.model.SkinAttentionRequest;
import com.x.publics.http.model.SkinDownloadRequest;
import com.x.publics.http.model.SkinListRequest;
import com.x.publics.http.model.TabRequest;
import com.x.publics.http.model.UpgradeRequest;
import com.x.publics.http.model.WallpaperRequest;
import com.x.publics.http.volley.RequestQueue;
import com.x.publics.http.volley.Request.Method;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.http.volley.toolbox.JsonObjectRequest;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.ui.view.zxing.decoding.Intents.Encode;

/**
 * @ClassName: DataFetcher
 * @Description: 数据请求中心
 
 * @date 2013-12-16 下午01:44:45
 * 
 */

public class DataFetcher {

	private static DataFetcher instance;
	public static RequestQueue requestQueue;

	// 广告banner
	private static String COMMON_BANNER_URL = Host.URL
			+ "rs/rave/hometheme?ct=%s&raveId=%s";
	// 分类
	private static String COMMON_CATEGORY_URL = Host.URL
			+ "rs/rave/category?raveId=%s&ct=%s&ps=%s&pn=%s";
	// 资源类型列表
	private static String CATEGORY_DETAIL_LIST_URL = Host.URL
			+ "rs/rave/categorylist?column=%s&categoryId=%s&ps=%s&pn=%s&raveId=%s";

	// 应用管理更新
	private static String APPS_UPGRADE_URL = Host.URL + "rs/rave/getUpdateApps";
	// 应用列表
	private static String COMMON_APP_LIST_URL = Host.URL
			+ "rs/rave/list?raveId=%s&ct=%s&ps=%s&pn=%s";
	// 评论列表
	private static String COMMENT_LIST_URL = Host.URL
			+ "rs/rave/commentlist?appId=%s&ps=%s&pn=%s";
	// 应用详情（byApkId）
	private static String APP_DETAIL_URL = Host.URL
			+ "rs/rave/app?apkId=%s&ct=%s&raveId=%s";
	// 应用详情（byPackageName）
	private static String APP_DETAIL_URL2 = Host.URL
			+ "rs/rave/appPackageName?packageName=%s&ct=%s&raveId=%s";
	// 添加评论
	private static String ADD_COMMENT_URL = Host.URL + "rs/rave/comment";
	// 应用搜索
	private static String APP_SEARCH_URL = Host.URL + "rs/rave/searchApps";
	// 平台崩溃
	private static String APP_CRASH_REPORT_URL = Host.URL + "rs/rave/debacle";
	// 应用推荐
	private static String APP_RECOMMEND_URL = Host.URL
			+ "rs/rave/recommendlist?categoryId=%s&appId=%s&ps=%s&pn=%s&raveId=%s";
	// 用户反馈
	private static String ADD_FEEDBACK = Host.URL + "rs/rave/feedback";
	// 用户反馈（回馈提醒接口（rc=30022）
	private static String FEEDBACK_WARN = Host.URL
			+ "rs/rave/feedbackAttention?imei=%s";
	// 用户反馈（常见回馈列表接口（rc=30023)）
	private static String FEEDBACK_LIST = Host.URL
			+ "rs/rave/getFeedbackCommon?ps=%s&pn=%s";
	// 用户反馈（用户回馈记录接口（rc=30024）
	private static String FEEDBACK_CLIENT = Host.URL
			+ "rs/rave/getFeedbackClient?imei=%s&ps=%s&pn=%s";
	// App下载统计
	private static String ACCORD_APP_DOWNLOAD = Host.URL
			+ "rs/rave/appDownLoad";
	// 平台升级
	private static String PLATFORM_UPGRADE = Host.URL + "rs/rave/upgrade";
	// 获取图片栏目（HOT、TOP）
	private static String WALLPAPER_URL = Host.URL
			+ "rs/res/image/list?raveId=%s&ct=%s&ps=%s&pn=%s";
	// 获取图片栏目（ALBUM）
	private static String WALLPAPER_ALBUM_URL = Host.URL
			+ "rs/res/image/theme?raveId=%s&ct=%s&ps=%s&pn=%s";
	// 获取图片栏目（CATEGORIES）
	private static String WALLPAPER_CATEGORIES_URL = Host.URL
			+ "rs/rave/category?raveId=%s&ct=%s&ps=%s&pn=%s";
	// 获取图片栏目（CATEGORIES）二级界面
	private static String WALLPAPER_CATEGORIES_DETAIL_URL = Host.URL
			+ "rs/res/image/categorylist?column=%s&categoryId=%s&ps=%s&pn=%s&raveId=%s";
	// 获取图片栏目（ALBUM）二级界面
	private static String WALLPAPER_ALBUM_DETAIL_URL = Host.URL
			+ "rs/res/image/themelist?themeId=%s&ps=%s&pn=%s";
	// 图片专辑，一键下载
	private static String WALLPAPER_ALBUM_ONE_CLICK_URL = Host.URL
			+ "rs/res/image/themelist?themeId=%s";
	// 图片搜索
	private static String WALLPAPER_SEARCH_URL = Host.URL
			+ "rs/res/searchImage";
	// 获取音乐栏目 （HOT、TOP、NEW）
	private static String MUSIC_URL = Host.URL
			+ "rs/res/music/list?raveId=%s&ct=%s&ps=%s&pn=%s";
	// 获取音乐分类（CATEGORIES）
	private static String MUSIC_CATEGORIES_URL = Host.URL
			+ "rs/rave/category?raveId=%s&ct=%s&ps=%s&pn=%s";
	// 获取音乐分类子页面 （TOP、NEW）
	private static String MUSIC_CATEGORIES_ITEM_URL = Host.URL
			+ "rs/res/music/categorylist?column=%s&categoryId=%s&ps=%s&pn=%s&raveId=%s";
	// 铃声搜索
	private static String RINGTONES_SEARCH_URL = Host.URL
			+ "rs/res/searchMusic";
	// 壁纸下载统计
	private static String ACCORD_WALLPAPER_DOWNLOAD = Host.URL
			+ "rs/res/imageDownLoad";
	// 音乐下载统计
	private static String ACCORD_MUSIC_DOWNLOAD = Host.URL
			+ "rs/res/musicDownLoad";
	// 多国家列表
	private static String COUNTRY_LIST_URL = Host.URL + "rs/rave/country";
	// 手机激活
	private static String MACHINE_ACTIVATE = Host.URL + "rs/machine/run";
	// 获取搜索关键字
	private static String GET_SEARCH_KEYWORDS = Host.URL
			+ "rs/rave/getSearchKeywords?raveId=%s&albumId=%s&ps=%s&pn=%s";
	// 获取Apps搜索提示
	private static String GET_APPS_SEARCH_TIPS = Host.URL
			+ "rs/rave/searchAppsTip";
	// 获取Music搜索提示
	private static String GET_MUSIC_SEARCH_TIPS = Host.URL
			+ "rs/res/searchMusicTip";
	// 获取Image搜索提示
	private static String GET_IMAGE_SEARCH_TIPS = Host.URL
			+ "rs/res/searchImageTip";
	// 首页应用专辑
	private static String HOME_COLLECTION_URL = Host.URL
			+ "rs/rave/collection?ct=%s&raveId=%s&ps=%s&pn=%s";
	// 首页应用专辑详情
	private static String HOME_COLLECTION_DETAIL_URL = Host.URL
			+ "rs/rave/collectionlist?collectionId=%s&ps=%s&pn=%s";
	// 首页MUSTHAVE
	private static String HOME_MUST_HAVE_URL = Host.URL
			+ "rs/rave/musthave?ct=%s&raveId=%s&ps=%s&pn=%s";

	/**
	 * 账号系统
	 */
	private static String ACCOUNT_PLATFORM_INIT_URL = Host.URL
			+ "rs/account/start";
	private static String ACCOUNT_REGISTER = Host.URL + "rs/account/register";
	private static String ACCOUNT_FIND_PASSWORD = Host.URL
			+ "rs/account/findpassword";
	private static String ACCOUNT_CHANGEINFO = Host.URL
			+ "rs/account/changeInfo";
	private static String ACCOUNT_LOGIN = Host.URL + "rs/account/login";
	private static String ACCOUNT_LOGOUT = Host.URL + "rs/account/logout";

	// 新皮肤提醒
	private static String SKIN_ATTENTION_URL = Host.URL
			+ "rs/rave/skinAttention?skinCode=%s";
	// 获取皮肤
	private static String SKIN_LIST_URL = Host.URL
			+ "rs/rave/skinlist?raveId=%s&ps=%s&pn=%s";
	// 皮肤下载统计
	private static String SKIN_DOWNLOAD_STATISTIC_URL = Host.URL
			+ "rs/rave/skinDownload";
	// 首先滚动显示最新下载统计
	private static String APP_DOWNLOAD_LOG_URL = Host.URL
			+ "rs/appDownload/list";
	// 应用详情-开发商应用推荐
	private static String ISSUER_APP_RECOMMEND_URL = Host.URL
			+ "rs/rave/sameIssuerApplist?issuer=%s&appId=%s&raveId=%s";
	private static String AD_PUSH_URL = Host.URL
			+ "rs/rave/getPushMessage?imei=%s&versionName=%s";

	/** APP GAMES Category分类 **/
	private static String APP_GAMES_CATEGORY_URL = Host.URL
			+ "rs/rave/levelCategory?raveId=%s&ct=%s&ps=%s&pn=%s";

	private DataFetcher() {
		requestQueue = RequestQueueManager.getRequestQueue();
	}

	public static DataFetcher getInstance() {
		// Host.URL = SharedPrefsUtil.getValue(AmineApplication.context, "host",
		// "http://203.90.239.11/");
		Host.initHost();
		COMMON_BANNER_URL = Host.URL + "rs/rave/hometheme?ct=%s&raveId=%s";
		COMMON_CATEGORY_URL = Host.URL
				+ "rs/rave/category?raveId=%s&ct=%s&ps=%s&pn=%s";
		CATEGORY_DETAIL_LIST_URL = Host.URL
				+ "rs/rave/categorylist?column=%s&categoryId=%s&ps=%s&pn=%s&raveId=%s";

		APPS_UPGRADE_URL = Host.URL + "rs/rave/getUpdateApps";
		COMMON_APP_LIST_URL = Host.URL
				+ "rs/rave/list?raveId=%s&ct=%s&ps=%s&pn=%s";
		COMMENT_LIST_URL = Host.URL
				+ "rs/rave/commentlist?appId=%s&ps=%s&pn=%s";
		APP_DETAIL_URL = Host.URL + "rs/rave/app?apkId=%s&ct=%s&raveId=%s";
		APP_DETAIL_URL2 = Host.URL
				+ "rs/rave/appPackageName?packageName=%s&ct=%s&raveId=%s";
		ADD_COMMENT_URL = Host.URL + "rs/rave/comment";
		APP_SEARCH_URL = Host.URL + "rs/rave/searchApps";
		APP_CRASH_REPORT_URL = Host.URL + "rs/rave/debacle";
		APP_RECOMMEND_URL = Host.URL
				+ "rs/rave/recommendlist?categoryId=%s&appId=%s&ps=%s&pn=%s&raveId=%s";
		ADD_FEEDBACK = Host.URL + "rs/rave/feedback";
		FEEDBACK_CLIENT = Host.URL
				+ "rs/rave/getFeedbackClient?imei=%s&ps=%s&pn=%s";
		FEEDBACK_LIST = Host.URL + "rs/rave/getFeedbackCommon?ps=%s&pn=%s";
		FEEDBACK_WARN = Host.URL + "rs/rave/feedbackAttention?imei=%s";
		ACCORD_APP_DOWNLOAD = Host.URL + "rs/rave/appDownLoad";
		PLATFORM_UPGRADE = Host.URL + "rs/rave/upgrade";
		WALLPAPER_URL = Host.URL
				+ "rs/res/image/list?raveId=%s&ct=%s&ps=%s&pn=%s";
		WALLPAPER_ALBUM_URL = Host.URL
				+ "rs/res/image/theme?raveId=%s&ct=%s&ps=%s&pn=%s";
		WALLPAPER_CATEGORIES_URL = Host.URL
				+ "rs/rave/category?raveId=%s&ct=%s&ps=%s&pn=%s";
		WALLPAPER_CATEGORIES_DETAIL_URL = Host.URL
				+ "rs/res/image/categorylist?column=%s&categoryId=%s&ps=%s&pn=%s&raveId=%s";
		WALLPAPER_ALBUM_DETAIL_URL = Host.URL
				+ "rs/res/image/themelist?themeId=%s&ps=%s&pn=%s";
		WALLPAPER_ALBUM_ONE_CLICK_URL = Host.URL
				+ "rs/res/image/themelist?themeId=%s";
		WALLPAPER_SEARCH_URL = Host.URL + "rs/res/searchImage";
		MUSIC_CATEGORIES_URL = Host.URL
				+ "rs/rave/category?raveId=%s&ct=%s&ps=%s&pn=%s";
		MUSIC_URL = Host.URL + "rs/res/music/list?raveId=%s&ct=%s&ps=%s&pn=%s";
		MUSIC_CATEGORIES_ITEM_URL = Host.URL
				+ "rs/res/music/categorylist?column=%s&categoryId=%s&ps=%s&pn=%s&raveId=%s";
		RINGTONES_SEARCH_URL = Host.URL + "rs/res/searchMusic";
		ACCORD_WALLPAPER_DOWNLOAD = Host.URL + "rs/res/imageDownLoad";
		ACCORD_MUSIC_DOWNLOAD = Host.URL + "rs/res/musicDownLoad";
		COUNTRY_LIST_URL = Host.URL + "rs/rave/country";
		MACHINE_ACTIVATE = Host.URL + "rs/machine/run";

		/****** 账号系统 *******/
		ACCOUNT_PLATFORM_INIT_URL = Host.URL + "rs/account/start";
		ACCOUNT_REGISTER = Host.URL + "rs/account/register";
		ACCOUNT_FIND_PASSWORD = Host.URL + "rs/account/findpassword";
		ACCOUNT_CHANGEINFO = Host.URL + "rs/account/changeInfo";
		ACCOUNT_LOGIN = Host.URL + "rs/account/login";
		ACCOUNT_LOGOUT = Host.URL + "rs/account/logout";

		GET_SEARCH_KEYWORDS = Host.URL
				+ "rs/rave/getSearchKeywords?raveId=%s&albumId=%s&ps=%s&pn=%s";
		GET_APPS_SEARCH_TIPS = Host.URL + "rs/rave/searchAppsTip";
		GET_MUSIC_SEARCH_TIPS = Host.URL + "rs/res/searchMusicTip";
		GET_IMAGE_SEARCH_TIPS = Host.URL + "rs/res/searchImageTip";
		HOME_COLLECTION_URL = Host.URL
				+ "rs/rave/collection?ct=%s&raveId=%s&ps=%s&pn=%s";
		HOME_COLLECTION_DETAIL_URL = Host.URL
				+ "rs/rave/collectionlist?collectionId=%s&ps=%s&pn=%s";
		HOME_MUST_HAVE_URL = Host.URL
				+ "rs/rave/musthave?ct=%s&raveId=%s&ps=%s&pn=%s";

		SKIN_ATTENTION_URL = Host.URL + "rs/rave/skinAttention?skinCode=%s";
		SKIN_LIST_URL = Host.URL + "rs/rave/skinlist?raveId=%s&ps=%s&pn=%s";
		SKIN_DOWNLOAD_STATISTIC_URL = Host.URL + "rs/rave/skinDownload";
		APP_DOWNLOAD_LOG_URL = Host.URL + "rs/appDownload/list";
		ISSUER_APP_RECOMMEND_URL = Host.URL
				+ "rs/rave/sameIssuerApplist?issuer=%s&appId=%s&raveId=%s";
		AD_PUSH_URL = Host.URL
				+ "rs/rave/getPushMessage?imei=%s&versionName=%s";
		/** APP GAMES Category分类 **/
		APP_GAMES_CATEGORY_URL = Host.URL
				+ "rs/rave/levelCategory?raveId=%s&ct=%s&ps=%s&pn=%s";
		if (instance == null)
			instance = new DataFetcher();
		return instance;
	}

	// "GET" Request
	private void get(String url, Listener<JSONObject> listener,
			ErrorListener errorListener, boolean shouldCache) {
		JsonObjectRequest request = new JsonObjectRequest(Method.GET, url,
				null, listener, errorListener);
		request.setShouldCache(shouldCache);
		LogUtil.getLogger().d("get data ==>url:" + url);
		requestQueue.add(request);
	}

	// "POST" Request
	private void post(String url, JSONObject object,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, url,
				object, listener, errorListener);
		LogUtil.getLogger()
				.d("post data ==>url:" + url + ",JSONObject==>" + object != null ? object
						.toString() : "");
		request.setShouldCache(false);
		requestQueue.add(request);
	}

	public void getUpgradeApps(AppsUpgradeRequest appsUpgradeRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String jsonRequest = JsonUtil.objectToJson(appsUpgradeRequest);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(APPS_UPGRADE_URL, object, listener, errorListener);
	}

	/**
	 * @Title: getHomeData
	 * @Description: 通用获取应用列表
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 */

	public void getHomeData(TabRequest request, Listener<JSONObject> listener,
			ErrorListener errorListener, boolean shouldCache) {
		String requestUrl = String.format(COMMON_APP_LIST_URL,
				request.data.raveId, request.data.ct, request.page.ps,
				request.page.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getHomeCollectionData
	 * @Description: 获取首页应用专辑
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 */

	public void getHomeCollectionData(HomeCollectionRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(HOME_COLLECTION_URL, request.data.ct,
				request.data.raveId, request.page.ps, request.page.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getHomeCollectionDetailData
	 * @Description: 获取首页应用专辑详情
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 */

	public void getHomeCollectionDetailData(
			HomeCollectionDetailRequest request, Listener<JSONObject> listener,
			ErrorListener errorListener, boolean shouldCache) {
		String requestUrl = String.format(HOME_COLLECTION_DETAIL_URL,
				request.data.collectionId, request.page.ps, request.page.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	public void getHomeMustHaveData(HomeMustHaveRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(HOME_MUST_HAVE_URL, request.data.ct,
				request.data.raveId, request.page.ps, request.page.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getBannerData
	 * @Description: 获取广告
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void getBannerData(BannerRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(COMMON_BANNER_URL, request.ct,
				request.raveId);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getCategoryData
	 * @Description: 获取分类
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */

	public void getCategoryData(CategoryRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(COMMON_CATEGORY_URL, request.raveId,
				request.ct, request.pager.ps, request.pager.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getCategoryDetailData
	 * @Description: 获取分类资源列表
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */

	public void getCategoryDetailData(CategoryDetailRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(CATEGORY_DETAIL_LIST_URL,
				request.type, request.categoryId, request.pager.ps,
				request.pager.pn, request.raveId);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getAppDetailData
	 * @Description: 获取应用详情
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void getAppDetailData(DetailRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = null;

		switch (request.rc) {

		// 来源第三方市场
		case Constan.Rc.MARKET_APP_DETAIL:
			requestUrl = String.format(APP_DETAIL_URL2, request.packageName,
					request.ct, request.raveId);
			break;

		// 来源应用搜索
		case Constan.Rc.SEARCH_APP_DETAIL:
			requestUrl = Host.URL + request.actionUrl;
			break;

		// 来源应用列表
		case Constan.Rc.GET_APP_DETAIL:
			requestUrl = String.format(APP_DETAIL_URL, request.apkId,
					request.ct, request.raveId);
			break;
		}

		get(requestUrl, listener, errorListener, false);
	}

	/**
	 * @Title: addCommentsData
	 * @Description: 发表评论
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void addCommentsData(CommentRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		post(ADD_COMMENT_URL, requestObejct, listener, errorListener);
	}

	/**
	 * @Title: loadCommentsData
	 * @Description: 加载评论数据
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void loadCommentsData(CommentRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(COMMENT_LIST_URL, request.data.appId,
				request.ps, request.pn);
		get(requestUrl, listener, errorListener, false);

	}

	/**
	 * @Title: searchApps
	 * @Description: 应用搜索
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void searchApps(SearchRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		post(APP_SEARCH_URL, requestObejct, listener, errorListener);
	}

	/**
	 * @Title: sendCrashReport
	 * @Description: 平台崩溃（错误日志）
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void sendCrashReport(RaveCrashRequest crashRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(crashRequest));
		} catch (Exception e) {
			e.printStackTrace();
		}
		post(APP_CRASH_REPORT_URL, requestObejct, listener, errorListener);
	}

	/**
	 * @Title: loadRecommendData
	 * @Description: 应用推荐
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void loadRecommendData(RecommendRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(APP_RECOMMEND_URL,
				request.categoryId, request.appId, request.ps, request.pn,
				request.raveId);
		get(requestUrl, listener, errorListener, false);
	}

	/**
	 * @Title: 平台用户反馈接口(rc=30013)
	 * @Description: 用户反馈信息的提交
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void feedbackData(FeedbackRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("feedbackData------------>");
		post(ADD_FEEDBACK, requestObejct, listener, errorListener);
	}

	/**
	 * @Title: 用户回馈记录接口（rc=30024)
	 * @Description: 用户反馈获取对话列表数据
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void feedbackDialogueData(FeedbackDialogueRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(FEEDBACK_CLIENT, request.imei,
				request.ps, request.pn);
		get(requestUrl, listener, errorListener, false);
	}

	/**
	 * @Title: 常见回馈列表接口（rc=30023)
	 * @Description: 用户反馈获取对话列表数据
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void feedbackList(FeedbackListRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String
				.format(FEEDBACK_LIST, request.ps, request.pn);
		get(requestUrl, listener, errorListener, false);
	}

	/**
	 * @Title: 回馈提醒接口（rc=30022)
	 * @Description: 回馈提醒
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void feedbackWarn(FeedbackWarnRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(FEEDBACK_WARN, request.imei);
		get(requestUrl, listener, errorListener, false);
	}

	/**
	 * @Title: accordDownloadData
	 * @Description: 应用、壁纸、铃声下载统计
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void accordDownloadData(AccordDownloadRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String path = null;
		switch (request.rc) {
		case Constan.Rc.ACCORD_APP_DOWNLOAD:
			path = ACCORD_APP_DOWNLOAD;
			break;

		case Constan.Rc.ACCORD_WALLPAPER_DOWNLOAD:
			path = ACCORD_WALLPAPER_DOWNLOAD;
			break;

		case Constan.Rc.ACCORD_MUSIC_DOWNLOAD:
			path = ACCORD_MUSIC_DOWNLOAD;
			break;

		default:
			path = ACCORD_APP_DOWNLOAD;
			break;
		}

		post(path, requestObejct, listener, errorListener);
	}

	/**
	 * @Title: getWallpaperData
	 * @Description: 获取壁纸栏目数据
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void getWallpaperData(WallpaperRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String path = null;
		switch (request.data.ct) {
		// CATEGORIES
		case Constan.Ct.WALLPAPER_CATEGORY:
			path = WALLPAPER_CATEGORIES_URL;
			break;
		// ALBUM
		case Constan.Ct.WALLPAPER_ALBUM:
			path = WALLPAPER_ALBUM_URL;
			break;
		// HOT、TOP
		case Constan.Ct.WALLPAPER_NEW:
		case Constan.Ct.WALLPAPER_TOP:
			path = WALLPAPER_URL;
			break;
		}
		String requestUrl = String.format(path, request.data.raveId,
				request.data.ct, request.pager.ps, request.pager.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getWallpaperCategoryDetail
	 * @Description: 获取壁纸栏目（category）详情数据
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void getWallpaperCategoryDetail(WallpaperRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(WALLPAPER_CATEGORIES_DETAIL_URL,
				request.column, request.categoryId, request.pager.ps,
				request.pager.pn, request.data.raveId);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getWallpaperAlbumDetail
	 * @Description: 获取壁纸栏目（album）详情数据
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void getWallpaperAlbumDetail(WallpaperRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = null;
		if (request.pager == null) {
			requestUrl = String.format(WALLPAPER_ALBUM_ONE_CLICK_URL,
					request.themeId);
		} else {
			requestUrl = String.format(WALLPAPER_ALBUM_DETAIL_URL,
					request.themeId, request.pager.ps, request.pager.pn);
		}
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getMusicCategoryData
	 * @Description: 获取音乐栏目数据 （Category、TOP、HOT、NEW）
	 * @param request
	 * @param listener
	 * @param errorListener
	 * @param shouldCache
	 */
	public void getMusicData(RingtonesRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String path = null;
		switch (request.data.ct) {
		// CATEGORIES
		case Constan.Ct.RINGTONES_CATEGORY:
			path = MUSIC_CATEGORIES_URL;
			break;
		// NEW、TOP、HOT
		case Constan.Ct.RINGTONES_TOP:
		case Constan.Ct.RINGTONES_HOT:
		case Constan.Ct.RINGTONES_NEW:
			path = MUSIC_URL;
			break;
		}
		String requestUrl = String.format(path, request.data.raveId,
				request.data.ct, request.pager.ps, request.pager.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Description: 获取音乐分类子页面数据 （HOT、NEW）
	 * @param request
	 * @param listener
	 * @param errorListener
	 * @param shouldCache
	 */
	public void getMusicCategoryData(RingtonesRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(MUSIC_CATEGORIES_ITEM_URL,
				request.column, request.categoryId, request.pager.ps,
				request.pager.pn, request.data.raveId);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: searchWallpaper
	 * @Description: 图片搜索
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void searchWallpaper(SearchRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		post(WALLPAPER_SEARCH_URL, requestObejct, listener, errorListener);
	}

	/**
	 * @Title: searchRingtones
	 * @Description: 铃声搜索
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 * @throws
	 */
	public void searchRingtones(SearchRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		post(RINGTONES_SEARCH_URL, requestObejct, listener, errorListener);
	}

	/**
	 * 
	 * @Title: getPlatformUpgrade
	 * @Description: 平台升级
	 * @param @param upgradeRequest
	 * @param @param listener
	 * @param @param errorListener 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getPlatformUpgrade(UpgradeRequest upgradeRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String jsonRequest = JsonUtil.objectToJson(upgradeRequest);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(PLATFORM_UPGRADE, object, listener, errorListener);
	}

	/**
	 * 
	 * @Title: getCountryList
	 * @Description: 获取多国家列表数据
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getCountryList(CountryRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(COUNTRY_LIST_URL);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: initPlatform
	 * @Description: 平台初始化
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void initPlatform(PlatFormInitRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(ACCOUNT_PLATFORM_INIT_URL);
		String jsonRequest = JsonUtil.objectToJson(request);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(requestUrl, object, listener, errorListener);
	}

	/**
	 * @Title: activateDevice
	 * @Description: 手机激活
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void activateDevice(MachineActivateRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		post(MACHINE_ACTIVATE, requestObejct, listener, errorListener);
	}

	/**
	 * @Title:
	 * @Description: 注册
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void register(RegisterRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(ACCOUNT_REGISTER);
		String jsonRequest = JsonUtil.objectToJson(request);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(requestUrl, object, listener, errorListener);
	}

	/**
	 * @Title:
	 * @Description: 登陆
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void login(LoginRequest request, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String requestUrl = String.format(ACCOUNT_LOGIN);
		String jsonRequest = JsonUtil.objectToJson(request);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(requestUrl, object, listener, errorListener);
	}

	/**
	 * @Title:
	 * @Description: 登出
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void logout(LogoutRequest request, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String requestUrl = String.format(ACCOUNT_LOGOUT);
		String jsonRequest = JsonUtil.objectToJson(request);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(requestUrl, object, listener, errorListener);
	}

	/**
	 * @Title:
	 * @Description: 修改用户信息
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void modifyUserPwd(ModifyUserPwdRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(ACCOUNT_CHANGEINFO);
		String jsonRequest = JsonUtil.objectToJson(request);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(requestUrl, object, listener, errorListener);
	}

	/**
	 * @Title:
	 * @Description: 修改用户信息
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void modifyUserNickName(ModifyUserNickNameRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = String.format(ACCOUNT_CHANGEINFO);
		String jsonRequest = JsonUtil.objectToJson(request);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(requestUrl, object, listener, errorListener);
	}

	/**
	 * @Title:
	 * @Description: 登出
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void findPwd(FindPwdRequest request, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String requestUrl = String.format(ACCOUNT_FIND_PASSWORD);
		String jsonRequest = JsonUtil.objectToJson(request);
		JSONObject object = null;
		try {
			object = new JSONObject(jsonRequest);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		post(requestUrl, object, listener, errorListener);
	}

	/**
	 * 
	 * @Title: getSearchKeywords
	 * @Description: 获取搜索关键字
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getSearchKeywords(KeywordsRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(GET_SEARCH_KEYWORDS, request.raveId,
				request.albumId, request.pager.ps, request.pager.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * 
	 * @Title: getSearchData
	 * @Description: TODO
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 */
	public void getSearchData(SearchRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = Host.URL + request.action;

		switch (request.rc) {

		case Constan.Rc.KEYWORDS_APPS_LIST:
		case Constan.Rc.KEYWORDS_MUSIC_LIST:
		case Constan.Rc.KEYWORDS_WALLPAPER_LIST:
			requestUrl = requestUrl + "&pn=" + request.page.pn + "&ps="
					+ request.page.ps;
			get(requestUrl, listener, errorListener, shouldCache);
			break;

		case Constan.Rc.GET_APPS_SEARCH:
		case Constan.Rc.POST_MUSIC_SEARCH:
		case Constan.Rc.POST_WALLPAPER_SEARCH:
			JSONObject requestObejct = null;
			try {
				requestObejct = new JSONObject(JsonUtil.objectToJson(request));
			} catch (Exception e) {
				e.printStackTrace();
			}
			post(requestUrl, requestObejct, listener, errorListener);
			break;
		}
	}

	public void getSearchTips(SearchTipsRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = null;
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (request.rc) {
		case Constan.Rc.SEARCH_APPS_TIPS:
			requestUrl = GET_APPS_SEARCH_TIPS;
			break;
		case Constan.Rc.SEARCH_MUSIC_TIPS:
			requestUrl = GET_MUSIC_SEARCH_TIPS;
			break;
		case Constan.Rc.SEARCH_IMAGE_TIPS:
			requestUrl = GET_IMAGE_SEARCH_TIPS;
			break;
		}
		post(requestUrl, requestObejct, listener, errorListener);
	}

	/**
	 * @Title: getSkinAttention
	 * @Description: Skin更新提醒接口
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 */

	public void getSkinAttention(SkinAttentionRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(SKIN_ATTENTION_URL, request.skinCode);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: getSkinList
	 * @Description:Skin列表获取接口
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 */

	public void getSkinList(SkinListRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(SKIN_LIST_URL, request.raveId,
				request.pager.ps, request.pager.pn);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: skinDownloadStatistic
	 * @Description: Skin下载统计接口
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void skinDownloadStatistic(SkinDownloadRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		JSONObject requestObejct = null;
		try {
			requestObejct = new JSONObject(JsonUtil.objectToJson(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		post(SKIN_DOWNLOAD_STATISTIC_URL, requestObejct, listener,
				errorListener);
	}

	/**
	 * 
	 * @Title: getAppDownloadLogResult
	 * @Description: 获取首页滚动显示下载统计
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @param @param shouldCache
	 * @return void
	 */
	public void getAppDownloadLogResult(AppDownloadLogRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,
			boolean shouldCache) {
		String requestUrl = String.format(APP_DOWNLOAD_LOG_URL);
		get(requestUrl, listener, errorListener, shouldCache);
	}

	/**
	 * @Title: loadIssuerRecommendData
	 * @Description: 开发商应用推荐
	 * @param @param request
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */
	public void loadIssuerRecommendData(RecommendRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = "";
		try {
			requestUrl = String.format(ISSUER_APP_RECOMMEND_URL,
					URLEncoder.encode(request.issuer, "utf-8"), request.appId,
					request.raveId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		get(requestUrl, listener, errorListener, false);
	}

	/**
	 * @Title: getAdPush
	 * @Description: 获取广告推送
	 * @param @param imei
	 * @param @param versionName
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void getAdPush(String imei, String versionName,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		String requestUrl = "";
		try {
			requestUrl = String.format(AD_PUSH_URL, imei, versionName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		get(requestUrl, listener, errorListener, false);
	}

	/***
	 * 
	* @Title: 获取APP GAME 分类列表
	* @Description: TODO 
	* @param @param request
	* @param @param listener
	* @param @param errorListener    
	* @return void
	 */
	public void getAppGameCategory(AppGamesCategoryRequest request,
			Listener<JSONObject> listener, ErrorListener errorListener,	boolean shouldCache) {
		String requestStr = String.format(APP_GAMES_CATEGORY_URL, request.raveId,
					request.ct, request.pager.ps, request.pager.pn);
		get(requestStr,listener,errorListener,shouldCache);
	}
}
