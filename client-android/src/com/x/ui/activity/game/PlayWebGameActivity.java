/**   
* @Title: PlayWebGameActivity.java
* @Package com.x.ui.activity.game
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-25 上午10:34:18
* @version V1.0   
*/

package com.x.ui.activity.game;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.x.R;
import com.x.business.webgame.WebGameInterface;
import com.x.publics.utils.LogUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.ProgressWebView;

/**
* @ClassName: PlayWebGameActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-8-25 上午10:34:18
* 
*/
@SuppressLint("SetJavaScriptEnabled")
public class PlayWebGameActivity extends BaseActivity {

	private static final String TAG = "PlayWebGameActivity";
	private ProgressWebView mGameWebView;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_play_web_game);
		initUi();
	}

	private void initUi() {
		mGameWebView = (ProgressWebView) findViewById(R.id.apwg_game_webview);
		setWebView(mGameWebView);
		mGameWebView.loadUrl("http://game.zapp365.com/");
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setWebView(WebView webView) {
		WebSettings settings = webView.getSettings();

		// Enable Javascript
		settings.setJavaScriptEnabled(true);
		settings.setAppCacheEnabled(false);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setLoadsImagesAutomatically(true);
		settings.setUseWideViewPort(true);// 网页自适应
		settings.setLoadWithOverviewMode(true);// 网页自适应
		settings.setSupportZoom(false);
		settings.setAllowFileAccess(true); // 设置可以访问文件
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true);
		settings.setSaveFormData(true);

		webView.setHorizontalScrollBarEnabled(false);// 去掉横向的滚动条
		webView.setVerticalScrollBarEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				LogUtil.getLogger().d(
						TAG,
						"onReceivedError-->" + "errorCode:" + errorCode + ",description:" + description
								+ ",failingUrl:" + failingUrl);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});

		/*webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onConsoleMessage(ConsoleMessage cm) {
				LogUtil.getLogger().d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
				return super.onConsoleMessage(cm);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				LogUtil.getLogger().d(TAG, "onJsAlert-->message");
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				LogUtil.getLogger().d(TAG, "" + newProgress);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
				LogUtil.getLogger().d(TAG, "onJsConfirm-->message");
				return super.onJsConfirm(view, url, message, result);
			}

		});*/

		webView.addJavascriptInterface(new WebGameInterface(context), "WebGameInterface");
	}

}
