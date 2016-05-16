
package com.hykj.gamecenter.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.controller.ProtocolListener.ReqAddCommentListener;
import com.hykj.gamecenter.controller.ReqAddCommentController;
import com.hykj.gamecenter.fragment.WriteCommentFragment;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.UserCommentSubmitDialog;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;

public class WriteCommentActivity extends Activity {
    private static final String TAG = "WriteCommentActivity";
    public static final int SUBMIT_RESULT_CODE = 0x10;
    private int appId;
    private int verCode;
    private String verName;
    private String showName;
    private WriteCommentFragment writeCommentFragment;
    private String contentKey;
    private String ratingKey;

    private String commentContent;
    private int starRating;
    private String userName;
    // public UserCommentInfo mUserCommentInfo = null;
    private CSCommonActionBar actionBar;
    private UserCommentSubmitDialog submitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_write_comment);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        actionBar = (CSCommonActionBar) findViewById(R.id.actionBar);
        writeCommentFragment = (WriteCommentFragment) getFragmentManager()
                .findFragmentById(R.id.writeFragment);
        actionBar.getmRightEditButton().setVisibility(View.VISIBLE);
        // actionBar.getmRightEditButton( ).setText( this.getString(
        // R.string.app_user_comment_submit ) );
        //		actionBar.getmRightEditButton().setBackgroundResource(
        //				R.drawable.csls_actionbar_button_green);
        actionBar.SetOnActionBarClickListener(listener);

        appId = getIntent().getIntExtra("appId", -1);
        verCode = getIntent().getIntExtra("verCode", -1);
        verName = getIntent().getStringExtra("verName");
        showName = getIntent().getStringExtra("showName");

        Logger.d(TAG, "appId : " + appId);

        initKey();

    }

    private void initKey() {
        StringBuffer contentBuf = new StringBuffer();
        contentBuf.append(appId).append("content");
        contentKey = contentBuf.toString();

        StringBuffer ratingBuf = new StringBuffer();
        ratingBuf.append(appId).append("rating");
        ratingKey = ratingBuf.toString();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (getIntent().getByteArrayExtra("userCommentInfo") != null) {
            actionBar.setTitle(getString(
                    R.string.app_user_comment_update_title, showName));
        } else {
            actionBar.setTitle(getString(R.string.app_user_comment_new_title,
                    showName));
        }

        super.onResume();
    }

    // public String getContentKey()
    // {
    //
    // return contentKey;
    // }
    //
    // public String getRatingKey()
    // {
    // // StringBuffer ratingBuf = new StringBuffer( );
    // // ratingBuf.append( appId ).append( "rating" );
    // // return ratingBuf.toString( );
    // return ratingKey;
    // }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        saveCommentPreferences();

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        // saveCommentPreferences( );
        super.onPause();
    }

    private final CSCommonActionBar.OnActionBarClickListener listener = new CSCommonActionBar.OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, View view) {
            switch (position) {
                case RETURN_BNT:
                    onBackPressed();
                    break;
                case RIGHT_EDIT_BNT:
                    submitComment();
                    break;
                default:
                    break;
            }

        }

    };

    private void submitComment() {
        commentContent = writeCommentFragment.getCommentContent();
        starRating = writeCommentFragment.getStarRating();
        // if( HomePageActivity.getUserInfo( ) == null )
        // {
        // userName = "";
        // }
        // else
        // {
        // userName = HomePageActivity.getUserInfo( ).getUserInfo(
        // ).getUserName( );
        // }

        if (commentContent == null || commentContent.trim().isEmpty()) {
            writeCommentFragment.inputView
                    .setHint(R.string.app_user_no_comment_hint);
            inputAnimation();
            return;
        }
        submitDialog = new UserCommentSubmitDialog(this);
        submitDialog.show();
        reqAddComment();
    }

    private void reqAddComment() {
        // username为"",因为没有用户名，统一用游客和产品型号来表示
        ReqAddCommentController controller = new ReqAddCommentController("",
                appId, starRating, verCode, verName, commentContent,0,
                mReqAddCommentListener);
        controller.doRequest();
        // Logger.d( TAG , "reqAddComment" );
        // finish( );
    }

    private final ReqAddCommentListener mReqAddCommentListener = new ReqAddCommentListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e("mReqAddCommentListener", "onNetError errCode:" + errCode
                    + ",errorMsg:" + errorMsg);
            mHandler.sendEmptyMessage(MSG_NET_EORROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e("mReqAddCommentListener", "onReqFailed statusCode:"
                    + statusCode + ",errorMsg:" + errorMsg);
            mHandler.sendEmptyMessage(MSG_NET_EORROR);
        }

        @Override
        public void onReqAddCommentSucceed(long commentId, String userName) {
            // TODO Auto-generated method stub
            Logger.d("mReqAddCommentListener", "commentId : " + commentId);
            Logger.d("mReqAddCommentListener", "userName : " + userName);
            WriteCommentActivity.this.userName = userName;
            mHandler.sendEmptyMessage(MSG_SUCCESS_SUBMIT);
        }

    };

    private void saveCommentPreferences() {
        // Logger.d( TAG , "saveCommentPreferences " );
        final String commentContent = writeCommentFragment.getCommentContent();
        final int starRating = writeCommentFragment.getStarRating();
        // Logger.d( TAG , "commentContent: " + commentContent );
        // Logger.d( TAG , "starRating: " + starRating );
        if (!commentContent.trim().isEmpty()
                || (starRating != 0 && starRating != 5)) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    SharedPreferences preferences = App.getSharedPreference();
                    Editor editor = preferences.edit();
                    // Logger.d( TAG , "Thread: " + Thread.currentThread( ) );
                    if (!commentContent.trim().isEmpty()) {
                        // String strAppId = Integer.toString( appId );
                        // Logger.d( TAG , "contentKey : " + contentKey );
                        editor.putString(contentKey, commentContent.trim());

                    }

                    if (starRating != 0 && starRating != 5) {
                        // Logger.d( TAG , "ratingKey : " + ratingKey );
                        editor.putInt(ratingKey, starRating);
                    }
                    editor.commit();
                }
            }).start();
        }

    }

    private void removeCommentPreferences() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = App.getSharedPreference();
                // LogUtils.d( "removeCommentPreferences" );
                if (preferences.contains(contentKey)
                        || preferences.contains(ratingKey)) {
                    Editor editor = preferences.edit();
                    if (preferences.contains(contentKey)) {
                        editor.remove(contentKey);
                    }

                    if (preferences.contains(ratingKey)) {
                        editor.remove(ratingKey);
                    }
                    editor.commit();
                }
            }
        }).start();

    }

    private static final int MSG_SUCCESS_SUBMIT = 0x10;
    private static final int MSG_NET_EORROR = 0x20;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUCCESS_SUBMIT:
                    successSubmitComment();
                    break;

                case MSG_NET_EORROR:
                    submitDialog.dismiss();
                    CSToast.show(WriteCommentActivity.this,
                            getString(R.string.app_user_comment_submit_net_error));
                    break;
                default:
                    break;
            }
        }

    };

    private void successSubmitComment() {
        String commentTime = DateUtil.getCurrentTime();
        // Logger.d( TAG , "commentTime : " + commentTime );
        // Logger.d( TAG , "commentContent : " + commentContent );
        // Logger.d( TAG , "starNum : " + starRating );
        // Logger.d( TAG , "userName : " + userName );
        Intent intent = new Intent();
        intent.putExtra("starRating", starRating);
        intent.putExtra("commentContent", commentContent);
        intent.putExtra("commentTime", commentTime);
        intent.putExtra("userName", userName);
        setResult(SUBMIT_RESULT_CODE, intent);
        removeCommentPreferences();
        submitDialog.dismiss();
        finish();
    }

    int i = 0;

    public void inputAnimation() {

        // final LinearLayout touchScreen = (LinearLayout)findViewById(
        // R.id.animation_layout );// 获取页面textview对象
        final Timer timer = new Timer();
        TimerTask taskcc = new TimerTask() {
            @Override
            public void run() {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        writeCommentFragment.inputContainer
                                .setBackgroundResource(R.drawable.input_edit_background_green);
                    }
                }, 0);

                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        writeCommentFragment.inputContainer
                                .setBackgroundResource(R.drawable.input_edit_background);
                    }
                }, 300);
                i++;
                if (i == 5) {
                    timer.cancel();
                    i = 0;
                }
            }
        };
        timer.schedule(taskcc, 0, 600);// 参数分别是delay（多长时间后执行），duration（执行间隔）

    }
}
