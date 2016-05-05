#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from logic.jsonhandler import APIBase, BusinessException
import logic.api.common as api_common
import logic.applist as l_applist


class GroupElemJsonHandler(APIBase):
    """分组元素的JSON接口
    """
    @tornado.gen.coroutine
    def deal(self):
        
        self.check_request([("groupId","分组Id"),("pageSize","页尺寸"),("pageIndex","页码")])
        chnno = api_common.get_channelno_from_clientver(self.comm_args.get("clientVer",""))
        groupid = self.request["groupId"]
        seri_data = l_applist.req_groupelems_common(chnno, groupid, self.request["pageSize"], self.request["pageIndex"])

        if seri_data and seri_data[2]:
            dict_data = api_common.convert_seridata2dict(seri_data[2], "GroupElems")
            self.response['rescode'] = 0
            self.response['resmsg'] = '获取成功'
            self.response['data'] = dict_data
            

class SearchAppListJsonHandler(APIBase):
    """分组元素的JSON接口
    """
    @tornado.gen.coroutine
    def deal(self):
        
        self.check_request([("SearchKeyStr","搜索关键字")])
        chnno = api_common.get_channelno_from_clientver(self.comm_args.get("clientVer",""))
        search_key = self.request["SearchKeyStr"]
        app_class = self.request.get("appClass", 0)
        app_type = self.request.get("appType", 0)
        page_size = self.request.get("pageSize", 30)
        page_index = self.request.get("pageIndex", 1)
        order_type = self.request.get("orderType", 0)

        # 搜索接口暂没缓存
        seri_data = l_applist.req_applist4searchkey_common(chnno, search_key, app_class, app_type, page_size, page_index, order_type)

        self.response['rescode'] = 0
        self.response['resmsg'] = '获取成功'

        if seri_data:
            dict_data = api_common.convert_seridata2dict(seri_data, "GroupElems")
            self.response['data'] = dict_data

