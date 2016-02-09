/**   
* @Title: StatisticConstant.java
* @Package com.mas.amineappstore.business.statistic
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-5-30 下午2:03:44
* @version V1.0   
*/

package com.x.business.statistic;

/**
* @ClassName: StatisticConstant
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-5-30 下午2:03:44
* 
*/

public class StatisticConstan {

	public class PauseReason {
		public static final String NETWORK_ERROR = "网络故障";
		public static final String MANUAL_PAUSE = "主动暂停";
		public static final String MEMORY_ERROR = "空间不足";
		public static final String IO_ERROR = "读写异常";
		public static final String SERVER_ERROR = "服务器异常";
		public static final String URL_ERROR = "下载地址异常";
	}

	public class ContinueReason {
		public static final String MANUAL_CONTINUE = "主动继续";
		public static final String AUTO_CONTINUE = "自动继续更新";
		public static final String NETWORK_OK = "网络正常";
	}

	public class FailReason {
		public static final String MANUAL_CANCLE = "主动取消";
	}

	public class ModuleName {
		public static final String HOME_RECOMMEND = "Home-Recommend";
		public static final String HOME_NEW = "Home-New";
		public static final String HOME_TOP = "Home-Top";
		public static final String HOME_POPULAR = "Home-Popular";
		public static final String HOME_CONLLECTION = "Home-Conllection";
		public static final String HOME_MUST_HAVE = "Home-Must-Have";

		public static final String APPS_CATEGORIES = "Apps-Categories";
		public static final String APPS_HOT = "Apps-Hot";
		public static final String APPS_TOP = "Apps-Top";
		public static final String APPS_NEW = "Apps-New";

		public static final String GAMES_CATEGORIES = "Games-Categories";
		public static final String GAMES_HOT = "Games-Hot";
		public static final String GAMES_POPULAR = "Games-Popular";
		public static final String GAMES_NEW = "Games-New";

		public static final String RINGTONE_CATEGORIES = "Ringtone-Categories";
		public static final String RINGTONE_HOT = "Ringtone-Hot";
		public static final String RINGTONE_TOP = "Ringtone-Top";
		public static final String RINGTONE_NEW = "Ringtone-New";

		public static final String WALLPAPER_CATEGORIES = "WallPaper-Categories";
		public static final String WALLPAPER_ALBUM = "WallPaper-Album";
		public static final String WALLPAPER_TOP = "WallPaper-Top";
		public static final String WALLPAPER_NEW = "WallPaper-New";
		
		public static final String WALLPAPER_LIVE_WALLPAPER = "WallPaper-Live-Wallpaper";

		public static final String APP_MANAGEMENT = "App-Management";

		public static final String APK_MANAGEMENT = "Apk-Management";
		
		public static final String MYCONTENTS_PICTURE = "MyContents-Picture";
		public static final String MYCONTENTS_MUSIC = "MyContents-Music";
		public static final String MYCONTENTS_VIDEO = "MyContents-Video";
		public static final String MYCONTENTS_DOCUMENT = "MyContents-Document";
		
		public static final String QR_CODE = "QR_Code";
		
		public static final String SHARE_FREE_SEND = "Share-Free-Send";
		public static final String SHARE_FREE_RECEIVE = "Share-Free-Receive";
		
		public static final String WALLPAPER_SETWALLPAPAER  = "WallPaper-Setwallpapaer";
		
		public static final String WALLPAPER_EDIT  = "WallPaper-Edit";
		
		public static final String MYAPPS_ALL = "MyApps-All";
		public static final String MYAPPS_NEW = "MyApps-New";
		
		public static final String SEARCH_APP = "Search-Apps";
		public static final String SEARCH_RINGTONE = "Search-Ringtones";
		public static final String SEARCH_WALLPAPER = "Search-WallPapers";
		
		public static final String FEEDBACK_USER = "Feedback-User";
		public static final String FEEDBACK_MY_FEEDBACK = "Feedback-myFeedback";
		
		public static final String ACCOUNT_LOGIN = "Account-Login";
		public static final String ACCOUNT_REGISTER = "Account-Register";
		public static final String ACCOUNT_MAIN = "Account-Main";
		public static final String ACCOUNT_FIND_PASSWORD = "Account-Find-Password";
		public static final String ACCOUNT_MODIFY_PASSWORD = "Account-Modify-Password";
		public static final String ACCOUNT_SEND_PASSWORD_SUCCESS = "Account-Send-Password-Success";
		
		public static final String ACCOUNT_FIND_PWD_SUCCESS_FROM_REGISTER = "Account-Find-Pwd-Success-From-Register";
		public static final String ACCOUNT_FIND_PWD_SUCCESS_FROM_FIND_PWD = "Account-Find-Pwd-Success-From-Find-Pwd";
		public static final String ACCOUNT_FIND_PWD_SUCCESS_FROM_MODIFY_PWD = "Account-Find-Pwd-Success-From-Modify-Pwd";
		
		public static final String MAIN_HOME = "Main-Home";
		public static final String MAIN_APPS = "Main-Apps";
		public static final String MAIN_GAMES = "Main-Games";
		public static final String MAIN_RINGTONE = "Main-Ringtone";
		public static final String MAIN_WALLPAPER = "Main-Wallpaper";
		
		public static final String COLLECTION = "Collection";
		public static final String ALBUM = "Album";
		public static final String SILENT_INSTALL_OPEN = "Silent-Install-Open";
		public static final String SILENT_INSTALL_CLOSE = "Silent-Install-Close";
		
		public static final String APPLOCK_LOCKED = "Applock-Locked";
		public static final String APPS_DOWNLOADING_SHOW = "Apps-Downloading-Show";
		
	}

	public class SrcName {
		public static final int HOME_RECOMMEND = 1;   
		public static final int HOME_NEW = 2;
		public static final int HOME_TOP = 3;
		public static final int HOME_POPULAR = 4;

		public static final int APPS_CATEGORIES = 5;
		public static final int APPS_HOT = 6;
		public static final int APPS_TOP = 7;
		public static final int APPS_NEW = 8;

		public static final int GAMES_CATEGORIES = 9;
		public static final int GAMES_HOT = 10;
		public static final int GAMES_POPULAR = 11;
		public static final int GAMES_NEW = 12;

		public static final int RINGTONE_CATEGORIES = 13;
		public static final int RINGTONE_HOT = 14;
		public static final int RINGTONE_TOP = 15;
		public static final int RINGTONE_NEW = 16;

		public static final int WALLPAPER_CATEGORIES = 17;
		public static final int WALLPAPER_ALBUM = 18;
		public static final int WALLPAPER_TOP = 19;
		public static final int WALLPAPER_NEW = 20;

		public static final int SEARCH = 21;
		public static final int RELATED = 22;
		public static final int THRIDPART = 23;
		public static final int UPGRADE = 24;
		public static final int HOME_BANNER = 25;
		public static final int APPS_BANNER = 26;
		public static final int GAME_BANNER = 27;
		public static final int FAVORITE = 28;
		
		public static final int WALLPAPER_LIVE_WALLPAPER = 29;
		public static final int HOME_MUST_HAVE = 30;
		public static final int MY_APPS = 31;
		public static final int APPS_DOWNLOADING_SHOW = 32;
	}

	public class SearchType
	{
		public static final String SEARCH_ZAPP_APP = "ZappApp";
		public static final String SEARCH_ZAPP_LIST = "ZappAList";
		public static final String SEARCH_RINGTONE_LIST = "ZappRList";
		public static final String SEARCH_WALLPAPER_LIST = "ZappWList";
	}
	
	public class FileType {
		public static final int APPS = 1;
		public static final int GAME = 2;
		public static final int RINGTONES = 3;
		public static final int WALLPAPER = 4;
	}
	
	public class Pape{
		public static final String BASEFRAGMENT = "BaseFragment";
		public static final String PICTRUEFOLDERFRAGMENT = "PictrueFolderFragment";
		public static final String MUSICFRAGMENT = "MusicFragment";
		public static final String VIDEOFRAGMENT = "VideoFragment";
		public static final String DOCUMENTSFRAGMENT = "DocumentsFragment";
	}

}
