package com.mas.rave.util.email;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import com.mas.rave.util.Constant;

/**
 * 发送邮件的工具类
 * 
 */
public class SendEmail {

	private static Session session = null;

	private static String AUTH = "AUTH";

	private void initSession(String authType, String fromName, String fromPass,
			String smtpName, int port) {
		if (AUTH.equals(authType)) {
			session = this.getAuthSession(fromName, fromPass, smtpName, port);
		} else {
			session = this.getNoAuthSession(smtpName, port);
		}
	}

	/**
	 * 获取一个共享会话
	 * 
	 * @param 设置认证服务器ipOr域名
	 *            /端口
	 */
	private Session getNoAuthSession(String smtpName, int port) {
		Session session = null;
		try {
			Properties prop = new Properties();
			prop.put("mail.transport.protocol", "smtp");
			prop.put("mail.smtp.host", smtpName);
			prop.put("mail.smtp.port", port);
			prop.put("mail.smtp.auth", "false");
			session = Session.getDefaultInstance(prop, null);// share session
		} catch (Exception exce) {
			exce.printStackTrace();
		}
		return session;
	}

	/**
	 * 获取一个非共享会话，与特定的fromName/fromPass绑定
	 * 
	 * @param 设置认证Session的用户名
	 *            /密码/服务器ipOr域名/端口
	 */
	private Session getAuthSession(String fromName, String fromPass,
			String smtpName, int port) {
		Session session = null;
		try {
			Properties prop = new Properties();
			if (smtpName.indexOf("smtp.gmail.com") >= 0) {
				prop.setProperty("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				prop.setProperty("mail.smtp.socketFactory.fallback", "false");
				prop.setProperty("mail.smtp.port", "465");
				prop.setProperty("mail.smtp.socketFactory.port", "465");
				prop.put("mail.smtp.host", smtpName);
				prop.put("mail.smtp.auth", "true");
			} else {
				prop.put("mail.transport.protocol", "smtp");
				prop.put("mail.smtp.host", smtpName);
				prop.put("mail.smtp.port", port);
				prop.put("mail.smtp.auth", "true");
			}
			Authenticator auth = new EmailAuthentication(fromName, fromPass);
			session = Session.getInstance(prop, auth); // unshare session
		} catch (Exception exce) {
			exce.printStackTrace();
		}
		return session;
	}

	/**
	 * 构造无附件的邮件
	 * 
	 * @param fromName
	 *            String
	 * @param ccList
	 *            TODO
	 * @param subject
	 *            String
	 * @param toEmails
	 *            String[]
	 * @param session
	 *            Session
	 * @return Message
	 */
	private Message combineMessage(String fromName, String receiveList,
			String ccList, String subject, String content) {
		Message message = null;
		try {
			if (session == null) {
				throw new NullPointerException("会话对象为空,未能发送邮件");
			}
			message = new MimeMessage(session);
			message.setFrom(InternetAddress.parse(fromName)[0]);
			Address[] toAddresses = InternetAddress.parse(receiveList);
			message.setRecipients(Message.RecipientType.TO, toAddresses); // 直接发送
			if (ccList != null && !"".equals(ccList)) {
				Address[] ccAddresses = InternetAddress.parse(ccList);
				message.setRecipients(Message.RecipientType.CC, ccAddresses);				
			}
			message.setSentDate(new Date());
			message.setSubject(subject);
			// 一个multipart代表代表所有附件的容器可以容纳多个bodypart
			Multipart multiport = new MimeMultipart();
			// 添加文本信息
			BodyPart mbp = new MimeBodyPart();
			// mbp.setContent(content, "text/html;charset=gb2312"); // 通过设置编码，
			mbp.setContent(content, "text/html;charset=utf-8"); // 通过设置编码，
			// 添加多种不同的内容
			// 。
			multiport.addBodyPart(mbp);
			message.setContent(multiport);
		} catch (Exception exce) {
			exce.printStackTrace();
		}
		return message;
	}

	/**
	 * 发送邮件
	 * 
	 * @param session
	 *            Session
	 * @param message
	 *            Message
	 * @param fromName
	 *            String
	 * @param fromPass
	 *            String
	 * @return boolean
	 */
	private boolean transportEmail(Message message, String fromName,
			String fromPass) {
		boolean result = false;
		Transport transport = null;
		try {
			transport = session.getTransport("smtp");
			transport.connect(session.getProperty("mail.smtp.host"),
					filterFromName(fromName), fromPass);
			transport.sendMessage(message, message.getAllRecipients());
			result = true;
		} catch (Exception exce) {
			exce.printStackTrace();
		} finally {
			try {
				if (transport != null) {
					transport.close();
				}
			} catch (Exception exce) {
			}
		}
		return result;
	}

	/**
	 * 过滤发件的邮件名
	 * 
	 * @param fromName
	 * @return
	 */
	private String filterFromName(String fromName) {
		int beg = -1, end = -1;
		if (fromName != null && (beg = fromName.indexOf("<")) != -1
				&& (end = fromName.indexOf(">")) != -1) {
			return fromName.substring(beg + 1, end);
		} else {
			return fromName;
		}
	}

	/**
	 * 设置多个收件人，但邮件收件人之间用,逗号隔开,可以暗送、抄送等方式发送邮件
	 * 
	 * @param message
	 *            Message
	 * @param toCCEmails
	 *            String[]
	 */
	private void setReceiver(Message message, String receiveList,
			RecipientType ss) {
		try {
			Address[] toAddresses = InternetAddress.parse(receiveList);
			message.setRecipients(ss, toAddresses);
		} catch (Exception exce) {
			exce.printStackTrace();
		}
	}

	/**
	 * 添加附件
	 * 
	 * @param message
	 *            Message
	 */
	private void addFile(Message message, String[] file) {
		try {
			// 一个multipart代表代表所有附件的容器可以容纳多个bodypart
			Multipart multiport = (Multipart) message.getContent();

			for (int i = 0; i < file.length; i++) {
				// 一个bodypart就是一个附件文件，添加多个附件文件只需添加多个bodypart即可
				BodyPart bodypart = new MimeBodyPart();
				FileDataSource fileds = new FileDataSource(file[i]);
				DataHandler hander = new DataHandler(fileds);
				bodypart.setDataHandler(hander);
				bodypart.setFileName(new String(fileds.getName()
						.getBytes("gbk"), "iso-8859-1"));
				multiport.addBodyPart(bodypart);
			}

			message.setContent(multiport);
		} catch (Exception exce) {
			exce.printStackTrace();
		}
	}

	/**
	 * 发送邮件的默认方法(不带附件) 可以发送多个人，其中receiveList接收人列表的用户名用","逗号隔开
	 */
	public boolean defaultSendMethod(String smtpHost, int port,
			String fromEmail, String fromPass,String sender, String receiveList,
			String subject, String textContent) {
		String conte = textContent.replace("www.test.com", Constant.DEVELOPER_URL);
		return defaultSendMethod(smtpHost, port, fromEmail, fromPass,
				sender, receiveList, null, subject, conte); // ccList=null
	}

	public boolean defaultSendMethod(String smtpHost, int port,
			String fromEmail, String fromPass, String sender,
			String receiveList, String ccList, String subject, String textContent) {
		textContent = textContent.replaceAll("\n\r", "<br/>");
		textContent = textContent.replaceAll("\n", "<br/>");
		// textContent = textContent.replaceAll(" ", "&nbsp;");
		receiveList = receiveList.replaceAll(";", ",").replaceAll("；", ",")
				.replaceAll("，", ",");
		boolean result = false;
		try {
			this.initSession(this.AUTH, fromEmail, fromPass, smtpHost, port);
			Message message = combineMessage(sender, receiveList, ccList,
					subject, textContent);
			message.saveChanges();
			result = transportEmail(message, fromEmail, fromPass);
		} catch (Exception exce) {
			result = false;
			exce.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送邮件的默认方法(带附件) 多封附件向多个人发送 ，其中receiveList接收人列表的用户名用","逗号隔开
	 * @param sender TODO
	 */
	public boolean defaultSendMethod(String smtpHost, int port,
			String fromEmail, String fromPass, String sender,
			String receiveList, String subject, String textContent, String[] fileList) {
		String conte = textContent.replace("www.test.com", Constant.DEVELOPER_URL);
		return defaultSendMethod(smtpHost, port, fromEmail, fromPass,sender,
				receiveList, null, subject, conte, fileList); // ccList=null
	}

	public boolean defaultSendMethod(String smtpHost, int port,
			String fromEmail, String fromPass, String sender,
			String receiveList,String ccList, String subject, String textContent, String[] fileList) {
		textContent = textContent.replaceAll("\n\r", "<br/>");
		textContent = textContent.replaceAll("\n", "<br/>");
		// textContent = textContent.replaceAll(" ", "&nbsp;");
		receiveList = receiveList.replaceAll(";", ",").replaceAll("；", ",")
				.replaceAll("，", ",");
		boolean result = false;
		try {
			this.initSession(this.AUTH, fromEmail, fromPass, smtpHost, port);
			Message message = combineMessage(sender, receiveList, ccList,
					subject, textContent);
			this.addFile(message, fileList);
			message.saveChanges();
			result = transportEmail(message, fromEmail, fromPass);

		} catch (Exception exce) {
			result = false;
			exce.printStackTrace();
		}
		return result;
	}

}
