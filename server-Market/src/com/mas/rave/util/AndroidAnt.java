package com.mas.rave.util;
import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;


public class AndroidAnt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File  file = new File("E:/ant_android/AndroidAnt/build.xml");
		Project project = new Project();
		project.fireBuildStarted();
		project.init();
		
	    ProjectHelper helper=ProjectHelper.getProjectHelper();
        //������Ŀ�Ĺ����ļ�
        helper.parse(project, file);
        //ִ����Ŀ��ĳһ��Ŀ��
        project.executeTarget(project.getDefaultTarget());
        project.fireBuildFinished(null);
	}

}
