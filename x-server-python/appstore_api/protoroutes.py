#!/bin/env python
#-*- encoding=utf8 -*-

import logic.testhandler
import logic.appsconfig
import logic.applist
import logic.download
import logic.update
import logic.appinfo
import logic.appinform     # 应用举报
import logic.appfeedback   # 应用反馈
import logic.appcomment    # 应用评分和评论
import logic.apprecomm     # 应用推荐

HANDLER_MAP = {
    #'ReqTest': logic.testhandler.ReqTest,
    'ReqGlobalConfig': logic.appsconfig.ReqGlobalConfig,
    'ReqGroupElems': logic.applist.ReqGroupElems,
    'ReqDownRes':logic.download.ReqDownRes,
    'ReqAppsUpdate':logic.update.ReqAppsUpdate,
    'ReqAppList4SearchKey':logic.applist.ReqAppList4SearchKey,
    'ReqDistributeApps':logic.applist.ReqDistributeApps,
    'ReqAppInfo':logic.appinfo.ReqAppInfo,
    'ReqAppInform':logic.appinform.ReqAppInform,
    'ReqFeedback':logic.appfeedback.ReqFeedback,
    'ReqAddComment':logic.appcomment.ReqAddComment,
    'ReqUserScoreInfo':logic.appcomment.ReqUserScoreInfo,
    'ReqRecommApp':logic.apprecomm.ReqRecommApp  
}
