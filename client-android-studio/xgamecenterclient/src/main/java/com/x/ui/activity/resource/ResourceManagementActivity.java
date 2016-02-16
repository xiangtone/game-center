package com.x.ui.activity.resource;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.db.DownloadEntityManager;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.model.FileBean;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.downloadman.DownloadingActivity;
import com.x.ui.activity.home.HomeActivity;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.activity.zerodata.ZeroDataShareActivity;
import com.x.ui.activity.zerodata.ZeroDataShareConfirmActivity;
import com.x.ui.view.TabPageIndicator;

/**
 * 资源管理
 * 
 
 *
 */
public class ResourceManagementActivity extends BaseActivity implements OnClickListener {

	private Context context = this;
	private List<String> titleList = new ArrayList<String>();
	private int mode = 0;
	private String MusicTips, PictruesTips, VideoTips, DocumentsTips;
	private String Installed; //temp
	private ViewPager mViewPager;
	private View shareView = null;
	private Button share, cancel = null;
	private CheckBox checkBox = null;
	private ResourceManagementPagerAdapter REManagementPagerAdapter;
	private TabPageIndicator indicator;
	private int pageIndex = NativeResourceConstant.APPS_FRAGMENT; //默认页
	private MusicFragment musicFragment;
	private PictureFolderFragment pictrueFragment;
	private VideoFragment videoFragment;
	private DocumentsFragment documentsFragment;
	private AppsFragment appsFragment;
	private ArrayList<FileBean> listFile = null;
	private SharedPreferences preferences = null;
	private Editor editor = null;

	private ImageView mGobackIv, downloadIv, searchIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;
	private LinearLayout searchLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resource_management);
		preferences = getSharedPreferences(ZeroDataConstant.ZERO_DATA_RESOURCE_PREFES, MODE_PRIVATE);
		editor = preferences.edit();
		Intent intent = getIntent();
		if (intent != null) {
			mode = getIntent().getIntExtra("MODE", NativeResourceConstant.DEF_MODE);
		}
		initData();
		initViewPager();
		clearZeroDataSharePrefs();
		initNavigation();
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
		mTitleTv.setText(R.string.page_my_contents);
		mNavigationView.setOnClickListener(this);

		searchLl = (LinearLayout) findViewById(R.id.mh_search_ll);
		searchLl.setOnClickListener(this);
		searchIv = (ImageView) findViewById(R.id.mh_search_icon_iv);
		downloadIv = (ImageView) findViewById(R.id.mh_download_icon_iv);
	}

	private void initViewPager() {
		REManagementPagerAdapter = new ResourceManagementPagerAdapter(getSupportFragmentManager(), titleList);

		mViewPager = (ViewPager) findViewById(R.id.re_content_pager);
		mViewPager.setAdapter(REManagementPagerAdapter);
		mViewPager.setOffscreenPageLimit(titleList.size());

		indicator = (TabPageIndicator) findViewById(R.id.re_indicator);
		indicator.setViewPager(mViewPager);
		if (mode == NativeResourceConstant.SHARE_MODE) {
			setTabTitle(R.string.page_share_title);
			indicator.setOnPageChangeListener(pageChangeListener);
		} else {
			setTabTitle(R.string.page_my_contents);
		}
	}

	private void initData() {
		shareView = findViewById(R.id.share_bot_tools);
		share = (Button) findViewById(R.id.share_bot_share);
		share.setText(getResources().getString(R.string.share_tools, "0"));
		share.setOnClickListener(this);
		cancel = (Button) findViewById(R.id.share_bot_cancel);
		cancel.setOnClickListener(this);
		checkBox = (CheckBox) findViewById(R.id.share_bot_all);
		checkBox.setOnClickListener(listener);

		showTitle();
		titleList.clear();
		if (mode == NativeResourceConstant.SHARE_MODE)
			titleList.add(Installed);
		titleList.add(PictruesTips);
		titleList.add(MusicTips);
		titleList.add(VideoTips);
		titleList.add(DocumentsTips);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		onShow(true);
		supportInvalidateOptionsMenu();
		if (mode == NativeResourceConstant.DEF_MODE) {
			//update Album
			if (pictrueFragment != null) {
				pictrueFragment.loadAlbumImage();
				pictrueFragment.setCheckAll(false, false);
			}
			//update music
			if (musicFragment != null) {
				musicFragment.loadMusicAdapter();
				musicFragment.setCheckAll(false);
			}
		}
		//判断是否显示下载图标
		boolean visible = (mode == NativeResourceConstant.DEF_MODE)
				&& !DownloadEntityManager.getInstance().getAllUnfinishedMediaDownloadList().isEmpty();
		if (visible == true) {
			searchLl.setVisibility(View.VISIBLE);
			searchIv.setVisibility(View.GONE);
			downloadIv.setVisibility(View.VISIBLE);
		} else {
			searchLl.setVisibility(View.GONE);
		}
	}

	public void setShareView(int visibility) {
		shareView.setVisibility(visibility);
		boolean isShow = visibility == View.VISIBLE ? true : false;
		if (appsFragment != null) { //根据dataEye崩溃日志添加非空判断，未知bug复现步骤
			appsFragment.setViewHeight(isShow);
		}
		pictrueFragment.setViewHeight(isShow);
		musicFragment.setViewHeight(isShow);
		videoFragment.setViewHeight(isShow);
		documentsFragment.setViewHeight(isShow);
	}

	public boolean getShareView() {
		return shareView.getVisibility() == View.GONE ? false : true;
	}

	private void showTitle() {
		if (mode == NativeResourceConstant.SHARE_MODE) {
			Installed = ResourceUtil.getString(this, R.string.resource_app);
		}
		PictruesTips = ResourceUtil.getString(this, R.string.resource_pic);
		MusicTips = ResourceUtil.getString(this, R.string.resource_music);
		VideoTips = ResourceUtil.getString(this, R.string.resource_video);
		DocumentsTips = ResourceUtil.getString(this, R.string.resource_document);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				pageIndex = NativeResourceConstant.APPS_FRAGMENT;
				if (appsFragment != null)
					checkBox.setChecked(appsFragment.getCheckStatus());
				break;
			case 1:
				pageIndex = NativeResourceConstant.PIC_FRAGMENT;
				if (pictrueFragment != null)//根据dataEye崩溃日志添加非空判断，未知bug复现步骤
					checkBox.setChecked(pictrueFragment.getCheckStatus());
				break;
			case 2:
				pageIndex = NativeResourceConstant.MUSIC_FRAGMENT;
				if (musicFragment != null)
					checkBox.setChecked(musicFragment.getCheckStatus());
				break;
			case 3:
				pageIndex = NativeResourceConstant.VIDEO_FRAGMENT;
				if (videoFragment != null)
					checkBox.setChecked(videoFragment.getCheckStatus());
				break;
			case 4:
				pageIndex = NativeResourceConstant.DOC_FRAGMENT;
				if (documentsFragment != null)
					checkBox.setChecked(documentsFragment.getCheckStatus());
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	/**
	 * 分享模式，刷新总数
	 */
	int music = 0;
	int pic = 0;
	int video = 0;
	int doc = 0;
	int apps = 0;

	public void refreshFeed(int fragment) {
		if (mode == NativeResourceConstant.SHARE_MODE) {
			switch (fragment) {
			case NativeResourceConstant.MUSIC_FRAGMENT:
				if (musicFragment != null) {//根据dataEye崩溃日志添加非空判断，未知bug复现步骤
					music = musicFragment.getCheckListSize();
					checkBox.setChecked(musicFragment.getCheckStatus());
				}
				break;
			case NativeResourceConstant.PIC_FRAGMENT:
				if (pictrueFragment != null) {
					pic = pictrueFragment.getCheckListSize();
					checkBox.setChecked(pictrueFragment.getCheckStatus());
				}
				break;
			case NativeResourceConstant.VIDEO_FRAGMENT:
				if (videoFragment != null) {
					video = videoFragment.getCheckListSize();
					checkBox.setChecked(videoFragment.getCheckStatus());
				}
				break;
			case NativeResourceConstant.DOC_FRAGMENT:
				if (documentsFragment != null) {
					doc = documentsFragment.getCheckListSize();
					checkBox.setChecked(documentsFragment.getCheckStatus());
				}
				break;
			case NativeResourceConstant.APPS_FRAGMENT:
				if (appsFragment != null) {
					apps = appsFragment.getCheckListSize();
					checkBox.setChecked(appsFragment.getCheckStatus());
				}
				break;
			}
			int all = music + pic + video + doc + apps;
			share.setText(getResources().getString(R.string.share_tools, all));
			if (all > 0) {
				setShareView(View.VISIBLE);
			} else {
				setShareView(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mh_navigate_ll://导航的回退
			onBackPressed();
			break;
		case R.id.mh_search_ll://点击下载图标
			Intent intent = new Intent(ResourceManagementActivity.this, DownloadingActivity.class);
			intent.putExtra("activity_name", this.getClass().getName());
			startActivity(intent);
			break;
		// send
		case R.id.share_bot_share:
			listFile = new ArrayList<FileBean>();
			StringBuilder builder = new StringBuilder();
			listFile.addAll(appsFragment.getCheckList());
			listFile.addAll(pictrueFragment.getCheckList());
			listFile.addAll(musicFragment.getCheckList());
			listFile.addAll(videoFragment.getCheckList());
			listFile.addAll(documentsFragment.getCheckList());
			if (listFile.size() > 0) {
				for (int i = 0; i < listFile.size(); i++) {
					String path = listFile.get(i).getFilePath();
					if (i == (listFile.size() - 1))
						builder.append(path);
					else
						builder.append(path + ",@,");
				}
				//save
				editor.putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, builder.toString());
				editor.commit();
				//记录状态
				ConnectHotspotManage.getInstance(this).saveNetworkState(this);
				ZeroDataResourceHelper.saveFromActivity(this, ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY,
						ZeroDataShareActivity.class.getName());
				this.startActivity(new Intent(this, ZeroDataShareConfirmActivity.class));
				finish();
			} else {
				ToastUtil.show(getBaseContext(), getResources().getString(R.string.share_toast), Toast.LENGTH_SHORT);
			}
			break;

		// cancel	
		case R.id.share_bot_cancel:
			setShareView(View.GONE);
			appsFragment.setCheckAll(false);
			pictrueFragment.setCheckAll(false, true);
			musicFragment.setCheckAll(false);
			videoFragment.setCheckAll(false);
			documentsFragment.setCheckAll(false);
			break;
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (pageIndex) {
			case NativeResourceConstant.APPS_FRAGMENT:
				appsFragment.setCheckAll(checkBox.isChecked());
				break;
			case NativeResourceConstant.MUSIC_FRAGMENT:
				musicFragment.setCheckAll(checkBox.isChecked());
				break;
			case NativeResourceConstant.PIC_FRAGMENT:
				pictrueFragment.setCheckAll(checkBox.isChecked(), false);
				break;
			case NativeResourceConstant.VIDEO_FRAGMENT:
				videoFragment.setCheckAll(checkBox.isChecked());
				break;
			case NativeResourceConstant.DOC_FRAGMENT:
				documentsFragment.setCheckAll(checkBox.isChecked());
				break;
			}
		}
	};

	/**
	 * FragmentPager  Adapter
	 
	 *
	 */
	public class ResourceManagementPagerAdapter extends FragmentPagerAdapter {

		List<String> titles;

		public ResourceManagementPagerAdapter(FragmentManager fm, List<String> titles) {
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
			Bundle bundle = new Bundle();
			bundle.putInt("MODE", mode);
			if (mode == NativeResourceConstant.SHARE_MODE) {
				switch (position) {
				case 0:
					fragment = AppsFragment.newInstance(bundle);
					appsFragment = (AppsFragment) fragment;
					break;
				case 1:
					fragment = PictureFolderFragment.newInstance(bundle);
					pictrueFragment = (PictureFolderFragment) fragment;
					break;
				case 2:
					fragment = MusicFragment.newInstance(bundle);
					musicFragment = (MusicFragment) fragment;
					break;
				case 3:
					fragment = VideoFragment.newInstance(bundle);
					videoFragment = (VideoFragment) fragment;
					break;
				case 4:
					fragment = DocumentsFragment.newInstance(bundle);
					documentsFragment = (DocumentsFragment) fragment;
					break;
				}
			} else {
				switch (position) {
				case 0:
					fragment = PictureFolderFragment.newInstance(bundle);
					pictrueFragment = (PictureFolderFragment) fragment;
					//					DataEyeManager.getInstance().module(ModuleName.RESOURCE_MANAGEMEN_PICTURE, true);
					break;
				case 1:
					fragment = MusicFragment.newInstance(bundle);
					musicFragment = (MusicFragment) fragment;
					break;
				case 2:
					fragment = VideoFragment.newInstance(bundle);
					videoFragment = (VideoFragment) fragment;
					break;
				case 3:
					fragment = DocumentsFragment.newInstance(bundle);
					documentsFragment = (DocumentsFragment) fragment;
					break;
				}
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		onShow(false);
	}

	private void onShow(boolean show) {
		if (mode == NativeResourceConstant.SHARE_MODE || mViewPager == null)
			return;
		int item = mViewPager.getCurrentItem();
		switch (item) {
		case 0:
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_PICTURE, show);
			break;
		case 1:
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_MUSIC, show);
			break;
		case 2:
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_VIDEO, show);
			break;
		case 3:
			DataEyeManager.getInstance().module(ModuleName.MYCONTENTS_DOCUMENT, show);
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		if (mode == NativeResourceConstant.DEF_MODE) {//从资源管理进入
			SharedPrefsUtil.putValue(this, "resFirst", false);
			String parentActivityN = getIntent().getStringExtra("activity_name");
			if (parentActivityN == null) {
				startActivity(new Intent(this, MainActivity.class));
			}
		} else if (mode == NativeResourceConstant.SHARE_MODE) {//从零流量分享进入
			SharedPrefsUtil.putValue(this, "shareFirst", false);
			exitZeroDataShare();
		} else {
			String parentActivityN = getIntent().getStringExtra("activity_name");
			if (parentActivityN == null) {
				startActivity(new Intent(this, MainActivity.class));
			}
		}
	}

	/**
	 * 清除上一次零流量分享的数据
	 */
	public void clearZeroDataSharePrefs() {
		ZeroDataResourceHelper.getInstance(this).prefers.edit().putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "")
				.commit();
	}

	/**
	 * 退出零流量分享
	 */
	public void exitZeroDataShare() {
		Utils.showDialog(this, ResourceUtil.getString(this, R.string.warm_tips),
				ResourceUtil.getString(this, R.string.dialog_cancle_zerodata_share),
				ResourceUtil.getString(this, R.string.confirm), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(ResourceManagementActivity.this, ZeroDataShareActivity.class));
						dialog.dismiss();
						ResourceManagementActivity.this.finish();
					};
				}, ResourceUtil.getString(this, R.string.cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, searchLl, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, share, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(context, checkBox, SkinConstan.OPTION_BTN);
	}

}
