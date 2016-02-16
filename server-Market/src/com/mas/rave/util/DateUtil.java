package com.mas.rave.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	public static String getTime() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	// 当天
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	// 当天
	public static String getCurrentDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	// 本月第一天
	public static String getTheMonthFirstDay() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 0);
		Date d = cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	public static Date getTheMonthFirstDay_Date() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		Date d = cal.getTime();
		return d;
	}

	public static Date getTomorrow(String endDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
		cal.add(Calendar.DATE, +1);
		return cal.getTime();
	}

	// 将string转换为date
	public static Date StringToDate(String date) {
		try {
			if (StringUtils.isEmpty(date)) {
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Date StringToDate1(String date) {
		try {
			if (StringUtils.isEmpty(date)) {
				return null;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 将string转换为date
	public static String DateToString(Date date) {
		try {
			if (date == null || date.equals("")) {
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String DateToString1(Date date) {
		try {
			if (date == null || date.equals("")) {
				return "";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
