#!/bin/env python
#-*- encoding=utf8 -*-

import logic.api.userhandler
import logic.api.grouplistjson
import logic.api.groupelemjson
import logic.api.appinfojson
import logic.api.apprecommjson

HANDLER_MAP = {
    #'sendcode': logic.api.userhandler.SendCode,
    "ReqGlobalConfig":logic.api.grouplistjson.GroupListJsonHandler,
    'ReqGroupElems':logic.api.groupelemjson.GroupElemJsonHandler,
    'ReqAppInfo':logic.api.appinfojson.AppInfoJsonHandler,
    'ReqAppList4SearchKey':logic.api.groupelemjson.SearchAppListJsonHandler,
    'ReqRecommApp':logic.api.apprecommjson.AppRecommJsonHandler,
}