package com.mas.rave.common.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.mas.rave.common.ImportRule;
import com.mas.rave.exception.ResParserException;
import com.mas.rave.service.AppBatchUploadService;
import com.mas.rave.util.CellUtil;
import com.mas.rave.util.Constant;
import com.mas.rave.vo.AppResourceVO;

/**
 * 资源入库处理类
 * 
 * @author shirong.jiang
 * 
 */
public class AppUploadTask implements Callable<List<String>> {
	private Logger logger = Logger.getLogger(AppUploadTask.class);

	private AppBatchUploadService appBatchUploadService;

	private ImportRule appImportRule;

	private ExcelParser excelParser;

	private List<Row> rows;

	private List<String> ups;

	private List<String> repeats;

	public AppUploadTask(ExcelParser excelParser, AppBatchUploadService appBatchUploadService, ImportRule appImportRule, List<Row> rows, List<String> ups, List<String> repeats) {
		this.excelParser = excelParser;
		this.appBatchUploadService = appBatchUploadService;
		this.appImportRule = appImportRule;
		this.rows = rows;
		this.ups = ups;
		this.repeats = repeats;
	}

	@Override
	public List<String> call() throws Exception {
		List<String> msgList = new ArrayList<String>();
		List<String> heads = excelParser.getHeads();
		int cellNum = heads.size();
		for (int i = 0; i < rows.size(); i++) {
			try {
				AppResourceVO resourceVo = (AppResourceVO) appImportRule.getResource();
				Row row = rows.get(i);
				boolean valid = true;
				for (int idx = 0; idx < cellNum; idx++) {
					Cell cell = row.getCell(idx);
					String value = CellUtil.getValue(cell);
					String reason = appImportRule.checkField(heads.get(idx), value, resourceVo);
					if (StringUtils.isNotEmpty(reason)) {
						msgList.add("文件[" + excelParser.getExcelFile() + "],第[" + row.getRowNum() + "]行不合法，原因：" + reason);
						valid = false;
						break;
					}
				}
				if (valid) {
					// 保存资源信息到数据库
					String msg = null;
					try {
						appBatchUploadService.saveResource(resourceVo);
						if (resourceVo.getUpType() == 1) {
							ups.add("应用[" + resourceVo.getAppName() + "]更新");
						} else if (resourceVo.getReplaceType() == 1) {
							repeats.add("应用[" + resourceVo.getAppName() + "]重复");
						}
					} catch (Exception ex) {
						msg = ex.getMessage() + "," + resourceVo.getAppName();
						logger.error("app应用资源入库出现异常", ex);
					}

					if (StringUtils.isEmpty(msg)) {
						try {
							// 迁移资源
							moveAppResource(resourceVo);
						} catch (Exception e) {
							msg = e.getMessage() + "," + resourceVo.getAppName();
							logger.error("app资源迁移整理发生异常,原因：" + e.getMessage(), e);
						}
						try {
							// 删除excel文件中的行
							excelParser.removeRow(row);
						} catch (Exception e) {
							//msgList.add("excel资源删除行[" + row.getRowNum() + "]时异常");
							logger.error("excel资源删除行[" + row.getRowNum() + "]时异常", e);
						}
						try {
							// 删除已经处理完毕的应用目录
							String appPath = Constant.FTP_UPLOAD_PATH == "" ? Constant.RESOURCE_UPLOAD_PATH : Constant.FTP_UPLOAD_PATH;
//							String appPath = Constant.RESOURCE_UPLOAD_PATH;
							appPath += resourceVo.getType() + File.separator + resourceVo.getAppName();
							File curAppDir = new File(appPath);
							if (curAppDir.exists()) {
								FileUtils.forceDelete(curAppDir);
							}
						} catch (Exception e) {
							//msgList.add("清理应用[" + resourceVo.getAppName() + "]的资源时发生异常");
							logger.error("清理应用[" + resourceVo.getAppName() + "]的资源时发生异常", e);
						}
					} else {
						msgList.add(msg);
					}
				}
			} catch (Throwable e) {
				msgList.add(e.getMessage());
				logger.error("app应用资源导入发生不可预期的异常", e);
			}
		}
		return msgList;
	}

	public void moveAppResource(AppResourceVO resourceVo) {
		File destFile = null;
		File srcFile = null;
		try {
			// 移动apk文件
			File[] apkFiles = appBatchUploadService.getUploadSrcFile(resourceVo, Constant.APK_ADR);
			if (apkFiles == null || apkFiles.length != 1) {
				logger.debug("应用[" + resourceVo.getAppName() + "]APK资源不合法");
				throw new ResParserException("应用[" + resourceVo.getAppName() + "]APK资源不合法");
			}
			srcFile = apkFiles[0];
			destFile = new File(Constant.LOCAL_FILE_PATH + resourceVo.getApkUrl());
			if (destFile.exists() && destFile.isFile()) {
				FileUtils.forceDelete(destFile);
			}
			FileUtils.moveFile(srcFile, destFile);
			// FileUtils.copyFileToDirectory(srcFile, destFile);
		} catch (Exception e) {
			throw new ResParserException("app资源迁移整理发生异常", e);
		}
		try {
			// 移动logo文件
			File[] logoFiles = appBatchUploadService.getUploadSrcFile(resourceVo, Constant.LOGO_ADR);
			for (File f : logoFiles) {
				if (f.getName().startsWith("small_")) {
					destFile = new File(Constant.LOCAL_FILE_PATH + resourceVo.getLogo());
					// FileUtils.moveFile(f, destFile);
				} else {
					destFile = new File(Constant.LOCAL_FILE_PATH + resourceVo.getBigLogo());
					// FileUtils.moveFile(f, destFile);
				}
				if (destFile.exists() && destFile.isFile()) {
					FileUtils.forceDelete(destFile);
				}
				FileUtils.moveFile(f, destFile);
				// FileUtils.copyFileToDirectory(srcFile, destFile);
			}
		} catch (Exception e) {
			throw new ResParserException("logo资源迁移整理发生异常", e);
		}

		try {
			// 移动pic文件
			File[] picFiles = appBatchUploadService.getUploadSrcFile(resourceVo, Constant.IMG_ADR);
			for (File picFile : picFiles) {
				String nPicFile = resourceVo.getPicUrls().get(picFile.getName());
				destFile = new File(Constant.LOCAL_FILE_PATH + nPicFile);
				if (destFile.exists() && destFile.isFile()) {
					FileUtils.forceDelete(destFile);
				}
				FileUtils.moveFile(picFile, destFile);
				// FileUtils.copyFileToDirectory(srcFile, destFile);
			}
		} catch (Exception e) {
			throw new ResParserException("pic资源迁移整理发生异常", e);
		}
	}
}
