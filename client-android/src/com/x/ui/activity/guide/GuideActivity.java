package com.x.ui.activity.guide;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.publics.utils.Utils;
import com.x.ui.activity.home.MainActivity;

public class GuideActivity extends Activity implements OnPageChangeListener {
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	//底部小店图片  
	private ImageView[] dots;
	//记录当前选中位置  
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//防止按opne时候出现的页面显示混乱和退步出去的问题
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		setContentView(R.layout.guide_splash);

		init();
	}

	//新手引导部分
	class ViewPagerAdapter extends PagerAdapter {
		//界面列表
		private List<View> views;

		public ViewPagerAdapter(List<View> views) {
			this.views = views;
		}

		//销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		//获得当前界面数
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		//初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager) arg0).addView(views.get(arg1), 0);
			if (arg1 == views.size() - 1) {
				final TextView startNow = (TextView) arg0.findViewById(R.id.guideHomePageTv);
				startNow.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startNow.setEnabled(false);
						Intent mainIntent = new Intent(GuideActivity.this, MainActivity.class);
						startActivity(mainIntent);
						GuideActivity.this.finish();
					}
				});
			}
			return views.get(arg1);
		}

		//判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

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

	/**
	 *初始化ViewPager
	 */
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.guide_homepage1, null));
		views.add(inflater.inflate(R.layout.guide_homepage2, null));
		views.add(inflater.inflate(R.layout.guide_homepage3, null));
		vp = (ViewPager) findViewById(R.id.guideViewpager);
		vpAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);
		//初始化底部小点  
		initDots();
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.homePageGuideLl);
		dots = new ImageView[views.size()];
		//循环取得小点图片  
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);//都设为灰色  
			dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应  
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(false);//设置为白色，即选中状态  
	}

	/** 
	*这只当前引导小点的选中  
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > views.size() - 1 || currentIndex == positon) {
			return;
		}
		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	/**
	 * 屏蔽返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
		super.onDestroy();
		if (views != null) { //图片资源回收
			for (int i = 0; i < views.size(); i++) {
				View v = views.get(i);
				Utils.recycleBackgroundResource(v);
			}
		}
	}
}
