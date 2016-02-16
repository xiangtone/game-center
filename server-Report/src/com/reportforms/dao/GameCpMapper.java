package com.reportforms.dao;

import java.util.List;

public interface GameCpMapper<T> extends BaseMapper<T, Long> {

	public List<T> queryAll();
}
