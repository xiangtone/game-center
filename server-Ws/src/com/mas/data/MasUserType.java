package com.mas.data;

public enum MasUserType 
{
	NEW(2,"新用户")	,OLD(1, "老用户");
	private Integer id;

	private String name;

	private MasUserType(Integer id, String name)
	{
		this.id = id;
		this.name = name;
	}

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
}
