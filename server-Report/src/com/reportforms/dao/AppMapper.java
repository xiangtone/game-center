package com.reportforms.dao;

import java.util.List;

public interface AppMapper<T> extends BaseMapper<T, Long> {

	public List<T> query();
}
