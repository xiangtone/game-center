package com.reportforms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.Category;
import com.reportforms.service.CategoryService;
import com.reportforms.vo.CategoryVO;

@Controller
@RequestMapping("/category")
public class CategoryAction {
	
	@Autowired
	private CategoryService<Category> categoryService;
	
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(HttpServletRequest request,@RequestParam("id") Integer id){
		Map<String, Object> resultMap = new HashMap<String,Object>();
		PaginationBean paramBean = new PaginationBean();
		paramBean.getParams().put("state", true);
		paramBean.getParams().put("fatherId", id);
		//切换数据源
		DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
		List<Category> list = categoryService.query(paramBean);
		DataSourceSwitch.clearDataSourceType();
		StringBuilder strb = new StringBuilder();
		List<CategoryVO> vos = new ArrayList<CategoryVO>();
		if(!CollectionUtils.isEmpty(list)){
			for (Category category : list) {
				CategoryVO vo = new CategoryVO();
				vo.setId(category.getId());
				vo.setName(category.getName());
				vos.add(vo);
				//strb.append("{\"id\":").append(category.getId()).append(",");
				//strb.append("\"name\":\"").append(category.getName()).append("\"},");
			}
			//resultMap.put("categorys", strb.substring(0, strb.length() - 1 ).toString());
			resultMap.put("categorys", vos);
		}
		return resultMap;
	}

}
