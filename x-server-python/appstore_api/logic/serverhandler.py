#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.web
import config as config
import libs.util as util
import libs.cache as cache
from logic.common import Common

import base_utils.des as des
import base_utils.logger as nw_logger
from base_utils.db import MysqlConnection


_redis = cache.redis
#logger = nw_logger.CommonLog

from logic.handlers import BaseHandler

class SyncHandler(BaseHandler):

    def get(self):
        self.end('server:非法请求..')

    def post(self):
        try:
            post_data = self.request.body

            # debug
            #import model.test as test
            #post_data = test.gen_auto_update_post()

            result = self.update(post_data)
        except Exception, ex:
            result = {'rescode': '5', 'resmsg':'其他'}
            #logger.write_log('post', ex)
            self.logger.write('post', ex)
        r_json = util.MyJSONEncoder.encode(result)
        self.end(r_json)

    def end(self, pack_content):
        self.write(pack_content)
        self.finish()

    def update(self, post_data):
        """后台主动请求同步缓存信息
        """
        result = {}
        try:
            de_data = des.decrypt(post_data)  # 解密
        except:
            result = {'rescode':1, 'resmsg':'解密错误'}
        if not result:
            params = util.urldecode(de_data)
            if 'ts' in params.keys() and 'sign' in params.keys():
                if util.md5_str(params['ts']).lower() == params['sign'].lower():
                    Common.sync()
                    result = {'rescode':0, 'resmsg':'成功'}
                else:
                    result = {'rescode':3, 'resmsg':'校验错误'}
            else:
                result = {'rescode':2, 'resmsg':'参数错误'}
        return result


class MonitorHandler(BaseHandler):
    """
    站点监控
    """
    def get(self):

        m_ret = 'unknown_error'

        try:
            if _redis.echo('HelloWorld') == 'HelloWorld':
                m_ret = 'cache_ok,'

            with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
                query_data = mysql.query("select 1")
                m_ret += 'db_ok'

        except Exception,ex:
            m_ret = ex.message
            #logger.write_log('monitor',ex.message)
            self.logger.write("monitor", ex.message)

        self.write(str(m_ret))
        self.finish()