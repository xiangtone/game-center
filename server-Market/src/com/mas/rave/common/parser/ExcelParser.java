package com.mas.rave.common.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mas.rave.exception.ResParserException;
import com.mas.rave.util.CellUtil;
import com.mas.rave.util.Constant;
import com.mas.rave.util.RandNum;

public class ExcelParser {

	private Logger logger = Logger.getLogger(ExcelParser.class); 
	
	private Workbook workbook;
	
	private Sheet sheet;
	
	private String excelFile;
	
	private InputStream stream;
	
	private int totalRow;
	
	private List<Row> rows;
	
	private List<String> heads;
	
	public ExcelParser(String excelFile){
		this.excelFile = excelFile;
	}
	
	protected static List<String> parseHead(Row row){
		List<String> heads = new ArrayList<String>();
		int cellNum = row.getPhysicalNumberOfCells();
		for(int cellnum = 0 ; cellnum < cellNum ; cellnum++){
			Cell cell = row.getCell(cellnum);
			String value = CellUtil.getValue(cell);
			if(StringUtils.isNotEmpty(value)){
				heads.add(value);
			}
		} 
		return heads;
	}
	
	public void parserExcel(){
		
		try{
			stream = new FileInputStream(excelFile);
	        try {
	        	workbook = new XSSFWorkbook(stream);
	        } catch (Exception ex) {
	        	workbook = new HSSFWorkbook(stream);
	        }
	       
	        sheet = workbook.getSheetAt(0);
			totalRow = sheet.getPhysicalNumberOfRows();
			
			if(logger.isDebugEnabled()){
				logger.debug("totalRow:" + totalRow);
			}
			 
			if(totalRow < 2 ){
				throw new ResParserException("excel文件[" +excelFile +"]只有[" + rows + "]，格式不正确，至少2行");
			}
			rows = new ArrayList<Row>();
			for(int i=1;i<totalRow;i++){
				rows.add(sheet.getRow(i));
			}
			Row head = sheet.getRow(0);
			heads = parseHead(head);
		}catch(Exception e){
			throw new ResParserException("解释excel文件[" + excelFile + "]发生不可预知的异常" ); 
		}finally{
			close();
		}
	}
	
	public List<Row> getRows() {
		return rows;
	}
	
	public Sheet getSheet() {
		return sheet;
	}
	
	public List<String> getHeads() {
		return heads;
	}
	
	public Workbook getWorkbook() {
		return workbook;
	}
	
	public void close(){
		if(stream != null){
			try {
				stream.close();
			} catch (IOException e) {
				throw new ResParserException("关闭[" + excelFile + "]的文件流异常",e);
			}
		}
	}
	
	public void removeRow(Row row){
		sheet.removeRow(row);
	}
	
	public int getTotalRow(){
		if(rows!=null){
			return rows.size();
		}
		return 0;
	}
	
	public String getExcelFile() {
		return excelFile;
	}
	
	public void errResult2File(){
		FileOutputStream fos = null;
		String newFileName = null;
		try{
			String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
			String suffix = excelFile.substring(excelFile.indexOf("."));
			newFileName = fileName + suffix;
			File logDir = new File(Constant.RESOURCE_UPLOAD_PATH + "/log/");
			if(!logDir.exists()){
				logDir.mkdirs();
			}
			File newFile = new File(logDir.getAbsoluteFile() + File.separator + newFileName);
			if(!newFile.exists()){
				newFile.createNewFile();
			}
			fos = new FileOutputStream(newFile);
			workbook.write(fos);
		}catch(Exception e){
			throw new ResParserException("workbook写入新的文件出现异常",e);
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					throw new ResParserException("关闭[" + newFileName + "]的文件输出流异常",e);
				}
			}
		}
	}
	
	public void backExcelFile(){
		try{
			String newFileName = RandNum.randomFileName(excelFile);
			File destFile = new File(Constant.LOCAL_FILE_PATH + newFileName);
			
			File srcFile = new File(Constant.RESOURCE_UPLOAD_PATH,excelFile);
			FileUtils.moveFile(srcFile, destFile);
		}catch(Exception e){
			throw new ResParserException("备份[" + excelFile + "]的文件异常",e);
		}
	}
	
	public String log2File(List<String> msgList){
		FileWriter writer = null;
		try{
			String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".log";
			File logDir = new File(Constant.RESOURCE_UPLOAD_PATH + "/log/");
			if(!logDir.exists()){
				logDir.mkdirs();
			}
			File newFile = new File(logDir.getAbsoluteFile() + File.separator + fileName);
			if(!newFile.exists()){
				newFile.createNewFile();
			}
			writer = new FileWriter(newFile);
			for(String s: msgList){
				writer.write(s + "\r\n");
			}
			return fileName;
		}catch(Exception e){
			logger.error("输出批量导入日志时出现异常",e);
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					throw new ResParserException("批量导出日志结果输出流关闭时异常",e);
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String excelFile = "aaaa/aa.txt";
		String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
		String suffix = excelFile.substring(excelFile.indexOf("."));
		String newFileName = fileName + suffix;
		System.out.println(newFileName);
	}
}
