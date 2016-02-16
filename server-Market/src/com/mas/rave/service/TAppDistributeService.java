package com.mas.rave.service;

import java.util.List;

import com.mas.rave.main.vo.Criteria;
import com.mas.rave.main.vo.TAppDistribute;

public interface TAppDistributeService {
    int countByExample(Criteria example);

    List<TAppDistribute> selectByExample(Criteria example);

}