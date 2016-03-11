package com.mas.rave.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil2 {
	public static ArrayList<String> readFileToList(String filePath,String chartSet)
	{
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			fis = new FileInputStream(filePath);
			isr = new InputStreamReader(fis,chartSet);
			br = new BufferedReader(isr);
			String line = null;  
			while ((line = br.readLine()) != null) 
			{   
				if(!"".equals(line))
					list.add(line);  
			}  
			br.close();  
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			try{if(fis!=null)fis.close();}catch(Exception ex){};
			try{if(isr!=null)isr.close();}catch(Exception ex){};
			try{if(br!=null)br.close();}catch(Exception ex){};
		}
		
		return list;
	}
	
	public static ArrayList<String> getChildFilePathsFromPath(String path)
	{
		ArrayList<String> list = new ArrayList<>();
		try
		{
			File basePath = new File(path);
			File[] files = basePath.listFiles();
		    for(File file:files)
		    {     
		    	if(!file.isDirectory())
		    	{
		    		list.add(file.getName());
		    	}
		    }
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return list;
	}
	
	public static boolean saveDataToFile(List<String> list,String path,String fileName)
	{
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try
		{
			File basePath = new File(path);
			
			if(!basePath.exists())
				return false;
			
			File writeFile = new File( path + fileName);
			
			if(!writeFile.exists())
			{
				boolean result = writeFile.createNewFile();
				System.out.println("result:" + result);
			}
			
			fos = new FileOutputStream(writeFile);
			osw = new OutputStreamWriter(fos,"UTF-8");
			bw = new BufferedWriter(osw);
			
			for(String data : list)
			{
				bw.write(data + "\r\n");
			}
			
			bw.flush();
			
			return true;
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			try{if(bw!=null)bw.close();}catch(Exception ex){};
			try{if(osw!=null)osw.close();}catch(Exception ex){};
			try{if(fos!=null)fos.close();}catch(Exception ex){};
		}
		
		return false;
	}
	
	public static void main(String[] args)
	{
		List<String> list = new ArrayList<>();
		
		list.add("你好吗，顶你个肺");
		
		list.add("数据报表->浩天数据->当日数据");
		list.add("//这里采用GBK编码，而不用环境编码格式，因为环境默认编码不等于操作系统编码");
		list.add("StringBuffer sb = new StringBuffer(); ");
		list.add("while((length = isr.read(buffer, 0, 1024) ) != -1)");
		list.add("通过路径获取文件的内容，这个方法因为用到了字符串作为载体，为了正确读取文件（不乱码");
		list.add("将要写入到文件中的字节数据");
		
		//System.out.println(saveDataToFile(list, "F:/Test/", "tbl_mr_2015121417.txt"));
		
		for(String s : readFileToList("F:/Test/tbl_mr_2015121417.txt", "UTF-8"))
		{
			System.out.println(s);
		}
	}
}
