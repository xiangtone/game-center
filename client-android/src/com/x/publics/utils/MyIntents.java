package com.x.publics.utils;

public class MyIntents {

	public static final String INTENT_UPDATE_UI = "com.x.activity.IManagementActivity";
	public static final String INTENT_DOWNLOADSERVICE = "com.x.download.IDownloadService";
	public static final String INTENT_FINISH_ACTIVITY = "com.x.activity.finish";
	public static final String INTENT_SETTINGS_CHANGE = "com.x.settings.change";
	public static final String INTENT_AUTO_DOWNLOAD_ERRORPAUSED = "com.x.autodownload.error.pause";
	public static final String INTENT_UPDATE_AUTO_WIFI_ACTION = "com.x.UPDATE_AUTO_WIFI_DOWNLOAD";
	public static final String INTENT_UPDATE_CHANGE_WIFI_ACTION = "com.x.UPDATE_CHANGE_WIFI";  // wifi切换触发获取应用更新请求成功广播

	public static final String INTENT_APKFILE_DELETE_UPDATE_UI = "com.x.activity.ApkManagementActivity.IDeleteApkUiReceiver";
	public static final String INTENT_DELETE_EXTRA_PATH = "file_path";
	public static final String INTENT_DELETE_EXTRA_TYPE = "file_type";
	
	public static final int EXTRA_TYPE_INSTALL = 1;
	public static final int EXTRA_TYPE_UNINSTALL = 2;
	public static final int EXTRA_TYPE_INSTALL_ALL = 3;
	public static final int EXTRA_TYPE_UNINSTALL_ALL = 4;
	public static final int EXTRA_TYPE_UNINSTALL_TO_INSTALL = 5;
	
	public static final String INTENT_APK_SCAN_UPDATE_UI = "com.x.activity.ApkManagementActivity.IScanApkUiReceiver";
	public static final String INTENT_EXTRA_APK_SCAN_STATUS = "scan_status";
	public static final int EXTRA_TYPE_APK_SCAN_START = 101;
	public static final int EXTRA_TYPE_APK_SCAN_FINISH = 102;
	public static final int EXTRA_TYPE_APK_SCAN_REFRESH = 103;

	public static final String TYPE = "type";
	public static final String PROCESS_SPEED = "process_speed";
	public static final String PROCESS_PROGRESS = "process_progress";
	public static final String PROCESS_PROMOT = "process_promot";
	public static final String URL = "url";
	public static final String DOWNLOADBEAN = "downloadbean";
	public static final String ERROR_CODE = "error_code";
	public static final String ERROR_INFO = "error_info";
	public static final String IS_PAUSED = "is_paused";
	public static final String PACKAGENAME = "packagename";
	public static final String VERSION_CODE = "version_code";
	public static final String FILE_SIZE = "file_size";
	public static final String LOCAL_PATH = "local_path";
	public static final String INSTALL_RESULT_CODE = "result_code";

	public class Types {

		public static final int PROCESS = 0;
		public static final int COMPLETE = 1;

		public static final int START = 2;
		public static final int PAUSE = 3;
		public static final int DELETE = 4;
		public static final int CONTINUE = 5;
		public static final int ADD = 6;
		public static final int STOP = 7;
		public static final int ERROR = 9;
		public static final int ADD_HISTORY = 10;
		public static final int COMPLETE_INSTALL = 11;
		public static final int PREDOWNLOAD = 12;
		public static final int WAIT = 13;
		public static final int COMPLETE_UNIINSTALL = 14;
		public static final int PAUSE_ALL = 15;
		public static final int DELETE_ALL_HISTORY = 16;
		public static final int DELETE_ALL_DOWNLOADING = 17;
		public static final int PAUSE_SOME = 18;
		public static final int AUTO_DOWNLOAD_CONTINUE = 19;
		public static final int DELETE_ALL_DOWNLOADING_MEDIA = 20;
		public static final int INSTALLING = 21;
		public static final int INSTALL_RESULT = 22;
		

		public static final int MAKE_UPGRADE = 1001;
		public static final int MERGE_PATCH = 1002;
		public static final int  VALID_WIFI = 1003;
		
		public static final int CHANGE_HOMEPAGE_UPDATE_NUM = 1004;
		public static final int CHANGE_APP_MANAGEMENT_UPDATE_NUM = 1005;
		
		public static final int DELETE_FAVORITE = 1006;
		public static final int ADD_FAVORITE = 1007;
		

	}
	

	/**刷新favoriteList**/
	public static final int INTENT_TYPE_FAVORITE_REFRESH_LIST = 100;
	public static final String INTENT_FAVORITE_AUTO_WIFI_ACTION = "com.x.FAVORITE_AUTO_WIFI_DOWNLOAD";

}
