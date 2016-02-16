package com.mas.rave.test;

import com.mas.rave.util.Constant;
import com.mas.rave.util.MD5;

public class pngTest {
	public static void main(String[] args) {
		print();
	}

	// 测试文件
	public static void print() {
		String st = Constant.LOCAL_FILE_PATH + "/"+"100000";
		System.out.println(st.substring(Constant.LOCAL_FILE_PATH.length(),
				st.length()));
	}

	public static void png() {
		String fileName = "1";
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
		System.out.println(str);
	}
}
