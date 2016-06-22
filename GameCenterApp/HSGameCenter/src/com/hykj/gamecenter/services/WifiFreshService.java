package com.hykj.gamecenter.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.hykj.gamecenter.net.JsonCallback;
import com.hykj.gamecenter.net.WifiHttpUtils;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/6/22.
 */
public class WifiFreshService extends Service {
    public static final int DELAY_TIME = 1000 * 60 * 10;
    private static final String TAG = "WifiFreshService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(runnable);
        handler.post(runnable);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * xutils web 请求
     *
     * @param url
     */
    private void doPost(final String url, final WifiHttpUtils wifiHttpUtils) {
        RequestParams params = new RequestParams(url);
        params.addParameter("_data", wifiHttpUtils.getParmarData());
        params.addParameter("_sign", wifiHttpUtils.getParmarSign());
        x.http().post(params, new JsonCallback(url, getApplicationContext()) {

            @Override
            protected void handleSucced(JSONObject ddata, String url) {
                switch (url) {
                    case WifiHttpUtils.URL_WIFI_FRESH:      //刷新会话成功
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, DELAY_TIME);  //10分钟刷新
                        break;

                }

            }

            @Override
            protected void onException(String code, String codemsg, String url) {
                if (!code.equals("99")) {
                    String errString = codemsg;
                    handler.removeCallbacks(runnable);
                    stopSelf();
                    Log.e(TAG, errString);
//                    CSToast.show(mParentActiity, errString);
                }
            }
        });

    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            WifiHttpUtils wifiHttpUtils = new WifiHttpUtils(new JSONObject());
            doPost(WifiHttpUtils.URL_WIFI_FRESH, wifiHttpUtils);
        }
    };

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
