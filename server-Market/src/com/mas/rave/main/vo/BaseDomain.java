package com.mas.rave.main.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class BaseDomain implements Serializable{

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
