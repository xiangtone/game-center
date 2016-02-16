package com.x.business.localapp.sort;

import com.x.business.localapp.sort.HanziToPinyin.Token;

import java.util.ArrayList;

public class PinYin {
	//汉字返回拼音，字母原样返回，都转换为小写
	public static String getPinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toUpperCase();
	}
	
	public static String getSortLetter(String appName){
		try {
			//汉字转换成拼音
			String pinyin = getPinYin(appName);
			String sortString = pinyin.substring(0, 1);
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				return sortString.toUpperCase();
			} else {
				return "#";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "#";
		}
	}
}
