function page_onload() {
    //matchGroupInfo(configData, 41, 4104, 0, false);
    if (configData == null)
        return;
    var gi = matchGroupInfo(configData, 41, 4104, 0, true); //搜索热词

    groupElems(gi.groupId),
    sendRequest(
             groupElems(gi.groupId),
             groupSuccess = {
                 success: onData
             });
}

function onData(e) {
    //e = {
    //    "data": {
    //        "groupElemInfo": [
    //            { "posId": -1, "iconUrl": "http://hsfs-10029187.file.myqcloud.com/M00/00/00/CmnJpFcExPSAX3mAAAAY2_HAQwg966.png", "recommWord": "\u597d\u73a9\u4e0d\u7d2f\u3001\u5feb\u4e50\u4ea4\u53cb", "recommFlag": 128, "thumbPicUrl": "", "orderNo": 0, "jumpGroupId": 0, "elemType": 1, "mainPackId": 215130, "recommLevel": 5, "mainVerCode": 67, "mainVerName": "1.7.25.67", "jumpLinkId": 0, "showType": 0, "packName": "com.wk.union.qihoo", "downTimes": 230000, "adsPicUrl": "", "mainSignCode": "9dca71dc5ce78cf329653923dc482b6c", "jumpGroupType": 0, "appTypeName": "\u7f51\u7edc\u6e38\u620f", "startTime": "20160406161400", "appId": 146135, "groupId": 101, "mainPackSize": 153495161, "jumpOrderType": 0, "jumpLinkUrl": "", "publishTime": "20260406161400", "endTime": "20260406161400", "showName": "\u4ed9\u8bed" }
    //        ]
    //    }, "rescode": 0, "resmsg": "\u83b7\u53d6\u6210\u529f"
    //};
    //var keys = [];
    //for (var i = 0; i < e.data.groupElemInfo.length; i++) {
    //    var item = e.data.groupElemInfo[i];
    //    switch (item.posId) {
    //        case 1:
    //            keys.push(item);
    //            break;
    //    }
    //}
    IniKeywords(e.data.groupElemInfo);
}

function IniKeywords(e) {
    //e = [{ "posId": 1, "iconUrl": "", "recommWord": "", "recommFlag": 0, "thumbPicUrl": "", "orderNo": 0, "jumpGroupId": 0, "elemType": 6, "mainPackId": 0, "recommLevel": 0, "mainVerCode": 0, "mainVerName": "", "jumpLinkId": 0, "showType": 0, "packName": "", "downTimes": 0, "adsPicUrl": "", "mainSignCode": "", "jumpGroupType": 0, "appTypeName": "", "startTime": "20150805193300", "appId": 0, "groupId": 113, "mainPackSize": 0, "jumpOrderType": 0, "jumpLinkUrl": "", "publishTime": "20250101000000", "endTime": "20250101000000", "showName": "仙剑奇侠" }, { "posId": 2, "iconUrl": "", "recommWord": "", "recommFlag": 0, "thumbPicUrl": "", "orderNo": 0, "jumpGroupId": 0, "elemType": 6, "mainPackId": 0, "recommLevel": 0, "mainVerCode": 0, "mainVerName": "", "jumpLinkId": 0, "showType": 0, "packName": "", "downTimes": 0, "adsPicUrl": "", "mainSignCode": "", "jumpGroupType": 0, "appTypeName": "", "startTime": "20150805193400", "appId": 0, "groupId": 113, "mainPackSize": 0, "jumpOrderType": 0, "jumpLinkUrl": "", "publishTime": "20250101000000", "endTime": "20250101000000", "showName": "西游" }];

    var cnt = $("#hotkeywords");
    var html = "";
    for (var i = 0; i < e.length; i++) {
        var item = e[i];
        html += "<li onclick='keyword_onclick(this,null);' >" + item.showName + "</li>";
    }
    cnt.html(html);

}

function keyword_onclick(sender, e) {
    var frm = document.getElementById("srFrm");
    frm["keyword"].value = sender.innerHTML;
    frm_onsubmit(frm, null);
}

function frm_onsubmit(sender, e) {
    location.href = "#" + sender["keyword"].value;
    var data = searchData(sender["keyword"].value, 0);

    sendRequest(data, { success: onSearchData });

}

function onSearchData(e) {
    var html = showList(e.data.groupElemInfo);
    $("#result").html(html);
    $(".hot_words").remove();
}