package com.x.publics.model;

import java.io.Serializable;
import java.util.List;

/**
 * {@link #image_id} 相册ID 
 * {@link #path_top_file} 封面显示的路径 
 * {@link #name_album}相册名称
 * {@link #isChecked} 是否选中
 * {@link #list} 相册集
 * 
 
 * 
 */
public class AlbumBean implements Serializable {

	private static final long serialVersionUID = -1975232269939215817L;
	private int image_id;
	private String path_top_file;
	private String name_album;
	private List<FileBean> list;
	private int ischeck = 0; //0=不选中，1=选中
	private int status = 0; // 0=CheckBox隐藏，1=CheckBox显示

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIscheck() {
		return ischeck;
	}

	public void setIscheck(int ischeck) {
		this.ischeck = ischeck;
	}

	public int getImage_id() {
		return image_id;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

	public String getPath_top_file() {
		return path_top_file;
	}

	public void setPath_top_file(String path_top_file) {
		this.path_top_file = path_top_file;
	}

	public String getName_album() {
		return name_album;
	}

	public void setName_album(String name_album) {
		this.name_album = name_album;
	}

	public List<FileBean> getList() {
		return list;
	}

	public void setList(List<FileBean> list) {
		this.list = list;
	}

}
