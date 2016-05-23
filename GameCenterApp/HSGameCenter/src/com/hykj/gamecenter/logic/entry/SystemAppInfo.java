package com.hykj.gamecenter.logic.entry;

import java.util.ArrayList;

/**
 * 所有系统应用的包名
 * 
 * @return
 */
public class SystemAppInfo {

	public static final ArrayList<String> allSystemApp() {
		ArrayList<String> systemInfoList = new ArrayList<String>();

		systemInfoList.add("com.android.gallery3d");
		systemInfoList.add("com.android.browser");
		systemInfoList.add("com.android.calculator2");
		systemInfoList.add("com.android.calendar");
		systemInfoList.add("com.android.deskclock");
		systemInfoList.add("com.android.feedback");
		systemInfoList.add("com.android.email");
		systemInfoList.add("com.google.android.music");
		// 安卓原生应用排除
		// systemInfoList.add( "com.android.vending" );
		systemInfoList.add("com.android.settings");
		systemInfoList.add("com.android.providers.downloads.ui");
		systemInfoList.add("com.google.android.googlequicksearchbox");
		systemInfoList.add("com.android.apkmanager");
		systemInfoList.add("com.softwinner.fireplayer");
		systemInfoList.add("com.softwinner.memory");
		systemInfoList.add("com.google.android.calendar");
		systemInfoList.add("com.google.android.gm");
		systemInfoList.add("com.android.music ");
		systemInfoList.add("com.softwinner.explore");
		systemInfoList.add("com.hykj.gamecenter");
		systemInfoList.add("com.cs.appstore");
		systemInfoList.add("com.sohu.inputmethod.sogou");
		systemInfoList.add("com.google.process.location");
		systemInfoList.add("com.android.phone");
		systemInfoList.add("com.android.systemui");

		return systemInfoList;
	}

	public static final String accountCenterApp() {
		return "com.cs.cssf";
	}

}
