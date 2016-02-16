package com.mas.util.email;

import java.util.List;


public interface SendEmailService {


	String sendEmail(List<String[]> emailList, String subject, String password,
			String nickname);


}
