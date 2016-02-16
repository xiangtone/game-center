package com.mas.util;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
 
/**
 * @author 作者: hhk
 * @version 创建时间：2013-5-20 上午12:36:51
 */
@Service
public class XorBase64 {
    public static XorPlus xorPlus= new XorPlus();
    
    
    public static String getXorBase64(String date) {
    	String xorUrl=XorPlus.encrypt(date);
    	return Base64.encodeBase64URLSafeString(xorUrl.getBytes()); 
    }
    public static String getDecodeXorBase64(String date) {
    	String xorUrl=getDecodeBase64String(date);
    	return XorPlus.decrypt(xorUrl);
    }
    
    public static String getBase64String(String date){
    	return Base64.encodeBase64URLSafeString(date.getBytes());
    }
    
    public static String getDecodeBase64String(String date){
    	return new String(Base64.decodeBase64(date));
    }
    
    public static void main(String[] args) {
    	String testUrl = "假装我很怀旧aaaabbbbbbbbb930232943";
    	/*xor加密*/
    	try {
			//加密的链接 
			/*String enStr=XorBase64.getXorBase64(testUrl);
			String decodeStr = XorBase64.getDecodeXorBase64(enStr);
			System.out.println("enStr====:"+enStr+"  decodeStr===:"+decodeStr);*/
			
			
			String decodeMiStr = XorBase64.getDecodeXorBase64("ZGlgYGNbY35o");
			System.out.println("decodeMiStr===:"+decodeMiStr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}