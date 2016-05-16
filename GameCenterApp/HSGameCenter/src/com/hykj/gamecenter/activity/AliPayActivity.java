
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
import android.text.Editable;
import android.text.TextUtils;
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

import com.alipay.sdk.app.PayTask;
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
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.RowCellLayout;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PayConstants;
import com.hykj.gamecenter.utils.Result;
import com.hykj.gamecenter.utils.SignUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//import cs.widget.CSCommonActionBar;
//import cs.widget.CSCommonActionBar.OnActionBarClickListener;

public class AliPayActivity extends Activity {
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
    public static final int PAY_TYPE_ALIPAY = 1;
    private int mPayTypeInt = PayConstants.KEY_PAY_TYPE_ALIPAY;
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
    private AlipayHandler mHandler;
    private final static String TAG = "AliPayActivity";
    private ContentResolver mContentResolver;
    private String rechargeAmt = "";
    private String rechargeOrderNo = "";
    private static final String RECHARGE_AMOUNT_ALI = "recharge_amount_ali";
    private static final String RECHARGE_ORDERNO_ALI = "recharge_orderno_ali";
    private String priceSelected = "";

    private static class AlipayHandler extends Handler {
        WeakReference<AliPayActivity> activity;

        public AlipayHandler(AliPayActivity acti) {
            // TODO Auto-generated constructor stub
            activity = new WeakReference<AliPayActivity>(acti);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            AliPayActivity mActivity = activity.get();
            if (null == mActivity) {
                Logger.d(TAG, "mActivity == null");
                return;
            }
            switch (msg.what) {
                case PAY_TYPE_ALIPAY: {

                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;
                    Logger.e(TAG, "resultStatus=" + resultStatus);
                    //返回码说明  9000 == 订单支付成功   8000 == 正在处理中   4000 == 订单支付失败
                    //                6001 == 用户中途取消   6002 == 网络连接出错 

                    mActivity.setRechargePayButtonEnabled(true);
                    //刷新列表
                    //          refreshHistoryListItem( );
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        mActivity.paySuccess();
                    }
                    else {
                        int status = Integer.valueOf(resultStatus);
                        switch (status) {
                            case 4000:
                                CSToast.show(App.getAppContext(), App.getAppContext()
                                        .getResources()
                                        .getString(R.string.alipay_error_msg_payfailed));
                                break;
                            case 6001:
                                CSToast.show(App.getAppContext(), App.getAppContext()
                                        .getResources()
                                        .getString(R.string.alipay_error_msg_paycancled));
                                break;
                            case 6002:
                                CSToast.show(App.getAppContext(), App.getAppContext()
                                        .getResources()
                                        .getString(R.string.alipay_error_msg_netfailed));
                                break;
                            case 8000:
                                CSToast.show(App.getAppContext(), App.getAppContext()
                                        .getResources()
                                        .getString(R.string.alipay_error_msg_payconfirm));
                                break;
                        }
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    }
                    break;
                }
                case MSG_RSP_SUBMITORDER:
                    mActivity.getSubmitOrderData();
                    break;

                case MSG_RSP_ONNETERROR:
                    CSToast.show(App.getAppContext(), App.getAppContext().getResources()
                            .getString(R.string.error_msg_net_fail));
                    mActivity.setRechargePayButtonEnabled(true);
                    break;
                case MSG_RSP_FAILED:
                    CSToast.showFailed(App.getAppContext(), msg.arg1, msg.obj.toString());
                    mActivity.setRechargePayButtonEnabled(true);
                    break;
                case MSG_START_NEXT:
                    // 预留跳成功充值界面

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
        mContentResolver = this.getContentResolver();
        setContentView(R.layout.ali_pay);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mHandler = new AlipayHandler(AliPayActivity.this);
        mUserInfoManager = UserInfoManager.getInstance();
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        if (accountInfo != null) {
            mOpenId = accountInfo.openId;
            mToken = accountInfo.token;
        }
        if (null != savedInstanceState) {
            rechargeAmt = savedInstanceState.getString(RECHARGE_AMOUNT_ALI);
            rechargeOrderNo = savedInstanceState.getString(RECHARGE_ORDERNO_ALI);
        }
        initView();
        initbindData();
        //上报进入充值详情页面
        ReportedInfo builder = new ReportedInfo();
        builder.statActId = ReportConstants.STATACT_ID_RECHARGE;
        builder.statActId2 = 1;//1=充值详情页面
        builder.ext1 = "" + PayConstants.KEY_PAY_TYPE_ALIPAY;
        ReportConstants.getInstance().reportReportedInfo(builder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRechargePayButtonEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub 
        if (mRspSubmitOrder != null) {
            outState.putString(RECHARGE_AMOUNT_ALI, mRspSubmitOrder.rechargeAmt);
            outState.putString(RECHARGE_ORDERNO_ALI, mRspSubmitOrder.orderNo);
        }
        super.onSaveInstanceState(outState);
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
        mActionBar.setTitle(mRes.getString(R.string.recharge_style_alipay));
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

                //开始支付前，判断网络
                /*if (!APNUtil.isNetworkAvailable(App.getAppContext())) {
                    return;
                }*/

                setRechargePayButtonEnabled(false);

                //上报充值按钮点击
                ReportedInfo builder = new ReportedInfo();
                builder.statActId = ReportConstants.STATACT_ID_RECHARGE;
                builder.statActId2 = 2;//2=充值按钮点击
                builder.ext1 = "" + PayConstants.KEY_PAY_TYPE_ALIPAY;
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
                //                mRechargeFinishViewPage.setVisibility(View.GONE);
                //                mRechargePayViewPage.setVisibility(View.VISIBLE);
                AliPayActivity.this.finish();
                /*Intent it = new Intent(O2OPhonePayActivity.this, O2OPayOrderListAtivity.class);
                startActivity(it);
                overridePendingTransition(R.anim.o2o_zoom_null, R.anim.o2o_zoom_null);
                mRechargeFinishViewPage.setVisibility(View.GONE);
                //      mRechargePayViewPage.setVisibility( View.VISIBLE );
                mHandler.sendEmptyMessageDelayed(MSG_FINISH_SHOW_RECHARGE, 100);*/

            }
        });
    }

    private List<String> queryDb() {
        List<String> priceList = new ArrayList<String>();
        String where = RechargeAmountsColumns.RECHARGE_AMOUNTS_TYPE + "='"
                + PayConstants.KEY_PAY_TYPE_STRING_ALIPAY + "'";
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

        //        int count = cursor.getCount();13828871910

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
                //                mParValue = 0;
                //                mlast = ((Button) v).getText().toString();

                //隐藏键盘
                //                closeInputMethod();
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
                    Intent intentSetting = new Intent(AliPayActivity.this,
                            RechargeListActivity.class);
                    startActivity(intentSetting);
                    AliPayActivity.this.finish();
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

    /** 支付宝支付 */
    private void getSubmitOrderData() {
        //解析数据
        String noticeUrl = mRspSubmitOrder.noticeUrl; //回调地址
        //        PayOrder payOrder = mRspSubmitOrder.getPayorder();
        String orderNo = mRspSubmitOrder.orderNo; //订单号(下单时生成的订单)
        Logger.i(TAG, "" + mRspSubmitOrder.rechargeAmt);
        int ammount = Integer.valueOf(mRspSubmitOrder.rechargeAmt);
        Logger.i(TAG, "ammount==" + ammount);
        float salePrice = (float) (ammount / 100.00);
        Logger.i(TAG, "salePrice==" + salePrice);
        String price = "" + salePrice;
        float parValue = ammount / 100;
        String subject = mRes.getString(R.string.recharge_account) + parValue
                + mRes.getString(R.string.recharge_yuan);
        String body = mRes.getString(R.string.recharge_description) + parValue
                + mRes.getString(R.string.recharge_yuan) + "——";

        Logger.i(TAG, "noticeUrl=" + noticeUrl);
        Logger.i(TAG, "price=" + price);
        Logger.i(TAG, "orderNo=" + orderNo);

        String submitOrderInfo = getOrderInfo(subject, body, price, orderNo, noticeUrl);//生成支付订单

        //        Logger.e(TAG, "submitOrderInfo=" + submitOrderInfo);

        //在跳支付之前，先保存数据库
        //        insertDb(mobile, mPhoneContactNameText.getText().toString());
        // 刷新列表
        //        addHistoryListItem(mAutoCompleteTextView.getText().toString(), mPhoneContactNameText
        //                .getText().toString());

        pay(submitOrderInfo);//跳到支付
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price, String orderNo,
            String noticeUrl) {
        //  Log.e( "getOrderInfo" , "getOrderInfo" );
        // 合作者身份ID
        String orderInfo = "partner=" + "\"" + StatisticManager.PARTNER + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + StatisticManager.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + noticeUrl + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(String orderInfo) {

        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(AliPayActivity.this);
                // 调用支付接口
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = PAY_TYPE_ALIPAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * sign the order info. 对订单信息进行签名
     * 
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, StatisticManager.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
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
