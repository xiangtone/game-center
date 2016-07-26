package com.hykj.gamecenter.net;

import android.content.SharedPreferences;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.statistic.StatisticManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

/**
 * Created by Administrator on 2016/6/20.
 */
public class WifiHttpUtils {
    public static final String APPKEY = "55e7c490641f3cbf46734361b5c5b980";
    public static final String URL_ICODE = "http://mem.wifi8.com/api2/wifiapp/getregcode";
    public static final String URL_UID = "http://mem.wifi8.com/api2/wifiapp/reguser";
    public static final String URL_UTIME = "http://mem.wifi8.com/api2/wifiapp/getutime";
    public static final String URL_SEDDID = "http://mem.wifi8.com/api2/wifiapp/loginuser";
    public static final String URL_WIFI_OPEN = "http://mem.wifi8.com/api2/wifiapp/opennetpd";
    public static final String URL_WIFI_FRESH = "http://mem.wifi8.com/api2/wifiapp/freshuser";

    public static final String SSID_HEAD = /*"花生地铁WiFi_测试_szoffice"*/"花生游戏WiFi";

    public static String SESSID = "";

    public hdata getmHdata() {
        return mHdata;
    }

    private hdata mHdata;
    private JSONObject mDdata;

    public class hdata {
        int ver = 3;
        int aid = 1;
        String aver = "1.0";
        int ostype = 1;
        String dcode = "";
        String seqno = "";
        String sessid = "";
        int resv = 0;

        public hdata(){
            if (!SESSID.equals("")) {
                this.sessid = SESSID;
            }else {
                String sessid = App.getSharedPreference().getString(StatisticManager.KEY_WIFI_SESSID, "");
                this.sessid = sessid;
            }
        }

        public hdata setVer(int ver) {
            this.ver = ver;
            return this;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public void setAver(String aver) {
            this.aver = aver;
        }

        public void setOstype(int ostype) {
            this.ostype = ostype;
        }

        public void setDcode(String dcode) {
            this.dcode = dcode;
        }

        public void setSeqno(String seqno) {
            this.seqno = seqno;
        }

        public void setSessid(String sessid) {
            this.sessid = sessid;
        }

        public void setResv(int resv) {
            this.resv = resv;
        }

        public JSONObject getJsonObject() {
            JSONObject hdata = new JSONObject();
            try {
                hdata.put("ver", ver);
                hdata.put("aid", aid);
                hdata.put("aver", aver);
                hdata.put("ostype", ostype);
                hdata.put("dcode", dcode);
                hdata.put("seqno", seqno);
                hdata.put("sessid", sessid);
                hdata.put("resv", resv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return hdata;
        }
    }

    /**
     * 创建验证码请求参数2.1
     * @param phoneno
     * @param ssid
     * @return
     */
    public static JSONObject createIcodeData(String phoneno, String ssid){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneno", phoneno);
            jsonObject.put("ssid", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * 创建检验手机号请求参数2.2
     * @param phoneno
     * @param regcode
     * @param equid
     * @return
     */
    public static JSONObject createUuid(String phoneno, String regcode, String equid){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneno", phoneno);
            jsonObject.put("regcode", regcode);
            jsonObject.put("equid", equid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * 创建用户登录参数2.4
     * @return
     */
    public static JSONObject createSessid(int utime, String equid){
        JSONObject jsonObject = new JSONObject();
        SharedPreferences sharedPreference = App.getSharedPreference();
        int uuid = sharedPreference.getInt(StatisticManager.KEY_WIFI_UUID, 0);
        String ucode = sharedPreference.getString(StatisticManager.KEY_WIFI_UCODE, "");
//        int utime = sharedPreference.getInt(StatisticManager.KEY_WIFI_UTIME, 0);

        try {
            jsonObject.put("uuid", uuid);
            jsonObject.put("upwd", MD5.md5(ucode+ utime));
            jsonObject.put("utime", utime);
            jsonObject.put("equid", equid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * 创建申请开网参数2.12
     * @return
     */
    public static JSONObject creteWifiOpen(String mac) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mac", mac);
            jsonObject.put("flag", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public WifiHttpUtils(JSONObject ddata) {
        mDdata = ddata;
        mHdata = new hdata();
    }

    public String getParmarData() {
        JSONObject data = new JSONObject();
        try {
            data.put("hdata", mHdata.getJsonObject());
            data.put("ddata", mDdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
    public String getParmarSign() {
        return MD5.md5(getParmarData()+ APPKEY);
    }


}
