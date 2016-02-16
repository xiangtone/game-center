var menu_flag = "";
$(document).ready(function(){
   //$("input[type=radio]").next().click(function(){
    //    $(this).prev().attr("checked", true)
   //})
   
	/*列表页面checkbox全选和反选*/
	$(".checkall").click(function(){
		if ($(this).attr("checked") == true) {
			$("[type=checkbox][name=recordId]:not(:disabled)").each(function(){
				if ($(this).attr("checked") != true) {
					$(this).attr("checked", "true");
					$(this).parent().parent().attr("bgColor", "#edf6ff");
				}
			});
		} else {
			$("[type=checkbox][name=recordId]").each(function(){
				if ($(this).attr("checked") == true) {
					$(this).attr("checked", "");
					$(this).parent().parent().attr("bgColor", "#ffffff");
				}
			});
		}
	});
});
/**
 * 表格变色效果
 * @param tr
 * @return
 */
function chgTrColor(tr){
	if ($(tr).find("[type=checkbox][name=recordId]").attr("checked") == true) {
		$(tr).attr("bgColor", "#edf6ff");
	} else {
		$(tr).attr("bgColor", ($(tr).attr("bgColor") == "#edf6ff")?"#ffffff":"#edf6ff");
	}
}

/**
 * 列表批量操作
 * @param url 请求地址
 * @param itemName 参数值
 * @param paramName 参数名
 * @param msg 提示信息
 * @return
 */
function batchOperate(url, itemName, paramName, msg){
	if (msg == null || msg == "") {
		msg = "删除之后，信息无法恢复，是否确定删除？";
	}
	var item = $("[name="+itemName+"]");
	var param = "";
	if (item.size() > 0) {
		item.each(function(){
			if ($(this).attr("checked") == true) {
				param += $(this).val();
				param += ",";
			}
		});
	}
	param = param.substr(0, param.lastIndexOf(","));
	if (param == "") {
		alert("请选择记录");
		return false;
	} else {
		if (confirm(msg)) {
			if (url.indexOf("?") > 0) {
				url = url + "&" + paramName + "=" + param;
			} else {
				url = url + "?" + paramName + "=" + param;
			}
			window.location = url;
		}
	}
}

function setDisabled(filter, scope){
    $(filter, scope).each(function(){
        $(this).attr("disabled", "disabled");
    });
}

function setabled(filter, scope){
    $(filter, scope).each(function(){
        $(this).attr("disabled", false);
    });
}

function setSelect(style){
    $("select").each(function(){
        $(this).css("display", style);
    });
}

function FormatNumber(srcStr,nAfterDot){
    var srcStr,nAfterDot;
    var resultStr,nTen;
    srcStr = ""+srcStr+"";
    strLen = srcStr.length;
    dotPos = srcStr.indexOf(".",0);
    if (dotPos == -1){
        resultStr = srcStr+".";
        for (i=0;i<nAfterDot;i++){
            resultStr = resultStr+"0";
        }
        return resultStr;
    } else{
        if ((strLen - dotPos - 1) >= nAfterDot){
            nAfter = dotPos + nAfterDot + 1;
            nTen =1;
            for(j=0;j<nAfterDot;j++){
            nTen = nTen*10;
        }
        resultStr = Math.round(parseFloat(srcStr)*nTen)/nTen;
        return resultStr;
        } else{
            resultStr = srcStr;
            for (i=0;i<(nAfterDot - strLen + dotPos + 1);i++){
                resultStr = resultStr+"0";
            }
            return resultStr;
        }
    }
}

function currentTime(obj){
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    if (month < 10) {
    	month = "0" + month;
    }
    var day = now.getDate();
    if (day < 10) {
    	day = "0" + day;
    }
    var hour = now.getHours();
    if (hour < 10) {
    	hour = "0" + hour;
    }
    var minute = now.getMinutes();
    if (minute < 10) {
    	minute = "0" + minute;
    }
    var second = now.getSeconds();
    if (second < 10) {
    	second = "0" + second;
    }
    var time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    $("#" + obj).val(time);
}

/*长时间，形如 (2003-12-05 13:04:06)*/
function strDateTime(str){
    var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
    var r = str.match(reg);
    if (r == null) 
        return false;
    var d = new Date(r[1], r[3] - 1, r[4], r[5], r[6], r[7]);
    return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4] && d.getHours() == r[5] && d.getMinutes() == r[6] && d.getSeconds() == r[7]);
}

/** 
 * 复制代码，支持IE/Firefox/NS
 */
function copyToClipboard(txt){
    if (window.clipboardData) {
        window.clipboardData.clearData();
        window.clipboardData.setData("Text", txt);
    }
    else 
        if (navigator.userAgent.indexOf("Opera") != -1) {
            window.location = txt;
        }
        else 
            if (window.netscape) {
                try {
                    netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
                } 
                catch (e) {
                    alert("你使用的FireFox浏览器,复制功能被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车。\n然后将“signed.applets.codebase_principal_support”双击，设置为“true”");
                    return;
                }
                var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
                if (!clip) 
                    return;
                var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
                if (!trans) 
                    return;
                trans.addDataFlavor('text/unicode');
                var str = new Object();
                var len = new Object();
                var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
                var copytext = txt;
                str.data = copytext;
                trans.setTransferData("text/unicode", str, copytext.length * 2);
                var clipid = Components.interfaces.nsIClipboard;
                if (!clip) 
                    return false;
                clip.setData(trans, null, clipid.kGlobalClipboard);
            }
}




jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};


$.extend({
    includePath: '',
    include: function(file)
    {
        var files = typeof file == "string" ? [file] : file;
        for (var i = 0; i < files.length; i++)
        {
            var name = files[i].replace(/^\s|\s$/g, "");
            var att = name.split('.');
            var ext = att[att.length - 1].toLowerCase();
            var isCSS = ext == "css";
            var tag = isCSS ? "link" : "script";
            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " language='javascript' type='text/javascript' ";
            var link = (isCSS ? "href" : "src") + "='" + $.includePath + name + "'";
            if ($(tag + "[" + link + "]").length == 0) document.write("<" + tag + attr + link + "></" + tag + ">");
        }
    }
});


$(document).ready(function(){
   $("body").append($("#dragBody1"));
});