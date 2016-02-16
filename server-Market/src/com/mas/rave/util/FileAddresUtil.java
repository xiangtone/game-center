package com.mas.rave.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 文件路径工具类
 * 
 * @author liwei.sz
 * 
 */
public class FileAddresUtil {
	static Logger logger = Logger.getLogger(FileAddresUtil.class);

	// 组装文件地址
	public static String getFilePath(File file) {
		return file.getPath().substring(Constant.LOCAL_FILE_PATH.length(), file.getPath().length());
	}

	// 生成无重复文件名
	public static String randomFileName(String fileName) {
		String[] strs = fileName.split("\\.");
		return MD5.getMd5Value(System.currentTimeMillis() + fileName) + "." + strs[strs.length - 1];
	}

	// 组装文件路径
	public static String getFilePath(int raveId, int cpId, int channelId, int appId, String type) {
		// 获取文件根路径
		StringBuffer str = new StringBuffer();
		str.append(raveId);
		str.append(File.separator);
		str.append(cpId);
		str.append(File.separator);
		str.append(channelId);
		str.append(File.separator);
		str.append(appId);
		if (type.equals(Constant.IMG_ADR)) {
			// 组装图片
			str.append(Constant.LOCAL_APK_PIC);
		} else if (type.equals(Constant.APK_ADR)) {
			// 组装apk
			str.append(Constant.LOCAL_APK_PATH);
		} else if (type.equals(Constant.LOGO_ADR)) {
			// 组装log
			str.append(Constant.LOCAL_APK_LOG);
		} else if (type.equals(Constant.MUSIC_ADR)) {
			// 组装音乐
			str.append(Constant.LOCAL_MUSIC_PATH);
		} else if (type.equals(Constant.VIEW_ADR)) {
			// 组装视屏
			str.append(Constant.LOCAL_VIEW_PATH);
		} else if (type.equals(Constant.PATCH_ADR)) {
			// 组装patch
			str.append(Constant.LOCAL_PATCH_PATH);
		}
		return str.toString();
	}

	// 根据文件名创建对应文件
	public static String getSuffix(String fileName) {
		String suffix = FileUtil.getFileSuffix(fileName);
		if (StringUtils.isNotEmpty(suffix)) {
			suffix = suffix.toLowerCase();
			if (Constant.MUSIC.toLowerCase().indexOf(suffix) != -1) {
				// 返回音乐
				return Constant.MUSIC_ADR;
			} else if (Constant.VIEW.toLowerCase().indexOf(suffix) != -1) {
				// 返回视屏文件
				return Constant.VIEW_ADR;
			} else if (Constant.APK.toLowerCase().indexOf(suffix) != -1) {
				// 返回apk文件
				return Constant.APK_ADR;
			} else if (Constant.PIC.toLowerCase().indexOf(suffix) != -1) {
				// 返回图片
				return Constant.IMG_ADR;
			} else if (Constant.TXT.toLowerCase().indexOf(suffix) != -1) {
				// 返回文本
				return Constant.TXT;
			}
		}
		// 无后缀默认给0
		return "0";
	}

	// 多个文件后缀检测
	// 根据文件名创建对应文件
	public static String getSuffixs(String file) {
		// 分割出多个文件
		String[] strs = file.split(",");
		String str = "0";
		if (strs != null && strs.length > 0) {
			for (int i = 0; i < strs.length; i++) {
				String suffix = FileUtil.getFileSuffix(strs[i]);
				if (Constant.MUSIC.toLowerCase().indexOf(suffix) != -1) {
					// 返回音乐
					str = Constant.MUSIC_ADR;
				} else if (Constant.VIEW.toLowerCase().indexOf(suffix) != -1) {
					// 返回视屏文件
					str = Constant.VIEW_ADR;
				} else if (Constant.APK.toLowerCase().indexOf(suffix) != -1) {
					// 返回apk文件
					str = Constant.APK_ADR;
				} else if (Constant.PIC.toLowerCase().indexOf(suffix) != -1) {
					// 返回图片
					str = Constant.IMG_ADR;
				} else {
					break;
				}
			}
		}
		return str;
	}

	// 组装标题文件目录　
	public static String getTitlePath(int appAlbumId, int apkId, String type) {
		// 获取文件根路径
		StringBuffer str = new StringBuffer();
		if (type.equals(Constant.TITLE_ADR)) {
			// 组装标题
			str.append(Constant.LOCAL_TITLE_PATH);
			str.append(File.separator);
			str.append(appAlbumId);
			str.append(File.separator);
			str.append(apkId);
			str.append(File.separator);
		} else if (type.equals(Constant.CATEGORY_ADR)) {
			str.append(Constant.LOCAL_CATEGORY_PATH);
			str.append(File.separator);
			str.append(appAlbumId);
			str.append(File.separator);
			str.append(apkId);
			str.append(File.separator);
		} else if (type.equals(Constant.PARTNERS_ADR)) {
			str.append(Constant.OUR_PARTNERS);
			str.append(File.separator);
			str.append(appAlbumId);
			str.append(File.separator);
			str.append(apkId);
			str.append(File.separator);
		}
		return str.toString();
	}

	// 获取app皮肤地址
	public static String getSkinPath(int flag) {
		String url = "zapp" + File.separator + "skin" + File.separator;
		if (flag == 1) {
			// logo
			url += "logo" + File.separator;
		} else {
			// apk
			url += "apk" + File.separator;
		}
		return url;
	}
}
