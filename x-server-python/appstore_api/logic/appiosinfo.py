#!/bin/env python
#-*- encoding=utf8 -*-

import base_utils.cache as cache

import tornado.gen
import base_utils.db as db
import json
import config


# iosApp信查寻息
def get_app_list():
	"""
	获取所有应用列表
	"""
	app_list = None
	app_list_dict = None

	#从缓存取
	cache_name = config.GLOBAL_SETTINGS["cache_prefix"] + "ios_app_list"
	ch = cache.Cache(config.GLOBAL_SETTINGS['redis'])
	app_list = ch.get(cache_name)

	if app_list:
		app_list_dict = json.loads(app_list)
	else:
			with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:
			# con = MySQLdb.connect(
			# 	host = 'localhost',
			# 	port = 3306,
			# 	user = 'root',
			# 	passwd = 'oddshou',
			# 	charset = 'utf8',
			# 	db = 'appstoreios'
			# 	)
			# cur = con.cursor(cursorclass=MySQLdb.cursors.DictCursor)
			# cur.execute("SELECT PackUrl,ShowName,UpdateDesc FROM packinfo limit 0, 10")
			# sql="SELECT * FROM applistios limit 0, 20"
			# app_list_dict = cur.fetchall()
			#app_list = json.dumps(app_list_dict)

			#参照项目整合部分
			sql="SELECT * FROM applistios limit 0,20"
			app_listcon=con.query(sql)
			#设置缓存
			ch.set(cache_name, app_list)
			ch.expire(cache_name, 5*60)
	return app_list
