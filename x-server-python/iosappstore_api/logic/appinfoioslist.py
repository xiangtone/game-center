#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet

import common
import model.appinfoioslist as m_appinfoios
from model.const import ApiSeriCacheName



def req_pageselems_common(pageSize,pageIndex,client_cache_ver=''):
    """ 协议通用调用方法
        返回：(是否最新版本，服务器当前版本，序列化后的数据)
        获取AppInfoIos信息
    """
    seri_name = ApiSeriCacheName.SERI_IOSPAGE_ELEMS % (pageIndex,pageSize)
    gen_func = m_appinfoios.gen_appinfopagelist
    rsp_data = common.get_cache_comm(seri_name,client_cache_ver,gen_func,pageSize,pageIndex)
    return rsp_data


class ReqPageElems(ProtoBase):

    """获取分组元素
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:
            req = self.request
            self.comm_params.update(dict(ext2=str(req.pageSize), ext3=str(req.pageIndex)))
            rsp_data = req_pageselems_common(req.pageSize, req.pageIndex, req.clientCacheVer)

            self.response.rescode = 0
            self.response.resmsg = u'成功'
            self.response.serverDataVer = rsp_data[1]
            self.response.pagesElems = rsp_data[2] if not rsp_data[0] else ""

        except Exception, ex:
            req = self.request
            self.get_error_result()
            self.logger.write('ReqPagesElems_' + "error_data:%s_%s," % (req.pageSize,req.pageIndex), ex)
        self.callback()