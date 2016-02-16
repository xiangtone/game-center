package com.mas.rave.test;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;

public class Test {
	public static void main(String[] args) {
		String view = "AVI,WMA,RMVB,RM,FLASH,MP,MID,3GP";
		System.out.println(view.toLowerCase().indexOf("rm"));

		System.out.println(FileUtil.getFileSuffix("aa.mp3"));
		System.out.println(MD5.getMd5Value("1111"));


		String st = "D:\\te\\te1\\Ringtones2.5.2.apk";
		String st1 = "D:\\te\\te2\\Ringtones2.5.3.apk";
		File file = new File(st);
		File file1 = new File(st1);
		try {
			FileUtils.moveFile(file, file1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
