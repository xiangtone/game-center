/**   
* @Title: DynamicListViewItem.java
* @Package com.x.ui.view.dynamic
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-23 下午2:40:33
* @version V1.0   
*/

package com.x.ui.view.dynamic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
* @ClassName: DynamicListViewItem
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-23 下午2:40:33
*
 */
public class DynamicListViewItem implements Parcelable {

	private int apkId;
	private String appIcon;
	private String appName;
	private String countryName;
	private String countryIcon;

	public DynamicListViewItem() {
		// TODO Auto-generated constructor stub
	}

	public DynamicListViewItem(Parcel source) {
		apkId = source.readInt();
		appIcon = source.readString();
		appName = source.readString();
		countryName = source.readString();
		countryIcon = source.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(apkId);
		dest.writeString(appIcon);
		dest.writeString(appName);
		dest.writeString(countryName);
		dest.writeString(countryIcon);
	}

	public static final Creator<DynamicListViewItem> CREATOR = new Creator<DynamicListViewItem>() {

		@Override
		public DynamicListViewItem createFromParcel(Parcel source) {
			return new DynamicListViewItem(source);
		}

		@Override
		public DynamicListViewItem[] newArray(int size) {
			return new DynamicListViewItem[size];
		}
	};

	/** 
	* @return apkId 
	*/

	public int getApkId() {
		return apkId;
	}

	/** 
	* @param apkId 要设置的 apkId 
	*/

	public void setApkId(int apkId) {
		this.apkId = apkId;
	}

	/** 
	* @return appIcon 
	*/

	public String getAppIcon() {
		return appIcon;
	}

	/** 
	* @param appIcon 要设置的 appIcon 
	*/

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	/** 
	* @return appName 
	*/

	public String getAppName() {
		return appName;
	}

	/** 
	* @param appName 要设置的 appName 
	*/

	public void setAppName(String appName) {
		this.appName = appName;
	}

	/** 
	* @return countryName 
	*/

	public String getCountryName() {
		return countryName;
	}

	/** 
	* @param countryName 要设置的 countryName 
	*/

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/** 
	* @return countryIcon 
	*/

	public String getCountryIcon() {
		return countryIcon;
	}

	/** 
	* @param countryIcon 要设置的 countryIcon 
	*/

	public void setCountryIcon(String countryIcon) {
		this.countryIcon = countryIcon;
	}

}
