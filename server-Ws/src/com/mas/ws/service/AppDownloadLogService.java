package com.mas.ws.service;

import com.mas.ws.pojo.AppDownloadLog;
import com.mas.ws.pojo.AppDownloadLogIcon;
import com.mas.ws.pojo.Criteria;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppDownloadLogService {
    int countByExample(Criteria example);

    AppDownloadLog selectByPrimaryKey(Integer id);

    List<AppDownloadLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(AppDownloadLog record);

    int updateByPrimaryKey(AppDownloadLog record);

    int insert(AppDownloadLog record);

    int insertSelective(AppDownloadLog record);
    
    //add by lixin
    List<AppDownloadLogIcon> queryList();
    
    String getIconUrlByCountryNm(@Param("country") String country);
}