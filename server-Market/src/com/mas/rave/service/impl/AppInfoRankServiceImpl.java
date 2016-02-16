package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.AppAlbumResMapper;
import com.mas.rave.dao.AppInfoRankMapper;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumResExample;
import com.mas.rave.main.vo.AppInfoRank;
import com.mas.rave.service.AppInfoRankService;

/**
 * t_app_info_rank实现接口
 * 
 * @author lin.lu
 */
@Service
public class AppInfoRankServiceImpl implements AppInfoRankService {

	@Autowired
	private AppInfoRankMapper appInfoRankMapper;

	@Autowired
	private AppAlbumResMapper appAlbumResMapper;
	
	// 查看单个
	public AppInfoRank getAppInfoRank(int appId, int raveId) {
		AppInfoRank record = new AppInfoRank();
		record.setAppId(appId);
		record.setRaveId(raveId);
		return appInfoRankMapper.selectByPrimaryKey(record);
	}

	// 增加
	public void addAppInfoRank(AppInfoRank record) {
		appInfoRankMapper.insert(record);
	}

	// 更新
	public void upAppInfoRank(AppInfoRank record) {
		appInfoRankMapper.updateByPrimaryKey(record);
	}

	/**
	 * 更新排名
	 * @param columnId
	 */
	public void updateRank(Integer columnId,Integer raveId){
		AppAlbumResExample example = new AppAlbumResExample();
		example.createCriteria().columnId(columnId).andRaveIdEqual(raveId,0);
		List<AppAlbumRes> data = appAlbumResMapper.selectByExample(example); //按sort排序
		if(data==null || data.size()==0){
//			System.out.println("###updateRank,data.size=0");
			return;
		}
		//System.out.println("###updateRank,data.size="+data.size());
		int rank = 1;
		AppInfoRank appInfoRank;
		for(AppAlbumRes res:data){
			if(res.getAppInfo()==null){
//				System.out.println("@@@raveId="+raveId+"@@@columnId="+columnId+",appName="+res.getAppName()+",res.getAppInfo() is null");
				rank++;
				continue;
			}
			appInfoRank = getAppInfoRank(res.getAppInfo().getId(), raveId);
			if(appInfoRank==null){
				appInfoRank = new AppInfoRank();
				appInfoRank.setAppId(res.getAppInfo().getId());
				appInfoRank.setRaveId(raveId);
				if(setRank(appInfoRank,columnId,rank)){
					addAppInfoRank(appInfoRank);
				}
			}else{
				if(setRank(appInfoRank,columnId,rank)){
					upAppInfoRank(appInfoRank);
				}
			}
			rank++;
		}	
	}
	public boolean setRank(AppInfoRank appInfoRank,Integer columnId,int rank){
		boolean flag = true;
		switch (columnId) {
		case 1:
			if(appInfoRank.getHrHigerank()==0){
				appInfoRank.setHrHigerank(rank);
			}else{
				appInfoRank.setHrHigerank(appInfoRank.getHrHigerank()>rank?rank:appInfoRank.getHrHigerank());
			}
			if(appInfoRank.getHrLowrank()==0){
				appInfoRank.setHrLowrank(rank);
			}else{
				appInfoRank.setHrLowrank(appInfoRank.getHrLowrank()<rank?rank:appInfoRank.getHrLowrank());
			}
			break;
		case 2:
			if(appInfoRank.getHnHigerank()==0){
				appInfoRank.setHnHigerank(rank);
			}else{
				appInfoRank.setHnHigerank(appInfoRank.getHnHigerank()>rank?rank:appInfoRank.getHnHigerank());
			}
			if(appInfoRank.getHnLowrank()==0){
				appInfoRank.setHnLowrank(rank);
			}else{
				appInfoRank.setHnLowrank(appInfoRank.getHnLowrank()<rank?rank:appInfoRank.getHnLowrank());
			}
			break;
		case 3:
			if(appInfoRank.getHtHigerank()==0){
				appInfoRank.setHtHigerank(rank);
			}else{
				appInfoRank.setHtHigerank(appInfoRank.getHtHigerank()>rank?rank:appInfoRank.getHtHigerank());
			}
			if(appInfoRank.getHtLowrank()==0){
				appInfoRank.setHtLowrank(rank);
			}else{
				appInfoRank.setHtLowrank(appInfoRank.getHtLowrank()<rank?rank:appInfoRank.getHtLowrank());
			}
			break;
		case 4:
			if(appInfoRank.getHpHigerank()==0){
				appInfoRank.setHpHigerank(rank);
			}else{
				appInfoRank.setHpHigerank(appInfoRank.getHpHigerank()>rank?rank:appInfoRank.getHpHigerank());
			}
			if(appInfoRank.getHpLowrank()==0){
				appInfoRank.setHpLowrank(rank);
			}else{
				appInfoRank.setHpLowrank(appInfoRank.getHpLowrank()<rank?rank:appInfoRank.getHpLowrank());
			}
			break;
		case 12:
			if(appInfoRank.getAhHigerank()==0){
				appInfoRank.setAhHigerank(rank);
			}else{
				appInfoRank.setAhHigerank(appInfoRank.getAhHigerank()>rank?rank:appInfoRank.getAhHigerank());
			}
			if(appInfoRank.getAhLowrank()==0){
				appInfoRank.setAhLowrank(rank);
			}else{
				appInfoRank.setAhLowrank(appInfoRank.getAhLowrank()<rank?rank:appInfoRank.getAhLowrank());
			}
			break;
		case 13:
			if(appInfoRank.getAtHigerank()==0){
				appInfoRank.setAtHigerank(rank);
			}else{
				appInfoRank.setAtHigerank(appInfoRank.getAtHigerank()>rank?rank:appInfoRank.getAtHigerank());
			}
			if(appInfoRank.getAtLowrank()==0){
				appInfoRank.setAtLowrank(rank);
			}else{
				appInfoRank.setAtLowrank(appInfoRank.getAtLowrank()<rank?rank:appInfoRank.getAtLowrank());
			}
			break;
		case 14:
			if(appInfoRank.getAnHigerank()==0){
				appInfoRank.setAnHigerank(rank);
			}else{
				appInfoRank.setAnHigerank(appInfoRank.getAnHigerank()>rank?rank:appInfoRank.getAnHigerank());
			}
			if(appInfoRank.getAnLowrank()==0){
				appInfoRank.setAnLowrank(rank);
			}else{
				appInfoRank.setAnLowrank(appInfoRank.getAnLowrank()<rank?rank:appInfoRank.getAnLowrank());
			}
			break;
		case 22:
			if(appInfoRank.getGhHigerank()==0){
				appInfoRank.setGhHigerank(rank);
			}else{
				appInfoRank.setGhHigerank(appInfoRank.getGhHigerank()>rank?rank:appInfoRank.getGhHigerank());
			}
			if(appInfoRank.getGhLowrank()==0){
				appInfoRank.setGhLowrank(rank);
			}else{
				appInfoRank.setGhLowrank(appInfoRank.getGhLowrank()<rank?rank:appInfoRank.getGhLowrank());
			}
			break;
		case 23:
			if(appInfoRank.getGtHigerank()==0){
				appInfoRank.setGtHigerank(rank);
			}else{
				appInfoRank.setGtHigerank(appInfoRank.getGtHigerank()>rank?rank:appInfoRank.getGtHigerank());
			}
			if(appInfoRank.getGtLowrank()==0){
				appInfoRank.setGtLowrank(rank);
			}else{
				appInfoRank.setGtLowrank(appInfoRank.getGtLowrank()<rank?rank:appInfoRank.getGtLowrank());
			}
			break;
		case 24:
			if(appInfoRank.getGnHigerank()==0){
				appInfoRank.setGnHigerank(rank);
			}else{
				appInfoRank.setGnHigerank(appInfoRank.getGnHigerank()>rank?rank:appInfoRank.getGnHigerank());
			}
			if(appInfoRank.getGnLowrank()==0){
				appInfoRank.setGnLowrank(rank);
			}else{
				appInfoRank.setGnLowrank(appInfoRank.getGnLowrank()<rank?rank:appInfoRank.getGnLowrank());
			}
			break;
			default:flag=false;
		}
		return flag;
	}
}
