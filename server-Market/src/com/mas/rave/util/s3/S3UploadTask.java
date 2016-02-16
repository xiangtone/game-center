package com.mas.rave.util.s3;

import java.io.File;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.services.s3.transfer.Upload;
import com.mas.rave.util.Constant;

public class S3UploadTask extends Thread {

	private List<String> fileUrlList; //上传文件列表
	private boolean clearLocalFile;
	
	public S3UploadTask(List<String> fileUrlList, boolean clearLocalFile){
		this.fileUrlList=fileUrlList;
		this.clearLocalFile=clearLocalFile;
	}
	
	@Override
	public void run() {
		//System.out.println("S3UploadTask...");
		try{
			S3Service service = new S3Service();
			service.uploadFile(fileUrlList);
			//清理本地文件
			if(clearLocalFile){
				for(String s:fileUrlList){
					File f = new File(s);
					if(f.exists()){
						f.delete();
					}
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

}

