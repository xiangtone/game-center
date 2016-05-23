
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqRechargeListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqAccRecharge;
import com.hykj.gamecenter.protocol.Pay.RspAccRecharge;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author tomqian 请求充值订单接口
 */
public class ReqRechargeController extends AbstractNetController {

    private final String TAG = "ReqRechargeController";
    private final ReqRechargeListener mListener;
    private final String mOpenId;//用户openId
    private final String mToken; //验证码
    private final int mPrice; //金额(单位:分)  
    private final int mPayType;//支付方式
    private final String mParams;//扩展信息
    private final String mCardNo;//卡号
    private final String mCardPwd;//卡密
    private final String mCardType;//充值卡类型,即标示,如移动联通电信等

    public ReqRechargeController(ReqRechargeListener listener, String openId, String token,
            int price, int payType, String params, String cardNo, String cardPwd, String cardType) {

        mListener = listener;
        mOpenId = openId;
        mToken = token;
        mPrice = price;
        mPayType = payType;
        mParams = params;
        mCardNo = cardNo;
        mCardPwd = cardPwd;
        mCardType = cardType;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqAccRecharge builder = new ReqAccRecharge();
        builder.openId = mOpenId;
        builder.token = mToken;
        builder.rechargeAmt = mPrice;
        builder.paytype = mPayType;
        if (mParams.length() > 0) {
            builder.exinfo = mParams;
        }
        builder.cardNo = mCardNo;
        builder.cardPwd = mCardPwd;
        builder.cardType = mCardType;
        Logger.i(TAG, "builder.openId=====" + builder.openId);
        Logger.i(TAG, "builder.token=====" + builder.token);
        Logger.i(TAG, "builder.rechargeAmt=====" + builder.rechargeAmt);
        Logger.i(TAG, "builder.paytype=====" + builder.paytype);
        Logger.i(TAG, "builder.exinfo=====" + builder.exinfo);
        Logger.i(TAG, "builder.cardNo=====" + builder.cardNo);
        Logger.i(TAG, "builder.cardPwd =====" + builder.cardPwd);
        Logger.i(TAG, "builder.cardType =====" + builder.cardType);
        Logger.i(TAG, "builder =====" + builder);
        return ReqAccRecharge.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_RECHARGE;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_RECHARGE_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        try {
            RspAccRecharge resBody = RspAccRecharge.parseFrom(byteString);
            //            Logger.i(TAG, "resBody=====" + resBody.toString());
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqRechargeSucceed(resBody);
            }
            else {
                if (mListener != null)
                    mListener.onReqRechargeFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg " + resMsg, "oddshou");
            }

        } catch (InvalidProtocolBufferNanoException e) {
            e.printStackTrace();
            handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
        }
    }

    @Override
    protected void handleResponseError(int errCode, String strErr) {
        if (mListener == null)
            return;

        mListener.onNetError(errCode, strErr);
    }

    @Override
    protected String getServerUrl() {
        Logger.i(TAG, "url=====" + ServerUrl.getAccountPayUrlApp());
        return ServerUrl.getAccountPayUrlApp();
    }
}
