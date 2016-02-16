package com.mas.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.market.pojo.RaveColumn;
import com.mas.market.pojo.TResImageAlbumRes;
import com.mas.market.pojo.TResMusicAlbumRes;

@XmlRootElement
public class ResResponse extends BaseResponse{
	
	private List<TResMusicAlbumRes> musiclist;
	
	private Integer musicNum;
	
	private List<TResImageAlbumRes> imagelist;
	
	private Integer imageNum;
	
	private List<RaveColumn> categorylist;

	private Integer categoryNum;
	
	private List<RaveColumn> themelist;
	
	private Integer themeNum;
	
	private List<TResMusicAlbumRes> recommendMusiclist;
	
	private List<TResImageAlbumRes> recommendImagelist;
	
	private Integer recommendNum;
	
	private Boolean isLast;

	public List<TResMusicAlbumRes> getMusiclist() {
		return musiclist;
	}

	public void setMusiclist(List<TResMusicAlbumRes> musiclist) {
		this.musiclist = musiclist;
	}

	public Integer getMusicNum() {
		return musicNum;
	}

	public void setMusicNum(Integer musicNum) {
		this.musicNum = musicNum;
	}

	public List<TResImageAlbumRes> getImagelist() {
		return imagelist;
	}

	public void setImagelist(List<TResImageAlbumRes> imagelist) {
		this.imagelist = imagelist;
	}

	public Integer getImageNum() {
		return imageNum;
	}

	public void setImageNum(Integer imageNum) {
		this.imageNum = imageNum;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}

	public List<RaveColumn> getCategorylist() {
		return categorylist;
	}

	public void setCategorylist(List<RaveColumn> categorylist) {
		this.categorylist = categorylist;
	}

	public Integer getCategoryNum() {
		return categoryNum;
	}

	public void setCategoryNum(Integer categoryNum) {
		this.categoryNum = categoryNum;
	}

	public List<RaveColumn> getThemelist() {
		return themelist;
	}

	public void setThemelist(List<RaveColumn> themelist) {
		this.themelist = themelist;
	}

	public Integer getThemeNum() {
		return themeNum;
	}

	public void setThemeNum(Integer themeNum) {
		this.themeNum = themeNum;
	}

	public List<TResMusicAlbumRes> getRecommendMusiclist() {
		return recommendMusiclist;
	}

	public void setRecommendMusiclist(List<TResMusicAlbumRes> recommendMusiclist) {
		this.recommendMusiclist = recommendMusiclist;
	}

	public List<TResImageAlbumRes> getRecommendImagelist() {
		return recommendImagelist;
	}

	public void setRecommendImagelist(List<TResImageAlbumRes> recommendImagelist) {
		this.recommendImagelist = recommendImagelist;
	}

	public Integer getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(Integer recommendNum) {
		this.recommendNum = recommendNum;
	}
}