package com.reportforms.model;

public class RoleMenu extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6996086020155412241L;

	private Integer id;

    private Role role;

    private Menu menu;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
    
    

}