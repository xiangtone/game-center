﻿
function PageOnload() {
    var gi = matchGroupInfo(configData, 41, 4102, null, true); //获取首页分组数据

    groupElems(group.groupId),
    sendRequest(
             groupElems(group.groupId),
             groupSuccess = {
                 success: OnData
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
    for (var i = 0; i < e.data.groupElemInfo.length; i++) {
        var item = e.data.groupElemInfo[i];
        switch (item.posId) {
            case 1:
                banner.push(item);
                break;
        }
    }
    BannerInit(banner);
}

function BannerInit(e) {

}