package com.mas.testIn;

import com.mas.util.VM;

public class Config
{
  static Integer prodId = Integer.valueOf(10004);

  static String cloud = "adapt.MAS";

  static String API_KEY = "678c5eafa32579e303adac9032f39273";

  static String SECRET_KEY = "350ABE870E26335F";

  static String serviceUrl = "http://sd.testin.cn";

  static String testInEmail = VM.getInatance().getProperty("testInEmail");

  static String testInPassword = VM.getInatance().getProperty("testInPassword");
  
  static String testInPath = VM.getInatance().getProperty("testInPath");

  static String testInPathReal = VM.getInatance().getProperty("testInPathReal");

  static String testInResServer = VM.getInatance().getProperty("testInResServer");

  static String testInPeportBackUrl = VM.getInatance().getProperty("testInPeportBackUrl");
}