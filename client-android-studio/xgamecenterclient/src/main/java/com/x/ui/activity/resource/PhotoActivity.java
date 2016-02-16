package com.x.ui.activity.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.model.FileBean;
import com.x.publics.utils.IntentUtil;
import com.x.publics.utils.NativeImageLoader;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ThreadUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.NativeImageLoader.NativeImageCallBack;
import com.x.ui.view.MyViewPager;
import com.x.ui.view.photo.PhotoView;

/**
 * 
 * @ClassName: PhotoActivity
 * @Description: 图片大图浏览
 
 * @date 2014-4-8 上午10:42:02
 * 
 */
public class PhotoActivity extends Activity implements OnClickListener {

	private ViewPager mViewPager;
	private View view = null;
	private View loadingView = null;
	private Button wallpaper = null;
	private ImageButton edit = null;
	private ImageButton share = null;
	private ArrayList<FileBean> list = new ArrayList<FileBean>();
	private int Item_ID = 0;
	private View loadingPb, loadingLogo;
	private Context context = this;

	// private Bitmap mBitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 兼容2.3
		setContentView(R.layout.activity_photo);
		Intent intent = getIntent();
		list = intent.getParcelableArrayListExtra("data");
		Item_ID = intent.getIntExtra("Item_id", Item_ID);
		initView();
	}

	private void initView() {
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		mViewPager = (MyViewPager) findViewById(R.id.photo_viewPager);
		mViewPager.setAdapter(new PhotoPagerAdapter(list, mViewPager));
		mViewPager.setCurrentItem(Item_ID);
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setOnPageChangeListener(pageChangeListener);
		view = (View) findViewById(R.id.pic_wallpaper_tools);
		edit = (ImageButton) view.findViewById(R.id.pic_edit);
		edit.setOnClickListener(this);
		share = (ImageButton) view.findViewById(R.id.pic_share);
		share.setOnClickListener(this);
		wallpaper = (Button) view.findViewById(R.id.pic_setwall);
		wallpaper.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().onResume(this);
		if (share != null) {
			share.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pic_edit:
			Intent intent = new Intent(PhotoActivity.this, PhotoEditActivity.class);
			intent.putExtra("data", list.get(Item_ID));
			startActivity(intent);
			if (Build.VERSION.SDK_INT >= 11) {
				overridePendingTransition(android.R.anim.fade_in, 0);
			}
			break;
		case R.id.pic_setwall:
			String path = list.get(Item_ID).getFilePath();
			Utils.setWallpaper(getBaseContext(), path);
			break;
		case R.id.pic_share:
			share.setEnabled(false);
			IntentUtil.shareFile(PhotoActivity.this, list.get(Item_ID).getFilePath());
			break;
		}
	}

	/*
	 * 保存图片
	 */
	public void saveBitmap(final Bitmap mBitmap, final String path) {
		if (!StorageUtils.isSDCardPresent()) {
			ToastUtil.show(getBaseContext(), getResources().getString(R.string.sdcard_not_found), Toast.LENGTH_SHORT);
			return;
		}
		loadingView.setVisibility(View.VISIBLE);
		String front = path.substring(0, path.lastIndexOf("."));
		String last = path.substring(path.lastIndexOf("."), path.length());
		final String mPath = front + "n" + last;
		final File f = new File(mPath);
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				try {
					if (!f.exists()) {
						f.createNewFile();
					}
				} catch (Exception e) {
					//					e.printStackTrace();
				}
				Utils.saveBitmapFile(PhotoActivity.this, f, mBitmap);
				onMessagr(mPath);
			}
		});
	}

	private void onMessagr(final String newPath) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				loadingView.setVisibility(View.GONE);
				Utils.setWallpaper(getBaseContext(), newPath);
			}
		});
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			Item_ID = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private class PhotoPagerAdapter extends PagerAdapter {
		// private Point mPoint = new Point(0, 0);
		private List<FileBean> list = null;
		private ViewPager mViewPager = null;

		public PhotoPagerAdapter(List<FileBean> list, ViewPager mViewPager) {
			this.list = list;
			this.mViewPager = mViewPager;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View rootView = getLayoutInflater().inflate(R.layout.activity_photo_item, null);
			final View loadingView = rootView.findViewById(R.id.l_loading_rl);
			final PhotoView photoView = (PhotoView) rootView.findViewById(R.id.photoview);
			View loadingPb = loadingView.findViewById(R.id.loading_progressbar);
			View loadingLogo = loadingView.findViewById(R.id.loading_logo);
			SkinConfigManager.getInstance().setViewBackground(loadingView.getContext(), loadingLogo,
					SkinConstan.LOADING_LOGO);
			SkinConfigManager.getInstance().setIndeterminateDrawable(loadingView.getContext(), (ProgressBar) loadingPb,
					SkinConstan.LOADING_PROGRASS_BAR);

			/*
			 * PhotoView photoView = new PhotoView(container.getContext());
			 * photoView.setOnMeasureListener(new OnMeasureListener() {
			 * 
			 * @Override public void onMeasureSize(int width, int height) {
			 * mPoint.set(width, height); } });
			 */
			String path = list.get(position).getFilePath();
			photoView.setTag(path);

			NativeImageCallBack imageCallBack = new NativeImageCallBack() {

				@Override
				public void onImageLoader(Bitmap bitmap, String path) {
					PhotoView mPhotoView = (PhotoView) mViewPager.findViewWithTag(path);
					if (bitmap != null && mPhotoView != null) {
						mPhotoView.setImageBitmap(bitmap);
						loadingView.setVisibility(View.GONE);
						photoView.setVisibility(View.VISIBLE);
					}
				}
			};

			Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, PhotoActivity.this, imageCallBack,
					true);
			if (bitmap != null) {
				photoView.setImageBitmap(bitmap);
				loadingView.setVisibility(View.GONE);
				photoView.setVisibility(View.VISIBLE);
			} else {
				//				photoView
				//						.setImageResource(R.drawable.banner_default_picture);
				//test
				NativeImageLoader.getInstance().loadNativeImage(path, PhotoActivity.this, imageCallBack, true);
			}

			// Now just add PhotoView to ViewPager and return it
			container.addView(rootView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			return rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			PhotoView photoView = (PhotoView) view.findViewById(R.id.photoview);
			Utils.recycleImageView(photoView);
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void finish() {
		super.finish();
		//释放Cache
		NativeImageLoader.getInstance().evictCacheHigh();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//释放Cache
		NativeImageLoader.getInstance().evictCacheHigh();
		try {
			int count = mViewPager.getChildCount();
			for (int i = 0; i < count; i++) {
				ImageView iv = (ImageView) mViewPager.getChildAt(i).findViewById(R.id.photoview);
				NetworkImageUtils.cancleDisplay(iv, this);
				Utils.recycleImageView(iv);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
