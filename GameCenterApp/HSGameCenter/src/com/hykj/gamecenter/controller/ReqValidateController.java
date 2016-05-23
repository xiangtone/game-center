
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqValidateListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.UAC.ReqValidate;
import com.hykj.gamecenter.protocol.UAC.RspValidate;
import com.hykj.gamecenter.utils.Logger;

public class ReqValidateController extends AbstractNetController {
    private final String TAG = "ReqValidateController";
    private final ReqValidateListener mListener;
    private final String mOpenId;
    private final String mMobile;

    public ReqValidateController(ReqValidateListener listener, String openId, String mobile)
    {
        mListener = listener;
        mOpenId = openId;
        mMobile = mobile;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqValidate builder = new ReqValidate();
        builder.openId = mOpenId;
        builder.mobile = mMobile;
        return ReqValidate.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_VALIDATE;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_VALIDATE_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString)
    {

        try
        {
            RspValidate resBody = RspValidate.parseFrom(byteString);

            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqValidateSucceed();
            }
            else {
                if (mListener != null)
                    mListener.onReqFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg "+ resMsg, "oddshou");
            }

        } catch (InvalidProtocolBufferNanoException e)
        {
            e.printStackTrace();
            handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
        }
    }

    @Override
    protected void handleResponseError(int errCode, String strErr)
    {
        if (mListener == null)
            return;

        mListener.onNetError(errCode, strErr);
    }

    @Override
    protected String getServerUrl()
    {
        return ServerUrl.getAccountUacUrlApp();
    }
}
