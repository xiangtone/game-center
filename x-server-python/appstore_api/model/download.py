#!/bin/env python
#-*- encoding=utf8 -*-

import datetime
import config as config
import libs.cache as cache
import libs.util as util
import proto.Apps_pb2 as proto
from base_utils.db import MysqlConnection

from model.const import APISrcCacheName

_redis = cache.redis


def _get_all_app_downtimes():
    """获取应用上次同步到现在的下载次数
    """
    down_keys = _redis.keys(APISrcCacheName.APP_DOWN_COUNT % '*')

    if down_keys:
        trans = _redis.pipeline()

        for item in down_keys:
            trans.hgetall(item)
        app_down = trans.execute()

        if app_down:
            result = {}

            for i in xrange(len(down_keys)):
                result[down_keys[i]] = app_down[i]
            return result

def sync_downtimes():
    """同步下载次数到缓存的源数据以及数据库
    """
    downtimes = _get_all_app_downtimes()
    if downtimes:

        # 同步到缓存
        appinfo_params = [] 
        packinfo_params = []
 
        for key,value in downtimes.items():
            index = key.rfind(":")
            appid = key[index + 1:]
            for k1,v1 in value.items():
                appinfo_params.append((int(v1),int(v1), int(appid)))
                packinfo_params.append((int(v1),int(v1),int(k1)))
    
        # 同步到数据库
        u_appinfo_sql = "update AppInfo set DownTimes = DownTimes + %s,DownTimesReal = DownTimesReal + %s  where AppID = %s"
        u_packinfo_sql = "update PackInfo set DownTimes = DownTimes + %s,DownTimesReal = DownTimesReal + %s  where PackID = %s"
        with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
            mysql.executemany(u_appinfo_sql, appinfo_params)
            mysql.executemany(u_packinfo_sql,packinfo_params)
            mysql.commit()

        # 删除key
        _redis.delete(*downtimes.keys())