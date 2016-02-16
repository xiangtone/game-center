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
import com.reportforms.model.ImageDownLoad;
import com.reportforms.service.ImageDownLoadService;

@Controller
@RequestMapping("/imagedownload")
public class ImageDownLoadAction {
	
	private static final Logger log = Logger.getLogger(ImageDownLoadAction.class);
	
	@Autowired
	private ImageDownLoadService<ImageDownLoad> imageDownLoadService;
	
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
		List<ImageDownLoad> list = null;
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			total = imageDownLoadService.queryByGroupByCounts(paginationBean);
			if(null != total && total.intValue() > 0){
				list = imageDownLoadService.queryByGroupBy(paginationBean);
			}
			if(CollectionUtils.isEmpty(list)){
				list = new ArrayList<ImageDownLoad>();
			}else {
				ImageDownLoad sumTotal = imageDownLoadService.queryByTotal(paginationBean);
				sumTotal.setCategoryName("总计:");
				resultMap.put("footer", new Object[]{sumTotal});
			}
			resultMap.put("total", null == total ? 0 : total.intValue());
			resultMap.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			log.error("query musicdownload data error:",e);
		}
		return resultMap;
	}
	
	/**
	 * 查看详情
	 */
	@ResponseBody
	@RequestMapping("/view")
	public Map<String, Object> view(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PaginationBean paginationBean = new PaginationBean(request);
		List<ImageDownLoad> list = new ArrayList<ImageDownLoad>();
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			total = imageDownLoadService.queryAllCounts(paginationBean);
			if(null != total && total.intValue() > 0){
				list = imageDownLoadService.query(paginationBean);
			}
			resultMap.put("total", null == total ? 0 : total.intValue());
			resultMap.put("rows", list);
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
		} catch (Exception e) {
			log.error("query musicdownload data error:",e);
		}
		return resultMap;
	}
	
	@RequestMapping("/list")
	public String list(){
		return "imagedownload/list";
	}
	
	@RequestMapping("/showMore")
	public String showMore(){
		return "imagedownload/view";
	}

}
