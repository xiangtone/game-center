#!/bin/env python
#-*- encoding=utf8 -*-
#
# Copyright 2014 zonehuang
#
# 作者：zone
#
# 功能：该模块提供 JSON 协议解析处理功能
#
# 说明：该文件一般不需要修改
#
# 版本：V1.0.1
import json

import tornado.web
import tornado.gen

import libs.util as util
from logic.handlers import BaseHandler


class BusinessException(Exception):
    def __init__(self, msg, rescode=1):
        self.msg = msg
        self.rescode = rescode


class JSONHandler(BaseHandler):
    """JSON 协议 Handler（入口）
    """
    def get(self):
        self.write('非法请求')
        self.finish()

    def parse_commm_params(self, request_data={}):
        result={}
        if 'header' in request_data:
            header = request_data['header']
            if 'udi' in header:
                udi = header.pop('udi')
                header.update(util.urldecode(udi))
            result = header
        return result

    @tornado.web.asynchronous
    @tornado.gen.coroutine
    def post(self):
        try:
            post_data = self.request.body
            self.set_header("Access-Control-Allow-Origin", "*")
            if post_data:
                request = json.loads(post_data)
                # 单个API请求
                if request and 'api' in request and 'params' in request:
                    method = request['api']
                    import jsonroutes
                    handler_map = jsonroutes.HANDLER_MAP
                    if method in handler_map:
                        handler = handler_map[method]()
                        handler.request = request['params']
                        handler.method = method
                        handler.tornado_handler = self
                        handler.logger = self.logger
                        handler.comm_args = self.parse_commm_params(request)

                        yield handler.deal()
                        self.write(json.dumps(handler.response))
                        self.finish()
                    else:
                        raise BusinessException("Api Not Exist!")

                # 多个API请求
                elif request and 'apilist' in request:
                    apilist = request['apilist']
                    import jsonroutes
                    reslist_result = dict(rescode=0,resmsg='',reslist=[])
                    for each_req in apilist:
                        method = each_req['api']
                        handler_map = jsonroutes.HANDLER_MAP
                        try:
                            if method in handler_map:
                                handler = handler_map[method]()
                                handler.request = each_req['params']
                                handler.method = method
                                handler.tornado_handler = self
                                handler.logger = self.logger
                                handler.comm_args = self.parse_commm_params(request)

                                yield handler.deal()
                                reslist_result['reslist'].append(handler.response)
                            else:
                                raise BusinessException("Api Not Exist!")
                                
                        except BusinessException, e:
                            response = {'rescode': 1, 'resmsg': 'success'}
                            response['resmsg'] = e.msg
                            response['rescode'] = e.rescode
                            reslist_result['reslist'].append(response)

                    self.write(json.dumps(reslist_result))
                    self.finish()

                else:
                    self.write('Wrong Request')
                    self.finish()
            else:
                self.write('Wrong Request')
                self.finish()
        except BusinessException, e:
            response = {'rescode': 1, 'resmsg': 'success'}
            response['resmsg'] = e.msg
            response['rescode'] = e.rescode
            self.write(json.dumps(response))
            self.finish()
        except Exception, e:
            print e
            # self.logger.write('post', e)
            response = {'rescode': 1, 'resmsg': 'exception'}
            self.write(json.dumps(response))
            self.finish()


class APIBase(object):
    def __init__(self):
        self.request = {}
        self.method = ''
        self.response = {'rescode': -1, 'resmsg': 'fail'}

    def check_request(self, param_list):
        """检查是否包含必传参数
        """
        #mark:测试传入参数
        print dict(param_list).items()

        for key,name in param_list:
            if key not in self.request:
                raise BusinessException('%s不能为空或注意大小写' % name)

    def deal(self):
        # 未实现
        raise NotImplementedError("")


#     #测试
# if __name__=="__main__":
#     jsonHandler=JSONHandler()
#     jsonHandler.post()



