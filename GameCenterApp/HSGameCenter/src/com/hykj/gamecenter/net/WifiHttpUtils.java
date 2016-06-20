package com.hykj.gamecenter.net;

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

    private hdata mHdata;
    private JSONObject mDdata;

    class hdata {
        int ver = 3;
        int aid = 1;
        String aver = "1.0";
        int ostype = 1;
        String dcode = "";
        String seqno = "";
        String sessid = "";
        int resv = 0;

        public void setVer(int ver) {
            this.ver = ver;
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
