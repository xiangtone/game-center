package com.mas.rave.detection;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.ChannelService;

/**
 * 资源检测
 * 
 * @author liwei.sz
 * 
 */
public class ResourceDetection {
	@Autowired
	private AppInfoService appService;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private AppFileService appFileService;

	@Autowired
	private ChannelService channelService;

	public boolean checkSource(HashMap<String, String> map) {
		boolean bol = false; // 标识
		// 获取标识
		String flag = map.get("flag");
		if (StringUtils.isNotEmpty(flag)) {
			// 标志设置
			if (flag.equals("1")) {
				// appinfo表检测
				AppInfo appInfo = new AppInfo();
				Channel channel = channelService.getChannel(Integer.parseInt(map.get("channelId")));
				if (channel != null) {
					appInfo.setChannel(channel);
					appInfo.setName(map.get("name"));
					if (appService.searchApp(appInfo) != null) {
						// 检测成功可正常录入
						bol = true;
					}
				}
			} else if (flag.equals("2")) {
				// appFile表检测
//				appFileService.get
			}
		}

		return false;
	}
}
