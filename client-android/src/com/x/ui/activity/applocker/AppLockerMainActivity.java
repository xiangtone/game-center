/**   
 * @Title: AppLockerMainActivity.java
 * @Package com.mas.amineappstore.ui.activity.applocker
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-10-14 下午3:42:01
 * @version V1.0   
 */

package com.x.ui.activity.applocker;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.x.R;
import com.x.business.applocker.LockManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.ui.activity.base.BaseActivity;

/**
 * @ClassName: AppLockerMainActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-10-14 下午3:42:01
 * 
 */

public class AppLockerMainActivity extends BaseActivity implements View.OnClickListener {

	private Context context = this;
	private LockManager lockManager = LockManager.getInstance(this);
	private ToggleButton applock_main_islock; // 总开关
	private RelativeLayout ll_Lock_List, ll_Advance_Setting;// 上锁清单 高级设置
	private TextView app_Lock_list_Count_Desc, app_Lock_list_Count;
	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applocker_main);
		initViews();
		initTitle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		registerBroadRec();
		applock_main_islock.setChecked(lockManager.getLockStatus());
		setLockListDesc(); // 改变主页面 ，上锁应用数量描述
	}

	private void registerBroadRec() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ApplockerPasswordActivity.CLOSE_APPLOCKER_MAIN_ACTION);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	private void initTitle() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);

		mTitleTv.setText(R.string.page_applocker);
	}

	private void initViews() {
		// start unclock activity when you already set password
		if (!lockManager.isFirstUse()) {
			ApplockerPasswordActivity.flag = true;
			startActivity(new Intent(this, ApplockerPasswordActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
			//			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		}
		applock_main_islock = (ToggleButton) findViewById(R.id.applock_main_islock);
		ll_Lock_List = (RelativeLayout) findViewById(R.id.ll_lock_list);
		ll_Advance_Setting = (RelativeLayout) findViewById(R.id.ll_advance_setting);
		applock_main_islock.setOnClickListener(onClickListener);
		// init lock switch state
		ll_Lock_List.setOnClickListener(onClickLockedAppListListener); // 进入上锁清单点击监听
		ll_Advance_Setting.setOnClickListener(onClickLockedAppListListener); // 进入高级设置点击监听

		app_Lock_list_Count = (TextView) findViewById(R.id.app_lock_list_count); // 数量
		app_Lock_list_Count_Desc = (TextView) findViewById(R.id.app_lock_list_count_desc); // 数量描述后缀
	}

	// 设置main页面中，清单列表 下方 具体上锁应用数量的描述
	private void setLockListDesc() {
		List<String> appLockList = lockManager.findLockeredApp();
		int appCounts = appLockList.size();

		if (appCounts != 0) {
			app_Lock_list_Count.setVisibility(View.VISIBLE); // display数字
			// 根据选择上锁应用数量，改变 数量描述
			app_Lock_list_Count.setText(String.valueOf(appCounts) + " ");
			app_Lock_list_Count_Desc.setVisibility(View.VISIBLE);// 隐藏数字描述后缀
			app_Lock_list_Count_Desc.setText(R.string.app_lock_list_count_desc);
		} else {
			// 未选则上锁应用，则改变 数量描述。隐藏原来文本，显示新内容
			app_Lock_list_Count.setVisibility(View.GONE); // 隐藏数字
			app_Lock_list_Count_Desc.setText(R.string.app_lock_list_nonelocked_desc);
		}
	}

	// 开关锁
	public OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (lockManager.isFirstUse()) {
				lockManager.saveLockStatus(false);
				ApplockerPwdSetterActivity.START_SRC = 1;
				startActivity(new Intent(AppLockerMainActivity.this, ApplockerPwdSetterActivity.class));
			} else {
				lockManager.saveLockStatus(!lockManager.getLockStatus());
				if (lockManager.getLockStatus()) {
					lockManager.startApplockerComponent();
				} else {
					lockManager.stopApplockerComponent();
				}
				lockManager.saveLockStatus(applock_main_islock.isChecked());
			}
		}
	};

	// 上锁清单 和高级设置 监听
	public OnClickListener onClickLockedAppListListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			// 转 普通锁
			case R.id.ll_lock_list:
				startActivity(new Intent(getApplicationContext(), AppCommonLockerActivity.class));
				//				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;
			// 转高级设置和时间锁
			case R.id.ll_advance_setting:
				startActivity(new Intent(getApplicationContext(), AppLockerAdvanceSettingActivity.class));
				//				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;
			default:
				break;
			}
		}
	};

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
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
		SkinConfigManager.getInstance().setViewBackground(context, ll_Lock_List, SkinConstan.LIST_VIEW_ITEM_BG);
		SkinConfigManager.getInstance().setViewBackground(context, ll_Advance_Setting, SkinConstan.LIST_VIEW_ITEM_BG);
		SkinConfigManager.getInstance()
				.setViewBackground(context, applock_main_islock, SkinConstan.SETTINGS_TOGGLE_BTN);
	}
}
