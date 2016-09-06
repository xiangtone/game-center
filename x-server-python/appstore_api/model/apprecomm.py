#!bin/env python 
#-*- encoding=utf8 -*-
#
# 创建人: wind
#
# 时  间: 2015-09-07
#
# 描  述: 应用推荐
#
#

import datetime
import random

import base_utils.db as db  
import base_utils.cache as cache
 
import config
import appinfo as m_appinfo
import proto.Apps_pb2 as app_proto
import libs.util as util
from const import ApiSeriRecommCacheName
import applist as m_applist
import logic.common as common

  

def get_app_id_list(apptype,order_type,channelno,architecture):
    """
        获取应用类型前100的应用id

        apptype        应用类型
        order_type     排序类型：0=自动-热门，2=按时间
        channelno      渠道号
    """
    app_id_list = []

    if apptype: 
        order_by_type = 'ai.DownTimes'
        if order_type==2:
            order_by_type = 'ai.OpUpdateTime'
 
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:

            #sql = "select ai.AppID from AppInfo ai inner join PackInfo pi on ai.AppID=pi.AppID and ai.MainPackID=pi.PackID where ai.AppType=%s and ai.Status=1 and pi.Status=1 and pi.IsMainVer=1 and ai.ChannelAdaptation like '%s' " % (apptype,('%%,'+str(channelno)+',%%'))
            sql = "select ai.AppID from AppInfo ai inner join PackInfo pi on ai.AppID=pi.AppID and ai.MainPackID=pi.PackID where ai.AppType=%s and ai.Status=1 and pi.Status=1 and pi.IsMainVer=1 " % (apptype)
            if channelno and int(channelno) in config.GLOBAL_SETTINGS.get("channel_adaptation_list", []):
                sql += " and ai.ChannelAdaptation like '%s' " % ('%%,'+str(channelno)+',%%')

            if channelno and int(channelno) in config.GLOBAL_SETTINGS.get("architecture_check_channel", []) and architecture and int(architecture)>0:
                sql += " and ai.Architecture&%s>0" % architecture
                    
            if order_type==2:
                sql = sql + """ order by ai.OpUpdateTime desc limit 0,100;"""
            else:
                sql = sql + """ order by ai.DownTimes desc limit 0,100;"""

            query_obj = con.query(sql)  
            if query_obj:
                for temp in query_obj: 
                    app_id_list.append(temp['AppID'])
    
    return app_id_list


  
def get_recommend_info(appid,apptype,order_type,channelno,architecture,seri_name):
    """
        获取应用的推荐

        参数: appid     应用id
              apptype   应用类型
    """
    # import pdb
    # pdb.set_trace()
    # 应用类型为空时，查询应用appid的应用类型
    if not apptype:
        # 查询数据库中appid的应用类型
        temp_list = [appid]
        temp_appinfo_list = m_appinfo.get_appinfos(temp_list)
        if temp_appinfo_list:
            apptype = temp_appinfo_list[0]['atype']


    # 查询应用类型前100的应用id
    appid_list = get_app_id_list(apptype,order_type,channelno,architecture)

    if appid_list: 
        # 移除自身的appid
        if appid in appid_list:
            appid_list.remove(appid)

        select_appid_list = []
        # 随机选取其中12个应用id
        # 以下代码可以优化 odddshou
        if len(appid_list)> 6:
            while len(select_appid_list)< 6:
                random_index = random.randint(0, len(appid_list)-1) # 生成随机数 
                select_appid = appid_list.pop(random_index)
                if select_appid not in select_appid_list:
                    select_appid_list.append(select_appid)
        else:
            select_appid_list.extend(appid_list)

        # 获取选中应用列表的信息
        appinfo_list = m_appinfo.get_appinfos(select_appid_list)

        # 序列化后的缓存名
        #seri_name = ApiSeriRecommCacheName.SERI_RECOMMEND_INFO % appid
        seri_elems_ver = util.time2ver(datetime.datetime.now())

        # 创建协议的对象
        elems_proto = app_proto.GroupElems()
     
        for i in range(len(appinfo_list)):  
            m_applist._appinfo2eleminfo(elems_proto,appinfo_list[i],i+1)
      
        seri_data = elems_proto.SerializeToString()  # 序列化
        seri_ret = common.union_cache(seri_elems_ver, seri_data)
        m_applist._redis.set(seri_name, seri_ret)
        # 获取当前时间距离第二天零点间隔多少秒
        expire_time = common.get_interval_seconds()
        m_applist._redis.expire(seri_name,expire_time)  # 设置缓存保存2天时间

        return seri_elems_ver, seri_data
    return "",""
   