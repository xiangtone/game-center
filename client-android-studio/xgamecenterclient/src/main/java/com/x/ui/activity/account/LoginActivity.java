package com.x.ui.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.account.AccountHelper;
import com.x.business.account.LoginManager;
import com.x.business.account.UserInfoManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.http.model.MasUser;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ProgressDialogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.editview.ClearEditText;

public class LoginActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

	/* 按钮、文本 */
	private Context context = this;
	private ClearEditText etUserName;
	private ClearEditText etPassword;
	private TextView btnLogin, tvRegister, tvFindPwd;
	private String userName, password;
	//	private TextView tvUnClear ;
	//	private TextView tvPwdClear ;
	private TextView tvUnHint, tvPwdHint;
	private boolean isSubmit = true;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	/**
	 * 程序主入口
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_account_login);
		setContentView(R.layout.activity_login);
		userName = getIntent().getStringExtra("userName");
		AccountHelper.addAct(LoginActivity.this);
		initViews();
		isSubmit = true;
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
		mTitleTv.setText(R.string.page_account_login);
		mNavigationView.setOnClickListener(this);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		btnLogin = (TextView) findViewById(R.id.btn_login);
		tvRegister = (TextView) findViewById(R.id.tv_register_link);
		tvFindPwd = (TextView) findViewById(R.id.tv_find_pwd);
		etUserName = (ClearEditText) findViewById(R.id.et_username);
		etPassword = (ClearEditText) findViewById(R.id.et_password);
		//		tvUnClear = (TextView) findViewById(R.id.tv_um_clare);
		//		tvPwdClear = (TextView) findViewById(R.id.tv_pwd_clear);
		tvUnHint = (TextView) findViewById(R.id.tv_um_hint);
		tvPwdHint = (TextView) findViewById(R.id.tv_pwd_hint);
		btnLogin.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		//		tvUnClear.setOnClickListener(this) ;
		//		tvPwdClear.setOnClickListener(this) ;
		etUserName.setOnFocusChangeListener(this);
		//		etPassword.setOnFocusChangeListener(this) ;
		tvFindPwd.setOnClickListener(this);

		//		tvUnClear.setVisibility(View.GONE) ;
		//		tvPwdClear.setVisibility(View.GONE) ;

		//		etUserName.addTextChangedListener(mUnTextWatcher) ;
		//		etPassword.addTextChangedListener(mPwdTextWatcher) ;

		//		tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG) ;
		//		tvFindPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG) ;

		tvRegister.setText(Html.fromHtml("<u>" + getString(R.string.register_btn_text) + "</u>"));
		tvFindPwd.setText(Html.fromHtml("<u>" + getString(R.string.find_password) + "</u>"));

		MasUser masUser = UserInfoManager.getInstence(this).getAccInfo();
		if (!TextUtils.isEmpty(userName)) {
			etUserName.setText(userName);
			AccountHelper.editEndFocus(etUserName);
		} else if (masUser != null) {
			etUserName.setText(masUser.getUserName());
			AccountHelper.editEndFocus(etUserName);
		}

	}

	/* 消息队列 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//			enabledBtn() ; 
			ProgressDialogUtil.closeProgressDialog();
			isSubmit = true;
			switch (msg.what) {
			case LoginManager.HANDLER_WHAT_LOGIN_SUCCESSFUL:
				ToastUtil.show(context, R.string.login_successful, Toast.LENGTH_LONG);
				startActivity(new Intent(LoginActivity.this, AccountActivity.class));
				AccountHelper.finishAct();
				break;
			case LoginManager.HANDLER_WHAT_LOGIN_FAILURE_UNPWD_ERROR:
				ToastUtil.show(context, R.string.login_username_or_pwd_error, Toast.LENGTH_LONG);
				break;
			case LoginManager.HANDLER_WHAT_LOGIN_FAILURE_UNKNOW:
				ToastUtil.show(context, R.string.login_failure, Toast.LENGTH_LONG);
				break;
			case AccountHelper.HANDLER_WHAT_NETWORK_ERROR:
				ToastUtil.show(context, R.string.login_fail_network_error, Toast.LENGTH_LONG);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 检测用户名和密码合法性
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	private boolean isValidityEditInfo(String userName, String passWord) {

		boolean isPassUn = isValidityUnEditInfo(userName);
		boolean isPassPassWord = isValidityPwdEditInfo(passWord);

		return (isPassUn && isPassPassWord);
	}

	/**
	 * 全局按钮事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		// 登录按钮
		case R.id.btn_login:
			userName = etUserName.getText().toString();
			password = etPassword.getText().toString();
			if (isValidityEditInfo(userName, password)) {
				// 网络检测
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				//				disEnabledBtn() ;
				if (isSubmit) {
					isSubmit = false;
					ProgressDialogUtil.openProgressDialog(context, ResourceUtil.getString(context, R.string.login_in),
							ResourceUtil.getString(context, R.string.login_in), false);
					LoginManager loginManager = new LoginManager(this, mHandler);
					loginManager.login(userName, password);
				}

				//				enabledBtn() ;
			}
			break;
		// 注册按钮
		case R.id.tv_register_link:
			//			disEnabledBtn() ;
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		//		case R.id.tv_um_clare:
		//			etUserName.setText("") ;
		//			break;
		//		case R.id.tv_pwd_clear:
		//			etPassword.setText("") ;
		//			break;
		case R.id.tv_find_pwd:
			//			disEnabledBtn() ;
			Intent intent = new Intent(this, FindPwdActivity.class);
			intent.putExtra("fromCase", AccountHelper.SKIP_CASE_LOGIN_FIND_PWD);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (view == etUserName) {
			if (!hasFocus) {
				userName = etUserName.getText().toString();
				isValidityUnEditInfo(userName);
			}
			//				else{
			//					controlTvUnClear() ;
			//					controlTvPwdClear() ;
			//				}
		} else if (view == etPassword) {
			if (!hasFocus) {
				password = etPassword.getText().toString();
				isValidityPwdEditInfo(password);
			}
			//				else{
			//					controlTvUnClear() ;
			//					controlTvPwdClear() ;
			//				}
		}
	}

	//    public void controlTvUnClear()
	//    {
	//    	userName = etUserName.getText().toString();
	//    	if(!TextUtils.isEmpty(userName))
	//    	{
	//    		etUserName.setClearIconVisible(true) ;
	//    	}else {
	//    		etUserName.setClearIconVisible(false) ;
	//    	}
	//    }
	//    
	//    public void controlTvPwdClear()
	//    {
	//    	password = etPassword.getText().toString();
	//    	if(!TextUtils.isEmpty(password))
	//    	{
	//    		tvPwdClear.setVisibility(View.VISIBLE) ;
	//    	}else {
	//    		tvPwdClear.setVisibility(View.GONE) ;
	//    	}
	//    }

	/**
	 * 核查编辑框信息合法性
	 */
	private boolean isValidityPwdEditInfo(String pwd) {
		password = Utils.removeSpace(pwd);
		if (TextUtils.isEmpty(password)) {
			tvPwdHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.pwd_is_null)));
			return false;
		} else if (password.length() < 6) {
			tvPwdHint
					.setText(AccountHelper.getAccountWarnString(this.getString(R.string.pwd_char_count_greater_check)));
			return false;
		} else if (password.length() > 15) {
			tvPwdHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.pwd_char_count_less_check)));
			return false;
		} else {
			tvPwdHint.setText("");
			return true;
		}
	}

	/**
	 * 核查编辑框信息合法性
	 */
	private boolean isValidityUnEditInfo(String uName) {
		userName = Utils.removeSpace(uName);
		if (TextUtils.isEmpty(userName)) {
			tvUnHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.username_is_null)));
			return false;
		} else if (userName.length() > 50) {
			tvUnHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.username_char_count_less_check)));
			return false;
		} else if (!AccountHelper.isEmail(userName)) {
			tvUnHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.username_no_email)));
			return false;
		} else {
			tvUnHint.setText("");
			return true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_LOGIN, true);
		//		enabledBtn() ;
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
	//		btnLogin.setEnabled(true) ;
	//		tvRegister.setEnabled(true) ;
	//		tvFindPwd.setEnabled(true) ;
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
	//		btnLogin.setEnabled(false) ;
	//		tvRegister.setEnabled(false) ;
	//		tvFindPwd.setEnabled(false) ;
	//	}
	//	TextWatcher mUnTextWatcher = new TextWatcher() {  
	//        @Override  
	//        public void onTextChanged(CharSequence s, int start, int before, int count) { 
	//        	
	//        }  
	//        @Override  
	//        public void beforeTextChanged(CharSequence s, int start, int count,  
	//                int after) {  
	//        }  
	//          
	//        @Override  
	//        public void afterTextChanged(Editable s) { 
	////        	controlTvUnClear() ;
	//        }
	//    }; 
	//    
	//    TextWatcher mPwdTextWatcher = new TextWatcher() {  
	//        @Override  
	//        public void onTextChanged(CharSequence s, int start, int before, int count) { 
	//        	
	//        }  
	//        @Override  
	//        public void beforeTextChanged(CharSequence s, int start, int count,  
	//                int after) {  
	//        }  
	//          
	//        @Override  
	//        public void afterTextChanged(Editable s) { 
	////        	controlTvPwdClear() ;
	//        }
	//    };

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_LOGIN, false);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, btnLogin, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setTextViewColor(context, tvRegister, SkinConstan.THEME_TEXT_LINK);
		SkinConfigManager.getInstance().setTextViewColor(context, tvFindPwd, SkinConstan.THEME_TEXT_LINK);
	}
}
