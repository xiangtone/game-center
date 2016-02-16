package com.mas.rave.vo;
/**
 * zAPP排行榜规则参数
 * @author jieding
 *
 */
public class TopVO {
	/**APP在APPAnnie上的安装数量*/
	private Double annieInstallTotal;
	/**平均安装，总安装除以天数*/
	private Double annieInstallAverage;
	/**APP在APPAnnie上评分*/
	private Double annieRatings;
	/**APP在APPAnnie上最早版本上线时间，
	 * 如果是我们自己运营的使用我们录入时间。
	 * (版本上线日期到至今的时间)*/
	private Double initialTime;
	/**APP在APPAnnie上的排行名次*/
	private double annieRank;
	/**APP包的大小*/
	private Double size;

	//extend
	private double RealDownload;
	private Double DownloadAverage;
	private double Pageviews;
	private double Ratings;
	private int categoryId;
	
	private Double score;

	public Double getAnnieInstallTotal() {
		return annieInstallTotal;
	}

	public void setAnnieInstallTotal(Double annieInstallTotal) {
		this.annieInstallTotal = annieInstallTotal;
	}

	public Double getAnnieInstallAverage() {
		return annieInstallAverage;
	}

	public void setAnnieInstallAverage(Double annieInstallAverage) {
		this.annieInstallAverage = annieInstallAverage;
	}

	public Double getAnnieRatings() {
		return annieRatings;
	}

	public void setAnnieRatings(Double annieRatings) {
		this.annieRatings = annieRatings;
	}

	public Double getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(Double initialTime) {
		this.initialTime = initialTime;
	}

	public double getAnnieRank() {
		return annieRank;
	}

	public void setAnnieRank(double annieRank) {
		this.annieRank = annieRank;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public double getRealDownload() {
		return RealDownload;
	}

	public void setRealDownload(double realDownload) {
		RealDownload = realDownload;
	}

	public Double getDownloadAverage() {
		return DownloadAverage;
	}

	public void setDownloadAverage(Double downloadAverage) {
		DownloadAverage = downloadAverage;
	}

	public double getPageviews() {
		return Pageviews;
	}

	public void setPageviews(double pageviews) {
		Pageviews = pageviews;
	}

	public double getRatings() {
		return Ratings;
	}

	public void setRatings(double ratings) {
		Ratings = ratings;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	
	
	
}
