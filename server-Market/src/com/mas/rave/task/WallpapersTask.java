package com.mas.rave.task;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.mas.rave.controller.ImageInfoController;
import com.mas.rave.main.vo.Category;
import com.mas.rave.main.vo.ImageInfo;
import com.mas.rave.service.CategoryService;
import com.mas.rave.service.ImageInfoService;
import com.mas.rave.service.MarketService;
import com.mas.rave.util.Constant;
import com.mas.rave.util.CrawlerFileUtils;
import com.mas.rave.util.FileAddresUtil;
import com.mas.rave.util.FileUtil;
import com.mas.rave.util.MD5;
import com.mas.rave.util.PinyinToolkit;
import com.mas.rave.util.RandNum;
import com.mas.rave.util.s3.S3Util;

public class WallpapersTask extends Thread {
	private static String picDownloadUrl = "http://server.voga360.com/wallpaper/download.htm?url=http://upload.voga360.com/";
	Logger log = Logger.getLogger(WallpapersTask.class);

	private ImageInfoService imageInfoService;

	private CategoryService categoryService;
	
	private MarketService marketService;
	
	public void setImageInfoService(ImageInfoService imageInfoService) {
		this.imageInfoService = imageInfoService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public void setMarketService(MarketService marketService) {
		this.marketService = marketService;
	}

	
	private Elements eles;
	private String wallPaperType;
	private String[] data;
	private String filePath;
	private String loginUser;
	public WallpapersTask() {}

	public WallpapersTask(Elements eles,String wallPaperType,String[]  data,String filePath,String loginUser) {
		this.eles =eles;
		this.wallPaperType =wallPaperType;
		this.data =data;
		this.filePath = filePath;
		this.loginUser = loginUser;
	}
	@Override
	public void run() {
		if(eles==null){
			 for(int i=0;i<data.length;i++){
			       if(data[i]!=null&&!data[i].equals("")){
							 JSONObject json = JSONObject.fromObject(data[i]);  
							 if(json.get("images")!=null&&!json.get("images").equals("")){
								 JSONArray images = json.getJSONArray("images");						 
								 if(images!=null){
									   for( Iterator j = images.iterator(); j.hasNext(); ){ 
										    batchAddFromURL(j.next(), filePath,loginUser);
									   }
								 }
							 }
			       }
			 }
		}else{
			for(Element e0:eles){
				batchAddFromEle(wallPaperType,e0, filePath,loginUser);

				String imageitem =e0.attr("imgname");
				String url = e0.attr("uploadsrc");
				String bigLogo = e0.attr("href");
				

			}
		}

		 ImageInfoController.isImageBatchAddThreadRun=false;
	}
	public void batchAddFromEle(String wallPaperType,Element ele, String filePath,
			String loginUser) {
		// 拼凑Pic 下载路径 http://server.voga360.com/wallpaper/download.htm?
		// url=http://upload.voga360.com/'+path+'&id='+id+'&t='wallpaperType.type+'
		// 使用注册器对指定DynaBean进行对象变换
		ImageInfo image = new ImageInfo();
		String path =ele.attr("uploadsrc");
		// 小图
		String smallPath = ele.attr("href");
		// 中图
		String largePath =ele.attr("href");
		String picName =ele.attr("imgname");
		// 查询picName 是否已经存在，如果存在则不插入
		List<ImageInfo> imageList = imageInfoService.selectByName(picName);
		if (imageList==null||imageList.size() == 0) {

			// 根据name 查询category
			// 如果category 没有,创建新的category 并保存数据
			Category category = null;
			try {
				category = addCategory1(wallPaperType);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				log.error("ImageInfoController batchAddFromURL", e2);
				
			}
//			System.out.println(id + " " + path + " " + type1);
			int categoryId = 0;
			if (category != null) {
				image.setCategory(category);
				categoryId = category.getId();
			} else {
				categoryId = 1;
			}
			// 设置name pinyin description brief free stars state
			image.setName(picName);
			image.setPinyin(PinyinToolkit.cn2Spell(picName));
			image.setFree(0);
			image.setRaveId(1);
			image.setStars(5);
			image.setState(true);
			image.setOperator("batchmag");
			try {
				String format= "jpg";
				String str = MD5.getMd5Value(System.currentTimeMillis() + picName);
				// 大图 image/meinukanfengjing/image/*.pic
				String fileName = filePath + File.separator + str
						+ Constant.LOCAL_IMAGE_PATH + str + "." + format;
				// 中图 image/meinukanfengjing/biglogo/*.pic
				String biglogoName = filePath + File.separator + str
						+ Constant.LOCAL_PIC_BIGLOGO + str + "." + format;
				// 小图 image/meinukanfengjing/logo/*.pic
				String logoName = filePath + File.separator + str
						+ Constant.LOCAL_APK_LOG + str + "." + format;
				CrawlerFileUtils.saveToFile(path, fileName);
				// 下载中图
				CrawlerFileUtils.saveToFile(largePath,
						biglogoName);
				// 下载小图
				CrawlerFileUtils.saveToFile(smallPath,
						logoName);
				File fimage = new File(fileName);
				File fbiglogo1 = new File(biglogoName);
				File flogo1 = new File(logoName);
				List<String> fileUrlList = new ArrayList<String>();//s3 list

				// 如果图片存在
				if ((fimage.exists() && fimage.length() != 0)
						&& (fbiglogo1.exists() || fbiglogo1.length() != 0)
						&& (flogo1.exists() && flogo1.length() != 0)) {
					BufferedImage bufferImage = null;
					try {
						bufferImage = ImageIO.read(fimage);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						log.error("ImageInfoController batchAddFromURL", e1);
						fimage.delete();
					}
					image.setWidth(bufferImage.getWidth());
					image.setLength(bufferImage.getHeight());
					image.setFileSize((int) fimage.length());
					image.setUrl(FileAddresUtil.getFilePath(fimage));
					image.setBiglogo(FileAddresUtil.getFilePath(fbiglogo1));
					image.setLogo(FileAddresUtil.getFilePath(flogo1));
					fileUrlList.add(image.getLogo());
					fileUrlList.add(image.getBiglogo());
					fileUrlList.add(image.getUrl());
					// 设置长宽、宽度、文件大小、路径	
					List<ImageInfo> imageList1 = imageInfoService.selectByName(picName);
					if(imageList1==null||imageList1.size()==0){
						imageInfoService.addImageInfo(image, categoryId);					
					}
				} 
				//s3upload
				S3Util.uploadS3(fileUrlList, false);	

			} catch (Exception e) {
				log.error("ImageInfoController batchAddFromURL", e);
				try {
					FileUtil.deleteFile(image.getUrl());
					FileUtil.deleteFile(image.getBiglogo());
					FileUtil.deleteFile(image.getLogo());
				} catch (IOException e1) {
					log.error("ImageInfoController batchAddFromURL", e1);
				}				
			}
		}
	}
	public void batchAddFromURL(Object imageitem, String filePath,
			String loginUser) {
		// 拼凑Pic 下载路径 http://server.voga360.com/wallpaper/download.htm?
		// url=http://upload.voga360.com/'+path+'&id='+id+'&t='wallpaperType.type+'
		// 使用注册器对指定DynaBean进行对象变换
		ImageInfo image = new ImageInfo();
		JSONObject imgItem = JSONObject.fromObject(imageitem);
		int id = (int) imgItem.get("id");
		String path = (String) imgItem.get("path");
		// 小图
		String smallPath = (String) imgItem.get("smallPath");
		// 中图
		String largePath = (String) imgItem.get("largePath");
		String picName = ((String) imgItem.get("name")).replaceAll(
				"[\\/:?*<>|.#'\"$@%^&*,;:]", "");
		// 查询picName 是否已经存在，如果存在则不插入
		List<ImageInfo> imageList = imageInfoService.selectByName(picName);
		if (imageList==null||imageList.size() == 0) {
			String format = (String) imgItem.get("format");
			String description = (String) imgItem.get("description");
			JSONObject wallpaperType = (JSONObject) imgItem
					.get("wallpaperType");
			// 根据name 查询category
			// 如果category 没有,创建新的category 并保存数据
			Category category = null;
			try {
				category = addCategory(wallpaperType);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				log.error("ImageInfoController batchAddFromURL", e2);
				
			}
			int type1 = (int) wallpaperType.get("type");
//			System.out.println(id + " " + path + " " + type1);
			int categoryId = 0;
			if (category != null) {
				image.setCategory(category);
				categoryId = category.getId();
			} else {
				categoryId = 1;
			}
			// 设置name pinyin description brief free stars state
			image.setName(picName);
			image.setPinyin(PinyinToolkit.cn2Spell(picName));
			image.setDescription(description);
			image.setBrief(description);
			image.setFree(0);
			image.setRaveId(1);
			image.setStars(5);
			image.setState(true);
			image.setOperator("batchmag");
			try {
				StringBuilder sb2 = new StringBuilder();
				sb2.append(picDownloadUrl);
				sb2.append(path);
				sb2.append("&id=").append(id);
				sb2.append("&t=").append(type1);
				String str = MD5.getMd5Value(System.currentTimeMillis() + picName);
				// 大图 image/meinukanfengjing/image/*.pic
				String fileName = filePath + File.separator + str
						+ Constant.LOCAL_IMAGE_PATH + str + "." + format;
				// 中图 image/meinukanfengjing/biglogo/*.pic
				String biglogoName = filePath + File.separator + str
						+ Constant.LOCAL_PIC_BIGLOGO + str + "." + format;
				// 小图 image/meinukanfengjing/logo/*.pic
				String logoName = filePath + File.separator + str
						+ Constant.LOCAL_APK_LOG + str + "." + format;
				CrawlerFileUtils.saveToFile(sb2.toString(), fileName);
				// 下载中图
				CrawlerFileUtils.saveToFile(picDownloadUrl + largePath,
						biglogoName);
				// 下载小图
				CrawlerFileUtils.saveToFile(picDownloadUrl + smallPath,
						logoName);
				File fimage = new File(fileName);
				File fbiglogo1 = new File(biglogoName);
				File flogo1 = new File(logoName);
				List<String> fileUrlList = new ArrayList<String>();//s3 list

				// 如果图片存在
				if ((fimage.exists() && fimage.length() != 0)
						&& (fbiglogo1.exists() || fbiglogo1.length() != 0)
						&& (flogo1.exists() && flogo1.length() != 0)) {
					BufferedImage bufferImage = null;
					try {
						bufferImage = ImageIO.read(fimage);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						log.error("ImageInfoController batchAddFromURL", e1);
						fimage.delete();
					}
					image.setWidth(bufferImage.getWidth());
					image.setLength(bufferImage.getHeight());
					image.setFileSize((int) fimage.length());
					image.setUrl(FileAddresUtil.getFilePath(fimage));
					image.setBiglogo(FileAddresUtil.getFilePath(fbiglogo1));
					image.setLogo(FileAddresUtil.getFilePath(flogo1));
					fileUrlList.add(image.getLogo());
					fileUrlList.add(image.getBiglogo());
					fileUrlList.add(image.getUrl());
					// 设置长宽、宽度、文件大小、路径	
					List<ImageInfo> imageList1 = imageInfoService.selectByName(picName);
					if(imageList1==null||imageList1.size()==0){
						imageInfoService.addImageInfo(image, categoryId);					
					}
				} 
				//s3upload
				S3Util.uploadS3(fileUrlList, false);	

			} catch (Exception e) {
				log.error("ImageInfoController batchAddFromURL", e);
				try {
					FileUtil.deleteFile(image.getUrl());
					FileUtil.deleteFile(image.getBiglogo());
					FileUtil.deleteFile(image.getLogo());
				} catch (IOException e1) {
					log.error("ImageInfoController batchAddFromURL", e1);
				}				
			}
		}
	}
	/**
	 * 解析wallpaperType 获取category 
	 * 或者创建category 并根据name查询category返回category
	 * @param wallpaperType "wallpaperType":
	 * {"id":26,
	 * "name":"Sexy",
	 * "display":0,
	 * "type":27,
	 * "picture_path":"mu/wallpaper/2014/3/24/08120261/08f7a15080fe4cc2a95d7ae43ba2ed96.png"},
	 * @return
	 */
	public Category addCategory(JSONObject wallpaperType) throws Exception{
		String firstCategory = "Wallpaper";
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
			category.setCategoryCn("墙纸");
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

		String name = (String) wallpaperType.get("name");
		//category logo 下载路径
		
		String picture_path = picDownloadUrl+ (String)wallpaperType.get("picture_path");
		List<Category> list = null;
		if(name!=null&&!name.equals("")){
			Category category = new Category();
			category.setName(name);
			category.setFatherId(fatherId);
			list =	categoryService.getCategorysByName(category);	
			//如果二级菜单不存在 创建二级菜单
			if(list==null||list.size()==0){
				String str = FileAddresUtil.getTitlePath(fatherId, 0, Constant.CATEGORY_ADR);
				// 获取后缀,检测是否非法文件
				String suffix = FileAddresUtil.getSuffix(picture_path);
				if (suffix.equals(Constant.IMG_ADR)) {
					File pic1 = new File(Constant.LOCAL_FILE_PATH + str + "big", RandNum.randomFileName(name+"."+FileUtil.getFileSuffix(picture_path)));
					try {
						CrawlerFileUtils.saveToFile(picture_path,pic1.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						return null;
					} 
					category1.setBigicon(FileAddresUtil.getFilePath(pic1));
					S3Util.uploadS3(category1.getBigicon(), false);
					category1.setFatherId(fatherId);
					category1.setName(name.trim());
					category1.setMarketInfo(marketService.getMarket(1));
					category1.setState(true);
					category1.setSort(0);
					categoryService.addCategory(category1);
					
					Category category2 = new Category();
					category2.setName(category1.getName());
					category2.setFatherId(category1.getFatherId());
					List<Category> list1= categoryService.getCategorysByName(category2);
					return list1.get(0);					
				} 	
			}else{
				return list.get(0);

			}
		}
		return null;
	}
	public Category addCategory1(String wallpaperType) throws Exception{
		String firstCategory = "Wallpaper";
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
			category.setCategoryCn("墙纸");
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
		List<Category> list = null;
		if(wallpaperType!=null&&!wallpaperType.equals("")){
			Category category = new Category();
			category.setName(wallpaperType);
			category.setFatherId(fatherId);
			list =	categoryService.getCategorysByName(category);	
			//如果二级菜单不存在 创建二级菜单
			if(list==null||list.size()==0){
					category1.setFatherId(fatherId);
					category1.setName(wallpaperType);
					category1.setMarketInfo(marketService.getMarket(1));
					category1.setState(true);
					category1.setSort(0);
					categoryService.addCategory(category1);					
					Category category2 = new Category();
					category2.setName(category1.getName());
					category2.setFatherId(category1.getFatherId());
					List<Category> list1= categoryService.getCategorysByName(category2);
					return list1.get(0);					
					
			}else{
				return list.get(0);

			}
		}
		return null;
	}
	
}
