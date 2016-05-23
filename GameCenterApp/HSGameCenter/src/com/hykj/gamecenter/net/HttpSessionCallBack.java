package com.hykj.gamecenter.net;

public interface HttpSessionCallBack {

	void onSucceed(byte[] rspData);

	void onError(int errCode, String strErr);

}
