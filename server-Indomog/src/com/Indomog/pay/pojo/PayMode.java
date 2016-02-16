package com.Indomog.pay.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class PayMode {

	private String BMod;
	private String SC;
	private String ProdID;
	private String SN;
	private String Val;
	private String TransferAmt;
	private String URL;
	
	@JSONField(name="BMod")
	public String getBMod() {
		return BMod;
	}
	@JSONField(name="BMod")
	public void setBMod(String bMod) {
		BMod = bMod;
	}
	@JSONField(name="SC")
	public String getSC() {
		return SC;
	}
	@JSONField(name="SC")
	public void setSC(String sC) {
		SC = sC;
	}
	@JSONField(name="ProdID")
	public String getProdID() {
		return ProdID;
	}
	@JSONField(name="ProdID")
	public void setProdID(String prodID) {
		ProdID = prodID;
	}
	@JSONField(name="SN")
	public String getSN() {
		return SN;
	}
	@JSONField(name="SN")
	public void setSN(String sN) {
		SN = sN;
	}
	@JSONField(name="Val")
	public String getVal() {
		return Val;
	}
	@JSONField(name="Val")
	public void setVal(String val) {
		Val = val;
	}
	@JSONField(name="TransferAmt")
	public String getTransferAmt() {
		return TransferAmt;
	}
	@JSONField(name="TransferAmt")
	public void setTransferAmt(String transferAmt) {
		TransferAmt = transferAmt;
	}
	@JSONField(name="URL")
	public String getURL() {
		return URL;
	}
	@JSONField(name="URL")
	public void setURL(String uRL) {
		URL = uRL;
	}
	@Override
	public String toString() {
		return "PayMode [BMod=" + BMod + ", SC=" + SC + ", ProdID=" + ProdID
				+ ", SN=" + SN + ", Val=" + Val + ", TransferAmt="
				+ TransferAmt + ", URL=" + URL + "]";
	}
}
