package com.mas.rave.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mas.rave.main.vo.AppScore;
import com.mas.rave.service.AppScoreService;
import com.mas.rave.util.ConstantScore;
import com.mas.rave.vo.ScoreVO;

/**
 * 修改配置
 * 
 * @author lisong.lan
 * 
 */

@Controller
@RequestMapping("/properties")
public class PropertiesController {


	Logger log = Logger.getLogger(PropertiesController.class);
	@Autowired
	private AppScoreService appScoreService;

	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request) {
		initAppConfig();
		/*
		 * ScoreVO score = new ScoreVO();
		 * score.setAnnieInstallTotal(ConstantScore.ANNIEINSTALLTOTAL);
		 * score.setAnnieInstallAverage(ConstantScore.ANNIEINSTALLAVERAGE);
		 * score.setAnnieRatings(ConstantScore.ANNIERATINGS);
		 * score.setInitialTime(ConstantScore.INITIALTIME);
		 * score.setAnnieRank(ConstantScore.ANNIERANK);
		 * score.setCategory1(ConstantScore.CATEGORY1);
		 * score.setCategory2(ConstantScore.CATEGORY2);
		 * score.setCategory3(ConstantScore.CATEGORY3);
		 * score.setCategory4(ConstantScore.CATEGORY4);
		 * score.setCategory5(ConstantScore.CATEGORY5);
		 * score.setCategory6(ConstantScore.CATEGORY6);
		 * score.setCategory7(ConstantScore.CATEGORY7);
		 * score.setCategory8(ConstantScore.CATEGORY8);
		 * score.setCategory9(ConstantScore.CATEGORY9);
		 * score.setCategory10(ConstantScore.CATEGORY10);
		 * score.setSize(ConstantScore.SIZE);
		 * score.setRealDownload(ConstantScore.REALDOWNLOAD);
		 * score.setDownloadAverage(ConstantScore.DOWNLOADAVERAGE);
		 * score.setPageviews(ConstantScore.PAGEVIEWS);
		 * score.setRatings(ConstantScore.RATINGS);
		 * score.setHomeRecommand(ConstantScore.HOMERECOMMAND);
		 * score.setHomeNew(ConstantScore.HOMENEW);
		 * score.setHomeTop(ConstantScore.HOMETOP);
		 * score.setHomePopular(ConstantScore.HOMEPOPULAR);
		 * score.setAppHot(ConstantScore.APPHOT);
		 * score.setAppTop(ConstantScore.APPTOP);
		 * score.setAppNew(ConstantScore.APPNEW);
		 * score.setGameHot(ConstantScore.GAMEHOT);
		 * score.setGameTop(ConstantScore.GAMETOP);
		 * score.setGameNew(ConstantScore.GAMENEW);
		 * model.addAttribute("properties", score);
		 */
		return "properties/list";
	}

	@ResponseBody
	@RequestMapping(value = "/setScore", method = RequestMethod.POST)
	public String setScore(HttpServletRequest request, @ModelAttribute ScoreVO score) {

		List<AppScore> scores = appScoreService.getAllScore();
		if (scores != null && scores.size() > 0) {
			for (AppScore appSocre : scores) {
				if (appSocre.getScoreKey().equals("AnnieInstallTotal")) {
					if (!score.getAnnieInstallTotal().equals(appSocre.getScoreValue())) {
						ConstantScore.ANNIEINSTALLTOTAL = score.getAnnieInstallTotal();
						appSocre.setScoreValue(score.getAnnieInstallTotal());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("AnnieInstallAverage")) {
					if (!score.getAnnieInstallAverage().equals(appSocre.getScoreValue())) {
						ConstantScore.ANNIEINSTALLAVERAGE = score.getAnnieInstallAverage();
						appSocre.setScoreValue(score.getAnnieInstallAverage());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("AnnieRatings")) {
					if (!score.getAnnieRatings().equals(appSocre.getScoreValue())) {
						ConstantScore.ANNIERATINGS = score.getAnnieRatings();
						appSocre.setScoreValue(score.getAnnieRatings());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("InitialTime")) {
					if (!score.getInitialTime().equals(appSocre.getScoreValue())) {
						ConstantScore.INITIALTIME = score.getInitialTime();
						appSocre.setScoreValue(score.getInitialTime());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("AnnieRank")) {
					if (!score.getAnnieRank().equals(appSocre.getScoreValue())) {
						ConstantScore.ANNIERANK = score.getAnnieRank();
						appSocre.setScoreValue(score.getAnnieRank());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category1")) {
					if (!score.getCategory1().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY1 = score.getCategory1();
						appSocre.setScoreValue(score.getCategory1());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category2")) {
					if (!score.getCategory2().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY2 = score.getCategory2();
						appSocre.setScoreValue(score.getCategory2());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category3")) {
					if (!score.getCategory3().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY3 = score.getCategory3();
						appSocre.setScoreValue(score.getCategory3());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category4")) {
					if (!score.getCategory4().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY4 = score.getCategory4();
						appSocre.setScoreValue(score.getCategory3());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category5")) {
					if (!score.getCategory5().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY5 = score.getCategory5();
						appSocre.setScoreValue(score.getCategory5());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category6")) {
					if (!score.getCategory6().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY6 = score.getCategory6();
						appSocre.setScoreValue(score.getCategory6());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category7")) {
					if (!score.getCategory7().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY7 = score.getCategory7();
						appSocre.setScoreValue(score.getCategory7());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category8")) {
					if (!score.getCategory8().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY8 = score.getCategory8();
						appSocre.setScoreValue(score.getCategory8());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category9")) {
					if (!score.getCategory9().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY9 = score.getCategory9();
						appSocre.setScoreValue(score.getCategory9());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Category10")) {
					if (!score.getCategory10().equals(appSocre.getScoreValue())) {
						ConstantScore.CATEGORY10 = score.getCategory10();
						appSocre.setScoreValue(score.getCategory10());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Size")) {
					if (!score.getSize().equals(appSocre.getScoreValue())) {
						ConstantScore.SIZE = score.getSize();
						appSocre.setScoreValue(score.getSize());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("RealDownload")) {
					if (!score.getRealDownload().equals(appSocre.getScoreValue())) {
						ConstantScore.REALDOWNLOAD = score.getRealDownload();
						appSocre.setScoreValue(score.getRealDownload());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("DownloadAverage")) {
					if (!score.getDownloadAverage().equals(appSocre.getScoreValue())) {
						ConstantScore.DOWNLOADAVERAGE = score.getDownloadAverage();
						appSocre.setScoreValue(score.getDownloadAverage());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Pageviews")) {
					if (!score.getPageviews().equals(appSocre.getScoreValue())) {
						ConstantScore.PAGEVIEWS = score.getPageviews();
						appSocre.setScoreValue(score.getPageviews());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Ratings")) {
					if (!score.getRatings().equals(appSocre.getScoreValue())) {
						ConstantScore.RATINGS = score.getRatings();
						appSocre.setScoreValue(score.getRatings());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("HomeRecommand")) {
					if (!score.getHomeRecommand().equals(appSocre.getScoreValue())) {
						ConstantScore.HOMERECOMMAND = score.getHomeRecommand();
						appSocre.setScoreValue(score.getHomeRecommand());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("HomeNew")) {
					if (!score.getHomeNew().equals(appSocre.getScoreValue())) {
						ConstantScore.HOMENEW = score.getHomeNew();
						appSocre.setScoreValue(score.getHomeNew());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("HomeTop")) {
					if (!score.getHomeTop().equals(appSocre.getScoreValue())) {
						ConstantScore.HOMETOP = score.getHomeTop();
						appSocre.setScoreValue(score.getHomeTop());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("HomePopular")) {
					if (!score.getHomePopular().equals(appSocre.getScoreValue())) {
						ConstantScore.HOMEPOPULAR = score.getHomePopular();
						appSocre.setScoreValue(score.getHomePopular());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("AppHot")) {
					if (!score.getAppHot().equals(appSocre.getScoreValue())) {
						ConstantScore.APPHOT = score.getAppHot();
						appSocre.setScoreValue(score.getAppHot());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("AppTop")) {
					if (!score.getAppTop().equals(appSocre.getScoreValue())) {
						ConstantScore.APPTOP = score.getAppTop();
						appSocre.setScoreValue(score.getAppTop());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("AppNew")) {
					if (!score.getAppNew().equals(appSocre.getScoreValue())) {
						ConstantScore.APPNEW = score.getAppNew();
						appSocre.setScoreValue(score.getAppNew());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("GameHot")) {
					if (!score.getGameHot().equals(appSocre.getScoreValue())) {
						ConstantScore.GAMEHOT = score.getGameHot();
						appSocre.setScoreValue(score.getGameHot());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("GameTop")) {
					if (!score.getGameTop().equals(appSocre.getScoreValue())) {
						ConstantScore.GAMETOP = score.getGameTop();
						appSocre.setScoreValue(score.getGameTop());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("GameNew")) {
					if (!score.getGameNew().equals(appSocre.getScoreValue())) {
						ConstantScore.GAMENEW = score.getGameNew();
						appSocre.setScoreValue(score.getGameNew());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Global")) {
					if (!score.getGlobal().equals(appSocre.getScoreValue())) {
						ConstantScore.GLOBAL = score.getGlobal();
						appSocre.setScoreValue(score.getGlobal());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("Email")) {
					if (!score.getEmail().equals(appSocre.getScoreValue())) {
						ConstantScore.EMAIL = score.getEmail();
						appSocre.setScoreValue(score.getEmail());
						appScoreService.updateSore(appSocre);
					}
				} else if (appSocre.getScoreKey().equals("SimulatedSwitch")) {
					if (!score.getSimulatedSwitch().equals(appSocre.getScoreValue())) {
						ConstantScore.SIMULATEDSWITCH = score.getSimulatedSwitch();
						appSocre.setScoreValue(score.getSimulatedSwitch());
						appScoreService.updateSore(appSocre);
					}
				}
			}
		}

		/*
		 * ConfigDateApplication.getInstance().setScorePropertie("AnnieInstallTotal"
		 * , score.getAnnieInstallTotal());
		 * ConfigDateApplication.getInstance().setScorePropertie
		 * ("AnnieInstallAverage", score.getAnnieInstallAverage());
		 * ConfigDateApplication.getInstance().setScorePropertie("AnnieRatings",
		 * score.getAnnieRatings());
		 * ConfigDateApplication.getInstance().setScorePropertie("InitialTime",
		 * score.getInitialTime());
		 * ConfigDateApplication.getInstance().setScorePropertie("AnnieRank",
		 * score.getAnnieRank());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category1",
		 * score.getCategory1());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category2",
		 * score.getCategory2());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category3",
		 * score.getCategory3());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category4",
		 * score.getCategory4());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category5",
		 * score.getCategory5());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category6",
		 * score.getCategory6());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category7",
		 * score.getCategory7());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category8",
		 * score.getCategory8());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category9",
		 * score.getCategory9());
		 * ConfigDateApplication.getInstance().setScorePropertie("Category10",
		 * score.getCategory10());
		 * ConfigDateApplication.getInstance().setScorePropertie("Size",
		 * score.getSize());
		 * ConfigDateApplication.getInstance().setScorePropertie("RealDownload",
		 * score.getRealDownload());
		 * ConfigDateApplication.getInstance().setScorePropertie
		 * ("DownloadAverage", score.getDownloadAverage());
		 * ConfigDateApplication.getInstance().setScorePropertie("Pageviews",
		 * score.getPageviews());
		 * ConfigDateApplication.getInstance().setScorePropertie("Ratings",
		 * score.getPageviews());
		 * ConfigDateApplication.getInstance().setScorePropertie
		 * ("HomeRecommand", score.getHomeRecommand());
		 * ConfigDateApplication.getInstance().setScorePropertie("HomeNew",
		 * score.getHomeNew());
		 * ConfigDateApplication.getInstance().setScorePropertie("HomeTop",
		 * score.getHomeTop());
		 * ConfigDateApplication.getInstance().setScorePropertie("HomePopular",
		 * score.getHomePopular());
		 * ConfigDateApplication.getInstance().setScorePropertie("AppHot",
		 * score.getAppHot());
		 * ConfigDateApplication.getInstance().setScorePropertie("AppTop",
		 * score.getAppTop());
		 * ConfigDateApplication.getInstance().setScorePropertie("AppNew",
		 * score.getAppNew());
		 * ConfigDateApplication.getInstance().setScorePropertie("GameHot",
		 * score.getGameHot());
		 * ConfigDateApplication.getInstance().setScorePropertie("GameTop",
		 * score.getGameTop());
		 * ConfigDateApplication.getInstance().setScorePropertie("GameNew",
		 * score.getGameNew());
		 */
		return "{\"flag\":\"0\"}";
	}

	public void initAppConfig() {
		List<AppScore> scores = appScoreService.getAllScore();
		if (scores != null && scores.size() > 0) {
			if (ConstantScore.map.get("configList") == null) {
				// 启用初始
				ConstantScore.map.put("configList", scores);
			}

			for (AppScore appSocre : scores) {
				if (appSocre.getScoreKey().equals("AnnieInstallTotal")) {
					ConstantScore.ANNIEINSTALLTOTAL = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("AnnieInstallAverage")) {
					ConstantScore.ANNIEINSTALLAVERAGE = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("AnnieRatings")) {
					ConstantScore.ANNIERATINGS = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("InitialTime")) {
					ConstantScore.INITIALTIME = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("AnnieRank")) {
					ConstantScore.ANNIERANK = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category1")) {
					ConstantScore.CATEGORY1 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category2")) {
					ConstantScore.CATEGORY2 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category3")) {
					ConstantScore.CATEGORY3 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category4")) {
					ConstantScore.CATEGORY4 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category5")) {
					ConstantScore.CATEGORY5 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category6")) {
					ConstantScore.CATEGORY6 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category7")) {
					ConstantScore.CATEGORY7 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category8")) {
					ConstantScore.CATEGORY8 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category9")) {
					ConstantScore.CATEGORY9 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Category10")) {
					ConstantScore.CATEGORY10 = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Size")) {
					ConstantScore.SIZE = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("RealDownload")) {
					ConstantScore.REALDOWNLOAD = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("DownloadAverage")) {
					ConstantScore.DOWNLOADAVERAGE = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Pageviews")) {
					ConstantScore.PAGEVIEWS = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Ratings")) {
					ConstantScore.RATINGS = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("HomeRecommand")) {
					ConstantScore.HOMERECOMMAND = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("HomeNew")) {
					ConstantScore.HOMENEW = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("HomeTop")) {
					ConstantScore.HOMETOP = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("HomePopular")) {
					ConstantScore.HOMEPOPULAR = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("AppHot")) {
					ConstantScore.APPHOT = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("AppTop")) {
					ConstantScore.APPTOP = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("AppNew")) {
					ConstantScore.APPNEW = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("GameHot")) {
					ConstantScore.GAMEHOT = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("GameTop")) {
					ConstantScore.GAMETOP = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("GameNew")) {
					ConstantScore.GAMENEW = appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Global")) {
					ConstantScore.GLOBAL= appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("Email")) {
					ConstantScore.EMAIL= appSocre.getScoreValue();
				} else if (appSocre.getScoreKey().equals("SimulatedSwitch")) {
					ConstantScore.SIMULATEDSWITCH= appSocre.getScoreValue();
				}
			}
		}
	}

}
