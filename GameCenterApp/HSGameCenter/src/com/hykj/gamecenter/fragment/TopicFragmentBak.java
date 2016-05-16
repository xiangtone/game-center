
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.GeneralAppAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.PAGE_SIZE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.entry.ISaveInfo;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.ICSLoadingViewListener;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TopicFragmentBak extends BaseFragment implements IFragmentInfo {
    private static final String TAG = "TopicAppFragment";
    private View mainView;
    private CSLoadingUIListView mListView;
    private GeneralAppAdapter mAdapter;
    private final ArrayList<GroupElemInfo> mAppList = new ArrayList<GroupElemInfo>();
    private static final int GROUP_ID_DEFAULT_VALUE = -1;
    private final int mGroupId = GROUP_ID_DEFAULT_VALUE;
    private int mCurrentPage = 1;
    private boolean isFooterPullEnable = true;

    private Context mContext;

    private boolean hasLoadData = false;
    private boolean isLoading = true;
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {

                case Msg.GET_DATA:
                    if (mAdapter != null && mListView != null) {

                        Log.d(TAG,
                                "handleMessage    mAppList.size " + mAppList.size());
                        mAdapter.appendData((List<GroupElemInfo>) msg.obj, false);
                        /*
                         * if( ( (List< GroupElemInfo >)msg.obj ).size( ) <
                         * PAGE_SIZE.TOPIC_LIST ) { mListView.setFooterPullEnable(
                         * false ); isFooterPullEnable = false; }
                         */
                        mListView.stopFooterRefresh();
                        mCurrentPage++;
                        isLoading = false;

                        break;
                    }

                case Msg.REQUEST_DATA:
                    reqAppList();
                    break;
                case Msg.REFRESH_LIST:
                    mAdapter.notifyDataSetChanged();
                    break;
                case Msg.NET_ERROR:
                    isLoading = false;
                    if (mAdapter != null && mAdapter.isEmpty()) {
                        mAdapter.notifyDataSetInvalidated();
                        break;
                    }
                    if (mListView != null) {
                        mListView.stopFooterRefresh();

                        if (mContext != null)
                            CSToast.show(App.getAppContext(),
                                    mContext.getString(R.string.error_msg_net_fail));
                    }
                    break;

                case Msg.LOADING:
                    isLoading = true;
                    break;
                case Msg.VIEW_CHANGE:
                    onConfige();
                    break;
                case Msg.REFRESH_ADAPTER:
                    if (mAdapter != null) {
                        mAdapter.setDisplayImage(true);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case Msg.LAST_PAGE:
                    if (mListView != null) {
                        mListView.setFooterPullEnable(false);
                        isFooterPullEnable = false;
                        mListView.stopFooterRefresh();
                        mListView.hideLoadingUI();
                        CSToast.show(mContext,
                                mContext.getString(R.string.tip_last_page));
                    }
                    break;
                case Msg.APPEND_DATA:
                    if (GlobalConfigControllerManager.getInstance()
                            .getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE)
                        break;
                    if (mListView != null && mAppList.size() <= 0) {
                        mListView.initRequestData();
                    } else {
                        LogUtils.d(" onCreateView   mAppList.size : "
                                + mAppList.size());
                        mAdapter.appendData(mAppList, false);
                    }
                    break;
                case Msg.UPDATE_STATE:
                    Log.i(TAG, "Topic UPDATE_STATE");
                    getDataList();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

        /*
         * 用此类时需要去掉 if (activity != null) ((HomePageActivity)
         * activity).setmTopicAppFragment(this);
         */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        LogUtils.d("owenli onCreate");
        getSaveState(savedInstanceState);
        super.onCreate(savedInstanceState);
        GlobalConfigControllerManager.getInstance().registForUpdate(mHandler,
                Msg.UPDATE_STATE, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContext = getActivity();

        // TODO Auto-generated method stub
        LogUtils.d("owenli onCreateView");
        mainView = inflater.inflate(R.layout.topic_app_fragment, null);

        mListView = (CSLoadingUIListView) mainView.findViewById(R.id.app_list);

        if (mAdapter == null)
            mAdapter = new GeneralAppAdapter(getActivity(), MAIN_TYPE.RANKING,
                    UITools.getColumnNumber(mContext), false);
        // else
        // mAdapter.removeAllData( );

        mAdapter.setAppPosType(ReportConstants.STATIS_TYPE.RANKING);
        mAdapter.showSnapShot(false);

        mListView.setFooterPullEnable(isFooterPullEnable);
        mListView.setHeaderPullEnable(false);
        mListView.setCSListViewListener(mCSListViewListener);
        mListView.setCSLoadingViewListener(mCSLoadingViewListener);
        mListView.setAdapter(mAdapter);
        showLoadingState();
        // sendAppendDataMsg( null );
        getDataList();
        return mainView;
    }

    private void getSaveState(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            ArrayList<GroupElemInfo> appList = (ArrayList<GroupElemInfo>) savedInstanceState
                    .getSerializable(ISaveInfo.GROUP_ELEM_INFO);
            mAppList.clear();
            mAppList.addAll(appList);
            isFooterPullEnable = savedInstanceState.getBoolean(
                    ISaveInfo.IS_FOOTER_PULL, true);
            mCurrentPage = savedInstanceState.getInt(ISaveInfo.CURRENT_PAGE, 0);
        }
    }

    @Override
    public void onConfigurationChanged(
            android.content.res.Configuration newConfig) {
        LogUtils.d("owenli onConfigurationChanged() ");
        if (mAdapter != null) {
            mAdapter.setColumnCount(UITools.getColumnNumber(mContext));
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onResume() {
        LogUtils.d("owenli onConfigurationChanged() ");
        // TODO Auto-generated method stub
        super.onResume();
        mAdapter.setDisplayImage(true);
        mAdapter.notifyDataSetChanged();

        mHandler.sendEmptyMessageDelayed(Msg.REFRESH_ADAPTER, 100);
    }

    private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {
        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadMore() {
            reqAppList();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        // isActive = false;
    }

    // @Override
    // public void onStop()
    // {
    // mAdapter.setDisplayImage( false );
    // super.onStop( );
    // }

    public void refreshAdapter() {
        if (mHandler != null)
            mHandler.sendEmptyMessage(Msg.REFRESH_ADAPTER);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestory");
        super.onDestroy();
        GlobalConfigControllerManager.getInstance().unregistForUpdate(mHandler);
    }

    private void onConfige() {
        mAdapter.setColumnCount(UITools.getColumnNumber(mContext));
        mAdapter.notifyDataSetChanged();
    }

    private void reqAppList() {
        int ints[] = DatabaseUtils.getGroupIdByDB(
                GROUP_TYPE.ALL_APP_AND_GAME_TYPE, ORDER_BY.AUTO);
        LogUtils.d("ints[0] = " + ints[0] + " ints[1] = " + ints[1]
                + " ints[2] = " + ints[2] + " ints[3] = " + ints[3]);
        ReqGroupElemsListController controller = new ReqGroupElemsListController(
                ints[0], ints[1], ints[2], ORDER_BY.AUTO, PAGE_SIZE.TOPIC_LIST,
                mCurrentPage, mAppElemsListener);
        controller.setClientPos(ReportConstants.STAC_APP_POSITION_RANK_GAME);
        // 云指令跳转渠道号设置
        // controller.setChnNo( ( (HomePageActivity)getActivity( ) ).getChnNo( )
        // );
        controller.doRequest();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        if (mAppList != null)
            outState.putSerializable(ISaveInfo.GROUP_ELEM_INFO, mAppList);
        outState.putInt(ISaveInfo.CURRENT_PAGE, mCurrentPage);
        outState.putBoolean(ISaveInfo.IS_FOOTER_PULL, isFooterPullEnable);
    }

    private final ReqGroupElemsListener mAppElemsListener = new ReqGroupElemsListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "mReqWebGameElemsListener,onNetError errCode:"
                    + errCode + ",errorMsg:" + errorMsg);
            mHandler.sendEmptyMessage(Msg.NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub
            Logger.e(TAG, "mReqWebGameElemsListener,onReqFailed statusCode:"
                    + statusCode + ",errorMsg:" + errorMsg);
            mHandler.sendEmptyMessage(Msg.NET_ERROR);
        }

        @Override
        public void onReqGroupElemsSucceed(GroupElemInfo[] infoList,
                String serverDataVer) {
            // TODO Auto-generated method stub
            Logger.d(TAG, "mGroupId --- " + mGroupId);
            Logger.d(TAG, "infoList.size()= " + infoList.length);
            if (infoList.length > 0) {
                mAppList.addAll(Tools.arrayToList(infoList));
                Message msg = Message.obtain();
                msg.what = Msg.GET_DATA;
                msg.obj = infoList;
                mHandler.sendMessage(msg);
            } else {
                mHandler.sendEmptyMessage(Msg.LAST_PAGE);
            }
        }
    };

    private final ICSLoadingViewListener mCSLoadingViewListener = new ICSLoadingViewListener() {

        @Override
        public void onRetryRequestData() {
            showLoadingState();
            int global = GlobalConfigControllerManager.getInstance()
                    .getLoadingState();
            switch (global) {
                case GlobalConfigControllerManager.NONETWORK_STATE:
                    GlobalConfigControllerManager.getInstance().reqGlobalConfig();
                    Log.e(TAG, "game retry config");
                    break;
                case GlobalConfigControllerManager.NORMAL_STATE:
                    reqAppList();
                    break;
                default:
                    break;
            }
            /*
             * if( GlobalConfigControllerManager.getInstance( ).getLoadingState(
             * ) == GlobalConfigControllerManager.NONETWORK_STATE ){
             * GlobalConfigControllerManager.getInstance( ).reqGlobalConfig( );
             * } else mHandler.sendEmptyMessage( Msg.REQUEST_DATA );
             */
        }

        @Override
        public void onInitRequestData() {
            if (GlobalConfigControllerManager.getInstance().getLoadingState() == GlobalConfigControllerManager.NONETWORK_STATE)
                GlobalConfigControllerManager.getInstance().reqGlobalConfig();
            else
                mHandler.sendEmptyMessage(Msg.REQUEST_DATA);
        }
    };

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    private synchronized void getDataList() {
        UITools.notifyStateChange(this);
    }

    @Override
    public void setHasLoadedData(boolean loaded) {
        hasLoadData = loaded;
    }

    @Override
    public boolean hasLoadedData() {
        return hasLoadData;
    }

    @Override
    public void initFragmentListData() {
        mListView.initRequestData();
        showLoadingState();
    }

    @Override
    public boolean isLoading() {
        return isLoading || mListView.isShowLoading();
    }

    private void showLoadingState() {
        mListView.showLoadingUI();
        UITools.checkLoadingState(this);
    }

    @Override
    public String getFragmentTabLabel() {
        // TODO Auto-generated method stub
        return IFragmentInfo.FragmentTabLabel.TOPIC_LABEL;
    }
}
