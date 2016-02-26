/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package		Cobub Razor
 
 * @copyright	Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license		http://www.cobub.com/products/cobub-razor/license
 * @link		http://www.cobub.com/products/cobub-razor/
 * @since		Version 0.1
 * @filesource
 */
package com.x.publics.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class MyCrashHandler implements UncaughtExceptionHandler {
	private static MyCrashHandler myCrashHandler;
	private Context context;
	private Activity activity;
	private Object stacktrace;
	private String time;

	private MyCrashHandler() {

	}

	public static synchronized MyCrashHandler getInstance() {
		if (myCrashHandler != null) {
			return myCrashHandler;
		} else {
			myCrashHandler = new MyCrashHandler();
			return myCrashHandler;
		}
	}

	public void init(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
	}

	public void uncaughtException(Thread arg0, Throwable arg1) {

		String errorinfo = getErrorInfo(arg1);
		String ssString = errorinfo.substring(0);
		String[] ss = ssString.split("\n\t");
		String headstring = ss[0] + "\n\t" + ss[1] + "\n\t" + ss[2] + "\n\t";
		String newErrorInfoString = headstring + errorinfo;

		stacktrace = errorinfo.replaceAll("\n\t", " ").replaceAll("\n", "");
		LogUtil.getLogger().saveLog(stacktrace.toString());
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				new AlertDialog.Builder(activity).setCancelable(false).setTitle(" 提示")
						.setMessage(/*stacktrace.toString()*/"程序异常，即将退出。")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								android.os.Process.killProcess(android.os.Process.myPid());

							}
						}).create().show();
				Looper.loop();
			}
		}).start();
	}

	private HashMap<String, Object> getErrorInfo(Context context) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HashMap<String, Object> errorInfo = new HashMap<String, Object>();
		//		errorInfo.put("clientId", CommentUtils.getClientId(context));
		//		errorInfo.put("exception", URLEncoder.encode(stacktrace.toString()));
		//		errorInfo.put("createTime", sdf.format(new Date(System.currentTimeMillis())));
		//		errorInfo.put("title", "error");
		//		errorInfo.put("type", "C");
		return errorInfo;
	}

	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

}