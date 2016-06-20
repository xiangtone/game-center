package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.PersonLogin;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.WifiConnect;

/**
 * Created by Administrator on 2016/6/15.
 */
public class WifiFragment extends Fragment implements IFragmentInfo {


    private TextView mTextLoadingState;
    private Activity mParentActiity;
    private boolean wifiConnectedBefore;
    private ConnectivityManager mConnManager;
    private WifiConnectedBroadCast wifiReceive;

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
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        wifiReceive = new WifiConnectedBroadCast();
        mParentActiity.registerReceiver(wifiReceive, filter);
    }

    @Override
    public void onAttach(Activity activity) {
        mParentActiity = activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi, null);
        mTextLoadingState = (TextView) rootView.findViewById(R.id.textLoadingState);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mParentActiity.unregisterReceiver(wifiReceive);
    }

    final class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            WifiConnect con = new WifiConnect(
                    (WifiManager) mParentActiity.getSystemService(Context.WIFI_SERVICE));
            //花生地铁WIFI_测试_szoffice
            String ssid = "花生地铁WiFi_测试_szoffice";
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
                            checkLogin();
                        }
                    }
                }
            }
        }
    }

    //检测登录状态，开网
    private void checkLogin() {
        String ssessid = App.getSharedPreference().getString(StatisticManager.KEY_WIFI_SESSID, "");
        if (TextUtils.isEmpty(ssessid)) {
            //登录
//            String macAddress = APNUtil.getMac(mParentActiity);
            final Intent intent = new Intent();
            intent.setClass(mParentActiity, PersonLogin.class);
            startActivityForResult(intent, Activity.RESULT_FIRST_USER);
        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
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
