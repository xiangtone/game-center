
package com.mas.rave.controller;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.*;
import com.mas.rave.service.*;
import com.mas.rave.util.Constant;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.StringUtil;
import com.mas.rave.util.s3.S3Util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/search")
public class SearchKeywordController{
	Logger log = Logger.getLogger(SearchKeywordController.class);
	@Autowired
	private SearchKeywordService searchKeywordService;
	@Autowired
	private SearchKeywordResListService searchKeywordResListService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private AppAlbumService appAlbumService;
	@Autowired
	private AppFileService appFileService;
	@Autowired
	private AppInfoService appInfoService;
	@Autowired
	private MusicInfoService musicInfoService;
	@Autowired
	private ImageInfoService imageInfoService;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private SearchKeywordIconService searchKeywordIconService;
	@RequestMapping("/list")
    public String list(HttpServletRequest request){
        try{
            SearchKeyword record = new SearchKeyword();
            String raveId = request.getParameter("raveId");
            String albumId = request.getParameter("albumId");
            if(StringUtils.isNotEmpty(raveId) && !raveId.equals("0"))
            {
                int raveId0 = Integer.parseInt(raveId);
                Country country = countryService.getCountry(raveId0);
                record.setCountry(country);
            }
            if(StringUtils.isNotEmpty(albumId) && !albumId.equals("0"))
            {
                int albumId0 = Integer.parseInt(albumId);
                AppAlbum appAlbum = appAlbumService.getAppAlbum(albumId0);
                record.setAppAlbum(appAlbum);
            }else{
            	 int albumId0 = 2;
                 AppAlbum appAlbum = appAlbumService.getAppAlbum(albumId0);
                 record.setAppAlbum(appAlbum);
            }
            int currentPage = RequestUtils.getInt(request, "currentPage", 1);
            int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
            PaginationVo<SearchKeyword>  result = searchKeywordService.selectByExample(record, currentPage, pageSize);
            request.setAttribute("result", result);
            List<Country> countrys = countryService.getCountrys();
            request.setAttribute("countrys", countrys);
            List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
            List<AppAlbum> appAlbums0 = new ArrayList<AppAlbum>();
            AppAlbum appAlbum1 = null;
            for(AppAlbum appAlbum:appAlbums){
                if(appAlbum1 == null && (appAlbum.getId() == 1 || appAlbum.getId() == 2 || appAlbum.getId() == 3))
                {
                    appAlbum1 = new AppAlbum();
                    appAlbum1.setId(2);
                    appAlbum1.setName("App&Game");
                    appAlbums0.add(appAlbum1);
                } else
                if(appAlbum.getId() == 4 || appAlbum.getId() == 5)
                    appAlbums0.add(appAlbum);
            }

            request.setAttribute("appAlbums", appAlbums0);
        }catch(Exception e){
            log.error("SearchKeywordController list", e);
            PaginationVo<SearchKeyword>  result = new PaginationVo<SearchKeyword> (null, 0, 10, 1);
            request.setAttribute("result", result);
        }
        return "search/list";
    }
	@RequestMapping("/listRes/{id}")
    public String listRes(HttpServletRequest request,  @PathVariable("id") Integer searchId){
        try{
        	
            SearchKeywordResList record = new SearchKeywordResList();
            record.setSearchId(searchId);
            int currentPage = RequestUtils.getInt(request, "currentPage", 1);
            int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
            PaginationVo<SearchKeywordResList> result = searchKeywordResListService.selectByExample(record, currentPage, pageSize);
            request.setAttribute("result", result);
            request.setAttribute("searchId", searchId);
            SearchKeyword  searchKeyword = searchKeywordService.selectByPrimaryKey(searchId);
            request.setAttribute("searchKeyword", searchKeyword);
        }catch(Exception e){
            log.error("SearchKeywordController list", e);
            PaginationVo<SearchKeywordResList> result = new PaginationVo<SearchKeywordResList>(null, 0, 10, 1);
            request.setAttribute("result", result);
        }
        return "search/listRes";
    }
	/**关键字是否已经存在*/
	@ResponseBody
	@RequestMapping(value = "/judgeKeywordExist", method = RequestMethod.POST)
	public boolean judgeKeywordExist(SearchKeyword criteria){
		List<SearchKeyword> list = 	searchKeywordService.selectByKeyword(criteria.getKeyword());
		if(criteria.getSearchId()==0){
			if(list.size()!=0){
				return false;//关键字已经存在
			}
		}else{
			if(list.size()>1||(list.size()==1&&list.get(0).getSearchId()!=criteria.getSearchId())){
				return false;//关键字已经存在
			}
		}
		return true;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(HttpServletRequest request, SearchKeyword searchKeyword, @RequestParam(value = "menus1", required = false) List<Integer> ids,
    		@RequestParam(required=false) MultipartFile resLogoFile)
    {
		try{
			List<String> fileUrlList = new ArrayList<String>();//s3 list
			// 上传url
			if (searchKeyword.getFlag() == 1 && resLogoFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(resLogoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_SEARCH_PATH+Constant.LOCAL_APK_LOG, 
							RandNum.randomFileName(resLogoFile.getOriginalFilename()));
					FileUtil.copyInputStream(resLogoFile.getInputStream(), apkLocalFile);// apk本地上传					
					FileUtil.deleteFile(searchKeyword.getResLogo());
					searchKeyword.setResLogo(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(searchKeyword.getResLogo());
				} else {
					return "{\"flag\":\"3\"}";
				}
			}
			List<SearchKeyword> list = 	searchKeywordService.selectByKeyword(searchKeyword.getKeyword());
			if(searchKeyword.getSearchId()==0){
				if(list.size()!=0){
					 return "{\"flag\":\"2\"}";//关键字已经存在
				}
			}else{
				if(list.size()>1||(list.size()==1&&list.get(0).getSearchId()!=searchKeyword.getSearchId())){
					 return "{\"flag\":\"2\"}";//关键字已经存在
				}
			}
			SearchKeywordIcon searchKeywordIcon = searchKeywordIconService.getSearchKeywordIcon(searchKeyword.getIconId());
			if(searchKeywordIcon!=null){
				searchKeyword.setIconUrl(searchKeywordIcon.getUrl());		
			}
	            Country country = countryService.getCountry(searchKeyword.getRaveId());
	            searchKeyword.setCountry(country);
	            HttpSession s = (HttpSession)RequestContextHolder.currentRequestAttributes().resolveReference("session");
	            User user = (User)s.getAttribute("loginUser");
	            if(user != null){
	                searchKeyword.setOperator(user.getName());
	            }
	            if(searchKeyword.getFlag() ==0){
   	                searchKeywordService.insert(searchKeyword);
		            return "{\"flag\":\"0\"}";
	            }
	            if(ids!=null&&ids.size()>0){
	            	   if(searchKeyword.getFlag() == 1){
	   	                searchKeyword.setResId(ids.get(0));
	   	                searchKeywordService.insert(searchKeyword);
	   	                S3Util.uploadS3(fileUrlList, false);
	   	            } else if(searchKeyword.getFlag() == 2){
	   	                searchKeywordService.insert(searchKeyword);
	   	                S3Util.uploadS3(fileUrlList, false);
	   	                for(Integer id:ids) {
	   	                	SearchKeywordResList  searchKeywordReslist = new SearchKeywordResList();
	   	                    searchKeywordReslist.setSearchId(searchKeyword.getSearchId());
	   	                    searchKeywordReslist.setResId(id);
	   	                    if(user != null){
	   	                        searchKeywordReslist.setOperator(user.getName());	
	   	                    }
	   	                  	 searchKeywordResListService.insert(searchKeywordReslist);
	   	                }

	   	            }
	   	            return "{\"flag\":\"0\"}";

	            }
	         
	 
	            return "{\"flag\":\"1\"}";
	       
		}catch(Exception e){
			try {
				FileUtil.deleteFile(searchKeyword.getResLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error("SearchKeywordController update deleteFile", e1);
			}
              log.error("SearchKeywordController add", e);	
		      return "{\"flag\":\"1\"}";
		}

    }
	@RequestMapping("/showAdd")
    public String showAdd(HttpServletRequest request,
    		@RequestParam(value = "menus1", required = false) List<Integer> ids1,
    		@RequestParam(value = "menus", required = false) List<Integer> ids){
		//合并ids和ids1
        String flags = request.getParameter("flag");
        int flag = 0;

        if(StringUtils.isNotEmpty(flags) && !flags.equals("0")){
      	  try{
                flag = Integer.parseInt(flags);
            }
            catch(Exception e){
                flag = 0;
            }
        }
        if(flag==2){
    		if(ids1!=null&&ids!=null){
    			ids.addAll(ids1);
    		}else if(ids1!=null&&ids==null){
    			ids = ids1;
    		}
        }else if(flag==1){
        	if(ids1!=null&&ids==null){
    			ids = ids1;
        	}
        }
		Set<Integer> set = new HashSet<Integer>();
		if(ids!=null){
			set.addAll(ids);
		}
		List<Integer> list = new ArrayList<Integer>(set);
		if(list!=null&&list.size()>0){
			ids = list;
		}
        List<Country> countrys = countryService.getCountrys();
        request.setAttribute("countrys", countrys);
        List<SearchKeywordIcon> searchIcons = searchKeywordIconService.getSearchKeywordIcons();
        request.setAttribute("searchIcons", searchIcons);
        List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
        List<AppAlbum> appAlbums0 = new ArrayList<AppAlbum>();
        AppAlbum appAlbum1 = null;
        for(AppAlbum appAlbum:appAlbums){
            if(appAlbum1 == null && (appAlbum.getId() == 1 || appAlbum.getId() == 2 || appAlbum.getId() == 3))
            {
                appAlbum1 = new AppAlbum();
                appAlbum1.setId(2);
                appAlbum1.setName("App&Game");
                appAlbums0.add(appAlbum1);
            } else
            if(appAlbum.getId() == 4 || appAlbum.getId() == 5)
                appAlbums0.add(appAlbum);
        }

        String albumIds = request.getParameter("albumId");
        String raveIds = request.getParameter("raveId");
        String keywords = request.getParameter("keyword");
        String sorts = request.getParameter("sort");
        String iconIds = request.getParameter("iconId");
        SearchKeyword searchKeyword1 = new SearchKeyword();
        int albumId = 0;
        int raveId = 0;
        int sort = 0;
        if(StringUtils.isNotEmpty(albumIds) && !albumIds.equals("0")){
            albumId = Integer.parseInt(albumIds);
        }
        else{
            albumId = 2;
        }
        if(StringUtils.isNotEmpty(raveIds) && !raveIds.equals("0")){
            raveId = Integer.parseInt(raveIds);
        }
        else{
            raveId = 1;
        }
        if(StringUtils.isNotEmpty(sorts)){
            sort = Integer.parseInt(sorts);

        }
        if(StringUtils.isNotEmpty(iconIds) && !iconIds.equals("0")){
        	Integer iconId = Integer.parseInt(iconIds);	
        	searchKeyword1.setIconId(iconId);
        }
        searchKeyword1.setAlbumId(albumId);
        searchKeyword1.setFlag(flag);
        searchKeyword1.setRaveId(raveId);
        searchKeyword1.setSort(sort);
        List data = getResData(ids, searchKeyword1);
        if(flag==1){
        	if(data!=null&&data.size()==1){
        		try{
                    searchKeyword1.setKeyword(((AppInfo)data.get(0)).getName());
        		}catch(Exception e){
        			try{
                        searchKeyword1.setKeyword(((MusicInfo)data.get(0)).getName());
            		}catch(Exception e1){
            			try{
                            searchKeyword1.setKeyword(((ImageInfo)data.get(0)).getName());
                		}catch(Exception e2){
                			if(keywords!=null&&!keywords.equals("")){
                   			 searchKeyword1.setKeyword(keywords);
                			}
                		}
            		}
        		}
        	}
        }else{
        	if(keywords!=null&&!keywords.equals("")){
      			 searchKeyword1.setKeyword(keywords);
   			}
        }  
        
        request.setAttribute("data", data);
        request.setAttribute("searchKeyword", searchKeyword1);
        request.setAttribute("appAlbums", appAlbums0);
        return "search/add";
    }
	@RequestMapping("/addSelectRes")
    public String addSelectRes(HttpServletRequest request, 
    		@RequestParam(value = "menus1", required = false) List<Integer> ids)
    {
        try
        {
            String flags = request.getParameter("flag");
            String albumIds = request.getParameter("albumId");
            String raveIds = request.getParameter("raveId");
            String keywords = request.getParameter("keyword");
            String sorts = request.getParameter("sort");
            String appnameid = request.getParameter("appnameid");
            String iconIds = request.getParameter("iconId");
            String categoryId = request.getParameter("categoryId") != null ? request.getParameter("categoryId") : "0";
            String category_parent = request.getParameter("category_parent") != null ? request.getParameter("category_parent") : "0";
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("category_parent", category_parent);
            int currentPage = RequestUtils.getInt(request, "currentPage", 1);
            int pageSize = RequestUtils.getInt(request, "pageSize", 48);
            HashMap<String,Object> map = new HashMap<String,Object>();
            MusicInfo criteria = new MusicInfo();
            ImageInfo criteria1 = new ImageInfo();
            SearchKeyword searchKeyword = new SearchKeyword();

            if(appnameid != null && !"".equals(appnameid))
                try
                {
					int appid = Integer.parseInt(appnameid.trim());
                    map.put("appid", appnameid.trim());
                    criteria.setId(appid);
                    criteria1.setId(appid);

                }
                catch(Exception e)
                {
                    map.put("appname", appnameid.trim());
                    criteria.setName(appnameid.trim());
                    criteria1.setName(appnameid.trim());

                }
            request.setAttribute("appnameid", appnameid);
            if(!"0".equals(categoryId)){
            	int categoryId0 = Integer.parseInt(categoryId);
            	 Category cat = categoryService.getCategory(categoryId0);
                 map.put("categoryId",categoryId0);
                 criteria.setCategory(cat);
                 criteria1.setCategory(cat);
            }
            else if(!"0".equals(category_parent)){
            	int category_parent0 = Integer.parseInt(category_parent);
                map.put("category_parent",category_parent0);            
            }
            criteria.setCategory_parent(4);
            criteria1.setCategory_parent(5);
            int flag = 0;
            int albumId = 0;
            int raveId = 0;
            int sort = 0;
            Integer iconId = null;
            if(StringUtils.isNotEmpty(flags) && !flags.equals("0"))
                try
                {
                    flag = Integer.parseInt(flags);
                }
                catch(Exception e)
                {
                    flag = 0;
                }
            if(StringUtils.isNotEmpty(albumIds) && !albumIds.equals("0")){
                albumId = Integer.parseInt(albumIds);
            }
            else{
                albumId = 2;
            }
            if(StringUtils.isNotEmpty(raveIds) && !raveIds.equals("0")){
                raveId = Integer.parseInt(raveIds);
            }
            else{
                raveId = 1;
            }
            if(StringUtils.isNotEmpty(iconIds) && !iconIds.equals("0")){
            	iconId = Integer.parseInt(iconIds);	
                searchKeyword.setIconId(iconId);
            }
            map.put("raveId", raveId);
            if(StringUtils.isNotEmpty(sorts)){
                sort = Integer.parseInt(sorts);
            }         
            searchKeyword.setAlbumId(albumId);
            searchKeyword.setFlag(flag);
            searchKeyword.setKeyword(keywords);
            searchKeyword.setRaveId(raveId);
            searchKeyword.setSort(sort);           
            if(ids!=null&&ids.size()>0){
                request.setAttribute("menus1", StringUtil.listInteger2String(ids));
            }
            request.setAttribute("searchKeyword", searchKeyword);
            Country country = countryService.getCountry(raveId);
            request.setAttribute("country", country);
            if(albumId == 2)
            {
            	if(ids!=null&&ids.size()>0){
            		map.put("ids", ids);           		
            	}
                PaginationVo<AppFile> result = searchKeywordService.getSelectAppFiles(map, currentPage, pageSize);
                request.setAttribute("result", result);
                int size = result.getData().size();
                request.setAttribute("counts", Integer.valueOf(size % 4 != 0 ? size / 4 + 1 : size / 4));
            } else if(albumId == 4){
                criteria.setState(true);
                criteria.setRaveId(Integer.valueOf(raveId));
                criteria.setSearchkey(1);
                if(ids!=null&&ids.size()>0){
                	criteria.setIds(ids);           		
            	}
                PaginationVo<MusicInfo> result = musicInfoService.searchMusics(criteria, currentPage, pageSize);
                request.setAttribute("result", result);
                int size = result.getData().size();
                request.setAttribute("counts", Integer.valueOf(size % 4 != 0 ? size / 4 + 1 : size / 4));
            } else if(albumId == 5){
                criteria1.setState(true);
                criteria1.setRaveId(Integer.valueOf(raveId));
                criteria1.setSearchkey(1);
                if(ids!=null&&ids.size()>0){
                	criteria1.setIds(ids);
            	}
                PaginationVo<ImageInfo> result = imageInfoService.searchImages(criteria1, currentPage, pageSize);
                request.setAttribute("result", result);
                int size = result.getData().size();
                request.setAttribute("counts", Integer.valueOf(size % 4 != 0 ? size / 4 + 1 : size / 4));
            }
        }catch(Exception e){
            log.error("SearchKeywordController info", e);
            PaginationVo<AppFile> result = new PaginationVo<AppFile>(null, 0, 10, 1);
			request.setAttribute("result", result);
        }
        return "search/addselectRes";
    }
	
	/** 删除searchKeyword */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		try{
			Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
			searchKeywordService.batchDelete(idIntArray);
			return "{\"success\":\"true\"}";	
		}catch(Exception e){
			return "{\"success\":\"false\"}";
		}

	}
	/**
	 * 
	 * @param request
	 * @param searchKeyword
	 * @param ids1 menus1表示已经入库的
	 * @param ids menus0 表示新增的
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String update(HttpServletRequest request, SearchKeyword searchKeyword, 
    		@RequestParam(value = "menus1", required = false) List<Integer> ids1,
    		@RequestParam(value = "menus0", required = false)List<Integer> ids,
    		@RequestParam(required=false) MultipartFile resLogoFile){
		List<String> fileUrlList = new ArrayList<String>();//s3 list
		List<SearchKeyword> list1 = 	searchKeywordService.selectByKeyword(searchKeyword.getKeyword());
		if(searchKeyword.getSearchId()==0){
			if(list1.size()!=0){
				 return "{\"flag\":\"2\"}";//关键字已经存在
			}
		}else{
			if(list1.size()>1||(list1.size()==1&&list1.get(0).getSearchId()!=searchKeyword.getSearchId())){
				 return "{\"flag\":\"2\"}";//关键字已经存在
			}
		}

		SearchKeyword searchKeyword1 = searchKeywordService.selectByPrimaryKey(searchKeyword.getSearchId());
		searchKeyword.setSearchNum(searchKeyword1.getSearchNum());
		
		SearchKeywordIcon searchKeywordIcon = searchKeywordIconService.getSearchKeywordIcon(searchKeyword.getIconId());
		if(searchKeywordIcon!=null){
			searchKeyword.setIconUrl(searchKeywordIcon.getUrl());
		}		
		Country country = countryService.getCountry(searchKeyword.getRaveId());
        searchKeyword.setCountry(country);
        HttpSession s = (HttpSession)RequestContextHolder.currentRequestAttributes().resolveReference("session");
        User user = (User)s.getAttribute("loginUser");
        if(user != null){
        	searchKeyword.setOperator(user.getName());	            	
        }
		try{
			// 上传url
			if (searchKeyword.getFlag() == 1 && resLogoFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(resLogoFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					File apkLocalFile = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_SEARCH_PATH+Constant.LOCAL_APK_LOG, 
							RandNum.randomFileName(resLogoFile.getOriginalFilename()));
					FileUtil.copyInputStream(resLogoFile.getInputStream(), apkLocalFile);// apk本地上传					
					FileUtil.deleteFile(searchKeyword.getResLogo());
					searchKeyword.setResLogo(FileAddresUtil.getFilePath(apkLocalFile));
					fileUrlList.add(searchKeyword.getResLogo());
				} else {
					return "{\"flag\":\"3\"}";
				}
			}
			if(searchKeyword.getFlag() == 0){
				searchKeywordService.updateByPrimaryKey(searchKeyword);
           		searchKeywordResListService.deleteBySearchId(searchKeyword.getSearchId());
           		S3Util.uploadS3(fileUrlList, false);
	            return "{\"flag\":\"0\"}";
			}
	    	
	    	//合并ids和ids1 并且去重
			if(ids1!=null&&ids!=null){
				ids.addAll(ids1);
			}else if(ids1!=null&&ids==null){
				ids = ids1;
			}
			Set<Integer> set = new HashSet<Integer>();
			if(ids!=null){
				set.addAll(ids);
			}
			List<Integer> list = new ArrayList<Integer>(set);
			if(list!=null&&list.size()>0){
				ids = list;
			}
	        if(ids != null&&ids.size()>0){
	            if(searchKeyword.getFlag() == 1){
	                searchKeyword.setResId(((Integer)ids.get(0)));
	                searchKeywordService.updateByPrimaryKey(searchKeyword);
	                S3Util.uploadS3(fileUrlList, false);
            		searchKeywordResListService.deleteBySearchId(searchKeyword.getSearchId());           		
	            } else if(searchKeyword.getFlag() == 2){
	                searchKeywordService.updateByPrimaryKey(searchKeyword);
	                S3Util.uploadS3(fileUrlList, false);
	                List<SearchKeywordResList> searchKeywordReslists = searchKeywordResListService.selectBySearchId(searchKeyword.getSearchId());
	                for(SearchKeywordResList searchKeywordReslist:searchKeywordReslists){
	                	int index = ids.indexOf(searchKeywordReslist.getResId());
	                	//表示数据库中存在的资源 修改的时候已经没了
	                	if(index==-1){
	                		searchKeywordResListService.deleteByPrimaryKey(searchKeywordReslist.getId());
	                	}else{
	                	 //表示数据库中存在的资源	现在还存在，把这个资源从ids中删除
	                		ids.remove(index);
	                	}
	                }
	                //最后把 ids中还剩下的资源插入到数据库中
	                if(ids != null&&ids.size()>0){
	                	for(Integer id:ids){
	                		SearchKeywordResList searchKeywordReslist = new SearchKeywordResList();
	                		 searchKeywordReslist.setSearchId(searchKeyword.getSearchId());
	                         searchKeywordReslist.setResId(id);
	                         if(user != null){
	                        	 searchKeywordReslist.setOperator(user.getName());
	                         }
	                         searchKeywordResListService.insert(searchKeywordReslist);
	                	}
	                }
	            }
	            return "{\"flag\":\"0\"}";
	        }
		}catch(Exception e){
			try {
				FileUtil.deleteFile(searchKeyword.getResLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error("SearchKeywordController update deleteFile", e1);
			}
            log.error("SearchKeywordController update", e);	
	        return "{\"flag\":\"1\"}";  
		}
		  return "{\"flag\":\"1\"}";  
    }
	/**
	 * 
	 * @param request
	 * @param searchId
	 * @param ids1 menus1 已经入库的
	 * @param ids0 menus0 表示已经加到列表中的
	 * @param ids menus 表示正要加到列表中的
	 * @return
	 */
	@RequestMapping("/show{id}")
    public String showEdit(HttpServletRequest request,  @PathVariable("id") Integer searchId, 
    		@RequestParam(value = "menus1", required = false) List<Integer> ids1,
    		@RequestParam(value = "menus0", required = false)List<Integer> ids0,
    		@RequestParam(value = "menus", required = false)List<Integer> ids){
		
		
		//合并ids和ids0

        SearchKeyword searchKeyword1 = searchKeywordService.selectByPrimaryKey(searchId);
        int albumIdoriginal = searchKeyword1.getAlbumId();
        String flags = request.getParameter("flag");
        String albumIds = request.getParameter("albumId");
        String raveIds = request.getParameter("raveId");
        String sorts = request.getParameter("sort");
        String keywords = request.getParameter("keyword");
        String iconIds = request.getParameter("iconId");
        String currentPage = request.getParameter("currentPage");
        request.setAttribute("albumId", albumIds);
        request.setAttribute("raveId", raveIds);
        request.setAttribute("currentPage", currentPage);
        if(StringUtils.isNotEmpty(flags) && !flags.equals("0")){
        	  try
              {
                  int flag = Integer.parseInt(flags);
                  searchKeyword1.setFlag(flag);
              }
              catch(Exception e)
              {
                  searchKeyword1.setFlag(1);
              }
        }         
        if(StringUtils.isNotEmpty(albumIds) && !albumIds.equals("0"))
        {
            int albumId = Integer.parseInt(albumIds);
            searchKeyword1.setAlbumId(albumId);
        }
        if(StringUtils.isNotEmpty(raveIds) && !raveIds.equals("0"))
        {
            int raveId = Integer.parseInt(raveIds);
            searchKeyword1.setRaveId(raveId);
            Country country = countryService.getCountry(raveId);
            searchKeyword1.setCountry(country);
        }
        if(StringUtils.isNotEmpty(sorts))
        {
            int sort = Integer.parseInt(sorts);
            searchKeyword1.setSort(sort);
        }
        if(StringUtils.isNotEmpty(iconIds) && !iconIds.equals("0")){
        	Integer iconId = Integer.parseInt(iconIds);	
        	searchKeyword1.setIconId(iconId);
        }
        if(searchKeyword1.getFlag()==2){
    		if(ids0!=null&&ids!=null){
    			ids.addAll(ids0);
    		}else if(ids0!=null&&ids==null){
    			ids = ids1;
    		}
        }else if(searchKeyword1.getFlag()==1){
        	if(ids0!=null&&ids==null){
    			ids = ids0;
        	}
        }		
		Set<Integer> set = new HashSet<Integer>();
		if(ids!=null){
			set.addAll(ids);
		}
		List<Integer> list = new ArrayList<Integer>(set);
		
		if(list!=null&&list.size()>0){
			ids = list;
		}
        List<Country> countrys = countryService.getCountrys();
        request.setAttribute("countrys", countrys);
        List<SearchKeywordIcon> searchIcons = searchKeywordIconService.getSearchKeywordIcons();
        request.setAttribute("searchIcons", searchIcons);
        List<AppAlbum> appAlbums = appAlbumService.getAppAlbum();
        List<AppAlbum> appAlbums0 = new ArrayList<AppAlbum>();
        AppAlbum appAlbum1 = null;
        for(AppAlbum appAlbum:appAlbums){
            if(appAlbum1 == null && (appAlbum.getId() == 1 || appAlbum.getId() == 2 || appAlbum.getId() == 3))
            {
                appAlbum1 = new AppAlbum();
                appAlbum1.setId(2);
                appAlbum1.setName("App&Game");
                appAlbums0.add(appAlbum1);
            } else
            if(appAlbum.getId() == 4 || appAlbum.getId() == 5)
                appAlbums0.add(appAlbum);
        }

        List data1 = getResData1(albumIdoriginal,ids1, searchKeyword1);
        List data = getResData(ids, searchKeyword1);      
        if(searchKeyword1.getFlag() == 1){
            if(StringUtils.isNotEmpty(flags) && !flags.equals("0")){
                if(data != null){
                	request.setAttribute("data1", data); 
                    if(keywords!=null&&!keywords.equals("")){
                        searchKeyword1.setKeyword(keywords);
                    }
                	 if(data!=null&&data.size()==1){
                 		try{
                             searchKeyword1.setKeyword(((AppInfo)data.get(0)).getName());
                 		}catch(Exception e){
                 			try{
                                 searchKeyword1.setKeyword(((MusicInfo)data.get(0)).getName());
                     		}catch(Exception e1){
                     			try{
                                     searchKeyword1.setKeyword(((ImageInfo)data.get(0)).getName());
                         		}catch(Exception e2){
                         			 searchKeyword1.setKeyword(keywords);
                         		}
                     		}
                 		}
                 	}
                }else{
                    request.setAttribute("data1", data1);  
                    if(keywords!=null&&!keywords.equals("")){
                        searchKeyword1.setKeyword(keywords);
                    }
                }
            } else{
                request.setAttribute("data1", data1);
                if(keywords!=null&&!keywords.equals("")){
                    searchKeyword1.setKeyword(keywords);
                }
            }
        } else if(searchKeyword1.getFlag() == 2){
            request.setAttribute("data1", data1);
            request.setAttribute("data", data);
        }
        request.setAttribute("searchKeyword", searchKeyword1);
        request.setAttribute("appAlbums", appAlbums0);
        return "search/edit";
    }
	  @RequestMapping("/editSelectRes")
	    public String editSelectRes(HttpServletRequest request, 
	    		@RequestParam(value = "menus1", required = false) List<Integer> ids1,
	    		@RequestParam(value = "menus0", required = false)List<Integer> ids0,
	    		@RequestParam(value = "menus", required = false)List<Integer> ids
	    		){
	        try{     	
	        	//合并ids和ids0
	            int searchId = 0;
	            String searchIds = request.getParameter("searchId");
	            if(StringUtils.isNotEmpty(searchIds) && !searchIds.equals("0"))
	                searchId = Integer.parseInt(searchIds);
	            String flags = request.getParameter("flag");
	            String albumIds = request.getParameter("albumId");
	            String raveIds = request.getParameter("raveId");
	            String keywords = request.getParameter("keyword");
	            String sorts = request.getParameter("sort");
	            String iconIds = request.getParameter("iconId");
	            String appnameid = request.getParameter("appnameid");
	            String categoryId = request.getParameter("categoryId") != null ? request.getParameter("categoryId") : "0";
	            String category_parent = request.getParameter("category_parent") != null ? request.getParameter("category_parent") : "0";
	            request.setAttribute("categoryId", categoryId);
	            request.setAttribute("category_parent", category_parent);
	            int currentPage = RequestUtils.getInt(request, "currentPage", 1);
	            int pageSize = RequestUtils.getInt(request, "pageSize", 48);
	            Map<String,Object> map = new HashMap<String,Object>();
	            MusicInfo criteria = new MusicInfo();
	            ImageInfo criteria1 = new ImageInfo();
	            SearchKeyword searchKeyword = new SearchKeyword();
	            if(appnameid != null && !"".equals(appnameid))
	                try
	                {
						int appid = Integer.parseInt(appnameid.trim());
	                    map.put("appid", appnameid.trim());
	                    criteria.setId(appid);
	                    criteria1.setId(appid);

	                }
	                catch(Exception e)
	                {
	                    map.put("appname", appnameid.trim());
	                    criteria.setName(appnameid.trim());
	                    criteria1.setName(appnameid.trim());

	                }
	            request.setAttribute("appnameid", appnameid);
	            if(!"0".equals(categoryId)){
	            	int categoryId0 = Integer.parseInt(categoryId);
	            	 Category cat = categoryService.getCategory(categoryId0);
	                 map.put("categoryId",categoryId0);
	                 criteria.setCategory(cat);
	                 criteria1.setCategory(cat);
	            }
	            else if(!"0".equals(category_parent)){
	            	int category_parent0 = Integer.parseInt(category_parent);
	                map.put("category_parent",category_parent0);
	            }
                criteria.setCategory_parent(4);
                criteria1.setCategory_parent(5);
	            int flag = 0;
	            int albumId = 0;
	            int raveId = 0;
	            int sort = 0;
	            Integer iconId = null;
	            if(StringUtils.isNotEmpty(flags) && !flags.equals("0"))
	                try
	                {
	                    flag = Integer.parseInt(flags);
	                }
	                catch(Exception e)
	                {
	                    flag = 0;
	                }

	            if(StringUtils.isNotEmpty(albumIds) && !albumIds.equals("0")){
	                albumId = Integer.parseInt(albumIds);
	            }
	            else{
	            	albumId = 2;            	
	            }
	            if(StringUtils.isNotEmpty(raveIds) && !raveIds.equals("0")){
	            	raveId = Integer.parseInt(raveIds);	            	
	            }
	            else{
	            	raveId = 1;	            	
	            }
	            map.put("raveId", raveId);
	            if(StringUtils.isNotEmpty(sorts)){
	                sort = Integer.parseInt(sorts);	
	            }
	            if(StringUtils.isNotEmpty(iconIds) && !iconIds.equals("0")){
	            	iconId = Integer.parseInt(iconIds);	
	            	searchKeyword.setIconId(iconId);
	            }
	            searchKeyword.setAlbumId(albumId);
	            searchKeyword.setFlag(flag);
	            searchKeyword.setKeyword(keywords);
	            searchKeyword.setRaveId(raveId);
	            searchKeyword.setSort(sort);
	            searchKeyword.setSearchId(searchId);
	            request.setAttribute("searchKeyword", searchKeyword);
	            Country country = countryService.getCountry(raveId);
	            request.setAttribute("country", country);    
	            if(ids0!= null && ids0.size()>0){
	            	request.setAttribute("menus0", StringUtil.listInteger2String(ids0));
	            } else{
	                request.setAttribute("menus0", null);
	            }
	            if(ids1 != null && ids1.size()>0){
	                request.setAttribute("menus1", StringUtil.listInteger2String(ids1));
	            } else{
	                request.setAttribute("menus1", null);
	            }
	    		if(ids0!=null&&ids1!=null){
	    			ids1.addAll(ids0);
	    		}else if(ids0!=null&&ids1==null){
	    			ids1 = ids0;
	    		}
	    		Set<Integer> set = new HashSet<Integer>();
	    		if(ids!=null){
	    			set.addAll(ids1);
	    		}
	    		List<Integer> list = new ArrayList<Integer>(set);
	    		
	    		if(list!=null&&list.size()>0){
	    			ids1 = list;
	    		}
	    		if(ids1!=null&&ids1.size()>0){
		            map.put("ids", ids1);
	    		}
	            if(albumId == 2)
	            {
	                PaginationVo<AppFile> result = searchKeywordService.getSelectAppFiles(map, currentPage, pageSize);
	                request.setAttribute("result", result);
	                int size = result.getData().size();
	                request.setAttribute("counts", Integer.valueOf(size % 4 != 0 ? size / 4 + 1 : size / 4));
	            } else  if(albumId == 4){
	                criteria.setState(true);
	                criteria.setRaveId(Integer.valueOf(raveId));
	                criteria.setSearchkey(1);
		    		if(ids1!=null&&ids1.size()>0){
		                criteria.setIds(ids1);
		    		}
	                PaginationVo<MusicInfo> result = musicInfoService.searchMusics(criteria, currentPage, pageSize);
	                request.setAttribute("result", result);
	                int size = result.getData().size();
	                request.setAttribute("counts", Integer.valueOf(size % 4 != 0 ? size / 4 + 1 : size / 4));
	            } else if(albumId == 5){
	                criteria1.setState(true);
	                criteria1.setRaveId(Integer.valueOf(raveId));
	                criteria1.setSearchkey(1);
	                if(ids1!=null&&ids1.size()>0){
		                criteria.setIds(ids1);
		    		}
	                PaginationVo<ImageInfo> result = imageInfoService.searchImages(criteria1, currentPage, pageSize);
	                request.setAttribute("result", result);
	                int size = result.getData().size();
	                request.setAttribute("counts", Integer.valueOf(size % 4 != 0 ? size / 4 + 1 : size / 4));
	            }
	        }catch(Exception e){
	            log.error("SearchKeywordController editSelectRes", e);
	            PaginationVo<AppFile> result = new PaginationVo<AppFile>(null, 0, 10, 1);
				request.setAttribute("result", result);
	        }
	        return "search/editselectRes";
	    }

    private List getResData1(int albumIdoriginal,List<Integer> ids1, SearchKeyword searchKeyword1)
    {
        List data = null;
        if(ids1 != null)
        {
            if(searchKeyword1.getAlbumId() == 2){
                data = new ArrayList<AppInfo>();
                for(Integer id:ids1){
                	AppInfo  appInfo = appInfoService.getApp(id);
                    data.add(appInfo);
                }

            } else if(searchKeyword1.getAlbumId() == 4){
                data = new ArrayList<MusicInfo>();
                for(Integer id:ids1){
                	MusicInfo musicInfo = musicInfoService.getMusicInfo(id);
                    data.add(musicInfo);
                }

            } else if(searchKeyword1.getAlbumId() == 5){
                data = new ArrayList<ImageInfo>();
                for(Integer id:ids1){
                	ImageInfo imageInfo = imageInfoService.getImageInfo(id);
                    data.add(imageInfo);
                }
            }
        }else if(albumIdoriginal==searchKeyword1.getAlbumId()){
            if(searchKeyword1.getFlag() == 1 && searchKeyword1.getResId() != 0){
                if(searchKeyword1.getAlbumId() == 2)
                {
                    data = new ArrayList<AppInfo>();
                    AppInfo appInfo = appInfoService.getApp(searchKeyword1.getResId());
                    data.add(appInfo);
                } else if(searchKeyword1.getAlbumId() == 4)
                {
                    data = new ArrayList<MusicInfo>();
                    MusicInfo musicInfo = musicInfoService.getMusicInfo(searchKeyword1.getResId());
                    data.add(musicInfo);
                } else  if(searchKeyword1.getAlbumId() == 5)
                {
                    data = new ArrayList<ImageInfo>();
                    ImageInfo imageInfo = imageInfoService.getImageInfo(searchKeyword1.getResId());
                    data.add(imageInfo);
                }
            } else if(searchKeyword1.getFlag() == 2){
                List<SearchKeywordResList> searchKeywordResLists = searchKeywordResListService.selectBySearchId(searchKeyword1.getSearchId());
                if(searchKeywordResLists != null){
                    if(searchKeyword1.getAlbumId() == 2){
                        data = new ArrayList<AppInfo>();
                        for(SearchKeywordResList searchKeywordResList:searchKeywordResLists ){
                            int resId = searchKeywordResList.getResId();
                            AppInfo appInfo = appInfoService.getApp(resId);
                            if(appInfo!=null){
                                data.add(appInfo);
                            }
                        }
                    } else if(searchKeyword1.getAlbumId() == 4){
                        data = new ArrayList<MusicInfo>();
                        for(SearchKeywordResList searchKeywordResList:searchKeywordResLists ){
                            int resId = searchKeywordResList.getResId();
                            MusicInfo musicInfo = musicInfoService.getMusicInfo(resId);
                            if(musicInfo!=null){
                                data.add(musicInfo);
                            }
                        }
                    } else if(searchKeyword1.getAlbumId() == 5){
                        data = new ArrayList<ImageInfo>();
                        for(SearchKeywordResList searchKeywordResList:searchKeywordResLists ){
                            int resId = searchKeywordResList.getResId();
                            ImageInfo  imageInfo = imageInfoService.getImageInfo(resId);
                            if(imageInfo!=null){
                                data.add(imageInfo);
                            }
                        }
                    }
                }
            }
        } 
 
        return data;
    }

    private List getResData(List<Integer> ids, SearchKeyword searchKeyword1){
        List data = null;
        if(ids != null){
            if(searchKeyword1.getAlbumId() == 2){
                data = new ArrayList<AppInfo>();
                for(Integer id:ids){
                	AppInfo  appInfo = appInfoService.getApp(id);
                    data.add(appInfo);
                }

            } else if(searchKeyword1.getAlbumId() == 4){
                data = new ArrayList<MusicInfo>();
                for(Integer id:ids){
                	MusicInfo musicInfo = musicInfoService.getMusicInfo(id);
                    data.add(musicInfo);
                }

            } else if(searchKeyword1.getAlbumId() == 5){
                data = new ArrayList<ImageInfo>();
                for(Integer id:ids){
                	ImageInfo imageInfo = imageInfoService.getImageInfo(id);
                    data.add(imageInfo);
                }
            }
        }
        return data;
    }
  
	@ResponseBody
	@RequestMapping("/secondCategory")
	public String querySecondCategory(@RequestParam("id") Integer id, @RequestParam("categoryId") Integer categoryId) {
		try {
			List<Category> list = categoryService.getCategorys(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				options.append("<option value='0'>--all--</option>");
				for (Category category : list) {
					String selected = "";
					if (category.getId() == categoryId) {
						selected = "selected";
					}
					options.append("<option value='");
					options.append(category.getId() + "' " + selected + ">");
					options.append(category.getName());
					options.append("</option>");
				}
				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			return "{\"success\":\"2\"}";
		}
	}
  
}
