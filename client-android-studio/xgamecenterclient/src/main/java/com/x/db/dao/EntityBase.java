package com.x.db.dao;

public interface EntityBase {

	String getCreateTableSql();

	String[] getInitTableSql();

	String[] getUpdateTableSql();
}
