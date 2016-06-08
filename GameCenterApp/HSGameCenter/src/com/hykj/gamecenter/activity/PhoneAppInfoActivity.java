
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.MenuAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION_PATH;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.ReqAppInfoListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqRecommAppListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserScoreInfoListener;
import com.hykj.gamecenter.controller.ReqAppInfoController;
import com.hykj.gamecenter.controller.ReqRecommAppController;
import com.hykj.gamecenter.controller.ReqUserScoreInfoController;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTaskStateListener;
import com.hykj.gamecenter.fragment.AppInfoSoftWareDetailFragment;
import com.hykj.gamecenter.fragment.AppInfoUserEvaluateFragment;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.ApkInstalledManager.InstallFinishListener;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.logic.entry.UriGroupElemInfo;
import com.hykj.gamecenter.mta.MTAConstants;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Apps.UserScoreInfo;
import com.hykj.gamecenter.protocol.Reported;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog.KnowBtnOnClickListener;
import com.hykj.gamecenter.ui.widget.AppInfoViewPager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSLoadingView;
import com.hykj.gamecenter.ui.widget.CSLoadingView.ICSListViewLoadingRetry;
import com.hykj.gamecenter.ui.widget.CSPagerSlidingTabStrip;
import com.hykj.gamecenter.ui.widget.CSPagerSlidingTabStrip.OnPageChangedRefreshMainUIListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.DownloadListButton;
import com.hykj.gamecenter.ui.widget.MenuWindow;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class PhoneAppInfoActivity extends Activity implements
		InstallFinishListener ,MenuAdapter.IMenuItemClickListen{

	private CSCommonActionBar mActionBar;
	private AppInfo mAppInfo;
	private int mAppPosType = 0;
	private int mAppType = -1;
	private boolean mbFromAdvEntry = false; // 是否是从广告位进入
	private int mAppPosPosition = 0;
	private Resources mRes = null;
	private AppInfoSoftWareDetailFragment mSoftWareDetailFragment;
	private AppInfoUserEvaluateFragment mUserEvaluateFragment;//hei 56dp 14sp

	private ApkInstalledManager mApkInstalledManager;
	private ApkDownloadManager mApkDownloadManager;
	private StatisticManager mStatisticManager;

	// tabs
	private CSPagerSlidingTabStrip mTabs;
	private AppInfoViewPager mViewPager;
	private TabsViewPagerAdapter mTabsViewPagerAdapter;
	private CSLoadingView mCSLoading;

	// appInfo;
	private ImageView mAppIcon;
	private TextView mAppTitle;
	private RatingBar mAppRating;
	private TextView mAppRecommend;
	private TextView mAppDownload;
	private TextView mAppSize;
	private DownloadListButton mAppDownloadBtn;
	private DownloadListButton mAppOpenOrUpdateBtn;
	private TextView mAppInstalled;

	// private CSProgressButton mAppProgress;
	private ProgressBar mAppActiveProgressLoading;
	private ProgressBar mAppActiveProgressResume;
//	private RoundCornerImageView mAppIconCover;

	private Context mContext;
	private MenuWindow mMenuWindow;
	private ArrayList<String> mListMenu = new ArrayList<String>();
	private Apps.Permission[] pers;

	private final static String TAG = "AppInfoActivity";

	public static final String APP_INFO_TAG = "appinfo_tag";
	public static final String APP_TYPE = "app_type";

	// details
	private GroupElemInfo mGroupElemInfo;
	//推荐列表
	private final ArrayList<GroupElemInfo> mGroupElemInfoList = new ArrayList<GroupElemInfo>();

	private final ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

	private String page_from = "";
	private final OnPageChangedRefreshMainUIListener mOnPageChangeRefreshMainUIListener = new OnPageChangedRefreshMainUIListener() {
		@Override
		public void onPageChangedRefreshMainUI(int position) {
		}
	};

	//定义浮动窗口布局
	private FrameLayout mFloatLayout;
	private WindowManager.LayoutParams wmParams;
	//创建浮动窗口设置布局参数的对象
	private WindowManager mWindowManager;

	// 跳转过来所需的参数
	// posType the type of position
	// posPosition ID of position
	// showName name
	// RecommLevel level

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (App.getDevicesType() == App.PHONE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mRes = getResources();
		// 应用信息
		mGroupElemInfo = getIntent().getParcelableExtra(
				KEY.GROUP_INFO);

		mAppType = getIntent().getIntExtra(KEY.MAIN_TYPE, MAIN_TYPE.ALL);

		mbFromAdvEntry = getIntent().getBooleanExtra(KEY.ADV_ENTRY, false);
		// 安装
		mApkInstalledManager = ApkInstalledManager.getInstance();
		mApkDownloadManager = DownloadService.getDownloadManager();
		// TODO
		mApkInstalledManager.addInstallListener(this);
		mStatisticManager = StatisticManager.getInstance();

		if (mGroupElemInfo == null) {
			// TODO
			// 用于支持云指令
			// handleAction();
//            mGroupElemInfo = new GroupElemInfo();
//                   mGroupElemInfo.appId = getIntent().getIntExtra(KEY.APP_ID, -1);
//            mAppInfo = Tools.createAppInfo(mGroupElemInfo);
		} else {
			mAppInfo = Tools.createAppInfo(mGroupElemInfo);
		}
		// handleAction( );
		mAppPosType = getIntent().getIntExtra(StatisticManager.APP_POS_TYPE, 0);
		Logger.i(TAG, "onCreate " + "mappPosType "+ mAppPosType, "oddshou");
		mAppPosPosition = getIntent().getIntExtra(
				StatisticManager.APP_POS_POSITION, 0);
		page_from = getIntent().getStringExtra(
				MTAConstants.KEY_DETAIL_PAGE_FROM);
		mApkDownloadManager.setmPagePostion(mAppPosType);
		// Add or Update by Lei for no title @2014/07/01 UPD START
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		// Add or Update by Lei for no title @2014/07/01 UPD END

		setContentView(R.layout.phone_app_info_main);
		SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
		mContext = App.getAppContext();

		mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
		mActionBar.SetOnActionBarClickListener(actionBarListener);
		mActionBar.setSettingTipVisible(View.GONE);
		mActionBar.setSettingImage(R.drawable.csls_comm_actionbar_setting_icon);
		mActionBar.setTitle(mAppInfo.showName);

		initHeaderAppInfo(mAppInfo);
		initFloatLayout();

		// 设置标签页
		mTabs = (CSPagerSlidingTabStrip) findViewById(R.id.tabs);
		mTabs.setmOnPageChangeRefreshMainUIListener(mOnPageChangeRefreshMainUIListener);
		// 自定义tab的样式
		mTabs.setUnderlineColorResource(R.color.csl_tab_underline_color);
		mTabs.setIndicatorColorResource(R.color.csl_tab_indicator_color);
		mTabs.setTabSelectTextColorResource(R.color.csl_tab_indicator_color);
		mTabs.setTextColorResource(R.color.csl_black);
		// Add or Update for tab layout by Lei @2014/07/01 UPD START
		// mTabs.setTabPaddingLeftRight(10);
		// Add or Update for tab layout by Lei@2014/07/01 UPD END

		mViewPager = (AppInfoViewPager) findViewById(R.id.tab_pager);

		mCSLoading = (CSLoadingView) findViewById(R.id.cs_loading);
		mCSLoading.setOnRetryListener(mICSListViewLoadingRetry);

		initFragments();
		initRequestData();
	}

	private void initFloatLayout() {
		//获取浮动窗口视图所在布局
//		mFloatLayout = (FrameLayout) inflater.inflate(R.layout.appinfo_download_frame, null);
		//添加mFloatLayout
//		mWindowManager.addView(mFloatLayout, wmParams);
		//浮动窗口按钮
		mAppActiveProgressLoading = (ProgressBar)/*mFloatLayout.*/findViewById(R.id.action_detail_progress_bar_loading);
		mAppActiveProgressResume = (ProgressBar)/*mFloatLayout.*/findViewById(R.id.action_detail_progress_bar_resume);
		mAppInstalled = (TextView)/*mFloatLayout. */findViewById(R.id.app_installed);
		mAppDownloadBtn = (DownloadListButton)/*mFloatLayout. */findViewById(R.id.app_download_btn);
		mAppOpenOrUpdateBtn = (DownloadListButton)/*mFloatLayout. */findViewById(R.id.app_openorupdate);
		mAppDownloadBtn.setKownOnClickListener(mKnowBtnOnClickListener);
		mAppDownloadBtn
				.setWifiLoadOnClickListener(mWifiDownLoadOnClickListener);
		mAppOpenOrUpdateBtn.setKownOnClickListener(mKnowBtnOnClickListener);
		mAppOpenOrUpdateBtn
				.setWifiLoadOnClickListener(mOpenOrUpdateOnClickListener);

//		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
//				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
//				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
	}

	private int chnNo = 0;
	private TextView mAppVersion;
	private TextView mTextAdv;
	private TextView mTextFree;
	private TextView mTextSafe;

	public void handleAction() {
		String action = getIntent().getAction();
		if (action != null
				&& action
				.equals("com.hykj.gamecenter.activity.PhoneAppInfoActivity")) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.getPath().equals(ACTION_PATH.APP_INFO)) {
				mApkInstalledManager.loadApps();
				mAppInfo = Tools.createAppInfo(new UriGroupElemInfo(uri));
				// LogUtils.e( uri.getHost( ) );
				// LogUtils.e( uri.getPath( ) );
				// LogUtils.e( uri.getQuery( ) );
				// 统计位置pos
				chnNo = getIntent().getIntExtra("chnNo", 0);
				LogUtils.e("aaaaa,chnNo=" + chnNo + "|"
						+ getIntent().getIntExtra("chnNo", 0));
				// App.init();
				App.initChnNo(chnNo);

			} else {
				LogUtils.e("uri is null");
				return;
			}
			// mAppInfo = Tools.createAppInfo( new JsonGroupElemInfo( paramStr )
			// );
		}
	}

	public void initFragments() {

		if (mSoftWareDetailFragment == null) {
			mSoftWareDetailFragment = new AppInfoSoftWareDetailFragment();
		}
		if (mUserEvaluateFragment == null) {
			mUserEvaluateFragment = new AppInfoUserEvaluateFragment();
		}

		mFragmentList.clear();
		mFragmentList.add(mSoftWareDetailFragment);
		//         mFragmentList.add( mUserEvaluateFragment );

		setViewPager();

	}

	public void setViewPager() {
		if (mTabsViewPagerAdapter == null)
			mTabsViewPagerAdapter = new TabsViewPagerAdapter(
					getFragmentManager(), mFragmentList);
		mViewPager.setAdapter(mTabsViewPagerAdapter);
		mViewPager.setCurrentItem(0);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		mViewPager.setPageMargin(pageMargin);
		mTabs.setViewPager(mViewPager);
	}

	private void initHeaderAppInfo(AppInfo mAppInfo) {
		// 基本信息init
		mAppIcon = (ImageView) findViewById(R.id.app_icon);
		mAppTitle = (TextView) findViewById(R.id.app_title);
		mAppRating = (RatingBar) findViewById(R.id.app_rating);
		mAppRecommend = (TextView) findViewById(R.id.app_recommended);
		mAppDownload = (TextView) findViewById(R.id.app_download);
		mAppSize = (TextView) findViewById(R.id.app_size);
		mAppVersion = (TextView) findViewById(R.id.app_version);

		// mAppProgress = (CSProgressButton)findViewById(
		// R.id.app_progress_button );
//		mAppActiveProgressLoading = (ProgressBar) findViewById(R.id.action_detail_progress_bar);
//		mAppIconCover = (RoundCornerImageView) findViewById(R.id.app_detail_icon_cover);
//		Drawable drawable = BitmapUtils.getRoundeDrawable(
//				App.getAppContext(),
//				App.getAppContext().getResources()
//						.getDrawable(R.drawable.loading_cover_view),
//				R.dimen.icon_size);
//		mAppIconCover.setBackgroundDrawable(drawable);

		initViews(mAppInfo);
//		mAppDownloadBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				return;
//			}
//		});

		mAppRating.setVisibility(View.VISIBLE);
		mAppRecommend.setVisibility(View.GONE);

		// 如果是推荐页与游戏页打开，显示推荐语，其他显示推荐等级
		/*
		 * if (MAIN_TYPE.APP == mAppType || MAIN_TYPE.GAME == mAppType ||
         * mbFromAdvEntry) {// 广告位也显示推荐语 mAppRating.setVisibility(View.GONE);
         * mAppRecommend.setVisibility(View.VISIBLE); } else {
         * mAppRating.setVisibility(View.VISIBLE);
         * mAppRecommend.setVisibility(View.GONE); }
         */

		// 设置安全tag
		mTextSafe = (TextView) findViewById(R.id.textSafe);
		mTextAdv = (TextView) findViewById(R.id.textAdv);
		mTextFree = (TextView) findViewById(R.id.textFree);
	}

	private void initViews(AppInfo mAppInfo) {
		mAppTitle.setText(mAppInfo.showName);
		mAppRating.setRating(mAppInfo.recommLevel / 2);
		mAppRecommend.setText(mAppInfo.recommWord);
		ImageLoader.getInstance().displayImage(mAppInfo.iconUrl, mAppIcon,
				DisplayOptions.optionsIcon);
		pers = mAppInfo.permission;
		Logger.e(TAG, "initViews " + "pers  == "+ pers.length, "oddshou");
		mListMenu.clear();
		if (pers.length > 0) {
			mListMenu.addAll(Arrays.asList(getResources().getStringArray(R.array.appinfo_menu_array_two)));
		} else {
			mListMenu.addAll(Arrays.asList(getResources().getStringArray(R.array.appinfo_menu_array_one)));
		}
	}

	private void initTag(AppInfo appInfo) {
		mTextSafe.setVisibility(View.VISIBLE);
		mTextAdv.setVisibility(View.VISIBLE);
		mTextFree.setVisibility(View.VISIBLE);
		int appFlag = mAppInfo.appTagFlag /*7*/;
		Logger.i(TAG, "initHeaderAppInfo " + appFlag, "oddshou");
		for (int i = 0; i < 5; i++) {
			if ((appFlag >> i) % 2 == 1) {
				switch (i) {
					case 0:     //安全
						// 表示第1位
						break;
					case 1:     //无广告
						// 表示第2位
						mTextAdv.setText(R.string.safe_noadv);
						Drawable drawableAdv = getResources().getDrawable(R.drawable.safe_noadv);
						drawableAdv.setBounds(0, 0, drawableAdv.getMinimumWidth(),
								drawableAdv.getMinimumHeight());
						mTextAdv.setCompoundDrawables(drawableAdv, null, null, null);
						break;
					case 2:     //收费
						// 表示第3位
						mTextFree.setText(R.string.safe_nofree);
						Drawable drawableFree = getResources().getDrawable(R.drawable.safe_nofree);
						drawableFree.setBounds(0, 0, drawableFree.getMinimumWidth(),
								drawableFree.getMinimumHeight());
						mTextFree.setCompoundDrawables(drawableFree, null, null, null);
						break;

					default:
						break;
				}
			}
		}
	}

	private final DownloadTaskStateListener downloadTaskStateListener = new DownloadTaskStateListener() {
		@Override
		public void onUpdateTaskState(DownloadTask task) {

			LogUtils.e("请求更新状态的应用:" + task.appName + "|应用编号:" + task.appId);
			if (task.appId == mAppInfo.appId) {
				LogUtils.e("实际更新状态的应用:" + task.appName + "|应用编号:" + task.appId);
				mAppDownloadBtn.setTag(task);
				updateDownLoadBtn(task);
			}
		}

		@Override
		public void onUpdateTaskProgress(DownloadTask task) {
			LogUtils.e("请求更新进度的应用:" + task.appName + "|应用编号:" + task.appId);
			if (task.appId == mAppInfo.appId) {
				LogUtils.e("实际更新进度的应用:" + task.appName + "|应用编号:" + task.appId);
				initProgress(task);
			}

		}

		@Override
		public void onUpdateTaskList(Object obj) {
			updateDownLoadBtn(null);
		}
	};

	private void initRequestData() {
		mCSLoading.showLoading();
		mUiHandler.sendEmptyMessage(MSG_REQUEST_DETAIL_DATA);

	}

	public void initProgress(DownloadTask task) {
		if (task != null) {
			long totalSize = task.fileLength;
			long dealtSize = task.progress;
			// LogUtils.e( "task.fileLength=" + task.fileLength );
			// LogUtils.e( "task.progress=" + task.progress );
			// mAdapter.updateProgress( packageName , dealtSize , totalSize );
			int percent = (int) (((double) dealtSize / (double) totalSize) * 100);
			if (percent <= 99
					&& task.packageName.equals(mAppInfo.packName)) {
				// mAppProgress.setProgress( 100 - percent );
				mAppActiveProgressLoading.setProgress(percent);
				Logger.i(TAG, "percent   ==  " + percent, "tomqian");
				mAppActiveProgressResume.setProgress(percent);
				String str = "";
				switch (task.getState()) {
					case LOADING:
						float percentD = (float) (((double) dealtSize / (double) totalSize) * 100);
						DecimalFormat fnum = new DecimalFormat("##0.00");
						str = fnum.format(percentD);
						str = String.format(this.getResources().getString(R.string.downloading_percent),
								str );
						mAppDownloadBtn.setText(str);
						break;
				}
			}
		}

	}

	public void downloadBtnToOpenOrUpdate(String packName) {
		LogUtils.e("应用:" + packName + ",已安装且未在下载队列中。");
		mAppDownloadBtn.setVisibility(View.GONE);
		mAppInstalled.setVisibility(View.GONE);
		mAppInstalled.setText(getString(R.string.app_installed));

		mAppActiveProgressLoading.setVisibility(View.GONE);
		mAppActiveProgressResume.setVisibility(View.GONE);
//		mAppIconCover.setVisibility(View.GONE);

		mAppOpenOrUpdateBtn.setVisibility(View.VISIBLE);
		if (mApkInstalledManager.isApkNeedToUpdate(packName)) {
			mAppOpenOrUpdateBtn
					.setText(mContext.getString(R.string.app_update));
			mAppOpenOrUpdateBtn
					.setBackgroundResource(R.drawable.btn_green_green_selector);
			mAppOpenOrUpdateBtn.setTextColor(mRes
					.getColorStateList(R.color.btn_noshadow_blue_color));
			mAppOpenOrUpdateBtn.setTag(mAppInfo);
		} else {
			mAppOpenOrUpdateBtn.setText(mContext.getString(R.string.app_open));
			mAppOpenOrUpdateBtn
					.setBackgroundResource(R.drawable.btn_gray_selector/*
                                                                        * R.drawable
                                                                        * .
                                                                        * btn_green_green_selector
                                                                        */);
			mAppOpenOrUpdateBtn
					.setTextColor(mRes.getColorStateList(R.color.btn_gray_color)/*
                                                                                 * mRes
                                                                                 * .
                                                                                 * getColorStateList
                                                                                 * (
                                                                                 * R
                                                                                 * .
                                                                                 * color
                                                                                 * .
                                                                                 * btn_noshadow_blue_color
                                                                                 * )
                                                                                 */);
			LogUtils.e("activity class name = "
					+ mApkInstalledManager.getActivityClassName(packName));
			LogUtils.e("packname = " + packName);
			mAppOpenOrUpdateBtn.setTag(new String[]{
					mApkInstalledManager.getActivityClassName(packName),
					packName
			});
		}
	}

	private void updateDownLoadBtn(DownloadTask dinfo) {

		// int padding = dpTopx( 18 );
		// mAppDownloadBtn.setPadding( padding , 0 , padding , 0 );
		LogUtils.e("updateDownLoadBtn");

		if (mAppInfo == null) {
			return;
		}
		dinfo = mApkDownloadManager.getDownloadTaskByAppId(mAppInfo.appId);
		final String packageName = mAppInfo.packName;
		if (mApkInstalledManager.isApkLocalInstalled(packageName)
				&& dinfo == null) {
			downloadBtnToOpenOrUpdate(packageName);
			return;
		}
		if (dinfo != null) {

			mAppDownloadBtn.setTag(dinfo);
			initProgress(dinfo);
			mAppActiveProgressLoading.setVisibility(View.GONE);
			mAppActiveProgressResume.setVisibility(View.GONE);
//			mAppIconCover.setVisibility(View.GONE);
			mAppInstalled.setVisibility(View.GONE);
			mAppDownloadBtn.setVisibility(View.VISIBLE);
			mAppOpenOrUpdateBtn.setVisibility(View.GONE);

			switch (dinfo.getState()) {
				case PREPARING:
					mAppActiveProgressLoading.setProgress(0);
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_green_green_selector);
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_noshadow_blue_color));
					mAppDownloadBtn.setText(getString(R.string.app_pause));
					mAppDownloadBtn.setEnabled(true);
					mAppActiveProgressLoading.setVisibility(View.VISIBLE);
					mAppActiveProgressResume.setVisibility(View.GONE);
//					mAppIconCover.setVisibility(View.VISIBLE);
					break;
				case WAITING:
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.shape_framework_theme);
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.color_green_normal));
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.download_tip_waiting));
					mAppActiveProgressLoading.setVisibility(View.VISIBLE);
					mAppActiveProgressResume.setVisibility(View.GONE);
//					mAppIconCover.setVisibility(Vapp_pausereak;
				case STARTED:
				case LOADING:
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.shape_framework_theme);
//					mAppDownloadBtn.setTextColor(mRes
//							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_green_normal));
					mAppDownloadBtn.setEnabled(true);
//					showProgress(dinfo);
//					mAppDownloadBtn.setText(getString(R.string.app_pause));
					mAppActiveProgressLoading.setVisibility(View.VISIBLE);
					mAppActiveProgressResume.setVisibility(View.GONE);
//					mAppIconCover.setVisibility(View.VISIBLE);
					break;
				case STOPPED:
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.shape_framework_red);
//					mAppDownloadBtn.setTextColor(mRes
//							.getColorStateList(R.color.btn_yellow_color));
					mAppDownloadBtn.setTextColor(mRes.getColor(R.color.textcolor_progress6));
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_resume));
					mAppActiveProgressLoading.setVisibility(View.GONE);
					mAppActiveProgressResume.setVisibility(View.VISIBLE);
//					mAppIconCover.setVisibility(View.VISIBLE);
					break;
				case SUCCEEDED:
					// TODO
					mAppActiveProgressLoading.setProgress(100);
					mAppActiveProgressResume.setVisibility(View.GONE);
//					mAppDownloadBtn
//							.setBackgroundResource(R.drawable.btn_green_selector);
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_yellow_selector);
//					mAppDownloadBtn.setTextColor(mRes
//							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_yellow_color));
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_install));
					// if( mApkInstalledManager.isApkLocalInstalled(
					// dinfo.packageName ) )
					// {
					// //如果安装立即刷新列表
					// mAppDownloadBtn.setVisibility( View.GONE );
					// mAppInstalled.setVisibility( View.VISIBLE );
					// mAppInstalled.setText( getString( R.string.app_installed
					// ) );
					// LogUtils.e( "详情界面,应用:" + dinfo.appName + "|" +
					// dinfo.packageName + ",已安装" );
					// downloadBtnToOpenOrUpdate( dinfo.packageName );
					// }
					// mAppProgress.setVisibility( View.GONE );
					break;
				case DELETED:
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_green_selector);
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_redownload));
					mAppActiveProgressLoading.setVisibility(View.VISIBLE);
					mAppActiveProgressResume.setVisibility(View.GONE);
//					mAppIconCover.setVisibility(View.VISIBLE);
					mAppInstalled.setVisibility(View.GONE);
					mAppDownloadBtn.setVisibility(View.VISIBLE);
					break;
				case INSTALLING:
					// TODO
					mAppActiveProgressLoading.setProgress(100);
					mAppActiveProgressResume.setVisibility(View.GONE);
					mAppInstalled.setVisibility(View.VISIBLE);
					mAppDownloadBtn.setVisibility(View.GONE);
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_green_selector);
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn.setEnabled(false);
					mAppDownloadBtn.setText(getString(R.string.app_install));
					// mAppProgress.setVisibility( View.GONE );
					mAppInstalled.setText(getString(R.string.app_installing));
					break;

				case FAILED_NETWORK:
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_green_selector);
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_retry));
					mAppActiveProgressLoading.setVisibility(View.VISIBLE);
					mAppActiveProgressResume.setVisibility(View.GONE);
//					mAppIconCover.setVisibility(View.VISIBLE);
					break;
				case FAILED_BROKEN:
					// mAppProgress.setVisibility( View.GONE );
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_green_selector);
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_redownload));
					break;
				case FAILED_NOEXIST:
					// mAppProgress.setVisibility( View.GONE );
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_gray_color));
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_gray_selector);
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_delete));
					break;
				case FAILED_SERVER:
					// mAppProgress.setVisibility( View.GONE );
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_green_selector);
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_retry));

					break;
				case FAILED_NOFREESPACE:
					// mAppProgress.setVisibility( View.GONE );
					mAppDownloadBtn.setTextColor(mRes
							.getColorStateList(R.color.btn_green_color));
					mAppDownloadBtn
							.setBackgroundResource(R.drawable.btn_green_selector);
					mAppDownloadBtn.setEnabled(true);
					mAppDownloadBtn.setText(getString(R.string.app_retry));
					break;
			}

		} else {
			mAppDownloadBtn.setVisibility(View.VISIBLE);
			mAppDownloadBtn.setText(getString(R.string.app_download));
			mAppDownloadBtn
					.setBackgroundResource(R.drawable.btn_green_green_selector);
			mAppDownloadBtn.setTextColor(mRes
					.getColorStateList(R.color.btn_noshadow_blue_color));
			mAppDownloadBtn.setEnabled(true);
			mAppInstalled.setVisibility(View.GONE);
			mAppDownloadBtn.setTag(dinfo);
			mAppActiveProgressLoading.setVisibility(View.GONE);
			mAppActiveProgressResume.setVisibility(View.GONE);
			mAppOpenOrUpdateBtn.setVisibility(View.GONE);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// refreshActionBarTip( );
		LogUtils.e("onResume");
//		createFloatView();
		updateDownLoadBtn(null);
		mApkDownloadManager.registerListener(downloadTaskStateListener); // 下载消息通知注册
		mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		if (mFloatLayout != null)
//		{
//			//移除悬浮窗口
//			mWindowManager.removeView(mFloatLayout);
//		}
		// ImageLoader.getInstance( ).clearMemoryCache( ); //在最栈顶activity时适当清除缓存
		mApkDownloadManager.unregisterListener(downloadTaskStateListener);// 界面关闭反注册消息通知
		mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		//通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)this .getSystemService(WINDOW_SERVICE);
		//设置window type
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		//设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		//设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		//调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = 0;
		wmParams.y = 0;

		//设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		wmParams.height = getResources().getDimensionPixelSize(R.dimen.info_footer_height);

		LayoutInflater inflater = LayoutInflater.from(this);
		//获取浮动窗口视图所在布局
		mFloatLayout = (FrameLayout) inflater.inflate(R.layout.appinfo_download_frame, null);
		//添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);
		//浮动窗口按钮
		mAppActiveProgressLoading = (ProgressBar)mFloatLayout.findViewById(R.id.action_detail_progress_bar_loading);
		mAppActiveProgressResume = (ProgressBar)mFloatLayout.findViewById(R.id.action_detail_progress_bar_resume);
		mAppInstalled = (TextView)mFloatLayout. findViewById(R.id.app_installed);
		mAppDownloadBtn = (DownloadListButton)mFloatLayout. findViewById(R.id.app_download_btn);
		mAppOpenOrUpdateBtn = (DownloadListButton)mFloatLayout. findViewById(R.id.app_openorupdate);
		mAppDownloadBtn.setKownOnClickListener(mKnowBtnOnClickListener);
		mAppDownloadBtn
				.setWifiLoadOnClickListener(mWifiDownLoadOnClickListener);
		mAppOpenOrUpdateBtn.setKownOnClickListener(mKnowBtnOnClickListener);
		mAppOpenOrUpdateBtn
				.setWifiLoadOnClickListener(mOpenOrUpdateOnClickListener);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		//设置监听浮动窗口的触摸移动

	}

	// 加载
	// private final ICSLoadingViewListener mCSLoadingViewListener = new
	// ICSLoadingViewListener( )
	// {
	//
	// @Override
	// public void onRetryRequestData()
	// {
	// mUiHandler.sendEmptyMessage( MSG_REQUEST_DETAIL_DATA );
	// }
	//
	// @Override
	// public void onInitRequestData()
	// {
	// mUiHandler.sendEmptyMessage( MSG_REQUEST_DETAIL_DATA );
	// }
	// };

	private final ICSListViewLoadingRetry mICSListViewLoadingRetry = new ICSListViewLoadingRetry() {

		@Override
		public void onRetry() {
			initRequestData();
		}

	};

	private static final int MSG_REQUEST_DETAIL_DATA = 1000;
	private static final int MSG_GET_DETAIL = 1001;
	private static final int MSG_NET_ERROR = 1002;
	private final static int MSG_REFRESH_APP_DOWNLOAD_BTN = 2001;

	private final Handler mUpdateRefreshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_CONSTANTS.MSG_CHECK_UPDATE: {
					// mAppDownloadBtn.setVisibility( View.GONE );
					// mAppInstalled.setVisibility( View.VISIBLE );
					// mAppInstalled.setText( getString( R.string.app_installed
					// ) );
					break;
				}
				case MSG_CONSTANTS.MSG_CHECK_TOAST: {
					CSToast.show(mContext, (String) msg.obj);
					break;
				}
			}
		}
	};

	private final Handler mUiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_REQUEST_DETAIL_DATA:
					reqAppDetailData();
					reqUserScore(mAppInfo.appId);
					reqCommApp();
					break;
				case MSG_GET_DETAIL: {
					Logger.i(TAG, "phone app info " + mUserScoreInfo + " appinfo.appdese " + mAppInfo.appDesc, "oddshou");
					if (mUserScoreInfo == null || TextUtils.isEmpty(mAppInfo.appDesc)/* || mGroupElemInfoList.size() == 0*/) {
						return;
					}
					//初始化标签tag
					initTag(mAppInfo);
					initViews(mAppInfo);
					mCSLoading.hide();
					mViewPager.setVisibility(View.VISIBLE);
					View view = findViewById(R.id.cs_app_info_layout);
					view.invalidate();
					mTabsViewPagerAdapter.notifyDataSetChanged();
					mAppDownload.setText(mAppInfo.downTimes + "");
					// mAppSize.setText( mContext.getString(
					// R.string.app_info_size_flag , mAppInfo.getPackSize( ) )
					// );
					mAppSize.setText(StringUtils.byteToString(mAppInfo
							.packSize));
					mAppVersion.setText(mAppInfo.verName);
					// 设置请求的到值
					mSoftWareDetailFragment.setData(mAppInfo, mUserScoreInfo, mGroupElemInfoList);
					mUserEvaluateFragment.setData(mAppInfo);
					mTabs.setmOnDoubleClickTabListener(mUserEvaluateFragment.mOnDoubleClickTabListener);
					break;
				}
				case MSG_NET_ERROR:
					mViewPager.setVisibility(View.GONE);
					// notifyRequestDataError( );
					mCSLoading.showNoNetwork();
					break;
				case MSG_REFRESH_APP_DOWNLOAD_BTN:
					LogUtils.e("安装成功刷新界面开始");
					// mAppDownloadBtn.setVisibility( View.GONE );
					// mAppInstalled.setVisibility( View.VISIBLE );
					updateDownLoadBtn((DownloadTask) msg.obj);
					break;

			}
		}
	};

	private void reqAppDetailData() {
		ReqAppInfoController controller = new ReqAppInfoController(
				mAppInfo.appId, 0, 0, mReqAppInfoListener);
		Logger.i(TAG, "reqAppList mAppPosType = " + mAppPosType);
		controller.setClientPos(ReportConstants.reportPos(
				mAppPosType)
				+ mAppPosPosition);

		// controller.setClientPos(mAppDetailSource);
		controller.setChnNo(chnNo);
		controller.doRequest();
		if (!TextUtils.isEmpty(page_from)) {
			MtaUtils.trackDetails(page_from);
		} else {
			MtaUtils.trackDetails("Others");
		}
	}

	private void reqCommApp() {
//        int ints[] = HomePageActivity.getGroupIdByDB(
//                GROUP_TYPE.ALL_ONLY_GAMES_TYPE, ORDER_BY.AUTO);
//        ReqGroupElemsListController controller = new ReqGroupElemsListController(
//                ints[0], ints[1], ints[2], ints[3],
//                12, 1,
//                mReqGroupElemsListener);

		ReqRecommAppController controller = new ReqRecommAppController(
				mAppInfo.appId, mAppInfo.appClass, mAppInfo.appType, 12, 0, ORDER_BY.AUTO, mReqRecommAppListener);
			controller.setClientPos(ReportConstants.reportPos(
					mAppPosType)
					+ mAppPosPosition);

		controller.doRequest();
		mGroupElemInfoList.clear();
	}

	private final ReqRecommAppListener mReqRecommAppListener = new ReqRecommAppListener() {

		@Override
		public void onNetError(int errCode, String errorMsg) {
			// TODO Auto-generated method stub
			Logger.e(TAG, "onNetError:" + errorMsg + ",errCode:" + errCode);
			mUiHandler.sendEmptyMessage(Msg.NET_ERROR);
		}

		@Override
		public void onReqRecommAppSucceed(GroupElemInfo[] infoList, String serverDataVer) {
			// TODO Auto-generated method stub
			Logger.i(TAG, "onReqGroupElemsSucceed:" + infoList.length);
			// 将infoList按照Position ID 进行排序
			if (infoList != null/* && infoList.length > 0*/) {
				mGroupElemInfoList.clear();
				mGroupElemInfoList.addAll(Tools.arrayToList(infoList));
				//                msg.obj = Tools.arrayToList(infoList);
				mUiHandler.removeMessages(MSG_GET_DETAIL);
				mUiHandler.sendEmptyMessage(MSG_GET_DETAIL);
			}
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			// TODO Auto-generated method stub
			Logger.e(TAG, "errorMsg:" + errorMsg + ",statusCode:" + statusCode);
			mUiHandler.sendEmptyMessage(Msg.NET_ERROR);
		}
	};

	// 请求评分信息
	private void reqUserScore(int appId) {
		ReqUserScoreInfoController scoreController = new ReqUserScoreInfoController(
				appId, mReqUserScoreInfoListener);
		scoreController.setClientPos(ReportConstants.reportPos(
				mAppPosType)
				+ mAppPosPosition);
		scoreController.doRequest();
		Logger.d(TAG, "reqUserScore " + appId);
	}

	private final ReqAppInfoListener mReqAppInfoListener = new ReqAppInfoListener() {

		@Override
		public void onNetError(int errCode, String errorMsg) {
			Logger.e("ADA", "onNetError errCode:" + errCode + ",errorMsg:"
					+ errorMsg);
			mUiHandler.sendEmptyMessage(MSG_NET_ERROR);
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			Logger.e("ADA", "onReqFailed statusCode:" + statusCode
					+ ",errorMsg:" + errorMsg);
			mUiHandler.sendEmptyMessage(MSG_NET_ERROR);
		}

		@Override
		public void onReqAppInfoSucceed(AppInfo appInfo, String serverCacheVer) {
			if (mAppInfo.appId == appInfo.appId) {
				mAppInfo = appInfo;
				mUiHandler.removeMessages(MSG_GET_DETAIL);
				mUiHandler.sendEmptyMessage(MSG_GET_DETAIL);
				Log.d(TAG, "onReqAppInfoSucceed ");
			} else {
				Logger.e("ADA", "onReqAppDetailSucceed getAppDetail null");
			}

		}

	};
	private UserScoreInfo mUserScoreInfo;
	private final ReqUserScoreInfoListener mReqUserScoreInfoListener = new ReqUserScoreInfoListener() {

		@Override
		public void onNetError(int errCode, String errorMsg) {
			Logger.e("mReqUserScoreInfoListener", "onNetError errCode:"
					+ errCode + ",errorMsg:" + errorMsg);
			mUiHandler.sendEmptyMessage(MSG_NET_ERROR);
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			Logger.e("mReqUserScoreInfoListener", "onReqFailed statusCode:"
					+ statusCode + ",errorMsg:" + errorMsg);
			mUiHandler.sendEmptyMessage(MSG_NET_ERROR);
		}

		@Override
		public void onReqUserScoreInfoSucceed(UserScoreInfo userScoreInfo) {
			Logger.d("mReqUserScoreInfoListener", "userScoreInfo "
					+ userScoreInfo);
			Message msg = Message.obtain();
			msg.what = MSG_GET_DETAIL;
			// msg.obj = userScoreInfo;
			mUserScoreInfo = userScoreInfo;
			mUiHandler.removeMessages(MSG_GET_DETAIL);
			mUiHandler.sendMessage(msg);
			Log.d(TAG, "onReqUserScoreInfoSucceed ");
			// userInfo = userScoreInfo;

			// refreshUI( );

		}

	};

    /*public void onCreateContextMenu(android.view.ContextMenu menu, View v,
            android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == (ImageView) mActionBar.findViewById(R.id.setting_icon)) {
            menu.add(1, 0, 0, "查看权限");
            menu.add(1, 1, 1, "举报不当");
        }

    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 0:
                CSToast.show(mContext, "查看权限");
                break;
            case 1:
                CSToast.show(mContext, "举报不当");
                break;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }*/

	private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

		@Override
		public void onActionBarClicked(int position, View view) {

			switch (position) {
				case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
					onBackPressed();
					break;
				case CSCommonActionBar.OnActionBarClickListener.SETTING_BNT:

					/*MorePopWindow morePopWindow = new MorePopWindow(PhoneAppInfoActivity.this,
							mAppInfo);
					morePopWindow.showPopupWindow(view);*/

					int menuWith = getResources().getDimensionPixelSize(R.dimen.menu_with);
					int menuHeight = getResources().getDimensionPixelSize(R.dimen.menu_height);
					MenuAdapter menuAdapter = new MenuAdapter(PhoneAppInfoActivity.this, mListMenu, PhoneAppInfoActivity.this, menuWith,menuHeight);
					if (mMenuWindow == null) {
						mMenuWindow = new MenuWindow(PhoneAppInfoActivity.this, menuAdapter);
						mMenuWindow.setSelectItem(0);   //默认
					}
					mMenuWindow.showPopupWindow(mActionBar);
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		if (mMenuWindow != null) {
			mMenuWindow.showPopupWindowConfigurationChanged(mActionBar);
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onMenuItemClick(View view, int position) {
		Intent intent = null;
		switch (position) {
			case 0:
				intent = new Intent(PhoneAppInfoActivity.this, ImproperReportActivity.class);
				intent.putExtra(StatisticManager.APP_ID, mAppInfo.appId);
				PhoneAppInfoActivity.this.startActivity(intent);
				break;
			case 1:
				 intent = new Intent(PhoneAppInfoActivity.this, ImproperDetailActivity.class);
				Logger.i("ImproperDetailActivity", "pers == " + pers.length);
				if (pers.length > 0) {
					ArrayList<String> list = new ArrayList<String>();
					for (int i = 0; i < pers.length; i++) {
						Apps.Permission per = pers[i];
						String str = per.permissionFlag.toString().trim();
						list.add(str);
					}
					Logger.i("ImproperDetailActivity", "list == " + list);
					intent.putStringArrayListExtra(StatisticManager.APP_PERMISSION, list);
				}

				// 上报访问当前页面
				Reported.ReportedInfo builder = new Reported.ReportedInfo();
				builder.statActId = ReportConstants.STATACT_ID_IMPROPER_DETAIL;
				//                builder.statActId2 = PAGE_INDEX.IMPROPER_DETAIL;
				builder.ext1 = "" + mAppInfo.appId;
				ReportConstants.getInstance().reportReportedInfo(builder);
				Logger.i("ImproperDetailActivity", "builder == " + builder);
				PhoneAppInfoActivity.this.startActivity(intent);
                break;

        }
		if (mMenuWindow.isShowing()) {
			mMenuWindow.dismiss();
		}
	}

    /*    private void showPopupWindow(View view) {
            // 一个自定义的布局，作为显示的内容
            View contentView = LayoutInflater.from(mContext).inflate(
                    R.layout.popwindow_dropdown_item, null);
            final PopupWindow popupWindow = new PopupWindow(contentView,
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
            popupWindow.setTouchable(true);
            // 设置好参数之后再show
            popupWindow.showAsDropDown(view);
        }*/

	private final KnowBtnOnClickListener mKnowBtnOnClickListener = new KnowBtnOnClickListener() {

		@Override
		public void onKnowClickListener(View v) {
			DownloadTask dinfo = (DownloadTask) v.getTag();
			if (dinfo == null) {
				// addTaskToDownLoadList(v);
				// ###############oddshou 非 wifi状态下 坚持下载
				mApkDownloadManager.startDownload(mAppInfo,
						ReportConstants.STAC_APP_POSITION_APP_DETAIL, mAppPosType);
//				if (mSoftWareDetailFragment != null) {
//					mSoftWareDetailFragment.scrollToBottom();
//				}
			} else {
				return;
			}
		}
	};

	// private void addTaskToDownLoadList(final View v) {
	// mApkDownloadManager.startDownload(mAppInfo,
	// StatisticManager.STAC_APP_POSITION_APP_DETAIL, mAppPosType);
	// new Thread() {
	// @Override
	// public void run() {
	// try {
	// sleep(500); // 睡眠500ms是等待下载的状态发生改变
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// DownloadTask task = (DownloadTask) v.getTag();
	// mApkDownloadManager.stopDownload(task);
	// // 非wifi情况下，系统将任务加入下载队列之后，自动暂停下载任务
	// mStatisticManager.reportDownloadStop(task.appId, task.packId,
	// task.nFromPos,
	// StatisticManager.STAC_DOWNLOAD_APK_OTHERS_STOP,
	// getString(R.string.not_wifi_stop));
	// //MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_OTHERS);
	// };
	// }.start();
	// }

	private final WifiDownLoadOnClickListener mOpenOrUpdateOnClickListener = new WifiDownLoadOnClickListener() {

		@Override
		public void onWifiClickListener(View v) {
			Object obj = v.getTag();
			if (obj instanceof String[]) {
				String[] args = (String[]) obj;
				Intent intent = new Intent("android.intent.action.MAIN");
				LogUtils.e(args[0] + "|" + args[1]);
				intent.setClassName(args[1], args[0]);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			} else if (obj instanceof AppInfo) {
				mApkDownloadManager.startDownload(mAppInfo,
						ReportConstants.STAC_APP_POSITION_APP_DETAIL,
						mAppPosType);
			}
		}
	};

	private final WifiDownLoadOnClickListener mWifiDownLoadOnClickListener = new WifiDownLoadOnClickListener() {

		@Override
		public void onWifiClickListener(View v) {
			// if( UITools.isFastDoubleClick( ) )
			// {
			// return;
			// }
//			v.setEnabled(false);
			DownloadTask dinfo = (DownloadTask) v.getTag();
			if (dinfo != null) {
				switch (dinfo.getState()) {
					case PREPARING:
					case WAITING:
					case STARTED:
					case LOADING:
						mApkDownloadManager.stopDownload(dinfo);
						// 用户主动暂停下载任务上报
						Logger.i(TAG, "dinfo.packId=" + dinfo.packId);
						ReportConstants
								.reportDownloadStop(
										dinfo.appId,
										dinfo.packId,
                                        /* dinfo.nFromPos */ReportConstants.STAC_APP_POSITION_APP_DETAIL,
										ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP,
										"");
						// MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_USER_ACTIVE_STOP);
						break;
					case SUCCEEDED:
						mApkDownloadManager.installDownload(dinfo);
						break;
					case STOPPED:
					case FAILED_NETWORK:
					case FAILED_SERVER:
					case FAILED_NOFREESPACE:
						mApkDownloadManager.resumeDownload(dinfo);
						// 暂停下载任务继续下载上报
						ReportConstants
								.reportDownloadResume(
										dinfo.appId,
										dinfo.packId,
                                        /* dinfo.nFromPos */ReportConstants.STAC_APP_POSITION_APP_DETAIL,
										ReportConstants.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME,
										"");
						// MtaUtils.trackDownloadResume();
						break;
					case FAILED_BROKEN:
					case DELETED:
						mApkDownloadManager.restartDownload(dinfo);
						break;
					case FAILED_NOEXIST:
						mApkDownloadManager.removeDownload(dinfo);
						// 取消下载任务
						ReportConstants.reportDownloadStop(dinfo.appId,
								dinfo.packId, /* dinfo.nFromPos */
								ReportConstants.STAC_APP_POSITION_APP_DETAIL,
								ReportConstants.STAC_DOWNLOAD_APK_CANCEL_TASK, "");
						// MtaUtils.trackDownloadCancel(dinfo.appName);
						break;
				}
			} else {
				// 详情点击下载   位置上报
				Logger.i(TAG, "mAppPosType mAppPosType mAppPosType = " + mAppPosType);
//                int position = mAppDetailSource;
//                if (mAppDetailSource == -1) {
//                    position = mAppPosPosition;
//                } 
//				if (mSoftWareDetailFragment != null) {
//					mSoftWareDetailFragment.scrollToBottom();
//				}
				mApkDownloadManager.startDownload(mAppInfo,
						ReportConstants.STAC_APP_POSITION_APP_DETAIL,
						mAppPosType);
				mAppActiveProgressLoading.setProgress(0);
				mAppActiveProgressResume.setProgress(0);
			}
		}
	};

	// //下载或者更新
	// private View.OnClickListener mDownloadClickListener = new
	// View.OnClickListener( )
	// {
	//
	// @Override
	// public void onClick( View v )
	// {
	// if( UITools.isFastDoubleClick( ) )
	// {
	// return;
	// }
	// DownloadTask dinfo = (DownloadTask)v.getTag( );
	// if( dinfo != null )
	// {
	// switch ( dinfo.getState( ) )
	// {
	// case PREPARING :
	// case WAITING :
	// case STARTED :
	// case LOADING :
	// mApkDownloadManager.stopDownload( dinfo );
	// break;
	// case SUCCEEDED :
	// mApkDownloadManager.installDownload( dinfo );
	// break;
	// case STOPPED :
	// case FAILED_NETWORK :
	// case FAILED_SERVER :
	// case FAILED_NOFREESPACE :
	// mApkDownloadManager.resumeDownload( dinfo );
	// break;
	// case FAILED_BROKEN :
	// case DELETED :
	// mApkDownloadManager.restartDownload( dinfo );
	// break;
	// case FAILED_NOEXIST :
	// mApkDownloadManager.removeDownload( dinfo );
	// break;
	// }
	// }
	// else
	// {
	// mApkDownloadManager.startDownload( mAppInfo , 0 );
	// mAppProgress.setProgress( 100 );
	// }
	//
	// }
	// };

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public class TabsViewPagerAdapter extends FragmentPagerAdapter {

		private final ArrayList<Fragment> mList;

		public TabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			mList = list;
		}

		@Override
		public Fragment getItem(int position) {
			if (mList.size() > 0) {
				return mList.get(position);
			} else {
				return null;
			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return mList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(
							R.string.app_info_navigation_software_detail_tab_title)
							.toUpperCase(l);
				case 1:
					return getString(
							R.string.app_info_navigation_user_evaluate_tab_title)
							.toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void apkInstallFinish(DownloadTask task) {

		if (task == null || mAppInfo == null) {
			return;
		}
		if (task.appId == mAppInfo.appId) {
			Message msg = Message.obtain();
			msg.what = MSG_REFRESH_APP_DOWNLOAD_BTN;
			msg.obj = task;
			mUiHandler.sendMessage(msg);
		}

	}

	public void setmSoftWareDetailFragment(
			AppInfoSoftWareDetailFragment mSoftWareDetailFragment) {
		this.mSoftWareDetailFragment = mSoftWareDetailFragment;
	}

	public void setmUserEvaluateFragment(
			AppInfoUserEvaluateFragment mUserEvaluateFragment) {
		this.mUserEvaluateFragment = mUserEvaluateFragment;
	}
}
