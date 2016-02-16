package com.mas.rave.common.log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.mas.rave.common.MyCollectionUtils;
import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.controller.PropertiesController;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumTheme;
import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.AppComment;
import com.mas.rave.main.vo.AppCountryScore;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileList;
import com.mas.rave.main.vo.AppFilePatch;
import com.mas.rave.main.vo.AppFileZappUpdate;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.main.vo.AppInfoConfig;
import com.mas.rave.main.vo.AppInfoRank;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.AppScore;
import com.mas.rave.main.vo.AppannieInfoCountryRank;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.ClientCountry;
import com.mas.rave.main.vo.ClientFeedback;
import com.mas.rave.main.vo.ClientFeedbackZapp;
import com.mas.rave.main.vo.ClientFeedbackZappCode;
import com.mas.rave.main.vo.ClientSkin;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.CountryExample;
import com.mas.rave.main.vo.Cp;
import com.mas.rave.main.vo.ImageAlbumRes;
import com.mas.rave.main.vo.ImageAlbumTheme;
import com.mas.rave.main.vo.ImageInfo;
import com.mas.rave.main.vo.Market;
import com.mas.rave.main.vo.Menu;
import com.mas.rave.main.vo.MenuType;
import com.mas.rave.main.vo.MusicAlbumRes;
import com.mas.rave.main.vo.MusicAlbumTheme;
import com.mas.rave.main.vo.MusicInfo;
import com.mas.rave.main.vo.Operation;
import com.mas.rave.main.vo.OperationType;
import com.mas.rave.main.vo.OurPartners;
import com.mas.rave.main.vo.Pay;
import com.mas.rave.main.vo.Province;
import com.mas.rave.main.vo.Role;
import com.mas.rave.main.vo.RoleMenu;
import com.mas.rave.main.vo.RoleOperation;
import com.mas.rave.main.vo.SearchKeyword;
import com.mas.rave.main.vo.SearchKeywordIcon;
import com.mas.rave.main.vo.TAppDistribute;
import com.mas.rave.main.vo.User;
import com.mas.rave.main.vo.UserRole;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumService;
import com.mas.rave.service.AppAlbumThemeService;
import com.mas.rave.service.AppCollectionService;
import com.mas.rave.service.AppCommentService;
import com.mas.rave.service.AppCountryScoreService;
import com.mas.rave.service.AppFileListService;
import com.mas.rave.service.AppFileService;
import com.mas.rave.service.AppFileZappUpdateService;
import com.mas.rave.service.AppInfoConfigService;
import com.mas.rave.service.AppInfoRankService;
import com.mas.rave.service.AppInfoService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.service.AppannieInfoCountryRankService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.ClientFeedbackService;
import com.mas.rave.service.ClientFeedbackZappCodeService;
import com.mas.rave.service.ClientFeedbackZappService;
import com.mas.rave.service.ClientSkinService;
import com.mas.rave.service.CountryService;
import com.mas.rave.service.CpService;
import com.mas.rave.service.ImageAlbumResService;
import com.mas.rave.service.ImageAlbumThemeService;
import com.mas.rave.service.ImageInfoService;
import com.mas.rave.service.LogService;
import com.mas.rave.service.MenuService;
import com.mas.rave.service.MusicAlbumResService;
import com.mas.rave.service.MusicAlbumThemeService;
import com.mas.rave.service.MusicInfoService;
import com.mas.rave.service.OurPartnersService;
import com.mas.rave.service.PayService;
import com.mas.rave.service.ProvinceService;
import com.mas.rave.service.RoleService;
import com.mas.rave.service.SearchKeywordIconService;
import com.mas.rave.service.SearchKeywordService;
import com.mas.rave.service.UserService;

@Component
@Aspect
public class LogAspect {

	private Logger logger = Logger.getLogger(LogAspect.class);

	@Autowired
	private LogService logService;

	@Autowired
	private AppAlbumColumnService appAlbumColumnService;

	@Autowired
	private AppAlbumThemeService appAlbumThemeService;

	@Autowired
	private AppCommentService appCommentService;
	@Autowired
	private AppFileService appFileService;

	@Autowired
	private AppInfoConfigService appInfoConfigService;

	@Autowired
	private AppInfoService appInfoService;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private AppAlbumService appAlbumService;

	@Autowired
	private AppAlbumResTempService appAlbumResTempService;

	@Autowired
	private AppAlbumResService appAlbumResService;

	@Autowired
	private AppFileListService appFileListService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CpService cpService;

	@Autowired
	private AppInfoRankService appInfoRankService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private PropertiesController propertiesController;

	@Autowired
	private ImageAlbumThemeService imageAlbumThemeService;

	@Autowired
	private ImageInfoService imageInfoService;

	@Autowired
	private MenuService menuService;
	@Autowired
	private MusicAlbumThemeService musicAlbumThemeService;

	@Autowired
	private PayService payService;
	@Autowired
	private MusicInfoService musicInfoService;

	@Autowired
	private MusicAlbumResService musicAlbumResService;

	@Autowired
	private ImageAlbumResService<ImageAlbumRes> imageAlbumResService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private SearchKeywordService searchKeywordService;

	@Autowired
	private ClientFeedbackZappService clientFeedbackZappService;
	@Autowired
	private ClientFeedbackService clientFeedbackService;
	@Autowired
	private SearchKeywordIconService searchKeywordIconService;

	@Autowired
	private ClientFeedbackZappCodeService clientFeedbackZappCodeService;

	@Autowired
	private AppFileZappUpdateService appFileZappUpdateService;

	@Autowired
	private AppScoreService appScoreService;

	@Autowired
	private AppCollectionService appCollectionService;

	@Autowired
	private OurPartnersService ourPartnersService;

	@Autowired
	private AppCountryScoreService appCountryScoreService;

	@Autowired
	private AppannieInfoCountryRankService appannieInfoCountryRankService;

	@Autowired
	private ClientSkinService clientSkinService;
	public static String name = null;

	@AfterReturning(value = "execution(* com.mas.rave.controller.*.*(..)) and args(..)", returning = "result")
	public void operationLog(JoinPoint point, Object result) {
		HttpSession s = (HttpSession) RequestContextHolder.currentRequestAttributes().resolveReference(RequestAttributes.REFERENCE_SESSION);
		User user = (User) s.getAttribute("loginUser");
		if (user != null) {
			MethodSignature methodSig = (MethodSignature) point.getStaticPart().getSignature();
			Method targetMethod = methodSig.getMethod();
			String method = getMethodName(targetMethod.getName(), targetMethod.getDeclaringClass().getSimpleName());
			Object[] args = point.getArgs();
			if (null != method && !method.equals("")) {

				logger.info("后置日志开始 。。。。。");
				if (targetMethod.getName().equals("doOperant")) {
					doOperantLog(args);

				}
				String resourceName = getResourceName(targetMethod.getName(), targetMethod.getDeclaringClass().getSimpleName());
				int rel = getoperateSucc(result, targetMethod.getDeclaringClass().getSimpleName());
				logger.info("用户" + user.getName() + "/" + ((rel == 0) ? "失败" : "成功") + "/" + method + "/" + name + "/" + resourceName);
				com.mas.rave.main.vo.Log log = new com.mas.rave.main.vo.Log();
				log.setAction(method);
				log.setOperator(user.getName());
				log.setSucc(rel);
				if (name == null) {
					log.setRes(resourceName);
				} else {
					log.setRes(name + "/" + resourceName);
				}
				try {
					logService.addLog(log);
				} catch (Exception e) {
					logger.error("日志记录失败 。。。。。", e);
				}
				logger.info("后置日志结束 。。。。。");
			}
		}

	}

	/**
	 * 获取生效的log
	 * 
	 * @param args
	 */
	private void doOperantLog(Object[] args) {
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn((Integer) args[1]);

		if (appAlbumColumn != null) {
			String appAlbumName = "";
			if (appAlbumColumn.getAppAlbum() != null) {
				appAlbumName = appAlbumColumn.getAppAlbum().getName() + "-->";
			}
			AppAlbumRes appAlbumRes = new AppAlbumRes();
			appAlbumRes.setAppAlbumColumn(appAlbumColumn);
			int raveId1 = 1;
			if (StringUtils.isNotEmpty((String) args[2])) {
				raveId1 = Integer.parseInt(((String) args[2]).trim());
				Country country = countryService.getCountry(raveId1);
				List<String> appNameList = appAlbumResService.selectByColumnId((Integer) args[1], raveId1);
				StringBuilder sb = new StringBuilder();
				sb.append(country.getName() + "(" + country.getNameCn() + ")" + "-->" + appAlbumName + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")");
				sb.append("-->分发总数(" + appNameList.size() + ")");
				if (appNameList != null && appNameList.size() > 0) {
					sb.append("-->(");
					for (String appName1 : appNameList) {
						sb.append(appName1).append(",");
					}
				}
				name = sb.substring(0, sb.length() - 1) + ")";

			}
		}
	}

	@Before(value = "execution(* com.mas.rave.controller.*.*(..)) and args(..)")
	public void operationLogBeforeDelete(JoinPoint point) {
		MethodSignature methodSig = (MethodSignature) point.getStaticPart().getSignature();
		Method targetMethod = methodSig.getMethod();
		String method = getMethodName(targetMethod.getName(), targetMethod.getDeclaringClass().getSimpleName());
		if (null != method && !method.equals("")) {
			name = null;
			logger.info("前置日志开始 。。。。。");
			Object[] args = point.getArgs();
			try {
				getoperateAppName(args, targetMethod, targetMethod.getDeclaringClass().getSimpleName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				name = null;
				logger.error("前置日志获取操作资源失败。。。。。", e);
			}
			logger.info("前置日志结束 。。。。。");
		}

	}

	@AfterThrowing(pointcut = "execution(* com.mas.rave.controller.*.*(..))", throwing = "ex")
	public void logerr(Exception ex) {

		Log logger = LogFactory.getLog(LogAspect.class);
		logger.error("", ex);
	}

	/**
	 * 0表示失败 1表示成功
	 * 
	 * @param result
	 * @return
	 */
	public int getoperateSucc(Object result, String className) {
		if (result instanceof String) {
			String rel = (String) result;
			if (rel.startsWith("{")) {
				if (rel.endsWith("}")) {
					rel.replace("\\", "");
					JSONObject json = JSONObject.fromObject(rel);

					if (json.get("flag") instanceof Integer || json.get("success") instanceof Integer) {
						if (json.get("flag") != null && ((Integer) json.get("flag") == 0) || (json.get("success") != null && (Integer) json.get("success") == 1)) {
							return 1;
						}
					} else if (json.get("flag") instanceof String || json.get("success") instanceof String) {
						if (className.equals("AppFileController") || className.equals("MarkAppFileController") || className.equals("CommunalFileController")) {
							if (json.get("flag") != null && (json.get("flag").equals("3"))) {
								return 1;
							}
						}
						if (json.get("flag") != null && (json.get("flag").equals("0") || json.get("flag").equals("true"))) {
							return 1;
						} else if (json.get("success") != null && (json.get("success").equals("1") || json.get("success").equals("true"))) {
							return 1;
						}
					}
				} else {
					logger.error("###not json result:" + rel);
					return 0;
				}
			} else {
				return 1;
			}
		} else if (result instanceof Map<?, ?>) {
			// System.out.println((( Map<?,?>) result).get("flag"));
			if (((Map<?, ?>) result).get("flag") != null) {
				if ((((Map<?, ?>) result).get("flag").equals("true")) || ((Map<?, ?>) result).get("flag").equals("1")) {
					return 1;
				}
			} else if (((Map<?, ?>) result).get("success") != null) {
				if ((((Map<?, ?>) result).get("success").equals("true")) || ((Map<?, ?>) result).get("success").equals("1")) {
					return 1;
				}
			}
		} else if (result instanceof List<?>) {
			int i = 0;
			for (Object s : ((List<?>) result)) {
				if (((String) s).startsWith("很抱歉")) {
					i++;
					break;
				} else {
					i = 0;
				}
			}
			if (i == 0) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}

	public String getoperateAppName(Object[] args, Method targetMethod, String className) throws Exception {

		String appName = null;
		switch (targetMethod.getName()) {
		case "add":
			appName = getAddLog(args, className, appName);
			break;
		case "edit":
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
			break;
		case "update":
			if (className.equals("AppCommentController") || className.equals("AppFileController") || className.equals("AppPicController") || className.equals("CommunalFileController")
					|| className.equals("MarkAppFileController")) {
				AppInfo appInfo = appInfoService.getApp((Integer) args[1]);
				appName = appInfo.getName();
				break;
			} else if (className.equals("OpenBannerController")) {
				AppAlbumTheme record = (AppAlbumTheme) args[1];
				Integer appAlbumThemId = (Integer) args[2];
				appName = "广告图管理" + record.getThemeId() + "替换平台广告管理" + appAlbumThemId;
				break;
			} else {
				for (Object arg : args) {
					appName = getAddOrUpdateName(arg);
					if (appName != null) {
						break;
					}
				}
				break;
			}
		case "addZapp":
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
			break;
		case "updateZapp":
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
			break;
		case "deleteZapp":
			for (Object arg : args) {
				appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				if (appName != null) {
					break;
				}
			}
			break;
		case "delete":
			for (Object arg : args) {
				appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				if (appName != null) {
					break;
				}
			}
			break;
		case "collectionDelete":
			for (Object arg : args) {
				appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				if (appName != null) {
					break;
				}
			}
			break;
		case "liveWallpaperDelete":
			for (Object arg : args) {
				appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				if (appName != null) {
					break;
				}
			}
			break;
		case "deleteById":
			for (Object arg : args) {
				if (arg instanceof String) {
					appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				}
				if (appName != null) {
					break;
				}
			}
			break;
		case "deleteByClientId":
			for (Object arg : args) {
				if (arg instanceof String) {
					appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				}
				if (appName != null) {
					break;
				}
			}
			break;
		case "disable":
			for (Object arg : args) {
				appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				if (appName != null) {
					break;
				}
			}
			break;
		case "liveWallpaperDoConfig":
			appName = doConfigAlbumColumn(args);
			if (appName != null) {
				break;
			}
			break;
		case "collectionDoConfig":
			appName = collectionDoConfigApp(args);
			if (appName != null) {
				break;
			}
			break;
		case "doConfig":

			if (className.equals("AppAlbumColumnController")) {
				appName = doConfigAlbumColumn(args);
				if (appName != null) {
					break;
				}

			} else if (className.equals("RoleController")) {
				appName = doConfigRole(args, appName);
				if (appName != null) {
					break;
				}

				// 角色配置
			} else if (className.equals("AppInfoController") || className.equals("CommunalController")) {
				appName = doConfigApp(args);
				if (appName != null) {
					break;
				}

				// 角色配置
			} else if (className.equals("WallpaperController")) {
				// Integer raveId,Integer columnId, Integer
				// albumId,List<Integer> ids
				// null
				// null
				// [1895, 1894, 1893, 1892]
				appName = doConfigWallpaper(args);
				if (appName != null) {
					break;
				}

			} else if (className.equals("RingtonesController")) {
				// Integer raveId,Integer columnId, Integer
				// albumId,List<Integer> ids
				// null
				// null
				// [1895, 1894, 1893, 1892]
				appName = doConfigRingtones(args);
				if (appName != null) {
					break;
				}

			} else if (className.equals("AppCountryScoreController")) {
				appName = doAppCountryScore(args);
				if (appName != null) {
					break;
				}
			} else if (className.equals("AppannieCountryRankController")) {
				appName = doAppannieCountryRank(args);
				if (appName != null) {
					break;
				}
			}
			break;
		// UserController
		case "login":
			appName = ((User) args[0]).getName();
			if (appName != null) {
				break;
			}
		case "active":
			for (Object arg : args) {
				appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
				if (appName != null) {
					break;
				}
			}
			break;
		// RoleController
		case "heigher":
			for (Object arg : args) {
				appName = getHeigherOrLowerArgByRole((Integer) arg);
				if (appName != null) {
					break;
				}
			}
			break;
		case "lower":
			for (Object arg : args) {
				appName = getHeigherOrLowerArgByRole((Integer) arg);
				if (appName != null) {
					break;
				}
			}
			break;
		// PropertiesController
		case "setScore":
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
			break;
		// MusicInfoController、AppFileController、ImageInfoController
		case "batchAdd":
			if (className.equals("AppFileController")) {
				break;
			} else if (className.equals("ImageInfoController") || className.equals("MusicInfoController")) {
				if (args.length > 3) {
					String category = (String) args[2];
					int pageNum = (Integer) args[3];
					appName = category + "分类的第" + pageNum + "页";
					break;
				}
			} else {
				break;
			}

			// menuController
		case "save":
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
			break;
		case "editSave":
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
			break;
		// MarkAppFileController
		case "deleteFile":
			appName = deleteFileLog(args);
			break;
		case "addSecond":
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
			break;
		// 修改排序信息 AppSortController
		case "execute":
			appName = sortLog(args);
			if (appName != null) {
				break;
			}
			break;
		case "doState":
			appName = doStateLog(args, className);
			break;
		case "top":
			appName = topLog("top", args);
			break;
		case "appannieCountryRank":
			appName = topLog("appannieCountryRank", args);
			break;

		case "batchOperant":
			appName = batchOperant(args, targetMethod, appName);
			break;
		case "updateZappCode":
			appName = updateZappCodeLog(appName);
			break;
		case "updateState":
			appName = updateStateLog(args, appName, className);
			break;
		case "laterRanking":
			appName = getLaterRankingLog(args);
			break;
		case "clearRanking":
			appName = getClearRankingLog(args);
			break;
		case "updateRanking":
			appName = getUpdateRankingLog(args);
			break;
		case "handDataCopy":
			appName = getHandDataCopyLog(args);
			break;
		case "simulatedDistribution":
			appName = simulatedDistributionLog(args);
			break;
		case "copyRanking":
			appName = copyRankingLog(args);
		break;
		default:
			break;
		}
		if (appName != null) {
			name = appName;
			return appName;
		}
		return null;
	}

	private String copyRankingLog(Object[] args) {
		Country country1 = countryService.getCountry((Integer) args[0]);// 国家
		String country0Name = getDefaultCountry();
		if(country1!=null){
			return "复制"+country0Name+"数据至"+country1.getName();			
		}else{
			return "复制"+country0Name+"数据至选定国家!";
		}
	}
	private String simulatedDistributionLog(Object[] args) {
		// raveId,albumId,columnId
		StringBuilder sb20 = new StringBuilder();
		Country country = countryService.getCountry((Integer) args[0]);// 国家
		sb20.append(country.getName() + "(" + country.getNameCn() + ")" + "-->");
		AppAlbum appAlbum = appAlbumService.getAppAlbum((Integer) args[1]);// 专辑大类别
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn((Integer) args[2]);// 专辑第二类别
		sb20.append(appAlbum.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")");
		return sb20.toString();
	}

	/**
	 * 手动分发配置铃声log
	 * 
	 * @param args
	 * @return
	 */
	private String doConfigRingtones(Object[] args) {
		StringBuilder sb20 = new StringBuilder();
		Country country = countryService.getCountry((Integer) args[1]);// 国家
		sb20.append(country.getName() + "(" + country.getNameCn() + ")" + "-->");
		if (args[2] == null) {
			// 主题分发
			sb20.append("主题").append("-->");
		} else {
			// 栏目分发
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn((Integer) args[2]);// 专辑第二类别
			AppAlbum appAlbum = appAlbumService.getAppAlbum((Integer) args[3]);// 专辑大类别
			sb20.append(appAlbum.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->");
		}
		StringBuilder sb0 = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) args[4];
		if (ids != null && ids.size() > 0 && ids.size() < 5) {
			for (Integer id : ids) {
				MusicInfo music = musicInfoService.getMusicInfo(id);
				if (music != null) {
					sb0.append(music.getName()).append(",");
				}
			}
			if (sb0 != null && !sb0.equals("")) {
				sb20.append(sb0.substring(0, sb0.length() - 1));
			}
		} else if (ids != null) {
			sb20.append(ids.size() + "条");
		}
		return sb20.toString();
	}

	/**
	 * 手动分发配置壁纸log
	 * 
	 * @param args
	 * @return
	 */
	private String doConfigWallpaper(Object[] args) {
		StringBuilder sb2 = new StringBuilder();
		Country country = countryService.getCountry((Integer) args[1]);// 国家
		sb2.append(country.getName() + "(" + country.getNameCn() + ")" + "-->");

		if (args[2] == null) {
			// 主题分发
			sb2.append("主题").append("-->");
		} else {
			// 栏目分发
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn((Integer) args[2]);// 专辑第二类别
			AppAlbum appAlbum = appAlbumService.getAppAlbum((Integer) args[3]);// 专辑大类别
			sb2.append(appAlbum.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->");
		}
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) args[4];
		if (ids != null && ids.size() > 0 && ids.size() < 5) {
			for (Integer id : ids) {
				PaginationBean params = new PaginationBean();
				params.getParams().put("id", id + "");
				ImageInfo image = imageInfoService.getImageInfo(id);
				if (image != null) {
					sb.append(image.getName()).append(",");

				}
			}
			if (sb != null && !sb.equals("")) {
				sb2.append(sb.substring(0, sb.length() - 1));
			}
		} else if (ids != null) {
			sb2.append(ids.size() + "条");
		}
		return sb2.toString();
	}

	/**
	 * 分配角色log
	 * 
	 * @param args
	 * @param appName
	 * @return
	 */
	private String doConfigRole(Object[] args, String appName) {
		StringBuilder sb = new StringBuilder();
		Role role = roleService.getRoleById((Integer) args[0]);
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) args[1];
		if (ids != null && ids.size() > 0) {
			for (Integer id : ids) {
				Menu menu0 = menuService.getMenuById(id);
				sb.append(menu0.getName()).append(",");
			}
			if (sb != null && !sb.equals("")) {
				appName = role.getName() + "被分配" + sb.substring(0, sb.length() - 1) + "等权限";
				if (appName.length() > 400) {
					appName = role.getName() + "被分配" + ids.size() + "个权限";
				}
			}
		} else {
			appName = role.getName() + "被分配0个权限";
		}
		return appName;
	}

	/**
	 * 手动分发/动态壁纸分发app资源
	 * 
	 * @param args
	 * @return
	 */
	private String doConfigAlbumColumn(Object[] args) {
		String appName = null;
		// 分发配置临时表
		// Integer raveId, Integer columnId, Integer albumId, List<Integer> ids
		Country country = countryService.getCountry((Integer) args[0]);// 国家
		AppAlbum appAlbum = appAlbumService.getAppAlbum((Integer) args[2]);// 专辑大类别
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn((Integer) args[1]);// 专辑第二类别
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) args[3];
		if (ids != null && ids.size() > 0 && ids.size() < 5) {
			for (Integer id : ids) {
				AppFile appFile = appFileService.getAppFile(id);
				if (appFile != null) {
					sb.append(appFile.getAppName()).append(",");
				}
			}

			if (sb != null && !sb.equals("")) {
				appName = country.getName() + "(" + country.getNameCn() + ")" + "-->" + appAlbum.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->"
						+ sb.substring(0, sb.length() - 1);
			}
		} else if (ids != null) {
			sb.append(ids.size() + "条");
			if (sb != null && !sb.equals("")) {
				appName = country.getName() + "(" + country.getNameCn() + ")" + "-->" + appAlbum.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->"
						+ sb.toString();
			}
		}
		return appName;
	}

	/**
	 * 强制排行管理
	 * 
	 * @param args
	 * @return
	 */
	private String doAppCountryScore(Object[] args) {
		String appName = null;
		// 分发配置临时表
		// Integer raveId, List<Integer> ids
		Country country = countryService.getCountry((Integer) args[0]);// 国家

		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) args[1];
		if (ids != null && ids.size() > 0 && ids.size() < 5) {
			for (Integer id : ids) {
				AppInfo appInfo = appInfoService.getApp(id);
				if (appInfo != null) {
					sb.append(appInfo.getName()).append(",");
				}
			}
			if (sb != null && !sb.equals("")) {
				appName = country.getName() + "(" + country.getNameCn() + ")" + sb.substring(0, sb.length() - 1);
			}
		} else if (ids != null) {
			sb.append(ids.size() + "条");
			if (sb != null && !sb.equals("")) {
				appName = country.getName() + "(" + country.getNameCn() + ")" + "-->" + sb.toString();
			}
		}
		return appName;
	}

	/**
	 * 强制排行管理
	 * 
	 * @param args
	 * @return
	 */
	private String doAppannieCountryRank(Object[] args) {
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray((String) args[0]);

		String appName = null;
		// 分发配置临时表
		// Integer raveId, List<Integer> ids
		Country country = null;
		StringBuilder sb = new StringBuilder();
		for (Integer id : idIntArray) {
			AppannieInfoCountryRank appannieInfoCountryRank = appannieInfoCountryRankService.selectByPrimaryKey(id);
			sb.append(appannieInfoCountryRank.getAppName()).append(",");
			if (country == null) {
				country = appannieInfoCountryRank.getCountry();
			}
		}
		if (sb != null && sb.length() > 1) {
			appName = country.getName() + "(" + country.getNameCn() + ")" + "-->" + sb.substring(0, sb.length() - 1);
		}
		return appName;
	}

	private String doConfigApp(Object[] args) {
		String appName = null;
		StringBuilder sb = new StringBuilder();
		String ids = args[0].toString();
		Integer apkId = (Integer) args[1];
		Integer raveId = (Integer) args[2];
		AppFile file = appFileService.getAppFile(apkId);
		if (raveId != null) {
			Country country = countryService.getCountry(raveId);
			sb.append(country.getName() + "(" + country.getNameCn() + ")" + "-->");
		}
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		for (int i = 0; i < idIntArray.length; i++) {
			AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(idIntArray[i]);
			sb.append(appAlbumColumn.getAppAlbum().getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")");
			sb.append(",");
		}
		appName = sb.substring(0, sb.length() - 1) + file.getAppInfo().getName();
		return appName;
	}

	/**
	 * 应用专题分发app资源
	 * 
	 * @param args
	 * @return
	 */
	private String collectionDoConfigApp(Object[] args) {
		String appName = null;
		// 分发配置临时表
		// Integer raveId,Integer collectionId,List<Integer> ids
		Country country = countryService.getCountry((Integer) args[0]);// 国家
		AppCollection appCollection = appCollectionService.getAppCollection((Integer) args[1]);
		StringBuilder sb = new StringBuilder();
		int type = (Integer) args[3];
		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) args[2];
		if (ids != null && ids.size() > 0 && ids.size() < 5) {
			for (Integer id : ids) {
				AppFile appFile = appFileService.getAppFile(id);
				if (appFile != null) {
					sb.append(appFile.getAppName()).append(",");
				}
			}

			if (sb != null && !sb.equals("")) {
				appName = country.getName() + "(" + country.getNameCn() + ")" + appCollection.getName() + "(" + appCollection.getNameCn() + ")" + "-->" + sb.substring(0, sb.length() - 1);
			}
		} else if (ids != null) {
			sb.append(ids.size() + "条");
			if (sb != null && !sb.equals("")) {
				appName = country.getName() + "(" + country.getNameCn() + ")" + appCollection.getName() + "(" + appCollection.getNameCn() + ")" + "-->" + sb.toString();
			}
		}
		if (type == 1) {
			appName += "/collection分发";
		} else {
			appName += "/Musthave分发";
		}
		return appName;
	}

	/**
	 * 删除文件的log
	 * 
	 * @param args
	 * @param appName
	 * @return
	 */
	private String deleteFileLog(Object[] args) {
		String appName = null;
		Integer[] idIntArray17 = MyCollectionUtils.splitToIntArray((String) args[0]);
		if (idIntArray17.length < 10) {
			StringBuilder sb17 = new StringBuilder();
			for (Integer id : idIntArray17) {
				AppFileList appFileList = appFileListService.getAppFileList(id);
				sb17.append(appFileList.getAppInfo().getName() + ",");
			}
			if (sb17 != null) {
				appName = sb17.substring(0, sb17.length() - 1);
			}
		} else {
			appName = idIntArray17.length + "条";
		}
		return appName;
	}

	/**
	 * 开发者apk和cp上线下线的log
	 * 
	 * @param args
	 * @param className
	 * @param appName
	 * @return
	 */
	private String doStateLog(Object[] args, String className) {
		String appName = null;
		if (className.equals("AppFileController")) {
			appName = appInfoService.getApp((Integer) args[1]).getName();
		} else if (className.equals("CpController")) {
			appName = cpService.getCp((Integer) args[1]).getName();
		}
		return appName;
	}

	/**
	 * 批量生效的log
	 * 
	 * @param args
	 * @param targetMethod
	 * @param appName
	 * @return
	 */
	private String batchOperant(Object[] args, Method targetMethod, String appName) {
		for (Object arg : args) {
			appName = getDeleteInfoName((String) arg, targetMethod.getDeclaringClass().getSimpleName(), targetMethod.getName());
			if (appName != null) {
				break;
			}
		}
		return appName;
	}

	/**
	 * 更新的版本号log
	 * 
	 * @param appName
	 * @return
	 */
	private String updateZappCodeLog(String appName) {
		List<ClientFeedbackZappCode> clientFeedbackZappCodeList = clientFeedbackZappCodeService.getAllClientFeedbackZappCode();
		if (clientFeedbackZappCodeList != null && clientFeedbackZappCodeList.size() != 0) {
			appName = clientFeedbackZappCodeList.get(0).getZappcode() + "";
		}
		return appName;
	}

	/**
	 * 国家是否显示log
	 * 
	 * @param args
	 * @param appName
	 * @return
	 */
	private String updateStateLog(Object[] args, String appName, String className) {
		if (className.equals("CountryController")) {
			StringBuilder sb0 = new StringBuilder();

			Country country00 = countryService.getCountry((Integer) args[0]);// 国家
			if (country00 != null) {
				sb0.append(country00.getName() + "(" + country00.getNameCn() + ")" + "-->");
				int state = (Integer) args[1];
				if (state == 1) {
					sb0.append("显示");
				} else if (state == 0) {
					sb0.append("不显示");
				}
				appName = sb0.toString();
			}
		} else if (className.equals("AppCountryScoreController")) {
			StringBuilder sb0 = new StringBuilder();
			AppCountryScore appCountryScore = appCountryScoreService.selectByPrimaryKey((Integer) args[0]);
			Country country00 = countryService.getCountry((Integer) args[2]);// 国家
			if (country00 != null) {
				sb0.append(country00.getName() + "(" + country00.getNameCn() + ")" + "-->");
			}
			if (appCountryScore != null) {
				sb0.append(appCountryScore.getAppName() + "-->");
				int state = (Integer) args[1];
				if (state == 1) {
					sb0.append("启用");
				} else if (state == 0) {
					sb0.append("不启用");
				}
			}
			appName = sb0.toString();
		}
		return appName;
	}

	/**
	 * appannie分发log
	 * 
	 * @param args
	 * @return
	 */
	private String topLog(String methodName, Object[] args) {
		String appName;
		AppAlbum appAlbum = appAlbumService.getAppAlbum((Integer) args[0]);// 类别
		Country country0 = countryService.getCountry((Integer) args[1]);// 国家
		if (methodName.equals("top")) {
			appName = country0.getName() + "(" + country0.getNameCn() + ")" + "-->" + appAlbum.getName();
		} else {
			appName = country0.getName() + "(" + country0.getNameCn() + ")" + "-->" + appAlbum.getName() + "-->appannieCountryRank分发";
		}
		return appName;
	}

	/**
	 * add方法日志处理
	 * 
	 * @param args
	 * @param className
	 * @param appName
	 * @return
	 */
	private String getAddLog(Object[] args, String className, String appName) {
		if (className.equals("AppCommentController") || className.equals("AppFileController") || className.equals("AppPicController") || className.equals("CommunalFileController")
				|| className.equals("MarkAppFileController")) {
			AppInfo appInfo = appInfoService.getApp((Integer) args[1]);
			appName = appInfo.getName();
		} else if (className.equals("ProxyIPController")) {
			if (((String) args[1]).length() > 19000) {
				appName = ((String) args[1]).replaceAll("\n", "#").substring(0, 19000) + "...";
			} else {
				appName = ((String) args[1]).replaceAll("\n", "#");
			}
		} else {
			for (Object arg : args) {
				appName = getAddOrUpdateName(arg);
				if (appName != null) {
					break;
				}
			}
		}
		return appName;
	}

	/**
	 * 手动分发延迟排名日志
	 * 
	 * @param args
	 * @return
	 */
	private String getLaterRankingLog(Object[] args) {
		String appName;
		int columnId = (int) args[0];
		int raveId = (int) args[1];
		String ids = args[2].toString();
		int num = (int) args[3];
		Integer[] idIntArray = MyCollectionUtils.splitToIntArray(ids);
		StringBuilder sb = new StringBuilder();
		for (Integer id : idIntArray) {
			AppAlbumRes appAlbumRes = appAlbumResTempService.getAppAlbumResTemp(id);
			if (appAlbumRes != null && appAlbumRes.getAppInfo() != null) {
				sb.append(appAlbumRes.getAppInfo().getName()).append(",");
			}
		}
		// 获取专辑
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
		Country country = countryService.getCountry(raveId);

		appName = country.getName() + "--> " + appAlbumColumn.getAppAlbum().getName() + "--> " + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "--> " + "延迟" + "排位" + num + "位("
				+ sb.substring(0, sb.length() - 1) + ")";
		return appName;
	}

	/**
	 * 清除手动分发排名日志
	 * 
	 * @param args
	 * @return
	 */
	private String getClearRankingLog(Object[] args) {
		String appName;
		int columnId = (int) args[0];
		int raveId = (int) args[1];
		// 获取专辑
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
		Country country = countryService.getCountry(raveId);
		appName = country.getName() + "--> " + appAlbumColumn.getAppAlbum().getName() + "--> " + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")";
		return appName;
	}

	/**
	 * 复制手动分发数据到Global
	 * 
	 * @param args
	 * @return
	 */
	private String getHandDataCopyLog(Object[] args) {
		String appName = null;
		int columnId = (int) args[0];
		int raveId = (int) args[1];
		// 获取专辑
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
		Country country = countryService.getCountry(raveId);
		String defaultCountry = getDefaultCountry();
		if (defaultCountry != null) {
			appName = "复制" + defaultCountry + "手动分发的数据到" + country.getName() + "中--> " + appAlbumColumn.getAppAlbum().getName() + "--> " + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn()
					+ ")";
		}
		return appName;
	}

	/**
	 * 获取默认的复制手动分发到Global的国家
	 * 
	 * @return
	 */
	private String getDefaultCountry() {
		String defaultCountry = "";
		List<AppScore> scores = appScoreService.getAllScore();
		for (AppScore appSocre : scores) {
			if (appSocre.getScoreKey().equals("Global")) {
				defaultCountry = appSocre.getScoreValue();
			}
		}
		if (defaultCountry == null || defaultCountry.equals("")) {
			defaultCountry = "Malaysia";
		}
		return defaultCountry;
	}

	/**
	 * 保存手动分发排名日志
	 * 
	 * @param args
	 * @return
	 */
	private String getUpdateRankingLog(Object[] args) {
		String appName;
		int columnId = (int) args[0];
		int raveId = (int) args[1];
		// 获取专辑
		AppAlbumColumn appAlbumColumn = appAlbumColumnService.getAppAlbumColumn(columnId);
		Country country = countryService.getCountry(raveId);
		appName = country.getName() + "--> " + appAlbumColumn.getAppAlbum().getName() + "--> " + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")";
		return appName;
	}

	public String getHeigherOrLowerArgByRole(Integer id) {
		Role role = roleService.getRoleById(id);
		return role.getName();
	}

	/**
	 * 获取操作的应用时属于哪一个(batchOperant/execute/deleteFile/active/disable
	 * disable/deleteById/delete)
	 * 
	 * @param arg
	 * @param className
	 * @return
	 */
	public String getDeleteInfoName(String arg, String className, String methodName) {

		switch (className) {
		case "AppAlbumColumnController":
			if (methodName.equals("delete")) {
				Integer[] idIntArray1 = MyCollectionUtils.splitToIntArray(arg);
				String name0 = "";
				for (Integer id : idIntArray1) {
					AppAlbumRes appAlbumRes = appAlbumResTempService.getAppAlbumResTemp(id);
					if (appAlbumRes != null) {
						AppAlbum albumName = appAlbumRes.getAppAlbum();
						AppAlbumColumn appAlbumColumn = appAlbumRes.getAppAlbumColumn();
						Country country = appAlbumRes.getCountry();
						name0 = country.getName() + "(" + country.getNameCn() + ")" + "-->" + albumName.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->";
					}
					if (name0 != null && !name0.equals("")) {
						break;
					}
				}
				if (idIntArray1.length < 10) {
					StringBuilder sb1 = new StringBuilder();
					for (Integer id : idIntArray1) {
						AppAlbumRes appAlbumRes = appAlbumResTempService.getAppAlbumResTemp(id);
						if (appAlbumRes != null) {
							AppInfo appInfo = appAlbumRes.getAppInfo();
							sb1.append(appInfo.getName() + ",");
						}
					}
					if (sb1 != null && sb1.length() > 1) {
						return name0 + sb1.substring(0, sb1.length() - 1);
					}
				} else {
					return name0 + idIntArray1.length + "条";
				}
			} else if (methodName.equals("collectionDelete")) {
				Integer[] idIntArray1 = MyCollectionUtils.splitToIntArray(arg);
				String name0 = "";
				for (Integer id : idIntArray1) {
					AppAlbumRes appAlbumRes = appAlbumResService.selectByPrimaryKey(id);
					if (appAlbumRes != null) {
						AppCollection appCollection = appAlbumRes.getAppCollection();
						Country country = appAlbumRes.getCountry();
						name0 = country.getName() + "(" + country.getNameCn() + ")" + "-->" + appCollection.getName() + "(" + appCollection.getNameCn() + ")" + "-->";
					}
					if (name0 != null && !name0.equals("")) {
						break;
					}
				}
				if (idIntArray1.length < 10) {
					StringBuilder sb1 = new StringBuilder();
					for (Integer id : idIntArray1) {
						AppAlbumRes appAlbumRes = appAlbumResService.selectByPrimaryKey(id);
						if (appAlbumRes != null) {
							AppInfo appInfo = appAlbumRes.getAppInfo();
							sb1.append(appInfo.getName() + ",");
						}
					}
					if (sb1 != null && sb1.length() > 1) {
						return name0 + sb1.substring(0, sb1.length() - 1) + "/应用分发管理";
					}
				} else {
					return name0 + idIntArray1.length + "条" + "/应用分发管理";
				}
			} else if (methodName.equals("liveWallpaperDelete")) {
				Integer[] idIntArray1 = MyCollectionUtils.splitToIntArray(arg);
				String name0 = "";
				for (Integer id : idIntArray1) {
					AppAlbumRes appAlbumRes = appAlbumResService.selectByPrimaryKey(id);
					if (appAlbumRes != null) {
						AppAlbum albumName = appAlbumRes.getAppAlbum();
						AppAlbumColumn appAlbumColumn = appAlbumRes.getAppAlbumColumn();
						Country country = appAlbumRes.getCountry();
						name0 = country.getName() + "(" + country.getNameCn() + ")" + "-->" + albumName.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->";
					}
					if (name0 != null && !name0.equals("")) {
						break;
					}
				}
				if (idIntArray1.length < 10) {
					StringBuilder sb1 = new StringBuilder();
					for (Integer id : idIntArray1) {
						AppAlbumRes appAlbumRes = appAlbumResService.selectByPrimaryKey(id);
						if (appAlbumRes != null) {
							AppInfo appInfo = appAlbumRes.getAppInfo();
							sb1.append(appInfo.getName() + ",");
						}
					}
					if (sb1 != null && sb1.length() > 1) {
						return name0 + sb1.substring(0, sb1.length() - 1) + "/动态壁纸分发";
					}
				} else {
					return name0 + idIntArray1.length + "条" + "/动态壁纸分发";
				}
			}

		case "AppAlbumThemeController":
			Integer[] idIntArray2 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray2.length < 5) {
				StringBuilder sb2 = new StringBuilder();
				for (Integer id : idIntArray2) {
					AppAlbumTheme appAlbumTheme = appAlbumThemeService.getAppAlbumTheme(id);
					sb2.append(appAlbumTheme.getName() + ",");
				}
				if (sb2 != null) {
					return sb2.substring(0, sb2.length() - 1);
				}
			} else {
				return idIntArray2.length + "条";
			}
		case "AppCommentController":
			Integer[] idIntArray3 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray3.length < 10) {
				StringBuilder sb3 = new StringBuilder();
				for (Integer id : idIntArray3) {
					AppComment appComment = appCommentService.getAppComment(id);
					sb3.append(appComment.getAppInfo().getName() + "-->" + appComment.getContent() + ",");
				}
				if (sb3 != null) {
					return sb3.substring(0, sb3.length() - 1);
				}
			} else {
				return idIntArray3.length + "条";
			}
		case "AppFileController":
			Integer[] idIntArray4 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray4.length < 10) {
				StringBuilder sb4 = new StringBuilder();
				for (Integer id : idIntArray4) {
					AppFile appFile = appFileService.getAppFile(id);
					sb4.append(appFile.getAppInfo().getName() + ",");
				}
				if (sb4 != null) {
					return sb4.substring(0, sb4.length() - 1);
				}
			} else {
				return idIntArray4.length + "条";
			}
		case "AppInfoConfigController":
			Integer[] idIntArray5 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray5.length < 10) {
				StringBuilder sb5 = new StringBuilder();
				for (Integer id : idIntArray5) {
					AppInfoConfig appInfoConfig = appInfoConfigService.getAppInfoConfig(id);
					sb5.append(appInfoConfig.getName() + ",");
				}
				if (sb5 != null) {
					return sb5.substring(0, sb5.length() - 1);
				}
			} else {
				return idIntArray5.length + "条";
			}
		case "AppInfoController":
			Long[] idIntArray6 = MyCollectionUtils.splitToIntArray1(arg);
			if (idIntArray6.length < 10) {
				StringBuilder sb6 = new StringBuilder();
				for (Long id : idIntArray6) {
					AppInfo appInfo = appInfoService.getApp(id);
					sb6.append(appInfo.getName() + ",");
				}
				if (sb6 != null) {
					return sb6.substring(0, sb6.length() - 1);
				}
			} else {
				return idIntArray6.length + "条";
			}
		case "AppPicController":
			Integer[] idIntArray7 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray7.length < 10) {
				StringBuilder sb7 = new StringBuilder();
				for (Integer id : idIntArray7) {
					AppPic appPic = appPicService.getAppPic(id);
					sb7.append(appPic.getAppInfo().getName() + ",");
				}
				if (sb7 != null) {
					return sb7.substring(0, sb7.length() - 1);
				}
			} else {
				return idIntArray7.length + "条";
			}
		case "CategoryController":
			Integer[] idIntArray8 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray8.length < 10) {

				StringBuilder sb8 = new StringBuilder();
				for (Integer id : idIntArray8) {
					Category category = categoryService.getCategory(id);
					Category categoryFather = categoryService.getCategory(category.getFatherId());
					if (categoryFather != null) {
						sb8.append(categoryFather.getName() + "-->");
					}
					sb8.append(category.getName() + ",");
				}
				if (sb8 != null) {
					return sb8.substring(0, sb8.length() - 1);
				}
			} else {
				return idIntArray8.length + "条";
			}
		case "ChannelController":

			Integer[] idIntArray9 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray9.length < 10) {
				StringBuilder sb9 = new StringBuilder();
				for (Integer id : idIntArray9) {
					Channel channel = channelService.getChannel(id);
					sb9.append(channel.getName() + ",");
				}
				if (sb9 != null) {
					return sb9.substring(0, sb9.length() - 1);
				}
			} else {
				return idIntArray9.length + "条";
			}
		case "CommunalController":
			Long[] idIntArray10 = MyCollectionUtils.splitToIntArray1(arg);
			if (idIntArray10.length < 10) {
				StringBuilder sb10 = new StringBuilder();
				for (Long id : idIntArray10) {
					AppInfo appInfo = appInfoService.getApp(id);
					sb10.append(appInfo.getName() + ",");
				}
				if (sb10 != null) {
					return sb10.substring(0, sb10.length() - 1);
				}
			} else {
				return idIntArray10.length + "条";
			}
		case "CommunalFileController":

			Integer[] idIntArray11 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray11.length < 10) {
				StringBuilder sb11 = new StringBuilder();
				for (Integer id : idIntArray11) {
					AppFile appFile = appFileService.getAppFile(id);
					sb11.append(appFile.getAppInfo().getName() + ",");
				}
				if (sb11 != null) {
					return sb11.substring(0, sb11.length() - 1);
				}
			} else {
				return idIntArray11.length + "条";
			}
		case "CountryController":
			Integer[] idIntArray12 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray12.length < 10) {
				StringBuilder sb12 = new StringBuilder();
				for (Integer id : idIntArray12) {
					Country country = countryService.getCountry(id);
					sb12.append(country.getName() + ",");
				}
				if (sb12 != null) {
					return sb12.substring(0, sb12.length() - 1);
				}
			} else {
				return idIntArray12.length + "条";
			}
		case "CpController":
			Long[] idIntArray13 = MyCollectionUtils.splitToIntArray1(arg);
			if (idIntArray13.length < 10) {
				StringBuilder sb13 = new StringBuilder();
				for (Long id : idIntArray13) {
					Cp cp = cpService.getCp(id);
					sb13.append(cp.getName() + ",");
				}
				if (sb13 != null) {
					return sb13.substring(0, sb13.length() - 1);
				}
			} else {
				return idIntArray13.length + "条";
			}
		case "ImageAlbumThemeController":
			Integer[] idIntArray14 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray14.length < 10) {
				StringBuilder sb14 = new StringBuilder();
				for (Integer id : idIntArray14) {
					ImageAlbumTheme imageAlbumTheme = imageAlbumThemeService.getImageAlbumTheme(id);
					sb14.append(imageAlbumTheme.getName() + ",");
				}
				if (sb14 != null) {
					return sb14.substring(0, sb14.length() - 1);
				}
			} else {
				return idIntArray14.length + "条";
			}
		case "ImageInfoController":
			Integer[] idIntArray15 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray15.length < 10) {
				StringBuilder sb15 = new StringBuilder();
				for (Integer id : idIntArray15) {
					ImageInfo imageInfo = imageInfoService.getImageInfo(id);
					sb15.append(imageInfo.getName() + ",");
				}
				if (sb15 != null) {
					return sb15.substring(0, sb15.length() - 1);
				}
			} else {
				return idIntArray15.length + "条";
			}
		case "MenuController":
			Integer[] idIntArray16 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray16.length < 10) {
				StringBuilder sb16 = new StringBuilder();
				for (Integer id : idIntArray16) {
					Menu menu = menuService.getMenuById(id);
					sb16.append(menu.getName() + ",");
				}
				if (sb16 != null) {
					return sb16.substring(0, sb16.length() - 1);
				}
			} else {
				return idIntArray16.length + "条";
			}
		case "MarkAppFileController":
			Integer[] idIntArray17 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray17.length < 10) {
				StringBuilder sb17 = new StringBuilder();
				for (Integer id : idIntArray17) {
					AppFile appFile = appFileService.getAppFile(id);
					sb17.append(appFile.getAppInfo().getName() + ",");
				}
				if (sb17 != null) {
					return sb17.substring(0, sb17.length() - 1);
				}
			} else {
				return idIntArray17.length + "条";
			}
		case "MarketController":

			Long[] idIntArray18 = MyCollectionUtils.splitToIntArray1(arg);
			if (idIntArray18.length < 10) {
				StringBuilder sb18 = new StringBuilder();
				for (Long id : idIntArray18) {
					AppInfo appInfo = appInfoService.getApp(id);
					sb18.append(appInfo.getName() + ",");
				}
				if (sb18 != null) {
					return sb18.substring(0, sb18.length() - 1);
				}
			} else {
				return idIntArray18.length + "条";
			}

		case "MusicAlbumThemeController":
			Long[] idIntArray19 = MyCollectionUtils.splitToIntArray1(arg);
			if (idIntArray19.length < 10) {
				StringBuilder sb19 = new StringBuilder();
				for (Long id : idIntArray19) {
					MusicAlbumTheme musicAlbumTheme = musicAlbumThemeService.getMusicAlbumTheme(id);
					sb19.append(musicAlbumTheme.getName() + ",");
				}
				if (sb19 != null) {
					return sb19.substring(0, sb19.length() - 1);
				}
			} else {
				return idIntArray19.length + "条";
			}

		case "MusicInfoController":
			Integer[] idIntArray20 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray20.length < 10) {
				StringBuilder sb20 = new StringBuilder();
				for (Integer id : idIntArray20) {
					MusicInfo musicInfo = musicInfoService.getMusicInfo(id);
					sb20.append(musicInfo.getName() + ",");
				}
				if (sb20 != null) {
					return sb20.substring(0, sb20.length() - 1);
				}
			} else {
				return idIntArray20.length + "条";
			}

		case "OpenAppInfoController":
			Long[] idIntArray21 = MyCollectionUtils.splitToIntArray1(arg);
			if (idIntArray21.length < 10) {
				StringBuilder sb21 = new StringBuilder();
				for (Long id : idIntArray21) {
					AppInfo appInfo = appInfoService.getApp(id);
					sb21.append(appInfo.getName() + ",");
				}
				if (sb21 != null) {
					return sb21.substring(0, sb21.length() - 1);
				}
			} else {
				return idIntArray21.length + "条";
			}
		case "PayController":
			Integer[] idIntArray22 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray22.length < 10) {
				StringBuilder sb22 = new StringBuilder();
				for (Integer id : idIntArray22) {
					Pay pay = payService.getPay(id);
					sb22.append("id=" + pay.getId() + "-->");
					int mogValue = pay.getMogValue();
					if (mogValue == 20000) {
						sb22.append("20V点卡面额 ");
					} else if (mogValue == 50000) {
						sb22.append("50V点卡面额 ");
					} else if (mogValue == 100000) {
						sb22.append("100V点卡面额 ");
					} else if (mogValue == 200000) {
						sb22.append("200V点卡面额 ");
					} else if (mogValue == 500000) {
						sb22.append("500V点卡面额 ");
					} else if (mogValue == 1000000) {
						sb22.append("1000V点卡面额 ");
					}
					sb22.append(",");
				}
				if (sb22 != null) {
					return sb22.substring(0, sb22.length() - 1);
				}
			} else {
				return idIntArray22.length + "条";
			}
		case "RoleController":
			Integer[] idIntArray23 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray23.length < 10) {
				StringBuilder sb23 = new StringBuilder();
				for (Integer id : idIntArray23) {
					Role role = roleService.getRoleById(id);
					sb23.append(role.getName() + ",");
				}
				if (sb23 != null) {
					return sb23.substring(0, sb23.length() - 1);
				}
			} else {
				return idIntArray23.length + "条";
			}
		case "UserController":

			Integer[] idIntArray24 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray24.length < 10) {
				StringBuilder sb24 = new StringBuilder();
				for (Integer id : idIntArray24) {
					User user = userService.getUserById(id);
					sb24.append(user.getName() + ",");
				}
				if (sb24 != null) {
					return sb24.substring(0, sb24.length() - 1);
				}
			} else {
				return idIntArray24.length + "条";
			}
		case "RingtonesController":
			Integer[] idIntArray25 = MyCollectionUtils.splitToIntArray(arg);
			String name1 = "";
			for (Integer id : idIntArray25) {
				PaginationBean params = new PaginationBean();
				params.getParams().put("id", id + "");
				List<MusicAlbumRes> list = musicAlbumResService.query(params);
				if (list != null && list.size() > 0) {
					Country country = list.get(0).getCountry();
					if (list.get(0).getMusicTheme() == null) {
						AppAlbum albumName = list.get(0).getAppAlbum();
						AppAlbumColumn appAlbumColumn = list.get(0).getAppAlbumColumn();
						name1 = country.getName() + "(" + country.getNameCn() + ")" + "-->" + albumName.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->";
					} else {
						MusicAlbumTheme theme = list.get(0).getMusicTheme();
						name1 = country.getName() + "(" + country.getNameCn() + ")" + "-->" + theme.getName() + "(" + theme.getNameCn() + ")" + "-->";
					}
				}
				if (name1 != null && !name1.equals("")) {
					break;
				}
			}
			if (idIntArray25.length < 10) {
				StringBuilder sb25 = new StringBuilder();

				for (Integer id : idIntArray25) {
					PaginationBean params = new PaginationBean();
					params.getParams().put("id", id + "");
					List<MusicAlbumRes> list = musicAlbumResService.query(params);
					if (list != null && list.size() > 0) {
						sb25.append(list.get(0).getMusicName() + ",");
					}
				}
				if (sb25 != null) {
					return name1 + sb25.substring(0, sb25.length() - 1);
				}
			} else {
				return name1 + idIntArray25.length + "条";
			}
		case "WallpaperController":
			Integer[] idIntArray26 = MyCollectionUtils.splitToIntArray(arg);
			String name2 = "";
			for (Integer id : idIntArray26) {
				PaginationBean params = new PaginationBean();
				params.getParams().put("id", id + "");
				List<ImageAlbumRes> list = imageAlbumResService.query(params);
				if (list != null && list.size() > 0) {
					Country country = list.get(0).getCountry();
					if (list.get(0).getImageAlbumTheme() == null) {
						AppAlbum albumName = list.get(0).getAppAlbum();
						AppAlbumColumn appAlbumColumn = list.get(0).getAppAlbumColumn();
						name2 = country.getName() + "(" + country.getNameCn() + ")" + "-->" + albumName.getName() + "-->" + appAlbumColumn.getName() + "(" + appAlbumColumn.getNameCn() + ")" + "-->";
					} else {
						ImageAlbumTheme theme = list.get(0).getImageAlbumTheme();
						name2 = country.getName() + "(" + country.getNameCn() + ")" + "-->" + theme.getName() + "(" + theme.getNameCn() + ")" + "-->";
					}
				}
				if (name2 != null && !name2.equals("")) {
					break;
				}
			}
			if (idIntArray26.length < 10) {
				StringBuilder sb26 = new StringBuilder();
				for (Integer id : idIntArray26) {
					PaginationBean params = new PaginationBean();
					params.getParams().put("id", id + "");
					List<ImageAlbumRes> list = imageAlbumResService.query(params);
					if (list != null && list.size() > 0) {
						sb26.append(list.get(0).getImageName() + ",");
					}
				}
				if (sb26 != null) {
					return name2 + sb26.substring(0, sb26.length() - 1);
				}
			} else {
				return name2 + idIntArray26.length + "条";
			}
		case "SearchKeywordController":

			Integer[] idIntArray27 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray27.length < 10) {
				StringBuilder sb27 = new StringBuilder();
				for (Integer id : idIntArray27) {
					SearchKeyword searchKeyword = searchKeywordService.selectByPrimaryKey(id);
					sb27.append(searchKeyword.getKeyword() + ",");
				}
				if (sb27 != null) {
					return sb27.substring(0, sb27.length() - 1);
				}
			} else {
				return idIntArray27.length + "条";
			}
		case "ClientFeedbackController":
			Integer[] idIntArray28 = MyCollectionUtils.splitToIntArray(arg);
			if (methodName.equals("deleteZapp")) {
				if (idIntArray28.length < 5) {
					StringBuilder sb28 = new StringBuilder();
					for (Integer id : idIntArray28) {
						ClientFeedbackZapp clientFeedbackZapp = clientFeedbackZappService.selectByPrimaryKey(id);
						sb28.append(clientFeedbackZapp.getQuestion() + ",");
					}
					if (sb28 != null) {
						return sb28.substring(0, sb28.length() - 1);
					}
				} else {
					return idIntArray28.length + "条";
				}
			} else if (methodName.equals("delete")) {
				if (idIntArray28.length < 5) {
					StringBuilder sb28 = new StringBuilder();
					for (Integer id : idIntArray28) {
						ClientFeedback clientFeedback = clientFeedbackService.getClientFeedback(id);
						if (clientFeedback != null) {
							sb28.append(clientFeedback.getContent() + "(" + clientFeedback.getClientId() + ")" + ",");
						}
					}
					if (sb28 != null) {
						return sb28.substring(0, sb28.length() - 1);
					}
				} else {
					return idIntArray28.length + "条";
				}
			} else if (methodName.equals("deleteByClientId")) {
				if (idIntArray28.length < 5) {
					StringBuilder sb28 = new StringBuilder();
					for (Integer id : idIntArray28) {
						sb28.append(id + ",");

					}
					if (sb28 != null) {
						return sb28.substring(0, sb28.length() - 1) + "/删除客户编号对应的所有记录";
					}
				} else {
					return idIntArray28.length + "条," + "/删除客户编号对应的所有记录";
				}
			}
		case "SearchKeywordIconController":

			Integer[] idIntArray29 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray29.length < 5) {
				StringBuilder sb29 = new StringBuilder();
				for (Integer id : idIntArray29) {
					SearchKeywordIcon searchKeywordIcon = searchKeywordIconService.getSearchKeywordIcon(id);
					sb29.append(searchKeywordIcon.getName() + "(" + searchKeywordIcon.getId() + ")" + ",");
				}
				if (sb29 != null) {
					return sb29.substring(0, sb29.length() - 1);
				}
			} else {
				return idIntArray29.length + "条";
			}
		case "AppFileZappUpdateController":

			Integer[] idIntArray30 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray30.length < 10) {
				StringBuilder sb30 = new StringBuilder();
				for (Integer id : idIntArray30) {
					AppFileZappUpdate appFileZappUpdate = appFileZappUpdateService.selectByPrimaryKey(id);
					sb30.append(appFileZappUpdate.getVersionName() + ",");
				}
				if (sb30 != null) {
					return sb30.substring(0, sb30.length() - 1);
				}
			} else {
				return idIntArray30.length + "条";
			}
		case "AppCollectionController":

			Integer[] idIntArray31 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray31.length < 10) {
				StringBuilder sb31 = new StringBuilder();
				for (Integer id : idIntArray31) {
					AppCollection appCollection = appCollectionService.getAppCollection(id);
					sb31.append(appCollection.getName() + ",");
				}
				if (sb31 != null) {
					return sb31.substring(0, sb31.length() - 1);
				}
			} else {
				return idIntArray31.length + "条";
			}
		case "AppMustHaveController":

			Integer[] idIntArray32 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray32.length < 10) {
				StringBuilder sb32 = new StringBuilder();
				for (Integer id : idIntArray32) {
					AppCollection appCollection = appCollectionService.getAppCollection(id);
					sb32.append(appCollection.getName() + ",");
				}
				if (sb32 != null) {
					return sb32.substring(0, sb32.length() - 1);
				}
			} else {
				return idIntArray32.length + "条";
			}
		case "OurPartnersController":

			Integer[] idIntArray33 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray33.length < 10) {
				StringBuilder sb33 = new StringBuilder();
				for (Integer id : idIntArray33) {
					OurPartners ourPartners = ourPartnersService.getOurPartners(id);
					sb33.append(ourPartners.getNameEn() + ",");
				}
				if (sb33 != null) {
					return sb33.substring(0, sb33.length() - 1);
				}
			} else {
				return idIntArray33.length + "条";
			}
		case "AppCountryScoreController":

			Integer[] idIntArray34 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray34.length < 10) {
				StringBuilder sb34 = new StringBuilder();
				for (Integer id : idIntArray34) {
					AppCountryScore appCountryScore = appCountryScoreService.selectByPrimaryKey(id);
					sb34.append(appCountryScore.getAppName() + ",");
				}
				if (sb34 != null) {
					return sb34.substring(0, sb34.length() - 1);
				}
			} else {
				return idIntArray34.length + "条";
			}
		case "AppSkinController":

			Integer[] idIntArray35 = MyCollectionUtils.splitToIntArray(arg);
			if (idIntArray35.length < 10) {
				StringBuilder sb34 = new StringBuilder();
				for (Integer id : idIntArray35) {
					ClientSkin skin = clientSkinService.selectByPrimaryKey(id);
					sb34.append(skin.getSkinName() + ",");
				}
				if (sb34 != null) {
					return sb34.substring(0, sb34.length() - 1);
				}
			} else {
				return idIntArray35.length + "条";
			}
		default:
			return null;
		}
	}

	private String sortLog(Object[] arg) {
		// 对表名为 tableName的做了排序更改
		String tableName = arg[1].toString();
		if (tableName.equals("t_app_album_column")) {
			return "栏目分发管理";
		} else if (tableName.equals("t_app_album_res_temp")) {
			return "分发管理";
		} else if (tableName.equals("t_app_album_res")) {
			return "动态壁纸分发管理";
		} else if (tableName.equals("t_app_collection_res")) {
			String type = arg[3].toString();
			if (type.equals("1")) {
				return "collection分发-->分发";
			} else {
				return "musthave分发-->分发";
			}

		} else if (tableName.equals("t_channel_info")) {
			return "平台渠道管理";
		} else if (tableName.equals("t_category")) {
			return "资源分类管理";

		} else if (tableName.equals("t_app_album_theme")) {
			return "平台广告管理";

		} else if (tableName.equals("t_res_music_album_res")) {
			return "铃声分发";

		} else if (tableName.equals("t_res_image_theme")) {
			return "壁纸主题管理";

		} else if (tableName.equals("t_res_music_theme")) {
			return "铃声主题管理";

		} else if (tableName.equals("t_res_image_album_res")) {
			return "壁纸分发";

		} else if (tableName.equals("t_search_keyword")) {
			return "搜索关键字";

		} else if (tableName.equals("t_search_keyword_reslist")) {
			return "搜索关键字列表";

		} else if (tableName.equals("t_app_picture")) {
			String type = arg[3].toString();
			if (type.equals("open")) {
				return "开发者APP截图信息 ";
			} else if (type.equals("app")) {
				return "自营APP截图信息 ";
			} else if (type.equals("market")) {
				return "平台更新APP截图信息 ";
			} else {
				return "全部APP截图信息 ";
			}

		} else if (tableName.equals("t_app_collection")) {
			String type = arg[3].toString();
			if (type.equals("1")) {
				return "collection分发";
			} else {
				return "collection分发";
			}
		} else if (tableName.equals("t_our_partners")) {
			return "合作伙伴";
		} else if (tableName.equals("t_client_skin")) {
			return "zApp皮肤";
		} else {
			return "";
		}
	}

	public static String getResourceName(String methodName, String className) {
		switch (className) {
		case "AppAlbumColumnController":
			return "栏目分发管理";
		case "AppAlbumThemeController":
			return "平台广告管理";
		case "AppCommentController":
			return "应用评论管理";
		case "AppFileController":
			if (methodName.equals("delete")) {
				return "自营、开发者apk文件管理";
			} else if (methodName.equals("doState")) {
				return "开发者app审核";
			} else {
				return "自营apk文件管理";
			}
		case "AppInfoConfigController":
			return "app配置管理";
		case "AppInfoController":
			return "自营app管理";
		case "AppPicController":
			return "截图管理";
		case "AppReportController":
			return "应用分发统计管理";
		case "AppSortController":
			return "排序";
		case "CategoryController":
			return "资源分类管理";
		case "ChannelController":
			return "平台渠道管理";
		case "ClientFeedbackController":
			return "平台回馈管理";
		case "CommunalController":
			return "全部app管理";
		case "CommunalFileController":
			return "全部apk文件管理";
		case "CountryController":
			return "平台国家管理";
		case "CpController":
			if (methodName.equals("doState")) {
				return "开发者CP审核";
			} else {
				return "平台CP管理";
			}
		case "ImageAlbumThemeController":
			return "壁纸专题管理";
		case "ImageInfoController":
			return "壁纸管理";
		case "LogController":
			return "日志管理管理";
		case "MarkAppFileController":
			return "平台更新apk文件管理";
		case "MarketController":
			return "平台更新管理";
		case "MenuController":
			return "菜单管理";
		case "MusicAlbumThemeController":
			return "铃声专题管理";
		case "MusicInfoController":
			return "铃声管理";
		case "OpenAppInfoController":
			return "开发者app管理";
		case "PayController":
			return "平台支付管理";
		case "PropertiesController":
			return "app排名配置";
		case "RingtonesController":
			return "铃声分发管理";
		case "RoleController":
			return "角色管理";
		case "UserController":
			return "用户管理";
		case "WallpaperController":
			return "壁纸分发管理";
		case "SearchKeywordController":
			return "搜索关键字管理";
		case "SearchKeywordIconController":
			return "图标管理/搜索关键字管理";
		case "ProxyIPController":
			return "代理IP";
		case "ClientCountryController":
			return "国家中英对应表 ";
		case "AppFileZappUpdateController":
			return "平台更新apk版本升级管理";
		case "AppCollectionController":
			return "collection分发 ";
		case "AppMustHaveController":
			return "musthave分发 ";
		case "AppannieInfoController":
			return "模拟分发 ";
		case "OpenBannerController":
			return "广告图管理";
		case "OurPartnersController":
			return "合作伙伴";
		case "AppCountryScoreController":
			return "强制排行管理";
		case "AppannieCountryRankController":
			return "appannie排行查询";
		case "AppSkinController":
			return "ZAPP皮肤管理";

		default:
			return "未知操作类型";
		}
	}

	public static String getMethodName(String methodName, String className) {
		switch (methodName) {
		case "add":
			return "增加";
		case "update":
			if (className.equals("OpenBannerController")) {
				return "替换";
			} else {
				return "编辑";
			}
		case "delete":
			return "删除";
		case "deleteById":
			return "删除";
		case "deleteByClientId":
			return "删除";
		case "disable":
			return "冻结";
		case "doConfig":
			return "分发/配置";
		case "collectionDoConfig":
			return "配置";
		case "liveWallpaperDoConfig":
			return "配置";
			// UserController
		case "login":
			return "登录";
		case "active":
			return "激活";
			// RoleController
		case "heigher":
			return "上升";
		case "lower":
			return "下降";
			// PropertiesController
		case "setScore":
			return "配置";
			// MusicInfoController
		case "batchAdd":
			return "批量入库";
			// menuController
		case "save":
			return "增加";
		case "editSave":
			return "编辑";
			// MarkAppFileController
		case "deleteFile":
			return "删除文件";
		case "addSecond":
			return "添加二级栏目";
			// 修改排序信息 AppSortController
		case "execute":
			return "修改";
		case "doState":
			return "上线(通过)/下线(取消)";
		case "top":
			return "appannie分发";
		case "appannieCountryRank":
			return "分发";
		case "doOperant":
			return "app生效";
			// 平台回馈管理
		case "addReply":
			return "添加回复";
		case "addZapp":
			return "添加常用回馈";
		case "updateZapp":
			return "修改常用回馈";
		case "deleteZapp":
			return "删除常用回馈";
		case "updateZappCode":
			return "更新常用回馈版本号";
		case "updateState":
			return "是否显示/启用";
		case "laterRanking":
			return "延迟手动排名";
		case "clearRanking":
			return "清除手动排名";
		case "updateRanking":
			return "保存手动排名";
		case "handDataCopy":
			return "复制手动排名";
		case "collectionDelete":
			return "删除";
		case "liveWallpaperDelete":
			return "删除";
		case "simulatedDistribution":
			return "模拟分发";
		case "copyRanking":
			return "复制强制排行";			
		default:
			return null;
		}
	}

	public String getAddOrUpdateName(Object o) {
		if (o instanceof AppAlbum) {
			return ((AppAlbum) o).getName();
		} else if (o instanceof AppAlbumRes) {
			return ((AppAlbumRes) o).getAlbumName();
		} else if (o instanceof AppAlbumColumn) {
			return ((AppAlbumColumn) o).getName();
		} else if (o instanceof AppAlbumTheme) {
			return ((AppAlbumTheme) o).getName();
		} else if (o instanceof AppComment) {
			return ((AppComment) o).getName();
		} else if (o instanceof AppFile) {
			return ((AppFile) o).getAppName();
		} else if (o instanceof AppFileList) {
			return ((AppFileList) o).getAppName();
		} else if (o instanceof AppFilePatch) {
			return ((AppFilePatch) o).getAppName();
		} else if (o instanceof AppInfo) {
			return ((AppInfo) o).getName();
		} else if (o instanceof AppInfoConfig) {
			return ((AppInfoConfig) o).getName();
		}
		// AppInfoRank 管理系统没有操作数据
		else if (o instanceof AppInfoRank) {
			return null;
		} else if (o instanceof AppPic) {
			if (((AppPic) o).getAppInfo() != null) {
				return ((AppPic) o).getAppInfo().getName();
			} else {
				return null;
			}
		}
		// AppScore 管理系统没有操作数据
		else if (o instanceof AppScore) {
			return null;
		} else if (o instanceof Category) {
			Category categoryFather = categoryService.getCategory(((Category) o).getFatherId());
			if (categoryFather != null) {
				return categoryFather.getName() + "-->" + ((Category) o).getName();
			} else {
				return ((Category) o).getName();
			}
		} else if (o instanceof Channel) {
			return ((Channel) o).getName();
		}
		// ClientFeedback
		else if (o instanceof ClientFeedback) {
			return ((ClientFeedback) o).getContent();
		} else if (o instanceof Country) {
			return ((Country) o).getName();
		} else if (o instanceof Cp) {
			return ((Cp) o).getName();
		} else if (o instanceof Market) {
			return ((Market) o).getName();
		} else if (o instanceof Menu) {
			return ((Menu) o).getName();
		} else if (o instanceof MenuType) {
			return ((MenuType) o).getName();
		} else if (o instanceof Operation) {
			return ((Operation) o).getName();
		} else if (o instanceof OperationType) {
			return ((OperationType) o).getName();
		}
		// Pay 管理系统没有操作数据
		else if (o instanceof Pay) {
			/**
			 * <option value="20000">20V</option> <option
			 * value="50000">50V</option> <option value="100000">100V</option>
			 * <option value="200000">200V</option> <option
			 * value="500000">500V</option> <option
			 * value="1000000">1000V</option>
			 */
			int mogValue = ((Pay) o).getMogValue();
			int id = ((Pay) o).getId();
			if (mogValue == 20000) {
				if (id != 0) {
					return "id=" + id + "-->" + "20V点卡面额 ";
				}
				return "20V点卡面额 ";
			} else if (mogValue == 50000) {
				if (id != 0) {
					return "id=" + id + "-->" + "50V点卡面额 ";
				}
				return "50V点卡面额 ";
			} else if (mogValue == 100000) {
				if (id != 0) {
					return "id=" + id + "-->" + "100V点卡面额 ";
				}
				return "100V点卡面额 ";
			} else if (mogValue == 200000) {
				if (id != 0) {
					return "id=" + id + "-->" + "200V点卡面额 ";
				}
				return "200V点卡面额 ";
			} else if (mogValue == 500000) {
				if (id != 0) {
					return "id=" + id + "-->" + "500V点卡面额 ";
				}
				return "500V点卡面额 ";
			} else if (mogValue == 1000000) {
				if (id != 0) {
					return "id=" + id + "-->" + "1000V点卡面额 ";
				}
				return "1000V点卡面额 ";
			} else {
				return "您选择的面值为其他";
			}
		} else if (o instanceof Province) {
			return ((Province) o).getName();
		} else if (o instanceof Role) {
			return ((Role) o).getName();
		} else if (o instanceof RoleMenu) {
			return ((RoleMenu) o).getRole().getName();
		} else if (o instanceof RoleOperation) {
			return ((RoleOperation) o).getRole().getName();
		} else if (o instanceof TAppDistribute) {
			return ((TAppDistribute) o).getAppName();
		} else if (o instanceof UserRole) {
			return ((UserRole) o).getUser().getName();
		} else if (o instanceof ImageAlbumRes) {
			return ((ImageAlbumRes) o).getImageInfo().getName();
		} else if (o instanceof ImageAlbumTheme) {
			return ((ImageAlbumTheme) o).getName();
		} else if (o instanceof ImageInfo) {
			return ((ImageInfo) o).getName();
		} else if (o instanceof MusicAlbumRes) {
			return ((MusicAlbumRes) o).getMusicInfo().getName();
		} else if (o instanceof MusicAlbumTheme) {
			return ((MusicAlbumTheme) o).getName();
		} else if (o instanceof MusicInfo) {
			return ((MusicInfo) o).getName();
		} else if (o instanceof User) {
			return ((User) o).getName();
		}
		// Log 管理系统没有操作数据
		else if (o instanceof Log) {
			return null;
		} else if (o instanceof SearchKeyword) {
			return ((SearchKeyword) o).getKeyword();
		} else if (o instanceof SearchKeywordIcon) {
			return ((SearchKeywordIcon) o).getName();
		} else if (o instanceof ClientFeedbackZapp) {
			return ((ClientFeedbackZapp) o).getQuestion() + "/常用回馈管理 ";
		} else if (o instanceof ClientCountry) {
			return ((ClientCountry) o).getCountryCn();
		} else if (o instanceof AppFileZappUpdate) {
			return ((AppFileZappUpdate) o).getVersionName();
		} else if (o instanceof AppCollection) {
			return ((AppCollection) o).getName();
		} else if (o instanceof OurPartners) {
			return ((OurPartners) o).getNameEn();
		} else if (o instanceof AppCountryScore) {
			return ((AppCountryScore) o).getAppName();
		} else if (o instanceof AppannieInfoCountryRank) {
			return ((AppannieInfoCountryRank) o).getAppName();
		} else if (o instanceof ClientSkin) {
			return ((ClientSkin) o).getSkinName();
		} else {
			return null;
		}

	}

}
