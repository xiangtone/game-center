package com.mas.rave.vo;

import org.jsoup.select.Elements;

import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoConfigService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.AppannieInfoBaseService;
import com.mas.rave.service.AppannieInfoCountryRankService;
import com.mas.rave.service.AppannieInfoService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.ProxyIPService;

public class AppBatchDistributionVO {
	private AppAlbumColumnService appAlbumColumnService;

	private AppAlbumService appAlbumService;

	private AppAlbumResTempService appAlbumResTempService;

	private AppFileService appFileService;

	private AppInfoService appInfoService;

	private AppInfoConfigService appInfoConfigService;

	private CountryService countryService;
	
	private ProxyIPService proxyIPService;
	
	private AppScoreService appScoreService;
	
	private AppannieInfoService appannieInfoService;
	
	private AppannieInfoCountryRankService appannieInfoCountryRankService;


	private AppannieInfoBaseService appannieInfoBaseService;
	
	private Elements elements;
	
	private int appType;
	private int raveId;
	private int stateType;

	public AppAlbumColumnService getAppAlbumColumnService() {
		return appAlbumColumnService;
	}
	public void setAppAlbumColumnService(AppAlbumColumnService appAlbumColumnService) {
		this.appAlbumColumnService = appAlbumColumnService;
	}
	public AppAlbumService getAppAlbumService() {
		return appAlbumService;
	}
	public void setAppAlbumService(AppAlbumService appAlbumService) {
		this.appAlbumService = appAlbumService;
	}
	public AppAlbumResTempService getAppAlbumResTempService() {
		return appAlbumResTempService;
	}
	public void setAppAlbumResTempService(
			AppAlbumResTempService appAlbumResTempService) {
		this.appAlbumResTempService = appAlbumResTempService;
	}
	public AppFileService getAppFileService() {
		return appFileService;
	}
	public void setAppFileService(AppFileService appFileService) {
		this.appFileService = appFileService;
	}
	public AppInfoService getAppInfoService() {
		return appInfoService;
	}
	public void setAppInfoService(AppInfoService appInfoService) {
		this.appInfoService = appInfoService;
	}
	public AppInfoConfigService getAppInfoConfigService() {
		return appInfoConfigService;
	}
	public void setAppInfoConfigService(AppInfoConfigService appInfoConfigService) {
		this.appInfoConfigService = appInfoConfigService;
	}
	public CountryService getCountryService() {
		return countryService;
	}
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
	public ProxyIPService getProxyIPService() {
		return proxyIPService;
	}
	public void setProxyIPService(ProxyIPService proxyIPService) {
		this.proxyIPService = proxyIPService;
	}
	public AppScoreService getAppScoreService() {
		return appScoreService;
	}
	public void setAppScoreService(AppScoreService appScoreService) {
		this.appScoreService = appScoreService;
	}
	
	
	public AppannieInfoService getAppannieInfoService() {
		return appannieInfoService;
	}
	public void setAppannieInfoService(AppannieInfoService appannieInfoService) {
		this.appannieInfoService = appannieInfoService;
	}
	
	public AppannieInfoCountryRankService getAppannieInfoCountryRankService() {
		return appannieInfoCountryRankService;
	}
	public void setAppannieInfoCountryRankService(
			AppannieInfoCountryRankService appannieInfoCountryRankService) {
		this.appannieInfoCountryRankService = appannieInfoCountryRankService;
	}
	
	public AppannieInfoBaseService getAppannieInfoBaseService() {
		return appannieInfoBaseService;
	}
	public void setAppannieInfoBaseService(
			AppannieInfoBaseService appannieInfoBaseService) {
		this.appannieInfoBaseService = appannieInfoBaseService;
	}
	public Elements getElements() {
		return elements;
	}
	public void setElements(Elements elements) {
		this.elements = elements;
	}
	public int getAppType() {
		return appType;
	}
	public void setAppType(int appType) {
		this.appType = appType;
	}
	public int getRaveId() {
		return raveId;
	}
	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}
	public int getStateType() {
		return stateType;
	}
	public void setStateType(int stateType) {
		this.stateType = stateType;
	}
	
	
}
