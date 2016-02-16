package com.reportforms.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doc")
public class DocumentAction {
	
	//private final Logger log = LoggerFactory.getLogger(DocumentAction.class);
	
	private final String FILE_PATH = "/doc/";
	
	private String FILE_NAME = "zApp报表系统使用说明书.docx";
	
	@RequestMapping("/download")
	public String downLoadDoc(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		File file = new File(contextPath + FILE_PATH);
		File outFile = null;
		if(file.exists()){
			File[] files = file.listFiles();
			if(files.length > 0){
				FILE_NAME = files[0].getName();
				outFile = new File(contextPath + FILE_PATH + FILE_NAME);
				//log.info("fileName=>" + filename);
				/*HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.setContentDispositionFormData("attachment", "zApp_Report_Manual.docx");
				
				//log.info("文档下载:" + file.getPath());
				return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(outFile),headers, HttpStatus.CREATED);*/
				
				OutputStream os = response.getOutputStream();  
			    try {  
			    	response.reset();
			    	response.setHeader("Content-Disposition", "attachment; filename=zApp_Report_Manual.docx");
			    	response.setCharacterEncoding("utf-8");
			    	response.addHeader("Content-Length", "" + outFile.length());
			    	response.setContentType("application/octet-stream; charset=utf-8");  
			        os.write(FileUtils.readFileToByteArray(outFile));  
			        os.flush();  
			    } finally {  
			        if (os != null) {  
			            os.close();  
			        }  
			    }
			}
		}
		return null;
	}

}
