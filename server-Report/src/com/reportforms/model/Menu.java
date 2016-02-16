package com.reportforms.model;

import java.util.ArrayList;
import java.util.List;

public class Menu extends BaseDomain implements Comparable<Menu>{
    /**
	 * 
	 */
	private static final long serialVersionUID = -457812255937946087L;

	private Integer id;

    private MenuType type;
    
    private String code;

    private String name;

    private String icon;

    private Integer seq;

    private String uri;
    
    private Integer parentId;
    
    private List<Menu> childMenu = new ArrayList<Menu>();
    
    private boolean checked = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MenuType getType() {
		return type;
	}

	public void setType(MenuType type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<Menu> getChildMenu() {
		return childMenu;
	}

	public void setChildMenu(List<Menu> childMenu) {
		this.childMenu = childMenu;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public int compareTo(Menu o) {
		// TODO Auto-generated method stub
		if(null != this.seq && null != o.seq){
			if(this.seq < o.seq){
				return -1;
			}else if(this.seq > o.seq){
				return 1;
			}else{
				return 0;
			}
		}
		return 0;
	}
   
}