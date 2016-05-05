#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.web
import config
import base_utils.cache as cache


class MonitorHandler(tornado.web.RequestHandler):
    def get(self):
        try:
            cache_obj = cache.Cache(config.GLOBAL_SETTINGS['redis'])
            if cache_obj.ping():
                if len(cache_obj.keys('hPackList:*')) > 0:
                    self.write('0')
        except Exception, ex:
            self.write('ex=%s' % ex)
