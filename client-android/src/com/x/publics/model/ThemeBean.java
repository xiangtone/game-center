package com.x.publics.model;

import java.io.Serializable;

/**
 * @ClassName: ThemeBean
 * @Desciption: 图片专辑实体类
 
 * @Date: 2014-3-25 上午9:55:02
 */
public class ThemeBean implements Serializable {

	private static final long serialVersionUID = -8981670846764488353L;
	private int themeId; // 专辑ID
	private String name; // 专辑名称
	private String icon; // 小图片
	private String bigicon; // 大图片
	private String description; // 专辑描述
	private int  wallpaperNum;  //专辑下的图片数量

	/** 
	 * @return wallpaperNum 
	 */
	
	public int getWallpaperNum() {
		return wallpaperNum;
	}

	/** 
	 * @param wallpaperNum 要设置的 wallpaperNum 
	 */
	
	public void setWallpaperNum(int wallpaperNum) {
		this.wallpaperNum = wallpaperNum;
	}

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getBigicon() {
		return bigicon;
	}

	public void setBigicon(String bigicon) {
		this.bigicon = bigicon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}