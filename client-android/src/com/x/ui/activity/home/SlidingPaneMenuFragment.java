/**   
* @Title: SlidingPaneMenuFragment.java
* @Package com.x.ui.activity.home
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-28 上午9:46:47
* @version V1.0   
*/

package com.x.ui.activity.home;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.x.business.account.UserInfoManager;
import com.x.business.audio.AudioEffectManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.model.MasUser;
import com.x.publics.utils.DevicesMountUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.DevicesMountUtil.ClearCache;
import com.x.ui.activity.account.AccountActivity;
import com.x.ui.activity.account.LoginActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.activity.myApps.MyAppsActivity;
import com.x.ui.activity.resource.ResourceManagementActivity;
import com.x.ui.activity.settings.SettingsActivity;
import com.x.ui.activity.tools.ToolsActivity;
import com.x.ui.view.circularseekbar.CircularSeekBar;
import com.x.ui.view.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;

/**
* @ClassName: SlidingPaneMenuFragment
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-28 上午9:46:47
* 
*/

public class SlidingPaneMenuFragment extends BaseFragment implements OnClickListener {

	private static final long DEFAULT_DELAY = 2000;
	private static final int DEFAULT_RAM_VALUE = 18;
	private static final int DEFAULT_STORAGE_VALUE = 12;

	public static final int HANDLER_MY_APPS_ID = 1101;
	public static final int HANDLER_SETTINGS_ID = 1102;
	public static final int HANDLER_MY_CONTENTS_ID = 1103;
	public static final int HANDLER_RAM_PROGRESS_ID = 1104;
	public static final int HANDLER_STORAGE_PROGRESS_ID = 1105;
	public static final int HANDLER_EQUALLY_PROGRESS_ID = 1106;
	public static final int HANDLER_CLEAR_STORAGE_ID = 1107;
	public static final int HANDLER_LOADING_COMPLETE_ID = 1108;
	public static final int HANDLER_STOP_ANIMATION_ID = 1109;
	public static final int HANDLER_TOOLS_ID = 1110;

	private Timer timer;
	private View currentView;
	private AnimationDrawable mAd;
	private CircularSeekBar csb1, csb2;
	private ProgressBar progressLoading;
	private Thread thread1, thread2, thread3;
	private int proRam, proStorage, proEqually, ramValue, storageValue;
	private ImageView myAppsTips, myContentsTips, settingsTips, toolsTips, clearView;
	private boolean flagStorage, flagRam, flagProgress, slidingMenuIsOpen;
	private BroadcastReceiver mDownloadUiReceiver;
	private boolean inited, isMyAppsCanVisviable, isMyContentTipsCanVisviable, isSettingsTipsCanVisviable,
			isToolsTipsCanVisviable;
	private TextView mAccountTv, txtProgress, txtStorage, txtRam;
	private static final int[] btns = { R.id.btn_account_ll, R.id.btn_my_apps_ll, R.id.btn_my_contents_ll,
			R.id.btn_tools_ll, R.id.btn_settings_ll, R.id.btn_clean };

	public static SlidingPaneMenuFragment newInstance(Bundle bundle) {
		SlidingPaneMenuFragment fragment = new SlidingPaneMenuFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	public View getCurrentView() {
		return currentView;
	}

	public void setCurrentViewPararms(FrameLayout.LayoutParams layoutParams) {
		currentView.setLayoutParams(layoutParams);
	}

	public FrameLayout.LayoutParams getCurrentViewParams() {
		return (LayoutParams) currentView.getLayoutParams();
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		setAccountInfo();
		if (!inited)
			registDownloadUiReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistDownloadUiReceiver();
	}

	@SuppressLint("WrongViewCast")
  @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_slidingpane_menu, container, false);

		// initialize view
		txtRam = (TextView) currentView.findViewById(R.id.tv_ram);
		txtStorage = (TextView) currentView.findViewById(R.id.tv_storage);
		mAccountTv = (TextView) currentView.findViewById(R.id.tv_account);
		txtProgress = (TextView) currentView.findViewById(R.id.tv_progress);
		myAppsTips = (ImageView) currentView.findViewById(R.id.ld_my_apps_tips);
		settingsTips = (ImageView) currentView.findViewById(R.id.ld_settings_tips);
		toolsTips = (ImageView) currentView.findViewById(R.id.ld_tools_tips);
		myContentsTips = (ImageView) currentView.findViewById(R.id.ld_my_contents_tips);
		progressLoading = (ProgressBar) currentView.findViewById(R.id.progress_loading);
		clearView = (ImageView) currentView.findViewById(R.id.btn_clean);
		mAd = (AnimationDrawable) clearView.getDrawable();
		mAd.stop();

		csb1 = (CircularSeekBar) currentView.findViewById(R.id.circularSeekBar1);
		csb2 = (CircularSeekBar) currentView.findViewById(R.id.circularSeekBar2);
		csb1.setOnSeekBarChangeListener(new CircleSeekBarListener1());
		csb2.setOnSeekBarChangeListener(new CircleSeekBarListener2());

		// listener buttons events
		for (int btn : btns) {
			currentView.findViewById(btn).setOnClickListener(this);
		}

		return currentView;
	}

	public class CircleSeekBarListener1 implements OnCircularSeekBarChangeListener {

		@Override
		public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
			// TODO Insert your code here
			circularSeekBar.setTopOffest(progress);
		}

		@Override
		public void onStopTrackingTouch(CircularSeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(CircularSeekBar seekBar) {
			// TODO Auto-generated method stub

		}
	}

	public class CircleSeekBarListener2 implements OnCircularSeekBarChangeListener {

		@Override
		public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
			// TODO Insert your code here
			circularSeekBar.setBottomOffest(progress);
		}

		@Override
		public void onStopTrackingTouch(CircularSeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(CircularSeekBar seekBar) {
			// TODO Auto-generated method stub

		}
	}

	public void setAccountInfo() {
		if (UserInfoManager.getInstence(mActivity).isLogin()) {
			MasUser masUser = UserInfoManager.getInstence(mActivity).getAccInfo();
			if (masUser != null) {
				mAccountTv.setText(masUser.getNickName());
			}

		} else {
			mAccountTv.setText(getResources().getString(R.string.account));
		}
	}

	// check apps update
	public void setMyAppsCanVisviable(boolean isMyAppsCanVisviable) {
		this.isMyAppsCanVisviable = isMyAppsCanVisviable;
		handler.sendEmptyMessage(HANDLER_MY_APPS_ID);
	}

	// check content download
	public void setMyContentsCanVisviable(boolean isMyContentTipsCanVisviable) {
		this.isMyContentTipsCanVisviable = isMyContentTipsCanVisviable;
		handler.sendEmptyMessage(HANDLER_MY_CONTENTS_ID);
	}

	// check new version
	public void setSettingsTipsCanVisviable(boolean isSettingsTipsCanVisviable) {
		this.isSettingsTipsCanVisviable = isSettingsTipsCanVisviable;
		handler.sendEmptyMessage(HANDLER_SETTINGS_ID);
	}

	// check skin code
	public void setToolsTipsCanVisviable(boolean isToolsTipsCanVisviable) {
		this.isToolsTipsCanVisviable = isToolsTipsCanVisviable;
		handler.sendEmptyMessage(HANDLER_TOOLS_ID);
	}

	private void getisNewTabCanVisviable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				isMyAppsCanVisviable = isNewTabCanVisviable();
				handler.sendEmptyMessage(HANDLER_MY_APPS_ID);
			}
		}).start();
	}

	private boolean isNewTabCanVisviable() {
		int updateCount = UpdateManage.getInstance(mActivity).getUpdateAppSize();
		int downloadCount = DownloadEntityManager.getInstance().getAllUnCompleteAppsDownloadCount();
		return (updateCount + downloadCount) > 0;
	}

	private void getisMyContentTipsCanVisviable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				isMyContentTipsCanVisviable = isMyContentTipsCanVisviable();
				handler.sendEmptyMessage(HANDLER_MY_CONTENTS_ID);
			}
		}).start();
	}

	private boolean isMyContentTipsCanVisviable() {
		return !DownloadEntityManager.getInstance().getAllUnfinishedMediaDownloadList().isEmpty();
	}

	public void getProgress(final boolean onClick) {
		slidingMenuIsOpen = true;
		storageValue = (DevicesMountUtil.getInstance().getStoragePercentage());
		ramValue = DevicesMountUtil.getInstance().getDevicesRAM(mActivity, 0);
		storageValue = (storageValue == 0) ? DEFAULT_STORAGE_VALUE : storageValue;
		ramValue = (ramValue == 0) ? DEFAULT_RAM_VALUE : ramValue;

		// Thread-1
		if (thread1 == null) {
			thread1 = new Thread() {
				@Override
				public void run() {

					while (0 < proEqually && slidingMenuIsOpen && onClick) {
						handler.sendEmptyMessage(HANDLER_EQUALLY_PROGRESS_ID);
						proEqually--;
						try {
							Thread.sleep(10);
							if (0 == proEqually && onClick) {
								handler.sendEmptyMessage(HANDLER_STOP_ANIMATION_ID);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					while (proEqually < (storageValue + ramValue) / 2 && slidingMenuIsOpen) {
						handler.sendEmptyMessage(HANDLER_EQUALLY_PROGRESS_ID);
						proEqually++;
						try {
							Thread.sleep(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(HANDLER_LOADING_COMPLETE_ID);
					flagProgress = true;
					thread1 = null;
				}
			};
			thread1.start();
		}
	}

	private void runStorageProgress() {
		// thread-2
		if (thread2 == null) {
			thread2 = new Thread() {
				@Override
				public void run() {

					while (proStorage < storageValue && slidingMenuIsOpen) {
						handler.sendEmptyMessage(HANDLER_STORAGE_PROGRESS_ID);
						proStorage++;
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					flagStorage = true;
					thread2 = null;
				}
			};
			thread2.start();
		}
	}

	private void runRamProgress() {
		// Thread-3
		if (thread3 == null) {
			thread3 = new Thread() {
				@Override
				public void run() {

					while (proRam < ramValue && slidingMenuIsOpen) {
						handler.sendEmptyMessage(HANDLER_RAM_PROGRESS_ID);
						proRam++;
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					flagRam = true;
					thread3 = null;
				}
			};
			thread3.start();
		}
	}

	public void resetProgress() {
		showLoadingView();
		slidingMenuIsOpen = false;
		if (timer != null) {
			timer.cancel();
		}

		proRam = 0;
		proStorage = 0;
		proEqually = 0;
		csb1.setProgress(0);
		csb2.setProgress(0);
		txtProgress.setText(proEqually + "");
		progressLoading.setVisibility(View.GONE);

		flagRam = false;
		flagStorage = false;
		flagProgress = false;
		if (mAd != null)
			mAd.stop();
	}

	private void showProgressView() {
		csb1.setVisibility(View.VISIBLE);
		csb2.setVisibility(View.VISIBLE);
		txtRam.setVisibility(View.VISIBLE);
		txtStorage.setVisibility(View.VISIBLE);
		progressLoading.setVisibility(View.GONE);
	}

	private void showLoadingView() {
		csb1.setVisibility(View.GONE);
		csb2.setVisibility(View.GONE);
		txtRam.setVisibility(View.GONE);
		txtStorage.setVisibility(View.GONE);
	}

	public void clear() {
		showLoadingView();
		// start clear time
		final long start = System.currentTimeMillis();
		// clear ram
		DevicesMountUtil.getInstance().killBackgroundProcesses(mActivity);
		// clear storage
		DevicesMountUtil.getInstance().clearAllAppCache(mActivity, new ClearCache() {
			@Override
			public void resultCache(long sum) {
				// start end time
				long end = System.currentTimeMillis();
				long delay = (end - start > DEFAULT_DELAY) ? end - start : DEFAULT_DELAY;

				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (slidingMenuIsOpen)
							handler.sendEmptyMessage(HANDLER_CLEAR_STORAGE_ID);
					}
				}, delay);
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btn_account_ll:
			if (UserInfoManager.getInstence(mActivity).isLogin()) {
				startActivity(new Intent(mActivity, AccountActivity.class));
			} else {
				startActivity(new Intent(mActivity, LoginActivity.class));
			}
			break;

		case R.id.btn_my_apps_ll:
			Intent myAppsIntent = new Intent(mActivity, MyAppsActivity.class);
			myAppsIntent.putExtra("activity_name", getActivity().getClass().getName());
			startActivity(myAppsIntent);
			break;

		case R.id.btn_my_contents_ll:
			Intent resourceintent = new Intent(mActivity, ResourceManagementActivity.class);
			resourceintent.putExtra("MODE", NativeResourceConstant.DEF_MODE);
			startActivity(resourceintent);
			break;

		case R.id.btn_tools_ll:
			startActivity(new Intent(mActivity, ToolsActivity.class));
			break;

		case R.id.btn_settings_ll:
			startActivity(new Intent(mActivity, SettingsActivity.class));
			break;

		case R.id.btn_clean:
			if (flagProgress && flagStorage && flagRam) {
				flagRam = false;
				flagStorage = false;
				flagProgress = false;
				progressLoading.setVisibility(View.VISIBLE);
				mAd.start();
				clear();
			}
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case HANDLER_RAM_PROGRESS_ID:
				csb2.setProgress(proRam);
				break;

			case HANDLER_STORAGE_PROGRESS_ID:
				csb1.setProgress(proStorage);
				break;

			case HANDLER_EQUALLY_PROGRESS_ID:
				txtProgress.setText(proEqually + "");
				break;

			case HANDLER_MY_APPS_ID:
				if (isMyAppsCanVisviable) {
					myAppsTips.setVisibility(View.VISIBLE);
				} else {
					myAppsTips.setVisibility(View.GONE);
				}
				break;

			case HANDLER_MY_CONTENTS_ID:
				if (isMyContentTipsCanVisviable) {
					myContentsTips.setVisibility(View.VISIBLE);
				} else {
					myContentsTips.setVisibility(View.GONE);
				}
				break;

			case HANDLER_SETTINGS_ID:
				if (isSettingsTipsCanVisviable) {
					settingsTips.setVisibility(View.VISIBLE);
				} else {
					settingsTips.setVisibility(View.GONE);
				}
				break;
			case HANDLER_TOOLS_ID:
				if (isToolsTipsCanVisviable) {
					toolsTips.setVisibility(View.VISIBLE);
				} else {
					toolsTips.setVisibility(View.GONE);
				}
				break;
			case HANDLER_CLEAR_STORAGE_ID:
				getProgress(true);
				break;

			case HANDLER_LOADING_COMPLETE_ID:
				proRam = 0;
				proStorage = 0;
				csb1.setProgress(0);
				csb2.setProgress(0);

				showProgressView();
				loadAnimation();
				break;

			case HANDLER_STOP_ANIMATION_ID:
				mAd.stop();
				AudioEffectManager.getInstance().playCleanAudioEffect(mActivity);
				break;
			}
		};
	};

	/**
	 * @Title: registDownloadUiReceiver
	 * @Description: 注册广播
	 * @param
	 * @return void
	 * @throws
	 */
	private void registDownloadUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter downloadUiReceiverfilter = new IntentFilter();
		downloadUiReceiverfilter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, downloadUiReceiverfilter);

		inited = true;
	}

	/**
	 * @Title: unregistDownloadUiReceiver
	 * @Description: 注销广播
	 * @param
	 * @return void
	 * @throws
	 */
	private void unregistDownloadUiReceiver() {
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
		inited = false;
	}

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			handleIntent(intent);
		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.COMPLETE:
				case MyIntents.Types.DELETE:
				case MyIntents.Types.ADD:
					//					getisMyContentTipsCanVisviable();
					//					getisNewTabCanVisviable();
					break;
				}
			}
		}
	}

	private void loadAnimation() {
		Animation animation1 = AnimationUtils.loadAnimation(mActivity, R.anim.anim_alpha);
		Animation animation2 = AnimationUtils.loadAnimation(mActivity, R.anim.circular_anim_scale_left);
		Animation animation3 = AnimationUtils.loadAnimation(mActivity, R.anim.circular_anim_scale_right);
		csb1.startAnimation(animation1);
		csb2.startAnimation(animation1);
		txtRam.startAnimation(animation3);
		txtStorage.startAnimation(animation2);
		animation2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runRamProgress();
				runStorageProgress();
			}
		});
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		if (SkinConstan.skinEnabled) {
			SkinConfigManager.getInstance().setCircleProgressColor(mActivity, csb1, csb2);
			for (int i = 0; i < btns.length - 1; i++) {
				View view = currentView.findViewById(btns[i]);
				SkinConfigManager.getInstance().setViewBackground(mActivity, view, SkinConstan.ITEM_THEME_BG);
			}
		}
	}
}
