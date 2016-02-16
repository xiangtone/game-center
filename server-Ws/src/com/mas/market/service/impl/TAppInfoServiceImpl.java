package com.mas.market.service.impl;

import com.mas.market.mapper.TAppInfoMapper;
import com.mas.market.pojo.TAppInfo;
import com.mas.market.service.TAppInfoService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TAppInfoServiceImpl implements TAppInfoService {
    @Autowired
    private TAppInfoMapper tAppInfoMapper;

    private static final Logger logger = LoggerFactory.getLogger(TAppInfoServiceImpl.class);

    public TAppInfo selectByPrimaryKey(Integer id) {
        return this.tAppInfoMapper.selectByPrimaryKey(id);
    }

}