package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MusicSearchLog;

import java.util.List;

public interface MusicSearchLogService {
    int countByExample(Criteria example);

    MusicSearchLog selectByPrimaryKey(Integer id);

    List<MusicSearchLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(MusicSearchLog record);

    int updateByPrimaryKey(MusicSearchLog record);

    int insert(MusicSearchLog record);

    int insertSelective(MusicSearchLog record);
}