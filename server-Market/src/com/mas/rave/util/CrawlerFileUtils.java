package com.mas.rave.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.mas.rave.appannie.ProxyService;

public class CrawlerFileUtils {
	 public final static boolean DEBUG = true; //调试控制
	 private static int BUFFER_SIZE = 20140; //缓冲区大小
	 static Logger log = Logger.getLogger(CrawlerFileUtils.class);
   
	    public static void writeNativeFile(String categoryFile,String str) throws IOException{
	    	ProxyService.closeProxy();
			FileWriter fw = new FileWriter(categoryFile, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(str);
			pw.close(); 
			fw.close();
	    }

		/**
		 * @param urlPath 图片路径
		 * @throws Exception 
		 */
		public static void writeNetWorkFile(String urlPath,String fileName) throws Exception{
			ProxyService.closeProxy();	
			URL url = new URL(urlPath);//：获取的路径
			//:http协议连接对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(6 * 1000);
			if (conn.getResponseCode() <10000){
				InputStream inputStream = conn.getInputStream();
				byte[] data = readStream(inputStream);
				if(data.length>(1024*10)){ 
					FileOutputStream outputStream = new FileOutputStream(fileName);
					outputStream.write(data);
//					System.err.println("�?+ ++DOWN_COUNT +"图片下载成功");
					outputStream.close();
				}
			}
			
		}
		/**
		 * 创建文件
		 * @param fileName
		 * @throws UnsupportedEncodingException 
		 */
		public static void createFile(String fileName) throws UnsupportedEncodingException{
			//log.info("System.getProperty('file.encoding'))  "+System.getProperty("file.encoding"));
	    	File file  = new File(new String(fileName.getBytes("UTF-8"),System.getProperty("file.encoding")));
	    	if(!file.exists()){
	    		    if (!file.getParentFile().exists()) {
	    			   file.getParentFile().mkdirs();
	    			  }
	    			  try {
	    			   file.createNewFile();
	    			  } catch (IOException e) {
	    			   e.printStackTrace();
	    			  }
	    	}
	    }
	    
	    
		/**
		 * 读取url中数据，并以字节的形式返�?
		 * @param inputStream
		 * @return
		 * @throws Exception
		 */
		public static byte[] readStream(InputStream inputStream) throws Exception{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = inputStream.read(buffer)) !=-1){
				outputStream.write(buffer, 0, len);
			}
			outputStream.close();
			inputStream.close();
			return outputStream.toByteArray();
		}
		   /**
	     * 将HTTP资源另存为文�?
	     *
	     * @param destUrl String
	     * @param fileName String
		 * @throws IOException 
	     * @throws Exception
	     */
	public static void saveToFile(String destUrl, String fileName)
			throws IOException {
		ProxyService.closeProxy();
		File toFile = new File(fileName);
		if (!toFile.exists()) {
			createFile(fileName);
		}
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		// 建立链接
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		// 连接指定的资源
		httpUrl.connect();
		httpUrl.setConnectTimeout(5000);
		// 获取网络输入流
		bis = new BufferedInputStream(httpUrl.getInputStream());
		// 建立文件
		fos = new FileOutputStream(fileName);

		if (DEBUG)
			log.info("正在获取链接[" + destUrl + "]的内容..\n将其保存为文件[" + fileName + "]");
		// 保存文件
		while ((size = bis.read(buf)) != -1){
			ProxyService.closeProxy();
			fos.write(buf, 0, size);
		}

		fos.close();
		bis.close();
		httpUrl.disconnect();

	}	    		
	       //获取网络文件，转存到fileDes中，fileDes�?��带文件后�?��   
	        public static void saveUrlFile(String fileUrl,String fileDes) throws Exception  
	        {  
	        	ProxyService.closeProxy();
	           File toFile = new File(fileDes);  
	            if (toFile.exists())  
	            {  
	    //          throw new Exception("file exist");   
	               return;  
	            }  
	            createFile(fileDes);  
	            FileOutputStream outImgStream = new FileOutputStream(toFile);  
	            outImgStream.write(getUrlFileData(fileUrl));  
	            outImgStream.close();  
	       }  
	      //获取链接地址文件的byte数据   
	          public static byte[] getUrlFileData(String fileUrl) throws Exception  
	         {  
			      ProxyService.closeProxy();
	              URL url = new URL(fileUrl);  
	              HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	              httpConn.connect();  
	              httpConn.setConnectTimeout(5000);
	              InputStream cin = httpConn.getInputStream();  
	              ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	               byte[] buffer = new byte[1024*100];  
	              int len = 0;  
	               while ((len = cin.read(buffer)) != -1) {  
	                    outStream.write(buffer, 0, len);  
	                }  
	               cin.close();  
	              byte[] fileData = outStream.toByteArray();  
	                outStream.close();  
	              return fileData;  
	            } 
	    	  /**
	  	     * 写txt文件
	  	     * @param filePath
	  	     * @param str
	    	 * @throws UnsupportedEncodingException 
	  	     */
	  	    public static void writeTextFile(String filePath,String str) throws UnsupportedEncodingException{
				ProxyService.closeProxy();
	  	    	BufferedWriter br=null;
	  	    	CrawlerFileUtils.createFile(filePath);
	  	    	try {
	  				br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
	  				
	  				br.write(str);
	  			} catch (UnsupportedEncodingException e1) {
	  				// TODO Auto-generated catch block
	  				e1.printStackTrace();
	  			} catch (FileNotFoundException e1) {
	  				// TODO Auto-generated catch block
	  				e1.printStackTrace();
	  			} catch (IOException e) {
	  				// TODO Auto-generated catch block
	  				e.printStackTrace();
	  			} finally{
	  				try {
	  					br.close();
	  				} catch (IOException e) {
	  					// TODO Auto-generated catch block
	  					e.printStackTrace();
	  				}
	  			} 

	  	    }
	  		/**
	  		 * 获取文件后缀
	  		 * @param filename
	  		 * @return
	  		 */
	  		public static String getExtension(String filename) {   
	  			   if ((filename != null) && (filename.length() > 0)) {   
	  			        int i = filename.lastIndexOf('.');   
	  			       if ((i >-1) && (i < (filename.length()))) {   
	  			            return filename.substring(i, filename.length());   
	  			       }   
	  			    }   
	  			    return filename;   
	  		}

}
