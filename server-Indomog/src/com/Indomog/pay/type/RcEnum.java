package com.Indomog.pay.type;

public enum RcEnum {
	
	Inquiry("4103"),Redeem("4206"),Refund("4299"),Verify("4109"),Purchase("4201");
	
	private String RC;

	private RcEnum(String rC) {
		RC = rC;
	}

	public String getRC() {
		return RC;
	}

	public void setRC(String rC) {
		RC = rC;
	}
}
