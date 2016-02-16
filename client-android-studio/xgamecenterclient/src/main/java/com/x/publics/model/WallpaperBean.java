package com.x.publics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: WallpaperBean
 * @Desciption: 壁纸实体类
 
 * @Date: 2014-3-14 下午2:01:26
 */
public class WallpaperBean implements Parcelable {

	private int imageId; // 图片ID
	private int categoryId; // 类型ID
	private String imageName; // 图片名称
	private String logo; // 小图（手机）
	private String biglogo; // 中图（平板）
	private String url; // 大图（下载）
	private String brief; // 图片简介
	private int fileSize; // 文件大小

	public WallpaperBean() {
		// TODO Auto-generated constructor stub
	}
	
	public WallpaperBean(Parcel source) {
		imageId = source.readInt();
		categoryId = source.readInt();
		imageName = source.readString();
		logo = source.readString();
		biglogo = source.readString();
		url = source.readString();
		brief = source.readString();
		fileSize = source.readInt();
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBiglogo() {
		return biglogo;
	}

	public void setBiglogo(String biglogo) {
		this.biglogo = biglogo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(imageId);
		dest.writeInt(categoryId);
		dest.writeString(imageName);
		dest.writeString(logo);
		dest.writeString(biglogo);
		dest.writeString(url);
		dest.writeString(brief);
		dest.writeInt(fileSize);
	}
	
	public static final Creator<WallpaperBean> CREATOR = new Creator<WallpaperBean>() {

		@Override
		public WallpaperBean createFromParcel(Parcel source) {
			return new WallpaperBean(source);
		}

		@Override
		public WallpaperBean[] newArray(int size) {
			return new WallpaperBean[size];
		}
	};

}
