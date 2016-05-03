var pageData = { "isLoading": false, "pageIndex": 2, "newPageCnt": null };

function page_onload() {
    if (configData == null) {
        pageDone = true;
        return;
    }

    var gi = getMatchGroupInfo();
    sendRequest(groupElems(gi.groupId), groupSuccess = { success: OnData });
    pageData.newPageCnt = null;
}

function page_onbottom() {
    if (pageData.isLoading)
        return;
    pageData.isLoading = true;

    var div = $("<div>loading...</div>").appendTo("#result");
    pageData.newPageCnt = div;
    nextPage();
}

function nextPage() {
    var gi = getMatchGroupInfo();
    var ge = groupElems(gi.groupId, pageData.pageIndex++);
    sendRequest(ge, groupSuccess = { success: OnData });
}


function OnData(e) {
    var cnt = pageData.newPageCnt == null ? $("#result") : pageData.newPageCnt
    if (e.data == null || e.data.groupElemInfo == null || e.data.groupElemInfo.length == 0) {
        cnt.html("没有更多了……");
        cnt.css({"color":"#999","font-size":"0.14rem","padding":"0.15rem 0","text-align": "center"});
        return;
    }
    var html = showList(e.data.groupElemInfo);
    cnt.html(html);
    pageData.isLoading = false;

}

