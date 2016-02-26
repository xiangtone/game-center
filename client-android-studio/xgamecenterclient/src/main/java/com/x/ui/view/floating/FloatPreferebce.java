package com.x.ui.view.floating;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
* @ClassName: FloatPreferebce
* @Description: 悬浮窗数据存储

* @date 2014-5-20 下午4:38:56
*
 */
public class FloatPreferebce {
	private static Context mContext;
	private static FloatPreferebce floatPreferebce = null;
	
	private FloatPreferebce() {}
	
	public final static FloatPreferebce getInstance(Context context){
		if(floatPreferebce == null){
			floatPreferebce = new FloatPreferebce();
		}
		mContext = context;
		return floatPreferebce;
	}

	private SharedPreferences getSharedPreferences() {
		return mContext.getSharedPreferences(MConstants.PERFERENCE_NAME, Context.MODE_PRIVATE);
	}

	private Editor getEditer() {
		return getSharedPreferences().edit();
	}

	public float getFloatX() {
		SharedPreferences preferences = getSharedPreferences();
		return preferences.getFloat(MConstants.PREF_FLOAT_X, 0f);
	}

	public void setFloatX(float x) {
		Editor editor = getEditer();
		editor.putFloat(MConstants.PREF_FLOAT_X, x);
		editor.commit();
	}

	public float getFloatY() {
		SharedPreferences preferences = getSharedPreferences();
		return preferences.getFloat(MConstants.PREF_FLOAT_Y, 0f);
	}

	public void setFloatY(float y) {
		Editor editor = getEditer();
		editor.putFloat(MConstants.PREF_FLOAT_Y, y);
		editor.commit();
	}

	public int getOrientation(){
		SharedPreferences preferences = getSharedPreferences();
		return preferences.getInt(MConstants.PREF_ORIENTATION, -1);
	}
	
	public void setOrientation(int orientation){
		Editor editor = getEditer();
		editor.putInt(MConstants.PREF_ORIENTATION, orientation);
		editor.commit();
	}
	
	public float getDegree(){
		SharedPreferences preferences = getSharedPreferences();
		return preferences.getFloat(MConstants.MENU_DEGREE, -1);
	}
	
	public void setDegree(float degree){
		Editor editor = getEditer();
		editor.putFloat(MConstants.MENU_DEGREE, degree);
		editor.commit();
	}
	
	public boolean getSearch(){
		SharedPreferences preferences = getSharedPreferences();
		return preferences.getBoolean(MConstants.IS_SEARCH, false);
	}
	
	public void setSearch(boolean search){
		Editor editor = getEditer();
		editor.putBoolean(MConstants.IS_SEARCH, search);
		editor.commit();
	}
	
	public long getCoolingTime(){
		SharedPreferences preferences = getSharedPreferences();
		return preferences.getLong(MConstants.COOLING_TIME, -1);
	}
	
	public void setCoolingTime(long time){
		Editor editor = getEditer();
		editor.putLong(MConstants.COOLING_TIME, time);
		editor.commit();
	}	
	
	//Temp
	public void setTempSave(String key, String value){
		Editor editor = getEditer();
		editor.putString(key, value);
		editor.commit();
	}
	public String getTempSave(String key){
		SharedPreferences preferences = getSharedPreferences();
		return preferences.getString(key, null);
	}

	/*公共参数*/
	public class MConstants {
		private static final String PERFERENCE_NAME = "zApp_floating";
		private static final String PREF_FLOAT_X = "float_x";
		private static final String PREF_FLOAT_Y = "float_y";
		private static final String PREF_ORIENTATION = "orientation";
		private static final String MENU_DEGREE = "deta_degree";
		private static final String IS_SEARCH = "is_search";
		private static final String COOLING_TIME = "cooling_time";
		//常量
		public static final int VERTICAL_LEFT = 1;
		public static final int VERTICAL_RIGHT = 2;
	}
}
