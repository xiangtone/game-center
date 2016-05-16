package com.hykj.gamecenter.services;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.hykj.gamecenter.account.SDKActivity;
import com.hykj.gamecenter.sdk.aidl.ICallBackInterface;
import com.hykj.gamecenter.sdk.aidl.SDKServiceInterface;
import com.hykj.gamecenter.sdk.entry.NiuAppEntry;
import com.hykj.gamecenter.sdk.entry.NiuAppInfo;
import com.hykj.gamecenter.sdk.entry.NiuOrderInfo;
import com.hykj.gamecenter.sdk.entry.NiuSDKRoleInfo;
//import com.niuwan.gamecenter.sdk.ui.SDKActivity;
import com.hykj.gamecenter.utils.Logger;

public class SDKServiceInterfaceImpl extends SDKServiceInterface.Stub {

	private static final String TAG = "SDKServiceInterfaceImpl";
    private ICallBackInterface mCallBack;
	private final Context mContext;

	public SDKServiceInterfaceImpl(Context context) {
		mContext = context;
	}

	@Override
	public void registerCallBack(ICallBackInterface iCallBackInterface) throws RemoteException {
		mCallBack = iCallBackInterface;
	}

	@Override
	public void login(NiuAppInfo padaAppInfo) throws RemoteException {
		NiuAppEntry entry = new NiuAppEntry(padaAppInfo);
		entry.setCallback(mCallBack);
		Intent intent = new Intent(mContext, SDKActivity.class);
		intent.putExtra("app", entry);
		intent.putExtra("sdk", SDKActivity.SDK_REQUEST_LOGIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		Logger.d(TAG, "login " + entry, "oddshou");
	}

	@Override
	public void pay(NiuAppInfo padaAppInfo, NiuOrderInfo padaOrderInfo, NiuSDKRoleInfo padaSDKRoleInfo) throws RemoteException {
		NiuAppEntry entry = new NiuAppEntry(padaAppInfo);
		entry.setCallback(mCallBack);
		Intent intent = new Intent(mContext, SDKActivity.class);
		intent.putExtra("app", entry);
		intent.putExtra("order", padaOrderInfo);
		intent.putExtra("roleinfo", padaSDKRoleInfo);
		intent.putExtra("sdk", SDKActivity.SDK_REQUEST_PAY);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		Logger.d(TAG, "pay " + entry + " roleInfo "+ padaSDKRoleInfo, "oddshou");
	}

}
