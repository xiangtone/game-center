package com.mas.util.email;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class SendEmailServiceImpl implements SendEmailService {

	   private JavaMailSender mailSender;
	   private VelocityEngine velocityEngine;

	   public void setMailSender(JavaMailSender mailSender) {
	      this.mailSender = mailSender;
	   }

	   public void setVelocityEngine(VelocityEngine velocityEngine) {
	      this.velocityEngine = velocityEngine;
	   }

	   @Override
	   public String sendEmail(final List<String[]> emailList,final String subject,final String password,final String nickname){
	      MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws MessagingException {
	        	MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true,"UTF-8");  
	        	message.setSubject(subject);
	        	if(emailList.size()==2){
	        		message.setTo(emailList.get(0));
	        		message.setCc(emailList.get(1));
	        	}else if(emailList.size()==1){
	        		message.setTo(emailList.get(0));
	        	} 
	            message.setFrom("gamecenter@bjxiangtone.com");
				Map<String,Object> model = new HashMap<String,Object>();
	            model.put("password", password);
	            model.put("nickname", nickname);
	            String text = VelocityEngineUtils.mergeTemplateIntoString(
	               velocityEngine,"com/mas/util/email/password.vm","UTF-8",model);
	            message.setText(text, true);
	         }
	      };
	      if(emailList.size()>0){
	    	  try{
	    		  this.mailSender.send(preparator);
	    		  return "success";
	    	  }catch(MailException e){
	    		  try{
		    		  this.mailSender.send(preparator);
		    		  return "success";
		    	  }catch(MailException e2){
		    		  e2.printStackTrace();
		    		  return e.getMessage();
		    	  }
	    	  }
	      }
		return "failure";
	   }
}
