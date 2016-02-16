package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class PayRechargeFailing implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String orderId;

    /**
     * 充值面额
     */
    private Integer rechargeValue;

    /**
     * 充值类型
     */
    private String rechargeType;

    /**
     * 充值兑换成的A币总额（包括赠送）
     */
    private Integer exchangeAValue;

    /**
     * 帐号aValue值
     */
    private Integer aValue;

    /**
     * 帐号赠送送A币值
     */
    private Integer aValuePresent;

    /**
     * 卡号
     */
    private String cardNo;

    private Date createTime;

    private String IP;

    /**
     * 客户端编号
     */
    private Integer clientId;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 帐号
     */
    private String userName;

    private String userPwd;

    private Integer appId;

    private String apkKey;

    private Integer cpId;

    private Integer serverId;

    private Integer channelId;

    /**
     * session值
     */
    private String sessionId;

    /**
     * 备注
     */
    private String remark;

    /**
     * Now调用印尼支付接口服务器时间
     */
    private String now;

    /**
     * RMID
     */
    private String RMID;

    /**
     * QID值
     */
    private String QID;

    /**
     * RspCode
     */
    private String rspCode;

    /**
     * RspDesc
     */
    private String rspDesc;

    /**
     * TrxID
     */
    private String trxID;

    /**
     * TrxStatus
     */
    private String trxStatus;

    /**
     * BID
     */
    private String BID;

    /**
     * TrxRC
     */
    private String trxRC;

    /**
     * TrxTime
     */
    private String trxTime;

    /**
     * TrxValue
     */
    private String trxValue;

    /**
     * BMod
     */
    private String BMod;

    /**
     * ProdID
     */
    private String prodID;

    /**
     * SN
     */
    private String SN;

    /**
     * Val
     */
    private String val;

    /**
     * Signature
     */
    private String signature;

    /**
     * Certificate
     */
    private String certificate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return 充值面额
     */
    public Integer getRechargeValue() {
        return rechargeValue;
    }

    /**
     * @param rechargevalue 
	 *            充值面额
     */
    public void setRechargeValue(Integer rechargeValue) {
        this.rechargeValue = rechargeValue;
    }

    /**
     * @return 充值类型
     */
    public String getRechargeType() {
        return rechargeType;
    }

    /**
     * @param rechargetype 
	 *            充值类型
     */
    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    /**
     * @return 充值兑换成的A币总额（包括赠送）
     */
    public Integer getExchangeAValue() {
        return exchangeAValue;
    }

    /**
     * @param exchangeavalue 
	 *            充值兑换成的A币总额（包括赠送）
     */
    public void setExchangeAValue(Integer exchangeAValue) {
        this.exchangeAValue = exchangeAValue;
    }

    /**
     * @return 帐号aValue值
     */
    public Integer getaValue() {
        return aValue;
    }

    /**
     * @param avalue 
	 *            帐号aValue值
     */
    public void setaValue(Integer aValue) {
        this.aValue = aValue;
    }

    /**
     * @return 帐号赠送送A币值
     */
    public Integer getaValuePresent() {
        return aValuePresent;
    }

    /**
     * @param avaluepresent 
	 *            帐号赠送送A币值
     */
    public void setaValuePresent(Integer aValuePresent) {
        this.aValuePresent = aValuePresent;
    }

    /**
     * @return 卡号
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * @param cardno 
	 *            卡号
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * @return 客户端编号
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientid 
	 *            客户端编号
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return 用户编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userid 
	 *            用户编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return 帐号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param username 
	 *            帐号
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getApkKey() {
        return apkKey;
    }

    public void setApkKey(String apkKey) {
        this.apkKey = apkKey;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return session值
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionid 
	 *            session值
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark 
	 *            备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return Now调用印尼支付接口服务器时间
     */
    public String getNow() {
        return now;
    }

    /**
     * @param now 
	 *            Now调用印尼支付接口服务器时间
     */
    public void setNow(String now) {
        this.now = now;
    }

    /**
     * @return RMID
     */
    public String getRMID() {
        return RMID;
    }

    /**
     * @param rmid 
	 *            RMID
     */
    public void setRMID(String RMID) {
        this.RMID = RMID;
    }

    /**
     * @return QID值
     */
    public String getQID() {
        return QID;
    }

    /**
     * @param qid 
	 *            QID值
     */
    public void setQID(String QID) {
        this.QID = QID;
    }

    /**
     * @return RspCode
     */
    public String getRspCode() {
        return rspCode;
    }

    /**
     * @param rspcode 
	 *            RspCode
     */
    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    /**
     * @return RspDesc
     */
    public String getRspDesc() {
        return rspDesc;
    }

    /**
     * @param rspdesc 
	 *            RspDesc
     */
    public void setRspDesc(String rspDesc) {
        this.rspDesc = rspDesc;
    }

    /**
     * @return TrxID
     */
    public String getTrxID() {
        return trxID;
    }

    /**
     * @param trxid 
	 *            TrxID
     */
    public void setTrxID(String trxID) {
        this.trxID = trxID;
    }

    /**
     * @return TrxStatus
     */
    public String getTrxStatus() {
        return trxStatus;
    }

    /**
     * @param trxstatus 
	 *            TrxStatus
     */
    public void setTrxStatus(String trxStatus) {
        this.trxStatus = trxStatus;
    }

    /**
     * @return BID
     */
    public String getBID() {
        return BID;
    }

    /**
     * @param bid 
	 *            BID
     */
    public void setBID(String BID) {
        this.BID = BID;
    }

    /**
     * @return TrxRC
     */
    public String getTrxRC() {
        return trxRC;
    }

    /**
     * @param trxrc 
	 *            TrxRC
     */
    public void setTrxRC(String trxRC) {
        this.trxRC = trxRC;
    }

    /**
     * @return TrxTime
     */
    public String getTrxTime() {
        return trxTime;
    }

    /**
     * @param trxtime 
	 *            TrxTime
     */
    public void setTrxTime(String trxTime) {
        this.trxTime = trxTime;
    }

    /**
     * @return TrxValue
     */
    public String getTrxValue() {
        return trxValue;
    }

    /**
     * @param trxvalue 
	 *            TrxValue
     */
    public void setTrxValue(String trxValue) {
        this.trxValue = trxValue;
    }

    /**
     * @return BMod
     */
    public String getBMod() {
        return BMod;
    }

    /**
     * @param bmod 
	 *            BMod
     */
    public void setBMod(String BMod) {
        this.BMod = BMod;
    }

    /**
     * @return ProdID
     */
    public String getProdID() {
        return prodID;
    }

    /**
     * @param prodid 
	 *            ProdID
     */
    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    /**
     * @return SN
     */
    public String getSN() {
        return SN;
    }

    /**
     * @param sn 
	 *            SN
     */
    public void setSN(String SN) {
        this.SN = SN;
    }

    /**
     * @return Val
     */
    public String getVal() {
        return val;
    }

    /**
     * @param val 
	 *            Val
     */
    public void setVal(String val) {
        this.val = val;
    }

    /**
     * @return Signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature 
	 *            Signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return Certificate
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * @param certificate 
	 *            Certificate
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}