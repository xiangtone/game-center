package com.x.publics.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences存储数据方式工具类
 
 *
 */
public class SharedPrefsUtil {

	public final static String SETTING = "rave_config";
	public final static String WALLPAPER = "wallpaper_oneClick_download";
	/*
	 * 2014-10-10 13:56:33 wei.luo
	 * 添加applocker密码
	 */
	public final static String APPLOCKER = "applocker";

	public static int MODE = Context.MODE_PRIVATE;

	public static void putValue(Context context, String key, int value) {
		Editor sp = context.getSharedPreferences(SETTING, MODE).edit();
		sp.putInt(key, value);
		sp.commit();
	}

	public static void putValue(Context context, String key, float value) {
		Editor sp = context.getSharedPreferences(SETTING, MODE).edit();
		sp.putFloat(key, value);
		sp.commit();
	}

	public static void putValue(Context context, String key, long value) {
		Editor sp = context.getSharedPreferences(SETTING, MODE).edit();
		sp.putLong(key, value);
		sp.commit();
	}

	public static void putValue(Context context, String key, boolean value) {
		Editor sp = context.getSharedPreferences(SETTING, MODE).edit();
		sp.putBoolean(key, value);
		sp.commit();
	}

	public static void putValue(Context context, String key, String value) {
		Editor sp = context.getSharedPreferences(SETTING, MODE).edit();
		sp.putString(key, value);
		sp.commit();
	}

	public static int getValue(Context context, String key, int defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING, MODE);
		int value = sp.getInt(key, defValue);
		return value;
	}

	public static Float getValue(Context context, String key, Float defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING, MODE);
		Float value = sp.getFloat(key, defValue);
		return value;
	}

	public static long getValue(Context context, String key, long defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING, MODE);
		long value = sp.getLong(key, defValue);
		return value;
	}

	public static boolean getValue(Context context, String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING, MODE);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}

	public static String getValue(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(SETTING, MODE);
		String value = sp.getString(key, defValue);
		return value;
	}

	/**
	 * 存储一键下载主题记录
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putThemeValue(Context context, String key, boolean value) {
		Editor sp = context.getSharedPreferences(WALLPAPER, MODE).edit();
		sp.putBoolean(key, value);
		sp.commit();
	}

	/**
	 * 获取一键下载主题记录
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getThemeValue(Context context, String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(WALLPAPER, MODE);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}

	/**
	 * 清除一键下载记录
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void clearThemeValue(Context context) {
		Editor sp = context.getSharedPreferences(WALLPAPER, MODE).edit();
		sp.clear();
		sp.commit();
	}

	/**
	* @Title: removeValue 
	* @Description: 删除指定的记录
	* @param @param context
	* @param @param key   
	* @return void   
	* @throws
	 */
	public static void removeValue(Context context, String key) {
		Editor sp = context.getSharedPreferences(SETTING, MODE).edit();
		sp.remove(key);
		sp.commit();
	}

	/**
	 * put String
	* @Title: putLockerValue 
	* @Description: TODO record the password
	* @param @param context
	* @param @param key 
	* @param @param value    
	* @return void
	* @time 2014-10-10 14:01:25
	 */
	public static void putLockerValue(Context context, String key, String value) {
		Editor editor = context.getSharedPreferences(APPLOCKER, MODE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * put boolean
	* @Title: putLockerValue 
	* @Description: TODO 
	* @param @param context
	* @param @param key
	* @param @param value    
	* @return void
	 */
	public static void putLockerValue(Context context, String key, Boolean value) {
		Editor editor = context.getSharedPreferences(APPLOCKER, MODE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 
	* @Title: getLockerValue 
	* @Description: TODO get the password
	* @param @param context
	* @param @param key
	* @param @param defValue
	* @param @return    
	* @return String
	 */
	public static String getLockerValue(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(APPLOCKER, MODE);
		return sp.getString(key, defValue);
	}

	/**
	 * 
	* @Title: getLockerValue 
	* @Description: TODO 
	* @param @param context
	* @param @param key
	* @param @param defValue
	* @param @return    
	* @return boolean
	 */
	public static boolean getLockerValue(Context context, String key, Boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(APPLOCKER, MODE);
		return sp.getBoolean(key, defValue);
	}

	/**
	 * 
	* @Title: removeLockerValue 
	* @Description: TODO delete the password
	* @param @param context
	* @param @param key    
	* @return void
	 */
	public static void removeLockerValue(Context context, String key) {
		Editor editor = context.getSharedPreferences(APPLOCKER, MODE).edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 
	* @Title: isFirstUseAppLocker 
	* @Description: TODO Checks whether the preferences contains the key.
	* @param @param context
	* @param @param key
	* @param @return    
	* @return boolean
	 */
	public static boolean isFlagExist(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(APPLOCKER, MODE);
		return sp.contains(key);
	}

}
