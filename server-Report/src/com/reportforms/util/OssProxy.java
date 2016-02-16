package com.reportforms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyRep.Type;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.ObjectMetadata;

public class OssProxy {
	
	 private static final String ACCESS_ID = "1fpumj6d3egnzg1gj5y4lq8i";
	 private static final String ACCESS_KEY = "qi3586g7KqZt2BcKkiTXviE0At4=";
	 private static final String OSS_ENDPOINT = "http://oss.aliyuncs.com";
	 
	 public static void upload(File file,String ossPath,String contentType) throws FileNotFoundException, OSSException, ClientException {
		  String bucketName = Constant.OSS_BUCKETNAME;
		  ObjectMetadata objectMeta = new ObjectMetadata();
		  objectMeta.setContentLength(file.length());
		  objectMeta.setContentType(contentType);
		  OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
		  InputStream input = new FileInputStream(file);
	      client.putObject(bucketName, ossPath, input, objectMeta);
	     
		}	
	 
		public static void upload(InputStream inputStream, long length, String fileName,
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
		
	 public static void main(String[] args)throws Exception {
		 File apkLocalFile = new File("E:/1111.png");
		 //upload(apkLocalFile,"image/"+apkLocalFile.getName(),"image");
		 upload(new FileInputStream(apkLocalFile),apkLocalFile.length(),apkLocalFile.getName(),"image/");
	 }
}
