package com.mas.rave.dao;

import java.util.List;

import com.mas.rave.main.vo.AppScore;

/**
 * appscore
 * 
 * @author liwei.sz
 * 
 */
public interface AppScoreMapper {
	public List<AppScore> getAllScore();

	public void updateSore(AppScore score);

}