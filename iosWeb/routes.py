#!/bin/env python
#-*- encoding=utf8 -*-

import handlers.jsonhandler


URL_PATTERN = [
    (r'.*/jsonapi', handlers.jsonhandler.JSONHandler), 
    # (r'.*', logic.protohandler.ProtoHandler),
    (r'.*', handlers.jsonhandler.JSONHandler),
]
