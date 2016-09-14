#!/bin/env python
#-*- encoding=utf8 -*-
import json
import urllib
import hashlib
from datetime import datetime, date
from time import struct_time, strftime
import sys
import os



# def write_log(msg, ex=None):
#     """写日志（暂时打印在屏幕上）
#     """
#     # time_str = strftime('%Y-%m-%d %H:%M:%S')
#     # if ex == None:
#     # log_msg = '%s %s'%(time_str, msg)
#     # else:
#     # log_msg = '%s [Exception] %s %s'%(time_str, msg, str(ex))
#     # print log_msg
#     time_str = strftime('%Y-%m-%d %H:%M:%S')
#     if ex is None:
#         log_msg = '%s %s' % (time_str, msg)
#     else:
#         log_msg = '%s [Exception] %s %s' % (time_str, msg, str(ex))
#     old_stdout = sys.stdout
#     log_file = open('log.log', 'a')
#     sys.stdout = log_file
#     print log_msg
#     sys.stdout = old_stdout
#     log_file.close()
# write_log = logger.write_log

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


def list_to_dict(list_, key=None, key2int=False):
    """序列转换为字典
    """
    res = {}
    index = 0
    for eachItem in list_:
        if key is None:
            res[index] = eachItem
            index += 1
        else:
            if key2int:
                res[int(eachItem[key])] = eachItem
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

def str2time(name):
    """把'yyyy-MM-dd HH:mm:ss'
    格式的字符串转为时间类型
    """
    return datetime.strptime(name, '%Y-%m-%d %H:%M:%S')

def time2ver(time):
    """把时间转成yyyyMMddHHss的时间版本
    """
    return time.strftime("%Y%m%d%H%M%S") if isinstance(time, datetime) else str2time(time).strftime("%Y%m%d%H%M%S")
#大文件的MD5值
def getfile_md5(filename):
    if not os.path.isfile(filename):
        return
    hashstr=hashlib.md5()
    f=file(filename,'rb')
    while True:
        b=f.read(2*2048)
        if not b:
            break
        hashstr.update(b)
    f.close()
    return hashstr.hexdigest()

#普通文件的MD5值
def calc_md5(filepath):
    with open(filepath,'rb') as f:
        md5obj = hashlib.md5()
        md5obj.update(f.read())
        hash = md5obj.hexdigest()
        print(hash)
        return hash

#普通文件的sh1值
def calc_sha1(filepath):
    with open(filepath, 'rb') as f:
        sha1obj = hashlib.sha1()
        sha1obj.update(f.read())
        hash = sha1obj.hexdigest()
        print(hash)
        return hash

def md5_str(word):
    """MD5
    """
    result = word
    if isinstance(word, unicode):  
        result = word.encode("utf-8")  
    elif not isinstance(word, str):  
        result = str(word)  
    m = hashlib.md5()  
    m.update(result)

    return m.hexdigest() 

def get_log_session(tornado_ins):
    """获取log日志的session
    """
    try:
        return tornado_ins.request.headers['X-Log-Session']
    except:
        return 0l

def get_real_ip(tornado_ins):
    """获取真实ip地址
    """
    try:
        return tornado_ins.request.headers['X-Real-Ip']
    except:
        return tornado_ins.request.remote_ip

