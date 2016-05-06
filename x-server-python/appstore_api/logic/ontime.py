#!/bin/env python
#-*- encoding=utf8 -*-
import model.applist as m_applist

class _OnTimeStart(object):

    """ 启动初始化
    """

    def data_start_init(self):
        """ 数据初始化
        """
        # 搜索关键字初始化
        m_applist.SearchInfo.gen_src_key_info()
        

OnTimeStart = _OnTimeStart()