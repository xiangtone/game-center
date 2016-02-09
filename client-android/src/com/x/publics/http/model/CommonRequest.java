/**   
* @Title: CommonRequest.java
* @Package com.mas.amineappstore.http.model
* @Description: TODO 

* @date 2014-1-15 上午09:21:22
* @version V1.0   
*/

package com.x.publics.http.model;

/**
* @ClassName: CommonRequest
* @Description: TODO 

* @date 2014-1-15 上午09:21:22
* 
*/

public class CommonRequest {

	public int rc;

	public String sign;

	public CommonRequest(int rc, String sign) {
		this.rc = rc;
		this.sign = sign;
	}

	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
