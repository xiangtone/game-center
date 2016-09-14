#!/bin/env python
# -*- encoding=utf8 -*-
import tornado.gen
from logic.jsonhandler import APIBase, BusinessException
import logic.api.common as api_common
import logic.appinfoios as l_appinfoios


class AppInfoIosJsonHandler(APIBase):
    """应用信息的JSON接口
    """
    @tornado.gen.coroutine
    def deal(self):
        self.check_request([("appId","应用id")])
        seri_data = l_appinfoios.req_appinfoios_common(self.request["appId"])
        #seri_data = l_appinfoios.req_appinfoios_common(self.request)

        if seri_data and seri_data[2]:
            dict_data = api_common.convert_seridata2dict(seri_data[2], "AppInfoIos")
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = dict_data
