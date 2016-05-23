
package com.hykj.gamecenter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.SystemClock;

public class DateUtil {

    public static long TEN_MINUTE = 1000 * 60 * 10;

    /**
     * @param 服务器时间
     * @return 转化后的Date值
     */
    public static Date stringToDate(String dateStr) {
        // String dateStr = "20130609105223555";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.CHINA);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            /*
             * Calendar calendar = Calendar.getInstance();
             * calendar.setTime(date); android.util.Log.e("MAD", "" + calendar);
             * int year = calendar.get(Calendar.YEAR); int month =
             * calendar.get(Calendar.MONTH) + 1; int day =
             * calendar.get(Calendar.DAY_OF_MONTH); int hour =
             * calendar.get(Calendar.HOUR_OF_DAY); int minute =
             * calendar.get(Calendar.MINUTE); int seconds =
             * calendar.get(Calendar.SECOND); int mseconds =
             * calendar.get(Calendar.MILLISECOND); android.util.Log.e("MAD", ""
             * + "" + year + "-" + month + "-" + day + "," + hour + ":" + minute
             * + "." + seconds + "." + mseconds);
             */
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static long stringToLong(String dateStr) {

        Date date = stringToDate(dateStr);
        return date.getTime();
    }

    public static String longToString(long milliseconds) {

        Date date = new Date();
        date.setTime(milliseconds);

        return dateToServerString(date);
    }

    /**
     * @param Date值
     * @return 用于终端显示的时间String
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        return sdf.format(date);
    }

    public static String dateToServerString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * @param 服务器时间
     * @return 用于终端显示的时间String
     */
    public static String serverDateToApp(String dateStr) {
        return dateToString(stringToDate(dateStr));
    }

    public static String getCurrentTime() {
        long time = System.currentTimeMillis();
        return longToString(time);
    }

    public static long getTimeNow() {
        return SystemClock.elapsedRealtime();
    }

    public static String longToInfoString(long milliseconds) {

        Date date = new Date();
        date.setTime(milliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.CHINA);
        return sdf.format(date);
    }

    public static String remainTimeByTimeFormat(long seconds) {
        StringBuffer str = new StringBuffer();

        long hours = seconds / 3600;
        long minute = seconds % 3600 / 60;
        long second = seconds % 60;

        str.append(String.format("%02d:%02d:%02d", hours, minute, second));
        return str.toString();
    }

    public static String remainTime(long seconds) {
        StringBuffer str = new StringBuffer();
        boolean bShow = false;
        long day = seconds / (24 * 3600);
        if (day > 0) {
            str.append(String.format("%2d d", day));
            bShow = true;
        }
        long hour = seconds % (24 * 3600) / 3600;
        if (hour > 0 || bShow) {
            str.append(String.format("%2d h", hour));
            bShow = true;
        }
        long minute = seconds % 3600 / 60;
        if (minute > 0 || bShow) {
            str.append(String.format("%2d m", minute));
            bShow = true;
        }
        long second = seconds % 60;
        str.append(String.format("%2d s", second));
        return str.toString();
    }

    // // 返回相对时间
    // // 2、该账号上次登录距离当前未满5分钟，则显示为，“刚刚登陆过”；
    // // 3、该账号上次登录距离当前超过5分钟，未满60分钟，则显示为“N分钟前登录过”
    // // 4、该账号上次登录距离当前超过1小时，则显示为“N小时前登录过”
    // public static String getRelativeTime(Resources res, String serverStr) {
    // String ret = "";
    //
    // Calendar now = Calendar.getInstance();
    //
    // Date date = stringToDate(serverStr);
    // Calendar server = Calendar.getInstance();
    // server.setTime(date);
    //
    // if (now.getTimeInMillis() < server.getTimeInMillis()) {
    // ret = res.getString(R.string.sdk_approle_lastlogin_none);
    // } else {
    // int timeInMin = (int) ((now.getTimeInMillis() - server.getTimeInMillis())
    // / (60 * 1000));
    // if (timeInMin < 5) {
    // ret = res.getString(R.string.sdk_approle_lastlogin_soon);
    // } else if (timeInMin < 60) {
    // ret = res.getString(R.string.sdk_approle_lastlogin_minutes, timeInMin);
    // } else {
    // int timeInHour = timeInMin / 60;
    // ret = res.getString(R.string.sdk_approle_lastlogin_hours, timeInHour);
    // }
    // }
    // return ret;
    // }

    // 20130831112020 to 2013-08-31
    public static String strToNeedStr(String dateString) {

        StringBuffer sbf = new StringBuffer();
        sbf.append(dateString.substring(0, 4)).append("-");
        sbf.append(dateString.substring(4, 6)).append("-");
        sbf.append(dateString.substring(6, 8));
        return sbf.toString();

    }

    public static String changeDateFormat(String dateStr) {
        // String dateStr = "20130609105223555";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.CHINA);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            /*
             * Calendar calendar = Calendar.getInstance();
             * calendar.setTime(date); android.util.Log.e("MAD", "" + calendar);
             * int year = calendar.get(Calendar.YEAR); int month =
             * calendar.get(Calendar.MONTH) + 1; int day =
             * calendar.get(Calendar.DAY_OF_MONTH); int hour =
             * calendar.get(Calendar.HOUR_OF_DAY); int minute =
             * calendar.get(Calendar.MINUTE); int seconds =
             * calendar.get(Calendar.SECOND); int mseconds =
             * calendar.get(Calendar.MILLISECOND); android.util.Log.e("MAD", ""
             * + "" + year + "-" + month + "-" + day + "," + hour + ":" + minute
             * + "." + seconds + "." + mseconds);
             */
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
                Locale.CHINA);

        return sdf2.format(date);
    }
}
