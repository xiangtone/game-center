package com.hykj.gamecenter.services;

import android.app.Service;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.broadcast.WifiUpdateReceiver;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.utils.NanoHTTPD;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by win7 on 2016/7/11.
 */
public class DaemonService extends Service {


    private static final String TAG = "DaemonService";
    private MyNanoHttpd mMyNanoHttpd;
    private WifiUpdateReceiver.WifiListener mWifiListener;
    private static final int PORT = 55556;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mWifiListener == null) {
            mWifiListener = new WifiUpdateReceiver.WifiListener() {
                @Override
                public void networkChange(int currentNetwork, NetworkInfo networkInfo) {
                    if (currentNetwork != -1) {
                        String ip = APNUtil.getCurrentIp();

                        if (TextUtils.isEmpty(ip)) {
                            return;
                        }
                        //网络切换，ip切换，重新设置监听
                        if (mMyNanoHttpd != null && mMyNanoHttpd.isAlive()) {
                            mMyNanoHttpd.stop();
                        }
                        try {
                            mMyNanoHttpd = new MyNanoHttpd(ip, PORT);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
        WifiUpdateReceiver.setWifiConnectListen(mWifiListener);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "DaemonService start");
        if (mMyNanoHttpd == null) {
            try {
                mMyNanoHttpd = new MyNanoHttpd(APNUtil.getCurrentIp(), PORT);
                Log.e(TAG, "ip " + APNUtil.getCurrentIp());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Couldn't start server:\n" + e);
            }
        }
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WifiUpdateReceiver.removeWifiListener(mWifiListener);
    }

    public class MyNanoHttpd extends NanoHTTPD {

        public MyNanoHttpd(int port) throws IOException {
            super(port);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
//            Log.e(TAG, "Running! Point your browsers to http://localhost:/5555");
        }

        public MyNanoHttpd(String hostname, int port) throws IOException {
            super(hostname, port);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
//            Log.e(TAG, "Running! Point your browsers to http://localhost:/5555");
        }

        @Override
        public Response serve(IHTTPSession session) {
            if (session.getMethod().equals(Method.GET)) {
                String uri = session.getUri();
                Map<String, List<String>> parameters = session.getParameters();
                Log.e(TAG, parameters.toString());
                if (uri.equals("/sendIntent/openapp")) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(getApplicationContext(), HomePageActivity.class);
                    DaemonService.this.startActivity(intent);
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "1");
                } else if (uri.equals("/sendIntent/checkapp")) {
                    return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "2");
                }
                Log.e(TAG, "uri " + uri + " method " + session.getMethod());
            }
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "0");
        }


    }
}
