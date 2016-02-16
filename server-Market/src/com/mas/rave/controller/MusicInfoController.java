package com.mas.rave.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioHeader;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.common.web.MusicParseUtils;
import com.mas.rave.common.web.RequestUtils;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.MusicInfo;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.MarketService;
import com.mas.rave.service.MusicInfoService;
import com.mas.rave.service.UserService;
import com.mas.rave.task.RingtonesTask;
import com.mas.rave.util.Constant;
import com.mas.rave.util.CrawlerPaperAndRingtoneUtils;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.PinyinToolkit;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.StringUtil;
import com.mas.rave.util.s3.S3Util;

/**
 * music业务处理
 * 
 * @author jieding
 * 
 */
@Controller
@RequestMapping("/music")
public class MusicInfoController {
	private static String basicUrl = "http://na.voga360.com";
	public static boolean isMusicBatchAddThreadRun = false;
	private final String FTP_APK_PATH = "/home/ftpapk/";
	Logger log = Logger.getLogger(MusicInfoController.class);
	@Autowired
	private MarketService marketService;
	@Autowired
	private MusicInfoService musicInfoService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@Autowired
	private CountryService countryService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		try {
			String name = request.getParameter("name");
			// 获取 状态
			String state = request.getParameter("state");

			String categroyId = request.getParameter("categoryId");

			String categoryId1 = request.getParameter("categoryId1");

			String raveId = request.getParameter("raveId");

			MusicInfo musicInfo = new MusicInfo();
			if (StringUtils.isNotEmpty(name)) {
				// name = "%" + name + "%";
				musicInfo.setName(name.trim());
			}
			// 设置默认状态为是.
			if (StringUtils.isEmpty(state)) {
				musicInfo.setState(true);
			} else {
				musicInfo.setState(Boolean.parseBoolean(state));
			}
			if (StringUtils.isNotEmpty(categoryId1) && !categoryId1.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(categoryId1));
				musicInfo.setCategory(category);
			} else if (StringUtils.isNotEmpty(categroyId) && !categroyId.equals("0")) {
				Category category = new Category();
				category.setId(Integer.parseInt(categroyId));
				musicInfo.setCategory(category);
				musicInfo.setCategory_parent1(Integer.parseInt(categroyId));
			}
			if (StringUtils.isNotEmpty(raveId) && !raveId.equals("0")) {
				try {
					musicInfo.setRaveId(Integer.parseInt(raveId));
				} catch (Exception e) {
					log.error("MusicInfoController list raveId", e);

				}
			}

			int currentPage = RequestUtils.getInt(request, "currentPage", 1);
			int pageSize = RequestUtils.getInt(request, "pageSize", PaginationVo.DEFAULT_PAGESIZE);

			PaginationVo<MusicInfo> result = musicInfoService.searchMusics(musicInfo, currentPage, pageSize);
			request.setAttribute("result", result);

			// 所有一级分类信息
			List<Category> categorys = categoryService.getCategorys(1);
			request.setAttribute("categorys", categorys);
			Category category0 = new Category();
			category0.setName("Ringtones");
			category0.setFatherId(1);
			List<Category> ringtonesCategorys = categoryService.getCategorysByName(category0);
			Category ringtonesCategory = null;
			if (ringtonesCategorys.size() != 0) {
				ringtonesCategory = ringtonesCategorys.get(0);
			}
			request.setAttribute("ringtonesCategory", ringtonesCategory);
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("MusicInfoController list", e);
			PaginationVo<MusicInfo> result = new PaginationVo<MusicInfo>(null, 0, 10, 1);
			request.setAttribute("result", result);
		}
		return "music/list";
	}

	/** 判断主题名是否已经存在 */
	@ResponseBody
	@RequestMapping(value = "/judgeNameExist", method = RequestMethod.POST)
	public boolean judgeThemeNameExist(MusicInfo criteria) {
		List<MusicInfo> listmusic = musicInfoService.selectByName(criteria.getName());
		if (criteria.getId() == 0) {
			if (listmusic.size() != 0) {
				return false;// 主题名已经存在
			}
		} else {
			if (listmusic.size() > 1 || (listmusic.size() == 1 && listmusic.get(0).getId() != criteria.getId())) {
				return false;// 主题名已经存在
			}
		}

		return true;
	}

	@RequestMapping("/add")
	public String showAdd(HttpServletRequest request) {
		// 所有分类信息
		List<Category> categorys = categoryService.getCategorys(1);
		request.setAttribute("categorys", categorys);
		List<Country> countrys = countryService.getCountrys();
		request.setAttribute("countrys", countrys);
		return "music/add";
	}

	/** 查看详细 */
	@ResponseBody
	@RequestMapping("/getCategory")
	public String getCategory(@RequestParam Integer id, @RequestParam Integer level) {
		try {
			List<Category> list = categoryService.getCategorys(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				options.append("<option value='0'>--all--</option>");
				for (Category category : list) {
					options.append("<option value='");
					options.append(category.getId() + "'>");
					options.append(category.getName());
					if (level == 2) {
						options.append("(").append(category.getCateName()).append(")");
					}
					options.append("</option>");
				}

				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\",\"rave\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "{\"success\":\"2\"}";
		}
	}

	@ResponseBody
	@RequestMapping("/secondCategory")
	public String querySecondCategory(@RequestParam Integer id, @RequestParam Integer level) {
		try {
			List<Category> list = categoryService.getCategorys(id);
			if (CollectionUtils.isEmpty(list)) {
				return "{\"success\":\"0\"}";
			} else {
				StringBuilder options = new StringBuilder();
				options.append("<option value='0'>--all--</option>");
				for (Category category : list) {
					options.append("<option value='");
					options.append(category.getId() + "'>");
					options.append(category.getName());
					if (level == 2) {
						options.append("(").append(category.getCateName()).append(")");
					}
					options.append("</option>");
				}

				return "{\"success\":\"1\",\"option\":\"" + options.toString() + "\",\"rave\":\"" + options.toString() + "\"}";
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "{\"success\":\"2\"}";
		}
	}

	@ResponseBody
	@RequestMapping("/ftpUrl")
	public Map<String, String> queryFtpFilesURL(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		String musicPath = request.getParameter("musicPath");
		try {
			// 输入路径为空,则取系统默认路径
			if (StringUtils.isEmpty(musicPath)) {
				Resource rs = new ClassPathResource("config/resource.properties");
				Properties props = PropertiesLoaderUtils.loadProperties(rs);
				musicPath = props.getProperty("apk.ftp.path", FTP_APK_PATH);
			}
			if (StringUtils.isNotEmpty(musicPath)) {
				getFilePath(musicPath, map);
			}
		} catch (Exception e) {
			log.error("read apk resource failed !", e);
		}
		return map;
	}

	/** 新增app信息 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute MusicInfo music, @RequestParam Integer categoryId1, @RequestParam Integer categoryId2, @RequestParam(required = false) MultipartFile logoFile, @RequestParam(required = false) MultipartFile musicFile) {
		try {
			if (music.getAnotherName() != null) {
				music.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(music.getAnotherName()));
			}
			List<String> fileUrlList = new ArrayList<String>();// s3 list

			String fileName = Constant.LOCAL_FILE_PATH + Constant.LOCAL_MUSIC_PATH + MD5.getMd5Value(System.currentTimeMillis() + PinyinToolkit.cn2Spell(music.getName()).replaceAll("[\\/:?*<>|]", ""));
			music.setPinyin(PinyinToolkit.cn2Spell(music.getName()).replaceAll("[\\/:?*<>|]", ""));

			// ftp上传路径
			String musicAddress = music.getMusicAddress().trim();
			// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
			String uploadType = music.getUploadType();
			// 选择本地apk上传方式,但提交文件为空
			if (uploadType.equals("1") && musicFile.getSize() == 0) {
				// log.info("music file is empty ! upload failed !");
				// 文件有问题
				log.info("文件有问题");
				return "{\"flag\":\"5\"}";
			}
			// 选择ftp上传方式,但是没提交路径为空
			if (uploadType.equals("2") && StringUtils.isEmpty(musicAddress)) {
				// log.info("ftp path is empty ! upload failed !");
				// 文件有问题
				log.info("文件有问题");
				return "{\"flag\":\"5\"}";
			}
			int index = musicAddress.lastIndexOf("/") == -1 ? musicAddress.lastIndexOf("\\") : musicAddress.lastIndexOf("/");
			String musicName = StringUtils.isEmpty(musicAddress) ? musicFile.getOriginalFilename() : musicAddress.substring(index + 1).trim();
			// 获取上传文件后缀
			String suffix = FileAddresUtil.getSuffix(musicName);
			if (suffix.equals(Constant.MUSIC_ADR)) {
				File musicLocalFile = new File(fileName + Constant.LOCAL_MUSIC_PATH, RandNum.randomFileName(musicName));
				if (uploadType.equals("1")) {
					FileUtil.copyInputStream(musicFile.getInputStream(), musicLocalFile);// apk本地上传
				} else {
					// File destDir = new
					// File(musicLocalFile.getAbsolutePath());
					File srcFile = new File(musicAddress);
					FileUtils.copyFileToDirectory(srcFile, musicLocalFile.getParentFile());
					// 移动到新目录后,重命名该文件
					File newFile = new File(musicLocalFile.getParent(), musicName);
					if (newFile.isFile()) {
						newFile.renameTo(musicLocalFile);
					}
				}

				MusicParseUtils musicParse = new MusicParseUtils();
				if (music.getDurationString() != null && !music.getDurationString().equals("") && !music.getDurationString().equals("00:00:00")) {
					music.setDuration(stringToLongTime(music.getDurationString()));
				} else {
					AudioHeader audioh = musicParse.getAudioHeader(musicLocalFile);
					if (audioh != null) {
						music.setDuration(audioh.getTrackLength());// 获取音乐播放时长
					}
				}
				music.setFileSize((int) musicLocalFile.length());// 获取文件大小
				music.setUrl(FileAddresUtil.getFilePath(musicLocalFile));// 音乐下载地址
				fileUrlList.add(music.getUrl());
				if (logoFile.getSize() != 0) {
					String suffix1 = FileAddresUtil.getSuffix(logoFile.getOriginalFilename());
					if (suffix1.equals(Constant.IMG_ADR)) {
						File musicLogoLocalFile = new File(fileName + Constant.LOCAL_APK_LOG, RandNum.randomFileName(logoFile.getOriginalFilename()));
						FileUtil.copyInputStream(logoFile.getInputStream(), musicLogoLocalFile);// apk本地上传
						music.setLogo(FileAddresUtil.getFilePath(musicLogoLocalFile));
						fileUrlList.add(music.getLogo());
					} else {
						return "{\"flag\":\"2\"}";
					}
				} else {
					com.mas.rave.util.mp3.MusicInfo musicInfo = new com.mas.rave.util.mp3.MusicInfo();
					try {
						if (musicInfo.hasImage(musicLocalFile.getAbsolutePath())) {

							String musicfilename = FileUtil.trimExtension(musicLocalFile.getName());
							File file11 = com.mas.rave.util.mp3.MusicInfo.writeImage(musicInfo.getImage(), fileName + Constant.LOCAL_APK_LOG, musicfilename);
							if (file11 != null) {
								music.setLogo(FileAddresUtil.getFilePath(file11));
								fileUrlList.add(music.getLogo());
							} else {
								log.info(musicLocalFile.getAbsolutePath() + "没有图片");
							}

						} else {
							log.info(musicLocalFile.getAbsolutePath() + "没有图片");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("MusicInfoController add get music image error", e);
					}
				}
			}
			List<MusicInfo> listmusic = musicInfoService.selectByName(music.getName());
			if (music.getId() == 0) {
				if (listmusic.size() != 0) {
					return "{\"flag\":\"4\"}";
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						music.setRaveId(cate.getRaveId());
					} else {
						music.setRaveId(1);
					}
					musicInfoService.addMusicInfo(music, categoryId);

				}
			} else {
				if (listmusic.size() > 1 || (listmusic.size() == 1 && listmusic.get(0).getId() != music.getId())) {
					return "{\"flag\":\"4\"}";
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						music.setRaveId(cate.getRaveId());
					} else {
						music.setRaveId(1);
					}
					musicInfoService.addMusicInfo(music, categoryId);

				}
			}
			// s3upload
			S3Util.uploadS3(fileUrlList, false);
			// System.out.println(music.getName());
			// System.out.println(musicFile.getName());
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(music.getLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("MusicInfoController add", e);

			return "{\"flag\":\"4\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	private int stringToLongTime(String durationString) {
		String[] times = durationString.split(":");
		int hour = Integer.parseInt(times[0]);
		int minute = Integer.parseInt(times[1]);
		int second = Integer.parseInt(times[2]);
		return hour * 3600 + minute * 60 + second;
	}

	/** 新增app信息 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@ModelAttribute MusicInfo music, @RequestParam Integer categoryId1, @RequestParam Integer categoryId2, @RequestParam(required = false) MultipartFile logoFile, @RequestParam(required = false) MultipartFile musicFile) {
		try {
			if (music.getAnotherName() != null) {
				music.setAnotherName(StringUtil.convertBlankLinesToHashSymbol(music.getAnotherName()));
			}
			List<String> fileUrlList = new ArrayList<String>();// s3 list

			Category category = categoryService.getCategory(categoryId1);
			if (category != null) {

			} else {
				// 选择的类别不存在
				return "{\"flag\":\"5\"}";
			}
			// ftp上传路径
			String musicAddress = music.getMusicAddress().trim();
			// 选择上传方式,1:本地apk文件上传,2:通过ftp路径上传
			String uploadType = music.getUploadType();
			String fileName = Constant.LOCAL_FILE_PATH + Constant.LOCAL_MUSIC_PATH + MD5.getMd5Value(System.currentTimeMillis() + PinyinToolkit.cn2Spell(music.getName()).replaceAll("[\\/:?*<>|]", ""));
			MusicInfo musicInfo1 = musicInfoService.getMusicInfo(music.getId());
			music.setPinyin(PinyinToolkit.cn2Spell(music.getName()).replaceAll("[\\/:?*<>|]", ""));
			if (logoFile.getSize() != 0) {
				String suffix1 = FileAddresUtil.getSuffix(logoFile.getOriginalFilename());
				if (suffix1.equals(Constant.IMG_ADR)) {
					FileUtil.deleteFile(music.getLogo());
					File musicLogoLocalFile = new File(fileName + Constant.LOCAL_APK_LOG, RandNum.randomFileName(logoFile.getOriginalFilename()));
					FileUtil.copyInputStream(logoFile.getInputStream(), musicLogoLocalFile);// apk本地上传
					music.setLogo(FileAddresUtil.getFilePath(musicLogoLocalFile));
					fileUrlList.add(music.getLogo());
				} else {
					return "{\"flag\":\"2\"}";
				}
			}
			if ((musicFile == null || musicFile.getSize() == 0) && (StringUtils.isEmpty(musicAddress))) {
				// music.setDuration(musicInfo1.getDuration());
				if (music.getDurationString() != null && !music.getDurationString().equals("") && !music.getDurationString().equals("00:00:00")) {
					music.setDuration(stringToLongTime(music.getDurationString()));
				}
				music.setFileSize(musicInfo1.getFileSize());
			} else {
				int index = musicAddress.lastIndexOf("/") == -1 ? musicAddress.lastIndexOf("\\") : musicAddress.lastIndexOf("/");
				String musicName = StringUtils.isEmpty(musicAddress) ? musicFile.getOriginalFilename() : musicAddress.substring(index + 1).trim();
				// 获取上传文件后缀
				String suffix = FileAddresUtil.getSuffix(musicName);

				// 获取上传文件后缀
				if (suffix.equals(Constant.MUSIC_ADR)) {
					FileUtil.deleteFile(music.getUrl());
					File musicLocalFile = new File(fileName + Constant.LOCAL_MUSIC_PATH, RandNum.randomFileName(musicName));
					if (uploadType.equals("1")) {
						FileUtil.copyInputStream(musicFile.getInputStream(), musicLocalFile);// apk本地上传
					} else {
						// File destDir = new
						// File(musicLocalFile.getAbsolutePath());
						File srcFile = new File(musicAddress);
						FileUtils.copyFileToDirectory(srcFile, musicLocalFile.getParentFile());
						// 移动到新目录后,重命名该文件
						File newFile = new File(musicLocalFile.getParent(), musicName);
						if (newFile.isFile()) {
							newFile.renameTo(musicLocalFile);
						}
					}
					MusicParseUtils musicParse = new MusicParseUtils();
					if (music.getDurationString() != null && !music.getDurationString().equals("") && !music.getDurationString().equals("00:00:00")) {
						music.setDuration(stringToLongTime(music.getDurationString()));
					} else {
						AudioHeader audioh = musicParse.getAudioHeader(musicLocalFile);
						if (audioh != null) {
							music.setDuration(audioh.getTrackLength());// 获取音乐播放时长
						}
					}
					music.setFileSize((int) musicLocalFile.length());// 获取文件大小
					music.setUrl(FileAddresUtil.getFilePath(musicLocalFile));// 音乐下载地址
					fileUrlList.add(music.getUrl());
					if (logoFile.getSize() == 0) {
						com.mas.rave.util.mp3.MusicInfo musicInfo = new com.mas.rave.util.mp3.MusicInfo();
						try {
							if (musicInfo.hasImage(musicLocalFile.getAbsolutePath())) {

								String musicfilename = FileUtil.trimExtension(musicLocalFile.getName());
								File file11 = com.mas.rave.util.mp3.MusicInfo.writeImage(musicInfo.getImage(), fileName + Constant.LOCAL_APK_LOG, musicfilename);
								if (file11 != null) {
									if (music.getLogo() != null && !music.getLogo().equals("")) {
										FileUtil.deleteFile(music.getLogo());
									}
									music.setLogo(FileAddresUtil.getFilePath(file11));
									fileUrlList.add(music.getLogo());
								} else {
									log.info(musicLocalFile.getAbsolutePath() + "没有图片");
								}

							} else {
								log.info(musicLocalFile.getAbsolutePath() + "没有图片");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("MusicInfoController add get music image error", e);
						}
					}

				} else {
					return "{\"flag\":\"3\"}";
				}
			}

			music.setInitDowdload(musicInfo1.getInitDowdload());
			music.setRealDowdload(musicInfo1.getRealDowdload());

			List<MusicInfo> listmusic = musicInfoService.selectByName(music.getName());
			if (music.getId() == 0) {
				if (listmusic.size() != 0) {
					return "{\"flag\":\"4\"}";
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						music.setRaveId(cate.getRaveId());
					} else {
						music.setRaveId(1);
					}
					musicInfoService.upMusicInfo(music, categoryId);
				}
			} else {
				if (listmusic.size() > 1 || (listmusic.size() == 1 && listmusic.get(0).getId() != music.getId())) {
					return "{\"flag\":\"4\"}";
				} else {
					Integer categoryId = 0;
					if (null == categoryId2 || categoryId2 == 0) {
						categoryId = categoryId1;
					} else {
						categoryId = categoryId2;
					}
					// 将一级id设置为二级国家id
					Category cate = categoryService.getCategory(categoryId);
					if (cate != null) {
						music.setRaveId(cate.getRaveId());
					} else {
						music.setRaveId(1);
					}
					musicInfoService.upMusicInfo(music, categoryId);
				}
			}
			// s3upload
			S3Util.uploadS3(fileUrlList, false);
			// System.out.println(music.getName());
			// System.out.println(musicFile.getName());
		} catch (Exception e) {
			try {
				FileUtil.deleteFile(music.getLogo());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error("MusicInfoController add", e);

			return "{\"flag\":\"4\"}";
		}
		return "{\"flag\":\"0\"}";
	}

	/** 加载单个music信息 */
	@RequestMapping("/{id}")
	public String edit(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			MusicInfo music = musicInfoService.getMusicInfo(id);
			if (music.getAnotherName() != null) {
				music.setAnotherName(StringUtil.convertHashSymbolToBlankLines(music.getAnotherName()));
			}
			model.addAttribute("music", music);
			// 所有分类信息
			List<Category> fatherCategorys = categoryService.getCategorys(1);
			request.setAttribute("fatherCategorys", fatherCategorys);
			request.setAttribute("page", request.getParameter("page"));
			// 所有分类信息
			if (music.getCategory() != null) {
				List<Category> categorys = categoryService.getCategorys(music.getCategory().getFatherId());
				request.setAttribute("categorys", categorys);
			}
			List<Country> countrys = countryService.getCountrys();
			request.setAttribute("countrys", countrys);
		} catch (Exception e) {
			log.error("AppInfoController edit", e);
		}
		return "music/edit";
	}

	/** 删除music */
	@ResponseBody
	@RequestMapping("/delete")
	public String delete(@RequestParam("id") String ids, Model model) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		musicInfoService.batchDelete(idIntArray);
		return "{\"success\":\"true\"}";
	}

	/** 查看详细 */
	@RequestMapping("/info/{id}")
	public String info(HttpServletRequest request, @PathVariable("id") Integer id, Model model) {
		try {
			MusicInfo music = musicInfoService.getMusicInfo(id);
			if (music.getAnotherName() != null) {
				music.setAnotherName(StringUtil.convertHashSymbolToBlankLines(music.getAnotherName()));
			}
			// System.out.println(music.getCategory().getCategoryCn());
			// music.setCategory(category);
			model.addAttribute("music", music);
		} catch (Exception e) {
			log.error("MusicInfoController info", e);
		}
		return "music/musicInfo";
	}

	/** 查看详细 */
	@RequestMapping("/batchAddShow")
	public String batchAddShow(HttpServletRequest request) {
		// type=27&startNum=42&cacheTime=1100
		Map<String, String> map2 = null;
		String jsstr = CrawlerPaperAndRingtoneUtils.getHeadJSForURL(basicUrl);
		if (jsstr != null && !jsstr.equals("")) {
			map2 = CrawlerPaperAndRingtoneUtils.getSecondCategoryForUrl(jsstr, "ringTypeList");

		}
		List<String> category = new ArrayList<String>();
		if (map2 != null) {
			Set<Map.Entry<String, String>> entryseSet2 = map2.entrySet();
			for (Map.Entry<String, String> entry2 : entryseSet2) {
				try {
					category.add(new String(entry2.getKey().getBytes("iso8859-1"), "UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
				}
			}
		}
		request.setAttribute("categorys", category);
		return "music/batchAdd";
	}

	/** 批量入库 */
	@ResponseBody
	@RequestMapping("/batchAdd")
	public List<String> batchAdd(HttpServletRequest request, String loginUser, String category, int pageNum) {
		List<String> resList = new ArrayList<String>();
		// type=27&startNum=42&cacheTime=1100
		Map<String, String> map2 = null;

		String jsstr = CrawlerPaperAndRingtoneUtils.getHeadJSForURL(basicUrl);
		if (jsstr != null && !jsstr.equals("")) {
			map2 = CrawlerPaperAndRingtoneUtils.getSecondCategoryForUrl(jsstr, "ringTypeList");
		}
		if (map2 != null) {
			int n = 0;
			int m = 0;
			String filePath = Constant.LOCAL_FILE_PATH + Constant.LOCAL_MUSIC_PATH + category;
			Elements elements = null;
			// 获取到每个APK对应的真实url
			String pagepath = nextPage(map2.get(category), pageNum);
			// ringtones/Blues/ringtoneName/...music
			elements = CrawlerPaperAndRingtoneUtils.downloadRingtoneSByURL(pagepath);

			for (Element element : elements) {
				// 该铃声已经存在数据库
				String result = null;
				try {
					result = isRingtonesExistInDB(element);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("MusicInfoController batchAdd", e);
				}
				if (result != null && result.endsWith("已经存在")) {
					m++;
					resList.add("<font color='red'>" + result + "</font>");
				}
				n++;
			}
			resList.add("------------------------------------------------------");
			int mm = 0;
			for (Element element : elements) {
				// 该铃声已经存在数据库,但是所属类别在数据库中找不到
				String result = null;
				try {
					result = isRingtonesCategoryTrue(element);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("MusicInfoController batchAdd", e);
				}
				if (result != null) {
					resList.add("<font color='blue'>" + result + "</font>");
					m++;
					mm++;

				}
			}
			resList.add("------------------------------------------------------");
			for (Element element : elements) {
				//
				String result = null;
				try {
					result = isRingtonesNeedDownload(category, element);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("MusicInfoController batchAdd", e);
				}
				if (result != null) {
					resList.add(result);

				}
			}
			resList.add("------------------------------------------------------");
			resList.add("已经成功录入" + n + "个文件," + "其中" + m + "个文件已经存在" + "但是" + mm + "个文件所属类别唯一标识有误");

			if (isMusicBatchAddThreadRun == false) {
				RingtonesTask task = new RingtonesTask(elements, filePath, loginUser);
				task.setCategoryService(categoryService);
				task.setMarketService(marketService);
				task.setMusicInfoService(musicInfoService);
				task.start();
				isMusicBatchAddThreadRun = true;
			} else {
				resList.add("<font color='red'>已经有其它操作人在执行该操作，请先处理其它操作！</font>");
				return resList;
			}

			resList.add("下载操作正在后台执行，请先处理其它操作！");
		} else {
			resList.add("很抱歉，系统出现无法预知的异常或者网络连接异常，请联系管理员！！！");
			return resList;
		}
		return resList;
	}

	/**
	 * 查询下载的铃声是否已经存在数据库
	 * 
	 * @param element
	 */
	public String isRingtonesExistInDB(Element element) throws Exception {
		String ringtoneName = element.select("div.title").first().select("a").text().replaceAll("[\\/:?*<>|.#'\"$@%^&*,;:]", "");
		// 查询数据库中是否存在数据，如果存在
		if (ringtoneName != null && !ringtoneName.equals("")) {
			List<MusicInfo> musicList = musicInfoService.selectByName(ringtoneName);
			if (musicList != null && musicList.size() != 0) {
				if (musicList.get(0).getCategory() != null) {
					return musicList.get(0).getCategory().getName() + "/" + musicList.get(0).getName() + "已经存在";
				}
			}
		}

		return null;
	}

	/**
	 * 查询下载的壁纸类别是否已经和t_category匹配
	 * 
	 * @param element
	 */
	public String isRingtonesCategoryTrue(Element element) throws Exception {
		String ringtoneName = element.select("div.title").first().select("a").text().replaceAll("[\\/:?*<>|.#'\"$@%^&*,;:]", "");
		// 查询数据库中是否存在数据，如果存在
		if (ringtoneName != null && !ringtoneName.equals("")) {
			List<MusicInfo> musicList = musicInfoService.selectByName(ringtoneName);
			if (musicList != null && musicList.size() != 0) {
				if (musicList.get(0).getCategory() == null) {
					return ringtoneName + "  已存在，但其库中的对应类别不存在，不能正常下载！";
				}
			}
		}

		return null;
	}

	/**
	 * 查询下载的铃声是否需要下载
	 * 
	 * @param element
	 */
	public String isRingtonesNeedDownload(String category, Element element) throws Exception {
		String ringtoneName = element.select("div.title").first().select("a").text().replaceAll("[\\/:?*<>|.#'\"$@%^&*,;:]", "");
		// 查询数据库中是否存在数据，如果存在
		if (ringtoneName != null && !ringtoneName.equals("")) {
			List<MusicInfo> musicList = musicInfoService.selectByName(ringtoneName);
			if (musicList == null || musicList.size() == 0) {
				return category + "/" + ringtoneName + "正在下载";
			}
		}

		return null;
	}

	/**
	 * 次级菜单url的简单翻页
	 * 
	 * @param url
	 * @param page
	 *            所选择的页面
	 * @return
	 */
	private static String nextPage(String url, int page) {

		String s = page + ".html";
		String url1 = url.replaceAll("1.html", s);

		return url1;
	}

	/**
	 * 遍历文件目录,取得所有apk的名称和路径
	 * 
	 * @param path
	 * @param map
	 */
	public void getFilePath(String path, Map<String, String> map) {
		File file = new File(path);
		File[] files = file.listFiles();
		if (null != files && files.length > 0) {
			for (File f : files) {
				// 如果目录下还有子目录,递归遍历
				if (f.isDirectory()) {
					getFilePath(f.getAbsolutePath(), map);
				} else {
					String suffix = FileAddresUtil.getSuffix(f.getName());
					if (suffix.equals(Constant.MUSIC_ADR)) {
						map.put(f.getName(), f.getPath());
					}
				}
			}
		}
	}
}
