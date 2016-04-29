var pageData = { "isLoading": false, "newPageCnt": null, "keyword": null, "pageIndex": 1 };

function initAppInfo() {
	//解析id获取游戏详情
	var searchString = location.search;
	appId = searchString.substring(searchString.lastIndexOf("=") + 1, searchString.length);
	
	

	getGameDetails(appId, { success: getappInfo });
	
	getappRecomm(appId, {success : getappRecommSuccess});
}

function getappInfo(data){
	console.log("------------this is getappInfo log-----------------");
	console.log(data);
	
	var html = "";
	html += "<figure><img src='"+data.data.iconUrl +"' alt=' " + data.data.showName +"'><figcaption>" +
			"<h4>" + data.data.showName + "</h4>" +
			"<h5><span>" + data.data.downTimes + "</span></h5>"+
			"<h5 id=''><img src='imgs/star-" +(Math.round(data.data.recommLevel / 2))+ ".png' alt='等级'> </h5>"+
			"</figcaption></figure><a  class='game_Detil_download' href='#'>  免费下载（<span id=''>" + (data.data.packSize/ 1048576.0).toFixed(2) +"</span>MB）</a>";
	$("div.game_Detil").html(html);
	
	data.data.updateDesc == "" ? $("div#update_detail").hide() : $("div#update_detail p").html(data.data.updateDesc);
	$("div#apk_detail p").html(data.data.appDesc);
	
	var appdetails = "";
	appdetails += "<h6 class='p_details_h'>开发商：<span id=''>"+data.data.devName +"</span></h6>"+
    "<h6 class='p_details_h'>更新日期：<time id=''>"+ getTimeString(data.data.publishTime) +"</time></h6>"+
    "<h6 class='p_details_h'>版本号：<span id=''>"+ data.data.verCode +"</span></h6>";
	
	$(".app_details_h").html(appdetails);
	

 	var imgs = $(".swiper-slide").children("img");
	for(var i = 0; i < 5 && i < imgs.length; i++){
		imgs[i].src = data.data.appPicUrl[i];
	}
}

function getTimeString(time){
	var newTime = "";
	newTime += time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
	return newTime;
}

function getappRecommSuccess(data){
	console.log("------------this is recomm log-----------------");
	console.log(data);
	var html = "";
	for(i = 0; i < data.data.groupElemInfo.length && i < 3; i++){
		var item = data.data.groupElemInfo[i];
		html += "<li><a href='#'>" +
				"<img src='" + item.iconUrl + "' alt='" +item.showName + "'/><p>"+ item.showName + "</p></a></li>"
	}
	
	$(".app_details_regames").html(html);
	

}

initAppInfo();
