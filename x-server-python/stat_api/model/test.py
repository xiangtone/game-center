#!/bin/env python
#-*- encoding=utf8 -*-
import stat_api.proto.Reported_pb2 as reported
import stat_api.proto.Packet_pb2 as packet

def gen_post_data():
    """生成测试数据
    """
    req_pack = packet.ReqPacket()
    req_pack.mask = 1
    req_pack.udi = 'ei=1'
    req_pack.clientId = 0

    rpt =  reported.ReqReported()
    for i in xrange(5):
        info =  rpt.reportedInfo.add()
        info.statActId = i + 1
        info.statActId2 = 0
        info.actionTime = '201308311525'
        info.ext1 = 'ext1'
        i = i + 1
    req_pack.action.append('ReqReported')
    req_pack.params.append(rpt.SerializeToString())

    # 封包
    req_pack.reqNo=1
    return req_pack.SerializeToString()
