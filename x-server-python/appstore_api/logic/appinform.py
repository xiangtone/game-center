#!bin/env python
#-*- encoding=utf8 -*-
#
# 创建人：wind
# 
# 时  间：20150831
#
# 描  述：应用举报


import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet

from const import CommStatusCode
import model.appinform as inform_db

  

class ReqAppInform(ProtoBase):

    """应用举报
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:  
            req = self.request  # 请求的对象 
            appId = req.appId   # 应用ID
            openId = self.comm_params.get('oi','')             # 获取用户openid
            clientId = self.comm_params["clientid"]             # ClientId 
            version = self.comm_params['clientver']            # 版本号
            brandFlag = self.comm_params.get('bd','')             # 来源品牌 
            modelFlag = self.comm_params.get('md','')             # 来源机型
            deviceFlag = self.comm_params.get('de','')             # 来源子机型
            level = self.comm_params.get('pl',0)             # 平台Level 
            tempArr = version.split('.')  # clientver  渠道号  2015.06.26.11  后面11为渠道号，
            channelNo = int(tempArr[-1])    # 取最后两位是渠道号 
            informType = req.informType      # 举报类型，1=强制广告，2=无法安装，3=质量不好，4=版本旧，5=恶意扣费，6=携带病毒，多个以逗号分隔
            informDetail = req.informDetail  # 更多描述(限200字符)
            imei = self.comm_params['ei']+self.comm_params['ai']
             
            log = "ReqAppInform request : appId=%s, informType=%s ,informDetail=%s" % (appId,informType,informDetail)
            self.logger.write(log)
            params = 1

            if not informType and not informDetail:
                params = 0

            if appId and params:
                inform = {}
                inform['openid'] = openId
                inform['imei'] = imei
                inform['appid'] = appId
                inform['clientid'] = clientId
                inform['version'] = version
                inform['brandflag'] = brandFlag
                inform['modelflag'] = modelFlag
                inform['deviceflag'] = deviceFlag
                inform['level'] = level
                inform['channelno'] = channelNo
                inform['informtype'] = informType
                inform['informdetail'] = informDetail

                result = 0
                # 判断举报次数是否一天超过十次
                inform_count = inform_db.set_appinform_cache(imei,openId) 
                if not inform_count: 
                    result = inform_db.add_app_inform(inform)
                else: 
                    result = 1

                    log = "ReqAppInform response : inform too more"  
                    self.logger.write(log)

                if result:
                    self.response.rescode = 0
                    self.response.resmsg = u'成功'
                else:
                    self.response.rescode = 1
                    self.response.resmsg = u'失败' 
            else:
                self.response.rescode = 2
                self.response.resmsg = u'信息不能为空'
         
        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqAppInform', ex)
            
            self.response.rescode = CommStatusCode.exception_error
            self.response.resmsg = CommStatusCode.exception_error_msg
 
        self.callback()

