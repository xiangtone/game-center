#!/bin/env python
#-*- encoding=utf8 -*-

import datetime
import config as config
import libs.cache as cache
import libs.util as util
import proto.Apps_pb2 as proto
import logic.common as common
from base_utils.db import MysqlConnection

from model.const import APISrcCacheName
from model.const import ApiSeriCacheName

_redis = cache.redis
APP_INFO_FIELD = APISrcCacheName.AppInfoField


def gen_appinfo(appid):
    """ 生成单个AppInfo
    参数：应用id
    返回：(版本，序列化后的结果)
    """
    if appid and appid > 0:
        apps_data = gen_appinfos([appid])

        if apps_data:
            return apps_data[0]

    return '', ''


def gen_appinfos(appids=[], is_return_seri=True):
    """ 读取数据库，生成多个AppInfo
    参数：应用ID列表
    返回：AppInfo应用信息的列表
    """
    # 读取数据库，生成缓存，并返回
    if appids:
        select_sql = """
                    select
                    a.AppID,a.AppName,a.ShowName,a.ForDeviceType,
                    a.IsNetGame,a.AppType,a.AppTag,a.EvilLevel,
                    a.DownTimes,a.DownTimesReal,a.CommentTimes,
                    a.SearchKeys,a.AppDesc,a.Remarks,a.CreateTime,
                    a.UpdateTime,a.DataStatus,a.RecommTag,a.RecommLevel,
                    a.RecommWord,a.DevName,
                    a.IssueType,a.ChannelNos,a.ChannelAdaptation,  -- 新增的渠道号信息
                    a.Architecture,                                -- 新增设备架构适配 2015.12.23
                    p.permission,                                  -- 新增的权限列表
                    p.PackID,p.PackSize,p.VerCode,p.VerName,
                    p.PackName,p.PackSign,p.IconUrl,p.IconUrl2,
                    p.AppPicUrl,p.AppPicUrl2,p.PackUrl,p.PackUrl2,p.PackMD5,
                    p.CompDesc,p.UpdateDesc
                    -- ,c.CPName
                    ,ifnull(at.AppTypeName,'') as AppTypeName
                    from appinfo a
                    inner join packinfo p
                    on a.AppID=p.AppID
                    and a.MainPackID=p.PackID
                    and a.DataStatus=1
                    and a.Status=1
                    and p.Status=1
                    and a.AppID in (%s)
                    -- left join cps c
                    -- on c.CPID=a.CPID
                    left join apptypes at on
                    at.AppType = a.AppType
        """

        ids = ''
        for item in appids:
            ids += str(item) + ','
        select_sql = select_sql % ids[0:-1]

        with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
            query_data = mysql.query(select_sql)

        # 写缓存
        if query_data:
            appinfo_list = []
            trans = _redis.pipeline()

            for item in query_data:
                appinfo_list.append(_write_appinfo(item, is_return_seri, trans))

            trans_ret = trans.execute()
            return appinfo_list
    return []


def _write_appinfo(appinfo={}, is_return_seri=True, trans=None,):
    """ 
    写应用信息的缓存
    参数:
        is_return_seri:是否返回序列化后的结果。是，结果就返回(ver,seri_result)。否，结果返回 dict(appinfo)
    """
    if appinfo:
        mapping = {}
        appinfo_cname = APISrcCacheName.APP_INFO % appinfo.get("AppID", 0)
        mapping[APP_INFO_FIELD.appid] = appinfo.get("AppID", 0)
        mapping[APP_INFO_FIELD.packid] = appinfo.get("PackID", 0)
        mapping[APP_INFO_FIELD.show_name] = appinfo.get("ShowName", "")
        mapping[APP_INFO_FIELD.pack_name] = appinfo.get("PackName", "")
        mapping[APP_INFO_FIELD.sign_code] = appinfo.get("PackSign", "")
        mapping[APP_INFO_FIELD.dev_name] = appinfo.get("DevName", "")
        mapping[APP_INFO_FIELD.app_class] = appinfo.get("AppType", 0) / 100
        mapping[APP_INFO_FIELD.app_type] = appinfo.get("AppType", 0)
        mapping[APP_INFO_FIELD.down_times] = appinfo.get("DownTimes", 0)
        mapping[APP_INFO_FIELD.comment_times] = appinfo.get("CommentTimes", 0)
        mapping[APP_INFO_FIELD.comment_score] = 0   # remarks:暂时没有评论
        mapping[APP_INFO_FIELD.app_tag_flag] = appinfo.get("AppTag", 0)
        mapping[APP_INFO_FIELD.recomm_level] = appinfo.get("RecommLevel", 0)
        mapping[APP_INFO_FIELD.recomm_flag] = appinfo.get("RecommTag", 0)
        mapping[APP_INFO_FIELD.recomm_word] = appinfo.get("RecommWord", "")
        mapping[APP_INFO_FIELD.thumb_url] = ""  # remarks:截图没有
        mapping[APP_INFO_FIELD.icon_url] = appinfo.get("IconUrl", "").strip() or appinfo.get("IconUrl2", "")
        mapping[APP_INFO_FIELD.app_pic_url] = appinfo.get("AppPicUrl", "").strip() or appinfo.get("AppPicUrl2", "")
        mapping[APP_INFO_FIELD.pack_url] = appinfo.get("PackUrl", "")
        mapping[APP_INFO_FIELD.pack_url2] = appinfo.get("PackUrl2", "")
        mapping[APP_INFO_FIELD.pack_md5] = appinfo.get("PackMD5", "")
        mapping[APP_INFO_FIELD.pack_size] = appinfo.get("PackSize", 0)
        mapping[APP_INFO_FIELD.ver_code] = appinfo.get("VerCode", 0)
        mapping[APP_INFO_FIELD.ver_name] = appinfo.get("VerName", "")
        mapping[APP_INFO_FIELD.comp_desc] = appinfo.get("CompDesc", "")
        mapping[APP_INFO_FIELD.lan_desc] = ""  # remarks:语言没有
        mapping[APP_INFO_FIELD.app_desc] = appinfo.get("AppDesc", "")
        mapping[APP_INFO_FIELD.update_desc] = appinfo.get("UpdateDesc", "")
        mapping[APP_INFO_FIELD.publish_time] = util.time2ver(appinfo.get("UpdateTime", datetime.datetime.now()))
        mapping[APP_INFO_FIELD.search_keys] = appinfo.get("SearchKeys", "")
        mapping[APP_INFO_FIELD.pack_md5] = appinfo.get("PackMD5", "")
        mapping[APP_INFO_FIELD.app_type_name] = appinfo.get("AppTypeName", "")
        mapping[APP_INFO_FIELD.issue_type] = appinfo.get("IssueType", 1)
        mapping[APP_INFO_FIELD.channelnos] = appinfo.get("ChannelNos", "")
        mapping[APP_INFO_FIELD.channel_adaptation] = appinfo.get("ChannelAdaptation", "")
        mapping[APP_INFO_FIELD.permission] = appinfo.get("permission", "")
        mapping[APP_INFO_FIELD.architecture] = appinfo.get("Architecture",0)

        

        if trans:
            trans.hmset(appinfo_cname, mapping)

        else:
            _redis.hmset(appinfo_cname, mapping)

        seri_ver = util.time2ver(appinfo.get("UpdateTime", datetime.datetime.now()))
        seri_result = gen_appinfo4proto(seri_ver, mapping, trans)

        if is_return_seri:
            return seri_result

        else:
            return mapping


def set_appinfo_proto_field(appinfo_proto, appinfo={}):
    """ 设置应用协议字段
    """
    appinfo_proto.appId = int(appinfo[APP_INFO_FIELD.appid])
    appinfo_proto.packId = int(appinfo[APP_INFO_FIELD.packid])
    appinfo_proto.showName = unicode(appinfo[APP_INFO_FIELD.show_name])
    appinfo_proto.packName = unicode(appinfo[APP_INFO_FIELD.pack_name])
    appinfo_proto.signCode = appinfo[APP_INFO_FIELD.sign_code]
    appinfo_proto.devName = unicode(appinfo[APP_INFO_FIELD.dev_name])
    appinfo_proto.appClass = unicode(appinfo[APP_INFO_FIELD.app_class])
    appinfo_proto.appType = unicode(appinfo[APP_INFO_FIELD.app_type])
    appinfo_proto.downTimes = unicode(common.show_downtimes(appinfo[APP_INFO_FIELD.down_times]))
    appinfo_proto.commentTimes = int(appinfo[APP_INFO_FIELD.comment_times])
    appinfo_proto.commentScore = int(appinfo[APP_INFO_FIELD.comment_score])
    appinfo_proto.appTagFlag = int(appinfo[APP_INFO_FIELD.app_tag_flag])
    appinfo_proto.recommLevel = int(appinfo[APP_INFO_FIELD.recomm_level])
    appinfo_proto.recommFlag = int(appinfo[APP_INFO_FIELD.recomm_flag])
    appinfo_proto.recommWord = unicode(appinfo[APP_INFO_FIELD.recomm_word])
    appinfo_proto.thumbPicUrl = appinfo[APP_INFO_FIELD.thumb_url]
    appinfo_proto.iconUrl = appinfo[APP_INFO_FIELD.icon_url]
    for each_pic_url in appinfo[APP_INFO_FIELD.app_pic_url].split(','):
        appinfo_proto.appPicUrl.append(each_pic_url)
    appinfo_proto.packUrl = appinfo[APP_INFO_FIELD.pack_url]
    appinfo_proto.packMD5 = unicode(appinfo[APP_INFO_FIELD.pack_md5])
    appinfo_proto.packSize = int(appinfo[APP_INFO_FIELD.pack_size])
    appinfo_proto.verCode = int(appinfo[APP_INFO_FIELD.ver_code])
    appinfo_proto.verName = unicode(appinfo[APP_INFO_FIELD.ver_name])
    appinfo_proto.compDesc = unicode(appinfo[APP_INFO_FIELD.comp_desc])
    appinfo_proto.lanDesc = unicode(appinfo[APP_INFO_FIELD.lan_desc])
    appinfo_proto.appDesc = unicode(appinfo[APP_INFO_FIELD.app_desc])
    appinfo_proto.updateDesc = unicode(appinfo[APP_INFO_FIELD.update_desc])
    appinfo_proto.publishTime = appinfo[APP_INFO_FIELD.publish_time]
    appinfo_proto.packUrl2 = unicode(appinfo[APP_INFO_FIELD.pack_url2])
    permission_list = unicode(appinfo[APP_INFO_FIELD.permission])
    if permission_list:
        permission_array = permission_list.split(',')
        for user_pms in permission_array:
            if user_pms:
                pms = appinfo_proto.permission.add()
                pms.permissionFlag = user_pms


def get_appinfo_list_proto(appinfo_data_list):
    """ 生成 协议AppInfoList 结果
    参数: appinfo源信息列表
    """
    if appinfo_data_list:
        list_proto = proto.AppInfoList()

        for item in appinfo_data_list:
            appinfo_proto = list_proto.appInfo.add()
            set_appinfo_proto_field(appinfo_proto, item)

        return list_proto.SerializeToString()
    return ""


def gen_appinfo4proto(ver, appinfo={}, trans=None):
    """ 生成应用协议化后的结果缓存
    """
    if appinfo:
        appinfo_proto = proto.AppInfo()
        set_appinfo_proto_field(appinfo_proto, appinfo)

        seri_ret = appinfo_proto.SerializeToString()
        appinfo_seri_cname = ApiSeriCacheName.SERI_APP_INFO % appinfo[APP_INFO_FIELD.appid]
        result = common.union_cache(ver, seri_ret)

        if trans:
            trans.set(appinfo_seri_cname, result)

        else:
            _redis.set(appinfo_seri_cname, result)

        return ver, seri_ret
    return '', ''


def get_appinfos(appids=[]):
    """ 返回多个AppInfo，
   可自动检测缓存是否存在
   参数：应用ID列表
   返回：AppInfo原始信息的列表
    """
    if appids:
        appids = [int(i) for i in appids]
        trans = _redis.pipeline()

        for item in appids:
            appinfo_cname = APISrcCacheName.APP_INFO % item
            trans.hgetall(appinfo_cname)

        trans_ret = trans.execute()
        trans_ret = [i for i in trans_ret if i]
        need2gen_appid = [i for i in appids if i not in [int(j[APP_INFO_FIELD.appid]) for j in trans_ret]]
        trans_ret += gen_appinfos(need2gen_appid, False)
        return trans_ret
    return []


def get_appinfo_by_packname(pack_name, pack_sign=''):
    """ 获取appinfo信息
    """
    if pack_name:
        select_sql = """
                    select 
                    a.AppID,
                    a.PackName,
                    a.PackSign,
                    a.AppName,
                    a.ShowName,
                    p.PackSize,
                    p.VerCode,
                    p.VerName,
                    p.PackMD5,
                    p.PackUrl,
                    p.PackUrl2
                    from appinfo a
                    inner join packinfo p
                    on a.AppID=p.AppID
                    and a.MainPackID=p.PackID
                    and a.DataStatus=1 and p.Status=1
                    and a.PackName='%s'
                    """ % (pack_name) + "" if pack_sign=="" else " and a.PackSign='%s'" % (pack_sign) + "limit 1" 

        with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
            query_data = mysql.query(select_sql)
            if query_data:
                return query_data[0]

