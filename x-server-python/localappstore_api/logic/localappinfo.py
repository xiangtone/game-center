#!/bin/env python
# -*- encoding=utf8 -*-

import base_utils.cache as cache

import tornado.gen
import base_utils.db as db
import json
import config


#App所有信查寻息
@tornado.gen.coroutine
def get_app_list():
    """
    获取所有应用列表
    """
    app_list = None
    app_list_dict = None

    # 从缓存取
    cache_name = config.GLOBAL_SETTINGS["cache_prefix"] + "local_app_list"
    ch = cache.Cache(config.GLOBAL_SETTINGS['redis'])
    app_list = ch.get(cache_name)
    #app_list =None
    if app_list:
        app_list_dict = json.loads(app_list)
    else:
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:

            # 参照项目整合部分
            sql = "SELECT prs.SiteID,prs.AppID,prs.SubwayPackUrl from v_pack_site_relation prs;"
            app_list_dict = con.query(sql)
            # 设置缓存
            ch.set(cache_name, json.dumps(app_list_dict))
            ch.expire(cache_name, 5 * 60)
    raise tornado.gen.Return(app_list_dict)


# app单条信息
@tornado.gen.coroutine
def get_app_item(appid):
    """
    获取所有应用列表
    """
    app_list = None
    app_list_dict = None

    # 从缓存取
    cache_name = config.GLOBAL_SETTINGS["cache_prefix"] + "local_app_list"
    ch = cache.Cache(config.GLOBAL_SETTINGS['redis'])
    app_list = ch.get(cache_name)

    if app_list:
        app_list_dict = json.loads(app_list)
    else:
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:

            # 参照项目整合部分
            sql = "SELECT prs.SiteID,prs.AppID,prs.SubwayPackUrl from v_pack_site_relation prs where AppID=%s"
            app_list_dict = con.query(sql,appid)
            # 设置缓存
            ch.set(cache_name, json.dumps(app_list_dict))
            ch.expire(cache_name, 5 * 60)
    for item in app_list_dict:
        if item['AppID'] == int(appid):
            break
    raise tornado.gen.Return(item)

# # 测试
# if __name__ == "__main__":
#      get_app_list()
#      get_app_item(2)