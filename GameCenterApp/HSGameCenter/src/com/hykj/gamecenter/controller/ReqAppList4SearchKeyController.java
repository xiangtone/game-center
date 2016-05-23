
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.controller.ProtocolListener.ReqAppList4SearchKeyListener;
import com.hykj.gamecenter.net.ServerUrl;
import com.hykj.gamecenter.protocol.Apps.GroupElems;
import com.hykj.gamecenter.protocol.Apps.ReqAppList4SearchKey;
import com.hykj.gamecenter.protocol.Apps.RspAppList4SearchKey;
import com.hykj.gamecenter.protocol.Packet;
import com.hykj.gamecenter.utils.Logger;

/**
 * 获取应用列表
 * 
 * @author
 */
public class ReqAppList4SearchKeyController extends AbstractNetController
{

    private static final String TAG = ReqAppList4SearchKeyController.class.getName();

    private ReqAppList4SearchKeyListener mListener = null;
    private String SearchKeyStr;
    private int isHotKey;
    private int appClass;
    private int appType;
    private int pageSize;
    private int pageIndex;
    private int orderType;

    public ReqAppList4SearchKeyController(String searchkeystr, int ishotkey, int appclass,
            int apptype, int pagesize, int pageindex, int ordertype,
            ReqAppList4SearchKeyListener mListener)
    {
        this.mListener = mListener;
        SearchKeyStr = searchkeystr;// 搜索关键字
        isHotKey = ishotkey;// 是否热门搜索词：0=是，1=不是
        appClass = appclass;// 应用类别：0=全部，11=应用，12=游戏
        appType = apptype;// 应用类型：0=全部
        pageSize = pagesize;// 页尺寸：页面需要下发多少个
        pageIndex = pageindex;// 页码
        orderType = ordertype;// 排序类型：0=自动-热门，2=按时间
    }

    @Override
    protected byte[] getRequestBody()
    {
        //获取根据关键词搜索得到的应用列表
        ReqAppList4SearchKey builder = new ReqAppList4SearchKey();
        //TODO
        /*builder.setSearchKeyStr(SearchKeyStr);// 搜索关键字
        builder.setIsHotKey(isHotKey);// 是否热门搜索词：0=是，1=不是
        builder.setAppClass(appClass);// 应用类别：0=全部，11=应用，12=游戏
        builder.setAppType(appType);// 应用类型：0=全部
        builder.setPageSize(pageSize);// 页尺寸：页面需要下发多少个
        builder.setPageIndex(pageIndex);// 页码
        builder.setOrderType(orderType);// 排序类型：0=自动-热门，2=按时间
        */
        builder.searchKeyStr = SearchKeyStr;
        builder.isHotKey = isHotKey;
        builder.appClass = appClass;
        builder.appType = appType;
        builder.pageSize = pageSize;
        builder.pageIndex = pageIndex;
        builder.orderType = orderType;

        Logger.i(TAG, "ReqAppList4SearchKey request is start");
        Logger.i(TAG, "SearchKeyStr:" + SearchKeyStr);
        Logger.i(TAG, "isHotKey:" + isHotKey);
        Logger.i(TAG, "appClass:" + appClass);
        Logger.i(TAG, "appType:" + appType);
        Logger.i(TAG, "pageSize:" + pageSize);
        Logger.i(TAG, "pageIndex:" + pageIndex);
        Logger.i(TAG, "orderType:" + orderType);
        return ReqAppList4SearchKey.toByteArray(builder);
    }

    @Override
    protected int getRequestMask()
    {
        return Packet.DEFAULT;
    }

    @Override
    protected String getRequestAction()
    {
        return ACTION.ACTION_APPSLIST4_SEARCHKEY;
    }

    @Override
    protected String getResponseAction()
    {
        return ACTION.ACTION_APPSLIST4_SEARCHKEY_RESPONSE;
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
            RspAppList4SearchKey resBody = RspAppList4SearchKey.parseFrom(byteString);
            int resCode = resBody.rescode;
            if (resCode == 0)
            {
                GroupElems ge = GroupElems.parseFrom(resBody.groupElems);
                mListener.onReqAppList4SearchKeySucceed(ge.groupElemInfo);
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
