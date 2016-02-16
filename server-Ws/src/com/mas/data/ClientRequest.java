package com.mas.data;
import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.ClientActivateLog;
import com.mas.ws.pojo.ClientUser;
import com.mas.ws.pojo.MasUser;

@XmlRootElement
public class ClientRequest extends BaseRequest{
	
	private Data data;
	
	private ClientUser clientInfo;
	
	private ClientActivateLog clientActive;
	
	private MasData masSdk;
	
	private MasUser masUser;
	
	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public ClientUser getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientUser clientInfo) {
		this.clientInfo = clientInfo;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public MasData getMasSdk() {
		return masSdk;
	}

	public void setMasSdk(MasData masSdk) {
		this.masSdk = masSdk;
	}

	public ClientActivateLog getClientActive() {
		return clientActive;
	}

	public void setClientActive(ClientActivateLog clientActive) {
		this.clientActive = clientActive;
	}
	
}
