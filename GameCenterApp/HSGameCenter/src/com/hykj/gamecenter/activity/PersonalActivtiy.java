
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.CSAccountManager;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.controller.HelpRequest;
import com.hykj.gamecenter.controller.HelpRequest.IReqAccInfoSucceed;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.protocol.Pay.UserAccInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushConfig;

public class PersonalActivtiy extends Activity {
	protected static final String TAG = "PersonalActivtiy";
	// 更新账户信息设置最短时间间隔 5秒
	public static long CONSTANT_LAST_TIME_ACC;
	private CSCommonActionBar mActionBar;

	private Button mBtnLogin;
	private Button mBtnRecharge;

	private CSAccountManager mCsAccountManager;

	private UserInfoManager mUserInfoManager;

	private TextView mTextName;

	private TextView mTextBalance;

	private TextView mTextRechargeCount;

	private TextView mTextConsumeCount;

	private TextView mTextAccount;

	private HelpRequest mHelpRequest;

	private ImageView mImgPersonLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (App.getDevicesType() == App.PHONE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_persional_activtiy);
		SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
		mCsAccountManager = CSAccountManager.getInstance(this);
		mUserInfoManager = UserInfoManager.getInstance();
		mHelpRequest = new HelpRequest(new ControllCallback());
		initView();

		// 上报进入个人中心
		ReportedInfo builder = new ReportedInfo();
		builder.statActId = ReportConstants.STATACT_ID_PERSON;
		builder.statActId2 = 1;
		ReportConstants.getInstance().reportReportedInfo(builder);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mActionBar.setSettingTipVisible(UpdateUtils.hasUpdate() ? View.VISIBLE
				: View.GONE);
		if (mCsAccountManager.hasCSAccount()) {
			mUserInfoManager.initUserInfo();
			AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
			if (accountInfo == null) {
				// 有账户 但是 无效
				mCsAccountManager.removeCSAccount();
			} else {
				// gotoAccountCenter( );
				// 执行 当前账户信息的初始化
				initLoginText(mUserInfoManager.getCSUserInfo().getAccInfo(), accountInfo);
				//                //若更新间隔少于五秒则不更新
				//                long current = System.currentTimeMillis();
				//                if (current - CONSTANT_LAST_TIME_ACC < 5000) {
				//                    return;
				//                }
				//                CONSTANT_LAST_TIME_ACC = current;
				mHelpRequest
						.reqUserAccInfo(accountInfo.openId/* OPENID */, accountInfo.token/* TOKEN */);
				return;
			}

		}
		initLoginText(null, null);
	}

	private int clickTime = 0;

	private void initView() {
		// init actionbar
		mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
		mActionBar.SetOnActionBarClickListener(actionBarListener);
		//        mActionBar.setSettingTipVisible(View.GONE);
		mActionBar.setSettingImage(R.drawable.csls_comm_actionbar_newset_icon);
		mActionBar.setTitle(getString(R.string.persional_center));

		// init layout view
		findViewById(R.id.layoutBalance).setOnClickListener(mViewOnlickListen);
		findViewById(R.id.layoutRechargeHistory).setOnClickListener(mViewOnlickListen);
		findViewById(R.id.layoutConsume).setOnClickListener(mViewOnlickListen);
		findViewById(R.id.layoutAccountManage).setOnClickListener(mViewOnlickListen);
		findViewById(R.id.layoutUserFeedback).setOnClickListener(mViewOnlickListen);
		findViewById(R.id.layoutPersonLogin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickTime >= 10) {
					ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					clipboard.setPrimaryClip(ClipData.newPlainText(null, XGPushConfig.getToken(PersonalActivtiy.this)));
					CSToast.show(PersonalActivtiy.this, "token 已复制到剪贴板");
				} else {
					clickTime++;
				}
			}
		});

		// init button
		mBtnLogin = (Button) findViewById(R.id.btnLogin);
		mBtnLogin.setOnClickListener(mViewOnlickListen);
		mBtnRecharge = (Button) findViewById(R.id.btnRecharge);
		mBtnRecharge.setOnClickListener(mViewOnlickListen);

		// init text
		mTextName = (TextView) findViewById(R.id.textName);
		mTextBalance = (TextView) findViewById(R.id.textBalance);
		mTextRechargeCount = (TextView) findViewById(R.id.textRechargeCount);
		mTextConsumeCount = (TextView) findViewById(R.id.textConsumeCount);
		mTextAccount = (TextView) findViewById(R.id.textAccount);
		mImgPersonLogo = (ImageView) findViewById(R.id.imgPersonLogo);
		mImgPersonLogo.setOnClickListener(mViewOnlickListen);
	}

	/**
	 * 初始化 登录 状态 和未登录状态 的显示 信息 accInfo 和 accountInfo 只要有个 为 null 表示 未
	 *
	 * @param accInfo     null 则显示 为 未登录状态,否则 为登录状态
	 * @param accountInfo null
	 */
	private void initLoginText(UserAccInfo accInfo, AccountInfo accountInfo) {
		if (accInfo != null && accountInfo != null) {
			mTextName.setText(accountInfo.nickName/* userName */);
			mTextName.setVisibility(View.VISIBLE);
			mBtnLogin.setVisibility(View.GONE);
			String url = accountInfo.headImgUrl;
			// if (url != null && url.length() > 0) {
			ImageLoader.getInstance().displayImage(url, mImgPersonLogo,
					DisplayOptions.optionsLoginIcon);
			// }else{
			// mImgPersonLogo.setBackgroundResource(R.drawable.img_person_logined);
			// }
			mTextRechargeCount.setText(String.valueOf(accInfo.rechargeCount));
			mTextConsumeCount.setText(String.valueOf(accInfo.consumeCount));
			mTextBalance
					.setText(String.format(getString(R.string.person_balance), accInfo.newCoin));
			mTextAccount.setText(accountInfo.nickName);
		} else {
			mTextName.setText("");
			mTextName.setVisibility(View.GONE);
			mBtnLogin.setVisibility(View.VISIBLE);
			// mImgPersonLogo.setBackgroundResource(R.drawable.img_person_login);
			ImageLoader.getInstance().displayImage("", mImgPersonLogo,
					DisplayOptions.optionsNotLoginIcon);
			mTextRechargeCount.setText("0");
			mTextConsumeCount.setText("0");
			mTextBalance.setText(String.format(getString(R.string.person_balance), "0"));
			mTextAccount.setText("");
		}
		//消费记录，充值记录为0时，不显示
		if (mTextRechargeCount.getText().equals("0")) {
			mTextRechargeCount.setText("");
		}
		if (mTextConsumeCount.getText().equals("0")) {
			mTextConsumeCount.setText("");
		}
	}

	private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

		@Override
		public void onActionBarClicked(int position, View view) {

			switch (position) {
				case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
					onBackPressed();
					break;
				case CSCommonActionBar.OnActionBarClickListener.SETTING_BNT:
					Intent intentSetting = new Intent(PersonalActivtiy.this,
							SettingListActivity.class);
					startActivity(intentSetting);
				default:
					break;
			}
		}
	};

	class ControllCallback implements IReqAccInfoSucceed {

		@Override
		public void onReqUserAccInfoSucceed(UserAccInfo accInfo) {
			// TODO Auto-generated method stub
			mUserInfoManager.setAccInfo(accInfo);

			mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ACCINFOSUCCEED);
			Logger.i(TAG, "onReqUserAccInfoSucceed " + accInfo, "oddshou");
		}

		@Override
		public void onNetError(int errCode, String errorMsg) {
			// TODO Auto-generated method stub
			Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
			mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ERROR);
		}

		@Override
		public void onReqFailed(int statusCode, String errorMsg) {
			// TODO Auto-generated method stub
			Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
			Message msg = mHandler.obtainMessage();
			msg.what = HelpRequest.MSG_REQ_FAILED;
			msg.arg1 = statusCode;
			msg.obj = errorMsg;
			mHandler.sendMessage(msg);
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case HelpRequest.MSG_REQ_ACCINFOSUCCEED:
					initLoginText(mUserInfoManager.getCSUserInfo().getAccInfo(), mUserInfoManager
							.getCSUserInfo().getAccountInfo());
					break;
				case HelpRequest.MSG_REQ_FAILED:
					if (msg.arg1 == 102) { // 非法用户
						CSAccountManager.getInstance(PersonalActivtiy.this).removeCSAccount();
						initLoginText(null, null);
						//                        failed102();
					} else {
						CSToast.showFailed(PersonalActivtiy.this, msg.arg1, msg.obj.toString());
					}
					break;
				case HelpRequest.MSG_REQ_ERROR:
					CSToast.showError(PersonalActivtiy.this);
					break;

				default:
					break;
			}

		}

	};
	//    private CSAlertDialog errorDialog;

	//    private void failed102(){
	//        if (null == errorDialog) {
	//            errorDialog = new CSAlertDialog(this,
	//                    getString(R.string.tips_error_openid),
	//                    false);
	//        }
	//        errorDialog.setTitle(getString(R.string.title_account_exit));
	//        errorDialog.setmLeftBtnTitle(getString(R.string.exit));
	//        errorDialog.setmRightBtnTitle(getString(R.string.re_login));
	//        errorDialog.addRightBtnListener(new OnClickListener() {
	//            @Override
	//            public void onClick(View v) {
	//                // TODO Auto-generated method stub
	//                //进入登录页面。。。
	//                Intent intent = new Intent();
	//                intent.setClass(PersonalActivtiy.this, PersonLogin.class);
	//                PersonalActivtiy.this.startActivity(intent);
	//                errorDialog.dismiss();
	//            }
	//
	//        });
	//        errorDialog.addLeftBtnListener(new OnClickListener() {
	//
	//            @Override
	//            public void onClick(View v) {
	//                // TODO Auto-generated method stub
	//                //返回个人中心
	//                errorDialog.dismiss();
	//            }
	//        });
	//        // mAccountErrorDialog.setCanceledOnTouchOutside(true);
	//        errorDialog.show();
	//    }

	private OnClickListener mViewOnlickListen = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// CSToast.show(PersonalActivtiy.this, "click " + v.getId());
			final Intent intent = new Intent();
			switch (v.getId()) {
				case R.id.layoutRechargeHistory: // 充值记录
					if (mCsAccountManager.hasCSAccount()) {
						intent.setClass(PersonalActivtiy.this, RechargeListActivity.class);
						PersonalActivtiy.this.startActivity(intent);
					} else {
						intent.setClass(PersonalActivtiy.this, PersonLogin.class);
						intent.putExtra(StatisticManager.KEY_LOGIN_CLASS,
								RechargeListActivity.class.getName());
						PersonalActivtiy.this.startActivity(intent);
					}
					break;
				case R.id.layoutConsume: // 消费记录
					if (mCsAccountManager.hasCSAccount()) {
						Intent consumeHistoryIntent = new Intent(PersonalActivtiy.this,
								ConsumeListActivity.class);
						startActivity(consumeHistoryIntent);
					} else {
						intent.setClass(PersonalActivtiy.this, PersonLogin.class);
						intent.putExtra(StatisticManager.KEY_LOGIN_CLASS,
								ConsumeListActivity.class.getName());
						PersonalActivtiy.this.startActivity(intent);
					}
					break;
				case R.id.btnLogin: // 快速登录
					intent.setClass(PersonalActivtiy.this, PersonLogin.class);
					PersonalActivtiy.this.startActivity(intent);
					break;
				case R.id.btnRecharge: // 账户充值
					if (mCsAccountManager.hasCSAccount()) {
						intent.setClass(PersonalActivtiy.this, RechargeActivity.class);
						PersonalActivtiy.this.startActivity(intent);
					} else {
						intent.setClass(PersonalActivtiy.this, PersonLogin.class);
						intent.putExtra(StatisticManager.KEY_LOGIN_CLASS,
								RechargeActivity.class.getName());
						PersonalActivtiy.this.startActivity(intent);
					}
					break;
				case R.id.layoutBalance:
					if (mCsAccountManager.hasCSAccount()) {
						intent.setClass(PersonalActivtiy.this, RechargeActivity.class);
						PersonalActivtiy.this.startActivity(intent);
					} else {
						intent.setClass(PersonalActivtiy.this, PersonLogin.class);
						intent.putExtra(StatisticManager.KEY_LOGIN_CLASS,
								RechargeActivity.class.getName());
						PersonalActivtiy.this.startActivity(intent);
					}
					break;
				case R.id.layoutAccountManage: // 账号管理, 当前退出账号
					if (mCsAccountManager.hasCSAccount()) {
						Intent accManagerIntent = new Intent(PersonalActivtiy.this,
								AccountManagerActivity.class);
						startActivity(accManagerIntent);
					} else {
						intent.setClass(PersonalActivtiy.this, PersonLogin.class);
						// intent.putExtra(StatisticManager.KEY_LOGIN_CLASS,
						// AccountManagerActivity.class.getName());
						PersonalActivtiy.this.startActivity(intent);
					}
					break;
				case R.id.imgPersonLogo:
					if (mCsAccountManager.hasCSAccount()) {
						Intent accManagerIntent = new Intent(PersonalActivtiy.this,
								AccountManagerActivity.class);
						startActivity(accManagerIntent);
					} else {
						intent.setClass(PersonalActivtiy.this, PersonLogin.class);
						// intent.putExtra(StatisticManager.KEY_LOGIN_CLASS,
						// AccountManagerActivity.class.getName());
						PersonalActivtiy.this.startActivity(intent);
					}
					break;
				case R.id.layoutUserFeedback:
					Intent feedBackIntent = new Intent(PersonalActivtiy.this, FeedbackActivity.class);
					startActivity(feedBackIntent);
					break;
				default:
					break;
			}
		}
	};
}
