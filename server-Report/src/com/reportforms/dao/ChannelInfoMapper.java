package com.reportforms.dao;

import java.util.List;

public interface ChannelInfoMapper<T> extends BaseMapper<T, Long>{

	public List<T> queryAll();
}
