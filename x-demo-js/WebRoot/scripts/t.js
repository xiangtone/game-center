if (typeof dcodeIO === 'undefined' || !dcodeIO.ProtoBuf) {
	throw (new Error(
			"ProtoBuf.js is not present. Please see www/index.html for manual setup instructions."));
}
var ProtoBuf = dcodeIO.ProtoBuf;
var builder = ProtoBuf.loadProtoFile("Apps.proto");
var ReqGlobalConfig = builder.build("ReqGlobalConfig");
var RspGlobalConfig = builder.build("RspGlobalConfig");
var reqMsg = new ReqGlobalConfig();
reqMsg.setGroupsCacheVer('1');
var contentPb = new Uint8Array(reqMsg.toArrayBuffer());

url = "http://115.159.125.75/appstore_api";
var xhr = new XMLHttpRequest();
xhr.open("post", url, true);
// xhr.responseType = "blob";
xhr.responseType = "arraybuffer";
xhr.onload = function() {
	if (this.status == 200) {
		var arrayBuffer = xhr.response;
		if (arrayBuffer) {
			console.log(arrayBuffer.toString());
			var byteArray = new Uint8Array(arrayBuffer);
			for (var i = 0; i < byteArray.byteLength; i++) {
				// do something
				console.log(byteArray[i]);
			}
			var rspMsg = RspGlobalConfig.decode(arrayBuffer);
			console.log(rspMsg.rescode);
		}
	}
}
xhr.send(contentPb);

// $.ajax({
// type : "post",
// url : "http://115.159.125.75/appstore_api",
// async : true,
// data : contentPb,
// dataType : "binary",
// // dataType : "jsonp",
// success : function(msg) {
// console.log(msg);
// },
// error : ajaxNetworkError
// });
// function ajaxNetworkError(XMLHttpRequest, textStatus, errorThrown) {
// // alert("ajaxNetworkError:" + XMLHttpRequest.status + "-" +
// // XMLHttpRequest.readyState + "-" + textStatus);
// console.log("ajaxNetworkError:" + XMLHttpRequest.status + "-"
// + XMLHttpRequest.readyState + "-" + textStatus + "-");
// }
