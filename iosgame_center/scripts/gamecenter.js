/**
 * Created by win7 on 2016/5/13.
 */
var pageDone = false;
var appList = null;


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

var syncConfigData = {
    "header": syncHead,
    "api": "ReqIosAppInfo",
    "params": {}
}

var configSuccessFun = {
    success: function (data) {
        // console.log(data.data);
        appList = data.data;
        // if (window.localStorage) {
        //     window.localStorage.setItem("globalData", JSON.stringify(data.data.groupInfo));
        // }
        console.log("group data success");
        console.log(appList);
        if (pageDone) {
            page_onload();
        }
    }
}

sendRequest(syncConfigData, configSuccessFun);

function page_onload() {
    if (appList == null) {
        return;
    }

    var banner = [];
    var recomm = [];
    var other = [];
    for (var i = 0; i < appList.length; i++) {
        var item = appList[i];
        if (item.ShowType == 1) {
            banner.push(item);
        } else if (recomm.length < 8) {
            recomm.push(item);
        } else {
            other.push(item);
        }
    }
    initBanner(banner);
    initRecomm(recomm);
    initOther(other);
}

function initBanner(data) {
    var ol = "<ol class='carousel-indicators'>";
    var div = "<div class='carousel-inner'>";
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        ol += "<li data-target='#myCarousel' data-slide-to='" + i + "'" + (i == 0 ? "class='active'" : "") + "></li>";
        div += "<div class=" +
            (i == 0 ? "'item active'" : "item") +
            "><a href='" +
            'iosgame_details.html?appid=' + item.AppID +
            "'><img class='img' src='" +
            item.AdsPicUrl +
            "' alt=" +
            item.ShowName +
            "></a><div class='carousel-caption'></div></div>"
    }
    ol += "</ol>";
    div += "</div>";
    $("#myCarousel").html(ol + div);
    $('.carousel').carousel({
        interval: 3000
    });
}

function initRecomm(data) {
    var html = "";
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        html += "<li><a href='" +
            'iosgame_details.html?appid=' + item.AppID +
            "'><img src='" +
            item.IconUrl +
            "' alt=" +
            'loading...' +
            "/><p>" +
            item.ShowName +
            "</p></a><a  class='download_btn' href='" +
            item.PackUrl +
            "'>打开</a></li>"
    }
    $(".app_details_regames").html(html);
}

function initOther(data) {
    var html = "";
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        html += "<li class='g_game_li'><a href='" +
            "iosgame_details.html?appid=" + item.AppID +
            "'><figure class='li_figure'><img src='" +
            item.IconUrl +
            "' alt='loading...'><figcaption class='li_figure_figcaption'><h4>" +
            item.ShowName +
            "</h4><h6>" +
            item.RecommWord +
            "</h6></figcaption></figure></a><div class='g_game_r'><a style='display: block'  href= '" +
            item.PackUrl +
            "'><button class='btn btn-danger btn-sm btn_new'>打开</button></a></div></li>"
    }
    $(".g_game").html(html);


}

function allClick(itemId) {
    if (appList != null) {
        for (var i = 0; i < appList.length; i++) {
            var item = appList[i];
            if (item.AppID == itemId) {
                // itemJsonString = JSON.stringify(item);
                SetCookie("ios_game_app", JSON.stringify(item));
            }
        }
    }
}

function SetCookie(name, value)//两个参数，一个是cookie的名子，一个是值
{
    var exp = new Date();    //new Date("December 31, 9998");
    exp.setTime(exp.getTime() + 60 * 1000);
    var esvalue = escape(value);
    document.cookie = name + "=" + esvalue + ";expires=" + exp.toGMTString();
}
function goBack() {
    // history.back();
    history.go(-1);
}