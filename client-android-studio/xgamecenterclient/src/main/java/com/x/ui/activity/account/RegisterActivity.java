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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.account.AccountHelper;
import com.x.business.account.RegisterManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ProgressDialogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;

public class RegisterActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

	/* 按钮、文本 */
	private Context context = this;
	private EditText etUserName;
	private EditText etNickname;
	private TextView tvLoginLink;
	private TextView btnRegister;
	//	private TextView tvUnClear ,tvNnClear;
	private TextView tvUnHint, tvNnHint;
	public String userName, nickName;
	public RegisterManager registerManager;
	private boolean isSubmit = true;
	AccountHelper accountHelper = null;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	/**
	 * 程序主入口
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_account_register);
		setContentView(R.layout.activity_register);
		AccountHelper.addAct(RegisterActivity.this);
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
		mTitleTv.setText(R.string.page_account_register);
		mNavigationView.setOnClickListener(this);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		tvLoginLink = (TextView) findViewById(R.id.tv_reg_login_link);
		btnRegister = (TextView) findViewById(R.id.btn_register);
		etUserName = (EditText) findViewById(R.id.et_username);
		etNickname = (EditText) findViewById(R.id.et_nickname);
		//			tvUnClear = (TextView) findViewById(R.id.tv_reg_un_clare);
		//			tvNnClear = (TextView) findViewById(R.id.tv_reg_nn_clare);
		tvUnHint = (TextView) findViewById(R.id.tv_reg_un_hint);
		tvNnHint = (TextView) findViewById(R.id.tv_reg_nn_hint);
		tvLoginLink.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		//			tvUnClear.setOnClickListener(this) ;
		//			tvNnClear.setOnClickListener(this) ;
		etNickname.setOnClickListener(this);
		etUserName.setOnFocusChangeListener(this);
		etNickname.setOnFocusChangeListener(this);

		//			tvUnClear.setVisibility(View.GONE) ;
		//			tvNnClear.setVisibility(View.GONE) ;

		//			etUserName.addTextChangedListener(mUnTextWatcher) ;
		//			etNickname.addTextChangedListener(mNnTextWatcher) ;

		//			tvLoginLink.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG) ;

		tvLoginLink.setText(Html.fromHtml("<u>" + getString(R.string.reg_have_account) + "</u>"));

		String systemEmailAccount = AccountHelper.getSystemEmailAccount(this);
		if (!TextUtils.isEmpty(systemEmailAccount)) {
			etUserName.setText(systemEmailAccount);
			AccountHelper.editEndFocus(etUserName);
		}
	}

	/* 消息队列 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			ProgressDialogUtil.closeProgressDialog();
			isSubmit = true;
			switch (msg.what) {
			case AccountHelper.HANDLER_WHAT_HORBIDDEN_ACTION:
				int remainTimes = (Integer) msg.obj;
				btnRegister.setEnabled(false);
				String text = context.getString(R.string.register) + "(" + remainTimes + "s)";
				btnRegister.setText(text);
				btnRegister.postInvalidate();
				if (accountHelper != null) {
					accountHelper.doHorbiddenHandler(context, mHandler, remainTimes);
				}

				break;
			case RegisterManager.HANDLER_WHAT_REGISTER_SUCCESSFUL:
				ToastUtil.show(context, R.string.reg_success, Toast.LENGTH_LONG);
				Intent intent = new Intent(RegisterActivity.this, SendPwdSuccessActivity.class);
				intent.putExtra("userName", userName);
				intent.putExtra("fromCase", AccountHelper.SKIP_CASE_REGISTER_SUCCESS);
				startActivity(intent);
				if (accountHelper != null) {
					accountHelper.launchHorbiddenHandler(context, mHandler);
				}
				break;
			case RegisterManager.HANDLER_WHAT_REGISTER_ALREADY_REGISTERED:
				ToastUtil.show(context, R.string.reg_fail_have_registered, Toast.LENGTH_LONG);
				break;
			case RegisterManager.HANDLER_WHAT_REGISTER_INVALID_EMAIL:
				ToastUtil.show(context, R.string.reg_fail_email_invalid, Toast.LENGTH_LONG);
				break;
			case RegisterManager.HANDLER_WHAT_REGISTER_FAILURE_UNKNOW:
				ToastUtil.show(context, R.string.reg_fail, Toast.LENGTH_LONG);
				break;
			case AccountHelper.HANDLER_WHAT_NETWORK_ERROR:
				ToastUtil.show(context, R.string.reg_fail_network_error, Toast.LENGTH_LONG);
				break;
			case AccountHelper.HANDLER_WHAT_ENABLE_ACTION:
				btnRegister.setEnabled(true);
				btnRegister.setText(R.string.register);
				btnRegister.postInvalidate();
				break;
			default:
				break;
			}
			//			//激活按钮
			//			if(msg.what !=AccountHelper.HANDLER_WHAT_HORBIDDEN_ACTION)
			//			{
			//				enabledBtn() ;
			//			}
		}
	};

	/**
	 * 检测用户名和密码合法性
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	private boolean isValidityEditInfo(String userName, String nickName) {

		boolean isPassUn = isValidityUnEditInfo(userName);
		boolean isPassNickName = isValidityNickNameEditInfo(nickName);
		return (isPassUn && isPassNickName);
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
		case R.id.btn_register:
			userName = etUserName.getText().toString();
			nickName = etNickname.getText().toString();
			if (isValidityEditInfo(userName, nickName)) {
				// 网络检测
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				//				disEnabledBtn() ;
				if (isSubmit) {
					isSubmit = false;
					ProgressDialogUtil.openProgressDialog(context,
							ResourceUtil.getString(context, R.string.reg_registering),
							ResourceUtil.getString(context, R.string.reg_registering), false);
					RegisterManager registerManager = new RegisterManager(this, mHandler);
					registerManager.register(userName, nickName);
				}

				//				enabledBtn() ;
			}
			break;
		// 注册按钮
		case R.id.tv_reg_login_link:
			//			disEnabledBtn() ;
			startActivity(new Intent(this, LoginActivity.class));
			this.finish();
			break;
		//		case R.id.tv_reg_un_clare:
		//			etUserName.setText("") ;
		//			break;
		//		case R.id.tv_reg_nn_clare:
		//			etNickname.setText("") ;
		//			break;
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (view == etUserName) {
			if (!hasFocus) {
				userName = etUserName.getText().toString();
				isValidityUnEditInfo(userName);
			}
		} else if (view == etNickname) {
			if (!hasFocus) {
				nickName = etNickname.getText().toString();
				isValidityNickNameEditInfo(nickName);
			}
		}
	}

	/**
	 * 核查编辑框信息合法性
	 */
	private boolean isValidityNickNameEditInfo(String nName) {
		nickName = Utils.removeSpace(nName);
		if (TextUtils.isEmpty(nickName)) {
			tvNnHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.nickname_is_null)));
			return false;
		} else if (nickName.length() < 3) {
			tvNnHint.setText(AccountHelper.getAccountWarnString(this
					.getString(R.string.nickname_char_count_greater_check)));
			return false;
		} else if (nickName.length() > 30) {
			tvNnHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.nickname_char_count_less_check)));
			return false;
		} else {
			tvNnHint.setText("");
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
		accountHelper = new AccountHelper();
		accountHelper.doHorbiddenHandler(context, mHandler, -1);
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_REGISTER, true);
		//		enabledBtn() ;
	}

	//	
	//	/**
	//	 * 启用按钮
	//	* @Title: enabledBtn 
	//	* @Description: TODO 
	//	* @param     
	//	* @return void
	//	 */
	//	public void enabledBtn()
	//	{
	//		tvLoginLink.setEnabled(true) ;
	//		btnRegister.setEnabled(true) ;
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
	//		tvLoginLink.setEnabled(false) ;
	//		btnRegister.setEnabled(false) ;
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
	//        	controlTvUnClear() ;
	//        }
	//    }; 
	//    
	//    TextWatcher mNnTextWatcher = new TextWatcher() {  
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
	//        	controlTvNnClear() ;
	//        }
	//    };
	//	 public void controlTvUnClear()
	//	    {
	//	    	userName = etUserName.getText().toString();
	//	    	if(!TextUtils.isEmpty(userName))
	//	    	{
	//	    		tvUnClear.setVisibility(View.VISIBLE) ;
	//	    	}else {
	//	    		tvUnClear.setVisibility(View.GONE) ;
	//	    	}
	//	    }
	//	    
	//	    public void controlTvNnClear()
	//	    {
	//	    	nickName = etNickname.getText().toString();
	//	    	if(!TextUtils.isEmpty(nickName))
	//	    	{
	//	    		tvNnClear.setVisibility(View.VISIBLE) ;
	//	    	}else {
	//	    		tvNnClear.setVisibility(View.GONE) ;
	//	    	}
	//	    }
	@Override
	public void onPause() {
		super.onPause();
		accountHelper = null;
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_REGISTER, false);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, btnRegister, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setTextViewColor(context, tvLoginLink, SkinConstan.THEME_TEXT_LINK);
	}
}
