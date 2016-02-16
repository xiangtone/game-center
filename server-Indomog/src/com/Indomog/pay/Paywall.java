package com.Indomog.pay;
import com.Indomog.pay.conf.IndomogConfig;
import com.Indomog.pay.data.IndomogRequest;
import com.Indomog.pay.data.IndomogResponse;
import com.Indomog.pay.pojo.Data;
import com.Indomog.pay.pojo.PayMode;
import com.Indomog.pay.pojo.Request;
import com.Indomog.pay.pojo.Response;
import com.Indomog.pay.type.RcEnum;
import com.Indomog.pay.utils.Util;
import com.alibaba.fastjson.JSON;

public class Paywall extends IndomogConfig{

	public static void main(String[] args) {
		//System.out.println(RcEnum.Inquiry.getRC());
		//-------------test--------
		Inquiry("7471886029301200",50000,System.currentTimeMillis()+"");
		//-------------formal---------
		//Inquiry("2375821079461650",20000,System.currentTimeMillis()+"");
	}
	public static IndomogResponse Inquiry(String SC,String QID){
		return Inquiry(SC,0,QID);
	}
	public static IndomogResponse Inquiry(String SC,Integer Val,String QID){
		Data data = new Data();
		data.setRMID(RMID);
		data.setQID(QID);
		data.setRC(RcEnum.Inquiry.getRC());
		data.setAlg(Alg);
		data.setAlgID(AlgID);
		data.setName(Name);
		data.setEmailHP(EmailHP);
		data.setIPD(IPD);
		String Now = Util.getLastHourTime();
		data.setNow(Now);
		PayMode payMode = new PayMode();
		payMode.setBMod("MOGVCH");
		payMode.setSC(SC);
		Request request = new Request();
		request.setData(data);
		request.setPayMode(payMode);
		IndomogRequest indomogRequest = new IndomogRequest();
		indomogRequest.setRequest(request);
		indomogRequest.setSignature(RcEnum.Inquiry.getRC(),secretkey);
		String js = JSON.toJSONString(indomogRequest);
		String str = Util.doPostJson(url, js);
		System.out.println(str);
		if("timeOut".equals(str)){
			IndomogResponse indomogResponse = new IndomogResponse();
			Response response = new Response();
			response.setData(new Data());
			response.getData().setRspCode("004");
			response.getData().setRspDesc(Util.getIndomogCode("004"));
			indomogResponse.setResponse(response);
			return indomogResponse;
		}else{
			IndomogResponse indomogResponse = JSON.parseObject(str, IndomogResponse.class);
			if("000".equals(indomogResponse.getResponse().getData().getRspCode())){
				String mogValue = indomogResponse.getResponse().getPayMode().getVal();
				if(0!=Val.intValue() && Val.intValue()!=Integer.parseInt(mogValue)){
					indomogResponse.getResponse().getData().setRspCode("003");
					indomogResponse.getResponse().getData().setRspDesc("Please choose the right value.Voucher:"+mogValue+" is not the same Choose value:"+Val);
				}else{
					indomogResponse = redeem(indomogRequest);
				}
			}
			indomogResponse.getResponse().getData().setNow(Now);
			return indomogResponse;
		}
	}
	public static IndomogResponse redeem(IndomogRequest redeemRequest){
		
		redeemRequest.getRequest().getData().setRC(RcEnum.Redeem.getRC());
		redeemRequest.setSignature(RcEnum.Redeem.getRC(),secretkey);
		String js = JSON.toJSONString(redeemRequest);
		String str = Util.doPostJson(url, js);
		if("timeOut".equals(str)){
			return verify(redeemRequest);
		}
		IndomogResponse indomogResponse = JSON.parseObject(str, IndomogResponse.class);
		return indomogResponse;
	}
	
	public static IndomogResponse verify(IndomogRequest redeemRequest){
		Data data = new Data();
		data.setRMID(redeemRequest.getRequest().getData().getRMID());
		data.setQID(redeemRequest.getRequest().getData().getQID());
		data.setRC(RcEnum.Verify.getRC());
		data.setAlg(redeemRequest.getRequest().getData().getAlg());
		data.setAlgID(redeemRequest.getRequest().getData().getAlgID());
		Request request = new Request();
		request.setData(data);
		IndomogRequest indomogRequest = new IndomogRequest();
		indomogRequest.setRequest(request);
		indomogRequest.setSignature(RcEnum.Verify.getRC(),secretkey);
		String js = JSON.toJSONString(indomogRequest);
		String str = Util.doPostJson(url, js);
		if("timeOut".equals(str)){
			IndomogResponse indomogResponse = new IndomogResponse();
			Response response = new Response();
			response.setData(new Data());
			response.getData().setRspCode("004");
			response.getData().setRspDesc(Util.getIndomogCode("004"));
			indomogResponse.setResponse(response);
			return indomogResponse;
		}else{
			IndomogResponse indomogResponse = JSON.parseObject(str, IndomogResponse.class);
			return indomogResponse;
		}
	}
}
