var configData = null;
var pageDone = false;

$(document).ready(function () {
    $(window).scroll(window_onscroll);
});

// var configChannelId = "BD537D84B909BE7962F046A0AA0CBD8C";   //huashengWIFI
var configChannelId = "038360A9989CAEAFF00318BBB3400757";   //schoolWIFI

document.write("<script src='" +
    getChannel()+
    "'><\/script>");

function getChannel(){
    //渠道设置
    //1.school
    return "http://sdk.talkingdata.com/app/h5/v1?appid="+ configChannelId +"&vn=正式版本v1.0&vc=16.05.23";
}
//http://127.0.0.1:42010/jsonapi
//http://appstore.api.huashenggame.com/jsonapi
function sendRequest(syncConfigData, successFun) {
    $.ajax({
        type: "post",
        url: "http://http://127.0.0.1:42010/jsonapi",
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
    "api": "ReqGlobalConfig",
    "params": {}
}

// 获取分组数据data
function groupElems(groupId, pageIndex, pageSize) {
    var pageSize = arguments[2] ? arguments[2] : 36;
    var pageIndex = arguments[1] ? arguments[1] : 1;
    var syncGroupElems = {
        "header": syncHead,
        "api": "ReqGroupElems",
        "params": {
            "groupId": groupId,
            "pageSize": pageSize,
            "pageIndex": pageIndex
        }
    }
    return syncGroupElems;
}
// 搜索请求data
function searchData(searchString, pageIndex) {
    var pageIndex = arguments[1] ? arguments[1] : 1;
    var syncSearchData = {
        "header": syncHead,
        "api": "ReqAppList4SearchKey",
        "params": {
            "SearchKeyStr": searchString,
            "isHotKey": 1,
            "pageIndex": pageIndex
        }
    }
    return syncSearchData;
}

// 应用推荐data
function recommData(appId, appClass, appType, pageIndex) {
    var pageIndex = arguments[3] ? arguments[3] : 1;
    var syncRecommData = {
        "header": syncHead,
        "api": "ReqRecommApp",
        "params": {
            "appId": appId,
            "appClass": appClass,
            "appType": appType,
            "pageSize": 3,
            "pageIndex": pageIndex
        }
    }
    return syncRecommData;
}

function matchGroupInfo(groupList, groupClass, groupType, orderType,
                        matchSingle) {
    if (groupList) {
        var matchList = [];
        if (orderType == null && groupType != null) {
            $.each(groupList, function (i, groupInfo) {
                if (groupInfo.groupClass == groupClass
                    && groupInfo.groupType == groupType) {
                    matchList.push(groupInfo);
                }
            })

        } else if (orderType == null && groupType == null) {
            $.each(groupList, function (i, groupInfo) {
                if (groupInfo.groupClass == groupClass) {
                    matchList.push(groupInfo);
                }
            })
        } else {
            $.each(groupList, function (i, groupInfo) {
                if (groupInfo.groupClass == groupClass
                    && groupInfo.groupType == groupType
                    && groupInfo.orderType == orderType) {
                    matchList.push(groupInfo);
                }
            })
        }
        if (matchList.length > 0) {
            if (matchSingle) {
                return matchList[0];
            } else {
                return matchList;
            }
        }

    }

}

var configSuccessFun = {
    success: function (data) {
        // console.log(data.data.groupInfo);
        configData = data.data.groupInfo;
        if (window.localStorage) {
            window.localStorage.setItem("globalData", JSON.stringify(data.data.groupInfo));
        }
        console.log("group data success");
        console.log(configData);
        console.log("pageDone " + pageDone);
        if (pageDone && typeof page_onload != "undefined") {
            page_onload();
        }

    }
}


// 获取分组列表
// sendRequest(syncConfigData, configSuccessFun);
//每个页面都会执行这个方法，检查globalConfig 是否存在
function initGlobalData(hardRefresh) {
    if (window.localStorage && !hardRefresh) {
        configData = JSON.parse(window.localStorage.getItem("globalData"));
        if (typeof configData == "undefined")
            sendRequest(syncConfigData, configSuccessFun);
    } else {
        sendRequest(syncConfigData, configSuccessFun);
    }

    getPosIdPush();
}

function setBackTitle() {
    //设置back title
    //取出title
    var rx = /[&|#?](title)=[^&|#?]*/
    var mc = rx.exec(decodeURI(location.href));
    var title = "";
    if (mc != null) {
//			id = parseInt(mc[0]);
        title = mc[0].replace(/[&|#?](title)=/ig, "");
        if (title != "") {
            // try {
            title = decodeURI(title);
            $(".fa").html('返回');
            // } catch (e) {
            //     console.log("head not found");
            // }
        }

    }
}

function getPosIdPush() {
    var rx = /[&|#?](posId)=[\d]*/
    var mc = rx.exec(decodeURI(location.href));
    var id = 0;
    if (mc != null) {
        id = mc[0].replace(/[&|#?](posId)=/ig, "");
        if (!isNaN(id)){
            TDAPP.onEvent("页面点击", id);
        }

    }
}

// 分类数据(groupClass = 12, groupType > 1200)
function getClassifys() {
    var matchList = [];
    $.each(configData, function (i, groupInfo) {
        if (groupInfo.groupClass == 12 && groupInfo.groupType > 1200) {
            matchList.push(groupInfo);
        }
    })
    return matchList;

}

// 获取应用详情
function getGameDetails(appId, successFun) {
    var syncAppDetailData = {
        "header": syncHead,
        "api": "ReqAppInfo",
        "params": {
            "appId": appId,
            "packId": 0

        }
    }
    sendRequest(syncAppDetailData, successFun);
}

// 获取应用推荐
function getappRecomm(appId, callback) {
    sendRequest(recommData(appId), callback);
}

function showList(data, totalPos) {
    var html = "<table>";
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        html += "<tr><td class='td_img'><a href='game_details.html?appid="
            + item.appId
            + "#title=" + document.title + "#posId="+ (totalPos) +"'><img class='logo_img' src='"
            + item.iconUrl
            + "' alt='"
            + item.showName
            + "'/></a></td>"
            + "<td class='td_h'><a href='game_details.html?appid="
            + item.appId
            + "#title=" + document.title + "#posId="+ (totalPos) +"'><h4>"
            + item.showName
            + "</h4>"
        + "<h5><img src='imgs/star-"
        + (item.recommLevel / 2).toFixed()
        + ".png' alt='等级'> </h5>"
        + "<h5><span>"

        + "<h5><span>"
            + item.downTimes
            + "</span>人下载 &nbsp;&nbsp;<span>"
            + (item.mainPackSize / 1048576.0).toFixed(2)
            + "</span>MB</h5></a>"
            + "</td><td><a href='javascript:void(0);' onclick='getApk(" + item.appId +"," + totalPos+ ");' >"
            +"<button class='btn btn-warning btn-sm'>下载</button></a></td></tr>";
    }
    return html + "</table>";

}

function getApk(appid, posId) {
//	alert("下载APK" + appid);
    getGameDetails(
        appid,
        appInfoSuccess = {
            success: function (data) {
                console.log("------------this is getappInfo log-----------------");
                console.log(data);

                var apkUrl = data.data.packUrl;
                try {
                    var elemIF = document.createElement("iframe");
                    elemIF.src = apkUrl;
                    elemIF.style.display = "none";
                    document.body.appendChild(elemIF);
                    pushDown(posId, data.data);
                } catch (e) {

                }
            }
        });

}

function pushDown(posId, item) {
    var kv = {"appName": item.showName, "packName": item.packName};
    TDAPP.onEvent("列表点击下载", posId, kv);

}

function pushDownHuaS(posId) {
    var kv = {"appName": "花生游戏中心", "packName": "com.hykj.gamecenter"};
    TDAPP.onEvent("列表点击下载", posId, kv);
}

function pushDownDetail(posId, item) {
    // var item = JSON.parse(itemString);
    var kv = {"appName": item.showName, "packName": item.packName};
    TDAPP.onEvent("详情下载", posId, kv);
}



function goBack() {
    // history.back();
    history.go(-1);
}

function goSearch() {
    // window.open("search.html" + "#title=" + document.title);
    // var cnt = obj.value;
    // obj.target="_blank";
    // obj.href = "search.html#title="+document.title;
    // obj.click();
    window.location.href = "search.html#title="+document.title;
}

function window_onscroll() {
    var offset = $(document).height() - $(window).scrollTop()
        - $(window).height();
    // var offset = $(document).scrollTop() + $(document).clientHeight() -
    // $(document).scrollHeight();
    if (Math.abs(offset) < 100) {
        if (typeof (page_onbottom) != "undefined")
            page_onbottom();
    }
}
