package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * @ClassName: AccordDownloadRequest
 * @Desciption: 统计App下载数量，请求数据
 
 * @Date: 2014-2-21 下午1:22:39
 */

public class AccordDownloadRequest extends CommonRequest {

	public AccordDownloadRequest(int rc) {
		super(rc, Constan.SIGN);
	}

	public MasPlay masPlay;
	public MasUser masUser;
	public AccordDownloadData data;

	public static class AccordDownloadData {

		public int apkId;
		public int appId;
		public int imageId;
		public int musicId;
		public String appName;
		public String imageName;
		public String musicName;
		public int clientId;
		public int ct;
		public int raveId;
		public String packageName;
		public int versionCode;
		public String versionName;
		public int downOrUpdate; // 1=下载， 2=更新
		
		public AccordDownloadData() {
			this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
		}

		public int getApkId() {
			return apkId;
		}

		public void setApkId(int apkId) {
			this.apkId = apkId;
		}

		public int getAppId() {
			return appId;
		}

		public void setAppId(int appId) {
			this.appId = appId;
		}

		public int getCt() {
			return ct;
		}

		public void setCt(int ct) {
			this.ct = ct;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public int getClientId() {
			return clientId;
		}

		public void setClientId(int clientId) {
			this.clientId = clientId;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public int getImageId() {
			return imageId;
		}

		public void setImageId(int imageId) {
			this.imageId = imageId;
		}

		public int getMusicId() {
			return musicId;
		}

		public void setMusicId(int musicId) {
			this.musicId = musicId;
		}

		public String getImageName() {
			return imageName;
		}

		public void setImageName(String imageName) {
			this.imageName = imageName;
		}

		public String getMusicName() {
			return musicName;
		}

		public void setMusicName(String musicName) {
			this.musicName = musicName;
		}

		public int getDownOrUpdate() {
			return downOrUpdate;
		}

		public void setDownOrUpdate(int downOrUpdate) {
			this.downOrUpdate = downOrUpdate;
		}
	}

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public AccordDownloadData getData() {
		return data;
	}

	public void setData(AccordDownloadData data) {
		this.data = data;
	}

	public MasPlay getMasPlay() {
		return masPlay;
	}

	public void setMasPlay(MasPlay masPlay) {
		this.masPlay = masPlay;
	}

}
