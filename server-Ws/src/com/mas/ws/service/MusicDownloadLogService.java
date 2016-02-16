package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MusicDownloadLog;

import java.util.List;

public interface MusicDownloadLogService {
    int countByExample(Criteria example);

    MusicDownloadLog selectByPrimaryKey(Integer id);

    List<MusicDownloadLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(MusicDownloadLog record);

    int updateByPrimaryKey(MusicDownloadLog record);

    int insert(MusicDownloadLog record);

    int insertSelective(MusicDownloadLog record);
}