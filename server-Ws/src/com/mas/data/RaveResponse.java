package com.mas.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.market.pojo.RaveColumn;
import com.mas.market.pojo.TAppCollection;
import com.mas.market.pojo.TClientFeedback;
import com.mas.market.pojo.TClientFeedbackZapp;
import com.mas.market.pojo.TClientSkin;
import com.mas.market.pojo.TRaveCountry;

@XmlRootElement
public class RaveResponse extends BaseResponse{
	
	private List<ArrayList<RaveColumn>> columnlist;
	private Integer autoRaveId;
	private Integer columnNum;
	private Boolean isLast;
	private List<RaveColumn> categorylist;
	
	private Integer categoryNum;

	private List<RaveColumn> themelist;
	
	private Integer themeNum;
	
	private List<TAppCollection> collectionList;
	
	private Integer collectionNum;

	private List<TRaveCountry> countryList;
	
	private Integer countryNum;
	
	private List<TClientFeedbackZapp> feedbackCommonList;
	private List<TClientFeedback> feedbackClientList;
	private Integer feedbackNum;
	private Integer feedbackCode;
	private Boolean feedbackAttention = null;
	private List<TClientSkin> skinlist;
	private Integer skinNum;
	private Integer skinCode;
	private Boolean skinAttention = null;
	
	public Integer getColumnNum() {
		return columnNum;
	}
	public void setColumnNum(Integer columnNum) {
		this.columnNum = columnNum;
	}
	public List<ArrayList<RaveColumn>> getColumnlist() {
		return columnlist;
	}
	public void setColumnlist(List<ArrayList<RaveColumn>> columnlist) {
		this.columnlist = columnlist;
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
	public List<TRaveCountry> getCountryList() {
		return countryList;
	}
	public void setCountryList(List<TRaveCountry> countryList) {
		this.countryList = countryList;
	}
	public Integer getCountryNum() {
		return countryNum;
	}
	public void setCountryNum(Integer countryNum) {
		this.countryNum = countryNum;
	}
	public Integer getAutoRaveId() {
		return autoRaveId;
	}
	public void setAutoRaveId(Integer autoRaveId) {
		this.autoRaveId = autoRaveId;
	}
	public Integer getFeedbackNum() {
		return feedbackNum;
	}
	public void setFeedbackNum(Integer feedbackNum) {
		this.feedbackNum = feedbackNum;
	}
	public Boolean getIsLast() {
		return isLast;
	}
	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}
	public List<TClientFeedbackZapp> getFeedbackCommonList() {
		return feedbackCommonList;
	}
	public void setFeedbackCommonList(List<TClientFeedbackZapp> feedbackCommonList) {
		this.feedbackCommonList = feedbackCommonList;
	}
	public List<TClientFeedback> getFeedbackClientList() {
		return feedbackClientList;
	}
	public void setFeedbackClientList(List<TClientFeedback> feedbackClientList) {
		this.feedbackClientList = feedbackClientList;
	}
	public Integer getFeedbackCode() {
		return feedbackCode;
	}
	public void setFeedbackCode(Integer feedbackCode) {
		this.feedbackCode = feedbackCode;
	}
	public Boolean getFeedbackAttention() {
		return feedbackAttention;
	}
	public void setFeedbackAttention(Boolean feedbackAttention) {
		this.feedbackAttention = feedbackAttention;
	}
	public List<TAppCollection> getCollectionList() {
		return collectionList;
	}
	public void setCollectionList(List<TAppCollection> collectionList) {
		this.collectionList = collectionList;
	}
	public Integer getCollectionNum() {
		return collectionNum;
	}
	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}
	public Integer getSkinNum() {
		return skinNum;
	}
	public void setSkinNum(Integer skinNum) {
		this.skinNum = skinNum;
	}
	public Integer getSkinCode() {
		return skinCode;
	}
	public void setSkinCode(Integer skinCode) {
		this.skinCode = skinCode;
	}
	public Boolean getSkinAttention() {
		return skinAttention;
	}
	public void setSkinAttention(Boolean skinAttention) {
		this.skinAttention = skinAttention;
	}
	public List<TClientSkin> getSkinlist() {
		return skinlist;
	}
	public void setSkinlist(List<TClientSkin> skinlist) {
		this.skinlist = skinlist;
	}
	
}