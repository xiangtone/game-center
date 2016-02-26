package com.x.business.zerodata.connection.helper;

public class ServerDeviceModel {

	public String SSID ;
	public String BSSID ;
	public int adapterLayout ;
	public String getSSID() {
		return SSID;
	}
	public void setSSID(String sSID) {
		SSID = sSID;
	}
	public String getBSSID() {
		return BSSID;
	}
	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}
	public int getAdapterLayout() {
		return adapterLayout;
	}
	public void setAdapterLayout(int adapterLayout) {
		this.adapterLayout = adapterLayout;
	}
	
	@Override
	public String toString() {
		return "ServerDeviceModel [SSID=" + SSID + ", BSSID=" + BSSID
				+ ", adapterLayout=" + adapterLayout + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BSSID == null) ? 0 : BSSID.hashCode());
		result = prime * result + ((SSID == null) ? 0 : SSID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerDeviceModel other = (ServerDeviceModel) obj;
		if (BSSID == null) {
			if (other.BSSID != null)
				return false;
		} else if (!BSSID.equals(other.BSSID))
			return false;
		if (SSID == null) {
			if (other.SSID != null)
				return false;
		} else if (!SSID.equals(other.SSID))
			return false;
		return true;
	}
	
	
	
}
