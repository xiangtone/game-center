
package com.hykj.gamecenter.activity;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.App.TimeRun;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.controller.HelpRequest;
import com.hykj.gamecenter.net.WifiHttpUtils;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SmsContent;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class PersonLogin extends AccountAuthenticatorActivity implements TimeRun {

    private CSCommonActionBar mActionBar;
    private EditText mEditLoginAccout;
    private EditText mEditCaptcha;
    private Button mBtnCaptcha;
    private Button mBtnLogin;
    private ProgressBar mProgressLogin;

    public static final String TAG = "PersonLogin";

    private HelpRequest mHelpRequest;
    private SmsContent mSmsContent;
    private TextView mTextUserAgreement;

    private View mLayoutLoading; // loading View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_person_login);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

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

    public static final int MSG_LOADING_END = 0X11;
    public static final int MSG_AUTH_FAILED = 0X12;
    public static final int MSG_LOADING_START = 0X13;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
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
                    if (time - mLastCaptchaTime < 2000) {
                        return;
                    }
                    mLastCaptchaTime = time;
                    if (!StringUtils.invalidateNumber(inputNumber)) {
                        CSToast.show(PersonLogin.this,
                                getString(R.string.toast_not_useful_account));
                        return;
                    }
                    //请求验证码
//                    mHelpRequest.reqValidate(openId, inputNumber.toString());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("phoneno", inputNumber.toString());
                        jsonObject.put("ssid", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    WifiHttpUtils wifiHttpUtils = new WifiHttpUtils(jsonObject);
                    doPost(WifiHttpUtils.URL_ICODE, wifiHttpUtils);


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

                default:
                    break;
            }
        }
    };


    /**
     * xutils web 请求
     *
     * @param url
     */
    private void doPost(final String url, WifiHttpUtils wifiHttpUtils) {
        RequestParams params = new RequestParams(url);
        params.addParameter("_data", wifiHttpUtils.getParmarData());
        Logger.i(TAG, wifiHttpUtils.getParmarData());
        Logger.i(TAG, wifiHttpUtils.getParmarSign());
        params.addParameter("_sign", wifiHttpUtils.getParmarSign());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String responseInfo) {
                switch (url) {
                    case WifiHttpUtils.URL_ICODE:
                        break;

                    case WifiHttpUtils.URL_UID:
                        break;

                }
                Logger.i(TAG, "onSuccess " + responseInfo);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Logger.i(TAG, "onError " + throwable.getMessage());
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

//            private void parseWeixinId(String responseInfo)
//                    throws JSONException {
//                JSONTokener jsonParser = new JSONTokener(responseInfo);
//                JSONObject mmInfo = (JSONObject) jsonParser.nextValue();
//                String openid = mmInfo.getString("openid").trim();
//                String accessToken = mmInfo.getString("access_token").trim();
//
//                getWebContent(String.format(
//                        "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
//                        accessToken, openid),
//                        TAG_WEIXIN_INFO);
//            }
        });

    }

    @Override
    public void onTimer(int count) {
        // TODO Auto-generated method stub
        Logger.d(TAG, "timeRemain " + count, "oddshou");
        initialCaptchaBtn(count);
    }

}
