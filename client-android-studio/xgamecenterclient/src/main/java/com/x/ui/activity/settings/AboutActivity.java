package com.x.ui.activity.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.feedback.FeedbackManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeHelper;
import com.x.publics.download.upgrade.FxActivity;
import com.x.publics.download.upgrade.UpgradeManager;
import com.x.publics.http.Host;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ProgressDialogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.feedback.FeedbackActivity;

/**
 * @ClassName: AboutActivity
 * @Desciption: 关于我们
 
 * @Date: 2014-2-21 上午10:36:37
 */
public class AboutActivity extends BaseActivity implements OnClickListener {

	private Context context = this;
	private boolean isAutoCheck = true;
	private ImageView feedbackTipIv, mGobackIv;
	private TextView verName, website, mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant, logoView;
	private static final int[] btns = { R.id.rel_feedback, R.id.btn_check_new_version };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		Utils.addFinishAct(this);
		initViews();
		checkVersion();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		showFeedbackTips();
	}

	/**
	 * 初始化界面参数
	 */
	private void initViews() {
		logoView = findViewById(R.id.img_about_icon);
		feedbackTipIv = (ImageView) findViewById(R.id.feedback_tip_iv);
		website = (TextView) findViewById(R.id.tv_website);
		verName = (TextView) findViewById(R.id.tv_version_name);
		website.setText(getDebugHint());// 获取服务器提示
		verName.setText("V" + Utils.getVersionName(context)); // 获取版本号

		for (int btn : btns) {
			findViewById(btn).setOnClickListener(this);
		}

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
		mTitleTv.setText(R.string.page_about);
		mNavigationView.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: getDebugHint
	 * @Description: TODO(获取提示)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getDebugHint() {
		String serverHint = getServerHint();
		String dataMineHint = getDataMineHint();
		String debugHint = "";
		if (!TextUtils.isEmpty(serverHint)) {
			debugHint = getServerHint();
		}
		if (!TextUtils.isEmpty(dataMineHint)) {
			if (!TextUtils.isEmpty(serverHint)) {
				debugHint = debugHint + "\n" + dataMineHint;
			} else {
				debugHint = dataMineHint;
			}
		}
		return debugHint;
	}

	/**
	 * @Title: getServerHint
	 * @Description: TODO(获取服务器提示)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getServerHint() {
		String zappHost = Host.getHostHint();
		//		String sdkHost = Host.getSDKHostHint(this);
		//		if (!TextUtils.isEmpty(sdkHost)) {
		//			return sdkHost + "\n" + zappHost;
		//		}
		return zappHost;
	}

	public String getDataMineHint() {
		return DataEyeHelper.getDataeyeHint();
	}

	private void showFeedbackTips() {
		boolean isRead = FeedbackManager.getInstance().isFeedbackRead(context);
		boolean attention = FeedbackManager.getInstance().getFeedbackAttention(context);
		if (!isRead || attention) {
			feedbackTipIv.setVisibility(View.VISIBLE);
		} else {
			feedbackTipIv.setVisibility(View.GONE);
		}
	}

	/**
	 * @Title: checkVersion
	 * @Description: 检测新版本
	 * @param @return
	 * @return void
	 * @throws
	 */
	private void checkVersion() {
		if (isAutoCheck) {
			// 自动检测
			if (UpgradeManager.getInstence(context).isUpgrade()) {
				startActivity(new Intent(context, FxActivity.class));
			}
			return;

		} else {
			// 手动检测
			UpgradeManager.getInstence(context).checkVersion(UpgradeManager.START_ON_NOTIFICATION, true);
		}
	}

	/**
	 * 全局按钮处理事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {

		// feedback
		case R.id.rel_feedback:
			Intent intent = new Intent(AboutActivity.this, FeedbackActivity.class);
			startActivity(intent);
			break;

		// check new version
		case R.id.btn_check_new_version:
			if (!NetworkUtils.isNetworkAvailable(context)) {
				ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}
			ProgressDialogUtil.openProgressDialog(context, ResourceUtil.getString(context, R.string.checking));
			isAutoCheck = false; // 手动检测
			checkVersion();
			break;
		case R.id.mh_navigate_ll:
			onBackPressed();
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
		SkinConfigManager.getInstance().setViewBackground(context, logoView, SkinConstan.ABOUT_US_BG);
		for (int btn : btns) {
			SkinConfigManager.getInstance().setViewBackground(context, findViewById(btn), SkinConstan.ITEM_THEME_BG);
		}
	}
}
