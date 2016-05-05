#!/bin/env python
#-*- encoding=utf8 -*-
import os
import sys

import tornado.ioloop
import tornado.web
import tornado.autoreload

from datetime import datetime


ROOT_PATH = os.path.dirname(os.path.realpath(__file__))

sys.path.append(ROOT_PATH)

import config

def load_config():
    config.GLOBAL_SETTINGS['root_path'] = ROOT_PATH
    config.TORNADO_SETTINGS['static_path'] = ROOT_PATH + config.TORNADO_SETTINGS['static_path']

    if 'load_global' in config.GLOBAL_SETTINGS and config.GLOBAL_SETTINGS['load_global']:
        import imp
        global_config = imp.load_source('global_config', os.path.join(ROOT_PATH, '..', 'global_config.py'))
        # sys.path.append(os.path.join(ROOT_PATH, '..'))
        # import global_config
        # sys.path.pop()
        global_setting = global_config.GLOBAL_SETTINGS_ALL
        for each_key in global_setting:
            if isinstance(global_setting[each_key], dict):
                if each_key in config.GLOBAL_SETTINGS:
                    for each_key_inside in global_setting[each_key]:
                        if each_key_inside not in config.GLOBAL_SETTINGS[each_key]:
                            config.GLOBAL_SETTINGS[each_key][each_key_inside] = global_setting[each_key][each_key_inside]
                else:
                    config.GLOBAL_SETTINGS[each_key] = global_setting[each_key]
            else:
                if each_key not in config.GLOBAL_SETTINGS:
                    config.GLOBAL_SETTINGS[each_key] = global_setting[each_key]

def init():
    """可以放初始化相关操作
    """
    import base_utils.logger as logger
    logger.GlobalLogger(config.GLOBAL_SETTINGS['logger'])


def run(port=config.GLOBAL_SETTINGS['default_port']):
    # pdb.set_trace()
    load_config()
    import routes
    init()
    
    application = tornado.web.Application(routes.URL_PATTERN, **config.TORNADO_SETTINGS)
    application.listen(port)
    print port
    ioloop = tornado.ioloop.IOLoop.instance()
    ioloop.start()


def run_test():
    return True


if __name__ == "__main__":
    if run_test():
        if len(sys.argv) > 1:
            run(int(sys.argv[1]))
        else:
            run()
