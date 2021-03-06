//if (typeof dcodeIO === 'undefined' || !dcodeIO.ProtoBuf) {
//	throw (new Error(
//			"ProtoBuf.js is not present. Please see www/index.html for manual setup instructions."));
//}
//var ProtoBuf = dcodeIO.ProtoBuf;
//var builderPacket = ProtoBuf.loadProtoFile("Packet.txt");
//var builderApps = ProtoBuf.loadProtoFile("Apps.txt");
//var ReqGlobalConfig = builderApps.build("ReqGlobalConfig");
//var RspGlobalConfig = builderApps.build("RspGlobalConfig");
//var ReqPacket = builderPacket.build("ReqPacket");
//var RspPacket = builderPacket.build("RspPacket");
//// var reqMsg = new ReqGlobalConfig();
//// reqMsg.setGroupsCacheVer('1');
//var reqMsg = new ReqPacket();
//reqMsg.setMask(1);
//reqMsg.setUdi("1");
//reqMsg.setAction("ReqGlobalConfig")
//reqMsg.setReqNo(0);
//reqMsg.setClientId(3);
//
//var params = new ReqGlobalConfig();
//
//reqMsg.setParams(new Uint8Array(params.toArrayBuffer()))
//var contentPb = new Uint8Array(reqMsg.toArrayBuffer());
//
//url = "http://115.159.125.75/appstore_api";
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
		type : "post",
		url : "http://115.159.125.75/appstore_api/jsonapi",
		async : true,
		// data : contentPb,
		data : JSON.stringify(syncConfigData),
		// dataType : "binary",
		dataType : "json",
		success : successFun.success,
		error : ajaxNetworkError
	});

}

function ajaxNetworkError(XMLHttpRequest, textStatus, errorThrown) {
	// alert("ajaxNetworkError:" + XMLHttpRequest.status + "-" +
	// XMLHttpRequest.readyState + "-" + textStatus);
	console.log("ajaxNetworkError:" + XMLHttpRequest.status + "-"
			+ XMLHttpRequest.readyState + "-" + textStatus + "-");
}

var syncHead = {
	"udi" : "a=1&b=1",
	"chnNo" : "",
	"chnPos" : "",
	"clientId" : "",
	"clientPos" : "",
	"clientVer" : ""
}

var syncConfigData = {
	"header" : syncHead,
	"api" : "ReqGlobalConfig",
	"params" : {}
}

// 获取分组数据data
function groupElems(groupId, pageIndex, pageSize) {
	var pageSize = arguments[2] ? arguments[2] : 36;
	var pageIndex = arguments[1] ? arguments[1] : 1;
	var syncGroupElems = {
		"header" : syncHead,
		"api" : "ReqGroupElems",
		"params" : {
			"groupId" : groupId,
			"pageSize" : pageSize,
			"pageIndex" : pageIndex
		}
	}
	return syncGroupElems;
}
// 搜索请求data
function searchData(searchString, pageIndex) {
	var pageIndex = arguments[1] ? arguments[1] : 1;
	var syncSearchData = {
		"header" : syncHead,
		"api" : "ReqAppList4SearchKey",
		"params" : {
			"SearchKeyStr" : searchString,
			"isHotKey" : 1,
			"pageIndex" : pageIndex
		}
	}
	return syncSearchData;
}

// 应用推荐data
function recommData(appId, appClass, appType, pageIndex) {
	var pageIndex = arguments[3] ? arguments[3] : 1;
	var syncRecommData = {
		"header" : syncHead,
		"api" : "ReqRecommApp",
		"params" : {
			"appId" : appId,
			"appClass" : appClass,
			"appType" : appType,
			"pageSize" : 3,
			"pageIndex" : pageIndex
		}
	}
	return syncRecommData;
}

function matchGroupInfo(groupList, groupClass, groupType, orderType,
		matchSingle) {
	if (groupList) {
		var matchList = [];
		if (orderType == null && groupType != null) {
			$.each(groupList, function(i, groupInfo) {
				if (groupInfo.groupClass == groupClass
						&& groupInfo.groupType == groupType) {
					matchList.push(groupInfo);
				}
			})

		} else if (orderType == null && groupType == null) {
			$.each(groupList, function(i, groupInfo) {
				if (groupInfo.groupClass == groupClass) {
					matchList.push(groupInfo);
				}
			})
		} else {
			$.each(groupList, function(i, groupInfo) {
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
	success : function(data) {
		// console.log(data.data.groupInfo);
		configData = data.data.groupInfo;
		console.log("group data success");
		// 获取热门(groupType = 4108)、最新(1200)、必备(3202)
		requestGroupElement();

		// getTopics();

		// getClassifys();
		// getHotwords();
	}
}

// 获取分组列表
sendRequest(syncConfigData, configSuccessFun);

// 搜索
sendRequest(searchData("hello"), searchSuccess = {
	success : function(data) {
		console.log("------------this is search log-----------------");
		console.log(data);
	}
})

// 获取应用推荐
sendRequest(recommData(133636), recommmSuccess = {
	success : function(data) {
		console.log("------------this is recomm log-----------------");
		console.log(data);
	}
})

getGameDetails(133636);

/*
 * 注意************banner 广告位可能跳转详情、分类、专题、 几乎所有数据请求都依赖于 globalConfig 请求 数据分页
 * 
 */

// 获取热门(groupClass = 41, groupType = 4108)、最新(12, 1200, 2)、必备(32,
// 3202)，同属于专题，有可能有主题图片，有可能没有
function requestGroupElement(group) {
	// var group = matchGroupInfo(configData, 41, 4102, null, true); //获取首页分组数据
	// var group = matchGroupInfo(configData, 41, 4108, null, true); //热门
	// var group = matchGroupInfo(configData, 12, 1200, 2, true); //最新
	// var group = matchGroupInfo(configData, 32, 3202, null, true); //必备
	// var group = matchGroupInfo(configData, 12, 1200, 0, true); //排行
	// var group = matchGroupInfo(configData, 41, 4104, 0, false); //搜索热词

	var group = group ? group : matchGroupInfo(configData, 41, 4104, 0, true);
	sendRequest(
			groupElems(group.groupId),
			groupSuccess = {
				success : function(data) {
					console.log("------------this is GroupElement log-----------------");
					console.log(data);
				}
			});
}

// 获取专题列表(groupClass = 32)
function getTopics() {
	var groupList = matchGroupInfo(configData, 32, null, null, false); // 专题列表
	console.log(groupList);
	// 获取单个专题------逐个获取
	requestGroupElement(groupList[0]);
}

// 分类数据(groupClass = 12, groupType > 1200)
function getClassifys() {
	if (configData) {
		var matchList = [];
		$.each(configData, function(i, groupInfo) {
			if (groupInfo.groupClass == 12 && groupInfo.groupType > 1200) {
				matchList.push(groupInfo);
			}
		})
		console.log(matchList);
		return matchList;
	}
}

// 获取应用详情
function getGameDetails(appId) {
	var syncAppDetailData = {
		"header" : syncHead,
		"api" : "ReqAppInfo",
		"params" : {
			"appId" : appId,
			"packId" : 0,

		}
	}
	sendRequest(syncAppDetailData,
			{
				success : function(data) {
					console.log("------------this is GameDetails log-----------------");
					console.log(data);
				}
			})
}
