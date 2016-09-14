#!/bin/env python
#-*- encoding=utf8 -*-
#
# Copyright 2016 
#
# 作者：jensen
#
# 功能：该模块提供给cos生成fdfs的所有目录
#
# 版本：V1.0.0

import qcloud_cos as qcloud

appid = '10029187'
secret_id = 'AKIDVw3XlWnOW9TMAFqbziF0NDlJEvRTofrW'
secret_key = 'yByAISeC4H5GUk7rVpaj8Ud72gwm7f9j'


# 初始化
bucket = 'hsfs'
cos = qcloud.Cos(appid,secret_id,secret_key)
obj = cos.createFolder(bucket, '/M00/')
# print obj

#print int('A', 16)
#print str(hex(5))
# print str(hex(5))[2:].upper()

count = 0

#创建一级目录
for i in xrange(16):
	for j in xrange(16):
		dir_name = str(hex(i))[2:].upper() + str(hex(j))[2:].upper()
		dir_path = '/M00/' + dir_name + "/"
		obj = cos.createFolder(bucket, dir_path)
		print dir_path

		# 创建二级目录
		for k in xrange(16):
			for l in xrange(16):
				second_dir_name = str(hex(k))[2:].upper() + str(hex(l))[2:].upper()
				second_dir_path = dir_path + second_dir_name + "/"
				obj = cos.createFolder(bucket, second_dir_path)
				print second_dir_path
				count+=1

print count
