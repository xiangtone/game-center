#!/bin/env python
# -*- encoding=utf8 -*-


import logic.api.localappsjson

HANDLER_MAP = {
    # 'sendcode': logic.api.userhandler.SendCode,
    'ReqLocalAppInfo': logic.api.localappsjson.LocalAppsJsonHandler,
    'ReqLocalApplist': logic.api.localappsjson.LocalApplistJsonHandler
}
