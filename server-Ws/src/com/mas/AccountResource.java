package com.mas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.data.AppRequest;
import com.mas.data.AppResponse;
import com.mas.data.ClientRequest;
import com.mas.data.ClientResponse;
import com.mas.data.Data;
import com.mas.log.InsertLogService;
import com.mas.market.pojo.TAppFile;
import com.mas.market.service.TAppFileService;
import com.mas.util.AddressUtils;
import com.mas.util.MessageDigestUtil;
import com.mas.util.StringUtil;
import com.mas.util.email.SendEmailService;
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
@Path(value="/account")
@Produces("application/json")
public class AccountResource extends BaseResoure{
	
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
	@Autowired
	private SendEmailService sendEmailService;
	@POST
	@Path(value="start")
	public ClientResponse start(ClientRequest req){
		ClientResponse rep = new ClientResponse();
		rep.setTrxrc(60000);
		try{
			Data data = req.getData();
			Integer raveId= this.getAutoCountry(data.getRaveId());
			rep.setAutoRaveId(raveId);
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
			// 添加新设备
			ClientUser clientUser = clientUserService.addClientMobile(req.getClientInfo(),data);
			rep.setClientId(clientUser.getClientId());
			MasUser masUser = req.getMasUser();
			boolean hasUser = false;
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user){
					if(masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
						hasUser = true;
						masUser.setNickName(user.getNickName());
						rep.setMasUser(masUser);
						rep.setIslogin(true);rep.setLoginInfo("Automatic login success");
					}
				}
			}
			InsertLogService.activate(clientActivateLogService,clientUserService,req, clientUser, this.getRequest(), hasUser);
		}catch (Exception e)
		{
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@POST
	@Path(value="register")
	public AppResponse register(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(60001);
		try {
				if(req.getData()!=null){
					Data data = req.getData();
					MasUser masUser = new MasUser();
					if(StringUtil.emailFormat(data.getUserName())){
						masUser.setUserName(data.getUserName());
						masUser.setEmail(data.getUserName());
						masUser.setNickName(data.getNickName());
						String password = StringUtil.getRandomPassword(6);
						masUser.setUserPwd(MessageDigestUtil.getMD5(password));
						List<String[]> emailList = new ArrayList<String[]>();
						emailList.add(new String[]{data.getUserName()});
						masUser.setCreateTime(new Date());
						masUser.setTypeId(2);
						com.mas.ws.pojo.Criteria c=new com.mas.ws.pojo.Criteria();
						c.put("userName", data.getUserName());
						List<MasUser> masUsers = masUserService.selectByExample(c);
						if(null!=masUsers && masUsers.size()==1){
							rep.getState().setCode(201);
							rep.getState().setMsg("The email already registered.");	
							return rep;
						}else{
							String ip = AddressUtils.getClientIp(this.getRequest());
							masUser.setIP(ip);
							String[] address = AddressUtils.getAddresses(ip);
							if(null!=address){
								masUser.setCountry(address[0]);
								masUser.setProvince(address[1]);
								masUser.setCity(address[2]);
							}
							//20150323，注册时，发送成功后，再写入数据库
							String emailResult = sendEmailService.sendEmail(emailList, "Welcome to zApp!",password,data.getNickName());
							if("success".equals(emailResult)){
								masUserService.insertSelective(masUser);
								rep.getState().setMsg("The register password is sent to email. ");
							}else{
								rep.getState().setCode(202);
								rep.getState().setMsg("The email is invalid addresses.");
								masUserService.deleteByPrimaryKey(masUser.getUserId());
								return rep;
							}
						}
						MasUser user = new MasUser();
						user.setUserId(masUser.getUserId());
						user.setUserName(masUser.getUserName());
						user.setNickName(masUser.getNickName());
						rep.setMasUser(user);
					}else{
						rep.getState().setCode(202);
						rep.getState().setMsg("The email is invalid addresses.");	
						return rep;
					}
				}else{
					rep.getState().setCode(501);rep.getState().setMsg("zapp registration parameter error");
				}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@POST
	@Path(value="findpassword")
	public AppResponse findpassword(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(60002);
		try {
				if(req.getData()!=null){
					Data data = req.getData();
					MasUser masUser = new MasUser();
					if(StringUtil.emailFormat(data.getUserName())){
						String password = StringUtil.getRandomPassword(6);
						List<String[]> emailList = new ArrayList<String[]>();
						emailList.add(new String[]{data.getUserName()});
						com.mas.ws.pojo.Criteria c=new com.mas.ws.pojo.Criteria();
						c.put("userName", data.getUserName());
						List<MasUser> masUsers = masUserService.selectByExample(c);
						if(null==masUsers || masUsers.size()==0){
							rep.getState().setCode(203);
							rep.getState().setMsg("The email is not registered.");	
							return rep;
						}else{
							String emailResult = sendEmailService.sendEmail(emailList, "Password Reset Request on zApp!",password,masUsers.get(0).getNickName());
							if("success".equals(emailResult)){
								masUser.setUserName(data.getUserName());
								masUser.setFindPwdNum(1);
								masUser.setUpdateTime(new Date());
								masUser.setUserPwd(MessageDigestUtil.getMD5(password));
								masUserService.updateByUserName(masUser);
							}else{
								rep.getState().setCode(202);
								rep.getState().setMsg("The email is invalid addresses.");	
								return rep;
							}
						}
						MasUser user = new MasUser();
						user.setUserId(masUsers.get(0).getUserId());
						user.setUserName(masUser.getUserName());
						user.setNickName(masUsers.get(0).getNickName());
						rep.setMasUser(user);
					}else{
						rep.getState().setCode(202);
						rep.getState().setMsg("The email is invalid addresses.");	
						return rep;
					}
				}else{
					rep.getState().setCode(501);rep.getState().setMsg("zapp registration parameter error");
				}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		rep.getState().setMsg("The new password is sent to email. ");
		return rep;
	}
	@POST
	@Path(value="changeInfo")
	public AppResponse changeInfo(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(60003);
		try {
				if(req.getData()!=null){
					Data data = req.getData();
					MasUser masUser =null;
					com.mas.ws.pojo.Criteria c=new com.mas.ws.pojo.Criteria();
					c.put("userName", data.getUserName());
					List<MasUser> masUsers = masUserService.selectByExample(c);
					if(StringUtil.emailFormat(data.getUserName()) && null!=masUsers && masUsers.size()==1){
						if(StringUtils.isNotBlank(data.getNickName())){
							masUser = new MasUser();
							masUser.setNickName(data.getNickName());
							masUser.setUpdateTime(new Date());
							masUser.setUserName(data.getUserName());
							masUserService.updateByUserName(masUser);
							masUsers.get(0).setNickName(masUser.getNickName());
							rep.getState().setMsg("The nickName edit success.");
						}else if(StringUtils.isNotBlank(data.getUserPwd()) && StringUtils.isNotBlank(data.getUserPwdNew())){
							if(!masUsers.get(0).getUserPwd().equals(data.getUserPwd())){
								rep.getState().setCode(204);
								rep.getState().setMsg("The old password is incorrect.");	
								return rep;
							}else{
								masUser = new MasUser();
								masUser.setUpdateTime(new Date());
								masUser.setUserName(data.getUserName());
								masUser.setUserPwd(data.getUserPwdNew());
								masUserService.updateByUserName(masUser);
								rep.getState().setMsg("The password edit success.");
							}
						}else{
							rep.getState().setCode(501);rep.getState().setMsg("parameter error");
						}
						MasUser user = new MasUser();
						user.setUserId(masUsers.get(0).getUserId());
						user.setUserName(masUser.getUserName());
						user.setUserPwd(data.getUserPwdNew());
						user.setNickName(masUsers.get(0).getNickName());
						rep.setMasUser(user);
					}else{
						rep.getState().setCode(202);
						rep.getState().setMsg("The email is invalid addresses.");	
						return rep;
					}
				}else{
					rep.getState().setCode(501);rep.getState().setMsg("parameter error");
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
		rep.setTrxrc(60004);
		try {
			final Data data=req.getData();
			com.mas.ws.pojo.Criteria c=new com.mas.ws.pojo.Criteria();
			c.put("userName", data.getUserName()==null?"":data.getUserName());
			c.put("userPwd", data.getUserPwd()==null?"":data.getUserPwd());
			List<MasUser> masUsers = masUserService.selectByExample(c);
			if(null==masUsers || masUsers.size()==0){
				rep.getState().setCode(205);rep.getState().setMsg("Email password is incorrect, please re-login!");
			}else{
				final MasUser masUser = masUsers.get(0);
				masUser.setUpdateTime(new Date());
				masUser.setLoginNum(1);
				masUserService.updateByUserName(masUser);
				final String ip = AddressUtils.getClientIp(this.getRequest());
				new Thread(){ public void run(){
					AppLoginLog appLoginLog=new AppLoginLog();
					appLoginLog.setUserId(masUser.getUserId());
					appLoginLog.setUserName(masUser.getUserName());
					appLoginLog.setNickName(masUser.getNickName());
					appLoginLog.setUserPwd(masUser.getUserPwd());
					appLoginLog.setAppId(data.getAppId());
					appLoginLog.setClientId(data.getClientId());
					appLoginLog.setApkKey(data.getApkKey());
					appLoginLog.setCpId(data.getCpId());
					appLoginLog.setChannelId(data.getChannelId());
					appLoginLog.setServerId(data.getServerId());
					appLoginLog.setCreateTime(new Date());
					appLoginLog.setEntrance(2);
					appLoginLog.setIP(ip);
					String[] address = AddressUtils.getAddresses(ip);
					if(null!=address){
						appLoginLog.setCountry(address[0]);
						appLoginLog.setProvince(address[1]);
						appLoginLog.setCity(address[2]);
					}
					appLoginLogService.insertSelective(appLoginLog);
				}}.start();
				rep.setIslogin(true);
				MasUser user = new MasUser();
				user.setUserId(masUser.getUserId());
				user.setUserName(masUser.getUserName());
				user.setUserPwd(masUser.getUserPwd());
				user.setNickName(masUser.getNickName());
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
		rep.setTrxrc(60005);
		try {
			AppLogoutLog appLogoutLog=new AppLogoutLog();
			Data data=req.getData();
			String sessionId = getRequest().getSession().getId();
			appLogoutLog.setSessionId(sessionId);
			appLogoutLog.setUserId(data.getUserId());
			appLogoutLog.setUserName(data.getUserName());
			appLogoutLog.setNickName(data.getNickName());
			appLogoutLog.setAppId(data.getAppId());
			appLogoutLog.setApkKey(data.getApkKey());
			appLogoutLog.setCpId(data.getCpId());
			appLogoutLog.setServerId(data.getServerId());
			appLogoutLog.setClientId(data.getClientId());
			appLogoutLog.setChannelId(data.getChannelId());
			appLogoutLog.setCreateTime(new Date());
			String ip = AddressUtils.getClientIp(this.getRequest());
			appLogoutLog.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				appLogoutLog.setCountry(address[0]);
				appLogoutLog.setProvince(address[1]);
				appLogoutLog.setCity(address[2]);
			}
			appLogoutLogService.insertSelective(appLogoutLog);
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
}
