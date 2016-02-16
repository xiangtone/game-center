package com.mas.rave.util.s3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


public class S3Service {

	public static String S3_FAIL_URL_FILE="s3_fail_url_file.txt";
	/**
	 * 批量上传文件
	 * @param fileUrlList 文件列表
	 */
	public void uploadFile(List<String> fileUrlList){
		String bucketName=Constant.S3_BUCKET;
		AmazonS3 s3 = createAmazonS3Client(bucketName, Regions.AP_SOUTHEAST_1);
		if(s3==null){
			System.out.println("Exception:s3 is null.exit!");
			return;
		}
		System.out.println("begin upload to S3...");
		for(String s:fileUrlList){
			s = convertKey(s);
			try{
				uploadFile(s3,bucketName,s,Constant.LOCAL_FILE_PATH+s);
//				uploadFileThread(s3,bucketName,s,Constant.LOCAL_FILE_PATH+s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void uploadFile(String fileUrl){
		String bucketName=Constant.S3_BUCKET;
		AmazonS3 s3 = createAmazonS3Client(bucketName, Regions.AP_SOUTHEAST_1);
		if(s3==null){
			System.out.println("Exception:s3 is null.exit!");
			return;
		}
		//System.out.println("begin upload...");
		fileUrl = convertKey(fileUrl);
		try{
			uploadFile(s3,bucketName,fileUrl,Constant.LOCAL_FILE_PATH+fileUrl);
//			uploadFileThread(s3,bucketName,fileUrl,Constant.LOCAL_FILE_PATH+fileUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteFile(String fileUrl){
		String bucketName = Constant.S3_BUCKET;
		fileUrl = convertKey(fileUrl);
		try {
			AmazonS3 s3 = createAmazonS3Client(bucketName, Regions.AP_SOUTHEAST_1);
			if(s3==null){
				System.out.println("Exception:s3 is null.exit!");
				return;
			}
			s3.deleteObject(bucketName, fileUrl);
			System.out.println(fileUrl+" is success delete from S3.");
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	public void deleteFile(List<String> fileUrlList){
		String bucketName = Constant.S3_BUCKET;
		try {
			AmazonS3 s3 = createAmazonS3Client(bucketName, Regions.AP_SOUTHEAST_1);
			if(s3==null){
				System.out.println("Exception:s3 is null.exit!");
				return;
			}
			for(String fileUrl:fileUrlList){
				fileUrl = convertKey(fileUrl);
				s3.deleteObject(bucketName, fileUrl);
				System.out.println(fileUrl+" is success delete from S3.");				
			}
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	public String convertKey(String s){
		if(s!=null && (s.startsWith("/") || s.startsWith("\\")) ){
			s = s.substring(1);
		}
		return s.replace("\\", "/");
	}
	private void uploadFile(AmazonS3 s3,String bucketName,String s3Path,String localPath){
		 try {
			 s3.putObject(new PutObjectRequest(bucketName, s3Path, new File(localPath)).withCannedAcl(CannedAccessControlList.PublicRead));
			 System.out.println(s3Path+" is success upload to s3.");
	        } catch (Exception e) {
	        	System.out.println("###Exception:"+s3Path);
	        	fileWriter(Constant.LOCAL_FILE_PATH+S3_FAIL_URL_FILE, s3Path);
	        	e.printStackTrace();
	        }
	}
	private void uploadFileThread(AmazonS3 s3,String bucketName,String s3Path,String localPath){
		TransferManager tm = new TransferManager(s3);  
		TransferManagerConfiguration conf = tm.getConfiguration();  
		  
		int threshold = 4 * 1024 * 1024;  
		conf.setMultipartUploadThreshold(threshold);  
		tm.setConfiguration(conf);  
		  
		try{
			Upload upload = tm.upload(bucketName, s3Path, new File(localPath));  
			TransferProgress p = upload.getProgress();  

			while (upload.isDone() == false)  
			{             
				int percent =  (int)(p.getPercentTransfered());  
				System.out.print("\r" + localPath + " - " + "[ " + percent + "% ] "   
						+ p.getBytesTransfered() + " / " + p.getTotalBytesToTransfer() );  
				// Do work while we wait for our upload to complete...  
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}  
			}  
			System.out.print("\r" + localPath + " - " + "[ 100% ] "   
					+ p.getBytesTransfered() + " / " + p.getTotalBytesToTransfer() );  

			// 默认添加public权限  
			s3.setObjectAcl(bucketName, s3Path, CannedAccessControlList.PublicRead); 
		} catch (Exception e) {
			System.out.println("###Exception:"+s3Path);
			fileWriter(Constant.LOCAL_FILE_PATH+S3_FAIL_URL_FILE, s3Path);
			e.printStackTrace();
		}
	}
	private AmazonS3 createAmazonS3Client(String bucketName,Regions regions){
		 /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(regions));

        System.out.println("Getting Started with Amazon S3");
        
        try {
        	if(!s3.doesBucketExist(bucketName)){
        		System.out.println("Creating bucket " + bucketName + "\n");
                s3.createBucket(bucketName);
        	} 
        	return s3;
        	
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return null;
	}
	/**
	 * 创建文件
	 * 
	 * @param fileName
	 */
	public static void createFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists() ) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();				
			}
			try {
				file.createNewFile();				
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("###createFile excepiton:"+fileName);
			}
		}
	}
	public static void fileWriter(String fileName,String s){
		createFile(fileName);
		//创建一个FileWriter对象
		FileWriter fw=null;
		try{
			fw = new FileWriter(fileName,true);
			fw.write(s+"\n");			
			//刷新缓冲区
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw!=null)fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
//		fileWriter("d:/11/b.txt","1111111111111111");
//		fileWriter("d:/11/b.txt","2222222222222222");
	}
}

