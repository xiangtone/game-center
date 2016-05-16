
package com.hykj.gamecenter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

public class SmsContent extends ContentObserver {
    private final static String SMS_TAG = "游戏中心";
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    private static final String TAG = "SmsContent";
    private static final int YZMLENGTH = 4; //4位验证码
    private Activity mActivity = null;
    private String mSmsContent = "";
    private EditText mVerifyText;

    public SmsContent(Activity activity, Handler handler, EditText verifyText) {
        super(handler);
        this.mActivity = activity;
        this.mVerifyText = verifyText;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = null;// 光标
        // 读取收件箱中指定号码的短信
        cursor = mActivity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] {
                "_id", "address", "body", "read"
        }, "read=?", new String[] {
                "0"
        }, "date desc");
        if (cursor != null) {// 如果短信为未读模式
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                String smsbody = cursor.getString(cursor.getColumnIndex("body"));
                Log.d(TAG, "sms body:" + smsbody);
                if (!smsbody.contains(SMS_TAG))
                    return;
                Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + YZMLENGTH+ "})(?![0-9])");  
                
                Matcher m = p.matcher(smsbody.toString());    
                if (m.find()) { 
                    mSmsContent = m.group(0);
                } 
//                String regEx = "[^0-9]";
//                Pattern p = Pattern.compile( regEx );
//                Matcher m = p.matcher( smsbody.toString( ) );
//                mSmsContent = m.replaceAll( "" ).trim( ).toString( );
                Logger.i(TAG, "SmsContent " + mSmsContent, "oddshou");
                if (mVerifyText != null) {
                    mVerifyText.setText(mSmsContent);
                }
            }
        }
    }
}
