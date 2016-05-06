#!bin/env python 
#-*- encoding=utf8 -*-
#
# 创建人：wind
#
# 时  间：20150901
#
# 描  述：用户反馈

import datetime

import base_utils.db as db  
import base_utils.cache as cache
import base_utils.logger as logger

import config

def add_app_feedback(openid,imei,content,usercontact,clientid,channelno,version,brandFlag,modelFlag,deviceFlag):
    """
        添加用户反馈信息

        openid         反馈的用户
        imei           设备imei
        content        反馈内容
        usercontact    联系方式
        clientid       ClientId
        channelno      渠道号
        version        版本号
        brandFlag      品牌
        modelFlag      机型
        deviceFlag     子机型
    """

    nowTime = datetime.datetime.now() 
    inform_time = nowTime.strftime('%Y-%m-%d %H:%M:%S')  
     
    try:
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:    
            # 设置保存的信息的字编码 
            con.execute("SET NAMES utf8mb4;") 
            sql = "insert into FeedBack (OpenId,Imei,Content,UserContact,ClientId,ChannelNo,Version,BrandFlag,ModelFlag,DeviceFlag,CreateTime,UpdateTime,Status) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);"
            sql_result = con.execute_rowcount(sql,openid,imei,content,usercontact,clientid,channelno,version,brandFlag,modelFlag,deviceFlag,inform_time,inform_time,1)  
            if sql_result==1:
                con.commit()  
            return sql_result
            
    except Exception,ex:   
        mylogger = logger.GlobalLogger() 
        mylogger.write(u'appinform add_app_inform  error: %s' % (ex))
        return 0
 

