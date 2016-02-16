package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.ImageAlbumResMapper;
import com.mas.rave.dao.ImageAlbumThemeMapper;
import com.mas.rave.main.vo.ImageAlbumRes;
import com.mas.rave.main.vo.ImageAlbumTheme;
import com.mas.rave.main.vo.ImageAlbumThemeExample;
import com.mas.rave.service.ImageAlbumThemeService;
import com.mas.rave.main.vo.ImageAlbumThemeExample.Criteria;
import com.mas.rave.util.FileUtil;
@Service
public class ImageAlbumThemeServiceImpl implements ImageAlbumThemeService {
	@Autowired
	private ImageAlbumThemeMapper imageAlbumThemeMapper;
	

	@Autowired
	private ImageAlbumResMapper<ImageAlbumRes> imageAlbumResMapper;
	@Override
	public PaginationVo<ImageAlbumTheme> searchImageAlbumThemes(ImageAlbumThemeCriteria criteria,
			int currentPage, int pageSize) {
		ImageAlbumThemeExample example = new ImageAlbumThemeExample();
		Map<Integer, Object> params = criteria.getParams();
		Criteria criteria1  = example.createCriteria();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				criteria1.andNameLike(params.get(1).toString());
			}else if(key.equals(2)){
				criteria1.andNameCnLike(params.get(2).toString());

			}			
			if(key.equals(4)){
				criteria1.andRaveIdEqual(Integer.parseInt(params.get(4).toString()));
			}
		}
		example.setOrderByClause("createTime desc");
		List<ImageAlbumTheme> data = imageAlbumThemeMapper.selectByExample(example,
				new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = imageAlbumThemeMapper.countByExample(example);
		PaginationVo<ImageAlbumTheme> result = new PaginationVo<ImageAlbumTheme>(
				data, recordCount, pageSize, currentPage);
		return result;
	}
	@Override
	public void addImageAlbumTheme(ImageAlbumTheme imageAlbumTheme) {
		imageAlbumThemeMapper.insert(imageAlbumTheme);
	}

	@Override
	public List<ImageAlbumTheme> searchImageAlbumTheme(ImageAlbumThemeExample example) {
		// TODO Auto-generated method stub
		return imageAlbumThemeMapper.selectByExample(example);
	}

	@Override
	public ImageAlbumTheme getImageAlbumTheme(long id) {
		// TODO Auto-generated method stub
		return imageAlbumThemeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ImageAlbumTheme> getAllImageAlbumThemes() {
		// TODO Auto-generated method stub
		return imageAlbumThemeMapper.getAllImageAlbumThemes();
	}

	@Override
	public void upImageAlbumTheme(ImageAlbumTheme imageAlbumTheme) {
		imageAlbumThemeMapper.updateByPrimaryKey(imageAlbumTheme);

	}

	@Override
	public void delImageAlbumTheme(Integer id) {
	
		ImageAlbumTheme imageAlbumTheme = imageAlbumThemeMapper
						.selectByPrimaryKey(id);
				String bigUrl = null;
				String smallUrl = null;
				if (imageAlbumTheme != null) {
					//删除分发数据		
					 List<ImageAlbumRes> imageAlbumRes = imageAlbumResMapper.selectByThemeId(id);
					 if(imageAlbumRes!=null){
						 for(ImageAlbumRes imageres:imageAlbumRes){
									PaginationBean params = new PaginationBean();
									params.getParams().put("ids", Integer.toString(imageres.getId()));
									imageAlbumResMapper.deleteById(params);		 
						 }
	 
					 }
					 if(imageAlbumTheme.isState()==true){
						 imageAlbumTheme.setState(false);
						 imageAlbumThemeMapper.updateByPrimaryKey(imageAlbumTheme);
					 }else{
							// 设置删除文件路径
							
							bigUrl = imageAlbumTheme.getBigicon();
							smallUrl = imageAlbumTheme.getIcon();
							imageAlbumThemeMapper.deleteByPrimaryKey(id);
							try {
								// 删除对应文件
								FileUtil.deleteFile(bigUrl);
								FileUtil.deleteFile(smallUrl);
							} catch (IOException e) {
								e.printStackTrace();
							}
					 }

				}
		

	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delImageAlbumTheme(id);
		}

	}

	@Override
	public void updateSortByPrimarykey(ImageAlbumTheme imageAlbumTheme) {
		imageAlbumThemeMapper.updateSortByPrimarykey(imageAlbumTheme);

	}
	@Override
	public List<ImageAlbumTheme> selectByThemeName(ImageAlbumTheme record) {
		// TODO Auto-generated method stub
		return imageAlbumThemeMapper.selectByThemeName(record);
	}
	@Override
	public List<ImageAlbumTheme> selectByThemeNameCn(String nameCn) {
		// TODO Auto-generated method stub
		return imageAlbumThemeMapper.selectByThemeNameCn(nameCn);
	}

}
