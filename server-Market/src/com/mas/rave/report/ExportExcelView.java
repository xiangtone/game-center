package com.mas.rave.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.document.AbstractExcelView;


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
		String excelName = new SimpleDateFormat("yyyy-MM-dd:hhmmss").format(new Date());
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
			//HSSFRow s_row = sheet.createRow(1);// 第二行
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
				cell = f_row.createCell(j);
				cell.setCellValue(cellTitle);
				cell.setCellStyle(style);
			}
			//合并第一行的所有列
//			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, propSize - 1));
//			cell = getCell(sheet, 0, 0);
//			cell.setCellStyle(style);
//			setText(cell, this.template.getTitle());
			// 从第三行开始,往列中填入数据
			int start = i * maxRow;
			int end = (i + 1) * maxRow;
			if (allRowCounts < end) {
				end = allRowCounts;
			}
			for (int j = start; j < end; j++) {
				HSSFRow row = sheet.createRow(j + 1);// 第三行开始
				for (int k = 0; k < propSize; k++) {
					String cellName = props.get(k).getName();
					Object obj = dataList.get(j);
					Class c = obj.getClass();
					try {
						Method method = c.getDeclaredMethod("get" + cellName.replaceFirst(cellName.substring(0, 1), cellName.substring(0, 1).toUpperCase()));
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
							Object param = method.invoke(obj);
							if (null!=param && param instanceof Double) {
								DecimalFormat df = new DecimalFormat("########0.0####");   
								  cell.setCellValue(df.format(((Double) param).doubleValue()));
							}else if (null!=param && param instanceof Integer) {
								  cell.setCellValue(((Integer) param).intValue());
							}else if (null!=param && param instanceof Float) {
								  cell.setCellValue(((Float) param).floatValue());
							}else if (null!=param && param instanceof Date) {
							    cell.setCellValue( new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date)param));
							}
							else{
								cell.setCellValue(null == method.invoke(obj) ? "" : method.invoke(obj).toString());
							}
						}
					} catch (Exception e) {
						logger.error("create excel sheet and row failure! ", e);
					}
				}
			}
			logger.info("create sheet :" + sheet.getSheetName() + " success !");
		}
	}

	private static XSSFWorkbook readTemplate(HttpServletRequest request) {
		// AccountRechargeSum.xls
		String contextPath = request.getSession().getServletContext()
				.getRealPath("");

		String filePath = contextPath + File.separator
				+ "template/AccountRechargeSum.xlsx";

//		System.out.println("filePath ==> " + filePath);

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
//				System.out.println("rowNumber=>" + row.getRowNum());

				for (int j = row.getFirstCellNum(); j < row
						.getPhysicalNumberOfCells(); j++) {
					XSSFCell cell = row.getCell(j);
					if (null == cell) {
						continue;
					}
//					System.out.println("cell =>" + cell.getColumnIndex()
//							+ "||cellValue=>" + cell.getStringCellValue());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workbook;
	}

	public static XMLModelTemplate getTemplate(HttpServletRequest request,HttpServletResponse response){
		
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(
				request.getSession().getServletContext());
		
		XMLModelTemplate template = (XMLModelTemplate) context.getBean("modelTemplate");
		if(null == template){
			logger.info("XMLModelTemplate is null !");
			return null;
		}
		//解析xml定义的模板文件
		template = TemplateUtil.readXml2Template(request, template);
		
		return template;
	}
}
