package com.reportforms.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author tony.li
 *
 * @param <T> 数据元类型
 */
public class PaginationVo<T> implements Serializable{
	
	public static int DEFAULT_PAGESIZE= 10;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5829002584489096420L;
	/**数据*/
	private List<T> data;
	/**记录总数*/
	private int recordCount;
	/**每页大小*/
	private int pageSize;
	/**当前页标*/
	private int currentPage;
	/**最大页数下标*/
	private int maxPage;
	
	/**
	 * 
	 * @param data 数据
	 * @param recordCount 记录总数
	 * @param pageSize 每页大小
	 * @param currentPage 当前页标
	 */
	public PaginationVo(List<T> data,int recordCount,int pageSize,int currentPage){
		if(pageSize < 1 || recordCount < 0 || recordCount > Integer.MAX_VALUE)
			throw new IllegalArgumentException("pageSize必需大于1,recordCount必需大于或等0");
		this.recordCount = recordCount;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.maxPage = (recordCount + pageSize - 1) / pageSize;
		this.data = data;
	}
	/**
	 * 下一页下标
	 * @return
	 */
	public int getNextPage(){
		return (this.currentPage + 1) > this.maxPage ? this.maxPage : (this.currentPage + 1);
	}
	/**
	 * 上一页下标
	 * @return
	 */
	public int getPrePage(){
		return (this.currentPage - 1) < 1 ? 1 : (this.currentPage - 1);
	}
	/**
	 * 总页数
	 * @return
	 */
	public int getPages(){
		return this.maxPage;
	}
	/**
	 * 是否还有下一页
	 * @return true 有下一页
	 */
	public boolean isHasNextPage(){
		return (this.currentPage + 1) <= this.maxPage ? true : false;
	}

	/**
	 * 是否还有上一页
	 * @return true 有上一页
	 */
	public boolean isHasPrePage(){
		return (this.currentPage - 1) > 0 ? true : false;
	}
	public List<T> getData() {
		return data;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	
	
}
