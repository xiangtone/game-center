package com.mas;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.mas.data.AppDownloadData;
import com.mas.data.AppDownloadResponse;
import com.mas.market.pojo.TAppInfo;
import com.mas.market.pojo.TRaveCountry;
import com.mas.market.service.TAppFileService;
import com.mas.market.service.TAppInfoService;
import com.mas.market.service.TRaveCountryService;
import com.mas.util.DateUtil;
import com.mas.util.VM;
import com.mas.ws.pojo.AppDownloadLogIcon;
import com.mas.ws.service.AppDownloadLogService;

/**
 * @author lixin
 */
@Service
@Path(value="/appDownload")
@Produces("application/json")
public class AppDownloadResource extends BaseResoure {
	
	@Autowired
	private AppDownloadLogService appDownloadLogService;
	
	@Autowired
	private TRaveCountryService tRaveCountryService;
	
	@Autowired
	private TAppFileService tAppFileService;
	
	@Autowired
	private TAppInfoService tAppInfoService;

	@GET
	@Path("/list")
	public AppDownloadResponse list(){
		AppDownloadResponse adr = new AppDownloadResponse();
		adr.setTrxrc(80001);
		try{
			List<AppDownloadData> dataList = new ArrayList<AppDownloadData>();
			List<AppDownloadLogIcon> logList = appDownloadLogService.queryList();
			int count = 0;
			if(logList != null && logList.size() > 0){
				for(AppDownloadLogIcon appDownloadLogIcon : logList){
					AppDownloadData appDownloadData = new AppDownloadData();
					appDownloadData.setAppName(appDownloadLogIcon.getAppName());
					appDownloadData.setApkId(appDownloadLogIcon.getApkId());
					appDownloadData.setCountryName(appDownloadLogIcon.getCountryName());
					if(appDownloadLogIcon.getCountryName() != null && !"".equals(appDownloadLogIcon.getCountryName())){
						appDownloadData.setCountryName(appDownloadLogIcon.getCountryName());
						if(appDownloadLogIcon.getIconUrl() != null &&!"".equals(appDownloadLogIcon.getIconUrl())){
							appDownloadData.setCountryIcon(VM.getInatance().getResServer()+appDownloadLogIcon.getIconUrl());
						}else{
							appDownloadData.setCountryIcon(" ");
						}
					}else{
						appDownloadData.setCountryName("Global");
						String iconUrl = appDownloadLogService.getIconUrlByCountryNm("Global");
						if(iconUrl != null && !"".equals(iconUrl)){
							appDownloadData.setCountryIcon(VM.getInatance().getResServer()+iconUrl);
						}else{
							appDownloadData.setCountryIcon(" ");
						}
					}
					

					if(appDownloadLogIcon.getAppIcon() != null && !"".equals(appDownloadLogIcon.getAppIcon())){
						appDownloadData.setAppIcon(VM.getInatance().getResServer()+appDownloadLogIcon.getAppIcon());
					}else{
						appDownloadData.setAppIcon(" ");
					}
					dataList.add(appDownloadData);
				}
				count = logList.size();
				adr.setListSize(count);
			}
			adr.setDataList(dataList);
		}catch (Exception e) {
			adr.getState().setCode(500);
			adr.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		
		
		return adr;
	}
	
}
