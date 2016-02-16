package com.mas.util;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
 
/**
 * 类说明：URL参数解析
 * @author 作者: hhk
 * @version 创建时间：2013-5-20 上午12:36:51
 */
@Service
public class URLAnalysis {
    private Map<String, String> paramMap = new HashMap<String, String>();
    public static XorPlus xorPlus= new XorPlus();
    
    
    public void analysis(String url) {
        paramMap.clear();
        if (!"".equals(url)) {// 如果URL不是空字符串
            url = url.substring(url.indexOf('?') + 1);
            String paramaters[] = url.split("&");
            for (String param : paramaters) {
                String values[] = param.split("=");
                if(values.length>1){
                	paramMap.put(values[0], values[1]);
                }
            }
        }
    }
    
    public void analysisWithDBase64(String url) {
    	paramMap.clear();
    	if (!"".equals(url)) {// 如果URL不是空字符串
    		url=getDecodeBase64String(url);
    		url = url.substring(url.indexOf('?') + 1);
    		String paramaters[] = url.split("&");
    		for (String param : paramaters) {
    			String values[] = param.split("=");
    			if(values.length>1){
    				paramMap.put(values[0], values[1]);
    			}
    		}
    	}
    }
    
    public void analysisWithXorBase64(String base64XorUrl) {
    	paramMap.clear();
    	String xorUrl=getDecodeBase64String(base64XorUrl);
    	String url=xorPlus.decrypt(xorUrl);
    	if (!"".equals(url)) {// 如果URL不是空字符串
    		url = url.substring(url.indexOf('?') + 1);
    		String paramaters[] = url.split("&");
    		for (String param : paramaters) {
    			String values[] = param.split("=");
    			if(values.length>1){
    				paramMap.put(values[0], values[1]);
    			}
    		}
    	}
    }
    
    public String getXorBase64(String url) {
    	String xorUrl=xorPlus.encrypt(url);
    	return Base64.encodeBase64URLSafeString(xorUrl.getBytes()); 
    }
 
    public String getParam(String name) {
        return paramMap.get(name);
    }

    public Long getLongParam(String name) {
    	return Long.valueOf(paramMap.get(name));
    }
    
    public Integer getIntegerParam(String name) {
    	return Integer.valueOf(paramMap.get(name));
    }

    
    public static String getBase64String(String url){
    	return Base64.encodeBase64URLSafeString(url.getBytes());
    }
    
    public static String getDecodeBase64String(String url){
    	return new String(Base64.decodeBase64(url));
    }
    
    public static void main(String[] args) {
//    	P2ltZWk9c2RzZHNmb2p3ZXFqb2pqcXdlMTEhJm5hbWU9aGVsZGRsbyZpZD0xMDA=
    	String testUrl = "IMEI=868033018069445&MAC=3c:43:8e:06:71:4e&CH=1001&CS=10011&BCH=11111&PH=18620180272&PV=XT7M&PV=PAIDaaaaaaaaaaaaaaaaaaa&NT=WIFI&SS=chinaunion&SW=300&SH=400" +
    			"";
    	/*xor加密*/
    	try {
			URLAnalysis urlAnalysis=new URLAnalysis();
//			加密的链接 
			String enStr=urlAnalysis.getXorBase64(testUrl);
			urlAnalysis.analysisWithXorBase64(enStr);
//			String 解密后链接=xorPlus.decrypt(加密的链接);
//			System.out.println(解密后链接);
//			URLAnalysis urlAnalysis = new URLAnalysis();
//			urlAnalysis.analysis(解密后链接);
	        System.out.println("SS = " + urlAnalysis.getParam("SS"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	/*非加密*/
//    	String a=Base64.encodeBase64URLSafeString(testUrl.getBytes());
//    	System.out.println("a:"+a+"长度:"+a.length());
//    	String b=new String(Base64.decodeBase64(a));
//    	System.out.println("b:"+b);
//        URLAnalysis urlAnalysis = new URLAnalysis();
//        urlAnalysis.analysis(testUrl);
//        System.out.println("PV = " + urlAnalysis.getParam("PV"));
//        System.out.println("SH = " + urlAnalysis.getParam("SH"));
//        Assert.notNull(urlAnalysis.getParam("SH"));
//        System.out.println("IMEI = " + urlAnalysis.getParam("IMEI"));
    }
/*    1.客户端加密登录方式说明
    例如：
    正常的链接 http://g.y6.cn/page/client/login?M=dasdasduoiasodadua79asd7dads.
    将http://g.y6.cn/page/client/login?M=_

    需要传的字段：
    IMEI-手机串号
    IMSI-手机卡串号
    MAC-手机网卡地址
    BCH-(BCH)盒子渠道编号(联运对应csId)
    PH-(phone)手机号码 18620180272
    CH-(channelId)社区的公共渠道号
    PV-(phoneVendor)手机产商
    VC-(versionCode)版本号
    SW-(screenWidth)屏幕宽
    SH-(screenHeight)屏幕高
    SS-(serviceSupplier)运营商类型
    NT-(netType)网络类型
    _为IMEI=868033018069445&MAC=3c:43:8e:06:71:4e&CH=1001&CS=10011&BCH=11111&PH=18620180272&PV=XT7M&PV=PAIDaaaaaaaaaaaaaaaaaaa&NT=WIFI&SS=chinaunion&SW=300&SH=400的base64加密串 （Base64.encodeBase64URLSafeString）
    服务端会根据这个解析*/
}