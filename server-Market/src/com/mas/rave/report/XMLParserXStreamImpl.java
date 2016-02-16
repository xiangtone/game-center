package com.mas.rave.report;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

public class XMLParserXStreamImpl  {
	private static Logger log = Logger.getLogger(XMLParserXStreamImpl.class);
	private final static String encode="UTF-8";
	public final static String XML_HEAD="<?xml version=\"1.0\" encoding=\""+encode+"\" ?>\n";
	public static Object formXML(String xmlString,Object object) {
		log.debug("execute Object formXML(String xmlString="+xmlString+",Object object="+object+") ");
//		System.out.println("execute Object formXML(String xmlString="+xmlString+",Object object="+object+") ");
		XStream xstream = new XStream(new Xpp3Driver(new XmlFriendlyNameCoder("_-", "_")));
		// 非常重要，使用声明特性。
		//xstream.registerLocalConverter(definedIn, fieldName, converter);
		xstream.processAnnotations(object.getClass());
		return xstream.fromXML(xmlString);
	}

	public static Object formXML(byte[] body,Object object) {
		log.debug("execute Object toXMLString(byte[] body="+body+",Object object="+object+") ");
		if(null == body || body.length <= 0)
		{
			return null;
		}
		XStream xstream = new XStream(new Xpp3Driver(new XmlFriendlyNameCoder("_-", "_")));
		// 非常重要，使用声明特性。	
		xstream.processAnnotations(object.getClass());
		InputStream in = new ByteArrayInputStream(body);
		return (Object)xstream.fromXML(in);
	}
	public static String toXMLStringAppendXmlHead(Object object) throws IOException {
		log.debug("execute Object toXMLString(Object object="+object+") ");
		//XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
		XStream xstream = new XStream(new Xpp3Driver(new XmlFriendlyNameCoder("_-", "_")));
		// 非常重要，使用声明特性。
		xstream.processAnnotations(object.getClass());
		ByteArrayOutputStream outputStream=null;
		Writer writer=null;
		try {
			outputStream = new ByteArrayOutputStream();

			writer = new OutputStreamWriter(outputStream,encode);
			writer.write(XML_HEAD);
			xstream.toXML(object, writer);
			String xml = outputStream.toString(encode);
//			System.out.println("xml=========:"+xml);
			return xml;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			if(outputStream!=null){
				outputStream.close();
			}
			if(writer!=null){
				writer.close();
			}
		}
	}
	
	
	public static String toXMLString(Object object) throws IOException {
		log.debug("execute Object toXMLString(Object object="+object+") ");
		//XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
		XStream xstream = new XStream(new Xpp3Driver(new XmlFriendlyNameCoder("_-", "_")));
		// 非常重要，使用声明特性。
		xstream.processAnnotations(object.getClass());
		ByteArrayOutputStream outputStream=null;
		Writer writer=null;
		try {
			outputStream = new ByteArrayOutputStream();

			writer = new OutputStreamWriter(outputStream,encode);
			//writer.write("<?xml version=\"1.0\" encoding=\""+encode+"\" ?>\n");
			xstream.toXML(object, writer);
			String xml = outputStream.toString(encode);;
			return xml;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			if(outputStream!=null){
				outputStream.close();
			}
			if(writer!=null){
				writer.close();
			}
		}
	}
	
	

	

	public static void main(String[] args) throws Exception{
		String enCode="UTF-8";
		XMLParserXStreamImpl xmlParser = new XMLParserXStreamImpl();
		String xmlString="<?xml version=\"1.0\" encoding=\"UTF-8\"?>      "+
		"<g3quay>                                    "+
		"   <app_name>g3quay</app_name>              "+
		"<locationList>                              "+
		"<location>                                  "+
		"         <cid>234</cid>                     "+
		"          <lac>232324</lac>                 "+
		"       </location>                          "+
		"       <location>                           "+
		"          <cid>234</cid>                    "+
		"          <lac>232324</lac>                 "+
		"     </location>                            "+
		"</locationList>                             "+
		"</g3quay>                                   ";
	//	System.out.println(xmlString);
	//	com.aspire.g3.inf.celllocation.UploadCellLocationReq req=new com.aspire.g3.inf.celllocation.UploadCellLocationReq();
		//req=(com.aspire.g3.inf.celllocation.UploadCellLocationReq)xmlParser.formXML(xmlString.getBytes(),req);
		
	//	System.out.println(xmlParser.toXMLString(req));
		
	//	System.out.println(" "+req.getApp_name()+" "+req.getLocationList().getLocation());
		 
	}
}

