package com.mas.rave.main.vo;

public class Role extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8278320917222911951L;

	private Integer id;

    private String name;

    private String description;

    private Integer seq;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

    
}