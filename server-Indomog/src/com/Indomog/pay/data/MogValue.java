package com.Indomog.pay.data;

public enum MogValue{
	
	v20(20000),v50(50000),v100(100000),v200(200000),v500(500000),v1000(1000000);
	
	private Integer value;

	private MogValue(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
