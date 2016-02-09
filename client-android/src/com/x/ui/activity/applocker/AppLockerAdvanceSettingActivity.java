/**   
 * @Title: AppLockerAdvanceSetting.java
 * @Package com.mas.amineappstore.ui.activity.applocker
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-10-15 上午10:39:47
 * @version V1.0   
 */

package com.x.ui.activity.applocker;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.applocker.LockManager;
import com.x.business.skin.SkinConfigManager;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.ScrollerNumberPicker;

/**
 * @ClassName: AppLockerAdvanceSetting
 * @Description: AppLock高级设置页面
 
 * @date 2014-10-15 上午10:39:47
 * 
 */

public class AppLockerAdvanceSettingActivity extends BaseActivity implements View.OnClickListener {
	/*
	 * 重设密码相关控件
	 */
	RelativeLayout resetPassword; // 高级设置重设密码 布局控件
	Button fromTimeUp, fromTimeDown, toTimeUp, toTimeDown; // 高级设置，起始时间和终止时间的 选择
															// + - 按钮
	TextView fromTimeTv, toTimeTv;
	int fromTime;
	int toTime;
	private Context context;
	private ScrollerNumberPicker fromTimeHourNumberPicker, toTimeHourNumberPicker;
	private LockManager lockManager;
	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applocker_advance_setting);
		context = this;
		lockManager = LockManager.getInstance(context);
		initViews();// 初始化页面
		initTitle();
		setListenerEvent();// 设置所有监听
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
	}

	private void initTitle() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);

		mTitleTv.setText(R.string.page_applocker_advance_settings);
	}

	/*
	 * 高级设置里的重置密码选项的监听
	 */
	public OnClickListener onClickAdvanceSettingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			// 转 重设密码页面
			case R.id.ll_reset_password:
				Intent intentLock = new Intent();
				intentLock.setClass(getApplicationContext(), ApplockerPwdSetterActivity.class);
				ApplockerPwdSetterActivity.START_SRC = 2;
				startActivity(intentLock);
				//				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;

			default:
				break;
			}
		}
	};

	/**装载数据 0-23  1-24*/
	private ArrayList<String> getDataList(int startNum, int endNum) {
		ArrayList<String> dataList = new ArrayList<String>();
		for (int i = startNum; i <= endNum; i++) {
			//处理拼接时间数字
			if (i < 10) {
				dataList.add("0" + i);
			} else {
				dataList.add("" + i);
			}
		}
		return dataList;

	}

	// 初始化页面
	private void initViews() {
		resetPassword = (RelativeLayout) findViewById(R.id.ll_reset_password);
		fromTimeHourNumberPicker = (ScrollerNumberPicker) findViewById(R.id.from_time_hour);
		toTimeHourNumberPicker = (ScrollerNumberPicker) findViewById(R.id.to_time_hour);
		fromTimeHourNumberPicker.setData(getDataList(0, 23));
		toTimeHourNumberPicker.setData(getDataList(1, 24));
		fromTimeHourNumberPicker.setOnSelectListener(new StartTimeScrollListener());
		toTimeHourNumberPicker.setOnSelectListener(new StopTimeScrollListener());

		fromTimeHourNumberPicker.setDefault(Integer.valueOf(lockManager.getStartLockTime()));
		toTimeHourNumberPicker.setDefault(Integer.valueOf(lockManager.getStopLockTime()) - 1);
	}

	private void setListenerEvent() {
		/* 重设密码布局控件的监听跳转动作 */
		resetPassword.setOnClickListener(onClickAdvanceSettingListener);
	}

	/**滑动时间选择器的监听 */
	private class StartTimeScrollListener implements ScrollerNumberPicker.OnSelectListener {
		@Override
		public void endSelect(int id, String text) {
			int stopLockTime = Integer.valueOf(lockManager.getStopLockTime());
			int startLockTime = Integer.valueOf(text);
			lockManager.saveStartLockTime(text);
			if (startLockTime >= stopLockTime) {
				toTimeHourNumberPicker.setDefault(startLockTime);
				lockManager.saveStopLockTime(String.valueOf(startLockTime + 1));
			}
		}

		@Override
		public void selecting(int id, String text) {

		}

	}

	private class StopTimeScrollListener implements ScrollerNumberPicker.OnSelectListener {

		@Override
		public void endSelect(int id, String text) {
			int stopLockTime = Integer.valueOf(text);
			int startLockTime = Integer.valueOf(lockManager.getStartLockTime());
			lockManager.saveStopLockTime(text);
			if (startLockTime >= stopLockTime) {
				fromTimeHourNumberPicker.setDefault(stopLockTime - 1);
				lockManager.saveStartLockTime(String.valueOf(stopLockTime - 1));
			}
		}

		@Override
		public void selecting(int id, String text) {

		}

	}

	private void setTimeData() {
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
	}
}
