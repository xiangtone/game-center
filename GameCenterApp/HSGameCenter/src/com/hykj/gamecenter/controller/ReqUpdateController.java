
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUpdateListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Updater.ReqUpdate;
import com.hykj.gamecenter.protocol.Updater.RspUpdate;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author froyohuang 请求更新接口
 */
// 应用商店本身更新
public class ReqUpdateController extends AbstractNetController {

    private final String PACKAGE_NAME = "com.hykj.gamecenter"/* "com.cs.appstore"*/;

    private final String TAG = "ReqUpdateController";
    private final ReqUpdateListener mListener;

    public ReqUpdateController(ReqUpdateListener listener) {
        mListener = listener;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqUpdate builder = new ReqUpdate();
        builder.packName = App.PackageName();
        builder.verName = App.VersionName();
        builder.verCode = App.VersionCode();
        /*builder.setPackName(App.PackageName());
        builder.setVerName(App.VersionName());
        builder.setVerCode(App.VersionCode());*/
        Logger.d(TAG, "odshou package_name " + PACKAGE_NAME + " versionName " + App.VersionName()
                + " versionCode " + App.VersionCode());
        return ReqUpdate.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_UPDATE;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_UPDATE_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        try {
            RspUpdate resBody = RspUpdate.parseFrom(byteString);

            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onRequpdateSucceed(resBody);
            } else {
                if (mListener != null)
                    mListener.onReqFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg "+ resMsg, "oddshou");
            }

        } catch (InvalidProtocolBufferNanoException e) {
            e.printStackTrace();
            Logger.e(TAG, "oddshou InvalidProtocolBufferException e");
            handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
        }
    }

    @Override
    protected void handleResponseError(int errCode, String strErr) {
        if (mListener == null)
            return;

        mListener.onNetError(errCode, strErr);
    }

    @Override
    protected String getServerUrl() {
        return ServerUrl.getServerUrlUpdate();
    }

}
