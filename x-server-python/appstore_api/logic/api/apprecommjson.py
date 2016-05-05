#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from logic.jsonhandler import APIBase, BusinessException
import logic.api.common as api_common
import logic.apprecomm as l_apprecomm


class AppRecommJsonHandler(APIBase):
    """ 应用推荐的JSON接口
    """

    @tornado.gen.coroutine
    def deal(self):
        self.check_request([("appId","应用id")])
        appid = self.request["appId"]
        app_class = self.request.get("appClass", 0)
        app_type = self.request.get("appType", 0)
        page_size = self.request.get("pageSize", 12)
        page_index = self.request.get("pageIndex", 1)
        order_type = self.request.get("orderType", 0)
        chnno = api_common.get_channelno_from_clientver(self.comm_args.get("clientVer",""))

        seri_data = l_apprecomm.req_recommapp_common(appid, chnno, '', app_type, order_type)

        if seri_data and seri_data[2]:
            dict_data = api_common.convert_seridata2dict(seri_data[2], "GroupElems")
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = dict_data
