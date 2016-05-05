#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import proto.test_pb2 as proto_packet
import libs.util as util


class ReqTest(ProtoBase):
    """获取测试结果
    """
    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        self.response.rescode = 0
        self.response.resmsg = 'I got %s and %s. ' % (self.request.name, self.request.age)
        self.response.exmsg = 'test'
        # print self.raw_request.request.remote_ip
        # print self.comm_params
        # import tornado.httpclient
        # http_client = tornado.httpclient.AsyncHTTPClient()
        # response = yield http_client.fetch('http://google.com')
        # a
        
        
        if self.request.name == 'cong':
            raise Exception('test error')
        # self.callback()
        # print 3333
