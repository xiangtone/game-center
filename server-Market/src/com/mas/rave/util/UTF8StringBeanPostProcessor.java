package com.mas.rave.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

public class UTF8StringBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		// TODO Auto-generated method stub
		if(bean instanceof StringHttpMessageConverter){
			MediaType type = new MediaType("text","plain", Charset.forName("UTF-8"));
			List<MediaType> list = new ArrayList<>();
			list.add(type);
			((StringHttpMessageConverter)bean).setSupportedMediaTypes(list);
		}
		
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		// TODO Auto-generated method stub
		return bean;
	}

}
