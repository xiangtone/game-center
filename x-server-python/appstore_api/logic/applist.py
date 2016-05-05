#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet

import common
import libs.cache as cache
import model.distribute as m_distribute
import model.appsconfig as m_appsconfig
import model.applist as m_applist
from model.const import ApiSeriCacheName
from model.const import APISrcCacheName
from model.const import BGSrcCacheName

_redis = cache.redis

GROUP_INFO = BGSrcCacheName.GroupInfoField

def req_groupelems_common(channelno, groupid, page_size, page_index, client_cache_ver='', architecture=0):
    """ 协议通用调用方法
        返回：(是否最新版本，服务器当前版本，序列化后的数据)
        获取分组信息
    """
    seri_name = ApiSeriCacheName.SERI_CHNNO_GROUP_ELEMS % (architecture, channelno, groupid, page_size, page_index)
    gen_func = m_applist.gen_group_elems
    rsp_data = common.get_cache_comm(seri_name, client_cache_ver, gen_func, groupid, page_size, page_index, seri_name, channelno, architecture)
    return rsp_data

def req_applist4searchkey_common(channelno, search_key, app_class, app_type, page_size, page_index, order_type, architecture=0):
    """ 协议通用调用方法
        返回：(序列化后的数据)
        根据关键词搜索得到的应用列表
    """
    return m_applist.SearchInfo.search_app(search_key, app_class, app_type, page_size, page_index, order_type, channelno, architecture)


class ReqGroupElems(ProtoBase):

    """获取分组元素
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:
            req = self.request
            self.comm_params.update(dict(ext2=str(req.groupId), ext3=str(req.groupClass), ext4=str(req.groupType), ext5=str(req.orderType)))

            # 根据请求的渠道号，匹配相应的缓存
            # 如果渠道号不在渠道列表中，统一取default

            channelno = int(common.get_channel_no(self.comm_params.get("clientver")))

            # 临时去除，后面补上
            #exist_channelno_array = _redis.lrange(BGSrcCacheName.CHNNOLIST, 0, -1)
            #chnno_list = [int(i) for i in exist_channelno_array if exist_channelno_array]
            #if channelno in chnno_list:
            #    seri_name = ApiSeriCacheName.SERI_CHNNO_GROUP_ELEMS % (channelno, req.groupId, req.pageSize, req.pageIndex)
            #else:
            #    seri_name = ApiSeriCacheName.SERI_CHNNO_GROUP_ELEMS % ('default', req.groupId, req.pageSize, req.pageIndex)

            # 2015.11.5注释，修改调用公共的方法
            # seri_name = ApiSeriCacheName.SERI_CHNNO_GROUP_ELEMS % (channelno, req.groupId, req.pageSize, req.pageIndex)
            # gen_func = m_applist.gen_group_elems
            # rsp_data = common.get_cache_comm(seri_name, self.request.clientCacheVer, gen_func, req.groupId, req.pageSize, req.pageIndex, seri_name, channelno)

            architecture = common.match_architecture(self.comm_params)
            rsp_data = req_groupelems_common(channelno, req.groupId, req.pageSize, req.pageIndex, req.clientCacheVer, architecture)

            self.response.rescode = 0
            self.response.resmsg = u'成功'
            self.response.serverDataVer = rsp_data[1]
            self.response.groupElems = rsp_data[2] if not rsp_data[0] else ""

        except Exception, ex:
            req = self.request
            self.get_error_result()
            self.logger.write('ReqGroupElems_' + "error_data:%s_%s_%s," % (req.groupId,req.groupClass,req.groupType), ex)
        self.callback()


class ReqAppList4SearchKey(ProtoBase):

    """根据关键词搜索得到的应用列表
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:
            req = self.request
            self.comm_params.update(dict(ext2=str(req.SearchKeyStr), ext3=str(req.isHotKey), ext4=str(req.appClass), ext5=str(req.appType)))

            channelno = int(common.get_channel_no(self.comm_params.get("clientver")))
            architecture = common.match_architecture(self.comm_params)

            self.response.rescode = 0
            self.response.resmsg = u'成功'

            # 2015.11.5注释，修改为调用公共的方法
            #self.response.groupElems = m_applist.SearchInfo.search_app(req.SearchKeyStr, req.appClass, req.appType, req.pageSize, req.pageIndex, req.orderType, channelno)
            self.response.groupElems = req_applist4searchkey_common(channelno, req.SearchKeyStr, req.appClass, req.appType, req.pageSize, req.pageIndex, req.orderType, architecture)

        except Exception, ex:
            self.get_error_result()
            req = self.request
            self.logger.write('ReqAppList4SearchKey'+ "error_data:%s_%s_%s," % (req.SearchKeyStr,req.appClass,req.appType), ex)
        self.callback()


class ReqDistributeApps(ProtoBase):

    """ 获取分发的应用列表
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:
            req = self.request
            schemeid = 103  # match_schemeid
            
            # match_groupid
            if schemeid:   
                cache_name = APISrcCacheName.MATCH_GROUPID % (schemeid, req.groupClass, req.groupType, req.orderType)
                gen_func = m_distribute.match_group_id
                groupid = common.get_cache_comm(cache_name, "", gen_func, schemeid, req.groupClass, req.groupType, req.orderType)

            # get_data
            if groupid:    
                seri_name = ApiSeriCacheName.SERI_GROUP_ELEMS % (groupid, req.pageSize, req.pageIndex)
                gen_func = m_applist.gen_group_elems
                rsp_data = common.get_cache_comm(seri_name, self.request.clientCacheVer, gen_func, groupid, req.pageSize, req.pageIndex)

            self.response.rescode = 0
            self.response.resmsg = u'成功'
            if rsp_data:
                self.response.serverDataVer = rsp_data[1]
                self.response.groupElems = rsp_data[2] if not rsp_data[0] else ""

        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqDistributeApps', ex)
        self.callback()

