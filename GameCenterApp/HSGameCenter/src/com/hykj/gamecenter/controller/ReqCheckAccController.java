
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqCheckAccListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqCheckAccRecharge;
import com.hykj.gamecenter.protocol.Pay.ReqPayType;
import com.hykj.gamecenter.protocol.Pay.RspCheckAccRecharge;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author tomqian //支付方式
 */
public class ReqCheckAccController extends AbstractNetController {

    private final String TAG = "ReqCheckAccController";
    private final ReqCheckAccListener mListener;
    private final String mOpenId;
    private final String mToken;
    private final String mOrderNo;

    public ReqCheckAccController(ReqCheckAccListener listener, String openId, String token,
            String orderNo) {

        mListener = listener;
        mOpenId = openId;
        mToken = token;
        mOrderNo = orderNo;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqCheckAccRecharge builder = new ReqCheckAccRecharge();
        builder.openId = mOpenId;
        builder.token = mToken;
        builder.orderNo = mOrderNo;
        return ReqPayType.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_RECHARGE_CHECKACC;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_RECHARGE_RESPONSE_CHECKACC;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        try {
            RspCheckAccRecharge resBody = RspCheckAccRecharge.parseFrom(byteString);
            Logger.i(TAG, "resBody=====" + resBody.toString());
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqCheckAccSucceed(resBody);
            }
            else {
                if (mListener != null)
                    mListener.onReqCheckAccFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg "+ resMsg, "oddshou");
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
