package com.mas.ws.service;

import java.util.List;

import com.mas.data.Data;
import com.mas.ws.pojo.ClientUser;
import com.mas.ws.pojo.Criteria;

public interface ClientUserService {
    int countByExample(Criteria example);

    ClientUser selectByPrimaryKey(Integer clientId);

    List<ClientUser> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(ClientUser record);

    int updateByPrimaryKey(ClientUser record);

    int insert(ClientUser record);

    int insertSelective(ClientUser record);

	ClientUser addClientMobile(ClientUser clientInfo, Data data);

}