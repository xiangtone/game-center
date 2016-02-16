package com.mas.market.pojo;

public enum StarsEnum{
	
	one(1),two(2),three(3),four(4),five(5);
	
	private Integer value;

	private StarsEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
