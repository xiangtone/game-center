package com.mas.rave.service;

import java.io.File;

import com.mas.rave.exception.ServiceException;
import com.mas.rave.vo.AppResourceVO;

public interface AppBatchUploadService {

	public void saveResource(AppResourceVO appResourceVO)throws ServiceException;
	
	public File[] getUploadSrcFile(AppResourceVO appResourceVO,String recType)throws ServiceException;
	
}
