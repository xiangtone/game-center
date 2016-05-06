#!/bin/env python
#-*- encoding=utf8 -*-
import urllib2

import proto.Packet_pb2 as packet
import proto.test_pb2 as totest


def post(url, data):
    """发送HTTP请求
    """
    try:
        f = urllib2.urlopen(url=url, data=data)
        #print f
        return f.read()
    except:
        return None

# ===============================================================
#
#    Google Buffer
#

reqNo = 0
mask = 0
udi = 'ei=352272016759242'

action = 'ReqTest'
inner_pack = totest.ReqTest()
inner_pack.name = 'zhicong'
# inner_pack.age = 25
param = inner_pack.SerializeToString()
# param2 = inner_pack.SerializeToString()

pack = packet.ReqPacket()
pack.mask = mask
pack.udi = udi
pack.reqNo = 0
pack.clientId = 111
pack.action.append(action)
pack.params.append(param)

http_body = pack.SerializeToString()

res = post('http://127.0.0.1:8090/api', http_body)
res_pack = packet.RspPacket()
res_pack.ParseFromString(res)

if res_pack.rescode == 0:
    res_inner_pack = totest.RspTest()
    res_inner_pack.ParseFromString(res_pack.params[0])
    print res_inner_pack.rescode
    print res_inner_pack.resmsg
    if res_inner_pack.HasField('exmsg'):
        print "exmsg=" + str(res_inner_pack.exmsg)
else:
    print res_pack.rescode
    print res_pack.resmsg

# ===============================================================
#
#    JSON
#
# data = """{
#    "reqUpdate": {
#         "packName": "com.pcsuite.exe",
#         "verName": "5.01.00",
#         "verCode": 0
#     }
# }"""

# # res = post('http://127.0.0.1:8091/jsonapi', data)
# res = post('http://192.168.1.12/updater/jsonapi', data)
# print '%s' % res
