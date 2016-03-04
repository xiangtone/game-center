/**
 * @Title: ApkManagerActivity.java
 * @Package com.x.activity
 * @Description: TODO

 * @date 2014-2-14 上午11:37:07
 * @version V1.0
 */

package com.x.ui.activity.apkman;

		import java.io.File;
		import java.util.ArrayList;
		import java.util.LinkedList;
		import java.util.List;
		import java.util.Queue;
		import java.util.Random;
		import java.util.Timer;
		import java.util.TimerTask;

		import android.annotation.SuppressLint;
		import android.content.BroadcastReceiver;
		import android.content.Context;
		import android.content.Intent;
		import android.content.IntentFilter;
		import android.content.pm.ApplicationInfo;
		import android.content.pm.PackageInfo;
		import android.content.pm.PackageManager;
		import android.os.Bundle;
		import android.os.Handler;
		import android.os.Message;
		import android.support.v4.app.Fragment;
		import android.support.v4.app.FragmentManager;
		import android.support.v4.app.FragmentPagerAdapter;
		import android.support.v4.view.PagerAdapter;
		import android.support.v4.view.ViewPager;
		import android.util.Log;
		import android.view.View;
		import android.view.View.OnClickListener;
		import android.view.ViewGroup;
		import android.widget.Button;
		import android.widget.FrameLayout;
		import android.widget.ImageView;
		import android.widget.ProgressBar;
		import android.widget.TextView;

		import com.x.R;
		import com.x.AmineApplication;
		import com.x.business.skin.SkinConfigManager;
		import com.x.business.skin.SkinConstan;
		import com.x.business.statistic.DataEyeManager;
		import com.x.business.statistic.StatisticConstan.ModuleName;
		import com.x.publics.download.BroadcastManager;
		import com.x.publics.model.ApkInfoBean;
		import com.x.publics.utils.MyIntents;
		import com.x.publics.utils.ResourceUtil;
		import com.x.publics.utils.StorageUtils;
		import com.x.ui.activity.base.BaseActivity;
		import com.x.ui.view.TabPageIndicator;

/**
 * @ClassName: ApkManagerActivity
 * @Description: TODO

 * @date 2014-2-14 上午11:37:07
 *
 */

public class ApkManagementActivity extends BaseActivity implements OnClickListener {

	private int searchNumber = 0;
	private int apkNumber = 0;
	private Thread searchThread;
	private volatile boolean isSearchDone = false;
	private boolean isSearchCancel = false;
	private String searchPath = "/sdcard";
	private String searchIngPath = "";
	private Context context;
	private List<String> titleList = new ArrayList<String>();
	private String installTips, unInstallTips;
	private ViewPager mViewPager;
	private TabPageIndicator indicator;
	private TextView scanTipsTv;
	private ImageView rescanBtn;
	public ApksPagerAdapter mApksPagerAdapter;
	private ArrayList<ApkInfoBean> apkInstallTempList, apkUninstallTempList;
	private boolean inited;
	private BroadcastReceiver deleteApkUiReceiver;
	private Timer timer;
	private Timer timer2;
	private Button lucencyBtn;

	private ProgressBar progress;
	private FrameLayout scanFra;
	private int totalProgress = 100, ascending, tempData, progress1, progress2, tempProgress;
	private boolean boo = true;
	private Timer timer3;
	private int[] mRand = new int[] { 1, 2, 3, 4, 5, 1, 2, 3, 2, 2 };

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant, loadingPb, loadingLogo;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_app_management);
		progress = (ProgressBar) findViewById(R.id.aam_scan_progress);
		progress.setProgress(0);
		progress.setMax(100);
		initNavigation();
		scanFra = (FrameLayout) findViewById(R.id.aam_scan_fra);
		lucencyBtn = (Button) findViewById(R.id.act_app_btn);
		findViewById(R.id.aam_apk_scan_rl).setVisibility(View.VISIBLE);
		scanTipsTv = (TextView) findViewById(R.id.aam_scan_apk_tips_tv);
		scanTipsTv.setVisibility(View.VISIBLE);
		scanTipsTv.setText(ResourceUtil.getString(context, R.string.apk_scan_tips, "0", "0MB"));
		rescanBtn = (ImageView) findViewById(R.id.aam_rescan_btn);
		rescanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				//重新扫描，数据置零
				progress2 = progress1 = tempData = tempProgress = 0;
				totalProgress = 100;
				progress.setProgress(0);
				progress.setVisibility(View.VISIBLE);
				scanFra.setVisibility(View.VISIBLE);

				uiHandler.sendEmptyMessage(1);
				scanApkFile();
			}
		});

		initViewPager();
		startScanFile();
	}

	/**
	 * @Title: initNavigation
	 * @Description: 初始化导航栏
	 * @param
	 * @return void
	 */
	private void initNavigation() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_apk_management);
		mNavigationView.setOnClickListener(this);
		loadingPb = findViewById(R.id.loading_prograssbar);
		loadingLogo = findViewById(R.id.loading_logo);
	}

	@Override
	public void onPause() {
		super.onPause();
		lucencyBtn.setVisibility(View.VISIBLE);
		DataEyeManager.getInstance().module(ModuleName.APK_MANAGEMENT, false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		lucencyBtn.setVisibility(View.GONE);
		if (!inited)
			registerBroadcastReceiver();
		DataEyeManager.getInstance().module(ModuleName.APK_MANAGEMENT, true);
	}

	/**
	 * @Title: initViewPager
	 * @Description: 初始化viewpager
	 * @param
	 * @return void
	 * @throws
	 */
	private void initViewPager() {
		installTips = ResourceUtil.getString(context, R.string.apk_installed, "0");
		unInstallTips = ResourceUtil.getString(context, R.string.apk_uninstalled, "0");
		titleList.clear();
		titleList.add(unInstallTips);
		titleList.add(installTips);
		mApksPagerAdapter = new ApksPagerAdapter(getSupportFragmentManager(), titleList);
		mViewPager = (ViewPager) findViewById(R.id.aam_content_pager);
		mViewPager.setAdapter(mApksPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		indicator = (TabPageIndicator) findViewById(R.id.aam_indicator);
		indicator.setViewPager(mViewPager);
		indicator.notifyDataSetChanged();
	}

	public class ApksPagerAdapter extends FragmentPagerAdapter {

		List<String> titles;

		public ApksPagerAdapter(FragmentManager fm, List<String> titles) {
			super(fm);
			this.titles = titles;
		}

		private void setTitles(List<String> titles) {
			this.titles = titles;
			indicator.notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
				case 0:
					fragment = ApkUninstalledFragment.newInstance(null);
					break;
				case 1:
					fragment = ApkInstalledFragment.newInstance(null);
					break;
			}
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (isSearchCancel)
				return;
			switch (msg.what) {
				case 0:
					if (!isSearchDone) {
						String tips = (String) msg.obj;
						scanTipsTv.setText(ResourceUtil.getString(context, R.string.apk_scanning, searchIngPath));
						Log.d("Scanning", tips);
					}
					break;
				case 1:
					AmineApplication.apkInstallFileList = new ArrayList<ApkInfoBean>(apkInstallTempList);
					AmineApplication.apkUninstallFileList = new ArrayList<ApkInfoBean>(apkUninstallTempList);
					installTips = ResourceUtil.getString(context, R.string.apk_installed, ""
							+ AmineApplication.apkInstallFileList.size());
					unInstallTips = ResourceUtil.getString(context, R.string.apk_uninstalled, ""
							+ AmineApplication.apkUninstallFileList.size());
					titleList.clear();
					titleList.add(unInstallTips);
					titleList.add(installTips);
					//				mApksPagerAdapter.notifyDataSetChanged();
					sendScanRefreshBroadcast();
					indicator.notifyDataSetChanged();
					break;
				case 2:
					AmineApplication.apkInstallFileList = new ArrayList<ApkInfoBean>(apkInstallTempList);
					AmineApplication.apkUninstallFileList = new ArrayList<ApkInfoBean>(apkUninstallTempList);
					installTips = ResourceUtil.getString(context, R.string.apk_installed, ""
							+ AmineApplication.apkInstallFileList.size());
					unInstallTips = ResourceUtil.getString(context, R.string.apk_uninstalled, ""
							+ AmineApplication.apkUninstallFileList.size());
					titleList.clear();
					titleList.add(unInstallTips);
					titleList.add(installTips);
					//				mApksPagerAdapter.notifyDataSetChanged();
					sendScanRefreshBroadcast();
					indicator.notifyDataSetChanged();

					int totalNum2 = AmineApplication.apkInstallFileList.size()
							+ AmineApplication.apkUninstallFileList.size();
					long totalSize2 = 0;
					for (ApkInfoBean apkInfoBean : AmineApplication.apkInstallFileList) {
						totalSize2 += apkInfoBean.getFileSize();
					}
					for (ApkInfoBean apkInfoBean : AmineApplication.apkUninstallFileList) {
						totalSize2 += apkInfoBean.getFileSize();
					}
					scanTipsTv.setText(ResourceUtil.getString(context, R.string.apk_scan_tips, "" + totalNum2, ""
							+ StorageUtils.size(totalSize2)));
					rescanBtn.setVisibility(View.VISIBLE);
					//扫描完毕，设置进度条充满
					scanFra.setVisibility(View.GONE);
					progress.setVisibility(View.GONE);
					timer.cancel();
					timer2.cancel();
					timer3.cancel();
					sendScanFinishBroadcast();
					break;
				case 3://更新进度条

					updateProgress();
					break;
				default:
					break;
			}
		};
	};

	/**
	 * @Title: startScanFile
	 * @Description: 扫描文件
	 * @param
	 * @return void
	 * @throws
	 */
	private void startScanFile() {
		scanApkFile();
	}

	private void scanApkFile() {

		searchThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					sendScanStartBroadcast();
					searchFile(searchPath);
				} catch (Exception e) {
					e.printStackTrace();
					isSearchDone = true;//不使用线程stop,因为其不是安全的函数
				}
			}
		});
		searchThread.start();
	}

	/**
	 * 搜索文件
	 * @param path
	 * @return
	 */
	private void searchFile(String path) {
		final Random rand = new Random();
		searchNumber = 0;
		apkNumber = 0;
		isSearchDone = false;
		isSearchCancel = false;
		AmineApplication.apkUninstallFileList = new ArrayList<ApkInfoBean>();
		AmineApplication.apkInstallFileList = new ArrayList<ApkInfoBean>();
		apkInstallTempList = new ArrayList<ApkInfoBean>();
		apkUninstallTempList = new ArrayList<ApkInfoBean>();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Message msg = uiHandler.obtainMessage(0);
				msg.obj = searchIngPath;
				uiHandler.sendMessage(msg);
			}
		}, 0, 50);
		timer2 = new Timer();
		timer2.schedule(new TimerTask() {

			@Override
			public void run() {
				uiHandler.sendEmptyMessage(1);
			}
		}, 0, 1000);
		timer3 = new Timer();
		timer3.schedule(new TimerTask() {

			@Override
			public void run() {
				ascending = mRand[rand.nextInt(10)];
				uiHandler.sendEmptyMessage(3);
			}
		}, 0, 500);
		final Queue<String> queue = new LinkedList<String>();
		queue.add(path);
		while (!queue.isEmpty() && !isSearchCancel) {

			File f = new File(queue.peek());
			File[] fList = f.listFiles();
			if (fList != null) {
				for (int i = 0; i < fList.length; i++) {
					if (isSearchCancel)
						return;
					if (fList[i].isDirectory()) {
						queue.add(fList[i].getPath());
					} else {
						searchIngPath = fList[i].getPath();
						String end = fList[i].getName()
								.substring(fList[i].getName().lastIndexOf(".") + 1, fList[i].getName().length())
								.toLowerCase();

						if (end.equalsIgnoreCase("apk")) {
							apkNumber++;
							ApkInfoBean apkInfoBean = new ApkInfoBean();
							apkInfoBean.setFileSize(fList[i].length());
							apkInfoBean.setApkPath(fList[i].getPath());
							try {
								makeApkInfo(fList[i].getPath(), context, apkInfoBean);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}
			}
			queue.poll();
		}
		isSearchDone = true; //不使用线程stop,因为其不是安全的函数
		uiHandler.sendEmptyMessage(2);
	}

	private void sendScanStartBroadcast() {
		Intent i = new Intent(MyIntents.INTENT_APK_SCAN_UPDATE_UI);
		i.putExtra(MyIntents.INTENT_EXTRA_APK_SCAN_STATUS, MyIntents.EXTRA_TYPE_APK_SCAN_START);
		BroadcastManager.sendBroadcast(i);
	}

	private void sendScanFinishBroadcast() {
		Intent i = new Intent(MyIntents.INTENT_APK_SCAN_UPDATE_UI);
		i.putExtra(MyIntents.INTENT_EXTRA_APK_SCAN_STATUS, MyIntents.EXTRA_TYPE_APK_SCAN_FINISH);
		BroadcastManager.sendBroadcast(i);
	}

	private void sendScanRefreshBroadcast() {
		Intent i = new Intent(MyIntents.INTENT_APK_SCAN_UPDATE_UI);
		i.putExtra(MyIntents.INTENT_EXTRA_APK_SCAN_STATUS, MyIntents.EXTRA_TYPE_APK_SCAN_REFRESH);
		BroadcastManager.sendBroadcast(i);
	}

	/**
	 * 获取apk包的信息：版本号，名称，图标等
	 * @param absPath apk包的绝对路径
	 * @param context
	 */
	public void makeApkInfo(String absPath, Context context, ApkInfoBean apkInfoBean) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
		if (pkgInfo != null) {
			ApplicationInfo appInfo = pkgInfo.applicationInfo;
			appInfo.sourceDir = absPath;
			appInfo.publicSourceDir = absPath;
			String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
			String packageName = appInfo.packageName; // 得到包名
			String version = pkgInfo.versionName; // 得到版本信息
			int versionCode = pkgInfo.versionCode;
			String pkgInfoStr = String.format("PackageName:%s, Vesion: %s,VersionCode: %s, AppName: %s", packageName,
					version, versionCode, appName);
			Log.i("apkInfo", String.format("PkgInfo: %s", pkgInfoStr));

			apkInfoBean.setAppName(appName);
			apkInfoBean.setPackageName(packageName);
			apkInfoBean.setVersionName(version);
			apkInfoBean.setVersionCode(versionCode);

			PackageManager mManager = context.getPackageManager();
			try {
				PackageInfo info = mManager.getPackageInfo(packageName, 0);
				int installedCode = info.versionCode;
				if (versionCode != installedCode) {
					apkInfoBean.setInstalled(false);
					apkUninstallTempList.add(apkInfoBean);
				} else {
					apkInfoBean.setInstalled(true);
					apkInstallTempList.add(apkInfoBean);
				}
			} catch (Exception e) {
				apkInfoBean.setInstalled(false);
				apkUninstallTempList.add(apkInfoBean);
			}
		}
	}

	private class DeleteApkUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			apkInstallTempList = new ArrayList<ApkInfoBean>(AmineApplication.apkInstallFileList);
			apkUninstallTempList = new ArrayList<ApkInfoBean>(AmineApplication.apkUninstallFileList);
			ArrayList<String> paths = intent.getStringArrayListExtra(MyIntents.INTENT_DELETE_EXTRA_PATH);
			int type = intent.getIntExtra(MyIntents.INTENT_DELETE_EXTRA_TYPE, -1);
			if (type != -1) {
				if (type == MyIntents.EXTRA_TYPE_INSTALL) {
					makeNewData(paths, apkInstallTempList);
				} else if (type == MyIntents.EXTRA_TYPE_UNINSTALL) {
					makeNewData(paths, apkUninstallTempList);
				} else if (type == MyIntents.EXTRA_TYPE_UNINSTALL_ALL) {
					apkUninstallTempList = new ArrayList<ApkInfoBean>();
				} else if (type == MyIntents.EXTRA_TYPE_INSTALL_ALL) {
					apkInstallTempList = new ArrayList<ApkInfoBean>();
				} else if (type == MyIntents.EXTRA_TYPE_UNINSTALL_TO_INSTALL) {
					makeNewData(paths, apkUninstallTempList, apkInstallTempList);
				}
				uiHandler.sendEmptyMessage(2);
			}

		}

	}

	/**
	 * @Title: makeNewData
	 * @Description: 重装数据
	 * @param @param paths
	 * @param @param list
	 * @return void
	 * @throws
	 */

	private void makeNewData(ArrayList<String> paths, ArrayList<ApkInfoBean> list) {
		for (String path : paths) {
			for (ApkInfoBean apkInfoBean : list) {
				if (apkInfoBean.getApkPath().equals(path)) {
					list.remove(apkInfoBean);
					return;
				}
			}
		}
	}

	/**
	 * @Title: makeNewData
	 * @Description: 重装数据
	 * @param @param paths
	 * @param @param list
	 * @param @param list2
	 * @return void
	 * @throws
	 */

	private void makeNewData(ArrayList<String> paths, ArrayList<ApkInfoBean> list, ArrayList<ApkInfoBean> list2) {
		for (String path : paths) {
			for (ApkInfoBean apkInfoBean : list) {
				if (apkInfoBean.getApkPath().equals(path)) {
					list.remove(apkInfoBean);
					apkInfoBean.setInstalled(true);
					list2.add(apkInfoBean);
					return;
				}
			}
		}
	}

	/**
	 * @Title: registerBroadcastReceiver
	 * @Description: 注册广播
	 * @param
	 * @return void
	 * @throws
	 */

	private void registerBroadcastReceiver() {
		deleteApkUiReceiver = new DeleteApkUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_APKFILE_DELETE_UPDATE_UI);
		BroadcastManager.registerReceiver(deleteApkUiReceiver, filter);
		inited = true;
	}

	/**
	 * @Title: unregisterReceiver
	 * @Description: 注销广播
	 * @param
	 * @return void
	 * @throws
	 */

	private void unregisterReceiver() {
		BroadcastManager.unregisterReceiver(deleteApkUiReceiver);
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		inited = false;
		unregisterReceiver();
		isSearchCancel = true;
		if (timer != null)
			timer.cancel();
		if (timer2 != null)
			timer2.cancel();
		if (timer3 != null)
			timer3.cancel();
	}

	/**
	 * @Title: updateProgress
	 * @Description: 更新进度条
	 * @param
	 * @return void
	 */
	private void updateProgress() {
		tempData = tempData + ascending;
		if (boo)
			progress1 = tempData * 100 / totalProgress;
		tempProgress = progress1;
		if ((totalProgress - tempData) < 30)
			totalProgress = totalProgress + 10;
		progress2 = tempData * 100 / totalProgress;
		if (progress2 < progress1) {
			boo = false;
		} else {
			boo = true;
		}
		if (isSearchDone == false) {
			if (progress2 < progress1) {//增大了分母后的比例，用回上次保存的比例，否则进度条后退
				if (boo == false) {
					progress.setProgress(tempProgress);
				} else {
					progress.setProgress(progress1);
				}
			} else {
				progress.setProgress(progress2);
			}
		} else {
			progress.setProgress(totalProgress * 100 / totalProgress);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
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
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
		SkinConfigManager.getInstance().setProgressDrawable(context, progress, SkinConstan.DOWNLOAD_PROGRESS_BG);
		SkinConfigManager.getInstance().setViewBackground(context, rescanBtn, SkinConstan.APK_REFRESH_BTN);
	}
}
