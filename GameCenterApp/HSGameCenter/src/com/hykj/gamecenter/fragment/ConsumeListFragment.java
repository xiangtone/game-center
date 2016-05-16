
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
import com.hykj.gamecenter.adapter.ConsumeListAdapter;
import com.hykj.gamecenter.controller.ProtocolListener.MAIN_TYPE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqConsumeListListener;
import com.hykj.gamecenter.controller.ReqConsumeListController;
import com.hykj.gamecenter.db.CSSFContentProvider;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.ConsumerRecordsColumns;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.protocol.Pay.ConsumeInfo;
import com.hykj.gamecenter.protocol.Pay.RspConsumeList;
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

public class ConsumeListFragment extends Fragment
{
    private static final String TAG = "ConsumeListFragment";
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
    private RspConsumeList mRspRechargeList;
    private Context mContext;
    private ConsumeListAdapter mAdapter;
    private ConsumeInfo[] infos;
    private ContentResolver mContentResolver;
    private int mCheckPosotion = -1;
    private static final String[] CONSUME_QUERY_PROJECTION = new String[] {
            ConsumerRecordsColumns.CONSUME_ORDERNO,
            ConsumerRecordsColumns.CONSUME_PACKNAME, ConsumerRecordsColumns.CONSUME_APPNAME,
            ConsumerRecordsColumns.CONSUME_PRODUCTNAME, ConsumerRecordsColumns.CONSUME_CONSUMECOIN,
            ConsumerRecordsColumns.CONSUME_TIME, ConsumerRecordsColumns.CONSUME_STATUS
    };
    private Boolean mIsFirstDbNull = false;//是否第一次插入数据
    private Boolean mIsFirstGetDataBoolean = false;//是否第一次获取数据
    private View noDataView;
    private ArrayList<HashMap<String, Object>> mConsumeHistoryList;
    private boolean mIsStart = true; //是否是下拉刷新
    private boolean mIsRefeshing = false; //是否正在刷新中
    private UserInfoManager mUserInfoManager;
    private String mOpenId = "";
    private String mToken = "";
    private CSLoadingView mCSLoading;

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
                    mAdapter.setData(mConsumeHistoryList);//无数据,给适配器传递数据
                    break;
                case MSG_REFRESH_FINISH:
                    showDataView();
                    refreshFinish();//数据更新完毕,刷新UI
                    break;

                case Msg.REQUEST_DATA:
                    ReqConsumeList("", 0);//获取充值记录 times=空 and page=0 取最新的前10条数据
                    break;
                case Msg.GET_DATA:

                    //                    Logger.i(TAG, "handleMessage infos===" + infos.length);
                    //                    Logger.i(TAG, "handleMessage listCount===" + mRspRechargeList.listCount);
                    /*if (mAdapter != null && mListView != null && infos.length > 0) {

                        mAdapter.appendData(Tools.arrayToList(infos),
                                false);
                        mListView.hideLoadingUI();
                        mListView.stopFooterRefresh();
                        mListView.stopHeaderRefresh();
                         mListView.stopFooterRefresh();
                         isLoading = false;

                        break;
                    }*/
                case MSG_RSP_FAILED:
                    mIsRefeshing = false;
                    stopReFresh();
                    if (mConsumeHistoryList.size() <= 0) {
                        noDataView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                    if (mContext != null)
                        CSToast.showFailed(App.getAppContext(), msg.arg1, msg.obj.toString());
                    break;
                case MSG_RSP_NET_ERROR:
                    mIsRefeshing = false;
                    stopReFresh();
                    if (mConsumeHistoryList.size() <= 0) {
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
                default:
                    break;
            }

        }
    };

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
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
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new ConsumeListAdapter(mContext.getApplicationContext(), MAIN_TYPE.ALL, 1,
                    false,
                    mConsumeHistoryList);
            mAdapter.setHandler(mHandler);
        }
        mListView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
        mainView = inflater.inflate(R.layout.consume_list_fragment, null);
        mUserInfoManager = UserInfoManager.getInstance();
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        if (accountInfo != null) {
            mOpenId = accountInfo.openId;
            mToken = accountInfo.token;
        }

        mListView = (CSLoadingUIListView) mainView.findViewById(R.id.app_list);
        mListView.setFooterPullEnable(isFooterPullEnable);
        mListView.setHeaderPullEnable(isHeaderPullEnable);
        mListView.setCSListViewListener(mCSListViewListener);
        //        mListView.setCSLoadingViewListener(mCSLoadingViewListener);

        mCSLoading = (CSLoadingView) mainView.findViewById(R.id.cs_loading);
        mCSLoading.setOnRetryListener(mICSListViewLoadingRetry);
        noDataView = mainView.findViewById(R.id.layout_nodata);
        showLoadingState();

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

    private void showDataView() {
        mCSLoading.hide();
        mListView.setVisibility(View.VISIBLE);
    }

    private void initGetDatafromDb() {
        mCSLoading.showLoading();
        //   from db
        mConsumeHistoryList = new ArrayList<HashMap<String, Object>>();//在数组中存放数据
        int count = queryDb();//从数据库查询
        Logger.i(TAG, "count ==" + count);
        if (count == 0) {
            //数据库是空的，请求服务器,获得数据后写入数据库
            mIsFirstDbNull = true;
            mHandler.sendEmptyMessage(Msg.REQUEST_DATA);
            return;
        }
        //数据库内有数据，先展示现有数据
        Logger.i(TAG, "mConsumeHistoryList.size() ==" + (mConsumeHistoryList.size()));
        Logger.i(TAG, "mConsumeHistoryList.size() ==" + (mConsumeHistoryList.size() > 0));
        if (mConsumeHistoryList.size() > 0) {
            setAdapter();
            showDataView();
            mIsStart = false; //手动上拉加载
            ReqConsumeList(
                    (String) mConsumeHistoryList.get(0).get(
                            PayConstants.CONSUME_LIST_CONSUME_TIME), 0);//进行一次主动上拉加载最新数据
        }
        //        mHandler.sendEmptyMessage(MSG_SET_PULLLISTVIEW);
        //        mPullListView.doPullRefreshing(true, 0);//首次进入刷新
    }

    private final ICSListViewListener mCSListViewListener = new ICSListViewListener() {
        @Override
        public void onRefresh() {
            Logger.i(TAG, "上拉加载");
            if (mIsRefeshing) {
                return;
            }
            mIsRefeshing = true;
            mIsStart = false; //上拉加载
            ReqConsumeList(getFinishTimeFromDb(), 0);//page=0且times为当前列表数组数据第一条数据的submitTime
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
            ReqConsumeList(getFinishTimeFromDb(), 1);//page=1且times为当前列表数组数据最后一条数据的submitTime
        }
    };

    protected String getFinishTimeFromDb() {
        String sortOrder;
        if (mIsStart) {
            sortOrder = " ASC";
        }
        else {
            sortOrder = " DESC";
        }
        Cursor cursor = mContentResolver.query(
                CSSFContentProvider.CONSUME_CONTENT_URI,
                new String[] {
                    ConsumerRecordsColumns.CONSUME_TIME
                }, null, null,
                ConsumerRecordsColumns.CONSUME_TIME + sortOrder);
        Logger.i(TAG, "getCount=" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            Logger.i(TAG, "consume" + "=" + cursor.getString(0));
            //      Log.e( "查数据库时间" , sortOrder + "=" + cursor.getString( 0 ) );
            return cursor.getString(0);

        }
        if (cursor != null) {
            cursor.close();
        }

        return "";
    }

    /* private final ICSLoadingViewListener mCSLoadingViewListener = new ICSLoadingViewListener() {

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
                     ReqRechargeList();
                     break;
                 default:
                     break;
             }
         }

         @Override
         public void onInitRequestData() {
             if (GlobalConfigControllerManager.getInstance().getLoadingState() == GlobalConfigControllerManager.NONETWORK_STATE)
                 GlobalConfigControllerManager.getInstance().reqGlobalConfig();
             else
                 mHandler.sendEmptyMessage(Msg.REQUEST_DATA);
         }
     };*/

    private void ReqConsumeList(String times, int page) {
        //        mOpenId = "4060f17dc0d30cb3edb862e011ed8d22";
        //        mToken = "79e88e8c67c72c96060ddd5c8d075e00";
        /*if (mCurrentAccount != null) {
            openId = mCurrentAccount.getOpenId();
        }*/
        //        Log.e("Tag", "" + openId);
        Logger.i(TAG, "ReqConsumeList times=" + times);

        if (mOpenId != null && mToken != null) {
            ReqConsumeListController controller = new ReqConsumeListController(
                    mReqRechargeListListener, mOpenId, mToken, times, page);
            controller.doRequest();
        }

    }

    private ReqConsumeListListener mReqRechargeListListener = new ReqConsumeListListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.i(TAG, "onNetError errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_NET_ERROR;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqConsumeListSucceed(RspConsumeList rspConsumeList) {
            //  拿到数据
            mRspRechargeList = rspConsumeList;
            infos = mRspRechargeList.consumeInfo;
            /* if (mRspRechargeList != null) {
             }*/
            //            Logger.i(TAG, "rspRechargeList=" + rspConsumeList);
            Logger.i(TAG, "infos.length=" + infos.length);
            if (infos.length <= 0) {
                if (!mIsFirstGetDataBoolean) {
                    mHandler.sendEmptyMessage(MSG_NO_DATA);
                } else {
                    mHandler.sendEmptyMessage(MSG_LAST_PAGE);//没有数据了，最后一页
                }
            } else {
                mIsFirstGetDataBoolean = true;
                Logger.i(TAG, "mIsFirstDbNull=" + mIsFirstDbNull);
                if (!mIsFirstDbNull) {
                    mHandler.sendEmptyMessage(MSG_REFRESH_LIST);//非第一次获取数据,更新当前列表数据
                }
                mHandler.sendEmptyMessage(MSG_INSERT_DB);//更新数据库数据，有则更新状态，无则插入新数据
            }
        }

        @Override
        public void onReqConsumeListFailed(int statusCode, String errorMsg) {
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
                ConsumeInfo consumeOrder = infos[i];
                // 插入数据库前，先判断是否有相同的订单号，没有就insert，有就跳过 break
                if (isExistOrderNo(orderList, consumeOrder.orderNo)) {
                    break;
                }
                else {
                    insertDb(consumeOrder);//更新了新的订单则插入新数据
                }
            }
        }

        //重新查询新的数据库数据，更新UI
        /*    if (mIsFirstDbNull) {
            }*/
        mHandler.sendEmptyMessage(MSG_QUERY_DB);

    }

    //插入数据库
    private void insertDb(ConsumeInfo consumeOrder) {
        final ContentValues values = new ContentValues();
        //        String openId = "b9a7f7595eed239ab5ef24ca475a1731";
        values.put(ConsumerRecordsColumns.CONSUME_ORDERNO, consumeOrder.orderNo);
        values.put(ConsumerRecordsColumns.CONSUME_OPENID, mOpenId);
        values.put(ConsumerRecordsColumns.CONSUME_ROLEID, consumeOrder.roleId);
        values.put(ConsumerRecordsColumns.CONSUME_CPORDER, consumeOrder.cpOrderNo);
        values.put(ConsumerRecordsColumns.CONSUME_APPID, consumeOrder.appId);
        values.put(ConsumerRecordsColumns.CONSUME_APPNAME, consumeOrder.appName);
        values.put(ConsumerRecordsColumns.CONSUME_PACKNAME, consumeOrder.packName);
        values.put(ConsumerRecordsColumns.CONSUME_CONSUMECOIN, consumeOrder.consumeNewCoin);
        values.put(ConsumerRecordsColumns.CONSUME_PRODUCTCODE, consumeOrder.productCode);
        values.put(ConsumerRecordsColumns.CONSUME_PRODUCTNAME, consumeOrder.productName);
        values.put(ConsumerRecordsColumns.CONSUME_PRODUCTCOUNT, consumeOrder.productCount);
        values.put(ConsumerRecordsColumns.CONSUME_TIME, consumeOrder.consumeTime);
        values.put(ConsumerRecordsColumns.CONSUME_STATUS, consumeOrder.consumeStatus);
        mContentResolver.insert(CSSFContentProvider.CONSUME_CONTENT_URI, values);
    }

    private List<String> getNoLIstFromDb() {
        List<String> list = new ArrayList<String>();

        Cursor cursor = mContentResolver.query(
                CSSFContentProvider.CONSUME_CONTENT_URI,
                new String[] {
                    ConsumerRecordsColumns.CONSUME_ORDERNO
                }, null, null,
                ConsumerRecordsColumns.CONSUME_TIME + " DESC");

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
        //        mOpenId = "4060f17dc0d30cb3edb862e011ed8d22";
        where = ConsumerRecordsColumns.CONSUME_OPENID + "='" + mOpenId + "'";

        /* mCurrentAccount = O2OManager.getInstance(this).getCurrentAccount();
         if (mCurrentAccount != null) {
             String openId = mCurrentAccount.getOpenId()null;
             where = RechargeRecordsColumns.RECHARGE_OPENID + "='" + openId + "'";
         }*/

        Cursor cursor = mContentResolver.query(
                CSSFContentProvider.CONSUME_CONTENT_URI,
                CONSUME_QUERY_PROJECTION, where, null,
                ConsumerRecordsColumns.CONSUME_TIME + " DESC");

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            mConsumeHistoryList.clear();
            while (!cursor.isAfterLast()) {
                getDataFromCursor(cursor);
                cursor.moveToNext();
            }
        }
        //第一次数据库是空的，只有读取数据后才会设置列表
        if (mIsFirstDbNull) {
            //            mHandler.sendEmptyMessage(MSG_SET_LISTDATA);//给适配器传递数据
            setAdapter();
            mIsFirstDbNull = false;
        }

        Logger.i(TAG, "insertDataTODb 数据库一共有数据行===" + mConsumeHistoryList.size());
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
        String orderNo = cursor.getString(0);//消费订单号，唯一标识该条充值记录
        String packname = cursor.getString(1);//  来源应用包名
        String appName = cursor.getString(2);// 来源应用的名称
        String productName = cursor.getString(3);// 消费商品的名称  如元宝或者道具类
        int consumeCoin = cursor.getInt(4);// 消费New币(游戏中心的货币)
        String consumeTime = cursor.getString(5);// 消费时间，yyyyMMddHHmmss 
        int consumeStatus = cursor.getInt(6);//  消费状态，1:消费失败,2:消费成功

        map.put(PayConstants.CONSUME_LIST_ORDERNO, orderNo);
        map.put(PayConstants.CONSUME_LIST_PACKNAME, packname);
        map.put(PayConstants.CONSUME_LIST_APPNAME, appName);
        map.put(PayConstants.CONSUME_LIST_PRODUCTNAME, productName);
        map.put(PayConstants.CONSUME_LIST_CONSUMECOIN, consumeCoin);
        map.put(PayConstants.CONSUME_LIST_CONSUME_TIME, consumeTime);
        map.put(PayConstants.CONSUME_LIST_CONSUME_STATUS, consumeStatus);

        mConsumeHistoryList.add(map);
    }

    private void refreshList() {
        //   刷新列表
        if (mIsStart) {
            for (int i = 0; i < infos.length; i++) {
                //          Log.e( "下拉刷新" , "i=" + i );
                ConsumeInfo consumeOrder = infos[i];
                refresh(consumeOrder);
            }

        }
        else {

            for (int i = infos.length - 1; i >= 0; i--) {
                //          Log.e( "上啦加载" , "i=" + i );
                ConsumeInfo consumeOrder = infos[i];
                refresh(consumeOrder);
            }

        }

        mHandler.sendEmptyMessage(MSG_REFRESH_FINISH);
    }

    private void refresh(ConsumeInfo consumeOrder) {
        String orderNo = consumeOrder.orderNo;
        for (int i = 0; i < mConsumeHistoryList.size(); i++) {
            String newOrderNo = (String) mConsumeHistoryList.get(i).get(
                    PayConstants.CONSUME_LIST_ORDERNO);
            if (newOrderNo.equals(orderNo)) {
                return;
            }
        }
        addTOHistoryListItemTop(consumeOrder);
    }

    private void addTOHistoryListItemTop(ConsumeInfo consumeOrder) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(PayConstants.CONSUME_LIST_ORDERNO, consumeOrder.orderNo);
        map.put(PayConstants.CONSUME_LIST_PACKNAME, consumeOrder.packName);
        map.put(PayConstants.CONSUME_LIST_APPNAME, consumeOrder.appName);
        map.put(PayConstants.CONSUME_LIST_PRODUCTNAME, consumeOrder.productName);
        map.put(PayConstants.CONSUME_LIST_CONSUMECOIN, consumeOrder.consumeNewCoin);
        map.put(PayConstants.CONSUME_LIST_CONSUME_TIME, consumeOrder.consumeTime);
        map.put(PayConstants.CONSUME_LIST_CONSUME_STATUS, consumeOrder.consumeStatus);

        if (mIsStart) {
            mConsumeHistoryList.add(map);
        }
        else {
            mConsumeHistoryList.add(0, map);
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

    private void bindData()
    {

    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }

    private void showLoadingState() {
        mListView.showLoadingUI();
        //        UITools.checkLoadingState(this);
    }
}
