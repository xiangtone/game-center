#!/bin/env python
# -*- encoding=utf8 -*-

########################
## author:nicajonh  ##
## brief:ios应用sql分页查询类
## date:2016-08-26  ##
########################

import config
import os
from base_utils.db import MysqlConnection


class MySQLQueryPagination(object):
    def __init__(self, numPerPage=20):
        self.numPerPage = numPerPage

    def queryForList(self, sql, param=None):
        totalPageNum = self.__calTotalPages(sql, param)
        for pageIndex in range(totalPageNum):
            yield self.__queryEachPage(sql, pageIndex, param)

    def queryForPageList(self, sql, pageIndex,param=None):
        qSql = self.__createPaginaionQuerySql(sql, pageIndex)
        query_data = []
        with MysqlConnection(config.GLOBAL_SETTINGS['db']) as conn:
            if param==None:
                query_data = conn.query(qSql)
            else:
                query_data = conn.query(qSql)
            return query_data
        return []

    def __createPaginaionQuerySql(self, sql, currentPageIndex):
        startIndex = self.__calStartIndex(currentPageIndex)
        qSql = r'select * from (%s) total_table limit %s,%s' % (sql, startIndex, self.numPerPage)
        return qSql

    def __queryEachPage(self, sql, currentPageIndex, param=None):
        qSql = self.__createPaginaionQuerySql(sql, currentPageIndex)
        query_data = []
        with MysqlConnection(config.GLOBAL_SETTINGS['db']) as conn:
            if param is None:
                query_data = conn.query(qSql)
            else:
                query_data = conn.query(qSql, param)
        return query_data

    def __calStartIndex(self, currentPageIndex):
        startIndex = (currentPageIndex-1) * self.numPerPage
        return startIndex

    def __calTotalRowsNum(self, sql, param=None):
        ''''' 计算总行数 '''
        qSql = r'select count(*) total_table from (%s)' % sql
        query_data = []
        with MysqlConnection(config.GLOBAL_SETTINGS['db']) as conn:
            if param is None:
                query_data = conn.query(qSql)
            else:
                query_data = conn.query(qSql, param)
        totalRowsNum = 0
        if query_data:
            totalRowsNum = len(query_data)
        return totalRowsNum

    def __calTotalPages(self, sql, param):
        ''''' 计算总页数 '''
        totalRowsNum = self.__calTotalRowsNum(sql, param)
        totalPages = 0
        if (totalRowsNum % self.numPerPage) == 0:
            totalPages = totalRowsNum / self.numPerPage
        else:
            totalPages = (totalRowsNum / self.numPerPage) + 1
        return totalPages

    def __calLastIndex(self, totalRows, totalPages, currentPageIndex):
        """
        计算结束时候的索引
        :param totalRows:
        :param totalPages:
        :param currentPageIndex:
        :return:
        """
        lastIndex = 0
        if totalRows < self.numPerPage:
            lastIndex = totalRows
        elif ((totalRows % self.numPerPage == 0) \
                      or (totalRows % self.numPerPage != 0 \
                                  and currentPageIndex < totalPages)):
            lastIndex = currentPageIndex * self.numPerPage
        elif (totalRows % self.numPerPage != 0 and currentPageIndex == totalPages):  # 最后一页
            lastIndex = totalRows
        return lastIndex
