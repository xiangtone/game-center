package com.hykj.gamecenter.ui.widget;

import android.view.View;

public class OnWifiClickListener
{
    /**
     * 在wifi状态下下载监听
     */
    public interface WifiDownLoadOnClickListener
    {
	void onWifiClickListener(View v);
    }

    /**
     * 在继续状态下是否继续下载(在3G网络环境下)
     */
    public interface NoWifiContinueDownloadListener
    {
	void NoWifiContinueListener(View v);
    }

}
