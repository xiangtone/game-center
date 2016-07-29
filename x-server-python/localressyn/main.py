#!/bin/env python
#-*- encoding=utf8 -*-
import os,sys
import asyncdown as asyncdown
from configmanager import Config
import fdfsinit as fdfs
import thread,time
import util as util

#ResInfo 每次启动程序需要同步的资源信息列表

# SAVEPATH="D:\\VAR" # 指定同步那一个目录 win dev
SAVEPATH="D:\\VAR3"
#SAVEPATH="/var/apkdata" # 指定同步那一个目录linux test


#线程同步下载apk信息到数据库系统
#
# def synDataInfo(number):
#
#     sys.stdout.write('开始同步apk下载记录开始....')
#     asyncdown.syndownloadinfo()
#     sys.stdout.write('开始同步apk下载记录结束....')
#     sys.stdout.flush()
#     print number
#     time.sleep(number)

if __name__ == '__main__':
    # 初始化分布式目录
    if not os.path.exists(os.path.join(SAVEPATH,"M00")):
        fdfs.initDir(SAVEPATH)
    #配置域名信息
    util.getSiteDomain()
    if Config().getsynDomainInfo() == '0':
        if asyncdown.getdomainId(util.getSiteDomain()) == 0:
            asyncdown.syndomaininfo()  # 导入域名信息到数据库
            Config().setsynDomainInfo(issyn='1')


    urllist,ResInfo=asyncdown.downloadapks(SAVEPATH)
    #后期加入日志功能----***************-----
    #urllist,ResInfo
    #多线程导入数据记录
    asyncdown.syndownloadinfo(ResInfo)
    # thread.start_new_thread(synDataInfo,(2,))
    sys.exit(0)

