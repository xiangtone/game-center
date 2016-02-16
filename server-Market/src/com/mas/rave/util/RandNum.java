package com.mas.rave.util;

import java.util.Random;
import java.util.UUID;

/**
 * rand num util
 * 
 * @author liwei.sz
 * 
 */
public class RandNum {
	public static void main(String[] args) {

	}

	/*
	 * get rand number
	 */
	public static String getRandNum() {
		UUID uid = UUID.randomUUID();
		String[] str = uid.toString().split("-");
		StringBuffer st = new StringBuffer();
		if (str != null && str.length > 0) {
			for (int i = 0; i < str.length; i++) {
				st.append(str[i]);
			}
		}
		return st.toString();
	}

	// 生成数字随机数
	public static String getRandNum1() {
		return System.currentTimeMillis() + "";
	}

	// 生成数字随机数
	public static String getRandNumStr() {
		StringBuffer st = new StringBuffer();
		st.append(System.currentTimeMillis());
		st.append(getRandNum().substring(10));
		return st.toString();
	}

	// 生成无重复文件名
	public static String randomFileName(String fileName) {
		String[] strs = fileName.split("\\.");
		String str = MD5.getMd5Value(System.currentTimeMillis() + fileName);
		try {
			if (strs[1] == null) {
				// 无后缀
				str += "." + "png";
			} else {
				str += "." + strs[strs.length - 1];
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			str += "." + "png";
		}
		return str;
	}

	// 生成随机密码
	public static String randomPwd(int num) {
		return getRandNum().substring(0, num);
	}

	// 获取随机数
	public static int randNum() {
		Random ran = new Random();
		return ran.nextInt(10000) * 100;
	}
	// 生成随机次数
	// c65880ed078f0a2611a81183b473eeec
}
