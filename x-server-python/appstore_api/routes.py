#!/bin/env python
#-*- encoding=utf8 -*-
import logic.protohandler
import logic.handlers
import logic.serverhandler
import logic.jumphandler
import logic.prototesthandler
import logic.jsonhandler

URL_PATTERN = [
    (r'.*/jsonapi', logic.jsonhandler.JSONHandler),
    (r'.*/monitor',logic.serverhandler.MonitorHandler),
    (r'.*/prototest', logic.prototesthandler.ProtoTestHandler),
    (r'.*/redirect', logic.jumphandler.AppJumpHandler),
    (r'.*/appsserver/sync',logic.serverhandler.SyncHandler),
    (r'.*', logic.protohandler.ProtoHandler),
    #(r'.*', logic.handlers.NotFoundHandler),
]
