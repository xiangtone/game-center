/**   
* @Title: PinyinComparator.java
* @Package com.x.util
* @Description: TODO 

* @date 2014-2-10 上午10:14:18
* @version V1.0   
*/

package com.x.business.localapp.sort;

import java.util.Comparator;

import com.x.publics.model.InstallAppBean;

/**
* @ClassName: PinyinComparator
* @Description: TODO 

* @date 2014-2-10 上午10:14:18
* 
*/

public class PinyinComparator3 implements Comparator<InstallAppBean> {

	@Override
	public int compare(InstallAppBean o1, InstallAppBean o2) {
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
