package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqConsumeListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqConsume;
import com.hykj.gamecenter.protocol.Pay.RspConsume;
import com.hykj.gamecenter.sdk.entry.ConsumeRequestInfo;
import com.hykj.gamecenter.utils.Logger;

public class ReqConsumeController extends AbstractNetController {
    private static final String TAG = "ReqConsumeController";
    private ConsumeRequestInfo mRqstInfo;
    private ReqConsumeListener mListener;

    public ReqConsumeController(ConsumeRequestInfo rqstInfo, ReqConsumeListener listener) {
        // TODO Auto-generated constructor stub
        mListener = listener;
        mRqstInfo = rqstInfo;
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqConsume builder = new ReqConsume();
        builder.openId = mRqstInfo.openId;
        builder.token = mRqstInfo.token;
        builder.appId = mRqstInfo.appId;
        builder.appToken = mRqstInfo.appToken;
        builder.roleId = mRqstInfo.roleId;
        builder.roleToken = mRqstInfo.roleToken;
        builder.consumeNewCoin = mRqstInfo.consumeNewCoin;
        builder.cpOrderNo = mRqstInfo.cpOrderNo;
        builder.productCode = mRqstInfo.productCode;
        builder.productName = mRqstInfo.productName;
        builder.productCount = mRqstInfo.productCount;
        builder.packName = mRqstInfo.packName;
        builder.exInfo = mRqstInfo.exInfo;
        return ReqConsume.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_CONSUME;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_CONSUME_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {
        // TODO Auto-generated method stub
        try
        {
            RspConsume resBody = RspConsume.parseFrom( byteString );

            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if( resCode == 0 ) {
            if( mListener != null )
                mListener.onConsumeSucceed(resBody);
            }
            else {
            if( mListener != null )
                mListener.onReqFailed( resCode , resMsg );
            Logger.e(TAG, TAG + resCode + " resMsg "+ resMsg, "oddshou");
            }

        }
        catch ( InvalidProtocolBufferNanoException e )
        {
            e.printStackTrace( );
            handleResponseError( ERROR.ERROR_BAD_PACKET , e.getMessage( ) );
        }
    }

    @Override
    protected void handleResponseError(int errCode, String strErr) {
        // TODO Auto-generated method stub
        if (mListener == null)
            return;

        mListener.onNetError(errCode, strErr);
    }

    @Override
    protected String getServerUrl() {
        // TODO Auto-generated method stub
        return ServerUrl.getAccountPayUrlApp();
    }

}
