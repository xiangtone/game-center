#!/bin/env python
#-*- encoding=utf8 -*-
import base_utils.cache as cache
import config

def get_update_info(scheme_id, pack_name, channel_name):
    """获取更新信息
    """
    cache_obj = cache.Cache(config.GLOBAL_SETTINGS['redis'])
    cache_name = 'hPackList:%s_%s_%s' % (scheme_id, pack_name, channel_name)
    update_info = cache_obj.hgetall(cache_name)
    if update_info is None or update_info == {}:
        cache_name = 'hPackList:%s_%s_%s' % (0, pack_name, channel_name)
        update_info = cache_obj.hgetall(cache_name)
    return update_info
