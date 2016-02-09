/**   
 * @Title: CategoryDetailActivity.java
 * @Package com.mas.amineappstore.activity.base
 * @Description: TODO 
 
 * @date 2014-2-14 上午10:05:47
 * @version V1.0   
 */

package com.x.ui.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.publics.utils.ResourceUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.TabPageIndicator;

/**
 * @ClassName: CategoryDetailActivity
 * @Description: TODO
 
 * @date 2014-2-14 上午10:05:47
 * 
 */

public class CategoryDetailActivity extends BaseActivity implements View.OnClickListener {

	private Context context = this;
	public static ViewPager mViewPager;
	private CategoryDetailFragmentPagerAdapter ctDetailFragmentPagerAdapter;

	private int categoryId, ct;
	private String categoryName;
	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	public static void launch(Context context, int categoryId, String categoryName, int ct) {
		Intent intent = new Intent(context, CategoryDetailActivity.class);
		intent.putExtra("categoryId", categoryId);
		intent.putExtra("categoryName", categoryName);
		intent.putExtra("ct", ct);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_detail);
		initData();
		initUi();
	}

	private void initData() {
		Intent intent = getIntent();
		if (intent != null) {
			categoryId = intent.getIntExtra("categoryId", 0);
			categoryName = intent.getStringExtra("categoryName");
			ct = intent.getIntExtra("ct", 0);
		}
	}

	private void initUi() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);

		mTitleTv.setText((categoryName != null) ? categoryName : "");
		initViewPager();
	}

	private void initViewPager() {
		String[] titles = { ResourceUtil.getString(this, R.string.app_hot),
				ResourceUtil.getString(this, R.string.app_new) };
		ctDetailFragmentPagerAdapter = new CategoryDetailFragmentPagerAdapter(this.getSupportFragmentManager(), titles);

		mViewPager = (ViewPager) findViewById(R.id.fh_content_pager);
		mViewPager.setAdapter(ctDetailFragmentPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.fh_indicator);
		indicator.setViewPager(mViewPager);
	}

	public class CategoryDetailFragmentPagerAdapter extends FragmentPagerAdapter {

		String titles[];

		public CategoryDetailFragmentPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			Bundle b = new Bundle();
			b.putInt("categoryId", categoryId);
			b.putInt("ct", ct); // 传入ct值
			switch (position) {
			case 0:
				fragment = CategoryHotFragment.newInstance(b);
				break;
			case 1:
				fragment = CategoryNewFragment.newInstance(b);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
	}
}
