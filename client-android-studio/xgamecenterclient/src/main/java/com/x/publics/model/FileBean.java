package com.x.publics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件实体类
 
 * 
 */
public class FileBean implements Parcelable {

	private long dbId;
	private String fileName;
	private String filePath;
	private String icon;
	private long fileSize;
	private long ModifiedDate; //最后修改时间
	private int ischeck = 0; // 0=不选中，1=选中
	private int status = 0; // 0=CheckBox隐藏(正常)，1=CheckBox显示(编辑)
	private long duration;  //音频时长
	private long albumID;   //音频封面ID

	public FileBean() {
	}

	public FileBean(long dbId, String fileName, String filePath, String icon, long fileSize, 
			long ModifiedDate, int ischeck, int status, long duration, long albumID) {
		this.dbId = dbId;
		this.fileName = fileName;
		this.filePath = filePath;
		this.icon = icon;
		this.fileSize = fileSize;
		this.ModifiedDate = ModifiedDate;
		this.ischeck = ischeck;
		this.status = status;
		this.duration = duration;
		this.albumID = albumID;
	}

	public FileBean(Parcel source) {
		dbId = source.readLong();
		fileName = source.readString();
		filePath = source.readString();
		icon = source.readString();
		fileSize = source.readLong();
		ModifiedDate = source.readLong();
		ischeck = source.readInt();
		status = source.readInt();
		duration = source.readLong();
		albumID = source.readLong();
	}

	public int getIscheck() {
		return ischeck;
	}

	public void setIscheck(int ischeck) {
		this.ischeck = ischeck;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(long modifiedDate) {
		ModifiedDate = modifiedDate;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public long getAlbumID() {
		return albumID;
	}

	public void setAlbumID(long albumID) {
		this.albumID = albumID;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(dbId);
		dest.writeString(fileName);
		dest.writeString(filePath);
		dest.writeString(icon);
		dest.writeLong(fileSize);
		dest.writeLong(ModifiedDate);
		dest.writeInt(ischeck);
		dest.writeInt(status);
		dest.writeLong(duration);
		dest.writeLong(albumID);
	}

	public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {

		@Override
		public FileBean createFromParcel(Parcel source) {
			return new FileBean(source);
		}

		@Override
		public FileBean[] newArray(int size) {
			return new FileBean[size];
		}
	};
}
