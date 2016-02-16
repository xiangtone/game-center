package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.SkinDownloadLog;

import java.util.List;

public interface SkinDownloadLogService {
    int countByExample(Criteria example);

    SkinDownloadLog selectByPrimaryKey(Integer id);

    List<SkinDownloadLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(SkinDownloadLog record);

    int updateByPrimaryKey(SkinDownloadLog record);

    int insert(SkinDownloadLog record);

    int insertSelective(SkinDownloadLog record);
}