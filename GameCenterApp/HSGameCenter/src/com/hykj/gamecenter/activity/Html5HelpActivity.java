
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSLoadingView;
import com.hykj.gamecenter.ui.widget.CSLoadingView.ICSListViewLoadingRetry;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;

public class Html5HelpActivity extends Activity {
    private static final String TAG = "Html5HelpActivity";
    private CSCommonActionBar mActionBar;
    private WebView mWebView;

    private View mLoadingDataViewPage = null;
    private CSLoadingView mLoadingView;
    private View mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.recharge_help_main);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        mActionBar = (CSCommonActionBar) findViewById(R.id.movie_actionbar);
        String title = getIntent().getStringExtra(StatisticManager.KEY_HTML5_HELP_TITLE);
        mActionBar.setTitle(title);
        mActionBar.SetOnActionBarClickListener(mActionBarclickListener);

        mWebView = (WebView) findViewById(R.id.webView_help);
        //        mLoadingDataViewPage = ((ViewStub) findViewById(R.id.networkingLoading)).inflate();
        mLoading = findViewById(R.id.networkingLoading);
        mLoading.setVisibility(View.VISIBLE);
        mLoading.setBackgroundColor(getResources().getColor(R.color.white));
        mLoadingView = (CSLoadingView) findViewById(R.id.loadingView);
        mLoadingView.setOnRetryListener(mICSListViewLoadingRetry);
        mLoadingView.setBackgroundColor(getResources().getColor(R.color.white));
        mLoadingView.hide();
        String url = getIntent().getStringExtra(StatisticManager.KEY_HTML5_HELP_URL);
        Logger.i(TAG, "initView " + "url = " + url, "oddshou");
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(mWebViewClient);

    }

    private final ICSListViewLoadingRetry mICSListViewLoadingRetry = new ICSListViewLoadingRetry() {

        @Override
        public void onRetry() {
            mWebView.reload();
            mLoading.setVisibility(View.VISIBLE);
            mLoadingView.hide();
        }

    };

    private OnActionBarClickListener mActionBarclickListener = new OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, View view) {
            // TODO Auto-generated method stub
            switch (position) {
                case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;

                default:
                    break;
            }
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("url", "url=" + url);
            if (url.indexOf("0755") > 0) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
//            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            // TODO Auto-generated method stub
            mLoadingView.showNoNetwork();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            //            mLoadingDataViewPage.setVisibility(View.GONE);
            super.onPageFinished(view, url);
            //             mLoading.setVisibility(view.GONE);
            delayHandler.sendEmptyMessageDelayed(MSG_LOADING_GONG, 300);
        }
    };

    public static final int MSG_LOADING_GONG = 0X01;
    private Handler delayHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_LOADING_GONG:
                    mLoading.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }

        }
    };

}
