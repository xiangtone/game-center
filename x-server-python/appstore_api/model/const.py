#!/bin/env python
#-*- encoding=utf8 -*-


class GlobalConfig(object):

    """ 商店业务的全局配置
    """
    THUMB_PIC_SWITCH = 0          # 缩略图开关
    UPDATE_CHECK_RATE = 60 * 6    # 应用升级检测频率


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


class ElemTypeEnum(object):

    """ 元素类型
    """
    APP = 1             # 应用
    LINK = 2            # 链接
    JUMP2CALSS = 3      # 跳转到分类
    JUMP2GAME = 4       # 跳转到网游单机
    JUMP2SUBJECT = 5    # 跳转到专题
    SEARCH_WORD = 6      # 搜索词
    FLASH_PIC = 7           # 闪屏图


class GroupClassEnum(object):

    """ 分组类别
    """
    APP_GAME = 10
    APP = 11
    GAME = 12
    OLGAME_SINGLEGAME = 21
    SUBJECT = 31
    GAME_SUBJECT = 32
    RECOMM = 41
    DISTRIBUTE = 51 

    # 只能后台配置的数据
    ONLY_FOR_CONFIG = (SUBJECT, GAME_SUBJECT, RECOMM, DISTRIBUTE) 
    # 可自动，又可后台配置控制的数据
    CONTROLLABLE = (APP_GAME, APP, GAME, OLGAME_SINGLEGAME)


class GroupTypeEnum(object):

    """ 分组类别
    """
    ALL_APP_GAME = 1000
    RISE_FASTER_APP_GAME = 1001

    ALL_APP = 1100

    ALL_GAME = 1200

    OL_GAME = 2101
    SINGLE_GAME = 2102

    DEFAULT_SUBJECT = 3100 
    ESSENTIAL_GAME_SUBJECT = 3101       # 必备游戏
    ESSENTAIL_SUBJECT = 3102            # 必备应用游戏

    START_RECOMM = 4101
    FIRST_PAGE_RECOMM_4_GAME =4102
    RECOMM_GAME_4_SEARCH = 4103
    RECOMM_SEARCH_WORD = 4104
    FLASH_RECOMM = 4105
    FIRST_PAGE_RECOMM_4_APPGAME = 4106
    QUALITY_APP_RECOMM = 4107
    QUALITY_GAME_RECOMM = 4108
    AVOID_FLOW_RECOMM_APPGAME = 4109
    
    LAUNCHER_DISTRI = 5101


class DBIsNetGameEnum(object):

    """ 是否网游
    """
    NET_GAME = 1
    SINGLE_GAME =2


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


    class GroupInfoField(object):

        """ 分组信息字段
        """
        group_id = 'gid'
        group_class = 'gclass'
        group_type = 'gtype'
        order_type = 'odtype'
        order_no = 'odno'
        group_tips = 'gtips'
        group_name = 'gname'
        group_desc = 'gdesc'
        group_pic_url = 'gpic'
        start_time = 'stime'
        end_time = 'etime'
        type_pic_url = 'tpic'
        # create_time = 'ctime'
        # update_time = 'utime'


    class GroupElemField(object):

        """ 分组元素字段
        """
        group_elem_id = "geid"
        group_id = "gid"
        pos_id = "posid"
        elem_type = "etype"
        elem_id = "eid"
        show_type = "stype"
        group_type = "gtype"
        order_type = "otype"
        order_no = "ono"
        recomm_val = "rval"
        recomm_title = "rtitle"
        recomm_word = "rword"
        recomm_pic_url = "rpic"
        recomm_tag = "rmtag" # 推荐角标(recommFlag)
        remarks = "rmks"
        start_time = "stime"
        end_time = "etime"
        create_time = "etime"
        update_time = "utime"

    class LinkInfoField(object):

        """外链字段
        """
        link_id = 'lid'
        cp_id = 'cpid'
        dev_name = 'dname'
        link_name = 'lname' 
        show_name = 'sname'
        link_url = 'lurl'
        icon_url = 'iurl'
        coop_type = 'ctype'
        link_tag = 'ltag'
        link_desc = 'ldesc'
        remarks = 'rmks'
        create_time = 'ctime'
        update_time = 'utime'

    class NewestAppVerField(object):
        """ 最新应用版本字段
        """
        appid = 'appid'
        packid = 'packid'
        version_name = 'vname'
        version_code = 'vcode'
        app_class = 'aclass'
        pack_name = 'pname'



class APISrcCacheName(object):

    """ 接口生成的缓存源
    """
    APP_INFO = CacheInfo.API_SRC_PREFIX + "hAppInfo:%s"                     # 应用信息,参数:appid
    APP_DOWN_COUNT = CacheInfo.API_PRIVATE_SRC_PREFIX + "hAppDowntimes:%s"  # 应用下载成功次数,参数:appid
    MATCH_GROUPID = CacheInfo.API_SRC_PREFIX + "matchGroupID:%s_%s_%s_%s"   # 匹配分组id,参数:schemeid_groupclass_grouptype_ordertype
     

    class AppInfoField(object):


        """ 应用信息字段
        """
        appid = 'appid'
        packid = 'packid'
        show_name = 'soname'
        pack_name = 'pkname'
        sign_code = 'sgcode'
        dev_name = 'devname'
        app_class = 'aclass'
        app_type = 'atype'
        down_times = 'dwtimes'
        comment_times = 'cmtimes'
        comment_score = 'cmscore'
        app_tag_flag = 'apptag'
        recomm_level = 'rmlevel'
        recomm_flag = 'rmflag'
        recomm_word = 'rmword'
        thumb_url = 'tmurl'
        icon_url = 'icurl'
        app_pic_url = 'picurl'
        pack_url = 'pkurl'
        pack_url2 = 'pkurl2'
        pack_md5 = 'pkmd5'
        part_md5 = 'ptmd5'
        pack_size = 'pksize'
        ver_code = 'vcode'
        ver_name = 'vname'
        comp_desc = 'cmdesc'
        lan_desc = 'landesc'
        app_desc = 'appdesc'
        update_desc = 'upddesc'
        publish_time = 'pbltime'
        search_keys = 'shkeys'
        app_type_name = 'atname'
        issue_type = 'issuetype'
        channelnos = 'chnnos'
        channel_adaptation = 'chnadapt'
        permission = 'permission'
        architecture = 'archit'


   
class APISrcCommentCacheName(object):

    """ 评论及评分的接口生成的缓存源
    """
    # 新增时间: 2015-09-07
    APP_WAITAUDIT_CACHE = CacheInfo.API_SRC_PREFIX+'comments:waitaudit:hash' # 待审核的评论缓存 
    APP_PASSED_CACHE = CacheInfo.API_SRC_PREFIX+'comments:passed:%s'         # Hash 审核通过的评论缓存 %appId
    APP_SCORE_CACHE = CacheInfo.API_SRC_PREFIX+'comments:score:%s'           # 应用的评分缓存  
    APP_COMMENT_ID = CacheInfo.API_SRC_PREFIX+'comment:id'                   # 保存评论ID的缓存


class ApiSeriRecommCacheName(object):

    """ 应用推荐接口生成的序列化后的缓存
    """
    
    # 新增 2015-09-07
    SERI_RECOMMEND_INFO = CacheInfo.SERI_PREFIX + "recommend:%s:%s:%s"      # 序列化后的应用推荐信息缓存,参数 architecture:chnno:appid
 

class APISrcInformCacheName(object):

    """ 应用举报次数的缓存
    """
    
    # 新增 2015-09-07
    APP_INFORM_COUNT_CACHE = CacheInfo.API_SRC_PREFIX + "appinform"                        # 序列化后的应用推荐信息缓存,参数:appid
 
