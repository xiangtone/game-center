package com.mas.rave.util;

import java.io.UnsupportedEncodingException;

/**
 * 对于CU进行处理
 * @author kun.shen
 *
 */
public class CU{
	/**
	 * 截取第10,11位获取国家
	 * @param cu
	 * @return
	 */
	public static String getCountry(String cu){
		String cu_str = binstrToStr(getCu(cu));
		return cu_str.substring(9, 11);
	} 
	public static String getCuString(String cu){
		return binstrToStr(getCu(cu));
	}
	/**
	 * 取反运算，获取真实cu
	 * @param cu
	 * @return
	 */
	public static String getCu(String cu){
		if(cu==null||"".equals(cu)){
			return null;
		}
		String cu_buffer = "";
		for(char c:cu.toCharArray()){
	    	if(c == '1'){
	    		cu_buffer  += 0;
	    	}else if(c == '0'){
	    		cu_buffer  += 1;
	    	}else{
	    		cu_buffer += c;
	    	}
		}
		return cu_buffer;
	}
	public static void main(String[] args) throws UnsupportedEncodingException{
		String s = "1100011 1100010";
		
	}
	
	//将字符串转换成二进制字符串，以空格相隔
    private static String StrToBinstr(String str) {
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=Integer.toBinaryString(strChar[i])+ " ";
        }
        return result;
    }
    
    //将二进制字符串转换成Unicode字符串
    private static String binstrToStr(String binStr) {
        String[] tempStr=strToStrArray(binStr);
        char[] tempChar=new char[tempStr.length];
        for(int i=0;i<tempStr.length;i++) {
            tempChar[i]=binstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }
    
    //将二进制字符串转换为char
    private static char binstrToChar(String binStr){
        int[] temp=binstrToIntArray(binStr);
        int sum=0;   
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }   
        return (char)sum;
    }
    
    //将二进制字符串转换成int数组
    private static int[] binstrToIntArray(String binStr) {       
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];   
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
    
    //将初始二进制字符串转换成字符串数组，以空格相隔
    private static String[] strToStrArray(String str) {
        return str.split(" ");
    }
}
