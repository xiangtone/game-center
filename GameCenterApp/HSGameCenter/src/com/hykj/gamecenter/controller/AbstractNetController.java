
package com.hykj.gamecenter.controller;

import android.util.Log;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.DeviceInfo;
import com.hykj.gamecenter.controller.ProtocolListener.ERROR;
import com.hykj.gamecenter.net.AsyncHttpPostSession;
import com.hykj.gamecenter.net.AsyncHttpSession;
import com.hykj.gamecenter.net.HttpSessionCallBack;
import com.hykj.gamecenter.protocol.Packet.ReqPacket;
import com.hykj.gamecenter.protocol.Packet.RspPacket;
import com.hykj.gamecenter.utils.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractNetController
{

    private final String TAG = "AbstractNetController";

    private AsyncHttpPostSession mSession;
    private static AtomicInteger mRequestSeq = new AtomicInteger(0);

    private int chnNo = 0;// 渠道号(由后台分配给渠道并传入，默认为0)
    private int chnPos = 0;
    private int clientPos = 0;

    public void setChnNo(int chnNo)
    {
        this.chnNo = chnNo;
    }

    public void setChnPos(int chnPos)
    {
        this.chnPos = chnPos;
    }

    public void setClientPos(int clientPos)
    {
        this.clientPos = clientPos;
    }

    /**
     * 子类实现，返回请求的业务包体
     * 
     * @return ByteString
     */
    protected abstract byte[] getRequestBody();

    /**
     * 子类实现，返回请求的Mask设置
     * 
     * @return
     */
    protected abstract int getRequestMask();

    /**
     * 子类实现，返回请求的action设置
     * 
     * @return
     */
    protected abstract String getRequestAction();

    /**
     * 子类实现，返回回复包的action设置
     * 
     * @return
     */
    protected abstract String getResponseAction();

    /**
     * 子类实现，处理返回的业务数据与业务本身的逻辑错误
     * 
     * @param byteString
     */
    protected abstract void handleResponseBody(byte[] byteString);

    /**
     * 子类实现，处理返回的网络与包数据相关的错误
     * 
     * @param errCode
     * @param strErr
     */
    protected abstract void handleResponseError(int errCode, String strErr);

    /**
     * @return 对应请求的服务器地址
     */
    protected abstract String getServerUrl();

    protected AbstractNetController()
    {
    }

    public AsyncHttpSession getSession()
    {
        return mSession;
    }

    public void doRequest()
    {
        mSession = new AsyncHttpPostSession(getServerUrl());
        mSession.registerCallBack(mHttpSessionCallBack);
        mSession.doPost(makePacket());
                Logger.e(TAG, "doRequest action "+ getRequestAction(), "oddshou");
    }

    private byte[] makePacket()
    {
        /* // 包头
         ReqPacket.Builder builder = ReqPacket.newBuilder();
         builder.setMask(getRequestMask());
         builder.setUdi(getDeviceInfo());
         builder.addAction(getRequestAction());
         builder.addParams(preHandleRequestBody(getRequestBody()));
         builder.setReqNo(getRequestSequence());
         // 渠道号（由后台分配给渠道并传入，默认为0）、只需在应用启动的第一次进行设置
         builder.setChnNo(App.getChnNo());
         // 渠道位置（由后台分配给渠道或SDK传入，确定如：广告位第二个广告等位置信息，默认为0），暂时不做处理使用默认值
         builder.setChnPos(chnPos);
         //应用的ID（直接与服务端沟通的应用的ID）(定义: 1= 帐号账户 , 2= 游戏商城 , 3 = PcSuite,6 = 应用商店, 12 = 游戏中心)
         builder.setClientId(12);
         // 应用的位置（直接与服务端沟通的应用的位置ID，与渠道位置类似）
         builder.setClientPos(clientPos);
         builder.setClientVer(App.VersionName());

         LogUtils.e("渠道号：" + builder.build().getChnNo());
         Log.d(TAG, "Prepare send packet: squence=" + getRequestSequence() + "&action="
                 + getRequestAction() + " app.versionName " + App.VersionName());*/

        ReqPacket req = new ReqPacket();
        req.mask = getRequestMask();
        req.udi = getDeviceInfo();
        req.action = new String[] {
                getRequestAction()
        };
        req.params = new byte[][] {
                getRequestBody()
        };
        req.reqNo = getRequestSequence();
        req.chnNo = App.getChnNo();
        req.chnPos = chnPos;
        req.clientId = App.getmClientId();
        req.clientPos = clientPos;
        req.clientVer = App.VersionName();
        req.rsaKeyVer = "rsa-default";

        Logger.e("id for client", "req.clientPos:====" + req.clientPos, "oddshou");
        return ReqPacket.toByteArray(req);
    }

    /*
     * 预处理请求包
     */
    /* private ByteString preHandleRequestBody(ByteString body)
     {
         ByteString handleBody = body;

         if ((getRequestMask() | 0xFF) == MaskCode.PARAMS_RSA_VALUE)
         {
             // TODO encrypt body
             handleBody = body;
         }

         if ((getRequestMask() | 0xFF) == MaskCode.PARAMS_GZIP_VALUE)
         {
             // TODO zip compress
             handleBody = body;
         }
         return handleBody;
     }*/

    /*
     * 预处理返回包
     */
    /*private ByteString preHandleResponseBody(ByteString body, int mask)
    {
        ByteString handleBody = body;

        if ((mask | 0xFF) == MaskCode.PARAMS_RSA_VALUE)
        {

        }

        if ((mask | 0xFF) == MaskCode.PARAMS_GZIP_VALUE)
        {

        }

        return handleBody;
    }*/

    private String getDeviceInfo()
    {
        //        Logger.i(TAG, "DeviceInfo:" + DeviceInfo.getDeviceInfo());
        return DeviceInfo.getDeviceInfo();
    }

    /*
     * private String getServerUrl() { return ServerUrl.SERVER_URL; }
     */
    private int getRequestSequence()
    {
        return mRequestSeq.getAndIncrement();

    }

    private final HttpSessionCallBack mHttpSessionCallBack = new HttpSessionCallBack()
    {

        @Override
        public void onSucceed(byte[] rspData)
        {
            try
            {
                RspPacket resPacket = RspPacket.parseFrom(rspData);
                int resCode = resPacket.rescode;

                if (resCode == 0)
                {
                    String action = resPacket.action[0];
                    if (action.equals(getResponseAction()))
                    {
                        //Modified by tomqian@2015/06/024  START
                        /*ByteString result = preHandleResponseBody(resPacket.getParams(0),
                                resPacket.getMask());
                        //Modified for actionbar style tedlei@2014/08/06 UPD START
                        //handleResponseBody( preHandleResponseBody( resPacket.getParams( 0 ) , resPacket.getMask( ) ) );
                        Log.e(TAG, "result-->" + result.toString());*/

                        Log.e(TAG, "result-->" + resPacket.params[0].toString());
                        handleResponseBody(resPacket.params[0]);
                        //Modified for actionbar style tedlei@2014/08/06 UPD END
                        Log.e(TAG, "handleResponseBody end");
                        //Modified by tomqian@2015/06/024  END
                    }
                    else
                    {
                        // 包的action不匹配，在调试过程中可能会出现
                        Log.e(TAG, "Action from server is misMatch, reqAction");
                        handleResponseError(ERROR.ERROR_ACTION_MISMATCH,
                                "Action from server is misMatch, reqAction:" + getRequestAction()
                                        + ",resAction:" + action);
                    }
                }
                else
                {
                    String msg = resPacket.resmsg;
                    handleResponseError(ERROR.ERROR_ACTION_FAIL, msg);
                    Logger.e(TAG, "rescode error " + resCode + " msg "+ msg, "oddshou");
                }

            }
            catch (InvalidProtocolBufferNanoException e)
            {

                Log.e(TAG, "BAD");
                e.printStackTrace();
                handleResponseError(ERROR.ERROR_BAD_PACKET, e.getMessage());
            }
        }

        @Override
        public void onError(int errCode, String strErr)
        {
            Logger.e(TAG, "onError "+ errCode + " strErr "+ strErr, "oddshou");
            //这里的回调都可以认为是网络原因
            handleResponseError(errCode, strErr);
        }
    };

}
