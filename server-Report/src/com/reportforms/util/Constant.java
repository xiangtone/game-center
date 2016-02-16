package com.reportforms.util;

import java.io.Serializable;

public class Constant {
	// 服务器路径 "/usr/widget_local"
	// 本地测试路径 "f:/widget_local"

	public static final String LOCAL_FILE_PATH = "/usr/local/kasvaac/widget_local";

	public static final String LOG_FILE = "/usr/local/kasvaac/widget_log/loud_widget.log";

	public static final String LOCAL_APK_PATH = LOCAL_FILE_PATH + "/app";

	public static final String LOCAL_IMAGE_PATH = LOCAL_FILE_PATH + "/image/";

	private static final String OSS_URL = "";

	public static final String APK_URL = "/app/";

	public static final String IMG_URL = "/image/";

	public static final String OSS_APK_URL = OSS_URL + APK_URL;

	public static final String OSS_IMG_URL = OSS_URL + IMG_URL;

	public static final String OSS_BUCKETNAME = "widget";

	// s3路径
	public static final String S3_URL = "";

	public static final String S3_BUCKET = "onetouch_store";

	public static final String S3_APK_URL = S3_URL + "/apk/";

	public static final String S3_IMG_URL = S3_URL + "/image/";

	public static final String FOLDER_APK = "apk/";

	public static final String FOLDER_IMAGE = "image/";

	// 基本参数
	// public static final int BLACK_LIST = 0;//对应phone_apk表，为0表示为黑名单，不上线资源

	// 语言设置
	// 英文en 法文fr 中文zh 德文de 日文ja 韩文ko
	public static final String[] luanguage = { "en", "fr", "zh", "de", "ja",
			"ko" };

	// 缓存名字
	public static final String TEST = "test"; // 测试缓存
	public static final String AD_CACHE = "ad_cache";// 广告缓存
	public static final String COUNTRY_CACHE = "country_cache";// 国家缓存
	public static final String SOURCEINFO_CACHE = "sourceInfo_cache";// apk缓存
	public static final String COLLECTIONAPK_CACHE = "collectionApk_cache";
	public static final String SOURCELANGUAGE_CACHE = "sourceLanguage_cache";

	public static final String PHONE_CACHE = "phone_cache";
}
