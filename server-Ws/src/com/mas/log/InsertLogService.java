package com.mas.log;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.mas.data.ClientAppRequest;
import com.mas.data.ClientRequest;
import com.mas.market.pojo.TAppAlbumRes;
import com.mas.market.service.TAppAlbumResService;
import com.mas.market.service.TClientAppInfoService;
import com.mas.ws.pojo.ClientUser;
import com.mas.ws.service.AppPageopenLogService;
import com.mas.ws.service.ClientActivateLogService;
import com.mas.ws.service.ClientUserService;
@Service
public class InsertLogService {

	public static void getUpdateApps(TClientAppInfoService tClientAppInfoService,ClientAppRequest req,List<TAppAlbumRes> updateList){
		InsertLogThread task = new InsertLogThread();
		task.settClientAppInfoService(tClientAppInfoService);
		task.setUpdateList(updateList);
		task.setReq(req);
		task.setMethod("getUpdateApps");
		task.start();
	}
	public static void app(AppPageopenLogService appPageopenLogService,TAppAlbumResService tAppAlbumResService,TAppAlbumRes res,Integer raveId,Integer ct,HttpServletRequest request){
		InsertLogThread task = new InsertLogThread();
		task.setRes(res);;
		task.setColumnId(ct);
		task.setRaveId(raveId);
		task.setAppPageopenLogService(appPageopenLogService);
		task.settAppAlbumResService(tAppAlbumResService);
		task.setRequest(request);
		task.setMethod("app");
		task.start();
	}
	public static void activate(ClientActivateLogService clientActivateLogService,ClientUserService clientUserService,ClientRequest clientReq,ClientUser clientUser,HttpServletRequest request,boolean hasUser){
		InsertLogThread task = new InsertLogThread();
		task.setClientActivateLogService(clientActivateLogService);
		task.setClientUserService(clientUserService);
		task.setClientReq(clientReq);
		task.setClientUser(clientUser);
		task.setRequest(request);
		task.setHasUser(hasUser);
		task.setMethod("activate");
		task.start();
	}
}
