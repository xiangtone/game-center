package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app对应专辑
 * 
 * @author liwei.sz
 * 
 */
public class AppAlbumColumn {
	private int columnId;
	private AppAlbum appAlbum;// 大类别Id',
	private String name;// 页签名称',
	private String nameCn;// 页签中文名称',
	private String icon;// 页签的小图标（列表显示）',
	private String bigicon;// 专题大图（页签详情时要显示）',
	private String description;// 页签描述',
	private int sort;// 排序（按数字大小从大到小排序）',
	private boolean state;// 状态',
	private int flag;// 标识',
	private Date createTime;// 创建时间',
	private String operator;// 后台操作人',
	private boolean checked;

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public AppAlbum getAppAlbum() {
		return appAlbum;
	}

	public void setAppAlbum(AppAlbum appAlbum) {
		this.appAlbum = appAlbum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
