package com.hykj.gamecenter.sdk.entry;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import com.hykj.gamecenter.sdk.aidl.ICallBackInterface;

public class NiuAppEntry implements Parcelable {

	private int appId;
	private String appKey;
	private String appName;
	private String appPackageName;
	private int appType;
	private int devMode;
	private ICallBackInterface callback;

	public static final Parcelable.Creator<NiuAppEntry> CREATOR = new Creator<NiuAppEntry>() {

		@Override
		public NiuAppEntry createFromParcel(Parcel source) {
			NiuAppEntry entry = new NiuAppEntry();
			entry.setAppId(source.readInt());
			entry.setAppKey(source.readString());
			entry.setAppName(source.readString());
			entry.setAppPackageName(source.readString());
			entry.setAppType(source.readInt());
			entry.setDevMode(source.readInt());
			IBinder binder = source.readStrongBinder();
			ICallBackInterface ii = ICallBackInterface.Stub.asInterface(binder);
			entry.setCallback(ii);
			return entry;
		}

		@Override
		public NiuAppEntry[] newArray(int size) {
			return new NiuAppEntry[size];
		}

	};

	public NiuAppEntry() {
	}

	public NiuAppEntry(NiuAppInfo niuAppInfo) {
		appId = niuAppInfo.getAppId();
		appKey = niuAppInfo.getAppKey();
		appName = niuAppInfo.getAppName();
		appPackageName = niuAppInfo.getAppPackageName();
		appType = niuAppInfo.getAppType();
		devMode = niuAppInfo.getDevMode();
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPackageName() {
		return appPackageName;
	}

	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public int getDevMode() {
		return devMode;
	}

	public void setDevMode(int devMode) {
		this.devMode = devMode;
	}

	public ICallBackInterface getCallback() {
		return callback;
	}

	public void setCallback(ICallBackInterface callback) {
		this.callback = callback;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(appId);
		dest.writeString(appKey);
		dest.writeString(appName);
		dest.writeString(appPackageName);
		dest.writeInt(appType);
		dest.writeInt(devMode);
		dest.writeStrongInterface(callback);
	}

}
