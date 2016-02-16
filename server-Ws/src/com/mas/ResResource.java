package com.mas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.data.BaseResponse;
import com.mas.data.MasData;
import com.mas.data.Page;
import com.mas.data.RaveAppRequest;
import com.mas.data.RaveData;
import com.mas.data.ResResponse;
import com.mas.market.pojo.RaveColumn;
import com.mas.market.pojo.TAppAlbumRes;
import com.mas.market.pojo.TCategory;
import com.mas.market.pojo.TResImage;
import com.mas.market.pojo.TResImageAlbumRes;
import com.mas.market.pojo.TResImageTheme;
import com.mas.market.pojo.TResMusic;
import com.mas.market.pojo.TResMusicAlbumRes;
import com.mas.market.pojo.TResMusicTheme;
import com.mas.market.pojo.TSearchKeyword;
import com.mas.market.service.TCategoryService;
import com.mas.market.service.TResImageAlbumResService;
import com.mas.market.service.TResImageService;
import com.mas.market.service.TResImageThemeService;
import com.mas.market.service.TResMusicAlbumResService;
import com.mas.market.service.TResMusicService;
import com.mas.market.service.TResMusicThemeService;
import com.mas.market.service.TSearchKeywordService;
import com.mas.util.AddressUtils;
import com.mas.util.DateUtil;
import com.mas.util.VM;
import com.mas.ws.pojo.ImageDownloadLog;
import com.mas.ws.pojo.MasUser;
import com.mas.ws.pojo.MusicDownloadLog;
import com.mas.ws.pojo.MusicSearchLog;
import com.mas.ws.service.ImageDownloadLogService;
import com.mas.ws.service.ImageSearchLogService;
import com.mas.ws.service.MusicDownloadLogService;
import com.mas.ws.service.MusicSearchLogService;
/**
 * @author hhk
 */
@Service
@Path(value="/res")
@Produces("application/json")
public class ResResource  extends BaseResoure
{
	private static Integer max_age = 300;
	@Autowired
	private TResImageAlbumResService tResImageAlbumResService;
	@Autowired
	private TResImageThemeService tResImageThemeService;
	@Autowired
	private TResMusicAlbumResService tResMusicAlbumResService;
	@Autowired
	private TResMusicThemeService tResMusicThemeService;
	@Autowired
	private TCategoryService tCategoryService;
	@Autowired
	private TResImageService tResImageService;
	@Autowired
	private TResMusicService tResMusicService;
	@Autowired
	private MusicSearchLogService musicSearchLogService;
	@Autowired
	private ImageSearchLogService imageSearchLogService;
	@Autowired
	private ImageDownloadLogService imageDownloadLogService;
	@Autowired
	private MusicDownloadLogService musicDownloadLogService;
	@Autowired
	private TSearchKeywordService tSearchKeywordService;
	@GET
	@Path("/music/list")
	public ResResponse musiclist(@QueryParam("raveId") Integer raveId,@QueryParam("ct") Integer ct,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(31001);
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
			//------只显示100个排行-----
			int preCount = startIndex + ps;
			if(startIndex <= 100 && preCount > 100){
				int realPs = 100 - startIndex;
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(realPs);
			}else{
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(ps);
			}
			//cr.setMysqlOffset(startIndex);
			//cr.setMysqlLength(ps);
			if(34==ct){
				cr.setOrderByClause( " t1.sort desc,t1.createTime desc " );
			}else{
				cr.setOrderByClause( " t1.sort desc,t1.realDowdload desc " );
			}
			List<TResMusicAlbumRes> list = tResMusicAlbumResService.selectColunmList(cr);
			if(startIndex > 100 || (list == null || list.size() == 0)){
				rep.setIsLast(true);rep.setMusicNum(0);
			}else{
				List<TResMusicAlbumRes> reslist = new ArrayList<TResMusicAlbumRes>();
				for(TResMusicAlbumRes res:list){
					TResMusicAlbumRes upres = new TResMusicAlbumRes();
					upres.setMusicId(res.getMusicId());
					upres.setBrief(res.getBrief());
					upres.setMusicName(res.getMusicName());
					upres.setDuration(res.getDuration());
					if(null!=res.getLogo() && !"".equals(res.getLogo().trim())){
						upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					}else{
						upres.setLogo("");
					}
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					upres.setDownloadNum(getDownloadNum(res.getRealDowdload()));
					upres.setOnlineTime(getOnlineTime(res.getCreateTime()));
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setMusicNum(reslist.size());
				rep.setMusiclist(reslist);
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
	@Path("/image/list")
	public ResResponse imagelist(@QueryParam("raveId") Integer raveId,@QueryParam("ct") Integer ct,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(32001);
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
			//------只显示150个排行-----
			int preCount = startIndex + ps;
			if(startIndex <= 150 && preCount > 150){
				int realPs = 150 - startIndex;
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(realPs);
			}else{
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(ps);
			}
			//cr.setMysqlOffset(startIndex);
			//cr.setMysqlLength(ps);
			/*
			if(44==ct){
				cr.setOrderByClause( " sort desc,createTime desc " );
			}else{
				cr.setOrderByClause( " sort desc,realDowdload asc " );
			}
			*/
			cr.setOrderByClause( " sort desc,realDowdload desc " );
			
			List<TResImageAlbumRes> list = tResImageAlbumResService.selectByExample(cr);
			if(startIndex > 150 || (list == null || list.size() == 0)){
				rep.setIsLast(true);rep.setImageNum(0);
			}else{
				List<TResImageAlbumRes> reslist = new ArrayList<TResImageAlbumRes>();
				for(TResImageAlbumRes res:list){
					TResImageAlbumRes upres = new TResImageAlbumRes();
					upres.setImageId(res.getImageId());
					upres.setBrief(res.getBrief());
					upres.setImageName(res.getImageName());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setBiglogo(VM.getInatance().getResServer()+res.getBiglogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setImageNum(reslist.size());
				rep.setImagelist(reslist);
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
	@Path("/music/theme")
	public ResResponse musictheme(@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(31002);
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
			cr.setOrderByClause(" sort desc,createTime desc ");
			List<TResMusicTheme> list = tResMusicThemeService.selectTheme(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setThemeNum(0);
			}else{
				List<RaveColumn> themelist = new ArrayList<RaveColumn>();
				for(TResMusicTheme res:list){
					RaveColumn theme = new RaveColumn();
					theme.setBigicon(VM.getInatance().getResServer()+res.getBigicon());
					theme.setIcon(VM.getInatance().getResServer()+res.getBigicon());
					theme.setName(res.getNameCn());
					theme.setDescription(res.getDescription());
					theme.setThemeId(res.getThemeId());
					themelist.add(theme);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setThemelist(themelist);
				rep.setThemeNum(themelist.size());
				
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
	@Path("/image/theme")
	public ResResponse imagetheme(@QueryParam("raveId") Integer raveId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(32002);
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
			//------只显示150个排行-----
			int preCount = startIndex + ps;
			if(startIndex <= 150 && preCount > 150){
				int realPs = 150 - startIndex;
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(realPs);
			}else{
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(ps);
			}
			//cr.setMysqlOffset(startIndex);
			//cr.setMysqlLength(ps);
			cr.setOrderByClause(" sort desc,createTime desc ");
			List<TResImageTheme> list = tResImageThemeService.selectTheme(cr);
			if(startIndex > 150 || (list == null || list.size() == 0)){
				rep.setIsLast(true);rep.setThemeNum(0);
			}else{
				List<RaveColumn> themelist = new ArrayList<RaveColumn>();
				for(TResImageTheme res:list){
					RaveColumn theme = new RaveColumn();
					theme.setBigicon(VM.getInatance().getResServer()+res.getBigicon());
					theme.setIcon(VM.getInatance().getResServer()+res.getBigicon());
					theme.setName(res.getName());
					theme.setDescription(res.getDescription());
					theme.setThemeId(res.getThemeId());
					com.mas.market.pojo.Criteria crTemp = new  com.mas.market.pojo.Criteria();
					crTemp.put("themeId", res.getThemeId());
					int count = tResImageAlbumResService.countByExample(crTemp);
					theme.setWallpaperNum(count);
					themelist.add(theme);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setThemelist(themelist);
				rep.setThemeNum(themelist.size());
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
	@Path("/music/themelist")
	public ResResponse musicthemelist(@QueryParam("themeId") Integer themeId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(31003);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("themeId",themeId);
			if(null!=ps && null!=pn){
				int startIndex=getStartIndex(ps,pn);
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(ps);
			}else{
				rep.setIsLast(true);
			}
			cr.setOrderByClause( " sort desc,initDowdload+realDowdload desc " );
			List<TResMusicAlbumRes> list = tResMusicAlbumResService.selectByExample(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setMusicNum(0);
			}else{
				List<TResMusicAlbumRes> reslist = new ArrayList<TResMusicAlbumRes>();
				for(TResMusicAlbumRes res:list){
					TResMusicAlbumRes upres = new TResMusicAlbumRes();
					upres.setMusicId(res.getMusicId());
					upres.setBrief(res.getBrief());
					upres.setMusicName(res.getMusicName());
					upres.setDuration(res.getDuration());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(null!=ps && list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setMusicNum(reslist.size());
				rep.setMusiclist(reslist);
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
	@Path("/image/themelist")
	public ResResponse imagethemelist(@QueryParam("themeId") Integer themeId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(32003);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			//cr.put("raveId",raveId);
			cr.put("themeId",themeId);
			if(null!=ps && null!=pn){
				int startIndex=getStartIndex(ps,pn);
				//------只显示150个排行-----
				int preCount = startIndex + ps;
				if(startIndex <= 150 && preCount > 150){
					int realPs = 150 - startIndex;
					cr.setMysqlOffset(startIndex);
					cr.setMysqlLength(realPs);
				}else{
					cr.setMysqlOffset(startIndex);
					cr.setMysqlLength(ps);
				}
				//cr.setMysqlOffset(startIndex);
				//cr.setMysqlLength(ps);
			}else{
				rep.setIsLast(true);
			}
			//cr.setOrderByClause( " sort desc,initDowdload+realDowdload desc " );
			cr.setOrderByClause( " sort desc,createTime desc " );
			List<TResImageAlbumRes> list = tResImageAlbumResService.selectByExample(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setImageNum(0);
			}else{
				List<TResImageAlbumRes> reslist = new ArrayList<TResImageAlbumRes>();
				for(TResImageAlbumRes res:list){
					TResImageAlbumRes upres = new TResImageAlbumRes();
					upres.setImageId(res.getImageId());
					upres.setBrief(res.getBrief());
					upres.setImageName(res.getImageName());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setBiglogo(VM.getInatance().getResServer()+res.getBiglogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(null!=ps && list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setImageNum(reslist.size());
				rep.setImagelist(reslist);
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
	@Path("/music/categorylist")
	public ResResponse musiccategorylist(@QueryParam("raveId") Integer raveId,@QueryParam("column") String column,@QueryParam("categoryId") Integer categoryId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(31004);
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
			List<Integer> categoryIds = getCatIds(categoryId);//适应二级分类
			cr.put("categoryIds",categoryIds);
			int startIndex=getStartIndex(ps,pn);
			//------只显示100个排行-----
			int preCount = startIndex + ps;
			if(startIndex <= 100 && preCount > 100){
				int realPs = 100 - startIndex;
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(realPs);
			}else{
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(ps);
			}
			//cr.setMysqlOffset(startIndex);
			//cr.setMysqlLength(ps);
			if("hot".equals(column)){
				//cr.setOrderByClause( " initDowdload + realDowdload desc " );
				cr.setOrderByClause( " realDowdload desc " );
			}else if("new".equals(column)){
				cr.setOrderByClause( " createTime desc " );
			}
			List<TResMusic> list = tResMusicService.categorylist(cr);
			if(startIndex > 100 || (list == null || list.size() == 0)){
				rep.setIsLast(true);rep.setMusicNum(0);
			}else{
				List<TResMusicAlbumRes> reslist = new ArrayList<TResMusicAlbumRes>();
				for(TResMusic res:list){
					TResMusicAlbumRes upres = new TResMusicAlbumRes();
					upres.setMusicId(res.getId());
					upres.setBrief(res.getBrief());
					upres.setMusicName(res.getName());
					upres.setDuration(res.getDuration());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					upres.setDownloadNum(getDownloadNum(res.getRealDowdload()));
					upres.setOnlineTime(getOnlineTime(res.getCreateTime()));
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setMusicNum(reslist.size());
				rep.setMusiclist(reslist);
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
	@Path("/image/categorylist")
	public ResResponse imagecategorylist(@QueryParam("raveId") Integer raveId,@QueryParam("column") String column,@QueryParam("categoryId") Integer categoryId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(32004);
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
			List<Integer> categoryIds = getCatIds(categoryId); ////适应二级分类
			cr.put("categoryIds",categoryIds);
			int startIndex=getStartIndex(ps,pn);
			//------只显示150个排行-----
			int preCount = startIndex + ps;
			if(startIndex <= 150 && preCount > 150){
				int realPs = 150 - startIndex;
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(realPs);
			}else{
				cr.setMysqlOffset(startIndex);
				cr.setMysqlLength(ps);
			}
			//cr.setMysqlOffset(startIndex);
			//cr.setMysqlLength(ps);
			if("hot".equals(column)){
				//cr.setOrderByClause( " initDowdload + realDowdload desc " );
				cr.setOrderByClause( " realDowdload desc " );
			}else if("new".equals(column)){
				cr.setOrderByClause( " createTime desc " );
			}
			List<TResImage> list = tResImageService.categorylist(cr);
			if(startIndex > 150 || (list == null || list.size() == 0)){
				rep.setIsLast(true);rep.setImageNum(0);
			}else{
				List<TResImageAlbumRes> reslist = new ArrayList<TResImageAlbumRes>();
				for(TResImage res:list){
					TResImageAlbumRes upres = new TResImageAlbumRes();
					upres.setImageId(res.getId());
					upres.setBrief(res.getBrief());
					upres.setImageName(res.getName());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setBiglogo(VM.getInatance().getResServer()+res.getBiglogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setImageNum(reslist.size());
				rep.setImagelist(reslist);
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
	@Path("/searchMusic")
	public BaseResponse searchMusic(final RaveAppRequest req,@QueryParam("searchId") final Integer searchId){
		ResResponse rep=new ResResponse();
		rep.setTrxrc(31005);
		try {
			final RaveData data=req.getData();
			final String content = StringUtils.lowerCase(data.getContent().trim());
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			Integer raveId = data.getRaveId();
			Page page = req.getPage();
			int ps = page.getPs(),pn = page.getPn();
			
			cr.put("albumId",4);
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
				return this.keywordMusicList(raveId,keyword.getSearchId(), pn, ps);
			}
			
			cr = new  com.mas.market.pojo.Criteria();
			cr.put("raveId",raveId);
			final int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			
			//---------------add by lixin-----------------
			List<TResMusic> listTemp = new ArrayList<TResMusic>();
			String keyTemp = content;
			keyTemp = keyTemp.replaceAll(" ", ",").replaceAll(";", ",").replaceAll("，", ",").replaceAll("；", ",").replaceAll(",,", ",");
			String[] keyArray = keyTemp.split(",");
			if(keyTemp.contains(",") && !ArrayUtils.isEmpty(keyArray)){
				if(keyArray.length > 3){
					for(int i = 0; i < 3; i++){
						String key = "nameLike"+i;
						cr.put(key, keyArray[i]);
					}
				}else{
					for(int i=0; i < keyArray.length; i++){
						String key = "nameLike"+i;
						cr.put(key, keyArray[i]);
					}
				}
				listTemp = tResMusicService.searchMusicByArray(cr);
			}else{
				cr.put("nameLike",content);
				listTemp = tResMusicService.searchMusic(cr);
			}
			final List<TResMusic> list = listTemp;
			//--------------------------------------------
			
			//cr.put("nameLike",content);
			//final List<TResMusic> list = tResMusicService.searchMusic(cr);
			if(null != list && list.size()>0){
				List<TResMusicAlbumRes> reslist = new ArrayList<TResMusicAlbumRes>();
				for(TResMusic res:list){
					TResMusicAlbumRes upres = new TResMusicAlbumRes();
					upres.setMusicId(res.getId());
					upres.setBrief(res.getBrief());
					upres.setMusicName(res.getName());
					upres.setDuration(res.getDuration());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setMusicNum(reslist.size());
				rep.setMusiclist(reslist);
			}else{
				rep.setIsLast(true);rep.setMusicNum(0);
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("columnId",32);cr.put("raveId",raveId);
				cr.setMysqlOffset(0);
				cr.setMysqlLength(6);
				cr.setOrderByClause( " RAND() " );
				List<TResMusicAlbumRes> list2 = tResMusicAlbumResService.selectByExample(cr);
				List<TResMusicAlbumRes> reslist = new ArrayList<TResMusicAlbumRes>();
				for(TResMusicAlbumRes res:list2){
					TResMusicAlbumRes upres = new TResMusicAlbumRes();
					upres.setMusicId(res.getMusicId());
					upres.setBrief(res.getBrief());
					upres.setMusicName(res.getMusicName());
					upres.setDuration(res.getDuration());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				rep.setRecommendMusiclist(reslist);
				rep.setRecommendNum(reslist.size());
			}
			new Thread(){public void run() {
				MasUser user = req.getMasUser();
				if(startIndex==0){
					MusicSearchLog searchLog = new MusicSearchLog();
					searchLog.setClientId(data.getClientId());
					searchLog.setContent(content);
					searchLog.setUserId(user.getUserId());
					searchLog.setUserName(user.getUserName());
					searchLog.setSearchNum(list.size());
					musicSearchLogService.insertSelective(searchLog);
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
	@Path("/searchImage")
	public BaseResponse searchImage(final RaveAppRequest req,@QueryParam("searchId") final Integer searchId){
		ResResponse rep=new ResResponse();
		rep.setTrxrc(32005);
		try {
			final RaveData data=req.getData();
			final String content = StringUtils.lowerCase(data.getContent().trim());
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			Integer raveId = data.getRaveId();
			Page page = req.getPage();
			int ps = page.getPs(),pn = page.getPn();
			
			cr.put("albumId",5);
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
				return this.keywordImageList(raveId,keyword.getSearchId(), pn, ps);
			}
			
			cr = new  com.mas.market.pojo.Criteria();
			cr.put("raveId",raveId);
			final int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			
			//---------------add by lixin-----------------
			List<TResImage> listTemp = new ArrayList<TResImage>();
			String keyTemp = content;
			keyTemp = keyTemp.replaceAll(" ", ",").replaceAll(";", ",").replaceAll("，", ",").replaceAll("；", ",").replaceAll(",,", ",");
			String[] keyArray = keyTemp.split(",");
			if(keyTemp.contains(",") && !ArrayUtils.isEmpty(keyArray)){
				if(keyArray.length > 3){
					for(int i = 0; i < 3; i++){
						String key = "nameLike"+i;
						cr.put(key, keyArray[i]);
					}
				}else{
					for(int i=0; i < keyArray.length; i++){
						String key = "nameLike"+i;
						cr.put(key, keyArray[i]);
					}
				}
				listTemp = tResImageService.searchImageByArray(cr);
			}else{
				cr.put("nameLike",content);
				listTemp = tResImageService.searchImage(cr);
			}
			final List<TResImage> list = listTemp;
			//--------------------------------------------
			
			//cr.put("nameLike",content);
			//final List<TResImage> list = tResImageService.searchImage(cr);
			if(null != list && list.size()>0){
				List<TResImageAlbumRes> reslist = new ArrayList<TResImageAlbumRes>();
				for(TResImage res:list){
					TResImageAlbumRes upres = new TResImageAlbumRes();
					upres.setImageId(res.getId());
					upres.setBrief(res.getBrief());
					upres.setImageName(res.getName());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setBiglogo(VM.getInatance().getResServer()+res.getBiglogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setImageNum(reslist.size());
				rep.setImagelist(reslist);
			}else{
				rep.setIsLast(true);rep.setImageNum(0);
				cr = new  com.mas.market.pojo.Criteria();
				cr.put("columnId",43);cr.put("raveId",raveId);
				cr.setMysqlOffset(0);
				cr.setMysqlLength(12);
				cr.setOrderByClause( " RAND() " );
				List<TResImageAlbumRes> list2 = tResImageAlbumResService.selectByExample(cr);
				List<TResImageAlbumRes> reslist = new ArrayList<TResImageAlbumRes>();
				for(TResImageAlbumRes res:list2){
					TResImageAlbumRes upres = new TResImageAlbumRes();
					upres.setImageId(res.getImageId());
					upres.setBrief(res.getBrief());
					upres.setImageName(res.getImageName());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setBiglogo(VM.getInatance().getResServer()+res.getBiglogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				rep.setRecommendImagelist(reslist);
				rep.setRecommendNum(reslist.size());
			}
			new Thread(){public void run() {
				MasUser user = req.getMasUser();
				if(startIndex==0){
					MusicSearchLog searchLog = new MusicSearchLog();
					searchLog.setClientId(data.getClientId());
					searchLog.setContent(content);
					searchLog.setUserId(user.getUserId());
					searchLog.setUserName(user.getUserName());
					searchLog.setSearchNum(list.size());
					musicSearchLogService.insertSelective(searchLog);
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
	@Path("/musicDownLoad")
	public BaseResponse musicDownLoad(RaveAppRequest req){
		BaseResponse rep=new BaseResponse();
		rep.setTrxrc(31006);
		try {
			MusicDownloadLog log = new MusicDownloadLog();
			MasData mas = req.getMasPlay();
			log.setMasPackageName(mas.getMasPackageName());
			log.setMasVersionName(mas.getMasVersionName());
			log.setMasVersionCode(mas.getMasVersionCode());
			MasUser user = req.getMasUser();
			log.setUserId(user.getUserId());
			log.setUserName(user.getUserName());
			RaveData data = req.getData();
			log.setColumnId(data.getCt());
			log.setRaveId(data.getRaveId());
			log.setClientId(data.getClientId());
			log.setMusicId(data.getMusicId());
			log.setMusicName(data.getMusicName());
			String ip = AddressUtils.getClientIp(this.getRequest());
			log.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				log.setCountry(address[0]);
				log.setProvince(address[1]);
				log.setCity(address[2]);
			}
			musicDownloadLogService.insertSelective(log);
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("id",data.getMusicId());
			tResMusicService.updateDownLoad(cr);
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@POST
	@Path("/imageDownLoad")
	public BaseResponse imageDownLoad(RaveAppRequest req){
		BaseResponse rep=new BaseResponse();
		rep.setTrxrc(32006);
		try {
			ImageDownloadLog log = new ImageDownloadLog();
			MasData mas = req.getMasPlay();
			log.setMasPackageName(mas.getMasPackageName());
			log.setMasVersionName(mas.getMasVersionName());
			log.setMasVersionCode(mas.getMasVersionCode());
			MasUser user = req.getMasUser();
			log.setUserId(user.getUserId());
			log.setUserName(user.getUserName());
			RaveData data = req.getData();
			log.setColumnId(data.getCt());
			log.setRaveId(data.getRaveId());
			log.setClientId(data.getClientId());
			log.setImageId(data.getImageId());
			log.setImageName(data.getImageName());
			String ip = AddressUtils.getClientIp(this.getRequest());
			log.setIP(ip);
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				log.setCountry(address[0]);
				log.setProvince(address[1]);
				log.setCity(address[2]);
			}
			imageDownloadLogService.insertSelective(log);
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("id",data.getImageId());
			tResImageService.updateDownLoad(cr);
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@GET
	@Path("/keywordMusicList")
	public ResResponse keywordMusicList(@QueryParam("raveId") Integer raveId,@QueryParam("searchId") final Integer searchId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(31007);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("searchId",searchId);
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TResMusic> list = tResMusicService.keywordMusicList(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setMusicNum(0);
			}else{
				List<TResMusicAlbumRes> reslist = new ArrayList<TResMusicAlbumRes>();
				for(TResMusic res:list){
					TResMusicAlbumRes upres = new TResMusicAlbumRes();
					upres.setMusicId(res.getId());
					upres.setBrief(res.getBrief());
					upres.setMusicName(res.getName());
					upres.setDuration(res.getDuration());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setMusicNum(reslist.size());
				rep.setMusiclist(reslist);
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
	@Path("/keywordImageList")
	public ResResponse keywordImageList(@QueryParam("raveId") Integer raveId,@QueryParam("searchId") final Integer searchId,@QueryParam("pn") Integer pn,@QueryParam("ps") Integer ps){
		ResResponse rep = new ResResponse();
		rep.setTrxrc(32007);
		try {
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
			cr.put("searchId",searchId);
			int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			List<TResImage> list = tResImageService.keywordImageList(cr);
			if(list == null || list.size() == 0){
				rep.setIsLast(true);rep.setImageNum(0);
			}else{
				List<TResImageAlbumRes> reslist = new ArrayList<TResImageAlbumRes>();
				for(TResImage res:list){
					TResImageAlbumRes upres = new TResImageAlbumRes();
					upres.setImageId(res.getId());
					upres.setBrief(res.getBrief());
					upres.setImageName(res.getName());
					upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
					upres.setBiglogo(VM.getInatance().getResServer()+res.getBiglogo());
					upres.setFileSize(res.getFileSize());
					upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
					upres.setCategoryId(res.getCategoryId());
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setImageNum(reslist.size());
				rep.setImagelist(reslist);
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
	@Path("/searchMusicTip")
	public BaseResponse searchMusicTip(final RaveAppRequest req){
		ResResponse rep=new ResResponse();
		rep.setTrxrc(31008);
		try {
			final RaveData data=req.getData();
			final String content = StringUtils.lowerCase(data.getContent().trim());
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
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
			cr.put("nameLike",content);
			Page page = req.getPage();
			int ps = page.getPs(),pn = page.getPn();
			final int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			final List<TResMusic> list = tResMusicService.searchMusicTip(cr);
			if(null != list && list.size()>0){
				List<TResMusicAlbumRes> reslist = new ArrayList<TResMusicAlbumRes>();
				for(int i=0;i<list.size();i++){
					TResMusic res = list.get(i);
					TResMusicAlbumRes upres = new TResMusicAlbumRes();
					if(i==0){
						upres.setMusicId(res.getId());
						upres.setBrief(res.getBrief());
						upres.setMusicName(res.getName());
						upres.setDuration(res.getDuration());
						upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
						upres.setFileSize(res.getFileSize());
						upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
						upres.setCategoryId(res.getCategoryId());
					}else{
						upres.setMusicName(res.getName());
					}
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setMusicNum(reslist.size());
				rep.setMusiclist(reslist);
			}else{
				rep.setIsLast(true);rep.setMusicNum(0);
			}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	@POST
	@Path("/searchImageTip")
	public BaseResponse searchImageTip(final RaveAppRequest req){
		ResResponse rep=new ResResponse();
		rep.setTrxrc(32008);
		try {
			final RaveData data=req.getData();
			final String content = StringUtils.lowerCase(data.getContent().trim());
			com.mas.market.pojo.Criteria cr = new  com.mas.market.pojo.Criteria();
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
			cr.put("nameLike",content);
			Page page = req.getPage();
			int ps = page.getPs(),pn = page.getPn();
			final int startIndex=getStartIndex(ps,pn);
			cr.setMysqlOffset(startIndex);
			cr.setMysqlLength(ps);
			final List<TResImage> list = tResImageService.searchImageTip(cr);
			if(null != list && list.size()>0){
				List<TResImageAlbumRes> reslist = new ArrayList<TResImageAlbumRes>();
				for(int i=0;i<list.size();i++){
					TResImage res = list.get(i);
					TResImageAlbumRes upres = new TResImageAlbumRes();
					if(i==0){
						upres.setImageId(res.getId());
						upres.setBrief(res.getBrief());
						upres.setImageName(res.getName());
						upres.setLogo(VM.getInatance().getResServer()+res.getLogo());
						upres.setBiglogo(VM.getInatance().getResServer()+res.getBiglogo());
						upres.setFileSize(res.getFileSize());
						upres.setUrl(VM.getInatance().getResServer()+res.getUrl());
						upres.setCategoryId(res.getCategoryId());
					}else{
						upres.setImageName(res.getName());
					}
					reslist.add(upres);
				}
				if(list.size() < ps){
					rep.setIsLast(true);
				}
				rep.setImageNum(reslist.size());
				rep.setImagelist(reslist);
			}else{
				rep.setIsLast(true);rep.setImageNum(0);
			}
		} catch (Exception e) {
			rep.getState().setCode(500);
			rep.getState().setMsg(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return rep;
	}
	
	private String getOnlineTime(Date date){
		//Date date = tMusic.getCreateTime();
		String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
		dateStr = dateStr.substring(5);
		return dateStr;
	}
	
	private String getDownloadNum(Integer realDownloadInt){
		String downloadNum = "100+";
		if(realDownloadInt != null){
			//int realDownloadInt = tMusic.getRealDowdload().intValue();
			if(realDownloadInt <= 10){
				downloadNum = "100+";
				return downloadNum;
			}
			if(realDownloadInt > 10 && realDownloadInt <=20){
				downloadNum = "500+";
				return downloadNum;
			}
			if(realDownloadInt > 20 && realDownloadInt <= 40){
				downloadNum = "1000+";
				return downloadNum;
			}
			if(realDownloadInt > 40 && realDownloadInt <= 60){
				downloadNum = "5000+";
				return downloadNum;
			}
			if(realDownloadInt > 60 && realDownloadInt <= 100){
				downloadNum = "10000+";
				return downloadNum;
			}
			if(realDownloadInt > 100 && realDownloadInt <= 500){
				downloadNum = "50000+";
				return downloadNum;
			}
			if(realDownloadInt > 500 && realDownloadInt <= 1000){
				downloadNum = "100000+";
				return downloadNum;
			}
			if(realDownloadInt > 1000){
				downloadNum = "200000+";
				return downloadNum;
			}
		}
		return downloadNum;
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
}
