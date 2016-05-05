#!/bin/env python
#-*- encoding=utf8 -*-
import proto.Apps_pb2 as apps
import proto.Packet_pb2 as packet


def get_test_post_data():
    """获取模拟的测试数据
    """

    #import libs.pinyin as pinyin
    #py_module = pinyin.PinYin()
    #py_module.load_word()


    req_pack = packet.ReqPacket()
    req_pack.mask = 1
    req_pack.udi = 'ei=9870&ui=5&ai=1&wm=abcde&si=154565&mf=colorful&bd=onda&pv=11'
    req_pack.clientId = 13
    req_pack.clientVer = '4.09.25.90'


    ## 填充内部包  订单通知状态
    #request = proto_packet.ReqAppInform() 
    #request.appId = 12  
    #request.informType = '1,2'  
    #request.informDetail = ''  
    #req_pack.action.append('ReqAppInform')
    #req_pack.params.append(request.SerializeToString())


    global_config = apps.ReqGlobalConfig()
    global_config.groupsCacheVer = ''
    req_pack.action.append('ReqGlobalConfig')
    req_pack.params.append(global_config.SerializeToString())

    group_elem = apps.ReqGroupElems()
    group_elem.groupId = 101
    group_elem.pageSize = 30
    group_elem.pageIndex = 1
    req_pack.action.append('ReqGroupElems')
    req_pack.params.append(group_elem.SerializeToString())

    group_elem = apps.ReqGroupElems()
    group_elem.groupId = 93
    group_elem.pageSize = 30
    group_elem.pageIndex = 1
    req_pack.action.append('ReqGroupElems')
    req_pack.params.append(group_elem.SerializeToString())

    group_elem = apps.ReqGroupElems()
    group_elem.groupId = 100
    group_elem.pageSize = 30
    group_elem.pageIndex = 1
    req_pack.action.append('ReqGroupElems')
    req_pack.params.append(group_elem.SerializeToString())

    # apps_update = apps.ReqAppsUpdate()
    # #apps_update.checkUpdateType = 2
    # local = apps_update.localAppVer.add()
    # local.packName = 'com.teatimesgames.snowball'
    # local.verName = '0.1'
    # local.verCode = 1
    # local.signCode = ''
    # req_pack.action.append('ReqAppsUpdate')
    # req_pack.params.append(apps_update.SerializeToString())

    #app_search = apps.ReqAppList4SearchKey()
    #app_search.SearchKeyStr = u'百度输入法'
    #app_search.appClass = 0
    #app_search.appType = 0
    #app_search.isHotKey = 0
    #app_search.pageSize = 20
    #app_search.pageIndex = 1
    #app_search.orderType = 0
    #req_pack.action.append('ReqAppList4SearchKey')
    #req_pack.params.append(app_search.SerializeToString())

    #user_apps =  apps.ReqUserApps()
    #local =  user_apps.localAppVer.add()
    #local.packName = 'com.android.GameLua.pada'
    #local.verName = '1.6'
    #local.verCode = 1
    #req_pack.action.append('ReqUserApps')
    #req_pack.params.append(user_apps.SerializeToString())

    #down_res = apps.ReqDownRes()
    #down_res.appId = 101459
    #down_res.packId = 101459
    #down_res.downloadRes = 0
    #req_pack.action.append('ReqDownRes')
    #req_pack.params.append(down_res.SerializeToString())

    #app_info = apps.ReqAppInfo()
    #app_info.appId = 101469
    #app_info.packId = 0
    #req_pack.action.append('ReqAppInfo')
    #req_pack.params.append(app_info.SerializeToString())

    # 封包
    req_pack.reqNo = 1
    return req_pack.SerializeToString()
