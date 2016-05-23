
package com.hykj.gamecenter.controller;

import android.util.Log;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReportLanuchInfoListener.RESCODE_STATE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqReportedListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.Reported.ReqReported;
import com.hykj.gamecenter.protocol.Reported.RspReported;

public class ReqReportedInfoController extends AbstractNetController
{

    private static final String TAG = "ReqReportedInfoCon";
    private ReportedInfo[] mReportInfoList;
    private ReqReportedListener mListener;

    public ReqReportedInfoController(ReportedInfo [] reportInfoList, ReqReportedListener listener)
    {
        mReportInfoList = reportInfoList; 
        mListener = listener;
    }

    @Override
    protected byte[] getRequestBody()
    {
        ReqReported builder = new ReqReported();
        builder.reportedInfo = mReportInfoList;
        return ReqReported.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        // TODO Auto-generated method stub
        return ACTION.ACTION_REPORTED_INFO;
    }

    @Override
    protected String getResponseAction()
    {
        // TODO Auto-generated method stub
        return ACTION.ACTION_REPORTED_INFO_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString)
    {
        try
        {
            RspReported resBody = RspReported.parseFrom(byteString);
            int resCode = resBody.rescode;
            String resMsg = resBody.resmsg;
            if (RESCODE_STATE.REPORT_FAILED != resCode)
            {
                if (mListener != null) {
                    mListener.onReqReportedSucceed();
                }
            }
            else
            {
                Log.e(TAG, "handleResponseFailed-- resCode = " + resCode + "---- resMsg = " + resMsg );
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
        Log.e(TAG, "handleResponseError " + errCode + " strErr " + strErr);
    }

    @Override
    protected String getServerUrl()
    {
        // TODO Auto-generated method stub
        return ServerUrl.getServerUrlReport();
    }

}
