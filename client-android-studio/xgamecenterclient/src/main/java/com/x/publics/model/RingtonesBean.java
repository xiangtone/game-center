package com.x.publics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: RingtonesBean
 * @Description: 铃声实体类
 
 * @date 2014-4-9 下午1:26:55
 * 
 */
public class RingtonesBean implements Parcelable {

	private int musicId; // 音乐ID
	private int categoryId; // 类型ID
	private int duration; // 时长 (秒)
	private String musicName; // 音乐名称
	private String logo; // 小图（手机）
	private String biglogo; // 中图（平板）
	private String url; // 下载
	private String filepath; // 本地路径
	private String brief; // 简介
	private int fileSize; // 文件大小
	private int totalBytes;
	private int currentBytes;
	private int status; // 应用状态 0正常 1下载中 2暂停
	private String process; // 下载进度
	
	private String onlineTime; 
	private String downloadNum; 

	private boolean isFromMain ;
	public RingtonesBean() {
		// TODO Auto-generated constructor stub
	}

	public RingtonesBean(Parcel source) {
		musicId = source.readInt();
		categoryId = source.readInt();
		duration = source.readInt();
		musicName = source.readString();
		logo = source.readString();
		biglogo = source.readString();
		url = source.readString();
		filepath = source.readString();
		brief = source.readString();
		fileSize = source.readInt();
		totalBytes = source.readInt();
		currentBytes = source.readInt();
		status = source.readInt();
		process = source.readString();
	}

	
	/** 
	 * @return isFromMain 
	 */
	
	public boolean isFromMain() {
		return isFromMain;
	}

	/** 
	 * @param isFromMain 要设置的 isFromMain 
	 */
	
	public void setFromMain(boolean isFromMain) {
		this.isFromMain = isFromMain;
	}

	/** 
	 * @return creator 
	 */
	
	public static Creator<RingtonesBean> getCreator() {
		return CREATOR;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(int totalBytes) {
		this.totalBytes = totalBytes;
	}

	public int getCurrentBytes() {
		return currentBytes;
	}

	public void setCurrentBytes(int currentBytes) {
		this.currentBytes = currentBytes;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeInt(musicId);
		dest.writeInt(categoryId);
		dest.writeInt(duration);
		dest.writeString(musicName);
		dest.writeString(logo);
		dest.writeString(biglogo);
		dest.writeString(url);
		dest.writeString(filepath);
		dest.writeString(brief);
		dest.writeInt(fileSize);
		dest.writeInt(totalBytes);
		dest.writeInt(currentBytes);
		dest.writeInt(status);
		dest.writeString(process);
	}

	public static final Creator<RingtonesBean> CREATOR = new Creator<RingtonesBean>() {

		@Override
		public RingtonesBean createFromParcel(Parcel source) {
			return new RingtonesBean(source);
		}

		@Override
		public RingtonesBean[] newArray(int size) {
			return new RingtonesBean[size];
		}
	};

}
