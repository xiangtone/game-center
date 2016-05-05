#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
import libs.cache as cache
from model.const import BGSrcCacheName
from model.const import APISrcCacheName


GROUP_INFO = BGSrcCacheName.GroupInfoField


_redis = cache.redis


def match_group_id(schemeid, group_class, group_type, order_type):
    """ 匹配分组id
    """
    if schemeid:
        groups = m_appsconfig.get_groups(schemeid)
        for item in groups:
            if int(item[GROUP_INFO.group_class]) == group_class and \
                int(item[GROUP_INFO.group_type]) == group_type and \
                int(item[GROUP_INFO.order_type]) == order_type:
                groupid =  int(item[GROUP_INFO.group_id])
                cache_name = APISrcCacheName.MATCH_GROUPID % (schemeid, group_class, group_type, order_type)
                _redis.set(cache_name, groupid)
                return groupid
    return 0