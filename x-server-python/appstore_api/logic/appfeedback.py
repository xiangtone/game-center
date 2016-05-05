#!bin/env python
#-*- encoding=utf8 -*-
#
# 创建人：wind
#
# 时  间：20150901
#
# 描  述：用户反馈
#
# 修改时间：20150902
#
# 修改描述: 
# 


import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet
 
from const import CommStatusCode
import model.appfeedback as feedback_db

class ReqFeedback(ProtoBase):

    """用户反馈
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:  

            req = self.request  # 请求的对象  
            openId = self.comm_params.get('oi','')             # 获取用户openid
            clientId = self.comm_params["clientid"]             # ClientId 
            version = self.comm_params['clientver']            # 版本号 
            brandFlag = self.comm_params.get('bd','')             # 来源品牌 
            modelFlag = self.comm_params.get('md','')             # 来源机型
            deviceFlag = self.comm_params.get('de','')             # 来源子机型
            tempArr = version.split('.')  # clientver  渠道号  2015.06.26.11  后面11为渠道号，
            channelNo = int(tempArr[-1])    # 取最后两位是渠道号  
            content = self.request.feedBackContent   # 反馈的内容
            userContact = self.request.userContact  # 用户的联系方式
            imei = self.comm_params['ei']+self.comm_params['ai']

            log = "ReqFeedback request : Content=%s, userContact=%s " % (content,userContact)
            self.logger.write(log)
   
            if content:
                # 添加反馈信息
                result = feedback_db.add_app_feedback(openId,imei,content,userContact,clientId,channelNo,version,brandFlag,modelFlag,deviceFlag)
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
            self.logger.write('ReqFeedback', ex)

            self.response.rescode = CommStatusCode.exception_error
            self.response.resmsg = CommStatusCode.exception_error_msg
 
        self.callback()


