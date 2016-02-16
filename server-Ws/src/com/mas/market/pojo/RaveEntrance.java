package com.mas.market.pojo;

public enum RaveEntrance
{
	Homes(1,"Homes"),Apps(2,"Apps"),Games(3,"Games"),Ringtones(4,"Ringtones"),Wallpaper(5,"Wallpaper");
	private Integer id;

	private String name;

	private RaveEntrance(Integer id, String name)
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
