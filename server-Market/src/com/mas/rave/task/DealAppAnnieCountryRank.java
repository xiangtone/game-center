package com.mas.rave.task;


import com.mas.rave.controller.AppAlbumColumnController;
import com.mas.rave.vo.AppBatchDistributionVO;

public class DealAppAnnieCountryRank extends Thread{	
	@Override
	public void run() {
		while(true){
			try{
				//log
				AppBatchDistributionVO appBatchDistributionVO =AppAlbumColumnController.appannieCountryRankQueue.poll();

				if(appBatchDistributionVO!=null){
					AppAnnieCountryRankTask task = new AppAnnieCountryRankTask(appBatchDistributionVO.getRaveId(),appBatchDistributionVO.getAppType(),appBatchDistributionVO.getElements(),appBatchDistributionVO.getStateType());				
					task.setAppAlbumColumnService(appBatchDistributionVO.getAppAlbumColumnService());
					task.setAppAlbumResTempService(appBatchDistributionVO.getAppAlbumResTempService());
					task.setAppAlbumService(appBatchDistributionVO.getAppAlbumService());
					task.setAppFileService(appBatchDistributionVO.getAppFileService());
					task.setAppInfoService(appBatchDistributionVO.getAppInfoService());
					task.setProxyIPService(appBatchDistributionVO.getProxyIPService());
					task.setAppInfoConfigService(appBatchDistributionVO.getAppInfoConfigService());
					task.setCountryService(appBatchDistributionVO.getCountryService());
					task.setAppScoreService(appBatchDistributionVO.getAppScoreService());
					task.setAppannieInfoCountryRankService(appBatchDistributionVO.getAppannieInfoCountryRankService());
					task.setAppannieInfoBaseService(appBatchDistributionVO.getAppannieInfoBaseService());
					task.runAppAnnieCountryRank();
				}else{
						break;//结束					
				}				
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
		
	}

}
