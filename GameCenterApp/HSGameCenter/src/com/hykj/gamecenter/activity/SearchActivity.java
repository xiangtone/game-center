
package com.hykj.gamecenter.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.AdapterSearchHot;
import com.hykj.gamecenter.adapter.AdapterSearchHot.HotWordClickListen;
import com.hykj.gamecenter.adapter.GeneralAppAdapter;
import com.hykj.gamecenter.adapter.HotSearchAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.PAGE_SIZE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqAppList4SearchKeyListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.controller.ProtocolListener.SUB_TYPE;
import com.hykj.gamecenter.controller.ReqAppList4SearchKeyController;
import com.hykj.gamecenter.controller.ReqGroupElemsListController;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.HotWortdsColumns;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTaskStateListener;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.SearchRecommendControllerManager;
import com.hykj.gamecenter.logic.SearchRecommendControllerManager.LoadingStateListener;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.widget.CSActionBar;
import com.hykj.gamecenter.ui.widget.CSActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSActionBar.OnSearchEditTextWatcherListener;
import com.hykj.gamecenter.ui.widget.CSPullListView;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.ICSLoadingViewListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends CSLoadingActivity implements HotWordClickListen,
        OnSearchEditTextWatcherListener {
    private CSActionBar mActionBar;
    private CSPullListView mResultListView;

    private View mSearchMainView;
    private View mLoadingFrame;
    private GeneralAppAdapter mResultAdapter;

    private Context context;
    private ApkDownloadManager mApkDownloadManager;

    private static final int HOT_SEARCH_GROUP_NUMBER_LANDSCAPE = 4;
    private static final int HOT_SEARCH_GROUP_NUMBER_PORTRAIT = 3;
    private static final int HOT_WORDS_NUMBER = 12;

    private static final int SEARCH_RECOMMEND = 0;
    private static final int SEARCH_RESULT = 1;
    private static final String SEARCH_STRING = "search_string";
    private static final String SEARCH_RESULT_LIST = "search_result_list";
    private static final String CURRENT_POSITION = "current_positon";
    protected static final String TAG = "SearchActivity";

    // 获取应用的页数，第一页和后台确认为1
    private int mCurrentPage = 1;
    private int mAppType;
    private int mAppSubType;
    private int mOrderBy;
    private int mCurrentPositon = SEARCH_RECOMMEND;

    private ApkInstalledManager mApkInstalledManager;

    private static final int RESULT_FROM_ACTION_SEARCH = 0x01;
    private static final int RESULT_FROM_ACTION_LOADMORE = 0x02;
    private int mResultFromAction;

    private View mSearchLayout;
    private View mNoResultView;
    private TextView mNoResultTxt;
    private String mCurSearchString;
    private boolean isHotKey = false;

    private ArrayList<GroupElemInfo> mSearchResult = new ArrayList<GroupElemInfo>();
    private ArrayList<GroupElemInfo> mSearchHotList = new ArrayList<GroupElemInfo>();

    private HotSearchAdapter mSearchRecommendAdapter;
    private ListView mSearchRecommendListView;

    private SearchRecommendControllerManager mSearchRecommendControllerManager;

    View searchFrame;
    private GridView mGridView;
    private AdapterSearchHot mAdapterSearchHot;
    /**
     * 搜索热词页
     */
    private View mLayoutSearchHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LogUtils.d("Owenli search onCreate");
        setContentView(R.layout.activity_search, R.layout.search_actionbar);

        context = App.getAppContext();
        mApkDownloadManager = DownloadService.getDownloadManager();
        mApkInstalledManager = ApkInstalledManager.getInstance();

        View headerView = getHeaderView();
        mActionBar = (CSActionBar) headerView.findViewById(R.id.ActionBar);
        mActionBar.SetOnActionBarClickListener(actionBarListener);
        mActionBar.setmOnSearchEditTextWatcherListener(this);

        mSearchLayout = findViewById(R.id.search_layout);

        searchFrame = findViewById(R.id.search_frame);
        // searchFrame.setOnClickListener(mBackListener);
        // no result UI
        mNoResultView = findViewById(R.id.no_result);
        mNoResultView.setVisibility(View.GONE);

        mNoResultTxt = (TextView) findViewById(R.id.txt_no_result);

        // search main
        mSearchMainView = findViewById(R.id.search_interface);

        mSearchRecommendAdapter = new HotSearchAdapter(this);
        mSearchRecommendAdapter.setHandler(mUiHandler);
        mSearchRecommendAdapter
                .setColumnCount(UITools.isPortrait() ? HOT_SEARCH_GROUP_NUMBER_PORTRAIT
                        : HOT_SEARCH_GROUP_NUMBER_LANDSCAPE);

        mSearchRecommendListView = (ListView) mSearchMainView
                .findViewById(R.id.search_recommend_list);
        View footerContainer = LayoutInflater.from(context).inflate(
                R.layout.search_recommend_interface_footer, null);
        mSearchRecommendListView.addFooterView(footerContainer);// 必须放在setAdapter之前
        mSearchRecommendListView.setAdapter(mSearchRecommendAdapter);
        mSearchRecommendListView.setVisibility(View.GONE);

        mLayoutSearchHot = findViewById(R.id.search_hot);

        // 初始化 热词
        // 1.从数据库中读取热词, 如果没有 则 使用默认 的 热词
        List<String> hotWordsList = new ArrayList<String>();
        hotWordsList = queryHotWords(hotWordsList);
        if (hotWordsList.size() < HOT_WORDS_NUMBER) {
            hotWordsList = Arrays.asList(getResources().getStringArray(
                    R.array.hot_words_list));
        }
        mSearchHotList.clear();
        for (String string : hotWordsList) {
            GroupElemInfo groupInfo = new GroupElemInfo();
            groupInfo.showName = string;
            mSearchHotList.add(groupInfo);
        }
        // 搜索热词 gridView
        mGridView = (GridView) findViewById(R.id.gridView);
        if (App.getDevicesType() == App.PHONE){
            mGridView.setNumColumns(3);
        }else {
            mGridView.setNumColumns(4);
        }
        mAdapterSearchHot = new AdapterSearchHot(this, mSearchHotList);
        mGridView.setAdapter(mAdapterSearchHot);
        mAdapterSearchHot.setHotWordClickListen(this);

        mResultListView = (CSPullListView) findViewById(R.id.app_list);
        mResultListView.setFooterPullEnable(false);
        mResultListView.setHeaderPullEnable(false);
        mResultListView.setCSListViewListener(mCSListViewListener);
        mResultAdapter = new GeneralAppAdapter(this, MAIN_TYPE.SEARCH,
                UITools.getColumnNumber(this), false);
        mResultAdapter.showSnapShot(false);
        mResultAdapter.setAppPosType(ReportConstants.STATIS_TYPE.SEARCH);
        mResultAdapter.setDisplayImage(true);
        mResultListView.setAdapter(mResultAdapter);

        mAppType = getIntent().getIntExtra(KEY.MAIN_TYPE, MAIN_TYPE./*GAME_CLASS*/ALL);
        mAppSubType = getIntent().getIntExtra(KEY.SUB_TYPE, SUB_TYPE.ALL);
        mOrderBy = getIntent().getIntExtra(KEY.ORDERBY, ORDER_BY.AUTO);

        mSearchRecommendControllerManager = SearchRecommendControllerManager
                .getInstance();
        mSearchRecommendControllerManager
                .setLoadingStateListener(mLoadingStateListener);

        if (savedInstanceState != null) {
            /*
             * mSearchResult = (ArrayList<GroupElemInfo>) savedInstanceState
             * .getSerializable(SEARCH_RESULT_LIST);
             */
            mCurrentPositon = savedInstanceState.getInt(CURRENT_POSITION);
            mCurSearchString = savedInstanceState.getString(SEARCH_STRING);
        }

        if (mCurrentPositon == SEARCH_RESULT) {
            if (mCurSearchString != null) {

                Message msgSetEditText = Message.obtain();
                msgSetEditText.what = MSG_SEARCH_EDITOR_TEXT;
                msgSetEditText.obj = mCurSearchString;
                mUiHandler.sendMessage(msgSetEditText);

                if (mSearchResult != null && mSearchResult.size() != 0) {
                    ArrayList<GroupElemInfo> searchResult = new ArrayList<GroupElemInfo>();
                    searchResult.addAll(mSearchResult);
                    Message msgResultData = Message.obtain();
                    msgResultData.what = MSG_GET_SEARCH_RESULT_DATA;
                    msgResultData.obj = searchResult;
                    mUiHandler.sendMessage(msgResultData);
                } else {
                    doSearch(mCurSearchString);
                }
            }
            // mSearchLayout.setVisibility(View.VISIBLE);
            setLoadingViewBackgroundResource(R.color.background);
            mLayoutSearchHot.setVisibility(View.GONE);
            mResultListView.setVisibility(View.VISIBLE);
        } else {
            cheakSearchRecommendLoadingState();
            // mSearchLayout.setVisibility(View.GONE);
            mLayoutSearchHot.setVisibility(View.VISIBLE);
            mResultListView.setVisibility(View.GONE);

        }
        requestSearchHot();
    }

    private List<String> queryHotWords(List<String> list)
    {
        // mGroupInfoList.clear( );
        list.clear();
        Cursor hotwordsCursor = getContentResolver().query(CSACContentProvider.HOT_WORDS_URI, null,
                null, null, null);
        if (hotwordsCursor != null && hotwordsCursor.moveToNext())
        {
            while (!hotwordsCursor.isAfterLast())
            {
                String hotWords = hotwordsCursor.getString(hotwordsCursor
                        .getColumnIndex(HotWortdsColumns.WORDS));
                list.add(hotWords);
                hotwordsCursor.moveToNext();
            }
            hotwordsCursor.close();
        }

        return list;
    }

    private void cheakSearchRecommendLoadingState() {
        int hotSearchLoadingState = mSearchRecommendControllerManager
                .getHotSearchLoadingState();

        mCurrentPositon = SEARCH_RECOMMEND;

        if (hotSearchLoadingState == SearchRecommendControllerManager.LOADING_STATE) {
            // mUiHandler.sendEmptyMessage( MSG_LOADING );
            setLoadingTipText(getString(R.string.csl_loading));
            setCSLoadingViewListener(mSeachRecommendLoadingListener);
        } else if (hotSearchLoadingState == SearchRecommendControllerManager.NONETWORK_STATE) {
            // mUiHandler.sendEmptyMessage( MSG_MAIN_SEARCH_NET_ERROR );
            setLoadingTipText(getString(R.string.csl_loading));
            setCSLoadingViewListener(mSeachRecommendLoadingListener);
        } else {
            if (hotSearchLoadingState == SearchRecommendControllerManager.NORMAL_STATE) {
                mUiHandler.sendEmptyMessage(MSG_RECOMMEND_RESUTLE);
            }
        }
    }

    private final LoadingStateListener mLoadingStateListener = new LoadingStateListener() {
        @Override
        public void onChange() {
            cheakSearchRecommendLoadingState();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mResultAdapter.setColumnCount(UITools.getColumnNumber(this));
        mResultAdapter.notifyDataSetChanged();

        mSearchRecommendAdapter
                .setColumnCount(UITools.isPortrait() ? HOT_SEARCH_GROUP_NUMBER_PORTRAIT
                        : HOT_SEARCH_GROUP_NUMBER_LANDSCAPE);
        mSearchRecommendAdapter.notifyDataSetChanged();

        super.onConfigurationChanged(newConfig);
    }

    private static final int SS_NORESULT = 0x1001;
    private static final int SS_SEARCH_RESULT = 0x1002;
    private static final int SS_RECOMMEND_RESULT = 0x1005;

    private void setSearchStatus(int nStatus) {
        switch (nStatus) {
            case SS_RECOMMEND_RESULT:
                if (mLoadingFrame != null) {
                    mLoadingFrame.setVisibility(View.GONE);
                }
                if (mNoResultView != null)
                    mNoResultView.setVisibility(View.GONE);
                if (mResultListView != null) {
                    mResultListView.setVisibility(View.GONE);
                }
                break;
            case SS_NORESULT:
                if (mSearchMainView != null)
                    mSearchMainView.setVisibility(View.GONE);
                if (mLayoutSearchHot != null) {
                    mLayoutSearchHot.setVisibility(View.GONE);
                }
                if (mNoResultView != null)
                    mNoResultView.setVisibility(View.VISIBLE);
                mNoResultTxt
                        .setText(String.format(
                                this.getString(R.string.search_no_result),
                                mCurSearchString));
                if (mResultListView != null) {
                    mResultListView.setVisibility(View.GONE);
                }
                break;
            case SS_SEARCH_RESULT:
                if (mLoadingFrame != null) {
                    mLoadingFrame.setVisibility(View.GONE);
                }
                if (mSearchMainView != null)
                    mSearchMainView.setVisibility(View.GONE);
                if (mNoResultView != null)
                    mNoResultView.setVisibility(View.GONE);
                if (mLayoutSearchHot != null) {
                    mLayoutSearchHot.setVisibility(View.GONE);
                }
                if (mResultListView != null) {
                    mResultListView.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }

    private void requestData() {
        ReqAppList4SearchKeyController controller = new ReqAppList4SearchKeyController(
                mCurSearchString, isHotKey ? 0 : 1, mAppType, mAppSubType,
                PAGE_SIZE.SEARCH, mCurrentPage, mOrderBy, mReqAppListListener);
        /*
         * controller
         * .setClientPos(StatisticManager.STAC_APP_POSITION_SEARCH_HOT_WORD_APP
         * );
         */
        controller.doRequest();
    }

    private void requestSearchHot() {
        int ints[] = DatabaseUtils.getGroupIdByDB(
                GROUP_TYPE.SEARCH_HOT_WORDS, ORDER_BY.AUTO);
        Logger.d(TAG, "ints[0] = " + ints[0] + " ints[1] = " + ints[1]
                + " ints[2] = " + ints[2] + " ints[3] = " + ints[3]);
        ReqGroupElemsListController controller = new ReqGroupElemsListController(
                ints[0], ints[1], ints[2], ints[3],
                0, 0,
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
            // mUiDownLoadHandler.sendEmptyMessage(Msg.NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e(TAG, "errorMsg:" + errorMsg + ",statusCode:" + statusCode);
            // mUiDownLoadHandler.sendEmptyMessage(Msg.NET_ERROR);
        }

        @Override
        public void onReqGroupElemsSucceed(GroupElemInfo[] infoList,
                String serverDataVer) {
            Logger.i(TAG, "onReqGroupElemsSucceed:" + infoList.length);
            // 将infoList按照Position ID 进行排序
            if (infoList != null && infoList.length > 0) {
                // mGroupElemInfo.addAll(Tools.arrayToList(infoList));
                Message msg = Message.obtain();
                msg.what = MSG_REQUEST_HOT_WORDS;
                msg.obj = Tools.arrayToList(infoList);
                mUiHandler.sendMessage(msg);
                List<String> hotWordsList = new ArrayList<String>();
                hotWordsList = queryHotWords(hotWordsList);
                if (hotWordsList.size() < HOT_WORDS_NUMBER) {
                    //插入数据
                    for (int i = 0; i < infoList.length; i++) {
                        getContentResolver()
                                .insert(CSACContentProvider.HOT_WORDS_URI,
                                        createContentValues(infoList[i]));
                    }
                } else {
                    //更新 数据库
                    for (int i = 0; i < HOT_WORDS_NUMBER; i++) {
                        getContentResolver()
                                .update(CSACContentProvider.HOT_WORDS_URI,
                                        createContentValues(infoList[i]),
                                        BaseColumns._ID + " = " + i + 1, null);
                    }
                }
            } else {
                // mUiDownLoadHandler.sendEmptyMessage(Msg.LAST_PAGE);
            }
        }

    };

    private ContentValues createContentValues(GroupElemInfo groupElemInfo) {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put(HotWortdsColumns.WORDS, groupElemInfo.showName);
        return values;
    }

    private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {

        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadMore() {
            mResultFromAction = RESULT_FROM_ACTION_LOADMORE;
            mUiHandler.sendEmptyMessage(MSG_REQUEST_DATA);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // outState.putSerializable(SEARCH_RESULT_LIST, mSearchResult);
        outState.putInt(CURRENT_POSITION, mCurrentPositon);
        outState.putString(SEARCH_STRING, mCurSearchString);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
        mApkDownloadManager.registerListener(downloadTaskStateListener);
        mResultAdapter.notifyDataSetChanged();
        // refreshActionBarTip();
    }

    @Override
    public void onPause() {
        super.onPause();
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);
        mApkDownloadManager.unregisterListener(downloadTaskStateListener);
    }

    private final ReqAppList4SearchKeyListener mReqAppListListener = new ReqAppList4SearchKeyListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            LogUtils.e("onNetError errCode:" + errCode + ",errorMsg:"
                    + errorMsg);
            mUiHandler.sendEmptyMessage(MSG_NET_ERROR);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            LogUtils.e("onReqFailed statusCode:" + statusCode + ",errorMsg:"
                    + errorMsg);
            mUiHandler.sendEmptyMessage(MSG_NET_ERROR);
        }

        @Override
        public void onReqAppList4SearchKeySucceed(GroupElemInfo[] infoes) {
            LogUtils.d("infoes.size()=" + infoes.length);

            if (infoes.length <= 0) {
                mUiHandler.sendEmptyMessage(MSG_NO_RESUTLE);
            } else {
                Message msg = Message.obtain();
                msg.what = MSG_GET_SEARCH_RESULT_DATA;
                msg.obj = Tools.arrayToList(infoes);
                mUiHandler.sendMessage(msg);
            }
        }

    };

    private static final int MSG_BASE = 1000;
    private static final int MSG_REQUEST_DATA = MSG_BASE + 1;
    private static final int MSG_GET_SEARCH_RESULT_DATA = MSG_BASE + 2;
    private static final int MSG_LAST_PAGE = MSG_BASE + 3;
    private static final int MSG_NET_ERROR = MSG_BASE + 4;
    private static final int MSG_SHOW_TOAST = MSG_BASE + 5;
    private static final int MSG_REFRESH_LIST = MSG_BASE + 7;
    private static final int MSG_NO_RESUTLE = MSG_BASE + 8;
    public static final int MSG_SEARCH_ITEM = MSG_BASE + 9;
    private static final int MSG_MAIN_SEARCH_NET_ERROR = MSG_BASE + 12;
    public static final int MSG_HISTORY_DATA_LOADED = MSG_BASE + 13;
    public static final int MSG_LOADING = MSG_BASE + 14;
    public static final int MSG_RECOMMEND_RESUTLE = MSG_BASE + 15;
    public static final int MSG_SEARCH_EDITOR_TEXT = MSG_BASE + 16;
    public static final int MSG_REQUEST_HOT_WORDS = MSG_BASE + 17;

    private final Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADING:
                    notifyLoadingState();
                    break;
                case MSG_RECOMMEND_RESUTLE:
                    if (mSearchRecommendAdapter != null) {
                        mSearchRecommendAdapter.clearData();
                        mSearchRecommendAdapter
                                .addData(mSearchRecommendControllerManager
                                        .getHotSearchinfoList());
                        mSearchRecommendAdapter.notifyDataSetChanged();
                    }
                    setSearchStatus(SS_RECOMMEND_RESULT);
                    notifyRequestDataReceived();
                    break;
                case MSG_MAIN_SEARCH_NET_ERROR:
                    notifyRequestDataError();
                    break;
                case MSG_REQUEST_DATA:
                    if (mSearchLayout.getVisibility() != View.VISIBLE) {
                        mSearchLayout.setVisibility(View.VISIBLE);
                        setLoadingViewBackgroundResource(R.color.background);
                    }

                    notifyLoadingState();
                    requestData();
                    break;
                case MSG_GET_SEARCH_RESULT_DATA: {
                    if (mResultAdapter != null && mResultListView != null) {
                        // showListUI();
                        // 由于请求回来的数据没有带页数，所以当网速较慢，连续多次点击搜索，多个结果相继返回时，无法判断
                        // 只能检查是否重复数据 froyohuang 2013.10.21
                        setSearchStatus(SS_SEARCH_RESULT);
                        notifyRequestDataReceived();
                        mSearchResult.clear();
                        mSearchResult.addAll((ArrayList<GroupElemInfo>) msg.obj);
                        if (mResultAdapter.isDuplicateData(mSearchResult)) {
                            mResultAdapter.removeAllData();
                        }
                        mResultAdapter.appendData((ArrayList<GroupElemInfo>) msg.obj,
                                false);
                        mResultAdapter.notifyDataSetChanged();

                        if (((ArrayList<AppInfo>) msg.obj).size() < PAGE_SIZE.SEARCH) {
                            mResultListView.setFooterPullEnable(false);
                        }
                        mResultListView.stopFooterRefresh();
                    }

                    break;
                }
                case MSG_NO_RESUTLE:
                    if (mResultFromAction == RESULT_FROM_ACTION_SEARCH) {
                        setSearchStatus(SS_NORESULT);
                        notifyRequestDataReceived();
                    } else if (mResultFromAction == RESULT_FROM_ACTION_LOADMORE) {
                        mUiHandler.sendEmptyMessage(MSG_LAST_PAGE);
                    }
                    break;

                case MSG_NET_ERROR:
                    if (mResultAdapter != null && mResultAdapter.isEmpty()) {
                        notifyRequestDataError();
                        mResultAdapter.notifyDataSetInvalidated();
                        break;
                    }
                    if (mResultListView != null) {
                        mResultListView.stopFooterRefresh();
                        CSToast.show(context,
                                context.getString(R.string.error_msg_net_fail));
                    }
                    break;
                case MSG_LAST_PAGE:
                    if (mResultListView != null) {
                        mResultListView.setFooterPullEnable(false);
                        mResultListView.stopFooterRefresh();
                        CSToast.show(context,
                                context.getString(R.string.tip_last_page));
                    }
                    break;
                case MSG_SHOW_TOAST:
                    CSToast.show(context, (String) msg.obj);
                    break;
                case MSG_REFRESH_LIST:
                    if (mResultAdapter != null)
                        mResultAdapter.notifyDataSetChanged();
                    break;
                case MSG_SEARCH_ITEM:
                    String searchStr = (String) msg.obj;
                    if (mActionBar != null) {
                        mActionBar.setSearchEditorText(searchStr);
                        mActionBar.hideKeyboard();
                    }
                    isHotKey = true;
                    doSearch(searchStr);
                    break;
                case MSG_SEARCH_EDITOR_TEXT:
                    String str = (String) msg.obj;
                    if (mActionBar != null) {
                        mActionBar.setSearchEditorText(str);
                        mActionBar.setSearchEditorTextFocusable(false);
                    }
                    break;
                case MSG_REQUEST_HOT_WORDS:
                    ArrayList<GroupElemInfo> list = (ArrayList<GroupElemInfo>) msg.obj;
                    mSearchHotList.clear();
                    mSearchHotList.addAll(list);
                    mAdapterSearchHot.notifyDataSetChanged();
                    break;
            }
        }
    };

    private final DownloadTaskStateListener downloadTaskStateListener = new DownloadTaskStateListener() {
        @Override
        public void onUpdateTaskState(DownloadTask task) {
            // mResultAdapter.updateDownloadState( task );
        }

        @Override
        public void onUpdateTaskProgress(DownloadTask task) {
            // mResultAdapter.updateProgress( task.appId , task.packageName ,
            // (int)task.progress , (int)task.fileLength );
        }

        @Override
        public void onUpdateTaskList(Object obj) {
            mResultAdapter.notifyDataSetChanged();
        }
    };

    private final Handler mUpdateRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONSTANTS.MSG_CHECK_UPDATE: {
                    LogUtils.e("SearchActivity, MSG_CHECK_UPDATE");
                    mResultAdapter.notifyDataSetChanged();
                    // refreshActionBarTip();
                    break;
                }
                case MSG_CONSTANTS.MSG_CHECK_TOAST: {
                    CSToast.show(SearchActivity.this, (String) msg.obj);
                    break;
                }
            }
        }
    };

    private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, String strEdit) {

            switch (position) {
                case CSActionBar.OnActionBarClickListener.HOMEUP_BNT:
                    // onBackPressed( );有时会导致运行异常，致使客户端停止运行
                    // 详见http://www.myexception.cn/android/413855.html
                    SearchActivity.this.finish();
                    break;
                case CSActionBar.OnActionBarClickListener.DO_SEARCH_BNT:
                    if (UITools.isFastDoubleClick()) {
                        break;
                    }
                    isHotKey = false;
                    doSearch(strEdit);
                    break;

                case CSActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;

                default:
                    break;
            }
        }
    };

    private void doSearch(String strEdit) {
        mCurSearchString = strEdit;
        mCurrentPositon = SEARCH_RESULT;
        // reset
        mCurrentPage = 1;
        mResultAdapter.removeAllData();

        setLoadingTipText(getString(R.string.searching));
        setCSLoadingViewListener(mSeachResultLoadingListener);
        initRequestData();
        // set status
        // setSearchStatus( SS_SEARCHING );

        // request search data
        // mResultListView.initRequestData( );
    }

    public ICSLoadingViewListener mSeachResultLoadingListener = new ICSLoadingViewListener() {
        @Override
        public void onRetryRequestData() {
            mResultFromAction = RESULT_FROM_ACTION_SEARCH;
            mUiHandler.sendEmptyMessage(MSG_REQUEST_DATA);
        }

        @Override
        public void onInitRequestData() {
            mResultFromAction = RESULT_FROM_ACTION_SEARCH;
            mUiHandler.sendEmptyMessage(MSG_REQUEST_DATA);
        }

    };

    public ICSLoadingViewListener mSeachRecommendLoadingListener = new ICSLoadingViewListener() {
        @Override
        public void onInitRequestData() {
            mSearchRecommendControllerManager.reqConfig();
            notifyLoadingState();
        }

        @Override
        public void onRetryRequestData() {
            mSearchRecommendControllerManager.reqConfig();
            notifyLoadingState();
        }

    };

    // 目标点击空白处返回,加入 搜索热词后 去除
    private final View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (mSearchLayout.isShown())
                return;
            finish();
            // onBackPressed( );
        }
    };

    @Override
    public void onclickHotWord(View v, String word) {
        // TODO Auto-generated method stub
        mActionBar.setSearchEditorText(word);
        mActionBar.hideKeyboard();
        isHotKey = true;
        doSearch(word);
    }

    @Override
    public void beforeSearchEditTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSearchEditTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        if (s.length() <= 0) {
            mLayoutSearchHot.setVisibility(View.VISIBLE);
            mResultListView.setVisibility(View.GONE);
            if (mNoResultView != null)
                mNoResultView.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterSearchEditTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }
}
