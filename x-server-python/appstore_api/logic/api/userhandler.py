#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from logic.jsonhandler import APIBase, BusinessException


class SendCode(APIBase):
    """发送验证码
    """
    @tornado.gen.coroutine
    def deal(self):
        self.check_request([("phoneNum","手机号")])

        phone_num = self.request['phoneNum']
        print phone_num
        self.response['rescode'] = 0
        self.response['resmsg'] = ''