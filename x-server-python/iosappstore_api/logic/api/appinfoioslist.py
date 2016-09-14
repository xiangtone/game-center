#!/bin/env python
# -*- encoding=utf8 -*-
import tornado.gen
from logic.jsonhandler import APIBase
import logic.api.common as api_common
import logic.appinfoioslist as l_applist
import json


class PagesElemJsonHandler(APIBase):
    """分组元素的JSON接口
    """

    @tornado.gen.coroutine
    def deal(self):
        self.check_request([("pageSize", "页尺寸"), ("pageIndex", "页码")])
        seri_data = l_applist.req_pageselems_common(self.request["pageSize"], self.request["pageIndex"])

        if seri_data and seri_data[2]:
            dict_data = api_common.convert_seridata2dict(seri_data[2], "IosApplist")
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = dict_data
