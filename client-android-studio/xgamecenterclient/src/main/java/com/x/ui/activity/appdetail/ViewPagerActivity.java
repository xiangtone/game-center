package com.x.ui.activity.appdetail;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
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
import com.x.publics.model.PictureBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;

/**
 * @ClassName: ViewPagerActivity
 * @Desciption: 查看大图 viewPager
 
 * @Date: 2014-1-21 上午8:35:20
 */

public class ViewPagerActivity extends Activity {

	private ViewPager mPager;
	private ViewPagerAdapter mAdapter;
	private ArrayList<PictureBean> picList;
	private int index = 0;
	private String picUrl;
	private ImageView[] imageViews;
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏
		setContentView(R.layout.activity_viewpager);
		initViews();
	}

	private void initViews() {
		index = getIntent().getIntExtra("picIndex", 0);
		picList = getIntent().getParcelableArrayListExtra("picList");

		mPager = (ViewPager) findViewById(R.id.viewpager);
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);

		// 对imageviews进行填充
		imageViews = new ImageView[picList.size()];
		// 小图标
		for (int i = 0; i < picList.size(); i++) {
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// 设置每个小圆点距离左边的间距
			margin.setMargins(15, 0, 0, 0);
			imageView = new ImageView(this);
			// 设置每个小圆点的宽高
			imageView.setLayoutParams(new LayoutParams(15, 15));
			imageViews[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				imageViews[i].setBackgroundResource(R.drawable.ic_page_indicator_unfocused);
			} else {
				// 其他图片都设置未选中状态
				imageViews[i].setBackgroundResource(R.drawable.ic_page_indicator_focused);
			}
			group.addView(imageViews[i], margin);
		}

		mAdapter = new ViewPagerAdapter();
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new ViewPageChangeListener());
		// 设置当前显示的图片（下标值），默认从0开始...注意，这句话必须在mPager.setAdapter(mAdapter)之后才起作用
		mPager.setCurrentItem(index);
	}

	private final class ViewPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.ic_page_indicator_unfocused);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.ic_page_indicator_focused);
				}
			}

		}

	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return picList.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {

			View rootView = getLayoutInflater().inflate(R.layout.viewpage_item, null);
			final View loadingView = (RelativeLayout) rootView.findViewById(R.id.l_loading_rl);
			final ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview);
			View loadingPb = loadingView.findViewById(R.id.loading_progressbar);
			View loadingLogo = loadingView.findViewById(R.id.loading_logo);
			SkinConfigManager.getInstance().setViewBackground(loadingView.getContext(), loadingLogo,
					SkinConstan.LOADING_LOGO);
			SkinConfigManager.getInstance().setIndeterminateDrawable(loadingView.getContext(), (ProgressBar) loadingPb,
					SkinConstan.LOADING_PROGRASS_BAR);

			picUrl = picList.get(position).getUrl();

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.banner_default_picture).bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).cacheInMemory(true)
					.showImageForEmptyUri(R.drawable.banner_default_picture)
					.showImageOnFail(R.drawable.banner_default_picture).cacheOnDisc(true).considerExifParams(false)
					.displayer(new SimpleBitmapDisplayer()).build();

			ImageLoader.getInstance().displayImage(ViewPagerActivity.this, true, ImageType.NETWORK, picUrl, imageView,
					options, new ImageLoadingListener() {

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

			((ViewPager) collection).addView(rootView, 0);

			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 点击关闭当前activity
					ViewPagerActivity.this.finish();
				}
			});

			return rootView;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			try {
				ImageView iv = (ImageView) ((View) view).findViewById(R.id.imageview);
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

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		try {
			int count = mPager.getChildCount();
			for (int i = 0; i < count; i++) {
				ImageView iv = (ImageView) mPager.getChildAt(i).findViewById(R.id.imageview);
				NetworkImageUtils.cancleDisplay(iv, this);
				Utils.recycleImageView(iv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

}