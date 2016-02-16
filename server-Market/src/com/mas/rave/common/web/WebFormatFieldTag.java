package com.mas.rave.common.web;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
/**
 * 设置web统一规范
 * 比如设置内容显示长度
 * @author jieding
 *
 */
public class WebFormatFieldTag  extends SimpleTagSupport {
	
	private String type;
	
	private String fieldValue;

	private int len;
	
	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void doTag() throws JspException, IOException {
		StringBuilder sb = new StringBuilder("");
		if(type.equals("1")) { //超出规定长度以省略号代替
			if(null != fieldValue && !fieldValue.trim().equals("")) {
//				fieldValue = fieldValue.replace("<br>", "\n");
//				fieldValue = fieldValue.replace("&nbsp;", " ");
//				fieldValue = fieldValue.replace("&lt;", "<");
//				fieldValue = fieldValue.replace("&gt;", ">");
//				fieldValue = fieldValue.replace("&amp;", "&");
				fieldValue=fieldValue
						.replaceAll("&amp;", "&")
						.replaceAll("&lt;", "<")
						.replaceAll("&gt;", ">")
						.replaceAll("&apos;", "\'")
						.replaceAll("&#039;", "\'")
						.replaceAll("&#034;", "\"")
						.replaceAll("&quot;", "\"")
						.replaceAll("&nbsp;", " ")
						.replaceAll("&copy;", "@");
				
				
				if(fieldValue.length() > len) {
					String subString  = fieldValue.substring(0,len);
					subString=subString
							.replaceAll("&","&amp;")
							.replaceAll("<","&lt;")
							.replaceAll(">","&gt;")
							.replaceAll("\'","&apos;")
							.replaceAll("\"","&quot;")
							.replaceAll(" ","&nbsp;")
							.replaceAll("@","&copy;")
							.replaceAll("\n","<br>");
					sb.append(subString+"...");
				}else{
					fieldValue=fieldValue
							.replaceAll("&","&amp;")
							.replaceAll("<","&lt;")
							.replaceAll(">","&gt;")
							.replaceAll("\'","&apos;")
							.replaceAll("\"","&quot;")
							.replaceAll(" ","&nbsp;")
							.replaceAll("@","&copy;")
							.replaceAll("\n","<br>");
					sb.append(fieldValue);
				}
			}
		}else if(type.equals("2")){		
			int value = (int) Math.floor(Float.parseFloat(fieldValue));
			DecimalFormat df=new DecimalFormat("00");    
			sb.append(df.format(value));
		}
		this.getJspContext().getOut().write(sb.toString());
	}
}
