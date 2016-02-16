package com.mas.rave.util.s3;

import java.util.ArrayList;
import java.util.List;

import com.mas.rave.util.Constant;

public class S3Util {

	public static void uploadS3(String fileUrl, boolean clearLocalFile){
		//System.out.println("uploadS3..."+fileUrl);
		if(!"true".equals(Constant.RUN_S3)){
			return;
		}
		List<String> fileUrlList = new ArrayList<String>();
		fileUrlList.add(fileUrl);
		//thread
		S3UploadTask task = new S3UploadTask(fileUrlList, clearLocalFile);
		task.start();
	}
	public static void uploadS3(List<String> fileUrlList, boolean clearLocalFile){
		if(!"true".equals(Constant.RUN_S3)){
			return;
		}
		//thread
		S3UploadTask task = new S3UploadTask(fileUrlList, clearLocalFile);
		task.start();
	}
	public static void deleteFile(String fileUrl){
		if(!"true".equals(Constant.RUN_S3)){
			return;
		}
		List<String> fileUrlList = new ArrayList<String>();
		fileUrlList.add(fileUrl);
		//thread
		S3DeleteTask task = new S3DeleteTask(fileUrlList);
		task.start();
	}
	public static void deleteFile(List<String> fileUrlList){
		if(!"true".equals(Constant.RUN_S3)){
			return;
		}
		//thread
		S3DeleteTask task = new S3DeleteTask(fileUrlList);
		task.start();
	}
}
