#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet

import common
import config
import libs.cache as cache
import model.const as m_const
import model.appsconfig as m_appsconfig
from model.const import ApiSeriCacheName
from model.const import GlobalConfig


def req_globalconfig_common(schemeid, cache_version=''):
    """ 协议通用调用方法
        返回：(是否最新版本，服务器当前版本，序列化后的数据)
        获取分组信息
    """
    seri_name = ApiSeriCacheName.SERI_APPS_CONFIG % schemeid
    gen_func = m_appsconfig.gen_appsconfig
    rsp_data = common.get_cache_comm(seri_name, cache_version, gen_func, schemeid)
    return rsp_data


class ReqGlobalConfig(ProtoBase):

    """获取商店的配置
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    def set_start_cache(self):
        """ 设置启动进入游戏中心的cache
        """
        control_update_flag = config.GLOBAL_SETTINGS.get("control_update_rate", False)
        if control_update_flag:
            device_id = self.comm_params["ei"] + self.comm_params["ai"]
            if device_id:
                cache_name = m_const.CacheInfo.API_PRIVATE_SRC_PREFIX + "ReqConfig:" + device_id
                cache.redis.set(cache_name, 1)
                cache.redis.expire(cache_name, 30)


    @tornado.gen.coroutine
    def deal(self):
        try:

            # 用于控制升级频率检测
            self.set_start_cache()

            schemeid = yield self.match_schemeid()

            # 2015.11.5注释，修改成从通用方法获取
            # seri_name = ApiSeriCacheName.SERI_APPS_CONFIG % schemeid
            # gen_func = m_appsconfig.gen_appsconfig
            # rsp_data = common.get_cache_comm(seri_name, self.request.groupsCacheVer, gen_func, schemeid)
            rsp_data = req_globalconfig_common(schemeid, self.request.groupsCacheVer)
            
            self.response.rescode = 0
            self.response.resmsg = u'成功'
            self.response.thumbPicSwitch = GlobalConfig.THUMB_PIC_SWITCH
            #self.response.updateCheckRate = GlobalConfig.UPDATE_CHECK_RATE
            self.response.updateCheckRate = config.GLOBAL_SETTINGS.get("check_rate",{}).get("update_check_rate", 60*6)
            self.response.recommCheckRate = config.GLOBAL_SETTINGS.get("check_rate",{}).get("recomm_check_rate", 60*24*7)

            # print self.response.updateCheckRate
            # print self.response.recommCheckRate

            self.response.groupsServerVer = rsp_data[1]
            self.response.groups = rsp_data[2] if not rsp_data[0] else ""

        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqGlobalConfig', ex)
        self.callback()

    @tornado.gen.coroutine
    def match_schemeid(self):
        """匹配方案id
        default: 昂达平板101，手机102，桌面分发103，华硕版游戏中心104
        """

        # default
        schemeid = 102

        clientid = self.comm_params.get("clientid")

        # 第一版桌面，海外版桌面
        if clientid in (4,13):       
            schemeid = 103

        # 游戏中心--华硕版
        elif clientid == 12:    
            schemeid = 104
            
        # 应用商店
        elif clientid == 6:     
            channelno = self.comm_params.get("clientver").split('.')[3]

            # 2015.12.10 修改 101方案为昂达平板专用方案
            if int(channelno) == 20:
                schemeid = 101
            else:
                schemeid = 102

            # 2015.12.10 注释
            # if int(channelno) in (11,21,31):
            #     schemeid = 102
            # elif int(channelno) in (10,20,30):
            #     schemeid = 101

        raise tornado.gen.Return(schemeid)
            