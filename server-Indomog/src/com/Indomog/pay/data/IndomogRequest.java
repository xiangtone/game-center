package com.Indomog.pay.data;

import com.Indomog.pay.pojo.Request;
import com.Indomog.pay.type.RcEnum;
import com.Indomog.pay.utils.MessageDigestUtil;
import com.alibaba.fastjson.annotation.JSONField;

public class IndomogRequest {

	private String Signature;
	
	private Request Request;

	public void setSignature(String RC,String key) {
		if(RcEnum.Inquiry.getRC().equals(RC)){
			Signature = Request.getData().getRMID()+
						Request.getData().getQID()+
						Request.getData().getRC()+
						Request.getData().getAlg()+
						Request.getData().getAlgID()+
						Request.getData().getName()+
						Request.getData().getEmailHP()+
						Request.getData().getIPD()+
						Request.getData().getNow()+
						Request.getPayMode().getBMod()+
						Request.getPayMode().getSC()+
						key;
		}else if(RcEnum.Redeem.getRC().equals(RC)){
			Signature = Request.getData().getRMID()+
					Request.getData().getQID()+
					Request.getData().getRC()+
					Request.getData().getAlg()+
					Request.getData().getAlgID()+
					Request.getData().getName()+
					Request.getData().getEmailHP()+
					Request.getData().getIPD()+
					Request.getData().getNow()+
					Request.getPayMode().getBMod()+
					Request.getPayMode().getSC()+
					key;
		}else if(RcEnum.Refund.getRC().equals(RC)){
			Signature = Request.getData().getRMID()+
					Request.getData().getQID()+
					Request.getData().getRC()+
					Request.getData().getAlg()+
					Request.getData().getAlgID()+
					Request.getData().getNow()+
					key;
		}else if(RcEnum.Verify.getRC().equals(RC)){
			Signature = Request.getData().getRMID()+
					Request.getData().getQID()+
					Request.getData().getRC()+
					Request.getData().getAlg()+
					Request.getData().getAlgID()+
					Request.getData().getNow()+
					key;
		}
		Signature = MessageDigestUtil.getSHA1(Signature);
	}
	@JSONField(name="Signature")
	public String getSignature() {
		return Signature;
	}
	@JSONField(name="Request")
	public Request getRequest() {
		return Request;
	}

	public void setRequest(Request request) {
		Request = request;
	}
	
}
