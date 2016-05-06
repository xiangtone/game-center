#!/bin/env python
#-*- encoding=utf8 -*-
import json
import urllib
from datetime import datetime, date
from time import struct_time, strftime
import sys


def urldecode(query):
    """url解码，string -> dict
    """
    d = {}
    a = query.split('&')
    for s in a:
        if s.find('=') >= 0:
            k, v = map(urllib.unquote, s.split('=')[:2])
            d[k] = v
    return d


class _MyJSONEncoder(json.JSONEncoder):
    """JSON编码器，为默认的json编码器增加datetime类型支持
    """
    def default(self, obj):
        if isinstance(obj, struct_time):
            return strftime('%Y%m%d%H%M%S', obj)
        elif isinstance(obj, datetime):
            return obj.strftime('%Y%m%d%H%M%S')
        elif isinstance(obj, date):
            return obj.strftime('%Y%m%d')
        else:
            return json.JSONEncoder.default(self, obj)

MyJSONEncoder = _MyJSONEncoder()


class TEnum:
    """枚举类，增加枚举类型支持"""
    def __init__(self, arguments):
        self._keys = []
        self._vals = []
        idx = 0
        val = idx
        for name in arguments:
            if arguments[name] is None:
                val = idx
            else:
                val = arguments[name]
            self._keys.append(name)
            self._vals.append(val)
            setattr(self, name.strip(), val)
            idx = idx + 1

    def has_key(self, key_):
        return (key_ in self._keys)

    def has_val(self, val_):
        return (val_ in self._vals)


def list_to_dict(list_, key=None):
    """序列转换为字典
    """
    res = {}
    index = 0
    for eachItem in list_:
        if key is None:
            res[index] = eachItem
            index += 1
        else:
            res[eachItem[key]] = eachItem
    return res


class ConfigObj(object):
    pass


def parser_config(module_):
    """将模块转换为对象返回
    """
    all_config = module_.__dict__
    res = ConfigObj()
    for each_key in all_config:
        each_val = all_config[each_key]
        if each_key.startswith('_'):
            continue
        if isinstance(each_val, dict):
            inner_config = ConfigObj()
            for each_inner_key in each_val:
                setattr(inner_config, each_inner_key, each_val[each_inner_key])
            setattr(res, each_key, inner_config)
        else:
            setattr(res, each_key, all_config[each_key])
    return res

def convert_str_to_time(name):
    """把'yyyy-MM-dd HH:mm:ss'
    格式的字符串转为时间类型
    """
    return datetime.strptime(name, '%Y-%m-%d %H:%M:%S')

def convert_time_to_timever(time):
    """把时间转成yyyyMMddHHss的时间版本
    """
    return time.strftime("%Y%m%d%H%M%S") if isinstance(
           time, datetime) else convert_str_to_time(time).strftime("%Y%m%d%H%M%S")