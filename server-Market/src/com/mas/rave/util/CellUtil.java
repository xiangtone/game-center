package com.mas.rave.util;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

public class CellUtil {

	public static String getValue(Cell cell) {
		String value = "";
		if(cell ==  null){
			return value;
		}
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				value = String.valueOf((int)cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value += cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				break;
		}
		return value;
	}
	
}
