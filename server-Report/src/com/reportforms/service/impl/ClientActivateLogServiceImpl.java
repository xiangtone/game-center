package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.AppMapper;
import com.reportforms.dao.ClientActivateLogMapper;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.App;
import com.reportforms.model.ClientActivateLog;
import com.reportforms.service.ClientActivateLogService;

@Service
public class ClientActivateLogServiceImpl extends BaseServiceImpl<ClientActivateLog> implements
		ClientActivateLogService<ClientActivateLog> {
	
	@Autowired
	private ClientActivateLogMapper<ClientActivateLog> clientActivateLogMapper;
	
	@Autowired
	private AppMapper<App> appMapper;
	
	@Override
	public List<ClientActivateLog> query(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		List<ClientActivateLog> list = clientActivateLogMapper.query(paramsBean,new RowBounds(paramsBean.getStart(),paramsBean.getLimit()));
		DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3); 
		List<App> apps = appMapper.query();
		//移除数据源,使用默认数据源
		DataSourceSwitch.clearDataSourceType();
		if(null != list && null != apps){
			for (ClientActivateLog activateLog : list) {
				for (App app : apps) {
					if(app.getId().intValue() == activateLog.getAppId().intValue()){
						activateLog.setAppName(app.getAppName());
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return clientActivateLogMapper.queryAllCounts(paramsBean);
	}
}
