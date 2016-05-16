
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqAddCommentListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.ReqAddComment;
import com.hykj.gamecenter.protocol.Apps.RspAddComment;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 用户发布评论
 * 
 * @author
 */
public class ReqAddCommentController extends AbstractNetController
{

    private static final String TAG = ReqAddCommentController.class.getName();

    private ReqAddCommentListener mListener = null;
    private String userName;
    private int appId;
    private int userScore;
    private int userVerCode;
    private String userVerName;
    private String comments;
    private int commentId;

    public ReqAddCommentController(String username, int appid, int userscore, int uservercode,
            String uservername, String comments, int commentId, ReqAddCommentListener mListener)
    {
        this.mListener = mListener;
        appId = appid;
        userName = username;
        userScore = userscore;
        this.comments = comments;
        userVerCode = uservercode;
        userVerName = uservername;
        this.commentId = commentId;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqAddComment builder = new ReqAddComment();
        //TODO
        /*builder.setUserName( userName );
        builder.setAppId( appId );
        builder.setUserScore( userScore );
        builder.setUserVerCode( userVerCode );
        builder.setUserVerName( userVerName );
        builder.setComments( comments );*/
        builder.userName = userName;
        builder.appId = appId;
        builder.userScore = userScore;
        builder.userVerCode = userVerCode;
        builder.userVerName = userVerName;
        builder.comments = comments;
        builder.commentId = commentId;
        Logger.i(TAG, "appscoreinfo request is start");
        Logger.i(TAG, "appId:" + appId);
        return ReqAddComment.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_ADD_COMMENTS;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_ADD_COMMENTS_RESPONSE;
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
            RspAddComment resBody = RspAddComment.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                mListener.onReqAddCommentSucceed(resBody.commentId, resBody.userName);
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
