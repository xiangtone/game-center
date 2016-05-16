package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqFeedbackListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Apps.ReqFeedback;
import com.hykj.gamecenter.protocol.UAC.RspOpenId;

public class ReqFeedbackController extends AbstractNetController {
    private String mFeedbackContent;
    private String mUserContact;
    private ReqFeedbackListener mListener;

    public ReqFeedbackController(String feedBackContent, String userContact, ReqFeedbackListener listen) {
        // TODO Auto-generated constructor stub
        mFeedbackContent = feedBackContent;
        mUserContact = userContact;
        mListener = listen;
    }

    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqFeedback builder = new ReqFeedback();
        builder.feedBackContent = mFeedbackContent;
        builder.userContact = mUserContact;
        return ReqFeedback.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_FEEDBACK;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_FEEDBACK_RESPONSE;
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
                    mListener.onReqFeedbackSucceed();
            }
            else {
                if (mListener != null)
                    mListener.onReqFailed(resCode, resMsg);
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
        return ServerUrl.getServerUrlApp();
    }

}
