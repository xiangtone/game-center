#!/bin/env python
#-*- encoding:utf-8 -*-

#
# Copyright 2016 nicajonh
#
# 作者：nicajonh
#
# 功能：提供地铁内apk网资源下载接口
#
# 说明：该文件一般不需要修改
#
# 版本：V1.0.1
import tornado.gen
from logic.jsonhandler import APIBase
from logic import localappinfo
import json

class LocalApplistJsonHandler(APIBase):
    """apk 分组的JSON接口
    """

    @tornado.gen.coroutine
    def deal(self):
        app_list = yield localappinfo.get_app_list()
        if app_list:
            # json序列化
            # app_list = json.dumps(app_list)
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = app_list


class LocalAppsJsonHandler(APIBase):
    """
    单个apk应用详情
    """
    @tornado.gen.coroutine
    def deal(self):
        #检测appid合法性
        self.check_request([('appid', "应用id")])
        item = yield localappinfo.get_app_item(self.request['appid'])
        if item:
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = item


