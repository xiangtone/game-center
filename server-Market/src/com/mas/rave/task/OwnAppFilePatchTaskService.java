package com.mas.rave.task;

import com.mas.rave.main.vo.OwnAppFile;
import com.mas.rave.service.OwnAppFileListService;
import com.mas.rave.service.OwnAppFilePatchService;

public class OwnAppFilePatchTaskService {

	public static void addAppFilePatchTask(OwnAppFile appFile, OwnAppFileListService appFileListService, OwnAppFilePatchService appFilePatchService) {
		OwnAppFilePatchTask task = new OwnAppFilePatchTask();
		task.setAppFile(appFile);
		task.setAppFileListService(appFileListService);
		task.setAppFilePatchService(appFilePatchService);
		task.start();
	}
}
