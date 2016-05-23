/**
 * Created by win7 on 2016/5/23.
 */
//部署前需配置 id 和url
// var configChannelId = "7C9F9A3359FCDA3A5DB68A3AFC6F0E1B";   //huashengWIFI
var configChannelId = "66BCFDE2834F07DF9E6319A7EBF54C73";   //schoolWIFI

document.write("<script src='" +
     getChannel()+
    "'><\/script>");

function getUrl(localhost) {
    return localhost ? "http://127.0.0.1:42010/jsonapi" : "http://appstore.api.huashenggame.com/jsonapi";
}

function getChannel(){
    //渠道设置
    //1.school
    return "http://sdk.talkingdata.com/app/h5/v1?appid="+ configChannelId +"&vn=正式版本v1.0&vc=16.05.23";
}

function sendRequest(syncConfigData, successFun) {
    $.ajax({
        type: "post",
        url: getUrl(false),
        async: true,
        // data : contentPb,
        data: JSON.stringify(syncConfigData),
        // dataType : "binary",
        dataType: "json",
        success: successFun.success,
        error: ajaxNetworkError
    });

}

function ajaxNetworkError(XMLHttpRequest, textStatus, errorThrown) {
    // alert("ajaxNetworkError:" + XMLHttpRequest.status + "-" +
    // XMLHttpRequest.readyState + "-" + textStatus);
    console.log("ajaxNetworkError:" + XMLHttpRequest.status + "-"
        + XMLHttpRequest.readyState + "-" + textStatus + "-");
}

var syncHead = {
    "udi": "a=1&b=1",
    "chnNo": "",
    "chnPos": "",
    "clientId": "",
    "clientPos": "",
    "clientVer": ""
}

function pushDown(posId, showName) {
    var kv = {"appName": showName};
    TDAPP.onEvent("列表下载", posId, kv);

}

function pushDownDetail(posId, showName) {
    // var item = JSON.parse(itemString);
    var kv = {"appName": showName};
    TDAPP.onEvent("详情下载", posId, kv);
}

function goBack() {
    // history.back();
    history.go(-1);
}