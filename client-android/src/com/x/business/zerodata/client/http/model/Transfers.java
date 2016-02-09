/**   
* @Title: Transfers.java
* @Package com.mas.amineappstore.business.zerodata.client.http.model
* @Description: TODO 

* @date 2014-3-20 下午12:33:00
* @version V1.0   
*/

package com.x.business.zerodata.client.http.model;

/**
* @ClassName: Transfers
* @Description: TODO 

* @date 2014-3-20 下午12:33:00
* 
*/

public class Transfers {

	private String fileRawPath; //文件原始路径
	private int fileType; //文件类型
	private String fileSuffix; //文件后缀
	private long fileSize; //文件大小
	private String fileName; //文件名
	private String fileUrl; //文件请求地址
	private String exAttribute; //额外属性

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

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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
