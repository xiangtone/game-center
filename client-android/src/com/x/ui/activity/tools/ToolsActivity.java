/**   
* @Title: ToolsActivity.java
* @Package com.mas.amineappstore.ui.activity
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-27 下午5:58:28
* @version V1.0   
*/

package com.x.ui.activity.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.ui.activity.apkman.ApkManagementActivity;
import com.x.ui.activity.applocker.AppLockerMainActivity;
import com.x.ui.activity.appman.UninstallAppsActivity;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.skin.SkinActivity;
import com.x.ui.activity.zerodata.ZeroDataShareActivity;
import com.zbar.lib.CaptureActivity;

/**
* @ClassName: ToolsActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-27 下午5:58:28
* 
*/

public class ToolsActivity extends BaseActivity implements OnClickListener {

	private TextView mTitleTv;
	private Context context = this;
	private ImageView mGobackIv, mSkinTips;
	private View mNavigationView, mTitleView, mTitlePendant;

	private static final int[] btns = new int[] { R.id.mh_navigate_ll, R.id.btn_uninstall, R.id.btn_qrcode_scanner,
			R.id.btn_wifi_share, R.id.btn_apk_manage, R.id.btn_app_lock, R.id.btn_skin };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);
		initUi();
	}

	private void initUi() {
		// Buttons events listener 
		for (int btn : btns) {
			findViewById(btn).setOnClickListener(this);
		}
		initNavigation();
		mSkinTips = (ImageView) findViewById(R.id.ac_tools_tips);
	}

	private void setSkintipsVisviable() {
		boolean visviable = !SkinConfigManager.getInstance().getSkinIsRead(context);
		mSkinTips.setVisibility(visviable ? View.VISIBLE : View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		SharedPrefsUtil.removeValue(context, "isFromScanQRCode");
		setSkintipsVisviable();
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
		//mSkinTip = (ImageView) findViewById(R.id.skin_new_tip);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.tools);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mh_navigate_ll:
			onBackPressed();
			break;

		case R.id.btn_uninstall:
			startActivity(new Intent(context, UninstallAppsActivity.class));
			break;

		case R.id.btn_qrcode_scanner:
			ZeroDataResourceHelper.saveFromActivity(context, ZeroDataConstant.ZERO_DATA_CLIENT_ACTIVITY_KEY,
					ToolsActivity.class.getName());
			startActivity(new Intent(context, CaptureActivity.class));
			break;

		case R.id.btn_wifi_share:
			startActivity(new Intent(context, ZeroDataShareActivity.class));
			break;

		case R.id.btn_apk_manage:
			startActivity(new Intent(context, ApkManagementActivity.class));
			break;

		case R.id.btn_app_lock:
			startActivity(new Intent(context, AppLockerMainActivity.class));
			break;

		case R.id.btn_skin:
			startActivity(new Intent(context, SkinActivity.class));
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
