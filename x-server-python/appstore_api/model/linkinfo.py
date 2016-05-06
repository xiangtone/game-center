#!/bin/env python
#-*- encoding=utf8 -*-

import libs.cache as cache
from model.const import BGSrcCacheName

_redis = cache.redis


def get_link_info(linkid):
    """ 获取单个LinkInfo信息
    """
    if linkid and linkid > 0:
        link_infos = get_link_infos([linkid])
        return link_infos[0] if link_infos else {}
    return {}


def get_link_infos(linkids=[]):
    """ 获取多个LinkInfo信息
    """
    if linkids:
        trans = _redis.pipeline()

        for item in linkids:
            link_cname = BGSrcCacheName.LINK_INFO % item
            trans.hgetall(link_cname)

        trans_ret = trans.execute()
        return [i for i in trans_ret if i]
    return []
