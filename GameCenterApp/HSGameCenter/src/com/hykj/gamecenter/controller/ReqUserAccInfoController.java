package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserAccInfoListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqUserAccInfo;
import com.hykj.gamecenter.protocol.Pay.RspUserAccInfo;
import com.hykj.gamecenter.utils.Logger;

public class ReqUserAccInfoController extends AbstractNetController {
    
    private static final String TAG = "ReqUserAccInfoController";
    private ReqUserAccInfoListener mListener;
    private String mToken;
    private String mOpenId;

    public ReqUserAccInfoController(ReqUserAccInfoListener  listener , String openId , String token) {
        // TODO Auto-generated constructor stub
        this.mListener = listener;
        this.mOpenId = openId;
        this.mToken = token;
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqUserAccInfo builder = new ReqUserAccInfo();
        builder.openId = mOpenId;
        builder.token = mToken;
        return ReqUserAccInfo.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_ACCINFO;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_ACCINFO_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {
        // TODO Auto-generated method stub
        try
        {
            RspUserAccInfo resBody = RspUserAccInfo.parseFrom( byteString );

            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if( resCode == 0 ) {
            if( mListener != null )
                mListener.onReqUserAccInfoSucceed( resBody.userAccInfo);
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
