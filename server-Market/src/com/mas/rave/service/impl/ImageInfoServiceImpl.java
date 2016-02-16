package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.CategoryMapper;
import com.mas.rave.dao.ImageAlbumResMapper;
import com.mas.rave.dao.ImageInfoMapper;
import com.mas.rave.dao.SearchKeywordMapper;
import com.mas.rave.dao.SearchKeywordResListMapper;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.ImageAlbumRes;
import com.mas.rave.main.vo.ImageInfo;
import com.mas.rave.service.ImageInfoService;
import com.mas.rave.util.FileUtil;

@Service
public class ImageInfoServiceImpl implements ImageInfoService {
	@Autowired
	private ImageInfoMapper imageMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private ImageAlbumResMapper<ImageAlbumRes> imageAlbumResMapper;

	@Autowired
	private SearchKeywordMapper searchKeywordMapper;

	@Autowired
	private SearchKeywordResListMapper searchKeywordResListMapper;

	@Override
	public PaginationVo<ImageInfo> searchImages(ImageInfo criteria, int currentPage, int pageSize) {
		int recordCount = imageMapper.countByExample(criteria);
		criteria.setCurrentPage((currentPage - 1) * pageSize);
		criteria.setPageSize(pageSize);
		List<ImageInfo> data = imageMapper.selectByExample(criteria);
		PaginationVo<ImageInfo> result = new PaginationVo<ImageInfo>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public ImageInfo searchImageInfo(ImageInfo criteria) {
		// TODO Auto-generated method stub
		List<ImageInfo> data = imageMapper.selectByExample(criteria);
		if (data != null) {
			return data.get(0);
		} else {
			return null;
		}
	}

	@Override
	public ImageInfo getImageInfo(long id) {
		// TODO Auto-generated method stub
		return imageMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ImageInfo> getAllImageInfos() {
		// TODO Auto-generated method stub
		return imageMapper.getAllImageInfos();
	}

	@Override
	public int addImageInfo(ImageInfo image, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			image.setCategory(category);
		}
		return imageMapper.insert(image);
	}

	@Override
	public int insertSelective(ImageInfo record) {
		// TODO Auto-generated method stub
		return imageMapper.insertSelective(record);
	}

	@Override
	public void upImageInfo(ImageInfo image, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			image.setCategory(category);
		}
		// 修改国家清除对应分发数据
		ImageInfo imagetInfo = imageMapper.selectByPrimaryKey(image.getId());
		if (imagetInfo != null) {
			if (image.getRaveId() == imagetInfo.getCountry().getId()) {
				imageMapper.updateByPrimaryKey(image);
				// 同步分发
				List<ImageAlbumRes> imageAlbumRes = imageAlbumResMapper.selectByImageId(image.getId());
				if (imageAlbumRes != null) {
					for (ImageAlbumRes imageres : imageAlbumRes) {
						imageres.setCategoryId(category.getId());
						imageres.setImageName(image.getName());
						imageres.setBrief(image.getBrief());
						imageres.setDescription(image.getDescription());
						imageres.setFree(image.getFree());
						imageres.setFileSize(image.getFileSize());
						imageres.setBiglogo(image.getBiglogo());
						imageres.setWidth(image.getWidth());
						imageres.setLength(image.getLength());
						imageres.setStars(image.getStars());
						imageres.setLogo(image.getLogo());
						imageres.setUrl(image.getUrl());
						imageAlbumResMapper.updateByPrimaryKey(imageres);
					}
				}
			} else {
				imageMapper.updateByPrimaryKey(image);
				imageAlbumResMapper.deleteByImageId(image.getId());
			}
		}

	}

	@Override
	public void delImageInfo(Integer id) {
		// 删除logo图片
		ImageInfo image = imageMapper.selectByPrimaryKey(id);

		if (image != null) {
			// 删除分发数据
			List<ImageAlbumRes> imageAlbumRes = imageAlbumResMapper.selectByImageId(id);
			if (imageAlbumRes != null) {
				for (ImageAlbumRes imageres : imageAlbumRes) {
					PaginationBean params = new PaginationBean();
					params.getParams().put("ids", Integer.toString(imageres.getId()));
					imageAlbumResMapper.deleteById(params);
				}

			}
			// 删除搜索关键字中的数据
			searchKeywordMapper.deleteByResId(id);
			searchKeywordResListMapper.deleteByResId(id);
			if (image.isState() == true) {
				image.setState(false);
				imageMapper.updateByPrimaryKey(image);
			} else {
				imageMapper.deleteByPrimaryKey(id);
				// 删除已经下载的文件
				try {
					if (image.getLogo() != null && image.getLogo() != "") {
						FileUtil.deleteFile(image.getLogo());
					}
					if (image.getBiglogo() != null && image.getBiglogo() != "") {
						FileUtil.deleteFile(image.getBiglogo());
					}
					if (image.getUrl() != null && image.getUrl() != "") {
						FileUtil.deleteFile(image.getUrl());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delImageInfo(id);
		}

	}

	@Override
	public void upImageInfoAlbumRes(ImageInfo imageInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ImageInfo> selectByName(String name) {
		// TODO Auto-generated method stub
		return imageMapper.selectByName(name);
	}

	@Override
	public int getImageInfoCountByCategory(int categoryId) {
		// TODO Auto-generated method stub
		return imageMapper.getImageInfoCountByCategory(categoryId);
	}

}
