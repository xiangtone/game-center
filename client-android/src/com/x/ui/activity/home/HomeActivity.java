/**   
* @Title: HomeActivity.java
* @Package com.x.ui.activity.home
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-28 上午10:14:57
* @version V1.0   
*/

package com.x.ui.activity.home;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.x.R;
import com.nineoldandroids.view.ViewHelper;
//import com.testin.agent.TestinAgent;
import com.x.business.audio.AudioEffectManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.guide.GuideMainActivity;
import com.x.ui.activity.home.SlidingPaneHomeFragment.OnShowRedTipsListener;
import com.x.ui.view.MyCheckBoxDialog;
import com.x.ui.view.MySlidingPaneLayout;

/**
* @ClassName: HomeActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-28 上午10:14:57
* 
*/

public class HomeActivity extends BaseActivity implements OnShowRedTipsListener {

	private final Context context = this;
	private FinishActivityBroadcast finishActivityBroadcast;
	private boolean inited, isPanelOpen;
	private SlidingPaneMenuFragment menuFragment;
	private SlidingPaneHomeFragment homeFragment;
	public MySlidingPaneLayout mSlidingPaneLayout;
	private final DisplayMetrics displayMetrics = new DisplayMetrics();
	private int maxMargin = 0;

	// 新手引导
	private boolean mainIsFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		TestinAgent.init(this);
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		setContentView(R.layout.activity_home);
		SharedPrefsUtil.clearThemeValue(this); // 清除一键下载记录
		showGuideActivity();
		initSlidingPanelayout();
		initSlidingFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		if (!inited)
			registBroadcastReceiver();
		closeLeftDrawer();
	}

	private void initSlidingFragment() {
		menuFragment = SlidingPaneMenuFragment.newInstance(null);
		homeFragment = SlidingPaneHomeFragment.newInstance(null);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		if (!menuFragment.isAdded()) {
			ft.replace(R.id.ah_slidingpane_menu, menuFragment);
		}
		if (!homeFragment.isAdded()) {
			ft.replace(R.id.ah_slidingpane_content, homeFragment);
		}
		ft.commit();
	}

	private void initSlidingPanelayout() {
		maxMargin = displayMetrics.heightPixels / 10;
		mSlidingPaneLayout = (MySlidingPaneLayout) findViewById(R.id.ah_slidingpane_layout);
		mSlidingPaneLayout.setDisplayMetrics(displayMetrics);
		mSlidingPaneLayout.setShadowDrawable(null);
		mSlidingPaneLayout.setSliderFadeColor(0);
		mSlidingPaneLayout.closePane();
		mSlidingPaneLayout.setPanelSlideListener(new PanelSlideListener() {

			@Override
			public void onPanelSlide(View arg0, float slideOffset) {

				float offScale = 1 - (slideOffset * maxMargin * 2) / displayMetrics.heightPixels;
				ViewHelper.setScaleX(homeFragment.getCurrentView(), offScale);// 设置水平方向的缩放比
				ViewHelper.setScaleY(homeFragment.getCurrentView(), offScale);// 设置垂直方向的缩放比
				ViewHelper.setPivotY(homeFragment.getCurrentView(), displayMetrics.heightPixels / 2);
				ViewHelper.setPivotX(homeFragment.getCurrentView(), 0);// 设置缩放和选择的点

				float scale = 1 - ((1 - slideOffset) * maxMargin * 2) / displayMetrics.heightPixels;

				ViewHelper.setScaleX(menuFragment.getCurrentView(), scale);// 设置水平方向的缩放比
				ViewHelper.setScaleY(menuFragment.getCurrentView(), scale);// 设置垂直方向的缩放比
				ViewHelper.setPivotY(menuFragment.getCurrentView(), displayMetrics.heightPixels / 2);
				ViewHelper.setPivotX(menuFragment.getCurrentView(), 0);// 设置缩放和选择的点
				ViewHelper.setAlpha(menuFragment.getCurrentView(), slideOffset);
			}

			@Override
			public void onPanelOpened(View arg0) {
				if (!isPanelOpen) {
					isPanelOpen = true;
					menuFragment.getProgress(false);
				}
			}

			@Override
			public void onPanelClosed(View arg0) {
				menuFragment.resetProgress();
				isPanelOpen = false;
			}
		});

	}

	private void showGuideActivity() {
		mainIsFirst = SharedPrefsUtil.getValue(this, "mainIsFirst", true);
		if (mainIsFirst == false)
			return;
		Intent intent = new Intent(this, GuideMainActivity.class);
		startActivity(intent);
		SharedPrefsUtil.putValue(this, "mainIsFirst", false);
	}

	private void registBroadcastReceiver() {
		finishActivityBroadcast = new FinishActivityBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_FINISH_ACTIVITY);
		BroadcastManager.registerReceiver(finishActivityBroadcast, filter);
		inited = true;
	}

	private void unregistBroadcastReceiver() {
		BroadcastManager.unregisterReceiver(finishActivityBroadcast);
		inited = false;
	}

	private class FinishActivityBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			completeFinish();
		}
	}

	private void completeFinish() {
		this.finish();
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			showLeftDrawer();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showLeftDrawer() {
		if (mSlidingPaneLayout != null) {
			if (mSlidingPaneLayout.isOpen()) {
				mSlidingPaneLayout.closePane();
			} else {
				mSlidingPaneLayout.openPane();
			}
		}
	}

	private void closeLeftDrawer() {
		if (SharedPrefsUtil.getValue(this, "isGoHome", false)) {
			SharedPrefsUtil.putValue(this, "isGoHome", false);
			homeFragment.getHomeTab();
			mSlidingPaneLayout.closePane();
		}
	}

	private static Boolean isExit = false;
	static Handler exitHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	protected void exit() {
		if (isExit == false) {
			isExit = true;
			ToastUtil.show(this, this.getResources().getString(R.string.exit_tips), Toast.LENGTH_SHORT);
			exitHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			List<DownloadBean> hist = DownloadEntityManager.getInstance().getAllDownloading();
			int count = hist.size();
			if (count != 0) {
				SharedPrefsUtil.putValue(this, "edcl_continue_download_cb", true);
				showExitDialog(count);
			} else {
				this.finish();
				// System.exit(0);
			}
		}
	}

	protected void showExitDialog(int count) {
		MyCheckBoxDialog.Builder builder = new MyCheckBoxDialog.Builder(this); // 自定义dialog
		builder.setTitle(R.string.warm_tips);
		builder.setMessage(ResourceUtil.getString(this, R.string.dialog_app_exit_downloading, "" + count));
		// 左边按钮
		builder.setPositiveButton(ResourceUtil.getString(this, R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		// 右边按钮
		builder.setNegativeButton(ResourceUtil.getString(this, R.string.confirm),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (SharedPrefsUtil.getValue(HomeActivity.this, "edcl_continue_download_cb", true)) {
							HomeActivity.this.finish();
						} else {
							stopDownload();
						}

					}
				});

		try {
			builder.create().show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void stopDownload() {
		Intent intent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		intent.putExtra(MyIntents.TYPE, MyIntents.Types.STOP);
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregistBroadcastReceiver();
		AudioEffectManager.getInstance().releaseMusicPlayer();
		DataEyeManager.getInstance().exit();
	}

	@Override
	public void onShowSettingsTips(boolean isVisiable) {
		if (menuFragment != null) {
			menuFragment.setSettingsTipsCanVisviable(isVisiable);
		}
	}

	@Override
	public void onShowMyAppsTips(boolean isVisiable) {
		if (menuFragment != null) {
			menuFragment.setMyAppsCanVisviable(isVisiable);
		}
	}

	@Override
	public void onShowToolsTips(boolean isVisiable) {
		if (menuFragment != null) {
			menuFragment.setToolsTipsCanVisviable(isVisiable);
		}
	}

	@Override
	public void onShowMyContentsTips(boolean isVisiable) {
		if (menuFragment != null) {
			menuFragment.setMyContentsCanVisviable(isVisiable);
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		if (mSlidingPaneLayout != null) {
			SkinConfigManager.getInstance().setViewBackground(context, mSlidingPaneLayout, SkinConstan.APP_USER_BG);
		}
	}

}
