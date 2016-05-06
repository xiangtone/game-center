#!bin/env python 
#-*- encoding=utf8 -*-
#
# 创建人：wind
#
# 时  间：20150901
#
# 描  述：用户举报

import datetime

import base_utils.db as db  
import base_utils.cache as cache
import base_utils.logger as logger

from model.const import APISrcInformCacheName
import config
import logic.common as common

APP_INFORM_COUNT_CACHE = ""
_CACHE_INFORM_FILED = "%s_%s"   # imei_openId
  
def set_appinform_cache(imei,openid):
    """
        设置用户举报次数的缓存
        imei     设备imei
        openid   用户openId

        返回int  0-可以操作数据库，1-举报次数过多
    """
    # 判断缓存是否存在 
    cache_obj = cache.Cache(config.GLOBAL_SETTINGS['redis'])  # 引用缓存信息
    key = APISrcInformCacheName.APP_INFORM_COUNT_CACHE
    field = _CACHE_INFORM_FILED % (imei,openid)

    # 判断是否已经评论过
    if cache_obj.exists(key): 
        if cache_obj.hexists(key,field):  
            value = cache_obj.hget(key,field)
            if int(value)<10:  
                cache_obj.hset(key,field,(int(value)+1)) 
            else:
                return 1
        else:
            cache_obj.hset(key,field,1) 
    else:  
        expire_time = common.get_interval_seconds()
        
        cache_obj.hset(key,field,1)    
        cache_obj.expire(key,expire_time)  # 设置缓存保存2天时间

    return 0

def add_app_inform(inform_obj):
    """
        添加用户举报信息

        inform_obj  包含举报信息的字典对象
    
            openid        # 获取用户openid
            imei          # 设备imei
            appid         # 应用ID
            clientid      # ClientId
            version       # 版本号
            brandflag     # 来源品牌
            modelflag     # 来源机型
            deviceflag    # 来源子机型
            level         # 平台Level
            channelno     # 渠道号
            informtype    # 举报类型
            informdetail  # 更多描述

    """
    openId = inform_obj['openid']   # 获取用户openid
    imei = inform_obj['imei']       # 设备imei
    appId = inform_obj['appid']     # 应用ID
    clientId = inform_obj['clientid'] # ClientId
    version = inform_obj['version']  # 版本号
    brandFlag = inform_obj['brandflag'] # 来源品牌
    modelFlag = inform_obj['modelflag'] # 来源机型
    deviceFlag = inform_obj['deviceflag']  # 来源子机型
    level = inform_obj['level']  # 平台Level
    channelNo = inform_obj['channelno']  # 渠道号
    informType = inform_obj['informtype'] # 举报类型
    informDetail = inform_obj['informdetail']  # 更多描述

    nowTime = datetime.datetime.now() 
    inform_time = nowTime.strftime('%Y-%m-%d %H:%M:%S') 
    start_time = nowTime.strftime('%Y-%m-%d 00:00:00')
    end_time = (nowTime+datetime.timedelta(days=1)).strftime('%Y-%m-%d 00:00:00')
    inform_id = 0 
    # 1.该openid是否在同一天对同一个应用进行了举报，再次举报将修改之前的举报信息
    try:
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:  
            sql = "SELECT InformId from AppInform where CreateTime>=%s and CreateTime<%s and OpenId=%s and Imei=%s and AppId=%s and Status=1 " 
            info = con.query(sql,start_time,end_time,openId,imei,appId)   
            if info:
                inform_id = info[0]['InformId']
            # 设置保存的信息的字编码 
            con.execute("SET NAMES utf8mb4;") 
            # 结果值，0-失败，1-成功    
            sql_result = 0
            if inform_id:  
                sql = "update AppInform set InformType=%s,InformDetail=%s,InformTime=%s,UpdateTime=%s where InformId=%s"
                sql_result = con.execute_rowcount(sql,informType,informDetail,inform_time,inform_time,inform_id)  
                if sql_result==1:
                    con.commit()

            else:
                sql = "insert into AppInform (OpenId,Imei,AppId,ClientId,ChannelNo,Version,BrandFlag,ModelFlag,DeviceFlag,Level,InformType,InformDetail,InformTime,CreateTime,UpdateTime,Status) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);"
                sql_result = con.execute_rowcount(sql,openId,imei,appId,clientId,channelNo,version,brandFlag,modelFlag,deviceFlag,level,informType,informDetail,inform_time,inform_time,inform_time,1)  
                if sql_result==1:
                    con.commit() 
                     
            return sql_result
            
    except Exception,ex:   
        mylogger = logger.GlobalLogger() 
        mylogger.write(u'appinform add_app_inform  error: %s' % (ex))
        return 0
 

