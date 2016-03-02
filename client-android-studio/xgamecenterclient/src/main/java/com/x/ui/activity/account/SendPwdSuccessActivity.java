package com.x.ui.activity.account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.account.AccountHelper;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.utils.ResourceUtil;
import com.x.ui.activity.base.BaseActivity;

public class SendPwdSuccessActivity extends BaseActivity implements OnClickListener {

	/* 按钮、文本 */
	private Context context = this;
	private TextView tvbtn, tvOpenBrowser, tvTitle, tvMsg;
	private String userName;
	private int fromCase;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	/**
	 * 程序主入口
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_pwd_success);
		initViews();
		userName = getIntent().getStringExtra("userName");
		fromCase = getIntent().getIntExtra("fromCase", 0);
		initData();
		AccountHelper.finishAct();
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		tvbtn = (TextView) findViewById(R.id.send_pwd_success_btn);
		tvTitle = (TextView) findViewById(R.id.send_pwd_success_title);
		tvMsg = (TextView) findViewById(R.id.send_pwd_success_msg);
		tvOpenBrowser = (TextView) findViewById(R.id.tv_open_browser);
		tvbtn.setOnClickListener(this);
		tvOpenBrowser.setOnClickListener(this);
		initNavigation();
	}

	/**
	 * 初始化数据
	* @Title: initData 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void initData() {
		switch (fromCase) {
		case AccountHelper.SKIP_CASE_ACCOUNT_FIND_PWD:
			tvbtn.setText(R.string.send_pwd_success_case_account_find_pwd_btn_ok);
			tvTitle.setText(R.string.send_pwd_success_case_account_find_pwd_title);
			tvMsg.setText(ResourceUtil.getString(context,R.string.send_pwd_success_case_account_find_pwd_msg,ResourceUtil.getString(context,R.string.app_name)));
			break;
		case AccountHelper.SKIP_CASE_LOGIN_FIND_PWD:
			tvbtn.setText(R.string.send_pwd_success_case_login_find_pwd_btn_login);
			tvTitle.setText(R.string.send_pwd_success_case_login_find_pwd_title);
			tvMsg.setText(ResourceUtil.getString(context,R.string.send_pwd_success_case_login_find_pwd_msg,ResourceUtil.getString(context,R.string.app_name)));
			break;
		case AccountHelper.SKIP_CASE_REGISTER_SUCCESS:
			tvbtn.setText(R.string.send_pwd_success_case_register_success_btn_login);
			tvTitle.setText(R.string.send_pwd_success_case_register_success_title);
			tvMsg.setText(ResourceUtil.getString(context,R.string.send_pwd_success_case_register_success_msg,ResourceUtil.getString(context,R.string.app_name)));
			mTitleTv.setText(R.string.page_account_registered_successfully);
			break;
		default:
			break;
		}
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
		mTitleTv.setText(R.string.page_account_forget_password);
		mNavigationView.setOnClickListener(this);
	}

	/**
	 * 全局按钮事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		// 登录按钮
		case R.id.send_pwd_success_btn:
			//			disEnabledBtn();
			back();
			break;

		case R.id.tv_open_browser:
			//			disEnabledBtn() ;
			Intent i = new Intent();
			i.setAction(Intent.ACTION_VIEW);
			i.setData(Uri.parse("http://"));
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			break;
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		dataeyeModule(true);
		//		enabledBtn() ;
	}

	public void dataeyeModule(boolean isShow) {
		switch (fromCase) {
		case AccountHelper.SKIP_CASE_ACCOUNT_FIND_PWD:
			DataEyeManager.getInstance().module(ModuleName.ACCOUNT_FIND_PWD_SUCCESS_FROM_MODIFY_PWD, isShow);
			break;
		case AccountHelper.SKIP_CASE_LOGIN_FIND_PWD:
			DataEyeManager.getInstance().module(ModuleName.ACCOUNT_FIND_PWD_SUCCESS_FROM_FIND_PWD, isShow);
			break;
		case AccountHelper.SKIP_CASE_REGISTER_SUCCESS:
			DataEyeManager.getInstance().module(ModuleName.ACCOUNT_FIND_PWD_SUCCESS_FROM_REGISTER, isShow);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	* @Title: back 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void back() {
		Intent intent;
		switch (fromCase) {
		case AccountHelper.SKIP_CASE_ACCOUNT_FIND_PWD:
			intent = new Intent(SendPwdSuccessActivity.this, AccountActivity.class);
			startActivity(intent);
			break;
		case AccountHelper.SKIP_CASE_LOGIN_FIND_PWD:
		case AccountHelper.SKIP_CASE_REGISTER_SUCCESS:
			intent = new Intent(SendPwdSuccessActivity.this, LoginActivity.class);
			intent.putExtra("userName", userName);
			startActivity(intent);
			break;
		default:
			break;
		}
		this.finish();
	}

	@Override
	public void onBackPressed() {
		back();
	}

	//	/**
	//	 * 启用按钮
	//	* @Title: enabledBtn 
	//	* @Description: TODO 
	//	* @param     
	//	* @return void
	//	 */
	//	public void enabledBtn()
	//	{
	//		tvbtn.setEnabled(true) ;
	//		tvOpenBrowser.setEnabled(true) ;
	//	}
	//
	//	/**
	//	 * 禁用按钮
	//	* @Title: disEnabled 
	//	* @Description: TODO 
	//	* @param     
	//	* @return void
	//	 */
	//	public void disEnabledBtn()
	//	{
	//		tvbtn.setEnabled(false) ;
	//		tvOpenBrowser.setEnabled(false) ;
	//	}

	@Override
	public void onPause() {
		super.onPause();
		dataeyeModule(false);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, tvbtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}
}
