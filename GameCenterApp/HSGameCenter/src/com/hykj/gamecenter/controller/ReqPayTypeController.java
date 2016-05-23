
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqPayTypeListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqPayType;
import com.hykj.gamecenter.protocol.Pay.RspPayType;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author tomqian //支付方式
 */
public class ReqPayTypeController extends AbstractNetController {

    private final String TAG = "ReqPayType";
    private final ReqPayTypeListener mListener;
    private final String mOpenId;

    public ReqPayTypeController(ReqPayTypeListener listener, String openId) {

        mListener = listener;
        mOpenId = openId;

    }

    @Override
    protected byte[] getRequestBody() {
        ReqPayType builder = new ReqPayType();
        builder.openId = mOpenId;
        return ReqPayType.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_REQPAYTYPE;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_REQPAYTYPE_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        try {
            RspPayType resBody = RspPayType.parseFrom(byteString);
            Logger.i(TAG, "resBody=====" + resBody.toString());
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqPayTypeSucceed(resBody);
            }
            else {
                if (mListener != null)
                    mListener.onReqPayTypeFailed(resCode, resMsg);
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
