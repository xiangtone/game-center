/**   
* @Title: TransferResponse.java
* @Package com.x.business.zerodata.client.http.model
* @Description: TODO 

* @date 2014-3-20 上午11:30:24
* @version V1.0   
*/

package com.x.business.zerodata.client.http.model;

import java.util.List;

/**
* @ClassName: TransferResponse
* @Description: TODO 

* @date 2014-3-20 上午11:30:24
* 
*/

public class TransferResponse {
	
	public State state;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

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

	public List<Transfers> transferList;

	public List<Transfers> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<Transfers> transferList) {
		this.transferList = transferList;
	}

}
