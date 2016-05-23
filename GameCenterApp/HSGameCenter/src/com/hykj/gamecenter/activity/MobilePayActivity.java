
package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.account.UserInfoManager;
import com.hykj.gamecenter.controller.ProtocolListener.ReqRechargeListener;
import com.hykj.gamecenter.controller.ReqRechargeController;
import com.hykj.gamecenter.db.CSSFContentProvider;
import com.hykj.gamecenter.db.CSSFDatabaseHelper.RechargeAmountsColumns;
import com.hykj.gamecenter.protocol.Pay.RspAccRecharge;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.UAC.AccountInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.RowCellLayout;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PayConstants;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

//import cs.widget.CSCommonActionBar;
//import cs.widget.CSCommonActionBar.OnActionBarClickListener;

public class MobilePayActivity extends Activity {
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
    private int mPayTypeInt = -1;
    private static final int MSG_RSP_SUBMITORDER = 1001;//成功提交订单
    private static final int MSG_RSP_ONNETERROR = 1002;
    private static final int MSG_RSP_FAILED = 1003;
    private EditText pay_card;
    private EditText pay_password;
    private RspAccRecharge mRspSubmitOrder = null;
    private LinearLayout mRechargePayViewPage = null;
    private View mRechargeFinishViewPage = null;
    private TextView mtextPrice;
    private TextView payTips;
    private Button mRechargeFinish;
    private UserInfoManager mUserInfoManager;
    private String mOpenId = "";
    private String mToken;
    private final String TAG = "MobilePayActivity";
    private String mAmountText = "";
    private ContentResolver mContentResolver;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RSP_SUBMITORDER:
                    paySuccess();
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
        setContentView(R.layout.mobile_pay);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mUserInfoManager = UserInfoManager.getInstance();
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        if (accountInfo != null) {
            mOpenId = accountInfo.openId;
            mToken = accountInfo.token;
        }
        mPayTypeInt = getIntent().getIntExtra(PayConstants.KEY_PAYTYPE_MOBILE, -1);
        Logger.i(TAG, "mPayTypeInt=====" + mPayTypeInt);
        initView();
        initbindData();
        //上报进入充值详情页面
        ReportedInfo builder = new ReportedInfo();
        builder.statActId = ReportConstants.STATACT_ID_RECHARGE;
        builder.statActId2 = 1;//1=充值详情页面
        builder.ext1 = "" + mPayTypeInt;
        ReportConstants.getInstance().reportReportedInfo(builder);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setRechargePayButtonEnabled(true);
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
        mActionBar.setTitle(mRes.getString(R.string.recharge_style_mobilepay));
        initFinishView();

        pay_card = (EditText) findViewById(R.id.pay_card);
        pay_password = (EditText) findViewById(R.id.pay_password);
        //        pay_password.setInputType(0x81);
        payTips = (TextView) findViewById(R.id.pay_tips);
        StringUtils.setTextColor(8, 20,
                getApplicationContext().getResources().getColor(R.color.editcolor_red), payTips);

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
                builder.ext1 = "" + mPayTypeInt;
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
                MobilePayActivity.this.finish();

            }
        });
    }

    private List<String> queryDb() {
        List<String> priceList = new ArrayList<String>();
        String type = "" + mPayTypeInt;
        String where = RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE + "='"
                + type + "'";
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
        Logger.i(TAG, "getDataFromCursor price=====" + price);
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

            if (priceList.size() <= (WORD_ROW_COUNT * 2)) {
                // 加满了一排 或者 加到了最后一个
                if (index == (WORD_ROW_COUNT - 1) /*|| index == (p.length - WORD_ROW_COUNT)*/
                        || index == (priceList.size() - 1)) {
                    // 先加到list中，还要根据下发的参数设置几行可见
                    if (index == (WORD_ROW_COUNT - 1)) {
                        mLinearLayout.addView(rowLayout);
                    } else {
                        mLinearLayout.addView(rowLayout, row_lp);
                    }
                }
            } else {
                if (index == (WORD_ROW_COUNT - 1) || index == (priceList.size() - WORD_ROW_COUNT)
                        || index == (priceList.size() - 1)) {
                    // 先加到list中，还要根据下发的参数设置几行可见
                    if (index == (WORD_ROW_COUNT - 1)) {
                        mLinearLayout.addView(rowLayout);
                    } else {
                        mLinearLayout.addView(rowLayout, row_lp);
                    }
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
        priceButton.setBackgroundResource(R.drawable.price_btn_selector);
        priceButton
                .setTextColor(getResources().getColorStateList(R.color.black));
        priceButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        priceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v)
                        .setTextColor(getResources().getColorStateList(
                                R.color.normal_blue));
                v.setSelected(true);
                mAmountText = p;

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
                    Intent intentSetting = new Intent(MobilePayActivity.this,
                            RechargeListActivity.class);
                    startActivity(intentSetting);
                    MobilePayActivity.this.finish();
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
        String payNo = pay_card.getText().toString();
        String payPwd = pay_password.getText().toString();

        int amount = -1;
        String payAmount = mAmountText;
        if (payAmount.length() != 0) {
            amount = Integer.parseInt(payAmount);
        } else {
            CSToast.show(getApplicationContext(), "请选择手机充值卡的面额");
            setRechargePayButtonEnabled(true);
            return;
        }
        if (payNo.length() == 0) {
            CSToast.show(getApplicationContext(), "请输入手机充值卡的号码");
            setRechargePayButtonEnabled(true);
            return;
        }
        if (payPwd.length() == 0) {
            CSToast.show(getApplicationContext(), "请输入手机充值卡的密码");
            setRechargePayButtonEnabled(true);
            return;
        }
        if (payNo.length() > 20 || payNo.length() < 10) {
            CSToast.show(getApplicationContext(), "请输入有效的手机充值卡号码");
            pay_card.setText("");
            setRechargePayButtonEnabled(true);
            return;
        }
        if (payPwd.length() > 20 || payPwd.length() < 8) {
            CSToast.show(getApplicationContext(), "请输入有效的手机充值卡密码");
            pay_password.setText("");
            setRechargePayButtonEnabled(true);
            return;
        }

        //支付宝单位为分，这里amount全部需要换算为  元/分 单位
        amount = amount * 100;

        Logger.e(TAG, "OpenId=" + mOpenId);
        Logger.e(TAG, "Token=" + mToken);
        Logger.e(TAG, "amount=" + amount);
        Logger.e(TAG, "mPayTypeInt=" + mPayTypeInt);

        if (amount > 0 && mPayTypeInt != -1) {
            ReqRechargeController controller = new ReqRechargeController(mRechargeListener,
                    mOpenId,
                    mToken, amount, mPayTypeInt, "", payNo, payPwd, "");
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
            //rspSubmitOrder=notice_url: "http://niuwan.vicp.cc:8061/pay_api/szfback"
            //order_desc: ""
            //order_ex_info: ""
            //order_no: "20150722-151525-4201507221047561679"
            //order_time: "20150722104756"
            //recharge_amt: "2000"
            //rescode: 0
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

    /** 成功支付 */
    private void paySuccess() {
        mRechargePayViewPage.setVisibility(View.GONE);
        mRechargeFinishViewPage.setVisibility(View.VISIBLE);
        mActionBar.setSettingButtonVisible(View.VISIBLE);
        mRechargeFinishViewPage.setFocusable(true);
        mRechargeFinishViewPage.setFocusableInTouchMode(true);
        mRechargeFinishViewPage.requestFocus();
        int ammount = Integer.valueOf(mRspSubmitOrder.rechargeAmt);
        Logger.i(TAG, "ammount=" + ammount);
        mtextPrice.setText(this.getResources().getString(R.string.recharge_text_finish,
                (ammount / 10)));
    }

}
