package com.reportforms.util;

import com.reportforms.stroage.service.StorageService;
import com.reportforms.stroage.service.impl.StrorageServiceOssImpl;
import com.reportforms.stroage.service.impl.StrorageServiceS3Impl;

public class StorageManager {
	
	private String key;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

    public StorageManager(){
    	
    }
    
	public StorageManager(String key){
		this.key = key;
	}
	
	
	public StorageService getStorageServcie(){
		if("S3".equals(key)){
			return new StrorageServiceS3Impl();
		}if("OSS".equals(key)){
			return new StrorageServiceOssImpl();
		}else{
			return new StrorageServiceOssImpl();
		}
	}
	
}
