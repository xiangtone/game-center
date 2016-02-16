package com.reportforms.template;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("properties")
public class XMLPropertiesTemplate {

	@XStreamAsAttribute
	@XStreamAlias("name")
	private String name;
	
	@XStreamAsAttribute
	@XStreamAlias("title")
	private String title;
	
	@XStreamAsAttribute
	@XStreamAlias("columnsWidth")
	private Integer columnsWidth;
	
	@XStreamAsAttribute
	@XStreamAlias("isSums")
	private Boolean isSums;
	
//	@XStreamAlias("dataType")
//	private String dataType;
//	
//	@XStreamAlias("columnSort")
//	private Integer columnSort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getColumnsWidth() {
		return columnsWidth;
	}

	public void setColumnWidth(Integer columnsWidth) {
		this.columnsWidth = columnsWidth;
	}

	public Boolean getIsSums() {
		return isSums;
	}

	public void setIsSums(Boolean isSums) {
		this.isSums = isSums;
	}

//	public String getDataType() {
//		return dataType;
//	}
//
//	public void setDataType(String dataType) {
//		this.dataType = dataType;
//	}
//
//	public Integer getColumnSort() {
//		return columnSort;
//	}
//
//	public void setColumnSort(Integer columnSort) {
//		this.columnSort = columnSort;
//	}
}
