#!/bin/env python
#-*- encoding=utf8 -*-

from threading import Thread
import os
import urllib2
import urlparse
import config as config
import base_utils.logger as logger
import util as util
from Queue import Queue
# '''保存路径'''
#SAVE_FOLDER = "D:\\VAR"测评目录

#####解决日志乱码问题#########
import sys
reload(sys)
sys.setdefaultencoding( "utf-8" )
##############################


class Downloader(Thread):
  def __init__(self, task,savefolders):
    Thread.__init__(self)
    self.logger=logger.GlobalLogger(config=config.GLOBAL_SETTINGS)
    self.savefolders=savefolders
    self.task = task
  def run(self):
    '''覆盖父类的run方法'''
    while True:
      url = self.task.get()
      self.download(url,self.savefolders)
      self.task.task_done()


  def build_path(self,savefolder,filepath):
    """
    decs:#例如"M00/00/00"目录分离成列表重新成各平（win/linux）台目录
    :param savefolder:
    :param filepath:
    :return:
    """
    diro, filename = os.path.split(filepath)
    dirs=diro.split(r'/')[1:]
    dirn=os.path.join(dirs[0],dirs[1],dirs[2])
    myPath = os.path.join(savefolder, dirn)
    return myPath,filename


  #desc:下载文件到目录
  #param:url
  #param:tosave 存储目录
  def download(self,url,tosave):
    filepath = urlparse.urlparse(url).path #解析url目录与文件名
    save_dir,filename=self.build_path(tosave,filepath)
    save_filepath=os.path.join(save_dir,filename)
    handle = urllib2.urlopen(url)

    print save_filepath
    with open(save_filepath, 'wb') as handler:
      while True:
        chunk = handle.read(1024)
        if not chunk:
            break
        else:
            handler.write(chunk)
    msg = u"已经从 %s下载完成\n" % url
    self.logger.write(msg=msg)
    # util.write_log(msg)
    sys.stdout.write(msg)
    sys.stdout.flush()



def start_download(urls,savepath):
    """
    desc:开启下载对列
    :param urls:
    :param savepath:
    :return:
    """
    # 创建一个队列
    quene = Queue()
    # 获取list的大小
    size = len(urls)
    print ('the size is %s') % size
    # 开启线程
    for _ in xrange(size):
        t = Downloader(quene,savepath)
        t.setDaemon(True)
        t.start()

    # 入队列
    for url in urls:
        print url
        quene.put(url)
    quene.join()

#***************测试******************
# if __name__=="__main__":
#
#     urls=[]
#
#     urla = r'http://hsfs-10029187.file.myqcloud.com/M00/00/00/CmnJpFcEiDaEfs_pAAAAAMnYDc4096.apk'
#
#     urlb = r'http://hsfs-10029187.file.myqcloud.com/M00/00/02/CmnJpFcPNQKEHxDkAAAAAKk4TkA022.apk'
#
#
#     urls.append(urla)
#     urls.append(urlb)
#
#     start_download(urls,SAVE_FOLDER)
#




