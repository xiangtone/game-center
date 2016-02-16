package com.mas.rave.vo;

import java.util.ArrayList;
import java.util.List;

import com.mas.rave.main.vo.MenuType;


public class MenuTypeVO extends MenuType{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6798669341464368038L;

	private boolean displayable;
	
	private List<MenuVO> menuvos = new ArrayList<MenuVO>();


	public List<MenuVO> getMenuvos() {
		return menuvos;
	}


	public void setMenuvos(List<MenuVO> menuvos) {
		this.menuvos = menuvos;
	}


	public boolean isDisplayable() {
		return displayable;
	}


	public void setDisplayable(boolean displayable) {
		this.displayable = displayable;
	}
	
	
}
