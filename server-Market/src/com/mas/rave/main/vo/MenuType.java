package com.mas.rave.main.vo;

import java.util.ArrayList;
import java.util.List;

import com.mas.rave.common.MyNumberUtils;



public class MenuType extends BaseDomain implements Comparable<MenuType>{
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1608941212402683488L;

	private Integer id;

    private String name;

    private String icon;

    private Integer seq;
    
    private List<Menu> menus = new ArrayList<Menu>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	@Override
	public int compareTo(MenuType o) {
		return MyNumberUtils.compareInteger(o.getId(), this.id);
	}
    
}