
package com.hykj.gamecenter.controller;

import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.hykj.gamecenter.App;
import com.hykj.gamecenter.net.AsyncHttpPostSession;
import com.hykj.gamecenter.net.HttpSessionCallBack;
import com.hykj.gamecenter.protocol.OpenRtb;
import com.hykj.gamecenter.protocol.OpenrtbYesdat;
import com.hykj.gamecenter.utils.DevicesInfo;
import com.hykj.gamecenter.utils.Logger;

import static com.hykj.gamecenter.protocol.OpenrtbYesdat.*;

public abstract class AdvNetController {

    private final String TAG = "AdvNetController";

    private AsyncHttpPostSession mSession;
    private ProtocolListener.ReqAdvListener listener;

    /**
     * @return 对应请求的服务器地址
     */
    protected abstract String getServerUrl();

    public AdvNetController(ProtocolListener.ReqAdvListener listener) {

        this.listener = listener;
    }

    public void doRequest() {
        mSession = new AsyncHttpPostSession(getServerUrl());
        mSession.registerCallBack(mHttpSessionCallBack);
        mSession.doPost(getRequestBody());
    }

    private final HttpSessionCallBack mHttpSessionCallBack = new HttpSessionCallBack() {

        @Override
        public void onSucceed(byte[] rspData) {
            try {
                OpenRtb.BidResponse bidResponse = OpenRtb.BidResponse.parseFrom(rspData);
                handleResponseBody(bidResponse);
            } catch (InvalidProtocolBufferNanoException e) {
                e.printStackTrace();
                handleResponseError(ProtocolListener.ERROR.ERROR_BAD_PACKET, e.getMessage());
            }
        }

        @Override
        public void onError(int errCode, String strErr) {
            Logger.e(TAG, "onError " + errCode + " strErr " + strErr, "oddshou");
            //这里的回调都可以认为是网络原因
            handleResponseError(errCode, strErr);
        }
    };

    /**
     * 返回请求的业务包体
     *
     * @return ByteString
     */
    protected byte[] getRequestBody() {
        OpenRtb.BidRequest bidRequest = new OpenRtb.BidRequest();
        bidRequest.id = "32wei idi";        //32 位id ，验证返回，相当于 action
        //imp 段
        OpenRtb.BidRequest.Imp imp = new OpenRtb.BidRequest.Imp();
        imp.id = "32曝光id";      //原样返回
        imp.tagid = "0";        //广告位id
        OpenrtbYesdat.ImpExt impExt = new ImpExt();
        impExt.extTagid = "扩展id";   //广告扩展id
        impExt.displayType = 0;     //广告展示类型
        imp.setExtension(yImpExt, impExt);
        OpenRtb.BidRequest.Imp.Banner banner = new OpenRtb.BidRequest.Imp.Banner();
        banner.w = 640;     //广告横幅宽
        banner.h = 320;     //广告横幅高
        imp.banner = banner;
        bidRequest.imp = new OpenRtb.BidRequest.Imp[]{imp};
        //app 段
        OpenRtb.BidRequest.App app = new OpenRtb.BidRequest.App();
        app.name = "花生游戏";
        app.bundle = "com.hykj.gamecenter";
        bidRequest.app = app;
        //device 段
        OpenRtb.BidRequest.Device device = new OpenRtb.BidRequest.Device();
        DevicesInfo devicesInfo = new DevicesInfo(App.getAppContext());
        device.os = devicesInfo.os;
        device.osv = devicesInfo.osv;   //测试参数
        device.make = devicesInfo.make;
        device.model = devicesInfo.model;
        device.carrier = devicesInfo.carrier;
        device.ua = devicesInfo.ua;
        device.ip = devicesInfo.ip;
        device.geo = new OpenRtb.BidRequest.Geo();
        device.connectiontype = devicesInfo.connectiontype;
        DeviceExt deviceExt = new DeviceExt();
        deviceExt.imei = devicesInfo.y_device_ext_imei;
        deviceExt.mac = devicesInfo.y_device_ext_mac;
        device.setExtension(yDeviceExt, deviceExt);
        bidRequest.device = device;
        //user 段 (选填)
        bidRequest.user = new OpenRtb.BidRequest.User();

        return OpenRtb.BidRequest.toByteArray(bidRequest);

    }

    /**
     * 子类实现，返回请求的action设置
     *
     * @return
     */
    protected String getRequestId() {
        return "";      //需要生成一个请求id便于核对返回
    }

    /**
     * 子类实现，返回回复包的action设置
     *
     * @return
     */
//    protected String getResponseAction() {
//
//    }

    /**
     * 子类实现，处理返回的业务数据与业务本身的逻辑错误
     *
     * @param bidResponse
     */
    protected void handleResponseBody(OpenRtb.BidResponse bidResponse) {
        String id = bidResponse.id;
        OpenRtb.BidResponse.SeatBid[] seatbids = bidResponse.seatbid;
        if (seatbids.length <= 0) {
            listener.onReqFailed();
            return;
        }
        OpenRtb.BidResponse.SeatBid seatBid = seatbids[0];
        OpenRtb.BidResponse.SeatBid.Bid[] bids = seatBid.bid;
        if (bids.length <= 0) {
            listener.onReqFailed();
            return;
        }
        OpenRtb.BidResponse.SeatBid.Bid bid = bids[0];
        String id1 = bid.id;
        String impid = bid.impid;
        if (bid.hasExtension(OpenrtbYesdat.yBidExt)){
            BidExt bidExtension = bid.getExtension(OpenrtbYesdat.yBidExt);
            BidExt.Creative[] creatives = bidExtension.creative;
            if (creatives.length <= 0) {
                listener.onReqFailed();
                return;
            }
            BidExt.Creative creative = creatives[0];    //包含素材地址，反馈地址，文案等信息
            listener.onReqRecommAppSucceed(creative);
        }

    }

    /**
     * 子类实现，处理返回的网络与包数据相关的错误
     *
     * @param errCode
     * @param strErr
     */
    protected void handleResponseError(int errCode, String strErr) {

    }


}
