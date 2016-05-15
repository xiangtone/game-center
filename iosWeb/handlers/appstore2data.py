#!/bin/env python
#-*- encoding=utf8 -*-

import nwbase_utils.cache as cache

import tornado.gen
import MySQLdb
import json
import config

# @tornado.gen.coroutine
# def get_group_list():
#     """
#     获取所有的分组信息
#     5分钟刷新一次
#     """
#     cache_name = config.GLOBAL_SETTINGS["cache_prefix"] + "group_list"
#     ch = cache.Cache(config.GLOBAL_SETTINGS['redis'])
#     group_list = ch.get(cache_name)
#     result = []
#     if group_list:
#         result = json.loads(group_list)
#     else:
#         body_data = dict(api="ReqGlobalConfig", params={})
#         url = config.GLOBAL_SETTINGS["jsonapi_url"]
#         http_client = AsyncHTTPClient()
#         request = HTTPRequest(url=url, method="POST", body=json.dumps(body_data))
#         rsp_data = yield http_client.fetch(request)
#         #result_content = result.body.decode("utf-8")
#         if rsp_data:
#             json_data = json.loads(rsp_data.body)
#             if json_data and 'data' in json_data and 'groupInfo' in json_data['data']:
#                 result = json_data['data']['groupInfo']

#                 # 写缓存并设置过期时间
#                 ch.set(cache_name, json.dumps(result))
#                 ch.expire(cache_name, 5*60)
#                
#     raise tornado.gen.Return(result)

@tornado.gen.coroutine
def get_app_list():
	"""
	获取所有应用列表
	"""
	app_list = None
	app_list_dict = None

	cache_name = config.GLOBAL_SETTINGS["cache_prefix"] + "ios_app_list"
	ch = cache.Cache(config.GLOBAL_SETTINGS['redis'])
	app_list = ch.get(cache_name)

	if app_list:
		app_list_dict = json.loads(app_list)
	else:
		# 查询数据库获取数据
		con = None

		try:
			con = MySQLdb.connect(
				host = 'localhost',
				port = 3306,
				user = 'root',
				passwd = 'oddshou',
				charset = 'utf8',
				db = 'appstoreios'
				)
			
			cur = con.cursor(cursorclass=MySQLdb.cursors.DictCursor)
			# cur.execute("SELECT PackUrl,ShowName,UpdateDesc FROM packinfo limit 0, 10")
			cur.execute("SELECT * FROM applistios limit 0, 20")
			app_list_dict = cur.fetchall()
			app_list = json.dumps(app_list_dict)
			ch.set(cache_name, app_list)
			ch.expire(cache_name, 5*60)
		except Exception, e:
			# import nwbase_utils.logger as logger
			# logger = logger.GlobalLogger(config.GLOBAL_SETTINGS['logger'])
			# logger.info('write_file_info_excpetion: %s' % e);
			raise e
		finally:
			if con:
				con.close()

	raise tornado.gen.Return(app_list_dict)

# @tornado.gen.coroutine
# def get_app_detail(appid):
# 	"""
# 	获取所有应用列表
# 	"""
# 	app_list = None
# 	app_list_dict = None

# 	cache_name = config.GLOBAL_SETTINGS["cache_prefix"] + appid
# 	ch = cache.Cache(config.GLOBAL_SETTINGS['redis'])
# 	app_list = ch.get(cache_name)

# 	if app_list:
# 		app_list_dict = json.loads(app_list)
# 	else:
# 		# 查询数据库获取数据
# 		con = None

# 		try:
# 			con = MySQLdb.connect(
# 				host = 'localhost',
# 				port = 3306,
# 				user = 'root',
# 				passwd = 'oddshou',
# 				charset = 'utf8',
# 				db = 'appstoreios'
# 				)
			
# 			cur = con.cursor(cursorclass=MySQLdb.cursors.DictCursor)
# 			# cur.execute("SELECT PackUrl,ShowName,UpdateDesc FROM packinfo limit 0, 10")
# 			cur.execute("SELECT * FROM applistios limit 0, 10")
# 			app_list_dict = cur.fetchall()
# 			app_list = json.dumps(app_list_dict)
# 			ch.set(cache_name, app_list)
# 			ch.expire(cache_name, 5*60)
# 		except Exception, e:
# 			# import nwbase_utils.logger as logger
# 			# logger = logger.GlobalLogger(config.GLOBAL_SETTINGS['logger'])
# 			# logger.info('write_file_info_excpetion: %s' % e);
# 			raise e
# 		finally:
# 			if con:
# 				con.close()

# 	raise tornado.gen.Return(app_list_dict)