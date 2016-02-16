package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Category;

/**
 * app分类信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface CategoryService {
	static class CategoryCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public CategoryCriteria idEqualTo(Integer id) {
			params.put(1, id);
			return this;
		}

		public CategoryCriteria nameLike(String name) {
			params.put(2, name);
			return this;
		}

		public CategoryCriteria fatherIdEqualTo(Integer id) {
			params.put(3, id);
			return this;
		}

		public CategoryCriteria fatherIdValue(Integer id) {
			params.put(4, id);
			return this;
		}
		
		public CategoryCriteria stateEqualTo(boolean state){
			params.put(5, state);
			return this;
		}

		public CategoryCriteria otherValue(String value) {
			params.put(5, value);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示app分类信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<Category> searchCategorys(CategoryCriteria criteria, int currentPage, int pageSize);
	
	public PaginationVo<Category> searchCategorys(Category example, int currentPage, int pageSize);

	// 查找所有一级或二级分类
	public List<Category> getCategorys(Integer fatherId);

	// 获取所有分类信息
	public List<Category> getAllCategorys();

	// 获取所有二级分类信息
	public List<Category> getSecondCategorys();

	// 查看单个app分类信息
	public Category getCategory(int id);
	/**
	 * 根据name查询分类信息
	 * @param name
	 * @return
	 */
	public  List<Category> getCategorysByName(Category category);

	// 增加app分类信息
	public void addCategory(Category category);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Category record);

	// 更新app分类信息
	public void upCategory(Category category);

	// 删除app分类信息
	public void delCategory(Integer id);

	void batchDelete(Integer[] ids);

	public void updateSortByPrimarykey(Category category);
}
