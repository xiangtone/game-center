#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from jsonhandler import APIBase
import appstore2data



class ApplistJsonHandler(APIBase):
    """分组列表的JSON接口
    """
    @tornado.gen.coroutine
    def deal(self):

        app_list = yield appstore2data.get_app_list()
        
        self.response['rescode'] = 0
        self.response['resmsg'] = '获取成功'

        self.response['data'] = app_list



        # scheme_id = 104
        # seri_data = l_appsconfig.req_globalconfig_common(scheme_id)
        # if seri_data and seri_data[2]:
        #     dict_data = api_common.convert_seridata2dict(seri_data[2], "Groups")
        #     self.response['rescode'] = 0
        #     self.response['resmsg'] = '获取成功'
        #     self.response['data'] = dict_data
            

