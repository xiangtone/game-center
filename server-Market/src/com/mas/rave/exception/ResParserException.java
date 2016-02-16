package com.mas.rave.exception;

public class ResParserException extends RuntimeException{

	public int retCode = 1000;
	
	private String message;
	
	public ResParserException(String message){
		this.message = message;
	}
	
	public ResParserException(int retCode,String message){
		this.retCode = retCode;
		this.message = message;
	}
	
	public ResParserException(int retCode,Throwable cause){
		super(cause);
		this.retCode = retCode;
	}
	
	public ResParserException(String message,Throwable cause){
		super(message,cause);
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
