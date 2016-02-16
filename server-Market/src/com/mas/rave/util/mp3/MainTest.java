package com.mas.rave.util.mp3;

import java.io.File;

public class MainTest {
		public static void main(String args[]){
			com.mas.rave.util.mp3.MusicInfo musicInfo=new com.mas.rave.util.mp3.MusicInfo();
			if(musicInfo.hasImage("G:/Music/陈奕迅 - 任我行.mp3")){
				try {
					File file =	MusicInfo.writeImage(musicInfo.getImage(), "G:/resource/", "任我行");
					System.out.println(file.getAbsolutePath());
					System.out.println(file.getAbsolutePath());
					System.out.println(file.getAbsolutePath());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println("Monica&Ludacris-Still Standing.mp3没有图片");
			}
		}
}
