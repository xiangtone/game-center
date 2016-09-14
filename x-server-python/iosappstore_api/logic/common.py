#!/bin/env python
#-*- encoding=utf8 -*-
import datetime
import base_utils.logger as nw_logger
import libs.cache as cache
import config
from model.const import BsConst
_redis = cache.redis
logger = nw_logger.CommonLog


def  get_cache_comm(seri_name, client_ver, gen_func, *args):
    """ 通用的取缓存检验方法
    返回：是否最新版本，服务器当前版本，序列化后的数据
    """
    server_ver, seri_data = '', ''
    is_newest = False
    data = _redis.get(seri_name)
    if data:
        server_ver, seri_data = split_cache(data)

    if not server_ver:
        server_ver, seri_data = gen_func(*args)
    elif server_ver == client_ver:
        is_newest = True

    return is_newest, server_ver, seri_data

def split_cache(cache_data=''):
    """ 拆分缓存
    返回:(版本,内容信息)
    """
    if cache_data and len(cache_data) >= BsConst.CACHE_VER_LENGTH + \
                    len(BsConst.CACHE_SPLITE) and  cache_data[BsConst.CACHE_VER_LENGTH:BsConst.CACHE_VER_LENGTH + \
                    len(BsConst.CACHE_SPLITE)] == BsConst.CACHE_SPLITE:
        return (cache_data[:BsConst.CACHE_VER_LENGTH], cache_data[BsConst.CACHE_VER_LENGTH + len(BsConst.CACHE_SPLITE):])
    return ("", "")

def union_cache(ver, seri_data):
    """ 组装缓存
    返回：版本+缓存信息的组装结果
    """
    return ver + BsConst.CACHE_SPLITE + seri_data

def show_downtimes(downtimes):
    """ 展示下载次数
    """
    downs = int(downtimes or 0)

    if downs > 1000 and downs < 10000:
        return "%s千人下载" % (downs/1000)
    elif downs > 10000:
        return "%s万人下载" % (downs/10000)
    else:
        return "%s人下载" % downs

def show_page_size(packsize):
    """ 展示大小
    """
    size = int(packsize or 0)
    return "%sM" % round(size, 2)

def get_channel_no(client_ver):
    """ 通过版本号获取渠道号
    """
    if client_ver:
        ver_array = client_ver.split('.')
        if(len(ver_array) == 4):
            return ver_array[3]

def get_interval_seconds():
    """
        获取当前时间距离第二天零点间隔多少秒 
    """
    nowTime = datetime.datetime.now() 
    inform_time = nowTime.strftime('%Y-%m-%d %H:%M:%S')  
    end_time = (nowTime+datetime.timedelta(days=1)).strftime('%Y-%m-%d 00:00:00') 
    end_date = datetime.datetime.strptime(end_time, '%Y-%m-%d 00:00:00') 

    # 计算缓存保存的时间，到第二天凌晨还有多少时间
    expire_time = (end_date-nowTime).seconds

    return expire_time

def match_architecture(comm_args):
    """ 匹配设备的架构
        arm=1, x86=2
    """
    arm_list = config.GLOBAL_SETTINGS.get("md_list",{}).get("arm_list",[])
    x86_list = config.GLOBAL_SETTINGS.get("md_list",{}).get("x86_list",[])

    arm_list = [i.lower() for i in arm_list]
    x86_list = [i.lower() for i in x86_list]

    if 'md' in comm_args:
        #md = comm_args['md'].replace(' ', '+').lower()
        md = comm_args['md'].lower()
        if md in arm_list:
            return 1
        if md in x86_list:
            return 2

    return 0

class _Common(object):

    def sync(self):

        """ 同步
        """
        try:
            import model.download as m_download
            import model.applist as m_applist

            # 同步应用的下载次数数据
            m_download.sync_downtimes() 

            # 清除搜索的缓存
            m_applist.SearchInfo.src_search_info = {}

        except Exception, ex:
            logger.write_log('sync', ex)
            raise

Common = _Common()