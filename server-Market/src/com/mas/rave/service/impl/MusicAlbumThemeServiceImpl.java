package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.MusicAlbumResMapper;
import com.mas.rave.dao.MusicAlbumThemeMapper;
import com.mas.rave.main.vo.MusicAlbumRes;
import com.mas.rave.main.vo.MusicAlbumTheme;
import com.mas.rave.main.vo.MusicAlbumThemeExample;
import com.mas.rave.main.vo.MusicAlbumThemeExample.Criteria;
import com.mas.rave.service.MusicAlbumThemeService;
import com.mas.rave.util.FileUtil;
@Service
public class MusicAlbumThemeServiceImpl implements MusicAlbumThemeService {
	@Autowired
	private MusicAlbumThemeMapper musicAlbumThemeMapper;
	@Autowired
	private MusicAlbumResMapper musicAlbumResMapper;
	
	@Override
	public PaginationVo<MusicAlbumTheme> searchMusicAlbumThemes(
			MusicAlbumThemeCriteria criteria, int currentPage, int pageSize) {
		MusicAlbumThemeExample example = new MusicAlbumThemeExample();
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
		List<MusicAlbumTheme> data = musicAlbumThemeMapper.selectByExample(example,
				new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = musicAlbumThemeMapper.countByExample(example);
		PaginationVo<MusicAlbumTheme> result = new PaginationVo<MusicAlbumTheme>(
				data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public List<MusicAlbumTheme> searchMusicAlbumTheme(MusicAlbumThemeExample criteria) {
		// TODO Auto-generated method stub
		return musicAlbumThemeMapper.selectByExample(criteria);
	}

	@Override
	public MusicAlbumTheme getMusicAlbumTheme(long id) {
		// TODO Auto-generated method stub
		return musicAlbumThemeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<MusicAlbumTheme> getAllMusicAlbumThemes() {
		// TODO Auto-generated method stub
		return musicAlbumThemeMapper.getAllMusicAlbumThemes();
	}

	@Override
	public void addMusicAlbumTheme(MusicAlbumTheme musicAlbumTheme) {
		// TODO Auto-generated method stub
		musicAlbumThemeMapper.insert(musicAlbumTheme);
	}


	@Override
	public void upMusicAlbumTheme(MusicAlbumTheme musicAlbumTheme) {
		musicAlbumThemeMapper.updateByPrimaryKey(musicAlbumTheme);
		
	}

	@Override
	public void delMusicAlbumTheme(Integer id) {	
		MusicAlbumTheme musicAlbumTheme = musicAlbumThemeMapper
						.selectByPrimaryKey(id);
				String bigUrl = null;
				String smallUrl = null;
				if (musicAlbumTheme != null) {					
					//删除分发数据
					List<MusicAlbumRes>  musicAlbumRes = musicAlbumResMapper.selectByThemeId(id);
					 if(musicAlbumRes!=null){
						 for(MusicAlbumRes musicres:musicAlbumRes){
									PaginationBean params = new PaginationBean();
									params.getParams().put("ids", Integer.toString(musicres.getId()));
									musicAlbumResMapper.deleteById(params);		 
						 }	 
					 }
					 if(musicAlbumTheme.isState()==true){
						 musicAlbumTheme.setState(false);
						 musicAlbumThemeMapper.updateByPrimaryKey(musicAlbumTheme);
					 }else{						 
						 // 设置删除文件路径
						 bigUrl = musicAlbumTheme.getBigicon();
						 smallUrl = musicAlbumTheme.getIcon();
						 musicAlbumThemeMapper.deleteByPrimaryKey(id);
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
			delMusicAlbumTheme(id);
		}

		
	}

	@Override
	public void updateSortByPrimarykey(MusicAlbumTheme musicAlbumTheme) {
		musicAlbumThemeMapper.updateSortByPrimarykey(musicAlbumTheme);
		
	}
	@Override
	public List<MusicAlbumTheme> selectByThemeName(MusicAlbumTheme criteria) {
		// TODO Auto-generated method stub
		return musicAlbumThemeMapper.selectByThemeName(criteria);
	}

	@Override
	public List<MusicAlbumTheme> selectByThemeNameCn(String nameCn) {
		// TODO Auto-generated method stub
		return musicAlbumThemeMapper.selectByThemeNameCn(nameCn);
	}





}
