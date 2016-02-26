package com.x.publics.model;


public class AppLockerBean implements Comparable<AppLockerBean>{
	
	private int id;
	private String appName;
	private String packageName;
	private int lockerSortType ;
	private String lockerSortTypeName ;
	private String lockerDesc ;
	private  boolean isLocked=false ;
	public boolean isGroupApp ;
	public String activityName ;
	
	public class lockerType {
		public static final int  Recommend = 2;
		public static final int General= 1;
		
	}

	public class lockerTypeName {
		public static final String RECOMMEND = "Recommend";
		public static final String GENERAL = "General";
	}
	
	/** 
	 * @return id 
	 */
	
	public int getId() {
		return id;
	}



	/** 
	 * @param id 要设置的 id 
	 */
	
	public void setId(int id) {
		this.id = id;
	}



	/** 
	 * @return appName 
	 */
	
	public String getAppName() {
		return appName;
	}



	/** 
	 * @param appName 要设置的 appName 
	 */
	
	public void setAppName(String appName) {
		this.appName = appName;
	}



	/** 
	 * @return packageName 
	 */
	
	public String getPackageName() {
		return packageName;
	}



	/** 
	 * @param packageName 要设置的 packageName 
	 */
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}



	/** 
	 * @return lockerSortType 
	 */
	
	public int getLockerSortType() {
		return lockerSortType;
	}



	/** 
	 * @param lockerSortType 要设置的 lockerSortType 
	 */
	
	public void setLockerSortType(int lockerSortType) {
		this.lockerSortType = lockerSortType;
	}



	/** 
	 * @return lockerSortTypeName 
	 */
	
	public String getLockerSortTypeName() {
		return lockerSortTypeName;
	}



	/** 
	 * @param lockerSortTypeName 要设置的 lockerSortTypeName 
	 */
	
	public void setLockerSortTypeName(String lockerSortTypeName) {
		this.lockerSortTypeName = lockerSortTypeName;
	}



	/** 
	 * @return lockerDesc 
	 */
	
	public String getLockerDesc() {
		return lockerDesc;
	}



	/** 
	 * @param lockerDesc 要设置的 lockerDesc 
	 */
	
	public void setLockerDesc(String lockerDesc) {
		this.lockerDesc = lockerDesc;
	}


	@Override
	public int compareTo(AppLockerBean another) {
		if(another.getLockerSortType() > this.getLockerSortType())
			return 1;
		else if(another.getLockerSortType() == this.getLockerSortType())
			return 0;
		return -1;
	}



	public boolean isLocked() {
		return isLocked;
	}



	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}



	/** 
	 * @return isGroupApp 
	 */
	
	public boolean isGroupApp() {
		return isGroupApp;
	}



	/** 
	 * @param isGroupApp 要设置的 isGroupApp 
	 */
	
	public void setGroupApp(boolean isGroupApp) {
		this.isGroupApp = isGroupApp;
	}



	/** 
	 * @return activityName 
	 */
	
	public String getActivityName() {
		return activityName;
	}



	/** 
	 * @param activityName 要设置的 activityName 
	 */
	
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	
}
