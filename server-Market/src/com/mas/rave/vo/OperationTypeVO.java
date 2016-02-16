package com.mas.rave.vo;

import java.util.List;

import com.mas.rave.main.vo.OperationType;


public class OperationTypeVO extends OperationType{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5400203668886946531L;
	private List<OperationVO> vos;

	public List<OperationVO> getVos() {
		return vos;
	}

	public void setVos(List<OperationVO> vos) {
		this.vos = vos;
	}

	@Override
	public int hashCode(){
		return this.getId();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == this)
			return true;
		if(obj == null || !(obj instanceof OperationTypeVO)){
			return false;
		}
		return ((OperationTypeVO)obj).getId().equals(this.getId());
	}
	
}
