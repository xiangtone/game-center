package com.x.ui.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.account.AccountHelper;
import com.x.business.account.LogoutManager;
import com.x.business.account.ModifyUserInfoManager;
import com.x.business.account.UserInfoManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.db.resource.NativeResourceConstant;
import com.x.publics.http.model.MasUser;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ProgressDialogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.home.HomeActivity;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.activity.myApps.MyAppsActivity;
import com.x.ui.activity.resource.ResourceManagementActivity;
import com.x.ui.activity.settings.SettingsActivity;
import com.x.ui.activity.tools.ToolsActivity;

public class AccountActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

	private TextView accountNameTv, btnOk, tvNnHint, tvModifyUserName, tvModifyChangePwdLink;
	private ImageView ivAccountHeadPortrait, ivAccountModifyHeadPortrait, ivModify;
	private EditText etModifyNickname;
	private TextView btnLogout, tvInfoNickname;;
	private static final int[] btns = new int[] { R.id.btn_head1, R.id.btn_head2, R.id.btn_head3, R.id.btn_head4,
			R.id.btn_head5, R.id.btn_head6, R.id.btn_head7, R.id.btn_head8, R.id.btn_my_apps_ll2, R.id.btn_my_contents_ll2,
			R.id.btn_tools_ll2, R.id.btn_settings_ll2 };
	private String modifyNickName, nickName;
	private int modifyHeadPortraitIndex = -1;
	private MasUser user;
	private int headPortraitIndex;
	private View llAccountPart1, llAccountPart2, acAccountBgll, acAccountModifyBgll;
	private Context context = this;
	private boolean isSubmit = true;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_account_home);
		setContentView(R.layout.activity_account);
		AccountHelper.addAct(AccountActivity.this);
		initUi();
		initData();
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
		mTitleTv.setText(R.string.page_account_home);
		mNavigationView.setOnClickListener(this);
	}

	private void initUi() {
		accountNameTv = (TextView) findViewById(R.id.tv_username);
		tvModifyChangePwdLink = (TextView) findViewById(R.id.tv_modify_change_pwd_link);
		tvModifyUserName = (TextView) findViewById(R.id.tv_modify_username);
		ivModify = (ImageView) findViewById(R.id.iv_modify);
		tvInfoNickname = (TextView) findViewById(R.id.tv_info_nickname);
		etModifyNickname = (EditText) findViewById(R.id.et_modify_nickname);
		tvNnHint = (TextView) findViewById(R.id.tv_modify_nn_hint);
		ivAccountHeadPortrait = (ImageView) findViewById(R.id.iv_account_head_portrait);
		ivAccountModifyHeadPortrait = (ImageView) findViewById(R.id.iv_account_modify_head_portrait);
		//		tvNnClear = (TextView) findViewById(R.id.tv_modify_nn_clare);
		btnLogout = (TextView) findViewById(R.id.btn_logout);
		btnOk = (TextView) findViewById(R.id.btn_ok);
		acAccountBgll = (View) findViewById(R.id.account_bg_ll);
		acAccountModifyBgll = (View) findViewById(R.id.account_modify_bg_ll);
		llAccountPart1 = (View) findViewById(R.id.ll_account_part1);
		llAccountPart2 = (View) findViewById(R.id.ll_account_part2);

		ivAccountHeadPortrait.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		ivModify.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		//		tvNnClear.setOnClickListener(this) ;
		tvModifyChangePwdLink.setOnClickListener(this);
		etModifyNickname.setOnFocusChangeListener(this);

		//		etModifyNickname.addTextChangedListener(mNnTextWatcher) ;
		//		tvModifyChangePwdLink.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG) ;
		tvModifyChangePwdLink.setText(Html.fromHtml("<u>" + getString(R.string.change_password) + "</u>"));
		// 注册监听器
		for (int btn : btns) {
			findViewById(btn).setOnClickListener(this);
		}
		showModifyLLAccountPart(false);//初始化显示
	}

	/**
	 * 
	* @Title: showLLAccountPart 
	* @Description: TODO 
	* @param @param part    
	* @return void
	 */
	public void showModifyLLAccountPart(boolean isShow) {
		if (isShow) {
			llAccountPart1.setVisibility(View.INVISIBLE);
			llAccountPart2.setVisibility(View.VISIBLE);
		} else {
			llAccountPart1.setVisibility(View.VISIBLE);
			llAccountPart2.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 初始化数据
	* @Title: initData 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	private void initData() {
		user = UserInfoManager.getInstence(this).getAccInfo();
		if (user != null) {
			tvInfoNickname.setText(user.getNickName());
			accountNameTv.setText(user.getUserName());
			tvModifyUserName.setText(user.getUserName());
			etModifyNickname.setText(user.getNickName());
		}
		ZeroDataResourceHelper.getSelfZerodataHeadPortrait(this);
		headPortraitIndex = ZeroDataResourceHelper.getSelfZerodataHeadPortraitValue(this);
		setHeadPortrait(headPortraitIndex);
		etModifyNickname.setText(user.getNickName());
	}

	/**
	 * 设置头像
	* @Title: setHeadPortrait 
	* @Description: TODO 
	* @param @param index    
	* @return void
	 */
	private void setHeadPortrait(int index) {
		ivAccountHeadPortrait.setImageResource(ZeroDataResourceHelper.getZerodataHeadPortraitResource(index));
		acAccountBgll.setBackgroundResource(ZeroDataResourceHelper.getZerodataHeadBgPortraitResource(index));
		ivAccountModifyHeadPortrait.setImageResource(ZeroDataResourceHelper.getZerodataHeadPortraitResource(index));
		//		acAccountModifyBgll.setBackgroundResource(ZeroDataResourceHelper.headPortraitBackgroundArray[index]) ;
		acAccountModifyBgll.setBackgroundResource(ZeroDataResourceHelper.getZerodataHeadBgPortraitResource(index));
	}

	/**
	 * 界面响应
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ProgressDialogUtil.closeProgressDialog();
			//			enabledBtn() ;
			isSubmit = true;
			switch (msg.what) {
			case LogoutManager.HANDLER_WHAT_LOGOUT_SUCCESSFUL:
				ToastUtil.show(AccountActivity.this, R.string.logout_success, Toast.LENGTH_LONG);
				startActivity(new Intent(AccountActivity.this, MainActivity.class));
				break;
			case LogoutManager.HANDLER_WHAT_LOGOUT_FAILURE_UNKNOW:
				ToastUtil.show(AccountActivity.this, R.string.logout_fail, Toast.LENGTH_LONG);
				break;
			case AccountHelper.HANDLER_WHAT_NETWORK_ERROR:
				ToastUtil.show(AccountActivity.this, R.string.logout_fail_network_error, Toast.LENGTH_LONG);
				break;
			case ModifyUserInfoManager.HANDLER_WHAT_MODIFY_SUCCESSFUL:
				ToastUtil.show(AccountActivity.this, R.string.modify_nickname_success, Toast.LENGTH_LONG);
				doModifyHeadPortraitIndex();
				initData();
				break;
			case ModifyUserInfoManager.HANDLER_WHAT_MODIFY_FAILURE_UNKNOW:
				ToastUtil.show(AccountActivity.this, R.string.modify_nickname_fail, Toast.LENGTH_LONG);
				initData();
				showModifyLLAccountPart(false);
				break;
			case ModifyUserInfoManager.HANDLER_WHAT_MODIFY_NICKNAME_FAILURE_NETWORK_ERROR:
				ToastUtil.show(AccountActivity.this, R.string.modify_nickname_fail_network_error, Toast.LENGTH_LONG);
				initData();
				showModifyLLAccountPart(false);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 
	* @Title: focusModifyView 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void focusModifyView() {
		etModifyNickname.requestFocus();
		AccountHelper.editEndFocus(etModifyNickname);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mh_navigate_ll:
				onBackPressed();
				break;
			case R.id.iv_account_head_portrait:
				showModifyLLAccountPart(true);
				focusModifyView();
				break;
			//		case R.id.tv_modify_nn_clare:
			//			etModifyNickname.setText("") ;
			//			break ;
			case R.id.iv_modify:
				showModifyLLAccountPart(true);
				focusModifyView();
				break;
			case R.id.btn_logout:
				if (!NetworkUtils.isNetworkAvailable(AccountActivity.this)) {
					ToastUtil.show(AccountActivity.this,
							AccountActivity.this.getResources().getString(R.string.network_canot_work), Toast.LENGTH_SHORT);
					return;
				}
				//			disEnabledBtn() ;
				ProgressDialogUtil.openProgressDialog(context, ResourceUtil.getString(context, R.string.logouting),
						ResourceUtil.getString(context, R.string.logouting), false);
				LogoutManager logoutManager = new LogoutManager(AccountActivity.this, mHandler);
				logoutManager.logout();
				//			enabledBtn() ;
				break;
			case R.id.btn_ok:
				doModifyUserInfo();
				break;
			case R.id.tv_modify_change_pwd_link:
				//			disEnabledBtn() ;
				startActivity(new Intent(this, ModifyPwdActivity.class));
				break;
			// 头像1
			case R.id.btn_head1:
				setHeadPortrait(0);
				modifyHeadPortraitIndex = 0;
				break;
			// 头像2
			case R.id.btn_head2:
				setHeadPortrait(1);
				modifyHeadPortraitIndex = 1;
				break;
			// 头像3
			case R.id.btn_head3:
				setHeadPortrait(2);
				modifyHeadPortraitIndex = 2;
				break;
			// 头像4
			case R.id.btn_head4:
				setHeadPortrait(3);
				modifyHeadPortraitIndex = 3;
				break;
			// 头像5
			case R.id.btn_head5:
				setHeadPortrait(4);
				modifyHeadPortraitIndex = 4;
				break;
			// 头像6
			case R.id.btn_head6:
				setHeadPortrait(5);
				modifyHeadPortraitIndex = 5;
				break;
			// 头像7
			case R.id.btn_head7:
				setHeadPortrait(6);
				modifyHeadPortraitIndex = 6;
				break;
			// 头像8
			case R.id.btn_head8:
				setHeadPortrait(7);
				modifyHeadPortraitIndex = 7;
				break;
			case R.id.btn_my_apps_ll2:
				Intent myAppsIntent = new Intent(context, MyAppsActivity.class);
				myAppsIntent.putExtra("activity_name", context.getClass().getName());
				startActivity(myAppsIntent);
				break;

			case R.id.btn_my_contents_ll2:
				Intent resourceintent = new Intent(context, ResourceManagementActivity.class);
				resourceintent.putExtra("MODE", NativeResourceConstant.DEF_MODE);
				startActivity(resourceintent);
				break;

			case R.id.btn_tools_ll2:
				startActivity(new Intent(context, ToolsActivity.class));
				break;

			case R.id.btn_settings_ll2:
				startActivity(new Intent(context, SettingsActivity.class));
				break;
		default:
			break;
		}
	}

	/**
	 * 提交修改信息
	* @Title: doModifyUserInfo 
	* @Description: TODO 
	* @param     
	* @return void
	 */
	public void doModifyUserInfo() {
		modifyNickName = etModifyNickname.getText().toString();
		nickName = tvInfoNickname.getText().toString();

		if (nickName.equals(modifyNickName)) {
			if (doModifyHeadPortraitIndex()) {
				ToastUtil.show(AccountActivity.this, R.string.modify_success, Toast.LENGTH_LONG);
			}
			showModifyLLAccountPart(false);

			//			enabledBtn() ;
		} else {
			if (isValidityNickNameEditInfo(modifyNickName)) {
				// 网络检测
				if (!NetworkUtils.isNetworkAvailable(this)) {
					ToastUtil
							.show(this, this.getResources().getString(R.string.network_canot_work), Toast.LENGTH_SHORT);
					return;
				}
				//				disEnabledBtn() ;
				if (isSubmit) {
					isSubmit = false;

					ProgressDialogUtil.openProgressDialog(context, ResourceUtil.getString(context, R.string.loading),
							ResourceUtil.getString(context, R.string.loading), false);
					ModifyUserInfoManager modifyUserInfoManager = new ModifyUserInfoManager(this, mHandler);
					modifyUserInfoManager.modifyNickName(user.getUserName(), modifyNickName);
					tvInfoNickname.setText(modifyNickName);
					showModifyLLAccountPart(false);
				}
				//				enabledBtn() ;
			}
		}

	}

	public boolean doModifyHeadPortraitIndex() {
		if (modifyHeadPortraitIndex != -1 && modifyHeadPortraitIndex != headPortraitIndex) {
			ZeroDataResourceHelper.saveZerodataHeadPortrait(this, modifyHeadPortraitIndex);
			//			modifyHeadPortraitIndex = headPortraitIndex;
			headPortraitIndex = modifyHeadPortraitIndex;
			return true;
		}
		return false;
	}

	/**
	 * 核查编辑框信息合法性
	 */
	private boolean isValidityNickNameEditInfo(String nickName) {
		modifyNickName = Utils.removeSpace(nickName);
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

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (view == etModifyNickname) {
			if (!hasFocus) {
				String nickName = etModifyNickname.getText().toString();
				isValidityNickNameEditInfo(nickName);
				//					tvNnClear.setVisibility(View.GONE) ;
			} else {
				//					tvNnClear.setVisibility(View.VISIBLE) ;
			}
		}
	}

	/* (非 Javadoc) 
	* <p>Title: onResume</p> 
	* <p>Description: </p>  
	* @see com.x.ui.activity.base.BaseActivity#onResume() 
	*/

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_MAIN, true);
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
	//		btnOk.setEnabled(true) ;
	//		btnLogout.setEnabled(true) ;
	//		tvModifyChangePwdLink.setEnabled(true) ;
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
	//		btnLogout.setEnabled(false) ;
	//		tvModifyChangePwdLink.setEnabled(false) ;
	//	}
	//	TextWatcher mNnTextWatcher = new TextWatcher() {  
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
	//	 
	//    
	//    public void controlTvNnClear()
	//    {
	//    	modifyNickName = etModifyNickname.getText().toString();
	//    	if(!TextUtils.isEmpty(modifyNickName))
	//    	{
	//    		tvNnClear.setVisibility(View.VISIBLE) ;
	//    	}else {
	//    		tvNnClear.setVisibility(View.GONE) ;
	//    	}
	//    }
	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().module(ModuleName.ACCOUNT_MAIN, false);
	}

	@Override
	protected void onDestroy() {
		Log.i("account","Destroy");
		super.onDestroy();
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, btnOk, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(context, btnLogout, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setTextViewColor(context, tvModifyChangePwdLink, SkinConstan.THEME_TEXT_LINK);
	}
}
