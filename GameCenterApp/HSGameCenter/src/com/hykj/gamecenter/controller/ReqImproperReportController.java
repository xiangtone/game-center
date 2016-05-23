
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqImproperReportListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.ReqAppInform;
import com.hykj.gamecenter.protocol.Apps.RspAppInform;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * @author tomqian 游戏举报接口
 */
public class ReqImproperReportController extends AbstractNetController {

    private final String TAG = "ReqImproperReportController";
    private final ReqImproperReportListener mListener;
    private final int mAppId;//游戏应用ID
    private final String mInformType; //举报类型，1=强制广告，2=无法安装，3=质量不好，4=版本旧，5=恶意扣费，6=携带病毒，多个以逗号分隔
    private final String mInformDetail;//更多描述(限200字符)

    public ReqImproperReportController(ReqImproperReportListener listener, int appId,
            String informType,
            String informDetail) {
        mListener = listener;
        mAppId = appId;
        mInformType = informType;
        mInformDetail = informDetail;
    }

    @Override
    protected byte[] getRequestBody() {
        ReqAppInform builder = new ReqAppInform();
        builder.appId = mAppId;
        builder.informType = mInformType;
        builder.informDetail = mInformDetail;
        Logger.i(TAG, "builder.appId=====" + builder.appId);
        Logger.i(TAG, "builder.informType=====" + builder.informType);
        Logger.i(TAG, "builder.informDetail=====" + builder.informDetail);
        Logger.i(TAG, "builder =====" + builder);
        return ReqAppInform.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        return ACTION.ACTION_IMPROPERREPORT;
    }

    @Override
    protected String getResponseAction() {
        return ACTION.ACTION_IMPROPERREPORT_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {

        try {
            RspAppInform resBody = RspAppInform.parseFrom(byteString);
            //            Logger.i(TAG, "resBody=====" + resBody.toString());
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (resCode == 0) {
                if (mListener != null)
                    mListener.onReqImproperReportSucceed(resCode, resMsg);
            }
            else {
                if (mListener != null)
                    mListener.onReqImproperReportFailed(resCode, resMsg);
                Logger.e(TAG, TAG + resCode + " resMsg " + resMsg, "oddshou");
            }

        } catch (InvalidProtocolBufferNanoException e) {
            e.printStackTrace();
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
        Logger.i(TAG, "url=====" + ServerUrl.getServerUrlApp());
        return ServerUrl.getServerUrlApp();
    }
}
