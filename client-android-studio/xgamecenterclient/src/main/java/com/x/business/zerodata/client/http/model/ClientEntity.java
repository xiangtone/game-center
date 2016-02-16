package com.x.business.zerodata.client.http.model;

public class ClientEntity {

	private int devicesLayout ;//装载布局
	
	TransferRequest transferRequest ;

	
	public TransferRequest getTransferRequest() {
		return transferRequest;
	}

	public void setTransferRequest(TransferRequest transferRequest) {
		this.transferRequest = transferRequest;
	}

	public int getDevicesLayout() {
		return devicesLayout;
	}

	public void setDevicesLayout(int devicesLayout) {
		this.devicesLayout = devicesLayout;
	}

}
