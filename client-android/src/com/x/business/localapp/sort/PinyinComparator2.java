/**   
* @Title: PinyinComparator.java
* @Package com.x.util
* @Description: TODO 

* @date 2014-2-10 上午10:14:18
* @version V1.0   
*/

package com.x.business.localapp.sort;

import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MyAppsBean;

import java.util.Comparator;

/**
* @ClassName: PinyinComparator
* @Description: TODO 

* @date 2014-2-10 上午10:14:18
* 
*/

public class PinyinComparator2 implements Comparator<MyAppsBean> {

	@Override
	public int compare(MyAppsBean o1, MyAppsBean o2) {
		try {
			if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
				return -1;
			} else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
				return 1;
			} else {
				return o1.getSortLetters().compareTo(o2.getSortLetters());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

}
