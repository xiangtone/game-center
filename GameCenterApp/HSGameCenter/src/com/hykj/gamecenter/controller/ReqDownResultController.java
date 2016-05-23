
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqDownResultListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.ReqDownRes;
import com.hykj.gamecenter.protocol.Apps.RspDownRes;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 下载上报结果
 * 
 * @author
 */
public class ReqDownResultController extends AbstractNetController
{

    private static final String TAG = ReqDownResultController.class.getName();

    private ReqDownResultListener mListener = null;
    private int appId;
    private int packId;
    private int downloadRes;
    private String remark;
    private String timeConsume;
    private String downloadSpeed;
    private int groupId;

    //成功需上报下载总耗时、下载速度
    public ReqDownResultController(int appid, int packid, int downloadres, String remarks,
            String timeconsume, String downloadspeed, ReqDownResultListener mListener)
    {
        this.mListener = mListener;
        appId = appid;
        packId = packid;
        downloadRes = downloadres;
        remark = remarks;
        timeConsume = timeconsume;
        downloadSpeed = downloadspeed;
    }
    //成功需上报下载总耗时、下载速度
    public ReqDownResultController(int appid, int packid, int downloadres, String remarks,
            String timeconsume, String downloadspeed, int groupId, ReqDownResultListener mListener)
    {
        this.mListener = mListener;
        appId = appid;
        packId = packid;
        downloadRes = downloadres;
        remark = remarks;
        timeConsume = timeconsume;
        downloadSpeed = downloadspeed;
        this.groupId = groupId;
    }

    //TODO
    public ReqDownResultController(int appid, int packid, int downloadres, String remarks,
            ReqDownResultListener mListener)
    {
        this.mListener = mListener;
        appId = appid;
        packId = packid;
        downloadRes = downloadres;
        remark = remarks;
        timeConsume = "";
        downloadSpeed = "";
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqDownRes builder = new ReqDownRes();
        /*builder.setAppId( appId );
        builder.setPackId( packId );
        builder.setDownloadRes( downloadRes );
        builder.setRemark( remark );
        //下载成功需格外上报
        builder.setTimeConsume( timeConsume );
        builder.setDownloadSpeed( downloadSpeed );*/
        builder.appId = appId;
        builder.packId = packId;
        builder.downloadRes = downloadRes;
        builder.remark = remark;
        builder.timeConsume = timeConsume;
        builder.downloadSpeed = downloadSpeed;
        builder.groupId = groupId;

        //TODO
        Logger.i(TAG, "ReqDownRes request is start");
        return ReqDownRes.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_DOWN_RESULT;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_DOWN_RESULT_RESPONSE;
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
            RspDownRes resBody = RspDownRes.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                mListener.onReqDownResultSucceed();
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
