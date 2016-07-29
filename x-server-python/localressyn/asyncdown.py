#-*- encoding=utf8 -*-
#
# Copyright 2016
#
# 作者：nicajonh
#
# 功能：实现增量下载apk到本地
#
# 版本：V1.0.0
import os,re,sys
import configmanager as ConfigManager
import fdfsinit as fdfs
import SDownQueue as SDQueue
import urlparse,util
import base_utils.db as db
import base_utils.logger as logger
import config



def getpacksinfo(num=5):
    """
    取出Packs基本信息字典
    :return dict:{'PackID':'PackID','PackUrl':'PackUrl'}
    """
    index=int(ConfigManager.Config().getlastIndex())
    query_new=[] #接收过滤后的url链接
    res_info=[] #需要同步的资源字典信息
    with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as conn:
        sql_raw="select PackID,ShowName,PackName,PackUrl from v_subway_packinfo limit %s,%s;"
        sql=sql_raw % (index,num)
        query_rs=conn.query(sql)
        if query_rs:
            query_new=filter(lambda item:re.match(r'^http://hsfs-10029187.file*',item['PackUrl']),query_rs)
            for item in query_new:
                adict=dict({'PackID':item['PackID'],'PackUrl':item['PackUrl']})
                res_info.append(adict)
        ConfigManager.Config().setlastIndex(int(ConfigManager.Config().getlastIndex())+num) #存储下载索引到本地
    return res_info

def downsyncPackUrl(purllist,savefolders):
    """
    :param packlist:传入下载链接
    :return:
    """
    durl=[] #下装队列url
    if isinstance(purllist,list):
        for i in xrange(len(purllist)):
            purl=urlparse.urlparse(purllist[i])
            filepath=os.path.join(savefolders,purl.path)#文件目录及文件名
            if not os.path.exists(filepath):
                durl.append(purllist[i])
            else:
                continue
    #同步下载Apk
    if len(durl) > 0:
        SDQueue.start_download(durl,savefolders)


def downloadapks(sfolders):
    """
    多线程同步下载apk资源
    :param sfolders: 保存路径
    :return:urlist,resinfo 下载信息:url列表与key-value字典信息
    """
    global ResInfo
    urllist=[] #资源urls信息
    resinfos=getpacksinfo()#资源key-value信息
    for item in resinfos:
        urllist.append(item['PackUrl'])
    downsyncPackUrl(urllist,sfolders)
    return urllist,resinfos

#同步导入数据记录到pack_site_relations表中
def syndownloadinfo(resdata=None):
    """
    :param:需要同步key-value{'PackID':xxx,'SubWayPackUrl':xxx}资源列表
    :return:
    """
    resinfo=[]
    if isinstance(resdata,list):
        resinfo=resdata
    else:
        return

    #记录开始同步日志
    domainstr = util.getSiteDomain()
    log=logger.GlobalLogger()
    #resinfo=getpacksinfo()#取得资源key-value信息

    domainId=getdomainId(domainstr)
    #域名替换
    for item in resinfo:
        item['PackUrl']=str(item['PackUrl']).replace("hsfs-10029187.file.myqcloud.com",util.getSiteDomain())

    #数据插入到记录
    with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as conn:
        sql_raw = "insert into pack_site_relations (SiteID,PackID,SubwayPackUrl) values (%s,%s,%s);"
        for item in resinfo:
            sql_result=conn.execute_rowcount(sql_raw,domainId,item['PackID'],item['PackUrl'])
            if sql_result==1:
                conn.commit()

    #同步结束记录到日志
    sys.stdout.write('开始同步apk下载记录结束....')
    log.write('开始同步apk下载记录结束....')
    sys.stdout.flush()

#查寻当前域名在download_sites中的id
def getdomainId(domainstr=""):
    sql_result=None
    with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as conn:
        sql_raw = "select SiteID from download_sites where Domainstr=%s"
        sql_result=conn.query(sql_raw,domainstr)
        if sql_result:
            return dict(sql_result[0])['SiteID']
        else:
            return 0

#同步导入内网信息数据到download_sites表中
def syndomaininfo():

    #time.sleep(2000)
    domainstr=util.getSiteDomain()
    with db.MysqlConnection(config.GLOBAL_SETTINGS['db']) as conn:
        sql_raw = "insert into download_sites (Domainstr) values (%s);"
        sql_result=conn.execute_rowcount(sql_raw,domainstr)
        if sql_result==1:
            conn.commit()
    return







