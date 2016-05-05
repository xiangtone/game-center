#!/bin/env python
#-*- encoding=utf8 -*-

import datetime
import jieba
import tornado.gen
import appinfo as m_appinfo
import config as config
import libs.cache as cache
import libs.util as util
import proto.Apps_pb2 as proto
import model.appinfo as m_appinfo
import logic.common as common
import model.linkinfo as m_linkinfo
from libs.pinyin import PinYin
from model.const import ApiSeriCacheName
from model.const import APISrcCacheName
from model.const import BGSrcCacheName
from model.const import BsConst
from model.const import ElemTypeEnum
from model.const import GroupClassEnum
from model.const import GroupTypeEnum
from model.const import DBIsNetGameEnum
from model.const import DBOrderTypeEnum
from base_utils.db import MysqlConnection

ElEM_INFO_FIELD = BGSrcCacheName.GroupElemField
GROUP_INFO_FIELD = BGSrcCacheName.GroupInfoField
APP_INFO_FIELD = APISrcCacheName.AppInfoField
LINK_INFO_FIELD = BGSrcCacheName.LinkInfoField
APP_VER_FIELD = BGSrcCacheName.NewestAppVerField


_redis = cache.redis


def gen_group_elems(group_id, page_size, page_index, seri_name, channelno, architecture):
    """ 生成分组元素列表
    返回：版本，分组元素列表信息
    """
    if group_id:
        start, end = 0, 0
        if page_size == 0 and page_index == 0:
            start, end = 0, -1

        else:
            start, end = (page_index - 1) * page_size, page_index * page_size - 1

        # channelno = int(common.get_channel_no(comm_args.get("clientver")))
        # exist_channelno_array = _redis.lrange(BGSrcCacheName.CHNNOLIST, 0, -1)
        # chnno_list = [int(i) for i in exist_channelno_array if exist_channelno_array]
        # if channelno in chnno_list:
        #     seri_name = ApiSeriCacheName.SERI_GROUP_ELEMS % (channelno, group_id, page_size, page_index)
        # else:
        #     seri_name = ApiSeriCacheName.SERI_GROUP_ELEMS % ('default', group_id, page_size, page_index)


        group_info_cname = BGSrcCacheName.GROUP_INFO % group_id
        group_elem_key_cname = BGSrcCacheName.GROUP_ELEM_KEY % group_id

        # index(groupid=0, groupclass=1, grouptype=2, ordertype=3)
        cache_data = _redis.hmget(group_info_cname, GROUP_INFO_FIELD.group_id, GROUP_INFO_FIELD.group_class, GROUP_INFO_FIELD.group_type, GROUP_INFO_FIELD.order_type)

        seri_elems_ver = util.time2ver(datetime.datetime.now())
        #seri_name = ApiSeriCacheName.SERI_GROUP_ELEMS % (group_id, page_size, page_index)

        # todo: 代码整理

        # 分组类别：10=应用游戏, 11=应用分类，12=游戏分类，21=网游单机，31=专题，32=游戏专题，41=推荐，51=分发
        if cache_data and cache_data[1]:
            # 生成协议数据
            elems_proto = proto.GroupElems()

            # 自动 + 后台可控数据 （class=10,11,12,21）
            if int(cache_data[1]) in GroupClassEnum.CONTROLLABLE:

                # 获取全部的分组元素的key信息,内容格式:[groupelemid]_[elemtype]_[elemid]_[posid]
                elemids = _redis.zrange(group_elem_key_cname, 0, -1)
 
                # 获取配置跟非配置的分类应用id列表
                appids = _get_appid_from_group_elem_key(elemids)

                # 获取未适配设备架构的应用 2015.12.23新增
                if config.GLOBAL_SETTINGS.get("remove_no_suit_bgconfig_app", False):
                    remove_appids = _get_appid_not_match_architecture(appids, architecture)
                    for i_appid in remove_appids:
                        appids.remove(i_appid)

                row_ids = _get_applist_id_4_type(int(cache_data[1]), int(cache_data[2]), int(cache_data[3]), channelno, architecture)

                # 去除已配置的应用id
                deal_row_ids = []
                for item in row_ids:
                    if item["AppID"] not in appids:
                        deal_row_ids.append(dict(elem_id=item["AppID"], elem_type=ElemTypeEnum.APP, pos_id=item["RowNum"], src="appinfo"))

                # 合并
                for elem_key in elemids:
                    spilt_data = elem_key.split("_")
                    if len(spilt_data) == 4:
                        deal_row_ids.insert(int(spilt_data[3]) - 1, 
                            dict(elem_id=int(spilt_data[2]), elem_type=int(spilt_data[1]), pos_id=int(spilt_data[3]), src="elem", key_name=elem_key))

                # 截取长度
                deal_row_ids = deal_row_ids[0 : BsConst.APPLIST_LENGTH]

                # 重置位置id
                for i, v in enumerate(deal_row_ids):
                    v["pos_id"] = i + 1

                # 得到最终的数据的基本信息
                elemkey_deal_result = deal_row_ids[start : end+1]

                # 转换为字典
                elemkey_deal_result_dic = {}
                for item in elemkey_deal_result:
                    elemkey_deal_result_dic[str(item["elem_type"]) + "_" + str(item["elem_id"])] = item

                # 最终的数据结构,dict(posid=..  ,val = .., src=)
                mixture_result = []

                # 获取非配置的应用信息
                appinfo_ids = [i["elem_id"] for i in elemkey_deal_result if i["src"] == "appinfo"]

                appinfos_data = m_appinfo.get_appinfos(appinfo_ids)
                for item in appinfos_data:
                    app_key = elemkey_deal_result_dic.get(str(ElemTypeEnum.APP) + "_" + str(item[APP_INFO_FIELD.appid]))
                    if app_key:
                        mixture_result.append(dict(val=item, pos_id=app_key["pos_id"], src="appinfo"))

                # 获取配置的元素信息
                elem_keys = [i["key_name"] for i in elemkey_deal_result if i["src"] == "elem"]

                trans = _redis.pipeline()

                for elem in elem_keys:
                    group_elem_cname = BGSrcCacheName.GROUP_ELEM % (group_id, elem)
                    trans.hgetall(group_elem_cname)

                trans_ret = trans.execute()

                if trans_ret:
                    trans_ret = [i for i in trans_ret if i]
                    app_data = [
                        i for i in trans_ret if i[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.APP)]
                    link_data = [
                        i for i in trans_ret if i[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.LINK)]

                    src_app_list = util.list_to_dict(m_appinfo.get_appinfos(
                        [i[BGSrcCacheName.GroupElemField.elem_id] for i in app_data]), APP_INFO_FIELD.appid,True)
                    src_link_list = util.list_to_dict(m_linkinfo.get_link_infos(
                        [i[BGSrcCacheName.GroupElemField.elem_id] for i in link_data]), LINK_INFO_FIELD.link_id,True)

                    for elem in trans_ret:
                        if elem:
                            elem_id = int(elem[ElEM_INFO_FIELD.elem_id])

                            # app
                            if elem[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.APP) \
                            and elem_id in src_app_list.keys():   
                                mixture_result.append(dict(val=[elem, src_app_list[elem_id]], src="elem", pos_id=int(elem[ElEM_INFO_FIELD.pos_id])))

                            # link
                            if elem[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.LINK):                                                                                    
                                mixture_result.append(dict(val=[elem, src_link_list[elem_id]], src="elem", pos_id=int(elem[ElEM_INFO_FIELD.pos_id])))

                # 写elem_proto
                mixture_result_sort = sorted(mixture_result, key=lambda x: x["pos_id"])
                for item in mixture_result_sort:
                    if item["src"] == "elem":
                        _data2eleminfo(elems_proto, item["val"][0], item["val"][1])
                    elif item["src"] == "appinfo":
                        _appinfo2eleminfo(elems_proto, item["val"], item["pos_id"])

            # 纯后台内容配置数据，直接根据分页数据
            elif int(cache_data[1]) in GroupClassEnum.ONLY_FOR_CONFIG:
                elemids = _redis.zrange(group_elem_key_cname, start, end)
                if elemids:
                    trans = _redis.pipeline()
                    for elem in elemids:
                        group_elem_cname = BGSrcCacheName.GROUP_ELEM % (group_id, elem)
                        trans.hgetall(group_elem_cname)
                    trans_ret = trans.execute()

                    if trans_ret:
                        trans_ret = [i for i in trans_ret if i]
                        app_data = [i for i in trans_ret if i[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.APP)]
                        link_data = [i for i in trans_ret if i[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.LINK)]

                        src_app_list = util.list_to_dict(m_appinfo.get_appinfos([i[BGSrcCacheName.GroupElemField.elem_id] for i in app_data]), APP_INFO_FIELD.appid, True)
                        src_link_list = util.list_to_dict(m_linkinfo.get_link_infos([i[BGSrcCacheName.GroupElemField.elem_id] for i in link_data]), LINK_INFO_FIELD.link_id, True)


                        # 去除未适配架构的应用
                        if config.GLOBAL_SETTINGS.get("remove_no_suit_bgconfig_app", False):
                            if architecture > 0:
                                remove_list = []
                                for k,v in src_app_list.items():
                                    if v[APP_INFO_FIELD.architecture] & architecture == 0:
                                        remove_list.append(k)
                                for item in remove_list:
                                    src_app_list.pop(item)

                        for elem in trans_ret:
                            if elem:
                                elem_id = int(elem[ElEM_INFO_FIELD.elem_id])

                                if elem[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.APP): 
                                    if elem_id in src_app_list.keys():   # app
                                        _data2eleminfo(elems_proto, elem, src_app_list[elem_id])

                                # link
                                elif elem[ElEM_INFO_FIELD.elem_type] == str(ElemTypeEnum.LINK):
                                    _data2eleminfo(elems_proto, elem, src_link_list[elem_id])

                                else:
                                    _data2eleminfo(elems_proto, elem)

            seri_data = elems_proto.SerializeToString()
            seri_ret = common.union_cache(seri_elems_ver, seri_data)
            _redis.set(seri_name, seri_ret)
            return seri_elems_ver, seri_data
    return "", ""


def gen_apps_udpate(local_app_ver_list, check_update_type=0):
    """ 获取应用更新
    参数: proto local_app
    """

    # 忽略更新的应用列表
    ingore_update_list = []

    if local_app_ver_list:
        trans = _redis.pipeline()

        for item in local_app_ver_list:
            main_sign_code = util.md5_str(item.signCode)
            cache_name = BGSrcCacheName.NEWEST_APP % (item.packName, main_sign_code)
            trans.hgetall(cache_name)

        trans_ret = trans.execute()

        match_appids = []

        for i in xrange(len(local_app_ver_list)):
            if trans_ret[i] and (check_update_type == 0 or int(trans_ret[i][APP_VER_FIELD.app_class])%10 == check_update_type):
                if local_app_ver_list[i].verCode < int(trans_ret[i][APP_VER_FIELD.version_code]
                ) and local_app_ver_list[i].packName not in ingore_update_list: 
                    match_appids.append(trans_ret[i][APP_VER_FIELD.appid])

        # 获取应用信息，并按下载量排序返回
        app_info_data = m_appinfo.get_appinfos(match_appids)
        app_info_data = sorted(app_info_data, key=lambda x: long(x[APP_INFO_FIELD.down_times]), reverse=True)

        return m_appinfo.get_appinfo_list_proto(app_info_data)

    return ""


def _data2eleminfo(elems_proto, eleminfo={}, valueinfo={}):
    """ Combine Data To ElemInfo
    合并ElemInfo跟AppInfo到 ElemProto协议的ElemInfo结构
    """
    if elems_proto and eleminfo :
        elem = elems_proto.groupElemInfo.add()
        elem.groupId = int(eleminfo.get(ElEM_INFO_FIELD.group_id))
        elem.posId = int(eleminfo.get(ElEM_INFO_FIELD.pos_id))
        elem.orderNo = int(eleminfo.get(ElEM_INFO_FIELD.order_no))
        elem.elemType = int(eleminfo.get(ElEM_INFO_FIELD.elem_type))
        elem.showName = unicode(eleminfo.get(ElEM_INFO_FIELD.recomm_title))
        elem.recommWord = unicode(eleminfo.get(ElEM_INFO_FIELD.recomm_word))
        elem.recommFlag = int(eleminfo.get(ElEM_INFO_FIELD.recomm_tag, 0)) # 推荐角标
        elem.recommLevel = int(eleminfo.get(ElEM_INFO_FIELD.recomm_val))
        elem.iconUrl = unicode(eleminfo.get(ElEM_INFO_FIELD.recomm_pic_url))
        elem.thumbPicUrl = ""  # todo:..
        elem.adsPicUrl = unicode(eleminfo.get(ElEM_INFO_FIELD.recomm_pic_url)) if int(eleminfo.get(ElEM_INFO_FIELD.show_type)) == 1 else ""
        elem.publishTime = unicode(eleminfo.get(ElEM_INFO_FIELD.create_time))
        elem.showType = int(eleminfo.get(ElEM_INFO_FIELD.show_type))
        elem.startTime = unicode(eleminfo.get(ElEM_INFO_FIELD.start_time))
        elem.endTime = unicode(eleminfo.get(ElEM_INFO_FIELD.end_time))

        if int(eleminfo[ElEM_INFO_FIELD.elem_type]) == ElemTypeEnum.APP:
            elem.appId = int(valueinfo.get(APP_INFO_FIELD.appid))
            elem.packName = unicode(valueinfo.get(APP_INFO_FIELD.pack_name))
            elem.mainPackId = int(valueinfo.get(APP_INFO_FIELD.packid))
            elem.mainVerCode = int(valueinfo.get(APP_INFO_FIELD.ver_code))
            elem.mainSignCode = unicode(
                valueinfo.get(APP_INFO_FIELD.sign_code))
            elem.mainVerName = unicode(valueinfo.get(APP_INFO_FIELD.ver_name))
            elem.mainPackSize = int(valueinfo.get(APP_INFO_FIELD.pack_size))
            elem.appTypeName = unicode(valueinfo.get(APP_INFO_FIELD.app_type_name, ""))
            elem.iconUrl = unicode(valueinfo.get(APP_INFO_FIELD.icon_url))
            elem.downTimes = int(valueinfo.get(APP_INFO_FIELD.down_times))

            # 数据修正
            if not elem.recommWord:
                elem.recommWord = unicode(valueinfo.get(APP_INFO_FIELD.recomm_word))
            if not elem.recommLevel:
                elem.recommLevel = int(valueinfo.get(APP_INFO_FIELD.recomm_level))

        elif int(eleminfo[ElEM_INFO_FIELD.elem_type]) == ElemTypeEnum.LINK:
            # todo:...
            elem.jumpLinkId = int(valueinfo.get(LINK_INFO_FIELD.link_id))
            elem.jumpLinkUrl = unicode(valueinfo.get(LINK_INFO_FIELD.link_url))
            elem.iconUrl = unicode(valueinfo.get(LINK_INFO_FIELD.icon_url)) if not elem.iconUrl else elem.iconUrl

        elif int(eleminfo[ElEM_INFO_FIELD.elem_type]) in (ElemTypeEnum.JUMP2CALSS, ElemTypeEnum.JUMP2GAME, ElemTypeEnum.JUMP2SUBJECT):
            elem.jumpGroupId = int(eleminfo.get(ElEM_INFO_FIELD.elem_id))
            elem.jumpGroupType = int(eleminfo.get(ElEM_INFO_FIELD.group_type))
            elem.jumpOrderType = int(eleminfo.get(ElEM_INFO_FIELD.order_type))


def _appinfo2eleminfo(elems_proto, appinfo, pos_id=0):
    """ AppInfo To ElemInfo
    把AppInfo结构的数据，转换为ElemInfo的数据
    """
    if elems_proto and appinfo:
        elem_info = elems_proto.groupElemInfo.add()
        elem_info.groupId = 0
        elem_info.posId = pos_id
        elem_info.orderNo = 0
        elem_info.elemType = 1
        elem_info.showName = unicode(appinfo[APP_INFO_FIELD.show_name])
        # 如果推荐语为空，取下载次数
        elem_info.recommWord = unicode(appinfo[APP_INFO_FIELD.recomm_word]) \
            if appinfo[APP_INFO_FIELD.recomm_word] else unicode(appinfo[APP_INFO_FIELD.down_times])
        elem_info.recommFlag = int(appinfo[APP_INFO_FIELD.recomm_flag])
        elem_info.recommLevel = int(appinfo[APP_INFO_FIELD.recomm_level])
        elem_info.iconUrl = appinfo[APP_INFO_FIELD.icon_url]
        elem_info.thumbPicUrl = appinfo[APP_INFO_FIELD.thumb_url]
        elem_info.adsPicUrl = ''
        elem_info.publishTime = appinfo[APP_INFO_FIELD.publish_time]
        elem_info.appId = int(appinfo[APP_INFO_FIELD.appid])
        elem_info.packName = appinfo[APP_INFO_FIELD.pack_name]
        elem_info.mainPackId = int(appinfo[APP_INFO_FIELD.packid])
        elem_info.mainVerCode = int(appinfo[APP_INFO_FIELD.ver_code])
        elem_info.mainSignCode = appinfo[APP_INFO_FIELD.sign_code]
        elem_info.mainVerName = appinfo[APP_INFO_FIELD.ver_name]
        elem_info.mainPackSize = int(appinfo[APP_INFO_FIELD.pack_size])
        elem_info.appTypeName = unicode(appinfo[APP_INFO_FIELD.app_type_name])
        elem_info.downTimes = int(appinfo.get(APP_INFO_FIELD.down_times))


def _get_applist_id_4_type(group_class, group_type, order_type, chnno=0, architecture=0):
    """ 获取每个分类的非配置的应用id列表（排除groupelem表的分类数据）
    根据类型，类别等参数自动匹配
    """
    if group_class and group_type:

        app_sql = """
                     select AppID from (
                     select a.AppID,at.AppClass,at.AppType,a.IsNetGame,a.UpdateTime,a.DownTimes,a.IssueType,a.ChannelNos,a.ChannelAdaptation,a.Architecture
                     from
                        appinfo a
                            inner join
                        packinfo p ON a.AppID = p.AppID
                            and a.MainPackID = p.PackID
                            and a.DataStatus = 1
                            and a.Status = 1
                            and p.Status = 1
                            left join
                        apptypes at ON at.AppType = a.AppType )t where 1=1
                        %s """ + "limit %s" % BsConst.APPLIST_LENGTH

        where_sql = ""

        # 应用或者游戏
        if group_class in (GroupClassEnum.APP, GroupClassEnum.GAME):

            if group_type in (GroupTypeEnum.ALL_APP, GroupTypeEnum.ALL_GAME):
                where_sql = " and AppClass= %s" % group_class

            else:
                where_sql = " and AppClass= %s and AppType = %s" % (
                    group_class, group_type)

        # 网游/单机
        if group_type == GroupTypeEnum.OL_GAME:
            where_sql = " and AppClass= %s and IsNetGame = %s" % (GroupClassEnum.GAME, DBIsNetGameEnum.NET_GAME)

        elif group_type == GroupTypeEnum.SINGLE_GAME:
            where_sql = " and AppClass= %s and IsNetGame = %s" % (GroupClassEnum.GAME, DBIsNetGameEnum.SINGLE_GAME)

        # 渠道号处理
        if chnno and int(chnno) > 0:
            where_sql += " and ((issuetype=2 and ChannelNos like '%s') or (issuetype <> 2))" % ( '%%,' + str(chnno) +',%%' )

        # 针对渠道匹配的优质应用
        if chnno and int(chnno) in config.GLOBAL_SETTINGS.get("channel_adaptation_list", []):
            where_sql += " and ChannelAdaptation like '%s'" % ( '%%,' + str(chnno) +',%%')

        # arm,x86适配,2015.12.22新增
        if chnno and int(chnno) in config.GLOBAL_SETTINGS.get("architecture_check_channel", []) and architecture and int(architecture)>0:
            where_sql += " and Architecture&%s>0" % architecture
            
        # 排序
        if order_type == DBOrderTypeEnum.UPDATE_TIME:
            where_sql += " order by UpdateTime desc"

        else:
            where_sql += " order by DownTimes desc"

        app_sql = app_sql % where_sql

        with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
            query_data = mysql.query(app_sql)
            if query_data:
                for i, v in enumerate(query_data):
                    v.update(dict(RowNum=i + 1))
        return query_data
    return []


def _get_appid_from_group_elem_key(group_elem_value):
    """ 提取后台配置的分组元素的应用id
    根据缓存[groupelemid]_[elemtype]_[elemid]的结构，提取应用id
    """
    appids = []
    for elem_key in group_elem_value:
        split_data = elem_key.split("_")

        if len(split_data) == 4 and int(split_data[1]) == 1:
            appids.append(int(split_data[2]))

    return appids


def _get_appid_not_match_architecture(appids=[], architecture=0):
    """ 获取没有匹配设备架构的应用
    """
    if architecture > 0 and appids and len(appids)>0:
        in_sql = ""
        for appid in appids:
            in_sql+= "%s," % appid

        sql = "select appid from appinfo where Architecture&%s=0 and appid in (%s)" % (architecture, in_sql[:-1])

        print sql

        with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
            query_data = mysql.query(sql)
            if query_data:
                return [i["appid"] for i in query_data]
            #return query_data
    return []



class _SearchInfo(object):

    """ 搜索相关
    """

    # (search_key,down_times,update_time,app_class,app_type)
    src_search_info = {}

    def search_app(self, search_key, app_class, app_type, page_size, page_index, order_type, chnno=0, architecture=0):
        """ 搜索应用
        """
        if not self.src_search_info:
            self.gen_src_key_info()

        search_data_list = self.src_search_info.get(order_type, self.src_search_info[DBOrderTypeEnum.AUTO])
        if search_data_list:

            # 旧的匹配方法
            #match_app_infos = [i["appid"] for i in search_data_list if
            #                   (app_class == 0 or app_class == i["app_class"]) and
            #                   (app_type == 0 or app_type == i["app_type"]) and
            #                   (i["show_name"].find(search_key.lower()) >= 0 or i["search_key"].find(search_key.lower()) >= 0) 
            #                   or i["pinyin"].find(search_key.lower()) >= 0]

            match_app_infos = []
            all_match_app_infos = []
            part_match_app_infos = []
            for i in search_data_list:
                # 匹配类别
                if (app_class == 0 or app_class == i["app_class"]) and (app_type == 0 or app_type == i["app_type"]):
                    # 匹配优质应用
                    if int(chnno) in config.GLOBAL_SETTINGS.get("channel_adaptation_list", []):
                        if not i["chnadapt"].find(",%s," % chnno) >= 0:
                            continue

                    # 按渠道分发
                    if i["issue_type"] == 2:
                        if not i["chnnos"].find(",%s," % chnno) >= 0:
                            continue

                    # 架构适配
                    if int(chnno) in config.GLOBAL_SETTINGS.get("architecture_check_channel", []):
                        if architecture > 0:
                            if i["architecture"] & architecture == 0:
                                continue

                    # match 100%
                    if (i["show_name"].find(search_key.lower()) >= 0 or i["search_key"].find(search_key.lower()) >= 0) or i["pinyin"].find(search_key.lower()) >= 0 :
                        all_match_app_infos.append(i["appid"])

                    # part match
                    else:
                        seg_list = jieba.cut(search_key, cut_all=True)  # 全模式
                        to_match_list =  [j for j in list(seg_list) if j.strip()]
                        match_time = 0

                        for x in xrange(len(to_match_list)):
                            if i["show_name"].find(to_match_list[x].lower()) >= 0:
                                match_time = match_time + 1

                        if match_time == len(to_match_list) and match_time > 0:
                            all_match_app_infos.append(i["appid"])

                        elif match_time > 0:
                            part_match_app_infos.append(i["appid"])

            match_app_infos = all_match_app_infos + part_match_app_infos

            start_index = (page_index - 1) * page_size
            end_index = start_index + page_size
            appinfo_data = m_appinfo.get_appinfos(match_app_infos[start_index:end_index])

            group_elems_proto = proto.GroupElems()

            #for appid in match_app_infos:

            for i in xrange(len(match_app_infos)):
                appid = match_app_infos[i]
                for appinfo in appinfo_data:
                    if int(appinfo["appid"]) == int(appid):
                        _appinfo2eleminfo(group_elems_proto, appinfo, i + 1)
                        break

            #for i, v in enumerate(appinfo_data):
            #    _appinfo2eleminfo(group_elems_proto, v, i + 1)

            return group_elems_proto.SerializeToString()
        return ""

    def gen_src_key_info(self):
        """ 生成关键字的源数据
        """
        select_sql = """
                    select
                        lower(a.SearchKeys) as search_key,
                        a.DownTimes as down_times,
                        a.UpdateTime as update_time,
                        ifnull(at.AppClass,0) as app_class,
                        ifnull(at.AppType,0) as app_type,
                        a.AppID as appid,
                        lower(a.ShowName) as show_name,
                        a.IssueType as issue_type,
                        a.ChannelNos as chnnos,
                        a.ChannelAdaptation as chnadapt,
                        a.Architecture as architecture
                    from
                        appinfo a
                            inner join
                        packinfo p ON a.AppID = p.AppID
                            and a.MainPackID = p.PackID
                            and a.DataStatus = 1
                            and a.Status = 1
                            and p.Status = 1
                            left join
                        apptypes at ON at.AppType = a.AppType
                    order by down_times desc
        """

        with MysqlConnection(config.GLOBAL_SETTINGS["db"]) as mysql:
            # 初始化拼音库
            pinyin = PinYin()
            pinyin.load_word()

            # 查询按下载量排序的关键字信息
            self.src_search_info[DBOrderTypeEnum.AUTO] = mysql.query(select_sql)

            # 生成拼音
            self.src_search_info[DBOrderTypeEnum.AUTO] = [i.update(dict(pinyin=pinyin.hanzi2pinyin_merge(unicode(i["show_name"])))) or i
                                                            for i in self.src_search_info[DBOrderTypeEnum.AUTO] if i]

            # 生成按更新时间排序的关键字信息
            self.src_search_info[DBOrderTypeEnum.UPDATE_TIME] = sorted(
                self.src_search_info[DBOrderTypeEnum.AUTO],
                key=lambda x: x["update_time"],
                reverse=True)
                

SearchInfo = _SearchInfo()
