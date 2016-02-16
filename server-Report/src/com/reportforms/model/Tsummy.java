package com.reportforms.model;

import java.sql.Timestamp;

public class Tsummy extends BaseDomain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8513802884158260101L;

	private Integer id;
	
	private Integer type;
	
	private String name;
	
	private float money;
	
	private Integer count;
	
	private Timestamp updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}
