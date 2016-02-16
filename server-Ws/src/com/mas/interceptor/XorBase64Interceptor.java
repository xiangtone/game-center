package com.mas.interceptor;

import java.io.InputStream;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.LoggerFactory;

import com.mas.util.InputStreamUtils;
import com.mas.util.XorBase64;


public class XorBase64Interceptor extends AbstractPhaseInterceptor<Message> {
	protected static final org.slf4j.Logger log = LoggerFactory.getLogger(XorBase64Interceptor.class);

	public XorBase64Interceptor() {
		//这儿使用receive，接收的意思
		super(Phase.RECEIVE);
	}
	public void handleMessage(Message message){
        try {
        	String query = (String) message.get(Message.QUERY_STRING);
			if (query != null)
			{
				log.info(query);
				if(query.indexOf("O=0")>=0){
					return;
				}
			}
			InputStream is = message.getContent(InputStream.class);
			String s = InputStreamUtils.InputStreamTOString(is);
			//这里可以对流做处理，从流中读取数据，然后修改为自己想要的数据
			//处理完毕，写回到message中
			//……………………处理代码……………………
			InputStream   inputStream   =  InputStreamUtils.StringTOInputStream(XorBase64.getDecodeXorBase64(s));
			if(is != null)
				message.setContent(InputStream.class, inputStream);
        } catch (Exception e) {
        	log.error("Error when split original inputStream. CausedBy : "+"\n"+e);
		}
	}
}
