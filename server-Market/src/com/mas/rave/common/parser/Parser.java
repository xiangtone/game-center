package com.mas.rave.common.parser;

import java.io.File;
import java.util.List;

import com.mas.rave.common.ImportRule;
import com.mas.rave.exception.ResParserException;

public interface Parser {

	public List<Object> parseResource(ImportRule importRule,File resourceFile)throws ResParserException;

}
