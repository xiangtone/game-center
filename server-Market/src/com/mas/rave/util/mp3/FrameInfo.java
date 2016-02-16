package com.mas.rave.util.mp3;

public class FrameInfo {
	private String frameId;
	private int frameContentSize;
	private byte[]flag;
	private byte[] content=null;
	public FrameInfo() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public int getFrameContentSize() {
		return frameContentSize;
	}

	public void setFrameContentSize(int frameContentSize) {
		this.frameContentSize = frameContentSize;
	}

	public byte[] getFlag() {
		return flag;
	}
	public void setFlag(byte[] flag) {
		this.flag = flag;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
