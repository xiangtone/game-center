package com.mas.rave.common.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.mas.rave.common.page.PaginationVo;



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
		String url = contextPath;
		String queryString = request.getQueryString();
		if(StringUtils.isBlank(queryString)){
			queryString = "";
		}else{
			queryString ="?"+ queryString;
			if(!queryString.endsWith("&")){
				queryString += "&";
			}
		}
		
		
//		queryString = queryString.replaceAll("pageSize=.*&", "");
		queryString = queryString.replaceAll("currentPage=.*&", "");//页数去掉 20140827
		queryString = queryString.replaceAll("page=.*&", "");//页数去掉 20140827
		queryString = queryString.replaceAll("params=.*&", "");
//		queryString += "pageSize=%1$s&";
//		queryString += "currentPage=%2$s&";
		queryString += params;
		if("?".equals(queryString)){//空参数时
			queryString="";
		}
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<form id='paginationForm' method='post' action='" + url+queryString + "'>");
			sb.append("第").append(paginationVo.getCurrentPage()).append("/").append(paginationVo.getPages())
			.append("页&nbsp;共").append(paginationVo.getRecordCount()).append("条");
			if(paginationVo.isHasPrePage()){
				linkUrl(sb,1,"首页");
				linkUrl(sb,paginationVo.getPrePage(),"上页");
			}else{
				sb.append("<button type=\"button\" id=\"homePage\" class=\"butsearch\" disabled>首页</button>&nbsp;");
				sb.append("<button type=\"button\" id=\"prePage\" class=\"butsearch\" disabled>上页</button>&nbsp;");
			}
			if(paginationVo.isHasNextPage()){
				linkUrl(sb,paginationVo.getNextPage(),"下页");
				linkUrl(sb,paginationVo.getPages(),"末页");
			}else{
				sb.append("<button type=\"button\" id=\"nextPage\" class=\"butsearch\" disabled>下页</button>&nbsp;");
				sb.append("<button type=\"button\" id=\"lastPage\" class=\"butsearch\" disabled>末页</button>&nbsp;");
			}
			linkUrl(sb,paginationVo.getCurrentPage(),"跳转");
			sb.append("<input type='hidden' id='pageSize' name='pageSize' value='");
			sb.append(paginationVo.getPageSize());
			sb.append("'/>");
			sb.append("<input type='hidden' name='maxPage' value='").append(paginationVo.getPages()).append("' />");
			sb.append("<input type='text' class='z_text' id='currentPage' name='currentPage' maxlength='9' style='width:30px;' value='");
			sb.append(paginationVo.getCurrentPage());
			sb.append("' />");
			sb.append("</form>");
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return TagSupport.SKIP_BODY;
	}

	private void linkUrl(StringBuilder sb,int pageIndex,String buttonName) {
	    sb.append("<button type=\"submit\" class=\"butsearch\" ");

		if(buttonName.equals("跳转")){
			sb.append(" onmouseover=\"$(this).attr('class', 'butsubmit')\" onmouseout=\"$(this).attr('class', 'butsearch')\"");
			
		}else{
			sb.append(" onmouseover=\"$(this).attr('class', 'butsubmit')\" onmouseout=\"$(this).attr('class', 'butsearch')\" onclick=\"document.getElementById('currentPage').value='")
			.append(pageIndex);		
		}		
		   sb.append("'\">").append(buttonName).append("</button>&nbsp;");
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
		String queryString = "pageSize=23&1111";
		String a = queryString.replaceAll("pageSize=.*&", "##");
		System.out.println(a);
//		a = a.replaceAll("CurrentPage=.*&", "&");
//		System.out.println(a);
	}
}
