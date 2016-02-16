package com.mas.testIn;

import java.io.IOException;
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

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mas.market.pojo.TAppFile;
import com.mas.market.pojo.TAppTestin;
import com.mas.market.service.TAppFileService;
import com.mas.market.service.TAppTestinService;
import com.mas.util.VM;

@WebServlet({"/apptest"})
public class AcceptAppIdServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private TAppTestinService tAppTestinService;
  private TAppFileService tAppFileService;

  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
    ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    this.tAppTestinService = ((TAppTestinService)context.getBean(TAppTestinService.class));
    this.tAppFileService = ((TAppFileService)context.getBean(TAppFileService.class));
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");
    Map<String,Object> map = new HashMap<String,Object>();
    String appIds = request.getParameter("appIds");
    map.put("appIds", appIds);
    if (appIds == null) {
      request.getRequestDispatcher("testInAppId.jsp").forward(request, response);
    } else {
      String[] appidArray = appIds.split(",");
      map.put("success", Boolean.valueOf(true));
      map.put("testInEmail", Config.testInEmail);
      map.put("testInPassword", Config.testInPassword);
      StringBuffer describe = new StringBuffer("");
      for (int i = 0; i < appidArray.length; i++) {
        try {
          Integer appId = Integer.valueOf(Integer.parseInt(appidArray[i]));
          List<TAppFile> list = this.tAppFileService.getApkForAppId(appId);
          if ((list != null) && (list.size() > 0)) {
            TestInUtil postInfo = new TestInUtil();
            for (TAppFile apk : list) {
              System.out.println(apk.getAppId() + "-" + apk.getId() + "  " + apk.getAppName() + "  " + apk.getVersionName() + "  " + VM.getInatance().getResServer() + apk.getUrl());
              String adaptId = postInfo.add(apk.getAppName(), apk.getVersionName(), VM.getInatance().getResServer() + apk.getUrl());
              if (adaptId == null) {
                map.put("success", Boolean.valueOf(false));
                map.put("describe", "testIn is download!");
              } else {
                describe.append("<a href='"+VM.getInatance().getResServer() + apk.getUrl()+"'>"+apk.getAppId() + "-" + apk.getAppName() + "(" + apk.getVersionName() + ")</a>\t<br>\t");
                TAppTestin tAppTestin = new TAppTestin();
                tAppTestin.setAdaptId(adaptId);
                tAppTestin.setApkId(apk.getId());
                tAppTestin.setAppId(apk.getAppId());
                tAppTestin.setAppName(apk.getAppName());
                tAppTestin.setVersionName(apk.getVersionName());
                tAppTestin.setVersionCode(apk.getVersionCode());
                tAppTestin.setPackageName(apk.getPackageName());
                tAppTestin.setCreateTime(new Date());
                this.tAppTestinService.insert(tAppTestin);
              }
            }
          } else {
            map.put("success", Boolean.valueOf(false));
            map.put("describe", "appId is not find!");
          }
        } catch (Exception e) {
          e.printStackTrace();
          map.put("success", Boolean.valueOf(false));
          map.put("describe", "appId is not find and Exception!");
        }
      }
      if (((Boolean)map.get("success")).booleanValue()) map.put("describe", describe);
      request.setAttribute("map", map);
      request.getRequestDispatcher("testInAppId.jsp").forward(request, response);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }
}