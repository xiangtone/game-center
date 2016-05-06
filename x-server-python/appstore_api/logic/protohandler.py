#!/bin/env python
#-*- encoding=utf8 -*-
#
# Copyright 2014 zonehuang
#
# 作者：zone
#
# 功能：该模块提供Google Buffer 协议解析处理功能
#
# 说明：该文件一般不需要修改
#
# 版本：V1.0.2
import datetime

import tornado.web
import tornado.gen
from base_utils import util as baseutil
import base_utils.logger as logger
from base_utils import statistic as stats
# import libs.statistics as stats

# import proto.Apps_pb2 as proto_packet
import proto.Packet_pb2 as packet
import const
import config
from handlers import BaseHandler


IS_AUTO_LOG = config.GLOBAL_SETTINGS.get('stat', {}).get('autolog', True)

class ProtoBase(object):
    """协议处理基类
    """
    def __init__(self, proto):
        """初始化协议处理基类

        参数：

        - proto：Google Buffer 协议定义（传递该参数因为一个站点可能需要使用多个Proto文件）

        """
        # logger.initial_log(config.GLOBAL_SETTINGS['logger'])
        # self.logger = logger.GlobalLogger()
        # stats.CommonStat.setLogger(self.logger)
        self.proto = proto
        self.logger = None
        self.stat = None

    def initialize(self, action, comm_params, request_body):
        """自动初始化

        参数：

        - action：协议名称

        - comm_params：通传参数

        - request_body：请求内容，用以生成请求协议包

        返回：None
        """
        self.action = action
        self.comm_params = comm_params
        try:
            self.request = self.get_req_pack(request_body)
            self.response = self.get_rsp_pack()
        except Exception, e:
            raise ProtoParseException('Proto Parse Error')
        self.callback = lambda:None
        # self.callback = lambda:callback(self.action, self.response.SerializeToString(), self.comm_params)

    def get_req_pack(self, params):
        """获取协议（请求）对象

        参数：

        - params：客户端提交过来的序列化为字符串的协议数据

        返回：

        转换后的协议对象实体
        """
        proto_cls = getattr(self.proto, 'Req' + self.action[3:])
        proto_model = proto_cls()
        proto_model.ParseFromString(params)
        return proto_model

    def get_rsp_pack(self):
        """获取协议（响应）对象

        返回：

        转换后的协议对象实体
        """
        proto_cls = getattr(self.proto, 'Rsp' + self.action[3:])
        proto_model = proto_cls()
        return proto_model

    def get_error_result(self, proto_except=None):
        """获取发生异常时的返回结果

        参数：

        - comm_params：通传参数

        返回：

        异常的协议对象实体（响应）
        """
        # proto_cls = getattr(self.proto, 'Rsp' + self.action[3:])
        proto_model_res = self.response
        if proto_except is None:
            proto_model_res.rescode = const.CommStatusCode.exception_error
            proto_model_res.resmsg = const.CommStatusCode.exception_error_msg
        else:
            proto_model_res.rescode = proto_except.rsp_code
            proto_model_res.resmsg = proto_except.rsp_msg
        self.comm_params.update({'rescode': proto_model_res.rescode})
        # return proto_model_res.SerializeToString()

    def add_stat(self, comm_params, extra_params=None):
        """写统计日志

        参数：

        - comm_params：通传参数

        - extra_params：附加参数（将覆盖通传参数中的值）

        返回：None
        """
        if extra_params:
            comm_params.update(extra_params)
        self.stat.record(comm_params)


class ProtoHandler(BaseHandler):
    """Google Buffer 协议 Handler（入口）
    """
    def get(self):
        self.write('非法请求')
        self.finish()

    @tornado.web.asynchronous
    @tornado.gen.coroutine
    def post(self):
        self.timestamp = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
	self.set_header("Access-Control-Allow-Origin", "*")
        try:
            post_data = self.request.body
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


class PacketParser(object):
    """Google Buffer 协议包解析器"""
    def __init__(self, pack_content, tornado_handler=None):
        """构造函数

        参数：

        - pack_content：协议请求包序列化后的字符串内容

        - tornado_handler：Tornado入口Handler（用以取通传参数）
        """
        import protoroutes

        self.req_packet = packet.ReqPacket()
        self.req_packet.ParseFromString(pack_content)
        self.proto_map = protoroutes.HANDLER_MAP
        self.res_packages = {}  # 响应包
        self.res_comm_params = {}  # 响应包的通传参数
        self.req_actions = []
        self.dealing = 0  # 正在处理的包数量
        self.tornado_handler = tornado_handler
        self.comm_params = self.get_com_param(tornado_handler)  # 通传参数
        # logger.initial_log(config.GLOBAL_SETTINGS['logger'])
        self.logger = tornado_handler.logger # logger.GlobalLogger()
        self.stat = tornado_handler.stat

    def get_com_param(self, tornado_handler=None):
        """获取通传参数"""
        udi = self.req_packet.udi
        com_param = baseutil.urldecode(udi)
        com_param['clientid'] = self.req_packet.clientId
        com_param['clientpos'] = self.req_packet.clientPos
        com_param['clientver'] = self.req_packet.clientVer
        com_param['rsakeyver'] = self.req_packet.rsaKeyVer
        com_param['chnno'] = self.req_packet.chnNo
        com_param['chnpos'] = self.req_packet.chnPos
        com_param['reqno'] = self.req_packet.reqNo
        if tornado_handler is not None:
            com_param['ip'] = baseutil.get_real_ip(tornado_handler)
            com_param['logno'] = baseutil.get_log_session(tornado_handler)
        return com_param

    @tornado.gen.coroutine
    def run(self):
        """开始协议处理（解析总包，提取出子包，分发给相应的类处理）

        参数：

        - finish_fuc：处理完所有包时的回调函数，用以结束请求

        返回：None
        """
        action_len = len(self.req_packet.action)
        self.dealing = action_len
        # self.finish_fuc = finish_fuc

        err_num = 0
        res_package = packet.RspPacket()
        for i in xrange(action_len):
            action = self.req_packet.action[i]
            params = self.req_packet.params[i]
            self.req_actions.append(action[3:])

            rsp_action = 'Rsp' + action[3:]
            rsp_param = ''
            try:
                self.comm_params['reqaction'] = action  # 用于记录统计数据
                self.comm_params['reqactno'] = i + 1
                handler = self.proto_map[action]()  # 初始化协议处理器
                handler.logger = self.logger 
                handler.stat = self.stat
                handler.initialize(action, self.comm_params, params)
                handler.raw_request = self.tornado_handler
                yield handler.deal()
                rsp_param = handler.response.SerializeToString()
                handler.comm_params.update({'resaction': rsp_action })
                # 20150502 统计日志中增加响应码字段
                handler.comm_params.update({'rescode': handler.response.rescode})
                if IS_AUTO_LOG:
                    self.stat.record(handler.comm_params)  # 写统计日志
            except ProtoParseException, e:
                # 协议解析错误
                self.logger.write('run ProtoParseException [%s]' % action, e)
                err_num = err_num + 1
            except Exception, e:
                self.logger.write('run Exception [%s]' % action, e)
                err_num = err_num + 1
                # self.callback(action, '', self.comm_params)
            res_package.action.append(rsp_action)
            res_package.params.append(rsp_param)
        
        res_package.mask = 0
        res_package.rescode = 0
        res_package.resmsg = ''
        self.tornado_handler.write(res_package.SerializeToString())
        self.tornado_handler.finish()


class ProtoParseException(Exception):
    """ 协议解析异常
    """
    def __init__(self, inter_msg, rsp_code=100, rsp_msg=u'服务器繁忙，请稍候重试。多次重试无效，请联系客服解决。'):
        """协议异常

        参数：

        - inter_msg：内部错误信息（一般不显示给用户）

        - rsp_code：出现异常时，返回给客户端的状态码

        - rsp_msg：出现异常时，返回给客户端的消息
        """
        self.inter_msg = inter_msg
        self.rsp_code = rsp_code
        self.rsp_msg = rsp_msg

    def __str__(self):
        return repr(self.inter_msg)


class ProtoException(Exception):
    def __init__(self, inter_msg, rsp_code=100, rsp_msg=u'服务器繁忙，请稍候重试。多次重试无效，请联系客服解决。'):
        """协议异常

        参数：

        - inter_msg：内部错误信息（一般不显示给用户）

        - rsp_code：出现异常时，返回给客户端的状态码

        - rsp_msg：出现异常时，返回给客户端的消息
        """
        self.inter_msg = inter_msg
        self.rsp_code = rsp_code
        self.rsp_msg = rsp_msg

    def __str__(self):
        return repr(self.inter_msg)
