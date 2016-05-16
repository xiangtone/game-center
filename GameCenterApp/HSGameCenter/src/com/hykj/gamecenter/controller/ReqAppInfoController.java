
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqAppInfoListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.ReqAppInfo;
import com.hykj.gamecenter.protocol.Apps.RspAppInfo;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 获取应用信息
 * 
 * @author
 */
public class ReqAppInfoController extends AbstractNetController
{

    private static final String TAG = ReqAppInfoController.class.getName();

    private ReqAppInfoListener mListener = null;
    private int appId;
    private int packId;
    private int scrType;

    public ReqAppInfoController(int appid, int packid, int scrtype, ReqAppInfoListener mListener)
    {
        this.mListener = mListener;
        appId = appid;
        packId = packid;
        scrType = scrtype;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqAppInfo builder = new ReqAppInfo();
        //TODO
        /*builder.setAppId( appId );
        builder.setPackId( packId );
        builder.setScrType( scrType );
        builder.setClientCacheVer( ControllerHelper.getInstance( ).getAppInfoCacheDataVer( ) );*/
        builder.appId = appId;
        builder.packId = packId;
        builder.scrType = scrType;
        builder.clientCacheVer = ControllerHelper.getInstance().getAppInfoCacheDataVer();

        Logger.i(TAG, "appinfo2 request is start");
        Logger.i(TAG, "dataVer:" + ControllerHelper.getInstance().getAppInfoCacheDataVer());
        Logger.i(TAG, "appId:" + appId);
        Logger.i(TAG, "packId:" + packId);
        Logger.i(TAG, "scrType:" + scrType);
        return ReqAppInfo.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_APPINFO;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_APPINFO_RESPONSE;
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
            RspAppInfo resBody = RspAppInfo.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                AppInfo appinfo = AppInfo.parseFrom(resBody.appInfo);
                mListener.onReqAppInfoSucceed(appinfo, resBody.serverCacheVer);
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
        //网络异常
        mListener.onNetError(errCode, strErr);
        Logger.e(TAG, TAG + errCode + " resMsg "+ strErr, "oddshou");
    }

    @Override
    protected String getServerUrl()
    {
        return ServerUrl.getServerUrlApp();
    }

}
