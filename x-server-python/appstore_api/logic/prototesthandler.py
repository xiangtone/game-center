#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from handlers import BaseHandler
from protohandler import PacketParser
import proto.Apps_pb2 as proto_packet
import proto.Packet_pb2 as packet


def generate_test_data():
    """生成测试数据
    """
    # 填充外层包
    req_pack = packet.ReqPacket()
    req_pack.mask = 0
    req_pack.reqNo = 0
    req_pack.clientId = 6
    req_pack.clientVer = '5.09.25.70'
    req_pack.udi = 'bd=l9k3vg16t3&md=ASUS_Z00UDB&mf=52sem0qx0i&ei=hvr5frepbt&la=zh-cn&ai=9g8xkioj9n&wm=f8dt0mjbwb&de=msm8226&sw=480&si=4ln5oxhhxb&sh=800&ui=q4z453p3cj&pv=a2ixx8t0ox&nt=1&pl=14&oi=222'

     

    # 填充内部包
    # request = proto_packet.ReqTest()
    # request.name = 'cong2'
    # request.age = 20
    # req_pack.action.append('ReqTest')
    # req_pack.params.append(request.SerializeToString())

    # 填充内部包
    # request = proto_packet.ReqTest()
    # request.name = 'cong3'
    # request.age = 22
    # req_pack.action.append('ReqTest')
    # req_pack.params.append(request.SerializeToString())

    # GlobalConfig
    # request = proto_packet.ReqGlobalConfig()
    # req_pack.action.append('ReqGlobalConfig')
    # req_pack.params.append(request.SerializeToString())

    # group_elem
    # request = proto_packet.ReqGroupElems()
    # request.groupId = 45
    # request.pageSize = 30
    # request.pageIndex = 1
    # req_pack.action.append('ReqGroupElems')
    # req_pack.params.append(request.SerializeToString())

    # global_config = proto_packet.ReqGlobalConfig()
    # global_config.groupsCacheVer = ''
    # req_pack.action.append('ReqGlobalConfig')
    # req_pack.params.append(global_config.SerializeToString())

    group_elem = proto_packet.ReqGroupElems()
    group_elem.groupId = 101
    group_elem.pageSize = 30
    group_elem.pageIndex = 1
    req_pack.action.append('ReqGroupElems')
    req_pack.params.append(group_elem.SerializeToString())

    # group_elem = proto_packet.ReqGroupElems()
    # group_elem.groupId = 93
    # group_elem.pageSize = 30
    # group_elem.pageIndex = 1
    # req_pack.action.append('ReqGroupElems')
    # req_pack.params.append(group_elem.SerializeToString())

    # group_elem = proto_packet.ReqGroupElems()
    # group_elem.groupId = 100
    # group_elem.pageSize = 30
    # group_elem.pageIndex = 1
    # req_pack.action.append('ReqGroupElems')
    # req_pack.params.append(group_elem.SerializeToString())

    # appinfo
    # request = proto_packet.ReqAppInfo()
    # request.appId = 125816
    # request.packId = 0
    # req_pack.action.append('ReqAppInfo')
    # req_pack.params.append(request.SerializeToString())

    # # search
    # request = proto_packet.ReqAppList4SearchKey()
    # request.SearchKeyStr = u'泡泡连击'
    # request.appClass = 0
    # request.appType = 0
    # request.isHotKey = 0
    # request.pageSize = 20
    # request.pageIndex = 1
    # request.orderType = 0
    # req_pack.action.append('ReqAppList4SearchKey')
    # req_pack.params.append(request.SerializeToString())


    # # 测试协议接口 2015-09-01
    # # 用户举报
    # request = proto_packet.ReqAppInform()
    # request.appId = 1233
    # request.informType = '1,2,3'
    # request.informDetail = '' 
    # req_pack.action.append('ReqAppInform')
    # req_pack.params.append(request.SerializeToString())

    # # 用户反馈
    # request = proto_packet.ReqFeedback()
    # request.feedBackContent = u'用户反馈内容'
    # request.userContact = u'74110'  
    # req_pack.action.append('ReqFeedback')
    # req_pack.params.append(request.SerializeToString())

    # 用户评论或更新
    #request = proto_packet.ReqAddComment()
    #request.userName = u'wind'  # 用户名
    #request.appId = 74116   # 应用ID
    #request.userScore = 2  # 用户评分
    #request.userVerCode = 150901   # 用户本地版本代码
    #request.userVerName = u'2015.09.01.1'  # 用户本地版本名称
    #request.comments = u''  # 评论内容
    #request.commentId = 123  # 评论ID，用于更新评论时填写 
    #req_pack.action.append('ReqAddComment')
    #req_pack.params.append(request.SerializeToString())

    # # 用户评分
    # request = proto_packet.ReqUserScoreInfo() 
    # request.appId = 74115   # 应用ID 
    # req_pack.action.append('ReqUserScoreInfo')
    # req_pack.params.append(request.SerializeToString())


    # # 应用推荐
    # request = proto_packet.ReqRecommApp() 
    # request.appId = 100009   # 应用ID  
    # request.appClass = ''  # 应用类别
    # request.appType =  '1201'   # 应用类型
    # request.pageSize = 20  # 页尺寸
    # request.pageIndex = 1  # 页码 (从1开始)
    # request.orderType = 0  # 排序类型：0=自动-热门，2=按时间
    # request.clientCacheVer = ''  # 终端缓存版本：如果有缓存则上传，下发时可省流量，加速度 
    # req_pack.action.append('ReqRecommApp')
    # req_pack.params.append(request.SerializeToString())

    # 应用更新检测
    # apps_update = proto_packet.ReqAppsUpdate()
    # apps_update.checkUpdateType = 2
    # local = apps_update.localAppVer.add()
    # local.packName = 'com.teatimesgames.snowball'
    # local.verName = '0.1'
    # local.verCode = 1
    # local.signCode = ''
    # req_pack.action.append('ReqAppsUpdate')
    # req_pack.params.append(apps_update.SerializeToString())
 
  
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

    def print_obj(self, rsp):
        result = {}
        for i in range(len(rsp.action)):
            each_action = rsp.action[i]
            inner_pack = getattr(proto_packet, each_action)
            pack = inner_pack()
            pack.ParseFromString(rsp.params[i])

            import json
            import libs.pbjson as pbjson
            dict_result = pbjson.pb2dict(pack)

            byte_proto_mapping={
                "appInfo":"AppInfo",
                "groupElems":"GroupElems",
                "appInfoList":"AppInfoList",
                "userComments":"UserComments"
            }
            for k,v in byte_proto_mapping.items():
                if k in dict_result:
                    appinfo_proto = getattr(proto_packet, v)
                    info = appinfo_proto()
                    info.ParseFromString(dict_result[k])
                    dict_result[k] = pbjson.pb2dict(info)
            #byte_proto_name = byte_proto_mapping.get()

            # if "appInfo" in dict_result:
            #     appinfo_proto = getattr(proto_packet, "AppInfo")
            #     info = appinfo_proto()
            #     info.ParseFromString(dict_result["appInfo"])
            #     dict_result["appInfo"] = pbjson.pb2dict(info)

            result[each_action + "_" + str(i)] = dict_result

        return result

    def write(self, msg):
        rsp = packet.RspPacket()
        rsp.ParseFromString(msg)
        result = self.print_obj(rsp)
        BaseHandler.write(self, str(result))

    #def write(self, msg):
    #    rsp = packet.RspPacket()
    #    rsp.ParseFromString(msg)
    #    result = self.print_obj(rsp)
    #    BaseHandler.write(self, result)

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


    def post(self):
        post_data = self.request.body
        #fp = open("file/rspappinfo",'rb')
        #post_data = fp.read()
        #fp.close()
        #print len(post_data)
        self.test_parse(post_data)

    def test_parse(self, content):
        self.req_packet = packet.RspPacket()
        self.req_packet.ParseFromString(content)
        action = self.req_packet.action
        params = self.req_packet.params
        for i in xrange(len(action)):
            if action[i] == "RspAppInfo":
                rsp_appinfo = proto_packet.RspAppInfo()
                rsp_appinfo.ParseFromString(params[i])
                if rsp_appinfo.appInfo:
                    appinfo = proto_packet.AppInfo()
                    appinfo.ParseFromString(rsp_appinfo.appInfo)
                    print appinfo.appId
                    print appinfo.appTagFlag

