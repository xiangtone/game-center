
package com.hykj.gamecenter.logic;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;

import java.util.List;

public class SearchRecommendControllerManager {
    private static SearchRecommendControllerManager mSearchRecommendControllerManager;

    private int mHotSearchLoadingState = NONETWORK_STATE;

    public static final int LOADING_STATE = 1;
    public static final int NONETWORK_STATE = 2;
    public static final int NORMAL_STATE = 3;

    private static int[] mHotSearchGroupId = new int[4];

    private List<GroupElemInfo> mHotSearchinfoList = null;

    private final Context mContext;
    private LoadingStateListener mLoadingStateListener;

    public interface LoadingStateListener {
        void onChange();
    }

    private SearchRecommendControllerManager() {
        mContext = App.getAppContext();

        if (mHotSearchGroupId[0] == 0)
            mHotSearchGroupId = getHotSearchGroupId();
    }

    public static SearchRecommendControllerManager getInstance() {
        if (mSearchRecommendControllerManager == null) {
            mSearchRecommendControllerManager = new SearchRecommendControllerManager();
        }
        return mSearchRecommendControllerManager;
    }

    public void reqConfig() {
        ReqGroupElemsListController hotSearchController = new ReqGroupElemsListController(
                mHotSearchGroupId[0], mHotSearchGroupId[1],
                mHotSearchGroupId[2], mHotSearchGroupId[3],
                HomePageActivity.REQ_PAGE_SIZE, 1, mReqHotSearchListener);
        hotSearchController
                .setClientPos(ReportConstants.STAC_APP_POSITION_SEARCH_GAME_APP);
        hotSearchController.doRequest();

        loadingHandler.sendEmptyMessage(MSG_LOADING);
    }

    public int[] getHotSearchGroupId() {
        int ints[] = new int[4];
        Cursor classifyCursor = mContext.getContentResolver().query(
                CSACContentProvider.GROUPINFO_CONTENT_URI,
                null,
                GroupInfoColumns.GROUP_TYPE + "="
                        + GROUP_TYPE.HOT_SEARCH_GROUP_TYPE, null, null);

        if (classifyCursor != null && classifyCursor.moveToNext()) {
            int gi = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.GROUP_ID));
            int gc = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.GROUP_CLASS));
            int gt = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.GROUP_TYPE));
            int go = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.ORDER_TYPE));
            ints[0] = gi;
            ints[1] = gc;
            ints[2] = gt;
            ints[3] = go;
            classifyCursor.close();
            return ints;
        }
        classifyCursor.close();
        return ints;
    }

    public List<GroupElemInfo> getHotSearchinfoList() {
        return mHotSearchinfoList;
    }

    public int[] getHotGamesGroupId() {
        int ints[] = new int[4];
        Cursor classifyCursor = mContext.getContentResolver().query(
                CSACContentProvider.GROUPINFO_CONTENT_URI,
                null,
                GroupInfoColumns.GROUP_TYPE + "="
                        + GROUP_TYPE.HOT_GAMES_GROUP_TYPE, null, null);
        if (classifyCursor != null && classifyCursor.moveToNext()) {
            int gi = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.GROUP_ID));
            int gc = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.GROUP_CLASS));
            int gt = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.GROUP_TYPE));
            int go = classifyCursor.getInt(classifyCursor
                    .getColumnIndex(GroupInfoColumns.ORDER_TYPE));
            ints[0] = gi;
            ints[1] = gc;
            ints[2] = gt;
            ints[3] = go;

            classifyCursor.close();
            return ints;
        }
        classifyCursor.close();
        return ints;
    }

    private static final int MSG_BASE = 10000;
    private static final int MSG_HOT_SEARCH_DATA_LOADED = MSG_BASE + 10;
    private static final int MSG_HOT_GAMES_SEARCH_NET_ERROR = MSG_BASE + 11;
    private static final int MSG_HOT_SEARCH_SEARCH_NET_ERROR = MSG_BASE + 12;
    private static final int MSG_LOADING = MSG_BASE + 13;

    private final ReqGroupElemsListener mReqHotSearchListener = new ReqGroupElemsListener() {
        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e("ReqHotSearchListener", "onNetError errCode:" + errCode
                    + ",errorMsg:" + errorMsg);
            loadingHandler.sendEmptyMessage(MSG_HOT_GAMES_SEARCH_NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e("ReqHotSearchListener", "onReqFailed statusCode:"
                    + statusCode + ",errorMsg:" + errorMsg);
            loadingHandler.sendEmptyMessage(MSG_HOT_GAMES_SEARCH_NET_ERROR);
        }

        @Override
        public void onReqGroupElemsSucceed(GroupElemInfo[] infoList,
                String serverDataVer) {
            Logger.d("ReqHotSearchListener", "infoes.size()=" + infoList.length);
            mHotSearchinfoList = Tools.arrayToList(infoList);
            loadingHandler.sendEmptyMessage(MSG_HOT_SEARCH_DATA_LOADED);
        }
    };

    private final Handler loadingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_LOADING:
                    mHotSearchLoadingState = LOADING_STATE;
                    stateChange();
                    break;

                case MSG_HOT_SEARCH_SEARCH_NET_ERROR:
                    mHotSearchLoadingState = NONETWORK_STATE;
                    stateChange();
                    break;

                case MSG_HOT_SEARCH_DATA_LOADED:
                    mHotSearchLoadingState = NORMAL_STATE;
                    stateChange();
                    break;

                default:
                    break;
            }
        }
    };

    public int getHotSearchLoadingState() {
        return mHotSearchLoadingState;
    }

    public void setLoadingStateListener(LoadingStateListener listener) {
        mLoadingStateListener = listener;
    }

    private void stateChange() {
        if (mLoadingStateListener != null) {
            mLoadingStateListener.onChange();
        }
    }

}
