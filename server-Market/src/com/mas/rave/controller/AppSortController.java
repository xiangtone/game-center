package com.mas.rave.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.dao.AppCollectionResMapper;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumTheme;
import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.ClientSkin;
import com.mas.rave.main.vo.ImageAlbumRes;
import com.mas.rave.main.vo.ImageAlbumTheme;
import com.mas.rave.main.vo.MusicAlbumRes;
import com.mas.rave.main.vo.MusicAlbumTheme;
import com.mas.rave.main.vo.OurPartners;
import com.mas.rave.main.vo.SearchKeyword;
import com.mas.rave.main.vo.SearchKeywordResList;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.service.AppAlbumResService;
import com.mas.rave.service.AppAlbumResTempService;
import com.mas.rave.service.AppAlbumThemeService;
import com.mas.rave.service.AppCollectionService;
import com.mas.rave.service.AppPicService;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.ChannelService;
import com.mas.rave.service.ClientSkinService;
import com.mas.rave.service.ImageAlbumResService;
import com.mas.rave.service.ImageAlbumThemeService;
import com.mas.rave.service.MusicAlbumResService;
import com.mas.rave.service.MusicAlbumThemeService;
import com.mas.rave.service.OurPartnersService;
import com.mas.rave.service.SearchKeywordResListService;
import com.mas.rave.service.SearchKeywordService;
import com.mysql.jdbc.MysqlDataTruncation;

/**
 * 修改排序
 * 
 * @author lisong.lan
 * 
 */

@Controller
@RequestMapping("/appSort")
public class AppSortController {

	private final Logger log = Logger.getLogger(AppSortController.class);

	@Autowired
	private AppAlbumColumnService appAlbumColumnService;
	@Autowired
	private AppAlbumResTempService appAlbumResTempService;
	@Autowired
	private AppAlbumResService appAlbumResService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private AppAlbumThemeService appAlbumThemeService;

	@Autowired
	private MusicAlbumResService musicAlbumResService;

	@Autowired
	private ImageAlbumThemeService imageAlbumThemeService;

	@Autowired
	private MusicAlbumThemeService musicAlbumThemeService;

	@Autowired
	private ImageAlbumResService<ImageAlbumRes> imageAlbumResService;

	@Autowired
	private SearchKeywordService searchKeywordService;

	@Autowired
	private SearchKeywordResListService searchKeywordResListService;

	@Autowired
	private AppCollectionService appCollectionService;

	@Autowired
	private AppCollectionResMapper appCollectionResMapper;

	@Autowired
	private AppPicService appPicService;

	@Autowired
	private OurPartnersService ourPartnersService;

	@Autowired
	private ClientSkinService clientSkinService;

	@ResponseBody
	@RequestMapping("/execute")
	public String execute(HttpServletRequest request, @RequestParam String tableName, @RequestParam String paramstr, @RequestParam(value = "type", required = false) String type) {

		if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(paramstr)) {
			// 不进行任何操作
			return "{\"success\":\"0\"}";
		}

		String[] params = paramstr.split("&");

		// 判断要修改的数据库表名
		if (tableName.equals("t_app_album_column")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					AppAlbumColumn appAlbumColumn = new AppAlbumColumn();
					int columnId = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_app_album_column sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					appAlbumColumn.setColumnId(columnId);
					appAlbumColumn.setSort(sort);
					appAlbumColumnService.updateSortByPrimaryKey(appAlbumColumn);
				}
				log.info("update t_app_album_column sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_app_album_column sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_app_album_res_temp")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					AppAlbumRes appAlbumResTemp = new AppAlbumRes();
					int id = Integer.parseInt(param[0]);
					double sort = 0;
					try {
						sort = Double.parseDouble(param[1]);
					} catch (Exception e1) {
						log.error("update t_app_album_column sort datas is not a Double ! ", e1);
						return "{\"success\":\"3\"}";
					}
					appAlbumResTemp.setId(id);
					appAlbumResTemp.setSort(sort);
					appAlbumResTempService.updateSortByPrimarykey(appAlbumResTemp);
				}
				log.info("update t_app_album_res_temp sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_app_album_res_temp sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}
		if (tableName.equals("t_app_album_res")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					AppAlbumRes appAlbumRes = new AppAlbumRes();
					int id = Integer.parseInt(param[0]);
					double sort = 0;
					try {
						sort = Double.parseDouble(param[1]);
					} catch (Exception e1) {
						log.error("update t_app_album_res sort datas is not a Double ! ", e1);
						return "{\"success\":\"3\"}";
					}
					appAlbumRes.setId(id);
					appAlbumRes.setSort(sort);
					appAlbumResService.updateSortByPrimarykey(appAlbumRes);
				}
				log.info("update t_app_album_res sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_app_album_res sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}
		if (tableName.equals("t_channel_info")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					Channel channel = new Channel();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_channel_info sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					channel.setId(id);
					channel.setSort(sort);
					channelService.updateSortByPrimarykey(channel);
				}
				log.info("update t_channel_info sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_channel_info sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_category")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					Category category = new Category();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_category sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					category.setId(id);
					category.setSort(sort);
					categoryService.updateSortByPrimarykey(category);
				}
				log.info("update t_category sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_category sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_app_album_theme")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					AppAlbumTheme appAlbumTheme = new AppAlbumTheme();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_app_album_theme sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					appAlbumTheme.setThemeId(id);
					appAlbumTheme.setSort(sort);
					appAlbumThemeService.updateSortByPrimarykey(appAlbumTheme);
				}
				log.info("update t_app_album_theme sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_app_album_theme sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_res_music_album_res")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					MusicAlbumRes musicAlbumRes = new MusicAlbumRes();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_res_music_album_res sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					musicAlbumRes.setId(id);
					musicAlbumRes.setSort(sort);
					musicAlbumResService.updateById(musicAlbumRes);
				}
				log.info("update t_res_music_album_res sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_res_music_album_res sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_res_image_theme")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					ImageAlbumTheme imageAlbumTheme = new ImageAlbumTheme();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_res_image_theme sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					imageAlbumTheme.setThemeId(id);
					imageAlbumTheme.setSort(sort);
					imageAlbumThemeService.updateSortByPrimarykey(imageAlbumTheme);
				}
				log.info("update t_res_image_theme sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_res_image_theme sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_res_music_theme")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					MusicAlbumTheme musicAlbumTheme = new MusicAlbumTheme();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_res_music_theme sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					musicAlbumTheme.setThemeId(id);
					musicAlbumTheme.setSort(sort);
					musicAlbumThemeService.updateSortByPrimarykey(musicAlbumTheme);
				}
				log.info("update t_res_music_theme sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_res_music_theme sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_res_image_album_res")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					ImageAlbumRes imageAlbumRes = new ImageAlbumRes();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_res_image_album_res sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					imageAlbumRes.setId(id);
					imageAlbumRes.setSort(sort);
					imageAlbumResService.updateById(imageAlbumRes);
				}
				log.info("update t_res_image_album_res sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_res_image_album_res sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_search_keyword")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_search_keyword sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					SearchKeyword searchKeyword = searchKeywordService.selectByPrimaryKey(id);
					searchKeyword.setSort(sort);
					searchKeywordService.updateByPrimaryKey(searchKeyword);
				}
				log.info("update t_search_keyword sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_search_keyword sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_search_keyword_reslist")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_search_keyword_reslist sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					SearchKeywordResList searchKeywordResList = searchKeywordResListService.selectByPrimaryKey(id);
					searchKeywordResList.setSort(sort);
					searchKeywordResListService.updateByPrimaryKey(searchKeywordResList);
				}
				log.info("update t_search_keyword_reslist sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_search_keyword_reslist sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_app_collection")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					AppCollection appCollection = new AppCollection();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_app_collection sort datas is not a integer ! ", e1);
						return "{\"success\":\"2\"}";
					}
					appCollection.setCollectionId(id);
					appCollection.setSort(sort);
					appCollectionService.updateSortByPrimarykey(appCollection);
				}
				log.info("update t_app_collection sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_app_collection sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}

		if (tableName.equals("t_app_collection_res")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					AppAlbumRes appAlbumRes = new AppAlbumRes();
					int id = Integer.parseInt(param[0]);
					double sort = 0;
					try {
						sort = Double.parseDouble(param[1]);
					} catch (Exception e1) {
						log.error("update t_app_collection_res sort datas is not a Double ! ", e1);
						return "{\"success\":\"3\"}";
					}
					appAlbumRes.setId(id);
					appAlbumRes.setSort(sort);
					appCollectionResMapper.updateSortByPrimarykey(appAlbumRes);
				}
				log.info("update t_app_collection_res sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_app_collection_res sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}
		if (tableName.equals("t_app_picture")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					AppPic pic = new AppPic();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_app_picture sort datas is not a Double ! ", e1);
						return "{\"success\":\"3\"}";
					}
					pic.setId(id);
					pic.setSort(sort);
					appPicService.updateSortByPrimarykey(pic);
				}
				log.info("update t_app_picture sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_app_picture sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}
		if (tableName.equals("t_our_partners")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					OurPartners our = new OurPartners();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_our_partners sort datas is not a Double ! ", e1);
						return "{\"success\":\"3\"}";
					}
					our.setId(id);
					our.setSort(sort);
					ourPartnersService.updateSortByPrimarykey(our);
				}
				log.info("update t_our_partners sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_our_partners sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}
		if (tableName.equals("t_client_skin")) {
			try {
				for (int i = 0; i < params.length; i++) {
					String[] param = params[i].split("#");
					ClientSkin skin = new ClientSkin();
					int id = Integer.parseInt(param[0]);
					int sort = 0;
					try {
						sort = Integer.parseInt(param[1]);
					} catch (Exception e1) {
						log.error("update t_client_skin sort datas is not a Double ! ", e1);
						return "{\"success\":\"3\"}";
					}
					skin.setSkinId(id);
					skin.setSort(sort);
					clientSkinService.updateSortByPrimarykey(skin);
				}
				log.info("update t_client_skin sort datas success !");
				return "{\"success\":\"1\"}";
			} catch (Exception e) {
				if (e.getCause() instanceof MysqlDataTruncation) {
					return "{\"success\":\"4\"}";
				}
				log.error("update t_client_skin sort datas failed ! ", e);
				return "{\"success\":\"0\"}";
			}
		}
		return "{\"success\":\"0\"}";
	}
}
