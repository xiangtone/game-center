package com.reportforms.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class BaseDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		//System.out.println("datasource===>" + DataSourceSwitch.getDataSourceType());
		return DataSourceSwitch.getDataSourceType();
	}
}
