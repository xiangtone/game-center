
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.SubjectAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.GROUP_CLASS;
import com.hykj.gamecenter.controller.ProtocolListener.ITEM_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ORDER_BY;
import com.hykj.gamecenter.controller.ProtocolListener.SUB_TYPE;
import com.hykj.gamecenter.data.GroupInfo;
import com.hykj.gamecenter.data.TopicInfo;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.GroupInfoColumns;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.DownloadStateViewCustomization;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.ICSLoadingViewListener;
import com.hykj.gamecenter.ui.widget.SubjectImageView;
import com.hykj.gamecenter.utils.Interface.IDownloadTaskCountChangeListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.hykj.gamecenter.utils.UITools;
import com.hykj.gamecenter.utils.UpdateUtils;

import java.util.ArrayList;

public class GroupListActivity extends Activity implements
        IDownloadTaskCountChangeListener {
    private static final String TAG = "GroupListActivity";

    public final static String KEY_TITLE = "title";

    private static final int CLASSIFY_GROUP_NUMBER_LANDSCAPE = 2;
    private static final int CLASSIFY_GROUP_NUMBER_PORTRAIT = 1;
    private CSCommonActionBar mActionBar;
    private CSLoadingUIListView mListView;
    private SubjectAdapter mAdapter;
    private Context context;
    private ApkDownloadManager mApkDownloadManager;
    private LayoutInflater mLayoutInflater;
    private ApkInstalledManager mApkInstalledManager;
    private SubjectImageView currentSubject;
    private TextView subjectTitle;

    private static int mGroupClass = GROUP_CLASS.SUBJECT_CLASS; // 默认显示专题分类
    private ArrayList<GroupInfo> mGroupInfoList = new ArrayList<GroupInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = App.getAppContext();
        mApkDownloadManager = DownloadService.getDownloadManager();
        mApkInstalledManager = ApkInstalledManager.getInstance();
        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String title = getIntent().getStringExtra(KEY_TITLE);

        setContentView(R.layout.activity_group_list);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);

        mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar);
        mActionBar.SetOnActionBarClickListener(actionBarListener);

        mListView = (CSLoadingUIListView) findViewById(R.id.app_list);

        /*
         * View headerView =
         * mLayoutInflater.inflate(R.layout.subject_list_header, null);
         * currentSubject = (SubjectImageView) headerView
         * .findViewById(R.id.current_subject); subjectTitle = (TextView)
         * headerView.findViewById(R.id.item_title);
         * currentSubject.setOnClickListener(mListener);
         * mListView.addListHeaderView(headerView);
         */

        mListView.setFooterPullEnable(false);
        mListView.setHeaderPullEnable(false);
        mListView.setCSListViewListener(mCSListViewListener);
        mListView.setCSLoadingViewListener(mCSLoadingViewListener);

        mAdapter = new SubjectAdapter(context, getColumn());
        mListView.setAdapter(mAdapter);

        mActionBar.setTitle(title);
        if (savedInstanceState != null) {
            mGroupInfoList = (ArrayList<GroupInfo>) savedInstanceState
                    .getSerializable("GROUP_INFO");
        }

        if (mGroupInfoList.size() <= 0) {
            // 默认显示专题分类
            mGroupClass = getIntent().getIntExtra(KEY.GROUP_CLASS,
                    GROUP_CLASS.SUBJECT_CLASS);
            Logger.i(TAG, "mGroupClass = " + mGroupClass);
            mListView.initRequestData();
        } else {
            mUiHandler.sendEmptyMessage(MSG_GET_DATA);
        }
    }

    private int getColumn() {
        return UITools.isPortrait() ? CLASSIFY_GROUP_NUMBER_PORTRAIT
                : CLASSIFY_GROUP_NUMBER_LANDSCAPE;
    }

    private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {

        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadMore() {
            reqDataList();
        }
    };

    private final OnClickListener mListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            if (UITools.isFastDoubleClick())

                return;
            GroupInfo info;

            info = mGroupInfoList.get(0);

            Intent intentAppList = new Intent(GroupListActivity.this,
                    GroupAppListActivity.class);
            intentAppList.putExtra(KEY.ORDERBY, info.orderType);
            intentAppList.putExtra(KEY.GROUP_ID, info.groupId);
            intentAppList.putExtra(KEY.GROUP_CLASS, info.groupClass);
            intentAppList.putExtra(KEY.GROUP_TYPE, info.groupType);

            intentAppList.putExtra(KEY.CATEGORY_NAME,
                    getString(R.string.topic_game_label));
            intentAppList.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.TOPIC);
            intentAppList.putExtra(KEY.ITEM_TYPE, ITEM_TYPE.UNSHOW_SNAPSHOT);
            intentAppList.putExtra(StatisticManager.APP_POS_TYPE,
                    ReportConstants.STATIS_TYPE.SUBJECT);

            TopicInfo topicInfo = new TopicInfo();
            topicInfo.mAppCount = info.recommWrod;
            topicInfo.mTopic = info.groupName;
            topicInfo.mTip = info.groupDesc;
            topicInfo.mPicUrl = info.groupPicUrl;

            intentAppList.putExtra(KEY.TOPIC_INFO, topicInfo);

            intentAppList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentAppList);

        }
    };

    private final ICSLoadingViewListener mCSLoadingViewListener = new ICSLoadingViewListener() {

        @Override
        public void onRetryRequestData() {
            mUiHandler.sendEmptyMessage(MSG_REQUEST_DATA);
        }

        @Override
        public void onInitRequestData() {
            mUiHandler.sendEmptyMessage(MSG_REQUEST_DATA);
        }
    };

    // 查询指定类型的分组信息(mGroupClass)
    private void reqDataList() {
        mGroupInfoList.clear();
        Cursor classifyCursor = this.getContentResolver().query(
                CSACContentProvider.GROUPINFO_CONTENT_URI, null,
                GroupInfoColumns.GROUP_CLASS + "=" + mGroupClass/*
                                                                 * GROUP_CLASS.
                                                                 * SUBJECT_CLASS
                                                                 */, null,
                " start_time desc , order_no desc");
        if (classifyCursor != null && classifyCursor.moveToNext()) {
            while (!classifyCursor.isAfterLast()) {
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.groupId = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_ID));
                groupInfo.groupClass = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_CLASS));
                groupInfo.groupType = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_TYPE));
                groupInfo.orderType = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.ORDER_TYPE));
                groupInfo.orderNo = classifyCursor.getInt(classifyCursor
                        .getColumnIndex(GroupInfoColumns.ORDER_NO));
                groupInfo.recommWrod = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.RECOMM_WORD));
                groupInfo.groupName = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_NAME));
                groupInfo.groupDesc = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_DESC));
                groupInfo.groupPicUrl = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.GROUP_PIC_URL));
                groupInfo.startTime = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.START_TIME));
                groupInfo.endTime = classifyCursor.getString(classifyCursor
                        .getColumnIndex(GroupInfoColumns.END_TIME));
                mGroupInfoList.add(groupInfo);
                classifyCursor.moveToNext();
            }
            classifyCursor.close();
        }
        mUiHandler.sendEmptyMessage(MSG_GET_DATA);
    }

    @Override
    public void onResume() {
        super.onResume();
        mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
        mApkDownloadManager.registerListener(apkDownloadHandler);
        mAdapter.notifyDataSetChanged();
        refreshActionBarTip();
    }

    @Override
    public void onPause() {
        super.onPause();
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);
        mApkDownloadManager.unregisterListener(apkDownloadHandler);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // 注册下载任务个数变化监听
        DownloadStateViewCustomization.addDownloadTaskCountChangeListener(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        // 反注册下载任务个数变化监听
        DownloadStateViewCustomization
                .removeDownloadTaskCountChangeListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mAdapter.setColumnCount(getColumn());
        mAdapter.notifyDataSetChanged();
        super.onConfigurationChanged(newConfig);
    }

    private static final int MSG_REQUEST_DATA = 1000;
    private static final int MSG_GET_DATA = 1001;
    private static final int MSG_LAST_PAGE = 1002;
    private static final int MSG_NET_ERROR = 2000;
    public static final int MSG_SHOW_TOAST = 2001;
    public static final int MSG_REFRESH_LIST = 2003;

    private final Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REQUEST_DATA:
                    reqDataList();
                    break;
                case MSG_GET_DATA: {
                    GroupInfo info;
                    if (mAdapter != null && mListView != null) {
                        Logger.i(TAG,
                                "mGroupInfoList.size = " + mGroupInfoList.size());
                        /*
                         * //第一个不在列表中显示，而显示在headview中 if (mGroupInfoList.size() > 0)
                         * { info = mGroupInfoList.get(0);
                         * ImageLoader.getInstance().displayImage( info.groupPicUrl,
                         * currentSubject, DisplayOptions.optionSubject);
                         * subjectTitle.setText(info.groupName); }
                         */
                        mAdapter.addData(mGroupInfoList);
                        mAdapter.notifyDataSetChanged();
                    }

                    break;
                }
                case MSG_NET_ERROR:
                    if (mAdapter != null && mAdapter.isEmpty()) {
                        mAdapter.notifyDataSetInvalidated();
                        break;
                    }
                    if (mListView != null) {
                        // mListView.stopFooterRefresh();
                        CSToast.show(context,
                                context.getString(R.string.error_msg_net_fail));
                    }
                    break;
                case MSG_LAST_PAGE:
                    if (mListView != null) {
                        // mListView.stopFooterRefresh();
                        CSToast.show(context,
                                context.getString(R.string.tip_last_page));
                    }
                    break;
                case MSG_SHOW_TOAST:
                    CSToast.show(context, (String) msg.obj);
                    break;
                case MSG_REFRESH_LIST:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("GROUP_INFO", mGroupInfoList);
        super.onSaveInstanceState(outState);
    }

    public Handler apkDownloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CONSTANTS.MSG_DOWNLOAD_STATE_CHANGE: {
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    };

    private final Handler mUpdateRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case Msg.REFRESH_HEADBAR:
                    refreshActionBarTip();
                    break;
                case MSG_CONSTANTS.MSG_CHECK_UPDATE: {
                    mAdapter.notifyDataSetChanged();
                    refreshActionBarTip();
                    break;
                }
                case MSG_CONSTANTS.MSG_CHECK_TOAST: {
                    CSToast.show(GroupListActivity.this, (String) msg.obj);
                    break;
                }
            }
        }
    };

    private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, View view) {

            switch (position) {
                case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;
                case CSCommonActionBar.OnActionBarClickListener.MANAGE_BNT:
                    Intent intentManage = new Intent(GroupListActivity.this,
                            HomePageActivity.class);
                    intentManage.putExtra(HomePageActivity.KEY_SELECT_ITEM, HomePageActivity.PAGE_INDEX.INDEX_UPDATE);
                    startActivity(intentManage);
                    break;
                case CSCommonActionBar.OnActionBarClickListener.SETTING_BNT:
                    Intent intentSetting = new Intent(GroupListActivity.this,
                            SettingListActivity.class);
                    startActivity(intentSetting);
                    break;
                case CSCommonActionBar.OnActionBarClickListener.SEARCH_BNT: {
                    Intent intentSearch = new Intent(GroupListActivity.this,
                            SearchActivity.class);
                    intentSearch.putExtra(KEY.MAIN_TYPE, MAIN_TYPE.ALL);
                    intentSearch.putExtra(KEY.SUB_TYPE, SUB_TYPE.ALL);
                    intentSearch.putExtra(KEY.ORDERBY, ORDER_BY.DOWNLOAD);
                    startActivity(intentSearch);
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void refreshActionBarTip() {
        mActionBar.setSettingTipVisible(UpdateUtils.hasUpdate() ? View.VISIBLE
                : View.GONE);
        mActionBar.setManageTipVisible(
                mApkInstalledManager.hasUpdateOrDownload(),
                mApkDownloadManager.getTaskCount()
                        + mApkInstalledManager.getUpdateInfoNotInTaskCount());
    }

    @Override
    public void onDownloadTaskCountChange() {
        // TODO Auto-generated method stub

        mUpdateRefreshHandler.sendEmptyMessage(Msg.REFRESH_HEADBAR);// 刷新下载图标个数
    }
}
