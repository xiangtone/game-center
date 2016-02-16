package com.mas.rave.util;

import java.util.HashMap;
import java.util.Map;

public class StringEncoder {
	private static Map<String, String> referencesMap = new HashMap<String, String>();

	static {
		referencesMap.put("'", "\\'");
		referencesMap.put("\"", "\\\"");
		referencesMap.put("\\", "\\\\");

		referencesMap.put("\n", "\\\n");
		referencesMap.put("\0", "\\\0");
		referencesMap.put("\b", "\\\b");
		referencesMap.put("\r", "\\\r");
		referencesMap.put("\t", "\\\t");
		referencesMap.put("\f", "\\\f");
	}
	 public static String stringToJson(String s) {    
         StringBuffer sb = new StringBuffer ();     
         for (int i=0; i<s.length(); i++) {     
       
             char c = s.charAt(i);     
             switch (c) {     
             case '\"':     
                 sb.append("\\\"");     
                 break;     
//             case '\\':   //如果不处理单引号，可以释放此段代码，若结合下面的方法处理单引号就必须注释掉该段代码
//                 sb.append("\\\\");     
//                 break;     
             case '/':     
                 sb.append("\\/");     
                 break;     
             case '\b':      //退格
                 sb.append("\\b");     
                 break;     
             case '\f':      //走纸换页
                 sb.append("\\f");     
                 break;     
             case '\n':     
                 sb.append("\\n"); //换行    
                 break;     
             case '\r':      //回车
                 sb.append("\\r");     
                 break;     
             case '\t':      //横向跳格
                 sb.append("\\t");     
                 break;     
             default:     
                 sb.append(c);    
             }}
         return sb.toString();     
      }
	 public static String stringToLikeMysql(String s) {    
         StringBuffer sb = new StringBuffer ();     
         for (int i=0; i<s.length(); i++) {     
       
             char c = s.charAt(i);     
             switch (c) {     
             case '%':     
                 sb.append("[").append(c).append("]");     
                 break;     
             case '\'':   
                 sb.append("[").append(c).append("]");     
                 break;     
             case '[':     
                 sb.append("[").append(c).append("]");     
                 break;     
             case ']':
                 sb.append("[").append(c).append("]");     
                 break;     
             case '_':     
                 sb.append("[").append(c).append("]");     
                 break;     
             case '.': 
                 sb.append("[").append(c).append("]");     
                 break;              
             default:     
                 sb.append(c);    
             }}
         return sb.toString();     
      }
	// escape sql tag with the source code.
	public static String encode(String source) {
		if (source == null)
			return "";

		StringBuffer sbuffer = new StringBuffer(source.length());

		for (int i = 0; i < source.length(); i++) {
			String c = source.substring(i, i + 1);

			// System.out.println("c=" + c);
			// System.out.println(referencesMap.get(c));

			if (referencesMap.get(c) != null) {
				sbuffer.append(referencesMap.get(c));
			} else {
				sbuffer.append(c);
			}
		}
		return sbuffer.toString();
	}

	public static void main(String[] args) {
		String tt = encode("Lep's World 3");
		System.out.print(tt);
	}
}
