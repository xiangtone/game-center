package com.mas.rave.task;

import com.mas.rave.main.vo.AppFile;
import com.mas.rave.service.AppFileListService;
import com.mas.rave.service.AppFilePatchService;

public class AppFilePatchTaskService {

	public static void addAppFilePatchTask(AppFile appFile, AppFileListService appFileListService, AppFilePatchService appFilePatchService){
		AppFilePatchTask task = new AppFilePatchTask();
		task.setAppFile(appFile);
		task.setAppFileListService(appFileListService);
		task.setAppFilePatchService(appFilePatchService);
		task.start();
	}
}
