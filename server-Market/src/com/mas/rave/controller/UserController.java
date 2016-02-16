package com.mas.rave.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.Role;
import com.mas.rave.main.vo.User;
import com.mas.rave.service.MenuService;
import com.mas.rave.service.RoleService;
import com.mas.rave.service.UserService;
import com.mas.rave.util.MD5;
import com.mas.rave.util.MenuUtil;
import com.mas.rave.vo.RoleVO;

@Controller
@RequestMapping("/user")
@SuppressWarnings("unchecked")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;

	/** 登录页 */
	@RequestMapping("/login")
	public String showLogin() {
		return "login";
	}

	/** 登录页 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		Enumeration<String> attrNames = request.getSession().getAttributeNames();
		while (attrNames.hasMoreElements()) {
			request.getSession().removeAttribute(attrNames.nextElement());
		}
		return "redirect:/";
	}

	/** 登录 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(User user, HttpServletRequest request) {
		if (null == user) {
			request.setAttribute("errorMsg", "用户名，密码输入有误，或者您的账号已冻结，请联系管理员！");
			return "login";
		}
		List<User> users = userService.selectByExample(user);
		if (null != users && !users.isEmpty()) {
			request.getSession().setAttribute("loginUser", users.get(0));
			return "redirect:/user/list";
		} else {
			request.setAttribute("errorMsg", "用户名，密码输入有误，或者您的账号已冻结，请联系管理员！");
			return "login";
		}
	}

	// @ResponseBody
	// @RequestMapping("/loadMenu")
	// public String loadMenu(HttpServletRequest request, HttpServletResponse
	// response) {
	//
	// String userName = request.getParameter("userName");
	// StringBuilder strb = new StringBuilder();
	// if (StringUtils.isNotEmpty(userName)) {
	// User user = userService.getUserByLoginName(userName);
	// List<Menu> list = userService.getMenusByUserId(user.getId());
	// Collections.sort(list);
	// strb.append("<ul>");
	// // 递归创建菜单树
	// create(list, strb, 0, request);
	// strb.append("</ul>");
	// // System.out.println("strb=>" + strb.toString());
	// } else {
	// strb.append("");
	// }
	// return strb.toString();
	// }

	/** 加载单个用户 */
	@RequestMapping("/{id}")
	public String loadUserById(@PathVariable("id") Integer id, Model model) {
		User user = userService.getUserById(id);

		// 处理用户当前所属角色
		List<Role> checkedRoles = userService.getRolesByUserId(id);
		List<Role> roles = roleService.getAllRoles();
		List<RoleVO> roleVos = new ArrayList<RoleVO>();
		for (Role role : roles) {
			RoleVO rv = new RoleVO();
			BeanUtils.copyProperties(role, rv);
			for (Role crv : checkedRoles) {
				if (rv.getId().equals(crv.getId())) {
					rv.setChecked(true);
					break;
				}
			}
			roleVos.add(rv);
		}
		model.addAttribute("roleVos", roleVos);

		model.addAttribute("user", user);
		return "user/edit";
	}

	/** 更新用户 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(HttpServletRequest request, User user) {
		User _user = userService.getUserByLoginName(user.getName());
		if (_user != null && !_user.getId().equals(user.getId())) {
			return "{\"flag\":\"1\"}";
		}
		// 如果用户没有修改密码,那么此处无需再次加密
		if (_user != null && !_user.getPassword().equals(user.getPassword())) {
			user.setPassword(MD5.getMd5Value(user.getPassword()).toUpperCase());
		}
		userService.update(user);
		// 获取当前登陆用户
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		// 如果是非admin用户,实时更新登陆用户信息
		if (!loginUser.getName().equals("admin")) {
			request.getSession().setAttribute("loginUser", user);
		}
		return "{\"flag\":\"0\"}";
	}

	/** 查询用户列表 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);
		String name = request.getParameter("name");
		User user = (User) request.getSession().getAttribute("loginUser");
		int activable = 0;// default
		try {
			activable = Integer.parseInt(request.getParameter("activable"));
		} catch (Exception e) {
		}
		// System.out.println("$$$$$$$$$$activable="+activable);
		UserService.UserCriteria criteria = new UserService.UserCriteria();
		if (StringUtils.isNotBlank(name)) {
			criteria.nameLike(name);
		} else {
			if (!user.getName().equals("admin")) {
				criteria.nameLike(user.getName());
			}
		}
		if (activable == 1) {
			criteria.activable(true);
		} else if (activable == 2) {
			criteria.activable(false);
		}
		List<Menu> list = userService.getMenusByUserId(user.getId());
		
		String menus = MenuUtil.parseMenus(list, request);
		request.getSession().setAttribute("menus", menus);
		PaginationVo<User> result = userService.searchUsers(criteria, currentPage, pageSize);
		request.setAttribute("result", result);
		return "user/list";
	}

	@ResponseBody
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public Map<String, Object> query(HttpServletRequest request) {

		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> map = request.getParameterMap();
		for (Object obj : map.keySet()) {
			System.out.println("Key=>" + obj.toString());
			// Object value = map.get(obj);
			// System.out.println("Value=>" + value);
		}

		int currentPage = RequestUtils.getInt(request, "page", 1);
		int pageSize = RequestUtils.getInt(request, "rows", PaginationVo.DEFAULT_PAGESIZE);
		String name = request.getParameter("name");
		User user = (User) request.getSession().getAttribute("loginUser");
		int activable = 0;// default
		try {
			activable = Integer.parseInt(request.getParameter("activable"));
		} catch (Exception e) {
		}
		UserService.UserCriteria criteria = new UserService.UserCriteria();
		if (StringUtils.isNotBlank(name)) {
			criteria.nameLike(name);
		} else {
			if (!user.getName().equals("admin")) {
				criteria.nameLike(user.getName());
			}
		}
		if (activable == 1) {
			criteria.activable(true);
		} else if (activable == 2) {
			criteria.activable(false);
		}
		PaginationVo<User> users = userService.searchUsers(criteria, currentPage, pageSize);
		List<User> list = users.getData();
		result.put("total", users.getRecordCount());
		result.put("rows", list);
		// result.put("total", 0);
		// result.put("rows", new ArrayList<User>());
		return result;
	}

	/** 删除用户 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		userService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 激活用户 */
	@ResponseBody
	@RequestMapping("/active")
	public String active(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		userService.batchActive(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 删除用户 */
	@ResponseBody
	@RequestMapping("/disable")
	public String disable(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		userService.batchDisable(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 新增用户页 */
	@RequestMapping("/add")
	public String showAdd() {
		return "user/add";
	}

	/** 新增用户 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(User user, HttpServletResponse response) {
		if (userService.getUserByLoginName(user.getName()) != null) {
			return "{\"flag\":\"1\"}";
		}
		user.setPassword(MD5.getMd5Value(user.getPassword()).toUpperCase());
		userService.addNewUser(user);
		return "{\"flag\":\"0\",\"id\":\"" + user.getId() + "\"}";
	}
}
