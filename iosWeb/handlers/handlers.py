#!/bin/env python
#-*- encoding=utf8 -*-
import os

import tornado.web
import tornado.template

import nwbase_utils.logger as logger
import nwbase_utils.statistic as stats
from nwbase_utils import util as baseutil

import config
# import niuwanauth


class BaseHandler(tornado.web.RequestHandler):
    """Tornado请求 Handler 基类，实现了普通HTTP ERROR的异常处理、模板生成功能；

    可用成员：

    .logger：日志记录器
    .stat: 统计记录器
    """
    def initialize(self):
        self.logger = logger.GlobalLogger()
        stat_config = config.GLOBAL_SETTINGS.get('stat')
        svcid = stat_config.get('svcid') if stat_config else '0'
        self.stat = stats.CommonStat(config.GLOBAL_SETTINGS['logger'], 'V1.1', svcid)
        self._dynamic_value = {}
        self._template_loader = tornado.template.Loader(os.path.join(config.GLOBAL_SETTINGS['root_path'], "templates"))

    def generate(self, templateFile):
        """使用模板页生成页面（使用self._dynamic_value定义变量）

        参数：

        - templateFile：模板文件（仅文件名；自动从模板文件夹加载模板文件）
        """
        template = self._template_loader.load(templateFile)
        template.autoescape = False
        self.write(template.generate(**self._dynamic_value))

    def write_error(self, status_code, **kwargs):
        """默认异常处理，一般不需要修改
        """
        if 'exc_info' in kwargs:
            if len(kwargs['exc_info']) >= 3:
                import traceback
                self.logger.write('HTTPError %s' % status_code, traceback.extract_tb(kwargs["exc_info"][2]))
            else:
                self.logger.write('HTTPError %s' % status_code, kwargs['exc_info'])
            # self.write('error')
        if status_code == 404:
            self._dynamic_value['error_code'] = 404
            self.generate("error.html")
        elif status_code >= 500:
            self._dynamic_value['error_code'] = 500
            self.generate("error.html")

    def get_header_comm_args(self):
        """ 解析header的通传参数
        """
        pack_content = self.request.headers.get("nw-header", None)
        if pack_content:
            import base64
            import proto.Packet_pb2 as packet
            de_pack_content = base64.b64decode(pack_content)
            req_packet = packet.ReqPacket()
            req_packet.ParseFromString(de_pack_content)
            udi = req_packet.udi
            com_param = baseutil.urldecode(udi)
            com_param['clientid'] = req_packet.clientId
            com_param['clientpos'] = req_packet.clientPos
            com_param['clientver'] = req_packet.clientVer
            com_param['rsakeyver'] = req_packet.rsaKeyVer
            com_param['chnno'] = req_packet.chnNo
            com_param['chnpos'] = req_packet.chnPos
            com_param['reqno'] = req_packet.reqNo
            com_param['ip'] = baseutil.get_real_ip(self)
            com_param['logno'] = baseutil.get_log_session(self)
            return com_param


class NotFoundHandler(BaseHandler):
    """HTTP 404 Handler
    """
    def get(self):
        raise tornado.web.HTTPError(404)


class TestHandler(BaseHandler):
    # @niuwanauth.auth
    def get(self):
        args = self.get_header_comm_args()
        param_list = dict((k, self.request.arguments[k][0]) for k in self.request.arguments)
        if args:
            self.write(args)
            
        self.generate("index.html")
        
        # self.stat.record({"ei":'1234', 'oi':'64234'})
        # self.logger.info('hello examp1e')
