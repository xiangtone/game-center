package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.ImageDownloadLog;

import java.util.List;

public interface ImageDownloadLogService {
    int countByExample(Criteria example);

    ImageDownloadLog selectByPrimaryKey(Integer id);

    List<ImageDownloadLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(ImageDownloadLog record);

    int updateByPrimaryKey(ImageDownloadLog record);

    int insert(ImageDownloadLog record);

    int insertSelective(ImageDownloadLog record);
}