package com.mas.rave.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.ImageInfo;
import com.mas.rave.main.vo.MessagePush;
import com.mas.rave.main.vo.User;
import com.mas.rave.service.MessagePushService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.DateUtil;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

/**
 * MessagePush业务处理
 * 
 * @author liwei
 * 
 */
@Controller
@RequestMapping("/push")
public class MessagePushController {
	Logger log = Logger.getLogger(MessagePushController.class);

	@Autowired
	private MessagePushService messagePushService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String title = request.getParameter("title");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String target = request.getParameter("target");
			String action = request.getParameter("action");
			MessagePush push = new MessagePush();
			if (StringUtils.isNotEmpty(title)) {
				push.setTitle(title);
			}
			if (StringUtils.isNotEmpty(target) && !target.equals("-1")) {
				push.setTarget(Integer.parseInt(target));
			} else {
				push.setTarget(-1);
			}
			if (StringUtils.isNotEmpty(action) && !action.equals("-1")) {
				push.setAction(Integer.parseInt(action));
			} else {
				push.setAction(-1);
			}
			if (startTime != null && !startTime.equals("")) {
				push.setStartTime(DateUtil.StringToDate(startTime + " 00:00:00"));
			}
			if (endTime != null && !endTime.equals("")) {
				push.setEndTime(DateUtil.StringToDate(endTime + " 23:59:59"));
			}
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<MessagePush> result = messagePushService.searchMessagePush(push, currentPage, pageSize);
			request.setAttribute("result", result);

		} catch (Exception e) {
			log.error("MessagePushController list", e);
			PaginationVo<ImageInfo> result = new PaginationVo<ImageInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "push/list";
	}

	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		return "push/add";
	}

	/** 新增push信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute MessagePush push, HttpServletRequest request) {
		try {
			String msg = checkMsg(push, 1);
			if (StringUtils.isNotEmpty(msg)) {
				return "{\"flag\":\"2\",\"msg\":\"" + msg + "\"}";//
			}
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			// 设置推送方式
			if (push.getMode() == 1) {
				if (StringUtils.isNotEmpty(push.getPic())) {
					fileUrlList.add(push.getPic());
				} else {
					return "{\"flag\":\"1\"}";//
				}
			}

			if (StringUtils.isNotEmpty(push.getIcon())) {
				fileUrlList.add(push.getIcon());
			} else {
				return "{\"flag\":\"1\"}";//
			}
			// s3upload
			S3Util.uploadS3(fileUrlList, false);
			User user = (User) request.getSession().getAttribute("loginUser");
			if (user != null) {
				push.setCreateBy(user.getName());
			}
			push.setStartTime(DateUtil.StringToDate(push.getStartString()));
			push.setEndTime(DateUtil.StringToDate(push.getEndString()));
			messagePushService.insert(push);
		} catch (Exception e) {
			try {
				// 删除已经下载的文件
				FileUtil.deleteFile(push.getPic());
				FileUtil.deleteFile(push.getIcon());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("MessagePushController add", e);
			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 修改push信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute MessagePush push, HttpServletRequest request) {
		try {
			if (push.getMode() == 1) {
				// 原先已有图片则可为空
				if (StringUtils.isEmpty(push.getPic())) {
					if (StringUtils.isEmpty(push.getPic1())) {
						return "{\"flag\":\"2\",\"msg\":\"推送图片不能为空\"}";
					}
				}
			}
			List<String> fileUrlList = new ArrayList<String>();// s3 list
			// 设置推送方式
			if (StringUtils.isNotEmpty(push.getPic1())) {
				// 检测图片是否合法
				FileUtil.deleteFile(push.getPic());
				push.setPic(push.getPic1());
				fileUrlList.add(push.getPic());
			}

			// 设置推送方式
			if (StringUtils.isNotEmpty(push.getIcon1())) {
				// 清除已有图片
				FileUtil.deleteFile(push.getIcon());
				push.setIcon(push.getIcon1());
				fileUrlList.add(push.getIcon());
			}
			// s3upload
			S3Util.uploadS3(fileUrlList, false);
			User user = (User) request.getSession().getAttribute("loginUser");
			if (user != null) {
				push.setLastUpdateBy(user.getName());
			}
			push.setStartTime(DateUtil.StringToDate(push.getStartString()));
			push.setEndTime(DateUtil.StringToDate(push.getEndString()));
			messagePushService.updateByPrimaryKey(push);
		} catch (Exception e) {
			try {
				// 删除已经下载的文件
				FileUtil.deleteFile(push.getPic());
				FileUtil.deleteFile(push.getIcon());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("MessagePushController add", e);

			return "{\"flag\":\"3\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个push信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			MessagePush push = messagePushService.selectByPrimaryKey(id);
			push.setStartString(DateUtil.DateToString(push.getStartTime()));
			push.setEndString(DateUtil.DateToString(push.getEndTime()));
			model.addAttribute("push", push);
		} catch (Exception e) {
			log.error("MessagePushController edit", e);
		}
		return "push/edit";
	}

	/** 删除music */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		messagePushService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 新增文件信息页 */
	@ResponseBody
	@RequestMapping("/uploadPic")
	public String uploadPic(HttpServletRequest request, HttpServletResponse response, @RequestParam("flag") String flag) {
		PrintWriter pw = null;
		JSONObject obj = new JSONObject();
		try {
			pw = response.getWriter();
			MultipartHttpServletRequest mhs = (MultipartHttpServletRequest) request;
			MultipartFile picFile = mhs.getFile("file1");
			// 设置推送方式
			if (picFile != null && picFile.getSize() != 0) {
				String suffix = FileAddresUtil.getSuffix(picFile.getOriginalFilename());
				if (suffix.equals(Constant.IMG_ADR)) {
					// 清除已有图片
					File pic1File = new File(Constant.LOCAL_FILE_PATH + Constant.LOCAL_PUSH, RandNum.randomFileName(picFile.getOriginalFilename()));
					FileUtil.copyInputStream(picFile.getInputStream(), pic1File);//
					if (StringUtils.isEmpty(checkImg(pic1File, Integer.parseInt(flag)))) {
						FileUtil.deleteFile(FileAddresUtil.getFilePath(pic1File));
						obj.put("flag", 1);
					} else {
						obj.put("flag", 0);
						obj.put("msg", FileAddresUtil.getFilePath(pic1File));
					}
				} else {
					obj.put("flag", 1);
				}
			} else {
				obj.put("flag", 1);
			}
			pw.println(obj.toString());

		} catch (Exception e) {
			try {
				obj.put("flag", 1);
			} catch (JSONException e1) {
				log.error("defeated error", e);
			}
			pw.println(obj.toString());
		}
		return null;
	}

	public String checkMsg(MessagePush push, int flag) {
		if (flag == 1) {
			if (push.getMode() == 1) {
				if (StringUtils.isEmpty(push.getPic())) {
					return "推送图片不能为空";
				}
			}
		}
		if (push.getTarget() == 1) {
			if (StringUtils.isEmpty(push.getVersionName())) {
				return "推送版本不能为空";
			}
		}
		if (push.getAction() == 1) {
			if (StringUtils.isEmpty(push.getUrl())) {
				return "后续动作链接不能为空";
			}
		}
		if (StringUtils.isEmpty(push.getIcon())) {
			return "上传图标不能为空";
		}
		return "";
	}

	public String checkImg(File file, int flag) throws IOException {
		BufferedImage bufferFile = ImageIO.read(file);
		int width = 0;
		int height = 0;
		int len = 0;
		if (flag == 1) {
			// 图片检测
			width = 760;
			height = 312;
			len = 60;
		} else {
			width = 36;
			height = 36;
			len = 5;
		}
		if (bufferFile.getWidth() <= width && bufferFile.getHeight() <= height) {
			if (file.length() / 1024 <= len) {
				return "ok";
			}
		}
		return "";
	}

}
