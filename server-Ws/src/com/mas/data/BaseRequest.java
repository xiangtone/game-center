package com.mas.data;

public class BaseRequest{

	private Integer rc;
	
	private String sign;
	
	private String sessionId;

	public Integer getRc() {
		return rc;
	}

	public void setRc(Integer rc) {
		this.rc = rc;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "BaseRequest [rc=" + rc + ", sign=" + sign + ", sessionId="
				+ sessionId + "]";
	}
}