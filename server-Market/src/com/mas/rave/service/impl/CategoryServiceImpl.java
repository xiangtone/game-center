package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.CategoryMapper;
import com.mas.rave.dao.MarketMapper;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.CategoryExample;
import com.mas.rave.service.CategoryService;

/**
 * app对应分类
 * 
 * @author liwei.sz
 * 
 */

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private MarketMapper marketMapper;

	public PaginationVo<Category> searchCategorys(CategoryCriteria criteria, int currentPage, int pageSize) {
		CategoryExample example = new CategoryExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				example.createCriteria().andOrValue(Integer.parseInt(params.get(key).toString()));
			} else if (key.equals(2)) {
				// 根据名字查找
				example.createCriteria().andNameLike(params.get(key).toString(), 1);
			} else if (key.equals(3)) {
				// 查看子级菜单
				example.createCriteria().andFatherIdEqualTo(Integer.parseInt(params.get(key).toString()), 1);
			} else if (key.equals(4)) {
				// 查看非子级菜单分类
				example.createCriteria().andFatherIdEqualTo(1, 2);
			}else if (key.equals(5)) {
				// 名字和分类查找
				String[] st = params.get(key).toString().split(",");
				example.createCriteria().andOhter(st[1], Integer.parseInt(st[0]));
			}else if(key.equals(6)){
				//查看状态
				example.createCriteria().andStateEqualTo(Boolean.parseBoolean(params.get(key).toString()));
			} 
		}
		example.setOrderByClause("createTime desc");
		List<Category> data = categoryMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = categoryMapper.countByExample(example);
		PaginationVo<Category> result = new PaginationVo<Category>(data, recordCount, pageSize, currentPage);
		return result;

	}

	// 查找所有一级或二级分类
	public List<Category> getCategorys(Integer fatherId) {
		//CategoryExample example = new CategoryExample();
		//example.createCriteria().andFatherIdEqualTo(fatherId, 1);
		Category example = new Category();
		example.setFatherId(fatherId);
		example.setState(true);//设置默认状态为是,即true
		List<Category> result = categoryMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 获取所有二级分类信息
	public List<Category> getSecondCategorys() {
		List<Category> result = categoryMapper.getSecondCategorys();
		if (CollectionUtils.isEmpty(result))
			return null;
		else
			return result;
	}

	// 查看单个app分类信息
	public Category getCategory(int id) {
		return categoryMapper.selectByPrimaryKey(id);
	}

	// 增加app分类信息
	public void addCategory(Category category) {
		categoryMapper.insert(category);
	}

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Category record) {
		return categoryMapper.insertSelective(record);
	}

	// 更新app分类信息
	public void upCategory(Category category) {
		categoryMapper.updateByPrimaryKey(category);
	}

	// 删除app分类信息
	public void delCategory(Integer id) {
		categoryMapper.deleteByPrimaryKey(id);
	}

	// 同时删除多个app分类
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			categoryMapper.deleteByPrimaryKey(id);
		}
	}

	// 获取所有分类信息
	public List<Category> getAllCategorys() {
		Category example = new Category();
		return categoryMapper.selectByExample(example);
	}

	@Override
	public void updateSortByPrimarykey(Category category) {
		// TODO Auto-generated method stub
		categoryMapper.updateSortByPrimarykey(category);
	}

	@Override
	public PaginationVo<Category> searchCategorys(Category example,
			int currentPage, int pageSize) {
		List<Category> data = categoryMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));
		int recordCount = categoryMapper.countByExample(example);
		PaginationVo<Category> result = new PaginationVo<Category>(data, recordCount, pageSize, currentPage);
		return result;
	}
	@Override
	public  List<Category> getCategorysByName(Category category){
	
		List<Category> data = categoryMapper.getCategorysByName(category);
		return data;
	}
}
