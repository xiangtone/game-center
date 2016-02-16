package com.mas.rave.common.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.ImportRule;
import com.mas.rave.exception.ResParserException;
import com.mas.rave.service.AppBatchUploadService;
import com.mas.rave.util.Constant;

@Service(value = "appBatchUploadEngine")
public class AppBatchUploadEngine {
	Logger logger = Logger.getLogger(AppBatchUploadEngine.class);

	private static ExecutorService executorService = Executors.newFixedThreadPool(Constant.UPLOAD_THREAD_NUM);

	@Autowired
	private AppBatchUploadService appBatchUpload;

	@Autowired
	private ImportRule appImportRule;

	public List<String> batchUpload(String excelFileName) {
		long beginTime = System.currentTimeMillis();
		List<String> msgList = new ArrayList<String>();
		List<String> upList = new ArrayList<String>();
		List<String> repeats = new ArrayList<String>();
		ExcelParser excelParser = null;
		try {
			// String resName = Constant.UPLOAD_DESCRIBE_FILE;
			String excelFile = getExcelFile(excelFileName);

			// 解释资源描述文件
			excelParser = new ExcelParser(excelFile);
			excelParser.parserExcel();

			// 按线程数对资源进行任务分配
			int totalRow = excelParser.getTotalRow();
			List<Row> rows = excelParser.getRows();
			List<Future<List<String>>> futureList = new ArrayList<Future<List<String>>>();
			if (totalRow < Constant.UPLOAD_THREAD_NUM) {
				Future<List<String>> future = executorService.submit(new AppUploadTask(excelParser, appBatchUpload, appImportRule, rows, upList, repeats));
				futureList.add(future);
			} else {
				int step = totalRow / Constant.UPLOAD_THREAD_NUM;
				int curIndex = 0;
				for (int i = 0; i < Constant.UPLOAD_THREAD_NUM; i++) {
					int last = curIndex + step;
					List<Row> subRows = null;
					if (i == (Constant.UPLOAD_THREAD_NUM - 1)) {
						subRows = rows.subList(curIndex, rows.size());
					} else {
						subRows = rows.subList(curIndex, last);
						curIndex = last;
					}
					Future<List<String>> future = executorService.submit(new AppUploadTask(excelParser, appBatchUpload, appImportRule, subRows, upList,repeats));
					futureList.add(future);
				}
			}
			for (Future<List<String>> future : futureList) {
				List<String> results = future.get();
				msgList.addAll(results);
			}
			int total = excelParser.getTotalRow();
			int success = total - msgList.size() - upList.size()-repeats.size();
			msgList.add(0, "本次上传总共有[" + total + "]个应用,成功[" + success + "]个,共有[" + upList.size() + "]更新成功,共有[" + repeats.size() + "]替换成功,失败[" + msgList.size() + "]");
			if (msgList.size() > 1) {
				excelParser.errResult2File();
			} else {
				excelParser.backExcelFile();
			}
			long endTime = System.currentTimeMillis();
			msgList.add("总共耗时:" + calconsumeTime(beginTime, endTime));
			excelParser.log2File(msgList);
		} catch (Exception e) {
			msgList.add("[parseResource]解释并导入资源时发行无法预知的错误");
			// logger.error("[parseResource]解释并导入资源时发行无法预知的错误",e);
			// throw new ResParserException("无法预知的错误,原因：" + e.getMessage(),e);
		}
		return msgList;
	}

	/**
	 * 备份资源描述文件
	 * 
	 * @param resName
	 */
	private String getExcelFile(String excelFileName) {
		String fullPath = (Constant.FTP_UPLOAD_PATH == "" ? Constant.RESOURCE_UPLOAD_PATH : Constant.FTP_UPLOAD_PATH ) + excelFileName;
		File excelFile = new File(fullPath);
		if (!excelFile.exists()) {
			throw new ResParserException("资源文件[" + fullPath + "]不存在，请重新检查");
		}
		return excelFile.getAbsolutePath();
	}

	private String calconsumeTime(long beingTime, long endTime) {
		long inteval = endTime - beingTime;
		if (inteval < 1024) {
			return inteval + "毫秒";
		} else if (inteval > 1024 && inteval < (1024 * 1024)) {
			return inteval / 1024 + "秒";
		} else if (inteval > 1024 && inteval < (1024 * 1024 * 1024)) {
			return inteval / (1024 * 1024) + "分钟";
		} else {
			return inteval / (1024 * 1024 * 1024) + "小时";
		}
	}

	public static void main(String[] args) {
		List<String> arry = new ArrayList<String>();
		for (int i = 0; i < 93; i++) {
			arry.add(i + "");
		}
		int step = 93 / 5;
		int curIndex = 0;
		for (int i = 0; i < 5; i++) {
			int last = curIndex + step;
			List<String> subRows = null;
			if (i == 4) {
				subRows = arry.subList(curIndex, arry.size());
			} else {
				subRows = arry.subList(curIndex, last);
				curIndex = last;
			}
			System.out.println(subRows);
			System.out.println("总数：" + subRows.size());
			System.out.println("============================");
		}
	}
}
