package com.mas.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper{
	
	@SuppressWarnings("deprecation")
	public CustomObjectMapper(){
	  super();
//	  //开启此参数后，Jackson转换JSON时会生成RootElement
//	  //需要配合@JsonRootName使用。
//	  //这里的配置根据实际情况增减。
//	  this.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
//	  this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE,true);
	  //设置输入:禁止把POJO中值为null的字段映射到json字符串中
	  this.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS,false);
	  this.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,false);
	  //设置输入:禁止把POJO中值为null的字段映射到json字符串中
	  this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,false);
	  //这里的配置根据实际情况增减。
	  this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	//设置输出:包含的属性不能为空  
	  this.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);
	  this.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,true);
	  // or (for older versions):
	  this.setSerializationInclusion(Include.NON_NULL);
	  this.setSerializationInclusion(Include.NON_EMPTY);
	 }
}