、#!bin/env python 
#-*- encoding=utf8 -*-
#
# 创建人：wind
#
# 时  间：20150901
#
# 描  述：应用的评分和评论

import datetime
import json
import math

import base_utils.db as db  
import base_utils.cache as cache
import base_utils.logger as logger
from model.const import APISrcCommentCacheName

import proto.Apps_pb2 as app_proto
import config

_EXPIRE_TIME = 86400   # 缓存保存60*60*24 = 86400 （一天）

# _WAITAUDIT_CACHE = 'comments:waitaudit:hash'        # 待审核的评论缓存
# _PASSED_CACHE = 'comments:passed:%s'              # Hash       审核通过的评论缓存         %appId
# _SCORE_CACHE = 'comments:score:%s'              # 应用的评分缓存 
# _COMMENT_ID = 'comment:id'   # 保存评论ID的缓存


_CACHE_APPID_OPENID_KEY = '%s_%s'               # String     审核通过的评论缓存单条记录的 key %(appId, OpenId)

def get_redis_cache():
    """
        获取缓存对象
    """
    ch = cache.Cache(config.GLOBAL_SETTINGS['redis'])  # 引用缓存信息
    return ch

def add_comments_passed_cache(cache_obj,key,filed,value):
    """
        将查询的评论信息添加到缓存中
    """
    # 
    # {commentid:评论id,openid:用户openid,username:用户名,appid:应用id,userscore:用户评分,vercode:本地版本代码,vername:本地版本号,comments:评论内容,commenttime:评论时间,auditstatus:审核状态,status:状态}
    # 
    if cache_obj:
        cache_obj = get_redis_cache()
 
    cache_obj.hset(key,filed,value)  # 添加到缓存里面  
    cache_obj.expire(key,_EXPIRE_TIME) # 设置过期时间

def add_comments_score_cache(cache_obj,key,score_obj):
    """
        添加应用的评分的信息到缓存中
    """
    if cache_obj:
        cache_obj = get_redis_cache()
    
    if score_obj:
        for filed in score_obj.keys(): 
            cache_obj.hset(key,filed,score_obj[filed])  # 添加到缓存里面  

        cache_obj.expire(key,_EXPIRE_TIME) # 设置过期时间

def get_app_sum_score(appid,isadd=0):
    """
        获取应用的评分信息
        appid     应用的id
        isadd     0=只查询，1=查询不到进行添加操作
    """
    score_obj = {}  # 应用总的评分信息
    # 查询缓存总是否存在 
    cache_obj = get_redis_cache()
    key = APISrcCommentCacheName.APP_SCORE_CACHE % appid

    # appid:应用id/commenttimes:评论次数/scoretimes:评分次数/scoresum:总得分/scoreavg:平均评分/scoretimes1:评分为1的次数/ 
    #      scoretimes2:评分为2的次数/scoretimes3:评分为3的次数/scoretimes4:评分为4的次数/scoretimes5:评分为5的次数
    keylist = cache_obj.keys(key) 
    if keylist:
        score_obj['appid'] = int(cache_obj.hget(key,'appid'))
        score_obj['commenttimes'] = int(cache_obj.hget(key,'commenttimes'))
        score_obj['scoretimes'] = int(cache_obj.hget(key,'scoretimes'))
        score_obj['scoresum'] = int(cache_obj.hget(key,'scoresum')) 
        score_obj['scoreavg'] = int(cache_obj.hget(key,'scoreavg')) 
        score_obj['scoretimes1'] = int(cache_obj.hget(key,'scoretimes1')) 
        score_obj['scoretimes2'] = int(cache_obj.hget(key,'scoretimes2')) 
        score_obj['scoretimes3'] = int(cache_obj.hget(key,'scoretimes3')) 
        score_obj['scoretimes4'] = int(cache_obj.hget(key,'scoretimes4'))
        score_obj['scoretimes5'] = int(cache_obj.hget(key,'scoretimes5')) 
 
    else:
        # 查询数据库
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:  
            sql = "select AppId,CommentTimes,ScoreTimes,ScoreSum,ScoreAvg,ScoreTimes1,ScoreTimes2,ScoreTimes3,ScoreTimes4,ScoreTimes5 from AppCommentSummary where AppId=%s"
            query_obj = yield from con.query(sql,appid)
            if query_obj:
                score_obj['appid'] = query_obj[0]['AppId']
                score_obj['commenttimes'] = query_obj[0]['CommentTimes']
                score_obj['scoretimes'] = query_obj[0]['ScoreTimes']
                score_obj['scoresum'] = query_obj[0]['ScoreSum']
                score_obj['scoreavg'] = query_obj[0]['ScoreAvg']
                score_obj['scoretimes1'] = query_obj[0]['ScoreTimes1']
                score_obj['scoretimes2'] = query_obj[0]['ScoreTimes2']
                score_obj['scoretimes3'] = query_obj[0]['ScoreTimes3']
                score_obj['scoretimes4'] = query_obj[0]['ScoreTimes4']
                score_obj['scoretimes5'] = query_obj[0]['ScoreTimes5']
                
                # 添加缓存  
                add_comments_score_cache(cache_obj,key,score_obj)

            else: 
                if isadd:
                    # 添加评分信息
                    sql = "insert into AppCommentSummary (AppId,CommentTimes,ScoreTimes,ScoreSum,ScoreAvg,ScoreTimes1,ScoreTimes2,ScoreTimes3,ScoreTimes4,ScoreTimes5) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);"
                    sql_result = con.execute_rowcount(sql,appid,0,0,0,0,0,0,0,0,0)  
                    if sql_result==1: 
                        con.commit()  
                        score_obj['appid'] = appid
                        score_obj['commenttimes'] = 0 
                        score_obj['scoretimes'] = 0
                        score_obj['scoresum'] = 0
                        score_obj['scoreavg'] = 0
                        score_obj['scoretimes1'] = 0
                        score_obj['scoretimes2'] = 0
                        score_obj['scoretimes3'] = 0
                        score_obj['scoretimes4'] = 0
                        score_obj['scoretimes5'] = 0

    return score_obj

def update_app_sum_score(appid,score):
    """
        修改应用评分的信息
    """
    # 获取缓存中的原评分信息
    score_obj = get_app_sum_score(appid,1)
    
    score_obj['scoretimes'] = score_obj['scoretimes']+1
    score_obj['scoresum'] = score_obj['scoresum']+score
    scoreNum = "scoretimes%s" % score 
    score_obj[scoreNum] = score_obj[scoreNum]+1
    score_obj['scoreavg'] = int(math.ceil(score_obj['scoresum']*2/score_obj['scoretimes']))
 
    # 查询数据库
    with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:
        sql = "update AppCommentSummary set ScoreTimes=%s,ScoreSum=%s,ScoreAvg=%s,ScoreTimes1=%s where AppId=%s "  
        if score==2:
            sql = "update AppCommentSummary set ScoreTimes=%s,ScoreSum=%s,ScoreAvg=%s,ScoreTimes2=%s where AppId=%s " 
        elif score==3:
            sql = "update AppCommentSummary set ScoreTimes=%s,ScoreSum=%s,ScoreAvg=%s,ScoreTimes3=%s where AppId=%s " 
        elif score==4:
            sql = "update AppCommentSummary set ScoreTimes=%s,ScoreSum=%s,ScoreAvg=%s,ScoreTimes4=%s where AppId=%s " 
        elif score==5:
            sql = "update AppCommentSummary set ScoreTimes=%s,ScoreSum=%s,ScoreAvg=%s,ScoreTimes5=%s where AppId=%s " 
        
        sql_result = con.execute_rowcount(sql,score_obj['scoretimes'],score_obj['scoresum'],score_obj['scoreavg'],score_obj[scoreNum],appid)  
        if sql_result==1: 
            con.commit()  
            
            # 更新缓存 
            cache_obj = get_redis_cache() 
            key = APISrcCommentCacheName.APP_SCORE_CACHE % appid
            add_comments_score_cache(cache_obj,key,score_obj)
 
        return sql_result

def add_app_score(comment_obj):
    """
        添加应用评分 
        comment_obj   包含评分和评论的字典对象

            commentid    # 评论id，更新评论时不为0
            openid       # 用户openId
            username     # 用户名
            appid        # 应用id
            userscore    # 评分
            vercode      # 本地版本代码
            vername      # 本地版本号
            comments     # 评论内容，空串代表无评论
            brand        # 品牌
            model        # 机型
            device       # 子机型
            ip           # 网络ip
    """
 
    nowTime = datetime.datetime.now() 
    inform_time = nowTime.strftime('%Y-%m-%d %H:%M:%S')  
     
    try:
        # 1.判断是评分还是评论，userscore=0表示评论，userscore!=0表示评分操作
        commentId = comment_obj['commentid'] 
        openId = comment_obj['openid']     
        userName = comment_obj['username']    
        appId = comment_obj['appid']    
        userScore = comment_obj['userscore']    
        verCode = comment_obj['vercode']    
        verName = comment_obj['vername']    
        # comments = comment_obj['comments']       
        brand = comment_obj['brand']    
        model = comment_obj['model']    
        device = comment_obj['device']    
        ip = comment_obj['ip']  

        # 缓存中查询该用户是否已经对该应用进行过评分操作
        cache_obj = get_redis_cache() 
        key = APISrcCommentCacheName.APP_PASSED_CACHE % appId
        filed = _CACHE_APPID_OPENID_KEY % (appId,openId)

        # 判断是否已经评论过
        if cache_obj.hexists(key,filed): 
            value = cache_obj.hget(key,filed)
            comment = json.loads(value)
            return comment['commentid'] 
           
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con: 
            sql_result = 0
            # 先查询有没有进行过评分 
            sql = "select CommentID,OpenId,UserName,AppID,UserScore,LocalVerCode,LocalVerName,Comments,CommentTime,AuditStatus,Status from AppComments where AppID=%s and OpenId=%s and AuditStatus=1 and Status=1;"
            query_obj = con.query(sql,appId,openId)
            if query_obj:
                sql_result = query_obj[0]['CommentID'];
                # 将其添加到缓存中
                # 
                # {commentid:评论id,openid:用户openid,username:用户名,appid:应用id,userscore:用户评分,vercode:本地版本代码,vername:本地版本号,comments:评论内容,commenttime:评论时间}
                # 
                comment = {} 
                comment['commentid'] = query_obj[0]['CommentID']
                comment['openid'] = query_obj[0]['OpenId']
                comment['username'] = query_obj[0]['UserName']
                comment['appid'] = query_obj[0]['AppID']
                comment['userscore'] = query_obj[0]['UserScore']
                comment['vercode'] = query_obj[0]['LocalVerCode']
                comment['vername'] = query_obj[0]['LocalVerName']
                comment['comments'] = query_obj[0]['Comments']
                comment['commenttime'] = query_obj[0]['CommentTime'].strftime('%Y-%m-%d %H:%M:%S')  
                value = json.dumps(comment)
                # 添加到缓存中
                add_comments_passed_cache(cache_obj,key,filed,value)
    
            else: 
                # 添加评分信息
                sql = "insert into AppComments (OpenId,UserName,AppID,UserScore,LocalVerCode,LocalVerName,Comments,BrandFlag,ModelFlag,DeviceFlag,IP,CommentTime,AuditStatus,Status,UpdateTime) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);"
                sql_result = con.execute_lastrowid(sql,openId,userName,appId,userScore,verCode,verName,'',brand,model,device,ip,inform_time,1,1,inform_time)  
                if sql_result!=0:
                    # 修改总的评分
                    sum_result = update_app_sum_score(appId,userScore)
                    if sum_result: 
                        con.commit()  
                    else:
                        sql_result = 0

            return sql_result
            
    except Exception,ex:   
        mylogger = logger.GlobalLogger() 
        mylogger.write(u'appinform add_app_score  error: %s' % (ex))
        return 0
  
# TODO: 评论暂时还没有完成......
def add_app_comment(comment_obj):
    """
        添加用户评分和评论
        comment_obj   包含评分和评论的字典对象

            commentid    # 评论id，更新评论时不为0
            openid       # 用户openId
            username     # 用户名
            appid        # 应用id
            userscore    # 评分
            vercode      # 本地版本代码
            vername      # 本地版本号
            comments     # 评论内容，空串代表无评论
            brand        # 品牌
            model        # 机型
            device       # 子机型
            ip           # 网络ip
    """
    

    nowTime = datetime.datetime.now() 
    inform_time = nowTime.strftime('%Y-%m-%d %H:%M:%S')  
        
    commentId = comment_obj['commentid'] 
    openId = comment_obj['openid']     
    userName = comment_obj['username']    
    appId = comment_obj['appid']    
    score = comment_obj['userscore']    
    verCode = comment_obj['vercode']    
    verName = comment_obj['vername']    
    comments = comment_obj['comments']       
    brand = comment_obj['brand']    
    model = comment_obj['model']    
    device = comment_obj['device']    
    ip = comment_obj['ip']   


    try:
        pass

        # # 1.判断是评分还是评论，userscore=0表示评论，userscore!=0表示评分操作
        # if score==0:
        #     # 用户评论
        #     pass 
        # else:
        #     # 用户评分
        #     pass 



        # with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con:    
        #     sql = "insert into FeedBack (OpenId,Content,UserContact,ClientId,ChannelNo,Version,CreateTime,UpdateTime,Status) values (%s,%s,%s,%s,%s,%s,%s,%s,%s);"
        #     sql_result = con.execute_rowcount(sql,openid,content,usercontact,clientid,channelno,version,inform_time,inform_time,1)  
        #     if sql_result==1:
        #         con.commit()  
        #     return sql_result
            
    except Exception,ex:   
        mylogger = logger.GlobalLogger() 
        mylogger.write(u'appinform add_app_inform  error: %s' % (ex))
        return 0

def get_app_user_score(appid,openid):
    """
        查询指定用户对指定的应用的评论及评分信息

        appid     应用id
        openid    用户openid

    """
    comment_info = {}
    
    # 先判断缓存中是否存在 
    cache_obj = get_redis_cache() 
    key = APISrcCommentCacheName.APP_PASSED_CACHE % appid
    filed = _CACHE_APPID_OPENID_KEY % (appid,openid)

    # 判断是否已经评论过
    if cache_obj.hexists(key,filed):   
        # 查询指定用户对应用的评分或评论信息
        value = cache_obj.hget(key,filed)
        #
        # {commentid:评论id,openid:用户openid,username:用户名,appid:应用id,userscore:用户评分,vercode:本地版本代码,vername:本地版本号,comments:评论内容,commenttime:评论时间}
        # 
        comment_info = json.loads(value) 
    else: 
        # 查询数据库信息   
        with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as con: 
            sql_result = 0
            # 先查询有没有进行过评分 
            sql = "select CommentID,OpenId,UserName,AppID,UserScore,LocalVerCode,LocalVerName,Comments,CommentTime from AppComments where AppID=%s and OpenId=%s and AuditStatus=1 and Status=1;"
            query_obj = con.query(sql,appid,openid)
            if query_obj: 
                # 将其添加到缓存中
                # 
                # {commentid:评论id,openid:用户openid,username:用户名,appid:应用id,userscore:用户评分,vercode:本地版本代码,vername:本地版本号,comments:评论内容,commenttime:评论时间}
                #  
                comment_info['commentid'] = query_obj[0]['CommentID']
                comment_info['openid'] = query_obj[0]['OpenId']
                comment_info['username'] = query_obj[0]['UserName']
                comment_info['appid'] = query_obj[0]['AppID']
                comment_info['userscore'] = query_obj[0]['UserScore']
                comment_info['vercode'] = query_obj[0]['LocalVerCode']
                comment_info['vername'] = query_obj[0]['LocalVerName']
                comment_info['comments'] = query_obj[0]['Comments']
                comment_info['commenttime'] = query_obj[0]['CommentTime'].strftime('%Y-%m-%d %H:%M:%S')  
                value = json.dumps(comment_info)
                # 添加到缓存中
                add_comments_passed_cache(cache_obj,key,filed,value)
    
    return comment_info



def get_app_score_list_proto(score_info,comment_info):
    """ 生成 协议UserScoreInfo 结果
        参数: 
            score_info 应用的评分信息
            comment_info  用户的评论及评分
    """
    if score_info:
        score_proto = app_proto.UserScoreInfo()
        score_proto.commentTimes = score_info['commenttimes'] 
        score_proto.scoreTimes = score_info['scoretimes']
        score_proto.scoreSum = score_info['scoresum']
        score_proto.scoreAvg = score_info['scoreavg']
        score_proto.scoreTime1 = score_info['scoretimes1']
        score_proto.scoreTime2 = score_info['scoretimes2']
        score_proto.scoreTime3 = score_info['scoretimes3']
        score_proto.scoreTime4 = score_info['scoretimes4'] 
        score_proto.scoreTime5 = score_info['scoretimes5']
        if comment_info:
            score_proto.userCommentInfo.commentId = comment_info['commentid'] 
            score_proto.userCommentInfo.userEI = ''
            score_proto.userCommentInfo.userName = comment_info['username'] 
            score_proto.userCommentInfo.userId = 0
            score_proto.userCommentInfo.userScore = comment_info['userscore']
            score_proto.userCommentInfo.localVerCode = comment_info['vercode']
            score_proto.userCommentInfo.localVerName = comment_info['vername'] 
            score_proto.userCommentInfo.comments = comment_info['comments'] 
            score_proto.userCommentInfo.commentTime = comment_info['commenttime'] 
            score_proto.userCommentInfo.openId = comment_info['openid']

        return score_proto.SerializeToString()
    else:
        return ""
    
     

 