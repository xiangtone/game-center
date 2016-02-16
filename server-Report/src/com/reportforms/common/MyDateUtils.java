package com.reportforms.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDateUtils {

	
	public static String format(Date date,String pattern){
		if(date == null || pattern == null)
			return null;
		DateFormat df = new SimpleDateFormat(pattern,Locale.ENGLISH);
		return df.format(date);
	}
}
