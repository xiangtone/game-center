package com.x.publics.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LoginUtils {
	
	private static Context context;

	
	public Dialog login_dialog; //动态加载的dialog


	
	
	private static LoginUtils loginUtils =null;
	
	public static LoginUtils getInstances(){
		if(loginUtils == null){
			loginUtils = new LoginUtils();
		}
	return loginUtils;	
	}

	
	
	/**
	 * 动态创建一个dialog窗口
	 * @return 
	 */
	public  WebView showLoginDialog(Context context,String url) {
		// 打开登陆界面
		// 装dialog的线性布局Layoutparams
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

		RelativeLayout plaqueRelative = new RelativeLayout(context);

		// 设置padding
		plaqueRelative.setGravity(Gravity.CENTER);
		// 装webview的相对布局Layoutparams
		plaqueRelative.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置dialog宽度高度

		WebView webpobView = null;

		// webview的Layoutparams
		LayoutParams plaqueParams = new LayoutParams(
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT));

		webpobView = new WebView(context);
		webpobView.setLayoutParams(plaqueParams);
		webpobView.loadUrl(url);

		// 设置支持javascript
		webpobView.getSettings().setJavaScriptEnabled(true);

		// 启动缓存
		webpobView.getSettings().setAppCacheEnabled(true);

		webpobView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		

		login_dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		login_dialog.setCancelable(false);
		login_dialog.show();

		login_dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				dialog.cancel();
				return false;
			}
		});

		// 相对布局装webview
		plaqueRelative.addView(webpobView);
		// 线性布局装相对布局
		linearLayout.addView(plaqueRelative);
		// dialog加载线性布局
		login_dialog.setContentView(linearLayout);

	  return webpobView;
	}
	
//	public class JavascriptInterface {
//		private Context context;
//		
//		public JavascriptInterface(Context context) {
//			this.context = context;
//		}
//	}

}

