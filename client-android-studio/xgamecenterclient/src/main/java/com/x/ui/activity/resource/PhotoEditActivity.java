package com.x.ui.activity.resource;

import java.io.File;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.NativeImageLoader;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ThreadUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.NativeImageLoader.NativeImageCallBack;
import com.x.ui.view.photo.crop.CropImageView;

/**
 * 
* @ClassName: PhotoEditActivity
* @Description: 图片编辑

* @date 2014-4-8 上午10:40:07
*
 */
public class PhotoEditActivity extends Activity implements OnClickListener {

	private Activity mActivity = null;
	private CropImageView imageView = null;
	private FileBean fileBean = null;
	private LinearLayout layout_edit, layout_save = null;
	private Button but_rotate, but_confirm = null;
	private Button but_share, but_wallpaper = null;
	private View loadingView, showView;
	private static final int ROTATE_DEGREES = 90;
	private Bitmap bitmap = null;
	private String path = null;
	private boolean isfinish = false; //是否完成修改
	private static final int SET_SHARE = 1; //分享
	private static final int SET_WALLPAPER = 2; //设置壁纸
	private int SET_MODE = 0;
	private View loadingPb, loadingLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //兼容2.3
		setContentView(R.layout.activity_photo_edit);
		mActivity = PhotoEditActivity.this;
		fileBean = (FileBean) getIntent().getParcelableExtra("data");
		path = StorageUtils.FILE_DOWNLOAD_WALLPAPER_PATH + System.currentTimeMillis() + ".png";
		initView();
	}

	private void initView() {
		showView = findViewById(R.id.ly_show_rl);
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		layout_edit = (LinearLayout) findViewById(R.id.piv_photo_edit);
		layout_save = (LinearLayout) findViewById(R.id.piv_photo_save);
		but_rotate = (Button) findViewById(R.id.pic_crop_rotate);
		but_rotate.setOnClickListener(this);
		but_confirm = (Button) findViewById(R.id.pic_crop_confirm);
		but_confirm.setOnClickListener(this);
		but_share = (Button) findViewById(R.id.pic_crop_share);
		but_share.setOnClickListener(this);
		but_wallpaper = (Button) findViewById(R.id.pic_crop_wallpaper);
		but_wallpaper.setOnClickListener(this);
		imageView = (CropImageView) findViewById(R.id.pic_crop_image);
		initImageView(fileBean.getFilePath());
		imageView.setCropOverlayVisibility(true);
	}

	private void initImageView(String path) {
		Bitmap bitmapss = NativeImageLoader.getInstance().loadNativeImage(path, PhotoEditActivity.this,
				new NativeImageCallBack() {

					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						if (bitmap != null) {
							imageView.setImageBitmap(bitmap);
							loadingView.setVisibility(View.GONE);
							showView.setVisibility(View.VISIBLE);
						}
					}
				}, true);
		if (bitmapss != null) {
			imageView.setImageBitmap(bitmapss);
			loadingView.setVisibility(View.GONE);
			showView.setVisibility(View.VISIBLE);
		} else {
			imageView.setImageResource(R.drawable.banner_default_picture);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		but_share.setEnabled(true);
		DataEyeManager.getInstance().onResume(this);
		DataEyeManager.getInstance().module(ModuleName.WALLPAPER_EDIT, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 图片旋转按钮	
		case R.id.pic_crop_rotate:
			bitmap = imageView.rotateImage(ROTATE_DEGREES);
			break;
		// 确定，完成编辑按钮
		case R.id.pic_crop_confirm:
			bitmap = imageView.getCroppedImage();
			imageView.setImageBitmap(bitmap);
			imageView.setCropOverlayVisibility(false);
			layout_edit.setVisibility(View.GONE);
			layout_save.setVisibility(View.VISIBLE);
			isfinish = true;
			break;
		// 图片分享按钮	
		case R.id.pic_crop_share:
			but_share.setEnabled(false);
			if (bitmap != null) {
				SET_MODE = SET_SHARE;
				saveBitmap(bitmap, true);
				isfinish = true;
			} else {
				IntentUtil.shareFile(PhotoEditActivity.this, fileBean.getFilePath());
			}
			break;

		// 设置壁纸按钮	
		case R.id.pic_crop_wallpaper:
			if (bitmap != null) {
				SET_MODE = SET_WALLPAPER;
				saveBitmap(bitmap, true);
				isfinish = true;
			} else {
				Utils.setWallpaper(getBaseContext(), fileBean.getFilePath());
			}
			break;
		}
	}

	private void onMessagr(final int handle) {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				loadingView.setVisibility(View.GONE);
				switch (handle) {
				case SET_WALLPAPER:
					Utils.setWallpaper(getBaseContext(), path);
					break;
				case SET_SHARE:
					IntentUtil.shareFile(PhotoEditActivity.this, path);
					break;
				}
			}
		});
	}

	/*
	 * 保存图片
	 */
	public void saveBitmap(final Bitmap mBitmap, final boolean msg) {
		if (!StorageUtils.isSDCardPresent()) {
			ToastUtil.show(getBaseContext(), getResources().getString(R.string.sdcard_not_found), Toast.LENGTH_SHORT);
			return;
		}
		loadingView.setVisibility(View.VISIBLE);
		final File f = new File(path);
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				try {
					StorageUtils.mkdir();
					if (f.exists()) {
						f.delete();
					}
					f.createNewFile();
				} catch (Exception e) {
					//					e.printStackTrace();
				}
				Utils.saveBitmapFile(mActivity, f, mBitmap);
				if (msg)
					onMessagr(SET_MODE);
			}
		});
	}

	/*
	 * 删除图片
	 */
	public void delBitmap() {
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				Utils.deletePicFile(mActivity, path);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (isfinish) {
			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					saveBitmap(bitmap, false);
					ToastUtil.show(getBaseContext(), getResources().getString(R.string.wallpaper_edit_save),
							Toast.LENGTH_SHORT);
					finish();
				}
			};
			DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					//delete
					delBitmap();
					if (bitmap != null && bitmap.isRecycled()) {
						bitmap.recycle();
					}
					bitmap = null;
					finish();
				}
			};
			Utils.showDialog(mActivity, ResourceUtil.getString(mActivity, R.string.warm_tips),
					ResourceUtil.getString(mActivity, R.string.wallpaper_edit_dia),
					ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
					ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
		DataEyeManager.getInstance().module(ModuleName.WALLPAPER_EDIT, false);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.slide_out_right, 0);
	}

	@Override
	protected void onDestroy() {
		if (imageView != null) {
			imageView.recycleImageView();
		}
		bitmap = null;
		super.onDestroy();
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
