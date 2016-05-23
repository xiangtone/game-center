
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.RepAppsUpdateListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.AppInfoList;
import com.hykj.gamecenter.protocol.Apps.LocalAppVer;
import com.hykj.gamecenter.protocol.Apps.ReqAppsUpdate;
import com.hykj.gamecenter.protocol.Apps.RspAppsUpdate;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

import java.util.ArrayList;

/**
 * 获取应用更新
 * 
 * @author
 */
public class ReqAppsUpdateListController extends AbstractNetController
{

    private static final String TAG = ReqAppsUpdateListController.class.getName();

    private RepAppsUpdateListener mListener = null;

    private ArrayList<LocalAppVer> localAppVer;
    public static final int CHECKUPDATETYPE_ALL = 0;
    public static final int CHECKUPDATETYPE_APP = 1;
    public static final int CHECKUPDATETYPE_GAME = 2;

    public ReqAppsUpdateListController(ArrayList<LocalAppVer> localappver,
            RepAppsUpdateListener mListener)
    {
        this.mListener = mListener;
        localAppVer = localappver;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqAppsUpdate builder = new ReqAppsUpdate();
        //TODO
        /*builder.addAllLocalAppVer(localAppVer);
        builder.setCheckUpdateType(ReqAppsUpdate.CHECKUPDATETYPE_FIELD_NUMBER);*/
        LocalAppVer[] localApps = new LocalAppVer[localAppVer.size()];
        for (int i = 0; i < localAppVer.size(); i++) {
            localApps[i] = localAppVer.get(i);
        }
        builder.localAppVer = localApps;
        builder.checkUpdateType = CHECKUPDATETYPE_ALL;

        return ReqAppsUpdate.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_APPS_UPDATE;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_APPS_UPDATE_RESPONSE;
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
            RspAppsUpdate resBody = RspAppsUpdate.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                AppInfoList ai = AppInfoList.parseFrom(resBody.appInfoList);
                mListener.onRepAppsUpdateSucceed(ai.appInfo);
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
