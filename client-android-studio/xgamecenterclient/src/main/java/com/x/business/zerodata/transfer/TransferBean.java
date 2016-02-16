package com.x.business.zerodata.transfer;

import android.os.Parcel;
import android.os.Parcelable;

public class TransferBean implements Parcelable {

	private String fileRawPath;//文件原始路径
	private int fileType; //文件类型
	private String fileSuffix; //文件后缀
	private long fileSize; //文件大小
	private long currentBytes; //下载大小
	private String fileName; //文件名
	private String fileSavePath;//存储路径
	private int fileStatus; //文件状态
	private String fileUrl; //文件请求地址
	private String speed;
	private String exAttribute; //额外属性

	@Override
	public int describeContents() {
		return 0;
	}

	public TransferBean(Parcel source) {
		fileRawPath = source.readString();
		fileType = source.readInt();
		fileSuffix = source.readString();
		fileSize = source.readLong();
		currentBytes = source.readLong();
		fileName = source.readString();
		fileSavePath = source.readString();
		fileStatus = source.readInt();
		fileUrl = source.readString();
		speed = source.readString();
		exAttribute = source.readString();
	}

	public static Parcelable.Creator<TransferBean> getCreator() {
		return CREATOR;
	}

	public static final Parcelable.Creator<TransferBean> CREATOR = new Parcelable.Creator<TransferBean>() {

		@Override
		public TransferBean createFromParcel(Parcel source) {
			return new TransferBean(source);
		}

		@Override
		public TransferBean[] newArray(int size) {
			return new TransferBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fileRawPath);
		dest.writeInt(fileType);
		dest.writeString(fileSuffix);
		dest.writeLong(fileSize);
		dest.writeLong(currentBytes);
		dest.writeString(fileName);
		dest.writeString(fileSavePath);
		dest.writeInt(fileStatus);
		dest.writeString(fileUrl);
		dest.writeString(speed);
		dest.writeString(exAttribute);

	}

	public TransferBean() {

	}

	public TransferBean(String fileRawPath, int fileType, String fileSuffix, long fileSize, String fileName,
			String fileSavePath, int fileStatus, String fileUrl, String exAttribute) {
		super();
		this.fileName = fileName;
		this.fileRawPath = fileRawPath;
		this.fileSavePath = fileSavePath;
		this.fileSize = fileSize;
		this.fileSuffix = fileSuffix;
		this.fileType = fileType;
		this.fileStatus = fileStatus;
		this.fileUrl = fileUrl;
		this.exAttribute = exAttribute;
	}

	public String getFileRawPath() {
		return fileRawPath;
	}

	public void setFileRawPath(String fileRawPath) {
		this.fileRawPath = fileRawPath;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSavePath() {
		return fileSavePath;
	}

	public void setFileSavePath(String fileSavePath) {
		this.fileSavePath = fileSavePath;
	}

	public int getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(int fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public long getCurrentBytes() {
		return currentBytes;
	}

	public void setCurrentBytes(long currentBytes) {
		this.currentBytes = currentBytes;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getExAttribute() {
		return exAttribute;
	}

	public void setExAttribute(String exAttribute) {
		this.exAttribute = exAttribute;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

}
