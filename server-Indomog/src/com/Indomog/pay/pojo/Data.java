package com.Indomog.pay.pojo;
import com.alibaba.fastjson.annotation.JSONField;

public class Data {

	private String RMID;
	private String QID;
	private String RC;
	private String Alg;
	private String AlgID;
	private String Name;
	private String EmailHP;
	private String IPD;
	private String Now;
	
	private String TrxID;
	private String TrxStatus;
	private String BID;
	private String TrxRC;
	private String TrxTime;
	private String TrxValue;
	private String RspCode;
	private String RspDesc;

	@JSONField(name="Now") 
	public String getNow(){
 		return Now;
	}
	@JSONField(name="Now") 
	public void setNow(String now) {
		Now = now;
	}
	@JSONField(name="RMID") 
	public String getRMID() {
		return RMID;
	}
	@JSONField(name="RMID") 
	public void setRMID(String rMID) {
		RMID = rMID;
	}
	@JSONField(name="QID") 
	public String getQID() {
		return QID;
	}
	@JSONField(name="QID") 
	public void setQID(String qID) {
		QID = qID;
	}
	@JSONField(name="RC") 
	public String getRC() {
		return RC;
	}
	@JSONField(name="RC") 
	public void setRC(String rC) {
		RC = rC;
	}
	@JSONField(name="Alg") 
	public String getAlg() {
		return Alg;
	}
	@JSONField(name="Alg") 
	public void setAlg(String alg) {
		Alg = alg;
	}
	@JSONField(name="AlgID") 
	public String getAlgID() {
		return AlgID;
	}
	@JSONField(name="AlgID") 
	public void setAlgID(String algID) {
		AlgID = algID;
	}
	@JSONField(name="Name") 
	public String getName() {
		return Name;
	}
	@JSONField(name="Name") 
	public void setName(String name) {
		Name = name;
	}
	@JSONField(name="EmailHP") 
	public String getEmailHP() {
		return EmailHP;
	}
	@JSONField(name="EmailHP") 
	public void setEmailHP(String emailHP) {
		EmailHP = emailHP;
	}
	@JSONField(name="IPD") 
	public String getIPD() {
		return IPD;
	}
	@JSONField(name="IPD") 
	public void setIPD(String iPD) {
		IPD = iPD;
	}
	@JSONField(name="TrxRC") 
	public String getTrxRC() {
		return TrxRC;
	}
	@JSONField(name="TrxRC") 
	public void setTrxRC(String trxRC) {
		TrxRC = trxRC;
	}
	@JSONField(name="TrxTime") 
	public String getTrxTime() {
		return TrxTime;
	}
	@JSONField(name="TrxTime") 
	public void setTrxTime(String trxTime) {
		TrxTime = trxTime;
	}
	@JSONField(name="RspCode") 
	public String getRspCode() {
		return RspCode;
	}
	@JSONField(name="RspCode") 
	public void setRspCode(String rspCode) {
		RspCode = rspCode;
	}
	@JSONField(name="RspDesc") 
	public String getRspDesc() {
		return RspDesc;
	}
	@JSONField(name="RspDesc") 
	public void setRspDesc(String rspDesc) {
		RspDesc = rspDesc;
	}
	@JSONField(name="TrxID")
	public String getTrxID() {
		return TrxID;
	}
	@JSONField(name="TrxID")
	public void setTrxID(String trxID) {
		TrxID = trxID;
	}
	@JSONField(name="TrxStatus")
	public String getTrxStatus() {
		return TrxStatus;
	}
	@JSONField(name="TrxStatus")
	public void setTrxStatus(String trxStatus) {
		TrxStatus = trxStatus;
	}
	@JSONField(name="BID")
	public String getBID() {
		return BID;
	}
	@JSONField(name="BID")
	public void setBID(String bID) {
		BID = bID;
	}
	@JSONField(name="TrxValue")
	public String getTrxValue() {
		return TrxValue;
	}
	@JSONField(name="TrxValue")
	public void setTrxValue(String trxValue) {
		TrxValue = trxValue;
	}
	@Override
	public String toString() {
		return "Data [RMID=" + RMID + ", QID=" + QID + ", RC=" + RC + ", Alg="
				+ Alg + ", AlgID=" + AlgID + ", Name=" + Name + ", EmailHP="
				+ EmailHP + ", IPD=" + IPD + ", Now=" + Now + ", TrxID="
				+ TrxID + ", TrxStatus=" + TrxStatus + ", BID=" + BID
				+ ", TrxRC=" + TrxRC + ", TrxTime=" + TrxTime + ", TrxValue="
				+ TrxValue + ", RspCode=" + RspCode + ", RspDesc=" + RspDesc
				+ "]";
	}
}
