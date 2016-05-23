
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.controller.HelpRequest;
import com.hykj.gamecenter.controller.HelpRequest.IReqSetUserNameSucceed;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;

public class RenameActivity extends Activity {
    protected static final String TAG = "RenameActivity";
    private CSCommonActionBar mActionBar;
    private EditText mEditUserName;
    private Button mBtnModify;
    private UserInfoManager mUserInfoManager;
    private ProgressBar mProgressModify;
    private HelpRequest mHelpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_rename);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mUserInfoManager = UserInfoManager.getInstance();
        initView();
        mHelpRequest = new HelpRequest(new ControllCallback());
    }

    private void initView() {
        // init actionbar
        mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
        mActionBar.SetOnActionBarClickListener(actionBarListener);
        mActionBar.setTitle(getString(R.string.title_rename));
        // init other view
        mEditUserName = (EditText) findViewById(R.id.editUserName);
        mEditUserName
                .setText(mUserInfoManager.getCSUserInfo().getAccountInfo().nickName/*userName*/);
        mBtnModify = (Button) findViewById(R.id.btnModify);
        mBtnModify.setOnClickListener(mOnclickListen);
        mProgressModify = (ProgressBar) findViewById(R.id.progressModify);
    }

    /**
     * @param b false 登录中, true 非登录状态
     */
    private void refreshModifybtn(boolean b)
    {
        mBtnModify.setEnabled(b);
        mProgressModify.setVisibility(b ? View.GONE : View.VISIBLE);
        String str = b ? "" : "...";
        mBtnModify.setText(getString(R.string.btn_title_modify) + str);
    }

    class ControllCallback implements IReqSetUserNameSucceed {

        @Override
        public void onReqSetUserNameSucceed() {
            // TODO Auto-generated method stub
            mUserInfoManager.updateAccountName(mEditUserName.getText().toString(), mUserInfoManager
                    .getCSUserInfo().getAccountInfo());
            mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_SETUSERNAMESUCCEED);
        }

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            mHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Message msg = mHandler.obtainMessage();
            msg.what = HelpRequest.MSG_REQ_FAILED;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 
     */
    private OnClickListener mOnclickListen = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btnModify:
                    CharSequence accountName = mEditUserName.getText();
                    if (!checkAccountName(accountName)) {
                        return;
                    } else {
                        mHelpRequest.reqSetUserName(accountName.toString(), mUserInfoManager
                                .getCSUserInfo().getAccountInfo());
                        refreshModifybtn(false);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HelpRequest.MSG_REQ_FAILED:
                    CSToast.showFailed(RenameActivity.this, msg.arg1, msg.obj.toString());
                    refreshModifybtn(true);
                    break;
                case HelpRequest.MSG_REQ_ERROR:
                    CSToast.showError(RenameActivity.this);
                    refreshModifybtn(true);
                    break;
                case HelpRequest.MSG_REQ_SETUSERNAMESUCCEED:
                    CSToast.show(RenameActivity.this, getString(R.string.toast_rename_succeed));
                    refreshModifybtn(true);
                    finish();
                    break;

                default:
                    break;
            }

        }
    };

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
     * 预处理用户名，返回 ture 表示 用户名可用
     * 
     * @param accountName
     * @return
     */
    protected boolean checkAccountName(CharSequence accountName) {
        // TODO Auto-generated method stub
        String toastString = null;
        do {
            if (accountName.length() <= 0) {
                // 用户名为空
                toastString = getString(R.string.toast_name_not_null);
                break;
            }
            if (StringUtils.isAllNumber(accountName)) {
                // 全数字
                toastString = getString(R.string.toast_name_cannot_allnumber);
                break;
            }
            int wordLength = StringUtils.getWordCount(accountName.toString());
            if (wordLength < 2 || wordLength > 24) {
                // 用户名长度错误
                toastString = getString(R.string.toast_name_less);
                break;
            }

            return true;
        } while (false);
        CSToast.show(this, toastString);

        return false;

    }
}
