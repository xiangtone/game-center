package com.x.ui.activity.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.NativeImageLoader;
import com.x.publics.utils.Utils;
import com.x.publics.utils.NativeImageLoader.NativeImageCallBack;
import com.x.ui.activity.resource.PhotoEditActivity;
import com.x.ui.view.photo.PhotoView;

/**
 * @ClassName: WallpaperOpenActivity.class
 * @Desciption: 打开壁纸，查看大图
 
 * @Date: 2014-4-30 下午4:21:10
 */
public class WallpaperOpenActivity extends Activity implements OnClickListener {

	private View showView;
	private View loadingView;
	private String localPath;
	private PhotoView photoView;
	private Context context = this;
	private View loadingPb, loadingLogo;
	private LinearLayout editBtn, setAsWpBtn, shareBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper_open);
		initView();
	}

	/**
	 * @desc:初始化UI
	 * @params: TODO
	 * @return: void
	 */
	private void initView() {
		// 获取图片路径
		localPath = getIntent().getStringExtra("localPath");

		photoView = (PhotoView) findViewById(R.id.photoview);
		showView = (RelativeLayout) findViewById(R.id.ly_wp_show_rl);
		loadingView = (RelativeLayout) findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		editBtn = (LinearLayout) findViewById(R.id.btn_wp_edit);
		setAsWpBtn = (LinearLayout) findViewById(R.id.btn_set_as_wp);
		shareBtn = (LinearLayout) findViewById(R.id.btn_wp_share);

		editBtn.setOnClickListener(this);
		setAsWpBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);

		loadImageBitmap(localPath);
	}

	/**
	 * @desc:加载图片资源
	 * @params: TODO
	 * @return: void
	 */
	private void loadImageBitmap(String path) {
		// TODO Auto-generated method stub
		Bitmap bitmapss = NativeImageLoader.getInstance().loadNativeImage(path, WallpaperOpenActivity.this,
				new NativeImageCallBack() {

					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						if (bitmap != null) {
							loadingView.setVisibility(View.GONE);
							showView.setVisibility(View.VISIBLE);
							photoView.setImageBitmap(bitmap);
						}
					}
				}, true);
		if (bitmapss != null) {
			loadingView.setVisibility(View.GONE);
			showView.setVisibility(View.VISIBLE);
			photoView.setImageBitmap(bitmapss);
		} else {
			photoView.setImageResource(R.drawable.banner_default_picture);
		}

	}

	/**
	 * @desc:全局按钮事件处理
	 * @params: TODO
	 * @return: void
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// 编辑按钮
		case R.id.btn_wp_edit:
			Intent intent = new Intent(this, PhotoEditActivity.class);
			FileBean fileBean = new FileBean();
			fileBean.setFilePath(localPath);
			intent.putExtra("data", fileBean);
			startActivity(intent);
			break;

		// 设置壁纸按钮
		case R.id.btn_set_as_wp:
			Utils.setWallpaper(this, localPath);
			break;

		// 分享按钮
		case R.id.btn_wp_share:
			IntentUtil.shareFile(this, localPath);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().onResume(this);
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
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
