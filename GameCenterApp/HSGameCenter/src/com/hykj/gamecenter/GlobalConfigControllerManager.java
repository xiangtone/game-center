
package com.hykj.gamecenter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ControllerHelper;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGlobalConfigListener;
import com.hykj.gamecenter.controller.ReqGlobalConfigController;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.protocol.Apps.GroupInfo;
import com.hykj.gamecenter.protocol.Apps.Groups;
import com.hykj.gamecenter.protocol.Apps.RspGlobalConfig;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.hykj.gamecenter.utilscs.RegistrantList;

//import cs.util.RegistrantList;

public class GlobalConfigControllerManager {
    private static GlobalConfigControllerManager mGlobalConfigControllerManager;
    private ReqGlobalConfigController controller;
    private static final String TAG = "GlobalConfigControllerManager";
    public String mGroupServerVersion;

    public static boolean mbShowNotification = true; // 是否显示通知栏通知
    public static final int LOADING_STATE = 1;
    public static final int NONETWORK_STATE = 2;
    public static final int NORMAL_STATE = 3;
    private static int loadingState = LOADING_STATE;
    private final Context mContext = App.getAppContext();
    private LoadingStateListener mLoadingStateListener;
    private boolean hasLoaded = false;

    private final RegistrantList updateRegistrantList = new RegistrantList();

    public void setNotificationVisible(boolean bShow) {
        mbShowNotification = bShow;
    }

    public boolean isNotificationShow() {
        return mbShowNotification;
    }

    public interface LoadingStateListener {
        void onChange();
    }

    /*    private GlobalConfigControllerManager() {
            // TODO Auto-generated constructor stub
            mContext = App.getAppContext();
        }*/

    public static GlobalConfigControllerManager getInstance() {
        if (mGlobalConfigControllerManager == null) {
            mGlobalConfigControllerManager = new GlobalConfigControllerManager();
            loadingState = NORMAL_STATE;
        }
        return mGlobalConfigControllerManager;
    }

    public void reqGlobalConfig() {
//        Log.d(TAG, "reqGlobalConfig");
        loadingHandler.sendEmptyMessage(MSG_LOADING);
        controller = new ReqGlobalConfigController(reqGlobalConfigListener);
        controller.doRequest();
    }

    private final ReqGlobalConfigListener reqGlobalConfigListener = new ReqGlobalConfigListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.i(TAG, "errCode:" + errCode + " errorMsg:" + errorMsg);

            loadingHandler.sendEmptyMessage(MSG_NONETWORK);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.i(TAG, "statusCode :" + statusCode + " errorMsg:" + errorMsg);

            loadingHandler.sendEmptyMessage(MSG_NONETWORK);
        }

        @Override
        public void onReqGlobalConfigSucceed(RspGlobalConfig config) {
            Logger.i(TAG, "onReqGlobalConfigSucceed");

            // loadingHandler.sendEmptyMessage( MSG_NORMAL );
            // 配置信息版本
            mGroupServerVersion = config.groupsServerVer;
            // 缓存配置信息
            handleGroups(mGroupServerVersion, config);

        }

    };

    private void handleGroups(String groupsServerVer, RspGlobalConfig config) {
        Logger.i(TAG, "groupsServerVer : " + groupsServerVer);
        // groupsServerVer 相同代表客户端已经是最新版本
        if(config.groups.length > 0){
            try {

                // 设置客户端更新检测间隔时间
                UpdateUtils
                        .setUpdateCheckRate(config.updateCheckRate * 60 * 1000);
                UpdateUtils.setUpdateRecomm(config.recommCheckRate * 60 * 1000);
                // 缓存groups版本信息
                ControllerHelper.getInstance().setGroupsConfigCacheDataVer(
                        config.groupsServerVer);
                // Logger.i( TAG , "GroupsServerVer:" +
                // config.getGroupsServerVer( ) );
                Groups groups = Groups.parseFrom(config.groups);
                handleGroupInfos(groups.groupInfo);
            } catch (InvalidProtocolBufferNanoException e) {
                e.printStackTrace();
                Logger.e(TAG, ERROR.ERROR_BAD_PACKET);
            }
        }else {
            // Logger.i( TAG , "loadingHandler.sendEmptyMessage( MSG_NORMAL ); "
            // );
            hasLoaded = true;
            loadingHandler.sendEmptyMessage(MSG_NORMAL);
        }
    }

    // 向sqllite 中写入配置信息数据
    private void handleGroupInfos(GroupInfo[] list) {
        final ContentValues[] values = new ContentValues[list.length];
        final ContentResolver cr = mContext.getContentResolver();
        // new Thread( new Runnable( )
        // {
        // @Override
        // public void run()
        // {
        for (int i = 0; i < list.length; i++) {
            GroupInfo gi = list[i];
            ContentValues value = new ContentValues();
            value.put(GroupInfoColumns.GROUP_ID, gi.groupId);
            value.put(GroupInfoColumns.GROUP_CLASS, gi.groupClass);
            value.put(GroupInfoColumns.GROUP_TYPE, gi.groupType);
            value.put(GroupInfoColumns.ORDER_TYPE, gi.orderType);
            value.put(GroupInfoColumns.ORDER_NO, gi.orderNo);
            value.put(GroupInfoColumns.GROUP_NAME, gi.groupName);
            value.put(GroupInfoColumns.GROUP_PIC_URL, gi.groupPicUrl);
            value.put(GroupInfoColumns.GROUP_DESC, gi.groupDesc);
            value.put(GroupInfoColumns.RECOMM_WORD, gi.recommWord);
            value.put(GroupInfoColumns.START_TIME, gi.startTime);
            value.put(GroupInfoColumns.END_TIME, gi.endTime);

            // Logger.d( TAG , "groupInfo.groupId --- " + gi.getGroupId( ) );
            // Logger.d( TAG , "groupInfo.groupClass --- " + gi.getGroupPicUrl(
            // ) );
            values[i] = value;
        }
        cr.delete(CSACContentProvider.GROUPINFO_CONTENT_URI, null, null);
        Logger.i(TAG, "delete is start");
        // 批量插入
        cr.bulkInsert(CSACContentProvider.GROUPINFO_CONTENT_URI, values);
        Logger.i(TAG, "get global config success");
        loadingHandler.sendEmptyMessage(MSG_NORMAL);
        // loadingHandler.sendEmptyMessageDelayed( MSG_NORMAL , 1000 );
        // }
        // } ).start( );
    }

    private final int MSG_LOADING = 1000;
    private final int MSG_NONETWORK = 1001;
    private final int MSG_NORMAL = 1002;

    private final Handler loadingHandler = new Handler(mContext.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADING:
                    loadingState = LOADING_STATE;
                    // App.setLoadingstate( loadingState );
                    stateChange();
                    break;
                case MSG_NONETWORK:
                    loadingState = NONETWORK_STATE;
                    // App.setLoadingstate( loadingState );
                    stateChange();
                    break;
                case MSG_NORMAL:
                    loadingState = NORMAL_STATE;
                    // App.setLoadingstate( loadingState );
                    stateChange();
                    break;
                default:
                    break;
            }
        }

    };

    public int getLoadingState() {
        // Logger.i( TAG , "loadingState = " + loadingState );
        if (loadingState != LOADING_STATE && loadingState != NONETWORK_STATE
                && loadingState != NORMAL_STATE) {
            // reqGlobalConfig( );
            // return LOADING_STATE;
            loadingState = NORMAL_STATE;
        }

        return loadingState;
    }

    public void setLoadingState(int loadingState) {
        GlobalConfigControllerManager.loadingState = loadingState;
    }

    public void setLoadingStateListener(LoadingStateListener listener) {
        mLoadingStateListener = listener;
    }

    public void removeLoadingStateListener() {
        mLoadingStateListener = null;
    }

    private void stateChange() {
        if (mLoadingStateListener != null) {
            mLoadingStateListener.onChange();
        }
        updateRegistrantList.notifyRegistrants();
    }

    public void registForUpdate(Handler handler, int what, Object obj) {
        LogUtils.e("registe Handler =" + handler);
        updateRegistrantList.add(handler, what, obj);
    }

    public void unregistForUpdate(Handler handler) {
        LogUtils.e("remove Handler");
        updateRegistrantList.remove(handler);
    }

    public boolean isHasLoaded() {
        return hasLoaded;
    }

}
