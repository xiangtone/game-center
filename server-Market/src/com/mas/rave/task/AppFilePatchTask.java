package com.mas.rave.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileList;
import com.mas.rave.main.vo.AppFilePatch;
import com.mas.rave.service.AppFileListService;
import com.mas.rave.service.AppFilePatchService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

public class AppFilePatchTask extends Thread {

	private AppFile appFile;
	private AppFileListService appFileListService;
	private AppFilePatchService appFilePatchService;
	

	public void setAppFile(AppFile appFile) {
		this.appFile = appFile;
	}
	public void setAppFileListService(AppFileListService appFileListService) {
		this.appFileListService = appFileListService;
	}
	public void setAppFilePatchService(AppFilePatchService appFilePatchService) {
		this.appFilePatchService = appFilePatchService;
	}

	@Override
	public void run() {
		//super.run();
		//System.out.println("AppFilePathcTask...");
		try{
			if(appFile!=null){
				dealAppFilePatch(appFile);
			}		
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void dealAppFilePatch(AppFile appFile) {
		try {
			List<AppFileList> appFileList = appFileListService.getAppFileLists(appFile.getId());
			//有旧版本的列表
			if(appFileList!=null && appFileList.size()>0 ){
				//先增加再删除
				for(AppFileList o:appFileList){
					//System.out.println("##########################dealAppFilePatch,new="+appFile.getVersionCode()+",oldList="+o.getVersionCode());
					if(appFile.getVersionCode()>o.getVersionCode()){
						// 组装文件地址
						String str = FileAddresUtil.getFilePath(appFile.getChannel().getProvince().getId(), appFile.getCp().getId(), appFile.getChannel().getId(), appFile.getAppInfo().getId(), Constant.PATCH_ADR);
						if (appFile.getAppInfo().getFree() == 1) {
							// 平台
							str = str.substring(0,str.length()-1);
							str += Constant.LOCAL_APK_MARKET;
						}
						//创建目录
						FileUtil.mkdirs(Constant.LOCAL_FILE_PATH + str);
						// 随机生成名字
						str += getRandApkFileName(); //str = url
						//生成差异包
						try{
							int fileSize =0;
							String md5="";
							boolean flag = false;
							File file1 = new File(Constant.LOCAL_FILE_PATH +o.getUrl());
							File file2 = new File(Constant.LOCAL_FILE_PATH +appFile.getUrl());
							//确保是linux环境才运行  && file1存在 && file2存在
							if("linux".equalsIgnoreCase(Constant.RUN_ENV) && "true".equals(Constant.RUN_S3) && file1.exists() && file2.exists() ){
								try{
									flag = ApkService.diff(Constant.LOCAL_FILE_PATH +o.getUrl(),
											Constant.LOCAL_FILE_PATH +appFile.getUrl(), 
											Constant.LOCAL_FILE_PATH + str);	
									fileSize = (int)FileUtil.genFileSize(Constant.LOCAL_FILE_PATH + str);
									md5 = getApkMD5(Constant.LOCAL_FILE_PATH +o.getUrl());
								}catch(Exception exx){
									exx.toString();
								}
								//System.out.println("##########################md5="+md5);
								if(flag==true && StringUtils.isNotEmpty(md5)){
									AppFilePatch appFilePatch = newAppFilePatch(appFile, o,str,fileSize,md5);
									appFilePatchService.addAppFilePatch(appFilePatch);
									S3Util.uploadS3(str, false);
								}else{
									System.out.println("$$$生成差异包失败:appName="+appFile.getAppName()+",flag="+flag+",md5="+md5);
									//delete patch file
									File f = new File(Constant.LOCAL_FILE_PATH + str);
									if(f.exists()){
										f.delete();
									}
								}
							}
						}catch(Exception e ){
							System.out.println("###生成差异包:"+e.toString());
						}
					}
				}//end for
				//--------清处旧的patch-----------
				List<AppFilePatch> appFilePatch = appFilePatchService.getAppFilePatchs(appFile.getId());
				//System.out.println("***appFilePatch.size()="+appFilePatch.size());
				if(appFilePatch!=null && appFilePatch.size()>0){
					for(AppFilePatch o:appFilePatch){
						if(o.getVersionCode()<appFile.getVersionCode()){
							System.out.println("###old patch delete.url="+o.getUrl());
							//删除
							FileUtil.deleteFile(Constant.LOCAL_FILE_PATH +o.getUrl());
							appFilePatchService.delAppFilePatch(o.getId());
						}
					}
				}
				//------------------------
			}
		} catch (Exception e) {
			//System.out.println("###dealAppFilePatch:"+e.toString());
			e.printStackTrace();
		}
	}
	private AppFilePatch newAppFilePatch(AppFile appFile, AppFileList o,String url,int fileSize, String lowMD5) {
		AppFilePatch appFilePatch = new AppFilePatch();
		appFilePatch.setApkId(appFile.getId());
		appFilePatch.setAppInfo(appFile.getAppInfo());
		appFilePatch.setChannel(appFile.getChannel());
		appFilePatch.setCp(appFile.getCp());
		appFilePatch.setAppName(appFile.getAppName());
		appFilePatch.setLowMD5(lowMD5);
		appFilePatch.setLowPackageName(o.getPackageName());
		appFilePatch.setLowVersionCode(o.getVersionCode());
		appFilePatch.setLowVersionName(o.getVersionName());
		appFilePatch.setPackageName(appFile.getPackageName());
		appFilePatch.setVersionCode(appFile.getVersionCode());
		appFilePatch.setVersionName(appFile.getVersionName());
		appFilePatch.setUrl(url);
		appFilePatch.setFileSize(fileSize);
		appFilePatch.setState(true);
		appFilePatch.setLowFileSize(o.getFileSize());
		return appFilePatch;
	}
	/*
	 * 随机的apk文件名
	 */
	private String getRandApkFileName() {
		return RandNum.getRandNumStr() + ".apk";
	}
	/*
	 * 生成md5
	 */
	private String getApkMD5(String path){
		Runtime run = Runtime.getRuntime();
		InputStream in;
		Process p;
		try {
			String[] cmd = new String[]{"sh","-c","unzip -p \""+path+"\"  *.RSA | keytool -printcert |grep MD5:"};
			p = run.exec(cmd);
            in = p.getInputStream();  
            BufferedReader read = new BufferedReader(new InputStreamReader(in));  
            String result = read.readLine();
//            System.out.println("INFO:"+result);
            if(result!=null){
            	return result.replace("MD5:", "").replace(":", "").trim();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
