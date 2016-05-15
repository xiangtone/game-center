var pageData = { "isLoading": false, "pageIndex": 1, "newPageCnt": null, "inited": false, "pageRefresh": true};

function page_onload() {
    if (configData == null) {
        pageDone = true;
        return;
    }

    var gi = matchGroupInfo(configData, 41, 4102, null, true); //获取首页分组数据
    sendRequest(groupElems(gi.groupId, pageData.pageIndex++), groupSuccess = { success: OnData });
    pageData.newPageCnt = null;

    var gi = matchGroupInfo(configData, 41, 4101, null, true); //获取推荐分组数据
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
        html += "<div class='swiper-slide'><a href='game_details.html?appId=" + item.appId + "#title=" +document.title+ "#posId="+(2000000) +"' "
            +"class='icon'><img src='" + item.iconUrl + "' /></a>"
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
    console.log("-------------------------onDate-------------" + pageData.pageIndex);
    console.log(e);

    var banner = [];
    var ad = [];
    var other = [];
    if (e.data != null && e.data.groupElemInfo != null) {
        for (var i = 0; i < e.data.groupElemInfo.length; i++) {
            var item = e.data.groupElemInfo[i];
            if(pageData.inited && item.posId >= 2){
            	other.push(item);
            	continue;
            }
            if (item.posId == 1)
                banner.push(item);
            else if (item.posId >=2 && item.showType == 1 && ad.length < 0)
                ad.push(item);
            else if (item.posId >= 2)
                other.push(item);
        }
    }
    if (!pageData.inited) {
        BannerInit(banner);
        BannerAdInit(ad);
        pageData.inited = true;
    }
    JinpinInit(other);
}

function BannerInit(e) {
    if (pageData.inited)
        return;
    var ol = "<ol class='carousel-indicators'>";
    var div = "<div class='carousel-inner'>";
    for (var i = 0; i < e.length; i++) {
        var item = e[i];
        ol += "<li data-target='#myCarousel' data-slide-to='" + i + "'" + (i == 0 ? " class='active'" : "") + "></li>";
        div += "<div class='item" + (i == 0 ? " active" : "") + "'>"
                + "<a href='game_details.html?appid=" + item.appId + "#title=" +document.title+ "#posId="+ (1000000 + item.posId) +"'><img class='img' src='" + item.adsPicUrl + "' alt='" + item.showName + "'></a>"
                + "<div class='carousel-caption'></div></div>"
    }
    ol += "</ol>";
    div += "</div>";
    $("#myCarousel").html(ol + div);


    $('.carousel').carousel({
        interval: 3000
    });

}

function BannerAdInit(e) {
    if (pageData.inited)
        return;

    var html = "";
    for (var i = 0; i < e.length && i < 2; i++) {
        var item = e[i];// { "posId": -1, "iconUrl": "http://hsfs-10029187.file.myqcloud.com/M00/00/00/CmnJpFcExPSAX3mAAAAY2_HAQwg966.png", "recommWord": "\u597d\u73a9\u4e0d\u7d2f\u3001\u5feb\u4e50\u4ea4\u53cb", "recommFlag": 128, "thumbPicUrl": "", "orderNo": 0, "jumpGroupId": 0, "elemType": 1, "mainPackId": 215130, "recommLevel": 5, "mainVerCode": 67, "mainVerName": "1.7.25.67", "jumpLinkId": 0, "showType": 0, "packName": "com.wk.union.qihoo", "downTimes": 230000, "adsPicUrl": "", "mainSignCode": "9dca71dc5ce78cf329653923dc482b6c", "jumpGroupType": 0, "appTypeName": "\u7f51\u7edc\u6e38\u620f", "startTime": "20160406161400", "appId": 146135, "groupId": 101, "mainPackSize": 153495161, "jumpOrderType": 0, "jumpLinkUrl": "", "publishTime": "20260406161400", "endTime": "20260406161400", "showName": "\u4ed9\u8bed" };
        html += "<a href='game_details.html?appid=" + item.appId +"#title=" +document.title+ "#posId="+ (1100000) +"'><img class='item_img' style='width:100%' src='" + item.adsPicUrl + "' alt='" + item.showName + "'></a>";
    }

    $("#adbanner").html(html);
}






function JinpinInit(e) {
    var cnt = pageData.newPageCnt == null ? $(".g_game") : pageData.newPageCnt;
    if (e == null || e.length == 0) {
        cnt.html("没有更多了……");
        cnt.css({"color":"#999","font-size":"0.14rem","padding":"0.15rem 0","text-align": "center","border-left":"none","border-right":"none"});
        return;
    }
    pageData.isLoading = false;
    var html = getTableRow(e);
    if (pageData.newPageCnt == null) {
        cnt.html(html);
    } else {
        cnt.before(html);
        cnt.remove();
    }

}

function getTableRow(e) {
    var html = "";
    var falgs = ["官方", "推荐", "首发", "免费", "礼包", "活动", "内测", "热门"];

    for (var i = 0; i < e.length; i++) {
        var item = e[i]; //<tr><td class='td_img'><a href='game_details.html?appid=" + item.appId
        html += "<li class='g_game_li'><a href='game_details.html?appId=" + item.appId+"#title=" +document.title+"#posId="+ (1000000) +"'><figure class='li_figure'><img src='" + item.iconUrl + "' alt='" + item.showName + "' >" +
        		"<figcaption class='li_figure_figcaption'><h4>" + item.showName + "</h4><h5>";

        for (var f = 0 ; f < 8; f++) {
            if ((item.recommFlag & (Math.pow(2, f))) != 0)
                html += "<span class='type" + f + "'>" + falgs[f] + "</span>";
        }

        // <span class='type1'>推荐</span>
        html += "</h5><h6><i>" + item.downTimes + "</i>人下载      &nbsp;&nbsp;        <i>" + (item.mainPackSize / 1048576.0).toFixed(2) + "</i>MB              </h6><h6>"+item.recommWord+"</h6>"
                + "</figcaption></figure></a><div class='g_game_r'>"
                + "<a style='display: block'  href='javascript:void(0);' onclick='getApk("+ item.appId +"," + 1000000+ ");' >"
                +"<button class='btn btn-danger btn-sm btn_new'>下载</button></a></div></li>";
    }
    html += "";
    return html;
}

function clickAppDeail(){
//	alert("hello");
	alert(item.showName);
}

function page_onbottom() {
    if (pageData.isLoading)
        return;
    pageData.isLoading = true;

    var li = $("<li>loading...</li>").appendTo(".g_game");
    pageData.newPageCnt = li;
    nextPage();
}

function nextPage() {
    var gi = matchGroupInfo(configData, 41, 4102, null, true); //获取首页分组数据
    sendRequest(groupElems(gi.groupId, pageData.pageIndex++), groupSuccess = { success: OnData });

}




jQuery(document).ready(function($){
    // browser window scroll (in pixels) after which the "back to top" link is shown
    var offset = 300,
    //browser window scroll (in pixels) after which the "back to top" link opacity is reduced
        offset_opacity = 800,
    //duration of the top scrolling animation (in ms)
        scroll_top_duration = 500,
    //grab the "back to top" link
        $back_to_top = $('.cd-top');

    //hide or show the "back to top" link
    $(window).scroll(function(){
        ( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if( $(this).scrollTop() > offset_opacity ) {
            $back_to_top.addClass('cd-fade-out');
        }
    });

    //smooth scroll to top
    $back_to_top.on('click', function(event){
        event.preventDefault();
        $('body,html').animate({
                scrollTop: 0 ,
            }, scroll_top_duration
        );
    });

});


