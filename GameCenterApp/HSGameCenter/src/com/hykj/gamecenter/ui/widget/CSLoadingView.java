
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class CSLoadingView extends LinearLayout {
    private final String tag = "CSLoadingView";

    public interface ICSListViewLoadingRetry {
        void onRetry();
    }

    private View mContainer;
    private ICSListViewLoadingRetry mRetryListener;
    private Context mContext;

    public CSLoadingView(Context context) {
        super(context);
        initView(context);
    }

    public CSLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setOnRetryListener(ICSListViewLoadingRetry listener) {
        mRetryListener = listener;
    }

    private void initView(Context context) {
        mContainer = LayoutInflater.from(context).inflate(
                R.layout.csl_cs_general_loading, null);
        addView(mContainer);
        setGravity(Gravity.CENTER);
        setVisibility(View.GONE);

        mContext = context;
    }

    public void setLoadingTipText(String tip) {
        TextView tipView = (TextView) mContainer
                .findViewById(R.id.csl_loading_tip);
        if (tipView != null && tip != null) {
            tipView.setText(tip);
        }
    }

    public boolean isNoNetworkShow() {
        View loadingView = mContainer.findViewById(R.id.csl_cs_loading);

        return loadingView.getVisibility() == View.VISIBLE;
    }

    public void showNoNetwork() {
        setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.VISIBLE);

        View loadingView = mContainer.findViewById(R.id.csl_cs_loading);
        loadingView.setVisibility(View.GONE);
        View noNetworkView = mContainer
                .findViewById(R.id.csl_cs_listview_no_networking);
        noNetworkView.setVisibility(View.VISIBLE);

        Button button = (Button) mContainer
                .findViewById(R.id.csl_network_retry_btn);
        button.setOnClickListener(mNetworkRetryButtonListener);

        Button settingbutton = (Button) mContainer
                .findViewById(R.id.csl_network_setting_btn);
        settingbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent toActivity = new Intent(Settings.ACTION_WIFI_SETTINGS);
                toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(toActivity);
            }
        });
    }

    public void showLoading() {
        setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.VISIBLE);

        View loadingView = mContainer.findViewById(R.id.csl_cs_loading);
        loadingView.setVisibility(View.VISIBLE);
        View noNetworkView = mContainer
                .findViewById(R.id.csl_cs_listview_no_networking);
        noNetworkView.setVisibility(View.GONE);
        Log.i(tag, "showLoading");
    }

    public void hide() {

        setVisibility(View.GONE);
        mContainer.setVisibility(View.GONE);

        View loadingView = mContainer.findViewById(R.id.csl_cs_loading);
        loadingView.setVisibility(View.GONE);
        View noNetworkView = mContainer
                .findViewById(R.id.csl_cs_listview_no_networking);
        noNetworkView.setVisibility(View.GONE);
        Log.i(tag, "hide");
    }

    private final View.OnClickListener mNetworkRetryButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mRetryListener == null)
                return;
            mRetryListener.onRetry();
        }
    };
}
