var appDetail = null;
var pageDone = false;

function getUrl(localhost) {
    return localhost ? "http://127.0.0.1:42010/jsonapi" : "http://115.159.125.75/appstore_api/jsonapi";
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
function getAppdetails(appId) {
    var syncAppDetailData = {
        "header": syncHead,
        "api": "ReqIosAppDetail",
        "params": {
            "appId": appId
        }
    }
    sendRequest(syncAppDetailData, getsuccess = {
        success: function (data) {
            appDetail = data.data;
            if (pageDone)
                page_onload();
        }
    });
}


function initAppInfo() {
    //解析id获取游戏详情
    var rx = /[&|#?]appid=[\d]*/
    var mc = rx.exec(location.href);
    var appId = 0;
    if (mc != null) {
//			id = parseInt(mc[0]);
        appId = mc[0].replace(/[^0-9]/ig, "");
        if (isNaN(appId))
            appId = 0;
    }
    getAppdetails(appId);
}

initAppInfo();

function page_onload() {

    if (!appDetail)
        return;
    initWithAppInfo(appDetail);
}

function initWithAppInfo(appdetail) {
    var html = "";
    html += "<figure><img src='" +
        appdetail.IconUrl +
        "' alt='loading...'><figcaption><h4 style='margin-top:0.1rem '>" +
        appdetail.ShowName +
        "</h4><h5>" +
        appdetail.RecommWord +
        "</h5></figcaption></figure><a  class='game_Detil_download' href='" +
        appdetail.PackUrl +
        "' > 前往App store下载</a>";
    $(".game_Detil").html(html);

    //添加详情图片
    var htmlDetails = "";
    var appPic = appDetail.AppPicUrl.split(",");
    for (var i = 0; i < appPic.length; i++) {
        htmlDetails += "<div class='swiper-slide details_img'><img src='" +
            appPic[i] +
            "' alt='loading...'/></div>";
    }
    $(".swiper-wrapper").html(htmlDetails);


    $(".p_details").html(appDetail.AppDesc);
    $(".p_details_h").html("开发商:"+  appDetail.DevName +"" );
}

function getCookie(name)//取cookies函数
{
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    document.cookie = name + "=" + "" + ";";
    if (arr != null)
        return (unescape(arr[2]));
    return null;
}

// function getappInfo(data){
//     console.log("------------this is getappInfo log-----------------");
//     console.log(data);
//
//     var html = "";
//     html += "<figure><img src='"+data.data.iconUrl +"' alt=' " + data.data.showName +"'><figcaption>" +
//         "<h4>" + data.data.showName + "</h4>" +
//         "<h5><span>" + data.data.downTimes + "</span></h5>"+
//         "<h5 id=''><img src='imgs/star-" +(Math.round(data.data.recommLevel / 2))+ ".png' alt='等级'> </h5>"+
//         "</figcaption></figure><a  class='game_Detil_download' href='" + data.data.packUrl + "' onclick=pushDown(5000000, data.data)>  免费下载（<span id=''>" + (data.data.packSize/ 1048576.0).toFixed(2) +"</span>MB）</a>";
//     $("div.game_Detil").html(html);
//
//     data.data.updateDesc == "" ? $("div#update_detail").hide() : $("div#update_detail p").html(data.data.updateDesc);
//     $("div#apk_detail p").html(data.data.appDesc);
//
//     var appdetails = "";
//     appdetails += "<h6 class='p_details_h'>开发商：<span id=''>"+data.data.devName +"</span></h6>"+
//         "<h6 class='p_details_h'>更新日期：<time id=''>"+ getTimeString(data.data.publishTime) +"</time></h6>"+
//         "<h6 class='p_details_h'>版本号：<span id=''>"+ data.data.verCode +"</span></h6>";
//
//     $(".app_details_h").html(appdetails);
//
//
//     var imgs = $(".swiper-slide").children("img");
//     for(var i = 0; i < 5 && i < imgs.length; i++){
//         imgs[i].src = data.data.appPicUrl[i];
//     }
// }

// function initAppInfo() {
//     //解析id获取游戏详情
//     var rx = /[&|#?]appid=[\d]*/
//     var mc = rx.exec(location.href);
//     var appId = 0;
//     if (mc != null) {
// //			id = parseInt(mc[0]);
//         appId = mc[0].replace(/[^0-9]/ig, "");
//         if (isNaN(appId))
//             appId = 0;
//     }
//     getGameDetails(appId, {success: getappInfo});
// }

// initAppInfo();
function goBack() {
    // history.back();
    history.go(-1);
}
