package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.ImageSearchLog;

import java.util.List;

public interface ImageSearchLogService {
    int countByExample(Criteria example);

    ImageSearchLog selectByPrimaryKey(Integer id);

    List<ImageSearchLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(ImageSearchLog record);

    int updateByPrimaryKey(ImageSearchLog record);

    int insert(ImageSearchLog record);

    int insertSelective(ImageSearchLog record);
}