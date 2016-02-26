package com.x.db.resource;

import java.util.HashSet;

/**
 * 
 
 *
 */
public class NativeResourceConstant {

	public static final int COLUMN_ID = 0;
	public static final int COLUMN_PATH = 1;
	public static final int COLUMN_SIZE = 2;
	public static final int COLUMN_MODIFIEDDATE = 3;
	
	public static final int MUSIC_ALBUMID = 4;
	public static final int MUSIC_DURATION = 5;

	public static final int MUSIC_FRAGMENT = 0;
	public static final int PIC_FRAGMENT = 1;
	public static final int VIDEO_FRAGMENT = 2;
	public static final int DOC_FRAGMENT = 3;
	public static final int APPS_FRAGMENT = 4;

	// 0=正常，1=编辑，2=全选，3=反选
	public static final int NORMAL = 0;
	public static final int EDIT = 1;
	public static final int ALL = 2;
	public static final int NOALL = 3;

	//窗口显示模式
	public static final int DEF_MODE = 0;//默认模式
	public static final int SHARE_MODE = 1;//分享模式
	
	//图片资源显示模式
	public static final int FOLDER_MODE = 0;//目录
	public static final int PIC_MODE = 1;//子目录
	public static final int SELECT_FOLDER = 2;//选择目录
	public static final int SELECT_PIC = 3;//选择子目录
	
	// 资源类型
	public enum Category {
		MUSIC, VIDEO, PICTURE, DOC, VIDEO_THUMBNAILS, ALBUM, UNKNOWN
	}

	/* 文本类型 */
	public static class FileType {
		public static final int UNKNOWN = 0;
		public static final int PDF = 1;
		public static final int DOC = 2;
		public static final int XLS = 3;
		public static final int TXT = 4;
		public static final int PPT = 5;
		public static final int APK = 6;
		public static final int MUSIC = 7;
		public static final int VIDEO = 8;
		public static final int PICTURE = 9;
	}

	// 排序类型
	public enum SortMethod {
		NAME, SIZE, DATE, TYPE, ALBUM, NOT
	}

	/* 音频设置类型 */
	public enum Voice {
		RINGTONES, NOTIFICATION, ALARM, ALL
	}

	//document类型过滤
	public static HashSet<String> sDocMimeTypesSet = new HashSet<String>() {
		{ //new
			add("%.pdf");
			add("%.doc%");
			add("%.xls%");
			add("%.text");
			add("%.txt");
			add("%.ppt");

			//Deprecated
			//			add("text/plain");
			//	        add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
			//	        add("application/msword");
			//	        add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");;
			//	        add("application/vnd.ms-excel");
			//	        add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			//	        add("application/pdf");
			//	        add("text/xml");
		}
	};

	//4个分类
	public static Category[] sCategories = new Category[] { Category.MUSIC, Category.VIDEO, Category.PICTURE,
			Category.DOC };
}
