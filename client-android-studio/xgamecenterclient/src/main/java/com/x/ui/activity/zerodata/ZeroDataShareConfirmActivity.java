package com.x.ui.activity.zerodata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.connection.manager.CreatHotspotManager;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.db.LocalAppEntityManager;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.resource.ResourceManagementActivity;

public class ZeroDataShareConfirmActivity extends BaseActivity implements OnClickListener {

	private TextView swLinkTv,swTv;
	private TextView swPackageFileTv;
	private Context context;
	private String tips;
	private boolean isStartServerActivity = false;
	private boolean isOnclick = true;
	private String appDetailActivityUri;

	private TextView mTitleTv;
	private ImageView mGobackIv, packageLogo, stepOne, stepTwo;
	private View mNavigationView, mTitleView, mTitlePendant, btnNext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharing_way);
		setTabTitle(R.string.page_share_title);
		context = this;
		initUi();
		initNavigation();
		putExtraUri(this.getIntent());
		initData();
		ConnectHotspotManage.getInstance(context).saveNetworkState(context);
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
		mTitleTv.setText(R.string.page_share_title);
		mNavigationView.setOnClickListener(this);
	}

	/**
	 * 
	* @Title: putExtraUri 
	* @Description: TODO(处理外部资源) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void putExtraUri(Intent intent) {
		if (intent != null) {
			appDetailActivityUri = intent.getStringExtra("appDetailActivityUri");
			if (!TextUtils.isEmpty(appDetailActivityUri)) {
				procAppDetailActivityShare(intent);
			} else {
				ZeroDataResourceHelper.getInstance(this).putExtraUriData(intent);
			}
		}
	}

	public void procAppDetailActivityShare(Intent intent) {
		ZeroDataResourceHelper.getInstance(this).prefers.edit().putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "")
				.commit();
		if ("appDetailActivityUri".equals(appDetailActivityUri)) {
			Intent resourceintent = new Intent(context, ResourceManagementActivity.class);
			resourceintent.putExtra("MODE", NativeResourceConstant.SHARE_MODE);
			startActivity(resourceintent);
			finish();
		} else {
			InstallAppBean installAppBean = LocalAppEntityManager.getInstance().getLocalAppByPackageName(
					appDetailActivityUri);
			if (installAppBean != null) {
				ZeroDataResourceHelper.getInstance(this).prefers.edit()
						.putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, installAppBean.getSourceDir()).commit();
			}
		}
	}

	private void initUi() {
		packageLogo = (ImageView) findViewById(R.id.package_logo);
		stepOne = (ImageView) findViewById(R.id.step_one);
		stepTwo = (ImageView) findViewById(R.id.step_two);
		swLinkTv = (TextView) findViewById(R.id.sw_prompt_of_link_Tv);
		swTv = (TextView) findViewById(R.id.sw_prompt_Tv);
		btnNext = findViewById(R.id.btn_next);
		swPackageFileTv = (TextView) findViewById(R.id.sw_packageFile_tv);
		swLinkTv.setText(ResourceUtil.getString(context,R.string.sharing_prompt1_of_link,ResourceUtil.getString(context,R.string.app_name)));
		swTv.setText(ResourceUtil.getString(context,R.string.sharing_prompt1,ResourceUtil.getString(context,R.string.app_name)));
		btnNext.setOnClickListener(this);
		swLinkTv.setOnClickListener(this);
	}

	private void initData() {
		int files = ZeroDataResourceHelper.getInstance(this).getShareResFileCount();
		String filesSize = ZeroDataResourceHelper.getInstance(this).getShareResFileCountSize();

		if (files == 0 && TextUtils.isEmpty(appDetailActivityUri)) {
			Intent resourceintent = new Intent(context, ResourceManagementActivity.class);
			resourceintent.putExtra("MODE", NativeResourceConstant.SHARE_MODE);
			startActivity(resourceintent);
			finish();
		}

		tips = files + ResourceUtil.getString(context, R.string.share_files) + "(" + filesSize + ")"
				+ ResourceUtil.getString(context, R.string.share_file_desc);

		if (files <= 1) {

			tips = files + ResourceUtil.getString(context, R.string.share_file) + "(" + filesSize + ")"
					+ ResourceUtil.getString(context, R.string.share_file_desc);
		}

		swPackageFileTv.setText(tips);
		ZeroDataResourceHelper.getInstance(this).putServerTransferData();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.btn_next:
			btnNext.setEnabled(false);
			isStartServerActivity = true;
			Intent intent = new Intent(ZeroDataShareConfirmActivity.this, ZeroDataServerActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sw_prompt_of_link_Tv:
			swLinkTv.setEnabled(false);
			CreatHotspotManager.getInstance(this).closeOpenHotspot();
			ConnectHotspotManage.getInstance(this).resumeServerNetwork();
			Intent iuIntent = new Intent(ZeroDataShareConfirmActivity.this, InstallationUplayActivity.class);
			startActivity(iuIntent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		exitZeroDataShare();
	}

	/**
	 * 退出零流量分享
	 */
	public void exitZeroDataShare() {
		Utils.showDialog(this, ResourceUtil.getString(this, R.string.warm_tips),
				ResourceUtil.getString(this, R.string.dialog_cancle_zerodata_share),
				ResourceUtil.getString(this, R.string.confirm), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						clearZeroDataSharePrefs();
						isOnclick = true;
						isStartServerActivity = false;
						//						CreatHotspotManager.getInstance(ZeroDataShareConfirmActivity.this).closeZeroDataHotspot();
						String fromActivity = ZeroDataResourceHelper.getFromActivity(ZeroDataShareConfirmActivity.this,
								ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY);
						if (ZeroDataTransferHistoryActivity.class.getName().equals(fromActivity)) {
							// 跳转接受记录
							ZeroDataResourceHelper.saveFromActivity(ZeroDataShareConfirmActivity.this,
									ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY, "");
							startActivity(new Intent(context, ZeroDataTransferHistoryActivity.class));
						} else if (ZeroDataShareActivity.class.getName().equals(fromActivity)) {
							// 跳转资源管理
							ZeroDataResourceHelper.saveFromActivity(ZeroDataShareConfirmActivity.this,
									ZeroDataConstant.ZERO_DATA_SERVER_ACTIVITY_KEY, "");
							Intent resourceintent = new Intent(context, ResourceManagementActivity.class);
							resourceintent.putExtra("MODE", NativeResourceConstant.SHARE_MODE);
							startActivity(resourceintent);
						}
						//						ConnectHotspotManage.getInstance(ZeroDataShareConfirmActivity.this).resumeServerNetwork();
						dialog.dismiss();
						ZeroDataShareConfirmActivity.this.finish();
					};
				}, ResourceUtil.getString(this, R.string.cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						isOnclick = true;
					}
				});
	}

	/**
	 * 清除上一次零流量分享的数据
	 */
	public void clearZeroDataSharePrefs() {
		ZeroDataResourceHelper.getInstance(this).prefers.edit().putString(ZeroDataConstant.ZERO_DATA_RESOURCE_KEY, "")
				.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			if (isOnclick) {
				isOnclick = false;
				onBackPressed();
			} else {
				return false;
			}
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		isStartServerActivity = false;
		ConnectHotspotManage.getInstance(this).setMobileDataEnabled(false);
		CreatHotspotManager.getInstance(this).reCreateOpenHotspot();
		if (swLinkTv != null) {
			swLinkTv.setEnabled(true);
			btnNext.setEnabled(true);
		}
	}

	@Override
	protected void onStop() {

		if (!isStartServerActivity) {
			CreatHotspotManager.getInstance(this).closeOpenHotspot();
			ConnectHotspotManage.getInstance(this).resumeServerNetwork();
		}
		super.onStop();
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, btnNext, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, packageLogo, SkinConstan.SHARE_PACKAGE_BG);
		SkinConfigManager.getInstance().setViewBackground(context, stepOne, SkinConstan.SHARE_STEP_ONE_BG);
		SkinConfigManager.getInstance().setViewBackground(context, stepTwo, SkinConstan.SHARE_STEP_TWO_BG);
		SkinConfigManager.getInstance().setTextViewColor(context, swPackageFileTv, SkinConstan.THEME_TEXT_LINK);
		SkinConfigManager.getInstance().setTextViewColor(context, swLinkTv, SkinConstan.THEME_TEXT_LINK);
	}

}
