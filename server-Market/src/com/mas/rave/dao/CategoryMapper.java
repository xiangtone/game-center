package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.CategoryExample;

/**
 * app分类数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface CategoryMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(CategoryExample example);
	
	/**
	 * 获取当前总页数
	 */
	int countByExample(Category example);

	/**
	 * 根据id删除对应app分类信息
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * 增加app分类信息
	 */
	int insert(Category record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(Category record);

	/**
	 * 分页查询app分类信息
	 */
	List<Category> selectByExample(CategoryExample example, RowBounds rowBounds);
	
	/**
	 * 分页查询app分类信息
	 */
	List<Category> selectByExample(Category example, RowBounds rowBounds);
	
	/**
	 * 分页查询app分类信息
	 */
	List<Category> selectByExample(CategoryExample example);
	
	/**
	 * 根据name 查询app分类信息
	 * @param name
	 * @return
	 */
	List<Category> getCategorysByName(Category category);
	
	/**
	 * 分页查询app分类信息
	 */
	List<Category> selectByExample(Category example);
	
	/**
	 * 所有二级分类
	 */
	List<Category> getSecondCategorys() ;

	/**
	 * 查找所有一级或二级分类
	 * 
	 * @param fatherId
	 * @return
	 */
	List<Category> getCategorys(String fatherId);

	/**
	 * 根据id查看app分类信息
	 */
	Category selectByPrimaryKey(Integer id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(Category record);
	
	int updateSortByPrimarykey(Category category);

	/**
	 * 根据二级分类查询应用分类 
	 * @param id
	 * @return
	 */
	Category selectByCategory(Integer id);
	
}