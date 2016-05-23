
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserScoreInfoListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.ReqUserScoreInfo;
import com.hykj.gamecenter.protocol.Apps.RspUserScoreInfo;
import com.hykj.gamecenter.protocol.Apps.UserScoreInfo;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 获取用户评分信息
 * 
 * @author
 */
public class ReqUserScoreInfoController extends AbstractNetController
{

    private static final String TAG = ReqUserScoreInfoController.class.getName();

    private ReqUserScoreInfoListener mListener = null;
    private int appId;

    public ReqUserScoreInfoController(int appid, ReqUserScoreInfoListener mListener)
    {
        this.mListener = mListener;
        appId = appid;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqUserScoreInfo builder = new ReqUserScoreInfo();
        //TODO
        builder.appId = appId;
        Logger.i(TAG, "appscoreinfo request is start");
        Logger.i(TAG, "appId:" + appId);
        return ReqUserScoreInfo.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_USER_SCORE_INFO;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_USER_SCORE_INFO_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString)
    {

        if (mListener == null)
        {
            return;
        }
        try
        {
            RspUserScoreInfo resBody = RspUserScoreInfo.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                UserScoreInfo userScoreInfo = UserScoreInfo.parseFrom(resBody.userScoreInfo);
                mListener.onReqUserScoreInfoSucceed(userScoreInfo);
            }
            else
            {
                String resMsg = resBody.resmsg;
                mListener.onReqFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg "+ resMsg, "oddshou");
            }
        } catch (InvalidProtocolBufferNanoException e)
        {
            handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected void handleResponseError(int errCode, String strErr)
    {
        if (mListener == null)
        {
            return;
        }
        mListener.onNetError(errCode, strErr);
    }

    @Override
    protected String getServerUrl()
    {
        return ServerUrl.getServerUrlApp();
    }

}
