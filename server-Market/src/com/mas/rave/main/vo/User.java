package com.mas.rave.main.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6736119433045312774L;

	private Integer id;

    private String name;

    private String password;

    private Boolean activable;

    private String mobile;

    private String email;

    private Date insertDate;
    
    private List<Role> roles = new ArrayList<Role>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActivable() {
		return activable;
	}

	public void setActivable(Boolean activable) {
		this.activable = activable;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
    
    
}