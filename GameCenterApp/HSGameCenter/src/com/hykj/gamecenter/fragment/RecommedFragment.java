
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.GroupAppListActivity;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.activity.PhoneAppInfoActivity;
import com.hykj.gamecenter.adapter.MixGridAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.ELEMENT_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.HOME_PAGE_POSITION;
import com.hykj.gamecenter.controller.ProtocolListener.ITEM_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.logic.entry.ISaveInfo;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.mta.MTAConstants;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.HomeCirculeRecommendAdvView;
import com.hykj.gamecenter.ui.HomeCirculeRecommendAdvView.CirculeAdvListener;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecommedFragment extends BaseFragment implements OnClickListener,
        CirculeAdvListener, IFragmentInfo {
    private static final String TAG = "RecommedFragment";

    private View mainView = null;
    private final List<GroupElemInfo> mGroupElemInfo = new ArrayList<GroupElemInfo>();
    private final ArrayList<GroupElemInfo> listInfos = new ArrayList<GroupElemInfo>(); // home界面list的数据

    // 广告位ID和数据对应的map
    private final SparseArray<GroupElemInfo> mPosToAppinfoMap = new SparseArray<GroupElemInfo>();

    private View headView = null;

    // 3个独立的广告位
    private HomeCirculeRecommendAdvView mHomeAdvView = null;

    private CSLoadingUIListView pUIListView = null;
    private boolean isFooterPullEnable = true;
    // public GroupGridAdapter homeListAdapter = null;
    public MixGridAdapter homeListAdapter;
    // 异常界面
    private View mLoadingFrame;
    private View mLoadingView;
    private View mNoNetworkView;
    private boolean isLoading = true;

    private Context mContext;

    // 推荐页传入的hander 用于处理加载完首页，如果有未安装或者下载的任务，弹出提示操作
    private Handler mHandler;
    private int mCurrentPage = 1;
    private int mCirculeIndex = 0;

    private boolean hasLoadData = false;

    public int getRecommendAdvCount() {
        return mHomeAdvView.getRecommendAdvCount();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;

        /*if (activity != null)
        	((HomePageActivity) activity).setmRecommedFragment(this);*/

    }

    // 放数据的请求
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate()");

        if (homeListAdapter == null) {
            homeListAdapter = new MixGridAdapter(mContext, MAIN_TYPE.APP,
                    UITools.getColumnNumber(mContext));
        }
        homeListAdapter.setAppPosType(ReportConstants.STATIS_TYPE.RECOM);
        GlobalConfigControllerManager.getInstance().registForUpdate(
                mUiDownLoadHandler, Msg.UPDATE_STATE, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        /*
         * setContentView()一旦调用, layout就会立刻显示UI；
         * 而inflate只会把Layout形成一个以view类实现成的对象， 有需要时再用setContentView(view)显示出来。
         * 一般在activity中通过setContentView()将界面显示出来， 但是如果在非activity中如何对控件布局设置操作了，
         * 这就需要LayoutInflater动态加载。
         */
        Logger.i(TAG, "onCreateView()");
        mainView = inflater.inflate(R.layout.activity_home_page_content,
                container, false);

        headView = inflater.inflate(
                R.layout.home_recommend_topic_top2, null, false);

        getSaveState(savedInstanceState);
        // 初始化异常界面
        initExceptionView();
        // home界面list view初始化
        initListView();
        initCirculeAdvView();
        initGroupHeadView();
        setDisplayStatus(LOADING_STATUS);
        getDataList();
        return mainView;

    }

    private void getSaveState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            List<GroupElemInfo> infos;
            infos = (List<GroupElemInfo>) savedInstanceState
                    .getSerializable(ISaveInfo.GROUP_ELEM_INFO);
            mGroupElemInfo.clear();
            mGroupElemInfo.addAll(infos);
            isFooterPullEnable = savedInstanceState.getBoolean(
                    ISaveInfo.IS_FOOTER_PULL, true);
            mCurrentPage = savedInstanceState.getInt(ISaveInfo.CURRENT_PAGE, 1);
            mCirculeIndex = savedInstanceState
                    .getInt(ISaveInfo.RECOMMED_CIRCLE_INDEX);
            savedInstanceState.clear();
        }
    }


    @Override
    public void onResume() {
        Logger.i(TAG, "onResume()");
        super.onResume();
        // 将banner的开始放到onResume
        if (mGroupElemInfo != null && mGroupElemInfo.size() > 0) {
            // changeView( );
            homeListAdapter.notifyDataSetChanged();
            if (!mHomeAdvView.isCricle()) {
                mHomeAdvView.onReply();
            }
        }
        getFocusable(mainView); // 获取焦点，防止自动跳到list view首项
    }

    @Override
    public void onPause() {
        Logger.i(TAG, "onPause");
        super.onPause();
        stopCircle();

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

    public void stopCircle() {
        if (null != mHomeAdvView) {
            mHomeAdvView.stopCricule();
        }
    }

    public void startCircle() {
        if (null != mHomeAdvView) {
            mHomeAdvView.onReply();
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (View.VISIBLE == mNoNetworkView.getVisibility()) {
            mUiDownLoadHandler.sendEmptyMessage(Msg.RELOAD_PAGE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Logger.i(TAG, "onDestroy()");
        if (null != mPosToAppinfoMap) {
            mPosToAppinfoMap.clear();
        }
        if (null != listInfos) {
            listInfos.clear();
        }
        GlobalConfigControllerManager.getInstance().unregistForUpdate(
                mUiDownLoadHandler);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListInfosData(List<GroupElemInfo> infos) {
        listInfos.clear();
        for (GroupElemInfo info : infos) {
            // info.getPosId() 为 1的是广告位
            if (info.posId >= 2) {
                listInfos.add(info);
            }
        }
    }

    private void pushData() {
        homeListAdapter.appendData(listInfos);
        homeListAdapter.notifyDataSetChanged();
    }

    /**
     * 让控件获取焦点
     * 
     * @param view
     */
    private void getFocusable(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
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
                mUiDownLoadHandler.sendEmptyMessage(Msg.RELOAD_PAGE);
            }
        });
    }

    private void setCirculeDataToAdvView(ArrayList<GroupElemInfo> carouselData) {
        if (carouselData.size() == 0)
            return;
        mHomeAdvView.scrollViewAddData(carouselData,
                mHomeAdvView.getCurScreen());
        mHomeAdvView.setCurScreen(mCirculeIndex);
        mHomeAdvView.onReply();
    }

    private void initCirculeAdvView() {
        if (mHomeAdvView == null)
            mHomeAdvView = new HomeCirculeRecommendAdvView(headView,
                    App.getAppContext(), getFragmentTabLabel());
        mHomeAdvView.setCurScreen(mCirculeIndex);
        mHomeAdvView.addAdvListener(this);

    }

    private void initGroupHeadView() {
        TextView title = (TextView) headView.findViewById(R.id.category_title);
        Button more = (Button) headView.findViewById(R.id.more);
        more.setVisibility(View.VISIBLE);
        title.setText(App.getAppContext().getString(R.string.nice_app_label));

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupAppListActivity.class);
                intent.putExtra(KEY.CATEGORY_NAME,
                        mContext.getString(R.string.nice_app_label));
                int ints[] = DatabaseUtils.getGroupIdByDB(
                        GROUP_TYPE.NICE_APP_TYPE, ORDER_BY.AUTO);
                LogUtils.d("GROUP_ID = " + ints[0] + "GROUP_CLASS = " + ints[1]
                        + "GROUP_TYPE = " + ints[2] + "ORDERBY = " + ints[3]);
                intent.putExtra(KEY.GROUP_ID, ints[0]);
                intent.putExtra(KEY.GROUP_CLASS, ints[1]);
                intent.putExtra(KEY.GROUP_TYPE, ints[2]);
                intent.putExtra(KEY.ORDERBY, ints[3]);
                // intent.putExtra( KEY.ORDERBY , groupInfo.orderType );
                intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.GAME_CLASS);
                // 统计位置
                intent.putExtra(StatisticManager.APP_POS_TYPE,
                        ReportConstants.STATIS_TYPE.RECOM_NICE);
                mContext.startActivity(intent);
            }
        });
    }

    private void initListView() {
        pUIListView = (CSLoadingUIListView) mainView
                .findViewById(R.id.home_app_list);
        // 给listview设置头
        pUIListView.addListHeaderView(headView);
        pUIListView.setHeaderDividersEnabled(false);
        pUIListView.setCSListViewListener(mCSListViewListener);
        pUIListView.setFooterPullEnable(isFooterPullEnable);
        pUIListView.setHeaderPullEnable(false);
        pUIListView.setAdapter(homeListAdapter);

    }

    private final Handler mUiDownLoadHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Msg.RELOAD_PAGE:

                    setDisplayStatus(LOADING_STATUS);
                    int global = GlobalConfigControllerManager.getInstance()
                            .getLoadingState();
                    switch (global) {
                        case GlobalConfigControllerManager.NONETWORK_STATE:
                            GlobalConfigControllerManager.getInstance()
                                    .reqGlobalConfig();
                            Log.e(TAG, "game retry config");
                            break;
                        case GlobalConfigControllerManager.NORMAL_STATE:
                            reqAppList();
                            break;
                        default:
                            break;
                    }
                    /*
                     * if (GlobalConfigControllerManager.getInstance()
                     * .getLoadingState() ==
                     * GlobalConfigControllerManager.NONETWORK_STATE) {
                     * GlobalConfigControllerManager.getInstance()
                     * .reqGlobalConfig(); Log.e(TAG, "retry config"); } else
                     * reqAppList();
                     */

                    break;
                case Msg.GET_DATA:
                    Logger.i(TAG, "----- Msg.GET_DATA");
                    // 界面显示
                    setDisplayStatus(NORMAL_STATUS);
                    // 设置广告为的数据
                    LogUtils.e("设置广告位中数据");
                    setCirculeDataToAdvView(getCarouselData((List<GroupElemInfo>) msg.obj));
                    setListInfosData((List<GroupElemInfo>) msg.obj);
                    // 设置数据
                    pushData();
                    // 推荐页数据加载完成后,发送消息给HomePageActivity
                    if (null != mHandler && null != msg.obj && mCurrentPage == 1) {
                        // 应用商店本身更新查询
                        mHandler.sendEmptyMessage(HomePageActivity.MSG_NOTE_TO_HANDLE_DOWNLOAD_TASK);
                    }
                    pUIListView.stopFooterRefresh();
                    mCurrentPage++;

                    break;
                case Msg.SHOW_TOAST:
                    String msgStr = (String) msg.obj;
                    CSToast.show(getActivity(), msgStr);
                    break;
                case Msg.NET_ERROR:
                    Logger.i(TAG, "MSG_NET_ERROR");
                    if (homeListAdapter != null && homeListAdapter.isEmpty()) {
                        setDisplayStatus(NO_NETWORK_STATUS);
                        break;
                    }
                    if (pUIListView != null) {
                        pUIListView.stopFooterRefresh();
                        CSToast.show(mContext,
                                mContext.getString(R.string.error_msg_net_fail));
                    }
                    break;
                case Msg.LOADING:
                    break;
                case Msg.REFRESH_ADAPTER:
                    if (homeListAdapter != null) {
                        // homeListAdapter.setDisplayImage( true );
                        homeListAdapter.notifyDataSetChanged();
                    }
                    break;

                case Msg.LAST_PAGE:
                    if (pUIListView != null) {
                        pUIListView.setFooterPullEnable(false);
                        isFooterPullEnable = false;
                        pUIListView.stopFooterRefresh();
                        setDisplayStatus(NORMAL_STATUS);
                        CSToast.show(mContext,
                                mContext.getString(R.string.tip_last_page));
                    }
                    break;
                case Msg.APPEND_DATA:
                    Log.i(TAG,
                            "GlobalConfigControllerManager.getInstance( ).getLoadingState( ) != GlobalConfigControllerManager.NORMAL_STATE"
                                    + (GlobalConfigControllerManager.getInstance()
                                            .getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE));
                    if (GlobalConfigControllerManager.getInstance()
                            .getLoadingState() != GlobalConfigControllerManager.NORMAL_STATE)
                        break;

                    Log.e(TAG, "recommend append data");
                    if (null != mGroupElemInfo && mGroupElemInfo.size() > 0) {
                        setDisplayStatus(NORMAL_STATUS);
                        setCirculeDataToAdvView(getCarouselData(mGroupElemInfo));
                        setListInfosData(mGroupElemInfo);
                        // 设置数据
                        pushData();
                        LogUtils.d("infos is not null");
                    } else {
                        Log.e(TAG, "request list");
                        reqAppList();
                        LogUtils.d("infos is null");
                    }
                    break;
                case Msg.UPDATE_STATE:
                    Log.i(TAG, "UPDATE_STATE");
                    getDataList();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onConfigurationChanged(
            android.content.res.Configuration newConfig) {
        Logger.i(TAG, "onConfigurationChanged() ");
        if (homeListAdapter != null) {
            homeListAdapter.setColumnCount(UITools.getColumnNumber(mContext));
        }
        mHomeAdvView.getHandler().sendEmptyMessage(
                HomeCirculeRecommendAdvView.MSG_HOME_REFRESH);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Logger.i(TAG, "onSaveInstanceState");
        if (mGroupElemInfo != null)
            outState.putSerializable(ISaveInfo.GROUP_ELEM_INFO,
                    (ArrayList<GroupElemInfo>) mGroupElemInfo);
        if (mHomeAdvView != null)
            outState.putSerializable(ISaveInfo.RECOMMED_CIRCLE_INDEX,
                    mHomeAdvView.getCurScreen());
        outState.putInt(ISaveInfo.CURRENT_PAGE, mCurrentPage);
        outState.putBoolean(ISaveInfo.IS_FOOTER_PULL, isFooterPullEnable);
        super.onSaveInstanceState(outState);
    }

    private ArrayList<GroupElemInfo> getCarouselData(List<GroupElemInfo> infos) {
        ArrayList<GroupElemInfo> carouseData = new ArrayList<GroupElemInfo>();
        for (GroupElemInfo info : infos) {
            if (HOME_PAGE_POSITION.CAROUSEL_POSITION == info.posId) {
                carouseData.add(info);
            }
        }
        LogUtils.d("carouseData.size : " + carouseData.size());
        // 将轮播按orderNo进行排序
        Collections.sort(carouseData, new Comparator<GroupElemInfo>() {

            @Override
            public int compare(GroupElemInfo lhs, GroupElemInfo rhs) {
                return lhs.orderNo - rhs.orderNo;
            }

        });
        return carouseData;
    }

    private static final int NORMAL_STATUS = 0;
    private static final int LOADING_STATUS = 1;
    private static final int NO_NETWORK_STATUS = 2;

    private void setDisplayStatus(int flag) {
        if (pUIListView == null) {
            return;
        }
        switch (flag) {
            case NORMAL_STATUS:
                pUIListView.setVisibility(View.VISIBLE);
                mLoadingFrame.setVisibility(View.GONE);
                mNoNetworkView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                isLoading = false;
                break;

            case LOADING_STATUS:
                pUIListView.setVisibility(View.GONE);
                mLoadingFrame.setVisibility(View.VISIBLE);
                mNoNetworkView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                isLoading = true;
                UITools.checkLoadingState(this);
                break;

            case NO_NETWORK_STATUS:
                pUIListView.setVisibility(View.GONE);
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
    public void onClick(View v) {
    }

    private void advOnClick(GroupElemInfo info, int position) {
        int appPosType = 0;
        switch (position) {
            case 1:
                appPosType = ReportConstants.STATIS_TYPE.RECOM_ADV;
                break;
            // case 2 :
            // appPosType = STATIS_TYPE.RECOMADV2;
            // break;
            // case 3 :
            // appPosType = STATIS_TYPE.RECOMADV3;
            // break;
            default:
                break;
        }

        if (info == null) {
            return;
        }

        int posId = position + ReportConstants.STAC_APP_POSITION_RECOMM_PAGE;
        int orderIndex = mHomeAdvView.getCurScreen() + 1; // 因为后台从一开始计算的，而客户端是从零开始的
        int elemId = 0;
        int type = info.elemType;
        Intent intent = new Intent();
        Log.d(TAG, "ElemType =" + type);
        switch (type) {
            case ELEMENT_TYPE.TYPE_APP:
                intent.setClass(
                        getActivity(),PhoneAppInfoActivity.class
                        /*App.getDevicesType() == App.PHONE ? PhoneAppInfoActivity.class
                                : PadAppInfoActivity.class*/);
                //                AppsGroupElemInfoParcelable infos = new AppsGroupElemInfoParcelable(info);
                intent.putExtra(KEY.GROUP_INFO, info);
                intent.putExtra(KEY.APP_POSITION, position);
                intent.putExtra(KEY.ADV_ENTRY, true);
                intent.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
                intent.putExtra(MTAConstants.KEY_DETAIL_PAGE_FROM,
                        MTAConstants.DETAIL_RECOMMEND_PAGE_CERCLE_ADV + orderIndex);
                elemId = info.appId;
                break;

            case ELEMENT_TYPE.TYPE_LINK:
                String link = info.jumpLinkUrl;
                Uri web = Uri.parse(link);
                intent = new Intent(Intent.ACTION_VIEW, web);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                elemId = info.jumpLinkId;
                break;

            case ELEMENT_TYPE.TYPE_SKIP_LOCAL_OR_ONLINE:
            case ELEMENT_TYPE.TYPE_SKIP_TIP:
            case ELEMENT_TYPE.TYPE_SKIP_CLASS:
                intent = jumpToTopicOrClass(intent, info, type, appPosType);
                elemId = info.jumpGroupId;
                break;

            // case ELEMENT_TYPE.TYPE_SKIP_LOCAL_OR_ONLINE :
            // intent.setClass( App.getAppContext( ) , .class );
            // intent.putExtra( AppListActivity.LIST_TYPE_KEY , getGroupIDByDB(
            // info ) );
            // break;
            default:
                break;
        }
        // 广告位点击上报
        ReportedInfo build = new ReportedInfo();
        build.statActId = ReportConstants.STATACT_ID_ADV;
        build.statActId2 = 1;   //首页元素
        build.ext1 = posId+ "";
        build.ext2 = type+ "";
        build.ext3 = elemId+ "";
        build.ext4 = 1+ "";     //showType 1 广告位, 0 表示其他
        build.ext5 = orderIndex + "";
        build.actionTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        ReportConstants.getInstance().reportReportedInfo(build);
        MtaUtils.trackAdvClick(true, "CircleAdv " + orderIndex);
        startActivity(intent);
    }

    @Override
    public void onCirCuleAdvListener(int i, GroupElemInfo info) {
        Log.d(TAG, "onCirCuleAdvListener");
        Logger.i(TAG, "i = " + i);
        if (info == null) {
            return;
        }
        // 推荐页广告栏1
        advOnClick(info, 1);
    }

    private Intent jumpToTopicOrClass(Intent intent, GroupElemInfo info,
            int type, int appPosType) {
        Logger.i(TAG, "type = " + type + "---appPosType=" + appPosType);
        // if( type == ELEMENT_TYPE.TYPE_SKIP_TIP )
        // {
        // intent.putExtra( KEY.MAIN_TYPE , MAIN_TYPE.TOPIC );
        // }
        // else if( type == ELEMENT_TYPE.TYPE_SKIP_CLASS )
        // {
        // intent.putExtra( KEY.MAIN_TYPE , MAIN_TYPE.GAME_CLASS );
        // }
        if (info.jumpGroupId > 0) {
            GroupInfo mGroupInfo = new GroupInfo();
            mGroupInfo = getGroupIDByDB(info);

            TopicInfo topicInfo = new TopicInfo();
            topicInfo.mAppCount = mGroupInfo.recommWrod;
            topicInfo.mTopic = mGroupInfo.groupName;
            topicInfo.mTip = mGroupInfo.groupDesc;
            topicInfo.mPicUrl = mGroupInfo.groupPicUrl;

            intent.putExtra(KEY.TOPIC_INFO, topicInfo);

            intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);
            intent.putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);

            intent.putExtra(KEY.GROUP_ID, mGroupInfo.groupId);
            intent.putExtra(KEY.GROUP_CLASS, mGroupInfo.groupClass);
            intent.putExtra(KEY.GROUP_TYPE, mGroupInfo.groupType);
            intent.putExtra(KEY.ORDERBY, getGroupIDByDB(info).orderType);
            intent.putExtra(KEY.CATEGORY_NAME,
                    mContext.getString(R.string.topic_game_label));
        } else {
            intent.putExtra(KEY.GROUP_ID, getGroupIDByDB(info).groupId);
            intent.putExtra(KEY.GROUP_CLASS, getGroupIDByDB(info).groupClass);
            intent.putExtra(KEY.GROUP_TYPE, getGroupIDByDB(info).groupType);
            intent.putExtra(KEY.ORDERBY, getGroupIDByDB(info).orderType);
            intent.putExtra(KEY.CATEGORY_NAME, getGroupIDByDB(info).groupName);
            intent.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.JING_PIN);
        }

        intent.putExtra(KEY.ORDERBY, info.jumpGroupType);
        intent.putExtra(StatisticManager.APP_POS_TYPE, appPosType);
        intent.setClass(App.getAppContext(), GroupAppListActivity.class);

        return intent;
    }

    private GroupInfo getGroupIDByDB(GroupElemInfo info) {
        GroupInfo groupInfo = new GroupInfo();
        Cursor cursor = App
                .getAppContext()
                .getContentResolver()
                .query(CSACContentProvider.GROUPINFO_CONTENT_URI,
                        null,
                        GroupInfoColumns.GROUP_TYPE + " =? and "
                                + GroupInfoColumns.GROUP_ID + " =?",
                        new String[] {
                                info.jumpGroupType + "",
                                info.jumpGroupId + ""
                        }, null);
        if (cursor != null && cursor.moveToNext()) {
            while (!cursor.isAfterLast()) {
                groupInfo.groupId = cursor.getInt(cursor
                        .getColumnIndex(GroupInfoColumns.GROUP_ID));
                groupInfo.groupClass = cursor.getInt(cursor
                        .getColumnIndex(GroupInfoColumns.GROUP_CLASS));
                groupInfo.groupType = cursor.getInt(cursor
                        .getColumnIndex(GroupInfoColumns.GROUP_TYPE));
                groupInfo.groupName = cursor.getString(cursor
                        .getColumnIndex(GroupInfoColumns.GROUP_NAME));
                groupInfo.recommWrod = cursor.getString(cursor
                        .getColumnIndex(GroupInfoColumns.RECOMM_WORD));
                groupInfo.groupDesc = cursor.getString(cursor
                        .getColumnIndex(GroupInfoColumns.GROUP_DESC));
                groupInfo.groupPicUrl = cursor.getString(cursor
                        .getColumnIndex(GroupInfoColumns.GROUP_PIC_URL));
                cursor.moveToNext();
            }
        }
        if (null != cursor) {
            cursor.close();
        }
        Logger.i(TAG, "groupId = " + groupInfo);
        return groupInfo;
    }

    private void reqAppList() {
        int ints[] = DatabaseUtils.getGroupIdByDB(
                GROUP_TYPE.HOME_RECOMMED_TYPE, ORDER_BY.AUTO);
        LogUtils.d("ints[0] = " + ints[0] + " ints[1] = " + ints[1]
                + " ints[2] = " + ints[2] + " ints[3] = " + ints[3]);

        // 请求第mCurrentPage页的数据
        ReqGroupElemsListController controller = new ReqGroupElemsListController(
                ints[0], ints[1], ints[2], ints[3],
                HomePageActivity.REQ_PAGE_SIZE, mCurrentPage,
                mReqGroupElemsListener);
        // 云指令跳转渠道号设置
        // controller.setChnNo( ( (HomePageActivity)getActivity( ) ).getChnNo( )
        // );
        controller.doRequest();
    }

    private final ReqGroupElemsListener mReqGroupElemsListener = new ReqGroupElemsListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e(TAG, "onNetError:" + errorMsg + ",errCode:" + errCode);
            mUiDownLoadHandler.sendEmptyMessage(Msg.NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e(TAG, "errorMsg:" + errorMsg + ",statusCode:" + statusCode);
            mUiDownLoadHandler.sendEmptyMessage(Msg.NET_ERROR);
        }

        @Override
        public void onReqGroupElemsSucceed(GroupElemInfo[] infoList,
                String serverDataVer) {
            // 将infoList按照Position ID 进行排序
            if (infoList != null && infoList.length > 0) {
                Logger.i(TAG, "onReqGroupElemsSucceed:" + infoList.length);
                mGroupElemInfo.addAll(Tools.arrayToList(infoList));
                Message msg = Message.obtain();
                msg.what = Msg.GET_DATA;
                msg.obj = infoList;
                mUiDownLoadHandler.sendMessage(msg);
            } else {
                mUiDownLoadHandler.sendEmptyMessage(Msg.LAST_PAGE);
            }
        }
    };

    @Override
    public Handler getHandler() {
        return mUiDownLoadHandler;
    }

    public void addHandler(Handler handler) {
        mHandler = handler;
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
        reqAppList();
    }

    @Override
    public boolean isLoading() {
        return mLoadingView.isShown();
    }

    @Override
    public String getFragmentTabLabel() {
        // TODO Auto-generated method stub
        return IFragmentInfo.FragmentTabLabel.RECOM_LABEL;
    }
}
