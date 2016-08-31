package com.hykj.gamecenter.adv;

import android.text.TextUtils;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.DevicesInfo;
import com.hykj.gamecenter.utils.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by win7 on 2016/8/24.
 * 广告接入细分
 * 1.请求一个广告位置，得到相应的广告信息，协议提供类型
 * 2.广告反馈：曝光反馈，点击反馈
 * 3.需要上传一些数据
 * 4.请求目前使用xutils处理
 */
public class AdvManager {

    private static final String SERVER_URL = /*"http://x.wifi8.com/api/req"*/"http://114.80.12.57/api/req";
    public static final String TAG_ID = /*"46b7505988327ecfc14218d50bc13bd4"*/"46b7505988327ecfc14218d50bc13bd4";
    public static final String TAG_ID_720 = "220de42c46e5b24f1b5aed9571bf8d65";
    public static final String EXT_TAG_ID = "440300020000";
    public static final int IMP_WIFI_ADV = 1;     //生成wifi连接页广告id
    public static final int IMP_LOGIN_ADV = 2;   //生成login页广告id
    public static final int IMG_SPLASH_ADV = 3;     //闪屏广告
    private static final String TAG = "AdvManager";

    public interface AdvPostListener {
//        void onReqFailed(String errmg);
//        void onNetError(int errCode, String errorMsg);

        void onReqAdvSucceed(JSONObject creative);
    }

    private AdvManager(AdvPostListener listener) {

    }

    public static void doPost(int impId, final AdvPostListener listener) {


        RequestParams params = new RequestParams(SERVER_URL);
        params.setHeader("Content-Type", "application/json");
        try {
            JSONObject reqJsonObject = new JSONObject();
            reqJsonObject.put("imp", getImpObject(impId));
            reqJsonObject.put("app", getAppObject());
            reqJsonObject.put("device", getDevicesObject());
            params.setAsJsonContent(true);
            params.setBodyContent(reqJsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                //json 解析 responseInfo
                JSONTokener responseData = new JSONTokener(s);
                try {
                    JSONObject objData = (JSONObject) responseData.nextValue();
                    int code = objData.getInt("code");
                    if (code == 0) {
                        JSONObject data = objData.getJSONObject("data");
                        if (data != null) {
                            JSONArray creatives = data.getJSONArray("creatives");
                            if (creatives.length() > 0) {
                                listener.onReqAdvSucceed(creatives.getJSONObject(0));
                            }
                        }
                    } else {
                        //错误
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Logger.e(TAG, "onError: ", "oddshou");
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public static JSONArray getImpObject(int impId) throws JSONException {
        //imp
        JSONObject imp = new JSONObject();
        imp.put("id", impId);
        JSONObject banner = new JSONObject();
        int w = 0;
        int h = 0;
        int display_tpye = 0;
        switch (impId) {
            case IMP_WIFI_ADV:
                w = 720;
                h = 960;
                display_tpye = 1;   //wifi连接页广告
                imp.put("tagid", TAG_ID_720);
                break;
            case IMP_LOGIN_ADV:
                w = 805;
                h = 322;
                display_tpye = 1;   //登陆页广告
                imp.put("tagid", TAG_ID);
                break;
            case IMG_SPLASH_ADV:
                w = 720;
                h = 960;
                display_tpye = 1;   //开屏广告
                imp.put("tagid", TAG_ID_720);
                break;
        }
        banner.put("w", w);
        banner.put("h", h);
        imp.put("banner", banner);
        JSONObject extension = new JSONObject();
        extension.put("display_type", display_tpye);
        imp.put("extension", extension);
        JSONArray arrayImp = new JSONArray();
        arrayImp.put(imp);
        return arrayImp;
    }

    public static JSONObject getAppObject() throws JSONException {
        //app
        JSONObject app = new JSONObject();
        app.put("name", App.getAppContext().getString(R.string.app_name));
        app.put("bundle", App.getAppContext().getPackageName());
        return app;
    }

    public static JSONObject getDevicesObject() throws JSONException {
        //devices
        JSONObject device = new JSONObject();
        DevicesInfo devicesInfo = new DevicesInfo(App.getAppContext());
        device.put("os", devicesInfo.os);
        device.put("osv", devicesInfo.osv);
        device.put("make", devicesInfo.make);
        device.put("model", devicesInfo.model);
        device.put("carrier", devicesInfo.carrier);
        device.put("ua", devicesInfo.ua);
        device.put("ip", devicesInfo.ip);
//        JSONObject geo = new JSONObject();
//        device.put("geo", geo);
        device.put("connectiontype", devicesInfo.connectiontype);
//        device.put("ifa", "");
        JSONObject extension1 = new JSONObject();
        extension1.put("imei", devicesInfo.y_device_ext_imei);
        extension1.put("mac", devicesInfo.y_device_ext_mac);
        device.put("extension", extension1);
        return device;
    }

    public static void clickFeedback(Creative creative) {
        String click_feedback_url = creative.click_feedback_url;
        String thirdparty_click_through_url = creative.thirdparty_click_through_url;
        if (!TextUtils.isEmpty(click_feedback_url)) {
            feedback(click_feedback_url);
        }
        if (!TextUtils.isEmpty(thirdparty_click_through_url)) {
            feedback(thirdparty_click_through_url);
        }

    }

    /**
     * 曝光
     */
    public static void exposure(Creative creative) {
        final String imp_feedback_url = creative.imp_feedback_url;
        final String thirdparty_imp_feedback_url = creative.thirdparty_imp_feedback_url;
        if (!TextUtils.isEmpty(imp_feedback_url)) {
            feedback(imp_feedback_url);
        }
        if (!TextUtils.isEmpty(thirdparty_imp_feedback_url)) {
            feedback(thirdparty_imp_feedback_url);
        }
    }

    private static void feedback(final String feedbackUrl) {
        new Thread() {
            @Override
            public void run() {
                HttpGet httpGet = null;
                    httpGet = new HttpGet(feedbackUrl);


                try {
                    HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 204 || httpResponse.getStatusLine().getStatusCode() == 200) {
                        Logger.i(TAG, "feedback success", "oddshou");
                    } else {
                        Logger.e(TAG, "feedback error ", "oddshou");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                super.run();
            }
        }.start();
    }

    public static class Creative {
        public int impid;
        public String crid;
        public String asset_url;
        public String headline;
        public String description;
        public String description_extention;
        public int w;
        public int h;
        public String imp_feedback_url;
        public String thirdparty_imp_feedback_url;
        public String click_feedback_url;
        public String thirdparty_click_through_url;
        public String bundle;
        public String download_complete_url;
        public String install_complete_url;

        public Creative(JSONObject createJson) {


            try {
                impid = createJson.getInt("impid");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                crid = createJson.getString("crid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                asset_url = createJson.getString("asset_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                description = createJson.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                headline = createJson.getString("headline");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                w = createJson.getInt("w");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                h = createJson.getInt("h");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                description_extention = createJson.getString("description_extention");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                imp_feedback_url = createJson.getString("imp_feedback_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                thirdparty_imp_feedback_url = createJson.getString("thirdparty_imp_feedback_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                click_feedback_url = createJson.getString("click_feedback_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                thirdparty_click_through_url = createJson.getString("thirdparty_click_through_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bundle = createJson.getString("bundle");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                download_complete_url = createJson.getString("download_complete_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                install_complete_url = createJson.getString("install_complete_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getStringWithKey(String key, JSONObject createJson) {
            if (!createJson.isNull(key))
                try {
                    createJson.getString(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        private void getIfnotNull(JSONObject createJson, String s) {

            Class cretive = getClass();

            for (Field field : cretive.getDeclaredFields()
                    ) {
                String key = field.getName();
                Type genericType = field.getGenericType();
                if (!createJson.isNull(key)) {
                    if (genericType.equals("class java.lang.String")) {

                    }
                }
            }
        }
    }
}
