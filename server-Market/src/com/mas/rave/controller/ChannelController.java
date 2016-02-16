package com.mas.rave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.Province;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.ProvinceService;
import com.mas.rave.util.MD5;
import com.mas.rave.util.RandNum;
/**
 * 渠道业务处理　
 * @author liwei.sz
 *
 */

@Controller
@RequestMapping("/channel")
public class ChannelController {
	Logger log = Logger.getLogger(ChannelController.class);
	@Autowired
	private ChannelService channelService;

	@Autowired
	private ProvinceService provinceService;

	/**
	 * 分页查看渠道信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String str = request.getParameter("channelId");
			ChannelService.ChannelCriteria criteria = new ChannelService.ChannelCriteria();
			if (StringUtils.isNotEmpty(str) && !str.equals("0")) {
				criteria.idEqualTo(Integer.parseInt(str));
			}
			//只加载父渠道ID为1的数据
			criteria.fatherIdEqualTo(1);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize",
					PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<Channel> result = channelService.searchChannels(
					criteria, currentPage, pageSize);
			request.setAttribute("channels", result);

			// 设置列表
			List<Channel> chas = channelService.getChannels(1);
			request.setAttribute("fatherIds", chas);
		} catch (Exception e) {
			log.error("ChannelController list");
			PaginationVo<Channel> result = new PaginationVo<Channel>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "channel/list";
	}

	/**
	 * 对应子渠道
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/listSecond/{id}")
	public String lisSecondt(HttpServletRequest request,
			@PathVariable("id") Integer id) {
		try {
			String str = request.getParameter("channelId");
			ChannelService.ChannelCriteria criteria = new ChannelService.ChannelCriteria();
			if (StringUtils.isNotEmpty(str) && !str.equals("0")) {
				criteria.idEqualTo(Integer.parseInt(str));
			}
			criteria.fatherIdEqualTo(id);
			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize",
					PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<Channel> result = channelService.searchChannels(
					criteria, currentPage, pageSize);
			request.setAttribute("channels", result);

			// 设置列表
			List<Channel> chas = channelService.getChannels(1);
			request.setAttribute("fatherIds", chas);
		} catch (Exception e) {
			log.error("ChannelController lisSecondt", e);
		}
		return "channel/listSecond";
	}

	/** 新增渠道信息页 */
	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		// 设置所有省份
		List<Province> provinces = provinceService.getProvinces();
		request.setAttribute("provinces", provinces);
		return "channel/add";
	}

	/** 新增渠道信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Channel channel, @RequestParam Integer provinceId) {
		try {
			Channel cha = channelService.getChannel(channel.getFatherId());
			channel.setFatherName(cha.getName());
			channel.setPwd(RandNum.randomPwd(6));
			channel.setPassword(MD5.getMd5Value(channel.getPwd()));
			channelService.addChannel(channel, provinceId);
		} catch (Exception e) {
			log.error("ChannelController add", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 新增渠道信息页 */
	@RequestMapping("/addSecond")
	public String showAddSecond(HttpServletRequest request) {
		List<Channel> fatherIds = channelService.getChannels(1);
		request.setAttribute("fatherIds", fatherIds);
		return "channel/addSecond";
	}

	/** 新增渠道信息 */
	@ResponseBody
	@RequestMapping(value = "/addSecond", method = RequestMethod.POST)
	public String addSecond(Channel channel) {
		try {
			if (StringUtils.isNotEmpty(channel.getFatherName())) {
				channel.setFatherId(Integer.parseInt(channel.getFatherName()
						.split(",")[0]));
				channel.setFatherName(channel.getFatherName().split(",")[1]);
			}
			channel.setPwd(RandNum.randomPwd(6));
			channel.setPassword(MD5.getMd5Value(channel.getPwd()));
			channelService.addChannel(channel, 0);
		} catch (Exception e) {
			log.error("ChannelController addSecond", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个渠道 */
	@RequestMapping("/{id},{fatherId}")
	public String edit(HttpServletRequest request,
			@PathVariable("id") Integer id,
			@PathVariable("fatherId") Integer fatherId, Model model) {
		try {
			Channel Channel = channelService.getChannel(id);
			model.addAttribute("channel", Channel);
			List<Channel> fatherIds = channelService.getChannels(1);
			model.addAttribute("fatherIds", fatherIds);
		} catch (Exception e) {
			log.error("ChannelController edit", e);
		}
		if (fatherId <= 1) {
			// 设置所有省份
			List<Province> provinces = provinceService.getProvinces();
			request.setAttribute("provinces", provinces);
			return "channel/edit";
		} else {
			return "channel/editSecond";
		}
	}

	/** 加载单个渠道 */
	@RequestMapping("/channelInfo/{id}")
	public String channelInfo(HttpServletRequest request,
			@PathVariable("id") Integer id, Model model) {
		try {
			Channel Channel = channelService.getChannel(id);
			model.addAttribute("channel", Channel);
		} catch (Exception e) {
			log.error("ChannelController channelInfo", e);
		}
		return "channel/channelInfo";
	}

	/** 更新渠道 */
	@ResponseBody
	@RequestMapping(value = "/{id},{fatherId}", method = RequestMethod.POST)
	public String update(Channel channel, @RequestParam Integer provinceId) {
		try {
			Channel _Channel = channelService.getChannel(channel.getId());
			if (_Channel != null && _Channel.getId() != (channel.getId()))
				return "{flag:1}";

			if (provinceId <= 0) {
				// 更新二级渠道
				channel.setFatherName(channel.getFatherName().split(",")[1]);
			} else {
				Channel cha = channelService.getChannel(channel.getFatherId());
				channel.setFatherName(cha.getName());
				// 更新一级渠道
			}
			channel.setPassword(MD5.getMd5Value(channel.getPwd()));
			channelService.upChannel(channel, provinceId);
		} catch (Exception e) {
			log.error("ChannelController update", e);
			return "{\"flag\":\"1\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 删除渠道 */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		channelService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

}
