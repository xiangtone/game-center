


function page_onload() {
	if (configData == null)
		return;
	var rx = /(&|#?)id=(\d+)/
	var mc = rx.exec(location.href);
	var id = 0;
	if (mc != null) {
		id = parseInt(mc[2]);
		if (isNaN(id))
			id = 0;
	}

	var glist = matchGroupInfo(configData, 32, null, null, false);
	mgi = glist[id];

	$(".nec_bg").html("<img src='" + mgi.groupPicUrl + "'/>");
	var groupDesc = mgi.groupDesc;
	console.log(groupDesc.charCodeAt());
	$(".nec_h4").first().html(groupDesc.replace(/\r\n/g, '<br/>'));
	
	document.title = mgi.groupName + " - " + document.title;
	list_onload();
}

function getMatchGroupInfo() {
	return mgi;
}
