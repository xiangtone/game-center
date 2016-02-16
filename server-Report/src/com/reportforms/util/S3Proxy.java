package com.reportforms.util;

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

public class S3Proxy {
	 
	 public static void upload(InputStream inputStream,long length,String fileName,String folder) throws Exception {
		 
		  AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAJEI6ITIVJDWKIBDQ","gEGo/XyI/WhlhH+I66YI2IRY++crnvr1hk7SQGDa");
		  
		  AmazonS3 s3 = new AmazonS3Client(awsCredentials);
	      String bucket_name = "onetouch_store";
	    
	      ObjectMetadata metadata = new ObjectMetadata();
	      metadata.setCacheControl("max-age=8640000"); 
	      metadata.setContentLength(length);
	      
//	      s3.putObject(new PutObjectRequest(bucket_name, bucket_folder+"AndroidAnt.apk", new File("F://AndroidAnt.apk")));
//	      S3Object s3Object = s3.getObject(bucket_name,bucket_folder+"AndroidAnt.apk");
//	      s3Object.setObjectMetadata(metadata);
	      s3.putObject(new PutObjectRequest(bucket_name, folder+fileName, inputStream ,metadata));
	      
		}	
	 
	 public static void main(String[] args) throws Exception{
		 File file = new File("F:/Chaatz/AppIcon_800x800.png");
		 S3Proxy.upload(new FileInputStream(file), file.length(), "", "image/AppIcon_800x800.png");
	 }
}
