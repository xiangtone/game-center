#!/bin/env python
#-*- encoding=utf8 -*-

import logic.reported
import logic.deploysubway

HANDLER_MAP = {
    'ReqReported': logic.reported.ReqReported
    #'ReqDeploySubWay':logic.reported.ReqDeploySubWay
}
