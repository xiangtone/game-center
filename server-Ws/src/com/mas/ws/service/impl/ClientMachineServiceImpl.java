package com.mas.ws.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.ws.mapper.ClientMachineMapper;
import com.mas.ws.pojo.ClientMachineWithBLOBs;
import com.mas.ws.service.ClientMachineService;

@Service
public class ClientMachineServiceImpl implements ClientMachineService {
    @Autowired
    private ClientMachineMapper clientMachineMapper;

    @Override
    public int insertSelective(ClientMachineWithBLOBs record) {
        return this.clientMachineMapper.insertSelective(record);
    }

	@Override
	public void updateByImeiSelective(ClientMachineWithBLOBs clientMachine) {
		// TODO Auto-generated method stub
		this.clientMachineMapper.updateByImeiSelective(clientMachine);
	}
}