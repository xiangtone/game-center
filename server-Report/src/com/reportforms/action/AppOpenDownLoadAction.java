package com.reportforms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.AppDownLoad;
import com.reportforms.model.Category;
import com.reportforms.service.AppDownLoadService;
import com.reportforms.service.CategoryService;

@Controller
@RequestMapping("/appdownload")
public class AppOpenDownLoadAction {
	
	private static final Logger log = Logger.getLogger(AppOpenDownLoadAction.class);
	
	@Autowired
	private AppDownLoadService<AppDownLoad> appDownLoadService;
	
	@Autowired
	private CategoryService<Category> categoryService;
	
	/**
	 * 进行分组过滤后的查询
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<AppDownLoad> list = null;
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			total = appDownLoadService.queryByGroupByCounts(paginationBean);
			if(null != total && total.intValue() > 0){
				list = appDownLoadService.queryByGroupBy(paginationBean);
			}
			if(CollectionUtils.isEmpty(list)){
				list = new ArrayList<AppDownLoad>();
			}else {
				AppDownLoad sumTotal = appDownLoadService.queryByTotal(paginationBean);
				if(null == paginationBean.getParams().get("free")){
					sumTotal.setCategoryName("总计:");
				}else {
					sumTotal.setSource("总计:");
				}
				resultMap.put("footer", new Object[]{sumTotal});
			}
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
			resultMap.put("total", null == total ? 0 : total.intValue());
			resultMap.put("rows", list);
			
		} catch (Exception e) {
			log.error("query appdownload data error:",e);
		}
		return resultMap;
	}
	
	/**
	 * 查看详情
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/view")
	public Map<String, Object> view(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<AppDownLoad> list = new ArrayList<AppDownLoad>();
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			total = appDownLoadService.queryAllCounts(paginationBean);
			if(null != total && total.intValue() > 0){
				list = appDownLoadService.query(paginationBean);
			}
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
			resultMap.put("total", null == total ? 0 : total.intValue());
			resultMap.put("rows", list);
			
		} catch (Exception e) {
			log.error("query appdownload data error:",e);
		}
		return resultMap;
	}
	
	/**
	 * 应用下载统计列表页面
	 * @return
	 */
	@RequestMapping("/list")
	public String list(){
		return "appdownload/list";
	}
	
	/**
	 * 自营下载统计页面
	 * @return
	 */
	@RequestMapping("/self")
	public String self(){
		return "selfappdownload/list";
	}
	
	/**
	 * 应用下载详情页面
	 * @return
	 */
	@RequestMapping("/showMore")
	public String showMore(){
		return "appdownload/view";
	}
	
	/**
	 * 自营下载详情页面
	 * @return
	 */
	@RequestMapping("/showMoreSelf")
	public String showMoreSelf(){
		return "selfappdownload/view";
	}

}
