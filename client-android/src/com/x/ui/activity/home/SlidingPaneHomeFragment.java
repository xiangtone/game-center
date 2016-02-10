/**   
* @Title: SlidingPaneHomeFragment.java
* @Package com.x.ui.activity.home
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-28 上午9:55:08
* @version V1.0   
*/

package com.x.ui.activity.home;

import java.lang.ref.WeakReference;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.x.R;
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
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.upgrade.UpgradeManager;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.http.model.FeedbackWarnResponse;
import com.x.publics.http.model.SkinAttentionResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.activity.ringtones.RingtonesFragment;
import com.x.ui.activity.search.SearchActivity;
import com.x.ui.activity.wallpaper.WallpaperFragment;

/**
* @ClassName: SlidingPaneHomeFragment
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-28 上午9:55:08
* 
*/

public class SlidingPaneHomeFragment extends BaseFragment implements View.OnClickListener {

	private RadioGroup mRgHost;
	private View currentView, mTitleView, mTitlePendant;
	private RadioButton homeTabBtn, appTabBtn, gameTabBtn, RingtoneTabBtn, wallpaperTabBtn;
	private Fragment mCurrentPage, homeFragement, appsFragment, gamesFragment, ringtonesFragment, wallpaperFragment;
	private int tabNum;
	public ImageView mTipsView;
	private View mSlideView, mMenuView;
	private SettingModel settingModel;

	private BroadcastReceiver mDownloadUiReceiver, mWifiChangeUpdateReceiver;
	private int lastCountryId;
	private int currentCountryId;

	private boolean inited;
	private boolean isMyAppsNew;
	private boolean isMyContentsNew;
	private boolean isSettingsNew;
	private boolean isToolsNew;

	private OnShowRedTipsListener mCallback;

	public static SlidingPaneHomeFragment newInstance(Bundle bundle) {
		SlidingPaneHomeFragment fragment = new SlidingPaneHomeFragment();
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnShowRedTipsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnShowRedTipsListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_slidingpane_home, container, false);
		initView(currentView);
		initData();
		return currentView;
	}

	private void initData() {
		settingModel = Utils.getSettingModel(mActivity);
		lastCountryId = CountryManager.getInstance().getCountryId(mActivity); // 初始化countryId
		feedbackWarn();
		getSkinCode();
		getLocalUpgradeApps();
		getisMyContentTipsCanVisviable();
		UpgradeManager.getInstence(mActivity).checkVersion(UpgradeManager.START_ON_NOTIFICATION, tipsHanlder, false);
	}

	@Override
	public void onResume() {
		super.onResume();

		setSkinTheme();

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
			registDownloadUiReceiver();

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

	private void registDownloadUiReceiver() {
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

	@SuppressLint("WrongViewCast")
  private void initView(View currentView) {
		mTitleView = currentView.findViewById(R.id.rl_title_bar);
		mTitlePendant = currentView.findViewById(R.id.title_pendant);
		mTipsView = (ImageView) currentView.findViewById(R.id.mh_notification_iv);
		mSlideView = currentView.findViewById(R.id.mh_navigate_ll);
		mMenuView = currentView.findViewById(R.id.mh_search_ll);
		mMenuView.setVisibility(View.VISIBLE);
		mMenuView.setOnClickListener(this);
		mSlideView.setOnClickListener(this);

		appTabBtn = (RadioButton) currentView.findViewById(R.id.mf_apps_tab_rb);
		homeTabBtn = (RadioButton) currentView.findViewById(R.id.mf_home_tab_rb);
		gameTabBtn = (RadioButton) currentView.findViewById(R.id.mf_games_tab_rb);
		RingtoneTabBtn = (RadioButton) currentView.findViewById(R.id.mf_ringtones_tab_rb);
		wallpaperTabBtn = (RadioButton) currentView.findViewById(R.id.mf_wallpaper_tab_rb);

		mRgHost = (RadioGroup) currentView.findViewById(R.id.mf_tab_rg);
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
				// Apps
				case R.id.mf_apps_tab_rb:
					if (appsFragment == null) {
						appsFragment = AppsFragment.newInstance(null);
					}
					switchFragments(appsFragment);
					break;
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
				}
			}
		});
		mRgHost.getChildAt(tabNum).performClick();
	}

	public void getHomeTab() {
		if (mRgHost != null) {
			mRgHost.getChildAt(0).performClick();
		}
	}

	public void switchFragments(Fragment fragment) {
		FragmentManager manager = getChildFragmentManager();
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
			getisMyappsCanVisviable();
			UpdateManage.getInstance(mActivity).setLastUpdateRequestFail();
		}
	};

	private void getisMyappsCanVisviable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				isMyAppsNew = isMyappsCanVisviable();
				mCallback.onShowMyAppsTips(isMyAppsNew);
				notificationTipsHandler.sendEmptyMessage(1);
			}
		}).start();
	}

	private boolean isMyappsCanVisviable() {
		int updateCount = UpdateManage.getInstance(mActivity).getUpdateAppSize();
		int downloadCount = DownloadEntityManager.getInstance().getAllUnCompleteAppsDownloadCount();
		return (updateCount + downloadCount) > 0;
	}

	private void getisMyContentTipsCanVisviable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				isMyContentsNew = isMyContentTipsCanVisviable();
				mCallback.onShowMyContentsTips(isMyContentsNew);
				notificationTipsHandler.sendEmptyMessage(1);
			}
		}).start();
	}

	private boolean isMyContentTipsCanVisviable() {
		return !DownloadEntityManager.getInstance().getAllUnfinishedMediaDownloadList().isEmpty();
	}

	private static class SkinRequestHandler extends Handler {
		private WeakReference<SlidingPaneHomeFragment> fragment;

		SkinRequestHandler(SlidingPaneHomeFragment f) {
			fragment = new WeakReference<SlidingPaneHomeFragment>(f);
		}

		public void handleMessage(Message msg) {
			SlidingPaneHomeFragment slidingPaneHomeFragment = fragment.get();
			switch (msg.what) {
			case SkinConfigManager.onSuccess:
				if (slidingPaneHomeFragment == null)
					return;
				SkinAttentionResponse response = (SkinAttentionResponse) msg.obj;
				if (response != null) {
					int oldSkinCode = SkinConfigManager.getInstance().getSkinCode(slidingPaneHomeFragment.mActivity);
					int newSkinCode = response.getSkinCode();
					if (oldSkinCode != newSkinCode) {
						SkinConfigManager.getInstance().setSkinCode(slidingPaneHomeFragment.mActivity, newSkinCode);
						SkinConfigManager.getInstance().setSkinIsRead(slidingPaneHomeFragment.mActivity, false);
					}
					slidingPaneHomeFragment.showToolsTips(slidingPaneHomeFragment.mActivity);
				}
				break;
			default:
				break;
			}
		};
	};

	private void getSkinCode() {
		SkinConfigManager.getInstance().getSkinAttention(mActivity, new SkinRequestHandler(this));
	}

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

	private void showToolsTips(Context context) {
		isToolsNew = !SkinConfigManager.getInstance().getSkinIsRead(context);
		mCallback.onShowToolsTips(isToolsNew);
		notificationTipsHandler.sendEmptyMessage(1);
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
		mCallback.onShowSettingsTips(isSettingsNew);
		notificationTipsHandler.sendEmptyMessage(1);
	}

	private void showFeedbackNotification(Context context) {
		boolean attention = FeedbackManager.getInstance().getFeedbackAttention(context);
		if (attention) {
			Utils.showNotification(context);
		}
	}

	private Handler notificationTipsHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (isMyAppsNew || isSettingsNew || isToolsNew || isMyContentsNew) {
					mTipsView.setVisibility(View.VISIBLE);
				} else {
					mTipsView.setVisibility(View.GONE);
				}
				break;
			case 2:
				break;
			default:
				break;
			}

		};
	};

	/**
	 * 新版本提示，New小红图标
	 */
	private Handler tipsHanlder = new Handler() {
		public void handleMessage(Message msg) {
			// 新版本提示
			if (mTipsView != null) {
				isSettingsNew = true;
				notificationTipsHandler.sendEmptyMessage(1);
				mCallback.onShowSettingsTips(isSettingsNew);
			}
		};
	};

	@Override
	public void onPause() {
		super.onPause();
		if (mCurrentPage != null) {
			dataEyeSwitchTabs(mCurrentPage, false);
			dataEyeSwitchFragments(mCurrentPage, false);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			if (((HomeActivity) getActivity()).mSlidingPaneLayout.isOpen()) {
				((HomeActivity) getActivity()).mSlidingPaneLayout.closePane();
			} else {
				((HomeActivity) getActivity()).mSlidingPaneLayout.openPane();
			}
			break;

		case R.id.mh_search_ll:
			openSearchView();
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

	public interface OnShowRedTipsListener {

		public void onShowSettingsTips(boolean isVisiable);

		public void onShowMyAppsTips(boolean isVisiable);

		public void onShowMyContentsTips(boolean isVisiable);

		public void onShowToolsTips(boolean isVisiable);
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		if (SkinConstan.skinEnabled) {
			SkinConfigManager.getInstance().setTitleSkin(mActivity, mTitleView, mSlideView, mTitlePendant, mMenuView);

			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, homeTabBtn, SkinConstan.RB_HOME_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, appTabBtn, SkinConstan.RB_APP_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, gameTabBtn, SkinConstan.RB_GAME_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, RingtoneTabBtn, SkinConstan.RB_RT_BG);
			SkinConfigManager.getInstance().setRadioBtnDrawableTop(mActivity, wallpaperTabBtn, SkinConstan.RB_WP_BG);
		}
	}
}
