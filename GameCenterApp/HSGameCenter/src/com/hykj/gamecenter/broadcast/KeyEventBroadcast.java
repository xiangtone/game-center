package com.hykj.gamecenter.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.logic.BroadcastManager;
import com.hykj.gamecenter.logic.NotificationCenter;
import com.hykj.gamecenter.utils.Logger;

//监听home键
public class KeyEventBroadcast extends BroadcastReceiver {
	private static final String TAG = "KeyEventBroadcast";

	final String SYSTEM_DIALOG_REASON_KEY = "reason";
	final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
	final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"; // 长按home key
	final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"; // 按下home Key

	/*
	 * 在每次点击Home按键时都会发出一个action为 Intent.ACTION_CLOSE_SYSTEM_DIALOGS的广播，
	 * 它是关闭系统Dialog的广播， 我们可以通过注册它来监听Home按键消息，我自定义了一个home按键监听工具类
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
			if (reason != null) {
				if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)
						|| reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {// 按Home按键
																				// 或
																				// 长按Home按键

					if (!isRepeatSend()) {
						Logger.i(TAG, "------------------");
						BroadcastManager.getInstance().sendBroadCastToDesk(
								false);
					}

					// homeKey按下
					if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
						NotificationCenter.sendHomeKeyPressedBroadcast();
						Log.d(TAG, "sendHomeKeyPressedBroadcast");
						Logger.i(TAG, "sendHomeKeyPressedBroadcast");
					}
				}
			}
		}
	}

	private static long lastClickTime = 0;

	private static synchronized boolean isRepeatSend() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) { // 500ms内不能同事起效
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public void registerReceive() {
		IntentFilter keyEventFilter = new IntentFilter();
		keyEventFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		App.getAppContext().registerReceiver(this, keyEventFilter);
	}

	public void unRegisterReceive() {
		try {
			App.getAppContext().unregisterReceiver(this);
		} catch (Exception e) {
			Logger.e(TAG, "KeyEvent is unregisterReceiver is Error");
		}
	}

}
