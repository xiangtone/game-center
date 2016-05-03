var configData = null;
var pageDone = false;

$(document).ready(function () {
    $(window).scroll(window_onscroll);
});

// var ProtoBuf = dcodeIO.ProtoBuf;
// var builderPacket = ProtoBuf.loadProtoFile("Packet.txt");
// var builderApps = ProtoBuf.loadProtoFile("Apps.txt");
// var ReqGlobalConfig = builderApps.build("ReqGlobalConfig");
// var RspGlobalConfig = builderApps.build("RspGlobalConfig");
// var ReqPacket = builderPacket.build("ReqPacket");
// var RspPacket = builderPacket.build("RspPacket");
// // var reqMsg = new ReqGlobalConfig();
// // reqMsg.setGroupsCacheVer('1');
// var reqMsg = new ReqPacket();
// reqMsg.setMask(1);
// reqMsg.setUdi("1");
// reqMsg.setAction("ReqGlobalConfig")
// reqMsg.setReqNo(0);
// reqMsg.setClientId(3);

// var params = new ReqGlobalConfig();

// reqMsg.setParams(new Uint8Array(params.toArrayBuffer()))
// var contentPb = new Uint8Array(reqMsg.toArrayBuffer());

// url = "http://115.159.125.75/appstore_api";
// var xhr = new XMLHttpRequest();
// xhr.open("post", url, true);
// // xhr.responseType = "blob";
// xhr.responseType = "arraybuffer";
// xhr.onload = function() {
// if (this.status == 200) {
// var arrayBuffer = xhr.response;
// if (arrayBuffer) {
// console.log(arrayBuffer.toString());
// var byteArray = new Uint8Array(arrayBuffer);
// for (var i = 0; i < byteArray.byteLength; i++) {
// // do something
// console.log(byteArray[i]);
// }
// // var rspMsg = RspGlobalConfig.decode(arrayBuffer);
// var rspMsg = RspPacket.decode(arrayBuffer);
// console.log(rspMsg);
// }
// }
// }
// xhr.send(contentPb);

function sendRequest(syncConfigData, successFun) {
    $.ajax({
        type: "post",
        url: "http://115.159.125.75/appstore_api/jsonapi",
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
    "clientId": 12,
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

    // setBackTitle();
}

function setBackTitle() {
    //设置back title
    //取出title
    var rx = /[&|#?](title)=[^&|#?]*/
    var mc = rx.exec(location.href);
    var title = "";
    if (mc != null) {
//			id = parseInt(mc[0]);
        title = mc[0].replace(/[&|#?](title)=/ig, "");
        if (title != "") {
            // try {
            $(".fa.fa-chevron-left").html(title);
            // } catch (e) {
            //     console.log("head not found");
            // }
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
            "packId": 0,

        }
    }
    sendRequest(syncAppDetailData, successFun);
}

// 获取应用推荐
function getappRecomm(appId, callback) {
    sendRequest(recommData(appId), callback);
}

function showList(data) {
    var html = "<table>";
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        html += "<tr><td class='td_img'><a href='game_details.html#appid="
            + item.appId
            + "#title=" + document.title + "'><img class='logo_img' src='"
            + item.iconUrl
            + "' alt='"
            + item.showName
            + "'/></a></td>"
            + "<td class='td_h'><a href='game_details.html#appid="
            + item.appId
            + "#title=" + document.title + "'><h4>"
            + item.showName
            + "</h4>"
            + "<h5><img src='imgs/star-"
            + (item.recommLevel / 2).toFixed()
            + ".png' alt='等级'> </h5>"
            + "<h5><span>"
            + item.downTimes
            + "</span>人下载 &nbsp;&nbsp;<span>"
            + (item.mainPackSize / 1048576.0).toFixed(2)
            + "</span>MB</h5></a>"
            + "</td><td><a href='javascript:void(0);' onclick='getApk("
            + item.appId
            + ");' ><button class='btn btn-warning btn-sm'>下载</button></a></td></tr>";
    }
    return html + "</table>";

}

function getApk(appid) {
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
                } catch (e) {

                }
            }
        });

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
