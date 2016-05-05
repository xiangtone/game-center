#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from handlers import BaseHandler
from protohandler import PacketParser
import proto.Updater_pb2 as proto_packet
import proto.Packet_pb2 as packet


def generate_test_data():
    """生成测试数据
    """
    # 填充外层包
    req_pack = packet.ReqPacket()
    req_pack.mask = 0
    req_pack.reqNo = 0
    req_pack.clientId = 1
    req_pack.udi = 'bd=l9k3vg16t3&md=unktraqexx&mf=52sem0qx0i&ei=hvr5frepbt&la=zh-cn&ai=9g8xkioj9n&wm=30:5a:3a:15:44:71&de=msm8226&sw=480&si=4ln5oxhhxb&sh=800&ui=q4z453p3cj&pv=a2ixx8t0ox&nt=1&pl=14'

    # 填充内部包
    # request = proto_packet.ReqUpdate()
    # request.packName = 'com.niuwan.launcher'
    # request.verName = '5.00.10.90'
    # request.verCode = 0
    # req_pack.action.append('ReqUpdate')
    # req_pack.params.append(request.SerializeToString())

    # 填充内部包
    request = proto_packet.ReqUpdate()
    request.packName = 'com.cs.appstore'
    request.verName = '6.01.14.20'
    request.verCode = 172
    req_pack.action.append('ReqUpdate')
    req_pack.params.append(request.SerializeToString())


    return req_pack.SerializeToString()


class ProtoTestHandler(BaseHandler):
    """获取测试结果
    """
    def print_obj(self, rsp):
        result = ''
        for i in range(len(rsp.action)):
            each_action = rsp.action[i]
            result += '---%d [%s] ---\n' % (i, each_action)
            inner_pack = getattr(proto_packet, each_action)
            pack = inner_pack()
            pack.ParseFromString(rsp.params[i])
            result += str(pack)
            result += '\n'

        return result.replace('\n', '<br />')

    def write(self, msg):
        #print msg
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