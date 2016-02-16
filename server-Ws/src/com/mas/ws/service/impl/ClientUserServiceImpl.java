package com.mas.ws.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.data.Data;
import com.mas.ws.mapper.ClientMachineMapper;
import com.mas.ws.mapper.ClientUserMapper;
import com.mas.ws.pojo.ClientUser;
import com.mas.ws.pojo.Criteria;
import com.mas.ws.service.ClientUserService;

@Service
public class ClientUserServiceImpl implements ClientUserService {
    @Autowired
    private ClientUserMapper clientUserMapper;
    @Autowired
    private ClientMachineMapper clientMachineMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(ClientUserServiceImpl.class);

    public int countByExample(Criteria example) {
        int count = this.clientUserMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public ClientUser selectByPrimaryKey(Integer clientId) {
        return this.clientUserMapper.selectByPrimaryKey(clientId);
    }

    public List<ClientUser> selectByExample(Criteria example) {
        return this.clientUserMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(ClientUser record) {
        return this.clientUserMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(ClientUser record) {
        return this.clientUserMapper.updateByPrimaryKey(record);
    }

    public int insert(ClientUser record) {
        return this.clientUserMapper.insert(record);
    }

    public int insertSelective(ClientUser record) {
        return this.clientUserMapper.insertSelective(record);
    }
    @Override
	public ClientUser addClientMobile(ClientUser clientUser, Data data) {
		ClientUser clientUserRecord = getClientMoblie(clientUser.getImei());
		if (clientUserRecord == null) {
			clientUser.setCreateTime(new Date());
			clientUser.setResolution(clientUser.getScreenWidth()+"x"+clientUser.getScreenHeight());
			clientUser.setActiveNum(0);//插入IP会更新会更新为1
			clientUser.setAppPackageNameFirst(data.getAppPackageName());
			clientUser.setAppVersionNameFirst(data.getAppVersionName());
			clientUser.setAppVersionCodeFirst(data.getAppVersionCode());
			clientUserMapper.insertSelective(clientUser);
		} else {
			clientUser.setUpdateTime(new Date());
			clientUser.setActiveNum(clientUserRecord.getActiveNum());
			clientUser.setClientId(clientUserRecord.getClientId());
			//clientUserMapper.updateByPrimaryKeySelective(clientData);//插入IP会更新
		}
		return clientUser;
	}
	public ClientUser getClientMoblie(String imei) {
		Criteria example = new Criteria();
		example.put("imei", imei);
		List<ClientUser> list = clientUserMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}