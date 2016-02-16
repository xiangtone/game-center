package com.reportforms.common.page;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


public class PaginationBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4449776617831608747L;
	
	private static final Logger logger = Logger.getLogger(PaginationBean.class);
	//目标页
	private int page = 1;
	//起始查询
	private int start = 0;
	//查询条数
	private int limit = 10;
	//默认查询条数
	private int defaultPageSize = 10;
	//排序字段
	private String sort =  "id";
	//排序方式
	private String order = "asc";
	//查询结果的全部行数
	private int totalItems = 0;
	//查询条件参数
	private Map<String, Object> params = new HashMap<String, Object>();
	
	public PaginationBean() {
		// TODO Auto-generated constructor stub
	}
	
	public PaginationBean(HttpServletRequest request){
		String page = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		try {
			if(StringUtils.isNotEmpty(page)){
				this.page = Integer.parseInt(page);
			}
			if(StringUtils.isNotEmpty(pageSize)){
				this.limit = Integer.parseInt(pageSize);
				if(this.limit > 10){
					this.defaultPageSize = this.limit;
				}
			}
			if(StringUtils.isNotEmpty(sort)){
				this.sort = sort;
			}
			if(StringUtils.isNotEmpty(order)){
				this.order = order;
			}
			Enumeration paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements())
	        {
	            String paramName = (String)paramNames.nextElement();

	            if (paramName.startsWith("Q_"))
	            {
	                String paramValue = request.getParameter(paramName);
	                int index = paramName.indexOf("_");
	                String queryName = paramName.substring(index + 1);
	                if(StringUtils.isNotEmpty(queryName)){
	                	String format = request.getParameter("format");
	                	//根据format参数判断是否需要增加时分秒
	                	if(StringUtils.isEmpty(format)){
	                		//开始日期增加 时:分:秒
		                	if(queryName.equals("startTime") && StringUtils.isNotEmpty(paramValue)){
		                		paramValue += " 00:00:00";
		                	}
		                	//结束日期增加 时:分:秒
		                	if(queryName.equals("endTime") && StringUtils.isNotEmpty(paramValue)){
		                		paramValue += " 23:59:59";
		                	}
	                	}
	                	//从request请求中过滤出查询参数,放入查询集合中
		                this.params.put(queryName, paramValue);
	                }
	            }
	        }
		} catch (Exception e) {
			logger.error("paginationBean format data error : ", e);
		}
		
		this.start = (this.page - 1) * this.defaultPageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getDefaultPageSize() {
		return defaultPageSize;
	}

	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
}
