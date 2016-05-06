#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import proto.Reported_pb2 as proto_packet
import libs.util as util
import datetime


import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )

class ReqReported(ProtoBase):
    """
        行为统计上报
    """

    def __init__(self):
         ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):

        proto_model = self.request

        if len(proto_model.reportedInfo) > 0:
            try:
                rpt_params = {}
                res_reported = self.response
                res_reported.rescode = 0
                res_reported.resmsg = u'成功'
                for i in xrange(len(proto_model.reportedInfo)):
                    rpt_info = proto_model.reportedInfo[i]
                    rpt_params['statid'] = rpt_info.statActId
                    rpt_params['statid2'] =  rpt_info.statActId2
                    rpt_params['actiontime'] =  str(util.convert_numstr_to_time(rpt_info.actionTime)) if rpt_info.actionTime else str(datetime.datetime.now)
                    rpt_params['ext1'] =  rpt_info.ext1
                    rpt_params['ext2'] =  rpt_info.ext2
                    rpt_params['ext3'] =  rpt_info.ext3
                    rpt_params['ext4'] =  rpt_info.ext4
                    rpt_params['ext5'] =  rpt_info.ext5
                    rpt_params['resaction'] = 'Rsp' + self.action[3:]
                    rpt_params['rescode'] = 0
                    self.add_stat(self.comm_params, rpt_params)

            except Exception, ex:
                self.get_error_result()
                self.logger.write("ReqReported", ex)
