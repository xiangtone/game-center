
package com.hykj.gamecenter.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.App.TimeRun;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.CSAccountManager;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.controller.HelpRequest;
import com.hykj.gamecenter.controller.HelpRequest.IReqAccInfoSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqBindSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqOpenIdSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqThirdLoginSucceed;
import com.hykj.gamecenter.controller.HelpRequest.IReqValidateSucceed;
import com.hykj.gamecenter.fragment.AppInfoSoftWareDetailFragment;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.protocol.Pay.UserAccInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.AccessTokenKeeper;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PackageUtil;
import com.hykj.gamecenter.utils.SmsContent;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;
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

import java.text.SimpleDateFormat;

public class PersonLogin extends AccountAuthenticatorActivity implements TimeRun {

    private CSCommonActionBar mActionBar;
    private EditText mEditLoginAccout;
    private EditText mEditCaptcha;
    private Button mBtnCaptcha;
    private Button mBtnLogin;
    private ProgressBar mProgressLogin;

    public static final String TAG = "PersonLogin";

    private AccountInfo mAccountInfo;
    private CSAccountManager mCsAccountManager;
    private HelpRequest mHelpRequest;
    private UserInfoManager mUserInfoManager;
    private SmsContent mSmsContent;
    private TextView mTextUserAgreement;
    private ImageView mImgWeibo;
    private ImageView mImgMm;
    private ImageView mImgQq;
    private IWXAPI mWeixinAPI;
    private boolean mIsLoginMm;
    private View mLayoutLoading; // loading View
    private Tencent mTencent;

    private WeiboAuth mWeiboAuth;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_person_login);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mHelpRequest = new HelpRequest(new ControllCallback());
        mCsAccountManager = CSAccountManager.getInstance(this);
        mUserInfoManager = UserInfoManager.getInstance();
        initView();
        String defaultMobile = App.getSharedPreference().getString(StatisticManager.KEY_MOBILE, "");
        if (defaultMobile.length() == 11) {
            mEditLoginAccout.setText(defaultMobile);
            mEditLoginAccout.setSelection(defaultMobile.length());
        }
        App app = App.getAppContext();
        app.setmTimeListen(PersonLogin.this);
        initialCaptchaBtn(app.getmCurrentTimer());
        mSmsContent = new SmsContent(this, new Handler(), mEditCaptcha);
        // 上报进入登录界面
        ReportedInfo builder = new ReportedInfo();
        builder.statActId = ReportConstants.STATACT_ID_PERSON;
        builder.statActId2 = 2;
        ReportConstants.getInstance().reportReportedInfo(builder);

        // 微信注册
        mWeixinAPI = WXAPIFactory.createWXAPI(this, StatisticManager.getWeixinAppId(), false);
        mWeixinAPI.registerApp(StatisticManager.getWeixinAppId());
        // qq
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        // mTencent = Tencent.createInstance(StatisticManager.QQ_APP_ID,
        // this.getApplicationContext());
        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        // 微博
        // 创建微博实例
        mWeiboAuth = new WeiboAuth(this, StatisticManager.getWeiboAppkey(),
                StatisticManager.WEIBO_REDIRECTURI, StatisticManager.WEIBO_SCOPE);
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        getContentResolver().unregisterContentObserver(mSmsContent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mIsLoginMm) {
            mIsLoginMm = false;
            String mmcode = App.getSharedPreference().getString(StatisticManager.KEY_MM_CODE, "");
            if (mmcode.length() > 0) {
                // 表示授权成功
                // mLayoutLoading.setVisibility(View.VISIBLE);
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

    private void initView() {
        // init actionbar
        mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
        mActionBar.SetOnActionBarClickListener(actionBarListener);
        mActionBar.setTitle(getString(R.string.quick_login));

        // init other view
        mEditLoginAccout = (EditText) findViewById(R.id.editLoginAccout);
        mEditCaptcha = (EditText) findViewById(R.id.editCaptcha);
        mBtnCaptcha = (Button) findViewById(R.id.btnCaptcha);
        mBtnCaptcha.setOnClickListener(mOnclickListen);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(mOnclickListen);
        mProgressLogin = (ProgressBar) findViewById(R.id.progressLogin);
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
        // init loading view
        mLayoutLoading = findViewById(R.id.layoutLoading);
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
        } else {
            mBtnCaptcha.setEnabled(false);
            mBtnCaptcha.setTextColor(getResources().getColor(R.color.csl_black_4c));
            mBtnCaptcha.setText(String.format(getString(R.string.get_captcha_after), count));
        }
    }

    // 处理注册成功后
    private void handleLogin() {
        final Account account = new Account(getString(R.string.cs_accountname),
                getString(R.string.cs_accounttype));
        AccountManager mAccountManager = AccountManager.get(this);
        mAccountManager.addAccountExplicitly(account, "pwd", null);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, getString(R.string.cs_accountname));
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.cs_accounttype));
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        mCsAccountManager.updateAccountUserData(UserInfoManager.getInstance().getCSUserInfo()
                .getAccountInfo());
    }

    // qq login
    public void qqLogin() {
        mTencent = Tencent.createInstance(StatisticManager.getQqAppId(), this.getApplicationContext());
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
            // showResult("onError:", "code:" + e.errorCode + ", msg:"
            // + e.errorMessage + ", detail:" + e.errorDetail);
            Logger.e(TAG, "BaseUiListener " + "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail, "oddshou");
            mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
        }

        @Override
        public void onCancel() {
            // showResult("onCancel", "");
            Logger.e(TAG, "BaseUiListener cancel", "oddshou");
            // ###########################注意
            mHandler.sendEmptyMessage(MSG_LOADING_END);
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            // doComplete(arg0);
            getUserInfo();

            // JSONTokener jsonParser = new JSONTokener(arg0.toString());
            // JSONObject mmInfo;
            // try {
            // mmInfo = (JSONObject) jsonParser.nextValue();
            // String token = mmInfo.getString(Constants.PARAM_ACCESS_TOKEN);
            // String expires = mmInfo.getString(Constants.PARAM_EXPIRES_IN);
            // String openId = mmInfo.getString(Constants.PARAM_OPEN_ID);
            // if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
            // && !TextUtils.isEmpty(openId)) {
            // mTencent.setAccessToken(token, expires);
            // mTencent.setOpenId(openId);
            // }
            // } catch (JSONException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }

    }

    private void getUserInfo() {
        QQToken qqToken = mTencent.getQQToken();
        UserInfo info = new UserInfo(getApplicationContext(), qqToken);
        // mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null,
        // Constants.HTTP_GET, new BaseApiListener("get_simple_userinfo",
        // false),
        // null);
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
                            sex, StatisticManager.RESOURCE_INT_GAMECENTER);
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

    private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, View view) {

            switch (position) {
                case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * @param b false 登录中, true 非登录状态
     */
    private void refreshLoginView(boolean b) {
        mBtnLogin.setEnabled(b);
        mProgressLogin.setVisibility(b ? View.GONE : View.VISIBLE);
        String str = b ? "" : "...";
        mBtnLogin.setText(getString(R.string.login) + str);
    }

    class ControllCallback implements IReqBindSucceed, IReqValidateSucceed, IReqAccInfoSucceed
            , IReqOpenIdSucceed, IReqThirdLoginSucceed {

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
            app.setmTimeListen(PersonLogin.this);
            app.startTimeRun(StatisticManager.TIEM_CAPTCHA);
        }

        @Override
        public void onReqBindSucceed(AccountInfo account) {
            // TODO Auto-generated method stub
            mAccountInfo = account;
            Logger.i(TAG, "onReqBindSucceed " + account + " name " + account.nickName, "oddshou");
            mHelpRequest.reqUserAccInfo(account.openId, account.token);
        }

        @Override
        public void onReqUserAccInfoSucceed(UserAccInfo accInfo) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "登录成功 ", "oddshou " + mAccountInfo);
            UserInfoManager.getInstance().setAccountInfo(mAccountInfo);
            handleLogin();
            mUserInfoManager.setAccInfo(accInfo);
            //信鸽绑定账号
            XGPushManager.registerPush(getApplicationContext(), accInfo.openId);
            App.getSharedPreference().edit()
                    .putString(StatisticManager.KEY_MOBILE, mAccountInfo.mobile)
                    .commit();
            String className = getIntent().getStringExtra(StatisticManager.KEY_LOGIN_CLASS);
            if (className != null && className.length() > 0) {
                try {
                    Class classes = Class.forName(className);
                    Intent intent = new Intent(PersonLogin.this, classes);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.e(TAG, "KEY_LOGIN_CLASS className error");
                }
            }
            setResult(AppInfoSoftWareDetailFragment.RESULT_CODE, null);
            finish();
            // mHandler.sendEmptyMessage(MSG_GET_WEB_CONTANT_FAILED);
        }

        @Override
        public void onReqOpenIdSucceed(AccountInfo accountInfo) {
            // TODO Auto-generated method stub
            if (accountInfo.openId.length() > 0 && accountInfo.token.length() > 0) {

                App.getSharedPreference().edit()
                        .putString(StatisticManager.KEY_OPENID, accountInfo.openId)
                        .putString(StatisticManager.KEY_TOKEN, accountInfo.token)
                        .commit();
                //                 mHelpRequest.reqValidate(accountInfo.openId,
                //                 accountInfo.token);
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

            // Logger.i(TAG, "onReqThirdLoginSucceed " + account + " name " +
            // account.nickName, "oddshou");
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
                case HelpRequest.MSG_REQ_FAILED:
                    CSToast.showFailed(PersonLogin.this, msg.arg1, msg.obj.toString());
                    refreshLoginView(true);
                    mLayoutLoading.setVisibility(View.GONE);
                    break;
                case HelpRequest.MSG_REQ_ERROR:
                    CSToast.showError(PersonLogin.this);
                    refreshLoginView(true);
                    mLayoutLoading.setVisibility(View.GONE);
                    break;
                case MSG_LOADING_END: // 暂时是不会调用的，请求失败时会调用。所以请求成功后会关闭当前 activity
                    mLayoutLoading.setVisibility(View.GONE);
                    break;
                case MSG_AUTH_FAILED:
                    mLayoutLoading.setVisibility(View.GONE);
                    CSToast.showNormal(PersonLogin.this,
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
    public long mLastCaptchaTime = 0;
    private boolean mIsopenidSucceed = false;

    private OnClickListener mOnclickListen = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            SharedPreferences preference = App.getSharedPreference();
            String openId = preference.getString(StatisticManager.KEY_OPENID, "");
            CharSequence inputNumber = mEditLoginAccout.getText();
            switch (v.getId()) {
                case R.id.btnCaptcha: // 获取验证码
                    // 过滤 按钮点击
                    long time = System.currentTimeMillis();
                    if (!mIsopenidSucceed && (time - mLastCaptchaTime < 2000)) {
                        return;
                    }
                    mIsopenidSucceed = false;
                    mLastCaptchaTime = time;
                    // 获取验证码之前需要先查询是否有openid,如果没有 则需要先获取 openid
                    String token = preference.getString(StatisticManager.KEY_TOKEN, "");
                    if (openId.length() > 0 && token.length() > 0) {
                        if (!StringUtils.invalidateNumber(inputNumber)) {
                            CSToast.show(PersonLogin.this,
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
                    if (!StringUtils.invalidateNumber(inputNumber)) {
                        CSToast.show(PersonLogin.this, getString(R.string.toast_not_useful_account));
                        return;
                    }
                    CharSequence inputInvalidateCode = mEditCaptcha.getText();
                    if (!StringUtils.invalidateCode(inputInvalidateCode)) {
                        CSToast.show(PersonLogin.this, getString(R.string.toast_validate_error));
                        return;
                    }
                    mHelpRequest.reqBind(openId, inputNumber.toString(),
                            inputInvalidateCode.toString(),
                            StatisticManager.RESOURCE_INT_GAMECENTER);
                    refreshLoginView(false);
                    break;
                case R.id.textUserAgreement:
                    Intent html5HelpIntent = new Intent(PersonLogin.this, Html5HelpActivity.class);
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

                    mSsoHandler = new SsoHandler(PersonLogin.this,
                            mWeiboAuth);
                    // 微博 无客户端且无网络的情况下 不显示 loading
                    if (!PackageUtil.existApk("com.sina.weibo", PersonLogin.this)
                            && !APNUtil.isNetworkAvailable(PersonLogin.this)) {

                    } else {
                        mHandler.sendEmptyMessage(MSG_LOADING_START);
                    }
                    mSsoHandler.authorize(new AuthListener());

                    // Oauth2.0 Web 授权
                    // mWeiboAuth.anthorize(new AuthListener());

                    // Intent html5HelpIntent2 = new Intent(PersonLogin.this,
                    // Html5HelpActivity.class);
                    // html5HelpIntent2.putExtra(StatisticManager.KEY_HTML5_HELP_TITLE,
                    // "微博登录");
                    // String code = "";
                    // String url =
                    // String.format("https://api.weibo.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=%s",
                    // StatisticManager.WEIBO_APPKEY,
                    // StatisticManager.WEIBO_REDIRECTURI, code);
                    // html5HelpIntent2.putExtra(StatisticManager.KEY_HTML5_HELP_URL,
                    // url);
                    // startActivity(html5HelpIntent2);

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
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(PersonLogin.this, mAccessToken);
                // Toast.makeText(PersonLogin.this,
                // "授权成功", Toast.LENGTH_SHORT).show();
                // Logger.i(TAG, "onComplete 授权成功", "oddshou");
                // 获取用户信息并登录
                String url = String.format(
                        "https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s",
                        mAccessToken.getToken(), mAccessToken.getUid());
                getWebContent(url, TAG_WEIBO_INFO);
            } else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                // Toast.makeText(PersonLogin.this, message,
                // Toast.LENGTH_LONG).show();
                Logger.i(TAG, "onComplete " + message, "oddshou");
                mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
            }
        }

        @Override
        public void onCancel() {
            // Toast.makeText(PersonLogin.this,
            // "取消授权", Toast.LENGTH_LONG).show();
            Logger.e(TAG, "onCancel 取消授权", "oddshou");
            mHandler.sendEmptyMessage(MSG_LOADING_END);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            // Toast.makeText(PersonLogin.this,
            // "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            Logger.e(TAG, "Auth exception " + e.getMessage(), "oddshou");
            mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        // String format =
        // getString(/*R.string.weibosdk_demo_token_to_string_format_1*/"Token：%1$s \n有效期：%2$s");
        String showText = String.format("Token：%s \n有效期：%s", mAccessToken.getToken(), date);
        Logger.i(TAG, "updateTokenView " + showText, "oddshou");

        String message = String.format("Token：%s \n有效期：%s", mAccessToken.getToken(), date);
        if (hasExisted) {
            message = "Token 仍在有效期内，无需再次登录" + "\n" + message;
        }
        // mTokenText.setText(message);
        Logger.i(TAG, "updateTokenView " + message, "oddshou");
    }

    private void loginWithWeixin() {
        if (mWeixinAPI == null) {
            mWeixinAPI = WXAPIFactory.createWXAPI(this, StatisticManager.getWeixinAppId(), false);
        }

        if (!mWeixinAPI.isWXAppInstalled()) {
            // 提醒用户没有按照微信
            // Logger.i(TAG, "loginWithWeixin 没有安装微信", "oddshou");
            CSToast.showNormal(this, getString(R.string.toast_not_install_mm));
            return;
        }

        mWeixinAPI.registerApp(StatisticManager.getWeixinAppId());

        SendAuth.Req req = new SendAuth.Req();
        req.scope = StatisticManager.WEIXIN_SCOPE;
        req.state = StatisticManager.WEIXIN_STATE;
        mIsLoginMm = true;
        mHandler.sendEmptyMessage(MSG_LOADING_START); // 显示 loading
        mWeixinAPI.sendReq(req);
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
//    private void getWebContent(String url, final int tag) {
//        HttpUtils http = new HttpUtils(null);
//        http.send(HttpRequest.HttpMethod.GET,
//                /* "http://www.lidroid.com" */url,
//                new RequestCallBack<String>() {
//                    @Override
//                    public void onStart() {
//                        // TODO Auto-generated method stub
//                        super.onStart();
//                        // mHandler.sendEmptyMessage(MSG_GET_WEB_CONTANT_START);
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        try {
//                            if (tag == TAG_WEIXIN_ID) {
//                                parseWeixinId(responseInfo);
//                            } else if (tag == TAG_WEIXIN_INFO) {
//                                parseWeixinInfo(responseInfo);
//                            } else if (tag == TAG_WEIBO_INFO) {
//                                parseWeiboInfo(responseInfo);
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                            mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        Logger.e(TAG, "onFailure " + error + " msg " + msg, "oddshou");
//                        mHandler.sendEmptyMessage(MSG_AUTH_FAILED);
//                    }
//
//                    private void parseWeixinId(ResponseInfo<String> responseInfo)
//                            throws JSONException {
//                        JSONTokener jsonParser = new JSONTokener(responseInfo.result);
//                        JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
//                        String openid = mmInfo.getString("openid").trim();
//                        String accessToken = mmInfo.getString("access_token").trim();
//
//                        getWebContent(String.format(
//                                "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
//                                accessToken, openid),
//                                TAG_WEIXIN_INFO);
//
//                        Logger.i(TAG, "onSuccess " + openid + " accessToken " + accessToken,
//                                "oddshou");
//                    }
//
//                    private void parseWeixinInfo(ResponseInfo<String> responseInfo)
//                            throws JSONException {
//                        JSONTokener jsonParser = new JSONTokener(responseInfo.result);
//                        JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
//                        String openid = mmInfo.getString("openid");
//                        String nickName = mmInfo.getString("nickname");
//                        /*
//                         * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像
//                         * ），用户没有头像时该项为空
//                         */
//                        String headImgurl = mmInfo.getString("headimgurl");
//                        // if (headImgurl.length() > 0 &&
//                        // headImgurl.endsWith("/0")) {
//                        // StringBuilder b = new StringBuilder(headImgurl);
//                        // b.replace(headImgurl.lastIndexOf("/0"),
//                        // headImgurl.length(), "/132");
//                        // headImgurl = b.toString();
//                        // }
//                        int sex = mmInfo.getInt("sex");
//                        mHelpRequest.reqThirdLogin(openid, "weixin", nickName, headImgurl, 0, sex,
//                                StatisticManager.RESOURCE_INT_GAMECENTER);
//                        Logger.i(TAG, "onSucceess " + nickName + " imgUrl " + headImgurl, "oddshou");
//                    }
//
//                    private void parseWeiboInfo(ResponseInfo<String> responseInfo)
//                            throws JSONException {
//                        JSONTokener jsonParser = new JSONTokener(responseInfo.result);
//                        JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
//                        Logger.i(TAG, "parseWeiboInfo " + mmInfo, "oddshou");
//                        String openid = mmInfo.getString("id");
//                        String nickName = mmInfo.getString("screen_name");
//                        String headImgurl = mmInfo.getString("avatar_large");
//                        String gender = mmInfo.getString("gender");
//                        int sex = 0;
//                        if (gender.equalsIgnoreCase("m")) {
//                            sex = 1;
//                        } else if (gender.equalsIgnoreCase("f")) {
//                            sex = 2;
//                        }
//                        mHelpRequest.reqThirdLogin(openid, "weixin", nickName, headImgurl, 0, sex,
//                                StatisticManager.RESOURCE_INT_GAMECENTER);
//                    }
//
//                });
//    }

    /**
     * xutils web 请求
     *
     * @param url
     * @param tag
     */
    private void getWebContent(String url, final int tag) {
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
                         * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像
                         * ），用户没有头像时该项为空
                         */
                String headImgurl = mmInfo.getString("headimgurl");
                // if (headImgurl.length() > 0 &&
                // headImgurl.endsWith("/0")) {
                // StringBuilder b = new StringBuilder(headImgurl);
                // b.replace(headImgurl.lastIndexOf("/0"),
                // headImgurl.length(), "/132");
                // headImgurl = b.toString();
                // }
                int sex = mmInfo.getInt("sex");
                mHelpRequest.reqThirdLogin(openid, StatisticManager.ThridSource.WEI_XIN, nickName, headImgurl, 0, sex,
                        StatisticManager.RESOURCE_INT_GAMECENTER);
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
                        StatisticManager.RESOURCE_INT_GAMECENTER);
            }
        });

    }

    @Override
    public void onTimer(int count) {
        // TODO Auto-generated method stub
        Logger.d(TAG, "timeRemain " + count, "oddshou");
        initialCaptchaBtn(count);
    }

}
