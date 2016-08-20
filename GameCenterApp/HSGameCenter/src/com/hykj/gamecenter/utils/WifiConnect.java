package com.hykj.gamecenter.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.hykj.gamecenter.net.WifiHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class WifiConnect {
    private static final String TAG = "wifiTest wifiConnect";
    private final Context mContext;
    WifiManager wifiManager;
    private boolean scanResult = false;

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    // 构造函数
    public WifiConnect(Context context, WifiManager wifiManager) {
        this.wifiManager = wifiManager;
        this.mContext = context;
    }

    // 打开wifi功能
    private boolean OpenWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    // 提供一个外部接口，传入要连接的无线网
    public boolean Connect(String SSID, String Password, WifiCipherType Type) {
        if (!this.OpenWifi()) {
            return false;
        }
        // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, Password, Type);
        if (wifiConfig == null) {
            return false;
        }

        boolean able = isAvaiable(SSID);
        Logger.i(TAG, able + "", "oddshou");
        if (!able) {
            return false;
        }

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        int netID = wifiManager.addNetwork(wifiConfig);
        Log.i(TAG, netID + "");
        boolean bRet = wifiManager.enableNetwork(netID, true);
        return bRet;
    }

    // 连接同一类型的wifi

    /**
     * @param SSIDHead
     * @param Type
     * @return -1 未检测到花生wifi， -2 连接失败， 1 重连成功，2 直连成功
     */
    public Integer Connect(String SSIDHead, WifiCipherType Type) {
        if (!this.OpenWifi()) {
            return -1;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            String connectedSsid = wifiInfo.getSSID();
            if (!TextUtils.isEmpty(connectedSsid) && ((connectedSsid.equals(SSIDHead) ||
                    connectedSsid.equals("\"" + SSIDHead + "\"")))) {
                boolean connected = wifiManager.enableNetwork(wifiInfo.getNetworkId(), true);
                if (connected) return 2;
            }
        }

        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mContext.registerReceiver(mReceiver, filter);
        wifiManager.startScan();
        // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
        // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句,同时确定扫描wifi结束。
        while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING || !scanResult) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        scanResult = false;

        List<ScanResult> availableSSID = new ArrayList<ScanResult>();
        //扫描可用wifi
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        if (scanResultList != null) {
            for (ScanResult scanResult : scanResultList) {
                Log.i(TAG, scanResult.SSID);
                if (scanResult.SSID.equals(SSIDHead) ||
                        scanResult.SSID.equals("\"" + WifiHttpUtils.SSID_HEAD + "\"")) {
                    availableSSID.add(scanResult);
                }
            }
        }
        if (availableSSID.size() <= 0) {
            return -1;
        }

        for (ScanResult scanResult : availableSSID) {
            WifiConfiguration wifiConfig = this.CreateWifiInfo(scanResult.SSID, "", Type);
            if (wifiConfig == null) {
                return -2;
            }

            WifiConfiguration tempConfig = this.IsExsits(scanResult.SSID);
            int netID = 0;
            if (tempConfig != null) {
                wifiManager.removeNetwork(tempConfig.networkId);
            }
            netID = wifiManager.addNetwork(wifiConfig);
            Log.i(TAG, netID + "");
            boolean bRet = wifiManager.enableNetwork(netID, true);
            if (!bRet) {
                continue;
            }
            return 1;
        }
        return -2;

    }

    /**
     * 广播接收，监听网络
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // wifi已成功扫描到可用wifi。
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                scanResult = true;
                if (mContext != null) {
                    mContext.unregisterReceiver(mReceiver);
                }
            }
        }
    };
    /*
     * 检查当前 wifi连接的 wifi 是否有效
     */
    private boolean isAvaiable(String SSID) {
        boolean able = false;
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        if (scanResultList != null) {
            for (ScanResult scanResult : scanResultList) {
                Log.i(TAG, scanResult.SSID);
                if (scanResult.SSID.equals(SSID)) {
                    able = true;

                    return able;
                }
            }
        }
        return able;
    }

    // 查看以前是否也配置过这个网络
    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                Log.i(TAG, existingConfig.SSID);
                if (existingConfig.SSID.equals("\"" + SSID + "\"") || existingConfig.SSID.equals(SSID)) {
                    return existingConfig;
                }
            }
        }

        return null;
    }

    private WifiConfiguration CreateWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
//			config.wepKeys[0] = null;

            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);


//			config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_WEP) {
            config.hiddenSSID = true;
            // 下面一行的另一个版本 config.wepKeys[0]= "\""+Password+"\"";
            config.preSharedKey = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            return null;
        }
        return config;
    }

    /**
     * 判断WiFi类型
     *
     * @param str
     * @returnWiFi类型的 string值
     */
    public WifiCipherType checkWifiType(String str) {
        WifiCipherType type = WifiCipherType.WIFICIPHER_WPA;
        if (str.compareToIgnoreCase("wep") == 0) {
            type = WifiCipherType.WIFICIPHER_WEP;
        } else if (str.compareToIgnoreCase("wpa/wpa2") == 0 || str.compareToIgnoreCase("wpa") == 0) {
            type = WifiCipherType.WIFICIPHER_WPA;
        } else {
            type = WifiCipherType.WIFICIPHER_NOPASS;
        }
        return type;
    }
}
