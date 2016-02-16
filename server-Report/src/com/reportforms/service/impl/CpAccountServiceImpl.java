package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.AppMapper;
import com.reportforms.dao.ChannelInfoMapper;
import com.reportforms.dao.CpAccountMapper;
import com.reportforms.dao.GameCpMapper;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.App;
import com.reportforms.model.Channel;
import com.reportforms.model.CpAccount;
import com.reportforms.model.GameCp;
import com.reportforms.service.CpAccountService;

@Service
public class CpAccountServiceImpl extends BaseServiceImpl<CpAccount> implements CpAccountService<CpAccount> {

	@Autowired
	private CpAccountMapper<CpAccount> cpAccountMapper;
	
	@Autowired
	private AppMapper<App> appMapper;
	
	@Autowired
	private GameCpMapper<GameCp> gameCpMapper;
	
	@Autowired
	private ChannelInfoMapper<Channel> channelInfoMapper;
	
	@Override
	public List<CpAccount> query(PaginationBean paginationBean) {
		List<CpAccount> list = cpAccountMapper.query(paginationBean, new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		return format(list);
	}

	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		return cpAccountMapper.queryAllCounts(paginationBean);
	}

	@Override
	public List<CpAccount> queryByGroupBy(
			PaginationBean paginationBean) {
		List<CpAccount> list = cpAccountMapper.queryByGroupBy(paginationBean, new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		return format(list);
	}

	@Override
	public Integer queryByGroupByCounts(PaginationBean paginationBean) {
		return cpAccountMapper.queryByGroupByCounts(paginationBean);
	}

	@Override
	public List<CpAccount> queryToExport(PaginationBean paginationBean) {
		List<CpAccount> list = cpAccountMapper.query(paginationBean, new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		return format(list);
	}
	
	
	private List<CpAccount> format(List<CpAccount> list){
		
		if(!CollectionUtils.isEmpty(list)){
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
			List<App> apps = appMapper.query();
			List<GameCp> gameCps = gameCpMapper.queryAll();
			List<Channel> channels = channelInfoMapper.queryAll();
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
			for (CpAccount cpAccount : list) {
				if(null != apps){
					for (App app : apps) {
						if(cpAccount.getAppId().intValue() == app.getId().intValue()){
							cpAccount.setAppName(app.getAppName());
						}
					}
				}
				if(null != gameCps){
					for (GameCp gameCp : gameCps) {
						if(cpAccount.getCpId().intValue() == gameCp.getId().intValue()){
							cpAccount.setCpName(gameCp.getCpName());
						}
					}
				}
				if(null != channels){
					for (Channel channel : channels) {
						if(cpAccount.getChannelId().intValue() == channel.getId()){
							cpAccount.setChannelName(channel.getName());
						}
					}
				}
			}
		}else{
			return null;
		}
		return list;
	}

}
