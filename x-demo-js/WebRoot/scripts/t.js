if (typeof dcodeIO === 'undefined' || !dcodeIO.ProtoBuf) {
	throw (new Error(
			"ProtoBuf.js is not present. Please see www/index.html for manual setup instructions."));
}
var ProtoBuf = dcodeIO.ProtoBuf;
var builder = ProtoBuf.loadProtoFile("Apps.proto");
var ReqGlobalConfig = builder.build("ReqGlobalConfig");
var msg = new ReqGlobalConfig();
msg.setGroupsCacheVer('1');
var contentPb = new Uint8Array(msg.toArrayBuffer());

$.ajax({
	type : "post",
	url : "http://115.159.125.75/appstore_api",
	async : true,
	data : contentPb,
	dataType : "binary",
	// dataType : "jsonp",
	success : function(msg) {
		console.log(msg);
	},
	error : ajaxNetworkError
});
function ajaxNetworkError(XMLHttpRequest, textStatus, errorThrown) {
	// alert("ajaxNetworkError:" + XMLHttpRequest.status + "-" +
	// XMLHttpRequest.readyState + "-" + textStatus);
	console.log("ajaxNetworkError:" + XMLHttpRequest.status + "-"
			+ XMLHttpRequest.readyState + "-" + textStatus + "-");
}