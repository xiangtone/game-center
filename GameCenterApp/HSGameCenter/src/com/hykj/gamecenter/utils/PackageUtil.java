/**
 * 
 */

package com.hykj.gamecenter.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

/**
 * @author passing
 */
public class PackageUtil {

    private static final String TAG = "PackageUtil";

    /**
     * 获取全部应用
     * 
     * @param context
     * @return
     */
    public static List<ResolveInfo> getAllPagckage(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(
                mainIntent, PackageManager.GET_GIDS);
        // 调用系统排序，根据name排序
        Collections.sort(resolveInfoList,
                new ResolveInfo.DisplayNameComparator(pm));

        return resolveInfoList;
    }

    public static boolean existApk(String pkgName, Context context) {
        try {
            context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 获取应用签名
     * 
     * @param context
     * @param pkgName
     * @return
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_SIGNATURES);
            return MD5.hexdigest(/* parseSignature( */pis.signatures[0]
                    .toByteArray()/* ) */);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPkgChnNo(Context context, String pkgName) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        String chnNo = "";
        try {
            info = manager.getPackageInfo(pkgName, 0);
            String version = info.versionName;
             String [] splits = version.split("\\.");
            chnNo = splits[splits.length - 1];
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            chnNo = "";
        }
        Logger.i(TAG, "getPkgChnNo pkgName "+ pkgName + " version "+ chnNo, "oddshou");
        return chnNo;
    }

    public static String parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();

            return pubKey;
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }
        return "";
    }

}
