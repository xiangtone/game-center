
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.Logger;

public class CSToast
{

    private static final String TAG = "CSToast";
    private static Toast mToast;
//    private static int mGravity = Gravity.TOP;


    public static void show(Context context, String msg)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastView = inflater.inflate(R.layout.csl_cs_toast, null);
        TextView text = (TextView) toastView.findViewById(R.id.csl_toast_msg);
        text.setText(msg);
        
        if (mToast == null)
        {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            TextView textView = (TextView)mToast.getView().findViewById(android.R.id.message);
            textView.setGravity(Gravity.CENTER);
            
//            mToast = new Toast(context);
        }
        mToast.setText(msg);
//        mToast.setView(toastView);
//        mToast.setDuration(Toast.LENGTH_SHORT);
//        mToast.setGravity( mGravity , 0 , 100 );
        mToast.show();
        
        Logger.e(TAG, "show toast " + msg + " context "+ msg, "oddshou");
    }

    public static void cancelToast()
    {
        if (mToast != null)
        {
            mToast.cancel();
        }
    }

    /**
     * 只用于 异常 toast 显示 1000 用户名已存在 1001 账户不存在 1002 账号余额不足 1003 原密码不对或新密码和原密码相同
     * 1004 此订单不存在
     * 
     * @param context
     * @param errorCode
     */
    public static void showFailed(Context context, int errorCode, String errMsg) {
        Logger.e(TAG, "showFailed errorCode " + errorCode + " errMsg " + errMsg, "oddshou");
        String string = errMsg;
        switch (errorCode) {
//            case 1:     //错误或 失败
//                string = errMsg;
//                break;
            case 102:   //非法用户
                string = context.getString(R.string.toast_error_openid);
                break;
            case 1000: // 此用户名已被占用
                string = context.getString(R.string.toast_name_has_used);
                break;
            case 1001: // 账户不存在

                break;
            case 1002: // 账号余额不足
                string = context.getString(R.string.toast_balance_not_enough);
                break;
            case 1003: // 原密码不对或新密码和原密码相同

                break;
            case 1004: // 此订单不存在

                break;
            case 1005: // 验证码错误
                string = context.getString(R.string.toast_validate_error);
                break;
            case 1012: //用户名不符合规则
                string = context.getString(R.string.toast_name_not_accept);
                break;
            default:
//                string = context.getString(R.string.error_msg_net_fail);
                break;
        }
//        if (errorCode < 1000) {
//            string = errMsg;
//        }
        if (string.length() > 0) {
            CSToast.show(context, string);
        }
    }

    /**
     * 只用于error 提示信息
     * 
     * @param context
     */
    public static void showError(Context context) {
        CSToast.show(context, context.getString(R.string.error_msg_net_fail));
    }

    public static void showNormal(Context context, String text) {
        CSToast.show(context, text);
    }
}
