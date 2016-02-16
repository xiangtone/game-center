package com.mas;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.data.AppRequest;
import com.mas.data.AppResponse;
import com.mas.data.Data;
import com.mas.market.pojo.TAppFile;
import com.mas.market.service.TAppFileService;
import com.mas.util.VM;
/**
 * @author lx
 */
@Service
@Path(value="/apkUpgrade")
@Produces("application/json")
public class ApkUpgradeResource extends BaseResoure{
	
	@Autowired
	private TAppFileService tAppFileService;

	
	@POST
	@Path(value="FourmsApkUpgrade")
	public AppResponse upgrade(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(90000);
		rep.setIslogin(null);
		try {
			Data data=req.getData();
			if(data==null||data.getAppPackageName()==null||data.getAppVersionCode()==null){
				throw new Exception("app parameter error");
			}
			Data d = getZappForUpgrade(data.getAppId(), data.getAppPackageName(),data.getAppVersionCode(),data.getMd5());
			if(null!=d){
				/*
				if(d.getUpgradeType()!=1){
					rep.setIsUpgrade(true);
				}else{
					rep.setIsUpgrade(false);
					d.setUpdateInfo("");
				}
				*/
				rep.setIsUpgrade(true);
				rep.setData(d);
			}else{
				rep.setIsUpgrade(false);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	private Data getZappForUpgrade(Integer appId,String packageName,Integer versionCode,String md5) throws Exception {
		com.mas.market.pojo.Criteria c=new com.mas.market.pojo.Criteria();
		//c.put("apkKey", apkKey);
		c.put("packageName", packageName);
		//c.put("appId", appId);
		c.put("versionCode", versionCode);
		if(null!=md5 && !"".equals(md5.trim())){
			c.put("md5", md5);
		}
    	c.setOrderByClause("versionCode desc");
    	//List<TAppFile> apks=tAppFileService.getZappForUpgrade(c);
    	List<TAppFile> apks=tAppFileService.getCommonApkUpgrade(c);
    	if(apks==null||apks.isEmpty()){
    		return null;
    	}else{
    		TAppFile apk = apks.get(0);
    		if(apk!=null && apk.getVersionCode()>versionCode){
				Data d = new Data();
				d.setUrl(VM.getInatance().getResServer()+apk.getUrl());
				d.setFileSize(apk.getFileSize());
				d.setUpdateInfo(replace(apk.getUpdateInfo()));
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
	
	private String replace(String info){
		if(!StringUtils.isEmpty(info)){
			info = info.replace("<br/>", "\n");
		}
		return info;
	}
	
}
