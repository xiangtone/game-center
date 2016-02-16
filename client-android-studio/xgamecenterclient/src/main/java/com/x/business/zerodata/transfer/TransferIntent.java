package com.x.business.zerodata.transfer;

public class TransferIntent {

	public static final String INTENT_UPDATE_UI = "com.x.activity.IZeroDataClientTransferActivity";
	public static final String INTENT_DOWNLOADSERVICE = "com.x.business.zerodata.transfer.TransferService";
	public static final String INTENT_FINISH_ACTIVITY = "com.x.activity.finish";
	public static final String INTENT_TRANSFER_RECONNECT = "com.x.ui.activity.zerodata.ZeroDataClientTransferActivity.Reconnect";
	//	public static final String INTENT_SETTINGS_CHANGE = "com.x.settings.change";

	//	public static final String INTENT_APKFILE_DELETE_UPDATE_UI = "com.x.activity.ApkManagementActivity.IDeleteApkUiReceiver";
	public static final String INTENT_DELETE_EXTRA_PATH = "file_path";
	public static final String INTENT_DELETE_EXTRA_TYPE = "file_type";
	public static final int EXTRA_TYPE_INSTALL = 1;
	public static final int EXTRA_TYPE_UNINSTALL = 2;
	public static final int EXTRA_TYPE_INSTALL_ALL = 3;
	public static final int EXTRA_TYPE_UNINSTALL_ALL = 4;
	public static final int EXTRA_TYPE_UNINSTALL_TO_INSTALL = 5;

	public static final String TYPE = "type";
	public static final String PROCESS_SPEED = "process_speed";
	public static final String PROCESS_PROGRESS = "process_progress";
	public static final String PROCESS_PROMOT = "process_promot";
	public static final String URL = "url";
	public static final String DOWNLOADBEAN = "downloadbean";
	public static final String DOWNLOADBEAN_LIST = "downloadbean_list";
	public static final String ERROR_CODE = "error_code";
	public static final String ERROR_INFO = "error_info";
	public static final String IS_PAUSED = "is_paused";
	public static final String PACKAGENAME = "packagename";
	public static final String VERSION_NAME = "version_name";
	public static final String FILE_SIZE = "file_size";

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
		public static final int ADD_SOME = 19;
	}

}
