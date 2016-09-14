#!/bin/env python
#-*- encoding=utf8 -*-


########################
## author:nicajonh  ##
## brief:ios应用model 分页查询
## date:2016-08-26  ##
########################

import datetime
import libs.cache as cache

import config as config
import logic.common as common
import json
import proto.Apps_pb2 as proto
import libs.util as util
import appinfoios as l_appinfoios
from libs.pagedal import MySQLQueryPagination
from model.const import APISrcCacheName
from model.const import ApiSeriCacheName


_redis = cache.redis
APPIOS_INFO_FIELD = APISrcCacheName.AppInfoIosField

def gen_appinfopagelist(pageSize,pageIndex,ver=''):
    """ 生成单个AppInfo
    参数：应用id
    返回：(版本，序列化后的结果)
    """
    seri_name = ApiSeriCacheName.SERI_IOSPAGE_ELEMS % (pageIndex, pageSize)
    appinfolist=gen_appios_list(pageSize,pageIndex)
    if appinfolist:
        ver = util.time2ver(datetime.datetime.now())
        expire_time = common.get_interval_seconds()

        #管理操作实现批处理
        tans =_redis.pipeline()

        result_data=l_appinfoios.get_appinfoios_list_proto(appinfolist)

        #s_applist_json=util.MyJSONEncoder.encode(appinfolist)
        _redis.set(seri_name,common.union_cache(ver,result_data))
        _redis.expire(seri_name, expire_time)  # 设置缓存保存2天时间
    return ver,result_data


def gen_appios_list(pageSize,pageIndex, is_return_seri=False):
    """
      读取数据库，生成多个AppInfoIos
    参数：pageSize 分页大小 默认大小20
    参数：pageIndex 分页索引
    返回：AppInfoIos应用信息的列表
    :param pageSize :
    :param pageSize :
    :return:
    """
    select_sql = """
          SELECT a.AppID,a.AppName,a.ShowName,a.DevName,a.AppType,a.AppSize,a.AppPrice,a.AppVersion,
        a.RecommFlagWord,a.ThumbPicUrl,a.IconPicUrl,a.AppPicUrl,a.AppUrl,a.AppDesc,a.RecommWord,
        a.CreateTime,a.UpdateTime,a.AdsPicUrl,a.Remarks
        FROM appinfo_ios a WHERE a.Status=1 ORDER BY a.AppID ASC
    """


    if pageSize >0 and pageSize <= 20 and pageIndex >= 0:
        trans = _redis.pipeline()
        query_data=MySQLQueryPagination().queryForPageList(select_sql,pageIndex)
        if query_data:
            appinfoios_list=[]
            for item in query_data:
                appinfoios_list.append(l_appinfoios.write_appinfoios(item, is_return_seri, trans))
            #写入缓存
            #trans.set(seri_name, json.dumps(query_data))
            trans_ret = trans.execute()
            return appinfoios_list
    return []


# def gen_appinfoios4protolist(appinfoios_list=[]):
#     """
#     生成应用协议列表数据
#     :param ver:
#     :param appinfoios:
#     :param trans:
#     :return:
#     """
#     applist_proto = proto.IosApplist()
#     for item in appinfoios_list:
#         if applist_proto:
#             appios_item=applist_proto.appinfoios.add()
#             l_appinfoios.set_appinfoios_proto_field(appios_item,item)
#         return applist_proto.SerializeToString()
#     return ''










