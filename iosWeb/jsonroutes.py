#!/bin/env python
#-*- encoding=utf8 -*-

import handlers.applistHandler

HANDLER_MAP = {
    #'sendcode': logic.api.userhandler.SendCode,
    # "ReqGlobalConfig":logic.api.grouplistjson.GroupListJsonHandler,
    # 'ReqGroupElems':logic.api.groupelemjson.GroupElemJsonHandler,
    # 'ReqAppInfo':logic.api.appinfojson.AppInfoJsonHandler,
    # 'ReqAppList4SearchKey':logic.api.groupelemjson.SearchAppListJsonHandler,
    # 'ReqRecommApp':logic.api.apprecommjson.AppRecommJsonHandler,
    'ReqAppList': handlers.applistHandler.ApplistJsonHandler,
    # 'ReqAppDetail': handlers.appdetail.AppDetailHandler,
}