package com.hykj.gamecenter.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.hykj.gamecenter.net.APNUtil;

/**
 * Created by win7 on 2016/8/24.
 */
public class DevicesInfo {
    public final String os = "android";
    public final String osv;  //  操作系统版本
    public final String make;    //手机厂商
    public final String model;   //手机型号
    public final int carrier = 0;   //运营商编号
    public final String ua = "";   //用户浏览器
    public final String ip; //用户浏览器
    public final String geo = ""; //设备当前地理位置信息
    public final int connectiontype;    //联网方式
    public final String y_device_ext_mac;   //扩展mac
    public final String y_device_ext_imei;  //设备imei


    public DevicesInfo(Context context) {
        make = android.os.Build.MANUFACTURER != null ? Build.MANUFACTURER : "";
        model = android.os.Build.MODEL != null ? Build.MODEL : "";
        String ip = APNUtil.getCurrentIp();
        this.ip = ip != null ? ip : "";

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String wm = wifi.getConnectionInfo().getMacAddress();
        this.y_device_ext_mac = wm != null ? wm : "";

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        this.y_device_ext_imei = imei != null ? imei : "";

        connectiontype = APNUtil.GetNetworkType(context);
        osv = Build.VERSION.RELEASE != null ? Build.VERSION.RELEASE : "";

    }
}
