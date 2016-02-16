package com.mas.rave.task;


import com.mas.rave.controller.AppAlbumColumnController;
import com.mas.rave.vo.AppBatchDistributionVO;

public class DealAppBatchDistribution extends Thread{	
	@Override
	public void run() {
		while(true){
			try{
				//log
				AppBatchDistributionVO appBatchDistributionVO =AppAlbumColumnController.distributionQueue.poll();

				if(appBatchDistributionVO!=null){
					AppBatchDistributionTask task = new AppBatchDistributionTask(appBatchDistributionVO.getRaveId(),appBatchDistributionVO.getAppType(),appBatchDistributionVO.getElements());				
					task.setAppAlbumColumnService(appBatchDistributionVO.getAppAlbumColumnService());
					task.setAppAlbumResTempService(appBatchDistributionVO.getAppAlbumResTempService());
					task.setAppAlbumService(appBatchDistributionVO.getAppAlbumService());
					task.setAppFileService(appBatchDistributionVO.getAppFileService());
					task.setAppInfoService(appBatchDistributionVO.getAppInfoService());
					task.setProxyIPService(appBatchDistributionVO.getProxyIPService());
					task.setAppInfoConfigService(appBatchDistributionVO.getAppInfoConfigService());
					task.setCountryService(appBatchDistributionVO.getCountryService());
					task.setAppScoreService(appBatchDistributionVO.getAppScoreService());
					task.setAppannieInfoService(appBatchDistributionVO.getAppannieInfoService());
					task.setAppannieInfoBaseService(appBatchDistributionVO.getAppannieInfoBaseService());
					task.runAppBatchDistribution();
				}else{
						break;//结束					
				}				
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
		
	}

}
