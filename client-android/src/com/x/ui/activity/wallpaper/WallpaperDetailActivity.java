package com.x.ui.activity.wallpaper;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.x.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.wallpaper.WallpaperManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FileBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.Utils;
import com.x.ui.activity.resource.PhotoEditActivity;
import com.x.ui.view.MyViewPager;
import com.x.ui.view.photo.PhotoView;

/**
 * @ClassName: WallpaperDetailActivity
 * @Desciption: 壁纸详情、预览、设置
 
 * @Date: 2014-4-9 上午9:29:25
 */
public class WallpaperDetailActivity extends Activity implements OnClickListener {

	private int index = 0;
	private ViewPager mPager;
	private ViewPagerAdapter mAdapter;
	private WallpaperBean wallpaperBean;
	private ArrayList<WallpaperBean> mList;

	private String localPath; // 图片存储的本地路径
	private Activity mActivity = this; // 当前Activity对象
	private DownloadUiReceiver mDownloadUiReceiver;// 广播接收对象
	private LinearLayout settingView, editBtn, setAsWpBtn, shareBtn;
	private RelativeLayout downloadBtn, pauseBtn, waitingBtn, connectingBtn, continueBtn;
	private ProgressBar downloadPercent, continuePercent, connectingPercent, waitingPercent;
	private boolean isInit = true;
	WallpaperBean wb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper_detail);
		index = getIntent().getIntExtra("picIndex", 0);
		mList = getIntent().getParcelableArrayListExtra("data");
		initViews();
	}

	private void initViews() {
		wallpaperBean = mList.get(index);
		editBtn = (LinearLayout) findViewById(R.id.btn_wp_edit);
		shareBtn = (LinearLayout) findViewById(R.id.btn_wp_share);
		setAsWpBtn = (LinearLayout) findViewById(R.id.btn_set_as_wp);
		pauseBtn = (RelativeLayout) findViewById(R.id.btn_wp_pause);
		waitingBtn = (RelativeLayout) findViewById(R.id.btn_wp_waiting);
		downloadBtn = (RelativeLayout) findViewById(R.id.btn_wp_download);
		continueBtn = (RelativeLayout) findViewById(R.id.btn_wp_continue);
		connectingBtn = (RelativeLayout) findViewById(R.id.btn_wp_connecting);
		settingView = (LinearLayout) findViewById(R.id.btn_layout_wp_setting);
		downloadPercent = (ProgressBar) findViewById(R.id.pb_download_percent);
		connectingPercent = (ProgressBar) findViewById(R.id.pb_connecting_percent);
		continuePercent = (ProgressBar) findViewById(R.id.pb_continue_percent);
		waitingPercent = (ProgressBar) findViewById(R.id.pb_waiting_percent);

		mPager = (MyViewPager) findViewById(R.id.viewpager);
		mAdapter = new ViewPagerAdapter();
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new ViewPageChangeListener());
		// 设置当前显示的图片（下标值），默认从0开始...注意，这句话必须在mPager.setAdapter(mAdapter)之后才起作用
		mPager.setCurrentItem(index);

		editBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		setAsWpBtn.setOnClickListener(this);
		downloadBtn.setOnClickListener(this);
		pauseBtn.setOnClickListener(this);
		waitingBtn.setOnClickListener(this);
		continueBtn.setOnClickListener(this);
		connectingBtn.setOnClickListener(this);
		settingView.setOnClickListener(this);
	}

	/**
	 * 对比壁纸状态
	 * 
	 * @return void
	 */
	private void compareStatus() {
		wallpaperBean = mList.get(index);
		ArrayList<DownloadBean> list = DownloadEntityManager.getInstance().getAllWallpaperDownload();
		for (DownloadBean downloadBean : list) {
			// 取得Url
			if (downloadBean.getUrl() != null && downloadBean.getUrl().equals(wallpaperBean.getUrl())) {
				// 赋值=设置进度条的进度
				if (downloadBean.getTotalBytes() == 0) {
					downloadPercent.setProgress(0);
					continuePercent.setProgress(0);
					connectingPercent.setProgress(0);
					waitingPercent.setProgress(0);
				} else {
					int progress = (int) (downloadBean.getCurrentBytes() * 100 / downloadBean.getTotalBytes());
					downloadPercent.setProgress(progress);
					continuePercent.setProgress(progress);
					connectingPercent.setProgress(progress);
					waitingPercent.setProgress(progress);
				}

				// 判断已下载的文件是否已存在
				String localPathUrl = downloadBean.getLocalPath();
				if (localPathUrl != null) {
					File file = new File(localPathUrl);
					File tempFile = new File(localPathUrl + DownloadTask.TEMP_SUFFIX);
					if (!file.exists() && !tempFile.exists()) {
						downloadBean.setStatus(0);
					}
				}

				initStatus(downloadBean.getStatus());
				return;
			}
		}
		initStatus(0);
	}

	/**
	 * 初始化壁纸状态
	 * 
	 * @return void
	 */
	private void initStatus(int status) {
		switch (status) {

		// 下载完成
		case DownloadTask.TASK_FINISH:
			pauseBtn.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			waitingBtn.setVisibility(View.GONE);
			connectingBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			settingView.setVisibility(View.VISIBLE);
			break;

		// 连接中。。
		case DownloadTask.TASK_CONNECTING:
			pauseBtn.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			waitingBtn.setVisibility(View.GONE);
			connectingBtn.setVisibility(View.VISIBLE);
			downloadBtn.setVisibility(View.GONE);
			settingView.setVisibility(View.GONE);
			break;

		// 等待中。。
		case DownloadTask.TASK_WAITING:
			pauseBtn.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			waitingBtn.setVisibility(View.VISIBLE);
			connectingBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			settingView.setVisibility(View.GONE);
			break;

		// 下载中。。
		case DownloadTask.TASK_DOWNLOADING:
			pauseBtn.setVisibility(View.VISIBLE);
			continueBtn.setVisibility(View.GONE);
			waitingBtn.setVisibility(View.GONE);
			connectingBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			settingView.setVisibility(View.GONE);
			break;

		// 暂停状态
		case DownloadTask.TASK_PAUSE:
			pauseBtn.setVisibility(View.GONE);
			continueBtn.setVisibility(View.VISIBLE);
			connectingBtn.setVisibility(View.GONE);
			waitingBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.GONE);
			settingView.setVisibility(View.GONE);
			break;

		// 正常状态
		default:
			pauseBtn.setVisibility(View.GONE);
			continueBtn.setVisibility(View.GONE);
			waitingBtn.setVisibility(View.GONE);
			connectingBtn.setVisibility(View.GONE);
			downloadBtn.setVisibility(View.VISIBLE);
			settingView.setVisibility(View.GONE);
			break;

		}

	}

	/**
	 * 界面创建时，注册广播接收
	 */
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().onResume(this);
		if (wallpaperBean != null) {
			DataEyeManager.getInstance().module(wallpaperBean.getImageName(), true);
		}
		if (shareBtn != null) {
			shareBtn.setEnabled(true);
		}
		registUiReceiver();// <方法调用>注册广播接收者
		if ((wb != null && !isInit)) {
			DataEyeManager.getInstance().view(StatisticConstan.FileType.WALLPAPER, wb.getCategoryId(),
					wb.getImageName(), wb.getFileSize(), null, null);
		} else if (index == 0) {
			DataEyeManager.getInstance().view(StatisticConstan.FileType.WALLPAPER, wallpaperBean.getCategoryId(),
					wallpaperBean.getImageName(), wallpaperBean.getFileSize(), null, null);
		}
		isInit = false;
	}

	/**
	 * 注册广播接收者
	 */
	private void registUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
		LogUtil.getLogger().d("AppDetailActivity==================registUiReceiver");
	}

	/**
	 * @ClassName: DownloadUiReceiver
	 * @Desciption: 下载广播实现内部类
	 
	 * @Date: 2014-2-14 下午2:10:05
	 */
	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (wallpaperBean.getUrl() != null)
				handleIntent(intent);
		}

		private void handleIntent(Intent intent) {
			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String url = null;

				DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
						"" + wallpaperBean.getImageId());

				switch (type) {
				// 已删除状态，出现下载按钮，隐藏其他所有按钮
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {
						pauseBtn.setVisibility(View.GONE);
						continueBtn.setVisibility(View.GONE);
						connectingBtn.setVisibility(View.GONE);
						waitingBtn.setVisibility(View.GONE);
						downloadBtn.setVisibility(View.VISIBLE);
						settingView.setVisibility(View.GONE);
					}
					break;
				// 下载完成状态，出现设置navbar
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {
						pauseBtn.setVisibility(View.GONE);
						continueBtn.setVisibility(View.GONE);
						waitingBtn.setVisibility(View.GONE);
						connectingBtn.setVisibility(View.GONE);
						downloadBtn.setVisibility(View.GONE);
						settingView.setVisibility(View.VISIBLE);

					}
					break;
				// 准备下载状态，初始化操作
				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {
						pauseBtn.setVisibility(View.GONE);
						continueBtn.setVisibility(View.GONE);
						waitingBtn.setVisibility(View.GONE);
						connectingBtn.setVisibility(View.VISIBLE);
						downloadBtn.setVisibility(View.GONE);
						settingView.setVisibility(View.GONE);

						// 判断分母不能为零
						if (downloadBean.getTotalBytes() == 0) {
							downloadPercent.setProgress(0);
							continuePercent.setProgress(0);
							connectingPercent.setProgress(0);
							waitingPercent.setProgress(0);
							return;
						} else {
							int progress = (int) (downloadBean.getCurrentBytes() * 100 / downloadBean.getTotalBytes());
							downloadPercent.setProgress(progress);
							continuePercent.setProgress(progress);
							connectingPercent.setProgress(progress);
							waitingPercent.setProgress(progress);
						}

					}

					break;
				// 异常、暂停状态，出现继续按钮，隐藏暂停按钮
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {
						pauseBtn.setVisibility(View.GONE);
						continueBtn.setVisibility(View.VISIBLE);
						waitingBtn.setVisibility(View.GONE);
						connectingBtn.setVisibility(View.GONE);
						downloadBtn.setVisibility(View.GONE);
						settingView.setVisibility(View.GONE);
					}
					break;
				// 等待状态，显示等待按钮
				case MyIntents.Types.WAIT:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {
						pauseBtn.setVisibility(View.GONE);
						continueBtn.setVisibility(View.GONE);
						waitingBtn.setVisibility(View.VISIBLE);
						connectingBtn.setVisibility(View.GONE);
						downloadBtn.setVisibility(View.GONE);
						settingView.setVisibility(View.GONE);
					}
					break;
				// 下载中状态，实时更新下载进度条
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {
						pauseBtn.setVisibility(View.VISIBLE);
						continueBtn.setVisibility(View.GONE);
						waitingBtn.setVisibility(View.GONE);
						connectingBtn.setVisibility(View.GONE);
						downloadBtn.setVisibility(View.GONE);
						settingView.setVisibility(View.GONE);

						refreshData(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
								intent.getStringExtra(MyIntents.PROCESS_SPEED),
								Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));

					}
					break;
				}
			}
		}
	}

	/**
	 * 实时刷新下载进度条
	 * 
	 * @param prompt
	 *            已下载文件大小
	 * @param speed
	 *            下载速度
	 * @param progress
	 *            下载进度
	 */
	private void refreshData(String prompt, String speed, Integer progress) {
		if (progress != 0) {
			downloadPercent.setProgress(progress);
			continuePercent.setProgress(progress);
			connectingPercent.setProgress(progress);
			waitingPercent.setProgress(progress);
		}
	}

	private final class ViewPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			index = position;
			compareStatus();
			wb = mList.get(position);
			if (wb != null) {
				DataEyeManager.getInstance().view(StatisticConstan.FileType.WALLPAPER, wb.getCategoryId(),
						wb.getImageName(), wb.getFileSize(), null, null);
			}

		}
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object instantiateItem(View collection, final int position) {
			View rootView = getLayoutInflater().inflate(R.layout.wallpaper_viewpage_item, null);
			final View loadingView = (RelativeLayout) rootView.findViewById(R.id.l_loading_rl);
			final PhotoView imageView = (PhotoView) rootView.findViewById(R.id.imageView);
			View loadingPb = loadingView.findViewById(R.id.loading_progressbar);
			View loadingLogo = loadingView.findViewById(R.id.loading_logo);
			SkinConfigManager.getInstance().setViewBackground(loadingView.getContext(), loadingLogo,
					SkinConstan.LOADING_LOGO);
			SkinConfigManager.getInstance().setIndeterminateDrawable(loadingView.getContext(), (ProgressBar) loadingPb,
					SkinConstan.LOADING_PROGRASS_BAR);

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.banner_default_picture).bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).cacheInMemory(true)
					.showImageForEmptyUri(R.drawable.banner_default_picture)
					.showImageOnFail(R.drawable.banner_default_picture).cacheOnDisc(true).considerExifParams(false)
					.displayer(new SimpleBitmapDisplayer()).build();

			ImageLoader.getInstance().displayImage(mActivity, true, ImageType.NETWORK,
					mList.get(position).getBiglogo(), imageView, options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							loadingView.setVisibility(View.GONE);
							imageView.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});

			// 对比壁纸状态
			if (index == position) {
				compareStatus();
			}

			((ViewPager) collection).addView(rootView, 0);

			/*
			 * imageView.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View arg0) { Intent intent = new
			 * Intent(mActivity, WallpaperBigViewActivity.class);
			 * intent.putExtra("wallpaperBean", mList.get(position));
			 * intent.putExtra("localPath", getLocalPath());
			 * startActivity(intent); } });
			 */

			return rootView;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			try {
				ImageView iv = (ImageView) ((View) view).findViewById(R.id.imageView);
				Utils.recycleImageView(iv);
			} catch (Exception e) {
				e.printStackTrace();
			}
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/**
	 * 获取下载的本地图片路径
	 * 
	 * @return
	 */
	private String getLocalPath() {
		DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
				"" + wallpaperBean.getImageId());

		if (downloadBean != null && downloadBean.getStatus() == DownloadTask.TASK_FINISH) {
			localPath = downloadBean.getLocalPath();
			return localPath;
		} else {
			return null;
		}
	}

	/**
	 * @Title: isFileExists
	 * @Description: 判断本地文件是否存在
	 * @param @return
	 * @return boolean
	 */
	private boolean isFileExists() {
		File localFile = new File(getLocalPath());
		if (localFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 全局按钮事件处理
	 * 
	 * @return void
	 */
	public void onClick(View v) {

		switch (v.getId()) {

		// 下载按钮
		case R.id.btn_wp_download:
			WallpaperManage.getInstance(mActivity).addDownload(wallpaperBean, false);
			break;

		// 连接中按钮、等待中按钮
		case R.id.btn_wp_waiting:
		case R.id.btn_wp_connecting:
			DownloadManager.getInstance().pauseDownload(mActivity, wallpaperBean.getUrl());
			break;

		// 暂停按钮
		case R.id.btn_wp_pause:
			DownloadManager.getInstance().pauseDownload(mActivity, wallpaperBean.getUrl());
			break;

		// 继续按钮
		case R.id.btn_wp_continue:
			DownloadManager.getInstance().continueDownload(mActivity, wallpaperBean.getUrl());
			break;

		// 壁纸编辑
		case R.id.btn_wp_edit:
			if (getLocalPath() != null && isFileExists()) {
				Intent intent = new Intent(mActivity, PhotoEditActivity.class);
				FileBean fileBean = new FileBean();
				fileBean.setFilePath(localPath);
				intent.putExtra("data", fileBean);
				startActivity(intent);
			} else {
				WallpaperManage.getInstance(mActivity).addDownload(wallpaperBean, false);
			}
			break;

		// 设置壁纸
		case R.id.btn_set_as_wp:
			if (getLocalPath() != null && isFileExists()) {
				Utils.setWallpaper(mActivity, localPath);
			} else {
				WallpaperManage.getInstance(mActivity).addDownload(wallpaperBean, false);
			}
			break;

		// 壁纸分享
		case R.id.btn_wp_share:
			shareBtn.setEnabled(false);
			if (getLocalPath() != null && isFileExists()) {
				IntentUtil.shareFile(mActivity, localPath);
			} else {
				WallpaperManage.getInstance(mActivity).addDownload(wallpaperBean, false);
			}
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
		if (wallpaperBean != null) {
			DataEyeManager.getInstance().module(wallpaperBean.getImageName(), false);
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			int count = mPager.getChildCount();
			for (int i = 0; i < count; i++) {
				ImageView iv = (ImageView) mPager.getChildAt(i).findViewById(R.id.imageView);
				NetworkImageUtils.cancleDisplay(iv, this);
				Utils.recycleImageView(iv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 注销广播
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setProgressDrawable(mActivity, connectingPercent,
				SkinConstan.WP_DETAIL_DOWNLOAD_PROGRESS);
		SkinConfigManager.getInstance().setProgressDrawable(mActivity, downloadPercent,
				SkinConstan.WP_DETAIL_DOWNLOAD_PROGRESS);
	}
}