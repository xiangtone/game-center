
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.controller.HelpRequest;
import com.hykj.gamecenter.controller.HelpRequest.IReqFeedbackSucceed;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;

public class FeedbackActivity extends Activity {
    protected static final String TAG = "FeedbackActivity";
    private CSCommonActionBar mActionBar;
    private EditText mEditFeedBack;
    private EditText mEditNumber;
    private Button mBtnSubmit;
    private ScrollView mLayoutScrollView;
    private HelpRequest mHelpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        initView();
        mHelpRequest = new HelpRequest(new ControllCallback());
    }

    private void initView() {
        // TODO Auto-generated method stub
        // init actionbar
        mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
        mActionBar.SetOnActionBarClickListener(actionBarListener);
        mActionBar.setTitle(getString(R.string.title_feedback));

        // other view
        mLayoutScrollView = (ScrollView) findViewById(R.id.layoutScrollView);
//        mLayoutScrollView.addOnLayoutChangeListener(mOnlayoutChangeListener);

        mEditFeedBack = (EditText) findViewById(R.id.editFeedBack);
        mEditNumber = (EditText) findViewById(R.id.editNumber);
        mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
        mBtnSubmit.setOnClickListener(mViewClickListener);

    }
    
    private Handler mUiHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HelpRequest.MSG_REQ_FAILED:
                    CSToast.showFailed(FeedbackActivity.this, msg.arg1, msg.obj.toString());
                    break;
                case HelpRequest.MSG_REQ_ERROR:
                    CSToast.showError(FeedbackActivity.this);
                    break;
                case HelpRequest.MSG_REQ_FEEDBACKSUCCEED:
                    CSToast.showNormal(FeedbackActivity.this, getString(R.string.toast_feedback_succeed));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private OnClickListener mViewClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btnSubmit:
                    // reqFeedback(content, userContact);
                    String content = mEditFeedBack.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        CSToast.show(FeedbackActivity.this, getString(R.string.toast_feedback_needed));
                        return;
                    }
                    String userContact = mEditNumber.getText().toString().trim();
                    mHelpRequest.reqFeedback(content, userContact);
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

    private OnLayoutChangeListener mOnlayoutChangeListener = new OnLayoutChangeListener() {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                int oldTop, int oldRight, int oldBottom) {
            // TODO Auto-generated method stub
            if (mLayoutScrollView != null && mEditNumber.isFocused()) {
                mLayoutScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
    };
    class ControllCallback implements IReqFeedbackSucceed{

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            mUiHandler.sendEmptyMessage(HelpRequest.MSG_REQ_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "onReqFailed " + statusCode + " errorMsg " + errorMsg, "oddshou");
            Message msg = mUiHandler.obtainMessage();
            msg.what = HelpRequest.MSG_REQ_FAILED;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mUiHandler.sendMessage(msg);
        }

        @Override
        public void onReqFeedbackSucceed() {
            // TODO Auto-generated method stub
            mUiHandler.sendEmptyMessage(HelpRequest.MSG_REQ_FEEDBACKSUCCEED);
        }
        
    }
}
