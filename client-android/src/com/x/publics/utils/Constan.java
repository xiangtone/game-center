/**   
* @Title: Constan.java
* @Package com.mas.amineappstore.util
* @Description: TODO 

* @date 2014-1-15 上午11:34:16
* @version V1.0   
*/

package com.x.publics.utils;

/**
* @ClassName: Constan
* @Description: TODO 

* @date 2014-1-15 上午11:34:16
* 
*/

public class Constan {

	public static final int PAGE_SIZE = 24;

	public static final int RAVE_ID = 1;

	public static final String SIGN = "";

	public static final String ZERO_START = "zerodata://";
	public static final String HTTP_START = "http://";
	public static final String HTTPS_START = "https://";

	public static class Rc {
		public static final int PLATFORM_LAUNCH = 30000;
		public static final int GET_APPS_UPGRADE = 30001;
		public static final int GET_TAB_RESOURCE = 30002;
		public static final int GET_APP_DETAIL = 30003;
		public static final int GET_APP_COMMENT = 30004;
		public static final int POST_APP_COMMENT = 30005;
		public static final int GET_APPS_SEARCH = 30007;
		public static final int POST_RAVE_CRASH = 30008;
		public static final int GET_APP_RECOMMEND = 30009;
		public static final int GET_CATEGORIES = 30010;
		public static final int GET_BANNER = 30012;
		public static final int POST_FEEDBACK = 30013;
		public static final int GET_FEEDBACKWARN = 30022;
		public static final int GET_FEEDBACKLIST = 30023;
		public static final int GET_FEEDBACKDIALOGUE = 30024;
		public static final int ACCORD_APP_DOWNLOAD = 30014;
		public static final int PLATFORM_UPGRADE = 30015;
		public static final int MARKET_APP_DETAIL = 30016;
		public static final int GET_COUNTRY = 30017;
		public static final int GET_SEARCH_KEYWORDS = 30018;
		public static final int KEYWORDS_APPS_LIST = 30019;
		public static final int SEARCH_APPS_TIPS = 30020;
		public static final int SEARCH_APP_DETAIL = 30021;
		public static final int ISSUER_APP_RECOMMEND = 30033;

		public static final int GET_MUSIC = 31001;
		public static final int GET_MUSIC_THEME = 31002;
		public static final int POST_MUSIC_SEARCH = 31005;
		public static final int ACCORD_MUSIC_DOWNLOAD = 31006;
		public static final int KEYWORDS_MUSIC_LIST = 31007;
		public static final int SEARCH_MUSIC_TIPS = 31008;

		public static final int GET_WALLPAPER = 32001;
		public static final int GET_IMAGE_THEME = 32002;
		public static final int POST_WALLPAPER_SEARCH = 32005;
		public static final int ACCORD_WALLPAPER_DOWNLOAD = 32006;
		public static final int KEYWORDS_WALLPAPER_LIST = 32007;
		public static final int SEARCH_IMAGE_TIPS = 32008;

		public static final int GET_HOME_COLLECTION = 30030;
		public static final int GET_HOME_COLLECTION_DETAIL = 30031;
		public static final int GET_HOME_MUST_HAVE = 30032;

		public static final int MACHINE_ACTIVATE = 50001;

		/** 账号系统 **/
		public static final int ACCCOUNT_START = 60000;
		public static final int ACCCOUNT_REGISTER = 60001;
		public static final int ACCCOUNT_FIND_PASSWORD = 60002;
		public static final int ACCCOUNT_CHANGEINFO = 60003;
		public static final int ACCCOUNT_LOGIN = 60004;
		public static final int ACCCOUNT_LOGOUT = 60005;

		/**皮肤**/
		public static final int SKIN_ATTENTION = 70001;
		public static final int SKIN_LIST = 70002;
		public static final int SKIN_DOWNLOAD = 70003;

		/**首页最新下载统计**/
		public static final int APP_DOWNLOAD_LOG = 80001;
		/**获取app  games游戏分类**/
		public static final int APP_GAMES_CATEGORY=30034;
	}

	public static class Page {
		public static final int HOME = 1;
		public static final int APPS = 2;
		public static final int GAMES = 3;
		public static final int FETURED = 4;
	}

	public static class Ct {
		public static final int HOME_RECOMMEND = 1;
		public static final int HOME_NEW = 2;
		public static final int HOME_TOP = 3;
		public static final int HOME_NECESSARY = 4;
		public static final int HOME_COLLECTION = 15;
		public static final int HOME_MUST_HAVE = 16;

		public static final int APP_CATEGORY = 2;
		public static final int APP_HOT = 12;
		public static final int APP_TOP = 13;
		public static final int APP_NEW = 14;

		public static final int GAME_CATEGORY = 3;
		public static final int GAME_HOT = 22;
		public static final int GAME_TOP = 23;
		public static final int GAME_NEW = 24;

		public static final int RINGTONES_CATEGORY = 4;
		public static final int RINGTONES_HOT = 32;
		public static final int RINGTONES_TOP = 33;
		public static final int RINGTONES_NEW = 34;

		public static final int WALLPAPER_CATEGORY = 5;
		public static final int WALLPAPER_ALBUM = 42;
		public static final int WALLPAPER_TOP = 43;
		public static final int WALLPAPER_NEW = 44;
		public static final int WALLPAPER_LIVE = 45;

		public static final int APP_CATEGORY_DETAIL = 11;
		public static final int GAME_CATEGORY_DETAIL = 21;
		public static final int APP_SEARCH = 100;
		public static final int HOME_BANNER = 101;
		public static final int APP_BANNER = 102;
		public static final int GAME_BANNER = 103;
		public static final int APP_RECOMMEND = 104;
		public static final int APP_DOWNLOAD_MANAGEMENT = 105;
		public static final int APP_UPDATE_MANAGEMENT = 106;
		public static final int APP_FAVORITE_MANAGEMENT = 107;
		public static final int MARKET_APP_DETAIL = 108;
		public static final int DYNAMIC_LISTVIEW_DETAIL = 109;
	}

	public static class Category {
		public static final String CATEGORY_HOT = "hot";
		public static final String CATEGORY_NEW = "new";
	}

	public static class Notification {
		public static int UPDATE_ID = 100000;
		public static int DOWNLOAD_ID = 100001;
		public static int COMPLETE_ID = 100002;
	}

	public static class MediaType {
		public static final String APP = "1";
		public static final String IMAGE = "2";
		public static final String MUSIC = "3";
		public static final String DOC = "4";
		public static final String VEDIO = "5";
		public static final String GAME = "6";
		public static final String THEME = "7";
	}
}
