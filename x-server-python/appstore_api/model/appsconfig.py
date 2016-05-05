#!/bin/env python
#-*- encoding=utf8 -*-

import datetime
import libs.cache as cache
import libs.util as util
import proto.Apps_pb2 as proto
import logic.common as common
from model.const import ApiSeriCacheName
from model.const import BGSrcCacheName
from model.const import BsConst


GROUP_INFO = BGSrcCacheName.GroupInfoField
_redis = cache.redis


def gen_appsconfig(schemeid):
    """ 生成配置信息
    返回：版本，分组信息结果
    """
    seri_name = ApiSeriCacheName.SERI_APPS_CONFIG % schemeid
    src_group_scheme = BGSrcCacheName.GROUP_SCHEME % schemeid
    ver = util.time2ver(datetime.datetime.now())
    groupids = _redis.smembers(src_group_scheme)

    if groupids:
        trans = _redis.pipeline()

        for item in groupids:
            group_cache = BGSrcCacheName.GROUP_INFO % item
            trans.hgetall(group_cache)

        trans_ret = trans.execute()

        if trans_ret:
            groups_proto = proto.Groups()

            for each_ret in trans_ret:
                if each_ret:
                    group_info = groups_proto.groupInfo.add()
                    group_info.groupId = int(each_ret[GROUP_INFO.group_id])
                    group_info.groupClass = int(
                        each_ret[GROUP_INFO.group_class])
                    group_info.groupType = int(each_ret[GROUP_INFO.group_type])
                    group_info.orderType = int(each_ret[GROUP_INFO.order_type])
                    group_info.orderNo = int(each_ret[GROUP_INFO.order_no])
                    group_info.recommWord = unicode(
                        each_ret[GROUP_INFO.group_tips])
                    group_info.groupName = unicode(
                        each_ret[GROUP_INFO.group_name])
                    group_info.groupDesc = unicode(
                        each_ret[GROUP_INFO.group_desc])
                    group_info.groupPicUrl = each_ret[GROUP_INFO.group_pic_url] if each_ret[GROUP_INFO.group_pic_url] else each_ret[GROUP_INFO.type_pic_url]
                    group_info.startTime = each_ret[GROUP_INFO.start_time]
                    group_info.endTime = each_ret[GROUP_INFO.end_time]
            result = groups_proto.SerializeToString()
            _redis.set(seri_name, common.union_cache(ver, result))
    return ver, result

def get_groups(schemeid):
    """ 获取所有的分组
    """
    src_group_scheme = BGSrcCacheName.GROUP_SCHEME % schemeid
    groupids = _redis.smembers(src_group_scheme)

    if groupids:
        trans = _redis.pipeline()

        for item in groupids:
            group_cache = BGSrcCacheName.GROUP_INFO % item
            trans.hgetall(group_cache)
        trans_ret = trans.execute()
        return [i for i in trans_ret if i]