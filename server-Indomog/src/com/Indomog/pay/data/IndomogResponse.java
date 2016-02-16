package com.Indomog.pay.data;
import com.Indomog.pay.pojo.Response;
import com.alibaba.fastjson.annotation.JSONField;

public class IndomogResponse {

	private String Signature;
	
	private Response Response;

	private String Certificate;
	
	@JSONField(name="Signature")
	public String getSignature() {
		return Signature;
	}
	@JSONField(name="Signature")
	public void setSignature(String signature) {
		Signature = signature;
	}
	@JSONField(name="Response")
	public Response getResponse() {
		return Response;
	}
	@JSONField(name="Response")
	public void setResponse(Response response) {
		Response = response;
	}
	@JSONField(name="Certificate")
	public String getCertificate() {
		return Certificate;
	}
	@JSONField(name="Certificate")
	public void setCertificate(String certificate) {
		Certificate = certificate;
	}

	@Override
	public String toString() {
		return "IndomogResponse [Signature=" + Signature + ", Response="
				+ Response + ", Certificate=" + Certificate + "]";
	}
	
}
