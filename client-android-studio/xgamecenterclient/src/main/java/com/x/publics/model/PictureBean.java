package com.x.publics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: PictureBean.class
 * @Desciption: 应用详情截图
 
 * @Date: 2014-4-15 下午2:22:24
 */

public class PictureBean implements Parcelable {

	private int width; // 图片宽度
	private int length; // 图片高度
	private String url; // 图片url

	public PictureBean() {
		// TODO Auto-generated constructor stub
	}

	public PictureBean(Parcel source) {
		width = source.readInt();
		length = source.readInt();
		url = source.readString();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(width);
		dest.writeInt(length);
		dest.writeString(url);
	}

	public static final Creator<PictureBean> CREATOR = new Creator<PictureBean>() {

		@Override
		public PictureBean createFromParcel(Parcel source) {
			return new PictureBean(source);
		}

		@Override
		public PictureBean[] newArray(int size) {
			return new PictureBean[size];
		}
	};

}
