package com.x.ui.activity.wallpaper.backup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.wallpaper.WallpaperManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FileBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NativeImageLoader;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.Utils;
import com.x.publics.utils.NativeImageLoader.NativeImageCallBack;
import com.x.ui.activity.resource.PhotoEditActivity;
import com.x.ui.view.photo.PhotoView;

/**
 * @ClassName: BigTestActivity
 * @Desciption: 查看大图
 
 * @Date: 2014-4-9 下午5:56:00
 */
public class WallpaperBigViewActivity extends Activity implements OnClickListener {

	private View loadingView;
	private String localPath;
	private Activity mActivity;
	private PhotoView photoView;
	private TextView downloadPercent;
	private DownloadBean downloadBean;
	private WallpaperBean wallpaperBean;
	private View loadingPb, loadingLogo;
	private DownloadUiReceiver mDownloadUiReceiver;// 广播接收对象
	private LinearLayout settingView, editBtn, setAsWpBtn, shareBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper_bigview);
		mActivity = this;
		wallpaperBean = getIntent().getParcelableExtra("wallpaperBean");
		localPath = getIntent().getStringExtra("localPath");
		initView();
	}

	private void initView() {
		editBtn = (LinearLayout) findViewById(R.id.btn_wp_edit);
		shareBtn = (LinearLayout) findViewById(R.id.btn_wp_share);
		setAsWpBtn = (LinearLayout) findViewById(R.id.btn_set_as_wp);
		photoView = (PhotoView) findViewById(R.id.photoview);
		loadingView = (RelativeLayout) findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		settingView = (LinearLayout) findViewById(R.id.l_wp_setting);
		downloadPercent = (TextView) findViewById(R.id.tv_download_percent);

		editBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		setAsWpBtn.setOnClickListener(this);

		setParams();
	}

	private void setParams() {
		if (wallpaperBean != null) {
			NetworkImageUtils.load(this, ImageType.NETWORK, wallpaperBean.getBiglogo(),
					R.drawable.banner_default_picture, R.drawable.banner_default_picture, photoView);
		}

		if (localPath != null) {
			initImageView(localPath);
			settingView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
		} else {
			// 壁纸下载
			WallpaperManage.getInstance(this).addDownload(wallpaperBean, false);
		}
	}

	/**
	 * 界面创建时，注册广播接收
	 */
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().onResume(this);
		registUiReceiver();// <方法调用>注册广播接收者
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
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
				downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
						"" + wallpaperBean.getImageId());

				switch (type) {
				// 已删除状态，出现下载按钮，隐藏其他所有按钮
				case MyIntents.Types.DELETE:

					break;
				// 下载完成状态，出现设置navbar
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {

						localPath = downloadBean.getLocalPath();
						if (localPath != null) {
							initImageView(downloadBean.getLocalPath());
							settingView.setVisibility(View.VISIBLE);
							loadingView.setVisibility(View.GONE);
						}
					}
					break;
				// 准备下载状态，初始化操作
				case MyIntents.Types.PREDOWNLOAD:

					break;
				// 异常、暂停状态，出现继续按钮，隐藏暂停按钮
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:

					break;
				// 等待状态，显示等待按钮
				case MyIntents.Types.WAIT:

					break;
				// 下载中状态，实时更新下载进度条
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url) && wallpaperBean.getUrl().equals(url)) {

						refreshData(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
								intent.getStringExtra(MyIntents.PROCESS_SPEED),
								intent.getStringExtra(MyIntents.PROCESS_PROGRESS));

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
	private void refreshData(String prompt, String speed, String progress) {
		if (progress != null) {
			downloadPercent.setText(progress + "%");
		}
	}

	private void initImageView(String path) {
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, WallpaperBigViewActivity.this,
				new NativeImageCallBack() {

					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						if (bitmap != null) {
							photoView.setImageBitmap(bitmap);
						}
					}
				}, true);
		if (bitmap != null) {
			photoView.setImageBitmap(bitmap);
		} else {
			// photoView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// 编辑壁纸
		case R.id.btn_wp_edit:
			Intent intent = new Intent(mActivity, PhotoEditActivity.class);
			FileBean fileBean = new FileBean();
			fileBean.setFilePath(localPath);
			intent.putExtra("data", fileBean);
			startActivity(intent);
			break;

		// 设置壁纸
		case R.id.btn_set_as_wp:
			Utils.setWallpaper(mActivity, localPath);
			break;

		// 分享壁纸
		case R.id.btn_wp_share:
			IntentUtil.shareFile(mActivity, localPath);
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
