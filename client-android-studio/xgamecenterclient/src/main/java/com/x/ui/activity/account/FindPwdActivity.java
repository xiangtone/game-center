package com.x.ui.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.account.AccountHelper;
import com.x.business.account.FindPwdManager;
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
import com.x.ui.view.editview.ClearEditText;

public class FindPwdActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

	/* 按钮、文本 */
	private Context context = this;
	private ClearEditText etUserName;
	private TextView btnNext;
	private String userName;
	//	private TextView tvUnClear ;
	private TextView tvUnHint, tvFindHintText;
	private int fromCase;
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
		setTabTitle(R.string.page_account_forget_password);
		setContentView(R.layout.activity_find_pwd);
		fromCase = getIntent().getIntExtra("fromCase", 0);
		AccountHelper.addAct(FindPwdActivity.this);
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
		mTitleTv.setText(R.string.page_account_forget_password);
		mNavigationView.setOnClickListener(this);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		btnNext = (TextView) findViewById(R.id.tv_find_pwd_next);
		tvFindHintText = (TextView) findViewById(R.id.tv_find_hint_text);
		etUserName = (ClearEditText) findViewById(R.id.et_find_pwd_username);
		//		tvUnClear = (TextView) findViewById(R.id.tv_find_pwd_un_clare);
		tvUnHint = (TextView) findViewById(R.id.tv_find_pwd_un_hint);
		btnNext.setOnClickListener(this);
		//		tvUnClear.setOnClickListener(this) ;
		etUserName.setOnFocusChangeListener(this);
		//		tvFindHintText.setText(AccountHelper.getAccountWarnString(this.getString(R.string.find_hint_text))) ;
		//		tvUnClear.setVisibility(View.GONE) ;
		//		etUserName.addTextChangedListener(mUnTextWatcher) ;
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
				btnNext.setEnabled(false);
				String text = context.getString(R.string.next) + "(" + remainTimes + "s)";
				btnNext.setText(text);
				btnNext.postInvalidate();
				if (accountHelper != null) {
					accountHelper.doHorbiddenHandler(context, mHandler, remainTimes);
				}
				break;
			case AccountHelper.HANDLER_WHAT_ENABLE_ACTION:
				btnNext.setEnabled(true);
				btnNext.setText(R.string.next);
				btnNext.postInvalidate();
				break;
			case FindPwdManager.HANDLER_WHAT_FIND_PWD_SUCCESSFUL:
				ToastUtil.show(context, R.string.send_email_succesful, Toast.LENGTH_LONG);
				Intent intent = new Intent(FindPwdActivity.this, SendPwdSuccessActivity.class);
				intent.putExtra("userName", userName);
				intent.putExtra("fromCase", fromCase);
				startActivity(intent);
				if (accountHelper != null) {
					accountHelper.launchHorbiddenHandler(context, mHandler);
				}

				break;
			case FindPwdManager.HANDLER_WHAT_FIND_PWD_FAILURE_EMAIL_ERROR:
			case FindPwdManager.HANDLER_WHAT_FIND_PWD_FAILURE_NOT_REGISTER:
				ToastUtil.show(context, R.string.email_error, Toast.LENGTH_LONG);
				break;
			case FindPwdManager.HANDLER_WHAT_FIND_PWD_FAILURE_UNKNOW:
				ToastUtil.show(context, R.string.send_email_error, Toast.LENGTH_LONG);
				break;
			case AccountHelper.HANDLER_WHAT_NETWORK_ERROR:
				ToastUtil.show(context, R.string.send_email_fail_network_error, Toast.LENGTH_LONG);
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
	private boolean isValidityEditInfo(String userName) {

		return isValidityUnEditInfo(userName);

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
		case R.id.tv_find_pwd_next:
			userName = etUserName.getText().toString();
			if (isValidityEditInfo(userName)) {
				// 网络检测
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				//				disEnabledBtn() ;
				if (isSubmit) {
					isSubmit = false;
					ProgressDialogUtil.openProgressDialog(context, ResourceUtil.getString(context, R.string.loading),
							ResourceUtil.getString(context, R.string.loading), false);
					FindPwdManager findPwdManager = new FindPwdManager(this, mHandler);
					findPwdManager.findPwd(userName);
				}

				//				enabledBtn() ;
			}
			break;
		//		case R.id.tv_find_pwd_un_clare:
		//			etUserName.setText("") ;
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
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_FIND_PASSWORD, true);
		//		enabledBtn() ;
	}

	/* (非 Javadoc) 
	* <p>Title: finish</p> 
	* <p>Description: </p>  
	* @see android.app.Activity#finish() 
	*/

	@Override
	public void finish() {
		super.finish();
		mHandler = null;
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
	//		btnNext.setEnabled(true) ;
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
	//		btnNext.setEnabled(false) ;
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
	//   
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

	@Override
	public void onPause() {
		super.onPause();
		accountHelper = null;
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_FIND_PASSWORD, false);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, btnNext, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}
}
