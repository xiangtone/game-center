package com.x.ui.activity.guide;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.utils.Utils;

public class ZeroDataGuideActivity extends Activity implements OnPageChangeListener {

	private ViewPager vp;
	private List<View> views;
	private ImageView[] dots;
	private int currentIndex;
	private ViewPagerAdapter vpAdapter;
	private int[] pics = { R.drawable.guide_tips_1, R.drawable.guide_tips_2, R.drawable.guide_tips_3 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zerodata_guide);
		views = new ArrayList<View>();

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayoutParams(mParams);
			iv.setImageResource(pics[i]);
			views.add(iv);
		}
		vp = (ViewPager) findViewById(R.id.zerodataGuideViewpager);
		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);
		initDots();

	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.zerodataGuideLl);
		dots = new ImageView[pics.length];
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(false);
			dots[i].setTag(i);
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(true);
	}

	private void setCurDot(int positon) {
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(true);
		dots[currentIndex].setEnabled(false);

		currentIndex = positon;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setCurDot(arg0);
	}

	class ViewPagerAdapter extends PagerAdapter {
		private List<View> views;

		public ViewPagerAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager) arg0).addView(views.get(arg1), 0);
			views.get(arg1).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ZeroDataGuideActivity.this.finish();
				}
			});
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub  

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub  
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		DataEyeManager.getInstance().onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
	}

	@Override
	protected void onDestroy() {
		try {
			int count = vp.getChildCount();
			for (int i = 0; i < count; i++) {
				ImageView iv = (ImageView) vp.getChildAt(i);
				Utils.recycleImageView(iv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

}
