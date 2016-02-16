package com.reportforms.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.service.BaseService;
import com.reportforms.template.XMLModelTemplate;
import com.reportforms.util.ExportExcelView;
import com.reportforms.util.TemplateUtil;

@Controller
@RequestMapping("/export")
@SuppressWarnings("unchecked")
public class ExportAction {
	
	private static final Logger logger = Logger.getLogger(ExportAction.class);
	
	@RequestMapping("/export2Excel")
	public ModelAndView exportToExcel(HttpServletRequest request,HttpServletResponse response){
		try {
			//System.out.println("character:" + request.getCharacterEncoding());
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.error("request setCharacterEncoding has error:", e1);
		}
		//解析并封装分页查询参数
		PaginationBean paginationBean = new PaginationBean(request);
		Set<String> keyset = paginationBean.getParams().keySet();
		for (String key : keyset) {
			try {
				paginationBean.getParams().put(key, new String((paginationBean.getParams().get(key).toString()).getBytes("ISO8859-1"),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("IOS8859-1 convert to utf-8 has error :", e);
			}
		}
		
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(
				request.getSession().getServletContext());
		
		XMLModelTemplate template = (XMLModelTemplate) context.getBean("modelTemplate");

		if(null == template){
			logger.info("XMLModelTemplate is null !");
			return null;
		}
		//解析xml定义的模板文件
		template = TemplateUtil.readXml2Template(request, template);
		
		if(StringUtils.isEmpty(template.getServiceName())){
			logger.info(" the serviceimpl name is null !");
			return null;
		}
		//从spring容器中取得service实例
		BaseService base = (BaseService)context.getBean(template.getServiceName());
		if(null == base){
			logger.info("the service base : " + template.getServiceName() + "is null !");
			return null;
		}
		if(StringUtils.isEmpty(template.getMethodName())){
			//设置默认调用方法
			template.setMethodName("query");
		}
		if(StringUtils.isEmpty(template.getCountsName())){
			//设置默认调用方法
			template.setCountsName("queryAllCounts");
		}
		List<Object> dataList = null;
		//通过类的反射机制调用xml模板文件中定义的方法
		Class c = base.getClass();
		String dataSource = (String)paginationBean.getParams().get("dataSource");
		try {
			//切换数据源
			if(StringUtils.isNotEmpty(dataSource)){
				DataSourceSwitch.setDataSourceType(dataSource);
				logger.info("use the dynamic datasource :" + dataSource);
			}
			Method method = c.getDeclaredMethod(template.getCountsName(), new Class[]{PaginationBean.class});
			Integer allCounts = (Integer)method.invoke(base, paginationBean);
			logger.info("query all counts : " + allCounts.intValue());
			paginationBean.setLimit(allCounts.intValue());
			method = c.getDeclaredMethod(template.getMethodName(), new Class[]{PaginationBean.class});
			dataList = (List<Object>)method.invoke(base, paginationBean);
			//移除数据源,使用默认数据源
			if(StringUtils.isNotEmpty(dataSource)){
				DataSourceSwitch.clearDataSourceType();
				logger.info("clear the dynamic datasource and use the default datasource .");
			}
			if(null == dataList || dataList.isEmpty()){
				logger.info("the query list is empty !");
				return null;
			}
		} catch (Exception e) {
			logger.error("query data list has error : ", e);
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", dataList);
		map.put("template", template);
		//生成excel文件并返回给页面
		return new ModelAndView(new ExportExcelView(),map);
	}

}
