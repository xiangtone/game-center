
function page_onload() {
    if (configData == null) {
        pageDone = true;
        return;
    }

    var gi = matchGroupInfo(configData, 41, 4102, null, true); //获取首页分组数据
    groupElems(gi.groupId);
    sendRequest(groupElems(gi.groupId), groupSuccess = { success: OnData });

    var gi = matchGroupInfo(configData, 41, 4101, null, true); //获取首页分组数据
    groupElems(gi.groupId);
    sendRequest(groupElems(gi.groupId), groupSuccess = { success: tuijianOnData });



}
function tuijianOnData(e) {
    //var icons = [];
    //for (var i = 0; i < e.data.groupElemInfo.length; i++) {
    //    var item = e.data.groupElemInfo[i];

    //}
    if (e.data == null || e.data.groupElemInfo == null)
        return;

    var html = "";
    for (var i = 0; i < e.data.groupElemInfo.length; i++) {
        var item = e.data.groupElemInfo[i];
        html += "<div class='swiper-slide'><a href='game_details.html?appId=" + item.appId + "' class='icon'><img src='" + item.iconUrl + "' /></a>"
            + "<p class='iconwz'>" + item.showName + "</p></div>";
    }
    $(".swiper-wrapper").html(html);
    //中间拖的
    new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        slidesPerView: 3.7,
        paginationClickable: true,
        spaceBetween: 25,
        freeMode: true
    });
}

function OnData(e) {
    //e = {
    //    "data": {
    //        "groupElemInfo": [
    //            { "posId": -1, "iconUrl": "http://hsfs-10029187.file.myqcloud.com/M00/00/00/CmnJpFcExPSAX3mAAAAY2_HAQwg966.png", "recommWord": "\u597d\u73a9\u4e0d\u7d2f\u3001\u5feb\u4e50\u4ea4\u53cb", "recommFlag": 128, "thumbPicUrl": "", "orderNo": 0, "jumpGroupId": 0, "elemType": 1, "mainPackId": 215130, "recommLevel": 5, "mainVerCode": 67, "mainVerName": "1.7.25.67", "jumpLinkId": 0, "showType": 0, "packName": "com.wk.union.qihoo", "downTimes": 230000, "adsPicUrl": "", "mainSignCode": "9dca71dc5ce78cf329653923dc482b6c", "jumpGroupType": 0, "appTypeName": "\u7f51\u7edc\u6e38\u620f", "startTime": "20160406161400", "appId": 146135, "groupId": 101, "mainPackSize": 153495161, "jumpOrderType": 0, "jumpLinkUrl": "", "publishTime": "20260406161400", "endTime": "20260406161400", "showName": "\u4ed9\u8bed" }
    //        ]
    //    }, "rescode": 0, "resmsg": "\u83b7\u53d6\u6210\u529f"
    //};
    var banner = [];
    var ad = [];
    var other = [];
    for (var i = 0; i < e.data.groupElemInfo.length; i++) {
        var item = e.data.groupElemInfo[i];
        if (item.posId == 1)
            banner.push(item);
        else if (item.showType == 1 && ad.length < 2)
            ad.push(item);
        else
            other.push(item);
    }

    BannerInit(banner);
    BannerAdInit(ad);
    JinpinInit(other);
}

function BannerInit(e) {
    //e = [{
    //    "posId": 1,
    //    "iconUrl": "http://hsfs-10029187.file.myqcloud.com/M00/00/03/CmnJpFcUTzqEcLwxAAAAAFneTgQ847.png",
    //    "recommWord": "让您可以更加真实的体验这个魔幻世界。",
    //    "recommFlag": 3,
    //    "thumbPicUrl": "",
    //    "orderNo": 25,
    //    "jumpGroupId": 0,
    //    "elemType": 1,
    //    "mainPackId": 215168,
    //    "recommLevel": 5,
    //    "mainVerCode": 104000,
    //    "mainVerName": "1.2.0",
    //    "jumpLinkId": 0,
    //    "showType": 1,
    //    "packName": "com.xcqy.shiyuegamekkkwan",
    //    "downTimes": 3222,
    //    "adsPicUrl": "http://hsfs-10029187.file.myqcloud.com/M00/00/04/CmnJpFcUUqWEdZFnAAAAAPUY56Q921.jpg",
    //    "mainSignCode": "0fdad1f58075d73b31783ca354a85ef6",
    //    "jumpGroupType": 0,
    //    "appTypeName": "角色扮演",
    //    "startTime": "20160418112000",
    //    "appId": 146173,
    //    "groupId": 101,
    //    "mainPackSize": 160614358,
    //    "jumpOrderType": 0,
    //    "jumpLinkUrl": "",
    //    "publishTime": "20260418112000",
    //    "endTime": "20260418112000",
    //    "showName": "星辰奇缘"
    //}
    //];


    var ol = "<ol class='carousel-indicators'>";
    var div = "<div class='carousel-inner'>";
    for (var i = 0; i < e.length; i++) {
        var item = e[i];
        ol += "<li data-target='#myCarousel' data-slide-to='" + i + "'" + (i == 0 ? " class='active'" : "") + "></li>";
        div += "<div class='item" + (i == 0 ? " active" : "") + "'>"
                + "<a href=''><img class='img' src='" + item.adsPicUrl + "' alt='" + item.showName + "'></a>"
                + "<div class='carousel-caption'></div></div>"
    }
    ol += "</ol>";
    div += "</div>";
    $("#myCarousel").html(ol + div);

    //    <li data-target='#myCarousel' data-slide-to='0' class='active'></li>
    //    <li data-target='#myCarousel' data-slide-to='1'></li>
    //    <li data-target='#myCarousel' data-slide-to='2'></li>
    //</ol>
    //<!-- 轮播（Carousel）项目 -->
    //<div class='carousel-inner'>
    //    <div class='item active'>
    //        <a href=''><img class='img' src='imgs/805-322.jpg' alt='First slide'></a>
    //        <div class='carousel-caption'></div>
    //    </div>
    //    <div class='item'>
    //        <a href=''><img class='img' src='imgs/805x322.jpg' alt='Second slide'></a>
    //        <div class='carousel-caption'></div>
    //    </div>
    //    <div class='item'>
    //        <a href=''><img class='img' src='imgs/8057322.jpg' alt='Third slide'></a>
    //        <div class='carousel-caption'></div>
    //    </div>
    //</div>";




    $('.carousel').carousel({
        interval: 3000
    });

}

function BannerAdInit(e) {
    var html = "";
    //<a href="game_details.html"><img class="item_img" style="width:100%" src="imgs/350x210.jpg" alt="banner"></a>
    for (var i = 0; i < e.length || e < 2; i++) {
        var item = e[i];// { "posId": -1, "iconUrl": "http://hsfs-10029187.file.myqcloud.com/M00/00/00/CmnJpFcExPSAX3mAAAAY2_HAQwg966.png", "recommWord": "\u597d\u73a9\u4e0d\u7d2f\u3001\u5feb\u4e50\u4ea4\u53cb", "recommFlag": 128, "thumbPicUrl": "", "orderNo": 0, "jumpGroupId": 0, "elemType": 1, "mainPackId": 215130, "recommLevel": 5, "mainVerCode": 67, "mainVerName": "1.7.25.67", "jumpLinkId": 0, "showType": 0, "packName": "com.wk.union.qihoo", "downTimes": 230000, "adsPicUrl": "", "mainSignCode": "9dca71dc5ce78cf329653923dc482b6c", "jumpGroupType": 0, "appTypeName": "\u7f51\u7edc\u6e38\u620f", "startTime": "20160406161400", "appId": 146135, "groupId": 101, "mainPackSize": 153495161, "jumpOrderType": 0, "jumpLinkUrl": "", "publishTime": "20260406161400", "endTime": "20260406161400", "showName": "\u4ed9\u8bed" };
        html += "<a href='game_details.html#appid=" + item.appId + "'><img class='item_img' style='width:100%' src='" + item.adsPicUrl + "' alt='" + item.showName + "'></a>";
    }

    $("#adbanner").html(html);
}

function JinpinInit(e) {

    var html = "";
    var falgs = ["官方", "推荐", "首发", "免费", "礼包", "活动", "内测", "热门"];

    for (var i = 0; i < e.length; i++) {
        var item = e[i]; //<tr><td class='td_img'><a href='game_details.html#appid=" + item.appId
        html += "<li class='g_game_li'><figure><img src='" + item.iconUrl + "' alt='" + item.showName + "'><figcaption>"
               + "<h4>" + item.recommWord + "</h4><h5>";

        for (var f = 0 ; f < 8; f++) {
            if ((item.recommFlag & (Math.pow(2 , f))) != 0)
                html += "<span class='type" + f + "'>" + falgs[f] + "</span>";
        }

        // <span class='type1'>推荐</span>
        html += "</h5><h6><i>" + item.downTimes + "</i>人下载</h6><h6><i>" + (item.mainPackSize / 1048576.0).toFixed(2) + "</i>MB</h6>"
                + "</figcaption></figure><div class='g_game_r'>"
                + "<a style='display: block' href='game_details.html#appId=" + item.appId
                + "'><button class='btn btn-danger btn-sm btn_new'>马上下载</button></a></div></li>";
    }


    $(".g_game").html(html);

}