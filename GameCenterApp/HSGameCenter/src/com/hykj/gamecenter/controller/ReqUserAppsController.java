
package com.hykj.gamecenter.controller;

import java.util.ArrayList;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.RepUserAppsListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.AppInfoList;
import com.hykj.gamecenter.protocol.Apps.LocalAppVer;
import com.hykj.gamecenter.protocol.Apps.ReqUserApps;
import com.hykj.gamecenter.protocol.Apps.RspUserApps;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 获取应用更新
 * 
 * @author greatzhang
 */
public class ReqUserAppsController extends AbstractNetController {

    private static final String TAG = "ReqUserAppsController";

    private RepUserAppsListener mListener = null;

    private final ArrayList<LocalAppVer> localAppVer;

    public ReqUserAppsController(ArrayList<LocalAppVer> localappver,
            RepUserAppsListener mListener) {
        this.mListener = mListener;
        localAppVer = localappver;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqUserApps builder = new ReqUserApps();
        //        builder.addAllLocalAppVer(localAppVer);

        LocalAppVer[] localApps = new LocalAppVer[localAppVer.size()];
        for (int i = 0; i < localAppVer.size(); i++) {
            localApps[i] = localAppVer.get(i);
        }
        builder.localAppVer = localApps;
        return ReqUserApps.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_APPS_UPDATE;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_APPS_UPDATE_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        if (mListener == null) {
            return;
        }
        try {
            RspUserApps resBody = RspUserApps.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0) {
                AppInfoList ai = AppInfoList
                        .parseFrom(resBody.appInfoList);
                mListener.onRepAppsUpdateSucceed(ai.appInfo);
            } else {
                String resMsg = resBody.resmsg;
                mListener.onReqFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg "+ resMsg, "oddshou");
            }
        } catch (InvalidProtocolBufferNanoException e) {
            handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected void handleResponseError(int errCode, String strErr) {
        if (mListener == null) {
            return;
        }
        mListener.onNetError(errCode, strErr);
    }

    @Override
    protected String getServerUrl() {
        return ServerUrl.getServerUrlApp();
    }

}
