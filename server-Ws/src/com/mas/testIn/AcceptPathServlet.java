package com.mas.testIn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mas.market.pojo.TAppTestin;
import com.mas.market.service.TAppTestinService;

@WebServlet({"/gotestin"})
public class AcceptPathServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private TAppTestinService tAppTestinService;

  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
    ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    this.tAppTestinService = ((TAppTestinService)context.getBean(TAppTestinService.class));
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");
    Map<String,Object> map = new HashMap<String,Object>();
    String path = request.getParameter("path");
    map.put("testInPath", Config.testInPath);
    if(null!=path){
    	List<String> fileNameList = getPathFiles(Config.testInPath);
    	map.put("success", Boolean.valueOf(true));
    	map.put("testInEmail", Config.testInEmail);
    	map.put("testInPassword", Config.testInPassword);
    	StringBuffer describe = new StringBuffer("");
    	if(fileNameList.size()>0){
    		TestInUtil postInfo = new TestInUtil();
    		for(String fileName:fileNameList){
    			String adaptId = postInfo.add(fileName.split(File.separator)[1], "", Config.testInResServer+fileName);
    			if (adaptId == null) {
    				map.put("success", Boolean.valueOf(false));
    				map.put("describe", "testIn is download!");
    			}else{
    				describe.append("<a href='"+Config.testInResServer+fileName+"'>"+fileName.split(File.separator)[1] + "</a>\t<br>\t");
    				TAppTestin tAppTestin = new TAppTestin();
    				tAppTestin.setAdaptId(adaptId);
    				tAppTestin.setAppName(fileName);
    				tAppTestin.setCreateTime(new Date());
    				this.tAppTestinService.insert(tAppTestin);
    			}
    		}
    		if (((Boolean)map.get("success")).booleanValue()) map.put("describe", describe);
    	}else{
    		map.put("success", Boolean.valueOf(false));
			map.put("describe", "the ftppath: "+Config.testInPath+"  no apk!");
    	}
    }
    request.setAttribute("map", map);
    request.getRequestDispatcher("testInPath.jsp").forward(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }
  
  private List<String> getPathFiles(String path){
	  List<String> fileNameList = new ArrayList<String>();
	  File dir = new File(path);
      File[] list = dir.listFiles();
      for (int i=0; i<list.length; i++) {
           if (list[i].isFile()) {
        	   String fileName = list[i].getName();
               System.out.println("File "+fileName);
               int dot = fileName.lastIndexOf('.');
   			   if ((dot > -1) && (dot < (fileName.length() - 1))) {
   				     if(fileName.substring(dot + 1).toLowerCase().equals("apk")){
   				    	 try {
   				    		 String destFile = System.currentTimeMillis()+File.separator+fileName;
   				    		 FileUtils.moveFile(new File(path+File.separator+fileName),new File(Config.testInPathReal+File.separator+destFile));
   				    		 fileNameList.add(destFile);
   				    	 } catch (IOException e) {
   				    		 // TODO Auto-generated catch block
   				    		 e.printStackTrace();
   				    	 }
   				     }
               }
            } else if (list[i].isDirectory()) {
               //System.out.println("Directory "+list[i].getName());
           }
      }
      return fileNameList;
  }
}