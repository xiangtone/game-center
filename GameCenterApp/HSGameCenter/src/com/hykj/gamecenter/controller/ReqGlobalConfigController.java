
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGlobalConfigListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.ReqGlobalConfig;
import com.hykj.gamecenter.protocol.Apps.RspGlobalConfig;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 获取商店配置项,只在应用起来的第一次进行获取
 * 
 * @author
 */
public class ReqGlobalConfigController extends AbstractNetController {

    private static final String TAG = ReqGlobalConfigController.class.getName();

    private ReqGlobalConfigListener mListener = null;

    public ReqGlobalConfigController(ReqGlobalConfigListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqGlobalConfig builder = new ReqGlobalConfig();
        /*
         * 如果ControllerHelper.getInstance( ).getGroupsConfigCacheDataVer( )
         * 与服务器的版本一致，则服务器端 不会下发数据， 因为我们没有缓存应用数据(只缓存了索引表)，所以每次都要请求新数据，
         * 所以这里缓存数据版本要传"",服务器救会下发数据
         */
        //		builder.setGroupsCacheVer(""/*
        //									 * ControllerHelper.getInstance()
        //									 * .getGroupsConfigCacheDataVer()
        //									 */);
        builder.groupsCacheVer = "";
        Logger.i(TAG, "global config request is start");
        Logger.i(TAG, "dataVer:"
                + ControllerHelper.getInstance().getGroupsConfigCacheDataVer());
        return ReqGlobalConfig.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_GLOBAL_CONFIG;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_GLOBAL_CONFIG_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        if (mListener == null) {
            return;
        }
        try {
            RspGlobalConfig resBody = RspGlobalConfig.parseFrom(byteString);

            // resBody.getGroups( ).getGroupInfoList( ).get( location )

            int resCode = resBody.rescode;
            if (resCode == 0) {
                mListener.onReqGlobalConfigSucceed(resBody);
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

    public void remove() {
        mListener = null;
    }
}
