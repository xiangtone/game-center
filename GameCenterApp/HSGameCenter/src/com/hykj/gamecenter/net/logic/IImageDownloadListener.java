package com.hykj.gamecenter.net.logic;

import android.graphics.Bitmap;

import com.hykj.gamecenter.net.HttpSessionCallBack;

public interface IImageDownloadListener extends HttpSessionCallBack {
	// 图标下载异常
	void onDownloadIconException(String url, int errorCode);

	// 图标下载完成
	void onDownloadIconFinish(String url, byte[] data);

	// 图标获取完成
	void onGetIcon(String url, Bitmap bitmap);

}
