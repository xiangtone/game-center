package com.mas.ws.service;

import com.mas.ws.pojo.ClientMachineWithBLOBs;


public interface ClientMachineService {

	int insertSelective(ClientMachineWithBLOBs record);

	void updateByImeiSelective(ClientMachineWithBLOBs clientMachine);
	
}