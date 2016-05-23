
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.ImproperDetailAdapter;
import com.hykj.gamecenter.logic.entry.AuthorityInfo;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSLoadingView;
import com.hykj.gamecenter.utils.AuthorityPullParser;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ImproperDetailActivity extends Activity {
    protected static final String TAG = "ImproperDetailActivity";
    private CSCommonActionBar mActionBar;
    private Resources mRes = null;
    private GridView mGridView;
    private ImproperDetailHandler mHandler;
    protected static LinkedHashMap<Integer, Boolean> mCheckBoxStatusMap = new LinkedHashMap<Integer, Boolean>();
    public final static int DATA_PARSE_FINISH = 1000;
    private ArrayList<Integer> list;

    private CSLoadingView mCSLoading;
    private ListView mListView;
    private ArrayList<String> mPermissions = new ArrayList<String>();
    private AuthorityPullParser parser;
    private List<AuthorityInfo> infos;
    private ImproperDetailAdapter mAdapter;
    private View emptyView;

    public class ImproperDetailHandler extends Handler {
        WeakReference<ImproperDetailActivity> activity;

        public ImproperDetailHandler(ImproperDetailActivity acti) {
            // TODO Auto-generated constructor stub
            activity = new WeakReference<ImproperDetailActivity>(acti);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            ImproperDetailActivity mActivity = activity.get();
            if (null == mActivity) {
                Logger.d(TAG, "mActivity == null");
                return;
            }
            switch (msg.what) {
                case DATA_PARSE_FINISH:
                    setAdapter(fitData());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mRes = getResources();
        setContentView(R.layout.improper_detail);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mHandler = new ImproperDetailHandler(ImproperDetailActivity.this);
        mPermissions = getIntent().getStringArrayListExtra(StatisticManager.APP_PERMISSION);
        initView();
        //        if (null == mPermissions)
        if (null == mPermissions || mPermissions.size() == 0) {
            //展示空界面
            mCSLoading.hide();
            mListView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            return;
        } else {
            mListView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            Logger.i(TAG, "mPermissions == " + mPermissions);
        }
        getDetailData();
    }

    private void initView() {
        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null)
        {
            mActionBar.SetOnActionBarClickListener(actionBarListener);
        }
        mActionBar.setTitle(mRes.getString(R.string.improper_detail));

        mCSLoading = (CSLoadingView) findViewById(R.id.cs_loading);
        //        mCSLoading.setOnRetryListener(mICSListViewLoadingRetry);

        mListView = (ListView) findViewById(R.id.app_list);
        emptyView = findViewById(R.id.download_expandlist_empty);
    }

    private void getDetailData() {
        mCSLoading.showLoading();//先loading，等数据
        try {
            InputStream is = getAssets().open("authoritycontrol.xml");
            //          parser = new SaxBookParser();  
            //          parser = new DomBookParser();  
            parser = new AuthorityPullParser();
            infos = parser.parse(is);
            /* for (AuthorityInfo info : infos) {
                 Logger.i("tom", info.toString());
             }*/
        } catch (Exception e) {
            Logger.i("TAG", e.getMessage());
        }
        Logger.i(TAG, "infos == " + infos);
        mHandler.sendEmptyMessage(DATA_PARSE_FINISH);
    }

    private List<AuthorityInfo> fitData() {
        List<AuthorityInfo> fitInfos = new ArrayList<AuthorityInfo>();
        for (int i = 0; i < mPermissions.size(); i++) {
            String str = mPermissions.get(i);
            Logger.i(TAG, "str == " + str);
            for (int j = 0; j < infos.size(); j++) {
                AuthorityInfo info = infos.get(j);
                if (info.getFlag().equals(str)) {
                    fitInfos.add(info);
                }
            }
        }
        Logger.i(TAG, "fitInfos == " + fitInfos);
        return fitInfos;
    }

    private void setAdapter(List<AuthorityInfo> infos) {
        if (mAdapter == null) {
            mAdapter = new ImproperDetailAdapter(getApplicationContext(), infos);
        }
        mCSLoading.hide();
        mListView.setAdapter(mAdapter);
    }

    /*private final ICSListViewLoadingRetry mICSListViewLoadingRetry = new ICSListViewLoadingRetry() {

        @Override
        public void onRetry() {
           
        }

    };*/

    private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, View view) {

            switch (position) {
                case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;
                default:
                    break;
            }
        }
    };

}
