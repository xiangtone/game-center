#!/bin/env python
#-*- encoding=utf8 -*-

"""
RedisPool 的单例化封装
使用方法：
redis.set('foo','bar')
"""

#import config as config
#from base_utils import cache

#_redis_config = config.GLOBAL_SETTINGS["redis"]

#cache.RedisPool.connect({'host': _redis_config["host"], 'port': _redis_config[
#                        "port"], 'db': _redis_config["db"]})

#redis = cache.RedisPool.strict_redis()


import config as config
from base_utils.cache import Cache

_redis_config = config.GLOBAL_SETTINGS["redis"]

redis = Cache(_redis_config)