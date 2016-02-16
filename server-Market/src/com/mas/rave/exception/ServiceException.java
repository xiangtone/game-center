package com.mas.rave.exception;

public class ServiceException extends RuntimeException{

	public int retCode = 2000;
	
	private String message;
	
	public ServiceException(String message){
		this.message = message;
	}
	
	public ServiceException(int retCode,String message){
		this.retCode = retCode;
		this.message = message;
	}
	
	public ServiceException(int retCode,Throwable cause){
		super(cause);
		this.retCode = retCode;
	}
	
	public ServiceException(String message,Throwable cause){
		super(message,cause);
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
