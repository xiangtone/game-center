package com.hykj.gamecenter.ui.widget;

public interface ICSLoadingViewListener
{

    /*
     * 
     */
    void onInitRequestData();

    /*
     * 在网络错误提示下，点击重试按钮后调用
     */
    void onRetryRequestData();

}
