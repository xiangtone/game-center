#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from handlers import BaseHandler
from protohandler import PacketParser
import proto.Reported_pb2 as proto_inner
import proto.Packet_pb2 as packet
import proto.Reported_pb2 as proto_repotred


def generate_test_data():
    """生成测试数据
    """
    # 填充外层包
    req_pack = packet.ReqPacket()
    req_pack.mask = 0
    req_pack.reqNo = 0
    req_pack.clientId = 1
    req_pack.udi = 'bd=l9k3vg16t3&md=unktraqexx&mf=52sem0qx0i&ei=hvr5frepbt&la=zh-cn&ai=9g8xkioj9n&wm=f8dt0mjbwb&de=msm8226&sw=480&si=4ln5oxhhxb&sh=800&ui=q4z453p3cj&pv=a2ixx8t0ox&nt=1&pl=14'

    ## 填充内部包
    #request = proto_packet.ReqTest()
    #request.name = 'cong2'
    #request.age = 20
    #req_pack.action.append('ReqTest')
    #req_pack.params.append(request.SerializeToString())

    ## 填充内部包
    #request = proto_packet.ReqTest()
    #request.name = 'cong3'
    #request.age = 22
    #req_pack.action.append('ReqTest')
    #req_pack.params.append(request.SerializeToString())


    rpt =  proto_repotred.ReqReported()
    for i in xrange(5):
        info =  rpt.reportedInfo.add()
        info.statActId = i + 1
        info.statActId2 = 0
        info.actionTime = '201308311525'
        info.ext1 = 'ext1'
        i = i + 1
    req_pack.action.append('ReqReported')
    req_pack.params.append(rpt.SerializeToString())

    return req_pack.SerializeToString()


class ProtoTestHandler(BaseHandler):
    """获取测试结果
    """
    def print_obj(self, rsp):
        result = ''
        for i in range(len(rsp.action)):
            each_action = rsp.action[i]
            result += '---%d [%s] ---\n' % (i, each_action)
            inner_pack = getattr(proto_inner, each_action)
            pack = inner_pack()
            pack.ParseFromString(rsp.params[i])
            result += str(pack)
            result += '\n'

        return result.replace('\n', '<br />')

    def write(self, msg):
        rsp = packet.RspPacket()
        rsp.ParseFromString(msg)
        result = self.print_obj(rsp)
        BaseHandler.write(self, result)
        
    @tornado.gen.coroutine
    def get(self):
        try:
            post_data = generate_test_data()
            if post_data:
                parser = PacketParser(post_data, self)
                yield parser.run()
            else:
                self.write('非法请求')
                self.finish()
        except Exception, e:
            self.logger.write('post', e)
            self.write('非法请求,解包失败')
            self.finish()