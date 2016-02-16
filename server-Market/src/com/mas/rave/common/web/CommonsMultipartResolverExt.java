package com.mas.rave.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class CommonsMultipartResolverExt extends CommonsMultipartResolver {
	Logger log = Logger.getLogger(CommonsMultipartResolverExt.class);
	private HttpServletRequest request;

	@Override
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
		upload.setSizeMax(-1);
		if (request != null) {
			log.info("CommonsMultipartResolverExt    注入监听......");
			FileUploadListener uploadProgressListener = new FileUploadListener();
			upload.setProgressListener(uploadProgressListener);
			HttpSession session = request.getSession();
			session.setAttribute("uploadProgressListener",
					uploadProgressListener);
		}
		return upload;
	}

	@Override
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
	       this.request=request;//获取到request,要用到session
	       try{
		       return super.resolveMultipart(request);  
	       }catch(Exception e){
	    	   return null;
	    	   
	       }
	 }
	}
