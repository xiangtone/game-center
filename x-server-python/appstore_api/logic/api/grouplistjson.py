#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from logic.jsonhandler import APIBase, BusinessException
import logic.api.common as api_common
import logic.appsconfig as l_appsconfig


class GroupListJsonHandler(APIBase):
    """分组列表的JSON接口
    """
    @tornado.gen.coroutine
    def deal(self):
        
        scheme_id = 104
        seri_data = l_appsconfig.req_globalconfig_common(scheme_id)
        if seri_data and seri_data[2]:
            dict_data = api_common.convert_seridata2dict(seri_data[2], "Groups")
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = dict_data
            

