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
import com.reportforms.model.ImageDownLoad;
import com.reportforms.model.MusicDownLoad;
import com.reportforms.service.MusicDownLoadService;

@Controller
@RequestMapping("/musicdownload")
public class MusicDownLoadAction {
	
	private static final Logger log = Logger.getLogger(MusicDownLoadAction.class);
	
	@Autowired
	private MusicDownLoadService<MusicDownLoad> musicDownLoadService;
	
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
		List<MusicDownLoad> list = null;
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			total = musicDownLoadService.queryByGroupByCounts(paginationBean);
			if(null != total && total.intValue() > 0){
				list = musicDownLoadService.queryByGroupBy(paginationBean);
			}
			if(CollectionUtils.isEmpty(list)){
				list = new ArrayList<MusicDownLoad>();
			}else {
				MusicDownLoad sumTotal = musicDownLoadService.queryByTotal(paginationBean);
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
	
	@RequestMapping("/list")
	public String list(){
		return "musicdownload/list";
	}
	
	@RequestMapping("/showMore")
	public String showView(){
		return "musicdownload/view";
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
		List<MusicDownLoad> list = new ArrayList<MusicDownLoad>();
		Integer total = 0;
		try {
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL2);
			total = musicDownLoadService.queryAllCounts(paginationBean);
			if(null != total && total.intValue() > 0){
				list = musicDownLoadService.query(paginationBean);
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

}
