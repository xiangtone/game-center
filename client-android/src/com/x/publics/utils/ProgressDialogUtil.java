package com.x.publics.utils;

import android.content.Context;

/**
 * @ClassName: ProgressDialogUtil
 * @Desciption: TODO
 
 * @Date: 2014-4-14 下午2:10:10
 */
public class ProgressDialogUtil {

	public static CustomProgressDialog progressDialog;

	/**
	 * 显示进度框
	 * @param context
	 * @param msg 显示内容
	 */
	public static void openProgressDialog(Context context, String msg) {
		progressDialog = CustomProgressDialog.createDialog(context);
		progressDialog.setMessage(msg);
		progressDialog.setCanceledOnTouchOutside(false); // 设置点击progressDialog以外的区域,对话框不消失
		if (null != progressDialog && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	/**
	 * 显示进度框
	 * @param context
	 * @param title 标题
	 * @param msg 显示内容
	 */
	public static void openProgressDialog(Context context, String title,
			String msg) {
		progressDialog = CustomProgressDialog.createDialog(context);
		progressDialog.setTitle(title); // --标题
		progressDialog.setMessage(msg); // --内容
		progressDialog.setCanceledOnTouchOutside(false); // 设置点击progressDialog以外的区域,对话框不消失
		if (null != progressDialog && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	/**
	 * 显示进度框, 可设定是否可以按"返回键"取消进度框(强制用户等待)
	 * @param context
	 * @param title
	 * @param msg
	 * @param cancelable
	 */
	public static void openProgressDialog(Context context, String title,
			String msg, boolean cancelable) {
		progressDialog = CustomProgressDialog.createDialog(context);
		progressDialog.setTitle(title);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(cancelable) ;
		if (null != progressDialog && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	/**
	 * 显示进度框
	 * @param context
	 * @param titleId 标题
	 * @param msgId 显示内容
	 */
	public static void openProgressDialog(Context context, int titleId,
			int msgId) {
		progressDialog = CustomProgressDialog.createDialog(context);
		progressDialog.setTitle(context.getString(titleId));
		progressDialog.setMessage(context.getString(msgId));
		if (null != progressDialog && !progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public static void setProgressDialogMsg(String msg) {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.setMessage(msg);
			}
		}
	}

	/**
	 * 关闭进度框
	 */
	public static void closeProgressDialog() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

}
