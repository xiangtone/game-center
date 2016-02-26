package com.x.publics.model;

import java.io.Serializable;

/**
 * 
 * @ClassName: KeywordBean
 * @Description: 搜索关键字实体类
 
 * @date 2014-7-7 下午2:55:48
 * 
 */
public class KeywordBean implements Serializable {

	private static final long serialVersionUID = 6293568730483036307L;
	private String action;
	private int actionRc;
	private String keyword;
	private String resLogo;
	private String iconUrl;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getActionRc() {
		return actionRc;
	}

	public void setActionRc(int actionRc) {
		this.actionRc = actionRc;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getResLogo() {
		return resLogo;
	}

	public void setResLogo(String resLogo) {
		this.resLogo = resLogo;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

}
