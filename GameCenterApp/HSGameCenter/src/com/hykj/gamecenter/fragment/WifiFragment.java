package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.PersonLogin;
import com.hykj.gamecenter.broadcast.WifiUpdateReceiver;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.db.CSACDatabaseHelper;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.net.JsonCallback;
import com.hykj.gamecenter.net.WifiHttpUtils;
import com.hykj.gamecenter.services.WifiFreshService;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.WifiConnect;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
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
    private WifiUpdateReceiver.WifiListener mWifiListener;
    private WifiManager mWifiManager;
    private ImageView mImgAdv;
    private GroupInfo mGroupInfo;
    private Button mBtnConnect;
    private View mLayoutLoading;
    private boolean mConnecting = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifiManager = (WifiManager) mParentActiity.getSystemService(Context.WIFI_SERVICE);

        //获取广告图分组信息
        String selection = CSACDatabaseHelper.GroupInfoColumns.GROUP_ID + " =?";
        String[] selectionArgs = new String[]{111 + ""};
        mGroupInfo = DatabaseUtils.getGroupinfoByDB(selection, selectionArgs);
    }

    @Override
    public void onAttach(Activity activity) {
        mParentActiity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        if (!mConnecting) {
            boolean checkIndentifySsid = checkIndentifySsid();
            updateState(checkIndentifySsid ? ConnectState.CONNECTED : ConnectState.UNCONNECTED);
        }


        if (mWifiListener == null) {
            mWifiListener = new WifiUpdateReceiver.WifiListener() {
                @Override
                public void networkChange(int currentNetwork, NetworkInfo networkInfo) {
                    if (currentNetwork == 1) {
                        wifiConnectedBefore = true;
                        if (mTextLoadingState != null) {
                            mTextLoadingState.setText(R.string.wifi_loading_success);
                        }
                        //验证登录并开网
                        //判断ssid为可用ssid
//                        checkLogin();

                        updateState(ConnectState.CONNECTED);
                    }
                }
            };
        }
        WifiUpdateReceiver.setWifiConnectListen(mWifiListener);
        super.onResume();
    }

    /**
     * 判断是已连接到指定wifi
     *
     * @return
     */
    private boolean checkIndentifySsid() {
        //检测是否已连接花生wifi
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return false;
        }
        ConnectivityManager connec = (ConnectivityManager) mParentActiity.getSystemService(Context.CONNECTIVITY_SERVICE);
        String connectedSsid = wifiInfo.getSSID();
        if ((connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                && connectedSsid != null) {
            for (String ssid : WifiHttpUtils.SSID_LIST) {
                if (connectedSsid.equalsIgnoreCase("\"" + ssid + "\"") || connectedSsid.equalsIgnoreCase(ssid)) {
                    return true;
                }

            }

        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mTextLoadingState = (TextView) rootView.findViewById(R.id.textLoadingState);
        mImgAdv = (ImageView) rootView.findViewById(R.id.imgAdv);
        mImgAdv.setOnClickListener(mOnclickListen);
        mBtnConnect = (Button) rootView.findViewById(R.id.btnConnect);
        mBtnConnect.setOnClickListener(mOnclickListen);
        mLayoutLoading = rootView.findViewById(R.id.layoutLoading);

        if (mGroupInfo != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(mGroupInfo.groupPicUrl, mImgAdv,
                    DisplayOptions.optionsSnapshot);
        }
    }

    @Override
    public void onPause() {
//        mParentActiity.unregisterReceiver(wifiReceive);
        WifiUpdateReceiver.removeWifiListener(mWifiListener);

        super.onPause();
    }

    private View.OnClickListener mOnclickListen = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgAdv:       //
                    if (mConnecting) return;
                    //进入游戏详情
                    break;
                case R.id.btnConnect:   //点击一键上网
                    ConnectTask task = new ConnectTask();
                    task.execute((Void[]) null);
                    updateState(ConnectState.CONNECTING);
                    break;
            }
        }
    };


    public enum ConnectState {
        UNCONNECTED,
        CONNECTING,
        CONNECTED,
        WIFIUNVISIBLE
    }

    /**
     * 控制连接状态显示 1未连接，显示一键上网
     * 2连接中 显示loading
     * 3已连接 显示已连接不可点击
     * 4未检测到wifi 显示一键上网
     *
     * @param state
     */
    private void updateState(ConnectState state) {
        mConnecting = false;
        switch (state) {
            case UNCONNECTED:
                mBtnConnect.setVisibility(View.VISIBLE);
                mLayoutLoading.setVisibility(View.INVISIBLE);
                mBtnConnect.setText(R.string.wifi_connect);
                mTextLoadingState.setText(R.string.wifi_welcom);
                mBtnConnect.setEnabled(true);
                break;
            case CONNECTING:
                mBtnConnect.setVisibility(View.INVISIBLE);
                mLayoutLoading.setVisibility(View.VISIBLE);
                mConnecting = true;
                mTextLoadingState.setText(R.string.wifi_welcom);
                break;
            case CONNECTED:
                mBtnConnect.setVisibility(View.VISIBLE);
                mLayoutLoading.setVisibility(View.INVISIBLE);
                mBtnConnect.setText(R.string.wifi_connected);
                mTextLoadingState.setText(R.string.wifi_welcom);
                mBtnConnect.setEnabled(false);
                break;
            case WIFIUNVISIBLE:
                mBtnConnect.setVisibility(View.VISIBLE);
                mLayoutLoading.setVisibility(View.INVISIBLE);
                mBtnConnect.setText(R.string.wifi_connect);
                mTextLoadingState.setText(R.string.wifi_unvisible);
                mBtnConnect.setEnabled(true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    final class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            WifiConnect con = new WifiConnect(mWifiManager);
            //花生地铁WiFi_测试_szoffice

//            String ssid = "SGV-test";

//            boolean connected = con.Connect(ssid,
//                    "26630499", WifiConnect.WifiCipherType.WIFICIPHER_WPA);
            String ssid = "huaying-1";

            boolean connected = con.Connect(ssid,
                    "hy1234560", WifiConnect.WifiCipherType.WIFICIPHER_WPA);
//            String ssid = "花生地铁WiFi_测试_szoffice";
//            boolean connected = con.Connect(ssid,
//                    "", WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);

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
                updateState(ConnectState.WIFIUNVISIBLE);
            }
        }
    }

    //检测登录状态，开网
    private void checkLogin() {
        String sessid = App.getSharedPreference().getString(StatisticManager.KEY_WIFI_SESSID, "");
        if (TextUtils.isEmpty(sessid)) {
            gotoLogin();

        } else {
            openWifiWithSessid(sessid);
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
    private void openWifiWithSessid(String sessid) {
        String mac = APNUtil.getMac(mParentActiity);
        if (!TextUtils.isEmpty(mac)) {
            WifiHttpUtils wifiHttpUtilsOpen = new WifiHttpUtils(WifiHttpUtils.creteWifiOpen(mac));
            wifiHttpUtilsOpen.getmHdata().setSessid(sessid);
            doPost(WifiHttpUtils.URL_WIFI_OPEN, wifiHttpUtilsOpen);
        } else {
            //无mac地址，开网失败
        }
    }

    private void openWifi() {
        //获取服务时间
        WifiHttpUtils wifiHttpUtils = new WifiHttpUtils(new JSONObject());
        wifiHttpUtils.getmHdata().setVer(2);
        doPost(WifiHttpUtils.URL_UTIME, wifiHttpUtils);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            openWifi();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static int RESTART_COUNT = 0;

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
                try {
                    switch (url) {
                        case WifiHttpUtils.URL_UTIME:   //获取服务器时间成功
                            int utime = ddata.getInt("utime");
                            //用户登录
                            WifiHttpUtils wifiHttpUtilsUlogin = new WifiHttpUtils(
                                    WifiHttpUtils.createSessid(utime, APNUtil.getMac(mParentActiity)));
                            wifiHttpUtilsUlogin.getmHdata().setVer(2);
                            doPost(WifiHttpUtils.URL_SEDDID, wifiHttpUtilsUlogin);
                            break;
                        case WifiHttpUtils.URL_SEDDID:
                            String sessid = ddata.getString("sessid");
                            SharedPreferences.Editor edit = App.getSharedPreference().edit();
                            edit.putString(StatisticManager.KEY_WIFI_SESSID, sessid);
                            edit.apply();
                            openWifiWithSessid(sessid);
                            break;
                        case WifiHttpUtils.URL_WIFI_OPEN:     //开网成功
                            int uuid = 0;
                            uuid = ddata.getInt("uuid");
                            String ucode = ddata.getString("ucode");
                            int uisnew = ddata.getInt("uisnew");
                            Intent intent = new Intent(mParentActiity, WifiFreshService.class);
                            mParentActiity.startService(intent);
                            updateState(ConnectState.CONNECTED);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //解析错误，交互协议没有出错的话不会出现
                }
            }

            @Override
            protected void onException(String code, String codemsg, String url) {
                if (!code.equals("99")) {
                    String errString = codemsg;
                    if (code.equals("4")) { //会话超时需要重连一次，如果继续会话超时则重新登陆连接
                        if (RESTART_COUNT++ <= 0) {
                            openWifi();
                        } else {
                            errString = getString(R.string.wifi_error_code_4);
                            gotoLogin();
                            Log.e(TAG, errString);
                            CSToast.show(mParentActiity, errString);
                            return;
                        }
                    } else {
                        switch (url) {
                            case WifiHttpUtils.URL_SEDDID:
                                switch (code) {
                                    case "100":
                                        errString = getString(R.string.wifi_error_sessid_100);
                                        break;
                                    case "101":
                                        errString = getString(R.string.wifi_error_sessid_101);
                                        break;
                                }
                                break;
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
                    }
                    Log.e(TAG, errString);
                    CSToast.show(mParentActiity, errString);
                    //普通error 停止连接状态
                    updateState(ConnectState.UNCONNECTED);
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
