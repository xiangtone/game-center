
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.controller.ProtocolListener.ReqPayNoticeListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqRechargeListener;
import com.hykj.gamecenter.controller.ReqPayNoticeController;
import com.hykj.gamecenter.controller.ReqRechargeController;
import com.hykj.gamecenter.db.CSSFContentProvider;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.RechargeAmountsColumns;
import com.hykj.gamecenter.protocol.Pay.RspAccRecharge;
import com.hykj.gamecenter.protocol.Pay.RspPayNotice;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.RowCellLayout;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PayConstants;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.payeco.android.plugin.PayecoPluginLoadingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import cs.widget.CSCommonActionBar;
//import cs.widget.CSCommonActionBar.OnActionBarClickListener;

public class UnionPayActivity extends Activity {
    private CSCommonActionBar mActionBar;
    private Resources mRes = null;
    //    private String CALL_BACK_CLASS_NAME = "com.niuwan.gamecenter.activity.AliPayActivity";
    //    private boolean mIsWXPaying = false;
    private final static int WORD_ROW_COUNT = 4;
    private int itemWidth = 100;
    private int itemHeight = 100;
    private LinearLayout mLinearLayout;
    private LinearLayout.LayoutParams row_lp;
    private View mView;
    private Button mRechargePay;
    private int mPayTypeInt = PayConstants.KEY_PAY_TYPE_UNIONPAY;
    private static final int MSG_RSP_SUBMITORDER = 1001;//成功提交订单
    private static final int MSG_RSP_ONNETERROR = 1002;
    private static final int MSG_RSP_FAILED = 1003;
    private static final int MSG_START_NEXT = 2000;//前端支付完成回应后，跳充值成功界面
    private EditText payText;
    private RspAccRecharge mRspSubmitOrder = null;
    private LinearLayout mRechargePayViewPage = null;
    private View mRechargeFinishViewPage = null;
    private TextView mtextPrice;
    private Button mRechargeFinish;
    private UserInfoManager mUserInfoManager;
    private String mOpenId = "";
    private String mToken;
    private ContentResolver mContentResolver;
    private final String TAG = "UnionPayActivity";
    private String rechargeAmt = "";
    private String rechargeOrderNo = "";
    private static final String RECHARGE_AMOUNT_UNION = "recharge_amount_union";
    private static final String RECHARGE_ORDERNO_UNION = "recharge_orderno_union";
    private String priceSelected = "";
    /**
     * @Fields payecoPayBroadcastReceiver : 易联支付插件广播
     */
    private BroadcastReceiver payecoPayBroadcastReceiver;
    //广播地址，用于接收易联支付插件支付完成之后回调客户端
    private final static String BROADCAST_PAY_END = "com.merchant.demo.broadcast";
    private final static String URL_PAY_NOTIFY = "";

    //    private AccountInfo mCurrentAccount;//账户信息
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RSP_SUBMITORDER:
                    getSubmitOrderData();
                    break;

                case MSG_RSP_ONNETERROR:
                    CSToast.show(App.getAppContext(), App.getAppContext().getResources()
                            .getString(R.string.error_msg_net_fail));
                    setRechargePayButtonEnabled(true);
                    break;
                case MSG_RSP_FAILED:
                    CSToast.showFailed(App.getAppContext(), msg.arg1, msg.obj.toString());
                    setRechargePayButtonEnabled(true);
                    break;
                case MSG_START_NEXT:
                    // 预留跳成功充值界面

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
        setContentView(R.layout.union_pay);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mUserInfoManager = UserInfoManager.getInstance();
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        if (accountInfo != null) {
            mOpenId = accountInfo.openId;
            mToken = accountInfo.token;
        }
        //初始化支付结果广播接收器
        initPayecoPayBroadcastReceiver();
        //注册支付结果广播接收器
        registerPayecoPayBroadcastReceiver();
        if (null != savedInstanceState) {
            rechargeAmt = savedInstanceState.getString(RECHARGE_AMOUNT_UNION);
            rechargeOrderNo = savedInstanceState.getString(RECHARGE_ORDERNO_UNION);
        }
        initView();
        initbindData();
        //上报进入充值详情页面
        ReportedInfo builder = new ReportedInfo();
        builder.statActId = ReportConstants.STATACT_ID_RECHARGE;
        builder.statActId2 = 1;//1=充值详情页面
        builder.ext1 = "" + PayConstants.KEY_PAY_TYPE_UNIONPAY;
        ReportConstants.getInstance().reportReportedInfo(builder);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub 
        if (mRspSubmitOrder != null) {
            outState.putString(RECHARGE_AMOUNT_UNION, mRspSubmitOrder.rechargeAmt);
            outState.putString(RECHARGE_ORDERNO_UNION, mRspSubmitOrder.orderNo);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setRechargePayButtonEnabled(true);
    }

    @Override
    protected void onDestroy() {
        unRegisterPayecoPayBroadcastReceiver();
        super.onDestroy();
    }

    /**
     * @Title registerPayecoPayBroadcastReceiver
     * @Description 注册广播接收器
     */
    private void registerPayecoPayBroadcastReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_PAY_END);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(payecoPayBroadcastReceiver, filter);
    }

    /**
     * @Title unRegisterPayecoPayBroadcastReceiver
     * @Description 注销广播接收器
     */
    private void unRegisterPayecoPayBroadcastReceiver() {

        if (payecoPayBroadcastReceiver != null) {
            unregisterReceiver(payecoPayBroadcastReceiver);
            payecoPayBroadcastReceiver = null;
        }
    }

    //初始化支付结果广播接收器
    private void initPayecoPayBroadcastReceiver() {
        payecoPayBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //接收易联支付插件的广播回调
                String action = intent.getAction();
                if (!BROADCAST_PAY_END.equals(action)) {

                    Logger.e(TAG, "接收到广播，但与注册的名称不一致[" + action + "]");
                    return;
                }

                //商户的业务处理
                String result = intent.getExtras().getString("upPay.Rsp");
                Logger.e(TAG, "接收到广播内容：" + result);

                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                    Logger.i(TAG, "json=" + json);
                    String code = json.getString("respCode");
                    if (json.has("respCode") && "0000".equals(code)) {
                        paySuccess();
                    } else {
                        if (json.has("respDesc")) {
                            CSToast.show(context, json.getString("respDesc"));
                            Logger.i(TAG, "respDesc=" + json.getString("respDesc"));
                        } else {
                            CSToast.show(context, "返回数据有误:");
                            Logger.i(TAG, "返回数据有误:");
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        Log.e("onNewIntent", "onNewIntent");

    }

    private void initView() {

        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null)
        {
            mActionBar.SetOnActionBarClickListener(actionBarListener);
            mActionBar.setSettingTipVisible(View.GONE);
            mActionBar.setSettingImage(R.drawable.csls_comm_actionbar_history_icon);
            mActionBar.setSettingButtonVisible(View.GONE);
        }
        mActionBar.setTitle(mRes.getString(R.string.recharge_style_unionpay));
        initFinishView();

        payText = (EditText) findViewById(R.id.pay_text);
        payText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Logger.i(TAG, "afterTextChanged s=" + s);
                if (s.length() == 0) {
                    return;
                }
                int count = Integer.valueOf(s.toString());
                if (count == 0 || count > 1000) {
                    Toast.makeText(getApplicationContext(), "单次充值金额需在1~1000之间，请重新输入",
                            Toast.LENGTH_SHORT).show();
                    payText.setText("");
                }
            }
        });
            itemWidth = mRes.getDimensionPixelSize(R.dimen.recharge_price_width);
            itemHeight = mRes.getDimensionPixelSize(R.dimen.recharge_price_height);
        mLinearLayout = (LinearLayout) findViewById(R.id.recharge_content);
        row_lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        row_lp.topMargin = mRes.getDimensionPixelSize(R.dimen.recharge_price_row_padding);
        mRechargePay = (Button) findViewById(R.id.recharge_pay);
        mRechargePay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                setRechargePayButtonEnabled(false);

                //上报充值按钮点击
                ReportedInfo builder = new ReportedInfo();
                builder.statActId = ReportConstants.STATACT_ID_RECHARGE;
                builder.statActId2 = 2;//2=充值按钮点击
                builder.ext1 = "" + PayConstants.KEY_PAY_TYPE_UNIONPAY;
                ReportConstants.getInstance().reportReportedInfo(builder);

                getRechargeData();
            }

        });
    }

    private void initFinishView() {
        //   充值完成界面

        mRechargePayViewPage = (LinearLayout) findViewById(R.id.recharge_pay_page);
        mRechargeFinishViewPage = ((ViewStub) findViewById(R.id.recharge_finish_page)).inflate();

        mtextPrice = (TextView) mRechargeFinishViewPage.findViewById(R.id.text_price);
        mRechargeFinish = (Button) mRechargeFinishViewPage.findViewById(R.id.recharge_finish);
        mRechargeFinishViewPage.setVisibility(View.GONE);
        mRechargeFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UnionPayActivity.this.finish();

            }
        });
    }

    private List<String> queryDb() {
        List<String> priceList = new ArrayList<String>();
        String where = RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE + "='"
                + PayConstants.KEY_PAY_TYPE_STRING_UNIONPAY + "'";
        Cursor cursor = mContentResolver.query(
                CSSFContentProvider.RECHARGEAMOUNTS_CONTENT_URI,
                RechargeActivity.RECHARGE_QUERY_PROJECTION, where, null,
                RechargeAmountsColumns.RECHARGE_AMOUNTS_PRICE + " ASC");//ASC升序  DESC降序

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                getDataFromCursor(cursor, priceList);
                cursor.moveToNext();
            }
        }
        Logger.i(TAG, "getDataFromCursor cursor=====" + cursor.getCount());
        Logger.i(TAG, "getDataFromCursor priceList=====" + priceList);

        //        int count = cursor.getCount();

        if (cursor != null) {
            cursor.close();
        }
        return priceList;
    }

    private void getDataFromCursor(Cursor cursor, List<String> priceList) {
        String price = String.valueOf(cursor.getInt(1));
        String showValue = cursor.getString(2);
        Logger.i(TAG, "getDataFromCursor showValue=====" + showValue);
        if (showValue.equals("1")) {
            priceSelected = price;
            Logger.i(TAG, "getDataFromCursor price=====" + price);
        }
        priceList.add(price);
    }

    public void initbindData() {
        List<String> priceList = queryDb();
        int rowIndex = 0;
        int index = 0;
        RowCellLayout rowLayout = null;
        RowCellLayout.LayoutParams lp = null;
        for (int i = 0; i < priceList.size(); i++) {
            rowIndex = rowIndex % WORD_ROW_COUNT;

            // 新建一行
            if (rowIndex == 0) {
                rowLayout = (RowCellLayout) View.inflate(this,
                        R.layout.lifecard_rowcell_layout, null);
                rowLayout.setUp(itemWidth, itemHeight, WORD_ROW_COUNT);
            }
            lp = new RowCellLayout.LayoutParams(rowIndex);
            rowLayout.addView(getInitItemView(priceList.get(i)), lp);

            // 加满了一排 或者 加到了最后一个
            if (index == (WORD_ROW_COUNT - 1) || index == (priceList.size() - 1)) {
                // 先加到list中，还要根据下发的参数设置几行可见
                if (index == (WORD_ROW_COUNT - 1)) {
                    mLinearLayout.addView(rowLayout);
                } else {
                    mLinearLayout.addView(rowLayout, row_lp);
                }
            }
            rowIndex++;
            index++;
        }
    }

    private View getInitItemView(final String p) {
        String yuan = mRes.getString(R.string.recharge_yuan);
        Button priceButton = new Button(this);
        priceButton.setGravity(Gravity.CENTER);
        priceButton.setSingleLine(true);
        priceButton.setText(p + yuan);
        //        Logger.i(TAG, "p=====" + p);
        //        priceButton.setBackgroundColor(Color.WHITE);
        priceButton.setBackgroundResource(R.drawable.price_btn_selector);
        priceButton
                .setTextColor(getResources().getColorStateList(R.color.black));
        priceButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        //  priceButton.setHeight( (int)this.getResources( ).getDimension( R.dimen.o2o_recharge_button_height ) );

        if (p.equals(priceSelected) && priceSelected != "" && priceSelected.length() > 0) {
            priceButton.setTextColor(getResources().getColorStateList(
                    R.color.normal_blue));
            priceButton.setSelected(true);

            payText.setText(p);
            payText.setSelection(p.length());
            mView = priceButton;
        }

        priceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v)
                        .setTextColor(getResources().getColorStateList(
                                R.color.normal_blue));
                v.setSelected(true);
                payText.setText(p);
                payText.setSelection(p.length());

                if (mView != null && mView != v) {
                    ((Button) mView).setTextColor(getResources()
                            .getColorStateList(R.color.black));
                    mView.setSelected(false);
                }
                mView = v;
            }
        });
        return priceButton;
    }

    private void setRechargePayButtonEnabled(boolean status) {
        if (status) {
            mRechargePay.setEnabled(status);
            mRechargePay.setText(getResources().getString(R.string.pay_bill_button));
        }
        else {
            mRechargePay.setEnabled(status);
            mRechargePay.setText(getResources().getString(R.string.pay_bill_paying_button));
        }

    }

    private final OnActionBarClickListener actionBarListener = new OnActionBarClickListener() {

        @Override
        public void onActionBarClicked(int position, View view) {

            switch (position) {
                case CSCommonActionBar.OnActionBarClickListener.RETURN_BNT:
                    onBackPressed();
                    break;
                case CSCommonActionBar.OnActionBarClickListener.SETTING_BNT:
                    Intent intentSetting = new Intent(UnionPayActivity.this,
                            RechargeListActivity.class);
                    startActivity(intentSetting);
                    UnionPayActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    // 获取充值订单号数据
    private void getRechargeData() {

        //        String openId = "f661f665775a43dc9a09e86507c81dad";
        //        String token = "dc76f80d6443b7305a518e23661420e7";
        String payAmount = payText.getText().toString();
        int amount = -1;
        if (payAmount.length() != 0) {
            amount = Integer.parseInt(payAmount);
        } else {
            CSToast.show(getApplicationContext(), "您输入的金额不正确，请重新输入");
            setRechargePayButtonEnabled(true);
            return;
        }
        //支付宝单位为分，这里amount全部需要换算为  元/分 单位
        amount = amount * 100;

        Logger.e(TAG, "OpenId=" + mOpenId);
        Logger.e(TAG, "Token=" + mToken);
        Logger.e(TAG, "amount=" + amount);
        Logger.e(TAG, "mPayTypeInt=" + mPayTypeInt);

        if (amount > 0) {
            ReqRechargeController controller = new ReqRechargeController(mRechargeListener,
                    mOpenId,
                    mToken, amount, mPayTypeInt, "", "", "", "");
            controller.doRequest();
        }
    }

    private ReqRechargeListener mRechargeListener = new ReqRechargeListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e(TAG, "onNetError errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_ONNETERROR;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqRechargeSucceed(RspAccRecharge rspOrder) {
            //  拿到数据
            Logger.e(TAG, "rspSubmitOrder=" + rspOrder);
            mRspSubmitOrder = rspOrder;
            mHandler.sendEmptyMessage(MSG_RSP_SUBMITORDER);

        }

        @Override
        public void onReqRechargeFailed(int statusCode, String errorMsg) {
            Logger.e(TAG, "onReqRechargeFailed errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_FAILED;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    };

    /** 银联支付 */
    private void getSubmitOrderData() {
        String exinfo = mRspSubmitOrder.orderExInfo;
        JSONObject json = null;
        try {
            //解析响应数据
            json = new JSONObject(exinfo);
            Logger.i(TAG, "json=" + json);

            //校验返回结果
            if (!json.has("RetCode") || !"0000".equals(json.getString("RetCode"))) {
                if (json.has("RetMsg")) {
                    Toast.makeText(UnionPayActivity.this, json.getString("RetMsg"),
                            Toast.LENGTH_LONG).show();
                    Logger.i(TAG, "RetMsg=" + json.getString("RetMsg"));
                } else {
                    Toast.makeText(UnionPayActivity.this, "返回数据有误:" + exinfo, Toast.LENGTH_LONG)
                            .show();
                    Logger.i(TAG, "返回数据有误:" + exinfo);
                }
                return;
            }

            json.remove("RetCode");//RetCode参数不需要传递给易联支付插件
            json.remove("RetMsg");//RetMsg参数不需要传递给易联支付插件

            String upPayReqString = json.toString();
            Logger.i(TAG, "请求易联支付插件，参数：" + upPayReqString);

            //跳转至易联支付插件
            Intent intent = new Intent(UnionPayActivity.this, PayecoPluginLoadingActivity.class);
            intent.putExtra("upPay.Req", upPayReqString);
            intent.putExtra("Broadcast", BROADCAST_PAY_END); //广播接收地址
            intent.putExtra("Environment", "01"); // 00: 测试环境, 01: 生产环境
            startActivity(intent);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Logger.i(TAG, "解析处理失败！:" + e);
            e.printStackTrace();
        }

        //解析数据
        String noticeUrl = mRspSubmitOrder.noticeUrl; //回调地址
        //        PayOrder payOrder = mRspSubmitOrder.getPayorder();
        String orderNo = mRspSubmitOrder.orderNo; //订单号(下单时生成的订单)
        int ammount = Integer.valueOf(mRspSubmitOrder.rechargeAmt);
        float salePrice = (float) (ammount / 100.00);
        String price = "" + salePrice;

        Logger.i(TAG, "noticeUrl=" + noticeUrl);
        Logger.i(TAG, "price=" + price);
        Logger.i(TAG, "orderNo=" + orderNo);

    }

    /** 成功支付 */
    private void paySuccess() {
        mRechargePayViewPage.setVisibility(View.GONE);
        mRechargeFinishViewPage.setVisibility(View.VISIBLE);
        mActionBar.setSettingButtonVisible(View.VISIBLE);
        mRechargeFinishViewPage.setFocusable(true);
        mRechargeFinishViewPage.setFocusableInTouchMode(true);
        mRechargeFinishViewPage.requestFocus();
        int ammount = -1;
        Logger.i(TAG, "rechargeAmt.length()=" + rechargeAmt.length());
        if (mRspSubmitOrder != null) {
            ammount = Integer.valueOf(mRspSubmitOrder.rechargeAmt);
            mtextPrice.setText(this.getResources().getString(R.string.recharge_text_finish,
                    (ammount / 10)));
            goPayNotice(mRspSubmitOrder);
            return;
        }
        if (rechargeAmt.length() > 0) {
            ammount = Integer.valueOf(rechargeAmt);
            Logger.i(TAG, "ammount=" + ammount);
            mtextPrice.setText(this.getResources().getString(R.string.recharge_text_finish,
                    (ammount / 10)));
        } else {
            mtextPrice.setText(this.getResources().getString(R.string.recharge_text_finished));
        }
        goPayNotice();
    }

    //支付完成回应
    private void goPayNotice() {
        //        String openId = "f661f665775a43dc9a09e86507c81dad";
        Logger.i(TAG, "rechargeOrderNo.length()=" + rechargeOrderNo.length());
        if (rechargeOrderNo.length() > 0) {
            String orderNo = rechargeOrderNo; //订单号(下单时生成的订单)
            int operation = PayConstants.KEY_PAY_OPERATION_RECHARGE;
            //业务类型 (1:话费充值,2:电影,3:团购,4:游戏中心充值,5:游戏中心消费)
            Logger.i(TAG, "goPayNotice orderNo=" + orderNo);

            ReqPayNoticeController controller = new ReqPayNoticeController(mReqPayNoticeListener,
                    mOpenId, orderNo,
                    operation);
            controller.doRequest();
        } else {
            return;
        }
    }

    private void goPayNotice(RspAccRecharge acc) {
        String orderNo = acc.orderNo; //订单号(下单时生成的订单)
        int operation = PayConstants.KEY_PAY_OPERATION_RECHARGE;
        //业务类型 (1:话费充值,2:电影,3:团购,4:游戏中心充值,5:游戏中心消费)
        Logger.i(TAG, "goPayNotice orderNo=" + orderNo);

        ReqPayNoticeController controller = new ReqPayNoticeController(mReqPayNoticeListener,
                mOpenId, orderNo,
                operation);
        controller.doRequest();

    }

    private ReqPayNoticeListener mReqPayNoticeListener = new ReqPayNoticeListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Message msg = Message.obtain();
            msg.what = MSG_RSP_ONNETERROR;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqPayNoticeSucceed(RspPayNotice rspPayNotice) {
            //  拿到数据
            Logger.i(TAG, "rspPayNotice=" + rspPayNotice);
            mHandler.sendEmptyMessage(MSG_START_NEXT);
        }

        @Override
        public void onReqPayNoticeFailed(int statusCode, String errorMsg) {
            Message msg = Message.obtain();
            msg.what = MSG_RSP_FAILED;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    };

}
