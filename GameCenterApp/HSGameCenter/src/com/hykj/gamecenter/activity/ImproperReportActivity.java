
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.App.TimeRun;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.ImproperReportAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.ReqImproperReportListener;
import com.hykj.gamecenter.controller.ReqImproperReportController;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class ImproperReportActivity extends Activity implements TimeRun {
    protected static final String TAG = "ImproperReportActivity";
    private CSCommonActionBar mActionBar;
    private Resources mRes = null;
    private GridView mGridView;
    private ImproperReportAdapter mAdapter;
    private ImproperReportHandler mHandler;
    protected static LinkedHashMap<Integer, Boolean> mCheckBoxStatusMap = new LinkedHashMap<Integer, Boolean>();
    public final static int CHECK_STATE_CHANGE = 1000;
    public final static int REPORT_SUCCEES = 1001;
    public final static int MSG_RSP_ONNETERROR = 1002;
    private int mAppId = -1;
    private EditText edt;
    private Button btn;
    private ArrayList<Integer> list;
    public final static String PREFERENCE_APPID = "improperreportappid";

    public class ImproperReportHandler extends Handler {
        WeakReference<ImproperReportActivity> activity;

        public ImproperReportHandler(ImproperReportActivity acti) {
            // TODO Auto-generated constructor stub
            activity = new WeakReference<ImproperReportActivity>(acti);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            ImproperReportActivity mActivity = activity.get();
            if (null == mActivity) {
                Logger.d(TAG, "mActivity == null");
                return;
            }
            switch (msg.what) {
                case CHECK_STATE_CHANGE:
                    mCheckBoxStatusMap = (LinkedHashMap<Integer, Boolean>) msg.obj;
                    Logger.i(TAG, "mCheckBoxStatusMap == " + mCheckBoxStatusMap);
                    break;
                case REPORT_SUCCEES:
                    //                    startTimer();
                    finishActivity();
                    CSToast.show(mActivity, getApplicationContext().getResources()
                            .getString(R.string.improper_report_result));
                    break;
                case MSG_RSP_ONNETERROR:
                    CSToast.show(mActivity, getApplicationContext().getResources()
                            .getString(R.string.improper_report_neterror));
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
        setContentView(R.layout.improper_report);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mHandler = new ImproperReportHandler(ImproperReportActivity.this);
        mAppId = getIntent().getIntExtra(StatisticManager.APP_ID, -1);
        //        Logger.i(TAG, "mAppId == " + mAppId);
        initView();

        /*int appId = App.mSharePrefences.getInt(PREFERENCE_APPID, -1);
        if (mAppId == appId) {
            App app = App.getAppContext();
            app.setmTimeListen(ImproperReportActivity.this);
            initialCaptchaBtn(app.getmCurrentTimer());
        } else {
            btn.setEnabled(true);
            btn.setTextColor(getResources().getColor(R.color.color_green));
        }*/
    }

    private void startReport() {
        if (mAppId != -1) {
            String informType = "";
            String informDetail = edt.getText().toString().trim();
            list = new ArrayList<Integer>();
            Iterator it = mCheckBoxStatusMap.entrySet().iterator();
            while (it.hasNext()) {
                LinkedHashMap.Entry entry = (LinkedHashMap.Entry) it.next();
                int key = (Integer) entry.getKey();
                boolean val = (Boolean) entry.getValue();
                //                Logger.i(TAG, "key == " + key);
                //                Logger.i(TAG, "val == " + val);
                if (val == true) {
                    list.add(key);
                }
            }
            Logger.i(TAG, "list == " + list.size());
            if (list.size() == 0) {
                informType = "";
            } else {
                informType = StringUtils.listToString(list);
            }
            if (list.size() == 0 && informDetail.length() == 0) {
                CSToast.show(getApplicationContext(), getApplicationContext().getResources()
                        .getString(R.string.improper_report_tip));
                return;
            }
            //            Logger.i(TAG, "informType == " + informType);
            ReqImproperReportController controller = new ReqImproperReportController(mListener,
                    mAppId, informType, informDetail);
            controller.doRequest();
        }
    }

    private ReqImproperReportListener mListener = new ReqImproperReportListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e(TAG, "onNetError errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_ONNETERROR;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqImproperReportSucceed(int statusCode, String errorMsg) {
            //  拿到数据
            Logger.e(TAG, "statusCode=" + statusCode);
            if (statusCode == 0) {
                mHandler.sendEmptyMessage(REPORT_SUCCEES);
            }
        }

        @Override
        public void onReqImproperReportFailed(int statusCode, String errorMsg) {
            Logger.e(TAG, "onReqRechargeFailed errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_ONNETERROR;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    };

    private void initView() {
        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null)
        {
            mActionBar.SetOnActionBarClickListener(actionBarListener);
        }
        mActionBar.setTitle(mRes.getString(R.string.improper_report));

        btn = (Button) findViewById(R.id.btnReport);
        edt = (EditText) findViewById(R.id.editText);

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startReport();
                /* if (UITools.isFastDoubleClick()) {
                 } */
            }
        });

        List<String> improperReportList = new ArrayList<String>();
        improperReportList = Arrays.asList(mRes.getStringArray(
                R.array.improper_report_list));
        mAdapter = new ImproperReportAdapter(this, improperReportList, mHandler);
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);
        // 初始化checkbox的状态
        for (int i = 1; i <= improperReportList.size(); i++) {
            mCheckBoxStatusMap.put(i, false);
        }

    }

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

    @Override
    public void onTimer(int count) {
        // TODO Auto-generated method stub
        initialCaptchaBtn(count);
    }

    private void startTimer() {
        App app = App.getAppContext();
        app.setmTimeListen(ImproperReportActivity.this);
        app.startTimeRun(StatisticManager.TIME_COUNTDOWN);
        SharedPreferences.Editor editor = App.mSharePrefences.edit();
        editor.putInt(PREFERENCE_APPID, mAppId);
        editor.commit();
    }

    private void finishActivity() {
        this.finish();
    }

    /**
     * 刷新 提交举报 按钮状态
     * 
     * @param count 剩余 秒数
     */
    private void initialCaptchaBtn(int count) {
        if (count <= 0) {
            btn.setEnabled(true);
            //            btn.setTextColor(getResources().getColor(R.color.action_blue_color));
            btn.setTextColor(getResources().getColor(R.color.color_first_normal));
            //            btn.setText(R.string.improper_report_start);
        }
        else {
            btn.setEnabled(false);
            btn.setTextColor(getResources().getColor(R.color.csl_black_4c));
            //            btn.setText(String.format(getString(R.string.improper_report_again), count));
        }
    }

}
