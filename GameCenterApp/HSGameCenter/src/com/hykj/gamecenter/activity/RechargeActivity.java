
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.adapter.RechargeStyleAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.ReqPayTypeListener;
import com.hykj.gamecenter.controller.ReqPayTypeController;
import com.hykj.gamecenter.db.CSSFContentProvider;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.RechargeAmountsColumns;
import com.hykj.gamecenter.protocol.Pay;
import com.hykj.gamecenter.protocol.Pay.PayTypeInfo;
import com.hykj.gamecenter.protocol.Pay.RspPayType;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSLoadingView;
import com.hykj.gamecenter.ui.widget.CSLoadingView.ICSListViewLoadingRetry;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SystemBarTintManager;

import java.util.ArrayList;

public class RechargeActivity extends Activity {
    private CSCommonActionBar mActionBar;
    private Resources mRes = null;
    private ListView mListView;
    protected static final int MSG_REQ_PAY_TYPE = 1000;
    private static final int MSG_RSP_PAY_TYPE = 1001;
    //    private RspPayType mRspPayType = null;//支付方式
    //  private AccountInfo mCurrentAccount;//账户信息
    private int mPayTypeInt = 1; //支付方式(1:支付宝,2:微信支付)
    private RspPayType mRspPayType;
    private static final int MSG_RSP_ONNETERROR = 1008;
    private static final int MSG_RSP_FAILED = 1009;
    private CSLoadingView mCSLoading;
    private ArrayList<Integer> payTypeList = new ArrayList<Integer>();
    private RechargeStyleAdapter mAdapter;
    private UserInfoManager mUserInfoManager;
    private String mOpenId = "";
    private final String TAG = "RechargeActivity";
    public static final String[] RECHARGE_QUERY_PROJECTION = new String[] {
            RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE,
            RechargeAmountsColumns.RECHARGE_AMOUNTS_PRICE,
            RechargeAmountsColumns.RECHARGE_AMOUNTS_SHOWVALUE
    };
    private ContentResolver mContentResolver;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REQ_PAY_TYPE:
                    ReqPayType();//获取支付类型
                    break;

                case MSG_RSP_PAY_TYPE:
                    //                    Log.e("paytype", "mRspPayType=" + mRspPayType);

                    mCSLoading.hide();
                    mListView.setVisibility(View.VISIBLE);
                    initPayShow();
                    break;

                case MSG_RSP_ONNETERROR:
                    //ERROR MESSAGE
                    mCSLoading.showNoNetwork();
                    //                    CSToast.show(App.getAppContext(), App.getAppContext().getResources()
                    //                            .getString(R.string.error_msg_net_fail));
                    break;

                case MSG_RSP_FAILED:
                    mCSLoading.showNoNetwork();
                    CSToast.showFailed(App.getAppContext(), msg.arg1, msg.obj.toString());
                    //                    CSToast.show(App.getAppContext(), (String) msg.obj);
                    //ERROR MESSAGE
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (App.getDevicesType() == App.PHONE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRes = getResources();
        mContentResolver = this.getContentResolver();
        setContentView(R.layout.activity_account_recharge);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mUserInfoManager = UserInfoManager.getInstance();
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        if (accountInfo != null) {
            mOpenId = accountInfo.openId;
        }
        initView();
        initRequestData();
    }

    //    @Override
    //    public void onBackPressed() {
    //        // TODO Auto-generated method stub
    //        setResult(5);
    //        Logger.i(TAG, "onBackPressed ", "oddshou");
    //        finish();
    //        super.onBackPressed();
    //    }

    private void initView() {

        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null)
        {
            mActionBar.SetOnActionBarClickListener(actionBarListener);
        }
        mActionBar.setTitle(mRes.getString(R.string.recharge_account));
        mListView = (ListView) findViewById(R.id.account_recharge_list);
        mCSLoading = (CSLoadingView) findViewById(R.id.cs_loading);
        mCSLoading.setOnRetryListener(mICSListViewLoadingRetry);
    }

    private final ICSListViewLoadingRetry mICSListViewLoadingRetry = new ICSListViewLoadingRetry() {

        @Override
        public void onRetry() {
            initRequestData();
        }

    };

    private void initRequestData() {
        mCSLoading.showLoading();
        mHandler.sendEmptyMessage(MSG_REQ_PAY_TYPE);
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

    private void ReqPayType() {
        //        String openId = "f661f665775a43dc9a09e86507c81dad";
        Logger.i(TAG, "mOpenId=====" + mOpenId);
        if (mOpenId.length() > 0) {
            ReqPayTypeController controller = new ReqPayTypeController(mReqPayTypeListener, mOpenId);
            controller.doRequest();
        }
    }

    private ReqPayTypeListener mReqPayTypeListener = new ReqPayTypeListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Message msg = Message.obtain();
            msg.what = MSG_RSP_ONNETERROR;
            msg.arg1 = errCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqPayTypeSucceed(RspPayType rspPayType) {
            //  拿到数据
            if (rspPayType == null || rspPayType.typeInfo.length < 1) {
                //下发数据为空，待处理
                mCSLoading.hide();
                return;
            }
            mRspPayType = rspPayType;
            mHandler.sendEmptyMessage(MSG_RSP_PAY_TYPE);

        }

        @Override
        public void onReqPayTypeFailed(int statusCode, String errorMsg) {
            Message msg = Message.obtain();
            msg.what = MSG_RSP_FAILED;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    };

    private void initPayShow() {
        payTypeList.clear();
        int size = mRspPayType.typeInfo.length;
        Logger.i(TAG, "size=====" + size);
            if (size > 0) {
            queryDb();
            //            return;
        }
        for (int i = 0; i < size; i++) {
            PayTypeInfo payTypeInfo = mRspPayType.typeInfo[i];
            //            Logger.i(TAG, "rspPayType=====" + payTypeInfo.toString());
            switch (payTypeInfo.type) {
                case 1://1:支付宝
                    if (payTypeInfo.status == 0) {//状态 0:支持 1:禁用
                        payTypeList.add(payTypeInfo.type);
                        insertDb(payTypeInfo);
                    }
                    else {

                    }
                    break;

                case 2://2:微信支付
                    if (payTypeInfo.status == 0) {
                        payTypeList.add(payTypeInfo.type);
                        insertDb(payTypeInfo);
                    }
                    else {

                    }
                    break;

                case 3://3:易联支付(银联支付)
                    if (payTypeInfo.status == 0) {
                        payTypeList.add(payTypeInfo.type);
                        insertDb(payTypeInfo);
                    }
                    else {

                    }
                    break;
                case 4://4:易宝支付(充值卡支付)
                    if (payTypeInfo.status == 0) {
                        payTypeList.add(payTypeInfo.type);
                        insertDb(payTypeInfo);
                    }
                    else {

                    }
                    break;
                case 5://5:神州付支付(充值卡支付)
                    if (payTypeInfo.status == 0) {
                        payTypeList.add(payTypeInfo.type);
                        insertDb(payTypeInfo);
                    }
                    else {

                    }
                    break;

                default:
                    break;
            }
        }
        Logger.i(TAG, "payTypeList=====" + payTypeList);
        if (payTypeList.size() == 0) {
            return;
        }
        setupAdapter();
    }

    private void setupAdapter() {
        // TODO Auto-generated method stub
        if (mAdapter == null) {
            mAdapter = new RechargeStyleAdapter(RechargeActivity.this, payTypeList);
        }
        mListView.setAdapter(mAdapter);
    }

    private void queryDb() {
        Cursor cursor = mContentResolver.query(
                CSSFContentProvider.RECHARGEAMOUNTS_CONTENT_URI,
                RECHARGE_QUERY_PROJECTION, null, null,
                RechargeAmountsColumns.RECHARGE_AMOUNTS_PRICE + " ASC");//ASC升序  DESC降序

        int count = cursor.getCount();
        if (count == 0) {
            return;
        }

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //                getDataFromCursor(cursor);
                cursor.moveToNext();
            }
        }
        Logger.i(TAG, "getDataFromCursor cursor=====" + cursor.getCount());

        if (count > 0) {
            int mCount = mContentResolver.delete(CSSFContentProvider.RECHARGEAMOUNTS_CONTENT_URI,
                    null, null);
            Logger.i(TAG, "getDataFromCursor mCount=====" + mCount);
            if (mCount == count) {
                Logger.i(TAG, "delete success");
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    /*private void getDataFromCursor(Cursor cursor) {
        int type = cursor.getInt(0);
        int price = cursor.getInt(1);
        Logger.i(TAG, "getDataFromCursor type=====" + type);
        Logger.i(TAG, "getDataFromCursor price=====" + price);
    }*/

    //插入数据库
    private void insertDb(PayTypeInfo info) {
        Pay.LimitInfo[] limits = info.limit;
        int type = info.type;
        for (int i = 0; i < limits.length; i++) {
            insertData(limits[i], type);
        }

    }

    private void insertData(Pay.LimitInfo info, int mType) {
        Logger.i(TAG, "getDataFromCursor showValue=====" + info.showValue);
        final ContentValues values = new ContentValues();
        values.put(RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE, mType);
        values.put(RechargeAmountsColumns.RECHARGE_AMOUNTS_PRICE, info.price);
        values.put(RechargeAmountsColumns.RECHARGE_AMOUNTS_SHOWVALUE, info.showValue);
        mContentResolver.insert(CSSFContentProvider.RECHARGEAMOUNTS_CONTENT_URI, values);
    }
}
