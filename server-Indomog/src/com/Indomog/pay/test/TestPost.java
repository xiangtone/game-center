package com.Indomog.pay.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.Indomog.pay.utils.DateUtil;
import com.Indomog.pay.utils.MessageDigestUtil;
import com.Indomog.pay.utils.Util;

public class TestPost {

	 public static void main(String[] args) {
		 testIndomog();
	}
	 
	
	private static void testIndomog(){
		 String url = "http://dev.indomog.com/h2h_paywall/index.php";
	    	// 填入各个表单域的值
	   	    Map<String,Object> map = new HashMap<String,Object>();
	   	    Map<String,Object> mapRequest = new HashMap<String,Object>();
	   	    Map<String,Object> mapData = new HashMap<String,Object>();
	   	    String RMID = "0910403545";
	   	    mapData.put("RMID",RMID);
	 		String QID = System.currentTimeMillis()+"";
	 		mapData.put("QID",QID);
	 		String RC = "4103";
	 		mapData.put("RC",RC);
	 		String Alg = "ts";
	 		mapData.put("Alg",Alg);
	 		String AlgID = "ts_0811120006";
	 		mapData.put("AlgID",AlgID);
	 		String Name = "test";
	 		mapData.put("Name",Name);
	 		String EmailHP = "asep@indomog.com";
	 		mapData.put("EmailHP",EmailHP);
	 		String IPD = "10.128.188.137";
	 		mapData.put("IPD",IPD);
	 		Calendar date = Calendar.getInstance();
	 		date.setTime(new Date());
	 		date.add(Calendar.HOUR_OF_DAY, -1);
	 		String Now = DateUtil.formatDate(date.getTime(),DateUtil.DATE_FORMAT_1);
	 		mapData.put("Now",Now);
	 		mapRequest.put("Data", mapData);
	 		Map<String,Object> mapPayModes = new HashMap<String,Object>();
	 		String BMod = "MOGVCH";
	 		mapPayModes.put("BMod",BMod);
	 		String SC = "7872886123294850";
	 		mapPayModes.put("SC",SC);
	 		mapRequest.put("PayMode", mapPayModes);
	 		map.put("Request", mapRequest);
	 		
	 		String key = "123456";
	 		String sign =  RMID+QID+RC+Alg+AlgID+Name+EmailHP+IPD+Now+BMod+SC+key;
	 		System.out.println("  "+QID+" sign===:"+MessageDigestUtil.getSHA1(sign));
	 		map.put("Signature",MessageDigestUtil.getSHA1(sign));
	 		
	 		String js;
			try {
				js = Util.encodeJson(map);
				System.out.println(js);
				String str = Util.doPost(url, js);
				System.out.println(str);
				
				js = Util.encodeJson(map);
				System.out.println(js);
				str = Util.doPostJson(url, js);
				System.out.println(str);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
