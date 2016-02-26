package com.x.ui.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.account.AccountHelper;
import com.x.business.account.ModifyUserInfoManager;
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
import com.x.ui.activity.home.MainActivity;

public class ModifyPwdActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

	/* 按钮、文本 */
	private Context context = this;
	private EditText etOldPwd;
	private EditText etNewPwd;
	private TextView btnOk, tvModifyFindPwdLink;
	//	private TextView tvOldPwdClear ,tvNewPwdClear;
	private TextView tvOldPwdHint, tvNewPwdHint;
	public String oldPwd, newPwd;
	public CheckBox cbShowPwd;
	private boolean isSubmit = true;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	/**
	 * 程序主入口
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_account_modify_pwd);
		setContentView(R.layout.activity_modify_pwd);
		AccountHelper.addAct(ModifyPwdActivity.this);
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
		mTitleTv.setText(R.string.page_account_modify_pwd);
		mNavigationView.setOnClickListener(this);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		btnOk = (TextView) findViewById(R.id.btn_modify_ok);
		etOldPwd = (EditText) findViewById(R.id.et_old_pwd);
		etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
		//			tvOldPwdClear = (TextView) findViewById(R.id.tv_old_pwd_clear);
		//			tvNewPwdClear = (TextView) findViewById(R.id.tv_new_pwd_clear);
		tvOldPwdHint = (TextView) findViewById(R.id.tv_old_pwd_hint);
		tvNewPwdHint = (TextView) findViewById(R.id.tv_new_pwd_hint);
		tvModifyFindPwdLink = (TextView) findViewById(R.id.tv_modify_find_pwd_link);
		cbShowPwd = (CheckBox) findViewById(R.id.cb_show_pwd);

		btnOk.setOnClickListener(this);
		//			tvOldPwdClear.setOnClickListener(this) ;
		//			tvNewPwdClear.setOnClickListener(this) ;
		etOldPwd.setOnClickListener(this);
		tvModifyFindPwdLink.setOnClickListener(this);

		etNewPwd.setOnFocusChangeListener(this);
		etOldPwd.setOnFocusChangeListener(this);

		//			tvOldPwdClear.setVisibility(View.GONE) ;
		//			tvNewPwdClear.setVisibility(View.GONE) ;

		//			etOldPwd.addTextChangedListener(mOldPwdTextWatcher) ;
		//			etNewPwd.addTextChangedListener(mNewPwdTextWatcher) ;

		//			tvModifyFindPwdLink.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG) ;
		tvModifyFindPwdLink.setText(Html.fromHtml("<u>" + getString(R.string.mpwd_forget_pwd) + "</u>"));
		cbShowPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				if (isChecked) {
					etNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				AccountHelper.editEndFocus(etNewPwd);
			}
		});

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ProgressDialogUtil.closeProgressDialog();
			isSubmit = true;
			//			enabledBtn() ;
			switch (msg.what) {
			case ModifyUserInfoManager.HANDLER_WHAT_MODIFY_SUCCESSFUL:
				ToastUtil.show(context, R.string.mpwd_modify_pwd_success, Toast.LENGTH_LONG);
				startActivity(new Intent(ModifyPwdActivity.this, MainActivity.class));
				break;
			case ModifyUserInfoManager.HANDLER_WHAT_MODIFY_PWD_FAILURE_OLD_PWD_ERRROR:
				ToastUtil.show(context, R.string.mpwd_old_pwd_error, Toast.LENGTH_LONG);
				break;
			case ModifyUserInfoManager.HANDLER_WHAT_MODIFY_FAILURE_UNKNOW:
				ToastUtil.show(context, R.string.mpwd_modify_fail, Toast.LENGTH_LONG);
				break;
			case AccountHelper.HANDLER_WHAT_NETWORK_ERROR:
				ToastUtil.show(context, R.string.modify_pwd_fail_network_error, Toast.LENGTH_LONG);
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

	private boolean isValidityEditInfo(String oldPwd, String newPwd) {
		boolean isPassOldPwd = isValidityOldPwdEditInfo(oldPwd);
		boolean isPassNewPwd = isValidityNewPwdEditInfo(newPwd);
		return (isPassOldPwd && isPassNewPwd);
	}

	class CheckBoxOnCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

		}

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
		case R.id.btn_modify_ok:
			oldPwd = etOldPwd.getText().toString();
			newPwd = etNewPwd.getText().toString();
			if (isValidityEditInfo(oldPwd, newPwd)) {
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
					ModifyUserInfoManager modifyUserInfoManager = new ModifyUserInfoManager(this, mHandler);
					modifyUserInfoManager.modifyPwd(newPwd, oldPwd);
				}

				//				enabledBtn() ;
			}
			break;
		//		case R.id.tv_old_pwd_clear:
		//			etOldPwd.setText("") ;
		//			break;
		//		case R.id.tv_new_pwd_clear:
		//			etNewPwd.setText("") ;
		//			break;
		case R.id.tv_modify_find_pwd_link:
			//			disEnabledBtn() ;
			Intent intent = new Intent(this, FindPwdActivity.class);
			intent.putExtra("fromCase", AccountHelper.SKIP_CASE_ACCOUNT_FIND_PWD);
			startActivity(intent);
			//			enabledBtn() ;
			break;
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (view == etOldPwd) {
			if (!hasFocus) {
				oldPwd = etOldPwd.getText().toString();
				isValidityOldPwdEditInfo(oldPwd);
			}
		} else if (view == etNewPwd) {
			if (!hasFocus) {
				newPwd = etNewPwd.getText().toString();
				isValidityNewPwdEditInfo(newPwd);
			}
		}
	}

	/**
	 * 核查编辑框信息合法性
	 */
	private boolean isValidityNewPwdEditInfo(String newPwds) {
		newPwd = Utils.removeSpace(newPwds);
		if (TextUtils.isEmpty(newPwd)) {
			tvNewPwdHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.pwd_is_null)));
			return false;
		} else if (newPwd.length() < 6) {
			tvNewPwdHint.setText(AccountHelper.getAccountWarnString(this
					.getString(R.string.pwd_char_count_greater_check)));
			return false;
		} else if (newPwd.length() > 15) {
			tvNewPwdHint
					.setText(AccountHelper.getAccountWarnString(this.getString(R.string.pwd_char_count_less_check)));
			return false;
		} else {
			tvNewPwdHint.setText("");
			return true;
		}
	}

	/**
	 * 核查编辑框信息合法性
	 */
	private boolean isValidityOldPwdEditInfo(String oldPwds) {
		oldPwd = Utils.removeSpace(oldPwds);
		if (TextUtils.isEmpty(oldPwd)) {
			tvOldPwdHint.setText(AccountHelper.getAccountWarnString(this.getString(R.string.pwd_is_null)));
			return false;
		} else if (oldPwd.length() < 6) {
			tvOldPwdHint.setText(AccountHelper.getAccountWarnString(this
					.getString(R.string.pwd_char_count_greater_check)));
			return false;
		} else if (oldPwd.length() > 15) {
			tvOldPwdHint
					.setText(AccountHelper.getAccountWarnString(this.getString(R.string.pwd_char_count_less_check)));
			return false;
		} else {
			tvOldPwdHint.setText("");
			return true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_MODIFY_PASSWORD, true);
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
	//		btnOk.setEnabled(true) ;
	//		tvModifyFindPwdLink.setEnabled(true) ;
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
	//		btnOk.setEnabled(false) ;
	//		tvModifyFindPwdLink.setEnabled(false) ;
	//	}
	//	
	//	TextWatcher mOldPwdTextWatcher = new TextWatcher() {  
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
	//        	controlTvOldPwdClear() ;
	//        }
	//    }; 
	//    
	//    TextWatcher mNewPwdTextWatcher = new TextWatcher() {  
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
	//        	controlTvNewPwdClear() ;
	//        }
	//    };
	//    
	//    public void controlTvOldPwdClear()
	//    {
	//    	oldPwd = etOldPwd.getText().toString();
	//    	if(!TextUtils.isEmpty(oldPwd))
	//    	{
	//    		tvOldPwdClear.setVisibility(View.VISIBLE) ;
	//    	}else {
	//    		tvOldPwdClear.setVisibility(View.GONE) ;
	//    	}
	//    }
	//    
	//    public void controlTvNewPwdClear()
	//    {
	//    	newPwd = etNewPwd.getText().toString();
	//    	if(!TextUtils.isEmpty(newPwd))
	//    	{
	//    		tvNewPwdClear.setVisibility(View.VISIBLE) ;
	//    	}else {
	//    		tvNewPwdClear.setVisibility(View.GONE) ;
	//    	}
	//    }

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_MODIFY_PASSWORD, false);
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, btnOk, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setTextViewColor(context, tvModifyFindPwdLink, SkinConstan.THEME_TEXT_LINK);
		SkinConfigManager.getInstance().setCheckBoxBtnDrawable(context, cbShowPwd, SkinConstan.CB_ACCOUNT_BTN);
	}
}
