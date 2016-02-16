package com.mas.log;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DuplicateKeyException;

import com.mas.data.ClientAppRequest;
import com.mas.data.ClientRequest;
import com.mas.data.Data;
import com.mas.data.MasData;
import com.mas.market.pojo.TAppAlbumRes;
import com.mas.market.pojo.TClientAppInfo;
import com.mas.market.service.TAppAlbumResService;
import com.mas.market.service.TClientAppInfoService;
import com.mas.util.AddressUtils;
import com.mas.ws.pojo.AppPageopenLog;
import com.mas.ws.pojo.ClientActivateLog;
import com.mas.ws.pojo.ClientUser;
import com.mas.ws.pojo.MasUser;
import com.mas.ws.service.AppPageopenLogService;
import com.mas.ws.service.ClientActivateLogService;
import com.mas.ws.service.ClientUserService;
public class InsertLogThread extends Thread {

	private String method;
	private TClientAppInfoService tClientAppInfoService;
	private AppPageopenLogService appPageopenLogService;
	private TAppAlbumResService tAppAlbumResService;
	private ClientAppRequest req;
	private List<TAppAlbumRes> updateList;
	private TAppAlbumRes res;
	private Integer columnId;
	private Integer raveId;
	//-------------active--------------------------
	private ClientRequest clientReq;
	private ClientUser clientUser;
	private HttpServletRequest request;
	private boolean hasUser;
	private ClientActivateLogService clientActivateLogService;
	private ClientUserService clientUserService;
	@Override
	public void run() {
		if("getUpdateApps".equals(getMethod())){
			TClientAppInfo appInfo = new TClientAppInfo();
			com.mas.market.pojo.Criteria criteria = new com.mas.market.pojo.Criteria();
			criteria.put("clientId", req.getClientId());
			appInfo.setAtUse(false);
			tClientAppInfoService.updateByExampleSelective(appInfo, criteria);
			List<Data> apps = req.getApps();
			for(Data app:apps){
				appInfo = new TClientAppInfo();
				appInfo.setAppName(app.getAppName());
				String packageName = app.getAppPackageName();
				appInfo.setAppPackageName(packageName);
				appInfo.setAppVersionName(app.getAppVersionName());
				appInfo.setAppVersionCode(app.getAppVersionCode());
				appInfo.setAppMD5(app.getMd5());
				appInfo.setAtUse(true);
				appInfo.setClientId(req.getClientId());
				appInfo.setCreateTime(new Date());
				appInfo.setSvAppVersionCode(0);
				appInfo.setSvAppVersionName("");
				appInfo.setIsPatch(false);appInfo.setIsUpdate(false);
				for(TAppAlbumRes res:updateList){
					if(packageName.equals(res.getPackageName())){
						appInfo.setSvAppVersionCode(res.getVersionCode());
						appInfo.setSvAppVersionName(res.getVersionName());
						appInfo.setIsPatch(res.getIsPatch());
						if(app.getAppVersionCode()<res.getVersionCode()){
							appInfo.setIsUpdate(true);
						}
						break;
					}
				}
				try{
					tClientAppInfoService.insertSelective(appInfo);
				}catch (DuplicateKeyException e) {
					criteria = new com.mas.market.pojo.Criteria();
					criteria.put("clientId", req.getClientId());
					appInfo.setCreateTime(null);
					appInfo.setUpdateTime(new Date());
					criteria.put("appPackageName",app.getAppPackageName());
					tClientAppInfoService.updateByExampleSelective(appInfo,criteria);
				}
			}
		}else if("app".equals(method)){
			AppPageopenLog record = new AppPageopenLog();
			record.setApkId(res.getApkId());
			record.setAppId(res.getAppId());
			record.setAppName(res.getAppName());
			record.setCategoryId(res.getCategoryId());
			record.setPackageName(res.getPackageName());
			record.setVersionCode(res.getVersionCode());
			record.setVersionName(res.getVersionName());
			record.setFree(res.getFree());
			record.setColumnId(columnId);
			record.setRaveId(raveId);
			String ip = AddressUtils.getClientIp(request);
			record.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				record.setCountry(address[0]);
				record.setProvince(address[1]);
				record.setCity(address[2]);
			}
			appPageopenLogService.insertSelective(record);
			tAppAlbumResService.updateAppOpenLog(res.getAppId());
		}else if("activate".equals(method)){
			// 记录日志
			ClientActivateLog clientActivateLog = new ClientActivateLog();
			clientActivateLog.setClientId(clientUser.getClientId());
			clientActivateLog.setImei(clientUser.getImei());
			clientActivateLog.setMac(clientUser.getMac());
			Data data = clientReq.getData();
			clientActivateLog.setChannelId(data.getChannelId());
			clientActivateLog.setServerId(data.getServerId());
			clientActivateLog.setCpId(data.getCpId());
			clientActivateLog.setAppId(data.getAppId());
			clientActivateLog.setApkKey(data.getApkKey());
			clientActivateLog.setAppPackageName(data.getAppPackageName());
			clientActivateLog.setAppVersionName(data.getAppVersionName());
			clientActivateLog.setAppVersionCode(data.getAppVersionCode());
			clientUser.setAppPackageNameLast(data.getAppPackageName());
			clientUser.setAppVersionNameLast(data.getAppVersionName());
			clientUser.setAppVersionCodeLast(data.getAppVersionCode());
			MasData mas = clientReq.getMasSdk();
			if(null!=mas){
				clientActivateLog.setMasPackageName(mas.getMasPackageName());
				clientActivateLog.setMasVersionName(mas.getMasVersionName());
				clientActivateLog.setMasVersionCode(mas.getMasVersionCode());
			}else{
				clientActivateLog.setMasPackageName("new version no sdk");
				clientActivateLog.setMasVersionName("-1");
				clientActivateLog.setMasVersionCode(-1);
			}
			ClientActivateLog clientActive = clientReq.getClientActive();
			if(null!=clientActive){
				clientActivateLog.setLatitude(clientActive.getLatitude());
				clientActivateLog.setLongitude(clientActive.getLongitude());
				clientActivateLog.setLac(clientActive.getLac());
				clientActivateLog.setCellId(clientActive.getCellId());
				clientActivateLog.setMcc(clientActive.getMcc());
				clientActivateLog.setMnc(clientActive.getMnc());
			}
			String ip = AddressUtils.getClientIp(request);
			clientActivateLog.setIP(ip);
			clientUser.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				clientActivateLog.setCountry(address[0]);
				clientActivateLog.setProvince(address[1]);
				clientActivateLog.setCity(address[2]);
				clientUser.setCountry(address[0]);
				clientUser.setProvince(address[1]);
				clientUser.setCity(address[2]);
			}
			clientUser.setActiveNum(clientUser.getActiveNum()+1);
			clientUserService.updateByPrimaryKeySelective(clientUser);
			if(hasUser){
				MasUser masUser = clientReq.getMasUser();
				clientActivateLog.setUserId(masUser.getUserId());
				clientActivateLog.setUserName(masUser.getUserName());
				clientActivateLog.setUserPwd(masUser.getUserPwd());
				//  去掉激活登陆的记录
				/*AppLoginLog appLoginLog = new AppLoginLog();
				appLoginLog.setSessionId(sessionId);
				appLoginLog.setEntrance(1);
				appLoginLog.setUserId(masUser.getUserId());
				appLoginLog.setUserName(masUser.getUserName());
				appLoginLog.setUserPwd(masUser.getUserPwd());
				appLoginLog.setAppId(data.getAppId());
				appLoginLog.setApkKey(data.getApkKey());
				appLoginLog.setCpId(data.getCpId());
				appLoginLog.setClientId(clientUser.getClientId());
				appLoginLog.setServerId(data.getServerId());
				appLoginLog.setChannelId(data.getChannelId());
				appLoginLog.setIP(ip);
				appLoginLogService.insertSelective(appLoginLog);*/
			}
			clientActivateLog.setCreateTime(new Date());
			clientActivateLogService.insertSelective(clientActivateLog);
		}
	}

	public TClientAppInfoService gettClientAppInfoService() {
		return tClientAppInfoService;
	}

	public void settClientAppInfoService(TClientAppInfoService tClientAppInfoService) {
		this.tClientAppInfoService = tClientAppInfoService;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public ClientAppRequest getReq() {
		return req;
	}

	public void setReq(ClientAppRequest req) {
		this.req = req;
	}

	public List<TAppAlbumRes> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<TAppAlbumRes> updateList) {
		this.updateList = updateList;
	}

	public TAppAlbumRes getRes() {
		return res;
	}

	public void setRes(TAppAlbumRes res) {
		this.res = res;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public AppPageopenLogService getAppPageopenLogService() {
		return appPageopenLogService;
	}

	public void setAppPageopenLogService(AppPageopenLogService appPageopenLogService) {
		this.appPageopenLogService = appPageopenLogService;
	}

	public TAppAlbumResService gettAppAlbumResService() {
		return tAppAlbumResService;
	}

	public void settAppAlbumResService(TAppAlbumResService tAppAlbumResService) {
		this.tAppAlbumResService = tAppAlbumResService;
	}

	public Integer getRaveId() {
		return raveId;
	}

	public void setRaveId(Integer raveId) {
		this.raveId = raveId;
	}

	public ClientRequest getClientReq() {
		return clientReq;
	}

	public void setClientReq(ClientRequest clientReq) {
		this.clientReq = clientReq;
	}

	public ClientUser getClientUser() {
		return clientUser;
	}

	public void setClientUser(ClientUser clientUser) {
		this.clientUser = clientUser;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public boolean isHasUser() {
		return hasUser;
	}

	public void setHasUser(boolean hasUser) {
		this.hasUser = hasUser;
	}

	public ClientActivateLogService getClientActivateLogService() {
		return clientActivateLogService;
	}

	public void setClientActivateLogService(
			ClientActivateLogService clientActivateLogService) {
		this.clientActivateLogService = clientActivateLogService;
	}

	public ClientUserService getClientUserService() {
		return clientUserService;
	}

	public void setClientUserService(ClientUserService clientUserService) {
		this.clientUserService = clientUserService;
	}

}
