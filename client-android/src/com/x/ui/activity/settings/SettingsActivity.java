/**   
 * @Title: SettingsActivity.java
 * @Package com.x.activity
 * @Description: TODO 
 
 * @date 2014-2-17 上午09:34:51
 * @version V1.0   
 */

package com.x.ui.activity.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.x.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.favorite.FavoriteManage;
import com.x.business.feedback.FeedbackManager;
import com.x.business.settings.SettingModel;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.update.UpdateManage;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.upgrade.UpgradeManager;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.RootUtil;
import com.x.publics.utils.SettingsUtils;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;

/**
 * @ClassName: SettingsActivity
 * @Description: TODO
 
 * @date 2014-2-17 上午09:34:51
 * 
 */

public class SettingsActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {

	private Context context = this;
	private ToggleButton gprsSavingModeTb, downloadPromptTb, onlyWifiDownloadTb, installAppTb, silentInstallAppTb, /*deleteLocalFileTb,*/
	autoDownloadFavTb, autoDownloadUpdateTb, /*floatingTb,*/soundTb;
	private View clearCacheView, appStoreView, silentInstallAppRl, aboutView;
	private TextView cacheSizeTv;
	private SettingModel settingModel;
	private long cacheSize;
	private TextView ficheModeBtn, listModeBtn;
	private boolean isFicheModeBtn = false;
	private boolean isListModeBtn = false;
	private ImageView countryIcon;

	private TextView mTitleTv;
	private ImageView mTipsIv, mGobackIv;
	private View mNavigationView, mTitleView, mTitlePendant;
	private static final int[] btns = { R.id.as_image_no_download_tb, R.id.as_download_prompt_tb,
			R.id.as_download_only_wifi_tb, R.id.as_auto_download_update_tb, R.id.as_auto_download_fav_tb,
			R.id.as_sound_effect_tb, R.id.as_install_tb, R.id.as_silent_install_tb };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initUi();
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
		mTitleTv.setText(R.string.page_settings);
		mNavigationView.setOnClickListener(this);
	}

	private void initUi() {
		gprsSavingModeTb = (ToggleButton) findViewById(R.id.as_image_no_download_tb);
		downloadPromptTb = (ToggleButton) findViewById(R.id.as_download_prompt_tb);
		onlyWifiDownloadTb = (ToggleButton) findViewById(R.id.as_download_only_wifi_tb);
		installAppTb = (ToggleButton) findViewById(R.id.as_install_tb);
		silentInstallAppRl = findViewById(R.id.as_silent_install_rl);
		silentInstallAppTb = (ToggleButton) findViewById(R.id.as_silent_install_tb);
		//		deleteLocalFileTb = (ToggleButton) findViewById(R.id.as_delete_local_file_tb);
		autoDownloadFavTb = (ToggleButton) findViewById(R.id.as_auto_download_fav_tb);
		autoDownloadUpdateTb = (ToggleButton) findViewById(R.id.as_auto_download_update_tb);
		//		floatingTb = (ToggleButton) findViewById(R.id.as_floating_tb);
		soundTb = (ToggleButton) findViewById(R.id.as_sound_effect_tb);

		clearCacheView = findViewById(R.id.as_clear_cache_rl);
		cacheSizeTv = (TextView) findViewById(R.id.as_cache_size_tv);

		appStoreView = findViewById(R.id.as_app_store_rl);

		listModeBtn = (TextView) findViewById(R.id.btn_list_mode);
		ficheModeBtn = (TextView) findViewById(R.id.btn_fiche_mode);

		countryIcon = (ImageView) findViewById(R.id.img_country_icon);
		aboutView = findViewById(R.id.as_apps_about_ll);
		mTipsIv = (ImageView) findViewById(R.id.as_app_about_tips_iv);
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (cacheSize == 0)
				cacheSizeTv.setText("Cache: 0M");
			else
				cacheSizeTv.setText("Cache:" + StorageUtils.size(cacheSize));
		};
	};

	private void getCacheSize() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				cacheSize = StorageUtils.getCacheFileSize(context);
				uiHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		settingModel = Utils.getSettingModel(context);
		gprsSavingModeTb.setChecked(settingModel.isGprsSavingMode());
		downloadPromptTb.setChecked(settingModel.isGprsDownloadPromt());
		onlyWifiDownloadTb.setChecked(settingModel.isOnlyWifiDownload());
		installAppTb.setChecked(settingModel.isAutoInstall());
		//		deleteLocalFileTb.setChecked(settingModel.isDeleteApkFile());
		autoDownloadFavTb.setChecked(settingModel.isAutoDownloadFavInWifi());
		autoDownloadUpdateTb.setChecked(settingModel.isAutoDownloadUpdateInWifi());
		//		floatingTb.setChecked(settingModel.isFloatMode());
		soundTb.setChecked(settingModel.isSoundEffect());

		// 判断当前的显示模式
		if (settingModel.isFicheMode()) {
			ficheBtnChecked();
			isFicheModeBtn = true;
		} else {
			listBtnChecked();
			isListModeBtn = true;
		}

		setOnCheckedChangeListener();

		getCacheSize();
		showAboutTips();
		if (RootUtil.checkRootPermission()) {
			findViewById(R.id.as_silent_install_divider).setVisibility(View.VISIBLE);
			silentInstallAppRl.setVisibility(View.VISIBLE);
			SystemClock.sleep(500); //睡眠500ms，避免授权成功但还没检测到zapp文件。
		} else {
			findViewById(R.id.as_silent_install_divider).setVisibility(View.GONE);
			silentInstallAppRl.setVisibility(View.GONE);
		}

		silentInstallAppTb.setChecked(settingModel.isSilentInstall() && RootUtil.isAppRootPermission());
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		if (countryIcon != null) {
			String iconUrl = SharedPrefsUtil.getValue(context, "COUNTRY_URL", "");
			if ("".equals(iconUrl.trim())) {
				return;
			}
			NetworkImageUtils.load(context, ImageType.NETWORK, iconUrl, R.drawable.ic_screen_default_picture,
					R.drawable.ic_screen_default_picture, countryIcon);
		}
	}

	private void showAboutTips() {
		boolean isRead = FeedbackManager.getInstance().isFeedbackRead(context);
		boolean attention = FeedbackManager.getInstance().getFeedbackAttention(context);
		boolean isZappUpdate = UpgradeManager.getInstence(context).isUpgrade();
		if (!isRead || attention || isZappUpdate) {
			mTipsIv.setVisibility(View.VISIBLE);
		} else {
			mTipsIv.setVisibility(View.GONE);
		}

	}

	private void setOnCheckedChangeListener() {
		gprsSavingModeTb.setOnCheckedChangeListener(this);
		downloadPromptTb.setOnCheckedChangeListener(this);
		onlyWifiDownloadTb.setOnCheckedChangeListener(this);
		installAppTb.setOnCheckedChangeListener(this);
		silentInstallAppTb.setOnCheckedChangeListener(this);
		//		deleteLocalFileTb.setOnCheckedChangeListener(this);
		autoDownloadFavTb.setOnCheckedChangeListener(this);
		autoDownloadUpdateTb.setOnCheckedChangeListener(this);
		//		floatingTb.setOnCheckedChangeListener(this);
		soundTb.setOnCheckedChangeListener(this);

		clearCacheView.setOnClickListener(this);
		appStoreView.setOnClickListener(this);

		listModeBtn.setOnClickListener(this);
		ficheModeBtn.setOnClickListener(this);
		aboutView.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.as_image_no_download_tb:
			SettingsUtils.putValue(context, SettingsUtils.GPRS_SAVING_MODE, isChecked);
			if (isChecked) {
				if (!NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI))
					ImageLoader.getInstance().pause();
			} else
				ImageLoader.getInstance().resume();
			break;
		case R.id.as_download_prompt_tb:
			SettingsUtils.putValue(context, SettingsUtils.GPRS_DOWNLOAD_PROMT, isChecked);
			break;
		case R.id.as_download_only_wifi_tb:
			SettingsUtils.putValue(context, SettingsUtils.ONLY_WIFI_DOWNLOAD, isChecked);
			if (isChecked && !NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI))
				DownloadManager.getInstance().pauseAllDownload(context);
			break;
		case R.id.as_install_tb:
			SettingsUtils.putValue(context, SettingsUtils.To_INSTALL, isChecked);
			break;
		case R.id.as_silent_install_tb:
			if (isChecked) {
				if (!RootUtil.isAppRootPermission()) {
					rootAuthorize();
				}
				DataEyeManager.getInstance().module(ModuleName.SILENT_INSTALL_OPEN, true);
				DataEyeManager.getInstance().module(ModuleName.SILENT_INSTALL_OPEN, false);
			} else {
				DataEyeManager.getInstance().module(ModuleName.SILENT_INSTALL_CLOSE, true);
				DataEyeManager.getInstance().module(ModuleName.SILENT_INSTALL_CLOSE, false);
			}

			SettingsUtils.putValue(context, SettingsUtils.SILENT_INSTALL, isChecked);
			break;
		/*case R.id.as_delete_local_file_tb:
			SettingsUtils.putValue(context, SettingsUtils.DELETE_APK_FILE, isChecked);
			break;*/
		case R.id.as_auto_download_fav_tb:
			SettingsUtils.putValue(context, SettingsUtils.AUTO_DOWNLOAD_FAV, isChecked);
			FavoriteManage.getInstance(this).onClickAutoWifiDownload();
			break;
		case R.id.as_auto_download_update_tb:
			SettingsUtils.putValue(context, SettingsUtils.AUTO_DOWNLOAD_UPDATE, isChecked);
			UpdateManage.getInstance(this).onClickAutoWifiDownload();
			break;
		/*case R.id.as_floating_tb:
			SettingsUtils.putValue(context, SettingsUtils.FLOAT_SETTING, isChecked);
			Intent intent = new Intent(SettingsActivity.this, FloatService.class);
			if(isChecked){
				getApplicationContext().startService(intent);
			}else{
				getApplicationContext().stopService(intent);
			}
			break;*/
		case R.id.as_sound_effect_tb:
			SettingsUtils.putValue(context, SettingsUtils.SOUND_SETTING, isChecked);
			break;
		default:
			break;
		}
	}

	ProgressDialog progressDialog = null;

	private void rootAuthorize() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(ResourceUtil.getString(context, R.string.su_authorize));
		Utils.executeAsyncTask(new AuthorizeTask());
	}

	private class AuthorizeTask extends AsyncTask<Void, Void, Boolean> {

		protected void onPreExecute() {
			progressDialog.show();
		};

		@Override
		protected Boolean doInBackground(Void... params) {
			return RootUtil.preparezlsu(context);
		};

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}

			if (!result) {
				SettingsActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						silentInstallAppTb.setChecked(false);
						ToastUtil.show(context, ResourceUtil.getString(context, R.string.su_authorize_failed),
								Toast.LENGTH_LONG);
					}
				});
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.as_clear_cache_rl:

			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					StorageUtils.clearAppCache(context);
					cacheSizeTv.setText("cache: 0M");
				}
			};

			DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
			Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips),
					ResourceUtil.getString(context, R.string.dialog_delete_cache),
					ResourceUtil.getString(context, R.string.confirm), positiveListener,
					ResourceUtil.getString(context, R.string.cancel), negativeListener);
			break;

		// 列表模式
		case R.id.btn_list_mode:
			if (isListModeBtn) {
				return;
			}
			isListModeBtn = true;
			isFicheModeBtn = false;
			listBtnChecked();
			SettingsUtils.putValue(context, SettingsUtils.IS_FICHE_MODE, false);
			ToastUtil.show(context, ResourceUtil.getString(context, R.string.list_mode_tips), Toast.LENGTH_SHORT);
			break;

		// 卡片模式
		case R.id.btn_fiche_mode:
			if (isFicheModeBtn) {
				return;
			}
			isListModeBtn = false;
			isFicheModeBtn = true;
			ficheBtnChecked();
			SettingsUtils.putValue(context, SettingsUtils.IS_FICHE_MODE, true);
			ToastUtil.show(context, ResourceUtil.getString(context, R.string.fiche_mode_tips), Toast.LENGTH_SHORT);
			break;

		// 多国家选择
		case R.id.as_app_store_rl:
			// check network
			if (!NetworkUtils.isNetworkAvailable(context)) {
				ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}
			startActivity(new Intent(context, CountryActivity.class));
			break;
		case R.id.as_apps_about_ll:
			this.startActivity(new Intent(this, AboutActivity.class));
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
		for (int btn : btns) {
			SkinConfigManager.getInstance().setViewBackground(context, findViewById(btn),
					SkinConstan.SETTINGS_TOGGLE_BTN);
		}
		SkinConfigManager.getInstance().setViewBackground(context, appStoreView, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, clearCacheView, SkinConstan.ITEM_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, aboutView,
				SkinConstan.SETTINGS_ITEM_BOTTOM_CORNER_BG);
	}

	private void listBtnChecked() {
		SkinConfigManager.getInstance().setViewBackground(context, ficheModeBtn, SkinConstan.BTN_UNCHECK_BG);
		SkinConfigManager.getInstance().setViewBackground(context, listModeBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}

	private void ficheBtnChecked() {
		SkinConfigManager.getInstance().setViewBackground(context, listModeBtn, SkinConstan.BTN_UNCHECK_BG);
		SkinConfigManager.getInstance().setViewBackground(context, ficheModeBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}
}
