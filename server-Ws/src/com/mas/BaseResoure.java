package com.mas;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TRaveCountry;
import com.mas.market.service.TRaveCountryService;
import com.mas.util.AddressUtils;
import com.mas.util.DateUtil;

public class BaseResoure 
{
	protected final Logger log = LoggerFactory.getLogger(getClass());
	@Context
	private MessageContext context;
	@Autowired
	protected TRaveCountryService tRaveCountryService;
	protected Integer getAutoCountry(Integer raveId){
		if(raveId==0){
				return getAutoRaveId(getRequest());
		}
		return raveId;
	}
	protected Integer getAutoRaveId(HttpServletRequest request){
		String ip = AddressUtils.getClientIp(request);
		try {
			String[] address = AddressUtils.getAddresses(ip);
			if(null!=address){
				Criteria example = new Criteria();
				example.put("state", true);
				List<TRaveCountry> raveCountryList = tRaveCountryService.selectByExample(example);
				for(TRaveCountry country:raveCountryList){
					if(address[0].trim().equals(country.getNameCn().trim())){
						return country.getId();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	
	protected HttpServletRequest getRequest(){
		HttpServletRequest request = (HttpServletRequest) context.get(AbstractHTTPDestination.HTTP_REQUEST);
		return request;
	}
	
	protected HttpServletResponse getResponse()
	{
		HttpServletResponse response = (HttpServletResponse) context.get(AbstractHTTPDestination.HTTP_RESPONSE);
		return response;
	}
	
	public int getStartIndex(Integer pageSize,Integer pageNum) {
		Integer startIndex = 0;
		// 当前页为空,起始下标为0
		if (pageNum.intValue() <= 0) {
			startIndex=0;
		} else {// 根据当前页和每页大小得到当前起始下标
			startIndex = (pageNum - 1) * pageSize;
		}
		return startIndex;
	}
	
	 /**
     * 获取所有参数
     * @return
     */
    protected Map<String,String[]> getParams(){
        HttpServletRequest request = this.getRequest();
        return request.getParameterMap();
    }
    /**
     * 获取指定的配置
     * @param name
     * @return
     */
    protected String getParam(String name){
        return getParam(name, "");
    }
    /**
     * 根据参数名称获取参数值，如果没有找到则以默认值返回
     * @param name
     * @param defaultValue
     * @return
     */
    protected String getParam(String name, String defaultValue){
        HttpServletRequest request = this.getRequest();
        String strValue = request.getParameter(name);
        return strValue == null ? defaultValue : strValue;
    }
    /**
     * 获取整形的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected int getIntParam(String name){
        return getParam(name, 0);
    }
    /**
     * 获取整形的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected int getParam(String name, Integer defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Integer.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取长整形的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected long getLongParam(String name){
        return getParam(name, 0L);
    }
    /**
     * 获取长整形的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected long getParam(String name, Long defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Long.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取单精度的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected float getFloatParam(String name){
        return getParam(name, 0F);
    }
    /**
     * 获取单精度的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected float getParam(String name, Float defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Float.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取双精度的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected double getDoubleParam(String name){
        return getParam(name, 0D);
    }
    /**
     * 获取双精度的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected double getParam(String name, Double defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Double.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取字节的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected byte getByteParam(String name){
        return getParam(name, (byte)0);
    }
    /**
     * 获取字节的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected byte getParam(String name, Byte defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Byte.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取字节的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected short getShortParam(String name){
        return getParam(name, (short)0);
    }
    /**
     * 获取字节的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected short getParam(String name, Short defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Short.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /**
     * 获取布尔的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected boolean getBooleanParam(String name){
        return getParam(name, false);
    }
    /**
     * 获取布尔的参数值
     * @param name
     * @param defaultValue
     * @return
     */
    protected boolean getParam(String name, Boolean defaultValue){
        String strValue = getParam(name, defaultValue.toString());
        try{
            return Boolean.valueOf(strValue);
        }
        catch(Exception e){
            return defaultValue;
        }
    }
    /*********************获取访问参数*******************/
    /*******************操作Cookie********************/
    /**
     * 获取指定键的Cookie
     * @param cookieName
     * @return 如果找到Cookie则返回 否则返回null
     */
    protected Cookie getCookie(String cookieName){
        if (StringUtils.isEmpty(cookieName) || this.getRequest().getCookies() == null)
            return null;
        for(Cookie cookie : this.getRequest().getCookies()){
            if (cookieName.equals(cookie.getName()))
                return cookie;
        }
        return null; 
    }
    /**
     * 获取指定键的Cookie值
     * @param cookieName
     * @return 如果找到Cookie则返回 否则返回null
     */
    protected String getCookieValue(String cookieName){
        Cookie cookie = this.getCookie(cookieName);
        return cookie == null ? null : cookie.getValue();
    }
    /**
     * 删除指定的Cookie
     * @param cookieName
     */
    protected void removeCookie(String cookieName){
        HttpServletResponse response = this.getResponse();
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    /**
     * 保存一个对象到Cookie里   Cookie只在会话内有效
     * @param cookieName
     * @param inst
     */
    protected void setCookie(String cookieName, Object inst){
        this.setCookie(cookieName, "/", inst);
    }
    /**
     * 保存一个对象到Cookie  Cookie只在会话内有效
     * @param cookieName
     * @param path
     * @param inst
     */
    protected void setCookie(String cookieName, String path, Object inst){
        if (StringUtils.isEmpty(cookieName) || inst == null)
            return;
        String strCookieString = this.object2CookieString(inst);
        this.setCookie(cookieName, path, strCookieString);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param inst
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, Object inst, int expiry){
        this.setCookie(cookieName, "/", inst, expiry);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param path
     * @param inst
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, String path, Object inst, int expiry){
        if (StringUtils.isEmpty(cookieName) || inst == null || expiry < 0)
            return;
        String strCookieString = this.object2CookieString(inst);
        this.setCookie(cookieName, path, strCookieString, expiry);
    }
    /**
     * 保存一个对象到Cookie里   Cookie只在会话内有效
     * @param cookieName
     * @param cookieValue
     */
    protected void setCookie(String cookieName, String cookieValue){
        this.setCookie(cookieName, "/", cookieValue);
    }
    /**
     * 保存一个对象到Cookie  Cookie只在会话内有效
     * @param cookieName
     * @param path
     * @param cookieValue
     */
    protected void setCookie(String cookieName, String path, String cookieValue){
        HttpServletResponse response = this.getResponse();
        if (StringUtils.isEmpty(cookieName) || cookieValue == null)
            return;
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (!StringUtils.isEmpty(path)){
            cookie.setPath(path);
        }
        response.addCookie(cookie);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param cookieValue
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, String cookieValue, int expiry){
        this.setCookie(cookieName, "/", cookieValue, expiry);
    }
    /**
     * 保存一个对象到Cookie
     * @param cookieName
     * @param path
     * @param cookieValue
     * @param expiry (秒)设置Cookie的有效时长， 负数不保存，0删除该Cookie 
     */
    protected void setCookie(String cookieName, String path, String cookieValue, int expiry){
        if (StringUtils.isEmpty(cookieName) || cookieValue == null || expiry < 0)
            return;
        HttpServletResponse response = this.getResponse();
        if (StringUtils.isEmpty(cookieName) || cookieValue == null)
            return;
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (!StringUtils.isEmpty(path)){
            cookie.setPath(path);
        }
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }
    
    /**
     * 把对象转换为Cookie存贮字串
     * @param inst
     * @return
     */
    private String object2CookieString(Object inst){
        if (inst == null)
            return "";
        StringBuilder strCookieValue = new StringBuilder();
        for(java.lang.reflect.Field field : inst.getClass().getDeclaredFields()){
            try{
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) || 
                        java.lang.reflect.Modifier.isFinal(field.getModifiers())){
                    continue;
                }    
                if (!this.isSimpleProperty(field.getType())) continue;//不是元数据
                field.setAccessible(true);// 提升权限
                Object objValue = field.get(inst);
                String strValue;
                if (field.getType().equals(Date.class)){
                    strValue = DateUtil.formatDate((Date)objValue, DateUtil.DATE_FORMAT_SECOND);
                }else{
                    strValue = objValue == null ? "" : objValue.toString();
                }
                if (strCookieValue.length() > 0){
                    strCookieValue.append(String.format("&%s=%s", field.getName(), URLEncoder.encode(strValue,"UTF-8")));
                }
                else{
                    strCookieValue.append(String.format("%s=%s", field.getName(), URLEncoder.encode(strValue,"UTF-8")));
                }    
            }
            catch(Exception e){
                continue;
            }
        }
        return strCookieValue.toString();
    }
    
    /**
     * 是否是简单的数据类型
     * @param type
     * @return
     */
    private boolean isSimpleProperty(Class<?> propType){
        if (!propType.isPrimitive() && !propType.isEnum() && (!propType.equals(String.class) && !propType.equals(Date.class)))
        {
            return false;
        }
        return true;
    }
    /*******************操作Cookie********************/
}