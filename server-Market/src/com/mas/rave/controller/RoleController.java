package com.mas.rave.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.MenuType;
import com.mas.rave.main.vo.Operation;
import com.mas.rave.main.vo.Role;
import com.mas.rave.main.vo.User;
import com.mas.rave.service.MenuService;
import com.mas.rave.service.RoleService;
import com.mas.rave.service.UserService;
import com.mas.rave.util.MenuUtil;
import com.mas.rave.vo.MenuTypeVO;
import com.mas.rave.vo.MenuVO;
import com.mas.rave.vo.OperationTypeVO;
import com.mas.rave.vo.OperationVO;

@Controller
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/add")
	public String toAdd() {
		return "role/add";
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute Role role) {
		if (roleService.getRoleByName(role.getName()) != null) {
			return "{flag:1}";
		}
		role = roleService.addNewRole(role);
		return "{flag:0,id:" + role.getId() + "}";
	}

	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request) {
		RoleService.RoleCriteria criteria = new RoleService.RoleCriteria();
		int currentPage = RequestUtils.getInt(request, "currentPage", 1);
		int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

		PaginationVo<Role> result = roleService.searchRoles(criteria, currentPage, pageSize);
		request.setAttribute("result", result);

		List<Role> roles = roleService.getAllRoles();
		model.addAttribute("roles", roles);
		return "role/list";
	}

	@RequestMapping("/{id}/higher")
	public String heigher(@PathVariable("id") Integer id) {
		roleService.higherRole(id);
		return "redirect:/role/list";
	}

	@RequestMapping("/{id}/lower")
	public String lower(@PathVariable("id") Integer id) {
		roleService.lowerRole(id);
		return "redirect:/role/list";
	}

	@RequestMapping("/{id}")
	public String toEdit(@PathVariable("id") Integer id, Model model) {
		Role role = roleService.getRoleById(id);
		model.addAttribute("role", role);
		return "role/edit";
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(Role role) {
		Role _role = roleService.getRoleByName(role.getName());
		if (_role != null && !_role.getId().equals(role.getId()))
			return "{\"flag\":\"1\"}";
		roleService.updateBaseInfo(role);
		return "{\"flag\":\"0\"}";
	}

	@RequestMapping("/{id}/config")
	public String toConfig(@PathVariable("id") Integer id, Model model) {
		Role role = roleService.getRoleById(id);
		model.addAttribute("role", role);

		// 菜单
		List<Menu> configedMenus = roleService.getMenusByRoleId(id);

		List<MenuType> menuTypes = menuService.getMenuTypes();
		List<MenuTypeVO> menuTypeVOs = new ArrayList<MenuTypeVO>();
		for (MenuType menuType : menuTypes) {

			MenuTypeVO menuTypeVO = new MenuTypeVO();
			BeanUtils.copyProperties(menuType, menuTypeVO);
			menuTypeVOs.add(menuTypeVO);

			List<Menu> menus = menuService.getMenusOfType(menuType.getId());
			List<MenuVO> menuVOs = new ArrayList<MenuVO>();
			menuTypeVO.setMenuvos(menuVOs);
			for (Menu menu : menus) {
				MenuVO menuVO = new MenuVO();
				BeanUtils.copyProperties(menu, menuVO);
				menuVOs.add(menuVO);

				for (Menu m : configedMenus) {
					if (m.getId().equals(menu.getId())) {
						menuVO.setChecked(true);
						break;
					}
				}
			}
		}
		model.addAttribute("menuTypes", menuTypeVOs);

		// 操作权限
		List<Operation> checkedOperations = roleService.getOperationsByRoleId(id);
		List<Operation> operations = roleService.getAllOperations();
		List<OperationTypeVO> roleVos = new ArrayList<OperationTypeVO>();
		for (Operation operation : operations) {
			OperationVO ovo = new OperationVO();
			BeanUtils.copyProperties(operation, ovo);
			for (Operation crv : checkedOperations) {
				if (ovo.getId().equals(crv.getId())) {
					ovo.setChecked(true);
					break;
				}
			}

			OperationTypeVO gvo = new OperationTypeVO();
			gvo.setId(operation.getType().getId());
			gvo.setName(operation.getType().getName());

			if (roleVos.contains(gvo)) {
				roleVos.get(roleVos.indexOf(gvo)).getVos().add(ovo);
			} else {
				List<OperationVO> vos = new ArrayList<OperationVO>();
				vos.add(ovo);
				gvo.setVos(vos);
				roleVos.add(gvo);
			}

		}
		model.addAttribute("roleVos", roleVos);

		return "role/config";
	}

	@RequestMapping(value = "/{id}/config", method = RequestMethod.POST)
	@ResponseBody
	public String doConfig(@PathVariable("id") Integer id, @RequestParam(value = "menus", required = false) List<Integer> menus,
			@RequestParam(value = "operations", required = false) List<Integer> operations,HttpServletRequest request) {
		roleService.resetRoleMenuRelationship(id, menus);
		roleService.resetRoleOperationRelationship(id, operations);
		User user = (User)request.getSession().getAttribute("loginUser");
		if(null != user){
			List<Menu> menu_List = userService.getMenusByUserId(user.getId());
			String menuStr = MenuUtil.parseMenus(menu_List, request);
			request.getSession().setAttribute("menus", menuStr);
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除角色 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		boolean bol = roleService.batchDelete(idIntArray);
		if (bol) {
			// 该角色有用户存在不能删掉
			return "{\"success\":\"false\"}";
		} else {
			return "{\"success\":\"true\"}";
		}

	}
}
