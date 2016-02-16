package com.reportforms.model;

public class RoleOperation extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 938424656655603828L;

	private Integer id;

    private Role role;

    private Operation operation;

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

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

    
}