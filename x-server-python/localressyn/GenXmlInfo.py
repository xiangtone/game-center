#!/bin/env python
#-*- encoding=utf8 -*-
import os
import hashlib
import xmltodict,json
import util,xmlutils
from xml.dom import minidom, Node
from xml.etree import ElementTree
from xml.etree.ElementTree import SubElement,Element

PORT=8090 #端口
RESDIR = r'D:\testfolders' #资源目录
XMLFILE = r'D:\testxmlpath\AppInfo.xml'

def generateUrl(port='',fname=''):
    """
    :param port:
    :param fname:
    :return:
    """
    url=''
    urlgen=r'http://'+util.getLocalIP()+r':%s'+'/%s'
    if fname !='':
        url=urlgen % (port,fname)
    return url


def walktoxml(path,filename):
    """
    遍历目录生成md5保存到xml
    :param path:
    :param xmlpath:
    :return:
    """
    applist=[]
    fl = os.listdir(path)  # get what we have in the dir.
    for f in fl:
        if os.path.isdir(os.path.join(path, f)):  # if is a dir.
            walktoxml(os.path.join(path, f))
        else:  # if is a file
            md5str=util.calc_md5(os.path.join(path,f))
            d=dict({'filename':f,'md5':md5str,'url':generateUrl(PORT,f)})
            #print d
            applist.append(d)
    generateAppinfoXml(filename,applist)  # choice 2 write to xml.


#desc:追加生成xml文档
#param:filename
#param:list队列数据

def generateAppinfoXml(filename,args):

    applist=None
    if isinstance(args,list):
       appresinfo=args
    else:
        return None
    #生成xml
    if len(appresinfo) > 0:
        if not os.path.exists(filename):
            generaterAppXml(filename,appresinfo)
        else:
            #remark 修改
            # os.remove(filename)
            # generaterAppXml(appresinfo, filename)
            appendNodeToXml(filename,appresinfo)

def appendNodeToXml(filename,args):
    """
     xml文件元素增量
    :param filename:
    :param args:
    :return:
    """
    addlist=[]
    if isinstance(args,list):
        if len(args) > 0:
            # 获取xml文件元素
            isInXml=True #元素在xml文件中标示位
            root=xmlutils.read_xml(filename)
            rootnode=root.getroot()
            #print rootnode.findall('appitem')
            listnode=root.getiterator('appitem')
            # for node in listnode:
            #     print node.attrib
            for i in xrange(len(args)):
                if args[i].has_key('md5'):
                    for node in listnode:
                        #node[0]取得xml的'md5'元素
                        if node[0].text == dict(args[i])['md5']:
                            isInXml = True#已经包含
                            break
                        else:
                            isInXml = False#不已经包含
                    #判断元素是否在XML文件中
                    if not isInXml:
                        print args[i]
                        element=Element('appitem',{'filename':dict(args[i])['filename']})
                        #子节点1
                        sbelement1=SubElement(element,'md5')
                        sbelement1.text=dict(args[i])['md5']
                        #子节点2
                        sbelement2 = SubElement(element,'url')
                        sbelement2.text = dict(args[i])['url']
                        rootnode.append(element)
                        isInXml=True
                else:
                    continue
        #保存xml文件
        xmlutils.write_xml(root,filename)

def generaterAppXml(filename,appres):
    """
     xml格式
    <appinfo>
        <appitem filename="filename">
            <md5>fsklafklsajfdr</md5>
            <url>http://xxx.xxx.xxx/lfkajslkdfjsla.apk</url>
        </appitem>
    </appinfo>
    生成APP-XML-INFO
    :param appres: 资源列表
    :param filename: 文件名
    :return:
    """
    doc = minidom.Document()
    appinfolist = doc.createElement('appinfo')
    doc.appendChild(appinfolist)
    for i in xrange(len(appres)):
        appitem = doc.createElement('appitem')
        appitem.setAttribute('filename', appres[i]['filename'])
        md5 = doc.createElement('md5')
        appitem.appendChild(md5)
        md5.appendChild(doc.createTextNode(appres[i]['md5']))
        url = doc.createElement('url')
        appitem.appendChild(url)
        url.appendChild(doc.createTextNode(appres[i]['url']))
        appinfolist.appendChild(appitem)
    #print doc.toprettyxml(indent=" ")
    with open(filename, r'w+') as f:
        f.write(doc.toprettyxml(indent=" "))

def parseXmltoJson(filename,*args,**kwargs):
    """
    解析应用信息Json
    :param filename:
    :param args:
    :param kwargs:
    :return:

    <appinfo>
        <appitem filename="filename">
            <md5>fsklafklsajfdr</md5>
            <url>http://xxx.xxx.xxx/lfkajslkdfjsla.apk</url>
        </appitem>
    </appinfo>
    生成APP-XML-INFO
    :param appres: 资源列表
    :param filename: 文件名
    :return:
    """
    xmlstr = open(filename, 'r').read()
    convertDict=xmltodict.parse(xmlstr)
    jsonstr = json.dumps(convertDict,indent=1)
    return jsonstr

def parseXmltoDict(filename,*args,**kwargs):
    """
    解析应用信息Json
    :param filename:
    :param args:
    :param kwargs:
    :return:

    <appinfo>
        <appitem filename="filename">
            <md5>fsklafklsajfdr</md5>
            <url>http://xxx.xxx.xxx/lfkajslkdfjsla.apk</url>
        </appitem>
    </appinfo>
    生成APP-XML-INFO
    :param appres: 资源列表
    :param filename: 文件名
    :return:
    """
    xmlstr = open(filename, 'r').read()
    convertDict=xmltodict.parse(xmlstr)
    return convertDict


if __name__=="__main__":
    import libs.pbjson as pbjson
    import proto.Subwaysync_pb2 as subwayproto
    #
    # mapp = subwayproto.SubWayApps()
    # appdic1 = mapp.item.add()
    # appdic2 = mapp.item.add()
    # appdic3 = mapp.item.add()
    #
    # appdic2.url="http://www.google.com"
    # appdic2.filename="demotest1"
    # appdic2.md5="asdkrjljoiulkjlfasdmlrj"
    #
    # appdic1.url = "http://www.google.com"
    # appdic1.filename = "demotest1"
    # appdic1.md5 = "asdkrjljoiulkjlfasdmlrj"
    #
    # str=appdic1.SerializeToString()
    #
    # appdic3.ParseFromString(str)
    #
    #
    # print appdic3
    jsonstr=json.dumps(parseXmltoDict(XMLFILE)['appinfo'])
    print jsonstr


