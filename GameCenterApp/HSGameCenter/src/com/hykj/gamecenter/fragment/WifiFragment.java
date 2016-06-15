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
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hykj.gamecenter.R;
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
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
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
                    "",WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);

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
            // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
            // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent
                    .getAction())) {

                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    State state = networkInfo.getState();
                    boolean isConnected = state == State.CONNECTED;// 当然，这边可以更精确的确定状态
                    // LogTag.showTAG_e(this.getClass().getSimpleName(),
                    // "isConnected"+isConnected);
                    if (isConnected && !wifiConnectedBefore) {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.wifi_connectedsuccess),
//                                Toast.LENGTH_LONG).show();
//                        msgHandler.removeMessages(MSG_CONNECTED_TIMEOUT);
//                        msgHandler.sendEmptyMessage(MSG_DISM_DIALOG);
                        wifiConnectedBefore = true;
                    }else{
                        wifiConnectedBefore = false;
                    }
                }
            }
        }
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
