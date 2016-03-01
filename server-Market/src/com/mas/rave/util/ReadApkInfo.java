package com.mas.rave.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadApkInfo {
	public static String getVersionName(String apkPath, String path, int flag)
	  {
	    String versionName = "";
	    try
	    {
	      Runtime rt = Runtime.getRuntime();
	      Process proc = null;
	      if (flag == 1)
	      {
	        String order = path + "aapt.exe" + " d badging \"" + apkPath;
	        proc = rt.exec(order);
	      }
	      else
	      {
	        rt.exec("chmod +x " + path + "aapt");
	        String[] str = { "/bin/sh", "-c", path + "aapt d badging " + apkPath };
	        proc = rt.exec(str);
	      }
	      if (proc == null) {
	        return null;
	      }
	      InputStream stderr = proc.getInputStream();
	      InputStreamReader isr = new InputStreamReader(stderr);
	      BufferedReader br = new BufferedReader(isr);
	      String line = null;
	      while ((line = br.readLine()) != null) {
	        if (line.contains("versionName"))
	        {
	          String st1 = line.substring(line.lastIndexOf("versionName"), line.length());
	          versionName = st1.substring(st1.indexOf("'"), st1.length()).replace("'", "").trim();
	          break;
	        }
	      }
	    }
	    catch (Throwable t)
	    {
	      t.printStackTrace();
	    }
	    return versionName;
	  }
}
