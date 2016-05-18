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


class AppiosDetailJsonHandler(APIBase):
    """
    单个ios应用详情
    """

    @tornado.gen.coroutine
    def deal(self):
        self.check_request([("appId", "应用id")])
        item = yield appiosinfo.get_app_item(self.request["appId"])
        if item:
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = item

