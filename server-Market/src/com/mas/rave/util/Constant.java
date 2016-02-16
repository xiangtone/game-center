package com.mas.rave.util;


public class Constant {

	// s3
	public static final String RUN_S3 = ConfigDateApplication.getInstance().getPropertie("run.s3").trim();
	public static final String S3_BUCKET = ConfigDateApplication.getInstance().getPropertie("s3.bucket").trim();
	// mail
	public static final String MAIL_SMTP = ConfigDateApplication.getInstance().getPropertie("mail.smtp").trim();
	public static final int MAIL_PORT = getInt(ConfigDateApplication.getInstance().getPropertie("mail.port").trim(), 25);
	public static final String MAIL_USERNAME = ConfigDateApplication.getInstance().getPropertie("mail.username").trim();
	public static final String MAIL_PASSOWRD = ConfigDateApplication.getInstance().getPropertie("mail.password").trim();
	public static final String DEVELOPER_URL = ConfigDateApplication.getInstance().getPropertie("developer.url").trim();
	public static final String APP_TEST_URL = ConfigDateApplication.getInstance().getPropertie("app.test.url").trim();

	// email
	public static final String MAIL_SENDER = ConfigDateApplication.getInstance().getServicePropertie("mail.sender").trim();
	public static final String MAIL_TITLE_APP_SUCC = ConfigDateApplication.getInstance().getServicePropertie("mail.title.app.succ").trim();
	public static final String MAIL_TITLE_APP_FAIL = ConfigDateApplication.getInstance().getServicePropertie("mail.title.app.fail").trim();
	public static final String MAIL_CONTENT_APP_SUCC = ConfigDateApplication.getInstance().getServicePropertie("mail.content.app.succ").trim();
	public static final String MAIL_CONTENT_APP_FAIL = ConfigDateApplication.getInstance().getServicePropertie("mail.content.app.fail").trim();
	public static final String MAIL_TITLE_CP_SUCC = ConfigDateApplication.getInstance().getServicePropertie("mail.title.cp.succ").trim();
	public static final String MAIL_TITLE_CP_FAIL = ConfigDateApplication.getInstance().getServicePropertie("mail.title.cp.fail").trim();
	public static final String MAIL_CONTENT_CP_SUCC = ConfigDateApplication.getInstance().getServicePropertie("mail.content.cp.succ").trim();
	public static final String MAIL_CONTENT_CP_FAIL = ConfigDateApplication.getInstance().getServicePropertie("mail.content.cp.fail").trim();

	// 运行环境
	public static final String RUN_ENV = ConfigDateApplication.getInstance().getPropertie("run.env").trim();
	// getMd5
	public static final String CREATE_PATCHAPK = ConfigDateApplication.getInstance().getPropertie("create.patchapk").trim();

	// 服务器路径 "/usr/widget_local"
	public static final String resServer = ConfigDateApplication.getInstance().getPropertie("resServer").trim();

	public static final String LOCAL_FILE_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.file.path").trim();
	// search
	public static final String LOCAL_SEARCH_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.search.path").trim();
	// url
	public static final String LOCAL_URL_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.url").trim();
	// apk
	public static final String LOCAL_APK_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.apk.path").trim();
	// patch
	public static final String LOCAL_PATCH_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.patch.path").trim();
	// 图片
	public static final String LOCAL_APK_PIC = ConfigDateApplication.getInstance().getServicePropertie("local.pic").trim();
	// 图片
	public static final String LOCAL_APK_LOG = ConfigDateApplication.getInstance().getServicePropertie("local.log").trim();
	// 图片
	public static final String LOCAL_PIC_BIGLOGO = ConfigDateApplication.getInstance().getServicePropertie("local.biglogo").trim();

	// 平台
	public static final String LOCAL_APK_MARKET = ConfigDateApplication.getInstance().getServicePropertie("local.market");

	// 音乐
	public static final String LOCAL_MUSIC_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.music.path").trim();

	// 墙纸
	public static final String LOCAL_IMAGE_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.image.path").trim();

	// 应用主题
	public static final String LOCAL_APP_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.app.path").trim();

	// 主题
	public static final String LOCAL_THEME_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.theme.path").trim();
	// 视屏
	public static final String LOCAL_VIEW_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.view.path").trim();

	// 标题
	public static final String LOCAL_TITLE_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.title.path").trim();

	public static final String LOCAL_CATEGORY_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.category.path").trim();

	// 上传APP并发使用的线程数
	public static final int UPLOAD_THREAD_NUM = getInt(ConfigDateApplication.getInstance().getServicePropertie("upload.thread.num").trim());

	// 资源上传路径
	public static final String RESOURCE_UPLOAD_PATH = ConfigDateApplication.getInstance().getServicePropertie("resource.upload.path").trim();
	// 上传资源描述文件
	public static final String UPLOAD_DESCRIBE_FILE = ConfigDateApplication.getInstance().getServicePropertie("upload.describe.file").trim();

	// 上传APP描述文件
	public static final String APP_DESCRIBE_INFO = ConfigDateApplication.getInstance().getServicePropertie("app.describe.info").trim();

	// 上传APP更新信息文件
	public static final String APP_UPDATE_INFO = ConfigDateApplication.getInstance().getServicePropertie("app.update.info").trim();

	// 数量控制
	public static final String COUNT_NUMBER = ConfigDateApplication.getInstance().getServicePropertie("count.number").trim();
	public static final String MAXCOUNT = ConfigDateApplication.getInstance().getServicePropertie("RingAndImage.MaxCount").trim();
	// 保留历史记录条数
	public static final String HAS_LIST = ConfigDateApplication.getInstance().getServicePropertie("has.list").trim();

	// 操作文档或
	public static final String DOC_NAME = ConfigDateApplication.getInstance().getServicePropertie("doc.name").trim();

	// 广告合作
	public static final String OUR_PARTNERS = ConfigDateApplication.getInstance().getServicePropertie("local.our.partners").trim();

	public static final String LOCAL_PUSH = ConfigDateApplication.getInstance().getServicePropertie("local.push").trim();
	
	public static final String LOCAL_BBS_PATH = ConfigDateApplication.getInstance().getServicePropertie("local.bbs").trim();


	// 语言设置
	// 英文en 法文fr 中文zh 德文de 日文ja 韩文ko
	public static final String[] luanguage = { "en", "fr", "zh", "de", "ja", "ko" };

	public static final String PIC = "JPG,GIF,PNG,BMP,JPEG";

	public static final String MUSIC = "MP3,WMA,WAV,MOD,RA,CD,MD,ASF,AAC,Mp3Pro,VQF,FLAC,APE,MID,OGG,M4A,AAC+,VQF";

	public static final String VIEW = "AVI,WMA,RMVB,RM,FLASH,MP4,MID,3GP";

	public static final String APK = "APK";

	public static final String TXT = "TXT";

	/************* other*****variable ********************/

	public static final String IMG_ADR = "1"; // 图片标识

	public static final String APK_ADR = "2"; // apk标识

	public static final String LOGO_ADR = "3"; // logo标识

	public static final String MUSIC_ADR = "4"; // 音乐标识

	public static final String VIEW_ADR = "5"; // 视屏标识

	public static final String TITLE_ADR = "6"; // 标题标识

	public static final String CATEGORY_ADR = "7"; // 分类标识

	public static final String APP_DESCRIBE_ADR = "9"; // 分类标识

	public static final String APP_UPDATEINFO_ADR = "10"; // 分类标识

	public static final String APP_TXT = "11"; // 分类标识

	public static final String PATCH_ADR = "12"; // 分类标识

	public static final String PARTNERS_ADR = "13"; // 广告合作

	public static final String APK_FTP_PATH = ConfigDateApplication.getInstance().getServicePropertie("apk.ftp.path").trim();

	public static String FTP_UPLOAD_PATH = "";

	public static final int BLACK_LIST = 0; // 黑名单

	public static final int AUTO_UPDATE = 1; // 自动根据

	public static final int INCREMENT = 2; // 增量更新

	public static int getInt(String str) {
		try {
			return Integer.parseInt(str.trim());
		} catch (Exception e) {
		}
		return 0;
	}

	public static int getInt(String str, int def) {
		try {
			return Integer.parseInt(str.trim());
		} catch (Exception e) {
		}
		return def;
	}
}
