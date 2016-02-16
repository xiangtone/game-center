package com.mas.rave.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.MenuType;
import com.mas.rave.main.vo.User;
import com.mas.rave.service.MenuService;
import com.mas.rave.service.UserService;
import com.mas.rave.util.MenuUtil;
import com.sun.xml.internal.ws.resources.HttpserverMessages;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	private final Logger log = Logger.getLogger(MenuController.class);
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 菜单项列表页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String page(HttpServletRequest request){
		List<Menu> list = menuService.getMenus();
		if(null != list && !list.isEmpty()){
			for (Menu menu : list) {
				for (Menu menu1 : list) {
					if(menu.getParentId().intValue() == menu1.getId().intValue()){
						menu.setParent(menu1);
					}
				}
			}
		}
		User user = (User)request.getSession().getAttribute("loginUser");
		if(null != user){
			List<Menu> menu_List = userService.getMenusByUserId(user.getId());
			String menuStr = MenuUtil.parseMenus(menu_List, request);
			request.getSession().setAttribute("menus", menuStr);
		}
		request.setAttribute("menuList", list);
		request.setAttribute("dataSize", null != list ? list.size() : 0);
		return "menu/list";
	}
	
	/**
	 * 向新增菜单页面跳转
	 * @param request
	 * @return
	 */
	@RequestMapping("/add")
	public String addpage(HttpServletRequest request){
		List<MenuType> list = menuService.getMenuTypes();
		request.setAttribute("menuTypes", list);
		return "menu/add";
	}
	
	/**
	 * 根据菜单类型查询父类菜单
	 * @param typeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryParentMenu")
	public Map<Integer, String> selectParentByType(@Param("typeId") Integer typeId){
		//根据菜单类型查找菜单项
		List<Menu> list = menuService.getMenusOfType(typeId.intValue() - 1);
		Map<Integer, String> map = new HashMap<Integer,String>();
		if(null != list && !list.isEmpty()){
			for (Menu menu : list) {
				map.put(menu.getId(), menu.getName());
			}
		}
		return map;
	}
	
	/**
	 * 保存菜单项数据
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="/save",method = RequestMethod.POST)
	public String save(@ModelAttribute Menu menu,HttpServletRequest request){
		menuService.addMenu(menu.getType().getId(), menu);
		return "redirect:/menu/list";
	}
	
	/**
	 * 删除菜单项
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@Param("id") String id,HttpServletRequest request){
		if(StringUtils.isNotEmpty(id)){
			String[] ids = id.split(",");
			List<Menu> list = menuService.queryByParentId(Arrays.asList(ids));
			if(null != list && !list.isEmpty()){
				return "{success:2}";
			}
			for (String s : ids) {
				try {
					menuService.deleteMenu(Integer.parseInt(s));
				} catch (Exception e) {
					// TODO: handle exception
					log.error("delete menu data failed !", e);
					return "{success:0}";
				}
			}
			log.info("delete menu data success !");
			return "{success:1}";
		}
		
		return "{success:1}";
	}
	
	@RequestMapping("/{id}")
	public String editPage(@PathVariable Integer id,HttpServletRequest request){
		Menu menu = menuService.getMenuById(id);
		List<MenuType> list = menuService.getMenuTypes();
		request.setAttribute("menuTypes", list);
		request.setAttribute("menu", menu);
		return "menu/edit";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String editSave(@ModelAttribute Menu menu){
		try {
			menuService.updateMenu(menu);
			log.info("update menu datas success !");
		} catch (Exception e) {
			log.error("update menu datas failed ! ", e);
		}
		return "redirect:/menu/list";
	}
	
	@ResponseBody
	@RequestMapping("/queryByName")
	public String queryByName(HttpServletRequest request){
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(code)){
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("name", name);
			paramMap.put("code", code);
			paramMap.put("id", id);
			List<Menu> list = menuService.queryByNameAndCode(paramMap);
			if(null != list && !list.isEmpty()){
				log.debug("menu name or code has the same name or code in database !");
				return "{\"success\":\"0\"}";
			}
		}
		return "{\"success\":\"1\"}";
	}

}
