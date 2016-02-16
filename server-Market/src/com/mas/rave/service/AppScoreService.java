package com.mas.rave.service;

import java.util.List;

import com.mas.rave.main.vo.AppScore;

/**
 * appScore
 * 
 * @author liwei.sz
 * 
 */
public interface AppScoreService {
	public List<AppScore> getAllScore();

	public void updateSore(AppScore score);

}
