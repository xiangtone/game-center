package com.reportforms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.model.Menu;
import com.reportforms.model.MenuExample;
import com.reportforms.model.User;
import com.reportforms.service.MenuService;
import com.reportforms.service.UserService;
import com.reportforms.util.MenuUtil;

@Controller
@RequestMapping("/menu")
public class MenuAction {
	
	private static final Logger log = Logger.getLogger(MenuAction.class);
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/list")
	public String list(){
		return "menu/list";
	}
	
	/**
	 * 查询
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public Map<String, Object> query(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Menu> list = new ArrayList<Menu>();
		Integer total = menuService.countByExample(new MenuExample());
		if(null != total && total.intValue() > 0){
			list  = menuService.queryAll(new Menu());
			resultMap.put("total", total.intValue());
			resultMap.put("rows", list);
		}else{
			resultMap.put("total", 0);
			resultMap.put("rows", list);
		}
		return resultMap;
	}
	
	/**
	 * 新增菜单/编辑菜单的数据唯一性校验
	 * 校验字段为:code,name,uri
	 * @param menu
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/valid",method = RequestMethod.POST)
	public Map<String, Object> valid(@ModelAttribute Menu menu){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Menu> list = menuService.queryByCriteria(menu);
		if(CollectionUtils.isEmpty(list)){
			resultMap.put("count",0);
		}else{
			int size = 0;
			//如果menu id 为空,即是新增数据的唯一性校验,不为空则为编辑数据的唯一性校验
			if(null != menu.getId()){
				for (Menu entity : list) {
					//将编辑数据本身排除掉
					if(entity.getId() != menu.getId()){
						size ++;
					}
				}
			}else{
				size = list.size();
			}
			resultMap.put("count", size);
		}
		return resultMap;
	}
	
	/**
	 * 新增菜单
	 * @param menu
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/add",method = RequestMethod.POST)
	public Map<String, Object> add(@ModelAttribute Menu menu){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			menuService.addMenu(menu.getType().getId(), menu);
			resultMap.put("success", true);
			resultMap.put("desc", "菜单成功保存");
		} catch (Exception e) {
			log.error("新增菜单数据异常:", e);
			resultMap.put("success", false);
			resultMap.put("desc", "菜单保存失败");
		}
		return resultMap;
	}
	
	/**
	 * 编辑菜单
	 * @param menu
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/edit",method = RequestMethod.POST)
	public Map<String, Object> edit(@ModelAttribute Menu menu,HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			menuService.updateMenu(menu);
			resultMap.put("success", true);
			resultMap.put("desc", "菜单编辑成功");
			reloadNavigationMenu(request);
		} catch (Exception e) {
			log.error("编辑菜单数据异常:", e);
			resultMap.put("success", false);
			resultMap.put("desc", "菜单编辑失败");
		}
		return resultMap;
	}
	
	/**
	 * 删除菜单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete",method = RequestMethod.POST)
	public Map<String, Object> delete(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String idStr = request.getParameter("ids");
		try {
			if(StringUtils.isEmpty(idStr)){
				resultMap.put("success", false);
				resultMap.put("desc", "请选择需要删除的记录");
				return resultMap;
			}
			String[] ids = idStr.split(",");
			if(ArrayUtils.isEmpty(ids)){
				resultMap.put("success", false);
				resultMap.put("desc", "请选择需要删除的记录");
				return resultMap;
			};
			for (String id : ids) {
				menuService.deleteMenu(Integer.parseInt(id));
			}
			resultMap.put("success", true);
			resultMap.put("desc", "菜单删除成功");
			reloadNavigationMenu(request);
		} catch (Exception e) {
			log.error("删除菜单数据异常:", e);
			resultMap.put("success", false);
			resultMap.put("desc", "菜单删除失败");
		}
		return resultMap;
	}
	
	/**
	 * 查找父级菜单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/queryParentMenu",method = RequestMethod.POST)
	public Map<String, Object> queryParentMenu(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String typeId = request.getParameter("typeId");
		try {
			if(StringUtils.isEmpty(typeId)){
				resultMap.put("success", false);
				resultMap.put("menus", null);
				return resultMap;
			}
			int parentTypeId = Integer.parseInt(typeId);
			List<Menu> menus = menuService.queryAll(new Menu());
			List<Menu> list = new ArrayList<Menu>();
			for (Menu menu : menus) {
				if(menu.getParentId().intValue() == (parentTypeId - 1)){
					list.add(menu);
				}
			}
			resultMap.put("success", true);
			resultMap.put("menus", CollectionUtils.isEmpty(list) ? null : list);
		} catch (Exception e) {
			log.error("查询父级菜单异常:", e);
			resultMap.put("success", false);
			resultMap.put("menus", null);
		}
		return resultMap;
	}
	
	/**
	 * 刷新左侧导航菜单栏
	 * @param request
	 */
	private void reloadNavigationMenu(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("loginUser");
		if(null != user){
			List<Menu> list = userService.getMenusByUserId(user.getId());
			if(!CollectionUtils.isEmpty(list)){
				MenuUtil.loadChildMenu(list);
				request.getSession().setAttribute("leftMenus", list);
			}
		}
	}

}
