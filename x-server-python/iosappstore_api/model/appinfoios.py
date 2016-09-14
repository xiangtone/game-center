#!/bin/env python
#-*- encoding=utf8 -*-


########################
## author:nicajonh  ##
## brief:ios应用model ##
## date:2016-08-26  ##
########################

import datetime
import libs.cache as cache
from base_utils.db import MysqlConnection
import config as config
import libs.util as util
import logic.common as common
import proto.Apps_pb2 as proto
import json
from model.const import APISrcCacheName
from model.const import ApiSeriCacheName

_redis = cache.redis
APPIOS_INFO_FIELD = APISrcCacheName.AppInfoIosField



def gen_appinfoios(appid):
    """ 生成单个AppInfo
    参数：应用id
    返回：(版本，序列化后的结果)
    """
    if appid and appid > 0:
        apps_data = gen_appinfoioss([appid])

        if apps_data:
            return apps_data[0]

    return '', ''


def gen_appinfoioss(appids=[], is_return_seri=True):
    """
      读取数据库，生成多个AppInfoIos
    参数：应用ID列表
    返回：AppInfoIos应用信息的列表
    :param appids:
    :param is_return_seri:
    :return:
    """
    if appids:
        select_sql ="""
            SELECT a.AppID,a.AppName,a.ShowName,a.DevName,AppType,AppSize,AppPrice,AppVersion,
            a.RecommFlagWord,a.ThumbPicUrl,a.IconPicUrl,a.AppPicUrl,a.AppUrl,a.AppDesc,a.RecommWord,
            a.CreateTime,a.UpdateTime,a.AdsPicUrl,a.Remarks
            FROM appinfo_ios a WHERE a.AppID  IN (%s) AND a.Status=1 ORDER BY a.AppID ASC;
        """
    ids = ''
    for item in appids:
        ids += str(item) + ','
    select_sql = select_sql % ids[0:-1]

    trans=_redis.pipeline()
    with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
        query_data=mysql.query(select_sql)
    #写缓存
    if query_data:
        appinfoios_list = []
        trans = _redis.pipeline()
        for item in query_data:
            appinfoios_list.append(write_appinfoios(item, is_return_seri, trans))

        trans_ret = trans.execute()
        return appinfoios_list
    return []




def get_appinfoios_list_proto(appinfoios_data_list):
    """ 生成 协议AppInfoList 结果
    参数: appinfo源信息列表
    """
    if appinfoios_data_list:
        applist_proto = proto.IosApplist()

        for item in appinfoios_data_list:
            appinfoios_proto = applist_proto.appinfoios.add()
            set_appinfoios_proto_field(appinfoios_proto, item)

        return applist_proto.SerializeToString()
    return ""


def gen_appinfo4proto(ver, appinfoios={}, trans=None):
    """ 生成应用协议化后的结果缓存
    """
    if appinfoios:
        appinfoios_proto = proto.AppInfoIos()
        set_appinfoios_proto_field(appinfoios_proto, appinfoios)

        seri_ret = appinfoios_proto.SerializeToString()
        appinfoios_seri_cname = ApiSeriCacheName.SERI_IOSAPP_INFO % appinfoios[APPIOS_INFO_FIELD.appid]
        result = common.union_cache(ver, seri_ret)

        if trans:
            trans.set(appinfoios_seri_cname, result)

        else:
            _redis.set(appinfoios_seri_cname, result)

        return ver, seri_ret
    return '', ''


def set_appinfoios_proto_field(appinfoios_proto, appinfoios={}):
    """
    设置应用协议字段
    """
    appinfoios_proto.appId = int(appinfoios[APPIOS_INFO_FIELD.appid])
    appinfoios_proto.appname = unicode(appinfoios[APPIOS_INFO_FIELD.appname])
    appinfoios_proto.showname = unicode(appinfoios[APPIOS_INFO_FIELD.showname])
    appinfoios_proto.devname = unicode(appinfoios[APPIOS_INFO_FIELD.devname])
    appinfoios_proto.apptype = unicode(appinfoios[APPIOS_INFO_FIELD.apptype])
    appinfoios_proto.appsize = unicode(appinfoios[APPIOS_INFO_FIELD.appsize])
    appinfoios_proto.appprice = unicode(appinfoios[APPIOS_INFO_FIELD.appprice])
    appinfoios_proto.appversion = unicode(appinfoios[APPIOS_INFO_FIELD.appversion])
    appinfoios_proto.iconpicurl = unicode(appinfoios[APPIOS_INFO_FIELD.iconpicurl])
    appinfoios_proto.recommflagword=unicode(appinfoios[APPIOS_INFO_FIELD.recommflagword])
    appinfoios_proto.thumbpicurl=unicode(appinfoios[APPIOS_INFO_FIELD.thumbpicurl])

    #map(lambda x:appinfoios_proto.apppicurl.append(x),str(appinfoios[APPIOS_INFO_FIELD.app_pic_url]).split(','))
    apppiclist = unicode(appinfoios[APPIOS_INFO_FIELD.app_pic_url])
    if apppiclist:
        pic_arrary= apppiclist.split(',')
        for pic_url in pic_arrary:
            if pic_url:
                piclist=appinfoios_proto.appPiclist.add()
                piclist.appPicUrl=pic_url
    appinfoios_proto.appurl = unicode(appinfoios[APPIOS_INFO_FIELD.appurl])
    appinfoios_proto.appdesc = unicode(appinfoios[APPIOS_INFO_FIELD.appdesc])
    appinfoios_proto.recommword = unicode(appinfoios[APPIOS_INFO_FIELD.recommword])
    appinfoios_proto.updatetime = unicode(appinfoios[APPIOS_INFO_FIELD.updatetime])
    appinfoios_proto.adspicurl = unicode(appinfoios[APPIOS_INFO_FIELD.adspicurl])
    appinfoios_proto.remarks = unicode(appinfoios[APPIOS_INFO_FIELD.remarks])


def write_appinfoios(appinfoios={}, is_return_seri=False, trans=None):
    """
      写应用信息的缓存
    参数:
    is_return_seri:是否返回序列化后的结果。是，结果就返回(ver, seri_result)。否，结果返回
    dict(appinfoios)
    """
    if appinfoios:
        mapping={}
        appinfoios_cname=APISrcCacheName.APPIOS_INFO % appinfoios.get("AppID",0)
        mapping[APPIOS_INFO_FIELD.appid] = appinfoios.get("AppID",0)
        mapping[APPIOS_INFO_FIELD.appname] = appinfoios.get("AppName","")
        mapping[APPIOS_INFO_FIELD.showname] = appinfoios.get("ShowName","")
        mapping[APPIOS_INFO_FIELD.devname] = appinfoios.get("DevName","")
        mapping[APPIOS_INFO_FIELD.apptype] = appinfoios.get("AppType","")
        mapping[APPIOS_INFO_FIELD.appsize] = appinfoios.get("AppSize","")
        mapping[APPIOS_INFO_FIELD.appprice]= appinfoios.get("AppPrice","")
        mapping[APPIOS_INFO_FIELD.appversion]=appinfoios.get("AppVersion","")
        mapping[APPIOS_INFO_FIELD.recommflagword]=appinfoios.get("RecommFlagWord","")
        mapping[APPIOS_INFO_FIELD.thumbpicurl]=appinfoios.get("ThumbPicUrl","")
        mapping[APPIOS_INFO_FIELD.iconpicurl] = appinfoios.get("IconPicUrl","")
        mapping[APPIOS_INFO_FIELD.app_pic_url] = appinfoios.get("AppPicUrl","")
        mapping[APPIOS_INFO_FIELD.appurl] = appinfoios.get("AppUrl","")
        mapping[APPIOS_INFO_FIELD.recommword] = appinfoios.get("RecommWord","")
        mapping[APPIOS_INFO_FIELD.appdesc] = appinfoios.get("AppDesc", "")
        mapping[APPIOS_INFO_FIELD.updatetime] = appinfoios.get("UpdateTime", "")
        mapping[APPIOS_INFO_FIELD.adspicurl] = appinfoios.get("AdsPicUrl","")
        mapping[APPIOS_INFO_FIELD.remarks] = appinfoios.get("Remarks","")

        if trans:
            trans.hmset(appinfoios_cname, mapping)

        else:
            _redis.hmset(appinfoios_cname, mapping)

        seri_ver=util.time2ver(appinfoios.get("UpdateTime",datetime.datetime.now()))
        seri_result = gen_appinfo4proto(seri_ver, mapping, trans)
        if is_return_seri:
            return seri_result

        else:
            return mapping









