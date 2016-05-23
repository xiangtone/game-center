
package com.hykj.gamecenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.utils.Logger;

import java.util.Locale;

public class DeviceInfo
{

    private static final String TAG = "DeviceInfo";
    private static StringBuilder userDiveceInfo; // user device info 在提交包头时使用

    public static void setDeviceInfo(Context context)
    {
        getUserDeviceInfo(context);
    }

    public static String getDeviceInfo()
    {
        //oi open id 
        String openid = App.getSharedPreference().getString(StatisticManager.KEY_OPENID, "");
        StringBuilder buidler = new StringBuilder(userDiveceInfo);
        buidler.append("&oi=" + openid);
        
//        Logger.i(TAG, "getDeviceInfo "+ buidler.toString(), "oddshou");
        return buidler.toString();
    }

    private static void getUserDeviceInfo(Context context)
    {

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Locale locale = context.getResources().getConfiguration().locale;

        StringBuilder sbuilder = new StringBuilder();
        // ei 设备ID（手机IMEI，平板CHIPID，取不到则不传） ------ 现在没chipid可取， 取imei
        String imei = tm.getDeviceId();
        // si 手机卡的IMSI值（取不到则不传）
        String imsi = tm.getSubscriberId();
        sbuilder.append("ei=" + (imei == null ? "" : imei));

        if (!TextUtils.isEmpty(imsi))
        {
            sbuilder.append("&si=" + imsi);
        }
        // ai ANDROID_ID值
        String ai = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(ai))
        {
            sbuilder.append("&ai=" + ai);
        }

        // wm 无线MAC地址（取不到则不传）
        String wm = wifi.getConnectionInfo().getMacAddress();
        if (!TextUtils.isEmpty(wm))
        {
            sbuilder.append("&wm=" + wm);
        }

        // mf 设备厂商（原值传入manufacturer值）
        // bd 设备品牌（原值传入brand值）
        // md 设备型号（原值传入model值）
        String mf = android.os.Build.MANUFACTURER;
        String bd = android.os.Build.BRAND;
        String md = android.os.Build.MODEL;
        int pl = android.os.Build.VERSION.SDK_INT;

        sbuilder.append("&mf=" + mf);
        sbuilder.append("&bd=" + bd);
        sbuilder.append("&md=" + md);
        sbuilder.append("&pl=" + pl);

        // sw 整型值，屏宽分辨率
        // sh 整型值，屏高分辨率
        sbuilder.append("&sw=" + dm.widthPixels);
        sbuilder.append("&sh=" + dm.heightPixels);

        // nt 整型值，网络类型数值（原值传入GetNetworkType值，传换为字符串）
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null)
        {
            sbuilder.append("&nt=" + cm.getActiveNetworkInfo().getType());
        }

        // la 国家与语言码（例如：zh-cn、zh-hk等，客户端组合）
        String la = locale.getLanguage() + "-" + locale.getCountry();
        sbuilder.append("&la=" + la);

        // device信息
        String de = android.os.Build.DEVICE;
        sbuilder.append("&de=" + de);
        
        userDiveceInfo = sbuilder;

        Logger.i("DeviceInfo", "udi:" + userDiveceInfo);
        Logger.i("DeviceInfo", "sdpi:" + dm.densityDpi);
        Logger.i("DeviceInfo", "sdpi:" + dm.density);
    }
}
