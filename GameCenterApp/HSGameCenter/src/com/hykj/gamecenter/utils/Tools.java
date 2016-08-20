
package com.hykj.gamecenter.utils;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.logic.entry.UriGroupElemInfo;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Apps.UserCommentInfo;
import com.hykj.gamecenter.protocol.Pay.AccRechargeInfo;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;

public class Tools {

    private static final String TAG = "Tools";

    //
    public static DownloadTask createDownloadTask(AppInfo appInfo, int nFromPos) {
        DownloadTask dinfo = new DownloadTask();
        dinfo.appDownloadURL = appInfo.packUrl;
        dinfo.packageName = appInfo.packName;
        dinfo.appName = appInfo.showName;
        dinfo.appId = appInfo.appId;
        dinfo.packId = appInfo.packId;
        dinfo.fileLength = appInfo.packSize;
        dinfo.nFromPos = nFromPos;
        dinfo.progress = 0;
        dinfo.appIconURL = appInfo.iconUrl;
        dinfo.setState(TaskState.PREPARING);
        dinfo.bRealAppDownloadURL = false;
        return dinfo;
    }

    public static AppInfo createAppInfo(GroupElemInfo elemInfo) {
        AppInfo appInfo = new AppInfo();
        appInfo.appId = elemInfo.appId;
        appInfo.verName = elemInfo.mainVerName;
        appInfo.packName = elemInfo.packName;
        appInfo.showName = elemInfo.showName;
        appInfo.packId = elemInfo.mainPackId;
        appInfo.iconUrl = elemInfo.iconUrl;
        appInfo.recommLevel = elemInfo.recommLevel;
        appInfo.recommFlag = elemInfo.recommFlag;
        appInfo.recommWord = elemInfo.recommWord;
        return appInfo;

    }

    /*public static AppInfo createAppInfo(AppsGroupElemInfoParcelable elemInfo) {
        AppInfo appInfo = new AppInfo();
        appInfo.appId = elemInfo.appId;
        appInfo.verName = elemInfo.mainVerName;
        appInfo.packName = elemInfo.packName;
        appInfo.showName = elemInfo.showName;
        appInfo.packId = elemInfo.mainPackId;
        appInfo.iconUrl = elemInfo.iconUrl;
        appInfo.recommLevel = elemInfo.recommLevel;
        appInfo.recommFlag = elemInfo.recommFlag;
        appInfo.recommWord = elemInfo.recommWord;
        return appInfo;

    }*/

    // 云指令需求
    public static AppInfo createAppInfo(UriGroupElemInfo jsonElemInfo) {
        AppInfo appInfo = new AppInfo();

        LogUtils.e("jsonElemInfo.getAppId( ) =" + jsonElemInfo.getAppId());

        appInfo.appId = jsonElemInfo.getAppId();
        appInfo.verName = jsonElemInfo.getMainVerName();
        appInfo.packName = jsonElemInfo.getPackName();
        appInfo.showName = jsonElemInfo.getShowName();
        appInfo.packId = jsonElemInfo.getMainPackId();
        appInfo.iconUrl = jsonElemInfo.getIconUrl();
        appInfo.recommLevel = jsonElemInfo.getRecommLevel();
        appInfo.recommFlag = jsonElemInfo.getRecommFlag();
        appInfo.recommWord = jsonElemInfo.getRecommWord();
        return appInfo;

    }

    public static long getSystemTime() {
        Time time = new Time();
        time.setToNow();
        long t = 0;
        // long t = ( ( ( ( ( time.year * 100 + time.month ) * 100 + time.month
        // ) * 100 + time.monthDay ) * 100 + time.hour ) * 100 + time.minute ) *
        // 100 + time.second;
        t = time.year * 100 + time.month + 1;
        t = t * 100 + time.monthDay;
        t = t * 100 + time.hour;
        t = t * 100 + time.minute;
        t = t * 100 + time.second;
        return t;
    }

    /**
     * 判断当前网络是否是3G网络
     * 
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断是否有SIM卡
     */
    public static boolean isHasSim() {
        TelephonyManager tm = (TelephonyManager) App.getAppContext()
                .getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
        LogUtils.d("SimState: " + tm.getSimState());
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    public static boolean isWifiConnected() {
        ConnectivityManager conMan = (ConnectivityManager) App.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        return wifi.equals(State.CONNECTED);
    }

    /**
     * 获取手机的Density
     */
    public static int getDisplayDensity(Context context) {

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        Logger.i(TAG, "getDisplayDensity " + "metric "+ metric, "oddshou");

        return metric.densityDpi;
    }

    /**
     * 获取手机的屏幕宽 (px)
     */
    public static int getDisplayWidth(Context context) {

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);
        return size.x;
    }

    /**
     * 获取手机的屏幕宽 (px)
     */
    public static int getDisplayHeight(Context context) {

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);
        return size.y;
    }

    public static ArrayList<GroupElemInfo> arrayToList(GroupElemInfo[] infos) {
        ArrayList<GroupElemInfo> elems = new ArrayList<GroupElemInfo>();
        for (int i = 0; i < infos.length; i++) {
            elems.add(infos[i]);
        }
        return elems;
    }

    public static ArrayList<UserCommentInfo> arrayToList(UserCommentInfo[] infos) {
        ArrayList<UserCommentInfo> elems = new ArrayList<UserCommentInfo>();
        for (int i = 0; i < infos.length; i++) {
            elems.add(infos[i]);
        }
        return elems;
    }

    public static ArrayList<AccRechargeInfo> arrayToList(AccRechargeInfo[] infos) {
        ArrayList<AccRechargeInfo> elems = new ArrayList<AccRechargeInfo>();
        for (int i = 0; i < infos.length; i++) {
            elems.add(infos[i]);
        }
        return elems;
    }

    public static String showDownTimes(int times, Context context) {
        String str = "";
        if (times >= 10000) {
            str = String.format(context.getResources().getString(R.string.text_downtimes_wan),
                    (times / 10000) + "");
            //            str = context.getResources().getString(R.string.text_downtimes_wan,
            //                    (times / 10000));
        } else if (times >= 1000 && times < 10000) {
            /*            str = context.getResources().getString(R.string.text_downtimes_thousand,
                                (times / 1000) + "");*/
            str = String.format(context.getResources().getString(R.string.text_downtimes_thousand),
                    (times / 1000) + "");
        } else {
            /*            str = context.getResources().getString(R.string.text_downtimes_other,
                                times + "");*/
            str = String.format(context.getResources().getString(R.string.text_downtimes_other),
                    times + "");
        }
        str = str.replace(" ", "");
        //        Logger.e("GameFragment", "text====" + str);
        return str.trim();
    }
}
