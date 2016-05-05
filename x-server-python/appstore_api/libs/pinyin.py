#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
    Author:cleverdeng
    E-mail:clverdeng@gmail.com
    ModifyBy:jensen
"""

__version__ = '0.9'
__all__ = ["PinYin"]

import os.path
import itertools
import sys

def cur_file_dir():
    """ 获取脚本的当前路径
    """
     #获取脚本路径
    path = sys.path[0]
    #判断为脚本文件还是py2exe编译后的文件，如果是脚本文件，则返回的是脚本的目录，
    #如果是py2exe编译后的文件，则返回的是编译后的文件路径
    if os.path.isdir(path):
        return path
    elif os.path.isfile(path):
        return os.path.dirname(path)


class PinYin(object):


    def __init__(self, dict_file=cur_file_dir() + '/libs/word.data'):
        self.word_dict = {}
        self.dict_file = dict_file


    def load_word(self):
        if not os.path.exists(self.dict_file):
            raise IOError("NotFoundPinYinLibFile")

        with file(self.dict_file) as f_obj:
            for f_line in f_obj.readlines():
                try:
                    line = f_line.split('    ')
                    self.word_dict[line[0]] = line[1]
                except:
                    line = f_line.split('   ')
                    self.word_dict[line[0]] = line[1]


    def hanzi2pinyin(self, string=""):
        """ 汉字转拼音
        只取第一个拼音匹配结果，没处理多音字
        返回:eg. list["zi","mu"]
        """
        result = []
        if string:
            if not isinstance(string, unicode):
                string = string.decode("utf-8")
        
            for char in string:
                key = '%X' % ord(char)
                split_array = self.word_dict.get(key, char).split()
                if split_array and len(split_array) > 0:
                    result.append(split_array[0][:-1].lower())

        return result

    def hanzi2pinyin_with_polyphone(self, string=""):
        """ 汉字转拼音
        有处理多音字
        返回:eg. list[["yin","yue"],["yin","le"]]
        """
        result = []
        if string:
            if not isinstance(string, unicode):
                string = string.decode("utf-8")
            tmp_rsult = []
            #for char in string:
            for char_index in xrange(len(string)):
                char = string[char_index]
                key = '%X' % ord(char)
                split_array = self.word_dict.get(key, char).split()
                if split_array and len(split_array) > 0:
                    char_set = set()
                    for i in xrange(len(split_array)):
                        char_set.add(split_array[i][:-1].lower())
                    tmp_rsult.extend([dict(name=j, order=char_index+1) for j in list(char_set)])

            result = self._own_combinations(tmp_rsult)
                   
        return result

    def _own_combinations(self, data=[]):
        """ 自定义组合数据
        """
        result=[]
        if data:
            iter_result = itertools.combinations(data,data[-1]["order"])
            for item in iter_result:
                item_result = True
                for i in xrange(len(item)):
                    if item[i]["order"] != i+1:
                        item_result = False
                        break
                if item_result:
                    result.append([i["name"] for i in item])
        return result

    def hanzi2pinyin_split(self, string="", split=""):
        result = self.hanzi2pinyin(string=string)
        if split == "":
            return result
        else:
            return split.join(result)

    def hanzi2pinyin_merge(self, string="", merge=" "):
        """ 汉字2拼音
        没处理多音字
        返回：全拼音 + 首拼音的组合
        """
        result = self.hanzi2pinyin(string=string)
        result2 = [i[0] for i in result if i]
        return "".join(result) + merge + "".join(result2)

    def hanzi2pinyin_merge_with_polyphone(self,string="",merge=" "):
        """ 汉字2拼音
        对多音字有处理
        返回：全拼音 + 首拼音的组合
        慎用：比较慢
        """
        result_list = []
        pinyin_result = self.hanzi2pinyin_with_polyphone(string=string)
        if pinyin_result:
            for item in pinyin_result:
                short_item = [i[0] for i in item if i]
                result_list.append("".join(item) + merge + "".join(short_item))
        return merge.join(result_list)
        


