package com.reportforms.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	//当天
	 public static String getCurrentDate(){
	        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	    }
	 //本月第一天
	 public static String getTheMonthFirstDay(){
		 Calendar cal = Calendar.getInstance();  
		 cal.set(Calendar.DATE,0); 
		 Date  d = cal.getTime();
		 return new SimpleDateFormat("yyyy-MM-dd").format(d);
	 }
	 public static Date getTheMonthFirstDay_Date(){
		 Calendar cal = Calendar.getInstance();  
		 cal.set(Calendar.DATE,1); 
		 Date  d = cal.getTime();
		 return d;
	 }
	 public static Date getTomorrow(String endDate) throws ParseException{
			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
			cal.add(Calendar.DATE,+1);
			return cal.getTime();
	 }
	 
	 public static String getTimestampToString(Timestamp timestamp){
		 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
	 }
	 
	 public static String getTimestampToString(Date date){
		 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	 }
}
