package com.reportforms.stroage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.reportforms.stroage.service.StorageService;
import com.reportforms.util.Constant;
import com.reportforms.util.S3Proxy;
/**
 * 实现亚马逊s3接口
 * @author kun.shen
 *
 */
public class StrorageServiceS3Impl  implements StorageService{
	
	private  String image_url = Constant.S3_IMG_URL;
	
	private  String apk_url = Constant.S3_APK_URL;
	
	private  String bucket_name = Constant.S3_BUCKET;
	
	private  String apk_folder = "apk/";
	    
	private  String image_folder = "image/";
	
	@Override
	public String getImageUrl() {
		return image_url;
	}

	@Override
	public  String getApkUrl() {
		return apk_url;
	}
	
	@Override
	public  String getBucketName() {
		return bucket_name;
	}
	
	@Override
	public String getApkFolder() {
		return apk_folder;
	}

	@Override
	public String getImageFolder() {
		return image_folder;
	}
	
	@Override
	public void upload(InputStream inputStream, long length, String fileName,
			String folder) {
		
		 AWSCredentials awsCredentials = new BasicAWSCredentials("","");
		  
		  AmazonS3 s3 = new AmazonS3Client(awsCredentials);
	      String bucket_name = "";
	      
	      //设置资源的http头参数，用于cloudFront的识别
	      ObjectMetadata metadata = new ObjectMetadata();
	      metadata.setCacheControl(""); 
	      metadata.setContentLength(length);
	      
	      s3.putObject(new PutObjectRequest(bucket_name, folder+fileName, inputStream ,metadata));
	}

	@Override
	public void upload(File file, long length, String fileName, String folder) {
		try {
			this.upload(new FileInputStream(file), length, fileName, folder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
