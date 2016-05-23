package com.hykj.gamecenter.logic.entry;

import android.net.Uri;
import com.hykj.gamecenter.utilscs.LogUtils;

public class UriGroupElemInfo {
	private int groupId; // 分组ID
	private int posId; // 位置ID，取推荐应用列表时，如首页推荐，可以在（当posId相同时）实现轮播，不支持轮播的页面，位置ID即是该数据在列表中的绝对位置，比排序号的优先级要高
	private int orderNo; // 排序号（该值不一定连续，可以用在列表显示时排序，支持轮播的页面，可以在对轮播顺序进行设定）
	private int elemType; // 元素类型：1App，2Link，3跳转至分类，4跳转至网游或单机，5跳转至专题（不支持跳转至推荐），6
							// 搜索词,
	private String showName; // 显示名称
	private String recommWord; // 推荐语
	private String appTypeName;// 应用类别
	private int recommFlag; // 推荐角标
	private int recommLevel; // 推荐级别，0~10代表从不推荐到推荐
	private String iconUrl; // ICON图标URL
	private String thumbPicUrl; // 缩略图URL
	private String adsPicUrl; // 广告图原图URL
	private String publishTime; // 发布时间
	private String showType; // 展示方式(1广告位)

	// ---------------------------------------- App
	private int appId; // 应用ID
	private String packName; // 安装包包名
	private int mainPackId; // 主安装包ID
	private int mainVerCode; // 主版本代码
	private String mainSignCode; // 主安装包签名特征码
	private String mainVerName; // 主版本名
	private int mainPackSize; // 主安装包大小
	// ---------------------------------------- Link
	private int jumpLinkId; // 链接ID
	private String jumpLinkUrl; // 链接地址
	// ---------------------------------------- 跳转至分类、网游、单机或专题
	private int jumpGroupId; // 跳转的分组ID，0依据分组与排序类型匹配-适用于分类，大于0具体分组-适用于专题（无需指定分组与排序类型）
	private int jumpGroupType; // 跳转的分组类型
	private int jumpOrderType; // 跳转的分组排序类型

	private final Uri mUri;

	public UriGroupElemInfo(Uri uri) {
		this.mUri = uri;
		setGroupElemInfo();
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getPosId() {
		return posId;
	}

	public void setPosId(int posId) {
		this.posId = posId;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getElemType() {
		return elemType;
	}

	public void setElemType(int elemType) {
		this.elemType = elemType;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getRecommWord() {
		return recommWord;
	}

	public void setRecommWord(String recommWord) {
		this.recommWord = recommWord;
	}

	public String getAppTypeName() {
		return appTypeName;
	}

	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}

	public int getRecommFlag() {
		return recommFlag;
	}

	public void setRecommFlag(int recommFlag) {
		this.recommFlag = recommFlag;
	}

	public int getRecommLevel() {
		return recommLevel;
	}

	public void setRecommLevel(int recommLevel) {
		this.recommLevel = recommLevel;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getThumbPicUrl() {
		return thumbPicUrl;
	}

	public void setThumbPicUrl(String thumbPicUrl) {
		this.thumbPicUrl = thumbPicUrl;
	}

	public String getAdsPicUrl() {
		return adsPicUrl;
	}

	public void setAdsPicUrl(String adsPicUrl) {
		this.adsPicUrl = adsPicUrl;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public int getMainPackId() {
		return mainPackId;
	}

	public void setMainPackId(int mainPackId) {
		this.mainPackId = mainPackId;
	}

	public int getMainVerCode() {
		return mainVerCode;
	}

	public void setMainVerCode(int mainVerCode) {
		this.mainVerCode = mainVerCode;
	}

	public String getMainSignCode() {
		return mainSignCode;
	}

	public void setMainSignCode(String mainSignCode) {
		this.mainSignCode = mainSignCode;
	}

	public String getMainVerName() {
		return mainVerName;
	}

	public void setMainVerName(String mainVerName) {
		this.mainVerName = mainVerName;
	}

	public int getMainPackSize() {
		return mainPackSize;
	}

	public void setMainPackSize(int mainPackSize) {
		this.mainPackSize = mainPackSize;
	}

	public int getJumpLinkId() {
		return jumpLinkId;
	}

	public void setJumpLinkId(int jumpLinkId) {
		this.jumpLinkId = jumpLinkId;
	}

	public String getJumpLinkUrl() {
		return jumpLinkUrl;
	}

	public void setJumpLinkUrl(String jumpLinkUrl) {
		this.jumpLinkUrl = jumpLinkUrl;
	}

	public int getJumpGroupId() {
		return jumpGroupId;
	}

	public void setJumpGroupId(int jumpGroupId) {
		this.jumpGroupId = jumpGroupId;
	}

	public int getJumpGroupType() {
		return jumpGroupType;
	}

	public void setJumpGroupType(int jumpGroupType) {
		this.jumpGroupType = jumpGroupType;
	}

	public int getJumpOrderType() {
		return jumpOrderType;
	}

	public void setJumpOrderType(int jumpOrderType) {
		this.jumpOrderType = jumpOrderType;
	}

	private void setGroupElemInfo() {
		try {
			this.setGroupId(Integer.parseInt(mUri.getQueryParameter("groupId")));
			this.setPosId(Integer.parseInt(mUri.getQueryParameter("posId")));
			this.setOrderNo(Integer.parseInt(mUri.getQueryParameter("orderNo")));
			this.setElemType(Integer.parseInt(mUri
					.getQueryParameter("elemType")));
			this.setShowName(mUri.getQueryParameter("showName"));
			this.setRecommWord(mUri.getQueryParameter("recommWord"));
			this.setRecommLevel(Integer.parseInt(mUri
					.getQueryParameter("recommLevel")));
			this.setRecommFlag(Integer.parseInt(mUri
					.getQueryParameter("recommFlag")));
			this.setIconUrl(mUri.getQueryParameter("iconUrl"));
			this.setThumbPicUrl(mUri.getQueryParameter("thumbPicUrl"));
			this.setAdsPicUrl(mUri.getQueryParameter("adsPicUrl"));
			this.setPublishTime(mUri.getQueryParameter("publishTime"));
			this.setShowType(mUri.getQueryParameter("showType"));
			// ---------------------------------------- App
			this.setAppId(Integer.parseInt(mUri.getQueryParameter("appId")));
			this.setPackName(mUri.getQueryParameter("packName"));
			this.setMainPackId(Integer.parseInt(mUri
					.getQueryParameter("mainPackId")));
			this.setMainVerCode(Integer.parseInt(mUri
					.getQueryParameter("mainVerCode")));
			this.setMainSignCode(mUri.getQueryParameter("mainSignCode"));
			this.setMainVerName(mUri.getQueryParameter("mainVerName"));
			this.setMainPackSize(Integer.parseInt(mUri
					.getQueryParameter("mainPackSize")));
			// ---------------------------------------- Link
			this.setJumpLinkId(Integer.parseInt(mUri
					.getQueryParameter("jumpLinkId")));
			this.setJumpLinkUrl(mUri.getQueryParameter("jumpLinkId"));
			// ---------------------------------------- 跳转至分类、网游、单机或专题
			this.setJumpGroupId(Integer.parseInt(mUri
					.getQueryParameter("jumpGroupId")));
			this.setJumpGroupType(Integer.parseInt(mUri
					.getQueryParameter("jumpGroupType")));
			this.setJumpOrderType(Integer.parseInt(mUri
					.getQueryParameter("jumpOrderType")));
		} catch (NumberFormatException e) {
			LogUtils.e("setGroupElemInfo,error:" + e.getMessage());
			return;
		} catch (Exception e) {
			LogUtils.e("setGroupElemInfo,error:" + e.getMessage());
			return;
		}

	}
}
