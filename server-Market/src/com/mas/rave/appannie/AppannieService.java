package com.mas.rave.appannie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mas.rave.util.ConstantScore;
import com.mas.rave.vo.ScoreArea;
import com.mas.rave.vo.TopVO;

public class AppannieService {
	public static final List<ScoreArea> AnnieInstallTotal=new ArrayList<ScoreArea>(); //AnnieInstall-total
	public static final List<ScoreArea> AnnieInstallAverage=new ArrayList<ScoreArea>();//AnnieInstall-average//
	public static final List<ScoreArea> AnnieRatings=new ArrayList<ScoreArea>();//AnnieRatings
	public static final List<ScoreArea> InitialTime=new ArrayList<ScoreArea>();//InitialTime
	public static final List<ScoreArea> AnnieRank=new ArrayList<ScoreArea>();//AnnieRank
	public static final Map<Integer,Integer> Category=new HashMap<Integer,Integer>();//Category
	public static final List<ScoreArea> Size=new ArrayList<ScoreArea>();//Size	
	public static final List<ScoreArea> RealDownload=new ArrayList<ScoreArea>();//RealDownload	
	public static final List<ScoreArea> DownloadAverage=new ArrayList<ScoreArea>();//Download-average
	public static final List<ScoreArea> Pageviews=new ArrayList<ScoreArea>();//Pageviews
	public static final List<ScoreArea> Ratings=new ArrayList<ScoreArea>();//Ratings	
	
	
	static{
		init();
	}
	
	private static void init(){
		System.out.println("init...");
//		AnnieInstallTotal.addAll(toList(ScoreManager.getConfigData("AnnieInstallTotal")));
//		AnnieInstallAverage.addAll(toList(ScoreManager.getConfigData("AnnieInstallAverage")));
//		AnnieRatings.addAll(toList(ScoreManager.getConfigData("AnnieRatings")));
//		InitialTime.addAll(toList(ScoreManager.getConfigData("InitialTime")));
//		AnnieRank.addAll(toList(ScoreManager.getConfigData("AnnieRank")));
//		Category.putAll(initCategory());
//		Size.addAll(toList(ScoreManager.getConfigData("Size")));
//		RealDownload.addAll(toList(ScoreManager.getConfigData("RealDownload")));
//		DownloadAverage.addAll(toList(ScoreManager.getConfigData("DownloadAverage")));
//		Pageviews.addAll(toList(ScoreManager.getConfigData("Pageviews")));
//		Ratings.addAll(toList(ScoreManager.getConfigData("Ratings")));
		//-------------------------
		
		
		AnnieInstallTotal.addAll(toList(ConstantScore.ANNIEINSTALLTOTAL));
		AnnieInstallAverage.addAll(toList(ConstantScore.ANNIEINSTALLAVERAGE));
		AnnieRatings.addAll(toList(ConstantScore.ANNIERATINGS));
		InitialTime.addAll(toList(ConstantScore.INITIALTIME));
		AnnieRank.addAll(toList(ConstantScore.ANNIERANK));
		Category.putAll(initCategory());
		Size.addAll(toList(ConstantScore.SIZE));
		RealDownload.addAll(toList(ConstantScore.REALDOWNLOAD));
		DownloadAverage.addAll(toList(ConstantScore.DOWNLOADAVERAGE));
		Pageviews.addAll(toList(ConstantScore.PAGEVIEWS));
		Ratings.addAll(toList(ConstantScore.RATINGS));
		
	}
	private static Map<Integer,Integer> initCategory(){
		Map<Integer,Integer> map=new HashMap<Integer,Integer>();
		String str;
		for(int i=1;i<=10;i++){
			//str = ScoreManager.getConfigData("Category"+i);
			str = getCategoryValueByNum(i);
			if(StringUtils.isNotEmpty(str)){
				String[] ss = str.split(",");
				for(String s:ss){
					if(StringUtils.isNotEmpty(s)){
						try{
							map.put(Integer.parseInt(s), i);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
		return map;
	}
	private static String getCategoryValueByNum(int num){
		switch(num){
		case 1:return ConstantScore.CATEGORY1;
		case 2:return ConstantScore.CATEGORY2;
		case 3:return ConstantScore.CATEGORY3;
		case 4:return ConstantScore.CATEGORY4;
		case 5:return ConstantScore.CATEGORY5;
		case 6:return ConstantScore.CATEGORY6;
		case 7:return ConstantScore.CATEGORY7;
		case 8:return ConstantScore.CATEGORY8;
		case 9:return ConstantScore.CATEGORY9;
		case 10:return ConstantScore.CATEGORY10;
		}
		return "";
	}
	private static List<ScoreArea> toList(String s){
		List<ScoreArea> list= new ArrayList<ScoreArea>();
		if(StringUtils.isEmpty(s)){
			return list; 
		}
		ScoreArea o;
		String[] ss = s.split("\\|");
		//System.out.println("ss.length="+ss.length);
		for(String s1:ss){
			if(StringUtils.isNotEmpty(s1)){
				String[] sss = s1.split(",");
				o = toScoreArea(sss);
				if(o!=null){
					list.add(o);
				}
			}
		}
		return list;
	}
	private static ScoreArea toScoreArea(String[] sss){
		ScoreArea o = null;
		if(sss.length==4){
			o = new ScoreArea();
			try{
				o.setLessScore(Double.parseDouble(sss[0]));
				o.setMaxScore(Double.parseDouble(sss[1]));
				o.setLessArea(Double.parseDouble(sss[2]));
				o.setMaxArea(Double.parseDouble(sss[3]));
				//System.out.println(sss[0]+","+sss[1]+","+sss[2]+","+sss[3]);
			}catch(Exception e){
				System.out.println("$$$toScoreArea:"+sss[0]+","+sss[1]+","+sss[2]+","+sss[3]);
				e.printStackTrace();
			}
		}else{
			System.out.println("$$$toScoreArea:length="+sss.length);
		}
		return o;
	}
	private void setScore(double[] weight,TopVO vo){
		double sum1=0,sum2=0;
		int item;
		for(int i=0;i<weight.length;i++){
			item = i + 1;//分类
			if(weight[i]!=0){
				sum1 +=weight[i];
				if(item==6){//Category
					Integer score = Category.get(vo.getCategoryId());
					if(score!=null){
						sum2 +=(weight[i] * score);
						//System.out.println("^^^^^^^^^score="+score);
					}
				}else{
					sum2 +=(weight[i] * getScoreByItem(item, getItemVal(vo, item)));
				}
			}
		}
		if(sum1!=0){
			vo.setScore(sum2/sum1);
		}else{
			vo.setScore(0.0);
		}
	}
	private double getScoreByItem(int item,double itemVal){
		List<ScoreArea> scoreAreaList = getScoreAreaListByItem(item);
		if(scoreAreaList==null){
			return 0;
		}
		return getScoreByList(scoreAreaList,itemVal);
	}
	private static double getScoreByList(List<ScoreArea> list,double val){
		for(ScoreArea scoreArea:list){
			if(val>=scoreArea.getLessArea() && val<=scoreArea.getMaxArea()){
				return ((val-scoreArea.getLessArea())/(scoreArea.getMaxArea()-scoreArea.getLessArea())) * (scoreArea.getMaxScore()-scoreArea.getLessScore()) + scoreArea.getLessScore();
			}	
		}
		return 0;
	}
	private List<ScoreArea> getScoreAreaListByItem(int item){
		switch(item){
		case 1:return AnnieInstallTotal;
		case 2:return AnnieInstallAverage;
		case 3:return AnnieRatings;
		case 4:return InitialTime;
		case 5:return AnnieRank;
		case 6:break;//Category
		case 7:return Size;
		case 8:return RealDownload;
		case 9:return DownloadAverage;
		case 10:return Pageviews;
		case 11:return Ratings;
		}
		return null;
	}
	private double getItemVal(TopVO vo,int item){
		switch(item){
		case 1:return vo.getAnnieInstallTotal();
		case 2:return vo.getAnnieInstallAverage();
		case 3:return vo.getAnnieRatings();
		case 4:return vo.getInitialTime();
		case 5:return vo.getAnnieRank();
		case 6:break;//Category
		case 7:return vo.getSize();
		case 8:return vo.getRealDownload();
		case 9:return vo.getDownloadAverage();
		case 10:return vo.getPageviews();
		case 11:return vo.getRatings();
		}
		return 0;
	}
	private double[] getWeightStrByTop(int listType){
		String str=null;
		switch(listType){
//		case 1:str=ScoreManager.getConfigData("HomeRecommand");break;
//		case 2:str=ScoreManager.getConfigData("HomeNew");break;
//		case 3:str=ScoreManager.getConfigData("HomeTop");break;
//		case 4:str=ScoreManager.getConfigData("HomePopular");break;
//		case 5:str=ScoreManager.getConfigData("AppHot");break;
//		case 6:str=ScoreManager.getConfigData("AppTop");break;
//		case 7:str=ScoreManager.getConfigData("AppNew");break;
//		case 8:str=ScoreManager.getConfigData("GameHot");break;
//		case 9:str=ScoreManager.getConfigData("GameTop");break;
//		case 10:str=ScoreManager.getConfigData("GameNew");break;
		case 1:str=ConstantScore.HOMERECOMMAND;break;
		case 2:str=ConstantScore.HOMENEW;break;
		case 3:str=ConstantScore.HOMETOP;break;
		case 4:str=ConstantScore.HOMEPOPULAR;break;
		case 5:str=ConstantScore.APPHOT;break;
		case 6:str=ConstantScore.APPTOP;break;
		case 7:str=ConstantScore.APPNEW;break;
		case 8:str=ConstantScore.GAMEHOT;break;
		case 9:str=ConstantScore.GAMETOP;break;
		case 10:str=ConstantScore.GAMENEW;break;
		}
		//System.out.println("str="+str);
		final int SIZE=11;//11个
		double[] weight = new double[SIZE]; 
		if(StringUtils.isNotEmpty(str)){
			String ss[] = str.split(",");
			if(ss.length==SIZE){
				for(int i=0;i<SIZE;i++){
					try{
						weight[i]=Double.parseDouble(ss[i]);
					}catch(Exception e){}
				}
			}
		}
		return weight;
	}
	public TopVO setScore(TopVO vo,int listType){
		double[] weight = getWeightStrByTop(listType);
		setScore(weight, vo); //算分
		return vo;
	}
	/*public List<TopVO> toScoreList(List<TopVO> list,int listType){
		double[] weight = getWeightStrByTop(listType);
		for(TopVO vo:list){
			setScore(weight, vo); //算分
		}
		//排序，从高到低
		Collections.sort(list, new Comparator<TopVO>(){
			@Override
			public int compare(TopVO o1, TopVO o2) {
				if(o2.getScore()>o1.getScore()){
					return 1;
				}else if(o2.getScore()<o1.getScore()){
					return -1;
				}
				return 0;
			}
		});
		return list;
	}*/
//	public static void main(String[] args) {
//		AppannieService appScore = new AppannieService();
//		TopVO vo = new TopVO();
//		vo.setAnnieInstallAverage(50001);
//		vo.setAnnieInstallTotal(1000001);
//		vo.setAnnieRank(5);
//		vo.setAnnieRatings(5);
//		vo.setDownloadAverage(50001);
//		vo.setInitialTime(10);
//		vo.setRealDownload(1000000);
//		vo.setSize(50);
//		vo.setCategoryId(60);
//		
//		appScore.setScore(vo, 2);
//		
//	}
}
