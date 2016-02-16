package com.mas.rave.main.vo;

public class Menu extends BaseDomain implements Comparable{
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
    
    private Menu parent;

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

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return this.id.compareTo(((Menu)o).id);
	}

   
}