package com.mas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class XorPlus {
	public static void main(String args[]) throws IOException {
		// BufferedReader stdin=new BufferedReader(new
		// InputStreamReader(System.in));
		System.out.print("请输入加密内容：");
		String mingwen = "加密这个abc";
		// String secret="天行健君自强不息";
		String secret = "masmasmas";
		char str[] = mingwen.toCharArray();
		int k;
		for (k = 0; k < str.length; k++) {
			str[k] = (char) (str[k] ^ secret.charAt(k % 8));
		}

		String s = new String(str);
		System.out.println("暗文：" + s);

		for (k = 0; k < str.length; k++) {
			str[k] = (char) (str[k] ^ secret.charAt(k % 8));
		}
		String t = new String(str);
		System.out.println("明文：" + t);
		
		
		String miwen = encrypt("测试测试测试若有若无fdfasf");
		String mingwe = decrypt(miwen);
		System.out.println(miwen+"       "+mingwe);
	}

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