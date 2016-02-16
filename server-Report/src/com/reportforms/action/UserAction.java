package com.reportforms.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reportforms.common.MyCollectionUtils;
import com.reportforms.common.page.PaginationVo;
import com.reportforms.common.web.RequestUtils;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.Menu;
import com.reportforms.model.MenuType;
import com.reportforms.model.Role;
import com.reportforms.model.User;
import com.reportforms.service.MenuService;
import com.reportforms.service.RoleService;
import com.reportforms.service.UserService;
import com.reportforms.util.MD5;
import com.reportforms.util.MenuUtil;
import com.reportforms.vo.MenuTypeVO;
import com.reportforms.vo.MenuVO;
import com.reportforms.vo.RoleVO;



@Controller
@RequestMapping("/user")
public class UserAction {
	
	private final Logger logger = Logger.getLogger(UserAction.class);

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	/**
	 * 渠道商起始账号
	 * 100000
	 */
	private Integer channel_account = 100000;
	
	/**
	 * CP厂商起始账号
	 * 300000
	 */
	private Integer cp_account = 300000;
	
	/**
	 * APP应用起始账号
	 * 500000
	 */
	private Integer app_account = 500000;
	
	/**登录页*/
	@RequestMapping("/login")
	public String showLogin(){
		return "login";
	}
	
	/**登录页*/
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request){
		Enumeration<String> attrNames = request.getSession().getAttributeNames();
		while(attrNames.hasMoreElements()){
			request.getSession().removeAttribute(attrNames.nextElement());
		}
		return "redirect:/";
	}
	
	/**登录*/
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(User user,HttpServletRequest request){
		if(null == user || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())){
			request.setAttribute("errorMsg", "用户名或密码输入有误！");
			return "login";
		}
		String userName = user.getName();
		try {
			Integer roleName = Integer.parseInt(userName);
			Integer result = 0;
			Integer roleId = 0;
			user.setPassword(MD5.getMd5Value(user.getPassword()));
			//切换数据源
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
			if(roleName.intValue() >= this.channel_account && roleName.intValue() < this.cp_account ){
				logger.info(" query channel user !");
				//用户编号在100000~300000之间为:渠道商角色
				result = userService.queryChannelUser(user);
				//已在数据库直接固定
				roleId = 3;
				//渠道ID作为查询条件
				user.setQueryRequirement("channelId");
			}else if(roleName.intValue() >= this.cp_account && roleName.intValue() < this.app_account){
				logger.info(" query cp user !");
				//用户编号在300000~500000之间为:CP厂商角色
				result = userService.queryCpUser(user);
				//已在数据库直接固定
				roleId = 4;
				//CPID作为查询条件
				user.setQueryRequirement("cpId");
			}else if(roleName.intValue() >= this.app_account){
				logger.info(" query app user !");
				//用户编号在500000~以上为:APP应用角色
				result = userService.queryAppUser(user);
				//已在数据库直接固定
				roleId = 5;
				//APPID作为查询条件
				user.setQueryRequirement("appId");
			}else{
				//其他角色
			}
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
			//用户名和密码验证不通过
			if(result.intValue() == 0 ){
				request.setAttribute("errorMsg", "用户名或密码输入有误！");
				return "login";
			}
			
			List<Menu> configedMenus = roleService.getMenusByRoleId(roleId);
			MenuUtil.loadChildMenu(configedMenus);
			request.getSession().setAttribute("leftMenus", configedMenus);
			request.getSession().setAttribute("loginUser",user);
			return "redirect:/wellcome/execute";
		} catch (Exception e) {
			logger.info("the user has system role !");
			List<User> users = userService.selectByExample(user);
			if(null != users && ! users.isEmpty()){
				List<Menu> configedMenus = userService.getMenusByUserId(users.get(0).getId());
				MenuUtil.loadChildMenu(configedMenus);
				request.getSession().setAttribute("leftMenus", configedMenus);
				request.getSession().setAttribute("loginUser",users.get(0));
				return "redirect:/wellcome/execute";
			}else{
				request.setAttribute("errorMsg", "用户名或密码输入有误！");
				return "login";
			}
		}
		
	}
	
	/**
	 * 加载菜单
	 * @param configedMenus
	 * @param menuTypes
	 * @param menuTypeVOs
	 */
	private void loadMenus(List<Menu> configedMenus,List<MenuType> menuTypes,List<MenuTypeVO> menuTypeVOs){
		for(MenuType menuType : menuTypes){
			
			MenuTypeVO menuTypeVO = new MenuTypeVO();
			menuTypeVO.setDisplayable(false);
			BeanUtils.copyProperties(menuType, menuTypeVO);
			menuTypeVOs.add(menuTypeVO);
			
			List<Menu> menus = menuService.getMenusOfType(menuType.getId());
			List<MenuVO> menuVOs = new ArrayList<MenuVO>();
			menuTypeVO.setMenuvos(menuVOs);
			for(Menu menu : menus){
				MenuVO menuVO = new MenuVO();
				BeanUtils.copyProperties(menu, menuVO);
				menuVOs.add(menuVO);
				
				for(Menu m : configedMenus){
					if(m.getId().equals(menu.getId())){
						menuVO.setChecked(true);
						menuTypeVO.setDisplayable(true);
						break;
					}
				}
			}
		}
	}
	
	/**加载单个用户*/
	@RequestMapping("/{id}")
	public String loadUserById(@PathVariable("id")Integer id,Model model){
		User user = userService.getUserById(id);
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		//处理用户当前所属角色
		List<Role> checkedRoles = userService.getRolesByUserId(id);
		List<Role> roles = roleService.getAllRoles();
		List<RoleVO> roleVos = new ArrayList<RoleVO>();
		for(Role role : roles){
			RoleVO rv = new RoleVO();
			BeanUtils.copyProperties(role, rv);
			for(Role crv : checkedRoles){
				if(rv.getId().equals(crv.getId())){
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
	
	/**更新用户*/
	@ResponseBody
	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	public String update(User user){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		User _user = userService.getUserByLoginName(user.getName());
		if(_user != null && !_user.getId().equals(user.getId()))
			return "{flag:1}";
		//如果用户没有修改密码,那么此处无需再次加密
		if(_user != null && ! _user.getPassword().equals(user.getPassword())){
			user.setPassword(MD5.getMd5Value(user.getPassword()).toUpperCase());
		}
		userService.update(user);
		return "{flag:0}";
	}
	
	/**查询用户列表*/
	@RequestMapping("/list")
	public String list(HttpServletRequest request){
		int currentPage = RequestUtils.getInt(request,"currentPage",1);
		int pageSize = RequestUtils.getInt(request,"pageSize",PaginationVo.DEFAULT_PAGESIZE);
		String name = request.getParameter("name");
		String param_activable = request.getParameter("activable");
		User loginUser = (User)request.getSession().getAttribute("loginUser");
		int activable = 0 ;//default
		if(StringUtils.isNotBlank(param_activable)){
			activable = Integer.parseInt(param_activable);
		}
		UserService.UserCriteria criteria = new UserService.UserCriteria();
		if(loginUser.getName().equals("admin")){
			if(StringUtils.isNotBlank(name)){
				criteria.nameLike(name);
			}
		}else{
			criteria.nameLike(loginUser.getName());
			request.setAttribute("name", loginUser.getName());
		}
		if(activable == 1){
			criteria.activable(1);
		}else if(activable == 2){
			criteria.activable(0);
		}
		PaginationVo<User> result = userService.searchUsers(criteria, currentPage, pageSize);
		request.setAttribute("result",result);
		return "user/list";
	}
	
	/**删除用户*/
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id")String ids,Model model){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		userService.batchDelete(idIntArray);
		return "{success:true}";
	}
	
	/**激活用户*/
	@ResponseBody
	@RequestMapping("/active")
	public String active(@RequestParam("id")String ids,Model model){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		userService.batchActive(idIntArray);
		return "{success:true}";
	}
	
	/**删除用户*/
	@ResponseBody
	@RequestMapping("/disable")
	public String disable(@RequestParam("id")String ids,Model model){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		userService.batchDisable(idIntArray);
		return "{success:true}";
	}
	
	/**新增用户页*/
	@RequestMapping("/add")
	public String showAdd(){
		return "user/add";
	}
	
	/**新增用户*/
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(User user,HttpServletResponse response){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		if(userService.getUserByLoginName(user.getName()) != null){
			return "{flag:1}";
		}
		userService.addNewUser(user);
		return "{flag:0,id:" + user.getId() + "}";
	}

}
