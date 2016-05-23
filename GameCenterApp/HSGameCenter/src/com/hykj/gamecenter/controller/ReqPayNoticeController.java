
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqPayNoticeListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqPayNotice;
import com.hykj.gamecenter.protocol.Pay.RspPayNotice;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author haizichen 回应话费充值成功接口
 */
public class ReqPayNoticeController extends AbstractNetController {

    private final String TAG = "ReqPayNoticeController";
    private final ReqPayNoticeListener mListener;
    private final String mOpenId;//用户openId 
    private final String mOrderNo;//订单号(下单时生成的订单) 
    private final int mOperation;//业务类型 (1:话费充值,2:电影,3:团购,4:游戏中心充值,5:游戏中心消费)

    public ReqPayNoticeController(ReqPayNoticeListener listener, String openId, String orderNo,
            int operation) {
        mListener = listener;
        mOpenId = openId;
        mOrderNo = orderNo;
        mOperation = operation;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqPayNotice builder = new ReqPayNotice();
        //	if( !TextUtils.isEmpty( mOpenId ) ) {
        builder.openId = mOpenId;
        builder.orderNo = mOrderNo;
        builder.operation = mOperation;
        //	}
        return ReqPayNotice.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_PAY_NOTICE;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_PAY_NOTICE_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        try {
            RspPayNotice resBody = RspPayNotice.parseFrom(byteString);

            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqPayNoticeSucceed(resBody);
            }
            else {
                if (mListener != null)
                    mListener.onReqPayNoticeFailed(resCode, resMsg);
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
