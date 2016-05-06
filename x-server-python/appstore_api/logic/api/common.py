#!/bin/env python
#-*- encoding=utf8 -*-

import config
import proto.Apps_pb2 as apps_proto
import libs.pbjson as pbjson


def get_channelno_from_clientver(client_ver):
    """获取渠道号
    """
    default_channel = config.GLOBAL_SETTINGS["web_default_channel"]
    if client_ver:
        return client_ver.split('.')[-1]
    return default_channel

def convert_seridata2dict(seri_data, proto_message_name):
    """proto协议序列化后的数据转换为协议对应的dict格式
    """
    proto_cls = getattr(apps_proto, proto_message_name)
    if proto_cls:
        proto_model = proto_cls()
        proto_model.ParseFromString(seri_data)
        return pbjson.pb2dict(proto_model)
