package com.x.business.search;

/**
 * 
 * @ClassName: SearchConstan
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-15 下午1:38:56
 * 
 */
public class SearchConstan {

	public static final int APP_FRAGMENT_ID = 0;
	public static final int RINGTONE_FRAGMENT_ID = 1;
	public static final int WALLPAPER_FRAGMENT_ID = 2;
	public static final String TAB_INDEX = "tabIndex";
	public static final String QUERY_KEY = "queryKey";
	public static final String ACTION_URL = "actionUrl";
	public static final String FROM_SEARCH = "fromSearch";
	public static final String SHOW_TIPS_VIEW = "showTipsView";
	public static final String APP_ACTION = "rs/rave/searchApps";
	public static final String MUSIC_ACTION = "rs/res/searchMusic";
	public static final String IMAGE_ACTION = "rs/res/searchImage";
	public static final String CURRENT_POSITION_ID = "currentPositionId";
	public static final String SEARCH_FRAGMENT_INITED = "searchFragmentInited";

	public class Album {
		public static final int APPS_ALBUM_ID = 2;
		public static final int RINGTONES_ALBUM_ID = 4;
		public static final int WALLPAPERS_ALBUM_ID = 5;
	}

	public class State {
		public static final int SEARCH_KEYWORDS_SUCCESS = 101;
		public static final int SEARCH_KEYWORDS_FAILURE = 102;
		public static final int SEARCH_KEYWORDS_EMPTY = 103;

		public static final int RESPONSE_APP_DATA_SUCCESS = 201;
		public static final int RESPONSE_APP_DATA_FAILURE = 202;

		public static final int RESPONSE_MUSIC_DATA_SUCCESS = 301;
		public static final int RESPONSE_MUSIC_DATA_FAILURE = 302;

		public static final int RESPONSE_IMAGE_DATA_SUCCESS = 401;
		public static final int RESPONSE_IMAGE_DATA_FAILURE = 402;

		public static final int RESPONSE_APPS_TIPS_SUCCESS = 501;
		public static final int RESPONSE_RINGTONES_TIPS_SUCCESS = 502;
		public static final int RESPONSE_WALLPAPERS_TIPS_SUCCESS = 503;

		public static final int REFRESH_DATA_SUCCESS = 601;
	}

}
