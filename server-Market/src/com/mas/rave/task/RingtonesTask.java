package com.mas.rave.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioHeader;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mas.rave.common.web.MusicParseUtils;
import com.mas.rave.controller.MusicInfoController;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.MusicInfo;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.MarketService;
import com.mas.rave.service.MusicInfoService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.CrawlerFileUtils;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.PinyinToolkit;
import com.mas.rave.util.s3.S3Util;

public class RingtonesTask extends Thread {
	
	Logger log = Logger.getLogger(RingtonesTask.class);

	private MarketService marketService;

	private MusicInfoService musicInfoService;

	private CategoryService categoryService;
	
	
	
	public void setMarketService(MarketService marketService) {
		this.marketService = marketService;
	}
	public void setMusicInfoService(MusicInfoService musicInfoService) {
		this.musicInfoService = musicInfoService;
	}
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	private Elements elements;
	private String filePath;
	private String loginUser;
	
	public RingtonesTask(){}
	public RingtonesTask(Elements elements,String filePath,String loginUser){
		this.elements= elements;
		this.filePath = filePath;
		this.loginUser = loginUser;
	}
	@Override
	public void run() {
		for (Element element : elements) {			
				addRingtonesByElement(element, filePath, loginUser);
			
		}
		MusicInfoController.isMusicBatchAddThreadRun=false;
	}
	/**
	 * 添加单个音乐
	 * @param element
	 * @throws IOException 
	 */
	public void addRingtonesByElement(Element element,String filePath,String loginUser){
		try {
			Category category = addCategory(element);
			MusicInfo music = new MusicInfo();
			String ringtoneName =	element.select("div.title").first().select("a").text().replaceAll("[\\/:?*<>|.#'\"$@%^&*,;:]", "");
			String ringtoneDownloadUrl  = element.children().last().attr("href");
			String data_url = element.select("div.title").first().attr("data-url");
			String exten  = CrawlerFileUtils.getExtension(data_url);
			List<MusicInfo> musicList = null;
			if(ringtoneName!=null&&!ringtoneName.equals("")){
				musicList = musicInfoService.selectByName(ringtoneName);
			}
			List<String> fileUrlList = new ArrayList<String>();//s3 list

			//查询数据库中是否存在数据，如果存在		
			if(musicList==null||musicList.size()==0){
				// ringtones/Blues/ringtoneName/ringtoneName.mp3
				String str = MD5.getMd5Value(System.currentTimeMillis() + ringtoneName);
				String fileName = filePath +File.separator  + str+Constant.LOCAL_MUSIC_PATH
						+ str + exten;
//				System.out.println(fileName + "   "+ringtoneDownloadUrl);
					try {
						CrawlerFileUtils.saveToFile(ringtoneDownloadUrl,fileName);
					} catch (IOException e) {
						log.error("RingtonesTask addRingtonesByElement", e);
					} 
					File filenew  = new File(fileName);
					if(filenew.exists()&&filenew.length()!=0){
						MusicParseUtils musicParse = new MusicParseUtils();	
						AudioHeader audioh = musicParse.getAudioHeader(filenew);
						if(audioh!=null){
							music.setDuration(audioh.getTrackLength());//获取音乐播放时长						
						}
						music.setFileSize((int)filenew.length());//获取文件大小
						music.setUrl(FileAddresUtil.getFilePath(filenew));//音乐下载地址
						fileUrlList.add(music.getUrl());

						music.setName(ringtoneName);
						music.setOperator("batchmag");
	
						music.setCategory(category);
						//设置拼音
						music.setPinyin(PinyinToolkit.cn2Spell(music.getName()));
						music.setFree(0);
						music.setRaveId(1);
						music.setStars(5);
						music.setState(true);
						com.mas.rave.util.mp3.MusicInfo musicInfo=new com.mas.rave.util.mp3.MusicInfo();
						try {       
							if(musicInfo.hasImage(fileName)){

								//String musicfilename = FileUtil.trimExtension(fileName);
								String logoPath = filePath +File.separator  + str+Constant.LOCAL_APK_LOG;
								File file11 =	com.mas.rave.util.mp3.MusicInfo.writeImage(musicInfo.getImage(), logoPath,str);
								if(file11!=null){
									music.setLogo(FileAddresUtil.getFilePath(file11));	
									fileUrlList.add(music.getLogo());

								}else{
									log.info(ringtoneName+"没有图片");
								}
							
							}else{
								log.info(ringtoneName+"没有图片");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("MusicInfoController add get music image error", e);
						}
						
						List<MusicInfo> musicList1 = musicInfoService.selectByName(ringtoneName);
						if(musicList1==null||musicList1.size()==0){
	    				    musicInfoService.addMusicInfo(music, category.getId());    
						}
					}	
			}
			//s3upload
			S3Util.uploadS3(fileUrlList, false);	

		} catch (Exception e) {
			log.error("RingtonesTask addRingtonesByElement", e);
		} 
	}
	/**
	 * 添加菜单栏
	 * @param element
	 * @return
	 */
	public Category addCategory(Element element) throws Exception{
		String firstCategory = "Ringtones";
		int fatherId = 1;
		Category category0 = new Category();
		category0.setName(firstCategory);
		category0.setFatherId(fatherId);
		List<Category> listfather = categoryService.getCategorysByName(category0);
		//如果一级菜单没有，添加一级菜单
		if(listfather==null||listfather.size()==0){
			Category category  = new Category();
			category.setFatherId(1); // 一级分类标识
			category.setName(firstCategory);
			category.setCategoryCn("铃声");
			category.setMarketInfo(marketService.getMarket(1));
			category.setState(true);
			category.setSort(0);
			categoryService.addCategory(category);
			
			Category category1 = new Category();
			category1.setName(category.getName());
			category1.setFatherId(category.getFatherId());
			List<Category> listfather1 = categoryService.getCategorysByName(category1);
			fatherId = listfather1.get(0).getId();
		}else{
			fatherId = listfather.get(0).getId();
		}
		Category category1  = new Category();

		String name = element.select("div.title").first().attr("data-typeName");
		//category logo 下载路径
		
		List<Category> list = null;
		if(name!=null&&!name.equals("")){
			
			Category category2 = new Category();
			category2.setName(name);
			category2.setFatherId(fatherId);
			
			list =	categoryService.getCategorysByName(category2);	
			//如果二级菜单不存在 创建二级菜单
			if(list==null||list.size()==0){
					category1.setFatherId(fatherId);
					category1.setName(name.trim());
					category1.setMarketInfo(marketService.getMarket(1));
					category1.setState(true);
					category1.setSort(0);
					categoryService.addCategory(category1);
					
					Category category3 = new Category();
					category3.setName(name);
					category3.setFatherId(fatherId);
					List<Category> list1= categoryService.getCategorysByName(category3);
					return list1.get(0);						
				}else{
					return list.get(0);
	
				}	
		}
		return null;
	}
}
