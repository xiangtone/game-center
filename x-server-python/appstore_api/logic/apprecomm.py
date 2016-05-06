#!bin/env python
#-*- encoding=utf8 -*-
#
# 创建人：wind
# 
# 时  间：20150831
#
# 描  述：应用推荐 


import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet
from model.const import ApiSeriRecommCacheName
import model.apprecomm as m_apprecomm
import common 
from const import CommStatusCode


def req_recommapp_common(appid, channel_no, client_cache_ver='', app_type='', order_type='', architecture=0):
    """ 协议通用调用方法
        返回：(是否最新版本，服务器当前版本，序列化后的数据)
        获取推荐应用
    """
    seri_name = ApiSeriRecommCacheName.SERI_RECOMMEND_INFO % (architecture,channel_no,appid)
    gen_func = m_apprecomm.get_recommend_info
    rsp_data = common.get_cache_comm(seri_name, client_cache_ver, gen_func, appid, app_type, order_type, channel_no, architecture,seri_name)
    return rsp_data


class ReqRecommApp(ProtoBase):

    """应用推荐
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:    
            req = self.request  # 请求的对象 
            appId = req.appId   # 应用ID
            appClass = req.appClass  # 应用类别
            appType = req.appType   # 应用类型
            pageSize = req.pageSize  # 页尺寸
            pageIndex = req.pageIndex  # 页码 (从1开始)
            orderType = req.orderType  # 排序类型：0=自动-热门，2=按时间
            clientCacheVer = req.clientCacheVer  # 终端缓存版本：如果有缓存则上传，下发时可省流量，加速度
                
            version = self.comm_params['clientver']            # 版本号  
            tempArr = version.split('.')  # clientver  渠道号  2015.06.26.11  后面11为渠道号，
            channelNo = tempArr[-1]   # 取最后两位是渠道号  

            # log = u"ReqRecommApp request : appId=%s " % (appId)
            # self.logger.write(log)

            if appId:
                # 2015.11.05注释，修改调用公共方法
                # 序列化后的缓存名
                # seri_name = ApiSeriRecommCacheName.SERI_RECOMMEND_INFO % appId
                # gen_func = m_apprecomm.get_recommend_info
                # rsp_data = common.get_cache_comm(seri_name, clientCacheVer, gen_func, appId,appType,orderType,channelNo)

                architecture = common.match_architecture(self.comm_params)
                rsp_data = req_recommapp_common(appId, channelNo, clientCacheVer, appType, orderType, architecture)
 
                self.response.rescode = 0
                self.response.resmsg = u'成功'
                self.response.serverDataVer = rsp_data[1] # 服务端数据版本，如果跟请求的版本一致，代表客户端已经是最新版本
                self.response.groupElems = rsp_data[2]      # 分组元素信息

                # log = u"ReqRecommApp rsponse  ok " 
                # self.logger.write(log)   
            else:
                self.response.rescode = 2
                self.response.resmsg = u'信息不能为空'
             
        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqRecommApp', ex)

            self.response.rescode = CommStatusCode.exception_error
            self.response.resmsg = CommStatusCode.exception_error_msg
 
        self.callback()