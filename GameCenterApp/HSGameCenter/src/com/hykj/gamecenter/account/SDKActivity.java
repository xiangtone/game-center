
package com.hykj.gamecenter.account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.App.TimeRun;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.Html5HelpActivity;
import com.hykj.gamecenter.activity.RechargeActivity;
import com.hykj.gamecenter.controller.HelpRequest;
import com.hykj.gamecenter.controller.HelpRequest.IReqAccInfoSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqAppRolesSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqBindSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqConsumeSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqOpenIdSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqThirdLoginSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqValidateSucceed;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.protocol.Pay.RspConsume;
import com.hykj.gamecenter.protocol.Pay.UserAccInfo;
import com.hykj.gamecenter.protocol.Pay.UserAppRoleInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.sdk.entry.ConsumeRequestInfo;
import com.hykj.gamecenter.sdk.entry.NiuAppEntry;
import com.hykj.gamecenter.sdk.entry.NiuOrderInfo;
import com.hykj.gamecenter.sdk.entry.NiuSDKConstant;
import com.hykj.gamecenter.sdk.entry.NiuSDKRoleInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSAlertDialog;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.AccessTokenKeeper;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PackageUtil;
import com.hykj.gamecenter.utils.SmsContent;
import com.hykj.gamecenter.utils.StringUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class SDKActivity extends AccountAuthenticatorActivity implements TimeRun {

    // 从SDK过来的请求实际只有2个：登陆 和 消费
    public static final int SDK_REQUEST_LOGIN = 1001;
    public static final int SDK_REQUEST_PAY = 1002;
    private static final String TAG = "SDKActivity";
    private int mRequestMode;
    private CSAccountManager mCSAccountManager;
    private NiuAppEntry mAppEntry;
    private UserInfoManager mUserInfoManager;
    private int mAppType;
    private NiuOrderInfo mAppOrderInfo;
    private NiuSDKRoleInfo mSDKRoleInfo;
    // 快速登录界面
    private ViewStub mViewstubPersonLogin;
    private View mViewstubPersonLoginView;
    private EditText mEditLoginAccout;
    private EditText mEditCaptcha;
    private Button mBtnCaptcha;
    private Button mBtnLogin;
    private ProgressBar mProgressLogin;
    private HelpRequest mHelpRequest;

    private boolean finishWithCallback = false;
    // 支付确认及余额不足
    private ViewStub mViewstubConsume;
    private View mViewstubConsumeView;
    private Button mBtnPayRecharge;
    private ProgressBar mProgressPayRecharge;
    private TextView mTextAppName;
    private TextView mTextTitle;
    private TextView mTextPrice;
    private TextView mTextBalance;
    private ImageView mImgLoginClose;
    private ImageView mImgPayRechargeClose;
    private CSAlertDialog mAccountErrorDialog;
    /**
     * sdk openid 不同于当前账号, 正在重新登录
     */
    private boolean mIsReloginIng = false;
    /**
     * 充值完成或者 重新登录一个账号后 会进行 updateAccInfo
     */
    private boolean mIsUpdateAccInfo = false; // 支付过程中, 正在更新当前账户信息
    private SmsContent mSmsContent;
    private TextView mTextUserAgreement;
    private boolean mIsUserAggrement = false; //跳转到 用户协议界面
    private ImageView mImgWeibo;
    private ImageView mImgMm;
    private ImageView mImgQq;
    private IWXAPI mWeixinAPI;
    private boolean mIsLoginMm;
    private boolean mIsThirdLogin;
    private View mLayoutLoading; // loading View
    private Tencent mTencent;
    // weibo
    private WeiboAuth mWeiboAuth;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk);

        init();
    }
    private void init() {
        mCSAccountManager = CSAccountManager.getInstance(this);
        mUserInfoManager = UserInfoManager.getInstance();
        mAppEntry = getIntent().getParcelableExtra("app");
        mAppType = mAppEntry.getAppType();
        // ******************debugMode 没有设置
        // ServerUrl.setDebugMode( mAppEntry.getDevMode( ) );
        Logger.e(TAG, "SDKActivity" + mAppEntry.getDevMode() + "");
        mRequestMode = getIntent().getIntExtra("sdk", SDK_REQUEST_LOGIN);
        Logger.e(TAG, "请求的界面是:" + mRequestMode);
        // 支付请求
        if (mRequestMode == SDK_REQUEST_PAY)
        {
            mAppOrderInfo = getIntent().getParcelableExtra(
                    "order");
            mSDKRoleInfo = getIntent().getParcelableExtra(
                    "roleinfo");
            Logger.i(TAG, "支付请求 sdk roleInfo " + mSDKRoleInfo, "oddshou");
        }
        mHelpRequest = new HelpRequest(new ControllCallback());
        setUpView();
        mSmsContent = new SmsContent(this, new Handler(), mEditCaptcha);
        // initialLoginView();

        if (mRequestMode == SDK_REQUEST_LOGIN) {
            //上报启动游戏
            //上报启动游戏
            ReportedInfo builder = new ReportedInfo();
            builder.statActId = ReportConstants.STATACT_ID_GAME_START;
            builder.ext1 = mAppEntry.getAppId() + "";
            builder.ext2 = mAppEntry.getAppPackageName();
            ReportConstants.getInstance().reportReportedInfoNow(builder, null);
        }
    }
    
    //oddshou onNewIntent 执行流程 onNewIntent，onRestart，onStart，onResume 
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
//        super.onNewIntent(intent);
        //这行必不可少，否则后面 getIntent 将错误
        setIntent(intent);
        init();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Logger.e(TAG, "onResume", "oddshou");
        if (mIsRecharging) {
            mIsRecharging = false;
            updateAccInfo();
        }

        if (mViewstubConsume != null && mViewstubConsumeView != null) {
            initConsumeInfo();
        }

        if (mIsUserAggrement) {
            mIsUserAggrement = false;
        }

        if (mIsLoginMm) {
            mIsLoginMm = false;
            String mmcode = App.getSharedPreference().getString(StatisticManager.KEY_MM_CODE, "");
            if (mmcode.length() > 0) {
                // 表示授权成功
                //                 mLayoutLoading.setVisibility(View.VISIBLE);
                getWebContent(
                        String
                                .format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                                        StatisticManager.getWeixinAppId(),
                                        StatisticManager.getWeixinAppSecret(), mmcode), TAG_WEIXIN_ID);
            } else {
                mHandler.sendEmptyMessage(MSG_LOADING_END);
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        getContentResolver().unregisterContentObserver(mSmsContent);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Logger.e("SDKA", "finishWithCallback:" + finishWithCallback);
        if (!finishWithCallback && !mIsRecharging && !mIsUserAggrement && !mIsThirdLogin
                && !mIsLoginMm)
        {
            sendCancelCallback();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        sendCancelCallback();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (mTencent != null) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void setUpView()
    {

        mLayoutLoading = findViewById(R.id.layoutLoading);
        if (mRequestMode == SDK_REQUEST_LOGIN)// 登录界面显示
        {
            if (mCSAccountManager.hasCSAccount())
            {
                mUserInfoManager.initUserInfo();
                if (mUserInfoManager.getCSUserInfo().getAccountInfo() == null)
                {
                    mCSAccountManager.removeCSAccount();
                    initialLoginView(false);
                }
                else
                {
                    // 根据游戏模式来决定是去角色选择（网游模式）还是直接返回（单机/应用模式）
                    // if (mAppType == NiuSDKConstant.GAME_TYPE_OFFLINE)
                    // {
                    // getAppRoles();
                    // }
                    // else
                    // {
                    // goToAppRoleView();
                    // goToOnlineRoleLogin( );
                    getAppRoles();
                    // }
                }
            }
            else
            { // 没有登录账号时，显示登陆界面
                initialLoginView(false);
            }
        }
        else if (mRequestMode == SDK_REQUEST_PAY)// 支付界面显示
        {
            if (mCSAccountManager.hasCSAccount())
            {
                mUserInfoManager.initUserInfo();
                if (mUserInfoManager.getCSUserInfo().getAccountInfo() == null)
                {
                    CSToast.show(this, getString(R.string.main_sf_login_hint));
                    mCSAccountManager.removeCSAccount();
                    //                    sendCancelCallback();
                    //                    finish();
                    preCheckRoleInfo();
                }
                else
                {
                    // initialPayView();
                    preCheckRoleInfo();
                }
            }
            else
            {
                // 没有账号时，显示登陆界面(正常逻辑下，此处不应该到达
                // 更新：显示登陆界面后续逻辑复杂，直接提示后退出20130718)
                // initialLoginView();
                //                CSToast.show(this, getString(R.string.main_sf_login_hint));
                //                sendCancelCallback();
                //                finish();
                preCheckRoleInfo();
            }
        }
    }

    private void initialPayView() {
        // TODO Auto-generated method stub
        if (mViewstubPersonLoginView != null
                && mViewstubPersonLoginView.getVisibility() == View.VISIBLE) {
            mViewstubPersonLoginView.setVisibility(View.GONE);
            mLayoutLoading.setVisibility(View.GONE);
        }
        if (mViewstubConsume == null)
        {
            mViewstubConsume = (ViewStub) findViewById(R.id.viewstubConsume);
            mViewstubConsumeView = mViewstubConsume.inflate();
            mTextAppName = (TextView) mViewstubConsumeView.findViewById(R.id.textAppName);
            mTextTitle = (TextView) mViewstubConsumeView.findViewById(R.id.textTitle);
            mTextPrice = (TextView) mViewstubConsumeView.findViewById(R.id.textPrice);
            mTextBalance = (TextView) mViewstubConsumeView.findViewById(R.id.textBalance);
            mBtnPayRecharge = (Button) mViewstubConsumeView.findViewById(R.id.btnPayRecharge);
            mBtnPayRecharge.setOnClickListener(mOnclickListen);
            mImgPayRechargeClose = (ImageView) mViewstubConsumeView
                    .findViewById(R.id.imgPayRechargeClose);
            mImgPayRechargeClose.setOnClickListener(mOnclickListen);
            mProgressPayRecharge = (ProgressBar) mViewstubConsumeView
                    .findViewById(R.id.progressPayRecharge);
        }
        mViewstubConsumeView.setVisibility(View.VISIBLE);
        initConsumeInfo();
    }

    private void initConsumeInfo() {
        // 查看账户余额是否足够
        CSUserInfo userInfo = mUserInfoManager.getCSUserInfo();
        UserAccInfo accInfo = userInfo.getAccInfo();
        if (accInfo.newCoin >= mAppOrderInfo.getValue()) {
            // 余额充足
            mTextAppName.setText(mAppOrderInfo.getItemName());
            mTextPrice.setText(mAppOrderInfo.getValue() + " "
                    + this.getApplicationContext().getResources()
                            .getString(R.string.recharge_item_count_unit));
            mTextTitle
                    .setText(getString(R.string.pay_charge, userInfo.getAccountInfo().nickName/*userInfo.getAccountInfo().userName*/));
            mTextBalance.setText(accInfo.newCoin + " "
                    + this.getApplicationContext().getResources()
                            .getString(R.string.recharge_item_count_unit));
            mBtnPayRecharge.setText(getString(R.string.pay_right));
            mBtnPayRecharge.setTag(Integer.valueOf(1));
        } else {
            // 余额不足
            mTextAppName.setText(mAppOrderInfo.getItemName());
            mTextPrice.setText(mAppOrderInfo.getValue() + " "
                    + this.getApplicationContext().getResources()
                            .getString(R.string.recharge_item_count_unit));
            mTextTitle.setText(getString(R.string.balance_not_enough,
                    userInfo.getAccountInfo().nickName/*userInfo.getAccountInfo().userName*/));
            mTextBalance.setText(accInfo.newCoin + " "
                    + this.getApplicationContext().getResources()
                            .getString(R.string.recharge_item_count_unit));
            mBtnPayRecharge.setText(getString(R.string.recharge_now));
            mBtnPayRecharge.setTag(Integer.valueOf(0));
        }
    }

    private void updateAccInfo() {
        mIsUpdateAccInfo = true;
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        mHelpRequest.reqUserAccInfo(accountInfo.openId, accountInfo.token);
    }

    private void getAppRoles() {
        // TODO Auto-generated method stub
        // initialLoginView();
        // 请求角色信息 返回给 sdk
        CSUserInfo userInfo = mUserInfoManager.getCSUserInfo();
        AccountInfo accountInfo = userInfo.getAccountInfo();
        Logger.i(TAG, "goToSingleRoleLogin " + mAppEntry.getAppId() + "  " + mAppEntry.getAppKey());
        mHelpRequest.reqAppRoles(accountInfo.openId, accountInfo.token, mAppEntry.getAppId(),
                mAppEntry.getAppKey());

    }

    private void sendCancelCallback()
    {
        try
        {
            if (mAppEntry != null && mAppEntry.getCallback() != null)
            {
                mAppEntry.getCallback().callback(NiuSDKConstant.SDK_USER_CANCEL, new Bundle());
                Logger.e(TAG, "用户自行取消", "oddshou");
                finishWithCallback = true;
            }
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    // 处理注册成功后
    private void handleLogin()
    {
        final Account account = new Account(getString(R.string.cs_accountname),
                getString(R.string.cs_accounttype));
        AccountManager mAccountManager = AccountManager.get(this);
        mAccountManager.addAccountExplicitly(account, "pwd", null);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, getString(R.string.cs_accountname));
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.cs_accounttype));
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        mCSAccountManager.updateAccountUserData(UserInfoManager.getInstance().getCSUserInfo()
                .getAccountInfo());
    }

    /**
     * 刷新 发送验证码 按钮状态
     * 
     * @param count 剩余 秒数
     */
    private void initialCaptchaBtn(int count) {
        if (count <= 0) {
            mBtnCaptcha.setEnabled(true);
            mBtnCaptcha.setTextColor(getResources().getColor(R.color.action_blue_color));
            mBtnCaptcha.setText(R.string.get_captcha);
        }
        else {
            mBtnCaptcha.setEnabled(false);
            mBtnCaptcha.setTextColor(getResources().getColor(R.color.csl_black_4c));
            mBtnCaptcha.setText(String.format(getString(R.string.get_captcha_after), count));
        }
    }

    /**
     * @param isReLogin 参数暂未使用
     */
    private void initialLoginView(boolean isReLogin) {
        // TODO Auto-generated method stub
        if (mViewstubPersonLogin == null)
        {
            mViewstubPersonLogin = (ViewStub) findViewById(R.id.viewstubPersonLogin);
            mViewstubPersonLoginView = mViewstubPersonLogin.inflate();
            mEditLoginAccout = (EditText) mViewstubPersonLoginView
                    .findViewById(R.id.editLoginAccout);
            String defaultMobile = App.getSharedPreference().getString(StatisticManager.KEY_MOBILE,
                    "");
            if (defaultMobile.length() == 11) {
                mEditLoginAccout.setText(defaultMobile);
            }
            mEditCaptcha = (EditText) mViewstubPersonLoginView.findViewById(R.id.editCaptcha);
            mBtnCaptcha = (Button) mViewstubPersonLoginView.findViewById(R.id.btnCaptcha);
            mBtnCaptcha.setOnClickListener(mOnclickListen);
            mBtnLogin = (Button) mViewstubPersonLoginView.findViewById(R.id.btnLogin);
            mBtnLogin.setOnClickListener(mOnclickListen);
            mImgLoginClose = (ImageView) mViewstubPersonLoginView.findViewById(R.id.imgLoginClose);
            mImgLoginClose.setOnClickListener(mOnclickListen);
            mProgressLogin = (ProgressBar) mViewstubPersonLoginView
                    .findViewById(R.id.progressLogin);
            mTextUserAgreement = (TextView) findViewById(R.id.textUserAgreement);
            String text = mTextUserAgreement.getText().toString();
            if (text != null) {
                StringUtils.setTextColor(text.indexOf('《'), text.indexOf('》') + 1,
                        getResources().getColor(R.color.normal_blue), mTextUserAgreement);
            }
            mTextUserAgreement.setOnClickListener(mOnclickListen);
            // weibo qq mm login view init
            mImgWeibo = (ImageView) findViewById(R.id.imgWeibo);
            mImgWeibo.setOnClickListener(mOnclickListen);
            mImgQq = (ImageView) findViewById(R.id.imgQq);
            mImgQq.setOnClickListener(mOnclickListen);
            mImgMm = (ImageView) findViewById(R.id.imgMm);
            mImgMm.setOnClickListener(mOnclickListen);

        }
        mViewstubPersonLoginView.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.GONE);

        App app = App.getAppContext();
        app.setmTimeListen(SDKActivity.this);
        initialCaptchaBtn(app.getmCurrentTimer());

        // 微信注册
        mWeixinAPI = WXAPIFactory.createWXAPI(this, StatisticManager.getWeixinAppId(), false);
        mWeixinAPI.registerApp(StatisticManager.getWeixinAppId());
        // qq
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(StatisticManager.getQqAppId(), this.getApplicationContext());
        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        // 微博
        // 创建微博实例
        mWeiboAuth = new WeiboAuth(this, StatisticManager.getWeiboAppkey(),
                StatisticManager.WEIBO_REDIRECTURI, StatisticManager.WEIBO_SCOPE);
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
    }

    // qq login
    public void qqLogin()
    {
        mIsThirdLogin = true;
        mTencent = Tencent.createInstance(StatisticManager.getQqAppId(), this.getApplicationContext());

//        if (PackageUtil.existApk("com.tencent.mobileqq", this)) {
//            mHandler.sendEmptyMessage(MSG_LOADING_START); //显示 loading
//            mTencent.login(this, StatisticManager.QQ_SCOPE, new BaseUiListener());
//        } else {
//            CSToast.showNormal(this, getString(R.string.toast_not_install_qq));
//        }

        if (!PackageUtil.existApk("com.tencent.mobileqq", this)) {
            CSToast.showNormal(this, getString(R.string.toast_not_install_qq));
        } else {
            mHandler.sendEmptyMessage(MSG_LOADING_START); // 显示 loading
            mTencent.login(this, StatisticManager.QQ_SCOPE, new BaseUiListener());
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onError(UiError e) {
            Logger.e(TAG, "BaseUiListener " + "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail, "oddshou");
            mIsThirdLogin = false;
            mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
        }

        @Override
        public void onCancel() {
            // showResult("onCancel", "");
            Logger.e(TAG, "BaseUiListener cancel", "oddshou");
            mIsThirdLogin = false;
            mHandler.sendEmptyMessage(MSG_LOADING_END);
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            // doComplete(arg0);
            mIsThirdLogin = false;
            getUserInfo();
        }
    }

    private void getUserInfo()
    {
        QQToken qqToken = mTencent.getQQToken();
        UserInfo info = new UserInfo(getApplicationContext(), qqToken);
        info.getUserInfo(new IUiListener() {

            @Override
            public void onError(UiError e) {
                // TODO Auto-generated method stub
                Logger.e(TAG, "BaseUiListener " + "code:" + e.errorCode + ", msg:"
                        + e.errorMessage + ", detail:" + e.errorDetail, "oddshou");
                mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                Logger.i(TAG, "onComplete", "oddshou");
                try {
                    JSONTokener jsonParser = new JSONTokener(arg0.toString());
                    JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
                    String headImgurl = mmInfo.getString("figureurl_qq_2");
                    String nickName = mmInfo.getString("nickname");
                    String gender = mmInfo.getString("gender");
                    int sex = 0;
                    if (gender.equalsIgnoreCase("男")) {
                        sex = 1;
                    } else if (gender.equalsIgnoreCase("女")) {
                        sex = 2;
                    }
                    mHandler.sendEmptyMessage(MSG_LOADING_START);
                    mHelpRequest.reqThirdLogin(mTencent.getOpenId(), StatisticManager.ThridSource.QQ, nickName, headImgurl, 0,
                            sex, StatisticManager.RESOURCE_INT_SDK);
                    Logger.i(TAG, "onComplete " + mmInfo, "oddshou");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Logger.e(TAG, "BaseUiListener cancel", "oddshou");
                mHandler.sendEmptyMessage(MSG_LOADING_END);
            }
        });
    }

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            mIsThirdLogin = false;
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                //                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(SDKActivity.this, mAccessToken);
                //                Toast.makeText(SDKActivity.this,
                //                        "授权成功", Toast.LENGTH_SHORT).show();
                Logger.i(TAG, "onComplete 授权成功", "oddshou");
                //获取用户信息并登录
                String url = String.format(
                        "https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s",
                        mAccessToken.getToken(), mAccessToken.getUid());
                //注意 微博请求没有执行 onstart##############################oddshou 
                mHandler.sendEmptyMessage(MSG_LOADING_START);
                getWebContent(url, TAG_WEIBO_INFO);
            } else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                //                Toast.makeText(SDKActivity.this, message, Toast.LENGTH_LONG).show();
                Logger.i(TAG, "onComplete " + message, "oddshou");
                mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
            }
        }

        @Override
        public void onCancel() {
            //            Toast.makeText(SDKActivity.this,
            //                    "取消授权", Toast.LENGTH_LONG).show();
            Logger.e(TAG, "onCancel 取消授权", "oddshou");
            mIsThirdLogin = false;
            mHandler.sendEmptyMessage(MSG_LOADING_END);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            //            Toast.makeText(SDKActivity.this,
            //                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            Logger.e(TAG, "Auth exception " + e.getMessage(), "oddshou");
            mIsThirdLogin = false;
            mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
        }
    }

    private void loginWithWeixin() {
        if (mWeixinAPI == null) {
            mWeixinAPI = WXAPIFactory.createWXAPI(this, StatisticManager.getWeixinAppId(), false);
        }

        if (!mWeixinAPI.isWXAppInstalled()) {
            // 提醒用户没有按照微信
            CSToast.showNormal(this, getString(R.string.toast_not_install_mm));
            return;
        }

        mWeixinAPI.registerApp(StatisticManager.getWeixinAppId());
        SendAuth.Req req = new SendAuth.Req();
        req.scope = StatisticManager.WEIXIN_SCOPE;
        req.state = StatisticManager.WEIXIN_STATE;
        mIsLoginMm = true;
        mHandler.sendEmptyMessage(MSG_LOADING_START); //显示 loading
        mWeixinAPI.sendReq(req);
    }

    private void handleRoleLogin(UserAppRoleInfo info)
    {
        CSUserInfo userInfo = mUserInfoManager.getCSUserInfo();
        NiuSDKRoleInfo roleInfo = new NiuSDKRoleInfo();
        roleInfo.setuId(userInfo.getAccountInfo().openId);
        roleInfo.setRoleId(info.roleId);
        roleInfo.setRoleToken(info.roleToken);
        roleInfo.setuName(/*userInfo.getAccountInfo().userName*/userInfo.getAccountInfo().nickName);
        roleInfo.setRoleName("");
        roleInfo.setuToken("");
        Logger.i(TAG, "handleRoleLogin roleInfo " + roleInfo, "oddshou");
        try
        {
            if (mAppEntry != null && mAppEntry.getCallback() != null)
            {
                Bundle bundle = new Bundle();
                bundle.putParcelable("roleinfo", roleInfo);
                Logger.e(TAG, "mAppEntry.getCallback( )=" + mAppEntry.getCallback());
                mAppEntry.getCallback().callback(0, bundle);
                finishWithCallback = true;
            }
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private void handlePayResult(int resCode)
    {
        try
        {
            if (mAppEntry != null && mAppEntry.getCallback() != null)
            {
                if (resCode == 0)
                {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("orderinfo", mAppOrderInfo);
                    mAppEntry.getCallback().callback(0, bundle);
                    // showToast(getString(R.string.msg_pay_success));
                }
                else
                {
                    // if (resCode == CONSUME_STATUS.LACK_BALANCE)
                    // {
                    // LogUtils.e("error,余额不足！");
                    // mAppEntry.getCallback().callback(resCode, new Bundle());
                    // }
                    // else
                    // {
                    mAppEntry.getCallback().callback(resCode, null);
                    // showToast(getString(R.string.msg_pay_fail));
                    // }

                }
                finishWithCallback = true;
            }
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }

        // App.getAppContext( ).needRefreshConsumeRecorder( );

        finish();
    }

    /**
     * 预处理支付信息,验证
     * <p>
     * 1.是否包含RoleInfo
     * <p>
     * 2.sdk 中的 openid 是否与当前账号匹配
     */
    private void preCheckRoleInfo()
    {
        // 检测是否有合法的
        if (mSDKRoleInfo == null)
        {
            sendCancelCallback();
            finish();
            return;
        }
        AccountInfo account = mUserInfoManager.getCSUserInfo().getAccountInfo();
        String openId = "";
        if (account != null) {
            openId = mUserInfoManager.getCSUserInfo().getAccountInfo().openId;
        }
        //当前已经退出账号
        if (account == null || TextUtils.isEmpty(openId)) {
            if (mViewstubPersonLoginView != null
                    && mViewstubPersonLoginView.getVisibility() == View.VISIBLE) {
                mViewstubPersonLoginView.setVisibility(View.GONE);
            }
            showAccountErrorDialog(R.string.consume_error_title2);
        }

        // 用户已不同，需要重新登录
        else if (!mSDKRoleInfo.getuId().equals(openId))
        {
            // 账户不匹配，从新登陆 或者返回游戏
            // initialPayView();
            // mViewstubConsume.setVisibility(View.GONE);
            if (mViewstubPersonLoginView != null
                    && mViewstubPersonLoginView.getVisibility() == View.VISIBLE) {
                mViewstubPersonLoginView.setVisibility(View.GONE);
            }
            showAccountErrorDialog(R.string.consume_error_title);
        }
        else
        {
            // 弹出支付框
            // initialPayView();
            updateAccInfo();
        }
    }

    private void preCheckLogin() {

    }

    private void showAccountErrorDialog(int dialogTips) {
        if (null == mAccountErrorDialog) {
            mAccountErrorDialog = new CSAlertDialog(this,
                    getString(R.string.consume_error_title, mSDKRoleInfo.getuName()),
                    false, false);
        }
        mAccountErrorDialog.setTips(getString(dialogTips, mSDKRoleInfo.getuName()));
        mAccountErrorDialog.setTitle(getString(R.string.tips));
        mAccountErrorDialog.setmLeftBtnTitle(getString(R.string.back_game));
        mAccountErrorDialog.setmRightBtnTitle(getString(R.string.re_login));
        mAccountErrorDialog.addRightBtnListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initialLoginView(true);
                mIsReloginIng = true;
                mAccountErrorDialog.dismiss();
            }

        });
        mAccountErrorDialog.addLeftBtnListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendCancelCallback();
                finish();
            }
        });
        // mAccountErrorDialog.setCanceledOnTouchOutside(true);
        mAccountErrorDialog.show();
    }

    class ControllCallback implements IReqBindSucceed, IReqValidateSucceed, IReqAppRolesSucceed,
            IReqConsumeSucceed, IReqAccInfoSucceed, IReqOpenIdSucceed, IReqThirdLoginSucceed {
        private AccountInfo mAccountInfo;

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

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onNetError " + errCode + " errorMsg " + errorMsg, "oddshou");
            mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ERROR);
        }

        @Override
        public void onReqValidateSucceed() {
            // TODO Auto-generated method stub
            App app = App.getAppContext();
            app.setmTimeListen(SDKActivity.this);
            app.startTimeRun(StatisticManager.TIEM_CAPTCHA);
        }

        @Override
        public void onReqBindSucceed(AccountInfo account) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "登录成功 ", "oddshou");
            // UserInfoManager.getInstance().setAccountInfo(account);
            // handleLogin();
            // mHandler.sendEmptyMessage(MSG_BIND_SUCCEED);
            mAccountInfo = account;
            mHelpRequest.reqUserAccInfo(account.openId, account.token);
        }

        @Override
        public void onReqUserAccInfoSucceed(UserAccInfo accInfo) {
            // TODO Auto-generated method stub
            if (!mIsUpdateAccInfo) {
                UserInfoManager.getInstance().setAccountInfo(mAccountInfo);
                handleLogin();
                App.getSharedPreference().edit()
                        .putString(StatisticManager.KEY_MOBILE, mAccountInfo.mobile)
                        .commit();
            }
            mUserInfoManager.setAccInfo(accInfo);
            //信鸽绑定账号
//            if (!App.getmChNoSelf().equals(StatisticManager.ChnNo.ASUS)) {
                XGPushManager.registerPush(getApplicationContext(), accInfo.openId);
//            }
            mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ACCINFOSUCCEED);

            //            mHandler.sendEmptyMessage(MSG_GET_WEB_CONTANT_FAILED);
        }

        @Override
        public void onReqGetUserAppRolesSucceed(UserAppRoleInfo roleInfo) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "onReqGetUserAppRolesSucceed " + "请求角色成功 " + roleInfo, "oddshou");
            handleRoleLogin(roleInfo);
            mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_APPROLESSUCCEED);
        }

        @Override
        public void onConsumeSucceed(RspConsume rspData) {
            // TODO Auto-generated method stub
            mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_CONSUMESUCCEED);
            mAppOrderInfo.setNiuOrderId(rspData.orderNo);
            handlePayResult(0);
            Logger.i("SDKA ConsumeListener", "onConsumeSucceed mAppOrderInfo:" + mAppOrderInfo);
        }

        @Override
        public void onReqOpenIdSucceed(AccountInfo accountInfo) {
            // TODO Auto-generated method stub
            if (accountInfo.openId.length() > 0 && accountInfo.token.length() > 0) {

                Editor edit = App.getSharedPreference().edit();
                edit.putString(StatisticManager.KEY_OPENID, accountInfo.openId);
                edit.putString(StatisticManager.KEY_TOKEN, accountInfo.token);
                edit.commit();

                //                mHelpRequest.reqValidate(accountInfo.openId, accountInfo.token);
                mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_OPENIDSUCCEED);
            }
        }

        @Override
        public void onReqThirdLoginSucceed(AccountInfo account) {
            // TODO Auto-generated method stub
            mAccountInfo = account;

            if (account.openId.length() > 0 && account.token.length() > 0) {

                App.getSharedPreference().edit()
                        .putString(StatisticManager.KEY_OPENID, account.openId)
                        .putString(StatisticManager.KEY_TOKEN, account.token)
                        .commit();
            }

            Logger.i(TAG, "onReqThirdLoginSucceed " + account + " name " + account.nickName,
                    "oddshou");
            mHelpRequest.reqUserAccInfo(account.openId, account.token);
        }

    }

    public static final int MSG_LOADING_END = 0X11;
    public static final int MSG_AUTH_FAILED = 0X12;
    public static final int MSG_LOADING_START = 0X13;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HelpRequest.MSG_REQ_OPENIDSUCCEED:
                    mIsopenidSucceed = true;
                    mOnclickListen.onClick(mBtnCaptcha);
                    break;
                case HelpRequest.MSG_REQ_APPROLESSUCCEED:
                    CSToast.show(
                            SDKActivity.this,
                            getString(R.string.toast_game_login_succeed,
                                    mUserInfoManager.getCSUserInfo().getAccountInfo().nickName/*userName*/));
                    finish();
                    break;
                case HelpRequest.MSG_REQ_ACCINFOSUCCEED:
                    // 支付过程中更新账户信息
                    if (mIsUpdateAccInfo) {
                        mIsUpdateAccInfo = false;
                        initialPayView();
                        // Toast.makeText(SDKActivity.this, "更新账户信息成功",
                        // Toast.LENGTH_LONG).show();
                        break;
                    }
                    // 以下为登录情况
                    refreshLoginView(true);
                    if (!mIsReloginIng) {
                        // 普通登录的情况
                        getAppRoles();
                    } else {
                        // 重新登录的情况
                        preCheckRoleInfo();
                    }
                    break;
                case HelpRequest.MSG_REQ_CONSUMESUCCEED:
                    CSToast.show(SDKActivity.this, getString(R.string.toast_consume_succeed));
                    refreshPayProgressView(true);

                    break;

                case HelpRequest.MSG_REQ_FAILED:
                    CSToast.showFailed(SDKActivity.this, msg.arg1, msg.obj.toString());
                    if (msg.arg1 == 102) { //非法用户
                        CSAccountManager.getInstance(SDKActivity.this).removeCSAccount();
                    }
                    refreshLoginView(true);
                    refreshPayProgressView(true);
                    mIsUpdateAccInfo = false;
                    mIsReloginIng = false;
                    mIsLoginMm = false;
                    mIsThirdLogin = false;
                    mLayoutLoading.setVisibility(View.GONE);
                    if (mViewstubConsumeView == null && mViewstubPersonLoginView == null) {
                        sendCancelCallback();
                        finish();
                    }
                    break;
                case HelpRequest.MSG_REQ_ERROR:
                    CSToast.showError(SDKActivity.this);
                    refreshLoginView(true);
                    refreshPayProgressView(true);
                    mIsUpdateAccInfo = false;
                    mIsReloginIng = false;
                    mIsLoginMm = false;
                    mIsThirdLogin = false;
                    mLayoutLoading.setVisibility(View.GONE);
                    if (mViewstubConsumeView == null && mViewstubPersonLoginView == null) {
                        sendCancelCallback();
                        finish();
                    }
                    break;
                case MSG_LOADING_END:
                    mLayoutLoading.setVisibility(View.GONE);
                    break;
                case MSG_AUTH_FAILED:
                    mLayoutLoading.setVisibility(View.GONE);
                    CSToast.showNormal(SDKActivity.this,
                            getString(R.string.toast_third_login_failed));
                    break;
                case MSG_LOADING_START:
                    mLayoutLoading.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

        }
    };
    /**
     * 最后点击 获取验证码时间
     */
    public long mLastCaptchaTime = 0L;
    private boolean mIsopenidSucceed = false;

    private OnClickListener mOnclickListen = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btnCaptcha: // 获取验证码
                    //过滤 按钮点击
                    long time = System.currentTimeMillis();
                    if (!mIsopenidSucceed && (time - mLastCaptchaTime < 2000)) {
                        return;
                    }
                    mIsopenidSucceed = false;
                    mLastCaptchaTime = time;
                    // 获取验证码之前需要先查询是否有openid,如果没有 则需要先获取 openid
                    SharedPreferences preference = App.getSharedPreference();
                    String openId = preference.getString(StatisticManager.KEY_OPENID, "");
                    String token = preference.getString(StatisticManager.KEY_TOKEN, "");
                    CharSequence inputNumber = mEditLoginAccout.getText();
                    if (openId.length() > 0 && token.length() > 0) {
                        if (!StringUtils.invalidateNumber(inputNumber)) {
                            CSToast.show(SDKActivity.this,
                                    getString(R.string.toast_not_useful_account));
                            return;
                        }
                        mHelpRequest.reqValidate(openId, inputNumber.toString());

                    } else {
                        // 无 openID
                        mHelpRequest.reqOpenid();
                    }
                    getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true,
                            mSmsContent);
                    //验证码输入框获取焦点
                    mEditCaptcha.requestFocus();
                    // 获取
                    break;
                case R.id.btnLogin: // 登录
                    CharSequence inputNumber2 = mEditLoginAccout.getText();
                    if (!StringUtils.invalidateNumber(inputNumber2)) {
                        CSToast.show(SDKActivity.this, getString(R.string.toast_not_useful_account));
                        return;
                    }
                    CharSequence inputInvalidateCode = mEditCaptcha.getText();
                    if (!StringUtils.invalidateCode(inputInvalidateCode)) {
                        CSToast.show(SDKActivity.this, getString(R.string.toast_validate_error));
                        return;
                    }
                    SharedPreferences preference2 = App.getSharedPreference();
                    String openId2 = preference2.getString(StatisticManager.KEY_OPENID, "");
                    mHelpRequest.reqBind(openId2, inputNumber2.toString(),
                            inputInvalidateCode.toString(), StatisticManager.RESOURCE_INT_SDK);
                    refreshLoginView(false);
                    break;
                case R.id.btnPayRecharge:
                    Integer tag = (Integer) v.getTag();
                    if (tag.intValue() == 1) {
                        // 余额充足
                        ConsumeRequestInfo reqInfo = createConsumeInfo();
                        mHelpRequest.reqConsume(reqInfo);
                        refreshPayProgressView(false);
                    } else {
                        // 余额不足
                        Intent intentRecharge = new Intent(SDKActivity.this, RechargeActivity.class);
                        intentRecharge.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentRecharge);
                        mIsRecharging = true;
                        //暂时隐藏支付界面
                        if (mViewstubConsumeView != null
                                && mViewstubConsumeView.getVisibility() == View.VISIBLE) {
                            mViewstubConsumeView.setVisibility(View.GONE);
                        }
                    }
                    break;
                case R.id.imgLoginClose: // 登录过程中点击关闭,
                    onBackPressed();
                    break;
                case R.id.imgPayRechargeClose: // 支付或者 充值界面点击关闭
                    onBackPressed();
                    break;
                case R.id.textUserAgreement:
                    mIsUserAggrement = true;
                    Intent html5HelpIntent = new Intent(SDKActivity.this, Html5HelpActivity.class);
                    html5HelpIntent.putExtra(StatisticManager.KEY_HTML5_HELP_TITLE,
                            getString(R.string.user_aggrement));
                    html5HelpIntent.putExtra(StatisticManager.KEY_HTML5_HELP_URL,
                            StatisticManager.getCONSTANT_USER_AGGREMENT());
                    startActivity(html5HelpIntent);
                    break;
                case R.id.imgWeibo: // 微博授权登录
                    // 上报进入登录界面
                    ReportedInfo builderWB = new ReportedInfo();
                    builderWB.statActId = ReportConstants.STATACT_ID_THIRD_LOGIN;
                    builderWB.statActId2 = 4;
                    ReportConstants.getInstance().reportReportedInfo(builderWB);

                    mSsoHandler = new SsoHandler(SDKActivity.this,
                            mWeiboAuth);
                    mIsThirdLogin = true;
                    // 微博 无客户端且无网络的情况下 不显示 loading
                    if (!PackageUtil.existApk("com.sina.weibo", SDKActivity.this)
                            && !APNUtil.isNetworkAvailable(SDKActivity.this)) {

                    } else {
                        mHandler.sendEmptyMessage(MSG_LOADING_START);
                    }

                    //                    mHandler.sendEmptyMessage(MSG_LOADING_START);
                    mSsoHandler.authorize(new AuthListener());
                    // Oauth2.0 Web 授权
                    //                   mWeiboAuth.anthorize(new AuthListener());
                    break;
                case R.id.imgQq: // qq授权登录
                    // 上报进入qq登录点击
                    ReportedInfo builder = new ReportedInfo();
                    builder.statActId = ReportConstants.STATACT_ID_THIRD_LOGIN;
                    builder.statActId2 = 3;
                    ReportConstants.getInstance().reportReportedInfo(builder);
                    qqLogin();
                    break;
                case R.id.imgMm: // 微信授权登录
                    // final SendAuth.Req req = new SendAuth.Req();
                    // req.scope = "snsapi_userinfo";
                    // req.state = "wechat_sdk_demo_test";
                    // mWeixinAPI.sendReq(req);
                    // 上报进入登录界面
                    ReportedInfo builderMM = new ReportedInfo();
                    builderMM.statActId = ReportConstants.STATACT_ID_THIRD_LOGIN;
                    builderMM.statActId2 = 2;
                    ReportConstants.getInstance().reportReportedInfo(builderMM);
                    loginWithWeixin();
                    break;

                default:
                    break;
            }
        }

    };
    private boolean mIsRecharging = false;

    /**
     * 只在支付中调用
     * 
     * @param b false 登录中, true 非登录状态
     */
    private void refreshPayProgressView(boolean b)
    {
        if (mBtnPayRecharge != null && mBtnPayRecharge != null) {
            mBtnPayRecharge.setEnabled(b);
            mProgressPayRecharge.setVisibility(b ? View.GONE : View.VISIBLE);
            String str = b ? "" : "...";
            mBtnPayRecharge.setText(getString(R.string.pay_right) + str);
        }
    }

    /**
     * @param b false 登录中, true 非登录状态
     */
    private void refreshLoginView(boolean b)
    {
        if (mBtnLogin != null && mProgressLogin != null) {
            mBtnLogin.setEnabled(b);
            mProgressLogin.setVisibility(b ? View.GONE : View.VISIBLE);
            String str = b ? "" : "...";
            mBtnLogin.setText(getString(R.string.login) + str);
        }
    }

    private ConsumeRequestInfo createConsumeInfo() {
        // TODO Auto-generated method stub
        CSUserInfo userInfo = mUserInfoManager.getCSUserInfo();
        AccountInfo accountInfo = userInfo.getAccountInfo();
        ConsumeRequestInfo consumeRequestInfo = new ConsumeRequestInfo();
        consumeRequestInfo.openId = mSDKRoleInfo.getuId();
        consumeRequestInfo.token = accountInfo.token;
        consumeRequestInfo.appId = mAppEntry.getAppId();
        consumeRequestInfo.appToken = mAppEntry.getAppKey();
        consumeRequestInfo.roleId = mSDKRoleInfo.getRoleId();
        consumeRequestInfo.roleToken = mSDKRoleInfo.getRoleToken();
        consumeRequestInfo.consumeNewCoin = mAppOrderInfo.getValue();
        consumeRequestInfo.cpOrderNo = mAppOrderInfo.getCpOrderId();
        consumeRequestInfo.productCode = mAppOrderInfo.getItemCode();
        consumeRequestInfo.productName = mAppOrderInfo.getItemName();
        consumeRequestInfo.productCount = mAppOrderInfo.getItemCount();
        consumeRequestInfo.packName = mAppEntry.getAppPackageName();
        consumeRequestInfo.exInfo = mAppOrderInfo.getExInfo();
        Logger.i(TAG, "createConsumeInfo " + consumeRequestInfo, "oddshou");
        return consumeRequestInfo;
    }

    /**
     * get 请求 weixin Id
     */
    public static final int TAG_WEIXIN_ID = 0X01;
    /**
     * get 请求 weixin info
     */
    public static final int TAG_WEIXIN_INFO = 0X02;
    /**
     * get 请求 weibo info
     */
    public static final int TAG_WEIBO_INFO = 0X03;

    /**
     * xutils web 请求
     * 
     * @param url
     * @param tag
     */
    private void getWebContent(String url, final int tag) {
        mHandler.sendEmptyMessage(MSG_LOADING_START);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String responseInfo) {
                try {
                    if (tag == TAG_WEIXIN_ID) {
                        parseWeixinId(responseInfo);
                    } else if (tag == TAG_WEIXIN_INFO) {
                        parseWeixinInfo(responseInfo);
                    } else if (tag == TAG_WEIBO_INFO) {
                        parseWeiboInfo(responseInfo);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
//                Logger.e(TAG, "onFailure " + error + " msg " + msg, "oddshou");
                mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
            private void parseWeixinId(String responseInfo)
                    throws JSONException {
                JSONTokener jsonParser = new JSONTokener(responseInfo);
                JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
                String openid = mmInfo.getString("openid").trim();
                String accessToken = mmInfo.getString("access_token").trim();

                getWebContent(String.format(
                                "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                                accessToken, openid),
                        TAG_WEIXIN_INFO);

                Logger.i(TAG, "onSuccess " + openid + " accessToken " + accessToken,
                        "oddshou");
            }

            private void parseWeixinInfo(String responseInfo)
                    throws JSONException {
                JSONTokener jsonParser = new JSONTokener(responseInfo);
                JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
                String openid = mmInfo.getString("openid");
                String nickName = mmInfo.getString("nickname");
                        /*
                         * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
                         */
                String headImgurl = mmInfo.getString("headimgurl");
                if (headImgurl.length() > 0 && headImgurl.endsWith("/0")) {
                    StringBuilder b = new StringBuilder(headImgurl);
                    b.replace(headImgurl.lastIndexOf("/0"), headImgurl.length(), "/132");
                    headImgurl = b.toString();
                }
                int sex = mmInfo.getInt("sex");
                mHelpRequest.reqThirdLogin(openid, StatisticManager.ThridSource.WEI_XIN, nickName, headImgurl, 0, sex,
                        StatisticManager.RESOURCE_INT_SDK);
                Logger.i(TAG, "onSucceess " + nickName + " imgUrl " + headImgurl, "oddshou");
            }

            private void parseWeiboInfo(String responseInfo)
                    throws JSONException {
                JSONTokener jsonParser = new JSONTokener(responseInfo);
                JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
                Logger.i(TAG, "parseWeiboInfo " + mmInfo, "oddshou");
                String openid = mmInfo.getString("id");
                String nickName = mmInfo.getString("screen_name");
                String headImgurl = mmInfo.getString("avatar_large");
                String gender = mmInfo.getString("gender");
                int sex = 0;
                if (gender.equalsIgnoreCase("m")) {
                    sex = 1;
                } else if (gender.equalsIgnoreCase("f")) {
                    sex = 2;
                }
                mHelpRequest.reqThirdLogin(openid, StatisticManager.ThridSource.WEI_BO, nickName, headImgurl, 0, sex,
                        StatisticManager.RESOURCE_INT_SDK);
            }
        });

    }

    @Override
    public void onTimer(int count) {
        // TODO Auto-generated method stub
        Logger.d(TAG, "timeRemain " + count, "oddshou");
        initialCaptchaBtn(count);
        //        if (count <= 0) {
        //            getContentResolver( ).unregisterContentObserver( mSmsContent );
        //        }
    }
}
