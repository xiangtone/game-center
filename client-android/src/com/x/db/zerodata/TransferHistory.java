/**   
* @Title: TransferHistory.java
* @Package com.mas.amineappstore.db.zerodata
* @Description: TODO 

* @date 2014-3-31 下午07:25:38
* @version V1.0   
*/

package com.x.db.zerodata;

import java.io.Serializable;

/**
* @ClassName: TransferHistory
* @Description: TODO 

* @date 2014-3-31 下午07:25:38
* 
*/

public class TransferHistory{

	private String fileRawPath;//文件原始路径
	private String fileSavePath;//存储路径
	private int fileType; //文件类型
	private String fileSuffix; //文件后缀
	private long fileSize; //文件大小
	private String fileName; //文件名
	private String fileUrl; //文件请求地址
	private int transferType; //传输类型 
	private String createTime;
	private String finishTime;
	private boolean finishTimeStatus;//用来判断finishTime的控件显示与否
	private String NameSort;

	public static class Type {
		public static final int SHARE = 0;
		public static final int RECEIVE = 1;
	}

	public String getFileRawPath() {
		return fileRawPath;
	}

	public void setFileRawPath(String fileRawPath) {
		this.fileRawPath = fileRawPath;
	}

	public String getFileSavePath() {
		return fileSavePath;
	}

	public void setFileSavePath(String fileSavePath) {
		this.fileSavePath = fileSavePath;
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

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public int getTransferType() {
		return transferType;
	}

	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public boolean isFinishTimeStatus() {
		return finishTimeStatus;
	}

	public void setFinishTimeStatus(boolean finishTimeStatus) {
		this.finishTimeStatus = finishTimeStatus;
	}

	public String getNameSort() {
		return NameSort;
	}

	public void setNameSort(String nameSort) {
		NameSort = nameSort;
	}

}
