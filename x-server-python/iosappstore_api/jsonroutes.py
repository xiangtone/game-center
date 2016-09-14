#!/bin/env python
# -*- encoding=utf8 -*-


import logic.api.appinfoioslist
import logic.api.appinfoiosjson

HANDLER_MAP = {
    # 'sendcode': logic.api.userhandler.SendCode,
    'ReqAppInfoIosPage':logic.api.appinfoioslist.PagesElemJsonHandler,
    'ReqAppInfoIos':logic.api.appinfoiosjson.AppInfoIosJsonHandler
}
