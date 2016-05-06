#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet

import config
import model.applist as m_applist
import model.const as m_const
import libs.cache as cache


class ReqAppsUpdate(ProtoBase):

    """获取应用更新
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:
            req = self.request

            self.response.rescode = 0
            self.response.resmsg = u'成功'

            if self.is_check_udpate():
                # 是否控制
                check_update_type = req.checkUpdateType
                control_update_version_list = config.GLOBAL_SETTINGS.get('activate_app_update',{}).get('control_version',[])
                client_ver = self.comm_params.get("clientver","")
                if client_ver and client_ver in control_update_version_list:
                    check_update_type = 0
                self.response.appInfoList = m_applist.gen_apps_udpate(req.localAppVer, check_update_type)
                self.set_check_update_record()

        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqAppsUpdate', ex)
        self.callback()

    def is_check_udpate(self):
        """ 是否检测更新
        """
        control_update_flag = config.GLOBAL_SETTINGS.get("control_update_rate", False)
        if control_update_flag:
            device_id = self.comm_params["ei"] + self.comm_params["ai"]
            req_config_cache_name = m_const.CacheInfo.API_PRIVATE_SRC_PREFIX + "ReqConfig:" + device_id
            req_config_value = cache.redis.get(req_config_cache_name)

            if req_config_value:
                return True

            else:   # 频率控制
                cache_name = m_const.CacheInfo.API_PRIVATE_SRC_PREFIX + "check_rate:" + device_id
                cache_value = cache.redis.get(cache_name)
                return True if not cache_value else False
        else:
            return True


    def set_check_update_record(self):
        """ "检测更新"操作记录
        """
        check_rate = config.GLOBAL_SETTINGS.get("check_update_rate", 60*60*24)
        device_id = self.comm_params["ei"] + self.comm_params["ai"]
        cache_name = m_const.CacheInfo.API_PRIVATE_SRC_PREFIX + "check_rate:" + device_id
        cache.redis.set(cache_name, 1)
        cache.redis.expire(cache_name, check_rate)