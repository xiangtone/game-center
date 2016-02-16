package com.reportforms.stroage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.reportforms.stroage.service.StorageService;
import com.reportforms.util.Constant;

public class StrorageServiceOssImpl implements StorageService{
	
	private  String image_url = Constant.OSS_IMG_URL;
	
    private  String apk_url = Constant.OSS_APK_URL;
	
    private  String bucket_name = Constant.OSS_BUCKETNAME;
    
    private  String apk_folder = "app/";
    
    private  String image_folder = "image/";
	
	 @Override
	public String getImageUrl() {
		return image_url;
	}

	@Override
	public String getApkUrl() {
		return apk_url;
	}

	@Override
	public String getBucketName() {
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

	private static final String ACCESS_ID = "";
	 private static final String ACCESS_KEY = "";
	 private static final String OSS_ENDPOINT = "";
	 
	@Override
	public void upload(InputStream inputStream, long length, String fileName,
			String folder) {
		
		  String bucketName = Constant.OSS_BUCKETNAME;
		  ObjectMetadata metadata = new ObjectMetadata();
		  metadata.setContentLength(length);
		  metadata.setCacheControl("max-age=8640000"); 
		  
		  OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
	      try {
			client.putObject(bucketName,  folder+fileName, inputStream, metadata);
		} catch (OSSException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
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
