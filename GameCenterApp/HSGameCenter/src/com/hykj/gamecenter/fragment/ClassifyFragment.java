
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.activity.SearchActivity;
import com.hykj.gamecenter.activity.SettingListActivity;
import com.hykj.gamecenter.adapter.ClassifyExpandListAdapter;
import com.hykj.gamecenter.controller.ProtocolListener;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_CLASS;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.entry.ISaveInfo;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.protocol.Reported;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.SearchBarCommon;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;

public class ClassifyFragment extends BaseFragment implements IFragmentInfo {
    private static final String TAG = "ClassifyFragment";

    private View mainView = null;
    private Activity mActivity;

    private final ArrayList<GroupInfo> mAppGroupInfoList = new ArrayList<GroupInfo>();
    private final ArrayList<GroupInfo> mGameGroupInfoList = new ArrayList<GroupInfo>();

    private ExpandableListView mListView;
    private ClassifyExpandListAdapter mAdapter;

    private View mLoadingFrame;
    private View mLoadingView;
    private View mNoNetworkView;
    private boolean hasLoadData = false;
    private boolean isLoading = true;

    private final OnGroupClickListener mOnGroupClickListener = new OnGroupClickListener() {

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            return true;
        }
    };
    private SearchBarCommon mSearchBarCommon;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

        if (activity != null)
            ((HomePageActivity) activity).setmClassifyFragment(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConfigControllerManager.getInstance().registForUpdate(mHandler,
                Msg.UPDATE_STATE, null);

        LogUtils.d("owenli onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d("owenli onCreateView()");
        mActivity = getActivity();
        mainView = inflater.inflate(R.layout.activity_classify_list, container,
                false);
        initActionBar(mainView);
        initListView();
        initExceptionView();
        setDisplayStatus(LOADING_STATUS);
        getSaveState(savedInstanceState);
        getDataList();
        return mainView;
    }

    @Override
    public void onConfigurationChanged(
            android.content.res.Configuration newConfig) {
        LogUtils.d("owenli onConfigurationChanged() ");
        if (mAdapter != null) {
            mAdapter.setColumnCount(UITools.getColumnNumber(mActivity));
        }
        super.onConfigurationChanged(newConfig);
    }

    private void getSaveState(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            ArrayList<GroupInfo> appGroupInfoList = (ArrayList<GroupInfo>) savedInstanceState
                    .getSerializable(ISaveInfo.APP_CLASSIFY);
            ArrayList<GroupInfo> gameGroupInfoList = (ArrayList<GroupInfo>) savedInstanceState
                    .getSerializable(ISaveInfo.GAME_CLASSIFY);
            mAppGroupInfoList.clear();
            mGameGroupInfoList.clear();
            mAppGroupInfoList.addAll(appGroupInfoList);
            mGameGroupInfoList.addAll(gameGroupInfoList);
        }
    }
    private void initActionBar(View rootView) {
        mSearchBarCommon = (SearchBarCommon)rootView.findViewById(R.id.searchBarCommon);
        mSearchBarCommon.setSearchBarCommonListen(new SearchBarCommon.SearchBarCommonListen() {
            @Override
            public void clickSearch(View view) {
                //进入search界面进行上报
                // 上报进入个人中心
                Reported.ReportedInfo reportBuilder = new Reported.ReportedInfo();
                reportBuilder.statActId = ReportConstants.STATACT_ID_PAGE_VISIT;
                reportBuilder.statActId2 = 5;//搜索
                ReportConstants.getInstance().reportReportedInfo(reportBuilder);

                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                intentSearch.putExtra(ProtocolListener.KEY.MAIN_TYPE, MAIN_TYPE./*GAME_CLASS*/ALL);
                intentSearch.putExtra(ProtocolListener.KEY.SUB_TYPE, ProtocolListener.SUB_TYPE.ALL);
                intentSearch.putExtra(ProtocolListener.KEY.ORDERBY, ProtocolListener.ORDER_BY.DOWNLOAD);
                startActivity(intentSearch);
            }

            @Override
            public void clickSettingImage(ImageView imageView) {
                Intent intentSetting = new Intent(getActivity(),
                        SettingListActivity.class);
                startActivity(intentSetting);
            }
        });
    }
    private void initListView() {
        mListView = (ExpandableListView) mainView.findViewById(R.id.app_list);
        //分类每行个数根据屏幕横竖屏*2
        mAdapter = new ClassifyExpandListAdapter(mActivity,
                ReportConstants.STATIS_TYPE.CLASSIFY, UITools.getColumnNumber(mActivity)*2);
        mAdapter.addAppTypeList(MAIN_TYPE.APP_CLASS);

        mAdapter.addAppTypeList(MAIN_TYPE.GAME_CLASS);
        if (!App.ismAllGame()) {
            mAdapter.addGroupName(App.getAppContext().getResources()
                    .getString(R.string.app_classify_title).trim());
        }
        mAdapter.addGroupName(App.getAppContext().getResources()
                .getString(R.string.game_classify_title).trim());

        mListView.setAdapter(mAdapter);
        /*
         * for(int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
         * expandableListView.expandGroup(i); }
         */
        // 实现ExpandableListView进入以后默认展开
        mListView.expandGroup(0);
        if (!App.ismAllGame()) {
            mListView.expandGroup(1);
        }
        mListView.setOnGroupClickListener(mOnGroupClickListener);
    }

    private void initExceptionView() {
        // loading
        mLoadingFrame = mainView.findViewById(R.id.loading_interface);
        mLoadingView = mLoadingFrame.findViewById(R.id.csl_cs_loading);
        mNoNetworkView = mLoadingFrame
                .findViewById(R.id.csl_cs_listview_no_networking);

        Button settingbutton = (Button) mainView
                .findViewById(R.id.csl_network_setting_btn);
        settingbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toActivity = new Intent(Settings.ACTION_WIFI_SETTINGS);
                toActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toActivity);
            }
        });

        Button button = (Button) mainView
                .findViewById(R.id.csl_network_retry_btn);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int global = GlobalConfigControllerManager.getInstance()
                        .getLoadingState();
                setDisplayStatus(LOADING_STATUS);
                switch (global) {
                    case GlobalConfigControllerManager.NONETWORK_STATE:
                        GlobalConfigControllerManager.getInstance()
                                .reqGlobalConfig();
                        Log.e(TAG, "game retry config");
                        break;
                    case GlobalConfigControllerManager.NORMAL_STATE:
                        DatabaseUtils.reqDataList(mGameGroupInfoList,
                                GROUP_CLASS.GAME_CLASSIFY_CLASS,
                                GROUP_TYPE.ALL_ONLY_GAMES_TYPE);
                        DatabaseUtils.reqDataList(mAppGroupInfoList,
                                GROUP_CLASS.APP_CLASSIFY_CLASS,
                                GROUP_TYPE.ALL_ONLY_APP_TYPE);
                        break;
                    default:
                        break;
                }
                /*
                 * if( GlobalConfigControllerManager.getInstance(
                 * ).getLoadingState( ) ==
                 * GlobalConfigControllerManager.NONETWORK_STATE ) {
                 * GlobalConfigControllerManager.getInstance( ).reqGlobalConfig(
                 * ); Log.e(TAG, "classify retry config"); } else { reqDataList(
                 * mGameGroupInfoList , GROUP_CLASS.GAME_CLASSIFY_CLASS ,
                 * GROUP_TYPE.ALL_GAMES_TYPE ); reqDataList( mAppGroupInfoList ,
                 * GROUP_CLASS.APP_CLASSIFY_CLASS , GROUP_TYPE.ALL_ONLY_APP_TYPE
                 * ); }
                 */
            }
        });
    }

    private static final int NORMAL_STATUS = 0;
    private static final int LOADING_STATUS = 1;
    private static final int NO_NETWORK_STATUS = 2;

    private void setDisplayStatus(int flag) {
        if (mListView == null) {
            return;
        }
        switch (flag) {
            case NORMAL_STATUS:
                mListView.setVisibility(View.VISIBLE);
                mLoadingFrame.setVisibility(View.GONE);
                mNoNetworkView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                isLoading = false;
                break;

            case LOADING_STATUS:
                mListView.setVisibility(View.GONE);
                mLoadingFrame.setVisibility(View.VISIBLE);
                mNoNetworkView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                isLoading = true;
                UITools.checkLoadingState(this);
                break;

            case NO_NETWORK_STATUS:
                mListView.setVisibility(View.GONE);
                mLoadingFrame.setVisibility(View.VISIBLE);
                mNoNetworkView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
                isLoading = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ISaveInfo.APP_CLASSIFY, mAppGroupInfoList);
        outState.putSerializable(ISaveInfo.GAME_CLASSIFY, mGameGroupInfoList);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onResume() {
        /*
         * if(GlobalConfigControllerManager.getInstance().isHasLoaded()){
         * getDataList(); }
         */
        LogUtils.d("owenli onResume()");
        super.onResume();
        mSearchBarCommon.setSettingTipVisible(UpdateUtils.hasUpdate() ? View.VISIBLE
                : View.GONE);
    }

    @Override
    public void onDestroyView() {
        Logger.i("ClassifyFragment", "onDestoryView");
        super.onDestroy();
    }

    @Override
    public void onDestroy() {
        Logger.i("ClassifyFragment", "onDestory");
        GlobalConfigControllerManager.getInstance().unregistForUpdate(mHandler);
        super.onDestroy();
    }

    /**
     * 获取横竖屏需要保存的数据
     */
    /*
     * public ArrayList<GroupInfo> saveInfo() { return mAppGroupInfoList; }
     */

    // private boolean isActive = true;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case Msg.NET_ERROR:
                    if (mAppGroupInfoList.size() <= 0
                            || mGameGroupInfoList.size() <= 0) {
                        setDisplayStatus(NO_NETWORK_STATUS);
                    }
                    break;
                case Msg.LOADING:
                    break;
                case Msg.APPEND_DATA:
                    if (GlobalConfigControllerManager.getInstance()
                            .getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE)
                        break;
                    if (mAppGroupInfoList.size() <= 0
                            || mGameGroupInfoList.size() <= 0) {
                        DatabaseUtils.reqDataList(mGameGroupInfoList,
                                GROUP_CLASS.GAME_CLASSIFY_CLASS,
                                GROUP_TYPE.ALL_ONLY_GAMES_TYPE);
                        DatabaseUtils.reqDataList(mAppGroupInfoList,
                                GROUP_CLASS.APP_CLASSIFY_CLASS,
                                GROUP_TYPE.ALL_ONLY_APP_TYPE);
                    }
                    setDisplayStatus(NORMAL_STATUS);
                    mAdapter.removeAllData();
                    if (!App.ismAllGame()) {
                        mAdapter.appendAppList(mAppGroupInfoList);
                    }
                    mAdapter.appendGameList(mGameGroupInfoList);
                    mAdapter.notifyDataSetChanged();
                    LogUtils.d(/*
                                * "Owenli 4 app list" + mAppGroupInfoList.size()
                                * +
                                */" game list" + mGameGroupInfoList.size());
                    break;
                case Msg.UPDATE_STATE:
                    Log.i(TAG, "Classify UPDATE_STATE");
                    getDataList();
                    break;
                default:
                    break;
            }
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
        //这个方法没有被调用------oddshou，继承自baseFragment可以全部都删除
//        reqDataList(mGameGroupInfoList, GROUP_CLASS.GAME_CLASSIFY_CLASS,
//                GROUP_TYPE.ALL_ONLY_GAMES_TYPE);
//        reqDataList(mAppGroupInfoList, GROUP_CLASS.APP_CLASSIFY_CLASS,
//                GROUP_TYPE.ALL_ONLY_APP_TYPE);
//        setDisplayStatus(NORMAL_STATUS);
//        mAdapter.removeAllData();
//        if (!App.ismAllGame()) {
//        mAdapter.appendAppList(mAppGroupInfoList);
//        }
//        mAdapter.appendGameList(mGameGroupInfoList);
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean isLoading() {
        return mLoadingView.isShown();
    }

    @Override
    public String getFragmentTabLabel() {
        // TODO Auto-generated method stub
        return IFragmentInfo.FragmentTabLabel.CLASSIFY_LABEL;
    }
}
