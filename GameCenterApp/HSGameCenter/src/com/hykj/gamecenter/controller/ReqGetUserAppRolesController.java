package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGetUserAppRolesListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqGetUserAppRoles;
import com.hykj.gamecenter.protocol.Pay.RspGetUserAppRoles;
import com.hykj.gamecenter.utils.Logger;

public class ReqGetUserAppRolesController extends AbstractNetController {
    
    private static final String TAG = "ReqGetUserAppRolesController";
    private ReqGetUserAppRolesListener mListener;
    private String mToken;
    private int mAppId;
    private String mAppToken;
    private String mOpenId;

    public ReqGetUserAppRolesController(String openId, String token, int appId, String appToken,
            ReqGetUserAppRolesListener listen) {
        // TODO Auto-generated constructor stub
        mOpenId = openId;
        mToken = token;
        mAppId = appId;
        mAppToken = appToken;
        mListener = listen;
        
        
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqGetUserAppRoles builder= new ReqGetUserAppRoles();
        builder.openId = mOpenId;
        builder.token = mToken;
        builder.appId = mAppId;
        builder.appToken = mAppToken;
        return ReqGetUserAppRoles.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_APP_ROLES;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_APP_ROLES_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {
        // TODO Auto-generated method stub
        try
        {
            RspGetUserAppRoles resBody = RspGetUserAppRoles.parseFrom( byteString );

            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if( resCode == 0 ) {
            if( mListener != null )
                mListener.onReqGetUserAppRolesSucceed(resBody.roleInfo);
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
