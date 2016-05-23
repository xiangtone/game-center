package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqThirdLoginListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.UAC.ReqThirdLogin;
import com.hykj.gamecenter.protocol.UAC.RspBind;
import com.hykj.gamecenter.utils.Logger;

public class ReqThirdLoginControll extends AbstractNetController {
    
    private static final String TAG = "ReqThirdLoginControll";
    private ReqThirdLoginListener mListener;
    private String mType;
    private String mOpenId;
    private String mNickName;
    private String mHeadImgUrl;
    private int mAge;
    private int mGender;
    private int mSource;

    public ReqThirdLoginControll(ReqThirdLoginListener listener, String openId, String type, String nickName
            ,String headImgUrl, int age, int gender, int source ) {
        // TODO Auto-generated constructor stub
        mListener = listener;
        mOpenId = openId;
        mType = type;
        mNickName = nickName;
        mHeadImgUrl = headImgUrl;
        mAge = age;
        mGender = gender;
        mSource = source;
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqThirdLogin builder = new ReqThirdLogin();
        builder.thirdOpenId = mOpenId;
        builder.thirdType = mType;
        builder.nickName = mNickName;
        builder.headImgUrl = mHeadImgUrl;
        builder.age = mAge;
        builder.gender = mGender;
        builder.source = mSource;
        return ReqThirdLogin.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_BING_THIRD;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_BIND_THIRD_RESPONSE;
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
                mListener.onReqThirdLoginSucceed( resBody.account);
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
