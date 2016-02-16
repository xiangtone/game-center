package com.mas.rave.report;

import java.util.List;

import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@Component("modelTemplate")
@XStreamAlias("model")
public class XMLModelTemplate {

	@XStreamAsAttribute
	@XStreamAlias("id")
	private String id;
	
	@XStreamAsAttribute
	@XStreamAlias("title")
	private String title;
	
	@XStreamAsAttribute
	@XStreamAlias("serviceName")
	private String serviceName;
	
	@XStreamAsAttribute
	@XStreamAlias("method")
	private String methodName;
	
	@XStreamAsAttribute
	@XStreamAlias("countsName")
	private String countsName;
	
	@XStreamImplicit
	private List<XMLPropertiesTemplate> props;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getCountsName() {
		return countsName;
	}

	public void setCountsName(String countsName) {
		this.countsName = countsName;
	}

	public List<XMLPropertiesTemplate> getProps() {
		return props;
	}

	public void setProps(List<XMLPropertiesTemplate> props) {
		this.props = props;
	}
}
