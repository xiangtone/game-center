package com.x.publics.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XorPlus {
	
	static String secret = "masaminemasamine";

	public static String encrypt(String mingwen) {
		char str[] = mingwen.toCharArray();
		int k;
		for (k = 0; k < str.length; k++) {
			str[k] = (char) (str[k] ^ secret.charAt(k % 8));
		}
		return new String(str);
	}

	public static String decrypt(String miwen) {
		char str[] = miwen.toCharArray();
		for (int k = 0; k < str.length; k++) {
			str[k] = (char) (str[k] ^ secret.charAt(k % 8));
		}
		String t = new String(str);
		return t;
	}

	public static String inputStreamDecrypt(InputStream is) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return decrypt(buffer.toString());
	}
}