package com.reportforms.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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

import com.reportforms.common.MyCollectionUtils;
//import com.reportforms.datasource.DataSourceInstances;
//import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.Menu;
import com.reportforms.model.MenuType;
import com.reportforms.model.Operation;
import com.reportforms.model.Role;
import com.reportforms.model.User;
import com.reportforms.service.MenuService;
import com.reportforms.service.RoleService;
import com.reportforms.service.UserService;
import com.reportforms.util.MenuUtil;
import com.reportforms.vo.MenuTypeVO;
import com.reportforms.vo.MenuVO;
import com.reportforms.vo.OperationTypeVO;
import com.reportforms.vo.OperationVO;


@Controller
@RequestMapping("/role")

public class RoleAction {
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/add")
	public String toAdd(){
		
		return "role/add";
	}
	
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String toAdd(@ModelAttribute Role role){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		if(roleService.getRoleByName(role.getName()) != null){
			return "{flag:1}";
		}
		role = roleService.addNewRole(role);
		return "{flag:0,id:" + role.getId() + "}";
	}
	
	@RequestMapping("/list")
	public String list(Model model){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		List<Role> roles = roleService.getAllRoles();
		model.addAttribute("roles", roles);
		return "role/list";
	}
	
	@RequestMapping("/{id}/higher")
	public String heigher(@PathVariable("id") Integer id){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		roleService.higherRole(id);
		return "redirect:/role/list";
	}
	
	@RequestMapping("/{id}/lower")
	public String lower(@PathVariable("id") Integer id){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		roleService.lowerRole(id);
		return "redirect:/role/list";
	}
	
	@RequestMapping("/{id}")
	public String toEdit(@PathVariable("id") Integer id,Model model){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		Role role = roleService.getRoleById(id);
		model.addAttribute("role", role);
		return "role/edit";
	}
	
	@ResponseBody
	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	public String update(Role role){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		Role _role = roleService.getRoleByName(role.getName());
		if(_role != null && !_role.getId().equals(role.getId()))
			return "{flag:1}";
		roleService.updateBaseInfo(role);
		return "{flag:0}";
	}
	
	@RequestMapping("/{id}/config")
	public String toConfig(@PathVariable("id") Integer id,Model model){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		Role role = roleService.getRoleById(id);
		model.addAttribute("role", role);
		
		//菜单
		List<Menu> configedMenus = roleService.getMenusByRoleId(id);
		
		List<Menu> allMenus = menuService.queryAll(new Menu());
		
		for (Menu menu1 : allMenus) {
			for(Menu menu2 : configedMenus){
				if(menu2.getId() == menu1.getId()){
					menu1.setChecked(true);
				}
			}
		}
		MenuUtil.loadChildMenu(allMenus);
		model.addAttribute("allMenus", allMenus);
		
		//操作权限
		List<Operation> checkedOperations = roleService.getOperationsByRoleId(id);
		List<Operation> operations = roleService.getAllOperations();
		List<OperationTypeVO> roleVos = new ArrayList<OperationTypeVO>();
		for(Operation operation : operations){
			OperationVO ovo = new OperationVO();
			BeanUtils.copyProperties(operation, ovo);
			for(Operation crv : checkedOperations){
				if(ovo.getId().equals(crv.getId())){
					ovo.setChecked(true);
					break;
				}
			}
			
			OperationTypeVO gvo = new OperationTypeVO();
			gvo.setId(operation.getType().getId());
			gvo.setName(operation.getType().getName());
			
			if(roleVos.contains(gvo)){
				roleVos.get(roleVos.indexOf(gvo)).getVos().add(ovo);
			}else{
				List<OperationVO> vos = new ArrayList<OperationVO>();
				vos.add(ovo);
				gvo.setVos(vos);
				roleVos.add(gvo);
			}
			
		}
		model.addAttribute("roleVos", roleVos);
		
		return "role/config";
	}
	
	
	@RequestMapping(value="/{id}/config",method=RequestMethod.POST)
	@ResponseBody
	public String doConfig(@PathVariable("id") Integer id,@RequestParam(value="menus",required=false) List<Integer> menus,@RequestParam(value="operations",required=false) List<Integer> operations,HttpServletRequest request){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		roleService.resetRoleMenuRelationship(id, menus);
		roleService.resetRoleOperationRelationship(id, operations);
		User user = (User)request.getSession().getAttribute("loginUser");
		if(null != user){
			List<Menu> list = userService.getMenusByUserId(user.getId());
			if(!CollectionUtils.isEmpty(list)){
				MenuUtil.loadChildMenu(list);
				request.getSession().setAttribute("leftMenus", list);
			}
		}
		return "{flag:0}";
	}
	
	/**删除角色*/
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id")String ids,Model model){
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL1);
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		List<User> list = roleService.getUsersByRoleId(idIntArray[0]);
		if(null != list && !list.isEmpty()){
			return "{success:false}";
		}
		roleService.batchDelete(idIntArray);
		return "{success:true}";
	}
}
