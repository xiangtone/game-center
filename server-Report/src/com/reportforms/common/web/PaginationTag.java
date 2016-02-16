package com.reportforms.common.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.reportforms.common.page.PaginationVo;



public class PaginationTag extends TagSupport{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4580965915826791063L;

	private PaginationVo<? extends Object> paginationVo;
	private String contextPath;
	private String params;
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		String url = contextPath + "?";
		String queryString = request.getQueryString();
		
		if(StringUtils.isBlank(queryString)){
			queryString = "";
		}else if(!queryString.endsWith("&")){
			queryString += "&";
		}
		
		queryString = queryString.replaceAll("pageSize=.*&", "");
		queryString = queryString.replaceAll("currentPage=.*&", "");
		queryString = queryString.replaceAll("params=.*&", "");
		queryString += "pageSize=%1$s&";
		queryString += "currentPage=%2$s&";
		queryString += params;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("第").append(paginationVo.getCurrentPage()).append("/").append(paginationVo.getPages())
			.append("页&nbsp;共").append(paginationVo.getRecordCount()).append("条");
			if(paginationVo.isHasPrePage()){
				linkUrl(url, queryString, sb,1,"首页");
				linkUrl(url, queryString, sb,paginationVo.getPrePage(),"上页");
			}else{
				sb.append("<button type=\"button\" class=\"butreset\" disabled>首页</button>&nbsp;");
				sb.append("<button type=\"button\" class=\"butreset\" disabled>上页</button>&nbsp;");
			}
			if(paginationVo.isHasNextPage()){
				linkUrl(url, queryString, sb,paginationVo.getNextPage(),"下页");
				linkUrl(url, queryString, sb,paginationVo.getPages(),"末页");
			}else{
				sb.append("<button type=\"button\" class=\"butreset\" disabled>下页</button>&nbsp;");
				sb.append("<button type=\"button\" class=\"butreset\" disabled>末页</button>&nbsp;");
			}
			
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return TagSupport.SKIP_BODY;
	}

	private void linkUrl(String url, String queryString, StringBuilder sb,int pageIndex,String buttonName) {
		sb.append("<button type=\"button\" class=\"butsearch\" onmouseover=\"$(this).attr('class', 'butsubmit')\" onmouseout=\"$(this).attr('class', 'butsearch')\" onclick=\"window.location.href='")
		.append(url).append(String.format(queryString, paginationVo.getPageSize(),pageIndex))
		.append("'\">").append(buttonName).append("</button>&nbsp;");
	}

	public void setPaginationVo(PaginationVo<? extends Object> paginationVo) {
		this.paginationVo = paginationVo;
	}
	
	
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public void setParams(String params) {
		this.params = params;
	}

	public static void main(String[] args){
		String queryString = "pageSize=23&CurrentPage=4&a=b";
		String a = queryString.replaceAll("pageSize=.*&", "&");
		a = a.replaceAll("CurrentPage=.*&", "&");
		System.out.println(a);
	}
}
