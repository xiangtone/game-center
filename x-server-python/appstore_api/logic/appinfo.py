#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet

import common
import model.distribute as m_distribute
import model.appsconfig as m_appsconfig
import model.appinfo as m_appinfo
from model.const import ApiSeriCacheName
from model.const import APISrcCacheName
from model.const import BGSrcCacheName


def req_appinfo_common(appid, packid=0, client_cache_ver=''):
    """ 协议通用调用方法
        返回：(是否最新版本，服务器当前版本，序列化后的数据)
        获取AppInfo信息
    """
    seri_name = ApiSeriCacheName.SERI_APP_INFO % appid
    gen_func = m_appinfo.gen_appinfo
    rsp_data = common.get_cache_comm(seri_name, client_cache_ver, gen_func, appid)
    return rsp_data


class ReqAppInfo(ProtoBase):

    """获取应用信息
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:
            req = self.request
            self.comm_params.update(dict(restype=str(1),resid=str(req.appId),resid2=str(req.packId),ext2=str(req.scrType)))

            # 2015.11.5注释，修改成调用公共方法
            # seri_name = ApiSeriCacheName.SERI_APP_INFO % req.appId
            # gen_func = m_appinfo.gen_appinfo
            # rsp_data = common.get_cache_comm(seri_name, self.request.clientCacheVer, gen_func, req.appId)
            rsp_data = req_appinfo_common(req.appId, req.packId, req.clientCacheVer)

            self.response.rescode = 0
            self.response.resmsg = u'成功'
            self.response.serverCacheVer = rsp_data[1]
            self.response.appInfo = rsp_data[2] if not rsp_data[0] else ""

        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqAppInfo', ex)
        self.callback()