package com.mas.rave.util.s3;

import java.util.List;


public class S3DeleteTask extends Thread {

	private List<String> fileUrlList;
	
	public S3DeleteTask(List<String> fileUrlList){
		this.fileUrlList=fileUrlList;
	}
	
	@Override
	public void run() {
		//System.out.println("S3DeleteTask...");
		try{
			S3Service service = new S3Service();
			service.deleteFile(fileUrlList);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

}

