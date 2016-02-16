package com.reportforms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.reportforms.template.XMLModelTemplate;

public class TemplateUtil {
	
	private static final Logger logger = Logger
			.getLogger(TemplateUtil.class);
	
	private static String xml_dir = "template";
	
	private static String[] STATISTICS_MODEL_S = {"musicdownload","imagedownload","appdownload","appdistribute"};
	
	/**
	 * 读取XML模板定义文件,获得模板对象
	 */
	public static XMLModelTemplate readXml2Template(
			HttpServletRequest request, XMLModelTemplate obj) {

		String contextPath = request.getSession().getServletContext()
				.getRealPath("/");

		String xmlName = request.getParameter("xmlName");
		if (StringUtils.isEmpty(xmlName)) {
			logger.info("the template xml is not find !");
			return null;
		}
		String filePath = contextPath + xml_dir + File.separator + xmlName;
		logger.info("xml file path is :" + filePath);
		XMLModelTemplate template = null;
		try {
			InputStream is = new FileInputStream(filePath);
			String xml = IOUtils.toString(is, "UTF-8");
			template = (XMLModelTemplate) XMLParserXStreamImpl
					.formXML(xml, obj);
		} catch (Exception e) {
			logger.error("xml format to object error : ", e);
		}

		return template;
	}
	
	/**
	 * 判断该模板是否需要统计
	 * @param value
	 * @return
	 */
	public static boolean containStatisticsModels(String value){
		return ArrayUtils.contains(STATISTICS_MODEL_S, value);
	}
	
}
