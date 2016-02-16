package com.mas.testIn;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mas.market.pojo.TAppTestin;
import com.mas.market.service.TAppTestinService;

@WebServlet({"/report"})
public class TestInCallbackServlet extends HttpServlet
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
    String json = readJSONString(request);
    JSONObject jsonObject = (JSONObject)JSON.parse(json);
    String reportUrl = jsonObject.getString("reportUrl");
    String adaptId = jsonObject.getString("adaptId");
    TAppTestin tAppTestin = new TAppTestin();
    tAppTestin.setAdaptId(adaptId);
    tAppTestin.setReportUrl(reportUrl);
    tAppTestin.setUpdateTime(new Date());
    this.tAppTestinService.updateByPrimaryKey(tAppTestin);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }
  private String readJSONString(HttpServletRequest request) {
    StringBuffer json = new StringBuffer();
    String line = null;
    try {
      BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null)
        json.append(line);
    }
    catch (Exception e) {
      System.out.println(e.toString());
    }
    return json.toString();
  }
}