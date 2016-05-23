
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.CSAccountManager;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.ui.widget.CSAlertDialog;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.utils.SystemBarTintManager;

/**
 * 账号管理(退出账号，修改用户名)
 * 
 * @author oddshou
 */
public class AccountManagerActivity extends Activity {

    private CSCommonActionBar mActionBar;

    private TextView mTextUserName;

    private UserInfoManager mUserInfoManager;

    private View mLayoutUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_account_manager);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        initView();
        mUserInfoManager = UserInfoManager.getInstance();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initText(mUserInfoManager.getCSUserInfo().getAccountInfo());
    }

    private void initView() {
        // init actionbar
        mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
        mActionBar.SetOnActionBarClickListener(actionBarListener);
        mActionBar.setTitle(getString(R.string.person_account_manage));

        //init other view
        mLayoutUserName = findViewById(R.id.layoutUserName);
        mLayoutUserName.setOnClickListener(mOnclickListen);
        findViewById(R.id.layoutAccountExit).setOnClickListener(mOnclickListen);
        mTextUserName = (TextView) findViewById(R.id.textUserName);
    }

    private void initText(AccountInfo account) {
        if (account != null) {
            mTextUserName.setText(account.nickName/*userName*/);
            mLayoutUserName.setEnabled(true);
        } else {
            mTextUserName.setText("");
            mLayoutUserName.setEnabled(false);
        }
    }

    private OnClickListener mOnclickListen = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.layoutUserName:
                    Intent intent = new Intent(AccountManagerActivity.this, RenameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.layoutAccountExit:
                    showExitDialog();
                    break;

                default:
                    break;
            }
        }

    };

    private CSAlertDialog mExitDialog;

    private void showExitDialog() {
        if (null == mExitDialog) {
            mExitDialog = new CSAlertDialog(this,
                    getString(R.string.content_exit),
                    false, false);
        }
        mExitDialog.setTitle(getString(R.string.title_exit));
        mExitDialog.setmLeftBtnTitle(getString(R.string.cancel));
        mExitDialog.setmRightBtnTitle(getString(R.string.exit));
        mExitDialog.addRightBtnListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CSAccountManager.getInstance(AccountManagerActivity.this).removeCSAccount();
                initText(null);
                finish();
                mExitDialog.dismiss();
            }

        });
        // mAccountErrorDialog.setCanceledOnTouchOutside(true);
        mExitDialog.show();
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
}
