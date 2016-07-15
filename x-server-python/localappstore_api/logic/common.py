#!/bin/env python
#-*- encoding:utf-8 -*-

import datetime
import libs.GenXmlInfo as genXmlUtils
import libs.util as util
import libs.xmlutils as xmlutil
from libs import pbjson

class Common(object):

    def sync(self):
        pass
    def genProtoAppInfo(self):
        jsonstr=genXmlUtils.parseXmltoJson(genXmlUtils.XMLFILE)
        return pbjson.json2pb(jsonstr)
