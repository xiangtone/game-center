package com.mas.data;
import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.ClientMachineWithBLOBs;

@XmlRootElement
public class MachineRequest extends BaseRequest{
	
	private ClientMachineWithBLOBs clientMachine;
	private Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public ClientMachineWithBLOBs getClientMachine() {
		return clientMachine;
	}

	public void setClientMachine(ClientMachineWithBLOBs clientMachine) {
		this.clientMachine = clientMachine;
	}

}
