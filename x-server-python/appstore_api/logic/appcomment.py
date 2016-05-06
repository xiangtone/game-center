#!bin/env python
#-*- encoding=utf8 -*-
#
# 创建人：wind
# 
# 时  间：20150831
#
# 描  述：应用评论和评分


import tornado.gen
from protohandler import ProtoBase
import proto.Apps_pb2 as proto_packet

from const import CommStatusCode
import model.appcomment as model_comment



class ReqAddComment(ProtoBase):

    """用户请求添加或更新评论
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:   
            req = self.request  # 请求的对象
            userName = req.userName   # 用户名
            appId = req.appId   # 应用ID
            userScore = req.userScore   # 用户评分
            userVerCode = req.userVerCode   # 用户本地版本代码
            userVerName = req.userVerName   # 用户本地版本名称
            comments = req.comments   # 评论内容
            commentId = req.commentId   # 评论ID，用于更新评论时填写  
            openId = self.comm_params.get('oi','')    # 获取用户openid 
            brandFlag = self.comm_params.get('bd','') # 来源品牌 
            modelFlag = self.comm_params.get('md','') # 来源机型
            deviceFlag = self.comm_params.get('de','')# 来源子机型
            ip = self.comm_params.get('ip','127.0.0.1')  # 终端ip      
            
            ext2 = 0  
            ext3 = 0 

            # 用户名为空时，显示设备的品牌和机型
            if userName=="":
                userName = "%s_%s" % (brandFlag,modelFlag)
 
            log = u"ReqAddComment request : userName=%s, appId=%s " % (userName,appId)
            self.logger.write(log)

            # 仅当没有登录功能，进行评星的时候启用
            if not openId:
                openId = "hs_" + self.comm_params.get('ei','') + self.comm_params.get('ai','')
                openId = openId[:50]
 
            if openId:
                if appId: 
                    operation = -1  # 0-评分，1-评论，-1-缺失参数

                    # 0代表无评分，1~5代表从差评到好评
                    if comments.strip():
                        # 用户评论
                        operation = 1 
                    else:
                        if userScore!=0:
                            # 用户评分
                            operation = 0   
                    
                    # 封装评分和评论对象
                    comment = {} 
                    comment['commentid'] = commentId
                    comment['openid'] = openId    
                    comment['username'] = userName   
                    comment['appid'] = appId   
                    comment['userscore'] = userScore   
                    comment['vercode'] = userVerCode   
                    comment['vername'] = userVerName   
                    comment['comments'] = comments      
                    comment['brand'] = brandFlag   
                    comment['model'] = modelFlag   
                    comment['device'] = deviceFlag   
                    comment['ip'] = ip  

                    if operation==0: 
                        # 评分操作 
                        ext2 = 1
                        result = model_comment.add_app_score(comment)

                        log = u"ReqAddComment response : result=%s " % result 
                        self.logger.write(log)

                        if result: 
                            self.response.rescode = 0
                            self.response.resmsg = u'成功'
                            self.response.commentId = result  # 评论ID
                            self.response.userName = userName   # 评论用户的用户名（品牌名+机型）
                        else:
                            self.response.rescode = 1
                            self.response.resmsg = u'失败'
                    elif operation==1:
                        # 评论操作
                        ext3 = 1
                        
                        self.response.rescode = 1
                        self.response.resmsg = u'失败' 

                        log = u"ReqAddComment response : operation==1 " 
                        self.logger.write(log)
                    else:
                        # 参数缺失 
                        self.response.rescode = 2
                        self.response.resmsg = u'信息不能为空'
                        
                        log = u"ReqAddComment response : fail... " 
                        self.logger.write(log)
                else:
                    self.response.rescode = 2
                    self.response.resmsg = u'信息不能为空'
            else:
                # 非法用户/用户token验证失败
                self.response.rescode = CommStatusCode.illegal_user 
                self.response.resmsg = CommStatusCode.illegal_user_msg 

            # 统计信息 
            if self.response.rescode==0:
                self.comm_params.update(dict(restype=str(1),resid=str(appId),ext2=str(ext2),ext3=str(ext3)))

        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqAddComment', ex)

            self.response.rescode = CommStatusCode.exception_error
            self.response.resmsg = CommStatusCode.exception_error_msg
 
        self.callback()




class ReqUserScoreInfo(ProtoBase):

    """用户评分
    """

    def __init__(self):
        ProtoBase.__init__(self, proto_packet)

    @tornado.gen.coroutine
    def deal(self):
        try:     
            appId = self.request.appId   # 应用ID
            openId = self.comm_params.get('oi','')    # 获取用户openid 
            
            log = u"ReqUserScoreInfo request : appId=%s, openId=%s " % (appId,openId)
            self.logger.write(log)

            # 仅当没有登录功能，进行评星的时候启用
            if not openId:
                openId = "hs_" + self.comm_params.get('ei','') + self.comm_params.get('ai','')
                openId = openId[:50]

            if appId: 
                # 获取应用的评分信息 
                score_obj = model_comment.get_app_sum_score(appId)
                if score_obj:
                    comment_info = model_comment.get_app_user_score(appId,openId) 
                    app_score_str = model_comment.get_app_score_list_proto(score_obj,comment_info)
 
                    self.response.rescode = 0
                    self.response.resmsg = u'成功'
                    self.response.userScoreInfo = app_score_str  

                    log = u"ReqUserScoreInfo response : ok " 
                    self.logger.write(log)
 
                else: 
                    self.response.rescode = 0
                    self.response.resmsg = u'成功' 
                    self.response.userScoreInfo = ''   

                    log = u"ReqUserScoreInfo response : ok 2 " 
                    self.logger.write(log)
            else:
                self.response.rescode = 2
                self.response.resmsg = u'信息不能为空'
                self.response.userScoreInfo = ''   # 评论ID 

                log = u"ReqUserScoreInfo response : fail.. " 
                self.logger.write(log)
             
        except Exception, ex:
            self.get_error_result()
            self.logger.write('ReqUserScoreInfo', ex)

            self.response.rescode = CommStatusCode.exception_error
            self.response.resmsg = CommStatusCode.exception_error_msg
 
        self.callback()
 
