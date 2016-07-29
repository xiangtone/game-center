#!/bin/env python
#-*- encoding=utf8 -*-

import shelve
#系统配置管理类
#author:nicajonh
#date:2016-7-12

class Singleton(object):
    def __new__(cls, *args, **kwargs):
        if not hasattr(cls,'_instance'):
            cls._instance=super(Singleton,cls).__new__(cls,*args,**kwargs)
        return cls._instance

class Config(Singleton):
    def getlastIndex(self):
        """
        取得上次下载数据索引
        :return:
        """
        file = shelve.open('sysconfig.dat', 'c')
        if not file.has_key('ldindex'):
             file['ldindex']='0'
             return '0'
        lindex = file['ldindex']
        return lindex

    def setlastIndex(self,num=0):
        """
        取得上次下载数据索引
        :param num:
        :return:
        """
        file = shelve.open('sysconfig.dat', 'c')
        file['ldindex'] = str(num)

    #只配置一次域名同步到数据库信息0:表示未配置,1:表示已经配置
    def getsynDomainInfo(self):
        """
         取得同步域名信息
        :return:
        """
        file = shelve.open('sysconfig.dat', 'c')
        if not file.has_key('isSynDomain'):
            file['isSynDomain'] = '0'
            return '0'
        isSyned = file['isSynDomain']
        return isSyned

    def setsynDomainInfo(self,issyn='0'):
        """
        填写取得同步域名信息
        :param num:
        :return:
        """
        file = shelve.open('sysconfig.dat', 'c')
        file['isSynDomain'] = issyn

    def LoadData(self):
        configdat=[]
        with shelve.open('sysconfig.dat','r') as db:
            for item in db.items():
                configdat.append(item)
        return configdat