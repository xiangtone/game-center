package com.mas.rave.appannie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NameMappingSerivce {
	public static InputStream getPropertiesFile(){
		try {
			return ScoreManager.getResourceAsStream("nameMapping.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
	public static String getNameMapping(String name) throws IOException{
		BufferedReader read = null;
		String s = null;
		InputStream is =  getPropertiesFile();
		read = new BufferedReader(new InputStreamReader(is));
		while ((s = read.readLine()) != null) {
			String[] ss = s.split("=");
			if (ss.length == 2) {
				if(ss[0].equals(name)){
					return ss[1];
				}
			}
		}
		return null;
	}
	public static void main(String[] args) throws IOException {
		String name = "Fotos - Photo Overlapping";
		System.out.println(NameMappingSerivce.getNameMapping(name));
		
	}

}
