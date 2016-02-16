package com.mas.data;

public enum PayWay 
{
	Indomog(1, "Indomog");
	private Integer id;

	private String name;

	private PayWay(Integer id, String name)
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
