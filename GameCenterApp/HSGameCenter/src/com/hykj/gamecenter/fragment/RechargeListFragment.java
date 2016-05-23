
package com.hykj.gamecenter.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.adapter.RechargeListAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqCheckAccListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqRechargeListListener;
import com.hykj.gamecenter.controller.ReqCheckAccController;
import com.hykj.gamecenter.controller.ReqRechargeListController;
import com.hykj.gamecenter.db.CSSFContentProvider;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.RechargeRecordsColumns;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.protocol.Pay.AccRechargeInfo;
import com.hykj.gamecenter.protocol.Pay.RspAccRechargeList;
import com.hykj.gamecenter.protocol.Pay.RspCheckAccRecharge;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.ui.widget.CSLoadingUIListView;
import com.hykj.gamecenter.ui.widget.CSLoadingView;
import com.hykj.gamecenter.ui.widget.CSLoadingView.ICSListViewLoadingRetry;
import com.hykj.gamecenter.ui.widget.CSPullListView.ICSListViewListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PayConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RechargeListFragment extends Fragment
{
    private static final String TAG = "RechargeListFragment";
    public Resources mRes = null;
    private View mainView;
    private CSLoadingUIListView mListView;
    private boolean isFooterPullEnable = true;
    private boolean isHeaderPullEnable = true;
    //    private RechargeListActivity mActivity;
    private boolean hasLoadData = false;
    private boolean isLoading = true;
    private static final int MSG_INSERT_DB = 1000;
    private static final int MSG_QUERY_DB = 1001;
    private static final int MSG_REFRESH_LIST = 1002;
    private static final int MSG_SET_LISTDATA = 1003;
    private static final int MSG_REFRESH_FINISH = 1004;
    private static final int MSG_NO_DATA = 1008;
    private static final int MSG_LAST_PAGE = 1009;
    protected static final int MSG_RSP_FAILED = 1010;
    protected static final int MSG_RSP_NET_ERROR = 1011;
    public static final int MSG_REQ_CHECK = 1020;
    public static final int MSG_RSP_CHECK = 1021;
    private RspAccRechargeList mRspRechargeList;
    private Context mContext;
    private RechargeListAdapter mAdapter;
    private AccRechargeInfo[] infos;
    private ContentResolver mContentResolver;
    private int mCheckPosotion = -1;
    private static final String[] RECHARGE_QUERY_PROJECTION = new String[] {
            RechargeRecordsColumns.RECHARGE_ORDERNO, /*RechargeRecordsColumns.RECHARGE_OPENID,*/
            RechargeRecordsColumns.RECHARGE_AMT, RechargeRecordsColumns.RECHARGE_CONFIRMAMT,
            RechargeRecordsColumns.RECHARGE_CONFIRMCOIN, RechargeRecordsColumns.RECHARGE_TYPE,
            RechargeRecordsColumns.RECHARGE_FLAG, RechargeRecordsColumns.RECHARGE_ACCOUNT,
            RechargeRecordsColumns.RECHARGE_SUBMITTIME, RechargeRecordsColumns.RECHARGE_CONFIRMTIME
            , RechargeRecordsColumns.RECHARGE_STATUS
    };
    private Boolean mIsFirstDbNull = false;//是否第一次插入数据
    private Boolean mIsFirstGetDataBoolean = false;//是否第一次获取数据
    private View noDataView;
    private ArrayList<HashMap<String, Object>> mRechargeHistoryList;
    private boolean mIsStart = true; //是否是下拉刷新
    private boolean mIsRefeshing = false; //是否正在刷新中
    private UserInfoManager mUserInfoManager;
    private String mOpenId = "";
    private String mToken = "";
    private CSLoadingView mCSLoading;
    private RspCheckAccRecharge mRspCheck;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_INSERT_DB:
                    insertDataTODb();//将获得的订单数据插入数据库recharge_records表中 Uri RECHARGE_CONTENT_URI
                    break;
                case MSG_QUERY_DB:
                    queryDb();//读取数据库
                    break;
                case MSG_REFRESH_LIST:
                    refreshList();//当前列表有数据,更新当前的状态
                    break;
                case MSG_SET_LISTDATA:
                    showDataView();
                    mAdapter.setData(mRechargeHistoryList);//无数据,给适配器传递数据
                    break;
                case MSG_REFRESH_FINISH:
                    showDataView();
                    refreshFinish();//数据更新完毕,刷新UI
                    break;

                case Msg.REQUEST_DATA:
                    ReqRechargeList("", 0);//获取充值记录 times=空 and page=0 取最新的前10条数据
                    break;
                case MSG_RSP_FAILED:
                    mIsRefeshing = false;
                    stopReFresh();
                    if (mRechargeHistoryList.size() <= 0) {
                        noDataView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                    if (mContext != null)
                        CSToast.showFailed(App.getAppContext(), msg.arg1, msg.obj.toString());
                    break;
                case MSG_RSP_NET_ERROR:
                    mIsRefeshing = false;
                    stopReFresh();
                    if (mRechargeHistoryList.size() <= 0) {
                        noDataView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                    if (mContext != null)
                        CSToast.show(App.getAppContext(), App.getAppContext().getResources()
                                .getString(R.string.error_msg_net_fail));
                    break;
                case MSG_NO_DATA://第一次获取无数据，展示无数据页面
                    mCSLoading.hide();
                    noDataView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    break;
                case MSG_LAST_PAGE:
                    // rsp  获取失败
                    mIsRefeshing = false;
                    if (mListView != null) {
                        mListView.setFooterPullEnable(false);
                        mListView.stopFooterRefresh();
                        mCSLoading.hide();
                        CSToast.show(mContext,
                                mContext.getString(R.string.tip_last_page));
                    }
                    break;
                case MSG_REQ_CHECK:

                    ReqCheckAccRecharge((Integer) msg.obj);
                    /* if (mContext != null)
                         CSToast.show(App.getAppContext(), "btn click");*/
                    break;
                case MSG_RSP_CHECK:
                    updateStatusToDb(mRspCheck, mContext.getApplicationContext());
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void onAttach(Activity activity)
    {

        super.onAttach(activity);
        //        Logger.i(TAG, "RechargeListFragment onAttach");
        /*if (activity != null) {
            ((RechargeListActivity) activity).setmDetailFragment(this);
            mActivity = (RechargeListActivity) activity;
            mRes = getResources();
        }*/
    }

    /**
     * 是不是已经包含在activity中，防止调用getActivity为空
     * 
     * @return
     */
    private boolean isInActivity()
    {
        return getActivity() != null && !getActivity().isFinishing();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // 安装
        //        mApkInstalledManager = ApkInstalledManager.getInstance();
        //        mApkInstalledManager.addInstallListener(this);
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new RechargeListAdapter(mContext.getApplicationContext(), MAIN_TYPE.ALL, 1,
                    false,
                    mRechargeHistoryList);
            mAdapter.setHandler(mHandler);
        }
        mListView.setAdapter(mAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        mainView = inflater.inflate(R.layout.recharge_list_fragment, null);
        mUserInfoManager = UserInfoManager.getInstance();
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        if (accountInfo != null) {
            mOpenId = accountInfo.openId;
            mToken = accountInfo.token;
        }

        mListView = (CSLoadingUIListView) mainView.findViewById(R.id.app_list);
        /* mListView.setMode(Mode.BOTH);
         // Set a listener to be invoked when the list should be refreshed.
         mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

             @Override
             public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                 // TODO Auto-generated method stub
                 if (refreshView.isHeaderShown()) {

                     //上拉加载更多 业务代码
                     Logger.i(TAG, "上拉加载");
                     if (mIsRefeshing) {
                         return;
                     }
                     mIsRefeshing = true;
                     mIsStart = false; //上拉加载
                     ReqRechargeList(getFinishTimeFromDb(), 0);//page=0且times为当前列表数组数据第一条数据的submitTime

                 }
                 else {
                     //下拉刷新 业务代码
                     Logger.i(TAG, "下拉刷新");
                     if (mIsRefeshing) {
                         return;
                     }
                     mIsRefeshing = true;
                     mIsStart = true; //下拉刷新
                     ReqRechargeList(getFinishTimeFromDb(), 1);//page=1且times为当前列表数组数据最后一条数据的submitTime

                 }
             }
         });*/

        mListView.setFooterPullEnable(isFooterPullEnable);
        mListView.setHeaderPullEnable(isHeaderPullEnable);
        mListView.setCSListViewListener(mCSListViewListener);
        //                mListView.setCSLoadingViewListener(mCSLoadingViewListener);
        mCSLoading = (CSLoadingView) mainView.findViewById(R.id.cs_loading);
        mCSLoading.setOnRetryListener(mICSListViewLoadingRetry);
        noDataView = mainView.findViewById(R.id.layout_nodata);

        //        mCSLoading.showLoading();

        //进入界面，第一步从数据库读入数据，如果为空，就请求服务器
        initGetDatafromDb();

        return mainView;
    }

    private final ICSListViewLoadingRetry mICSListViewLoadingRetry = new ICSListViewLoadingRetry() {

        @Override
        public void onRetry() {
            initGetDatafromDb();
        }

    };

    private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {
        @Override
        public void onRefresh() {
            Logger.i(TAG, "上拉加载");
            if (mIsRefeshing) {
                return;
            }
            mIsRefeshing = true;
            mIsStart = false; //上拉加载
            ReqRechargeList(getFinishTimeFromDb(), 0);//page=0且times为当前列表数组数据第一条数据的submitTime
        }

        @Override
        public void onLoadMore() {
            //            ReqRechargeList();
            Logger.i(TAG, "下拉刷新");
            if (mIsRefeshing) {
                return;
            }
            mIsRefeshing = true;
            mIsStart = true; //下拉刷新
            ReqRechargeList(getFinishTimeFromDb(), 1);//page=1且times为当前列表数组数据最后一条数据的submitTime
        }
    };

    private void initGetDatafromDb() {
        mCSLoading.showLoading();
        //   from db
        mRechargeHistoryList = new ArrayList<HashMap<String, Object>>();//在数组中存放数据
        int count = queryDb();//从数据库查询
        Logger.i(TAG, "count ==" + count);
        if (count == 0) {
            //数据库是空的，请求服务器,获得数据后写入数据库
            mIsFirstDbNull = true;
            mHandler.sendEmptyMessage(Msg.REQUEST_DATA);
            return;
        }
        //数据库内有数据，先展示现有数据
        Logger.i(TAG, "mRechargeHistoryList.size() ==" + (mRechargeHistoryList.size() > 0));
        if (mRechargeHistoryList.size() > 0) {
            setAdapter();
            showDataView();//59797946469
            mIsStart = false; //手动上拉加载
            ReqRechargeList(
                    (String) mRechargeHistoryList.get(0).get(
                            PayConstants.RECHARGE_LISTITEM_SUBMITTIME), 0);//进行一次主动上拉加载最新数据
        }
    }

    protected String getFinishTimeFromDb() {
        String sortOrder;
        if (mIsStart) {
            sortOrder = " ASC";
        }
        else {
            sortOrder = " DESC";
        }
        Cursor cursor = mContext.getContentResolver().query(
                CSSFContentProvider.RECHARGE_CONTENT_URI,
                new String[] {
                    RechargeRecordsColumns.RECHARGE_SUBMITTIME
                }, null, null,
                RechargeRecordsColumns.RECHARGE_SUBMITTIME + sortOrder);
        Logger.i(TAG, "getCount=" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            Logger.i(TAG, "submittime" + "=" + cursor.getString(0));
            //      Log.e( "查数据库时间" , sortOrder + "=" + cursor.getString( 0 ) );
            return cursor.getString(0);

        }
        if (cursor != null) {
            cursor.close();
        }

        return "";
    }

    private void showDataView() {
        mCSLoading.hide();
        mListView.setVisibility(View.VISIBLE);
    }

    private void ReqRechargeList(String times, int page) {
        //        String openId = "b9a7f7595eed239ab5ef24ca475a1731";
        //        String token = "0cb1270d77f1051d3c706bb53800dc5c";
        /*if (mCurrentAccount != null) {
            openId = mCurrentAccount.getOpenId();
        }*/
        Logger.i(TAG, "ReqRechargeList mOpenId=" + mOpenId);
        Logger.i(TAG, "ReqRechargeList mToken=" + mToken);
        Logger.i(TAG, "ReqRechargeList times=" + times);

        if (mOpenId != null && mToken != null) {
            ReqRechargeListController controller = new ReqRechargeListController(
                    mReqRechargeListListener, mOpenId, mToken, times, page);
            controller.doRequest();
        }

    }

    private ReqRechargeListListener mReqRechargeListListener = new ReqRechargeListListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.i(TAG, "onNetError errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_NET_ERROR;
            //            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqRechargeListSucceed(RspAccRechargeList rspRechargeList) {
            //  拿到数据
            mRspRechargeList = rspRechargeList;
            infos = mRspRechargeList.accRechargeInfo;
            //            Logger.i(TAG, "rspRechargeList=" + rspRechargeList);
            /*if (mRspRechargeList.accRechargeInfo.length != 0) {
                Logger.i(TAG, "rspRechargeList=" + rspRechargeList);
            } else {
                Logger.i(TAG, "rspRechargeList=" + rspRechargeList);
              }*/
            //            Logger.i(TAG, "rspRechargeList=" + rspRechargeList);
            if (infos.length <= 0) {
                Logger.i(TAG, "infos.length=" + infos.length);
                if (!mIsFirstGetDataBoolean) {
                    mHandler.sendEmptyMessage(MSG_NO_DATA);
                } else {
                    mHandler.sendEmptyMessage(MSG_LAST_PAGE);//没有数据了，最后一页
                }
            } else {
                Logger.i(TAG, "infos.length=" + infos.length);
                mIsFirstGetDataBoolean = true;
                Logger.i(TAG, "mIsFirstDbNull=" + mIsFirstDbNull);
                if (!mIsFirstDbNull) {
                    mHandler.sendEmptyMessage(MSG_REFRESH_LIST);//非第一次获取数据,更新当前列表数据
                }
                mHandler.sendEmptyMessage(MSG_INSERT_DB);//更新数据库数据，有则更新状态，无则插入新数据
            }
        }

        @Override
        public void onReqRechargeListFailed(int statusCode, String errorMsg) {
            Logger.i(TAG, "onReqRechargeListFailed errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_FAILED;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    };

    /* public void setHandler(Handler handler)
     {
         mUiHandler = handler;
     }*/
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Logger.i("AppInfoSoftWareDetailFragment", "onDestroy");
    }

    private void ReqCheckAccRecharge(int position) {
        //        String openId = "b9a7f7595eed239ab5ef24ca475a1731";
        //        String token = "0cb1270d77f1051d3c706bb53800dc5c";
        /*if (mCurrentAccount != null) {
            openId = mCurrentAccount.getOpenId();
        }*/
        //        Log.e("Tag", "" + openId);
        mCheckPosotion = position;

        String orderNo = (String) mRechargeHistoryList.get(position).get(
                PayConstants.RECHARGE_LISTITEM_ORDERNO);
        Logger.i(TAG, "ReqCheckAccRecharge    orderNo==" + orderNo);
        ReqCheckAccController controller = new ReqCheckAccController(
                mReqCheckAccRechargeListener, mOpenId, mToken, orderNo);
        controller.doRequest();

    }

    private ReqCheckAccListener mReqCheckAccRechargeListener = new ReqCheckAccListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.i(TAG, "onNetError errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_NET_ERROR;
            //            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqCheckAccSucceed(RspCheckAccRecharge rspCheck) {
            //  拿到数据
            Logger.i(TAG, "rspCheck=" + rspCheck);
            mRspCheck = rspCheck;
            mHandler.sendEmptyMessage(MSG_RSP_CHECK);
        }

        @Override
        public void onReqCheckAccFailed(int statusCode, String errorMsg) {
            Logger.i(TAG, "onReqRechargeListFailed errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_FAILED;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    };

    private void insertDataTODb() {
        // 先拿到数据库所有数据的订单号
        List<String> orderList = new ArrayList<String>();
        orderList = getNoLIstFromDb();

        if (orderList.size() == 0) {//  size == 0  说明数据库中无数据
            for (int i = 0; i < infos.length; i++) {
                insertDb(infos[i]);
            }
        } else {
            for (int i = 0; i < infos.length; i++) {
                AccRechargeInfo payOrder = infos[i];
                // 插入数据库前，先判断是否有相同的订单号，没有就insert，有就update
                if (isExistOrderNo(orderList, payOrder.orderNO)) {
                    updateDb(payOrder, payOrder.orderNO);//有相同订单更新相同订单的状态
                }
                else {
                    insertDb(payOrder);//更新了新的订单则插入新数据
                }
            }
        }

        //重新查询新的数据库数据，更新UI
        /*if (mIsFirstDbNull) {
            mHandler.sendEmptyMessage(MSG_QUERY_DB);
        }*/
        mHandler.sendEmptyMessage(MSG_QUERY_DB);
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    private void updateStatusToDb(RspCheckAccRecharge rspCheck, final Context context) {
        Logger.i(TAG, "orderNo=" + rspCheck.orderNo);
        int status = rspCheck.rechargeStatus;//订单状态，1:未处理,2:处理中,3:交易成功,4:交易失败,5:已退款
        int newCoin = rspCheck.rechargeNewCoin;//由于充值方式的差异，可能存在充值面额与实际到账金币不同，此处需更新
        final ContentValues values = new ContentValues();
        values.put(RechargeRecordsColumns.RECHARGE_STATUS, status);
        values.put(RechargeRecordsColumns.RECHARGE_CONFIRMCOIN, newCoin);
        int checkPosition = mContentResolver.update(CSSFContentProvider.RECHARGE_CONTENT_URI,
                values,
                RechargeRecordsColumns.RECHARGE_ORDERNO + " = " + "'" + rspCheck.orderNo + "'",
                null);
        Logger.i(TAG, "status=" + status);
        Logger.i(TAG, "mCheckPosotion=" + mCheckPosotion);
        Logger.i(TAG, "insertDb      checkPosition=" + checkPosition);
        switch (status) {
            case 2:
                // 状态不变仍在处理中  弹框提示语  金币将在3分钟内到账
                //                Toast.makeText(context, "金币将在3分钟内到账", Toast.LENGTH_SHORT).show();
                CSToast.show(
                        context,
                        context.getApplicationContext().getResources()
                                .getString(R.string.recharge_text_desp));
                break;
            case 3:
                //状态更改为充值成功  弹框  充值成功
                //                Toast.makeText(context, "充值成功", Toast.LENGTH_SHORT).show();
                CSToast.show(
                        context,
                        context.getApplicationContext().getResources()
                                .getString(R.string.recharge_text_success));
                break;
            case 4:
                //状态更改为充值失败  弹框  充值失败(原因)
                //                Toast.makeText(context, "充值失败", Toast.LENGTH_SHORT).show();
                /*CSToast.show(
                        context,
                        context.getApplicationContext().getResources()
                                .getString(R.string.recharge_text_failed));*/
                break;
        }
        switch (checkPosition) {
            case 0://代表数据并无更改
                if (mAdapter != null) {
                    mAdapter.setLoaded(true);
                    mAdapter.setData(mRechargeHistoryList);
                    Logger.i(TAG, "notifyDataSetChanged");
                }
                break;
            case 1://数据更改
                if (mCheckPosotion != -1 && mRechargeHistoryList.get(mCheckPosotion).containsKey(
                        PayConstants.RECHARGE_LISTITEM_STATUS)) {
                    mRechargeHistoryList.get(mCheckPosotion).put(
                            PayConstants.RECHARGE_LISTITEM_STATUS,
                            status);
                    mRechargeHistoryList.get(mCheckPosotion).put(
                            PayConstants.RECHARGE_LISTITEM_NEWCOIN,
                            newCoin);
                    Logger.i(TAG, "map change data");
                }
                if (mAdapter != null) {
                    mAdapter.setLoaded(true);
                    mAdapter.setData(mRechargeHistoryList);
                    Logger.i(TAG, "notifyDataSetChanged");
                }
                break;
        }
    }

    private void updateDb(AccRechargeInfo payOrder, String orderNo) {
        final ContentValues values = new ContentValues();
        values.put(RechargeRecordsColumns.RECHARGE_SUBMITTIME, payOrder.submitTime);
        values.put(RechargeRecordsColumns.RECHARGE_STATUS, payOrder.rechargeStatus);
        mContentResolver.update(CSSFContentProvider.RECHARGE_CONTENT_URI, values,
                RechargeRecordsColumns.RECHARGE_ORDERNO + " = " + orderNo, null);
    }

    //插入数据库
    private void insertDb(AccRechargeInfo payOrder) {
        final ContentValues values = new ContentValues();
        //        String openId = "b9a7f7595eed239ab5ef24ca475a1731";
        values.put(RechargeRecordsColumns.RECHARGE_ORDERNO, payOrder.orderNO);
        values.put(RechargeRecordsColumns.RECHARGE_OPENID, mOpenId);//正式环境openId从全局账户信息中获取
        values.put(RechargeRecordsColumns.RECHARGE_AMT, payOrder.rechargeAmt);
        values.put(RechargeRecordsColumns.RECHARGE_CONFIRMAMT, payOrder.confirmAmt);
        values.put(RechargeRecordsColumns.RECHARGE_CONFIRMCOIN, payOrder.confirmNewCoin);
        values.put(RechargeRecordsColumns.RECHARGE_TYPE, payOrder.channelType);
        values.put(RechargeRecordsColumns.RECHARGE_FLAG, payOrder.channelFlag);
        values.put(RechargeRecordsColumns.RECHARGE_ACCOUNT, payOrder.chargeAccount);
        values.put(RechargeRecordsColumns.RECHARGE_SUBMITTIME, payOrder.submitTime);
        values.put(RechargeRecordsColumns.RECHARGE_CONFIRMTIME, payOrder.confirmTime);
        values.put(RechargeRecordsColumns.RECHARGE_STATUS, payOrder.rechargeStatus);
        mContentResolver.insert(CSSFContentProvider.RECHARGE_CONTENT_URI, values);
    }

    private List<String> getNoLIstFromDb() {
        List<String> list = new ArrayList<String>();

        Cursor cursor = mContext.getContentResolver().query(
                CSSFContentProvider.RECHARGE_CONTENT_URI,
                new String[] {
                    RechargeRecordsColumns.RECHARGE_ORDERNO
                }, null, null,
                RechargeRecordsColumns.RECHARGE_SUBMITTIME);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Logger.i(TAG, "cursor.getString(0)" + cursor.getString(0));
                list.add(cursor.getString(0));

                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private boolean isExistOrderNo(List<String> List, String orderNo) {
        for (int i = 0; i < List.size(); i++) {
            if (List.get(i).equals(orderNo)) {
                return true;
            }
        }
        return false;
    }

    private int queryDb() {
        String where = null;
        //        String openId = "b9a7f7595eed239ab5ef24ca475a1731";
        where = RechargeRecordsColumns.RECHARGE_OPENID + "='" + mOpenId + "'";

        /* mCurrentAccount = O2OManager.getInstance(this).getCurrentAccount();
         if (mCurrentAccount != null) {
             String openId = mCurrentAccount.getOpenId()null;
             where = RechargeRecordsColumns.RECHARGE_OPENID + "='" + openId + "'";
         }*/

        Cursor cursor = mContext.getContentResolver().query(
                CSSFContentProvider.RECHARGE_CONTENT_URI,
                RECHARGE_QUERY_PROJECTION, where, null,
                RechargeRecordsColumns.RECHARGE_SUBMITTIME + " DESC");

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            mRechargeHistoryList.clear();
            while (!cursor.isAfterLast()) {
                getDataFromCursor(cursor);
                cursor.moveToNext();
            }
        }
        //        Logger.i(TAG, "mRechargeHistoryList===" + mRechargeHistoryList);
        //第一次数据库是空的，只有读取数据后才会设置列表
        if (mIsFirstDbNull) {
            //            mHandler.sendEmptyMessage(MSG_SET_LISTDATA);//给适配器传递数据
            setAdapter();
            mIsFirstDbNull = false;
        }

        Logger.i(TAG, "insertDataTODb 数据库一共有数据行===" + mRechargeHistoryList.size());
        int count = cursor.getCount();
        if (count > 0 && mAdapter != null) {
            mHandler.sendEmptyMessage(MSG_SET_LISTDATA);
        }

        if (cursor != null) {
            cursor.close();
        }
        return count;
    }

    private void getDataFromCursor(Cursor cursor) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String orderNo = cursor.getString(0);//充值订单号，唯一标识该条充值记录
        int rechargeNewCoin = cursor.getInt(3);//  确认游戏币 单位：(New币)
        int type = cursor.getInt(4);// 渠道类型，1:支付宝，2:微信,3=银联，4=手机充值卡
        String account = cursor.getString(6);// 充值帐号，如点卡卡号、电话充值卡卡号等
        String submitTime = cursor.getString(7);// 提交时间，yyyyMMddHHmmss
        int status = cursor.getInt(9);//充值状态，1:未处理,2:处理中,3:交易成功,4:交易失败,5:已退款

        map.put(PayConstants.RECHARGE_LISTITEM_ORDERNO, orderNo);
        map.put(PayConstants.RECHARGE_LISTITEM_NEWCOIN, rechargeNewCoin);
        map.put(PayConstants.RECHARGE_LISTITEM_TYPE, type);
        map.put(PayConstants.RECHARGE_LISTITEM_ACCOUNT, account);
        map.put(PayConstants.RECHARGE_LISTITEM_SUBMITTIME, submitTime);
        map.put(PayConstants.RECHARGE_LISTITEM_STATUS, status);
        map.put(PayConstants.RECHARGE_LISTITEM_TYPE_DES, getStatus(status));

        mRechargeHistoryList.add(map);
    }

    private String getStatus(int status) {
        String orderStatus = null;
        switch (status) {
            case 1:
                orderStatus = "未处理";
                //                orderStatus = this.getResources().getString(R.string.o2o_pay_order_list_status_1);
                break;
            case 2:
                orderStatus = "充值中";
                //                orderStatus = this.getResources().getString(R.string.o2o_pay_order_list_status_2);
                break;
            case 3:
                orderStatus = "充值成功";
                //                orderStatus = this.getResources().getString(R.string.o2o_pay_order_list_status_3);
                break;
            case 4:
                orderStatus = "充值失败";
                //                orderStatus = this.getResources().getString(R.string.o2o_pay_order_list_status_4);
                break;
            case 5:
                orderStatus = "已退款";
                //                orderStatus = this.getResources().getString(R.string.o2o_pay_order_list_status_5);
                break;
            default:
                break;
        }
        return orderStatus;
    }

    private void refreshList() {
        //   刷新列表
        if (mIsStart) {
            for (int i = 0; i < infos.length; i++) {
                //          Log.e( "下拉刷新" , "i=" + i );
                AccRechargeInfo payOrder = infos[i];
                refresh(payOrder);
            }

        }
        else {

            for (int i = infos.length - 1; i >= 0; i--) {
                //          Log.e( "上啦加载" , "i=" + i );
                AccRechargeInfo payOrder = infos[i];
                refresh(payOrder);
            }

        }

        mHandler.sendEmptyMessage(MSG_REFRESH_FINISH);
    }

    private void refresh(AccRechargeInfo payOrder) {
        String orderNo = payOrder.orderNO;
        int status = payOrder.rechargeStatus;
        for (int i = 0; i < mRechargeHistoryList.size(); i++) {
            String newOrderNo = (String) mRechargeHistoryList.get(i).get(
                    PayConstants.RECHARGE_LISTITEM_ORDERNO);
            if (newOrderNo.equals(orderNo)) {
                mRechargeHistoryList.get(i).put(PayConstants.RECHARGE_LISTITEM_STATUS, status);
                mRechargeHistoryList.get(i).put(PayConstants.RECHARGE_LISTITEM_TYPE_DES,
                        getStatus(status));
                return;
            }
        }

        addTOHistoryListItemTop(payOrder);
    }

    private void addTOHistoryListItemTop(AccRechargeInfo payOrder) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(PayConstants.RECHARGE_LISTITEM_ORDERNO, payOrder.orderNO);
        map.put(PayConstants.RECHARGE_LISTITEM_NEWCOIN, payOrder.confirmNewCoin);
        map.put(PayConstants.RECHARGE_LISTITEM_TYPE, payOrder.channelType);
        map.put(PayConstants.RECHARGE_LISTITEM_ACCOUNT, payOrder.chargeAccount);
        map.put(PayConstants.RECHARGE_LISTITEM_SUBMITTIME, payOrder.submitTime);
        map.put(PayConstants.RECHARGE_LISTITEM_STATUS, payOrder.rechargeStatus);
        map.put(PayConstants.RECHARGE_LISTITEM_TYPE_DES, getStatus(payOrder.rechargeStatus));

        if (mIsStart) {
            mRechargeHistoryList.add(map);
        }
        else {
            mRechargeHistoryList.add(0, map);
        }

    }

    private void refreshFinish() {
        mIsRefeshing = false;
        mAdapter.notifyDataSetChanged();
        stopReFresh();
    }

    private void stopReFresh() {
        if (mListView != null) {
            mCSLoading.hide();
            mListView.stopFooterRefresh();
            mListView.stopHeaderRefresh();
        }
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }
}
