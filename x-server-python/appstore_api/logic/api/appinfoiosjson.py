#!/bin/env python
# -*- encoding=utf8 -*-
import tornado.gen
from logic.jsonhandler import APIBase
from logic import appiosinfo
import json


class ApplistJsonHandler(APIBase):
    """分组IosAppinfo的JSON接口
    """

    @tornado.gen.coroutine
    def deal(self):
        app_list = yield appiosinfo.get_app_list()
        if app_list:
            # json序列化
            # app_list = json.dumps(app_list)
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = app_list
        else:
            self.response['rescode'] = 1
            self.response['resmsg'] = ''
            self.response['data'] = None
