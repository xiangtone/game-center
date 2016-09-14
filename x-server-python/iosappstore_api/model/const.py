#!/bin/env python
#-*- encoding=utf8 -*-





class BsConst(object):

    """ 业务相关的常量
    """
    CACHE_SPLITE = "$$"                                     # 缓存版本分隔符
    CACHE_VER_LENGTH = 14                                   # 缓存版本长度:20140101101010

    APPLIST_LENGTH = 500                                    # 列表的长度


class CacheInfo(object):

    """ 缓存名定义
    """
    __api_cache_prefix = "apps_api:"
    __api_private_cache_prefix = "apps_api_private:"
    __bg_cache_prefix = "apps:"

    SERI_PREFIX = __api_cache_prefix + "seri:"                      # 序列化后的缓存前缀
    API_SRC_PREFIX = __api_cache_prefix + "src:"                    # 接口生成的源缓存前缀
    API_PRIVATE_SRC_PREFIX = __api_private_cache_prefix + "src:"    # 接口生成的私有源缓存前缀
    BG_SRC_PREFIX = __bg_cache_prefix + ""                          # 后台生成的源缓存前缀



class DBOrderTypeEnum(object):

    """ 排序类型
    """
    AUTO = 0
    UPDATE_TIME = 2 


class ApiSeriCacheName(object):

    """ 接口生成的序列化后的缓存
    """
    SERI_APPS_CONFIG = CacheInfo.SERI_PREFIX + "appsconfig:%s"                  # 序列化后的全局配置缓存,参数:schemeid
    SERI_GROUP_ELEMS = CacheInfo.SERI_PREFIX + "groupelems:%s_%s_%s"            # 序列化后的分组元素缓存,参数:groupid_pagesize_pageindex
    SERI_APP_INFO = CacheInfo.SERI_PREFIX + "appinfo:%s"                        # 序列化后的应用信息缓存,参数:appid
 
    # 支持渠道分发应用后，新的缓存结构,(2015.12.23修改，增加架构支持)
    SERI_CHNNO_GROUP_ELEMS = CacheInfo.SERI_PREFIX + "groupelems:%s:%s:%s_%s_%s"   # 序列化后的分组元素缓存,参数:architecture:chnno:groupid_pagesize_pageindex

    # 支持IOS应用信息,新的缓存结构,(2016.8.26)
    SERI_IOSAPP_INFO=CacheInfo.SERI_PREFIX+"appinfoios:%s" # 序列化后的应用信息缓存,参数:appid
    SERI_IOSPAGE_ELEMS = CacheInfo.SERI_PREFIX + "pageelems:%s_%s" #序列后的分组元素缓存,参数pagesize_pageindex


class BGSrcCacheName(object):

    """ 后台生成的缓存源
    """
    GROUP_SCHEME = CacheInfo.BG_SRC_PREFIX + "sGroupScheme:%s"          # 分组方案,参数:schemeid
    GROUP_INFO = CacheInfo.BG_SRC_PREFIX + "hGroupInfo:%s"              # 分组信息,参数:groupid
    GROUP_ELEM_KEY = CacheInfo.BG_SRC_PREFIX + "ssGroupElemKey:%s"      # 分组元素Key,参数:groupid,返回Val:[groupelemid]_[elemtype]_[elemid]_[posid]
    GROUP_ELEM = CacheInfo.BG_SRC_PREFIX + "hGroupElems:%s_%s"          # 分组元素,参数:groupid_groupelemid_elemtype_elemid
    LINK_INFO = CacheInfo.BG_SRC_PREFIX + "hLinkInfo:%s"                # 外链信息，参数:linkid
    NEWEST_APP = CacheInfo.BG_SRC_PREFIX + "hNewestAppVer:%s_%s"        # 最新应用信息:参数:packname_md5(signcode)
    CHNNOLIST = CacheInfo.BG_SRC_PREFIX + "lChnnoList"                  # 上线应用配置的分发渠道汇总列表。






class APISrcCacheName(object):

    """ 接口生成的缓存源
    """

    APPIOS_INFO=CacheInfo.API_SRC_PREFIX + "AppInfoIos:%s"                  #ios应用信息参数appid
     


    class AppInfoIosField(object):

        """
         ios应用信息字段
        """
        appid = 'appid'
        appname = 'appname'
        showname = 'showname'
        devname = 'devname'
        apptype ='apptype'
        appsize = 'appsize'
        appprice='appprice'
        appversion='appversion'
        recommflagword='recommflagword'
        iconpicurl = 'iconpicurl'
        thumbpicurl= 'thumbpicurl'
        app_pic_url ='apppicurl'
        appurl = 'appurl'
        appdesc = 'appdesc'
        recommword = 'recommword'
        updatetime = "updatetime"
        adspicurl = 'adspicurl'
        remarks = 'remarks'
   

