/**
 * Created by win7 on 2016/5/13.
 */
var pageDone = false;
var appList = null;




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
        if (recomm.length < 8){
            recomm.push(item);
        }else {
            other.push(item);

        }
        if(item.ShowType == 1){
            banner.push(item);
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
            'iosgame_details.html?appid=' + item.AppID + "#posId="+ (1000001) +
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
            'iosgame_details.html?appid=' + item.AppID+ "#posId="+ (2000000)  +
            "'><img src='" +
            item.IconUrl +
            "' alt=" +
            'loading...' +
            "/><p>" +
            item.ShowName +
            "</p></a><a  class='download_btn' href='" +
            item.PackUrl +
            "' onclick=pushDown(2000000,'" + item.ShowName +"')>打开</a></li>"
    }
    $(".app_details_regames").html(html);
}

function initOther(data) {
    var html = "";
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        html += "<li class='g_game_li'><a href='" +
            "iosgame_details.html?appid=" + item.AppID + "#posId="+ (1000000) +
            "'><figure class='li_figure'><img src='" +
            item.IconUrl +
            "' alt='loading...'><figcaption class='li_figure_figcaption'><h4>" +
            item.ShowName +
            "</h4><h6>" +
            item.RecommWord +
            "</h6></figcaption></figure></a><div class='g_game_r'><a style='display: block'  href= '" +
            item.PackUrl +
            "' onclick=pushDown(1000000,'"+ item.ShowName+"')><button class='btn btn-danger btn-sm btn_new'>打开</button></a></div></li>"
    }
    $(".g_game").html(html);

}