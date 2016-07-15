#!/bin/env python
#-*- encoding=utf8 -*-
import logic.protohandler
import logic.handlers
import logic.prototesthandler
import logic.jsonhandler



URL_PATTERN = [
    (r'.*/test', logic.prototesthandler.ProtoTestHandler),
    (r'.*/jsonapi', logic.jsonhandler.JSONHandler),#移动前端api
    (r'.*', logic.protohandler.ProtoHandler)
]
