package com.hykj.gamecenter.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hykj.gamecenter.App;

public class UpdateUtils {

	private static final String UPDATE_CHECK = "HAS_UPDATE";
	private static final String UPDATE_CHECK_TIME = "UPDATE_TIME";
	private static final String UPDATE_TYPE = "UPDATE_TYPE";
	private static final String UPDATE_STATE = "UPDATE_STATE";
	private static final String TAG = "UpdateUtils";
	public static final String UPDATE_CHECK_RECOMM_TIME = "UPDATE_CHECK_RECOMM_TIME";
	private static final String UPDATE_TIME_LAP_KEY = "UPDATE_TIME_LAP_KEY";

	public static long getUpdateTimeLap() {
		UPDATE_TIME_LAP =  App.getSharedPreference().getLong(UPDATE_TIME_LAP_KEY, UPDATE_TIME_LAP);
		return UPDATE_TIME_LAP;
	}

	private static long UPDATE_TIME_LAP = 1000 * 60 * 60 * 6; // 间隔UPDATE_TIME_LAP小时检查
	private static final int RESET_UPDATE_STATE = 1000 * 60 * 10;// 间隔10min重置下载状态
	private static long UPDATE_RECOMM = 1000 * 60 * 60 * 24 * 7; //

	public static void setUpdateRecomm(final long time) {
		if (time != 0) {
			UPDATE_RECOMM = time;
		}
	}

	public static boolean shouldCheckRecommTiem() {
		SharedPreferences sp = App.getSharedPreference();
		long lastCheck = sp.getLong(UPDATE_CHECK_RECOMM_TIME, 0);
		long current = System.currentTimeMillis();
		return current > lastCheck && (current - lastCheck) > UPDATE_RECOMM;
	}

	public static void setUpdateRecommTimePreference() {
		Editor editor = App.getSharedPreference().edit();
		editor.putLong(UPDATE_CHECK_RECOMM_TIME, System.currentTimeMillis());
		editor.apply();
	}

	// 设置更新间隔单位为second
	public static void setUpdateCheckRate(final long iUpdateInterval) {
		if (iUpdateInterval != 0) {
			UPDATE_TIME_LAP = iUpdateInterval;
			Editor edit = App.getSharedPreference().edit();
			edit.putLong(UPDATE_TIME_LAP_KEY, UPDATE_TIME_LAP);
			edit.apply();

		}
	}

	public static void setUpdatePreference(boolean hasUpdate, int updateType) {
		Editor editor = App.getSharedPreference().edit();
		editor.putBoolean(UPDATE_CHECK, hasUpdate);
		editor.putLong(UPDATE_CHECK_TIME, System.currentTimeMillis());
		editor.putInt(UPDATE_TYPE, updateType);
		editor.commit();
	}

	public static boolean hasUpdate() {
		return App.getSharedPreference().getBoolean(UPDATE_CHECK, false);
	}

	public static boolean shouldCheckUpdate() {
		SharedPreferences sp = App.getSharedPreference();
		int lastupdateType = sp.getInt(UPDATE_TYPE, 0);
		if (lastupdateType == 3) {
			return true;
		}
		long lastCheck = sp.getLong(UPDATE_CHECK_TIME, 0);
		long current = System.currentTimeMillis();
		Logger.i(TAG, "shouldCheckUpdate " + "current " + current + " lastCheck " + lastCheck + " update_time_lap " + UPDATE_TIME_LAP, "oddshou");
		return current > lastCheck && (current - lastCheck) > UPDATE_TIME_LAP;
	}

	public static boolean getUpdateState() {
		SharedPreferences sp = App.getSharedPreference();
		return sp.getBoolean(UPDATE_STATE, false);
	}

	public static void saveUpdateState(boolean state) {
		Editor editor = App.getSharedPreference().edit();
		editor.putBoolean(UPDATE_STATE, state);
		editor.commit();
	}

	public static boolean shouldResetUpdateState() {
		SharedPreferences sp = App.getSharedPreference();
		long lastCheck = sp.getLong(UPDATE_CHECK_TIME, 0);
		long current = System.currentTimeMillis();
		return current > lastCheck && (current - lastCheck) > RESET_UPDATE_STATE;
	}
}
