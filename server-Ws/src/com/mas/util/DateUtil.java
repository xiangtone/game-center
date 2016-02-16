package com.mas.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class DateUtil
{
	// 用来全局控制 上一周，本周，下一周的周数变化
	private static int weeks = 0;

	public static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
	
	public static final String HOUR_START = " 00:00:00";

	public static final String HOUR_END = " 23:59:59";

	public static Date parseDateString(String dateStr, String formatStr)
	{
		if (dateStr == null || dateStr.trim().length() == 0)
		{
			return null;
		}
		DateFormat df = new SimpleDateFormat(formatStr);
		try
		{
			return df.parse(dateStr);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static String formatDate(Date date, String formatStr)
	{
		DateFormat df = new SimpleDateFormat(formatStr);
		try
		{
			return df.format(date);
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * 
	 * 获取前一天的当前时间
	 * 
	 * */
	public static String getSpecifiedDayBefore(String specifiedDay)
	{
		Calendar c = Calendar.getInstance();
		Date date = null;
		try
		{
			date = new SimpleDateFormat(DATE_FORMAT_SECOND).parse(specifiedDay);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat(DATE_FORMAT_SECOND).format(c.getTime());
		return dayBefore;
	}

	public static String getDayOfBefore(String specifiedDay)
	{
		Calendar c = Calendar.getInstance();
		Date date = null;
		try
		{
			date = new SimpleDateFormat(DATE_FORMAT_DAY).parse(specifiedDay);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);
		String dayBefore = new SimpleDateFormat(DATE_FORMAT_DAY).format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获取本月最后一天的时间
	 * */
	public static String getLastDayOfMonth()
	{
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SECOND);

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	private static int getMondayPlus(Date currentTime)
	{
		Calendar cd = Calendar.getInstance();
		cd.setTime(currentTime);
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 0)
		{
			return -6;
		}
		if (dayOfWeek == 1)
		{
			return 0;
		}
		else
		{
			return 1 - dayOfWeek;
		}
	}

	/**
	 * 获取相应周 周一日期
	 * */
	public static String getMondayOFWeek(Date currentTime)
	{
		weeks = 0;
		int mondayPlus = getMondayPlus(currentTime);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(currentTime);
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获取相应周 周日 的日期
	 * */
	public static String getCurrentWeekday(Date currentTime)
	{
		weeks = 0;
		int mondayPlus = getMondayPlus(currentTime);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(currentTime);
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获取 当天所在月的第一天
	 * */
	public static String getFirstDayOfMonth(Date currentTime)
	{
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(currentTime);
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获取 当天所在月的 最后一天
	 * */
	public static String getDefaultDay(Date currentTime)
	{
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(currentTime);
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 获取上周 周一的日期
	 * */
	public static String getPreviousWeekday(Date currentTime)
	{
		weeks--;
		int mondayPlus = getMondayPlus(currentTime);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(currentTime);
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获取上周 周日的日期
	 * */
	public static String getPreviousWeekSunday(Date currentTime)
	{
		weeks = 0;
		weeks--;
		int mondayPlus = getMondayPlus(currentTime);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(currentTime);
		currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获取上个月的第一天
	 * */
	public static String getPreviousMonthFirst(Date currentTime)
	{
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SECOND);
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(currentTime);
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/***
	 * 
	 * 获得上月最后一天的日期
	 * 
	 * */
	public static String getPreviousMonthEnd(Date currentTime)
	{
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SECOND);

		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(currentTime);
		lastDate.add(Calendar.MONTH, -1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}
	public static Map<String, Integer> getDayDetailInfo(Date day)
	{
		Map<String, Integer> map = new HashMap<String, Integer>();
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(day);
		int year = lastDate.get(Calendar.YEAR);
		int month = lastDate.get(Calendar.MONTH) + 1;
		int date = lastDate.get(Calendar.DATE);
		int week = lastDate.get(Calendar.WEEK_OF_MONTH);
		map.put("year", year);
		map.put("month", month);
		map.put("week", week);
		map.put("day", date);
		return map;
	}
	/**
	 * 获取当天、昨天 起始和结束时间
	 * 
	 * 当天所在周 周一 和 周日日期
	 * 
	 * 当天所在月 该月第一天和最后一天的日期
	 * */
	public static Map<String, Date> getDateInfo(Date currentTime)
	{
		String curtime = formatDate(currentTime, DATE_FORMAT_DAY);
		String beforeTime = getDayOfBefore(curtime);

		String mondayTime = getMondayOFWeek(currentTime);// 本周所在的周一日期
		String weekdayTime = getCurrentWeekday(currentTime);// 本周周日所在的日期

		String monthFirst = getFirstDayOfMonth(currentTime);// 该月的第一天的日期
		String monthLast = getDefaultDay(currentTime);// 该月最后一天的日期

		String mondayTimeBefore = getPreviousWeekday(currentTime);// 获取上周 周一的日期
		String weekdayTimeBefore = getPreviousWeekSunday(currentTime);// 获取上周 周日的日期

		String monthFirstBefore = getPreviousMonthFirst(currentTime);// 上个月的第一天的日期
		String monthLastBefore = getPreviousMonthEnd(currentTime);// 上个月的最后一天的日期

		Map<String, Date> map = new HashMap<String, Date>();
		map.put("curTimeStart", parseDateString(curtime + HOUR_START, DATE_FORMAT_SECOND));// 当天起始时间
		map.put("curTimeEnd", parseDateString(curtime + HOUR_END, DATE_FORMAT_SECOND));// 当天结束时间

		map.put("befTimeStart", parseDateString(beforeTime + HOUR_START, DATE_FORMAT_SECOND));// 昨天起始时间
		map.put("befTimeEnd", parseDateString(beforeTime + HOUR_END, DATE_FORMAT_SECOND));// 昨天结束时间

		map.put("mondayStart", parseDateString(mondayTime + HOUR_START, DATE_FORMAT_SECOND));// 本周周一开始时间
		map.put("weekdayEnd", parseDateString(weekdayTime + HOUR_END, DATE_FORMAT_SECOND));// 本周周日结束时间

		map.put("monthFirst", parseDateString(monthFirst + HOUR_START, DATE_FORMAT_SECOND));// 本月第一天 开始时间
		map.put("monthLast", parseDateString(monthLast + HOUR_END, DATE_FORMAT_SECOND));// 本月最后一天 结束时间

		map.put("lastMonday", parseDateString(mondayTimeBefore + HOUR_START, DATE_FORMAT_SECOND));// 上周 周一的起始时间
		map.put("lastWeekday", parseDateString(weekdayTimeBefore + HOUR_END, DATE_FORMAT_SECOND));// 上周周末的结束时间

		map.put("lastMonthFirst", parseDateString(monthFirstBefore + HOUR_START, DATE_FORMAT_SECOND));// 上个月第一天开始时间
		map.put("lastMonthLast", parseDateString(monthLastBefore + HOUR_END, DATE_FORMAT_SECOND));// 上个月 最后一天结束时间
		return map;
	}
	public static void main(String args[])
	{
		// Calendar c = Calendar.getInstance();
		// c.setTime(new Date());
		// int day = c.get(Calendar.DATE);
		// c.set(Calendar.DATE, day-1);
		// int d = c.get(Calendar.DATE);
		// System.out.println(d);
		String s = getDayOfBefore("2012-10-26 00:09:00");
		System.out.println(s);
		Date d = parseDateString(s + " 00:00:00", DATE_FORMAT_SECOND);
		System.out.println("==========" + formatDate(d, DATE_FORMAT_SECOND));
	}

}
