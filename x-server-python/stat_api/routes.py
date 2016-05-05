#!/bin/env python
#-*- encoding=utf8 -*-
import logic.protohandler
import logic.handlers
import logic.prototesthandler


URL_PATTERN = [
    (r'.*/test', logic.prototesthandler.ProtoTestHandler),
    (r'.*', logic.protohandler.ProtoHandler),
]
