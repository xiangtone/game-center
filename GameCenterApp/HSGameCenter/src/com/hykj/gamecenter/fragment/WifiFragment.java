package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.PersonLogin;
import com.hykj.gamecenter.broadcast.WifiUpdateReceiver;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.net.JsonCallback;
import com.hykj.gamecenter.net.WifiHttpUtils;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.WifiConnect;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/6/15.
 */
public class WifiFragment extends Fragment implements IFragmentInfo {


    private static final String TAG = "WifiFragment";
    private TextView mTextLoadingState;
    private Activity mParentActiity;
    private boolean wifiConnectedBefore;
    private ConnectivityManager mConnManager;
    private WifiConnectedBroadCast wifiReceive;
    private WifiUpdateReceiver.WifiListener mWifiListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnManager = (ConnectivityManager) mParentActiity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        wifiConnectedBefore = mWifi.isConnected();
        ConnectTask task = new ConnectTask();
        task.execute((Void[]) null);



    }

    @Override
    public void onAttach(Activity activity) {
        mParentActiity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        if (wifiReceive == null) {
//            wifiReceive = new WifiConnectedBroadCast();
//        }
//        mParentActiity.registerReceiver(wifiReceive, filter);

        if (mWifiListener == null) {
            mWifiListener = new WifiUpdateReceiver.WifiListener() {
                @Override
                public void networkChange(int currentNetwork) {
                    if (currentNetwork == 1) {
                        wifiConnectedBefore = true;
                        if (mTextLoadingState != null) {
                            mTextLoadingState.setText(R.string.wifi_loading_success);
                        }
                        //验证登录并开网
                        Logger.i(TAG, "checkLogin", "oddshou");
                        checkLogin();
                    }
                }
            };
        }
        WifiUpdateReceiver.setWifiConnectListen(mWifiListener);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi, null);
        mTextLoadingState = (TextView) rootView.findViewById(R.id.textLoadingState);

        return rootView;
    }

    @Override
    public void onPause() {
//        mParentActiity.unregisterReceiver(wifiReceive);
        WifiUpdateReceiver.removeWifiListener(mWifiListener);

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    final class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            WifiConnect con = new WifiConnect(
                    (WifiManager) mParentActiity.getSystemService(Context.WIFI_SERVICE));
            //花生地铁WiFi_测试_szoffice

            String ssid = "花生地铁WiFi_测试_szoffice";
//            String ssid = "SGV-test";

//            boolean connected = con.Connect(ssid,
//                    "26630499", WifiConnect.WifiCipherType.WIFICIPHER_WPA);
            boolean connected = con.Connect(ssid,
                    "", WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);

            return connected;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (!result) {
//                msgHandler.removeMessages(MSG_CONNECTED_TIMEOUT);
//                Toast.makeText(mParentActiity, getResources().getString(R.string.wifi_connectederror),
//                        Toast.LENGTH_LONG).show();
//                msgHandler.sendEmptyMessage(MSG_DISM_DIALOG);
                CSToast.show(mParentActiity, "连接wifi失败");
            }
        }
    }

    public class WifiConnectedBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                NetworkInfo info = APNUtil.getActiveNetwork(context);
                if (null != info) {//无网络时 info值为空
                    int type = info.getType();//type = 0 为mobile 状态  = 1 为 wifi 状态
                    String name = info.getTypeName();
                    State state = info.getState();
                    if (state == State.CONNECTED) {
                        if (type == 1 || name.equals("WIFI")) {
                            wifiConnectedBefore = true;
                            if (mTextLoadingState != null) {
                                mTextLoadingState.setText(R.string.wifi_loading_success);
                            }
                            //验证登录并开网
                            Logger.i(TAG, "checkLogin", "oddshou");
                            checkLogin();

                        }
                    }
                }
            }
        }
    }

    //检测登录状态，开网
    private void checkLogin() {
        String sessid = App.getSharedPreference().getString(StatisticManager.KEY_WIFI_SESSID, "");
        if (TextUtils.isEmpty(sessid)) {
            gotoLogin();

        } else {
            openWifi(sessid);
        }
    }

    private void gotoLogin() {
        //清空sessid
        App.getSharedPreference().edit().putString(StatisticManager.KEY_WIFI_SESSID, "").apply();
        //登录
        final Intent intent = new Intent();
        intent.setClass(mParentActiity, PersonLogin.class);
        startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }

    /**
     * 申请开网
     */
    private void openWifi(String sessid) {
        String mac = APNUtil.getMac(mParentActiity);
        if (!TextUtils.isEmpty(mac)) {
            WifiHttpUtils wifiHttpUtilsOpen = new WifiHttpUtils(WifiHttpUtils.creteWifiOpen(mac));
            wifiHttpUtilsOpen.getmHdata().setSessid(sessid);
            doPost(WifiHttpUtils.URL_WIFI_OPEN, wifiHttpUtilsOpen);
        } else {
            //无mac地址，开网失败
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String sessid = data.getStringExtra("sessid");
            openWifi(sessid);
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        x.http().post(params, new JsonCallback(url, mParentActiity) {

            @Override
            protected void handleSucced(JSONObject ddata, String url) {
//                try {
//                    switch (url) {
//                        case WifiHttpUtils.URL_WIFI_OPEN:     //开网成功
//                            int uuid = 0;
//                            uuid = ddata.getInt("uuid");
//                            String ucode = ddata.getString("ucode");
//                            int uisnew = ddata.getInt("uisnew");
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            protected void onException(String code, String codemsg, String url) {
                if (!code.equals("99")) {
                    String errString = codemsg;
                    switch (url) {
                        case WifiHttpUtils.URL_WIFI_OPEN:
                            switch (code) {
                                case "100":
                                    errString = getString(R.string.wifi_error_openwifi_100);
                                    break;
                                case "101":
                                    errString = getString(R.string.wifi_error_openwifi_101);
                                    break;
                                case "102":
                                    errString = getString(R.string.wifi_error_openwifi_102);
                                    break;
                                case "103":
                                    errString = getString(R.string.wifi_error_openwifi_103);
                                    break;
                                case "104":
                                    errString = getString(R.string.wifi_error_openwifi_104);
                                    break;
                                case "105":
                                    errString = getString(R.string.wifi_error_openwifi_105);
                                    gotoLogin();
                                    break;
                                case "106":
                                    errString = getString(R.string.wifi_error_openwifi_106);
                                    gotoLogin();
                                    break;
                                case "107":
                                    errString = getString(R.string.wifi_error_openwifi_107);
                                    break;
                            }
                            break;

                    }
                    Log.e(TAG, errString);
                    CSToast.show(mParentActiity, errString);
                }
            }
        });

    }

    public static String checkNetwrok(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return context.getString(R.string.wifi_mobile_link);
        networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return context.getString(R.string.wifi_link);

        return context.getString(R.string.wifi_link_none);
    }

    @Override
    public String getFragmentTabLabel() {
        return IFragmentInfo.FragmentTabLabel.WIFI_LABEL;
    }
}
