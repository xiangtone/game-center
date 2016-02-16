package com.reportforms.stroage.service;

import java.io.File;
import java.io.InputStream;

public interface StorageService {
	
	public  void upload(InputStream inputStream,long length,String fileName,String folder);
	
	public  void upload(File file,long length,String fileName,String folder);
	
	public  String getImageUrl();
	
	public String getApkUrl();
	
	public String getBucketName();
	
	public String getApkFolder();
	
	public String getImageFolder();
}
