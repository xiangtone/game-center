package com.reportforms.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.reportforms.template.XMLModelTemplate;
import com.reportforms.template.XMLPropertiesTemplate;

@SuppressWarnings("unchecked")
public class ExportExcelView extends AbstractExcelView {

	private static int maxRow = 50000;

	private static final Logger logger = Logger
			.getLogger(ExportExcelView.class);

	private XMLModelTemplate template;

	@Override
	protected void buildExcelDocument(Map<String, Object> paramMap,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String excelName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		//设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
        response.setContentType("application/octet-stream"); 
        response.setHeader("Content-Disposition","attachment; filename=" + excelName + ".xls");
		List<Object> list = (List<Object>)paramMap.get("list");
		this.template = (XMLModelTemplate)paramMap.get("template");
		create(list,workbook);
	}

	/**
	 * 创建excel并填入数据
	 * @param dataList
	 * @param request
	 * @param context
	 * @param workbook
	 */
	private void create(List<Object> dataList,HSSFWorkbook workbook) {

		// 计算所有数据个数
		int allRowCounts = dataList.size();
		// 计算需要新建多少个sheet
		int sheetCounts = allRowCounts % maxRow == 0 ? allRowCounts / maxRow
				: allRowCounts / maxRow + 1;
		for (int i = 0; i < sheetCounts; i++) {
			HSSFSheet sheet = workbook.createSheet(this.template.getTitle() + "("+ (i + 1) + ")");
			HSSFRow f_row = sheet.createRow(0);// 第一行
			HSSFRow s_row = sheet.createRow(1);// 第二行
			HSSFCellStyle style = workbook.createCellStyle();
			//设置单元格样式
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setTopBorderColor(HSSFColor.BLACK.index);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBottomBorderColor(HSSFColor.BLACK.index);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setLeftBorderColor(HSSFColor.BLACK.index);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setRightBorderColor(HSSFColor.BLACK.index);
			
			List<XMLPropertiesTemplate> props = this.template.getProps();
			int propSize = props.size();
			HSSFCell cell = null;
			for (int j = 0; j < propSize; j++) {
				String cellTitle = props.get(j).getTitle();
				// 创建第一行及所有列
				cell = f_row.createCell(j);
				//设置每列的宽度
				sheet.setColumnWidth(j, props.get(i).getColumnsWidth().intValue());
				cell.setCellStyle(style);
				// 创建第二行及所有列,并赋值
				cell = s_row.createCell(j);
				cell.setCellValue(cellTitle);
				cell.setCellStyle(style);
			}
			//合并第一行的所有列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, propSize - 1));
			cell = getCell(sheet, 0, 0);
			cell.setCellStyle(style);
			setText(cell, this.template.getTitle());
			// 从第三行开始,往列中填入数据
			int start = i * maxRow;
			int end = (i + 1) * maxRow;
			if (allRowCounts < end) {
				end = allRowCounts;
			}
			int startRow = 2;// 第三行开始
			int sumCounts = 0;
			for (int j = start; j < end; j++) {
				HSSFRow row = sheet.createRow(startRow);// 第三行开始
				Object obj = dataList.get(j);
				Class<?> c = obj.getClass();
				for (int k = 0; k < propSize; k++) {
					String cellName = props.get(k).getName();
					Boolean isSums = props.get(k).getIsSums() == null ? false : props.get(k).getIsSums();
					//判断该字段是否需要统计
					if(j == start && isSums.booleanValue() == true){
						sumCounts ++;
					}
					try {
						Method method = c.getDeclaredMethod("get" + cellName);
						cell = row.createCell(k);
						//设置每列的宽度
						sheet.setColumnWidth(k, props.get(k).getColumnsWidth().intValue());
						cell.setCellStyle(style);
						if (null == method) {
							logger.info("the field :" + cellName
									+ " is not find !");
							cell.setCellValue(" ");
							continue;
						} else {
							Class<?> type = method.getReturnType();
							Object returnValue = method.invoke(obj);
							if(type.getName().equals("java.lang.Integer") || type.getName().equals("java.lang.Long")){
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(null == returnValue ? 0 : Double.parseDouble(returnValue.toString()));
							}else{
								cell.setCellValue(null == returnValue ? "" : returnValue.toString());
							}
						}
					} catch (Exception e) {
						logger.error("create excel sheet and row failure! ", e);
					}
				}
				startRow ++;
			}
			HSSFRow lastRow = sheet.createRow(startRow);
			if(TemplateUtil.containStatisticsModels(this.template.getId()) && sumCounts > 0){
				for (int j = 0; j < sumCounts; j++) {
					double cellVal = 0;
					for (int k = 2; k < startRow; k++) {
						HSSFRow row = sheet.getRow(k);
						HSSFCell cell1 = row.getCell(j);
						cellVal += cell1.getNumericCellValue();
					}
					HSSFCell lastCell = lastRow.createCell(j);
					lastCell.setCellStyle(style);
					lastCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					lastCell.setCellValue(cellVal);
				}
			}else{
				HSSFCell lastCell = lastRow.createCell(0);
				lastCell.setCellStyle(style);
				lastCell.setCellValue("总计:" + (startRow - 2 ) + "条数据");
			}
			logger.info("create sheet :" + sheet.getSheetName() + " success !");
		}
	}

	
	/*
	private static XSSFWorkbook readTemplate(HttpServletRequest request) {
		// AccountRechargeSum.xls
		String contextPath = request.getSession().getServletContext()
				.getRealPath("");

		String filePath = contextPath + File.separator
				+ "template/AccountRechargeSum.xlsx";

		System.out.println("filePath ==> " + filePath);

		XSSFWorkbook workbook = null;

		try {
			InputStream is = new FileInputStream(filePath);

			workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = sheet.getFirstRowNum(); i < sheet
					.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = sheet.getRow(i);
				if (null == row) {
					continue;
				}
				System.out.println("rowNumber=>" + row.getRowNum());

				for (int j = row.getFirstCellNum(); j < row
						.getPhysicalNumberOfCells(); j++) {
					XSSFCell cell = row.getCell(j);
					if (null == cell) {
						continue;
					}
					System.out.println("cell =>" + cell.getColumnIndex()
							+ "||cellValue=>" + cell.getStringCellValue());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return workbook;

	}*/

}
