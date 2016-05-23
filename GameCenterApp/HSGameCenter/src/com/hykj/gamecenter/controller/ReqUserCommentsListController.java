
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUserCommentsListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.ReqUserComments;
import com.hykj.gamecenter.protocol.Apps.RspUserComments;
import com.hykj.gamecenter.protocol.Apps.UserComments;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 获取用户评论列表
 * 
 * @author
 */
public class ReqUserCommentsListController extends AbstractNetController
{

    private static final String TAG = "ReqUserCommentsListController";

    private ReqUserCommentsListener mListener = null;
    private int appId;
    private int pageSize;
    private int pageIndex;

    public ReqUserCommentsListController(int appid, int pagesize, int pageindex,
            ReqUserCommentsListener mListener)
    {
        this.mListener = mListener;
        appId = appid;
        pageSize = pagesize;
        pageIndex = pageindex;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqUserComments builder = new ReqUserComments();
        //TODO
        /*	builder.setAppId( appId );
        	builder.setPageSize( pageSize );
        	builder.setPageIndex( pageIndex );*/
        builder.appId = appId;
        builder.pageSize = pageSize;
        builder.pageIndex = pageIndex;
        Logger.i(TAG, "usercomments request is start");
        Logger.i(TAG, "appId:" + appId);
        Logger.i(TAG, "pageSize:" + pageSize);
        Logger.i(TAG, "pageIndex:" + pageIndex);
        return ReqUserComments.toByteArray(builder);
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
            RspUserComments resBody = RspUserComments.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                UserComments userComments = UserComments.parseFrom(resBody.userComments);
                mListener.onReqUserCommentsSucceed(userComments.userCommentInfo);
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
