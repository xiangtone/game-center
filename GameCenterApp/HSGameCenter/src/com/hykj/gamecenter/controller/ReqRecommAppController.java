package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqRecommAppListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.protocol.Apps.GroupElems;
import com.hykj.gamecenter.protocol.Apps.ReqRecommApp;
import com.hykj.gamecenter.protocol.Apps.RspGroupElems;

public class ReqRecommAppController extends AbstractNetController {

    private int appId;
    private String appClass;
    private String appType;
    private int pageSize;
    private int pageIndex;
    private int orderType;
    private String clientCacheVer;
    private ReqRecommAppListener listener;

    public ReqRecommAppController(int appId, String appClass, String appType,
            int pageSize, int pageIndex, int orderType, ReqRecommAppListener listener) {
        // TODO Auto-generated constructor stub
        this.appId = appId;
        this.appClass = appClass;
        this.appType = appType;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.orderType = orderType;
        this.listener = listener;
    }
    
    @Override
    protected byte[] getRequestBody() {
        // TODO Auto-generated method stub
        ReqRecommApp builder = new ReqRecommApp();
        builder.appId = appId;
        builder.appClass = appClass;
        builder.appType = appType;
        builder.pageSize = pageSize;
        builder.pageIndex = pageIndex;
        builder.orderType = orderType;
        builder.clientCacheVer = ControllerHelper.getInstance().getGroupElemsCacheDataVer();
        
        return ReqRecommApp.toByteArray(builder);
    }

    @Override
    protected int getRequestMask() {
        // TODO Auto-generated method stub
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_RECOMMAPP;
    }

    @Override
    protected String getResponseAction() {
        // TODO Auto-generated method stub
        return ACTION.ACTION_RECOMMAPP_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString) {
        // TODO Auto-generated method stub
        if (listener == null)
        {
            return;
        }
        try
        {
            RspGroupElems resBody = RspGroupElems.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                GroupElems ge = GroupElems.parseFrom(resBody.groupElems);
                listener.onReqRecommAppSucceed(ge.groupElemInfo, resBody.serverDataVer);
            }
            else
            {
                String resMsg = resBody.resmsg;
                listener.onReqFailed(resCode, resMsg);
            }
        } catch (InvalidProtocolBufferNanoException e)
        {
            handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void handleResponseError(int errCode, String strErr) {
        // TODO Auto-generated method stub
        if (listener == null)
        {
            return;
        }
        listener.onNetError(errCode, strErr);
    }

    @Override
    protected String getServerUrl() {
        // TODO Auto-generated method stub
        return ServerUrl.getServerUrlApp();
    }

}
