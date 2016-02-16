package com.mas.rave.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 报表处理工具.
 * <p/>
 * @author tony.li.fly@gmail.com
 *
 * 
 */
public class ReportUtils {

	/**
	 * 根据模板生成报表.
	 * 
	 * @param template 模板流  <strong>此方法不会处理关闭流操作，请调用方自行处理</strong>
	 * @param out 最终报表输出流  <strong>此方法不会处理关闭流操作，请调用方自行处理</strong>
	 * @param dataSource 动态报表数据源 目前dataSource数据源只支持字符串的动态替换
	 * @throws IOException 各种IO流异常
	 */
	public static void fillReport(InputStream template, OutputStream out,
			Map<String, String> dataSource) throws IOException{
		HSSFWorkbook wb = new HSSFWorkbook(template);
		HSSFSheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			HSSFRow row = sheet.getRow(rowIndex);
			int columns = row.getPhysicalNumberOfCells();
			for (int colIndex = 0; colIndex < columns; colIndex++) {
				HSSFCell cell = row.getCell(colIndex);
				String placeholder = cell.getStringCellValue();
				String value = parsePlaceholder(placeholder, dataSource);
				if (value != null)
					cell.setCellValue(value);
			}
		}
		wb.write(out);
		out.flush();
	}

	private static String parsePlaceholder(String placeholder,
			Map<String, String> dataSource) {
		int start = placeholder.indexOf("${");
		if (start == -1)
			return null;
		int end = placeholder.lastIndexOf("}");
		String key = placeholder.substring(start + 2, end).trim();
		return dataSource.get(key);
	}
}
