package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.activity.PersonLogin;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.broadcast.WifiUpdateReceiver;
import com.hykj.gamecenter.controller.ProtocolListener;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.db.CSACDatabaseHelper;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.DisplayOptions;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.net.JsonCallback;
import com.hykj.gamecenter.net.WifiHttpUtils;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.services.WifiFreshService;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.NetUtils;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utils.WifiConnect;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/15.
 */
public class WifiFragment extends BaseFragment implements IFragmentInfo {


    private static final String TAG = "WifiFragment";
    private TextView mTextLoadingState;
    private Activity mParentActiity;
    private ConnectivityManager mConnManager;
    private WifiUpdateReceiver.WifiListener mWifiListener;
    private WifiManager mWifiManager;
    private ImageView mImgAdv;
    private GroupInfo mGroupInfo;
    private Button mBtnConnect;
    private View mLayoutLoading;
    private boolean mConnecting = false;
    private boolean hasLoadData = false;
    private Apps.GroupElemInfo mGroupElemInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConfigControllerManager.getInstance().registForUpdate(mHandler,
                Msg.UPDATE_STATE, null);
        mWifiManager = (WifiManager) mParentActiity.getSystemService(Context.WIFI_SERVICE);

        if (mWifiListener == null) {
            mWifiListener = new WifiUpdateReceiver.WifiListener() {
                @Override
                public void networkChange(int currentNetwork, NetworkInfo networkInfo) {
                    if (currentNetwork == 1) {
                        //验证登录并开网
                        //地铁网络连接之后尝试登陆
                        boolean checkIndentifySsid = NetUtils.CheckIndentifySsid(mParentActiity, WifiHttpUtils.SSID_HEAD);
                        if (checkIndentifySsid){
                            checkLogin();
                        }
                    } else {
                        //wifi切换到其他状态
                        if (!mConnecting) {
                            updateState(ConnectState.UNCONNECTED);
                        }
                    }
                }
            };
        }
        WifiUpdateReceiver.setWifiConnectListen(mWifiListener);


    }

    @Override
    public void onAttach(Activity activity) {
        mParentActiity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
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
        getDataList();

//        //判断当前网络是否连接并测试连接状态
//        boolean checkIndentifySsid = NetUtils.CheckIndentifySsid(mParentActiity, WifiHttpUtils.SSID_HEAD);
//        updateState(checkIndentifySsid ? ConnectState.CONNECTED : ConnectState.UNCONNECTED);
//        //ping 公网进一步验证
//        if (checkIndentifySsid) {
////            new PingAddress().start();
//            WifiHttpUtils wifiHttpUtils = new WifiHttpUtils(new JSONObject());
//            doPost(WifiHttpUtils.URL_WIFI_FRESH, wifiHttpUtils);
//        }

        boolean connectedState = getArguments().getBoolean(HomePageActivity.KEY_WIFI_CONNECTED);
        updateState(connectedState ? ConnectState.CONNECTED : ConnectState.UNCONNECTED);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private View.OnClickListener mOnclickListen = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgAdv:       //
                    if (mConnecting) return;
                    //进入游戏详情
                    if (mGroupElemInfo != null) {
                        Intent intentDetail = new Intent(
                                mParentActiity, PhoneAppInfoActivity.class);
                        intentDetail.putExtra(ProtocolListener.KEY.GROUP_INFO, mGroupElemInfo);
                        intentDetail
                                .putExtra(StatisticManager.APP_POS_TYPE, ReportConstants.STATIS_TYPE.WIFI_CONNECT);
                        intentDetail.putExtra(StatisticManager.APP_POS_POSITION,
                                mGroupElemInfo.posId);
                        mParentActiity.startActivity(intentDetail);
                    }

                    break;
                case R.id.btnConnect:   //点击一键上网
                    ConnectTask task = new ConnectTask();
                    task.execute((Void[]) null);
                    updateState(ConnectState.CONNECTING);
                    break;
            }
        }
    };

    private void reGetData() {
        int global = GlobalConfigControllerManager.getInstance()
                .getLoadingState();
        setDisplayStatus(LOADING_STATUS);
        switch (global) {
            case GlobalConfigControllerManager.LOADING_STATE:
            case GlobalConfigControllerManager.NONETWORK_STATE:
                GlobalConfigControllerManager.getInstance()
                        .reqGlobalConfig();
                Log.e(TAG, "game retry config");
                break;
            case GlobalConfigControllerManager.NORMAL_STATE:
                //获取广告图分组信息
                String selection = CSACDatabaseHelper.GroupInfoColumns.GROUP_TYPE + " =?";
                String[] selectionArgs = new String[]{ProtocolListener.GROUP_TYPE.WIFI_ADV_RECOMMED_TYPE + ""};
                mGroupInfo = DatabaseUtils.getGroupinfoByDB(selection, selectionArgs);
                if (mGroupInfo != null && mImgAdv != null) {
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(mGroupInfo.groupPicUrl, mImgAdv,
                            DisplayOptions.optionsIcon);
                    reqAppList(mGroupInfo);
                }
                setDisplayStatus(NORMAL_STATUS);
                break;
            default:
                break;
        }
    }

    private void reqAppList(GroupInfo groupInfo) {

        ReqGroupElemsListController controller = new ReqGroupElemsListController(
                groupInfo.groupId, groupInfo.groupClass, groupInfo.groupType, groupInfo.orderNo,
                HomePageActivity.REQ_PAGE_SIZE, 1,
                mReqGameElemsListener);
        controller.setClientPos(ReportConstants.reportPos(
                ReportConstants.STATIS_TYPE.WIFI_CONNECT));
        controller.doRequest();
    }


    private final ProtocolListener.ReqGroupElemsListener mReqGameElemsListener = new ProtocolListener.ReqGroupElemsListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e("mReqGameElemsListener", "onNetError errCode:" + errCode
                    + ",errorMsg:" + errorMsg);
//            mHandler.sendEmptyMessage(MSG_NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e("mReqGameElemsListener", "onReqFailed statusCode:"
                    + statusCode + ",errorMsg:" + errorMsg);
//            mUiHandler.sendEmptyMessage(MSG_LAST_PAGE);
        }

        @Override
        public void onReqGroupElemsSucceed(Apps.GroupElemInfo[] infoList,
                                           String serverDataVer) {
            Logger.d(TAG, "infoList.size()= " + infoList.length);
//            if (infoList.length <= 0) {
//                mUiHandler.sendEmptyMessage(MSG_LAST_PAGE);
//            } else {
//                InfoList.addAll(Tools.arrayToList(infoList));
//                Message msg = Message.obtain();
//                msg.what = MSG_GET_DATA;
//                msg.obj = (Tools.arrayToList(infoList));
//                mUiHandler.sendMessage(msg);
//            }
            if (infoList.length > 0) {
                mGroupElemInfo = infoList[0];
            }

        }

    };

    private static final int NORMAL_STATUS = 0;
    private static final int LOADING_STATUS = 1;
    private static final int NO_NETWORK_STATUS = 2;

    private void setDisplayStatus(int flag) {
        switch (flag) {
            case NORMAL_STATUS:
                break;
            case LOADING_STATUS:
                break;
            case NO_NETWORK_STATUS:
                break;
            default:
                break;
        }
    }

    public static final int MSG_PING_SUCCEED = 0X01;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case Msg.NET_ERROR:
                    if (mGroupInfo == null) {
                        setDisplayStatus(NO_NETWORK_STATUS);
                    }
                    break;
                case Msg.LOADING:
                    break;
                case Msg.APPEND_DATA:
                    if (GlobalConfigControllerManager.getInstance()
                            .getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE)
                        break;
                    //获取广告图分组信息
                    String selection = CSACDatabaseHelper.GroupInfoColumns.GROUP_ID + " =?";
                    String[] selectionArgs = new String[]{111 + ""};
                    mGroupInfo = DatabaseUtils.getGroupinfoByDB(selection, selectionArgs);
                    if (mGroupInfo != null && mImgAdv != null) {
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(mGroupInfo.groupPicUrl, mImgAdv,
                                DisplayOptions.optionsIcon);
                        reqAppList(mGroupInfo);
                    }
                    setDisplayStatus(NORMAL_STATUS);
                    break;
                case Msg.UPDATE_STATE:
                    Log.i(TAG, "Classify UPDATE_STATE");
                    getDataList();
                    break;
                case MSG_PING_SUCCEED:
                    updateState(ConnectState.UNCONNECTED);
                    CSToast.show(mParentActiity, getResources().getString(R.string.wifi_erroring));
                    break;
                default:
                    break;
            }
        }
    };

    private synchronized void getDataList() {
        UITools.notifyStateChange(this);
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public boolean hasLoadedData() {
        return hasLoadData;
    }

    @Override
    public void setHasLoadedData(boolean loaded) {
        hasLoadData = loaded;
    }

    @Override
    public void initFragmentListData() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }


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
        GlobalConfigControllerManager.getInstance().unregistForUpdate(mHandler);
        WifiUpdateReceiver.removeWifiListener(mWifiListener);

        super.onDestroy();
    }

    final class ConnectTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub
            WifiConnect con = new WifiConnect(mWifiManager);
            int connected = con.Connect(WifiHttpUtils.SSID_HEAD,
                    WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);

            return connected;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            switch (result) {
                case -1:    //未检测到花生wifi
                    CSToast.show(mParentActiity, getResources().getString(R.string.wifi_unfind));
                    updateState(ConnectState.WIFIUNVISIBLE);
                    break;
                case -2:    //连接失败
//                    CSToast.show(mParentActiity, "连接wifi失败");
                    updateState(ConnectState.WIFIUNVISIBLE);
                    break;
                case 1:     //重连成功
                    break;
                case 2:     //直连成功
                    mWifiListener.networkChange(1, null);
                    break;

            }

//            if (!result) {
//                CSToast.show(mParentActiity, "连接wifi失败");
//                updateState(ConnectState.WIFIUNVISIBLE);
//            }
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
        WifiHttpUtils.SESSID = "";
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
        }else{
            updateState(ConnectState.UNCONNECTED);
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
                            WifiHttpUtils.SESSID = sessid;
                            openWifiWithSessid(sessid);
                            break;
                        case WifiHttpUtils.URL_WIFI_OPEN:     //开网成功
//                            int uuid = 0;
//                            uuid = ddata.getInt("uuid");
//                            String ucode = ddata.getString("ucode");
//                            int uisnew = ddata.getInt("uisnew");
                            RESTART_COUNT = 0;
                            Intent intent = new Intent(mParentActiity, WifiFreshService.class);
                            mParentActiity.startService(intent);
                            updateState(ConnectState.CONNECTED);
                            if (mGroupElemInfo == null) {
                                reGetData();
                            }
                            if (getActivity() instanceof IWifiConnected) {
                                ((IWifiConnected) getActivity()).wifiConnected();
                            }
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
                        int uuid = App.getSharedPreference().getInt(StatisticManager.KEY_WIFI_UUID, 0);
                        if (RESTART_COUNT++ <= 0 && uuid != 0) {
                            openWifi();
                            Log.e(TAG, errString);
                            return;
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

//    private void reqGameList() {
//
//
//        int ints[] = DatabaseUtils.getGroupIdByDB(
//                App.ismAllGame() ? GROUP_TYPE.GAME_RECOMMED_TYPE : GROUP_TYPE.HOME_RECOMMED_TYPE
//                , ORDER_BY.AUTO);
//        LogUtils.d("ints[0] = " + ints[0] + " ints[1] = " + ints[1]
//                + " ints[2] = " + ints[2] + " ints[3] = " + ints[3]);
//        ReqGroupElemsListController controller = new ReqGroupElemsListController(
//                ints[0], ints[1], ints[2], ints[3],
//                HomePageActivity.REQ_PAGE_SIZE, mCurrentPage,
//                mReqGroupElemsListener);
//        // 云指令跳转渠道号设置
//        // controller.setChnNo( ( (HomePageActivity)getActivity( ) ).getChnNo( )
//        // );
//        controller.doRequest();
//    }

    class PingAddress extends Thread {

        @Override
        public void run() {
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(
                        "/system/bin/ping -c 1 -w 1 " + "www.baidu.com");
                int status = p.waitFor();
                if (status == 1) {
                    mHandler.sendEmptyMessage(MSG_PING_SUCCEED);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface IWifiConnected{
        void wifiConnected();
    }
}
