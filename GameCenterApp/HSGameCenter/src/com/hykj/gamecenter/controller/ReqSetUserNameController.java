package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqSetUserNameListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.UAC.ReqSetUserName;
import com.hykj.gamecenter.protocol.UAC.RspSetUserName;
import com.hykj.gamecenter.utils.Logger;

public class ReqSetUserNameController extends AbstractNetController {
    private static final String TAG = "ReqSetUserNameController";
    private String mOpenId;
    private String mToken;
    private String mUserName;
    private ReqSetUserNameListener mListener;

    public ReqSetUserNameController(String openId, String token, String userName, ReqSetUserNameListener listen) {
        // TODO Auto-generated constructor stub
        mOpenId = openId;
        mToken = token;
        mUserName = userName;
        mListener = listen;
        
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqSetUserName builder = new ReqSetUserName();
        builder.openId = mOpenId;
        builder.token = mToken;
        builder.userName = mUserName;
        return ReqSetUserName.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_SET_USERNAME;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_SET_USERNAME_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {
        // TODO Auto-generated method stub
        try
        {
            RspSetUserName resBody = RspSetUserName.parseFrom( byteString );
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if( resCode == 0 ) {
            if( mListener != null )
                mListener.onReqSetUserNameSucceed();
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
        return ServerUrl.getAccountUacUrlApp();
    }

}
