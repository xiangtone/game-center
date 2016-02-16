package com.mas;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mas.market.pojo.TAppFile;
import com.mas.market.service.TAppFileService;
import com.mas.util.VM;

/**
 * Servlet implementation class ApkDownload
 */
@WebServlet("/apkUpgrade")
public class ApkDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TAppFileService tAppFileService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		tAppFileService = context.getBean(TAppFileService.class);   
		
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String packageName = request.getParameter("package");
		if(!StringUtils.isEmpty(packageName)){
			try {
				com.mas.market.pojo.Criteria c = new com.mas.market.pojo.Criteria();
				c.put("packageName", packageName);
				List<TAppFile> list = tAppFileService.getDownloadInfo(c);
				String url;
				if(null != list && list.size()>0){
					TAppFile app = list.get(0);
					url = VM.getInatance().getResServer() + app.getUrl();
					System.out.println("---------====="+url);
					response.sendRedirect(url);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}
}
