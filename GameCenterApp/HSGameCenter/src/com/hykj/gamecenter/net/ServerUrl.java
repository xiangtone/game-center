
package com.hykj.gamecenter.net;

public final class ServerUrl {

    private static final int DEBUG_SERVER = 1001;
    private static final int PUBLIC_SERVER = 1002;

    private static final String DEBUG_URL_ROOT_INNER = "http://192.168.1.12/";

//    private static final String DEBUG_URL_ROOT = /*"http://192.168.1.61/"; */"http://niuwan.vicp.cc:8061/";
    private static final String DEBUG_URL_ROOT = /*"http://192.168.1.61/"; */"http://115.159.125.75/";



//    private static final String DEBUG_URL_ROOT2 = /* "http://192.168.1.51:8090/appstore2_api"; */"http://niuwan.vicp.cc:8061/appstore2_api";
    private static final String DEBUG_URL_ROOT2 = /*"http://115.159.125.75/appstore_api"*/"http://appstore.api.huashenggame.com/";

    private static final String DEBUG_URL_ACCOUNT_PAY = "http://niuwan.vicp.cc:8061/pay_api/api";
    private static final String PUBLIC_URL_ACCOUNT_PAY = "http://pay.api.niuwan.cc/api";

    private static final String DEBUG_URL_ACCOUNT_UAC = "http://niuwan.vicp.cc:8061/uac_api/api"/*"http://192.168.1.51:8090/uac_api/api"*/;
    private static final String PUBLIC_URL_ACCOUNT_UAC = "http://uac.api.niuwan.cc/api";

    private static final String PUBLIC_UAC_URL = "http://uac.api.cs.cc";

    private static final String URL_APPS2 = "appstore_api";

    private static final String URL_APPS = "apps";

    private static final String URL_UPDATE = "updatesys_api/api";

    private static final String URL_REPORT = "stat_api";

    private static final String URL_PAYUA = "payua";

    // 帐号中心，测试服务器
    private static final String URL_UAC = "account";

    private static final String PUBLIC_UPDATE_URL = "http://updatesys.api.huashenggame.com/api";

//    private static final String PUBLIC_APPS_URL = "http://appstore2.api.niuwan.cc/";
    private static final String PUBLIC_APPS_URL = "http://appstore.api.huashenggame.com/";

    private static final String PUBLIC_REPORT_URL = "http://stat.api.huashenggame.com/";//"http://niuwan.vicp.cc:8061/"

    /**
     * 帐号支付中心URL
     */
    private static final String PUBLIC_PAYUA_URL = "http://payua.api.cs.cc";

    //暂时用测试服务器
    private static int CONNECT_TO = PUBLIC_SERVER/*DEBUG_SERVER*/;

    public static String getServerUrlApp() {
        if (CONNECT_TO == PUBLIC_SERVER) {
            return PUBLIC_APPS_URL;
        } else {
            return DEBUG_URL_ROOT2;
        }
    }

    public static String getAccountUacUrlApp() {
        if (CONNECT_TO == PUBLIC_SERVER) {
            return PUBLIC_URL_ACCOUNT_UAC;
        } else {
            return DEBUG_URL_ACCOUNT_UAC;
        }
    }

    public static String getAccountPayUrlApp() {
        if (CONNECT_TO == PUBLIC_SERVER) {
            return PUBLIC_URL_ACCOUNT_PAY;
        } else {
            return DEBUG_URL_ACCOUNT_PAY;
        }
    }

    // //测试
    // public static String getServerUrlApp2()
    // {
    // if( CONNECT_TO == PUBLIC_SERVER )
    // {
    // return PUBLIC_APPS_URL;
    // }
    // else
    // {
    // return DEBUG_URL_ROOT2 + URL_APPS2;
    // }
    // }

    public static String getServerUrlUpdate() {
        if (CONNECT_TO == PUBLIC_SERVER) {
            return PUBLIC_UPDATE_URL;
        } else {
            return DEBUG_URL_ROOT + URL_UPDATE;
        }
    }

    public static String getServerUrlReport() {
        if (CONNECT_TO == PUBLIC_SERVER) {
            return PUBLIC_REPORT_URL;
        } else {
            return DEBUG_URL_ROOT + URL_REPORT;
        }
    }

    /**
     * 帐号中心相关
     * 
     * @return 返回帐号中心服务器URL
     */
    public static String getServerUrlUAC() {
        if (CONNECT_TO == DEBUG_SERVER) {
            return PUBLIC_UAC_URL;
        } else {
            return DEBUG_URL_ROOT + URL_UAC;
        }
    }

    /**
     * 帐号中心相关，账户金额
     * 
     * @return 返回支付中心服务器URL
     */
    public static String getServerUrlPayUA() {
        if (CONNECT_TO == DEBUG_SERVER) {
            return PUBLIC_PAYUA_URL;
        } else {
            return DEBUG_URL_ROOT + URL_PAYUA;
        }
    }

}
