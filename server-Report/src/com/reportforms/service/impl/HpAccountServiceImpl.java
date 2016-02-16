package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.AppMapper;
import com.reportforms.dao.ChannelInfoMapper;
import com.reportforms.dao.GameCpMapper;
import com.reportforms.dao.HpAccountMapper;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.App;
import com.reportforms.model.Channel;
import com.reportforms.model.CpAccount;
import com.reportforms.model.GameCp;
import com.reportforms.model.HpAccount;
import com.reportforms.service.HpAccountService;

@Service
public class HpAccountServiceImpl extends BaseServiceImpl<HpAccount> implements HpAccountService<HpAccount> {

	@Autowired
	private HpAccountMapper<HpAccount> hpAccountMapper;
	
	@Autowired
	private AppMapper<App> appMapper;
	
	@Autowired
	private GameCpMapper<GameCp> gameCpMapper;
	
	@Autowired
	private ChannelInfoMapper<Channel> channelInfoMapper;
	
	@Override
	public List<HpAccount> query(PaginationBean entity) {
		List<HpAccount> list = hpAccountMapper.query(entity,
				new RowBounds(entity.getStart(), entity.getLimit()));
		return format(list);
	}

	@Override
	public Integer queryAllCounts(PaginationBean entity) {
		return hpAccountMapper.queryAllCounts(entity);
	}

	@Override
	public List<HpAccount> queryByGroupBy(
			PaginationBean pagination) {
		List<HpAccount> list = hpAccountMapper.queryByGroupBy(pagination,
				new RowBounds(pagination.getStart(), pagination.getLimit()));
		return format(list);
	}
	
	@Override
	public Integer queryByGroupByCounts(PaginationBean pagination) {
		return hpAccountMapper.queryByGroupByCounts(pagination);
	}

	@Override
	public List<HpAccount> queryToExport(PaginationBean entity) {
		List<HpAccount> list = hpAccountMapper.query(entity,
				new RowBounds(entity.getStart(), entity.getLimit()));
		return format(list);
	}
	
	private List<HpAccount> format(List<HpAccount> list){
		
		if(!CollectionUtils.isEmpty(list)){
			DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
			List<App> apps = appMapper.query();
			List<GameCp> gameCps = gameCpMapper.queryAll();
			List<Channel> channels = channelInfoMapper.queryAll();
			//移除数据源,使用默认数据源
			DataSourceSwitch.clearDataSourceType();
			for (HpAccount hpAccount : list) {
				if(null != apps){
					for (App app : apps) {
						if(hpAccount.getAppId().intValue() == app.getId().intValue()){
							hpAccount.setAppName(app.getAppName());
						}
					}
				}
				if(null != gameCps){
					for (GameCp gameCp : gameCps) {
						if(hpAccount.getCpId().intValue() == gameCp.getId().intValue()){
							hpAccount.setCpName(gameCp.getCpName());
						}
					}
				}
				if(null != channels){
					for (Channel channel : channels) {
						if(hpAccount.getChannelId().intValue() == channel.getId()){
							hpAccount.setChannelName(channel.getName());
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
