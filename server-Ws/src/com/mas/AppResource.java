package com.mas;
import java.util.Date;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.mas.data.AppRequest;
import com.mas.data.AppResponse;
import com.mas.data.BaseResponse;
import com.mas.data.ClientRequest;
import com.mas.data.ClientResponse;
import com.mas.data.Data;
import com.mas.data.HostData;
import com.mas.data.MasUserRequest;
import com.mas.data.MasUserResponse;
import com.mas.log.InsertLogService;
import com.mas.market.pojo.TAppFile;
import com.mas.market.service.TAppFileService;
import com.mas.util.AddressUtils;
import com.mas.util.VM;
import com.mas.ws.pojo.AppChangeuserLog;
import com.mas.ws.pojo.AppLoginLog;
import com.mas.ws.pojo.AppLogoutLog;
import com.mas.ws.pojo.ClientUser;
import com.mas.ws.pojo.MasUser;
import com.mas.ws.service.AppChangeuserLogService;
import com.mas.ws.service.AppLoginLogService;
import com.mas.ws.service.AppLogoutLogService;
import com.mas.ws.service.ClientActivateLogService;
import com.mas.ws.service.ClientUserService;
import com.mas.ws.service.MasUserService;

@Service
@Path(value="/app")
@Produces("application/json")
public class AppResource extends BaseResoure{
	@Autowired
	TAppFileService tAppFileService;
	@Autowired
	AppLoginLogService appLoginLogService;
	@Autowired
	AppLogoutLogService appLogoutLogService;
	@Autowired
	MasUserService masUserService;
	@Autowired
	private ClientActivateLogService clientActivateLogService;
	@Autowired
	private ClientUserService clientUserService;
	@Autowired
	private AppChangeuserLogService appChangeuserLogService;
	@POST
	@Path(value = "activate")
	public ClientResponse activate(ClientRequest req)
	{
		ClientResponse rep = new ClientResponse();
		rep.setTrxrc(10001);
		try
		{
			Data data = req.getData();
			if (data == null)
			{
				rep.getState().setCode(501);
				rep.getState().setMsg("app parameter error,data is null");
				return rep;
			}
			com.mas.market.pojo.Criteria c = new com.mas.market.pojo.Criteria();
			c.put("apkKey", data.getApkKey());
	    	List<TAppFile> apks = tAppFileService.selectByExample(c);
	    	if(apks==null||apks.isEmpty() || !apks.get(0).getAppId().equals(data.getAppId()) 
	    			|| !apks.get(0).getChannelId().equals(data.getChannelId()) 
	    			|| !apks.get(0).getCpId().equals(data.getCpId())
	    			|| !apks.get(0).getServerId().equals(data.getServerId()))
			{
				rep.getState().setCode(504);rep.getState().setMsg("appId,appKey,cpId,channelId,serverId parameters error.");
				return rep;
			}
			// 添加新平台
			ClientUser clientUser = clientUserService.addClientMobile(req.getClientInfo(),data);
			rep.setClientId(clientUser.getClientId());
		
			MasUser masUser = req.getMasUser();
			boolean hasUser = false;
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user){
					if(masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
						hasUser = true;
						masUser.setaValue(user.getaValue());
						masUser.setaValuePresent(user.getaValuePresent());
						rep.setMasUser(masUser);
						rep.setIslogin(true);rep.setLoginInfo("Automatic login success");
					}
				}
			}
			InsertLogService.activate(clientActivateLogService,clientUserService,req, clientUser, this.getRequest(), hasUser);
			Data d = getApkForUpgrade(data.getAppId(), data.getApkKey(),data.getAppVersionCode(),data.getMd5());
			if(null!=d){
				if(d.getUpgradeType()!=1){
					rep.setIsUpgrade(true);
					rep.setData(d);
				}else{
					rep.setIsUpgrade(false);
				}
			}else{
				rep.setIsUpgrade(false);
			}
			
			rep.setHosts(new HostData());
			rep.getHosts().setPay(VM.getInatance().getHostPayUrl());
		} catch (Exception e)
		{
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@POST
	@Path(value="reg")
	public AppResponse reg(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(10002);
		try {
				if(req.getData()!=null){
					Data data = req.getData();
					MasUser masUser = new MasUser();
					masUser.setUserName(data.getUserName());
					masUser.setUserPwd(data.getUserPwd());
					masUser.setCreateTime(new Date());
					masUser.setTypeId(1);
					try{
						masUserService.insertSelective(masUser);
					}catch (DuplicateKeyException e) {
						rep.getState().setCode(208);
						rep.getState().setMsg("The user name already exists.");	
						MasUser u = getUserNameTip(data.getUserName());
						rep.setMasUser(u);
						return rep;
					}
					rep.setIslogin(true);
					MasUser user = new MasUser();
					user.setUserId(masUser.getUserId());
					user.setUserName(masUser.getUserName());
					user.setUserPwd(masUser.getUserPwd());
					user.setaValue(0);
					user.setaValuePresent(0);
					rep.setMasUser(user);
				}else{
					rep.getState().setCode(501);rep.getState().setMsg("Zapp registration parameter error");
				}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	
	@POST
	@Path(value="login")
	public AppResponse login(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(10003);
		try {
			Data data=req.getData();
			com.mas.ws.pojo.Criteria c=new com.mas.ws.pojo.Criteria();
			c.put("userName", data.getUserName()==null?"":data.getUserName());
			c.put("userPwd", data.getUserPwd()==null?"":data.getUserPwd());
			List<MasUser> masUsers = masUserService.selectByExample(c);
			if(null==masUsers || masUsers.size()==0){
				rep.getState().setCode(201);rep.getState().setMsg("Account password is incorrect, please re-login!");
			}else{
				MasUser masUser = masUsers.get(0);
				masUser.setUpdateTime(new Date());
				masUserService.updateByPrimaryKeySelective(masUser);
				AppLoginLog appLoginLog=new AppLoginLog();
				String sessionId = getRequest().getSession().getId();
				appLoginLog.setSessionId(sessionId);
				appLoginLog.setUserId(masUser.getUserId());
				appLoginLog.setUserName(masUser.getUserName());
				appLoginLog.setUserPwd(masUser.getUserPwd());
				appLoginLog.setClientId(data.getClientId());
				appLoginLog.setAppId(data.getAppId());
				appLoginLog.setApkKey(data.getApkKey());
				appLoginLog.setCpId(data.getCpId());
				appLoginLog.setChannelId(data.getChannelId());
				appLoginLog.setServerId(data.getServerId());
				appLoginLog.setCreateTime(new Date());
				appLoginLog.setIP(AddressUtils.getClientIp(getRequest()));
				appLoginLog.setEntrance(1);
				appLoginLogService.insertSelective(appLoginLog);
				rep.setIslogin(true);
				MasUser user = new MasUser();
				user.setUserId(masUser.getUserId());
				user.setUserName(masUser.getUserName());
				user.setUserPwd(masUser.getUserPwd());
				user.setaValue(masUser.getaValue());
				user.setaValuePresent(masUser.getaValuePresent());
				rep.setMasUser(user);
			}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	
	@POST
	@Path(value="logout")
	public AppResponse logout(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(10004);
		try {
			AppLogoutLog appLogoutLog=new AppLogoutLog();
			Data data=req.getData();
			String sessionId = getRequest().getSession().getId();
			appLogoutLog.setSessionId(sessionId);
			appLogoutLog.setUserId(data.getUserId());
			appLogoutLog.setUserName(data.getUserName());
			appLogoutLog.setAppId(data.getAppId());
			appLogoutLog.setApkKey(data.getApkKey());
			appLogoutLog.setCpId(data.getCpId());
			appLogoutLog.setServerId(data.getServerId());
			appLogoutLog.setClientId(data.getClientId());
			appLogoutLog.setChannelId(data.getChannelId());
			appLogoutLog.setCreateTime(new Date());
			appLogoutLog.setIP(AddressUtils.getClientIp(getRequest()));
			appLogoutLogService.insertSelective(appLogoutLog);
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	
	@POST
	@Path(value="checkUserName")
	public MasUserResponse checkUserName(MasUserRequest req){
		MasUserResponse rep=new MasUserResponse();
		rep.setTrxrc(10006);
		String userName = req.getData().getUserName();
		MasUser user = this.getUserNameTip(userName);
		if(null == user){
			rep.getState().setMsg("The user name is available.");
			return rep;
		}else{
			rep.getState().setCode(208);
			rep.getState().setMsg("The user name already exists.");
			rep.setMasUser(user);
			return rep;
		}
	}
	private MasUser getUserNameTip(String userName){
		com.mas.ws.pojo.Criteria c=new com.mas.ws.pojo.Criteria();
		c.put("userNameLike",userName);
		List<MasUser> list = masUserService.selectByExample(c);
		MasUser user = new MasUser();
		if(null != list && list.size()!=0){
			String prompt = userName;
			for(int i=1;i<100;i++){
				boolean repeat = false;
				for(MasUser u:list){
					if(u.getUserName().equalsIgnoreCase(prompt)){
						repeat = true;break;
					}
				}
				if(!repeat){
					if(prompt.equalsIgnoreCase(userName)){//兼容 checkUserName 方法
						return null;
					}else{
						user.setRecommendName(prompt);
						return user;
					}
				}
				prompt = userName+i;
			}
		}
		return null;
	}
	@POST
	@Path(value="changeuser")
	public BaseResponse changeuser(AppRequest req){
		BaseResponse rep=new BaseResponse();
		rep.setTrxrc(10007);
		try {
			AppChangeuserLog applog=new AppChangeuserLog();
			Data data=req.getData();
			String sessionId = getRequest().getSession().getId();
			applog.setSessionId(sessionId);
			applog.setUserId(data.getUserId());
			applog.setUserName(data.getUserName());
			applog.setAppId(data.getAppId());
			applog.setApkKey(data.getApkKey());
			applog.setCpId(data.getCpId());
			applog.setServerId(data.getServerId());
			applog.setClientId(data.getClientId());
			applog.setChannelId(data.getChannelId());
			applog.setCreateTime(new Date());
			applog.setIP(AddressUtils.getClientIp(getRequest()));
			appChangeuserLogService.insertSelective(applog);
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	private Data getApkForUpgrade(Integer appId,String apkKey,Integer versionCode,String md5) throws Exception {
		com.mas.market.pojo.Criteria c=new com.mas.market.pojo.Criteria();
		c.put("apkKey", apkKey);
		c.put("appId", appId);
		c.put("versionCode", versionCode);
		if(null!=md5 && !"".equals(md5.trim())){
			c.put("md5", md5);
		}
    	c.setOrderByClause("versionCode desc");
    	//List<TAppFile> apks=tAppFileService.getApkForUpgrade(c);
    	List<TAppFile> apks=tAppFileService.getZappForUpgrade(c);
    	if(apks==null||apks.isEmpty()){
    		return null;
    	}else{
    		TAppFile apk = apks.get(0);
    		if(apk!=null && apk.getVersionCode()>versionCode){
    			Data d = new Data();
				d.setUrl(VM.getInatance().getResServer()+apk.getUrl());
				d.setFileSize(apk.getFileSize());
				d.setUpdateInfo(apk.getUpdateInfo());
				d.setAppName(apk.getAppName());
				d.setUpgradeType(apk.getUpgradeType());
				d.setIsPatch(apk.getHaslist());
				d.setVersionCode(apk.getVersionCode());
				d.setVersionName(apk.getVersionName());
				if(apk.getHaslist()){
					d.setUrlPatch(VM.getInatance().getResServer()+apk.getPath());
					d.setIsPatch(true);
					d.setPatchSize(apk.getServerId());
				}
				return d;
			}else{
				return null;
			}
    	}
    }
}