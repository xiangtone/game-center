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

import sys,os

count = 0

#创建一级目录
def initDir(savefolders):
	"""
	复制云端目录结构
	:param savefolders:
	:return:
	"""
	pathdir=os.path.join(savefolders,"M00")
	print pathdir
	if not os.path.exists(pathdir):
		os.mkdir(pathdir)
	global count;
	for i in xrange(16):
		for j in xrange(16):
			dir_name = str(hex(i))[2:].upper() + str(hex(j))[2:].upper()
			dir_path = os.path.join(pathdir,dir_name)
			#obj = cos.createFolder(bucket, dir_path)
			if os.path.exists(dir_path):
				continue
			else:
				os.mkdir(dir_path)
			print dir_path

			# 创建二级目录
			for k in xrange(16):
				for l in xrange(16):
					second_dir_name = str(hex(k))[2:].upper() + str(hex(l))[2:].upper()
					second_dir_path = os.path.join(dir_path,second_dir_name)
					if os.path.exists(dir_path):
						os.mkdir(second_dir_path)
					else:
						continue
					print second_dir_path
					count+=1

	print count

# if __name__=="__main__":
# 	initDir("D:\\VAR");