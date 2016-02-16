package com.mas.client;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.mas.util.XorPlus;

public class BaseTestResource
{
	protected XorPlus xorPlus=new XorPlus();
	
	@Autowired
	protected WebClient client;

	
	public WebClient getClient(String path)
	{
		client.back(true).path(path).type(MediaType.APPLICATION_JSON).accept("application/json")
				.encoding("UTF-8");
				//.query("UID", 2);
		return client;
	}
	
	public WebClient getClient(String path,String mediaType)
	{
		client.back(true).path(path).type(mediaType).accept("application/json").encoding("UTF-8");
		//.query("UID", 2);
		return client;
	}
	
	protected String getXorData(Object request) {
		String js=JSON.toJSONString(request);
		return XorPlus.encrypt(js);
	}
	
}
