
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqRechargeListListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqAccRechargeList;
import com.hykj.gamecenter.protocol.Pay.ReqPayType;
import com.hykj.gamecenter.protocol.Pay.RspAccRechargeList;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author tomqian //获取充值列表
 */
public class ReqRechargeListController extends AbstractNetController {

    private final String TAG = "ReqRechargeListController";
    private final ReqRechargeListListener mListener;
    private final String mOpenId;
    private final String mToken;
    private final String mTimes;
    private final int mPage;

    public ReqRechargeListController(ReqRechargeListListener listener, String openId, String token
            , String times, int page) {

        mListener = listener;
        mOpenId = openId;
        mToken = token;
        mTimes = times;
        mPage = page;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqAccRechargeList builder = new ReqAccRechargeList();
        builder.openId = mOpenId;
        builder.token = mToken;
        builder.times = mTimes;
        builder.page = mPage;
        return ReqPayType.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_RECHARGE_LIST;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_RECHARGE_RESPONSE_LIST;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        try {
            RspAccRechargeList resBody = RspAccRechargeList.parseFrom(byteString);
            //            Logger.i(TAG, "resBody=====" + resBody.toString());
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqRechargeListSucceed(resBody);
            }
            else {
                if (mListener != null)
                    mListener.onReqRechargeListFailed(resCode, resMsg);
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
