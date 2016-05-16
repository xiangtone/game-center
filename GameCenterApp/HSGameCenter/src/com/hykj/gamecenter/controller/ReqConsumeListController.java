
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqConsumeListListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Pay.ReqConsumeList;
import com.hykj.gamecenter.protocol.Pay.RspConsumeList;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author tomqian //获取消费列表
 */
public class ReqConsumeListController extends AbstractNetController
{

    private static final String TAG = "ReqUserCommentsListController";

    private ReqConsumeListListener mListener = null;
    private final String mOpenId;
    private final String mToken;
    private final String mTimes;
    private final int mPage;

    public ReqConsumeListController(ReqConsumeListListener mListener, String openId,
            String token, String times, int page)
    {
        this.mListener = mListener;
        mOpenId = openId;
        mToken = token;
        mTimes = times;
        mPage = page;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqConsumeList builder = new ReqConsumeList();
        builder.openId = mOpenId;
        builder.token = mToken;
        builder.times = mTimes;
        builder.page = mPage;
        return ReqConsumeList.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_CONSUME_LIST;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_CONSUME_LIST_RESPONSE;
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
            RspConsumeList resBody = RspConsumeList.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                mListener.onReqConsumeListSucceed(resBody);
            }
            else
            {
                String resMsg = resBody.resmsg;
                mListener.onReqConsumeListFailed(resCode, resMsg);
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
        Logger.i(TAG, "url=====" + ServerUrl.getAccountPayUrlApp());
        return ServerUrl.getAccountPayUrlApp();
    }

}
