/**   
* @Title: MainActivity.java
* @Package com.x.ui.activity.home
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-4-29 下午2:59:00
* @version V1.0   
*/

package com.x.ui.activity.home;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.account.util.AccountService;
import com.x.R;
import com.x.business.account.UserInfoManager;
import com.x.business.audio.AudioEffectManager;
import com.x.business.country.CountryManager;
import com.x.business.favorite.FavoriteManage;
import com.x.business.feedback.FeedbackManager;
import com.x.business.search.SearchConstan;
import com.x.business.settings.SettingModel;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.upgrade.UpgradeManager;
import com.x.publics.http.Host;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.http.model.FeedbackWarnResponse;
import com.x.publics.http.model.MasUser;
import com.x.publics.http.model.SkinAttentionResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FavoriteAppBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MyAppsBean;
import com.x.publics.model.MyAppsBean.ListType;
import com.x.publics.utils.CloneClass;
import com.x.publics.utils.DevicesMountUtil;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.DevicesMountUtil.ClearCache;
import com.x.ui.activity.account.AccountActivity;
import com.x.ui.activity.account.LoginActivity;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.guide.GuideMainActivity;
import com.x.ui.activity.myApps.MyAppsActivity;
import com.x.ui.activity.resource.ResourceManagementActivity;
import com.x.ui.activity.ringtones.RingtonesFragment;
import com.x.ui.activity.search.SearchActivity;
import com.x.ui.activity.settings.SettingsActivity;
import com.x.ui.activity.tools.ToolsActivity;
import com.x.ui.activity.wallpaper.WallpaperFragment;
import com.x.ui.view.MyCheckBoxDialog;
import com.x.ui.view.circularseekbar.CircularSeekBar;
import com.x.ui.view.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;
import com.x.ui.view.drag.SlideDragLayout;
import com.x.ui.view.drag.SlideDragLayout.DragListener;

/**
* @ClassName: MainActivity
* @Description: 首页

* @date 2015-4-29 下午2:59:00
* 
*/

public class MainActivity extends BaseActivity implements OnClickListener {

	@InjectView(R.id.am_slde_drag_layout)
	SlideDragLayout slideDragLayout;
	/*home*/
	@InjectView(R.id.mf_tab_rg)
	RadioGroup mRgHost;
	@InjectView(R.id.mf_home_tab_rb)
	RadioButton homeTabBtn;
	@InjectView(R.id.mf_apps_tab_rb)
	RadioButton appTabBtn;
	@InjectView(R.id.mf_games_tab_rb)
	RadioButton gameTabBtn;
	@InjectView(R.id.mf_ringtones_tab_rb)
	RadioButton RingtoneTabBtn;
	@InjectView(R.id.mf_wallpaper_tab_rb)
	RadioButton wallpaperTabBtn;
	@InjectView(R.id.rl_title_bar)
	View mTitleView;
	@InjectView(R.id.title_pendant)
	View mTitlePendant;
	@InjectView(R.id.mh_notification_iv)
	TextView mTipsView;
	@InjectView(R.id.mh_navigate_ll)
	View mSlideView;
	@InjectView(R.id.mh_search_ll)
	View mMenuView;
	/*menu*/
	@InjectView(R.id.circularSeekBar1)
	CircularSeekBar csb1;
	@InjectView(R.id.circularSeekBar2)
	CircularSeekBar csb2;
	@InjectView(R.id.progress_loading)
	ProgressBar progressLoading;
	@InjectView(R.id.ld_my_apps_tips)
	TextView myAppsTips;
	@InjectView(R.id.ld_my_contents_tips)
	ImageView myContentsTips;
	@InjectView(R.id.ld_settings_tips)
	ImageView settingsTips;
	@InjectView(R.id.ld_tools_tips)
	ImageView toolsTips;
	@InjectView(R.id.btn_clean)
	ImageView clearView;
	@InjectView(R.id.tv_account)
	TextView mAccountTv;
	@InjectView(R.id.tv_progress)
	TextView txtProgress;
	@InjectView(R.id.tv_storage)
	TextView txtStorage;
	@InjectView(R.id.tv_ram)
	TextView txtRam;

	private static final long DEFAULT_DELAY = 2000;
	private static final int DEFAULT_RAM_VALUE = 18;
	private static final int DEFAULT_STORAGE_VALUE = 12;
	public static final int HANDLER_MENU_ID = 1100;
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

	private MainActivity mActivity;
	private final DisplayMetrics displayMetrics = new DisplayMetrics();
	private Fragment mCurrentPage, homeFragement, appsFragment, gamesFragment, ringtonesFragment, wallpaperFragment;
	private int tabNum;
	private SettingModel settingModel;
	private BroadcastReceiver mDownloadUiReceiver, mWifiChangeUpdateReceiver;
	private FinishActivityBroadcast finishActivityBroadcast;
	private int lastCountryId;
	private int currentCountryId;
	private Timer timer;
	private AnimationDrawable mAd;
	private Thread thread1, thread2, thread3;
	private int proRam, proStorage, proEqually, ramValue, storageValue;
	private boolean flagStorage, flagRam, flagProgress, slidingMenuIsOpen;
	private boolean inited;

	private static final int[] btns = { R.id.btn_account_ll, R.id.btn_my_apps_ll, R.id.btn_my_contents_ll,
			R.id.btn_tools_ll, R.id.btn_settings_ll, R.id.btn_clean };

	private boolean isMyAppsNew;
	private boolean isMyContentsNew;
	private boolean isSettingsNew;
	private boolean isToolsNew;

	private List<InstallAppBean> updateList = new ArrayList<InstallAppBean>();
	private List<DownloadBean> downloadingList = new ArrayList<DownloadBean>();
	private List<DownloadBean> unInstallList = new ArrayList<DownloadBean>();

	// 新手引导
	private boolean mainIsFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		SharedPrefsUtil.clearThemeValue(this); // 清除一键下载记录
		showGuideActivity();
		initUi();
		getData();
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		setAccountInfo();

		if (mCurrentPage != null && inited) {
			dataEyeSwitchFragments(mCurrentPage, true);
			dataEyeSwitchTabs(mCurrentPage, true);
		}
		if (inited) {
			showSettingsTips(mActivity);
			showToolsTips(mActivity);
			onResumegetLocalUpgradeApps();
			getisMyContentTipsCanVisviable();
		}

		if (!inited)
			registBroadcastReceiver();

		closeLeftDrawer();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mCurrentPage != null) {
			dataEyeSwitchTabs(mCurrentPage, false);
			dataEyeSwitchFragments(mCurrentPage, false);
		}
	}

	private void getHomeTab() {
		if (mRgHost != null) {
			mRgHost.getChildAt(0).performClick();
		}
	}

	private void initUi() {
	  if (slideDragLayout==null){
	    Log.d(WINDOW_SERVICE, "slideDragLayout is null");
	  }
		slideDragLayout.setDisplayMetrics(displayMetrics);
		slideDragLayout.setDragListener(mDragListener);
		initMenuView();
		initHomeView();
	}

	private void getData() {
		settingModel = Utils.getSettingModel(mActivity);
		lastCountryId = CountryManager.getInstance().getCountryId(mActivity); // 初始化countryId
		feedbackWarn();
		getSkinCode();
		getLocalUpgradeApps();
		getisMyContentTipsCanVisviable();
		UpgradeManager.getInstence(mActivity).checkVersion(UpgradeManager.START_ON_NOTIFICATION, newVersionHanlder,
				false);
	}

	private void initHomeView() {
		mMenuView.setVisibility(View.VISIBLE);
		mMenuView.setOnClickListener(this);
		mSlideView.setOnClickListener(this);
		mRgHost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {

				// Homes
				case R.id.mf_home_tab_rb:
					if (homeFragement == null) {
						homeFragement = HomeFragment.newInstance(null);
					}
					switchFragments(homeFragement);
					break;
//				// Apps
//				case R.id.mf_apps_tab_rb:
//					if (appsFragment == null) {
//						appsFragment = AppsFragment.newInstance(null);
//					}
//					switchFragments(appsFragment);
//					break;
				// Games
				case R.id.mf_games_tab_rb:
					if (gamesFragment == null) {
						gamesFragment = GamesFragment.newInstance(null);
					}
					switchFragments(gamesFragment);
					break;
				// Ringtones
				case R.id.mf_ringtones_tab_rb:
					if (ringtonesFragment == null) {
						ringtonesFragment = RingtonesFragment.newInstance(null);
					}
					switchFragments(ringtonesFragment);
					break;

				// Wallpapers
				case R.id.mf_wallpaper_tab_rb:
					if (wallpaperFragment == null) {
						wallpaperFragment = WallpaperFragment.newInstance(null);
					}
					switchFragments(wallpaperFragment);
					break;
				// Me
				case R.id.mf_apps_tab_rb:
//					if (slideDragLayout.isOpened()) {
//						slideDragLayout.close();
//					} else {
//						slideDragLayout.open();
//					}

//					if (UserInfoManager.getInstence(mActivity).isLogin()) {
//						startActivity(new Intent(mActivity, AccountActivity.class));
//					} else {
//						startActivity(new Intent(mActivity, LoginActivity.class));
//					}

					AccountService.getInstances().showWebDialog(mActivity, Host.accountUrl);
					group.check(R.id.mf_home_tab_rb);
					break;
				}
			}
		});
		mRgHost.getChildAt(tabNum).performClick();
	}

	private void initMenuView() {
		mAd = (AnimationDrawable) clearView.getDrawable();
		mAd.stop();

		csb1 = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
		csb2 = (CircularSeekBar) findViewById(R.id.circularSeekBar2);
		csb1.setOnSeekBarChangeListener(new CircleSeekBarListener1());
		csb2.setOnSeekBarChangeListener(new CircleSeekBarListener2());

		// listener buttons events
		for (int btn : btns) {
			findViewById(btn).setOnClickListener(this);
		}
	}

	public class CircleSeekBarListener1 implements OnCircularSeekBarChangeListener {

		@Override
		public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
			circularSeekBar.setTopOffest(progress);
		}

		@Override
		public void onStopTrackingTouch(CircularSeekBar seekBar) {

		}

		@Override
		public void onStartTrackingTouch(CircularSeekBar seekBar) {

		}
	}

	public class CircleSeekBarListener2 implements OnCircularSeekBarChangeListener {

		@Override
		public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
			circularSeekBar.setBottomOffest(progress);
		}

		@Override
		public void onStopTrackingTouch(CircularSeekBar seekBar) {

		}

		@Override
		public void onStartTrackingTouch(CircularSeekBar seekBar) {

		}
	}

	private DragListener mDragListener = new DragListener() {

		@Override
		public void onOpen() {
			getProgress(false);
		}

		@Override
		public void onDrag(float percent) {

		}

		@Override
		public void onClose() {
			resetProgress();
		}
	};

	public void switchFragments(Fragment fragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		if (mCurrentPage == null && !isAlreadyAddFragment(fragment)) {
			ft.add(R.id.fsh_content_fl, fragment, fragment.getClass().toString()).commitAllowingStateLoss();
			dataEyeSwitchFragments(fragment, true);
			dataEyeSwitchTabs(fragment, true);
		} else {
			if (isAlreadyAddFragment(fragment)) {
				ft.hide(mCurrentPage).show(fragment).commitAllowingStateLoss();
			} else {
				ft.hide(mCurrentPage).add(R.id.fsh_content_fl, fragment, fragment.getClass().toString())
						.commitAllowingStateLoss();
			}
			dataEyeSwitchTabs(mCurrentPage, false);
			dataEyeSwitchFragments(mCurrentPage, false);
			dataEyeSwitchFragments(fragment, true);
			dataEyeSwitchTabs(fragment, true);
		}
		mCurrentPage = fragment;
	}

	private boolean isAlreadyAddFragment(Fragment fragment) {
		return fragment.isAdded()
		/*|| getChildFragmentManager().findFragmentByTag(fragment.getClass().toString()) != null*/;
	}

	private void dataEyeSwitchTabs(Fragment fragment, boolean show) {
		if (fragment instanceof HomeFragment) {
			if (((HomeFragment) fragment).mViewPager != null) {
				int item = ((HomeFragment) homeFragement).mViewPager.getCurrentItem();
				switch (item) {
				case 0:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.HOME_RECOMMEND, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.HOME_RECOMMEND, 0, null, 0L, null,
								null, false);
					}
					break;
				case 1:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.HOME_CONLLECTION, show);
					break;
				case 2:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.HOME_MUST_HAVE, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.HOME_MUST_HAVE, 0, null, 0L, null,
								null, false);
					}
					break;

				}
			}
		} else if (fragment instanceof AppsFragment) {

			if (((AppsFragment) fragment).mViewPager != null) {
				int item = ((AppsFragment) appsFragment).mViewPager.getCurrentItem();
				switch (item) {
				case 0:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.APPS_NEW, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.APPS_NEW, 0, null, 0L, null, null,
								false);
					}
					break;
				case 1:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.APPS_HOT, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.APPS_HOT, 0, null, 0L, null, null,
								false);
					}
					break;

				case 2:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.APPS_CATEGORIES, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.APPS_CATEGORIES, 0, null, 0L,
								null, null, false);
					}
					break;

				}
			}
		} else if (fragment instanceof GamesFragment) {
			if (((GamesFragment) fragment).mViewPager != null) {
				int item = ((GamesFragment) gamesFragment).mViewPager.getCurrentItem();
				switch (item) {
				case 0:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.GAMES_NEW, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.GAMES_NEW, 0, null, 0L, null,
								null, false);
					}
					break;
				case 1:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.GAMES_HOT, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.GAMES_HOT, 0, null, 0L, null,
								null, false);
					}
					break;

				case 2:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.GAMES_CATEGORIES, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.GAMES_CATEGORIES, 0, null, 0L,
								null, null, false);
					}
					break;
				}
			}
		} else if (fragment instanceof RingtonesFragment) {
			if (((RingtonesFragment) fragment).mViewPager != null) {
				int item = ((RingtonesFragment) ringtonesFragment).mViewPager.getCurrentItem();
				switch (item) {
				case 0:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.RINGTONE_NEW, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.RINGTONE_NEW, 0, null, 0L, null,
								null, false);
					}
					break;
				case 1:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.RINGTONE_HOT, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.RINGTONE_HOT, 0, null, 0L, null,
								null, false);
					}
					break;

				case 2:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.RINGTONE_CATEGORIES, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.RINGTONE_CATEGORIES, 0, null, 0L,
								null, null, false);
					}
					break;
				}
			}
		} else if (fragment instanceof WallpaperFragment) {
			if (((WallpaperFragment) fragment).mViewPager != null) {
				int item = ((WallpaperFragment) wallpaperFragment).mViewPager.getCurrentItem();
				switch (item) {

				case 0:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.WALLPAPER_ALBUM, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_ALBUM, 0, null, 0L,
								null, null, false);
					}
					break;

				case 1:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.WALLPAPER_LIVE_WALLPAPER, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_LIVE_WALLPAPER, 0, null,
								0L, null, null, false);
					}
					break;

				case 2:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.WALLPAPER_NEW, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_NEW, 0, null, 0L, null,
								null, false);
					}
					break;
				case 3:
					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.WALLPAPER_CATEGORIES, show);
					if (show) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_CATEGORIES, 0, null, 0L,
								null, null, false);
					}
					break;
				}
			}
		}
	}

	public void dataEyeSwitchFragments(Fragment fragment, boolean show) {
		if (fragment instanceof HomeFragment) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MAIN_HOME, show);
		} else if (fragment instanceof AppsFragment) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MAIN_APPS, show);
		} else if (fragment instanceof GamesFragment) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MAIN_GAMES, show);
		} else if (fragment instanceof RingtonesFragment) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MAIN_RINGTONE, show);
		} else if (fragment instanceof WallpaperFragment) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MAIN_WALLPAPER, show);
		}
	}

	/**
	 *  获取应用更新
	 */
	private void getLocalUpgradeApps() {
		UpdateManage.getInstance(mActivity).getAppsUpdate(myUpgradeResponseListent, myUpgradeErrorListener);
	}

	private Listener<JSONObject> myUpgradeResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("getAppsUpdate response==>" + response.toString());
			AppsUpgradeResponse upgradeResponse = (AppsUpgradeResponse) JsonUtil.jsonToBean(response,
					AppsUpgradeResponse.class);
			if (upgradeResponse != null && upgradeResponse.state.code == 200 && upgradeResponse.applist != null) {
				UpdateManage.getInstance(mActivity).setLastUpdateRequestSuccessTime(mActivity);
				UpdateManage.getInstance(mActivity).setLastUpdateRequestSuccess();
				UpdateManage.getInstance(mActivity).deleteAllUpdateApp();
				for (InstallAppBean installAppBean : upgradeResponse.applist) {
					UpdateManage.getInstance(mActivity).addUpdateApp(installAppBean);
					FavoriteManage.getInstance(mActivity).updateFavoriteAppInfo(installAppBean);
				}

				// 得到服务器应用更新数据
				Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.MAKE_UPGRADE);
				BroadcastManager.sendBroadcast(nofityIntent);

				// 获取升级数据，判断是否自动下载更新
				if (settingModel.isAutoDownloadUpdateInWifi()
						&& NetworkUtils.getNetworkInfo(mActivity).equals(NetworkUtils.NETWORK_TYPE_WIFI))
					UpdateManage.getInstance(mActivity).autoDownloadUpdateAppControl(true);

			} else {
				UpdateManage.getInstance(mActivity).setLastUpdateRequestFail();
			}
			getisMyappsCanVisviable();
		}
	};

	private ErrorListener myUpgradeErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			//			getisMyappsCanVisviable();
			UpdateManage.getInstance(mActivity).setLastUpdateRequestFail();
		}
	};

	private void getSkinCode() {
		SkinConfigManager.getInstance().getSkinAttention(mActivity, new SkinRequestHandler(this));
	}

	private static class SkinRequestHandler extends Handler {
		private WeakReference<MainActivity> mainActivity;

		SkinRequestHandler(MainActivity activity) {
			mainActivity = new WeakReference<MainActivity>(activity);
		}

		public void handleMessage(Message msg) {
			MainActivity mActivity = mainActivity.get();
			switch (msg.what) {
			case SkinConfigManager.onSuccess:
				if (mActivity == null)
					return;
				SkinAttentionResponse response = (SkinAttentionResponse) msg.obj;
				if (response != null) {
					int oldSkinCode = SkinConfigManager.getInstance().getSkinCode(mActivity);
					int newSkinCode = response.getSkinCode();
					if (oldSkinCode != newSkinCode) {
						SkinConfigManager.getInstance().setSkinCode(mActivity, newSkinCode);
						SkinConfigManager.getInstance().setSkinIsRead(mActivity, false);
					}
					mActivity.showToolsTips(mActivity);
				}
				break;
			default:
				break;
			}
		};
	};

	private void feedbackWarn() {
		FeedbackManager.getInstance().feedbackWarn(mActivity, warnListent, warnErrorListener);
	}

	/**
	 * < 回馈提醒 >数据响应
	 */
	private Listener<JSONObject> warnListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			// 响应数据，进行操作
			final FeedbackWarnResponse feedbackResponse = (FeedbackWarnResponse) JsonUtil.jsonToBean(response,
					FeedbackWarnResponse.class);
			if (feedbackResponse != null && feedbackResponse.state.code == 200) {
				int newFeedbackCode = feedbackResponse.getFeedbackCode();
				boolean attention = feedbackResponse.isFeedbackAttention();
				int oldFeedbackCode = FeedbackManager.getInstance().getFeedbackCode(mActivity);
				if (newFeedbackCode != oldFeedbackCode) {
					FeedbackManager.getInstance().setFeedbackRead(mActivity, false);
					FeedbackManager.getInstance().setFeedbackCode(mActivity, newFeedbackCode);
				}
				FeedbackManager.getInstance().setFeedbackAttention(mActivity, attention);
				showSettingsTips(mActivity);
				showFeedbackNotification(mActivity);
			}
		}
	};

	private ErrorListener warnErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler leftMenuHandler = new Handler() {
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

			case HANDLER_MY_APPS_ID:
				if (isMyAppsNew) {
					int count = updateList.size() + downloadingList.size() + unInstallList.size();
					if (count == 0) {
						return;
					}
					if (count <= 99) {
						myAppsTips.setText("" + count);
					} else {
						myAppsTips.setText("99+");
					}
					myAppsTips.setVisibility(View.VISIBLE);
				} else {
					myAppsTips.setVisibility(View.GONE);
				}
				break;

			case HANDLER_MY_CONTENTS_ID:
				if (isMyContentsNew) {
					myContentsTips.setVisibility(View.VISIBLE);
				} else {
					myContentsTips.setVisibility(View.GONE);
				}
				break;

			case HANDLER_SETTINGS_ID:
				if (isSettingsNew) {
					settingsTips.setVisibility(View.VISIBLE);
				} else {
					settingsTips.setVisibility(View.GONE);
				}
				break;
			case HANDLER_TOOLS_ID:
				if (isToolsNew) {
					toolsTips.setVisibility(View.VISIBLE);
				} else {
					toolsTips.setVisibility(View.GONE);
				}
				break;

			}
		};
	};

	/**
	 * 新版本提示，New小红图标
	 */
	private Handler newVersionHanlder = new Handler() {
		public void handleMessage(Message msg) {
			// 新版本提示
			if (mTipsView != null) {
				isSettingsNew = true;
				tipsHandler.sendEmptyMessage(HANDLER_MENU_ID);
				leftMenuHandler.sendEmptyMessage(HANDLER_SETTINGS_ID);
			}
		};
	};

	private Handler tipsHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_MENU_ID:
				if (isMyAppsNew || isSettingsNew || isToolsNew || isMyContentsNew) {

					int count = updateList.size() + downloadingList.size() + unInstallList.size();
					if (count == 0) {
						return;
					}
					mTipsView.setVisibility(View.VISIBLE);
					if (count <= 99) {
						mTipsView.setText("" + count);
					} else {
						mTipsView.setText("99+");
					}
				} else {
					mTipsView.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}

		};

	};

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
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				runRamProgress();
				runStorageProgress();
			}
		});
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
						leftMenuHandler.sendEmptyMessage(HANDLER_EQUALLY_PROGRESS_ID);
						proEqually--;
						try {
							Thread.sleep(10);
							if (0 == proEqually && onClick) {
								leftMenuHandler.sendEmptyMessage(HANDLER_STOP_ANIMATION_ID);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					while (proEqually < (storageValue + ramValue) / 2 && slidingMenuIsOpen) {
						leftMenuHandler.sendEmptyMessage(HANDLER_EQUALLY_PROGRESS_ID);
						proEqually++;
						try {
							Thread.sleep(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					leftMenuHandler.sendEmptyMessage(HANDLER_LOADING_COMPLETE_ID);
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
						leftMenuHandler.sendEmptyMessage(HANDLER_STORAGE_PROGRESS_ID);
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
						leftMenuHandler.sendEmptyMessage(HANDLER_RAM_PROGRESS_ID);
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
							leftMenuHandler.sendEmptyMessage(HANDLER_CLEAR_STORAGE_ID);
					}
				}, delay);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			if (slideDragLayout.isOpened()) {
				slideDragLayout.close();
			} else {
				slideDragLayout.open();
			}
			break;

		case R.id.mh_search_ll:
			openSearchView();
			break;
		case R.id.btn_account_ll:
			if (UserInfoManager.getInstence(mActivity).isLogin()) {
				startActivity(new Intent(mActivity, AccountActivity.class));
			} else {
				startActivity(new Intent(mActivity, LoginActivity.class));
			}
			break;

		case R.id.btn_my_apps_ll:
			Intent myAppsIntent = new Intent(mActivity, MyAppsActivity.class);
			myAppsIntent.putExtra("activity_name", getClass().getName());
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

	private void openSearchView() {
		int tabIndex = 0;
		if (mCurrentPage != null && mCurrentPage.equals(ringtonesFragment)) {
			tabIndex = 1;
		} else if (mCurrentPage != null && mCurrentPage.equals(wallpaperFragment)) {
			tabIndex = 2;
		} else {
			tabIndex = 0;
		}
		Intent intent = new Intent(mActivity, SearchActivity.class);
		intent.putExtra(SearchConstan.TAB_INDEX, tabIndex);
		startActivity(intent);
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

	private void setSkinTheme() {
		if (SkinConstan.skinEnabled) {
			if (slideDragLayout != null) {
				SkinConfigManager.getInstance().setViewBackground(mActivity, slideDragLayout, SkinConstan.APP_USER_BG);
			}

			SkinConfigManager.getInstance().setCircleProgressColor(mActivity, csb1, csb2);
			for (int i = 0; i < btns.length - 1; i++) {
				View view = findViewById(btns[i]);
				SkinConfigManager.getInstance().setViewBackground(mActivity, view, SkinConstan.ITEM_THEME_BG);
			}

			SkinConfigManager.getInstance().setTitleSkin(mActivity, mTitleView, mSlideView, mTitlePendant, mMenuView);

			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, homeTabBtn, SkinConstan.RB_HOME_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, appTabBtn, SkinConstan.RB_APP_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, gameTabBtn, SkinConstan.RB_GAME_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, RingtoneTabBtn, SkinConstan.RB_RT_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, wallpaperTabBtn, SkinConstan.RB_WP_BG);
		}
	}

	private void showGuideActivity() {
		mainIsFirst = SharedPrefsUtil.getValue(this, "mainIsFirst", true);
		if (mainIsFirst == false)
			return;
		Intent intent = new Intent(this, GuideMainActivity.class);
		startActivity(intent);
		SharedPrefsUtil.putValue(this, "mainIsFirst", false);
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
		if (slideDragLayout != null) {
			if (slideDragLayout.isOpened()) {
				slideDragLayout.close();
			} else {
				slideDragLayout.open();
			}
		}
	}

	private void closeLeftDrawer() {
		if (SharedPrefsUtil.getValue(this, "isGoHome", false)) {
			SharedPrefsUtil.putValue(this, "isGoHome", false);
			getHomeTab();
			slideDragLayout.close();
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
		if (slideDragLayout.isOpened()) {
			slideDragLayout.close();
		} else {
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
						if (SharedPrefsUtil.getValue(mActivity, "edcl_continue_download_cb", true)) {
							mActivity.finish();
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

	/*程序退出广播*/
	private class FinishActivityBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			completeFinish();
		}
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
					getisMyappsCanVisviable();
					getisMyContentTipsCanVisviable();
					break;
				default:
					break;
				}

			}
		}
	}

	public class WifiChangeUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_CHANGE_WIFI_ACTION)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.CHANGE_HOMEPAGE_UPDATE_NUM:
					getisMyappsCanVisviable();
					break;
				default:
					break;
				}

			}
		}
	}

	private void registBroadcastReceiver() {
		finishActivityBroadcast = new FinishActivityBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_FINISH_ACTIVITY);
		BroadcastManager.registerReceiver(finishActivityBroadcast, filter);

		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter downloadUiReceiverfilter = new IntentFilter();
		downloadUiReceiverfilter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, downloadUiReceiverfilter);

		mWifiChangeUpdateReceiver = new WifiChangeUpdateReceiver();
		IntentFilter wifiChangeUpdateReceiverfilter = new IntentFilter();
		wifiChangeUpdateReceiverfilter.addAction(MyIntents.INTENT_UPDATE_CHANGE_WIFI_ACTION);
		BroadcastManager.registerReceiver(mWifiChangeUpdateReceiver, wifiChangeUpdateReceiverfilter);

		inited = true;
	}

	private void unregistBroadcastReceiver() {
		BroadcastManager.unregisterReceiver(finishActivityBroadcast);
		inited = false;
	}

	private void getisMyContentTipsCanVisviable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				isMyContentsNew = isMyContentTipsCanVisviable();
				tipsHandler.sendEmptyMessage(HANDLER_MENU_ID);
				leftMenuHandler.sendEmptyMessage(HANDLER_MY_CONTENTS_ID);
			}
		}).start();
	}

	private boolean isMyContentTipsCanVisviable() {
		return !DownloadEntityManager.getInstance().getAllUnfinishedMediaDownloadList().isEmpty();
	}

	private void onResumegetLocalUpgradeApps() {
		// 获取当前国家ID
		currentCountryId = CountryManager.getInstance().getCountryId(mActivity);
		// 匹配当前国家ID 和 最后一次国家是否相等
		if (currentCountryId == lastCountryId) {
			boolean iscanSendUpdateRequest = UpdateManage.getInstance(mActivity).canSendUpdateRequest(mActivity);
			if (iscanSendUpdateRequest) {
				getLocalUpgradeApps();
			} else {
				getisMyappsCanVisviable();
			}
		} else {
			lastCountryId = currentCountryId;
			getLocalUpgradeApps();
		}
	}

	private void getisMyappsCanVisviable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				isMyAppsNew = isMyappsCanVisviable();
				tipsHandler.sendEmptyMessage(HANDLER_MENU_ID);
				leftMenuHandler.sendEmptyMessage(HANDLER_MY_APPS_ID);
			}
		}).start();
	}

	private boolean isMyappsCanVisviable() {
		downloadingList = DownloadEntityManager.getInstance().getAllUnFinishedAppsDownloadList();
		updateList = UpdateManage.getInstance(mActivity).findAllUpdateInstallApp();
		unInstallList = DownloadEntityManager.getInstance().getAllUnInstallAppsDownloadList();
		if (downloadingList != null) {
			for (DownloadBean downloadBean : downloadingList) {
				try {
					InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBean(
							downloadBean.getPackageName(), downloadBean.getVersionCode());

					for (InstallAppBean updateBean2 : updateList) {
						if (updateBean2.getPackageName().equals(updateBean.getPackageName())) {
							updateList.remove(updateBean2);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (unInstallList != null) {
			for (DownloadBean downloadBean : unInstallList) {
				try {
					InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBean(
							downloadBean.getPackageName(), downloadBean.getVersionCode());

					for (InstallAppBean updateBean2 : updateList) {
						if (updateBean2.getPackageName().equals(updateBean.getPackageName())
								&& updateBean2.getVersionCode() == updateBean.getVersionCode()) {
							updateList.remove(updateBean2);
							break;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		int updateCount = UpdateManage.getInstance(mActivity).getUpdateAppSize();
		int downloadCount = DownloadEntityManager.getInstance().getAllUnCompleteAppsDownloadCount();
		return (updateCount + downloadCount) > 0;
	}

	private void showFeedbackNotification(Context context) {
		boolean attention = FeedbackManager.getInstance().getFeedbackAttention(context);
		if (attention) {
			Utils.showNotification(context);
		}
	}

	public void showToolsTips(Context context) {
		isToolsNew = !SkinConfigManager.getInstance().getSkinIsRead(context);
		tipsHandler.sendEmptyMessage(HANDLER_MENU_ID);
		leftMenuHandler.sendEmptyMessage(HANDLER_TOOLS_ID);
	}

	private void showSettingsTips(Context context) {
		boolean isRead = FeedbackManager.getInstance().isFeedbackRead(context);
		boolean attention = FeedbackManager.getInstance().getFeedbackAttention(context);
		boolean isZappUpdate = UpgradeManager.getInstence(context).isUpgrade();
		if (!isRead || attention || isZappUpdate) {
			isSettingsNew = true;
		} else {
			isSettingsNew = false;
		}
		tipsHandler.sendEmptyMessage(HANDLER_MENU_ID);
		leftMenuHandler.sendEmptyMessage(HANDLER_SETTINGS_ID);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistBroadcastReceiver();
		AudioEffectManager.getInstance().releaseMusicPlayer();
		DataEyeManager.getInstance().exit();
	}
}
