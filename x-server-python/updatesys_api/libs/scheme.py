#!/bin/env python
#-*- encoding=utf8 -*-
#
# Copyright 2013 PADA
#
# 作者：zone
#
# 功能：该模块提供方案相关功能
#
# 版本：V1.0.0
#
import base_utils.logger as logger
import base_utils.cache as cache
import config

def get_scheme_id(comm_params):
    """获取方案Id
    """
    ei = comm_params['ei'] if 'ei' in comm_params else ''
    ai = comm_params['ai'] if 'ai' in comm_params else ''
    wm = comm_params.get('wm', '')

    # unique_id = ei + '|' + ai
    c = cache.Cache(config.GLOBAL_SETTINGS['redis'])

    # logger.GlobalLogger().info('debug: wm=' + wm)
    
    test_scheme_list = c.smembers("test_scheme")
    if wm in test_scheme_list:
        return 1  # 测试方案
    else:
        return 0