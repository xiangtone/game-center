package com.mas.testIn;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mas.util.HttpUtils;

public class TestInUtil
{
  private String serviceUrl;
  private String sid;
  private JSONArray models;

  public static void main(String[] args)
  {
    TestInUtil postInfo = new TestInUtil();
    String adaptId = postInfo.add("com.averygoodgame4you.games.f2f1310002.lite_9990000_play_16_", "1.5.1.007", 
      "http://203.90.239.11:8884/1/300001/100003/1/apk/market/com.mas.amineappstore_1.3.1.007_10007.apk");
    System.out.println("adaptId==========" + adaptId);
  }
  TestInUtil() {
    this.serviceUrl = getServiceUrl(Config.serviceUrl);
    this.sid = login(this.serviceUrl);
    this.models = getSpecimens(this.serviceUrl, this.sid, Config.cloud, "android");
  }

  protected String getServiceUrl(String serviceUrl)
  {
    JSONObject jsonObj = new JSONObject();
    try {
      jsonObj.put("op", "Dispatch.list");
      jsonObj.put("apikey", Config.API_KEY);
      jsonObj.put("timestamp", System.currentTimeMillis());

      String result = HttpUtils.doPostJson(serviceUrl + "/mcfg/mcfg.action", jsonObj.toString());
      JSONObject json = new JSONObject(result);
System.out.println(json);
      if (json.optInt("code") == 0) {
        JSONObject jsonData = json.optJSONObject("data");
        JSONArray arrayDispatches = jsonData.optJSONArray("dispatches");
        if ((arrayDispatches != null) && (arrayDispatches.length() > 0)) {
          JSONObject jsonDispatch = arrayDispatches.optJSONObject(0);
          StringBuffer url = new StringBuffer("http://");
          url.append(jsonDispatch.optString("externalIp"));
          url.append(":");
          url.append(jsonDispatch.optInt("externalPort"));
          return url.toString();
        }
      }
      else {
        System.out.println("调用 Dispatch.list 获取接口服务地址失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String login(String serviceUrl)
  {
    JSONObject jsonObj = new JSONObject();
    try {
      jsonObj.put("op", "Login.login");
      jsonObj.put("apikey", Config.API_KEY);
      jsonObj.put("timestamp", System.currentTimeMillis());

      jsonObj.put("email", Config.testInEmail);
      jsonObj.put("pwd", Config.testInPassword);

      String result = HttpUtils.doPostJson(serviceUrl + "/sso/user.action", jsonObj.toString());
      if (StringUtils.isBlank(result)) {
        return result;
      }
      JSONObject json = new JSONObject(result);
      if (json.optInt("code") == 0) {
        JSONObject jsonData = json.optJSONObject("data");
        System.out.println("调用 Login.login 用户登录接口  成功");
        return jsonData.optString("sid");
      }
      System.out.println("调用 Login.login 用户登录接口失败");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public JSONArray getSpecimens(String serviceUrl, String sid, String cloud, String syspfName)
  {
    JSONObject jsonObj = new JSONObject();
    try {
      jsonObj.put("op", "Model.getSpecimens");
      jsonObj.put("apikey", Config.API_KEY);
      jsonObj.put("timestamp", System.currentTimeMillis());
      jsonObj.put("sid", sid);
      jsonObj.put("cloud", cloud);
      jsonObj.put("syspfName", syspfName);

      String result = HttpUtils.doPostJson(serviceUrl + "/deviceunit/cfg.action", jsonObj.toString());
      if (StringUtils.isBlank(result)) {
        return null;
      }
      JSONObject json = new JSONObject(result);
      if (json.optInt("code") == 0) {
        JSONObject jsonData = json.optJSONObject("data");
        JSONArray list = jsonData.optJSONArray("list");
        if ((list != null) && (list.length() > 0))
          return list;
      }
      else {
        System.out.println("调用 Model.getSpecimens 获取测试机型接口失败");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String add(String appName, String appVersion, String packageUrl)
  {
	//System.out.println("serviceUrl=:"+serviceUrl+"-----sid=:"+sid);
    return add(this.serviceUrl, this.sid, this.models, appName, appVersion, packageUrl);
  }
  public String add(String serviceUrl, String sid, JSONArray models, String appName, String appVersion, String packageUrl) {
    JSONObject jsonObj = new JSONObject();
    try {
      jsonObj.put("op", "App.add");
      jsonObj.put("apikey", Config.API_KEY);
      jsonObj.put("timestamp", System.currentTimeMillis());
      jsonObj.put("sid", sid);
      jsonObj.put("appName", appName);
      jsonObj.put("appVersion", appVersion);
      jsonObj.put("subSource", "test111");
      jsonObj.put("packageUrl", packageUrl);
      jsonObj.put("syspfId", 1);
      jsonObj.put("testType", 0);
      jsonObj.put("script", "");
      jsonObj.put("prodId", Config.prodId);
      jsonObj.put("cloud", Config.cloud);
      jsonObj.put("safeDetect", 0);
      jsonObj.put("secrecy", 1);
      jsonObj.put("models", models);

      JSONObject jsonArray = new JSONObject();
      jsonArray.put("accountId", Config.testInEmail);
      jsonArray.put("accountPwd", Config.testInPassword);
      jsonObj.put("accounts", jsonArray);
      jsonObj.put("callbackUrl", Config.testInPeportBackUrl);
      //System.out.println("add--------json:"+jsonObj.toString());
      String result = HttpUtils.doPostJson(serviceUrl + "/realtest/nativeapp.action", jsonObj.toString());
      if (StringUtils.isBlank(result)) {
        return null;
      }

      JSONObject json = new JSONObject(result);
      //System.out.println("json====:" + json);
      if (json.optInt("code") == 0) {
        JSONObject jsonData = json.optJSONObject("data");
        return jsonData.optString("result");
      }
      System.out.println("调用 App.add 创建测试接口失败");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  public String getServiceUrl() {
    return this.serviceUrl;
  }
  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }
  public String getSid() {
    return this.sid;
  }
  public void setSid(String sid) {
    this.sid = sid;
  }
  public JSONArray getModels() {
    return this.models;
  }
  public void setModels(JSONArray models) {
    this.models = models;
  }
}