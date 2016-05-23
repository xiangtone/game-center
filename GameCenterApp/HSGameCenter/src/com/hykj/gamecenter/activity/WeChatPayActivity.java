
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
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar;
import com.hykj.gamecenter.ui.widget.CSCommonActionBar.OnActionBarClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.RowCellLayout;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PayConstants;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayReq.Options;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

public class WeChatPayActivity extends Activity implements IWXAPIEventHandler {
    private CSCommonActionBar mActionBar;
    private Resources mRes = null;
    //微信支付
    private IWXAPI api;
    private int mPayTypeInt = PayConstants.KEY_PAY_TYPE_WECHATPAY;
    private String APP_ID = StatisticManager.getWechatpayAppId(); // APP_ID  应用从官方网站申请到的合法appId    正式账号  闪现

    private String CALL_BACK_CLASS_NAME = "com.hykj.gamecenter.activity.WeChatPayActivity";
    private boolean mIsWXPaying = false;
    private final static int WORD_ROW_COUNT = 4;
    private int itemWidth = 100;
    private int itemHeight = 100;
    private LinearLayout mLinearLayout;
    private LinearLayout.LayoutParams row_lp;
    private View mView;
    private Button mRechargePay;
    private EditText payText;
    private static final int MSG_RSP_SUBMITORDER = 1001;//成功提交订单
    private static final int MSG_RSP_ONNETERROR = 1002;
    private static final int MSG_RSP_FAILED = 1003;
    protected static final int MSG_WECHAT_PAY_SUCCESS = 1020;//微信支付成功
    private RspAccRecharge mRspSubmitOrder = null;
    private LinearLayout mRechargePayViewPage = null;
    private View mRechargeFinishViewPage = null;
    private TextView mtextPrice;
    private Button mRechargeFinish;
    private UserInfoManager mUserInfoManager;
    private String mOpenId = "";
    private String mToken;
    private ContentResolver mContentResolver;
    private final String TAG = "WeChatPayActivity";
    private String rechargeAmt = "";
    private String rechargeOrderNo = "";
    private static final String RECHARGE_AMOUNT_WECHAT = "recharge_amount_wechat";
    private static final String RECHARGE_ORDERNO_WECHAT = "recharge_orderno_wechat";
    private String priceSelected = "";

    //    private AccountInfo mCurrentAccount;//账户信息
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RSP_SUBMITORDER:
                    wechatPay();
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

                case MSG_WECHAT_PAY_SUCCESS:
                    wechatPaySuccess(msg.arg1);
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
        setContentView(R.layout.wechat_pay);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        mUserInfoManager = UserInfoManager.getInstance();
        AccountInfo accountInfo = mUserInfoManager.getCSUserInfo().getAccountInfo();
        if (accountInfo != null) {
            mOpenId = accountInfo.openId;
            mToken = accountInfo.token;
        }
        api = WXAPIFactory.createWXAPI(this, APP_ID);//微信支付
        api.handleIntent(getIntent(), this);//微信支付
        if (null != savedInstanceState) {
            rechargeAmt = savedInstanceState.getString(RECHARGE_AMOUNT_WECHAT);
            rechargeOrderNo = savedInstanceState.getString(RECHARGE_ORDERNO_WECHAT);
        }
        initView();
        initbindData();
        //上报进入充值详情页面
        ReportedInfo builder = new ReportedInfo();
        builder.statActId = ReportConstants.STATACT_ID_RECHARGE;
        builder.statActId2 = 1;//1=充值详情页面
        builder.ext1 = "" + PayConstants.KEY_PAY_TYPE_WECHATPAY;
        ReportConstants.getInstance().reportReportedInfo(builder);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        if (mRspSubmitOrder != null) {
            outState.putString(RECHARGE_AMOUNT_WECHAT, mRspSubmitOrder.rechargeAmt);
            outState.putString(RECHARGE_ORDERNO_WECHAT, mRspSubmitOrder.orderNo);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        Logger.e(TAG, "onNewIntent");
    }

    private void initView() {

        if ((mActionBar = (CSCommonActionBar) findViewById(R.id.ActionBar)) != null)
        {
            mActionBar.SetOnActionBarClickListener(actionBarListener);
            mActionBar.setSettingTipVisible(View.GONE);
            mActionBar.setSettingImage(R.drawable.csls_comm_actionbar_history_icon);
            mActionBar.setSettingButtonVisible(View.GONE);
        }
        mActionBar.setTitle(mRes.getString(R.string.recharge_style_wechatpay));
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

                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                if (!isPaySupported) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.wechat_pay_notsupport),
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                setRechargePayButtonEnabled(false);

                //上报充值按钮点击
                ReportedInfo builder = new ReportedInfo();
                builder.statActId = ReportConstants.STATACT_ID_RECHARGE;
                builder.statActId2 = 2;//2=充值按钮点击
                builder.ext1 = "" + PayConstants.KEY_PAY_TYPE_WECHATPAY;
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
                WeChatPayActivity.this.finish();
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
                + PayConstants.KEY_PAY_TYPE_STRING_WECHATPAY + "'";
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
        priceButton.setBackgroundResource(R.drawable.price_btn_selector);
        priceButton
                .setTextColor(getResources().getColorStateList(R.color.black));
        priceButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        //  priceButton.setHeight( (int)this.getResources( ).getDimension( R.dimen.o2o_recharge_button_height ) );

        if (p.equals(priceSelected) && priceSelected != "" && priceSelected.length() > 0) {
            priceButton.setTextColor(getResources().getColorStateList(
                    R.color.actionbar_bg_color));
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
                                R.color.actionbar_bg_color));
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
                    Intent intentSetting = new Intent(WeChatPayActivity.this,
                            RechargeListActivity.class);
                    startActivity(intentSetting);
                    WeChatPayActivity.this.finish();
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
        String params = "wx_appid=" + APP_ID;

        Logger.i(TAG, "OpenId=" + mOpenId);
        Logger.i(TAG, "Token=" + mToken);
        Logger.i(TAG, "amount=" + amount);
        Logger.i(TAG, "mPayTypeInt=" + mPayTypeInt);
        Logger.i(TAG, "params=" + params);

        if (amount > 0) {
            ReqRechargeController controller = new ReqRechargeController(mRechargeListener,
                    mOpenId,
                    mToken, amount, mPayTypeInt, params, "", "", "");
            controller.doRequest();
        }
    }

    private ReqRechargeListener mRechargeListener = new ReqRechargeListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Message msg = Message.obtain();
            msg.what = MSG_RSP_ONNETERROR;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onReqRechargeSucceed(RspAccRecharge rspOrder) {
            //  拿到数据
            mRspSubmitOrder = rspOrder;
            Logger.i(TAG, "mRspSubmitOrder=" + mRspSubmitOrder);
            mHandler.sendEmptyMessage(MSG_RSP_SUBMITORDER);

        }

        @Override
        public void onReqRechargeFailed(int statusCode, String errorMsg) {
            Logger.i(TAG, "statusCode=" + statusCode);
            Logger.i(TAG, "errorMsg=" + errorMsg);
            Message msg = Message.obtain();
            msg.what = MSG_RSP_FAILED;
            msg.arg1 = statusCode;
            msg.obj = errorMsg;
            mHandler.sendMessage(msg);
        }
    };

    /** 微信支付 */
    protected void wechatPay() {

        try {

            String params = /*mRspSubmitOrder.getPayorder().getParams()*/mRspSubmitOrder.orderExInfo;
            PayReq req = new PayReq();
            req.appId = APP_ID;
            req.partnerId = getParam(params, "partnerid");
            req.prepayId = getParam(params, "prepayid");
            req.nonceStr = getParam(params, "noncestr");
            req.timeStamp = getParam(params, "timestamp");
            req.packageValue = getParam(params, "package");
            req.sign = getParam(params, "sign");
            req.extData = "app data"; // optional
            Options op = new Options();
            op.callbackClassName = CALL_BACK_CLASS_NAME;
//            op.callbackFlags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
//                      op.callbackFlags = op.INVALID_FLAGS;
            req.options = op;

            Logger.i(TAG, "appid=" + req.appId);
            Logger.i(TAG, "noncestr=" + req.nonceStr);
            Logger.i(TAG, "partnerid=" + req.partnerId);
            Logger.i(TAG, "package=" + req.packageValue);
            Logger.i(TAG, "prepayid=" + req.prepayId);
            Logger.i(TAG, "timestamp=" + req.timeStamp);
            Logger.i(TAG, "sign=" + req.sign);

            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.registerApp(APP_ID);
//            boolean opened = api.openWXApp();
//            Logger.e(TAG, "openWXApp == " + opened);
            mIsWXPaying = true;
            api.sendReq(req);

        } catch (Exception e) {
            Log.e("TASK_GET_TOKEN", "异常：" + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setRechargePayButtonEnabled(true);
    }

    public String getParam(String params, String param) {

        String array[] = params.split("&");
        String myParam;
        for (int i = 0; i < array.length; i++) {
            String segment = array[i];
            int lastIndex = param.length() > segment.length() ? segment.length() : param.length();
            if (segment.substring(0, lastIndex).equals(param)) {
                myParam = array[i].substring(param.length() + 1, array[i].length());
                return myParam;
            }
        }

        return null;
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResp(BaseResp resp) {
        // TODO Auto-generated method stub
        Logger.i(TAG, "resp.getType()=" + resp.getType());
        //微信支付回调
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            Message msg = Message.obtain();
            msg.what = MSG_WECHAT_PAY_SUCCESS;
            msg.arg1 = resp.errCode;
            mHandler.sendMessage(msg);

        }
    }

    private void wechatPaySuccess(int status) {
        //微信支付成功与失败errCode
        /*0 成功  展示成功页面
        -1  错误  可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
        -2  用户取消    无需处理。发生场景：用户不支付了，点击取消，返回APP。*/
        Log.i(TAG, "status=" + status);
        setRechargePayButtonEnabled(true);
        switch (status) {
            case 0:
                paySuccess();
                break;
            case -1:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.recharge_pay_failed), Toast.LENGTH_SHORT)
                        .show();
                break;
            case -2:
                // TODO 用户取消
                //      paySuccess( );//test
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.recharge_pay_failed), Toast.LENGTH_SHORT)
                        .show();
                break;

            default:
                break;
        }
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
            //            mHandler.sendEmptyMessage(MSG_START_NEXT);
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
