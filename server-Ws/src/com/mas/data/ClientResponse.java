package com.mas.data;
import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.MasUser;

@XmlRootElement
public class ClientResponse extends BaseResponse
{
	private Integer autoRaveId;
	public Integer getAutoRaveId() {
		return autoRaveId;
	}

	public void setAutoRaveId(Integer autoRaveId) {
		this.autoRaveId = autoRaveId;
	}

	private String sessionId;
	
	private Integer clientId;

	private HostData hosts;
	
	private MasUser masUser;

	private boolean islogin = false;
	private String loginInfo = "Automatic login fails";
	
	private Data data;
	private Boolean isUpgrade;
	
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public HostData getHosts() {
		return hosts;
	}

	public void setHosts(HostData hosts) {
		this.hosts = hosts;
	}

	public boolean isIslogin() {
		return islogin;
	}

	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}

	public String getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Boolean getIsUpgrade() {
		return isUpgrade;
	}

	public void setIsUpgrade(Boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

}
