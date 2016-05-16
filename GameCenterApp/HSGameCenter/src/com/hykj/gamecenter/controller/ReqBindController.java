package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqBindListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.UAC.ReqBind;
import com.hykj.gamecenter.protocol.UAC.RspBind;
import com.hykj.gamecenter.utils.Logger;

public class ReqBindController extends AbstractNetController {
    
    private final String TAG = "ReqBindController";
    private final ReqBindListener mListener;
    private final String mOpenId;
    private final String mMobile;
    private final String mValiCode;
    private int mSource;

    public ReqBindController( ReqBindListener listener , String openId , String mobile , String valiCode, int source)
    {
    mListener = listener;
    mOpenId = openId;
    mMobile = mobile;
    mValiCode = valiCode;
    mSource = source;
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqBind builder = new ReqBind();
        builder.openId = mOpenId;
        builder.mobile = mMobile;
        builder.verCode = mValiCode;
        builder.source = mSource;
        
        return ReqBind.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_BIND;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_BIND_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {
        // TODO Auto-generated method stub

        try
        {
            RspBind resBody = RspBind.parseFrom( byteString );

            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if( resCode == 0 ) {
            if( mListener != null )
                mListener.onReqBindSucceed( resBody.account);
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
        return ServerUrl.getAccountUacUrlApp();
    }

}
