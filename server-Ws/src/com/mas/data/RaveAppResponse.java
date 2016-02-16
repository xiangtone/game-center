package com.mas.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.market.pojo.TAppAlbumRes;
import com.mas.market.pojo.TAppComment;
import com.mas.market.pojo.TAppPicture;
import com.mas.market.pojo.TSearchKeyword;

@XmlRootElement
public class RaveAppResponse extends BaseResponse{
	
	private List<TAppAlbumRes> applist;
	private Integer appNum;
	private List<MustHaveData> mustHavelist;
	private Integer mustHaveNum;
	private List<TAppAlbumRes> recommendlist;
	private Integer recommendNum;
	private Boolean isLast;
	private TAppAlbumRes app;
	private List<TAppPicture> piclist;
	private Integer picNum;
	private List<TAppComment> commentlist;
	private List<TAppComment> starslist;
	private Integer commentNum;
	private List<TSearchKeyword> keywordlist;
	private Integer keywordNum;
	
	public List<TAppAlbumRes> getApplist() {
		return applist;
	}

	public void setApplist(List<TAppAlbumRes> applist) {
		this.applist = applist;
	}

	public Integer getAppNum() {
		return appNum;
	}

	public void setAppNum(Integer appNum) {
		this.appNum = appNum;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}

	public TAppAlbumRes getApp() {
		return app;
	}

	public void setApp(TAppAlbumRes app) {
		this.app = app;
	}

	public List<TAppPicture> getPiclist() {
		return piclist;
	}

	public void setPiclist(List<TAppPicture> piclist) {
		this.piclist = piclist;
	}

	public Integer getPicNum() {
		return picNum;
	}

	public void setPicNum(Integer picNum) {
		this.picNum = picNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public List<TAppComment> getCommentlist() {
		return commentlist;
	}

	public void setCommentlist(List<TAppComment> commentlist) {
		this.commentlist = commentlist;
	}

	public List<TAppComment> getStarslist() {
		return starslist;
	}

	public void setStarslist(List<TAppComment> starslist) {
		this.starslist = starslist;
	}

	public List<TAppAlbumRes> getRecommendlist() {
		return recommendlist;
	}

	public void setRecommendlist(List<TAppAlbumRes> recommendlist) {
		this.recommendlist = recommendlist;
	}

	public Integer getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(Integer recommendNum) {
		this.recommendNum = recommendNum;
	}

	public List<TSearchKeyword> getKeywordlist() {
		return keywordlist;
	}

	public void setKeywordlist(List<TSearchKeyword> keywordlist) {
		this.keywordlist = keywordlist;
	}

	public Integer getKeywordNum() {
		return keywordNum;
	}

	public void setKeywordNum(Integer keywordNum) {
		this.keywordNum = keywordNum;
	}

	public Integer getMustHaveNum() {
		return mustHaveNum;
	}

	public void setMustHaveNum(Integer mustHaveNum) {
		this.mustHaveNum = mustHaveNum;
	}

	public List<MustHaveData> getMustHavelist() {
		return mustHavelist;
	}

	public void setMustHavelist(List<MustHaveData> mustHavelist) {
		this.mustHavelist = mustHavelist;
	}
}
