package com.mas.rave.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SourceTest
{
	public static List<String> joinDependency(List<String> sourceList)
	{
		List<String> list = new ArrayList<>();
		
		boolean isStart = false;
		
		String result = "";
		
		String groupId = "";
		String artifactId = "";
		String version = "";
				
		
		for(String str : sourceList)
		{
			if("<dependency>".equalsIgnoreCase(str.trim()))
			{
				isStart = true;
			}
			
			if(isStart)
			{
				if(str.contains("groupId"))
				{
					groupId = str;
				}
				else if(str.contains("artifactId"))
				{
					artifactId = str;
				}
				else if(str.contains("version"))
				{
					version = str;
				}
				result += str + "|";
			}
			
			if("</dependency>".equalsIgnoreCase(str.trim()))
			{
				isStart = false;
				list.add(artifactId + groupId + version + "#" + result);
				result = "";
			}
		}
		
		return list;
	}
	
	public static void main(String[] args)
	{
		List<String> list = joinDependency(FileUtil2.readFileToList("F:/WorkSpace/GIT_SPACE/game-center/server-Market/pom.xml", "UTF-8"));
		
		Collections.sort(list);
		
		List<String>  deResult = new ArrayList<>();
		
		for(String s : list)
		{
			deResult.add(s.substring(s.indexOf("#") + 1,s.length()).replaceAll("\\|", "\r\n"));
		}
		
		for(String s : deResult)
		{
			System.out.println(s);
		}
	}
}
