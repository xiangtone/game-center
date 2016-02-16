package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.AppScoreMapper;
import com.mas.rave.main.vo.AppScore;
import com.mas.rave.service.AppScoreService;

/**
 * appScore
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppScoreServiceImpl implements AppScoreService {
	@Autowired
	private AppScoreMapper appScoreMapper;

	@Override
	public List<AppScore> getAllScore() {
		// TODO Auto-generated method stub
		return appScoreMapper.getAllScore();
	}

	@Override
	public void updateSore(AppScore score) {
		appScoreMapper.updateSore(score);
	}
}
