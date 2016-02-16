package com.mas.rave.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppInfoConfig;
import com.mas.rave.main.vo.User;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoConfigService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.LogService;

/**
 * app配置业务处理　
 * 
 * @author liwei.sz
 * 
 */
@Controller
@RequestMapping("/appInfoConfig")
public class AppInfoConfigController {
	Logger log = Logger.getLogger(AppInfoConfigController.class);
	@Autowired
	private AppInfoConfigService appInfoConfigService;
	@Autowired
	private LogService logService;
	@Autowired
	private AppFileService appFileService;
	@Autowired
	private AppInfoService appInfoService;
	/**
	 * 分页查看app配置信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			String type = request.getParameter("type");
			String state = request.getParameter("state");
			String packageName = request.getParameter("packageName");

			HashMap<String, Object> map = new HashMap<String, Object>(3);
			if (StringUtils.isNotEmpty(name)) {
				map.put("name", name.trim());
			}
			if (StringUtils.isNotEmpty(packageName)) {
				map.put("packageName", packageName.trim());
			}
			if (StringUtils.isNotEmpty(type) && !type.equals("-1")) {
				map.put("type", Integer.parseInt(type.trim()));
			}

			if (StringUtils.isNotEmpty(state) && !state.equals("-1")) {
				map.put("state", Integer.parseInt(state.trim()));
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<AppInfoConfig> result = appInfoConfigService.searchAppInfoConfig(map, currentPage, pageSize);
			request.setAttribute("result", result);
		} catch (Exception e) {
			log.error("AppInfoConfigController list", e);
			PaginationVo<AppInfoConfig> result = new PaginationVo<AppInfoConfig>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "appInfoConfig/list";
	}

	/** 新增app配置信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		return "appInfoConfig/add";

	}

	/** 新增app配置信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(AppInfoConfig record) {
		try {
			if(record.getType()==2){
				// 文件列表
				AppInfo appinfo0 = new AppInfo();
				appinfo0.setName(record.getName());
				List<AppInfo> appinfos = appInfoService.searchApps(appinfo0);	
				if (CollectionUtils.isEmpty(appinfos)) {
					return "{\"flag\":\"3\"}";//检查文件不存在
				}else{
					record.setName(appinfos.get(0).getName());
				}
			}
			// 根据名字和分类查看是否已经闯存在
			AppInfoConfig con = appInfoConfigService.getAppConfig(record.getName(), record.getType());
			if (con != null) {
				return "{\"flag\":\"2\"}";
			}
			appInfoConfigService.addAppInfoConfig(record);
		} catch (Exception e) {
			log.error("AppInfoConfigController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个app配置分类 */
	@RequestMapping("/{id}")
	public String edit(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
		try {
			// 所有app配置
			AppInfoConfig appConfig = appInfoConfigService.getAppInfoConfig(id);
//			appConfig.setName(appConfig.getName().replaceAll("\"", "&quot;").replaceAll("'", "&apos;"));
			request.setAttribute("appConfig", appConfig);
		} catch (Exception e) {
			log.error("AppInfoConfigController edit", e);
		}
		return "appInfoConfig/edit";
	}

	/** 更新app配置分类 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(AppInfoConfig record) {
		try {
			if(record.getType()==2){
				// 文件列表
				AppInfo appinfo0 = new AppInfo();
				appinfo0.setName(record.getName());
				List<AppInfo> appinfos = appInfoService.searchApps(appinfo0);	
				if (CollectionUtils.isEmpty(appinfos)) {
					return "{\"flag\":\"3\"}";//检查文件不存在
				}else{
					record.setName(appinfos.get(0).getName());
				}
			}
			AppInfoConfig con = appInfoConfigService.getAppConfig(record.getName(), record.getType());
			if (con != null && record.getId()!=con.getId()) {
				return "{\"flag\":\"2\"}";
			}
			appInfoConfigService.upAppInfoConfig(record);
		} catch (Exception e) {
			log.error("AppInfoConfigController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除app配置分类 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		appInfoConfigService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}
	/** 文件检测 */
	@ResponseBody
	@RequestMapping("/checkFile")
	public String checkFile(HttpServletRequest request) {
		try {
			String file = request.getParameter("name");
			if (StringUtils.isNotEmpty(file)) {
				// 文件列表

				AppInfo appinfo0 = new AppInfo();
				appinfo0.setName(file);
				List<AppInfo> appinfos = appInfoService.searchApps(appinfo0);			
				request.setAttribute("appFiles", appinfos);
				if (CollectionUtils.isEmpty(appinfos)) {
					return "{\"success\":\"0\"}";
				} else {
					StringBuilder options = new StringBuilder();
					for (AppInfo appinfo : appinfos) {
						options.append("<input type='radio' name='apkId' id='apkId' value='");
						options.append(appinfo.getId());
						options.append(",");
						options.append(appinfo.getName());
						options.append("'/>");
						options.append(appinfo.getName());
						options.append("</br>");
					}
					return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\"}";
				}
			}
		} catch (Exception e) {
			log.error("AppInfoConfigController info", e);
		}
		return "{\"success\":\"1\",\"option\":\"" + 0 + "\"}";
	}
	
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public String batchImport(
			@RequestParam("myfiles") CommonsMultipartFile mFile,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
//		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		try {
			   InputStream is = mFile.getInputStream();
			   Workbook wbWorkbook  =  readerExcel(is);
			   int n=0,m=0,j=0;//k=0;
			   if(wbWorkbook!=null){
				   Sheet sheet = wbWorkbook.getSheetAt(0);// 获取第一张Sheet表
				   int lastRowNum = sheet.getLastRowNum();// 获取行数
			        for (int i = 1; i <= lastRowNum; i++) { // 行循环	 
			        	Cell nameCell =sheet.getRow(i).getCell(0);
			        	String name="";
			        	if(nameCell!=null){			        		
			        		name = getCellValue(nameCell).toString();
			        	}
			        	Cell packageNameCell =sheet.getRow(i).getCell(1);
			        	String packageName="";
			        	if(packageNameCell!=null){			        		
			        		packageName = getCellValue(packageNameCell).toString().trim();
			        	}
			        	Cell typeCell =sheet.getRow(i).getCell(2);
			        	String type="";
			        	if(typeCell!=null){			        		
			        		type = getCellValue(typeCell).toString();
			        	}
			        	int type0 = getType(type);
			        	Cell descriptionCell =sheet.getRow(i).getCell(3);
			        	String description="";
			        	if(descriptionCell!=null){		
			        		description = getCellValue(descriptionCell).toString();
			        	}
			        	Cell appUrlCell =sheet.getRow(i).getCell(4);
			        	String appUrl="";
			        	if(appUrlCell!=null){			        		
			        		appUrl = getCellValue(appUrlCell).toString();
			        	}
			        	AppInfoConfig con = appInfoConfigService.getAppConfig(name.trim(), type0);
		    			AppInfoConfig config = new AppInfoConfig();
		    			config.setName(name.trim());
		    			config.setType(type0);
		    			config.setState(true);
		    			config.setPackageName(packageName);
		    			config.setDescription(description);
			        	if(appUrl!=null&&!appUrl.equals("")){
			        		config.setAppUrl(appUrl);
//			        		//验证地址是否能用
//			        		try{
//			        			Document doc = Jsoup.connect(appUrl).timeout(10000).get();
//			        			String title = doc.getElementsByTag("title").first().text();
//			        			if(doc.location().contains("404.html")||title.contains("404")){
//			        				k++;
//				        			sb.append(name).append(";");
//			        			}else{			        				
//			        				config.setAppUrl(appUrl);
//			        			}
//			        		}catch(HttpStatusException e){
//			        			//地址不可用
//			        			k++;
//			        			sb.append(name).append(";");	        				
//			        		}catch(Exception e){
//			        			//地址不可用
//			        			k++;
//			        			sb.append(name).append(";");	        				
//			        		}
			        	}
			        	try{
			        		if(name!=null&&!name.equals("")){
			        			sb1.append(name).append(";"); 
			        			if(con==null){
			        				//没有数据,需要添加
			        				appInfoConfigService.addAppInfoConfig(config);
			        				n++;
			        			}else{
			        				config.setId(con.getId());
			        				if(packageName==null||packageName.trim().equals("")){
			        					config.setPackageName(con.getPackageName());	
			        				}
			        				appInfoConfigService.upAppInfoConfig(config);
			        				m++;
			        			}		    			
			        			
			        		}			        		
			        	}catch(Exception e){
			        		j++;
			        		sb2.append(name).append(";");
			        	}

			        }
			        pw.println("总共需处理配置"+(lastRowNum)+"条!");
			        pw.println("新增配置"+n+"条!");
			        pw.println("修改配置 "+m+"条!");
			        StringBuilder res = new StringBuilder("总共需处理配置"+(lastRowNum)+"条,具体如下:");
			        if(sb1!=null&&sb1.length()>1){
			        	res = res.append(sb1.substring(0,sb1.length()-1));			        
			        }
			        res = res.append("\n其中新增配置 "+n+"条, 修改配置  "+m+"条!\n");
			        if(sb2!=null&&sb2.length()>1){
			        	res =res.append(sb2.substring(0,sb2.length()-1));
			        	pw.println(sb2.substring(0,sb2.length()-1));
			        	res = res.append("以上 "+j+"条数据不合理,插入失败\n!");
			        	pw.println("以上 "+j+"条数据不合理,插入失败\n!");
			        }
//			        if(sb!=null&&sb.length()>1){
//			        	res =res.append(sb.substring(0,sb.length()-1));
//			        	pw.println(k+"条路径无效,详情请看日志系统!");
//			        	res = res.append("\n以上 "+k+"条路径无效 !/app配置管理");
//			        }
			        addLog(res.toString(),1);
			        return null;
			   }else{
				    pw.println("读取文件失败!!");
				    String res ="读取文件失败!!";
				    addLog(res,0);
			        return null; 
			   }			
		} catch (Exception e) {
			log.error("AppInfoConfigController batchImport", e);
			pw.println("批量导入配置失败!!");
			String res ="批量导入配置失败!!";
			addLog(res,0);
			return null;
		}
	}

	private void addLog(String res,int success) {
		HttpSession s = (HttpSession) RequestContextHolder
				.currentRequestAttributes().resolveReference(
						RequestAttributes.REFERENCE_SESSION);
		User user = (User) s.getAttribute("loginUser");
		if(user!=null){
			 com.mas.rave.main.vo.Log log = new com.mas.rave.main.vo.Log();
			 log.setAction("批量导入");
			 log.setOperator(user.getName());
			 log.setSucc(success);
			 log.setRes(res);
			 logService.addLog(log);
		}
	}
 

	private int getType(String type) {
	  int type0 = 1;
	  if(type!=null&&!type.equals("")){
		  if(type.equals("自动更新")||type.equals("3")||type.equals("3.0")){
			  type0 = 3;
		  }else if(type.equals("需要增量")||type.equals("2")||type.equals("2.0")){
			  type0 = 2;
		  }else if(type.equals("黑名单")||type.equals("0")||type.equals("1.0")){
			  type0 = 0;
		  }else{
			  type0 = 1;
		  }
		  
	  }
		return type0;
	}
	
	 public static Workbook readerExcel( InputStream is) {
         try {
        	 if (!is.markSupported()) {
                 is = new PushbackInputStream(is, 8);
             }
        	 if (POIFSFileSystem.hasPOIFSHeader(is)) {
        		 return new HSSFWorkbook(is);
        	 }
			if (POIXMLDocument.hasOOXMLHeader(is)) {
			     return new XSSFWorkbook(OPCPackage.open(is));
			 }
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	 
	/**
	 * 获取列的数据信息
	 * 
	 * @param cell
	 * @return
	 */
	private Object getCellValue(Cell cell) {
		Object cellValue = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			cellValue = "";
			break;
		case Cell.CELL_TYPE_ERROR:
			cellValue = Byte.toString(cell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			double number = cell.getNumericCellValue();
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = getTime(number);
			} else {
				cellValue = Integer.toString((int) cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellValue = cell.getCellFormula();
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}

	/**
	 * [正确地处理整数后自动加零的情况]</li>
	 * 
	 * @param sNum
	 * @return
	 */
	private static String getTime(double daynum) {
		double totalSeconds = daynum * 86400.0D;
		// 总的分钟数
		int seconds = (int) totalSeconds / 60;
		// 实际小时数
		int hours = seconds / 60;
		int minutes = seconds - hours * 60;
		// 剩余的实际分钟数
		StringBuffer sb = new StringBuffer();
		if (String.valueOf(hours).length() == 1) {
			sb.append("0" + hours);
		} else {
			sb.append(hours);
		}
		sb.append(":");
		if (String.valueOf(minutes).length() == 1) {
			sb.append("0" + minutes);
		} else {
			sb.append(minutes);
		}
		return sb.toString();
	}
}
