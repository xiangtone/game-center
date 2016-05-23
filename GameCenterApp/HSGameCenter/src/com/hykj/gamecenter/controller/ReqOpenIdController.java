package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqOpenIdListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.UAC.ReqOpenId;
import com.hykj.gamecenter.protocol.UAC.RspOpenId;
import com.hykj.gamecenter.utils.Logger;

public class ReqOpenIdController extends AbstractNetController {
    
    private static final String TAG = "ReqOpenIdController";
    private ReqOpenIdListener mListener;
    private String mOpenid;
    private String mToken;

    public ReqOpenIdController(String openid, String token, ReqOpenIdListener listen) {
        // TODO Auto-generated constructor stub
         this.mListener = listen;
         this.mOpenid = openid;
         this.mToken = token;
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqOpenId builder = new ReqOpenId();
        builder.openId = mOpenid;
        builder.token = mToken;
        
        return ReqOpenId.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_REQOPENID;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_REQOPENID_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {
        // TODO Auto-generated method stub
        try {
            RspOpenId resBody = RspOpenId.parseFrom(byteString);
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqOpenIdSucceed(resBody.account);
            }
            else {
                if (mListener != null)
                    mListener.onReqFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg "+ resMsg, "oddshou");
            }

        } catch (InvalidProtocolBufferNanoException e) {
            e.printStackTrace();
            handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
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
