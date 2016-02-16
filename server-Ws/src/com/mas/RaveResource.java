package com.mas;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.data.AppDownloadData;
import com.mas.data.AppDownloadResponse;
import com.mas.data.AppRequest;
import com.mas.data.AppResponse;
import com.mas.data.BaseResponse;
import com.mas.data.ClientAppRequest;
import com.mas.data.Data;
import com.mas.data.MasData;
import com.mas.data.MustHaveData;
import com.mas.data.Page;
import com.mas.data.PushMessageData;
import com.mas.data.PushMessageResponse;
import com.mas.data.RaveAppRequest;
import com.mas.data.RaveAppResponse;
import com.mas.data.RaveData;
import com.mas.data.RaveResponse;
import com.mas.log.InsertLogService;
import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.RaveColumn;
import com.mas.market.pojo.RaveEntrance;
import com.mas.market.pojo.StarsEnum;
import com.mas.market.pojo.TAppAlbumColumn;
import com.mas.market.pojo.TAppAlbumRes;
import com.mas.market.pojo.TAppAlbumTheme;
import com.mas.market.pojo.TAppCollection;
import com.mas.market.pojo.TAppComment;
import com.mas.market.pojo.TAppFile;
import com.mas.market.pojo.TAppPicture;
import com.mas.market.pojo.TCategory;
import com.mas.market.pojo.TClientFeedback;
import com.mas.market.pojo.TClientFeedbackZapp;
import com.mas.market.pojo.TClientFeedbackZappcode;
import com.mas.market.pojo.TClientSkin;
import com.mas.market.pojo.TClientSkinCode;
import com.mas.market.pojo.TRaveCountry;
import com.mas.market.pojo.TSearchKeyword;
import com.mas.market.service.PushMessageService;
import com.mas.market.service.TAppAlbumColumnService;
import com.mas.market.service.TAppAlbumResService;
import com.mas.market.service.TAppAlbumThemeService;
import com.mas.market.service.TAppCollectionService;
import com.mas.market.service.TAppCommentService;
import com.mas.market.service.TAppFileService;
import com.mas.market.service.TAppPictureService;
import com.mas.market.service.TCategoryService;
import com.mas.market.service.TClientAppInfoService;
import com.mas.market.service.TClientFeedbackService;
import com.mas.market.service.TClientFeedbackZappService;
import com.mas.market.service.TClientFeedbackZappcodeService;
import com.mas.market.service.TClientSkinCodeService;
import com.mas.market.service.TClientSkinService;
import com.mas.market.service.TSearchKeywordService;
import com.mas.util.AddressUtils;
import com.mas.util.DateUtil;
import com.mas.util.VM;
import com.mas.ws.pojo.AppDownloadLog;
import com.mas.ws.pojo.AppDownloadLogIcon;
import com.mas.ws.pojo.AppSearchLog;
import com.mas.ws.pojo.MasUser;
import com.mas.ws.pojo.RaveDebacleLog;
import com.mas.ws.pojo.SkinDownloadLog;
import com.mas.ws.service.AppChangeuserLogService;
import com.mas.ws.service.AppDownloadLogService;
import com.mas.ws.service.AppPageopenLogService;
import com.mas.ws.service.AppSearchLogService;
import com.mas.ws.service.MasUserService;
import com.mas.ws.service.RaveDebacleLogService;
import com.mas.ws.service.SkinDownloadLogService;
/**
 * @author hhk
 */
@Service
@Path(value="/rave")
@Produces("application/json")
public class RaveResource  extends BaseResoure{
	private static Integer max_age = 300;
	@Autowired
	private TClientAppInfoService tClientAppInfoService;
	@Autowired
	private TAppAlbumColumnService tAppAlbumColumnService;
	@Autowired
	private TAppAlbumThemeService tAppAlbumThemeService;
	@Autowired
	private TCategoryService tCategoryService;
	@Autowired
	private TAppAlbumResService tAppAlbumResService;
	@Autowired
	private TAppPictureService tAppPictureService;
	@Autowired
	private TAppFileService tAppFileService;
	@Autowired
	private TAppCommentService tAppCommentService;
	@Autowired
	private MasUserService masUserService;
	@Autowired
	private AppChangeuserLogService appChangeuserLogService;
	@Autowired
	private AppSearchLogService appSearchLogService;
	@Autowired
	private RaveDebacleLogService raveDebacleLogService;
	@Autowired
	private TClientFeedbackService tClientFeedbackService;
	@Autowired
	private AppDownloadLogService appDownloadLogService; 
	@Autowired
	private AppPageopenLogService appPageopenLogService;
	@Autowired
	private TSearchKeywordService tSearchKeywordService;
	@Autowired
	private TClientFeedbackZappService tClientFeedbackZappService;
	@Autowired
	private TClientFeedbackZappcodeService tClientFeedbackZappcodeService;
	@Autowired
	private TAppCollectionService tAppCollectionService;
	@Autowired
	private TClientSkinCodeService tClientSkinCodeService;
	@Autowired
	private TClientSkinService tClientSkinService;
	@Autowired
	private SkinDownloadLogService skinDownloadLogService;
	@Autowired
	private PushMessageService pushMessageService;
	
	@POST
	@Path(value="start")
	public RaveResponse start(RaveAppRequest req){
		RaveData data = req.getData();
		RaveResponse rep = new RaveResponse();
		rep.setAutoRaveId(this.getAutoCountry(data.getRaveId()));
		rep.setTrxrc(30000);
		return rep;
	}
	@GET
	@Path("/list")
	public RaveAppResponse list(@QueryParam("raveId") Integer raveId,@QueryParam("ct") Integer ct,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30002);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("columnId",ct);
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TAppAlbumRes> list;
			if(45==ct.intValue()){
				list = tAppAlbumResService.columnlistByLiveWallpaper(cr);
			}else{
				list = tAppAlbumResService.columnlist(cr);
			}
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setAppNum(0);
			}else{
				List<TAppAlbumRes> applist = new ArrayList<TAppAlbumRes>();
				for(TAppAlbumRes res:list){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setBrief(res.getBrief());
					app.setAppName(res.getAppName());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					if(45==ct.intValue()){
						app.setFileType(1);
					}else{
						Integer fileType = getFileType(res.getCategoryId());
						app.setFileType(fileType);
					}
					applist.add(app);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setAppNum(list.size());
				rep.setApplist(applist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/hometheme")
	public RaveResponse hometheme(@QueryParam("raveId") Integer raveId,@QueryParam("ct") Integer ct){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30012);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("columnId", ct);
			List<TAppAlbumTheme> list = tAppAlbumThemeService.hometheme(cr);
			List<RaveColumn> themelist = new ArrayList<RaveColumn>();
			for(TAppAlbumTheme res:list){
				RaveColumn theme = new RaveColumn();
				theme.setBigicon(VM.getInatance().getResServer()+res.getBigicon());
				theme.setIcon(VM.getInatance().getResServer()+res.getBigicon());
				theme.setName(res.getName());
				theme.setDescription(res.getDescription());
				theme.setApkId(res.getApkId());
				Integer fileType = getFileType(res.getCategoryId());
				theme.setFileType(fileType);
				themelist.add(theme);
			}
			rep.setThemelist(themelist);
			rep.setThemeNum(themelist.size());
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	/*
	@GET
	@Path("/category")
	public RaveResponse category(@QueryParam("ct") Integer ct,@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30010);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("fatherId", ct);
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.setOrderByClause(" sort desc,createTime desc ");
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TCategory> list = tCategoryService.selectByCtRaveId(cr);
			List<RaveColumn> categorylist = new ArrayList<RaveColumn>();
			for(TCategory cate:list){
				RaveColumn category = new RaveColumn();
				category.setCategoryId(cate.getId());
				category.setBigicon(VM.getInatance().getResServer()+cate.getBigicon());
				category.setIcon(VM.getInatance().getResServer()+cate.getBigicon());
				category.setName(cate.getName());
				category.setRecommend(cate.getRecommend());
				categorylist.add(category);
			}
			rep.setCategorylist(categorylist);
			rep.setCategoryNum(categorylist.size());
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	*/
	
	@GET
	@Path("/category")
	public RaveResponse category(@QueryParam("ct") Integer ct,@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30010);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			List<Integer> catIds = getAllCatIds(ct,raveId);
			if(catIds != null && catIds.size() > 0){
				cr.put("catIds", catIds);
			}else{
				cr.put("catIds", null);
			}
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.setOrderByClause(" sort desc,createTime desc ");
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TCategory> list = tCategoryService.selectByAllCatIds(cr);
			List<RaveColumn> categorylist = new ArrayList<RaveColumn>();
			for(TCategory cate:list){
				RaveColumn category = new RaveColumn();
				category.setCategoryId(cate.getId());
				category.setBigicon(VM.getInatance().getResServer()+cate.getBigicon());
				category.setIcon(VM.getInatance().getResServer()+cate.getBigicon());
				category.setName(cate.getName());
				category.setRecommend(cate.getRecommend());
				categorylist.add(category);
			}
			rep.setCategorylist(categorylist);
			rep.setCategoryNum(categorylist.size());
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	
	@GET
	@Path("/levelCategory")
	public RaveResponse levelCategory(@QueryParam("ct") Integer ct,@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30034);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("fatherId", ct);
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("levelOne", "levelOne");
			cr.setOrderByClause(" sort desc,createTime desc ");
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TCategory> list = tCategoryService.selectLevelCat(cr);
			List<RaveColumn> categorylist = new ArrayList<RaveColumn>();
			for(TCategory cate:list){
				RaveColumn category = new RaveColumn();
				category.setCategoryId(cate.getId());
				category.setBigicon(VM.getInatance().getResServer()+cate.getBigicon());
				category.setIcon(VM.getInatance().getResServer()+cate.getBigicon());
				category.setName(cate.getName());
				category.setRecommend(cate.getRecommend());
				cr.put("levelOne", null);
				cr.put("levelTwo", "levelTwo");
				cr.put("fatherId", cate.getId());
				cr.setMysqlOffset(null);
				cr.setMysqlLength(null);
				List<TCategory> secList = tCategoryService.selectLevelCat(cr);
				if(secList != null && secList.size() > 0){
					List<RaveColumn> secondaryCatList = new ArrayList<RaveColumn>();
					for(TCategory secCat:secList){
						RaveColumn secCategory = new RaveColumn();
						secCategory.setCategoryId(secCat.getId());
						secCategory.setName(secCat.getName());
						secondaryCatList.add(secCategory);
					}
					category.setSecondaryCatListSize(secondaryCatList.size());
					category.setSecondaryCatList(secondaryCatList);
				}else{
					category.setSecondaryCatListSize(0);
				}
				categorylist.add(category);
			}
			if(list.size() < ps){
				rep.setIsLast(true);
			}
			rep.setCategorylist(categorylist);
			rep.setCategoryNum(categorylist.size());
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	
	@GET
	@Path("/categorylist")
	public RaveAppResponse categorylist(@QueryParam("raveId") Integer raveId,@QueryParam("column") String column,@QueryParam("categoryId") Integer categoryId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30011);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			//cr.put("categoryId",categoryId);
			List<Integer> categoryIds = getCatIds(categoryId);//适应games和apps二级分类修改
			Integer fileType = getFileType(categoryId);
			cr.put("categoryIds",categoryIds);
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			if("hot".equals(column)){
				cr.setOrderByClause( " t1.realDowdload desc " );
				//list = tAppAlbumResService.categoryHotList(cr);//查询t_app_category_res表
			}else if("new".equals(column)){
				cr.setOrderByClause( " t2.updateTime desc,t2.createTime desc " );
				//list = tAppAlbumResService.categorylist(cr);//查询t_app_info
			}
			List<TAppAlbumRes> list = tAppAlbumResService.categorylist(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setAppNum(0);
			}else{
				List<TAppAlbumRes> applist = new ArrayList<TAppAlbumRes>();
				for(TAppAlbumRes res:list){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setAppName(res.getAppName());
					app.setBrief(res.getBrief());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					app.setFileType(fileType);
					applist.add(app);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setAppNum(list.size());
				rep.setApplist(applist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/app")
	public RaveAppResponse appDetailByApkId(@QueryParam("apkId") Integer apkId,@QueryParam("raveId") Integer raveId,@QueryParam("ct") Integer ct){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30003);
		try {
			TAppAlbumRes res = tAppAlbumResService.appDetailByApkId(apkId);
			if(null!=res){
				TAppAlbumRes app = new TAppAlbumRes();
				app.setApkId(apkId);
				app.setAppId(res.getAppId());
				app.setAppName(res.getAppName());
				app.setBrief(res.getBrief());
				app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
				app.setStars(res.getStars());
				Float starsReal = res.getStarsReal();
				if(null == starsReal || starsReal==0){
					app.setStarsReal((float)res.getStars());
				}else{
					app.setStarsReal(starsReal);
				}
				app.setFileSize(res.getFileSize());
				app.setUrl(VM.getInatance().getResServer()+res.getUrl());
				app.setPublishTime(DateUtil.formatDate(res.getCreateTime(),DateUtil.DATE_FORMAT_DAY)); // 时间改为String
				app.setDescription(res.getDescription());
				app.setRealDowdload(res.getInitDowdload()+res.getRealDowdload());
				app.setPackageName(res.getPackageName());
				app.setVersionCode(res.getVersionCode());
				app.setVersionName(res.getVersionName());
				app.setCategoryId(res.getCategoryId());
				Integer fileType = getFileType(res.getCategoryId());
				app.setFileType(fileType);
				app.setIssuer(res.getIssuer());
				if(res.getFree() == 2){
					app.setAppSource("MAS");
				}else{
					app.setAppSource("Google");
				}
				rep.setApp(app);
				com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
				cr.put("appId",res.getAppId());
				cr.put("state",true);
				List<TAppPicture> list = tAppPictureService.selectByExample(cr);
				if(list == null || list.size() == 0){
					rep.setPicNum(0);
				}else{
					List<TAppPicture> piclist = new ArrayList<TAppPicture>();
					for(TAppPicture pic:list){
						TAppPicture apppic = new TAppPicture();
						apppic.setUrl(VM.getInatance().getResServer()+pic.getUrl());
						apppic.setLength(pic.getLength());
						apppic.setWidth(pic.getWidth());
						piclist.add(apppic);
					}
					rep.setPiclist(piclist);
					rep.setPicNum(list.size());
				}
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("appId",res.getAppId());
				List<TAppComment> starsGroup = tAppCommentService.selectByStarsGroup(cr);
				List<TAppComment> starslist = new ArrayList<TAppComment>();
				for(StarsEnum starsValue:StarsEnum.values()){
					TAppComment stars = new TAppComment();
					stars.setStars(starsValue.getValue());
					stars.setAmount(0);
					for(TAppComment t:starsGroup){
						if(t.getStars().intValue() == starsValue.getValue().intValue()){
							stars.setAmount(t.getId());
						}
					}
					starslist.add(stars);
				}
				rep.setStarslist(starslist);
				InsertLogService.app(appPageopenLogService,tAppAlbumResService,res,raveId,ct,this.getRequest());
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/appDetailByAppId")
	public RaveAppResponse appDetailByAppId(@QueryParam("appId") Integer appId,@QueryParam("raveId") Integer raveId,@QueryParam("ct") Integer ct){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30021);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("appId",appId);
			TAppAlbumRes res = tAppAlbumResService.appDetailByAppId(cr);
			if(null!=res){
				TAppAlbumRes app = new TAppAlbumRes();
				app.setApkId(res.getId());
				app.setAppId(res.getAppId());
				app.setAppName(res.getAppName());
				app.setBrief(res.getBrief());
				app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
				app.setStars(res.getStars());
				Float starsReal = res.getStarsReal();
				if(null == starsReal || starsReal==0){
					app.setStarsReal((float)res.getStars());
				}else{
					app.setStarsReal(starsReal);
				}
				app.setFileSize(res.getFileSize());
				app.setUrl(VM.getInatance().getResServer()+res.getUrl());
				app.setPublishTime(DateUtil.formatDate(res.getCreateTime(),DateUtil.DATE_FORMAT_DAY)); // 时间改为String
				app.setDescription(res.getDescription());
				app.setRealDowdload(res.getInitDowdload()+res.getRealDowdload());
				app.setPackageName(res.getPackageName());
				app.setVersionCode(res.getVersionCode());
				app.setVersionName(res.getVersionName());
				app.setCategoryId(res.getCategoryId());
				Integer fileType = getFileType(res.getCategoryId());
				app.setFileType(fileType);
				app.setIssuer(res.getIssuer());
				if(res.getFree() == 2){
					app.setAppSource("MAS");
				}else{
					app.setAppSource("Google");
				}
				rep.setApp(app);
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("appId",res.getAppId());
				List<TAppPicture> list = tAppPictureService.selectByExample(cr);
				if(list == null || list.size() == 0){
					rep.setPicNum(0);
				}else{
					List<TAppPicture> piclist = new ArrayList<TAppPicture>();
					for(TAppPicture pic:list){
						TAppPicture apppic = new TAppPicture();
						apppic.setUrl(VM.getInatance().getResServer()+pic.getUrl());
						apppic.setLength(pic.getLength());
						apppic.setWidth(pic.getWidth());
						piclist.add(apppic);
					}
					rep.setPiclist(piclist);
					rep.setPicNum(list.size());
				}
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("appId",res.getAppId());
				List<TAppComment> starsGroup = tAppCommentService.selectByStarsGroup(cr);
				List<TAppComment> starslist = new ArrayList<TAppComment>();
				for(StarsEnum starsValue:StarsEnum.values()){
					TAppComment stars = new TAppComment();
					stars.setStars(starsValue.getValue());
					stars.setAmount(0);
					for(TAppComment t:starsGroup){
						if(t.getStars().intValue() == starsValue.getValue().intValue()){
							stars.setAmount(t.getId());
						}
					}
					starslist.add(stars);
				}
				rep.setStarslist(starslist);
				InsertLogService.app(appPageopenLogService,tAppAlbumResService,res,raveId,ct,this.getRequest());
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/appPackageName")
	public RaveAppResponse appPackageName(@QueryParam("packageName") String packageName,@QueryParam("raveId") Integer raveId,@QueryParam("ct") Integer ct){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30016);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("packageName",packageName);
			TAppAlbumRes res = tAppAlbumResService.appDetailByPackageName(cr);
			if(null!=res){
				TAppAlbumRes app = new TAppAlbumRes();
				app.setApkId(res.getApkId());
				app.setAppId(res.getAppId());
				app.setAppName(res.getAppName());
				app.setBrief(res.getBrief());
				app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
				app.setStars(res.getStars());
				app.setFileSize(res.getFileSize());
				app.setUrl(VM.getInatance().getResServer()+res.getUrl());
				app.setPublishTime(DateUtil.formatDate(res.getCreateTime(),DateUtil.DATE_FORMAT_DAY)); // 时间改为String
				app.setDescription(res.getDescription());
				app.setRealDowdload(res.getInitDowdload()+res.getRealDowdload());
				app.setPackageName(res.getPackageName());
				app.setVersionCode(res.getVersionCode());
				app.setVersionName(res.getVersionName());
				app.setCategoryId(res.getCategoryId());
				Integer fileType = getFileType(res.getCategoryId());
				app.setFileType(fileType);
				app.setIssuer(res.getIssuer());
				if(res.getFree() == 2){
					app.setAppSource("MAS");
				}else{
					app.setAppSource("Google");
				}
				rep.setApp(app);
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("appId",res.getAppId());
				List<TAppPicture> list = tAppPictureService.selectByExample(cr);
				if(list == null || list.size() == 0){
					rep.setPicNum(0);
				}else{
					List<TAppPicture> piclist = new ArrayList<TAppPicture>();
					for(TAppPicture pic:list){
						TAppPicture apppic = new TAppPicture();
						apppic.setUrl(VM.getInatance().getResServer()+pic.getUrl());
						apppic.setLength(pic.getLength());
						apppic.setWidth(pic.getWidth());
						piclist.add(apppic);
					}
					rep.setPiclist(piclist);
					rep.setPicNum(list.size());
				}
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("appId",res.getAppId());
				List<TAppComment> starsGroup = tAppCommentService.selectByStarsGroup(cr);
				List<TAppComment> starslist = new ArrayList<TAppComment>();
				for(StarsEnum starsValue:StarsEnum.values()){
					TAppComment stars = new TAppComment();
					stars.setStars(starsValue.getValue());
					stars.setAmount(0);
					for(TAppComment t:starsGroup){
						if(t.getStars().intValue() == starsValue.getValue().intValue()){
							stars.setAmount(t.getId());
						}
					}
					starslist.add(stars);
				}
				rep.setStarslist(starslist);
				ct = 108;raveId=1;
				InsertLogService.app(appPageopenLogService,tAppAlbumResService,res,raveId,ct,this.getRequest());
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/recommendlist")
	public RaveAppResponse recommendlist(@QueryParam("categoryId") Integer categoryId,@QueryParam("raveId") Integer raveId,@QueryParam("appId") Integer appId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30009);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("categoryId",categoryId);
			cr.put("appId",appId);
			cr.setMysqlLength(ps);
			List<TAppAlbumRes> list = tAppAlbumResService.searchRecommend(cr);
			if(list == null || list.size() == 0){
				rep.setAppNum(0);
			}else{
				List<TAppAlbumRes> recommendlist = new ArrayList<TAppAlbumRes>();
				for(TAppAlbumRes res:list){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setAppName(res.getAppName());
					app.setBrief(res.getBrief());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					Integer fileType = getFileType(res.getCategoryId());
					app.setFileType(fileType);
					/*
					app.setIssuer(res.getIssuer());
					if(res.getFree() == 0){
						app.setAppSource("Google");
					}else if(res.getFree() == 2){
						app.setAppSource("MAS");
					}
					*/
					recommendlist.add(app);
				}
				rep.setRecommendNum(recommendlist.size());
				rep.setRecommendlist(recommendlist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/commentlist")
	public RaveAppResponse commentlist(@QueryParam("appId") Integer appId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30004);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			cr.put("appId",appId);
			//cr.put("userIdNotEq", 0);
			cr.setOrderByClause( " createTime desc " );
			List<TAppComment> list = tAppCommentService.selectByExample(cr);
			
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setAppNum(0);
			}else{
				List<TAppComment> commentlist = new ArrayList<TAppComment>();
				for(TAppComment res:list){
					TAppComment comment = new TAppComment();
					comment.setUserName(res.getNickName()==null?res.getUserName():res.getNickName());
					comment.setContent(res.getContent());
					comment.setPublishTime(DateUtil.formatDate(res.getCreateTime(),DateUtil.DATE_FORMAT_SECOND)); // 时间改为String
					comment.setStars(res.getStars());
					comment.setDeviceModel(res.getDeviceModel());
					comment.setDeviceVendor(res.getDeviceVendor());
					comment.setOsVersion(res.getOsVersion());
					comment.setOsVersionName(res.getOsVersionName());
					comment.setDeviceType(res.getDeviceType());
					commentlist.add(comment);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setCommentNum(commentlist.size());
				rep.setCommentlist(commentlist);
			}
			
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age=0");
		return rep;
	}
	
	/*
	 * 不登录不能评论
	@POST
	@Path("/comment")
	public RaveAppResponse comment(RaveAppRequest req){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30005);
		try {
			MasUser masUser = req.getMasUser();
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user && masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
					RaveData data = req.getData();
					final Integer appId = data.getAppId();
					TAppComment comment = new TAppComment();
					comment.setAppId(appId);
					comment.setClientId(data.getClientId());
					comment.setStars(data.getStars());
					comment.setContent(data.getContent());
					comment.setUserId(user.getUserId());
					comment.setUserName(user.getUserName());
					comment.setNickName(user.getNickName());
					comment.setDeviceModel(data.getDeviceModel());
					comment.setDeviceVendor(data.getDeviceVendor());
					comment.setOsVersion(data.getOsVersion());
					comment.setOsVersionName(data.getOsVersionName());
					comment.setDeviceType(data.getDeviceType());
					tAppCommentService.insertSelective(comment);
				}else{
					rep.getState().setCode(201);
					rep.getState().setMsg("account password is incorrect,please relogin");
				}
			}else{
				rep.getState().setCode(201);
				rep.getState().setMsg("account password is incorrect,please relogin");
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	*/
	
	
//	@GET
//	@Path("/comment")
	@POST
	@Path("/comment")
	public RaveAppResponse comment(RaveAppRequest req){
		RaveAppResponse rep = new RaveAppResponse();
		/*
		RaveAppRequest req = new RaveAppRequest();
		MasUser test1 = new MasUser();
		test1.setUserId(12);
		test1.setUserName("45054039@qq.com");
		test1.setUserPwd("e10adc3949ba59abbe56e057f20f883");
		RaveData test2 = new RaveData();
		test2.setAppId(510901);
		test2.setClientId(78);
		test2.setDeviceModel("ONE TOUCH 6012A");
		test2.setDeviceType(1);
		test2.setDeviceVendor("TCT");
		test2.setOsVersion("17");
		test2.setOsVersionName("4.2.2");
		test2.setStars(5);
		test2.setContent("这也是一个测试");
		req.setMasUser(test1);
		req.setData(test2);
		*/
		rep.setTrxrc(30005);
		try {
			MasUser masUser = req.getMasUser();
			RaveData data = req.getData();
			final Integer appId = data.getAppId();
			TAppComment comment = new TAppComment();
			if(null != masUser && masUser.getUserId()!=0){
				MasUser user = masUserService.selectByPrimaryKey(masUser.getUserId());
				if(null!=user && masUser.getUserName().equals(user.getUserName()) && masUser.getUserPwd().equals(user.getUserPwd())){
					comment.setUserId(user.getUserId());
					comment.setUserName(user.getUserName());
					comment.setNickName(user.getNickName());
				}else{
					rep.getState().setCode(201);
					rep.getState().setMsg("account password is incorrect,please relogin");
					return rep;
				}
			}else{
				comment.setUserId(0);
				comment.setUserName("Visitor");
				comment.setNickName("Visitor");
				//rep.getState().setCode(201);
				//rep.getState().setMsg("account password is incorrect,please relogin");
			}
			comment.setAppId(appId);
			comment.setClientId(data.getClientId());
			comment.setStars(data.getStars());
			comment.setContent(data.getContent());
			comment.setDeviceModel(data.getDeviceModel());
			comment.setDeviceVendor(data.getDeviceVendor());
			comment.setOsVersion(data.getOsVersion());
			comment.setOsVersionName(data.getOsVersionName());
			comment.setDeviceType(data.getDeviceType());
			tAppCommentService.insertSelective(comment);
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	
	@POST
	@Path("/searchApps")
	public RaveAppResponse searchApps(final RaveAppRequest req,@QueryParam("searchId") final Integer searchId){
		RaveAppResponse rep=new RaveAppResponse();
		rep.setTrxrc(30007);
		try {
			final RaveData data=req.getData();
			final String content = StringUtils.lowerCase(data.getContent().trim());
			Integer raveId = data.getRaveId();
			Page page = req.getPage();
			int ps = page.getPs(),pn = page.getPn();
			final int startIndex=getStartIndex(ps,pn);
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("albumId",2);
			cr.put("flag",2);
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("keyword",content);
			List<TSearchKeyword> keywordlist = tSearchKeywordService.selectKeywords(cr);
			if(null!=keywordlist && keywordlist.size()>0){
				TSearchKeyword keyword = keywordlist.get(0);
				return this.keywordAppsList(raveId,keyword.getSearchId(), pn, ps);
			}
			cr = new  com.mas.market.pojo.Criteria();
			cr.put("raveId",raveId);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			//---------------add by lixin-----------------
			List<TAppAlbumRes> list = new ArrayList<TAppAlbumRes>();
			String keyTemp = content;
			keyTemp = keyTemp.replaceAll(" ", ",").replaceAll(";", ",").replaceAll("，", ",").replaceAll("；", ",").replaceAll(",,", ",");
			String[] keyArray = keyTemp.split(",");
			if(keyTemp.contains(",") && !ArrayUtils.isEmpty(keyArray)){
				if(keyArray.length > 3){
					for(int i = 0; i < 3; i++){
						String key = "appNameLike"+i;
						cr.put(key, keyArray[i]);
					}
				}else{
					for(int i=0; i < keyArray.length; i++){
						String key = "appNameLike"+i;
						cr.put(key, keyArray[i]);
					}
				}
				list = tAppAlbumResService.searchAppsByArray(cr);
			}else{
				cr.put("appNameLike",content);
				list = tAppAlbumResService.searchApps(cr);
			}
			//--------------------------------------------
			
			
			//cr.put("appNameLike",content);
			//List<TAppAlbumRes> list = tAppAlbumResService.searchApps(cr);
			final List<TAppAlbumRes> applist = new ArrayList<TAppAlbumRes>();
			if(null != list && list.size()>0){
				for(TAppAlbumRes res:list){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setAppName(res.getAppName());
					app.setBrief(res.getBrief());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					Integer fileType = getFileType(res.getCategoryId());
					app.setFileType(fileType);
					applist.add(app);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
			}else{
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("columnId",1);
				cr.put("raveId",raveId);
				cr.setMysqlOffset(0);
				cr.setMysqlLength(6);
				cr.setOrderByClause( " RAND() " );
				List<TAppAlbumRes> list2 = tAppAlbumResService.selectByRAND(cr);
				List<TAppAlbumRes> recommendlist = new ArrayList<TAppAlbumRes>();
				for(TAppAlbumRes res:list2){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setAppName(res.getAppName());
					app.setBrief(res.getBrief());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					app.setFileType(res.getFileType());
					recommendlist.add(app);
				}
				rep.setRecommendlist(recommendlist);
				rep.setRecommendNum(recommendlist.size());
			}
			rep.setAppNum(applist.size());
			rep.setApplist(applist);
			new Thread(){public void run() {
				if(startIndex==0){
					MasUser user = req.getMasUser();
					AppSearchLog searchLog = new AppSearchLog();
					searchLog.setClientId(data.getClientId());
					searchLog.setContent(content);
					searchLog.setUserId(user.getUserId());
					searchLog.setUserName(user.getUserName());
					searchLog.setSearchNum(applist.size());
					appSearchLogService.insertSelective(searchLog);
				}
				if(null!=searchId){
					tSearchKeywordService.updateSearchNum(searchId);
				}
			}}.start();
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@POST
	@Path("/debacle")
	public RaveAppResponse debacle(RaveAppRequest req){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30008);
		try {
			RaveData data = req.getData();
			RaveDebacleLog debaclelog = new RaveDebacleLog();
			debaclelog.setClientId(data.getClientId());
			debaclelog.setContent(data.getContent());
			debaclelog.setDeviceModel(data.getDeviceModel());
			debaclelog.setDeviceVendor(data.getDeviceVendor());
			debaclelog.setOsVersion(data.getOsVersion());
			debaclelog.setOsVersionName(data.getOsVersionName());
			debaclelog.setDeviceType(data.getDeviceType());
			MasData mas = req.getMasPlay();
			debaclelog.setMasPackageName(mas.getMasPackageName());
			debaclelog.setMasVersionName(mas.getMasVersionName());
			debaclelog.setMasVersionCode(mas.getMasVersionCode());
			raveDebacleLogService.insertSelective(debaclelog);
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	@POST
	@Path("/appDownLoad")
	public BaseResponse appDownLoad(RaveAppRequest req){
		BaseResponse rep=new BaseResponse();
		rep.setTrxrc(30014);
		try {
			AppDownloadLog log = new AppDownloadLog();
			MasData mas = req.getMasPlay();
			log.setMasPackageName(mas.getMasPackageName());
			log.setMasVersionName(mas.getMasVersionName());
			log.setMasVersionCode(mas.getMasVersionCode());
			MasUser user = req.getMasUser();
			log.setUserId(user.getUserId());
			log.setUserName(user.getUserName());
			RaveData data = req.getData();
			log.setClientId(data.getClientId());
			log.setColumnId(data.getCt());
			log.setApkId(data.getApkId());
			log.setAppId(data.getAppId());
			log.setAppName(data.getAppName());
			log.setPackageName(data.getPackageName());
			log.setVersionCode(data.getVersionCode());
			log.setVersionName(data.getVersionName());
			log.setRaveId(data.getRaveId());
			log.setDownOrUpdate(data.getDownOrUpdate()==null?1:data.getDownOrUpdate());
			String ip = AddressUtils.getClientIp(this.getRequest());
			log.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				log.setCountry(address[0]);
				log.setProvince(address[1]);
				log.setCity(address[2]);
			}
			TAppAlbumRes tAppAlbumRes = tAppAlbumResService.getAppInfoByAppId(data.getAppId());
			log.setFree(tAppAlbumRes.getFree());
			appDownloadLogService.insertSelective(log);
			//if(data.getDownOrUpdate() == null || data.getDownOrUpdate() ==1){
				com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
				cr.put("appId",data.getAppId());
				tAppAlbumResService.updateAppDownLoad(cr);
			//}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	
	@POST
	@Path("/getUpdateApps")
	public RaveAppResponse getUpdateApps(ClientAppRequest req){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30001);
		try {
			List<Data> apps = req.getApps();
			if(req.getAppNum()!=apps.size()){
				rep.getState().setCode(501);
				rep.getState().setMsg("Transmission parameter error !");
			}else{
				HashMap<String,Data> mapApp = new HashMap<String,Data>();
				HashMap<String,Data> mapMd5 = new HashMap<String,Data>();
				List<String> packageList = new ArrayList<String>();
				for(Data app:apps){
					String packageName = app.getAppPackageName();
					packageList.add(packageName);
					mapApp.put(packageName,app);
					String md5 = app.getMd5();
					if(null!=md5 && !"".equals(md5.trim())){
						mapMd5.put(packageName,app);
					}
				}
				if(packageList.size()>0){
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("packageList", packageList);
					Integer raveId = req.getRaveId();
					if(null!=raveId){
						if(0==raveId){
							raveId = 1;	map.put("raveId",raveId);
						}else{
							map.put("raveId",raveId);
						}
					}else{
						raveId = 1;	map.put("raveId",raveId);
					}
					List<TAppAlbumRes> updateList = tAppAlbumResService.getAppsForUpdate(map);
					List<TAppAlbumRes> applist = new ArrayList<TAppAlbumRes>();
					for(TAppAlbumRes res:updateList){
						String packageName = res.getPackageName();
						Data dataApp = mapApp.get(packageName);
						if(null != dataApp && dataApp.getAppVersionCode()<res.getVersionCode()){
							TAppAlbumRes appres = new TAppAlbumRes();
							appres.setApkId(res.getApkId());
							appres.setAppId(res.getAppId());
							appres.setAppName(res.getAppName());
							appres.setBrief(res.getBrief());
							appres.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
							appres.setStars(res.getStars());
							appres.setFileSize(res.getFileSize());
							appres.setUrl(VM.getInatance().getResServer()+res.getUrl());
							appres.setIsPatch(false);
							appres.setPackageName(res.getPackageName());
							appres.setVersionCode(res.getVersionCode());
							appres.setVersionName(res.getVersionName());
							appres.setPublishTime(DateUtil.formatDate(res.getCreateTime(),DateUtil.DATE_FORMAT_DAY)); // 时间改为String
							appres.setCategoryId(res.getCategoryId());
							Integer fileType = getFileType(res.getCategoryId());
							appres.setFileType(fileType);
							Data data = mapMd5.get(packageName);
							if(null!=data){
								com.mas.market.pojo.Criteria criteria=new com.mas.market.pojo.Criteria();
								criteria.put("apkId", res.getApkId());
								criteria.put("md5", data.getMd5());
								criteria.put("lowVersionCode", data.getAppVersionCode());
								criteria.put("versionCode", res.getVersionCode());
								Integer lowFileSize = data.getFileSize();
								if(null!=lowFileSize && 0!=lowFileSize.intValue()){
									criteria.put("lowFileSize", lowFileSize);
								}
								TAppFile file = tAppFileService.getApkPatch(criteria);
								if(null!=file){
									String url = file.getUrl();
									if(null!=url && !"".equals(url)){
										appres.setUrlPatch(VM.getInatance().getResServer()+url);
										appres.setIsPatch(true);
										appres.setPatchSize(file.getFileSize());
										res.setIsPatch(true);
									}
								}
							}
							applist.add(appres);
						}
					}
					if(null != applist && applist.size()>0){
						rep.setApplist(applist);rep.setAppNum(applist.size());
					}else{
						rep.setAppNum(0);
					}
					//InsertLogService.getUpdateApps(tClientAppInfoService,req,updateList);
				}
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	@POST
	@Path(value="upgrade")
	public AppResponse upgrade(AppRequest req){
		AppResponse rep=new AppResponse();
		rep.setTrxrc(30015);
		rep.setIslogin(null);
		try {
			Data data=req.getData();
			if(data==null||data.getAppId()==null||data.getAppPackageName()==null||data.getAppVersionCode()==null){
				throw new Exception("app parameter error");
			}
			Data d = getZappForUpgrade(data.getAppId(), data.getApkKey(),data.getAppVersionCode(),data.getMd5());
			if(null!=d){
				if(d.getUpgradeType()!=1){
					rep.setIsUpgrade(true);
				}else{
					rep.setIsUpgrade(false);
					d.setUpdateInfo("");
				}
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
	private Data getZappForUpgrade(Integer appId,String apkKey,Integer versionCode,String md5) throws Exception {
		com.mas.market.pojo.Criteria c=new com.mas.market.pojo.Criteria();
		c.put("apkKey", apkKey);
		c.put("appId", appId);
		c.put("versionCode", versionCode);
		if(null!=md5 && !"".equals(md5.trim())){
			c.put("md5", md5);
		}
    	c.setOrderByClause("versionCode desc");
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
	@GET
	@Path("/country")
	public RaveResponse raveCountry(){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30017);
		Criteria example = new Criteria();
		example.put("state", true);
		List<TRaveCountry> raveCountryList = this.tRaveCountryService.selectByExample(example);
		List<TRaveCountry> countryList = new ArrayList<TRaveCountry>();
		TRaveCountry raveCountry = new TRaveCountry();
		raveCountry.setId(0);
		raveCountry.setName("Auto");
		raveCountry.setUrl(VM.getInatance().getResServer()+"picture/country/auto.png");
		countryList.add(raveCountry);
		for(TRaveCountry country:raveCountryList){
			raveCountry = new TRaveCountry();
			raveCountry.setId(country.getId());
			raveCountry.setName(country.getName());
			raveCountry.setUrl(VM.getInatance().getResServer()+country.getUrl());
			countryList.add(raveCountry);
		}
		rep.setCountryList(countryList);
		rep.setCountryNum(countryList.size());
		rep.setAutoRaveId(this.getAutoRaveId(this.getRequest()));
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	
	@GET
	@Path("/getSearchKeywords")
	public RaveAppResponse getSearchKeywords(@QueryParam("raveId") Integer raveId,@QueryParam("albumId") Integer albumId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30018);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("albumId",albumId);
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			cr.setOrderByClause(" sort desc,updateTime desc,createTime desc ");
			List<TSearchKeyword> list = tSearchKeywordService.selectKeywords(cr);
			if(list == null || list.size() == 0){
				rep.setAppNum(0);rep.setIsLast(true);
			}else{
				List<TSearchKeyword> keywordlist = new ArrayList<TSearchKeyword>();
				for(TSearchKeyword keywords:list){
					TSearchKeyword keyword = new TSearchKeyword();
					keyword.setKeyword(keywords.getKeyword());
					keyword.setIconUrl(keywords.getIconUrl()==null?"":VM.getInatance().getResServer()+keywords.getIconUrl());
					keyword.setResLogo(keywords.getResLogo()==null?"":VM.getInatance().getResServer()+keywords.getResLogo());
					int flag = keywords.getFlag();
					if(2==albumId){
						if(flag==0){
							keyword.setActionRc(30007);
							keyword.setAction("rs/rave/searchApps?searchId="+keywords.getSearchId());
						}else if(flag==1){
							keyword.setActionRc(30021);
							keyword.setAction("rs/rave/appDetailByAppId?appId="+keywords.getResId()+"&raveId="+raveId+"&ct=100");
						}else if(flag==2){
							keyword.setActionRc(30019);
							keyword.setAction("rs/rave/keywordAppsList?searchId="+keywords.getSearchId()+"&raveId="+raveId);
						}
					}else if(4==albumId){
						if(flag==0){
							keyword.setActionRc(31005);
							keyword.setAction("rs/res/searchMusic?searchId="+keywords.getSearchId());
						}else if(flag==1){
							keyword.setActionRc(0);
							keyword.setAction("zapp deal music download");
						}else if(flag==2){
							keyword.setActionRc(31007);
							keyword.setAction("rs/res/keywordMusicList?searchId="+keywords.getSearchId()+"&raveId="+raveId);
						}
					}else if(5==albumId){
						if(flag==0){
							keyword.setActionRc(32005);
							keyword.setAction("rs/res/searchImage?searchId="+keywords.getSearchId());
						}else if(flag==1){
							keyword.setActionRc(0);
							keyword.setAction("zapp deal image edit");
						}else if(flag==2){
							keyword.setActionRc(32007);
							keyword.setAction("rs/res/keywordImageList?searchId="+keywords.getSearchId()+"&raveId="+raveId);
						}
					}
					keywordlist.add(keyword);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setKeywordNum(keywordlist.size());
				rep.setKeywordlist(keywordlist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/keywordAppsList")
	public RaveAppResponse keywordAppsList(@QueryParam("raveId") Integer raveId,@QueryParam("searchId") final Integer searchId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30019);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("searchId",searchId);
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TAppAlbumRes> list = tAppAlbumResService.keywordAppsList(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setAppNum(0);
			}else{
				List<TAppAlbumRes> applist = new ArrayList<TAppAlbumRes>();
				for(TAppAlbumRes res:list){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setAppName(res.getAppName());
					app.setBrief(res.getBrief());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					Integer fileType = getFileType(res.getCategoryId());
					app.setFileType(fileType);
					applist.add(app);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setAppNum(list.size());
				rep.setApplist(applist);
				new Thread(){public void run() {
					if(null!=searchId){
						tSearchKeywordService.updateSearchNum(searchId);
					}
				}}.start();
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@POST
	@Path("/searchAppsTip")
	public BaseResponse searchAppsTip(RaveAppRequest req){
		RaveAppResponse rep=new RaveAppResponse();
		rep.setTrxrc(30020);
		try {
			final RaveData data=req.getData();
			final String content = StringUtils.lowerCase(data.getContent().trim());
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("appNameLike",content);
			Integer raveId = data.getRaveId();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			Page page = req.getPage();
			int ps = page.getPs(),pn = page.getPn();
			final int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TAppAlbumRes> list = tAppAlbumResService.searchAppsTip(cr);
			final List<TAppAlbumRes> applist = new ArrayList<TAppAlbumRes>();
			if(null != list && list.size()>0){
				for(int i=0;i<list.size();i++){
					TAppAlbumRes res = list.get(i);
					TAppAlbumRes app = new TAppAlbumRes();
					if(i==0){
						app.setApkId(res.getApkId());
						app.setAppId(res.getAppId());
						app.setAppName(res.getAppName());
						app.setBrief(res.getBrief());
						app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
						app.setStars(res.getStars());
						app.setFileSize(res.getFileSize());
						app.setUrl(VM.getInatance().getResServer()+res.getUrl());
						app.setPackageName(res.getPackageName());
						app.setVersionCode(res.getVersionCode());
						app.setVersionName(res.getVersionName());
						app.setCategoryId(res.getCategoryId());
						Integer fileType = getFileType(res.getCategoryId());
						app.setFileType(fileType);
					}else{
						app.setAppName(res.getAppName());
					}
					applist.add(app);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setAppNum(applist.size());
				rep.setApplist(applist);
			}else{
				rep.setIsLast(true);rep.setAppNum(0);
			}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@POST
	@Path("/feedback")
	public RaveAppResponse feedback(RaveAppRequest req){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30013);
		try {
				RaveData data = req.getData();
				MasUser user = req.getMasUser();
				TClientFeedback feedback = new TClientFeedback();
				feedback.setClientId(data.getClientId());
				feedback.setImei(data.getImei());
				feedback.setContent(data.getContent());
				feedback.setUserId(user.getUserId());
				feedback.setUserName(user.getUserName());
				feedback.setEmail(data.getEmail()==null?user.getUserName():data.getEmail());
				feedback.setDeviceModel(data.getDeviceModel());
				feedback.setDeviceVendor(data.getDeviceVendor());
				feedback.setOsVersion(data.getOsVersion());
				feedback.setOsVersionName(data.getOsVersionName());
				feedback.setDeviceType(data.getDeviceType());
				feedback.setCreateTime(new Date());
				MasData masPlay = req.getMasPlay();
				if(null!=masPlay){
					feedback.setMasPackageName(masPlay.getMasPackageName());
					feedback.setMasVersionCode(masPlay.getMasVersionCode());
					feedback.setMasVersionName(masPlay.getMasVersionName());
				}
				String ip = AddressUtils.getClientIp(this.getRequest());
				feedback.setIP(ip);
				String[] address = AddressUtils.getAddresses(ip);
				if(null!=address){
					feedback.setCountry(address[0]);
					feedback.setProvince(address[1]);
					feedback.setCity(address[2]);
				}
				feedback.setLookOver(true);
				tClientFeedbackService.insertSelective(feedback);
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	@GET
	@Path("/feedbackAttention")
	public RaveResponse feedbackAttention(@QueryParam("imei") String imei,@QueryParam("feedbackCode") Integer feedbackCode){
		RaveResponse rep = new RaveResponse();
		rep.setFeedbackAttention(false);
		rep.setTrxrc(30022);
		Criteria cr = new Criteria();
		TClientFeedbackZappcode code = tClientFeedbackZappcodeService.selectByPrimaryKey(1);
		rep.setFeedbackCode(code.getZappcode());
		cr = new Criteria();
		cr.put("imei",imei);
		cr.put("lookOver",false);
		List<TClientFeedback> list = tClientFeedbackService.selectByExample(cr);
		if(null != list && list.size() > 0){
			rep.setFeedbackAttention(true);
		}
		return rep;
	}
	@GET
	@Path("/getFeedbackCommon")
	public RaveResponse getFeedbackCommon(@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30023);
		Criteria cr = new Criteria();
		cr.put("state", true);
		int startIndex=getStartIndex(ps,pn);
		cr.setMysqlOffset(startIndex);
		cr.setMysqlLength(ps);
		cr.setOrderByClause(" createTime desc ");
		List<TClientFeedbackZapp> list = tClientFeedbackZappService.selectByExample(cr);
		List<TClientFeedbackZapp> feedbackList = new ArrayList<TClientFeedbackZapp>();
		if(list == null || list.size() == 0){
			rep.setIsLast(true);rep.setFeedbackNum(0);
		}else{
			for(TClientFeedbackZapp feedbacks:list){
				TClientFeedbackZapp feedback = new TClientFeedbackZapp();
				feedback.setSendTime(DateUtil.formatDate(feedbacks.getCreateTime(),DateUtil.DATE_FORMAT_SECOND)); // 时间改为String
				feedback.setQuestion(feedbacks.getQuestion());
				feedback.setReplyContent(feedbacks.getReplyContent());
				feedbackList.add(feedback);
			}
			if(feedbackList.size() < ps){
				rep.setIsLast(true);
			}
			rep.setFeedbackNum(feedbackList.size());
			rep.setFeedbackCommonList(feedbackList);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/getFeedbackClient")
	public RaveResponse getFeedbackClient(@QueryParam("imei") final String imei,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30024);
		Criteria cr = new Criteria();
		cr.put("imei",imei);
		int startIndex=getStartIndex(ps,pn);
		cr.setMysqlOffset(startIndex);
		cr.setMysqlLength(ps);
		cr.setOrderByClause(" createTime desc ");
		List<TClientFeedback> list = tClientFeedbackService.selectByExample(cr);
		List<TClientFeedback> feedbackList = new ArrayList<TClientFeedback>();
		if(list == null || list.size() == 0){
			rep.setIsLast(true);rep.setFeedbackNum(0);
		}else{
			for(TClientFeedback feedbacks:list){
				TClientFeedback feedback = new TClientFeedback();
				feedback.setSendTime(feedbacks.getCreateTime()); // 时间改为String
				feedback.setContent(feedbacks.getContent());
				feedback.setFeedbackType(feedbacks.getFeedbackType());
				feedbackList.add(feedback);
			}
			if(feedbackList.size() < ps){
				rep.setIsLast(true);
			}
			rep.setFeedbackNum(feedbackList.size());
			rep.setFeedbackClientList(feedbackList);
			new Thread(){ public void run(){ 
				com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
				cr.put("imei",imei);
				cr.put("lookOver",true);
				tClientFeedbackService.updateLookoverForClient(cr);
			}}.start();
		}
		return rep;
	}
	@GET
	@Path("/collection")
	public RaveResponse collection(@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(30030);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TAppCollection> list = tAppCollectionService.collection(cr);
			List<TAppCollection> collectionlist = new ArrayList<TAppCollection>();
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setCollectionNum(0);
			}else{
				for(TAppCollection res:list){
					TAppCollection collection = new TAppCollection();
					collection.setCollectionId(res.getCollectionId());
					collection.setBigicon(VM.getInatance().getResServer()+res.getBigicon());
					collection.setIcon(VM.getInatance().getResServer()+res.getBigicon());
					collection.setName(res.getName());
					collection.setDescription(res.getDescription());
					collection.setPublishTime(DateUtil.formatDate(res.getCreateTime(),DateUtil.DATE_FORMAT_DAY));
					collectionlist.add(collection);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setCollectionList(collectionlist);
				rep.setCollectionNum(collectionlist.size());
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/collectionlist")
	public RaveAppResponse collectionlist(@QueryParam("collectionId") Integer collectionId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30031);
		try {
			com.mas.market.pojo.Criteria cr = new com.mas.market.pojo.Criteria();
			cr.put("collectionId",collectionId);
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			cr.setOrderByClause( " sort desc,initDowdload+realDowdload desc " );
			List<TAppAlbumRes> list = tAppAlbumResService.collectionlist(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setAppNum(0);
			}else{
				List<TAppAlbumRes> applist = new ArrayList<TAppAlbumRes>();
				for(TAppAlbumRes res:list){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setBrief(res.getBrief());
					app.setAppName(res.getAppName());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					Integer fileType = getFileType(res.getCategoryId());
					app.setFileType(fileType);
					applist.add(app);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setAppNum(list.size());
				rep.setApplist(applist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/musthave")
	public RaveAppResponse musthave(@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30032);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TAppAlbumRes> list = tAppAlbumResService.musthave(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setMustHaveNum(0);
			}else{
				MustHaveData mustHave = null;
				Map<Integer,MustHaveData> musthavemap = new HashMap<Integer,MustHaveData>();
				List<MustHaveData> mustHavelist = new ArrayList<MustHaveData>();
				for(TAppAlbumRes res:list){
					String operator = res.getOperator();
					Integer id = res.getId();
					if(musthavemap.containsKey(id)){
						mustHave = musthavemap.get(id);
					}else{
						mustHave = new MustHaveData();
						mustHave.setName(operator);
						mustHave.setId(id);
						mustHave.setAppNum(0);
						mustHave.setApplist(new ArrayList<TAppAlbumRes>());
						musthavemap.put(id, mustHave);
						mustHavelist.add(mustHave);
					}
					if(null != res.getAppId()){
						TAppAlbumRes app = new TAppAlbumRes();
						app.setApkId(res.getApkId());
						app.setAppId(res.getAppId());
						app.setBrief(res.getBrief());
						app.setAppName(res.getAppName());
						app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
						app.setStars(res.getStars());
						app.setFileSize(res.getFileSize());
						app.setUrl(VM.getInatance().getResServer()+res.getUrl());
						app.setPackageName(res.getPackageName());
						app.setVersionCode(res.getVersionCode());
						app.setVersionName(res.getVersionName());
						app.setCategoryId(res.getCategoryId());
						app.setFileType(getFileType(res.getCategoryId()));
						mustHave.getApplist().add(app);
						mustHave.setAppNum(mustHave.getApplist().size());
					}
				}
				if(mustHavelist.size() < ps){
					rep.setIsLast(true);
				}
				rep.setMustHaveNum(mustHavelist.size());
				rep.setMustHavelist(mustHavelist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@GET
	@Path("/skinAttention")
	public RaveResponse skinAttention(@QueryParam("skinCode") Integer skinCode){
		RaveResponse rep = new RaveResponse();
		rep.setSkinAttention(false);
		rep.setTrxrc(70001);
		TClientSkinCode code = tClientSkinCodeService.selectByPrimaryKey(1);
		if(skinCode.intValue() != code.getSkincode().intValue()){
			rep.setSkinAttention(true);
		}
		rep.setSkinCode(code.getSkincode());
		return rep;
	}
	@GET
	@Path("/skinlist")
	public RaveResponse skinlist(@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(70002);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			cr.put("state", true);
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			cr.setOrderByClause(" sort desc,createTime desc ");
			List<TClientSkin> list = tClientSkinService.selectByExample(cr);
			if(list == null || list.size() == 0){
				rep.setSkinNum(0);
			}else{
				List<TClientSkin> skinlist = new ArrayList<TClientSkin>();
				for(TClientSkin res:list){
					TClientSkin app = new TClientSkin();
					app.setSkinId(res.getSkinId());
					app.setSkinName(res.getSkinName());
					app.setDescription(res.getDescription());
					app.setLogo(VM.getInatance().getResServer()+res.getLogo());
					app.setApkSize(res.getApkSize());
					app.setApkUrl(VM.getInatance().getResServer()+res.getApkUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					skinlist.add(app);
				}
				rep.setSkinNum(skinlist.size());
				rep.setSkinlist(skinlist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	@POST
	@Path("/skinDownload")
	public BaseResponse skinDownload(RaveAppRequest req){
		BaseResponse rep=new BaseResponse();
		rep.setTrxrc(70003);
		try {
			SkinDownloadLog log = new SkinDownloadLog();
			RaveData data = req.getData();
			log.setSkinId(data.getSkinId());
			log.setSkinName(data.getSkinName());
			log.setClinetId(data.getClientId());
			log.setImei(data.getImei());
			log.setPackageName(data.getPackageName());
			log.setVersionCode(data.getVersionCode());
			log.setVersionName(data.getVersionName());
			String ip = AddressUtils.getClientIp(this.getRequest());
			log.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				log.setCountry(address[0]);
				log.setProvince(address[1]);
				log.setCity(address[2]);
			}
			skinDownloadLogService.insertSelective(log);
			TClientSkin tClientSkin = new  TClientSkin();
			tClientSkin.setPackageName(data.getPackageName());
			tClientSkinService.updateSkinDownLoad(tClientSkin);
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@GET
	@Path("/getcolumn")
	public RaveResponse getcolumn(){
		RaveResponse rep = new RaveResponse();
		rep.setTrxrc(00000);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.setOrderByClause(" albumId ");
			List<TAppAlbumColumn> list = tAppAlbumColumnService.selectByExample(cr);
			List<ArrayList<RaveColumn>> columnlist = new ArrayList<ArrayList<RaveColumn>>();
			Map<Integer,ArrayList<RaveColumn>> map = new HashMap<Integer,ArrayList<RaveColumn>>();
			ArrayList<RaveColumn> column;
			for(TAppAlbumColumn columns:list){
				RaveColumn col = new RaveColumn();
				Integer albumId = columns.getAlbumId();
				if(map.containsKey(albumId)){
					column = map.get(albumId);
				}else{
					column = new ArrayList<RaveColumn>();
					map.put(albumId, column);
					columnlist.add(column);
				}
				for(RaveEntrance entrance:RaveEntrance.values()){
					if(entrance.getId()==albumId){
						col.setEntranceName(entrance.getName());
						col.setEntranceId(albumId);
					}
				}
				String url = "rs/rave/list";
				if(albumId==2||albumId==3){
					Integer columnId = columns.getColumnId();
					if(columnId==11 || columnId==21){
						url = "rs/rave/category";
						col.setCt(albumId);
					}else{
						col.setCt(columnId);
					}
				}else if(albumId==4){
					url = "rs/res/music/list";
					Integer columnId = columns.getColumnId();
					if(columnId==31){
						url = "rs/rave/category";
						col.setCt(albumId);
					}else{
						col.setCt(columnId);
					}
				}else if(albumId==5){
					url = "rs/res/image/list";
					Integer columnId = columns.getColumnId();
					if(columnId==41){
						url = "rs/rave/category";
						col.setCt(albumId);
					}else{
						col.setCt(columnId);
					}
				}
				col.setUrl(url);
				col.setBigicon(columns.getBigicon());
				col.setIcon(columns.getBigicon());
				col.setName(columns.getName());
				column.add(col);
			}
			rep.setColumnlist(columnlist);
			rep.setColumnNum(columnlist.size());
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age=3000");
		return rep;
	}
	
	@GET
	@Path("/sameIssuerApplist")
	public RaveAppResponse sameIssuerApplist(@QueryParam("issuer") String issuer,@QueryParam("raveId") Integer raveId,@QueryParam("appId") Integer appId){
		RaveAppResponse rep = new RaveAppResponse();
		rep.setTrxrc(30033);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			if(null!=raveId){
				if(0==raveId){
					raveId = 1;	cr.put("raveId",raveId);
				}else{
					cr.put("raveId",raveId);
				}
			}else{
				raveId = 1;	cr.put("raveId",raveId);
			}
			/*
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			*/
			cr.put("issuer",issuer);
			cr.put("appId",appId);
			List<TAppAlbumRes> list = tAppAlbumResService.searchSameIssuerRecommend(cr);
			if(list == null || list.size() == 0){
				//rep.setAppNum(0);
				rep.setRecommendlist(list);
			}else{
				List<TAppAlbumRes> recommendlist = new ArrayList<TAppAlbumRes>();
				for(TAppAlbumRes res:list){
					TAppAlbumRes app = new TAppAlbumRes();
					app.setApkId(res.getApkId());
					app.setAppId(res.getAppId());
					app.setAppName(res.getAppName());
					app.setBrief(res.getBrief());
					app.setLogo(VM.getInatance().getResServer()+res.getBigLogo());
					app.setStars(res.getStars());
					app.setFileSize(res.getFileSize());
					app.setUrl(VM.getInatance().getResServer()+res.getUrl());
					app.setPackageName(res.getPackageName());
					app.setVersionCode(res.getVersionCode());
					app.setVersionName(res.getVersionName());
					app.setCategoryId(res.getCategoryId());
					Integer fileType = getFileType(res.getCategoryId());
					app.setFileType(fileType);
					app.setIssuer(res.getIssuer());
					if(res.getFree() == 0){
						app.setAppSource("Google");
					}else if(res.getFree() == 2){
						app.setAppSource("MAS");
					}
					recommendlist.add(app);
				}
				/*
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				*/
				rep.setRecommendNum(recommendlist.size());
				rep.setRecommendlist(recommendlist);
			}
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		this.getResponse().addHeader("Cache-Control","max-age="+max_age);
		return rep;
	}
	
	@GET
	@Path("/getPushMessage")
	public PushMessageResponse getPushMessage(@QueryParam("imei") String imei,@QueryParam("versionName") String versionName){
		PushMessageResponse rep = new PushMessageResponse();
		rep.setTrxrc(80002);
		try{
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("versionName",versionName);
			List<PushMessageData> dataList = new ArrayList<PushMessageData>();
			dataList = pushMessageService.queryPushMessage(cr);
			int count = 0;
			if(dataList != null && dataList.size() > 0){
				for(int i = 0; i < dataList.size(); i++){
					PushMessageData data = dataList.get(i);
					if(data.getMode() == 1 && !StringUtils.isEmpty(data.getPicUrl())){
						data.setPicUrl(VM.getInatance().getResServer()+data.getPicUrl());
					}else{
						data.setPicUrl(" ");
					}
					if(!StringUtils.isEmpty(data.getUrl())){
						//data.setUrl(VM.getInatance().getResServer()+data.getUrl());
					}else{
						data.setUrl(" ");
					}
					if(!StringUtils.isEmpty(data.getIcon())){
						data.setIcon(VM.getInatance().getResServer()+data.getIcon());
					}else{
						data.setIcon(" ");
					}
				}
				count = dataList.size();
				rep.setListSize(count);
			}
			rep.setMessageList(dataList);
		}catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		}
		return rep;
	}
	
	private List<Integer> getCatIds(int catId){
		List<Integer> idList = new ArrayList<Integer>();
		TCategory category = tCategoryService.selectByPrimaryKey(catId);
		idList.add(catId);
		if(category.getLevel()==1){
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("fatherId", catId);
			cr.put("levelTwo", "levelTwo");
			List<TCategory> catList = tCategoryService.selectLevelCat(cr);
			if(catList != null && catList.size() > 0){
				for(TCategory cat : catList){
					idList.add(cat.getId());
				}
			}
		}
		return idList;
	}
	
	private Integer getFileType(int catId){
		int fileType = 0;
		TCategory category = tCategoryService.selectByPrimaryKey(catId);
		if(category.getLevel()==1){
			fileType = category.getFatherId()-1;
		}
		if(category.getLevel()==2){
			TCategory fatherCat = tCategoryService.selectByPrimaryKey(category.getFatherId());
			fileType = fatherCat.getFatherId()-1;
		}
		return fileType;
	}
	
	private List<Integer> getAllCatIds(int ct ,Integer raveId){
		com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
		List<Integer> catIds = new ArrayList<Integer>();
		cr.put("fatherId", ct);
		if(null!=raveId){
			if(0==raveId){
				raveId = 1;	cr.put("raveId",raveId);
			}else{
				cr.put("raveId",raveId);
			}
		}else{
			raveId = 1;	cr.put("raveId",raveId);
		}
		cr.put("levelOne", "levelOne");
		//cr.setOrderByClause(" sort desc,createTime desc ");
		List<Integer> fatherIds = tCategoryService.selectAllCatIds(cr);
		if(fatherIds != null && fatherIds.size() > 0){
			catIds.addAll(fatherIds);
			cr.put("levelOne", null);
			cr.put("levelTwo", "levelTwo");
			for(Integer id : fatherIds){
				cr.put("fatherId", id);
				List<Integer> sonIds = tCategoryService.selectAllCatIds(cr);
				if(sonIds != null && sonIds.size() > 0){
					catIds.addAll(sonIds);
				}
			}
		}
		return catIds;
	}
}
