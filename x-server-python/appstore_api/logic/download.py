#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import libs.cache as cache
import proto.Apps_pb2 as proto_packet
from model.const import APISrcCacheName


_redis = cache.redis


class ReqDownRes(ProtoBase):

    """ 下载结果上报
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:
            
            req = self.request
            self.comm_params.update(
                dict(restype = 1,
                     resid = req.appId,
                     resid2 = req.packId,
                     ext1 = str(req.groupId),   # 增加时间 2015-09-11 (统计专题下载来源哪个专题)
                     ext2 = str(req.downloadRes),
                     ext3 = str(req.remark),
                     ext4 = str(req.timeConsume),
                     ext5 = str(req.downloadSpeed)))
 
            # 记录下载成功次数
            if req.downloadRes == 0:
                cache_name = APISrcCacheName.APP_DOWN_COUNT % req.appId
                _redis.hincrby(cache_name, req.packId)

            self.response.rescode = 0
            self.response.resmsg = u'成功'
            
        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqDownRes', ex)
        self.callback()
