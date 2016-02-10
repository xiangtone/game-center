/**   
* @Title: CommonResponse.java
* @Package com.x.http.model
* @Description: TODO 

* @date 2014-1-15 上午10:23:36
* @version V1.0   
*/

package com.x.publics.http.model;

/**
* @ClassName: CommonResponse
* @Description: TODO 

* @date 2014-1-15 上午10:23:36
* 
*/

public class CommonResponse {

	public int trxrc;
	public State state;

	public static class State {
		public int code;
		public String msg;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}

	public int getTrxrc() {
		return trxrc;
	}

	public void setTrxrc(int trxrc) {
		this.trxrc = trxrc;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
