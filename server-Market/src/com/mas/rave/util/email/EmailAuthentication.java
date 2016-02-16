package com.mas.rave.util.email;

import javax.mail.PasswordAuthentication;

/*
 * function:邮件服务器身份验证时使用的用户凭证
 */
public class EmailAuthentication extends javax.mail.Authenticator{
   private String username;
   private String password;
	public EmailAuthentication(String username,String passord){
		this.username = username;
		this.password = passord;
	}
	@Override	
	protected PasswordAuthentication getPasswordAuthentication ( ) {		  
		return new PasswordAuthentication(username,password);
	}

}
