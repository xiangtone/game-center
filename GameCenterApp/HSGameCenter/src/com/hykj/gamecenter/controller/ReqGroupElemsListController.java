
package com.hykj.gamecenter.controller;

import android.util.Log;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqGroupElemsListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.GroupElems;
import com.hykj.gamecenter.protocol.Apps.ReqGroupElems;
import com.hykj.gamecenter.protocol.Apps.RspGroupElems;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 获取分组元素列表
 * 
 * @author
 */
public class ReqGroupElemsListController extends AbstractNetController
{

    private static final String TAG = ReqGroupElemsListController.class.getName();

    private ReqGroupElemsListener mListener = null;
    private int size = 10;
    private int index = 1;
    private int groupid;
    private int groupClass;
    private int groupType;
    private int orderType;

    public ReqGroupElemsListController(int groupId, int groupclass, int grouptype, int orderType,
            int pageSize, int pageIndex, ReqGroupElemsListener mListener)
    {
        this.mListener = mListener;
        this.groupid = groupId;
        this.size = pageSize;
        this.index = pageIndex;
        this.groupClass = groupclass;
        this.groupType = grouptype;
        this.orderType = orderType;
    }

    @Override
    protected byte[] getRequestBody()
    {
        //获取分组元素列表   ReqGroupElems
        ReqGroupElems builder = new ReqGroupElems();
        //TODO
        /*builder.setGroupId(groupid); // 分组ID
        builder.setGroupClass(groupClass); // 分组类别(统计数据参数)
        builder.setGroupType(groupType); // 分组类型(统计数据参数)
        builder.setPageSize(size); // 页尺寸（专题与推荐不支持分页，pageSize与pageIndex填0）
        builder.setPageIndex(index); // 页码
        builder.setOrderType(orderType); // 排序类型(统计数据参数)
        builder.setClientCacheVer(ControllerHelper.getInstance().getGroupElemsCacheDataVer());*/
        builder.groupId = groupid;
        builder.groupClass = groupClass;
        builder.pageSize = size;
        builder.pageIndex = index;
        builder.orderType = orderType;
        builder.groupType = groupType;
        builder.clientCacheVer = ControllerHelper.getInstance().getGroupElemsCacheDataVer();

        // 终端缓存版本：如果有缓存则上传，下发时可省流量，加速度
        Logger.i(TAG, "GroupElems request is start");
        Logger.i(TAG, "dataVer:" + ControllerHelper.getInstance().getGroupElemsCacheDataVer());
        Logger.i(TAG, "groupid:" + groupid);
        Logger.i(TAG, "groupClass:" + groupClass);
        Logger.i(TAG, "groupType:" + groupType);
        Logger.i(TAG, "index:" + index);
        Logger.i(TAG, "size:" + size);
        return ReqGroupElems.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_GROUP_ELEMS;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_GROUP_ELEMS_RESPONSE;
    }

    @Override
    protected void handleResponseBody(byte[] byteString)
    {

        if (mListener == null)
        {
            Log.e(TAG, "mListener is null");
            return;
        }
        try
        {
            RspGroupElems resBody = RspGroupElems.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                GroupElems ge = GroupElems.parseFrom(resBody.groupElems);
                mListener.onReqGroupElemsSucceed(ge.groupElemInfo,
                        resBody.serverDataVer);
            }
            else
            {
                String resMsg = resBody.resmsg;
                mListener.onReqFailed(resCode, resMsg);
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
