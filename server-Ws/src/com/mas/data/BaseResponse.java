package com.mas.data;


/**
 * hhk
 * @version 1.0.0
 */
public class BaseResponse{
	
	private Integer trxrc;
	
	private State state;

	public BaseResponse() {
		// TODO Auto-generated constructor stub
		state=new State();
	}
	public Integer getTrxrc() {
		return trxrc;
	}

	public void setTrxrc(Integer trxrc) {
		this.trxrc = trxrc;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	
}

